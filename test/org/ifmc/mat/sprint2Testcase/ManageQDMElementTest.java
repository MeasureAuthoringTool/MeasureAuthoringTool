package org.ifmc.mat.sprint2Testcase;

import java.util.List;
import java.util.UUID;

import junit.framework.AssertionFailedError;

import org.ifmc.mat.DTO.DataTypeDTO;
import org.ifmc.mat.client.codelist.ManageCodeListDetailModel;
import org.ifmc.mat.client.codelist.service.SaveUpdateCodeListResult;
import org.ifmc.mat.dao.ListObjectDAO;
import org.ifmc.mat.dao.PropertyOperator;
import org.ifmc.mat.dao.QualityDataSetDAO;
import org.ifmc.mat.dao.SpringInitializationTest;
import org.ifmc.mat.dao.UserDAO;
import org.ifmc.mat.dao.clause.MeasureDAO;
import org.ifmc.mat.dao.clause.MeasureSetDAO;
import org.ifmc.mat.dao.search.CriteriaQuery;
import org.ifmc.mat.dao.search.SearchCriteria;
import org.ifmc.mat.model.CodeListSearchDTO;
import org.ifmc.mat.model.ListObject;
import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.model.SecurityRole;
import org.ifmc.mat.model.User;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.model.clause.MeasureSet;
import org.ifmc.mat.server.exception.ExcelParsingException;
import org.ifmc.mat.server.service.CodeListNotUniqueException;
import org.ifmc.mat.server.service.CodeListOidNotUniqueException;
import org.ifmc.mat.server.service.impl.ManageCodeListServiceImpl;
import org.ifmc.mat.sprint1Testcase.TestCaseUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class ManageQDMElementTest extends SpringInitializationTest {

	@Autowired
	protected ListObjectDAO listObjectDAO;

	@Autowired
	protected UserDAO userDAO;

	@Autowired
	protected QualityDataSetDAO qualityDataSetDAO;

	@Autowired
	protected MeasureDAO measureDAO;
	
	@Autowired
	protected MeasureSetDAO measureSetDAO;

	@Autowired
	protected ManageCodeListServiceImpl codeListService;
	
	
	private String category = "1";
	private TestCaseUtil tcu = new TestCaseUtil();

	@Test
	@Rollback
	public void testCreateQDM() {
		MeasureSet ms = createAndSaveMeasureSet();
		Measure measure = createAndSaveMeasure(ms);
		SaveUpdateCodeListResult clResult = createCodeList();
		SaveUpdateCodeListResult qdsResult = null;
		if(clResult != null){
			qdsResult = createAndSaveQDM(clResult, measure);
		}
		tearDownArtifacts(measure, clResult, qdsResult);
		
	}

	private void tearDownArtifacts(Measure measure, SaveUpdateCodeListResult clResult, SaveUpdateCodeListResult qdsResult) {
		if(clResult != null){
			deleteQDS(qdsResult,measure);
			deleteCodeList(clResult);
		}
		deleteMeasure(measure);
	}

	private void deleteMeasure(Measure measure) {
		measure = measureDAO.find(measure.getId());

		if (measure == null) {
			throw new AssertionFailedError("Error in retrieving the measure object prior to delete.");
		}

		measureDAO.delete(measure);
	}

	private void deleteCodeList(SaveUpdateCodeListResult clResult) {
		ListObject listObject = listObjectDAO.find(clResult.getId());

		if (listObject == null) {
			throw new AssertionFailedError("Error in retrieving the Code list object prior to delete.");
		}
		listObjectDAO.delete(listObject);
	}

	private void deleteQDS(SaveUpdateCodeListResult qdsResult, Measure measure) {
		SearchCriteria criteria = new SearchCriteria("measureId.id", measure.getId(), PropertyOperator.EQ, null);
		CriteriaQuery query = new CriteriaQuery(criteria);
		List<QualityDataSet> qds = (List<QualityDataSet>)qualityDataSetDAO.find(query);
		if (qds == null || qds.isEmpty()) {
			throw new AssertionFailedError("Error in retrieving the QDM Element");
		}
		qualityDataSetDAO.delete(qds.get(0));
	}

	private MeasureSet createAndSaveMeasureSet(){
		MeasureSet ms = new MeasureSet();
		ms.setId(UUID.randomUUID().toString());
		measureSetDAO.save(ms);
		return ms;
	}
	private Measure createAndSaveMeasure(MeasureSet ms){
		//Create and save a measure to database.
		Measure measure = tcu.createMeasure();
		measure.setOwner(getUser());
		measure.setMeasureSet(ms);
		measureDAO.saveMeasure(measure);
		return measure;
	}

	private SaveUpdateCodeListResult createCodeList(){

		SaveUpdateCodeListResult result = null;
		try {
			//Create and save a code list to database.
			ManageCodeListDetailModel model = tcu.createManageCodeListDetailModel();
			result = codeListService.saveorUpdateCodeList(model);
		} catch (CodeListNotUniqueException e) {
			throw new AssertionFailedError("Duplicate LIST_OBJECT values are not allowed.");
		} catch (CodeListOidNotUniqueException e) {
			throw new AssertionFailedError("Duplicate LIST_OBJECT.OID values are not allowed.");
		} catch (ExcelParsingException e) {
			throw new AssertionFailedError("Could not parse excel sheet.");
		} catch(Exception e){
			throw new AssertionFailedError("An Exception was thrown: \r\n"+e.getMessage());
		}finally{
			return result;
		}
	}

	private SaveUpdateCodeListResult createAndSaveQDM(SaveUpdateCodeListResult result, Measure measure){
		//Create a QDM element by using the code list service.
		List<DataTypeDTO> datatypes = (List<DataTypeDTO>) getCodeListService().getQDSDataTypeForCategory(category);
		if (!datatypes.isEmpty()) {
			CodeListSearchDTO clDTO = new CodeListSearchDTO();
			clDTO.setId(result.getId());
			SaveUpdateCodeListResult qdsResult = getCodeListService().saveQDStoMeasure(measure.getId(), datatypes.get(0).getId(), clDTO,false);
			return qdsResult;
		}
		return null;
	}

	public User getUser(){
		List<User> users = userDAO.find();
		for(User u : users)
			if(u.getSecurityRole().getId() != SecurityRole.ADMIN_ROLE_ID)
				return u;
		return null;
	}
}
