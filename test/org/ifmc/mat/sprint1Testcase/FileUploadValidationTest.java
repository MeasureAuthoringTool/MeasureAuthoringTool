package org.ifmc.mat.sprint1Testcase;

import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;

import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.dao.CodeListDAO;
import mat.dao.ListObjectDAO;
import mat.dao.StewardDAO;
import mat.model.Code;
import mat.model.MeasureSteward;
import mat.server.exception.ExcelParsingException;
import mat.server.service.impl.ManageCodeListServiceImpl;
import mat.shared.ConstantMessages;

import org.ifmc.mat.dao.SpringInitializationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class FileUploadValidationTest  extends SpringInitializationTest {
	
	
	
	@Autowired
	protected ManageCodeListServiceImpl codeListService;
	@Autowired
	protected CodeListDAO codeListDAO;
	@Autowired
	protected ListObjectDAO listObjectDAO;
	@Autowired
	protected StewardDAO stewardDAO;
	
	private ManageCodeListDetailModel model;
	private TestCaseUtil tcu = new TestCaseUtil();
	
	@org.junit.Before
	public void setUp() {
		System.out.println("\n\n Inserting new codeList to test duplicate codes. \n\n");
		model = createManageCodeListDetailModel("testforcodeList1", "1", "1", "1", "1", "12.45","good");
		model.setCodes(createImportCodes());
	}
	
	
	
	private SaveUpdateCodeListResult insertCodeList() throws Exception {
		SaveUpdateCodeListResult result = null;
		try {
			result = codeListService.saveorUpdateCodeList(model);
			} catch (Exception e) {
				e.printStackTrace();
				assertFalse("Exception while inserting a new CodeList",result.isSuccess());
			}
			return result;
	}
	
	
	
	@Test
	@Rollback
	//This is the test for duplicate codes Exists in the database for a given codeListID.
	public void duplicateCodeExistsTest() throws Exception{
		SaveUpdateCodeListResult result  = insertCodeList();
		if(result == null){
			 throw new AssertionFailedError("SaveUpdate result is empty.");
		}
		
		model.setID(result.getId());
		model.setExistingCodeList(true);
		model.setImportFlag(true);
		List<Code> newCodesList = createImportCodes();
//		model.setCodes(newCodesList);//This is overwriting what i had before.
		model.getCodes().addAll(newCodesList);// This is adding on top of it.
		SaveUpdateCodeListResult importCodesResult = new SaveUpdateCodeListResult();
		try{
			result = codeListService.saveorUpdateCodeList(model);
			if(result.isSuccess() && !result.isDuplicateExists()){
				System.out.println("IMPORT_SUCCESS_MSG");
			}else{
				if(result.isAllCodesDups())
					System.out.println("All "+ "DUPLICATE_CODES_MSG");
				else
					System.out.println("IMPORT_SUCCESS_MSG " + result.getDuplicateCount()+" "+ ConstantMessages.DUPLICATE_CODES_MSG);
				
			}
		}catch(ExcelParsingException e){
			importCodesResult.setCodeListName(model.getName());
			System.out.println(e.getErrorMessage());
			//System.out.println("---Test Result---");
			//System.out.println(ConstantMessages.DUPLICATE_EXISTS +" "+ importCodesResult.getCodeListName() + ".Please remove then try again.");
		}
		
	}
	
	private List<Code> createImportCodes(){
		ArrayList<Code> newcodesList = new ArrayList<Code>(); 
		
		Code code = new Code();
		code.setCode("123");
		code.setDescription("import test code");
		
		Code code1 = new Code();
		code1.setCode("111");
		code1.setDescription("import test code");
		
		newcodesList.add(code);
		newcodesList.add(code1);
		return newcodesList;
	}
	
	
	private ManageCodeListDetailModel createManageCodeListDetailModel(String name, String cat, String codeSys, String objStat, String codeSysVer, String oid, String rationale){
		ManageCodeListDetailModel model = tcu.createManageCodeListDetailModel("testforcodeList1", "1", "1", "1", "1", "12.45");
		MeasureSteward steward = stewardDAO.find().get(0);
		model.setSteward(steward.getId());
		return model;
	}
	
	
}
