package org.ifmc.mat.dao;

import java.util.HashSet;
import java.util.List;

import org.ifmc.mat.model.Category;
import org.ifmc.mat.model.Code;
import org.ifmc.mat.model.CodeList;
import org.ifmc.mat.model.CodeSystem;
import org.ifmc.mat.model.DataType;
import org.ifmc.mat.model.GroupedCodeList;
import org.ifmc.mat.model.ListObject;
import org.ifmc.mat.model.MeasureSteward;
import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.model.User;
import org.ifmc.mat.model.clause.Measure;


public class CodeDAOTestHelper {
	
	private Code createCode() {
		Code code = new Code();
		code.setCode("428.6");
		code.setDescription("Heart Failure");
		return code;	
	}
	
	public CodeList getCodeListForTestCreateCodeListWithoutCodes(Category c, CodeSystem cs, User u, MeasureSteward s) {
		CodeList codeList = new CodeList();
		codeList.setCategory(c);
		codeList.setCodeSystem(cs);
		codeList.setCodeSystemVersion("1.0");
		codeList.setComment("comment");
		codeList.setName("Code list without codes");
		codeList.setObjectOwner(u);
		codeList.setOid("test oid");
		codeList.setRationale("rationale");
		codeList.setSteward(s);
		codeList.setDraft(true);
		codeList.setLastModified(null);
		return codeList;
	}
	
	public CodeList getCodeListForTestCreateCodeListWithCodes(Category c, CodeSystem cs, User u, MeasureSteward s) {
		CodeList codeList = new CodeList();
		codeList.setCategory(c);
		codeList.setCodeSystem(cs);
		codeList.setCodeSystemVersion("2.0");
		codeList.setComment("comment");
		codeList.setName("Code list with codes");
		codeList.setObjectOwner(u);
		codeList.setOid("test oid");
		codeList.setRationale("rationale");
		codeList.setSteward(s);
		codeList.setDraft(true);
		codeList.setLastModified(null);
		
		HashSet<Code> codes = new HashSet<Code>();
		codes.add(createCode());
		codeList.setCodes(codes);
		return codeList;
	}
	
	
	public ListObject getListObjectForTestCreateGroupedCodeList(Category c, CodeSystem cs, User u, MeasureSteward s, List<CodeList> cls) {
		ListObject listObject = new ListObject();
		listObject.setComment("comment");
		listObject.setCategory(c);
		listObject.setName("Group Code list name 1111111");
		listObject.setObjectOwner(u);
		listObject.setOid("test oid");
		listObject.setRationale("rationale");
		listObject.setSteward(s);
		listObject.setDraft(true);
		listObject.setLastModified(null);
		listObject.setCodeSystem(cs);
		listObject.setCodeSystemVersion("2.0");
		
		HashSet<GroupedCodeList> codes = new HashSet<GroupedCodeList>();
		for(CodeList cl : cls) {
			GroupedCodeList gcl = new GroupedCodeList();
			gcl.setDescription("Grouped " + cl.getName());
			gcl.setCodeList(cl);
			codes.add(gcl);
		}
		listObject.setCodesLists(codes);
		
		return listObject;
	}
	
	public QualityDataSet getQDSForTestCreateQDS(DataType dt, ListObject lo, Measure m) {
		QualityDataSet qds = new QualityDataSet();
		qds.setDataType(dt);
		qds.setListObject(lo);
		qds.setMeasureId(m);
		qds.setVersion("1.0");
		qds.setOid("1");
		return qds;
	}
	
	
}
