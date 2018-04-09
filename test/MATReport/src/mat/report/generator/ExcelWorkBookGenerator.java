package MATReport.src.mat.report.generator;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * 
 */

/**
 * @author vandavar
 * This creates the Excel Workbook with the Reporting Results, created in different worksheets.
 *
 */
public class ExcelWorkBookGenerator {
	private HSSFWorkbook workbook;
	
	private HSSFSheet firstSheet;
	private HSSFSheet secondSheet;
	private HSSFSheet thirdSheet;
	private HSSFSheet fourthSheet;
	private HSSFSheet fifthSheet;
	private HSSFSheet sixthSheet;
	private HSSFCellStyle headercellStyle;
	private String fileName;
	
	String getFileName(){
		if(fileName == null){
			Calendar cal = Calendar.getInstance();
			String tString = Long.toString(cal.getTimeInMillis());
			fileName ="workbook"+ tString + ".xls";
		}
		return fileName;
	}
	
	public ExcelWorkBookGenerator(){
		workbook = new HSSFWorkbook(); 
		firstSheet = workbook.createSheet("1-Summary of User Accounts"); 
		secondSheet = workbook.createSheet("PLACEHOLDER 2-User Acct Changes"); 
		thirdSheet = workbook.createSheet("3-Summary of Meas. & CL by User");
		fourthSheet = workbook.createSheet("4-Summary of Measures");
		fifthSheet = workbook.createSheet("5-QDM Elements");
		sixthSheet = workbook.createSheet("6-Trends over Time");
		headercellStyle = setHeaderStyle(workbook);
	}
	
	
	
	public HSSFSheet initWorkSheet(String workSheetName, ArrayList<String> headerList){
		HSSFSheet wkst = workbook.getSheet(workSheetName);
		HSSFRow headerRow = wkst.createRow(0);
		wkst.setDefaultColumnWidth(40);
		 //Create Header Row.
		int headercolCount=0;
		for(String header : headerList){
			HSSFCell cell = headerRow.createCell(headercolCount);
			cell.setCellValue(header);
			cell.setCellStyle(headercellStyle);
			headercolCount++;
		}
		return wkst;
	}
	
