package mat.server.service.impl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mat.dao.ListObjectDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.model.Code;
import mat.model.CodeList;
import mat.model.GroupedCodeList;
import mat.model.ListObject;
import mat.model.clause.MeasureExport;
import mat.shared.DateUtility;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Name;

import edu.emory.mathcs.backport.java.util.Arrays;





public abstract class XLSGenerator {
	
	protected final int measuredeveloper = 0;
	protected final int oid = 1;
	protected final int lastModified = 2;
	protected final int standardconcept = 3;
	protected final int standardcategory = 4;
	protected final int standardtaxonomy = 5;
	protected final int standardtaxonomyversion = 6;
	protected final int code = 7;
	protected final int codedescription = 8;
	
	protected final String AUTHOR = "###";
	protected final String TITLE = "Value Set Export";
	protected final String SUBJECT = "Value Set Export";
	protected final String KEYWORDS = "Value Set, OID, Export, Measure, Code, Descriptor";
	protected ArrayList<RowCacheItem> rowCache = new ArrayList<RowCacheItem>();	
	
	protected class RowCacheItem implements Comparable<RowCacheItem>{
		public String [] values;
		public HSSFCellStyle style;
		
	    /**
	     * Compare two RowCacheItems based on OID first
	     * if they match on OID, then compare on Code
	     */
		public int compareTo(RowCacheItem o) {
			DotCompare dc = new DotCompare();
			  int ret;
			  ret = dc.dotNotationCompare(this.values[oid], o.values[oid]);
			  if(ret != 0)
				  return ret;
			  return dc.dotNotationCompare(this.values[code], o.values[code]);
		 }
	}
	
	protected HSSFSheet createSheet(HSSFWorkbook wkbk, HSSFCellStyle style, String sheetName){
		
		HSSFSheet wkst = wkbk.createSheet(sheetName);
		
		HSSFFont font = wkbk.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLACK.index);
		style.setFont(font);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom((short) 1);
		
