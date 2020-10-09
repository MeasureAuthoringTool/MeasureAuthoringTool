package mat.server.service.impl;

import mat.dao.ListObjectDAO;
import mat.model.ListObject;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import mat.vsac.model.ValueSet;

import java.sql.Timestamp;

/**
 * ValueSetXLSGenerator java class.
 **/
public class ValueSetXLSGenerator extends XLSGenerator {

	/**
	 * Adds the value set work sheet.
	 * 
	 * @param loid
	 *            - String.
	 * @param names
	 *            - String Array.
	 * @param wkbk
	 *            -HSSFWorkbook.
	 * @param lo
	 *            - ListObject. *
	 */
	public final void addValueSetWorkSheet(final String loid, final String[] names,
			final HSSFWorkbook wkbk, final ListObject lo) {
		String sheetName = stripInvalidChars(lo.getName());

		HSSFCellStyle style = wkbk.createCellStyle();
		HSSFSheet wkst = createSheet(wkbk, style, sheetName);

		createHeaderRow(wkst, getHEADER_STRINGS(), names, 0, style);

		cacheXLSRow(lo, null, null);
		writeRowCache(wkst);
		rowCache.clear();
		sizeColumns(wkst);
	}
	
	/**
	 * Gets the xls.
	 * 
	 * @param loid
	 *            - String.
	 * @param lo
	 *            - ListObject.
	 * @return HSSFWorkbook. *
	 */

	public final HSSFWorkbook getXLS(final String loid, final ListObject lo) {

		HSSFWorkbook wkbk = new HSSFWorkbook();
		createMetaData(wkbk);
		// addDisclaimer(wkbk);
		// adding measure value set
		addValueSetWorkSheet(loid, getNAME_STRINGS(), wkbk, lo);
		return wkbk;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.impl.XLSGenerator#cacheXLSRow(mat.model.ListObject, mat.dao.ListObjectDAO, java.sql.Timestamp)
	 */
	@Override
	protected final void cacheXLSRow(final ListObject lo, final ListObjectDAO listObjectDAO,
			final Timestamp vsPackageDate) {
		if (!"22".equals(lo.getCategory().getId())) {
			processXLSRow(lo, null, null);
		}
	}

	/* (non-Javadoc)
	 * @see mat.server.service.impl.XLSGenerator#getErrorXLS()
	 */
	@Override
	public final HSSFWorkbook getErrorXLS() {
		HSSFWorkbook wkbk = new HSSFWorkbook();
		HSSFSheet wkst = wkbk.createSheet("Sheet 1");
		wkst.createRow(0).createCell(0).setCellValue("");
		return wkbk;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.impl.XLSGenerator#cacheXLSRow(mat.model.ValueSet)
	 */
	@Override
	protected void cacheXLSRow(final ValueSet lo) {

	}
}
