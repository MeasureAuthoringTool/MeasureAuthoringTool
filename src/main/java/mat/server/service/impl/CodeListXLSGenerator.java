package mat.server.service.impl;

import mat.dao.ListObjectDAO;
import mat.dao.QualityDataSetDAO;
import mat.model.ListObject;
import mat.model.clause.Measure;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import mat.vsacmodel.ValueSet;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** The Class CodeListXLSGenerator. */
public class CodeListXLSGenerator extends XLSGenerator{
	
	/** SVS Column name.**/
	private final String[] svsNameString = {
			"ValueSetDeveloper_SVS",
			"ValueSetOID_SVS",
			"Version_SVS",
			"ExpansionIdentifier_SVS",
			"LastModified_SVS",
			"ValueSetName_SVS",
			"QDMCategory_SVS",
			"CodeSystem_SVS",
			"CodeSystemVersion_SVS",
			"Code_SVS",
			"Descriptor_SVS"
	};
	
	/** List of String containing qdm OID.**/
	private List<String> qdmOIDs = new ArrayList<String>();
	
	
	/** Gets the xls.
	 * 
	 * @param m - Measure Object.
	 * @param allQDMs - List of String.
	 * @param qualityDataSetDAO - QualityDataSetDAO.
	 * @param listObjectDAO - ListObjectDAO.
	 * @param supplementalQDMS - List of String.
	 * @param ValueSets - List of ValueSet.
	 * @return HSSFWorkbook. * */
	public final HSSFWorkbook getXLS(final Measure m, final List<String> allQDMs,
			final QualityDataSetDAO qualityDataSetDAO,
			final ListObjectDAO listObjectDAO, final List<String> supplementalQDMS,
			final List<ValueSet> ValueSets) {
		ArrayList<ValueSet> qdmRefId = new ArrayList<ValueSet>();
		ArrayList<ValueSet> supplementRefId = new ArrayList<ValueSet>();
		for (String qdmId:allQDMs) {
			ValueSet ValueSet = findInList(qdmId, ValueSets);
			qdmRefId.add(ValueSet);
		}
		for (String supplRefId:supplementalQDMS) {
			ValueSet ValueSet = findInList(supplRefId, ValueSets);
			supplementRefId.add(ValueSet);
		}
		
		HSSFWorkbook wkbk = new HSSFWorkbook();
		createMetaData(wkbk);
		// addDisclaimer(wkbk);
		qdmOIDs.clear();
		//adding measure value set
		String sheetName = stripInvalidChars(m.getaBBRName());
		addVsacValueSetWorkSheet(sheetName, getNAME_STRINGS() , m , wkbk, qdmRefId, ValueSets);
		qdmOIDs.clear();
		//adding Supplemental Value Sets
		addVsacValueSetWorkSheet("Supplemental Value Sets", svsNameString, m , wkbk, supplementRefId , ValueSets);
		return wkbk;
	}
	
	/** Adds the mat.vsac value set work sheet.
	 * 
	 * @param sheetName -String.
	 * @param names - String Array.
	 * @param m - Measure.
	 * @param wkbk - HSSFWorkbook.
	 * @param supplementalQDMS - List of String.
	 * @param ValueSetList - List of ValueSet.
	 * 
	 *        * */
	public final void addVsacValueSetWorkSheet(final String sheetName, final String[] names, final Measure m,
			final HSSFWorkbook wkbk, final List<ValueSet> supplementalQDMS,
			final List<ValueSet> ValueSetList) {
		
		HSSFCellStyle style = wkbk.createCellStyle();
		HSSFSheet wkst = createSheet(wkbk, style, sheetName);
		
		createHeaderRow(wkst, getHEADER_STRINGS(), names,  0, style);
		for (ValueSet loFamily: supplementalQDMS) {
			cacheXLSRow(loFamily);
		}
		writeRowCache(wkst);
		rowCache.clear();
		sizeColumns(wkst);
	}
	
	/** Cache xls row.
	 * 
	 * @param lo - ValueSet Object
	 * 
	 *        * */
	
	@Override
	protected final void cacheXLSRow(final ValueSet lo) {
		
		if (lo == null) {
			return;
		}
		// Expansion Profile is different from version. There value could be same.
		// so v and ex are used for version and expansion profile are used to differentiate.
		String OIDAndVersionOrExpansionProfile = lo.getID() + ":v:" + lo.getVersion();
		if (lo.getExpansionProfile() != null) {
			OIDAndVersionOrExpansionProfile = lo.getID() + ":ex:" + lo.getExpansionProfile();
		}
		if (!qdmOIDs.contains(OIDAndVersionOrExpansionProfile)) {
			qdmOIDs.add(OIDAndVersionOrExpansionProfile);
			processXLSRow(lo);
		}
		
	}
	
	/** Find in list.
	 * 
	 * @param id - String qdm Id.
	 * @param ValueSets - List of ValueSet.
	 * @return ValueSet. */
	private ValueSet findInList(final String id , final List<ValueSet> ValueSets) {
		ValueSet result = null;
		if (ValueSets != null) {
			for (ValueSet valueSet : ValueSets) {
				if (valueSet.getQdmId().equalsIgnoreCase(id)) {
					result = valueSet;
					break;
				}
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.impl.XLSGenerator#cacheXLSRow(mat.model.ListObject, mat.dao.ListObjectDAO, java.sql.Timestamp)
	 */
	@Override
	protected final void cacheXLSRow(ListObject lo, final ListObjectDAO listObjectDAO, final Timestamp vsPackageDate){
		//Make sure we have the most recent non-draft value set from that OID that is before the timestamp
		/*ListObject vsTobeExported = listObjectDAO.findMostRecentValueSet(lo, vsPackageDate);
		if (vsTobeExported == null) {
			return;
		}
		lo = vsTobeExported;
		if (StringUtils.isNotBlank(DateUtility.convertDateToString(lo.getLastModified()))) {
			if (!qdmOIDs.contains(lo.getOid()) && !"22".equals(lo.getCategory().getId())) {
				qdmOIDs.add(lo.getOid());
				processXLSRow(lo, listObjectDAO, vsPackageDate);
			}
		}*/
	}
}
