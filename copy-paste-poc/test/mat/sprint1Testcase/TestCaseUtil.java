package mat.sprint1Testcase;

import java.util.Date;
import java.util.UUID;
import mat.client.codelist.ManageCodeListDetailModel;
import mat.model.clause.Measure;

public class TestCaseUtil {
	
	
	public ManageCodeListDetailModel createManageCodeListDetailModel(String name, String cat, String codeSys, String objStat, String codeSysVer, String oid){
		ManageCodeListDetailModel model = new ManageCodeListDetailModel();
		model.setName(name);
		model.setCategory(cat);
		model.setCodeSystem(codeSys);
		model.setCodeSystemVersion(codeSysVer);
		model.setOid(oid);
		model.setRationale("N/A");
		model.setDraft(true);
		return model;
	}
	
	public ManageCodeListDetailModel createManageCodeListDetailModel(String name, String cat, String codeSys, String objStat, String codeSysVer){
		return createManageCodeListDetailModel(name, cat, codeSys, objStat, codeSysVer, getMockOid());
	}
	
	public ManageCodeListDetailModel createManageCodeListDetailModel(){
		return createManageCodeListDetailModel("testforcodelist", "1", "1", "1", "1", getMockOid());
	}
	
	public Measure createMeasure() {
		Measure measure = new Measure();
		//let's try to create a new measure object with mock Data
		String meaName = "TestCaseMeasureName_" + new Date().getTime();
		String meaDesc = "TestCaseMeasureDesc_" + new Date().getTime();
		measure = new Measure();
		/*measure.setClauses(new ArrayList<Clause>());*/
		measure.setaBBRName(meaName);
		measure.setDescription(meaDesc);
		measure.setVersion("22.33");
		/*measure.setMeasureStatus("pending");*/
		measure.setMeasureScoring("Ratio");
		measure.setDraft(true);
		return measure;
	}
	
	public String getMockOid(){
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", ".");
		uuid = uuid.replaceAll("a", "10");
		uuid = uuid.replaceAll("b", "11");
		uuid = uuid.replaceAll("c", "12");
		uuid = uuid.replaceAll("d", "13");
		uuid = uuid.replaceAll("e", "14");
		uuid = uuid.replaceAll("f", "15");
		
		return uuid;
	}
}
