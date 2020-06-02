package mat.shared.validator.measure;

import java.util.ArrayList;
import java.util.List;

import mat.client.measure.ManageCompositeMeasureDetailModel;

public class ManageCompositeMeasureModelValidator extends ManageMeasureModelValidator {
	public static final String ERR_COMPOSITE_MEASURE_SCORE_REQUIRED = "Composite Scoring Method is required. ";
	
	public List<String> validateMeasureWithClone(ManageCompositeMeasureDetailModel model, boolean isClone) {
		List<String> message = performCommonMeasureValidation(model);
		if(!isClone) {
			message.addAll(validateNQF(model));
		}

		return message;
	}
	

	private List<String> performCommonMeasureValidation(ManageCompositeMeasureDetailModel model) {
		List<String> message = new ArrayList<String>();

		CommonMeasureValidator commonMeasureValidator = new CommonMeasureValidator();
		message.addAll(commonMeasureValidator.validateMeasureName(model.getMeasureName()));
		message.addAll(commonMeasureValidator.validateQDMName(model.getCQLLibraryName()));
		message.addAll(commonMeasureValidator.validateECQMAbbreviation(model.getShortName()));
		String compositeScoring = model.getCompositeScoringMethod();
		if((compositeScoring == null) || !CommonMeasureValidator.isValidValue(compositeScoring)) {
			message.add(ERR_COMPOSITE_MEASURE_SCORE_REQUIRED);
		}
		message.addAll(commonMeasureValidator.validateMeasureScore(model.getMeasScoring()));		
		return message;
	}
}
