package br.com.djcsv.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import br.com.djcsv.dto.ContaDto;

public class CSVUtilTest {
	
	public static final List<ContaDto> CONTA_DTO_LIST = Collections.unmodifiableList(new LinkedList<ContaDto>() {
		private static final long serialVersionUID = 3852411418018385216L;

		{
			add(new ContaDto("0101", "122256", 100.0, "A", "S"));
			add(new ContaDto("0101", "122268", 3200.50, "A", "S"));
        }
    });
	
	@Test
	public void testarTemExtCSV() throws IOException{
		
		Resource resourceCSV = new PathResource("src"+File.separator+"test"+File.separator+"java"+File.separator+"resource"+File.separator+"contas.csv");
		Resource resourceTXT = new PathResource("src"+File.separator+"test"+File.separator+"java"+File.separator+"resource"+File.separator+"contas.txt");
		
		assertTrue(CSVUtil.temExtCSV(resourceCSV));
		assertFalse(CSVUtil.temExtCSV(resourceTXT));
		
	}
	
	@Test
	public void testarLerCSV() throws IOException{
		Resource resourceCSV = new PathResource("src"+File.separator+"test"+File.separator+"java"+File.separator+"resource"+File.separator+"contas.csv");
		List<ContaDto> contas = new ArrayList<ContaDto>();
		
		contas = CSVUtil.lerCSV(resourceCSV, contas);
		
		assertEquals(2, contas.size());
		
		for (int i = 0; i < contas.size(); i++) {
			assertEquals(CONTA_DTO_LIST.get(i).getAgencia(), contas.get(i).getAgencia());
			assertEquals(CONTA_DTO_LIST.get(i).getConta(), contas.get(i).getConta());
			assertEquals(CONTA_DTO_LIST.get(i).getSaldo(), contas.get(i).getSaldo(), 0.0001);
			assertEquals(CONTA_DTO_LIST.get(i).getStatus(), contas.get(i).getStatus());
		}
	}
	
	@Test
	public void testarServicoBancoCentral(){
		List<ContaDto> listContaDto = CSVUtil.servicoBancoCentral(CONTA_DTO_LIST);
		
		assertEquals(2, listContaDto.size());
		
		listContaDto.forEach(c -> assertTrue(c.getProcessado() != null && !"".equals(c.getProcessado())));
	}
	
	@Test
	public void testarEscreverCSV(){
		Resource resourceCSV = new PathResource("src"+File.separator+"test"+File.separator+"java"+File.separator+"resource"+File.separator+"contas.csv");
		
		CSVUtil.escreverCSV(resourceCSV, CONTA_DTO_LIST);
		
		Resource resourceCSVProcessado = new PathResource("processados"+File.separator+"contas.csv");
		assertEquals("contas.csv", resourceCSVProcessado.getFilename());
		
		List<ContaDto> contas = new ArrayList<ContaDto>();
		
		contas = CSVUtil.lerCSV(resourceCSVProcessado, contas);
		
		assertEquals(2, contas.size());
		
		for (int i = 0; i < contas.size(); i++) {
			assertEquals(CONTA_DTO_LIST.get(i).getAgencia(), contas.get(i).getAgencia());
			assertEquals(CONTA_DTO_LIST.get(i).getConta(), contas.get(i).getConta());
			assertEquals(CONTA_DTO_LIST.get(i).getSaldo(), contas.get(i).getSaldo(), 0.0001);
			assertEquals(CONTA_DTO_LIST.get(i).getStatus(), contas.get(i).getStatus());
		}
		
	}

}
