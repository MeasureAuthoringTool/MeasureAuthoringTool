package mat.client.clause.cqlworkspace;

import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.WarningMessageAlert;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

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

	/** The dirty flag for page. */
	private Boolean isPageDirty = false;

	/** The is double click. */
	private Boolean isDoubleClick = false;

	/** The is nav bar click. */
	private Boolean isNavBarClick = false;

	/** The Loading flag for page. */
	private Boolean isLoading = false;




	/**
	 * Builds the measure lib CQL view.
	 * @param document 
	 *
	 * @return the vertical panel
	 */
	public VerticalPanel buildMeasureLibCQLView(Document document){
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

		setTextAndIcons(initialPopulation, "Initial Populations");

		setTextAndIcons(numerator, "Numerators");
		setTextAndIcons(denominator, "Denominators");
		setTextAndIcons(numeratorExclusions, "Numerator Exclusions");
		setTextAndIcons(denominatorExclusions, "Denominator Exclusions");
		setTextAndIcons(denominatorExceptions, "Denominator Exceptions");
		setTextAndIcons(measurePopulations, "Measure Populations");
		setTextAndIcons(measurePopulationExclusions, "Measure Population Exclusions");
		setTextAndIcons(stratifications, "Stratification");
		setTextAndIcons(measureObservations, "Measure Observations");
		setTextAndIcons(viewPopulations, "View Populations");


		/**
		 * Find Scoring type for the measure from the Measure XML.
		 */
		NodeList nodeList = document.getElementsByTagName("scoring");

		if ((nodeList != null) && (nodeList.getLength() > 0)) {
			Node scoringNode = nodeList.item(0);
			Node scoringIdAttribute = scoringNode.getAttributes()
					.getNamedItem("id");
			String scoringIdAttributeValue = scoringIdAttribute.getNodeValue();

			addAchorsByScoring(navPills, scoringIdAttributeValue);
		}		

		navPills.add(viewPopulations);//View Populations is always present
		viewPopulations.setActive(true);// View Populations is initially selected.

		navPills.setWidth("200px");

		messagePanel.add(successMessageAlert);
		messagePanel.add(warningMessageAlert);
		messagePanel.add(errorMessageAlert);
		messagePanel.add(warningConfirmationMessageAlert);
		//messagePanel.add(globalWarningConfirmationMessageAlert);
		//messagePanel.add(deleteConfirmationMessgeAlert);
		messagePanel.setStyleName("marginLeft15px");

		// rightHandNavPanel.add(messagePanel);
		rightHandNavPanel.add(navPills);
	}

	private void addAchorsByScoring(NavPills navPills,
			String scoringIdAttributeValue) {
		System.out.println("Adding anchors by scoring type...");
		if ("COHORT".equals(scoringIdAttributeValue)) {
			navPills.add(initialPopulation);
			navPills.add(stratifications);
		} else if("PROPOR".equals(scoringIdAttributeValue)){
			navPills.add(initialPopulation);
			navPills.add(numerator);
			navPills.add(numeratorExclusions);
			navPills.add(denominator);
			navPills.add(denominatorExclusions);
			navPills.add(denominatorExceptions);
			navPills.add(stratifications);				
		} else if("CONTVAR".equals(scoringIdAttributeValue)){
			navPills.add(initialPopulation);
			navPills.add(measurePopulations);
			navPills.add(measurePopulationExclusions);
			navPills.add(measureObservations);
			navPills.add(stratifications);
		} else if("RATIO".equals(scoringIdAttributeValue)){
			navPills.add(initialPopulation);
			navPills.add(numerator);
			navPills.add(numeratorExclusions);
			navPills.add(denominator);
			navPills.add(denominatorExclusions);
			navPills.add(measureObservations);
			navPills.add(stratifications);
		}
	}

	private void setTextAndIcons(AnchorListItem anchorListItem, String text) {
		anchorListItem.setIcon(IconType.PENCIL);
		anchorListItem.setText(text);
		anchorListItem.setTitle(text);
		anchorListItem.setId(text+"_Anchor");	

		/*Anchor anchor = (Anchor) (anchorListItem.getWidget(0));
		// Double Click causing issues.So Event is not propogated
		anchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
//		Label anchorLabel = new Label(text);
//		anchorLabel.setStyleName("transparentLabel");
//		anchorLabel.setId("includesLabel_Label");
//		anchor.add(anchorLabel);

		anchorListItem.setDataToggle(Toggle.COLLAPSE);
		anchorListItem.setHref("#collapseIncludes");
		anchorListItem.setId("includesLibrary_Anchor");
	//	anchorListItem.add(includesCollapse);

		anchor.setDataParent("#navGroup");*/

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
	 * Sets the checks if is page dirty.
	 *
	 * @param isPageDirty the new checks if is page dirty
	 */
	public void setIsPageDirty(Boolean isPageDirty) {
		this.isPageDirty = isPageDirty;
	}

	/**
	 * Sets the checks if is double click.
	 *
	 * @param isDoubleClick the new checks if is double click
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setIsDoubleClick(java.lang.Boolean)
	 */
	public void setIsDoubleClick(Boolean isDoubleClick) {
		this.isDoubleClick = isDoubleClick;
	}

	/**
	 * Checks if is double click.
	 *
	 * @return the boolean
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * isDoubleClick()
	 */
	public Boolean isDoubleClick() {
		return isDoubleClick;
	}

	/**
	 * Sets the checks if is nav bar click.
	 *
	 * @param isNavBarClick the new checks if is nav bar click
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setIsNavBarClick(java.lang.Boolean)
	 */
	public void setIsNavBarClick(Boolean isNavBarClick) {
		this.isNavBarClick = isNavBarClick;
	}

	/**
	 * Checks if is nav bar click.
	 *
	 * @return the boolean
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * isNavBarClick()
	 */
	public Boolean isNavBarClick() {
		return isNavBarClick;
	}

	/**
	 * Gets the checks if is page dirty.
	 *
	 * @return the checks if is page dirty
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getIsPageDirty()
	 */
	public Boolean getIsPageDirty() {
		return isPageDirty;
	}

	/**
	 * Show unsaved changes warning.
	 */
	public void showUnsavedChangesWarning() {
		getWarningMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
		getSuccessMessageAlert().clearAlert();
		getGlobalWarningConfirmationMessageAlert().clearAlert();
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
