package mat.sprint23.junit;

import java.util.List;

import mat.client.codelist.ManageValueSetSearchModel;
import mat.client.codelist.ManageValueSetSearchModel.Result;
import mat.client.measure.ManageMeasureDetailModel;
import mat.dao.ListObjectDAO;
import mat.dao.SpringInitializationTest;
import mat.dao.clause.MeasureDAO;
import mat.model.Code;
import mat.model.ListObject;
import mat.model.ListObjectLT;
import mat.model.User;
import mat.model.clause.Clause;
import mat.model.clause.Measure;
import mat.server.MeasureLibraryServiceImpl;
import mat.server.service.SimpleEMeasureService.ExportResult;
import mat.server.service.impl.ManageCodeListServiceImpl;
import mat.server.service.impl.SimpleEMeasureServiceImpl;
import mat.server.service.impl.ValueSetXLSGenerator;
import mat.shared.DateUtility;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;

public class Sprint23Tests extends SpringInitializationTest{
private MeasureLibraryServiceImpl measureLibService = new MeasureLibraryServiceImpl();
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	@Autowired
	protected MeasureDAO measureDAO;
	
	private Measure existingMeasure ;
	
	
	private ManageMeasureDetailModel measModel = new ManageMeasureDetailModel();
	
	@Before
	public void setUp(){
		measureLibService.setContext(applicationContext);
	}

	//Start US194
	@Test
	@Rollback
	public void S23_US194_DeleteMeasurePackageGroupings_Test(){
		List<Measure> listofMeasures = measureDAO.find();
		existingMeasure = listofMeasures.get(0);
		extractMeasureToModel();
		measureLibService.save(measModel);
	}
	
	private String changeMeasureScoring(){
		if(existingMeasure.getMeasureScoring().equalsIgnoreCase("Continuous Variable")){
			return "Proportion";
		}else if(existingMeasure.getMeasureScoring().equalsIgnoreCase("Proportion")){
			return "Ratio";
		}else if(existingMeasure.getMeasureScoring().equalsIgnoreCase("Ratio")){
			return "Proportion";
		}
		return "Proportion";
	}
	
	private void extractMeasureToModel(){
		measModel.setId(existingMeasure.getId());
		measModel.setName(existingMeasure.getaBBRName());
		measModel.setMeasScoring(changeMeasureScoring());
		measModel.setDraft(existingMeasure.isDraft());
		measModel.setVersionNumber(existingMeasure.getVersion());
		measModel.setFinalizedDate(DateUtility.convertDateToString(existingMeasure.getFinalizedDate()));
		measModel.setValueSetDate(DateUtility.convertDateToString(existingMeasure.getValueSetDate()));
	}
	//End US194
	
	//Start US191
	@Autowired
	private SimpleEMeasureServiceImpl eMeasureService;
	@Test
	@Rollback
	public void S23_US191_ExportValueSet_Test() throws Exception{
		List<ListObjectLT> loLTs = getService().getListObjectLTDAO().find();
		int i = 0;
		int ceil = 10;
		for(ListObjectLT loLT : loLTs){
			String id = loLT.getId();
			String name = loLT.getName();
			boolean containsInvalidCharacters = name.contains(":") || 
				name.contains("\\") || name.contains("/") || name.contains("?") ||
				name.contains("*") || name.contains("[") || name.contains("]");
			if(i <= ceil || containsInvalidCharacters){
				ExportResult result = eMeasureService.getValueSetXLS(id);
				assertNotNull(result.wkbkbarr);
			}
			
			i++;
		}
	}
	//End US191
	
	//Start US201
	@Test
	@Rollback
	public void S23_US201_ClauseDAO_Test() throws Exception{
		List<User> users = getService().getUserDAO().find();
		boolean testedSuperUser = false;
		for(User u : users){
			String userRole = u.getSecurityRole().getDescription();
			if(userRole.equalsIgnoreCase("Admin"))
				continue;
			else if(userRole.equalsIgnoreCase("Super user"))
				if(testedSuperUser)
					continue;
				else
					testedSuperUser = true;
			
			String measureOwnerId = u.getId();
			List<Clause> systemClauses = getService().getClauseDAO().findSystemClauses(measureOwnerId, userRole, applicationContext);
		}
		
	}
	//End US201
	
	//Start US205
	@Test
	@Rollback
	public void S23_US205_ExportValueSet_Test() throws Exception{
		List<ListObject> los = getService().getListObjectDAO().find();
		for(ListObject lo : los){
			if(!lo.isDraft()){
				String desc256 = "abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcd defg abcdef";
				Code c = new Code();
				c.setCode("1234");
				c.setDescription(desc256);
				lo.getCodes().add(c);
				String id = lo.getId();
				String name = lo.getName();
				
				ExportResult result = eMeasureService.getValueSetXLS(id);
				assertNotNull(result.wkbkbarr);
				
				ValueSetXLSGenerator vsxg = new ValueSetXLSGenerator();
				HSSFWorkbook wkbk = vsxg.getXLS(null, lo);
				assertEquals(2, wkbk.getNumberOfSheets());
				HSSFSheet sheet = wkbk.getSheetAt(1);
				assertNotSame("Sheet1", sheet.getSheetName());
				assertNotSame("Sheet2", sheet.getSheetName());
				break;
			}
		}
	}
	//End US205

	//Start US193
	@Autowired
	protected ManageCodeListServiceImpl codeListService;
	@Test
	@Rollback
	public void S23_US193_ValueSetClone_Test() throws Exception{
		ListObjectDAO listObjectDAO = getService().getListObjectDAO();
		List<ListObject> los = listObjectDAO.find();
		String cid = null;
		String cname = null;
		for(ListObject lo : los){
			String loid = lo.getId();
			ManageValueSetSearchModel model = codeListService.createClone(loid);
			assertNotNull(model.getData());
			assertFalse(model.getData().isEmpty());
			Result r = model.getData().get(0);
			cid = r.getId();
			assertNotNull(cid);
			break;
		}
		ListObject clone = listObjectDAO.find(cid);
		assertNotNull(clone);
		cname = clone.getName();
		assertTrue(cname.contains(" Clone "));
	}
	//End US193
}
