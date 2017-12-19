package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.WarningMessageAlert;

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




	/** The message panel. */
	private VerticalPanel messagePanel = new VerticalPanel();

	/** The CQL success message. */
	private MessageAlert successMessageAlert = new SuccessMessageAlert();

	/**  The CQL warning message. */
	private MessageAlert warningMessageAlert = new WarningMessageAlert();

	/** The CQL error message. */
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();

	/** The CQL warning message. */
	private WarningConfirmationMessageAlert warningConfirmationMessageAlert = new WarningConfirmationMessageAlert();

	/**  The delete confirmation box. */
	DeleteConfirmationDialogBox deleteConfirmationDialogBox = new DeleteConfirmationDialogBox();

	/** The CQL warning message. */
	private WarningConfirmationMessageAlert globalWarningConfirmationMessageAlert;// = new WarningConfirmationMessageAlert();

	/** The delete confirmation messge alert. */
	//private DeleteConfirmationMessageAlert deleteConfirmationMessgeAlert = new DeleteConfirmationMessageAlert();


	/** The Loading flag for page. */
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
			
			final String SCORING = "scoring";			
			
			setTextAndIcons(initialPopulation, "Initial Populations", IconType.PENCIL);
			setTextAndIcons(numerator, "Numerators", IconType.PENCIL);
			setTextAndIcons(denominator, "Denominators", IconType.PENCIL);
			setTextAndIcons(numeratorExclusions, "Numerator Exclusions", IconType.PENCIL);
			setTextAndIcons(denominatorExclusions, "Denominator Exclusions", IconType.PENCIL);
			setTextAndIcons(denominatorExceptions, "Denominator Exceptions", IconType.PENCIL);
			setTextAndIcons(measurePopulations, "Measure Populations", IconType.PENCIL);
			setTextAndIcons(measurePopulationExclusions, "Measure Population Exclusions", IconType.PENCIL);
			setTextAndIcons(stratifications, "Stratification", IconType.PENCIL);
			setTextAndIcons(measureObservations, "Measure Observations", IconType.PENCIL);
			
			/**
			 * Find Scoring type for the measure from the Measure XML.
			 */
			Node scoringNode = document.getElementsByTagName(SCORING).item(0);
			String scoringIdAttributeValue = ((Element) scoringNode).getAttribute("id");
			
			addAchorsByScoring(navPills, scoringIdAttributeValue);
			

		}
		
		setTextAndIcons(viewPopulations, "View Populations", IconType.BOOK);
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

	private void addAchorsByScoring(NavPills navPills,
			String scoringIdAttributeValue) {
		navPills.add(initialPopulation);
		//COHORT scoring has the initial population and stratifications. 
		if("PROPOR".equals(scoringIdAttributeValue)){			
			addNumDenoNavPills(navPills);
			navPills.add(denominatorExceptions);							
		} else if("CONTVAR".equals(scoringIdAttributeValue)){			
			navPills.add(measurePopulations);
			navPills.add(measurePopulationExclusions);
			navPills.add(measureObservations);			
		} else if("RATIO".equals(scoringIdAttributeValue)){			
			addNumDenoNavPills(navPills);
			navPills.add(measureObservations);			
		}
		navPills.add(stratifications);
	}

	private void addNumDenoNavPills(NavPills navPills) {
		navPills.add(numerator);
		navPills.add(numeratorExclusions);
		navPills.add(denominator);
		navPills.add(denominatorExclusions);
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
	//	getGlobalWarningConfirmationMessageAlert().clearAlert();
		//getDeleteConfirmationMessgeAlert().clearAlert();
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
		//getDeleteConfirmationMessgeAlert().clearAlert();
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
		//getDeleteConfirmationMessgeAlert().createWarningAlert(message);
		//getDeleteConfirmationMessgeAlert().getWarningConfirmationYesButton().setFocus(true);
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

	/**
	 * Gets the delete confirmation yes button.
	 *
	 * @return the delete confirmation yes button
	 */
	/*public Button getDeleteConfirmationYesButton() {
		return deleteConfirmationMessgeAlert.getWarningConfirmationYesButton();
	}*/

	/**
	 * Gets the delete confirmation no button.
	 *
	 * @return the delete confirmation no button
	 */
	/*public Button getDeleteConfirmationNoButton() {
		return deleteConfirmationMessgeAlert.getWarningConfirmationNoButton();
	}*/




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
