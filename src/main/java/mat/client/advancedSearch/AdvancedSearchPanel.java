package mat.client.advancedSearch;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.shared.ConstantMessages;
import mat.shared.MeasureSearchModel.PatientBasedType;
import mat.shared.MeasureSearchModel.VersionMeasureType;

public class AdvancedSearchPanel {

	
	private static final String CHECK_ALL_THAT_APPLY = "(Check all that apply. No selection will return all measure scoring types.)";
	private static final String MEASURE_SCORING = "Measure Scoring";
	private static final String NO_NOT_PATIENT_BASED = "No, Not Patient-based";
	private static final String YES_PATIENT_BASED = "Yes, Patient-based";
	private static final String ALL_MEASURES = "All Measures";
	private static final String HEIGHT_OF_BOXES = "30px";
	private static final String WIDTH_OF_BOXES = "200px";
	private static final String ADVANCED_SEARCH_STYLE = "advancedSearchLabels";
	
	private Anchor advanceSearchAnchor = new Anchor();
	HorizontalPanel anchorPanel;
	PanelCollapse panelCollapse;
	
	private ListBox searchStateListBox;
	private FormGroup searchStateGroup;
	
	private FormGroup patientIndicatorGroup;
	private ListBox patientIndicatorListBox;
	private ListBox modifiedOnList;
	
	private CheckBox proportionCheckbox;
	private CheckBox ratioCheckbox;
	private CheckBox cohortCheckbox;
	private CheckBox contVariableCheckbox;
	
	private FormGroup scoreGroup;
	private FormGroup modifiedGroup;
	private FormGroup modifiedByGroup;
	private FormGroup ownedByGroup;
	
	private Input modifiedBy;
	private Input ownedBy;
	
	public AdvancedSearchPanel(String forView) {
		setUpAnchorElement(forView);
		
		setUpCollapsePanel(forView);
	}


	private void setUpAnchorElement(String forView) {
		anchorPanel = new HorizontalPanel();
		anchorPanel.setWidth("100%");
		anchorPanel.setStyleName("advancedSearchAnchor");
		addAdvancedSearchLink(forView);
		anchorPanel.add(advanceSearchAnchor);
	}
	
	
	private void addAdvancedSearchLink(String forView) {
		advanceSearchAnchor.setDataToggle(Toggle.COLLAPSE);
		advanceSearchAnchor.setHref("#advancedPanelCollapse" + forView);
		advanceSearchAnchor.setText("Advanced Search");
		advanceSearchAnchor.setTitle("Advanced Search");
		
	}
	
	private Panel setUpCollapsePanel(String forView) {
		panelCollapse = new PanelCollapse();
		panelCollapse.setId("advancedPanelCollapse" + forView);
		panelCollapse.add(createAdvancedSearchMeasureContent(forView));

		return panelCollapse;
	}
	
	private VerticalPanel createAdvancedSearchMeasureContent(String forView) {
		String pluralType = "Measures";
		String type = "Measure";
		
		VerticalPanel advancedSearchContentPanel = new VerticalPanel();
		advancedSearchContentPanel.setWidth("100%");

		HorizontalPanel advancedSearchRow1 = new HorizontalPanel();
		buildStateSection(pluralType);
		buildPatientSection();
		buildScoreSection();
		buildDaysSection(type, pluralType);
		buildModifiedBySection(type);
		buildOwnedBySection(type);
		
		advancedSearchRow1.add(searchStateGroup);
		advancedSearchRow1.add(patientIndicatorGroup);
		advancedSearchRow1.add(scoreGroup);
		
		HorizontalPanel advancedSearchRow2 = new HorizontalPanel();
		advancedSearchRow2.add(modifiedGroup);
		advancedSearchRow2.add(modifiedByGroup);
		advancedSearchRow2.add(ownedByGroup);
		
		if("forMeasure".equals(forView)) {
			advancedSearchContentPanel.add(advancedSearchRow1);
			advancedSearchContentPanel.add(advancedSearchRow2);
		}
		return advancedSearchContentPanel;
	}
	
