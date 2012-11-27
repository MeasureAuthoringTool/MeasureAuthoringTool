package org.ifmc.mat.sprint1Testcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import mat.server.exception.ExcelParsingException;
import mat.server.servlet.ExcelSheetParser;

import org.junit.Before;
import org.junit.Test;

public class ExcelSheetParserTest {
	
	
	private FileInputStream fileInputStream = null;
	private String path = null;
	
	@Before
	public void setUp(){
		String ud = System.getProperty("user.dir");
		String fs = File.separator;
		path = ud+fs+"test"+fs+"code_only.xlsx";
		System.out.println(path);
	}
	
	@Test
	public void readExcelTest() {
		boolean caught = false;
		try{
			ExcelSheetParser parser = new ExcelSheetParser();
			fileInputStream = new FileInputStream(new File(path));
			parser.readExcel(fileInputStream, path);
		}catch(ExcelParsingException e){
			caught = true;
			System.out.println(e.getErrorMessage());
		}catch(IOException e){
			caught = true;
			System.out.println("Io Exception");
			e.printStackTrace();
		}catch(Exception e){
			caught = true;
			System.out.println("Some other exception");
			e.printStackTrace();
		}
		if(!caught){
			System.out.println("No excel Parsing Exception, Input file is a valid file");
		}
		
	}	
}
