package mat.client.populationworkspace;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import mat.client.cqlworkspace.DeleteConfirmationDialogBox;
import mat.client.event.MeasureSelectedEvent;
import mat.client.shared.CQLWorkSpaceConstants.POPULATIONS;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.WarningMessageAlert;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * The Class CQLPopulationLeftNavBarPanelView.
 */
public class CQLPopulationLeftNavBarPanelView {

	/** The right hand nav panel. */
	private VerticalPanel rightHandNavPanel = new VerticalPanel();

	private AnchorListItem initialPopulation;

	private AnchorListItem numerator;

	private AnchorListItem denominator;

	private AnchorListItem numeratorExclusions;

	private AnchorListItem denominatorExclusions;

	private AnchorListItem denominatorExceptions;

	private AnchorListItem measurePopulations;

	private AnchorListItem measurePopulationExclusions;

	private AnchorListItem stratifications;

	private AnchorListItem measureObservations;

	private AnchorListItem viewPopulations;

	private VerticalPanel messagePanel = new VerticalPanel();

	private MessageAlert successMessageAlert = new SuccessMessageAlert();

	private MessageAlert warningMessageAlert = new WarningMessageAlert();

	private MessageAlert errorMessageAlert = new ErrorMessageAlert();

	private WarningConfirmationMessageAlert warningConfirmationMessageAlert = new WarningConfirmationMessageAlert();

	private DeleteConfirmationDialogBox deleteConfirmationDialogBox = new DeleteConfirmationDialogBox();

	private WarningConfirmationMessageAlert globalWarningConfirmationMessageAlert;

	private Boolean isLoading = false;

	/**
	 * Builds the measure lib CQL view.
	 * @param document 
	 *
	 * @return the vertical panel
	 */
	public VerticalPanel buildMeasureLibCQLView(Document document){
		globalWarningConfirmationMessageAlert = new WarningConfirmationMessageAlert();
		buildLeftHandNavBar(document);
		return rightHandNavPanel;
	}

	/**
	 * Builds the left hand nav nar.
	 */
	private void buildLeftHandNavBar(Document document) {
		rightHandNavPanel.clear();
		NavPills navPills = new NavPills();
		navPills.setStacked(true);
		
		initialPopulation = new AnchorListItem();
		numerator = new AnchorListItem();
		denominator = new AnchorListItem();
		numeratorExclusions = new AnchorListItem();
		denominatorExclusions = new AnchorListItem();
		denominatorExceptions = new AnchorListItem();
		measurePopulations = new AnchorListItem();
		measurePopulationExclusions = new AnchorListItem();
		stratifications = new AnchorListItem();
		measureObservations = new AnchorListItem();
		viewPopulations = new AnchorListItem();
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			setTextAndIcons(initialPopulation, POPULATIONS.INITIAL_POPULATIONS.popName(), IconType.PENCIL);
			setTextAndIcons(denominator, POPULATIONS.DENOMINATORS.popName(), IconType.PENCIL);
			setTextAndIcons(denominatorExclusions, POPULATIONS.DENOMINATOR_EXCLUSIONS.popName(), IconType.PENCIL);
			setTextAndIcons(numerator, POPULATIONS.NUMERATORS.popName(), IconType.PENCIL);
			setTextAndIcons(numeratorExclusions, POPULATIONS.NUMERATOR_EXCLUSIONS.popName(), IconType.PENCIL);
			setTextAndIcons(denominatorExceptions, POPULATIONS.DENOMINATOR_EXCEPTIONS.popName(), IconType.PENCIL);
			setTextAndIcons(measurePopulations, POPULATIONS.MEASURE_POPULATIONS.popName(), IconType.PENCIL);
			setTextAndIcons(measurePopulationExclusions, POPULATIONS.MEASURE_POPULATION_EXCLUSIONS.popName(), IconType.PENCIL);
			setTextAndIcons(stratifications, POPULATIONS.STRATIFICATION.popName(), IconType.PENCIL);
			setTextAndIcons(measureObservations, POPULATIONS.MEASURE_OBSERVATIONS.popName(), IconType.PENCIL);
			MeasureSelectedEvent measure = MatContext.get().getCurrentMeasureInfo();
			addAnchorsByScoring(navPills, measure.getScoringType(), measure.isPatientBased());
		}
		setTextAndIcons(viewPopulations, POPULATIONS.VIEW_POPULATIONS.popName(), IconType.BOOK);
		navPills.add(viewPopulations);// View Populations is always present
		viewPopulations.setActive(true);// View Populations is initially selected.

