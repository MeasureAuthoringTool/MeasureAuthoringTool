package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.view.GeneralMeasureInformationView;
import mat.client.shared.MatContext;
import mat.shared.MatConstants;

/* This class handles events that pertain to the general information view and do not need to be visible to the presenter */
public class GeneralMeasureInformationObserver {
	public GeneralMeasureInformationView generalMeasureInformationView;
	public GeneralMeasureInformationObserver(GeneralMeasureInformationView generalMeasureInformationView) {
		this.generalMeasureInformationView = generalMeasureInformationView;
	}
	
	//.getCompositeScoringMethodInput().addChangeHandler(event -> createSelectionMapAndSetScorings())
	//.getMeasScoringChoice().addChangeHandler(event -> setPatientBasedIndicatorBasedOnScoringChoice((detailDisplay)));

	public void handleCompositeScoringChanged() {
		createSelectionMapAndSetScorings();
	}
	
	public void handleMeasureScoringChanged() {
		setPatientBasedIndicatorBasedOnScoringChoice();
	}
	
	private void setPatientBasedIndicatorBasedOnScoringChoice() {
		if (MatConstants.CONTINUOUS_VARIABLE.equalsIgnoreCase(generalMeasureInformationView.getMeasureScoringInput().getItemText(generalMeasureInformationView.getMeasureScoringInput().getSelectedIndex()))) {
			if(generalMeasureInformationView.getPatientBasedInput().getItemCount() > 1) {
				// yes is the second element in the list, so the 1 index. 
				generalMeasureInformationView.getPatientBasedInput().removeItem(1);
			}
			generalMeasureInformationView.getPatientBasedInput().setSelectedIndex(0);
			//generalMeasureInformationView.getHelpBlock().setText("Patient based indicator set to no.");
			
		} else {
			generalMeasureInformationView.resetPatientBasedInput(); 
			//generalMeasureInformationView.getHelpBlock().setText("Patient based indicator set to yes.");
		}
		
		//TODO add a help block here?
		//generalMeasureInformationView.getHelpBlock().setColor("transparent");
	}
	
	private void createSelectionMapAndSetScorings() {
		String compositeScoringValue = generalMeasureInformationView.getCompositeScoringValue();
		generalMeasureInformationView.setScoringChoices(MatContext.get().getSelectionMap().get(compositeScoringValue));
		setPatientBasedIndicatorBasedOnScoringChoice();
	}
}
