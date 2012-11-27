package org.ifmc.mat.model;

import org.ifmc.mat.model.clause.Measure;

public class QualityDataSet {
	private String id;
	private DataType dataType;
	
	private ListObject listObject;
	private Measure measureId;
	private String version;
	private String oid;
	private String occurrence;
	private boolean suppDataElement;
	
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
		if(dataType==null)
			dataType = new DataType();
		return dataType;
	}
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	public ListObject getListObject() {
		if(listObject==null)
			listObject = new ListObject();
		return listObject;
	}
	public void setListObject(ListObject listObject) {
		this.listObject = listObject;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOccurrence() {
		return occurrence;
	}
	public void setOccurrence(String occurrence) {
		this.occurrence = occurrence;
	}
	@Override
	public boolean equals(Object temp) {

		if (temp instanceof QualityDataSet) {
			QualityDataSet tempQDS = (QualityDataSet) temp;
			if(tempQDS.getId() != null && tempQDS.getId().equals(getId())) return true;
			if(tempQDS.getOid() != null && tempQDS.getOid().equals(getOid())) return true;	
		}
		return false;
	}
	public boolean isSuppDataElement() {
		return suppDataElement;
	}
	public void setSuppDataElement(boolean suppDataElement) {
		this.suppDataElement = suppDataElement;
	}
	
	
}
