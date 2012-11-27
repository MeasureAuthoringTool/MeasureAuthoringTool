package org.ifmc.mat.client.clause;

import org.ifmc.mat.model.CodeList;
import org.ifmc.mat.model.DataType;
import org.ifmc.mat.model.clause.Measure;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QualityDataSetModel implements IsSerializable{
	private String id;
	private DataType dataType;
	private CodeList codeList;
	private Measure measureId;
	private String version;
	private String occurrence;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Measure getMeasureId() {
		return measureId;
	}
	public void setMeasureId(Measure measureId) {
		this.measureId = measureId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DataType getDataType() {
		return dataType;
	}
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	public CodeList getCodeList() {
		return codeList;
	}
	public void setCodeList(CodeList codeList) {
		this.codeList = codeList;
	}
	public String getOccurrence() {
		return occurrence;
	}
	public void setOccurrence(String occurrence) {
		this.occurrence = occurrence;
	}

}
