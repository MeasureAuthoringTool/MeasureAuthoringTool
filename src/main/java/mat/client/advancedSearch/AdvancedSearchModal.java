package mat.client.advancedSearch;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.buttons.CancelButton;


public class AdvancedSearchModal {	
	private final String HEIGHT_OF_BOXES = "30px";
	private final String WIDTH_OF_BOXES = "200px";
	
	private Modal panel;
	private Input searchText;
	private ModalBody modalBody;
	private HelpBlock helpBlock = new HelpBlock();

	private FormGroup searchTextGroup;
	private FormGroup searchGroup;
	private ListBox searchBoxList;
	private FormGroup searchStateGroup;
	private ListBox searchStateList;
	private FormGroup scoreGroup;
	private CheckBox proportionCheckbox;
	private CheckBox ratioCheckbox;
	private CheckBox cohortCheckbox;
	private CheckBox contVariableCheckbox;
	private FormGroup patientIndecatorGroup;
	private ListBox patientIndecatorList;
	private FormGroup modifiedGroup;
	private ListBox modifiedOnList;
	private FormGroup modifiedByGroup;
	private Input modifiedBy;
	private FormGroup ownedByGroup;
	private Input ownedBy;
	private Button search;
	private Button cancel;
	private ButtonToolBar buttonToolBar;
	private String type;
	private String pluralType;

	
	
	public AdvancedSearchModal(String type, String pluralType) {
		this.type = type;
		this.pluralType = pluralType;
		
		panel = new Modal();
		panel.getElement().setAttribute("role", "dialog");
	    panel.setWidth("600px");
	    
	    panel.setClosable(true);
		panel.setFade(true);
		panel.setDataBackdrop(ModalBackdrop.STATIC);
		panel.setSize(ModalSize.MEDIUM);
		panel.getElement().getStyle().setZIndex(9999);
		panel.setRemoveOnHide(true);
		panel.setTitle("Advanced Search");
		panel.getElement().setAttribute("tabindex", "0");

		helpBlock.setText("");
		modalBody = new ModalBody();
		modalBody.getElement().setAttribute("role", "alert");
		modalBody.add(helpBlock);
		panel.add(modalBody);
		panel.getElement().focus();
	}
		
	private void buildSearchTextSection() {
		HorizontalPanel searchTextPanel = new HorizontalPanel();
		FormLabel searchTextLabel = new FormLabel();
		searchTextLabel.setText("Enter Text:");
		searchTextLabel.setTitle("Enter Text");
		searchTextLabel.setFor("searchTextId");
		searchTextLabel.setStyleName("advancedSearchLabels");
		searchTextLabel.setMarginRight(10);
		searchTextLabel.setFor("searchTextId");
		searchText = new Input(InputType.TEXT);
		searchText.getElement().getStyle().setPaddingLeft(1, Unit.PX);
		searchText.setWidth("450px");
		searchText.setHeight(HEIGHT_OF_BOXES);
		searchText.setTitle(" Search Text");
		searchText.setId("searchTextId");
		searchText.setPlaceholder(" Search " + pluralType);
		searchTextPanel.add(searchTextLabel);
		searchTextPanel.add(searchText);
		searchTextGroup = new FormGroup();
		searchTextGroup.add(searchTextPanel);
	}
	
	private void buildSearchBySection() {
		FormLabel searchLabel = new FormLabel();
		HorizontalPanel searchPanel = new HorizontalPanel();
		searchLabel.setText("Search By:");
		searchLabel.setTitle("Search By");
		searchLabel.setFor("SeachId");
		searchLabel.setMarginRight(10);
		searchLabel.setFor("searchTextInput");
		searchLabel.setStyleName("advancedSearchLabels");
		searchBoxList = new ListBox();
		searchBoxList.setWidth(WIDTH_OF_BOXES);
		searchBoxList.setHeight(HEIGHT_OF_BOXES);
		searchBoxList.addItem("All " + pluralType, "All " + pluralType);
		searchBoxList.setId("searchTextInput");
		searchBoxList.addItem("Only My " + pluralType, "Only My " + pluralType);
		searchPanel.add(searchLabel);
		searchPanel.add(searchBoxList);
		searchGroup = new FormGroup();
		searchGroup.add(searchPanel);
	}
	
