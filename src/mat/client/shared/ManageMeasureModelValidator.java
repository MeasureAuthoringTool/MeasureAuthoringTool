package mat.client.shared;

import java.util.ArrayList;
import java.util.List;
import mat.client.measure.ManageMeasureDetailModel;

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
		
		// US 421. Validate Measure Scoring choice
		String scoring = model.getMeasScoring();
		//String enteredScoringValue = detailDisplay.getMeasScoringValue();
		if ((scoring == null) || !isValidValue(model.getMeasScoring())) {
			//|| enteredScoringValue.equals("--Select--")) {
			message.add(MatContext.get().getMessageDelegate().s_ERR_MEASURE_SCORE_REQUIRED);
		}
		return message;
	}
	
	private boolean isValidValue(String value) {
		return !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}
}
