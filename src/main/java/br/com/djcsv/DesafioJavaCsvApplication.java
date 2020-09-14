package br.com.djcsv;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import br.com.djcsv.util.CSVUtil;

@SpringBootApplication
public class DesafioJavaCsvApplication {
	
	static Logger logger = LoggerFactory.getLogger(DesafioJavaCsvApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DesafioJavaCsvApplication.class, args);
		
		try {
			if(args != null && args.length > 0){
				String diretorio = System.getProperty("user.dir");
				Resource resource = new PathResource(diretorio+File.separator+args[0]);
				
				if(CSVUtil.temExtCSV(resource)){
					CSVUtil.processar(resource);				
				} else {
					logger.info("\n ###### Nao foi possivel processar arquivo. Arquivo com extensao nao suportada! ######");
				}
			} else {
				logger.info("\n ###### Nao foi possivel processar arquivo. Arquivo nao informado no argumento! ######");
			}
			
		} catch (Exception e) {
			logger.info("\n ###### Ocorreu um erro na aplicacao: " + e.getMessage() + " ######");
		}
		
	}

}
