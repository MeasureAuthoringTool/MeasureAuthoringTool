package mat.server.service.impl;

import mat.dao.ListObjectDAO;
import mat.dao.QualityDataSetDAO;
import mat.model.ListObject;
import mat.model.MatValueSet;
import mat.model.clause.Measure;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
	 * @param matValueSets - List of MatValueSet.
	 * @return HSSFWorkbook. * */
	public final HSSFWorkbook getXLS(final Measure m, final List<String> allQDMs,
			final QualityDataSetDAO qualityDataSetDAO,
			final ListObjectDAO listObjectDAO, final List<String> supplementalQDMS,
			final List<MatValueSet> matValueSets) {
		ArrayList<MatValueSet> qdmRefId = new ArrayList<MatValueSet>();
		ArrayList<MatValueSet> supplementRefId = new ArrayList<MatValueSet>();
		for (String qdmId:allQDMs) {
			MatValueSet matValueSet = findInList(qdmId, matValueSets);
			qdmRefId.add(matValueSet);
		}
		for (String supplRefId:supplementalQDMS) {
			MatValueSet matValueSet = findInList(supplRefId, matValueSets);
			supplementRefId.add(matValueSet);
		}
		
		HSSFWorkbook wkbk = new HSSFWorkbook();
		createMetaData(wkbk);
		// addDisclaimer(wkbk);
		qdmOIDs.clear();
		//adding measure value set
		String sheetName = stripInvalidChars(m.getaBBRName());
		addVsacValueSetWorkSheet(sheetName, getNAME_STRINGS() , m , wkbk, qdmRefId, matValueSets);
		qdmOIDs.clear();
		//adding Supplemental Value Sets
		addVsacValueSetWorkSheet("Supplemental Value Sets", svsNameString, m , wkbk, supplementRefId , matValueSets);
		return wkbk;
	}
	
	/** Adds the vsac value set work sheet.
	 * 
	 * @param sheetName -String.
	 * @param names - String Array.
	 * @param m - Measure.
	 * @param wkbk - HSSFWorkbook.
	 * @param supplementalQDMS - List of String.
	 * @param matValueSetList - List of MatValueSet.
	 * 
	 *        * */
	public final void addVsacValueSetWorkSheet(final String sheetName, final String[] names, final Measure m,
			final HSSFWorkbook wkbk, final List<MatValueSet> supplementalQDMS,
			final List<MatValueSet> matValueSetList) {
		
		HSSFCellStyle style = wkbk.createCellStyle();
		HSSFSheet wkst = createSheet(wkbk, style, sheetName);
		
		createHeaderRow(wkst, getHEADER_STRINGS(), names,  0, style);
		for (MatValueSet loFamily: supplementalQDMS) {
			cacheXLSRow(loFamily);
		}
		writeRowCache(wkst);
		rowCache.clear();
		sizeColumns(wkst);
	}
	
	/** Cache xls row.
	 * 
	 * @param lo - MatValueSet Object
	 * 
	 *        * */
	
	@Override
	protected final void cacheXLSRow(final MatValueSet lo) {
		
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
	 * @param matValueSets - List of MatValueSet.
	 * @return MatValueSet. */
	private MatValueSet findInList(final String id , final List<MatValueSet> matValueSets) {
		MatValueSet result = null;
		if (matValueSets != null) {
			for (MatValueSet valueSet : matValueSets) {
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
