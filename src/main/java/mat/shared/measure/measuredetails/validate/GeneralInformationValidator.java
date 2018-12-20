package mat.shared.measure.measuredetails.validate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.shared.StringUtility;
import mat.shared.measure.measuredetails.models.GeneralInformationModel;

public class GeneralInformationValidator {
	private static final String MEASURE_PERIOD_DATES_ERROR = "Please enter valid Measurement Period dates.";

	public List<String> validateModel(GeneralInformationModel generalInformationModel) {
		List<String> errorMessages = new ArrayList<>();
		if(generalInformationModel.getEndorseByNQF() && StringUtility.isEmptyOrNull(generalInformationModel.getNqfId())) {
			errorMessages.add("NQF Number is required when a measure is endorsed by NQF.");
		}
		
		if(!generalInformationModel.isCalendarYear()) {
			try {
				DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
				Date fromDate = dateFormat.parse(generalInformationModel.getMeasureFromPeriod());
				Date toDate = dateFormat.parse(generalInformationModel.getMeasureToPeriod());
				 if(fromDate.after(toDate)) {
					errorMessages.add(MEASURE_PERIOD_DATES_ERROR);
				}
			} catch(Exception e) {
				errorMessages.add(MEASURE_PERIOD_DATES_ERROR);
			}
		}

		return errorMessages;
	}
	
	public boolean isValueComplete(String value) {
		if(StringUtility.isEmptyOrNull(value)) {
			return false;
		}
		return true;
	}

	public MeasureDetailState getModelState(GeneralInformationModel model, boolean isComposite) {
		if(isValueComplete(model.getMeasureName()) &&	
		   isValueComplete(model.getGuid()) &&
		   isValueComplete(model.geteCQMAbbreviatedTitle()) &&
		   isValueComplete(model.geteCQMVersionNumber()) &&
		   isValueComplete(model.getScoringMethod()) &&
		   (model.geteMeasureId() > 0) &&
		   isCalendarYearComplete(model) && 
		   isCompositeScoringMethodComplete(model, isComposite) &&
		   isNQFComplete(model)
		) {
			return MeasureDetailState.COMPLETE;
		}

		return MeasureDetailState.INCOMPLETE;
	}

	private boolean isNQFComplete(GeneralInformationModel model) {
		if(model.getEndorseByNQF() != null && model.getEndorseByNQF()) {
			return isValueComplete(model.getNqfId());
		}
		return true;
	}

	private boolean isCompositeScoringMethodComplete(GeneralInformationModel model, boolean isComposite) {
		if(isComposite) {
			return isValueComplete(model.getCompositeScoringMethod());
		}
		return true;
	}

	private boolean isCalendarYearComplete(GeneralInformationModel model) {
		boolean isComplete = true;
		if(!model.isCalendarYear()) {
			if(!isValueComplete(model.getMeasureFromPeriod()) || !isValueComplete(model.getMeasureToPeriod())) {
				isComplete = false;
			} 
		}
		return isComplete;
	}
}