	void fillInTrendSheet(HSSFSheet wkst, HashMap<String, Object> dataList,ArrayList<String> headerList){
		
		try{
			int rowCount =1;
			HSSFRow row = wkst.createRow(rowCount);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue((String)dataList.get("DATE_STRING"));
			cell = row.createCell(1);
			cell.setCellValue((String)dataList.get("NEW_USER_ACCOUNTS"));
			cell = row.createCell(2);
			cell.setCellValue((String)dataList.get("MEASURES_COMPLETE"));
			cell = row.createCell(3);
			cell.setCellValue((String)dataList.get("ORG_OIDS"));
			
			FileOutputStream fileOut = new FileOutputStream(getFileName());
			workbook.write(fileOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		
		
	}
	
	
	public void fillInSheet(HSSFSheet wkst, HashMap<String, HashMap<String, Object>> dataList,ArrayList<String> headerList){

		try{
			int rowCount =1;
			//Create subsequent rows.
			for(String dataRecord: dataList.keySet()){
				HSSFRow row = wkst.createRow(rowCount);
				rowCount++;
				for(int i= 0; i< headerList.size(); i++){
					HSSFCell cell = row.createCell(i);
					if(dataList.get(dataRecord).get(headerList.get(i))!= null){
						cell.setCellValue(dataList.get(dataRecord).get(headerList.get(i)).toString());
					}
					else{
						cell.setCellValue("");
					}
				}	
			}	
			FileOutputStream fileOut = new FileOutputStream(getFileName());
			workbook.write(fileOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void createWorkSheet(String workSheetName, HashMap<String, HashMap<String, Object>> dataList,ArrayList<String> headerList){
	try{
		int rowCount =0;
		int headercolCount=0;
		
		HSSFSheet wkst = workbook.getSheet(workSheetName);
		HSSFRow headerRow = wkst.createRow(rowCount);
		wkst.setDefaultColumnWidth(40);
		 //Create Header Row.
		for(String header : headerList){
			HSSFCell cell = headerRow.createCell(headercolCount);
			cell.setCellValue(header);
			cell.setCellStyle(headercellStyle);
			headercolCount++;
		}
		rowCount++;
		
		//Create subsequent rows.
		for(String key: dataList.keySet()){
		HSSFRow row = wkst.createRow(rowCount);
			for(String innerkey: dataList.get(key).keySet()){
				for(int i= 0; i< headerRow.getPhysicalNumberOfCells();i++){
					HSSFCell HCell = headerRow.getCell(i);
					if(innerkey.equalsIgnoreCase(HCell.toString())){
						HSSFCell cell = row.createCell(i);
						if(dataList.get(key).get(innerkey)!= null){
							cell.setCellValue(dataList.get(key).get(innerkey).toString());
						    break;
						}
					}
				}	
			}
		rowCount++;
		}
	FileOutputStream fileOut = new FileOutputStream(getFileName());
	workbook.write(fileOut);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	
	/**  
	 * This method is used to set the styles for all the headers  
	 * of the excel sheet.  
	 * @param sampleWorkBook - Name of the workbook.  
	 * @return cellStyle - Styles for the Header data of Excel sheet.  
	 */ 

	 private HSSFCellStyle setHeaderStyle(HSSFWorkbook sampleWorkBook)  
	 {  
		 HSSFFont font = sampleWorkBook.createFont();
		 font.setFontHeightInPoints((short)11);
		 font.setFontName(HSSFFont.FONT_ARIAL);  
		 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		 font.setColor(IndexedColors.BLACK.getIndex()); 
		 HSSFCellStyle cellStyle = sampleWorkBook.createCellStyle();  
		 cellStyle.setFont(font); 
		 cellStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		 cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 return cellStyle;  
	}  


	
	public void createQDMWorkSheet(String workSheetName, ArrayList<LinkedHashMap<String, Object>> reportData,ArrayList<String> headerList){
		try{
			int rowCount =0;
			int headercolCount=0;
			HSSFSheet wkst = workbook.getSheet(workSheetName);
			HSSFRow headerRow = wkst.createRow(rowCount);
			 //Create Header Row.
			wkst.setDefaultColumnWidth(40);
			for(String header : headerList){
				HSSFCell cell = headerRow.createCell(headercolCount);
				cell.setCellValue(header);
				cell.setCellStyle(headercellStyle);
				headercolCount++;
			}
			rowCount++;
			
			
			for(LinkedHashMap<String, Object> rowData: reportData){
				//handle cat, dt and attribute				
				
				HSSFRow row = wkst.createRow(rowCount);
				
				HSSFCell cell = row.createCell(0);				
				cell.setCellValue((String) rowData.get(headerList.get(0)));
				cell = row.createCell(1);
				cell.setCellValue((String) rowData.get(headerList.get(1)));
				cell = row.createCell(2);
				cell.setCellValue((String) rowData.get(headerList.get(2)));
				
				ArrayList<String> measuresList = (ArrayList<String>) rowData.get("msrList");
				int totalCounter = 0;
				for(int i=3; i < (headerList.size()-1); i++){
					cell = row.createCell(i);
					if(measuresList.indexOf(headerList.get(i)) != -1){
						cell.setCellValue("X");
						totalCounter++;
					}
				}

				if(totalCounter != 0){
					cell = row.createCell((headerList.size()-1));					
					cell.setCellValue("" + totalCounter);
				}

				rowCount++;
			}
			FileOutputStream fileOut = new FileOutputStream(getFileName());
			workbook.write(fileOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void createSummaryWorkSheet(String workSheetName, ArrayList<LinkedHashMap<String, String>> reportData,ArrayList<String> headerList){
		try{
			int rowCount =0;
			int headercolCount=0;
			HSSFSheet wkst = workbook.getSheet(workSheetName);
			HSSFRow headerRow = wkst.createRow(rowCount);
			 //Create Header Row.
			wkst.setDefaultColumnWidth(40);
			for(String header : headerList){
				HSSFCell cell = headerRow.createCell(headercolCount);
				cell.setCellValue(header);
				cell.setCellStyle(headercellStyle);
				headercolCount++;
			}
			rowCount++;
			
			
			for(LinkedHashMap<String, String> rowData: reportData){
				//handle cat, dt and attribute				
				
				HSSFRow row = wkst.createRow(rowCount);
				
				for(int i=0; i < headerList.size(); i++){
					HSSFCell cell = row.createCell(i);
					cell.setCellValue((String) rowData.get(headerList.get(i)));
				}
	
				rowCount++;
			}
			FileOutputStream fileOut = new FileOutputStream(getFileName());
			workbook.write(fileOut);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
