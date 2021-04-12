package mat.shared.measure.measuredetails.validate;

import com.google.gwt.i18n.shared.DateTimeFormat;
import mat.client.measure.measuredetails.MeasureDetailState;
import mat.model.clause.ModelTypeHelper;
import mat.shared.StringUtility;
import mat.shared.measure.measuredetails.models.GeneralInformationModel;
import mat.shared.validator.measure.CommonMeasureValidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneralInformationValidator {
	private static final String MEASURE_PERIOD_DATES_ERROR = "The dates for Measurement Period are invalid. Please enter valid dates.";
    private static final String COMPOSITE_MEASURE_SCORE_REQUIRED_ERROR = "A Composite Scoring Method is required. ";
    private static final String NQF_REQUIRED_ERROR = "NQF Number is required when a measure is endorsed by NQF.";
	
	public List<String> validateModel(GeneralInformationModel generalInformationModel, boolean isComposite) {

	    List<String> errorMessages = new ArrayList<>();

		if (isComposite && StringUtility.isEmptyOrNull(generalInformationModel.getCompositeScoringMethod())) {
		    errorMessages.add(COMPOSITE_MEASURE_SCORE_REQUIRED_ERROR);
		}

		errorMessages.addAll(CommonMeasureValidator.validateMeasureName(generalInformationModel.getMeasureName()));
		errorMessages.addAll(CommonMeasureValidator.validateMeasureScore(generalInformationModel.getScoringMethod()));
		errorMessages.addAll(CommonMeasureValidator.validateECQMAbbreviation(generalInformationModel.geteCQMAbbreviatedTitle()));

		if (ModelTypeHelper.isFhir(generalInformationModel.getMeasureModel())) {
            errorMessages.addAll(CommonMeasureValidator.validatePatientBased(generalInformationModel.getScoringMethod(), generalInformationModel.isPatientBased()));
        }
		if (generalInformationModel.getEndorseByNQF() && StringUtility.isEmptyOrNull(generalInformationModel.getNqfId())) {
			errorMessages.add(NQF_REQUIRED_ERROR);
		}
		
		if (!generalInformationModel.isCalendarYear()) {
			try {
				DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
				Date fromDate = dateFormat.parseStrict(generalInformationModel.getMeasureFromPeriod());
				Date toDate = dateFormat.parseStrict(generalInformationModel.getMeasureToPeriod());
				if (fromDate.after(toDate)) {
					errorMessages.add(MEASURE_PERIOD_DATES_ERROR);
				}
			} catch(Exception e) {
				errorMessages.add(MEASURE_PERIOD_DATES_ERROR);
			}
		}
		return errorMessages;
	}
	
	public boolean isValueComplete(String value) {
		return StringUtility.isNotBlank(value);
	}

	public MeasureDetailState getModelState(GeneralInformationModel model, boolean isComposite) {
	    if (isValueComplete(model.getMeasureName()) &&
		   isValueComplete(model.getGuid()) &&
		   isValueComplete(model.geteCQMAbbreviatedTitle()) &&
		   isValueComplete(model.geteCQMVersionNumber()) &&
		   isValueComplete(model.getScoringMethod()) &&
		   isCalendarYearComplete(model) && 
		   isCompositeScoringMethodComplete(model, isComposite) &&
		   isNQFComplete(model)
		) {
			return MeasureDetailState.COMPLETE;
		}
		return MeasureDetailState.INCOMPLETE;
	}

	private boolean isNQFComplete(GeneralInformationModel model) {
		if (model.getEndorseByNQF() != null && model.getEndorseByNQF()) {
			return isValueComplete(model.getNqfId());
		}
		return true;
	}

	private boolean isCompositeScoringMethodComplete(GeneralInformationModel model, boolean isComposite) {
		if (isComposite) {
			return isValueComplete(model.getCompositeScoringMethod());
		}
		return true;
	}

	private boolean isCalendarYearComplete(GeneralInformationModel model) {
		boolean isComplete = true;
		if (!model.isCalendarYear()) {
			if (!isValueComplete(model.getMeasureFromPeriod()) || !isValueComplete(model.getMeasureToPeriod())) {
				isComplete = false;
			} 
		}
		return isComplete;
	}
}
