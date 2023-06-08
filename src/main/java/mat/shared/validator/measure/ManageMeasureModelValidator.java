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

	public List<String> validateMeasure(ManageMeasureDetailModel model) {
		List<String> message = performCommonMeasureValidation(model);
		message.addAll(validateNQF(model));
		return message;
	}

	public List<String> validateMeasureWithClone(ManageMeasureDetailModel model, boolean isClone) {
		List<String> message = performCommonMeasureValidation(model);
		if (!isClone) {
			message.addAll(validateNQF(model));
		}
		return message;
	}
	
	protected List<String> validateNQF(ManageMeasureDetailModel model) {
		List<String> message = new ArrayList<>();
		if (Optional.ofNullable(model.getEndorseByNQF()).orElse(false)) {
			if (StringUtility.isEmptyOrNull(model.getNqfId())) {
				message.add(MessageDelegate.CBE_NUMBER_REQUIRED_ERROR);
			}
		}
		return message;
	}
	
	private List<String> performCommonMeasureValidation(ManageMeasureDetailModel model) {
		List<String> message = new ArrayList<>();

		String libName = model.getCQLLibraryName();
        String scoring = model.getMeasScoring();

        if (model.isFhir()) {
            message.addAll(CommonMeasureValidator.validateFhirMeasureName(model.getMeasureName()));
            message.addAll(CommonMeasureValidator.validateFhirLibraryName(libName));
            message.addAll(CommonMeasureValidator.validatePatientBased(scoring, model.isPatientBased()));
        } else {
            message.addAll(CommonMeasureValidator.validateMeasureName(model.getMeasureName()));
            message.addAll(CommonMeasureValidator.validateQDMName(libName));
        }

        message.addAll(CommonMeasureValidator.validateECQMAbbreviation(model.getShortName()));
        message.addAll(CommonMeasureValidator.validateMeasureScore(scoring));

		logger.log(Level.INFO,"performCommonMeasureValidation isFhir=" + model.isFhir() + " " + libName);

		return message;
	}
}
