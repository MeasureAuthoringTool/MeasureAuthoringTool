package mat.model;

import mat.model.clause.Measure;

/**
 * The Class QualityDataSet.
 */
public class QualityDataSet {
	
	/** The id. */
	private String id;
	
	/** The data type. */
	private DataType dataType;
	
	/** The list object. */
	private ListObject listObject;
	
	/** The measure id. */
	private Measure measureId;
	
	/** The version. */
	private String version;
	
	/** The oid. */
	private String oid;
	
	/** The occurrence. */
	private String occurrence;
	
	/** The supp data element. */
	private boolean suppDataElement;
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Gets the measure id.
	 * 
	 * @return the measure id
	 */
	public Measure getMeasureId() {
		return measureId;
	}
	
	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the new measure id
	 */
	public void setMeasureId(Measure measureId) {
		this.measureId = measureId;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the data type.
	 * 
	 * @return the data type
	 */
	public DataType getDataType() {
		if(dataType==null)
			dataType = new DataType();
		return dataType;
	}
	
	/**
	 * Sets the data type.
	 * 
	 * @param dataType
	 *            the new data type
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * Gets the list object.
	 * 
	 * @return the list object
	 */
	public ListObject getListObject() {
		if(listObject==null)
			listObject = new ListObject();
		return listObject;
	}
	
	/**
	 * Sets the list object.
	 * 
	 * @param listObject
	 *            the new list object
	 */
	public void setListObject(ListObject listObject) {
		this.listObject = listObject;
	}
	
	/**
	 * Gets the oid.
	 * 
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}
	
	/**
	 * Sets the oid.
	 * 
	 * @param oid
	 *            the new oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * Gets the occurrence.
	 * 
	 * @return the occurrence
	 */
	public String getOccurrence() {
		return occurrence;
	}
	
	/**
	 * Sets the occurrence.
	 * 
	 * @param occurrence
	 *            the new occurrence
	 */
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
	
	/**
	 * Checks if is supp data element.
	 * 
	 * @return true, if is supp data element
	 */
	public boolean isSuppDataElement() {
		return suppDataElement;
	}
	
	/**
	 * Sets the supp data element.
	 * 
	 * @param suppDataElement
	 *            the new supp data element
	 */
	public void setSuppDataElement(boolean suppDataElement) {
		this.suppDataElement = suppDataElement;
	}
	
	
}
