package org.ifmc.mat.testdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class GenerateCodeListImportFile {

	private final int code = 0;
	private final int desc = 1;
	private final String[] cols = new String[]{"Code","Descriptor"};
	
	public void generate(String fname, int numCodes) throws IOException{
		/*file setup*/
		String curDir = System.getProperty("user.dir");
		String fpath = curDir+File.separator+fname;
		File f = new File(fpath);	
		if(f.exists())
			f.delete();
		f.createNewFile();
		
		/*create workbook and worksheet*/
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		
		/*create header row*/
		HSSFRow row = sheet.createRow(0); 
		row.createCell(code).setCellValue(cols[code]);
		row.createCell(desc).setCellValue(cols[desc]);
		
		/*generate numCodes rows in the file*/
		String descPrefix = "This is a forty char test for:";
		for(int i = 1; i <= numCodes; i++){
			HSSFRow codeRow = sheet.createRow(i); 

			String cstr = i+"";
			String lzs = "";
			for(int j = cstr.length(); j < 10; j++)
				lzs += "0";
			cstr = lzs+cstr;
			String dstr = descPrefix+cstr;
			codeRow.createCell(code).setCellValue(cstr);
			codeRow.createCell(desc).setCellValue(dstr);
		}
		
		FileOutputStream fileOut = new FileOutputStream(fname);
		wb.write(fileOut);
		fileOut.close();
	}
	
	public static void main(String[] args) throws IOException{
		String fname = "CodeListImport.xls";
		int numCodes = 40000;
		GenerateCodeListImportFile gclif = new GenerateCodeListImportFile();
		gclif.generate(fname, numCodes);
	}
}