		navPills.setWidth("200px");
		messagePanel.add(successMessageAlert);
		messagePanel.add(warningMessageAlert);
		messagePanel.add(errorMessageAlert);
		messagePanel.add(warningConfirmationMessageAlert);
		messagePanel.add(globalWarningConfirmationMessageAlert);
		messagePanel.setStyleName("marginLeft15px");
		rightHandNavPanel.add(navPills);
	}

	private void addAnchorsByScoring(NavPills navPills, String scoringIdAttributeValue, boolean isPatientBasedMeasure) {
		navPills.add(initialPopulation);
		//COHORT scoring has the initial population and stratifications. 
		if("PROPORTION".equalsIgnoreCase(scoringIdAttributeValue)){	
			addNumDenoNavPills(navPills);
			navPills.add(denominatorExceptions);			
			navPills.add(stratifications);
		} else if("CONTINUOUS VARIABLE".equalsIgnoreCase(scoringIdAttributeValue)){
			navPills.add(measurePopulations);
			navPills.add(measurePopulationExclusions);
			navPills.add(stratifications);
			navPills.add(measureObservations);			
		} else if("RATIO".equalsIgnoreCase(scoringIdAttributeValue)){			
			addNumDenoNavPills(navPills);
			//Measure Observations are not available 
			//for Patient based Ratio Measures
			if (!isPatientBasedMeasure) {
				navPills.add(measureObservations);
			}
		} else if("COHORT".equalsIgnoreCase(scoringIdAttributeValue)) {
			navPills.add(stratifications);
		}

	}

	private void addNumDenoNavPills(NavPills navPills) {
		navPills.add(denominator);
		navPills.add(denominatorExclusions);
		navPills.add(numerator);
		navPills.add(numeratorExclusions);
	}
	
	private void setTextAndIcons(AnchorListItem anchorListItem, String text, IconType iconType) {
		anchorListItem.setIcon(iconType);		
		anchorListItem.setText(text);
		anchorListItem.setTitle(text);
		anchorListItem.setId(text+"_Anchor");	
	}

	/**
	 * Gets the right hand nav panel.
	 *
	 * @return the right hand nav panel
	 */
	public VerticalPanel getRightHandNavPanel() {
		return rightHandNavPanel;
	}

	/**
	 * Gets the message panel.
	 *
	 * @return the message panel
	 */
	public VerticalPanel getMessagePanel() {
		return messagePanel;
	}

	/**
	 * Sets the message panel.
	 *
	 * @param messagePanel the new message panel
	 */
	public void setMessagePanel(VerticalPanel messagePanel) {
		this.messagePanel = messagePanel;
	}

	/**
	 * Gets the delete confirmation dialog box.
	 *
	 * @return the delete confirmation dialog box
	 */
	public DeleteConfirmationDialogBox getDeleteConfirmationDialogBox() {
		return deleteConfirmationDialogBox;
	}

	/**
	 * Sets the delete confirmation dialog box.
	 *
	 * @param deleteConfirmationDialogBox the new delete confirmation dialog box
	 */
	public void setDeleteConfirmationDialogBox(DeleteConfirmationDialogBox deleteConfirmationDialogBox) {
		this.deleteConfirmationDialogBox = deleteConfirmationDialogBox;
	}

	/**
	 * Gets the success message alert.
	 *
	 * @return the success message alert
	 */
	public MessageAlert getSuccessMessageAlert() {
		return successMessageAlert;
	}

	/**
	 * Gets the warning message alert.
	 *
	 * @return the warning message alert
	 */
	public MessageAlert getWarningMessageAlert() {
		return warningMessageAlert;
	}

	/**
	 * Gets the error message alert.
	 *
	 * @return the error message alert
	 */
	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}

	/**
	 * Gets the warning confirmation message alert.
	 *
	 * @return the warning confirmation message alert
	 */
	public WarningConfirmationMessageAlert getWarningConfirmationMessageAlert() {
		return warningConfirmationMessageAlert;
	}

	/**
	 * Gets the global warning confirmation message alert.
	 *
	 * @return the global warning confirmation message alert
	 */
	public WarningConfirmationMessageAlert getGlobalWarningConfirmationMessageAlert() {
		return globalWarningConfirmationMessageAlert;
	}

	/**
	 * Show unsaved changes warning.
	 */
	public void showUnsavedChangesWarning() {
		getWarningMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().createAlert();
		getWarningConfirmationMessageAlert().getWarningConfirmationYesButton().setFocus(true);
	}

	/**
	 * Show global unsaved changes warning.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * showGlobalUnsavedChangesWarning()
	 */
	public void showGlobalUnsavedChangesWarning() {
		getWarningMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().clearAlert();
		getGlobalWarningConfirmationMessageAlert().createAlert();
		getGlobalWarningConfirmationMessageAlert().getWarningConfirmationYesButton().setFocus(true);
	}

	/**
	 * Show delete confirmation message alert.
	 *
	 * @param message the message
	 */
	public void showDeleteConfirmationMessageAlert(String message) {
		getWarningMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getWarningConfirmationMessageAlert().clearAlert();
	}

	/**
	 * Sets the warning message alert.
	 *
	 * @param warningMessageAlert the new warning message alert
	 */
	public void setWarningMessageAlert(MessageAlert warningMessageAlert) {
		this.warningMessageAlert = warningMessageAlert;
	}

	/**
	 * Gets the warning confirmation yes button.
	 *
	 * @return the warning confirmation yes button
	 */
	public Button getWarningConfirmationYesButton() {
		return warningConfirmationMessageAlert.getWarningConfirmationYesButton();
	}

	/**
	 * Gets the warning confirmation no button.
	 *
	 * @return the warning confirmation no button
	 */
	public Button getWarningConfirmationNoButton() {
		return warningConfirmationMessageAlert.getWarningConfirmationNoButton();
	}

	/**
	 * Gets the global warning confirmation yes button.
	 *
	 * @return the global warning confirmation yes button
	 */
	public Button getGlobalWarningConfirmationYesButton() {
		return globalWarningConfirmationMessageAlert.getWarningConfirmationYesButton();
	}

	/**
	 * Gets the global warning confirmation no button.
	 *
	 * @return the global warning confirmation no button
	 */
	public Button getGlobalWarningConfirmationNoButton() {
		return globalWarningConfirmationMessageAlert.getWarningConfirmationNoButton();
	}

	/**
	 * Gets the delete confirmation dialog box yes button.
	 *
	 * @return the delete confirmation dialog box yes button
	 */
	public Button getDeleteConfirmationDialogBoxYesButton() {
		return deleteConfirmationDialogBox.getYesButton();
	}

	/**
	 * Gets the delete confirmation dialog box no button.
	 *
	 * @return the delete confirmation dialog box no button
	 */
	public Button getDeleteConfirmationDialogBoxNoButton() {
		return deleteConfirmationDialogBox.getNoButton();
	}

	public Boolean getIsLoading() {
		return isLoading;
	}

	public void setIsLoading(Boolean isLoading) {
		this.isLoading = isLoading;
	}

	public AnchorListItem getInitialPopulation() {
		return initialPopulation;
	}

	public AnchorListItem getNumerator() {
		return numerator;
	}

	public AnchorListItem getDenominator() {
		return denominator;
	}

	public AnchorListItem getNumeratorExclusions() {
		return numeratorExclusions;
	}

	public void setNumeratorExclusions(AnchorListItem numeratorExclusions) {
		this.numeratorExclusions = numeratorExclusions;
	}

	public AnchorListItem getDenominatorExclusions() {
		return denominatorExclusions;
	}

	public AnchorListItem getStratifications() {
		return stratifications;
	}

	public AnchorListItem getMeasureObservations() {
		return measureObservations;
	}

	public AnchorListItem getViewPopulations() {
		return viewPopulations;
	}

	public AnchorListItem getDenominatorExceptions() {
		return denominatorExceptions;
	}

	public AnchorListItem getMeasurePopulations() {
		return measurePopulations;
	}

	public AnchorListItem getMeasurePopulationExclusions() {
		return measurePopulationExclusions;
	}
}
