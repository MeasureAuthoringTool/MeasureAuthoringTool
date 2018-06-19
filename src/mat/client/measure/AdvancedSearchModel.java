package mat.client.measure;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.util.MatTextBox;

public class AdvancedSearchModel {
	private Modal panel;
	private Input searchText;
	private FormGroup searchTextGroup = new FormGroup();
	private FormGroup searchGroup = new FormGroup();
	private ListBox searchBoxList;
	private FormGroup searchStateGroup = new FormGroup();
	private ListBox searchStateList;
	private FormGroup scoreGroup = new FormGroup();
	private CheckBox proportionCheckbox;
	private CheckBox ratioCheckbox;
	private CheckBox cohortCheckbox;
	private CheckBox contVariableCheckbox;
	private FormGroup patientIndecatorGroup = new FormGroup();
	private ListBox patientIndecatorList;
	private FormGroup modifiedGroup = new FormGroup();
	private ListBox modifiedOnList;
	private FormGroup modifiedByGroup = new FormGroup();
	private Input modifiedBy;
	private FormGroup ownedByGroup = new FormGroup();
	private Input ownedBy;
	private Button search;
	private Button cancel;
	
	private static final String MEASURES = "Measures";
	
	public AdvancedSearchModel(String type) {
		panel = new Modal();
		panel.getElement().setAttribute("role", "dialog");
	    panel.setWidth("600px");
	    
	    panel.setClosable(true);
		panel.setFade(true);
		panel.setDataBackdrop(ModalBackdrop.STATIC);
		panel.setSize(ModalSize.MEDIUM);
		panel.getElement().getStyle().setZIndex(1000);
		panel.setRemoveOnHide(true);
		panel.setTitle("Advanced Search");
		panel.getElement().setAttribute("tabindex", "0");
		
		ModalBody modalBody = new ModalBody();
		
		modalBody.add(getSearchTextSection(type));
		
		modalBody.add(getSearchBySection(type));
		
		modalBody.add(getStateSection(type));
		
		modalBody.add(getScoreSection(type));
		
		modalBody.add(getPatientSection());
		
		modalBody.add(getDaysSection(type));

		modalBody.add(getModifiedBySection(type));
		
		modalBody.add(getOwnedBySection(type));
		
		modalBody.add(getButtonSection());
		
		panel.add(modalBody);
		panel.getElement().focus();
	}
	
	private FormGroup getSearchTextSection(String type) {
		HorizontalPanel searchTextPanel = new HorizontalPanel();
		FormLabel searchTextLabel = new FormLabel();
		searchTextLabel.setText("Enter Text:");
		searchTextLabel.setTitle("Enter Text");
		searchTextLabel.setFor("searchTextId");
		searchTextLabel.setMarginRight(10);
		searchTextLabel.getElement().setTabIndex(0);
		searchText = new Input(InputType.TEXT);
		searchText.setWidth("250px");
		searchText.setHeight("27px");
		searchText.setTitle("Search Text");
		searchText.setId("searchTextId");
		searchText.setPlaceholder("Search Text");
		searchTextPanel.add(searchTextLabel);
		searchTextPanel.add(searchText);
		searchTextGroup.add(searchTextPanel);
		return searchTextGroup;
	}
	
	private FormGroup getSearchBySection(String type) {
		FormLabel searchLabel = new FormLabel();
		HorizontalPanel searchPanel = new HorizontalPanel();
		searchLabel.setText("Search By:");
		searchLabel.setTitle("Search By");
		searchLabel.setFor("SeachId");
		searchLabel.setMarginRight(10);
		searchLabel.getElement().setTabIndex(0);
		searchBoxList = new ListBox();
		searchBoxList.addItem("All " + type, "All " + type);
		searchBoxList.setId("searchTextInput");
		searchBoxList.addItem("Only My " + type, "Only My " + type);
		searchPanel.add(searchLabel);
		searchPanel.add(searchBoxList);
		searchGroup.add(searchPanel);
		return searchGroup;
	}
	
	private FormGroup getStateSection(String type) {
		FormLabel stateLabel = new FormLabel();
		HorizontalPanel statePanel = new HorizontalPanel();
		stateLabel.setText("Show Only:");
		stateLabel.setTitle("Show Only");
		stateLabel.setFor("stateId");
		stateLabel.setPaddingRight(10);
		stateLabel.getElement().setTabIndex(0);
		searchStateList = new ListBox();
		searchStateList.setId("stateGroup");
		searchStateList.addItem("All " + type, "All " + type);
		searchStateList.addItem("Draft " + type, "Draft " + type);
		searchStateList.addItem("Versioned " + type, "Versioned " + type);
		statePanel.add(stateLabel);
		statePanel.add(searchStateList);
		searchStateGroup.add(statePanel);
		return searchStateGroup;
	}
	
