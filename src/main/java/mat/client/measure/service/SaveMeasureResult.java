package mat.client.measure.service;

import java.util.List;
import mat.client.shared.GenericResult;
import mat.model.Author;
import mat.model.MeasureType;
import mat.shared.ConstantMessages;

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
	
	private String id;
	
	private List<Author> authorList;
	
	private List<MeasureType> measureTypeList;
	
	private String versionStr;
		
	private ValidateMeasureResult validateResult; 

	public List<Author> getAuthorList() {
		return authorList;
	}

	public ValidateMeasureResult getValidateResult() {
		return validateResult;
	}

	public void setValidateResult(ValidateMeasureResult validateResult) {
		this.validateResult = validateResult;
	}

	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}

	public List<MeasureType> getMeasureTypeList() {
		return measureTypeList;
	}

	public void setMeasureTypeList(List<MeasureType> measureTypeList) {
		this.measureTypeList = measureTypeList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersionStr() {
		return versionStr;
	}
	
	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}
	
}