		return wkst;
	}
	
	protected void cacheRow(String [] values, HSSFCellStyle style){
		RowCacheItem row = new RowCacheItem();
		row.values = values.clone();
		row.style = style;
		rowCache.add(row);
	}
	
	protected void writeRowCache(HSSFSheet wkst){
		Collections.sort(rowCache);
		for(RowCacheItem row: rowCache){
			createXLSRow(wkst, row.values,row.style);
		}
	}
	
	
	//US 549: changing the Value Set Version to Last Modified 
	protected final String[] HEADER_STRINGS = {
			"Value Set Developer",
			"Value Set OID",
			"Last Modified",
			"Value Set Name",
			"QDM Category",
			"Code System",
			"Code System Version",
			"Code",
			"Descriptor"};
	
	protected final String[] NAME_STRINGS = {
			"ValueSetDeveloper",
			"ValueSetOID",
			"LastModified",
			"ValueSetName",
			"QDMCategory",
			"CodeSystem",
			"CodeSystemVersion",
			"Code",
			"Descriptor"};
	
	protected void generateName(HSSFWorkbook wkbk, String nameStr, String referenceStr){
		//TODO Error if POI gets an appostrophie, need to handle this
		//names are required for 508 testing
		Name name = wkbk.createName();
		name.setNameName(nameStr);
	    name.setRefersToFormula(referenceStr);
	}
	
	
	protected HSSFRow createXLSRow(HSSFSheet wkst, String[] values, HSSFCellStyle style){
		return createXLSRow(wkst, values, wkst.getLastRowNum()+1, style);
	}
	
	protected HSSFRow createXLSRow(HSSFSheet wkst, String[] values, int rownum, HSSFCellStyle style){
		HSSFRow row = wkst.createRow(rownum);
		row.createCell(measuredeveloper, HSSFCell.CELL_TYPE_STRING).setCellValue(values[measuredeveloper]);
		row.createCell(oid, HSSFCell.CELL_TYPE_STRING).setCellValue(values[oid]);
		row.createCell(lastModified, HSSFCell.CELL_TYPE_STRING).setCellValue(values[lastModified]);
		row.createCell(standardconcept, HSSFCell.CELL_TYPE_STRING).setCellValue(values[standardconcept]);
		row.createCell(standardcategory, HSSFCell.CELL_TYPE_STRING).setCellValue(values[standardcategory]);
		row.createCell(standardtaxonomy, HSSFCell.CELL_TYPE_STRING).setCellValue(values[standardtaxonomy]);
		row.createCell(standardtaxonomyversion, HSSFCell.CELL_TYPE_STRING).setCellValue(values[standardtaxonomyversion]);
		row.createCell(code, HSSFCell.CELL_TYPE_STRING).setCellValue(values[code]);
		row.createCell(codedescription, HSSFCell.CELL_TYPE_STRING).setCellValue(values[codedescription]);
		
		
		if(style != null){
			row.getCell(measuredeveloper).setCellStyle(style);
			row.getCell(oid).setCellStyle(style);
			row.getCell(lastModified).setCellStyle(style);
			row.getCell(standardconcept).setCellStyle(style);
			row.getCell(standardcategory).setCellStyle(style);
			row.getCell(standardtaxonomy).setCellStyle(style);
			row.getCell(standardtaxonomyversion).setCellStyle(style);
			row.getCell(code).setCellStyle(style);
			row.getCell(codedescription).setCellStyle(style);
		}
		
		return row;
	}
	
	protected void sizeColumns(HSSFSheet wkst){
		sizeColumn(wkst, (short) measuredeveloper);
		sizeColumn(wkst, (short) oid);
		sizeColumn(wkst, (short) lastModified);
		sizeColumn(wkst, (short) standardconcept);
		sizeColumn(wkst, (short) standardcategory);
		sizeColumn(wkst, (short) standardtaxonomy);
		sizeColumn(wkst, (short) standardtaxonomyversion);
		sizeColumn(wkst, (short) code);
		sizeColumn(wkst, (short) codedescription);
	}
	
	private void sizeColumn(HSSFSheet wkst, short col){
		try{
			wkst.autoSizeColumn(col);
		}catch(Exception e){
			wkst.setColumnWidth(col, (256*255));
		}
	}
	
	protected void addDisclaimer(HSSFWorkbook wkbk){
		String disclaimerText =
				 "The codes that you are exporting directly reflect the codes you entered into the "+
				 "Measure Authoring Tool.  These codes may be owned by a third party and "+
				 "subject to copyright or other intellectual property restrictions.  Use of these "+
				 "codes may require permission from the code owner or agreement to a license.  "+
				 "It is your responsibility to ensure that your use of any third party code is "+
				 "permissible and that you have fulfilled any notice or license requirements "+
				 "imposed by the code owner.  Use of the Measure Authoring Tool does not "+
				 "confer any rights on you with respect to these codes other than those codes that may "+
				 "be available from the code owner.";
		HSSFSheet wkst = wkbk.createSheet("Disclaimer");
		HSSFRow row = wkst.createRow(0);
		row.createCell(0,HSSFCell.CELL_TYPE_STRING).setCellValue(disclaimerText);
		wkst.setColumnWidth(0, (75*256));
		HSSFCell cell = row.getCell(0);
		HSSFCellStyle style = wkbk.createCellStyle();
		style.setWrapText(true);
		cell.setCellStyle(style);
	}

	protected String stripInvalidChars(String repStr){

		StringBuffer sb = new StringBuffer();
		String acceptableStr = " `~1!2@3#4$5%6^7&89(0)-_=+qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM,<.>{}|";
		
		for(char c : repStr.toCharArray()){
			if(acceptableStr.indexOf(c)>=0)
				sb.append(c);
		}
		
		if(sb.length() == 0)
			return "temp";
		return sb.toString();
		
	}
	
	public HSSFWorkbook getXLS(String measureId, MeasureExportDAO measureExportDAO){
		MeasureExport me = measureExportDAO.findForMeasure(measureId);
		me.getCodeListBarr();
		try {
			byte[] barr =  me.getCodeListBarr();
			ByteArrayInputStream bais = new ByteArrayInputStream(barr);
			HSSFWorkbook wkbk = new HSSFWorkbook(bais);
			return wkbk;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public HSSFWorkbook getErrorXLS(){
		HSSFWorkbook wkbk = new HSSFWorkbook();
		HSSFSheet wkst = wkbk.createSheet("Sheet 1");
		wkst.createRow(0).createCell(0).setCellValue("Measure must be re-packaged to capture the Value Set export. Please re-package and try again.");
		return wkbk;
	}
	
	/**
	 * NOTE: there is an error in the POI API that keeps us from using HSSFWorkbook.getBytes() 
	 * @param wkbk
	 * @return byte array from workbook
	 * @throws IOException
	 */
	public byte[] getHSSFWorkbookBytes(HSSFWorkbook wkbk) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		wkbk.write(baos);
		byte[] barr = baos.toByteArray();
		baos.close();
		return barr;
	}
	
	public HSSFRow createHeaderRow(HSSFSheet wkst, String[] values, String[] names, int rownum, HSSFCellStyle style){
		HSSFRow headerRow = createXLSRow(wkst, values, rownum, style);
		HSSFWorkbook wkbk = wkst.getWorkbook();
		
		generateName(wkbk, names[measuredeveloper], "'"+wkst.getSheetName()+"'!$A$1");
		generateName(wkbk, names[oid], "'"+wkst.getSheetName()+"'!$B$1");
		generateName(wkbk, names[lastModified], "'"+wkst.getSheetName()+"'!$C$1");
		generateName(wkbk, names[standardconcept], "'"+wkst.getSheetName()+"'!$D$1");
		generateName(wkbk, names[standardcategory], "'"+wkst.getSheetName()+"'!$E$1");
		generateName(wkbk, names[standardtaxonomy], "'"+wkst.getSheetName()+"'!$F$1");
		generateName(wkbk, names[standardtaxonomyversion], "'"+wkst.getSheetName()+"'!$G$1");
		generateName(wkbk, names[code], "'"+wkst.getSheetName()+"'!$H$1");
		generateName(wkbk, names[codedescription], "'"+wkst.getSheetName()+"'!$I$1");
		
		return headerRow;
	}
	
	public void createMetaData(HSSFWorkbook wkbk){
		//Author: ###, Title: Value Set Export, Subject: Value Set Export, Keywords: Value Set, OID, Export, Measure, Code, Descriptor
		wkbk.createInformationProperties();
		SummaryInformation si = wkbk.getSummaryInformation();
		si.setAuthor(AUTHOR);
		si.setTitle(TITLE);
		si.setSubject(SUBJECT);
		si.setKeywords(KEYWORDS);
	}
	
	protected void processXLSRow(ListObject lo, ListObjectDAO listObjectDAO, Timestamp vsPackageDate){
		
		String measureDeveloper = "";
        //US 178, Using steward organization name for SDE !!.
		if(lo.getSteward() != null && !lo.getSteward().getOrgName().equalsIgnoreCase("Other")){
			measureDeveloper = lo.getSteward().getOrgName();
		}else if(lo.getStewardOther() != null){
			measureDeveloper = lo.getStewardOther();
		}
		String standardConcept = lo.getName();
		String category = lo.getCategory().getDescription();
		String taxonomy = lo.getCodeSystem().getDescription();
		String taxonomyVersion = lo.getCodeSystemVersion();
		String oid = lo.getOid();
		String valueSetLastModified = DateUtility.convertDateToString(lo.getLastModified());
		
		if(lo instanceof CodeList){
			if(((CodeList)lo).getCodes().isEmpty()){
				String code = "";
				String description = "";
				cacheRow( new String[]{
						measureDeveloper,
						oid,
						valueSetLastModified,
						standardConcept,
						category,
						taxonomy,
						taxonomyVersion,
						code,
						description}, null);
			}
			Set<Code> codeSet = new HashSet<Code>();
			for(Code c : ((CodeList)lo).getCodes()){
				codeSet.add(c);
			}
			for(Code c : codeSet){
				String code = c.getCode();
				String description = c.getDescription();
				cacheRow(new String[]{
						measureDeveloper,
						oid,
						valueSetLastModified,
						standardConcept,
						category,
						taxonomy,
						taxonomyVersion,
						code,
						description}, null);
			}
		}else{
			if(lo.getCodesLists().isEmpty()){
				String code = "";
				String description = "";
				cacheRow(new String[]{
						measureDeveloper,
						oid,
						valueSetLastModified,
						standardConcept,
						category,
						taxonomy,
						taxonomyVersion,
						code,
						description}, null);
			}
			for(GroupedCodeList gcl : lo.getCodesLists()){
				String code = gcl.getCodeList().getOid();
				String description = gcl.getDescription();
				cacheRow(new String[]{
						measureDeveloper,
						oid,
						valueSetLastModified,
						standardConcept,
						category,
						taxonomy,
						taxonomyVersion,
						code,
						description}, null);
				cacheXLSRow(gcl.getCodeList(), listObjectDAO, vsPackageDate);
			}
		}
	}
	
	protected abstract void cacheXLSRow(ListObject lo, ListObjectDAO listObjectDAO, Timestamp vsPackageDate);
	
	
}
