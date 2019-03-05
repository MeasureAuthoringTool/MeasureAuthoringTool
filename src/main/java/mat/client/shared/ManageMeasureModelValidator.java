package mat.client.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mat.client.measure.ManageMeasureDetailModel;
import mat.shared.MatConstants;
import mat.shared.StringUtility;

public class ManageMeasureModelValidator {
	
	public List<String> validateMeasure(ManageMeasureDetailModel model){
		List<String> message = performCommonMeasureValidation(model);
		message.addAll(validateNQF(model));
		return message;
	}
	
	protected boolean isValidValue(String value) {
		return !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}

	public List<String> validateMeasureWithClone(ManageMeasureDetailModel model, boolean isClone) {
		List<String> message = performCommonMeasureValidation(model);
		if(!isClone) {
			message.addAll(validateNQF(model));
		}
		return message;
	}
	
	protected List<String> validateNQF(ManageMeasureDetailModel model) {
		List<String> message = new ArrayList<String>();
		if(Optional.ofNullable(model.getEndorseByNQF()).orElse(false)) { 
			if(StringUtility.isEmptyOrNull(model.getNqfId())) {
				message.add(MessageDelegate.NQF_NUMBER_REQUIRED_ERROR);
			}
		}
		return message;
	}
	
	private List<String> performCommonMeasureValidation(ManageMeasureDetailModel model) {
		List<String> message = new ArrayList<String>();

		if ((model.getName() == null) || "".equals(model.getName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getMeasureNameRequiredMessage());
		} else {
			if(!hasAtleastOneLetter(model.getName())){
				message.add(MessageDelegate.MEASURE_NAME_LETTER_REQUIRED);
			}
		}
		if ((model.getShortName() == null)
				|| "".equals(model.getShortName().trim())) {
			message.add(MatContext.get().getMessageDelegate()
					.getAbvNameRequiredMessage());
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
	
    protected boolean hasAtleastOneLetter(String s) {
        for(int i = 0; i < s.length(); i++) {
        	if(s.matches("[a-zA-Z].+")) {
                return true;
        	}
        }
        return false;
    }
}
