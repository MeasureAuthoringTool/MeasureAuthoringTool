package mat.client.shared;

import java.util.ArrayList;
import java.util.List;
import mat.client.measure.ManageMeasureDetailModel;
import mat.shared.MatConstants;
import mat.shared.StringUtility;

public class ManageMeasureModelValidator {
	
	public List<String> isValidMeasure(ManageMeasureDetailModel model){
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
		
		if(model.getEndorseByNQF()) {
			if(StringUtility.isEmptyOrNull(model.getNqfId())) {
				message.add(MessageDelegate.NQF_NUMBER_REQUIRED_ERROR);
			}
		}
		
		String scoring = model.getMeasScoring();
		if ((scoring == null) || !isValidValue(model.getMeasScoring())) {
			MatContext.get().getMessageDelegate();
			message.add(MessageDelegate.s_ERR_MEASURE_SCORE_REQUIRED);
		}
		
		// MAT-8602 Continous Variable measures must be patient based.
		if((scoring.equalsIgnoreCase(MatConstants.CONTINUOUS_VARIABLE) && (model.isPatientBased()))) {
			MatContext.get().getMessageDelegate();
			message.add(MessageDelegate.CONTINOUS_VARIABLE_IS_NOT_PATIENT_BASED_ERROR);
		}
		
		return message;
	}
	
	private boolean isValidValue(String value) {
		return !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}
}