	private void buildStateSection() {
		FormLabel stateLabel = new FormLabel();
		HorizontalPanel statePanel = new HorizontalPanel();
		stateLabel.setText("Show Only:");
		stateLabel.setTitle("Show Only");
		stateLabel.setFor("stateId");
		stateLabel.setFor("stateGroup");
		stateLabel.setStyleName("advancedSearchLabels");
		stateLabel.setPaddingRight(16);
		searchStateList = new ListBox();
		searchStateList.setWidth(WIDTH_OF_BOXES);
		searchStateList.setHeight(HEIGHT_OF_BOXES);
		searchStateList.setId("stateGroup");
		searchStateList.addItem("All " + pluralType, "All " + pluralType);
		searchStateList.addItem("Draft " + pluralType, "Draft " + pluralType);
		searchStateList.addItem("Versioned " + pluralType, "Versioned " + pluralType);
		statePanel.add(stateLabel);
		statePanel.add(searchStateList);
		searchStateGroup = new FormGroup();
		searchStateGroup.add(statePanel);
	}
	
	private void buildScoreSection() {
		HorizontalPanel scoreheader = new HorizontalPanel();
		FormLabel scoreLabel = new FormLabel();
		scoreLabel.setText(type + " Scoring:");
		scoreLabel.setTitle(type + " Scoring");
		scoreLabel.setWidth("550px");
		scoreLabel.getElement().setTabIndex(0);
		scoreheader.add(scoreLabel);
		HorizontalPanel helpTextRow = new HorizontalPanel();
		FormLabel helpText = new FormLabel();
		helpText.setText("(Check all that apply. No selection will return all measure scoring types.)");
		helpText.setTitle("(Check all that apply. No selection will return all measure scoring types.)");
		helpText.setStylePrimaryName("helpText");
		helpText.getElement().setTabIndex(0);
		helpTextRow.add(helpText);
		HorizontalPanel scoreRow1 = new HorizontalPanel();
		proportionCheckbox = new CheckBox("Proportion");
		proportionCheckbox.setTitle("Proportion");
		proportionCheckbox.getElement().setAttribute("style", "padding-left: 20px;");
		ratioCheckbox = new CheckBox("Ratio");
		ratioCheckbox.setTitle("Ratio");
		ratioCheckbox.getElement().setAttribute("style", "padding-left: 20px;");
		cohortCheckbox = new CheckBox("Cohort");
		cohortCheckbox.setTitle("Cohort");
		cohortCheckbox.getElement().setAttribute("style", "padding-left: 20px;");
		contVariableCheckbox = new CheckBox("Continuous");
		contVariableCheckbox.setTitle("Continuous");
		contVariableCheckbox.getElement().setAttribute("style", "padding-left: 20px;");
		scoreRow1.add(cohortCheckbox);
		scoreRow1.add(contVariableCheckbox);
		scoreRow1.add(proportionCheckbox);
		scoreRow1.add(ratioCheckbox);
		scoreGroup = new FormGroup();
		scoreGroup.add(scoreheader);
		scoreGroup.add(helpTextRow);
		scoreGroup.add(scoreRow1);
	}

	private void buildPatientSection() {
		HorizontalPanel patientPanel = new HorizontalPanel();
		FormLabel patientLabel = new FormLabel();
		patientLabel.setText("Patient-Based Indicator:");
		patientLabel.setTitle("Patient-Based Indicator");
		patientLabel.setStyleName("advancedSearchLabels");
		patientLabel.setFor("patientBase");
		patientIndecatorList = new ListBox();
		patientIndecatorList.setHeight(HEIGHT_OF_BOXES);
		patientIndecatorList.setWidth(WIDTH_OF_BOXES);
		patientIndecatorList.setId("patientBase");
		patientIndecatorList.addItem("All Measures", "All Measures");
		patientIndecatorList.addItem("Yes, Patient-based", "Yes, Patient-based");
		patientIndecatorList.addItem("No, Not Patient-based", "No, Not Patient-based");
		patientPanel.add(patientLabel);
		patientPanel.add(patientIndecatorList);
		patientIndecatorGroup = new FormGroup();
		patientIndecatorGroup.add(patientPanel);
	}