	private FormGroup getScoreSection(String type) {
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
		scoreGroup.add(scoreheader);
		scoreGroup.add(helpTextRow);
		scoreGroup.add(scoreRow1);
		return scoreGroup;
	}
	
	private FormGroup getPatientSection() {
		HorizontalPanel patientPanel = new HorizontalPanel();
		FormLabel patientLabel = new FormLabel();
		patientLabel.setText("Patient-Based Indicator:");
		patientLabel.setTitle("Patient-Based Indicator");
		patientLabel.setPaddingRight(10);
		patientLabel.getElement().setTabIndex(0);
		patientIndecatorList = new ListBox();
		patientIndecatorList.setId("patientBase");
		patientIndecatorList.addItem("All Measures", "All Measures");
		patientIndecatorList.addItem("Yes, Patient-based", "Yes, Patient-based");
		patientIndecatorList.addItem("No, Not Patient-based", "No, Not Patient-based");
		patientPanel.add(patientLabel);
		patientPanel.add(patientIndecatorList);
		patientIndecatorGroup.add(patientPanel);
		return patientIndecatorGroup;
	}
	
	private FormGroup getDaysSection(String type) {
		HorizontalPanel daysPanel = new HorizontalPanel();
		FormLabel daysLabel = new FormLabel();
		daysLabel.setText(type + " Last Modified Within:");
		daysLabel.setTitle(type + " Last Modified Within");
		daysLabel.setPaddingRight(10);
		daysLabel.getElement().setTabIndex(0);
		modifiedOnList = new ListBox();
		modifiedOnList.setId("modifiedDate");
		modifiedOnList.addItem("All " + type, "All " + type);
		modifiedOnList.addItem("14 days", "14 days");
		modifiedOnList.addItem("30 days", "30 days");
		modifiedOnList.addItem("60 days", "60 days");
		modifiedOnList.addItem("90 days", "90 days");
		daysPanel.add(daysLabel);
		daysPanel.add(modifiedOnList);
		modifiedGroup.add(daysPanel);
		return modifiedGroup;
	}
	
	private FormGroup getModifiedBySection(String type) {
		HorizontalPanel modifiedByPanel = new HorizontalPanel();
		FormLabel modifiedByLabel = new FormLabel();
		modifiedByLabel.setText(type + " Last Modified By:");
		modifiedByLabel.setTitle(type + " Last Modified By");
		modifiedByLabel.setPaddingRight(10);
		modifiedByLabel.getElement().setTabIndex(0);
		modifiedByLabel.setFor("modifiedById");
		modifiedBy = new Input(InputType.TEXT);
		modifiedBy.setWidth("250px");
		modifiedBy.setHeight("27px");
		modifiedBy.setId("modifiedById");
		modifiedBy.setPlaceholder("Modified By");
		modifiedBy.setTitle("Modified By");
		modifiedByPanel.add(modifiedByLabel);
		modifiedByPanel.add(modifiedBy);
		modifiedByGroup.add(modifiedByPanel);
		return modifiedByGroup;
	}
	
	private FormGroup getOwnedBySection(String type) {
		HorizontalPanel ownedByPanel = new HorizontalPanel();
		FormLabel ownedByLabel = new FormLabel();
		ownedByLabel.setText(type + " Owned By:");
		ownedByLabel.setTitle(type + " Owned By");
		ownedByLabel.setFor("ownedById");
		ownedByLabel.setPaddingRight(10);
		ownedByLabel.getElement().setTabIndex(0);
		ownedBy = new Input(InputType.TEXT);
		ownedBy.setWidth("250px");
		ownedBy.setHeight("27px");
		ownedBy.setId("ownedById");
		ownedBy.setPlaceholder("Owned By");
		ownedBy.setTitle("Owned By");
		ownedByPanel.add(ownedByLabel);
		ownedByPanel.add(ownedBy);
		ownedByGroup.add(ownedByPanel);
		return ownedByGroup;
	}
	
	private ButtonToolBar getButtonSection() {
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		
		cancel = new Button();
		cancel.setText("Cancel");
		cancel.setTitle("Cancel");
		cancel.setType(ButtonType.DANGER);
		cancel.setDataDismiss(ButtonDismiss.MODAL);
		
		search = new Button();
		search.setText("Search");
		search.setTitle("Search");
		search.setType(ButtonType.PRIMARY);
		
		buttonToolBar.add(search);
		buttonToolBar.add(cancel);
		return buttonToolBar;
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
