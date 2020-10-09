package mat.server.service.impl;

import mat.dao.ListObjectDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.model.Code;
import mat.model.CodeList;
import mat.model.GroupedCodeList;
import mat.model.ListObject;
import mat.model.clause.MeasureExport;
import mat.shared.ConstantMessages;
import mat.shared.DateUtility;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Name;
import mat.vsac.model.MatConcept;
import mat.vsac.model.ValueSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** The Class XLSGenerator. */
public abstract class XLSGenerator {
	
	/** The Class RowCacheItem. */
	protected class RowCacheItem implements Comparable<RowCacheItem> {
		
		/** The style. */
		public HSSFCellStyle style;
		
		/** The values. */
		public String[] values;
		
		/**
		 * Compare two RowCacheItems based on OID first. if they match on OID,
		 * then compare on Code.
		 * @param o - RowCacheItem.
		 * @return int.
		 */
		
		@Override
		public final int compareTo(final RowCacheItem o) {
			DotCompare dc = new DotCompare();
			int ret;
			ret = dc.dotNotationCompare(values[oid], o.values[oid]);
			if (ret != 0) {
				return ret;
			}
			return dc.dotNotationCompare(values[code], o.values[code]);
		}
	}
	
	/** The author. */
	protected final String AUTHOR = "eMeasureTool";
	
	/**
	 * WorkBook Header.
	 * **/
	private final String[] HEADER_STRINGS = { "Value Set Developer",
			"Value Set OID", "Version" ,"Expansion Identifier", "Revision Date", "Value Set Name",
			"Code System", "Code System Version", "Code" , "Descriptor"};
	
	/** The keywords. */
	protected final String KEYWORDS = "Value Set, OID, Export, Measure, Code, Descriptor";
	
	/**
	 * WorkBook Content.
	 * **/
	private final String[] NAME_STRINGS = { "ValueSetDeveloper",
			"ValueSetOID", "Version", "ExpansionIdentifier", "RevisionDate", "ValueSetName", "QDMCategory",
			"CodeSystem", "CodeSystemVersion", "Code" , "Descriptor"};
	
	/** The row cache. */
	protected ArrayList<RowCacheItem> rowCache = new ArrayList<RowCacheItem>();
	
	/** The measuredeveloper. */
	protected final int measuredeveloper = 0;

	/** The oid. */
	protected final int oid = 1;

	/** The version */
	protected final int version = 2;
	
	/** The expansionIdentifier */
	protected final int expansionIdentifier = 3;
	
	/** The revision date. */
	protected final int revisionDate = 4;	

	/** The standardconcept. */
	protected final int standardconcept = 5;
	
	/** The standardtaxonomy. */
	protected final int standardtaxonomy = 6;
	
	/** The standardtaxonomyversion. */
	protected final int standardtaxonomyversion = 7;
	
	/** The code. */
	protected final int code = 8;	
	
	/** The codedescription. */
	protected final int codedescription = 9;
		
	/** The subject. */
	protected final String SUBJECT = "Value Set Export";
	
	/** The title. */
	protected final String TITLE = "Value Set Export";
	
	/** Adds the disclaimer.
	 * 
	 * @param wkbk - HSSFWorkbook. * */
	@SuppressWarnings("deprecation")
	protected final void addDisclaimer(final HSSFWorkbook wkbk) {
		String disclaimerText = "The codes that you are exporting directly reflect the codes you entered into the "
				+ "Measure Authoring Tool.  These codes may be owned by a third party and "
				+ "subject to copyright or other intellectual property restrictions.  Use of these "
				+ "codes may require permission from the code owner or agreement to a license.  "
				+ "It is your responsibility to ensure that your use of any third party code is "
				+ "permissible and that you have fulfilled any notice or license requirements "
				+ "imposed by the code owner.  Use of the Measure Authoring Tool does not "
				+ "confer any rights on you with respect to these codes other than those codes that may "
				+ "be available from the code owner.";
		HSSFSheet wkst = wkbk.createSheet("Disclaimer");
		HSSFRow row = wkst.createRow(0);
		row.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue(
				disclaimerText);
		wkst.setColumnWidth(0, (75 * 256));
		HSSFCell cell = row.getCell(0);
		HSSFCellStyle style = wkbk.createCellStyle();
		style.setWrapText(true);
		cell.setCellStyle(style);
	}
	
