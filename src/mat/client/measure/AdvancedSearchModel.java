package mat.client.measure;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class AdvancedSearchModel {
	private Modal panel;
	private Input searchText;
	private FormGroup searchTextGroup = new FormGroup();
	private FormGroup searchGroup = new FormGroup();
	private RadioButton myRadio;
	private RadioButton allRadio;
	private FormGroup searchStateGroup = new FormGroup();
	private RadioButton draftRadio;
	private RadioButton versionRadio;
	private RadioButton allStateRadio;
	private FormGroup scoreGroup = new FormGroup();
	private CheckBox proportionCheckbox;
	private CheckBox ratioCheckbox;
	private CheckBox cohortCheckbox;
	private CheckBox contVariableCheckbox;
	private FormGroup patientIndecatorGroup = new FormGroup();
	private RadioButton patientBasedRadio;
	private RadioButton nonPatientBasedRadio;
	private RadioButton allPatientsBasedRadio;
	private FormGroup modifiedGroup = new FormGroup();
	private RadioButton fourteenDaysRadio;
	private RadioButton thirtyDaysRadio;
	private RadioButton sixtyDaysRadio;
	private RadioButton nintyDaysRadio;
	private RadioButton allDaysRadio;
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
		panel.getElement().setAttribute("tabindex", "0");
		//panel.setTitle("Advanced Search");
		ModalHeader modalHeader = new ModalHeader();
		HorizontalPanel header = new HorizontalPanel();
		header.setWidth("550px");
		HTML loginText = new HTML("Advanced Search");
		header.add(loginText);
		header.setStylePrimaryName("advanceSearchHeader");
		header.setHeight("30px");
		modalHeader.setHeight("50px");
		modalHeader.add(header);
				
		panel.add(modalHeader);
		
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
		HorizontalPanel searchTextHeader = new HorizontalPanel();
		FormLabel searchTextLabel = new FormLabel();
		searchTextLabel.setText("Enter Text:");
		searchTextLabel.setTitle("Enter Text");
		searchTextLabel.setFor("searchTextId");
		searchTextLabel.setStylePrimaryName("searchTextLabel");
		searchTextLabel.getElement().setTabIndex(0);
		searchTextHeader.add(searchTextLabel);
		HorizontalPanel searchTextRow1 = new HorizontalPanel();
		searchText = new Input(InputType.TEXT);
		searchText.setWidth("250px");
		searchText.setHeight("27px");
		searchText.setId("searchTextId");
		searchText.setPlaceholder("Search Text");
		searchText.setTitle("Search Text");
		searchText.setStylePrimaryName("searchTextInput");
		searchTextRow1.add(searchText);
		searchTextGroup.add(searchTextHeader);
		searchTextGroup.add(searchTextRow1);
		return searchTextGroup;
	}
	
	private FormGroup getSearchBySection(String type) {
		FormLabel searchLabel = new FormLabel();
		HorizontalPanel searchHeader = new HorizontalPanel();
		searchLabel.setText("Search by:");
		searchLabel.setTitle("Search by");
		searchLabel.setFor("SeachId");
		searchLabel.setStylePrimaryName("searchTextLabel");
		searchLabel.getElement().setTabIndex(0);
		searchHeader.add(searchLabel);
		HorizontalPanel searchRow1 = new HorizontalPanel();
		myRadio = new RadioButton("searchGroup", "Only my " + type);
		myRadio.setTitle("Only my " + type);
		myRadio.setStylePrimaryName("searchTextInput");
		myRadio.getElement().setTabIndex(0);
		allRadio = new RadioButton("searchGroup", "All " + type);
		allRadio.setTitle("All " + type);
		allRadio.setStylePrimaryName("searchTextInput");
		allRadio.getElement().setTabIndex(0);
		allRadio.setValue(true);
		searchRow1.add(myRadio);
		searchRow1.add(allRadio);
		searchGroup.add(searchHeader);
		searchGroup.add(searchRow1);
		return searchGroup;
	}
	
	private FormGroup getStateSection(String type) {
		FormLabel stateLabel = new FormLabel();
		HorizontalPanel stateHeader = new HorizontalPanel();
		stateLabel.setText("Show only:");
		stateLabel.setTitle("Show only");
		stateLabel.setFor("stateId");
		stateLabel.setStylePrimaryName("searchTextLabel");
		stateLabel.getElement().setTabIndex(0);
		stateHeader.add(stateLabel);
		HorizontalPanel stateRow1 = new HorizontalPanel();
		draftRadio = new RadioButton("stateGroup", "Draft " + type);
		draftRadio.setTitle("Draft " + type);
		draftRadio.setStylePrimaryName("searchTextInput");
		draftRadio.getElement().setTabIndex(0);
		versionRadio = new RadioButton("stateGroup", "Versioned " + type);
		versionRadio.setTitle("Versioned" + type);
		versionRadio.setStylePrimaryName("searchTextInput");
		versionRadio.getElement().setTabIndex(0);
		allStateRadio = new RadioButton("stateGroup", "All " + type);
		allStateRadio.setTitle("All " + type);
		allStateRadio.setStylePrimaryName("searchTextInput");
		allStateRadio.getElement().setTabIndex(0);
		allStateRadio.setValue(true);
		stateRow1.add(draftRadio);
		stateRow1.add(versionRadio);
		stateRow1.add(allStateRadio);
		searchStateGroup.add(stateHeader);
		searchStateGroup.add(stateRow1);
		return searchStateGroup;
	}
	
	private FormGroup getScoreSection(String type) {
		HorizontalPanel scoreheader = new HorizontalPanel();
		FormLabel scoreLabel = new FormLabel();
		scoreLabel.setText(type + " Score:");
		scoreLabel.setTitle(type + " Score");
		scoreLabel.setStylePrimaryName("searchTextLabel");
		scoreLabel.getElement().setTabIndex(0);
		scoreheader.add(scoreLabel);
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
		scoreGroup.add(scoreRow1);
		return scoreGroup;
	}
	
	private FormGroup getPatientSection() {
		HorizontalPanel patientHeader = new HorizontalPanel();
		FormLabel patientLabel = new FormLabel();
		patientLabel.setText("Patient-Based Indicator:");
		patientLabel.setTitle("Patient-Based Indicator");
		patientLabel.setStylePrimaryName("searchTextLabel");
		patientLabel.setWidth("550px");
		patientLabel.getElement().setTabIndex(0);
		patientHeader.add(patientLabel);
		HorizontalPanel patientRow1 = new HorizontalPanel();
		patientBasedRadio = new RadioButton("patientBase", "Yes, Patient-based");
		patientBasedRadio.setTitle("Yes, Patient-based");
		patientBasedRadio.setStylePrimaryName("searchTextInput");
		patientBasedRadio.getElement().setTabIndex(0);
		nonPatientBasedRadio = new RadioButton("patientBase", "No, not Patient-based");
		nonPatientBasedRadio.setTitle("No, not Patient-based");
		nonPatientBasedRadio.setStylePrimaryName("searchTextInput");
		nonPatientBasedRadio.getElement().setTabIndex(0);
		patientRow1.add(patientBasedRadio);
		patientRow1.add(nonPatientBasedRadio);
		allPatientsBasedRadio = new RadioButton("patientBase", "All Measures");
		allPatientsBasedRadio.setTitle("All Measures");
		allPatientsBasedRadio.setStylePrimaryName("searchTextInput");
		allPatientsBasedRadio.getElement().setTabIndex(0);
		allPatientsBasedRadio.setValue(true);
		patientRow1.add(allPatientsBasedRadio);
		patientIndecatorGroup.add(patientHeader);
		patientIndecatorGroup.add(patientRow1);
		return patientIndecatorGroup;
	}
	
	private FormGroup getDaysSection(String type) {
		HorizontalPanel daysHeader = new HorizontalPanel();
		FormLabel daysLabel = new FormLabel();
		daysLabel.setText(type + " Last Modified Within:");
		daysLabel.setTitle(type + " Last Modified Within");
		daysLabel.setStylePrimaryName("searchTextLabel");
		daysLabel.setWidth("550px");
		daysLabel.getElement().setTabIndex(0);
		daysHeader.add(daysLabel);
		HorizontalPanel daysRow1 = new HorizontalPanel();
		fourteenDaysRadio = new RadioButton("modifiedDate", "14 days");
		fourteenDaysRadio.setTitle("14 days");
		fourteenDaysRadio.setStylePrimaryName("searchTextInput");
		fourteenDaysRadio.getElement().setTabIndex(0);
		thirtyDaysRadio = new RadioButton("modifiedDate", "30 days");
		thirtyDaysRadio.setTitle("30 days");
		thirtyDaysRadio.setStylePrimaryName("searchTextInput");
		thirtyDaysRadio.getElement().setTabIndex(0);
		daysRow1.add(fourteenDaysRadio);
		daysRow1.add(thirtyDaysRadio);
		sixtyDaysRadio = new RadioButton("modifiedDate", "60 days");
		sixtyDaysRadio.setTitle("60 days");
		sixtyDaysRadio.setStylePrimaryName("searchTextInput");
		sixtyDaysRadio.getElement().setTabIndex(0);
		nintyDaysRadio = new RadioButton("modifiedDate", "90 days");
		nintyDaysRadio.setTitle("90 days");
		nintyDaysRadio.setStylePrimaryName("searchTextInput"); 
		nintyDaysRadio.getElement().setTabIndex(0);
		daysRow1.add(sixtyDaysRadio);
		daysRow1.add(nintyDaysRadio);
		allDaysRadio = new RadioButton("modifiedDate", "All " + type);
		allDaysRadio.setTitle("All " + type);
		allDaysRadio.setStylePrimaryName("searchTextInput");
		allDaysRadio.getElement().setTabIndex(0);
		allDaysRadio.setValue(true);
		daysRow1.add(allDaysRadio);
		modifiedGroup.add(daysHeader);
		modifiedGroup.add(daysRow1);
		return modifiedGroup;
	}
	
	private FormGroup getModifiedBySection(String type) {
		HorizontalPanel modifiedByHeader = new HorizontalPanel();
		FormLabel modifiedByLabel = new FormLabel();
		modifiedByLabel.setText(type + " Last Modified By:");
		modifiedByLabel.setTitle(type + " Last Modified By");
		modifiedByLabel.setStylePrimaryName("searchTextLabel");
		modifiedByLabel.setWidth("550px");
		modifiedByLabel.getElement().setTabIndex(0);
		modifiedByLabel.setFor("modifiedById");
		modifiedByHeader.add(modifiedByLabel);
		HorizontalPanel modifiedRow1 = new HorizontalPanel();
		modifiedBy = new Input(InputType.TEXT);
		modifiedBy.setId("modifiedById");
		modifiedBy.setWidth("250px");
		modifiedBy.setHeight("27px");
		modifiedBy.setId("modifiedById");
		modifiedBy.setPlaceholder("Modified By");
		modifiedBy.setTitle("Modified By");
		modifiedBy.setStylePrimaryName("searchTextInput");
		modifiedRow1.add(modifiedBy);
		modifiedByGroup.add(modifiedByHeader);
		modifiedByGroup.add(modifiedRow1);
		return modifiedByGroup;
	}
	
	private FormGroup getOwnedBySection(String type) {
		HorizontalPanel ownedByHeader = new HorizontalPanel();
		FormLabel ownedByLabel = new FormLabel();
		ownedByLabel.setText(type + " Owned Modified By:");
		ownedByLabel.setTitle(type + " Owned Modified By");
		ownedByLabel.setStylePrimaryName("searchTextLabel");
		ownedByLabel.setWidth("550px");
		ownedByLabel.getElement().setTabIndex(0);
		ownedByLabel.setFor("ownedById");
		ownedByHeader.add(ownedByLabel);
		HorizontalPanel ownedByRow1 = new HorizontalPanel();
		ownedBy = new Input(InputType.TEXT);
		ownedBy.setWidth("250px");
		ownedBy.setHeight("27px");
		ownedBy.setId("ownedById");
		ownedBy.setPlaceholder("Owned By");
		ownedBy.setTitle("Owned By");
		ownedBy.setStylePrimaryName("searchTextInput");
		ownedByRow1.add(ownedBy);
		ownedByGroup.add(ownedByHeader);
		ownedByGroup.add(ownedByRow1);
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
		
		buttonToolBar.add(cancel);
		buttonToolBar.add(search);
		cancel.setPull(Pull.RIGHT);
		search.setPull(Pull.RIGHT);
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

	public RadioButton getMyRadio() {
		return myRadio;
	}

	public void setMyRadio(RadioButton myRadio) {
		this.myRadio = myRadio;
	}

	public RadioButton getAllRadio() {
		return allRadio;
	}

	public void setAllRadio(RadioButton allRadio) {
		this.allRadio = allRadio;
	}

	public FormGroup getSearchStateGroup() {
		return searchStateGroup;
	}

	public void setSearchStateGroup(FormGroup searchStateGroup) {
		this.searchStateGroup = searchStateGroup;
	}

	public RadioButton getDraftRadio() {
		return draftRadio;
	}

	public void setDraftRadio(RadioButton draftRadio) {
		this.draftRadio = draftRadio;
	}

	public RadioButton getVersionRadio() {
		return versionRadio;
	}

	public void setVersionRadio(RadioButton versionRadio) {
		this.versionRadio = versionRadio;
	}

	public RadioButton getAllStateRadio() {
		return allStateRadio;
	}

	public void setAllStateRadio(RadioButton allStateRadio) {
		this.allStateRadio = allStateRadio;
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

	public RadioButton getPatientBasedRadio() {
		return patientBasedRadio;
	}

	public void setPatientBasedRadio(RadioButton patientBasedRadio) {
		this.patientBasedRadio = patientBasedRadio;
	}

	public RadioButton getNonPatientBasedRadio() {
		return nonPatientBasedRadio;
	}

	public void setNonPatientBasedRadio(RadioButton nonPatientBasedRadio) {
		this.nonPatientBasedRadio = nonPatientBasedRadio;
	}

	public RadioButton getAllPatientsBasedRadio() {
		return allPatientsBasedRadio;
	}

	public void setAllPatientsBasedRadio(RadioButton allPatientsBasedRadio) {
		this.allPatientsBasedRadio = allPatientsBasedRadio;
	}

	public FormGroup getModifiedGroup() {
		return modifiedGroup;
	}

	public void setModifiedGroup(FormGroup modifiedGroup) {
		this.modifiedGroup = modifiedGroup;
	}

	public RadioButton getFourteenDaysRadio() {
		return fourteenDaysRadio;
	}

	public void setFourteenDaysRadio(RadioButton fourteenDaysRadio) {
		this.fourteenDaysRadio = fourteenDaysRadio;
	}

	public RadioButton getThirtyDaysRadio() {
		return thirtyDaysRadio;
	}

	public void setThirtyDaysRadio(RadioButton thirtyDaysRadio) {
		this.thirtyDaysRadio = thirtyDaysRadio;
	}

	public RadioButton getSixtyDaysRadio() {
		return sixtyDaysRadio;
	}

	public void setSixtyDaysRadio(RadioButton sixtyDaysRadio) {
		this.sixtyDaysRadio = sixtyDaysRadio;
	}

	public RadioButton getNintyDaysRadio() {
		return nintyDaysRadio;
	}

	public void setNintyDaysRadio(RadioButton nintyDaysRadio) {
		this.nintyDaysRadio = nintyDaysRadio;
	}

	public RadioButton getAllDaysRadio() {
		return allDaysRadio;
	}

	public void setAllDaysRadio(RadioButton allDaysRadio) {
		this.allDaysRadio = allDaysRadio;
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
}
