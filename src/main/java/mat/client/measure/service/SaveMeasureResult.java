package mat.client.measure.service;

import java.util.List;
import mat.client.shared.GenericResult;
import mat.model.Author;
import mat.model.MeasureType;
import mat.shared.ConstantMessages;

/**
 * The Class SaveMeasureResult.
 */
public class SaveMeasureResult extends GenericResult {
	
	public static final int ID_NOT_UNIQUE = ConstantMessages.ID_NOT_UNIQUE;
	
	public static final int REACHED_MAXIMUM_VERSION = ConstantMessages.REACHED_MAXIMUM_VERSION;
	
	public static final int REACHED_MAXIMUM_MAJOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MAJOR_VERSION;
	
	public static final int REACHED_MAXIMUM_MINOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MINOR_VERSION;
	
	public static final int INVALID_VALUE_SET_DATE = ConstantMessages.INVALID_VALUE_SET_DATE;
	
	public static final int INVALID_DATA = ConstantMessages.INVALID_DATA;
	
	public static final int INVALID_CQL_DATA = ConstantMessages.INVALID_CQL_DATA;
	
	public static final int INVALID_GROUPING = ConstantMessages.INVALID_GROUPING;
	
	public static final int INVALID_PACKAGE_GROUPING = ConstantMessages.INVALID_PACKAGE_GROUPING;
	
	public static final int INVALID_EXPORTS = ConstantMessages.INVALID_EXPORTS;
	
	public static final int INVALID_CREATE_EXPORT = ConstantMessages.INVALID_CREATE_EXPORT;
	
	public static final int PACKAGE_VALIDATION_FAIL = 1;
	
	public static final int UNUSED_LIBRARY_FAIL = 2;
	
	public static final int PACKAGE_FAIL = -1;
	
	public static final int VALIDATE_MEASURE_AT_PACKAGE_FAIL = -3;
	
	/** The id. */
	private String id;
	
	/** The author list. */
	private List<Author> authorList;
	
	/** The measure type list. */
	private List<MeasureType> measureTypeList;
	
	/** The version str. */
	private String versionStr;
		
	private ValidateMeasureResult validateResult; 
	
	/**
	 * Gets the author list.
	 * 
	 * @return the author list
	 */
	public List<Author> getAuthorList() {
		return authorList;
	}

	public ValidateMeasureResult getValidateResult() {
		return validateResult;
	}

	public void setValidateResult(ValidateMeasureResult validateResult) {
		this.validateResult = validateResult;
	}

	/**
	 * Sets the author list.
	 * 
	 * @param authorList
	 *            the new author list
	 */
	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}
	
	/**
	 * Gets the measure type list.
	 * 
	 * @return the measure type list
	 */
	public List<MeasureType> getMeasureTypeList() {
		return measureTypeList;
	}
	
	/**
	 * Sets the measure type list.
	 * 
	 * @param measureTypeList
	 *            the new measure type list
	 */
	public void setMeasureTypeList(List<MeasureType> measureTypeList) {
		this.measureTypeList = measureTypeList;
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
	 * Gets the version str.
	 * 
	 * @return the version str
	 */
	public String getVersionStr() {
		return versionStr;
	}
	
	/**
	 * Sets the version str.
	 * 
	 * @param versionStr
	 *            the new version str
	 */
	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}
	
}
