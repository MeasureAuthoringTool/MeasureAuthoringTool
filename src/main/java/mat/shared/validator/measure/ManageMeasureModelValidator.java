package mat.shared.validator.measure;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.shared.MessageDelegate;
import mat.shared.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageMeasureModelValidator {
	private final Logger logger = Logger.getLogger("ManageMeasureModelValidator");

	public List<String> validateMeasure(ManageMeasureDetailModel model){
		List<String> message = performCommonMeasureValidation(model);
		message.addAll(validateNQF(model));
		return message;
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
		String libName = model.getCQLLibraryName();
		CommonMeasureValidator commonMeasureValidator = new CommonMeasureValidator();
		message.addAll(model.isFhir() ? commonMeasureValidator.validateFhirMeasureName(model.getMeasureName()) :
                commonMeasureValidator.validateMeasureName(model.getMeasureName()));
		logger.log(Level.INFO,"performCommonMeasureValidation isFhir=" + model.isFhir() + " " + libName);
		message.addAll(model.isFhir() ? commonMeasureValidator.validateFhirLibraryName(libName):
				commonMeasureValidator.validateQDMName(libName));
		message.addAll(commonMeasureValidator.validateECQMAbbreviation(model.getShortName()));
		String scoring = model.getMeasScoring();
		message.addAll(commonMeasureValidator.validateMeasureScore(scoring));
		message.addAll(commonMeasureValidator.validatePatientBased(scoring, model.isPatientBased()));
		return message;
	}
}
