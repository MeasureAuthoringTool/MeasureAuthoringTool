package mat.server.service.impl;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mat.dao.ListObjectDAO;
import mat.dao.QualityDataSetDAO;
import mat.model.ListObject;
import mat.model.clause.Measure;
import mat.shared.DateUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CodeListXLSGenerator extends XLSGenerator{
	 
	private final String[] SVS_NAME_STRINGS = {
		"ValueSetDeveloper_SVS",
		"ValueSetOID_SVS",
		"LastModified_SVS",
		"ValueSetName_SVS",
		"QDMCategory_SVS",
		"CodeSystem_SVS",
		"CodeSystemVersion_SVS",
		"Code_SVS",
		"Descriptor_SVS"};

	private List<String> qdmOIDs = new ArrayList<String>();
	
	public void addValueSetWorkSheet(String sheetName, String[] names, Measure m, HSSFWorkbook wkbk, List<ListObject> supplementalQDMS, ListObjectDAO listObjectDAO){

		HSSFCellStyle style = wkbk.createCellStyle();
		HSSFSheet wkst = createSheet(wkbk, style, sheetName);
		
		createHeaderRow(wkst, HEADER_STRINGS, names,  0, style);
//		SortUtility su = new SortUtility();
	//	SortedSet<ListObject> sortedListObjects = su.sortQDMsToListObjects(supplementalQDMS);//This sortedListObjects is the ListObjects used in the Measure Logic.
		Timestamp vsPackageDate = m.getValueSetDate();
		for(ListObject loFamily: supplementalQDMS){	
			cacheXLSRow(loFamily, listObjectDAO, vsPackageDate);
		}
		writeRowCache(wkst);
		rowCache.clear();
		sizeColumns(wkst);
	}
	
	public HSSFWorkbook getXLS(Measure m, List<String> allQDMs, QualityDataSetDAO qualityDataSetDAO,ListObjectDAO listObjectDAO,List<String> supplementalQDMS){
		ArrayList<ListObject> qdmRefId = new ArrayList<ListObject>();
		ArrayList<ListObject> supplementRefId = new ArrayList<ListObject>();
		for(String qdmId:allQDMs){
			ListObject listObject = listObjectDAO.find(qdmId);
			qdmRefId.add(listObject);
			
		}
		
		for(String supplRefId:supplementalQDMS){
			ListObject listObject = listObjectDAO.find(supplRefId);
			supplementRefId.add(listObject);
		}
		HSSFWorkbook wkbk = new HSSFWorkbook();
		createMetaData(wkbk);
		addDisclaimer(wkbk);
		//adding measure value set
		String sheetName = stripInvalidChars(m.getaBBRName());
		addValueSetWorkSheet(sheetName, NAME_STRINGS,m,wkbk,qdmRefId,listObjectDAO);
		qdmOIDs.clear();
		//adding Supplemental Value Sets
	    addValueSetWorkSheet("Supplemental Value Sets", SVS_NAME_STRINGS,m,wkbk,supplementRefId,listObjectDAO);
		return wkbk;
	}
	
	protected void cacheXLSRow( ListObject lo, ListObjectDAO listObjectDAO, Timestamp vsPackageDate){
		//Make sure we have the most recent non-draft value set from that OID that is before the timestamp
		ListObject vsTobeExported = listObjectDAO.findMostRecentValueSet(lo, vsPackageDate); 
		if(vsTobeExported == null)
			return;
		lo = vsTobeExported;
		if(StringUtils.isNotBlank(DateUtility.convertDateToString(lo.getLastModified()))){
			if(!qdmOIDs.contains(lo.getOid()) && !"22".equals(lo.getCategory().getId())){
				qdmOIDs.add(lo.getOid());
				processXLSRow(lo, listObjectDAO, vsPackageDate);
			}
		}
	}
	
}
