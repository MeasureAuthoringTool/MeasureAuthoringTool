package mat.client.shared;

import java.util.ArrayList;
import java.util.List;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.shared.MatConstants;

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

		if ((model.getName() == null) || "".equals(model.getName().trim())) {
			message.add(MatContext.get().getMessageDelegate()
					.getMeasureNameRequiredMessage());
		}
		if ((model.getShortName() == null)
				|| "".equals(model.getShortName().trim())) {
			message.add(MatContext.get().getMessageDelegate()
					.getAbvNameRequiredMessage());
		}
		
		String compositeScoring = model.getCompositeScoringMethod();
		if((compositeScoring == null) || !isValidValue(compositeScoring)) {
			MatContext.get().getMessageDelegate();
			message.add(ERR_COMPOSITE_MEASURE_SCORE_REQUIRED);
		}
		
		String scoring = model.getMeasScoring();
		if ((scoring == null) || !isValidValue(model.getMeasScoring())) {
			MatContext.get().getMessageDelegate();
			message.add(MessageDelegate.s_ERR_MEASURE_SCORE_REQUIRED);
		}
		
		if((scoring.equalsIgnoreCase(MatConstants.CONTINUOUS_VARIABLE) && (model.isPatientBased()))) {
			MatContext.get().getMessageDelegate();
			message.add(MessageDelegate.CONTINOUS_VARIABLE_IS_NOT_PATIENT_BASED_ERROR);
		}
		return message;
	}
}
