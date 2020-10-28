package mat.client.measure.service;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.shared.GenericResult;
import mat.model.Author;
import mat.model.MeasureType;
import mat.shared.ConstantMessages;

import java.util.List;

public class SaveMeasureResult extends GenericResult {

    public static final int ID_NOT_UNIQUE = ConstantMessages.ID_NOT_UNIQUE;

    public static final int REACHED_MAXIMUM_VERSION = ConstantMessages.REACHED_MAXIMUM_VERSION;

    public static final int REACHED_MAXIMUM_MAJOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MAJOR_VERSION;

    public static final int REACHED_MAXIMUM_MINOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MINOR_VERSION;

    public static final int INVALID_VALUE_SET_DATE = ConstantMessages.INVALID_VALUE_SET_DATE;

    public static final int INVALID_DATA = ConstantMessages.INVALID_DATA;

    public static final int INVALID_CQL_DATA = ConstantMessages.INVALID_CQL_DATA;

    public static final int INVALID_GROUPING = 9;

    public static final int INVALID_PACKAGE_GROUPING = 10;

    public static final int INVALID_EXPORTS = 11;

    public static final int INVALID_CREATE_EXPORT = 12;

    public static final int PACKAGE_VALIDATION_FAIL = 1;

    public static final int UNUSED_LIBRARY_FAIL = 2;

    public static final int PACKAGE_FAIL = -1;

    public static final int VALIDATE_MEASURE_AT_PACKAGE_FAIL = -3;

    private String id;

    private List<Author> authorList;

    private List<MeasureType> measureTypeList;

    private String versionStr;

    private ValidateMeasureResult validateResult;

    private ManageCompositeMeasureDetailModel compositeMeasureDetailModel;

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

    public ManageCompositeMeasureDetailModel getCompositeMeasureDetailModel() {
        return compositeMeasureDetailModel;
    }

    public void setCompositeMeasureDetailModel(ManageCompositeMeasureDetailModel compositeMeasureDetailModel) {
        this.compositeMeasureDetailModel = compositeMeasureDetailModel;
    }

	@Override
	public String toString() {
		return "SaveMeasureResult{" +
				"id='" + id + '\'' +
				", authorList=" + authorList +
				", measureTypeList=" + measureTypeList +
				", versionStr='" + versionStr + '\'' +
				", validateResult=" + validateResult +
				", compositeMeasureDetailModel=" + compositeMeasureDetailModel +
				'}';
	}
}
