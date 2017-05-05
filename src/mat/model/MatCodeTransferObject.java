package mat.model;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.cql.CQLCode;

public class MatCodeTransferObject implements IsSerializable , BaseModel{

	List<CQLCode> codeList;
	
	CQLCode cqlCode;

	String measureId;
	
	public CQLCode getCqlCode() {
		return cqlCode;
	}

	public List<CQLCode> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<CQLCode> codeList) {
		this.codeList = codeList;
	}

	public void setCqlCode(CQLCode cqlCode) {
		this.cqlCode = cqlCode;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	@Override
	public void scrubForMarkUp() {
		
	}
}