	/** Cache row.
	 * 
	 * @param values - String array.
	 * @param style - HSSFCellStyle. * */
	protected final void cacheRow(final String[] values, final HSSFCellStyle style) {
		RowCacheItem row = new RowCacheItem();
		row.values = values.clone();
		row.style = style;
		rowCache.add(row);
	}
	
	/** Cache xls row.
	 * 
	 * @param lo - ListObject.
	 * @param listObjectDAO - ListObjectDAO.
	 * @param vsPackageDate - Time stamp.
	 * 
	 *        * */
	protected abstract void cacheXLSRow(ListObject lo,
			ListObjectDAO listObjectDAO, Timestamp vsPackageDate);
	
	/** Cache xls row.
	 * 
	 * @param lo - ValueSet.
	 * 
	 *        * */
	protected abstract void cacheXLSRow(ValueSet lo);
	
	/** Creates the header row.
	 * 
	 * @param wkst - HSSFSheet.
	 * @param values - String Array.
	 * @param names - String Array.
	 * @param rownum - Integer.
	 * @param style -HSSFCellStyle.
	 * @return HSSFRow. * */
	public final HSSFRow createHeaderRow(final HSSFSheet wkst, final String[] values,
			final String[] names, final int rownum, final HSSFCellStyle style) {
		HSSFRow headerRow = createXLSRow(wkst, values, rownum, style);
		HSSFWorkbook wkbk = wkst.getWorkbook();
		
		generateName(wkbk, names[measuredeveloper], "'" + wkst.getSheetName()
				+ "'!$A$1");
		generateName(wkbk, names[oid], "'" + wkst.getSheetName() + "'!$B$1");
		generateName(wkbk, names[version], "'" + wkst.getSheetName()
		+ "'!$H$1");
		generateName(wkbk, names[expansionIdentifier], "'" + wkst.getSheetName()
		+ "'!$I$1");

		generateName(wkbk, names[revisionDate], "'" + wkst.getSheetName()
				+ "'!$C$1");
		generateName(wkbk, names[standardconcept], "'" + wkst.getSheetName()
				+ "'!$D$1");
		/*
		 * generateName(wkbk, names[standardcategory], "'" + wkst.getSheetName()
		 * + "'!$E$1");
		 */
		generateName(wkbk, names[standardtaxonomy], "'" + wkst.getSheetName()
				+ "'!$E$1");
		generateName(wkbk, names[standardtaxonomyversion],
				"'" + wkst.getSheetName() + "'!$F$1");
		generateName(wkbk, names[code], "'" + wkst.getSheetName() + "'!$G$1");
		generateName(wkbk, names[codedescription], "'" + wkst.getSheetName()
				+ "'!$J$1");
		
		return headerRow;
	}
	
	/** Creates the meta data.
	 * 
	 * @param wkbk - HSSFWorkbook.
	 * 
	 *        * */
	public final void createMetaData(final HSSFWorkbook wkbk) {
		// Author: eMeasureTool, Title: Value Set Export, Subject: Value Set
		// Export, Keywords: Value Set, OID, Export, Measure, Code, Descriptor
		wkbk.createInformationProperties();
		SummaryInformation si = wkbk.getSummaryInformation();
		si.setAuthor(AUTHOR);
		si.setTitle(TITLE);
		si.setSubject(SUBJECT);
		si.setKeywords(KEYWORDS);
	}
	
	/** Creates the sheet.
	 * 
	 * @param wkbk -HSSFWorkbook.
	 * @param style -HSSFCellStyle.
	 * @param sheetName - String.
	 * @return HSSFSheet. * */
	