	private void buildDaysSection() {
		HorizontalPanel daysPanel = new HorizontalPanel();
		FormLabel daysLabel = new FormLabel();
		daysLabel.setText(type + " Last Modified Within:");
		daysLabel.setTitle(type + " Last Modified Within");
		daysLabel.setStyleName("advancedSearchLabels");
		daysLabel.setFor("modifiedDate");
		modifiedOnList = new ListBox();
		modifiedOnList.setWidth(WIDTH_OF_BOXES);
		modifiedOnList.setHeight(HEIGHT_OF_BOXES);
		modifiedOnList.setId("modifiedDate");
		modifiedOnList.addItem("All " + pluralType, "All " + pluralType);
		modifiedOnList.addItem("14 days", "14 days");
		modifiedOnList.addItem("30 days", "30 days");
		modifiedOnList.addItem("60 days", "60 days");
		modifiedOnList.addItem("90 days", "90 days");
		daysPanel.add(daysLabel);
		daysPanel.add(modifiedOnList);
		modifiedGroup = new FormGroup();
		modifiedGroup.add(daysPanel);
	}
	
	private void buildModifiedBySection() {
		HorizontalPanel modifiedByPanel = new HorizontalPanel();
		FormLabel modifiedByLabel = new FormLabel();
		modifiedByLabel.setText(type + " Last Modified By:");
		modifiedByLabel.setTitle(type + " Last Modified By");
		modifiedByLabel.setFor("modifiedById");
		modifiedByLabel.setStyleName("advancedSearchLabels");
		modifiedBy = new Input(InputType.TEXT);
		modifiedBy.setWidth("250px");
		modifiedBy.setHeight(HEIGHT_OF_BOXES);
		modifiedBy.setId("modifiedById");
		modifiedBy.setPlaceholder(" Modified By");
		modifiedBy.setTitle(" Modified By");
		modifiedByPanel.add(modifiedByLabel);
		modifiedByPanel.add(modifiedBy);
		modifiedByGroup = new FormGroup();
		modifiedByGroup.add(modifiedByPanel);
	}

	private void buildOwnedBySection() {
		HorizontalPanel ownedByPanel = new HorizontalPanel();
		FormLabel ownedByLabel = new FormLabel();
		ownedByLabel.setText(type + " Owned By:");
		ownedByLabel.setTitle(type + " Owned By");
		ownedByLabel.setFor("ownedById");
		ownedByLabel.setStyleName("advancedSearchLabels");
		ownedBy = new Input(InputType.TEXT);
		ownedBy.setWidth("250px");
		ownedBy.setHeight(HEIGHT_OF_BOXES);
		ownedBy.setId("ownedById");
		ownedBy.setPlaceholder(" Owned By");
		ownedBy.setTitle(" Owned By");
		ownedByPanel.add(ownedByLabel);
		ownedByPanel.add(ownedBy);
		ownedByGroup = new FormGroup();
		ownedByGroup.add(ownedByPanel);
	}

	private void buildButtonSection() {
		buttonToolBar = new ButtonToolBar();
		
		cancel = new CancelButton("AdvancedSearchModal");
		cancel.setDataDismiss(ButtonDismiss.MODAL);
		
		search = new Button();
		search.setText("Search");
		search.setTitle("Search");
		search.setType(ButtonType.PRIMARY);
		
		buttonToolBar.add(search);
		buttonToolBar.add(cancel);
	}
	
	public void addSearchTextSection() {
		buildSearchTextSection();
		modalBody.add(searchTextGroup);
	}
	
	public void addSearchBySection() {
		buildSearchBySection();
		modalBody.add(searchGroup);
	}
	
	public void addStateSection() {
		buildStateSection();
		modalBody.add(searchStateGroup);
	}
	
	public void addScoreSection() {
		buildScoreSection();
		modalBody.add(scoreGroup);
	}
	
	public void addPatientSection() {
		buildPatientSection();
		modalBody.add(patientIndecatorGroup);
	}
	
	public void addDaysSection() {
		buildDaysSection();
		modalBody.add(modifiedGroup);
	}
	
	public void addModifiedBySection() {
		buildModifiedBySection();
		modalBody.add(modifiedByGroup);
	}
	
	public void addOwnedBySection() {
		buildOwnedBySection();
		modalBody.add(ownedByGroup);
	}
	
	public void addButtonSection() {
		buildButtonSection();
		modalBody.add(buttonToolBar);
	}
	
