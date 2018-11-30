package mat.client.measure.measuredetails.observers;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.client.measure.measuredetails.views.GeneralMeasureInformationView;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.MatContext;
import mat.shared.MatConstants;
import mat.shared.measure.measuredetails.models.GeneralInformationModel;

/* This class handles events that pertain to the general information view and do not need to be visible to the presenter */
public class GeneralMeasureInformationObserver {
	public GeneralMeasureInformationView generalMeasureInformationView;
	public GeneralMeasureInformationObserver(GeneralMeasureInformationView generalMeasureInformationView) {
		this.generalMeasureInformationView = generalMeasureInformationView;
	}
	
	public void handleCompositeScoringChanged() {
		createSelectionMapAndSetScorings();
		generalMeasureInformationView.setGeneralInformationModel(updateGeneralInformationModelFromView());
	}

	public void handleMeasureScoringChanged() {
		setPatientBasedIndicatorBasedOnScoringChoice();
		generalMeasureInformationView.setGeneralInformationModel(updateGeneralInformationModelFromView());
	}
	
	public void handleInputChanged() {
		generalMeasureInformationView.setGeneralInformationModel(updateGeneralInformationModelFromView());
	}
	
	private GeneralInformationModel updateGeneralInformationModelFromView() {
		GeneralInformationModel generalInformationModel = generalMeasureInformationView.getGeneralInformationModel();
		String compositeScoringValue = generalMeasureInformationView.getCompositeScoringValue().equals(MatContext.PLEASE_SELECT) ? null : generalMeasureInformationView.getCompositeScoringValue();
		generalInformationModel.setCompositeScoringMethod(compositeScoringValue);
		String scoringValue = generalMeasureInformationView.getMeasureScoringValue().equals(MatContext.PLEASE_SELECT) ? null : generalMeasureInformationView.getMeasureScoringValue();
		generalInformationModel.setScoringMethod(scoringValue);
		if(generalMeasureInformationView.getPatientBasedInput().getItemText(generalMeasureInformationView.getPatientBasedInput().getSelectedIndex()).equalsIgnoreCase("Yes")) {
			generalInformationModel.setPatientBased(true);
		} else {
			generalInformationModel.setPatientBased(false);
		}
		generalInformationModel.seteCQMAbbreviatedTitle(generalMeasureInformationView.getECQMAbbrInput().getText());
		generalInformationModel.setMeasureName(generalMeasureInformationView.getMeasureNameInput().getText());
		return generalInformationModel;
	}
	
	public void setPatientBasedIndicatorBasedOnScoringChoice() {
		List<String> patientBasedIndicatorOptions = MatContext.get().getPatientBasedIndicatorOptions(generalMeasureInformationView.getMeasureScoringInput().getItemText(generalMeasureInformationView.getMeasureScoringInput().getSelectedIndex()));
		generalMeasureInformationView.setPatientBasedInputOptions(patientBasedIndicatorOptions);
		if (MatConstants.CONTINUOUS_VARIABLE.equalsIgnoreCase(generalMeasureInformationView.getMeasureScoringInput().getItemText(generalMeasureInformationView.getMeasureScoringInput().getSelectedIndex()))) {
			generalMeasureInformationView.getPatientBasedInput().setSelectedIndex(0);
			generalMeasureInformationView.getHelpBlock().setText("Patient based indicator set to no.");
		} else {
			generalMeasureInformationView.getPatientBasedInput().setSelectedIndex(1);
			generalMeasureInformationView.getHelpBlock().setText("Patient based indicator set to yes.");
		}
		
		generalMeasureInformationView.getHelpBlock().setColor("transparent");
	}
	
	private void createSelectionMapAndSetScorings() {
		String compositeScoringValue = generalMeasureInformationView.getCompositeScoringValue();
		generalMeasureInformationView.setScoringChoices(MatContext.get().getSelectionMap().get(compositeScoringValue));
		setPatientBasedIndicatorBasedOnScoringChoice();
	}

