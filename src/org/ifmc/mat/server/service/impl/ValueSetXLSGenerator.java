package org.ifmc.mat.server.service.impl;
import java.sql.Timestamp;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.ifmc.mat.dao.ListObjectDAO;
import org.ifmc.mat.model.ListObject;





public class ValueSetXLSGenerator extends XLSGenerator{
	
	public void addValueSetWorkSheet(String loid, String[] names, HSSFWorkbook wkbk, ListObject lo){
		String sheetName = stripInvalidChars(lo.getName());
		
		HSSFCellStyle style = wkbk.createCellStyle();
		HSSFSheet wkst = createSheet(wkbk, style, sheetName);
		
		createHeaderRow(wkst, HEADER_STRINGS, names,  0, style);
		
		cacheXLSRow(lo, null, null);
		writeRowCache(wkst);
		rowCache.clear();
		sizeColumns(wkst);
	}
	
	public HSSFWorkbook getXLS(String loid, ListObject lo){
		
		HSSFWorkbook wkbk = new HSSFWorkbook();
		createMetaData(wkbk);
		addDisclaimer(wkbk);
		//adding measure value set
		addValueSetWorkSheet(loid, NAME_STRINGS,wkbk,lo);
		return wkbk;
	}
	
	@Override
	protected void cacheXLSRow(ListObject lo, ListObjectDAO listObjectDAO, Timestamp vsPackageDate){
		if(!"22".equals(lo.getCategory().getId())){
			processXLSRow(lo, null, null);
		}
	}
	@Override
	public HSSFWorkbook getErrorXLS(){
		HSSFWorkbook wkbk = new HSSFWorkbook();
		HSSFSheet wkst = wkbk.createSheet("Sheet 1");
		wkst.createRow(0).createCell(0).setCellValue("");
		return wkbk;
	}
}
