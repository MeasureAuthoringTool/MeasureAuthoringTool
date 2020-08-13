package mat.model;

import mat.model.clause.Measure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "QUALITY_DATA_MODEL")
public class QualityDataSet {
	
	private String id;
	
	private DataType dataType;
	
	private ListObject listObject;
	
	private Measure measureId;
	
	private String version;
	
	private String oid;
	
	private String occurrence;
	
	private boolean suppDataElement;
	
	@Column(name = "VERSION", nullable = false, length = 32)
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_ID", nullable = false)
	public Measure getMeasureId() {
		return measureId;
	}
	
	public void setMeasureId(Measure measureId) {
		this.measureId = measureId;
	}
	
	@Id
	@Column(name = "QUALITY_DATA_MODEL_ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DATA_TYPE_ID", nullable = false)
	public DataType getDataType() {
		if(dataType==null)
			dataType = new DataType();
		return dataType;
	}
	
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LIST_OBJECT_ID", nullable = false)
	public ListObject getListObject() {
		if(listObject==null)
			listObject = new ListObject();
		return listObject;
	}
	
	public void setListObject(ListObject listObject) {
		this.listObject = listObject;
	}
	
	@Column(name = "OID")
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}

	@Column(name = "OCCURRENCE", length = 200)
	public String getOccurrence() {
		return occurrence;
	}
	
	public void setOccurrence(String occurrence) {
		this.occurrence = occurrence;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object temp) {

		if (temp instanceof QualityDataSet) {
			QualityDataSet tempQDS = (QualityDataSet) temp;
			if(tempQDS.getId() != null && tempQDS.getId().equals(getId())) return true;
			if(tempQDS.getOid() != null && tempQDS.getOid().equals(getOid())) return true;	
		}
		return false;
	}
	
	@Column(name = "IS_SUPP_DATA_ELEMENT", nullable = false)
	public boolean isSuppDataElement() {
		return suppDataElement;
	}
	
	public void setSuppDataElement(boolean suppDataElement) {
		this.suppDataElement = suppDataElement;
	}
	
	
}