	public ConfirmationDialogBox getSaveConfirmation(GeneralInformationModel originalModel, GeneralInformationModel generalInformationModel) {
		if(scoringMethodHasChanged(originalModel, generalInformationModel)) {
			return buildSaveConfirmationDialogbox();
		}
		return null;
	}

	private ConfirmationDialogBox buildSaveConfirmationDialogbox() {
		String messageText = "Changing the 'Composite Scoring Method' and/or the 'Measure Scoring' will have the following impacts:<p><ul>" +
		"<li>Populations in the Population Workspace that do not apply to the new settings will be deleted.</li>" +
		"<li>Existing Groupings in the Measure Packager will be deleted..</li>" +
		"<li>The Patient-based Measure field will be reset to its default status for the scoring selected.</li>" +
		"</ul><p>Do you want to continue?";
		ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox(messageText, "Yes", "No");
		return confirmationDialogBox;
	}

	private boolean scoringMethodHasChanged(GeneralInformationModel originalModel, GeneralInformationModel generalInformationModel) {
		boolean scoringMethodChanged = false;
		if(originalModel.getCompositeScoringMethod() != null) {
			scoringMethodChanged = compositeScoringChanged(originalModel, generalInformationModel, scoringMethodChanged);
		} else if(generalInformationModel.getCompositeScoringMethod() != null){
			scoringMethodChanged = compositeScoringChanged(generalInformationModel, originalModel, scoringMethodChanged);
		}
		if(!scoringMethodChanged) {
			if(originalModel.getScoringMethod() != null) {
				scoringMethodChanged = scoringChanged(originalModel, generalInformationModel, scoringMethodChanged);
			} else if(generalInformationModel.getScoringMethod() != null){
				scoringMethodChanged = scoringChanged(generalInformationModel, originalModel, scoringMethodChanged);
			}
		}
		return scoringMethodChanged;
	}

	private boolean compositeScoringChanged(GeneralInformationModel model1, GeneralInformationModel model2, boolean scoringMethodChanged) {
		if(model1.getCompositeScoringMethod() != null && !model1.getCompositeScoringMethod().equals(model2.getCompositeScoringMethod())) {
			scoringMethodChanged = true;
		}
		return scoringMethodChanged;
	}
	
	private boolean scoringChanged(GeneralInformationModel model1, GeneralInformationModel model2, boolean scoringMethodChanged) {
		if(model1.getScoringMethod() != null && !model1.getScoringMethod().equals(model2.getScoringMethod())) {
			scoringMethodChanged = true;
		}
		return scoringMethodChanged;
	}
	
	public void generateAndSaveNewEmeasureid() {
		boolean isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();
		String measureId = MatContext.get().getCurrentMeasureId();
		int eMeasureId = generalMeasureInformationView.getGeneralInformationModel().geteMeasureId();
		if(isEditable && eMeasureId ==0){
			//TODO implement the server side method
			MatContext.get().getMeasureService().generateAndSaveMaxEmeasureId(isEditable, measureId, new AsyncCallback<Integer>() {
				
				@Override
				public void onFailure(Throwable caught) {
					//TODO create error message //MatContext.get().getMessageDelegate().getGenericErrorMessage();
					MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				}
				
				@Override
				public void onSuccess(Integer result) {
					if(result > 0){
						int maxEmeasureId = result.intValue();
						generalMeasureInformationView.getGenerateEMeasureIDButton().setEnabled(false);
						generalMeasureInformationView.geteMeasureIdentifierInput().setText(String.valueOf(maxEmeasureId));
						generalMeasureInformationView.geteMeasureIdentifierInput().setValue(String.valueOf(maxEmeasureId));
						generalMeasureInformationView.getGeneralInformationModel().seteMeasureId(maxEmeasureId);
						generalMeasureInformationView.geteMeasureIdentifierInput().setFocus(true);
					}
				}
			});
		}
	}
}
