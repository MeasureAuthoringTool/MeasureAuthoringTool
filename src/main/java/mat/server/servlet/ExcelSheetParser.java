package mat.server.servlet;

import mat.client.shared.MatContext;
import mat.model.Code;
import mat.server.exception.ExcelParsingException;
import mat.shared.ConstantMessages;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;


/**
 * The Class ExcelSheetParser.
 */
public class ExcelSheetParser {
	
	
	
	/**
	 * Read excel.
	 * 
	 * @param fileInputStream
	 *            the file input stream
	 * @param fileName
	 *            the file name
	 * @return the hash set
	 * @throws ExcelParsingException
	 *             the excel parsing exception
	 */
	@SuppressWarnings({ "resource", "deprecation", "unlikely-arg-type" })
	public HashSet<Code> readExcel(InputStream fileInputStream,String fileName) throws ExcelParsingException{
		Workbook workbook = null;
		Sheet s = null;
		Row rowData = null;
		Row headerData = null;
		int rowCount = 0;
		int totalSheet = 0;
		HashSet<Code> codeSet = new HashSet<Code>();
	    int mid= fileName.lastIndexOf(".");
	    String extension = fileName.substring(mid+1,fileName.length());
	  	
	    try {
			if(extension.equalsIgnoreCase("xlsx")){
				workbook =  new XSSFWorkbook(fileInputStream);
			}else{
				workbook =  new HSSFWorkbook(fileInputStream);
			}
			totalSheet = workbook.getNumberOfSheets();
			if(totalSheet > 0) {
				System.out.println("Total Sheet Found:" + totalSheet);
				for(int j=0;j<totalSheet;j++) {
					System.out.println("Sheet Name:" + workbook.getSheetAt(j).getSheetName());
				}
			}
 			//Getting Default Sheet i.e. 0
			s = workbook.getSheetAt(0);
 			
			//This returns last row in the sheet.
			//s.getPhysicalNumberOfRows() returns the number of rows which physically contains value in it. It does not work when there are empty rows.
			System.out.println("Last Row in the Sheet: "+ s.getLastRowNum());
			rowCount = s.getLastRowNum();
            
	        //Assuming First Row in the Sheet is Header	
			headerData = s.getRow(0);
			
			//Reading Individual Row Content
			for (int i = 1; i <= rowCount; i++) {
				//Get Individual Row
				try{
					rowData = s.getRow(i);
				}catch(IndexOutOfBoundsException e){
					break;
				}
				//adding null check
				if(rowData != null){
					Code code = new Code();
						for (int c = 0; c < 2; c++) {
							  String value = "";
							  if(rowData != null){
								  Cell cell = rowData.getCell(c);
								  
								  if(cell != null && !cell.toString().trim().equals("")){
									  switch (cell.getCellType()) {
				
									  case Cell.CELL_TYPE_FORMULA:
										  value =cell.getCellFormula();
										  break;
				
									  case Cell.CELL_TYPE_NUMERIC:
										  cell.setCellType(Cell.CELL_TYPE_STRING);//This line will take care if the excelsheet as 2 0r 2.0 
										  value = cell.getStringCellValue();
										  break;
				                     case Cell.CELL_TYPE_STRING:
										  value = cell.getStringCellValue();
										  break;
									  default:
									  }//end of switch
								  }
							  }
							  //System.out.println(headerData.getCell(c)+ ":" + value);
							  if(headerData.getCell(c) != null && !headerData.getCell(c).equals("")){
								  if(headerData.getCell(c).getStringCellValue().equalsIgnoreCase("Code")){
									  code.setCode(value.trim());
								  }else if(headerData.getCell(c).getStringCellValue().equalsIgnoreCase("Descriptor")){
									  code.setDescription(value.trim());
								  }
							  }
						}//end of for
					
					//Check for incomplete row
					if(code.getCode().trim().equalsIgnoreCase("")&& !code.getDescription().trim().equalsIgnoreCase("")){
						throw new ExcelParsingException(ConstantMessages.INCOMPLETE_ROW_ERROR);
					}else if(!code.getCode().trim().equalsIgnoreCase("")&& code.getDescription().trim().equalsIgnoreCase("")){
						throw new ExcelParsingException(ConstantMessages.INCOMPLETE_ROW_ERROR);
					}else if (!code.getCode().trim().equalsIgnoreCase("")&& !code.getDescription().trim().equalsIgnoreCase("")){
						if(!codeSet.add(code))
							throw new ExcelParsingException(MatContext.get().getMessageDelegate().getDuplicateErrorMessage());
					}
				}
			}//end of outer for
			  if(codeSet.isEmpty()){
				  throw new ExcelParsingException(MatContext.get().getMessageDelegate().getEmptyFileError());
			  }
			  
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new ExcelParsingException(MatContext.get().getMessageDelegate().getSystemErrorMessage());
		}
        return codeSet;
	}	

}
