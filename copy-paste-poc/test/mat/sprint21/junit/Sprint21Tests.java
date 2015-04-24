package mat.sprint21.junit;

import java.util.ArrayList;

import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.shared.MatContext;
import mat.dao.ListObjectDAO;
import mat.dao.SpringInitializationTest;
import mat.dao.clause.AttributeDetailsDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.DataType;
import mat.model.ListObject;
import mat.model.ListObjectLT;
import mat.model.clause.AttributeDetails;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.QDSAttributes;
import mat.server.service.impl.SimpleEMeasureServiceImpl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class Sprint21Tests extends SpringInitializationTest{
	
	@Test
	public void S21_US143_DataType_Test() {
		String dataTypeName = "Encounter";
		DataType dt = getService().getDataTypeDAO().findByDataTypeName(dataTypeName);
		assertNull(dt);
	}
	
	@Test
	public void S21_US144_DataType_Test() {
		String attrName = "discharge status";
		String dataTypeName = "Encounter, Performed";
		
		DataType dt = getService().getDataTypeDAO().findByDataTypeName(dataTypeName);
		assertNotNull(dt);
		
		QDSAttributesDAO qadao = getService().getqDSAttributesDAO();
		QDSAttributes attr = qadao.findByNameAndDataType(attrName, dt.getId());
		assertNotNull(attr);
	}
	
	@Test
	public void S21_US149_ListBoxMVP_Test(){
		String testString = "Value-Set-Name";
		String suffix = " - 1.22.333.4444.55555";
		MatContext mc = MatContext.get();
		String sansOid = mc.stripOffOID(testString+suffix);
		assertEquals(testString, sansOid);
		
		testString = "Belladonna alkaloids : Atropine-difenoxin";
		suffix = "-2.16.840.1.113883.3.67.1.101.12.592";
		sansOid = mc.stripOffOID(testString+suffix);
		assertEquals(testString, sansOid);
	}
	
	@Test
	public void S21_US146_GVS_Unique_Check_Test(){
		ListObjectDAO listObjectDAO = getService().getListObjectDAO();
		ArrayList<ManageCodeListDetailModel> models = new ArrayList<ManageCodeListDetailModel>();
		ArrayList<String> userIds = new ArrayList<String>();
		for(ListObject lo : listObjectDAO.find()){
			if(lo.getCodeSystem().getAbbreviation().equalsIgnoreCase("GRP")){
				ManageCodeListDetailModel model = new ManageCodeListDetailModel();
				String userId = lo.getCodeListDeveloper();
				String name = lo.getName();
				String steward = lo.getSteward().getId();
				String category = lo.getCategory().getId();
				String codeSystem = lo.getCodeSystem().getId();
				String stewardOth = lo.getStewardOther();
				String oid = lo.getOid();
				
				model.setName(name);
				model.setSteward(steward);
				model.setCategory(category);
				model.setCodeSystem(codeSystem);
				model.setStewardOther(stewardOth);
				model.setOid(oid);

				models.add(model);
				userIds.add(userId);
			}
		}
		
		for(int i = 0; i < models.size(); i++){
			ManageCodeListDetailModel model = models.get(i);
			String userId = userIds.get(i);
			listObjectDAO.getListObject(model, userId);
		}
		
	}
	
	@Test
	public void S21_US138_Attributes_Test() {
		
		String dataTypeId = "";
		
		QDSAttributesDAO qadao = getService().getqDSAttributesDAO();
		AttributeDetailsDAO addao = getService().getAttributeDetailsDAO();
		
		String attrName = "Recorder - Device";
		QDSAttributes attr = qadao.findByNameAndDataType(attrName, dataTypeId);
		assertNull(attr);
		AttributeDetails attrDet = addao.findByName(attrName.toLowerCase());
		assertNull(attrDet);
		
		attrName = "Recorder - Informant";
		attr = qadao.findByNameAndDataType(attrName, dataTypeId);
		assertNull(attr);
		attrDet = addao.findByName(attrName.toLowerCase());
		assertNull(attrDet);
		
		attrName = "Recorder";
		attr = qadao.findByNameAndDataType(attrName, dataTypeId);
		assertNotNull(attr);
		attrDet = addao.findByName(attrName.toLowerCase());
		assertNotNull(attrDet);
	}
	
	@Test
	public void S21_US139_Attributes_Test() {
		
		String dataTypeId = "";
		
		QDSAttributesDAO qadao = getService().getqDSAttributesDAO();
		AttributeDetailsDAO addao = getService().getAttributeDetailsDAO();
		
		String attrName = "Source - Device";
		QDSAttributes attr = qadao.findByNameAndDataType(attrName, dataTypeId);
		assertNull(attr);
		AttributeDetails attrDet = addao.findByName(attrName.toLowerCase());
		assertNull(attrDet);
		
		attrName = "Source - Informant";
		attr = qadao.findByNameAndDataType(attrName, dataTypeId);
		assertNull(attr);
		attrDet = addao.findByName(attrName.toLowerCase());
		assertNull(attrDet);
		
		attrName = "Source";
		attr = qadao.findByNameAndDataType(attrName, dataTypeId);
		assertNotNull(attr);
		attrDet = addao.findByName(attrName.toLowerCase());
		assertNotNull(attrDet);
	}

	@Test
	public void S21_US151_ListObject_Test() {
		//US151 requires that we get rid of the objectStatus field from classes: 
		//ListObject, ListObjectLT, and ManageCodeListDetailModel
		
		Class clazz = ListObject.class;
		boolean noSuchField = false;
		try {
			clazz.getField("objectStatus");
			noSuchField = false;
		} catch (SecurityException e) {
			noSuchField = false;
		} catch (NoSuchFieldException e) {
			noSuchField = true;
		}
		assertTrue(noSuchField);
		
		clazz = ListObjectLT.class;
		noSuchField = false;
		try {
			clazz.getField("objectStatus");
			noSuchField = false;
		} catch (SecurityException e) {
			noSuchField = false;
		} catch (NoSuchFieldException e) {
			noSuchField = true;
		}
		assertTrue(noSuchField);
		
		clazz = ManageCodeListDetailModel.class;
		noSuchField = false;
		try {
			clazz.getField("objectStatus");
			noSuchField = false;
		} catch (SecurityException e) {
			noSuchField = false;
		} catch (NoSuchFieldException e) {
			noSuchField = true;
		}
		assertTrue(noSuchField);
	}
	
	@Autowired
	private SimpleEMeasureServiceImpl eMeasureService;
	
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

		
		private final String[] NAME_STRINGS = {
				"ValueSetDeveloper",
				"ValueSetOID",
				"LastModified",
				"ValueSetName",
				"QDMCategory",
				"CodeSystem",
				"CodeSystemVersion",
				"Code",
				"Descriptor"};
	
	@Test
	public void S21_US136_MeasureExportXLS_Test() throws Exception {
		
		MeasureExportDAO measureExportDAO = getService().getMeasureExportDAO();
		
		MeasureExport me = measureExportDAO.find().get(0);
		Measure m = me.getMeasure();
		String mid = m.getId();
		
	//	eMeasureService.createSimpleXML(mid);
		HSSFWorkbook wkbk = eMeasureService.getWkbk();
		int namecount = wkbk.getNumberOfNames();
		assertEquals(18, namecount);
		
		for(String s : SVS_NAME_STRINGS){
			assertNotNull(wkbk.getName(s));
		}
		
		for(String s : NAME_STRINGS){
			assertNotNull(wkbk.getName(s));
		}
	}
	
}