	protected final HSSFSheet createSheet(final HSSFWorkbook wkbk, final HSSFCellStyle style,
			final String sheetName) {
		
		HSSFSheet wkst = wkbk.createSheet(sheetName);
		int heightPoint = 10;
		HSSFFont font = wkbk.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) heightPoint);
		font.setBold(true);
		font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
		style.setFont(font);
		style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		
		return wkst;
	}
	
	/** Creates the xls row.
	 * 
	 * @param wkst - HSSFSheet.
	 * @param values - String Array.
	 * @param style - HSSFCellStyle.
	 * @return HSSFRow. * */
	protected final HSSFRow createXLSRow(final HSSFSheet wkst, final String[] values,
			final HSSFCellStyle style) {
		return createXLSRow(wkst, values, wkst.getLastRowNum() + 1, style);
	}
	
	/** Creates the xls row.
	 * 
	 * @param wkst - HSSFSheet.
	 * @param values - String Array.
	 * @param rownum - Integer.
	 * @param style - HSSFCellStyle.
	 * @return HSSFRow. * */
	@SuppressWarnings("deprecation")
	protected final HSSFRow createXLSRow(final HSSFSheet wkst, final String[] values, final int rownum,
			final HSSFCellStyle style) {
		HSSFRow row = wkst.createRow(rownum);
		row.createCell(measuredeveloper, HSSFCell.CELL_TYPE_STRING)
		.setCellValue(values[measuredeveloper]);
		row.createCell(oid, HSSFCell.CELL_TYPE_STRING)
		.setCellValue(values[oid]);
		row.createCell(version, HSSFCell.CELL_TYPE_STRING)
		.setCellValue(values[version]);
		row.createCell(expansionIdentifier, HSSFCell.CELL_TYPE_STRING)
		.setCellValue(values[expansionIdentifier]);
		row.createCell(revisionDate, HSSFCell.CELL_TYPE_STRING).setCellValue(
				values[revisionDate]);
		row.createCell(standardconcept, HSSFCell.CELL_TYPE_STRING)
		.setCellValue(values[standardconcept]);
		row.createCell(standardtaxonomy, HSSFCell.CELL_TYPE_STRING)
		.setCellValue(values[standardtaxonomy]);
		row.createCell(standardtaxonomyversion, HSSFCell.CELL_TYPE_STRING)
		.setCellValue(values[standardtaxonomyversion]);
		row.createCell(code, HSSFCell.CELL_TYPE_STRING).setCellValue(
				values[code]);
		row.createCell(codedescription, HSSFCell.CELL_TYPE_STRING)
		.setCellValue(values[codedescription]);
		if (style != null) {
			row.getCell(measuredeveloper).setCellStyle(style);
			row.getCell(oid).setCellStyle(style);
			row.getCell(version).setCellStyle(style);
			row.getCell(expansionIdentifier).setCellStyle(style);
			row.getCell(revisionDate).setCellStyle(style);
			row.getCell(standardconcept).setCellStyle(style);
			row.getCell(standardtaxonomy).setCellStyle(style);
			row.getCell(standardtaxonomyversion).setCellStyle(style);
			row.getCell(code).setCellStyle(style);
			row.getCell(codedescription).setCellStyle(style);
		}
		return row;
	}
	
	/** Generate name.
	 * 
	 * @param wkbk - HSSFWorkbook.
	 * @param nameStr - String.
	 * @param referenceStr - String. * */
	protected final void generateName(final HSSFWorkbook wkbk, final String nameStr,
			final String referenceStr) {
		// names are required for 508 testing
		Name name = wkbk.createName();
		name.setNameName(nameStr);
		name.setRefersToFormula(referenceStr);
	}
	
	/** Gets the error xls.
	 * 
	 * @return HSSFWorkbook. * */
	public HSSFWorkbook getErrorXLS() {
		HSSFWorkbook wkbk = new HSSFWorkbook();
		HSSFSheet wkst = wkbk.createSheet("Sheet 1");
		wkst.createRow(0)
		.createCell(0)
		.setCellValue("Measure must be re-packaged to capture the Value Set export."
				+ " Please re-package and try again.");
		return wkbk;
	}
	
	/** Gets the header strings.
	 * 
	 * @return the hEADER_STRINGS */
	public String[] getHEADER_STRINGS() {
		return HEADER_STRINGS;
	}
	
	/**
	 * NOTE: there is an error in the POI API that keeps us from using.
	 * HSSFWorkbook.getBytes()
	 *
	 * @param wkbk -HSSFWorkbook.
	 * @return byte array from workbook
	 * @throws IOException - IOException.
	 */
	public final byte[] getHSSFWorkbookBytes(final HSSFWorkbook wkbk) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		wkbk.write(baos);
		byte[] barr = baos.toByteArray();
		baos.close();
		return barr;
	}
	
	/** Gets the name strings.
	 * 
	 * @return the nAME_STRINGS */
	public String[] getNAME_STRINGS() {
		return NAME_STRINGS;
	}
	
	/** Gets the xls.
	 * 
	 * @param measureId - String.
	 * @param measureExportDAO - MeasureExportDAO.
	 * @return HSSFWorkbook. * */
	public final HSSFWorkbook getXLS(final String measureId,
			final MeasureExportDAO measureExportDAO) {
		MeasureExport me = measureExportDAO.findByMeasureId(measureId);
		me.getCodeListBarr();
		try {
			byte[] barr = me.getCodeListBarr();
			ByteArrayInputStream bais = new ByteArrayInputStream(barr);
			HSSFWorkbook wkbk = new HSSFWorkbook(bais);
			return wkbk;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Process xls row.
	 * 
	 * @param lo - ListObject.
	 * @param listObjectDAO - ListObjectDAO.
	 * @param vsPackageDate - Timestamp.
	 * 
	 *        * */
	protected final void processXLSRow(final ListObject lo, final ListObjectDAO listObjectDAO,
			final Timestamp vsPackageDate) {
		
		String measureDeveloper = "";
		// US 178, Using steward organization name for SDE !!.
		if ((lo.getSteward() != null)
				&& !lo.getSteward().getOrgName().equalsIgnoreCase("Other")) {
			measureDeveloper = lo.getSteward().getOrgName();
		} else if (lo.getStewardOther() != null) {
			measureDeveloper = lo.getStewardOther();
		}
		String standardConcept = lo.getName();
		// String category = lo.getCategory().getDescription();
		String taxonomy = lo.getCodeSystem().getDescription();
		String taxonomyVersion = lo.getCodeSystemVersion();
		String oid = lo.getOid();
		String valueSetLastModified = DateUtility.convertDateToString(lo
				.getLastModified());
		
		if (lo instanceof CodeList) {
			if (((CodeList) lo).getCodes().isEmpty()) {
				String code = "";
				String description = "";
				cacheRow(new String[] {measureDeveloper, oid,
						valueSetLastModified, standardConcept,
						taxonomy, taxonomyVersion, code, description }, null);
			}
			Set<Code> codeSet = new HashSet<Code>();
			for (Code c : ((CodeList) lo).getCodes()) {
				codeSet.add(c);
			}
			for (Code c : codeSet) {
				String code = c.getCode();
				String description = c.getDescription();
				cacheRow(new String[] { measureDeveloper, oid, valueSetLastModified, standardConcept,
						taxonomy, taxonomyVersion, code, description }, null);
			}
		} else {
			if (lo.getCodesLists().isEmpty()) {
				String code = "";
				String description = "";
				cacheRow(new String[] { measureDeveloper, oid, valueSetLastModified, standardConcept,
						taxonomy, taxonomyVersion, code, description }, null);
			}
			for (GroupedCodeList gcl : lo.getCodesLists()) {
				String code = gcl.getCodeList().getOid();
				String description = gcl.getDescription();
				cacheRow(new String[] { measureDeveloper, oid, valueSetLastModified, standardConcept,
						taxonomy, taxonomyVersion, code, description }, null);
				cacheXLSRow(gcl.getCodeList(), listObjectDAO, vsPackageDate);
			}
		}
	}
	
	/** Process xls row.
	 * 
	 * @param lo - ValueSet.
	 * 
	 *        * */
	protected final void processXLSRow(final ValueSet lo) {
		
		String measureDeveloper = "";
		measureDeveloper = lo.getSource();
		String standardConcept = lo.getDisplayName();
		String code = "";
		String oid = lo.getID();
		String valueSetLastModified = lo.getRevisionDate();
		String expansionProfile = lo.getExpansionProfile();
		String version = "";
		if(!lo.getVersion().equalsIgnoreCase("Draft")
				) {
			version = lo.getVersion();
		}
		if(expansionProfile==null){
			expansionProfile = "";
		}
		if (ConstantMessages.GROUPING_CODE_SYSTEM
				.equalsIgnoreCase(lo.getType())) {
			String taxonomy = ConstantMessages.GROUPING_CODE_SYSTEM;
			String taxonomyVersion = "";
			String description = "";
			if (lo.getGroupedValueSet().get(0)
					.getConceptList().getConceptList() != null) {
				taxonomyVersion = lo.getGroupedValueSet().get(0)
						.getConceptList().getConceptList().get(0)
						.getCodeSystemVersion();
			}
			if (lo.getGroupedValueSet().size() == 0) {
				cacheRow(new String[] {measureDeveloper, oid, version, expansionProfile,
						valueSetLastModified, standardConcept, taxonomy,
						taxonomyVersion, code, description }, null);
			}
			for (ValueSet gcl : lo.getGroupedValueSet()) {
				code = gcl.getID();
				// description = gcl.getDescription();
				cacheRow(new String[] {measureDeveloper, oid, version, expansionProfile,
						valueSetLastModified, standardConcept,
						taxonomy, taxonomyVersion, code , description}, null);
				cacheXLSRow(gcl);
			}
		} else {
			String taxonomyVersion = "";
			String taxonomy = "";
			String description = "";
			if (lo.getConceptList().getConceptList() != null) {
				taxonomy = lo.getConceptList().getConceptList().get(0)
						.getCodeSystemName();
				taxonomyVersion = lo.getConceptList().getConceptList().get(0)
						.getCodeSystemVersion();
				if ((lo.getConceptList().getConceptList().size() == 0)) {
					cacheRow(new String[] {measureDeveloper, oid, version, expansionProfile,
							valueSetLastModified, standardConcept,
							taxonomy, taxonomyVersion, code , description}, null);
				}
				
				for (MatConcept concept : lo.getConceptList().getConceptList()) {
					code = concept.getCode();
					description = concept.getDisplayName();
					cacheRow(new String[] {measureDeveloper, oid, version, expansionProfile ,
							valueSetLastModified, standardConcept,
							taxonomy, taxonomyVersion, code, description}, null);
				}
			}
		}
	}
	
	/** Size column.
	 * 
	 * @param wkst - HSSFSheet.
	 * @param col - Short. * */
	private void sizeColumn(final HSSFSheet wkst, final short col) {
		try {
			wkst.autoSizeColumn(col);
		} catch (Exception e) {
			wkst.setColumnWidth(col, (256 * 255));
		}
	}
	
	/** Size columns.
	 * 
	 * @param wkst - HSSFSheet. * */
	protected final void sizeColumns(final HSSFSheet wkst) {
		sizeColumn(wkst, (short) measuredeveloper);
		sizeColumn(wkst, (short) oid);
		sizeColumn(wkst, (short) revisionDate);
		sizeColumn(wkst, (short) standardconcept);
		sizeColumn(wkst, (short) standardtaxonomy);
		sizeColumn(wkst, (short) standardtaxonomyversion);
		sizeColumn(wkst, (short) code);
		sizeColumn(wkst, (short) version);
		sizeColumn(wkst, (short) expansionIdentifier);
		sizeColumn(wkst, (short) codedescription);
	}
	
	/** Strip invalid chars.
	 * 
	 * @param repStr - String.
	 * @return String. * */
	protected final String stripInvalidChars(final String repStr) {
		StringBuffer sb = new StringBuffer();
		String acceptableStr = " `~1!2@3#4$5%6^7&89(0)-_=+qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM,<.>{}|";
		
		for (char c : repStr.toCharArray()) {
			if (acceptableStr.indexOf(c) >= 0) {
				sb.append(c);
			}
		}
		
		if (sb.length() == 0) {
			return "temp";
		}
		return sb.toString();
		
	}
	
	/** Write row cache.
	 * 
	 * @param wkst - HSSFSheet. * */
	protected final void writeRowCache(final HSSFSheet wkst) {
		Collections.sort(rowCache);
		for (RowCacheItem row : rowCache) {
			createXLSRow(wkst, row.values, row.style);
		}
	}
	
}