	private void buildStateSection(String pluralType) {
		FormLabel stateLabel = new FormLabel();
		stateLabel.setText("Show Only");
		stateLabel.setTitle("Show Only");
		stateLabel.setFor("stateId");
		stateLabel.setFor("stateGroup");
		stateLabel.setPaddingRight(16);
		searchStateListBox = new ListBox();
		searchStateListBox.setWidth(WIDTH_OF_BOXES);
		searchStateListBox.setHeight(HEIGHT_OF_BOXES);
		searchStateListBox.setId("stateGroup");
		searchStateListBox.addItem("All " + pluralType, VersionMeasureType.ALL.toString());
		searchStateListBox.addItem("Draft " + pluralType, VersionMeasureType.DRAFT.toString());
		searchStateListBox.addItem("Versioned " + pluralType, VersionMeasureType.VERSION.toString());

		searchStateGroup = new FormGroup();
		searchStateGroup.add(stateLabel);
		searchStateGroup.add(searchStateListBox);
		searchStateGroup.setStyleName(ADVANCED_SEARCH_STYLE);
	}
	
	private void buildPatientSection() {
		FormLabel patientLabel = new FormLabel();
		patientLabel.setText("Patient-Based Indicator");
		patientLabel.setTitle("Patient-Based Indicator");
		patientLabel.setFor("patientBase");
		patientIndicatorListBox = new ListBox();
		patientIndicatorListBox.setHeight(HEIGHT_OF_BOXES);
		patientIndicatorListBox.setWidth(WIDTH_OF_BOXES);
		patientIndicatorListBox.setId("patientBase");
		patientIndicatorListBox.addItem(ALL_MEASURES, PatientBasedType.ALL.toString());
		patientIndicatorListBox.addItem(YES_PATIENT_BASED, PatientBasedType.PATIENT.toString());
		patientIndicatorListBox.addItem(NO_NOT_PATIENT_BASED, PatientBasedType.NOT_PATIENT.toString());

		patientIndicatorGroup = new FormGroup();
		patientIndicatorGroup.add(patientLabel);
		patientIndicatorGroup.add(patientIndicatorListBox);
		patientIndicatorGroup.setStyleName(ADVANCED_SEARCH_STYLE);
	}
	
	private void buildScoreSection() {
		HorizontalPanel scoreheader = new HorizontalPanel();
		FormLabel scoreLabel = new FormLabel();
		scoreLabel.setText( MEASURE_SCORING);
		scoreLabel.setTitle(MEASURE_SCORING);
		scoreLabel.getElement().setTabIndex(0);
		scoreheader.add(scoreLabel);
		FormLabel helpText = new FormLabel();
		helpText.setText(CHECK_ALL_THAT_APPLY);
		helpText.setTitle(CHECK_ALL_THAT_APPLY);
		helpText.setStylePrimaryName("helpText");
		helpText.getElement().setTabIndex(0);
		helpText.getElement().setAttribute("style", "font-size: 10px;");
		scoreheader.add(helpText);
		HorizontalPanel scoreRow1 = new HorizontalPanel();
		proportionCheckbox = new CheckBox(ConstantMessages.PROPORTION_SCORING);
		proportionCheckbox.setTitle(ConstantMessages.PROPORTION_SCORING);
		proportionCheckbox.getElement().setAttribute("style", "padding-left: 20px;");
		ratioCheckbox = new CheckBox(ConstantMessages.RATIO_SCORING);
		ratioCheckbox.setTitle(ConstantMessages.RATIO_SCORING);
		ratioCheckbox.getElement().setAttribute("style", "padding-left: 20px;");
		cohortCheckbox = new CheckBox(ConstantMessages.COHORT_SCORING);
		cohortCheckbox.setTitle(ConstantMessages.COHORT_SCORING);
		contVariableCheckbox = new CheckBox(ConstantMessages.CONTINUOUS_VARIABLE_SCORING);
		contVariableCheckbox.setTitle(ConstantMessages.CONTINUOUS_VARIABLE_SCORING);
		contVariableCheckbox.getElement().setAttribute("style", "padding-left: 20px;");
		scoreRow1.add(cohortCheckbox);
		scoreRow1.add(contVariableCheckbox);
		scoreRow1.add(proportionCheckbox);
		scoreRow1.add(ratioCheckbox);
				
		
		scoreGroup = new FormGroup();
		scoreGroup.add(scoreheader);
		scoreGroup.add(scoreRow1);
		scoreGroup.setStyleName(ADVANCED_SEARCH_STYLE);
	}

