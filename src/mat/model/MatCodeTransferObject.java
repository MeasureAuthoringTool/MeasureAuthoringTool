package mat.model;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.cql.CQLCode;

public class MatCodeTransferObject implements IsSerializable , BaseModel{

	List<CQLCode> codeList;
	
	CQLCode cqlCode;

	String id;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void scrubForMarkUp() {
		
	}
}