	/*Hidden panel with info about what the modal does for 508 compliance*/
	public void setHelpBlock (String message) {
		helpBlock.setColor("transparent");
		helpBlock.setVisible(false);
		helpBlock.setHeight("0px");
		helpBlock.setText(message);
	}
	
	public Modal getPanel() {
		return panel;
	}

	public void setPanel(Modal panel) {
		this.panel = panel;
	}

	public Input getSearchText() {
		return searchText;
	}

	public void setSearchText(Input searchText) {
		this.searchText = searchText;
	}

	public FormGroup getSearchTextGroup() {
		return searchTextGroup;
	}

	public void setSearchTextGroup(FormGroup searchTextGroup) {
		this.searchTextGroup = searchTextGroup;
	}

	public FormGroup getSearchGroup() {
		return searchGroup;
	}

	public void setSearchGroup(FormGroup searchGroup) {
		this.searchGroup = searchGroup;
	}

	public FormGroup getSearchStateGroup() {
		return searchStateGroup;
	}

	public void setSearchStateGroup(FormGroup searchStateGroup) {
		this.searchStateGroup = searchStateGroup;
	}

	public FormGroup getScoreGroup() {
		return scoreGroup;
	}

	public void setScoreGroup(FormGroup scoreGroup) {
		this.scoreGroup = scoreGroup;
	}

	public CheckBox getProportionCheckbox() {
		return proportionCheckbox;
	}

	public void setProportionCheckbox(CheckBox proportionCheckbox) {
		this.proportionCheckbox = proportionCheckbox;
	}

	public CheckBox getRatioCheckbox() {
		return ratioCheckbox;
	}

	public void setRatioCheckbox(CheckBox ratioCheckbox) {
		this.ratioCheckbox = ratioCheckbox;
	}

	public CheckBox getCohortCheckbox() {
		return cohortCheckbox;
	}

	public void setCohortCheckbox(CheckBox cohortCheckbox) {
		this.cohortCheckbox = cohortCheckbox;
	}

	public CheckBox getContVariableCheckbox() {
		return contVariableCheckbox;
	}

	public void setContVariableCheckbox(CheckBox contVariableCheckbox) {
		this.contVariableCheckbox = contVariableCheckbox;
	}

	public FormGroup getPatientIndecatorGroup() {
		return patientIndecatorGroup;
	}

	public void setPatientIndecatorGroup(FormGroup patientIndecatorGroup) {
		this.patientIndecatorGroup = patientIndecatorGroup;
	}

	public FormGroup getModifiedGroup() {
		return modifiedGroup;
	}

	public void setModifiedGroup(FormGroup modifiedGroup) {
		this.modifiedGroup = modifiedGroup;
	}

	public FormGroup getModifiedByGroup() {
		return modifiedByGroup;
	}

	public void setModifiedByGroup(FormGroup modifiedByGroup) {
		this.modifiedByGroup = modifiedByGroup;
	}

	public Input getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Input modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public FormGroup getOwnedByGroup() {
		return ownedByGroup;
	}

	public void setOwnedByGroup(FormGroup ownedByGroup) {
		this.ownedByGroup = ownedByGroup;
	}

	public Input getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(Input ownedBy) {
		this.ownedBy = ownedBy;
	}

	public Button getSearch() {
		return search;
	}

	public void setSearch(Button search) {
		this.search = search;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public void showAdvanceSearch() {
		panel.show();
	}
	
	public void closeAdvanceSearch() {
		panel.hide();
	}
	
	public ListBox getSearchBoxList() {
		return searchBoxList;
	}

	public void setSearchBoxList(ListBox searchBoxList) {
		this.searchBoxList = searchBoxList;
	}

	public ListBox getSearchStateList() {
		return searchStateList;
	}

	public void setSearchStateList(ListBox searchStateList) {
		this.searchStateList = searchStateList;
	}

	public ListBox getPatientIndecatorList() {
		return patientIndecatorList;
	}

	public void setPatientIndecatorList(ListBox patientIndecatorList) {
		this.patientIndecatorList = patientIndecatorList;
	}

	public ListBox getModifiedOnList() {
		return modifiedOnList;
	}

	public void setModifiedOnList(ListBox modifiedOnList) {
		this.modifiedOnList = modifiedOnList;
	}
}
