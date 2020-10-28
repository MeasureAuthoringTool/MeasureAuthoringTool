package mat.client.clause;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.CodeList;
import mat.model.DataType;
import mat.model.clause.Measure;

/**
 * The Class QualityDataSetModel.
 */
public class QualityDataSetModel implements IsSerializable{
	
	/** The id. */
	private String id;
	
	/** The data type. */
	private DataType dataType;
	
	/** The code list. */
	private CodeList codeList;
	
	/** The measure id. */
	private Measure measureId;
	
	/** The version. */
	private String version;
	
	/** The occurrence. */
	private String occurrence;
	
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
	 * Gets the code list.
	 * 
	 * @return the code list
	 */
	public CodeList getCodeList() {
		return codeList;
	}
	
	/**
	 * Sets the code list.
	 * 
	 * @param codeList
	 *            the new code list
	 */
	public void setCodeList(CodeList codeList) {
		this.codeList = codeList;
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

}
