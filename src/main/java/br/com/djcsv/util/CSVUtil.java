package br.com.djcsv.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import br.com.djcsv.dto.ContaDto;
import br.com.djcsv.service.ReceitaService;

public class CSVUtil {
	
	static Logger logger = LoggerFactory.getLogger(CSVUtil.class);
	
	public static String TYPE = "text/csv";
	public static String[] HEADERs = { "agencia", "conta", "saldo", "status", "processado" };
	public static DecimalFormat df = new DecimalFormat("0.00");
	
	public static boolean temExtCSV(Resource resource) throws IOException {
		
		Tika tika = new Tika();
	    String mimeType = tika.detect(resource.getFile());

	    if (!TYPE.equals(mimeType)) {
	      return false;
	    }

	    return true;
    }
	
	public static void processar(Resource resource) throws Exception{
		
		logger.info("\n ###### Iniciando processamento do arquivo: " + resource.getFilename() + " ######");
		
		List<ContaDto> contas = new ArrayList<ContaDto>();
		
		contas = lerCSV(resource, contas);
		contas = servicoBancoCentral(contas);
        
        escreverCSV(resource, contas);
        
        logger.info("\n ###### Finalizando processamento do arquivo: " + resource.getFilename() + " ######");
        
	}

	public static List<ContaDto> lerCSV(Resource resource, List<ContaDto> contas) {
		
		try(InputStream inputStream = resource.getInputStream();
		    BufferedReader csvReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            CSVParser csvParser = new CSVParser(csvReader,
        									    CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withDelimiter(';'));){
			
			
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			for (CSVRecord csvRecord : csvRecords) {
				ContaDto contaDto = new ContaDto(csvRecord.get("agencia"),
												 removerCaractere(csvRecord.get("conta"),"-"),
												 df.parse((csvRecord.get("saldo"))).doubleValue(),
												 csvRecord.get("status")
												);
				
				contas.add(contaDto);
			}
			
		} catch (Exception e) {
		      throw new RuntimeException("Ocorreu erro na leitura do arquivo: " + e.getMessage());
	    } 
		
		return contas;
	}
	
	public static List<ContaDto> servicoBancoCentral(List<ContaDto> contas) {
		ReceitaService receitaService = new ReceitaService();
		
		contas.parallelStream().forEach(c -> {
			boolean processado = false;
			try {
				processado = receitaService.atualizarConta(c.getAgencia(), c.getConta(), c.getSaldo(), c.getStatus());
			} catch (Exception e) {
				logger.info("\n ###### Ocorreu erro no processamento do registro(agencia/conta): " + c.getAgencia() + "/" + c.getConta() + " ######");
			} finally {
				if(processado)
					c.setProcessado("S");
				else 
					c.setProcessado("N");
			}
		});
		
		return contas;
	}
	
	public static void escreverCSV(Resource resource, List<ContaDto> contas) {
		FileWriter fw = null;
		String csvSaida = null;
		
		try {
			String diretorio = System.getProperty("user.dir");
			Files.createDirectories(Paths.get(diretorio + "/processados"));
			csvSaida = diretorio+File.separator+"processados"+File.separator+resource.getFilename();
			
			fw = new FileWriter(csvSaida);
		} catch (IOException e) {
			throw new RuntimeException("Ocorreu erro na escrita do arquivo: " + e.getMessage());
		}
		
        try(CSVPrinter csvPrinter = new CSVPrinter(fw, CSVFormat.DEFAULT.withHeader(HEADERs).withDelimiter(';'))){
        	for (ContaDto contaDto : contas) {
        		List<String> dados = Arrays.asList(contaDto.getAgencia(),
        										   adicionarCaractereConta(contaDto.getConta(), "-"),
        										   df.format(contaDto.getSaldo()).replace('.', ','),
        										   contaDto.getStatus(),
        										   contaDto.getProcessado());
        		
        		csvPrinter.printRecord(dados);
			}        	
        } catch (IOException e) {
        	throw new RuntimeException("Ocorreu erro na escrita do arquivo: " + e.getMessage());
        }
        
        logger.info("\n ###### Arquivo processado. Verificar em: " + csvSaida + " ######");
	}

	public static String removerCaractere(String string, String c) {
		StringBuilder stringBuilder = new StringBuilder(string);
		int i = stringBuilder.indexOf(c);
		return stringBuilder.deleteCharAt(i).toString();
	}
	
	public static String adicionarCaractereConta(String string, String c) {
		StringBuilder stringBuilder = new StringBuilder(string);
		int length = stringBuilder.length();
		return stringBuilder.insert(length-1, c).toString();
	}
}