	private void buildDaysSection(String type, String pluralType) {
		FormLabel daysLabel = new FormLabel();
		daysLabel.setText(type + " Last Modified Within:");
		daysLabel.setTitle(type + " Last Modified Within");
		daysLabel.setFor("modifiedDate");
		
		modifiedOnList = new ListBox();
		modifiedOnList.setWidth(WIDTH_OF_BOXES);
		modifiedOnList.setHeight(HEIGHT_OF_BOXES);
		modifiedOnList.setId("modifiedDate");
		modifiedOnList.addItem("All " + pluralType, "0");
		modifiedOnList.addItem("14 days", "14");
		modifiedOnList.addItem("30 days", "30");
		modifiedOnList.addItem("60 days", "60");
		modifiedOnList.addItem("90 days", "90");

		modifiedGroup = new FormGroup();
		modifiedGroup.add(daysLabel);
		modifiedGroup.add(modifiedOnList);
		modifiedGroup.setStyleName(ADVANCED_SEARCH_STYLE);
	}
	
	private void buildModifiedBySection(String type) {
		FormLabel modifiedByLabel = new FormLabel();
		modifiedByLabel.setText(type + " Last Modified By:");
		modifiedByLabel.setTitle(type + " Last Modified By");
		modifiedByLabel.setFor("modifiedById");

		modifiedBy = new Input(InputType.TEXT);
		modifiedBy.setWidth("250px");
		modifiedBy.setHeight(HEIGHT_OF_BOXES);
		modifiedBy.setId("modifiedById");
		modifiedBy.setPlaceholder(" Modified By");
		modifiedBy.setTitle(" Modified By");
		
		modifiedByGroup = new FormGroup();
		modifiedByGroup.add(modifiedByLabel);
		modifiedByGroup.add(modifiedBy);
		modifiedByGroup.setStyleName(ADVANCED_SEARCH_STYLE);
	}

	private void buildOwnedBySection(String type) {
		FormLabel ownedByLabel = new FormLabel();
		ownedByLabel.setText(type + " Owned By:");
		ownedByLabel.setTitle(type + " Owned By");
		ownedByLabel.setFor("ownedById");
		
		ownedBy = new Input(InputType.TEXT);
		ownedBy.setWidth("250px");
		ownedBy.setHeight(HEIGHT_OF_BOXES);
		ownedBy.setId("ownedById");
		ownedBy.setPlaceholder(" Owned By");
		ownedBy.setTitle(" Owned By");
		
		ownedByGroup = new FormGroup();
		ownedByGroup.add(ownedByLabel);
		ownedByGroup.add(ownedBy);
		ownedByGroup.setStyleName(ADVANCED_SEARCH_STYLE);
	}
	
	public HorizontalPanel getAnchorPanel() {
		return anchorPanel;
	}
	
	public PanelCollapse getCollapsePanel() {
		return panelCollapse;
	}


	public Anchor getAdvanceSearchAnchor() {
		return advanceSearchAnchor;
	}


	public void setAdvanceSearchAnchor(Anchor advanceSearchAnchor) {
		this.advanceSearchAnchor = advanceSearchAnchor;
	}
	
	public VersionMeasureType getSearchStateValue() {
		return VersionMeasureType.valueOf(searchStateListBox.getSelectedValue());
	}
	
	public PatientBasedType getPatientBasedValue() {
		return PatientBasedType.valueOf(patientIndicatorListBox.getSelectedValue());
	}
	
	public List<String> getScoringTypeList(){
		List<String> scoringTypes = new ArrayList<>();
		if(proportionCheckbox.getValue()) {
			scoringTypes.add(ConstantMessages.PROPORTION_SCORING);
		}
		if(ratioCheckbox.getValue()) {
			scoringTypes.add(ConstantMessages.RATIO_SCORING);
		}
		if(cohortCheckbox.getValue()) {
			scoringTypes.add(ConstantMessages.COHORT_SCORING);
		}
		if(contVariableCheckbox.getValue()) {
			scoringTypes.add(ConstantMessages.CONTINUOUS_VARIABLE_SCORING);
		}
		
		return scoringTypes;
	}

	public String getModifiedWithinValue() {
		return modifiedOnList.getSelectedValue();
	}


	public String getModifiedByValue() {
		return modifiedBy.getValue();
	}

	public String getOwnedByValue() {
		return ownedBy.getValue();
	}
	
}
