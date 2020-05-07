package mat.client.advancedsearch;

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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.shared.ConstantMessages;
import mat.shared.MeasureSearchModel.PatientBasedType;
import mat.shared.SearchModel;
import mat.shared.SearchModel.ModelType;
import mat.shared.SearchModel.VersionType;

public class AdvancedSearchPanel {


    private static final String CHECK_ALL_THAT_APPLY = "(Check all that apply. No selection will return all measure scoring types.)";
    private static final String MEASURE_SCORING = "Measure Scoring";
    private static final String NO_NOT_PATIENT_BASED = "No, Not Patient-based";
    private static final String YES_PATIENT_BASED = "Yes, Patient-based";
    private static final String ALL_MEASURES = "All Measures";
    private static final String HEIGHT_OF_BOXES = "30px";
    private static final String WIDTH_OF_BOXES = "200px";
    private static final String ADVANCED_SEARCH_STYLE = "advancedSearchLabels";
    private static final String PADDING_LEFT_TWENTY_PIXEL = "padding-left: 20px;";
    private static final String STYLE = "style";
    private static final String OWNED_BY = " Owned By";
    private static final String OWNER_PLACEHOLDER = "Enter user first or last name";
    private static final String SEARCH_BY_LIBRARY_NAME = "Search by CQL Library Name";

    private boolean isMeasure = false;

    private Anchor advanceSearchAnchor = new Anchor();
    private HorizontalPanel anchorPanel;
    private PanelCollapse panelCollapse;

    private ListBox searchStateListBox;
    private ListBox patientIndicatorListBox;
    private ListBox modifiedOnList;
    private ListBox modelTypeListBox;

    private CheckBox proportionCheckbox;
    private CheckBox ratioCheckbox;
    private CheckBox cohortCheckbox;
    private CheckBox contVariableCheckbox;

    private FormGroup searchStateGroup;
    private FormGroup patientIndicatorGroup;
    private FormGroup scoreGroup;
    private FormGroup modifiedWithinGroup;
    private FormGroup modifiedByGroup;
    private FormGroup ownedByGroup;
    private FormGroup libraryNameGroup;

    private Input cqlLibraryName;
    private Input modifiedBy;
    private Input ownedBy;

    public AdvancedSearchPanel(String forView) {
        isMeasure = "forMeasure".equals(forView);
        setUpAnchorElement(forView);
        setUpCollapsePanel(forView);
    }


    private void setUpAnchorElement(String forView) {
        anchorPanel = new HorizontalPanel();
        anchorPanel.setStyleName("advancedSearchAnchor");
        anchorPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        buildModelType();
        anchorPanel.add(modelTypeListBox);
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
        buildSearchContent(forView);
        panelCollapse.add(createAdvancedSearchMeasureContent());

        return panelCollapse;
    }

    private void buildSearchContent(String forView) {
        String type = "";
        String pluralType = "";

        if (isMeasure) {
            type = "Measure";
            pluralType = "Measures";

            buildLibraryNameSection();
            buildPatientSection();
            buildScoreSection();

        } else {
            type = "Library";
            pluralType = "Libraries";
        }

        buildStateSection(type, pluralType);
        buildDaysSection(type, pluralType);
        buildModifiedBySection(type);
        buildOwnedBySection(type);

    }


    private VerticalPanel createAdvancedSearchMeasureContent() {

        final HorizontalPanel advancedSearchRow1 = new HorizontalPanel();
        advancedSearchRow1.add(searchStateGroup);

        final VerticalPanel advancedSearchVP = new VerticalPanel();
        advancedSearchVP.setWidth("100%");

        if (isMeasure) {
            final HorizontalPanel advancedSearchRow = new HorizontalPanel();
            advancedSearchRow.add(libraryNameGroup);
            advancedSearchRow1.add(patientIndicatorGroup);
            advancedSearchRow1.add(scoreGroup);

            final HorizontalPanel advancedSearchRow2 = new HorizontalPanel();
            buildCommonSearchHP(advancedSearchRow2);

            advancedSearchVP.add(advancedSearchRow);
            advancedSearchVP.add(advancedSearchRow1);
            advancedSearchVP.add(advancedSearchRow2);

        } else {
            buildCommonSearchHP(advancedSearchRow1);

            advancedSearchVP.add(advancedSearchRow1);
        }
        return advancedSearchVP;
    }


    private void buildCommonSearchHP(HorizontalPanel advancedSearchRow) {
        advancedSearchRow.add(modifiedWithinGroup);
        advancedSearchRow.add(modifiedByGroup);
        advancedSearchRow.add(ownedByGroup);
    }

    private void buildModelType() {
        modelTypeListBox = new ListBox();
        modelTypeListBox.getElement().setClassName("navPill");
        modelTypeListBox.setWidth("200px");
        modelTypeListBox.getElement().getStyle().setMarginRight(15, Style.Unit.PX);
        modelTypeListBox.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
        modelTypeListBox.setId("modelType");
        modelTypeListBox.setHeight(HEIGHT_OF_BOXES);
        modelTypeListBox.addItem("Model Type: All", SearchModel.ModelType.ALL.toString());
        modelTypeListBox.addItem("Model Type: FHIR / CQL Only", SearchModel.ModelType.FHIR.toString());
        modelTypeListBox.addItem("Model Type: QDM / CQL Only", SearchModel.ModelType.QDM_CQL.toString());
        if (isMeasure) {
            modelTypeListBox.addItem("Model Type: QDM / QDM Only", SearchModel.ModelType.QDM_QDM.toString());
        }
    }

    private void buildStateSection(String type, String pluralType) {
        final FormLabel stateLabel = new FormLabel();
        stateLabel.setText(type + " State");
        stateLabel.setTitle(type + " State");
        stateLabel.setFor("stateId");
        stateLabel.setFor("stateGroup");
        stateLabel.setPaddingRight(16);
        searchStateListBox = new ListBox();
        if (isMeasure) {
            searchStateListBox.setWidth(WIDTH_OF_BOXES);
        } else {
            searchStateListBox.setWidth("150px");
        }

        searchStateListBox.setHeight(HEIGHT_OF_BOXES);
        searchStateListBox.setId("stateGroup");
        searchStateListBox.addItem("All " + pluralType, VersionType.ALL.toString());
        searchStateListBox.addItem("Draft " + pluralType, VersionType.DRAFT.toString());
        searchStateListBox.addItem("Versioned " + pluralType, VersionType.VERSION.toString());

        searchStateGroup = new FormGroup();
        searchStateGroup.add(stateLabel);
        searchStateGroup.add(searchStateListBox);
        searchStateGroup.setStyleName(ADVANCED_SEARCH_STYLE);
    }

    private void buildPatientSection() {
        final FormLabel patientLabel = new FormLabel();
        patientLabel.setText(" Patient-Based Indicator");
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
        final HorizontalPanel scoreheader = new HorizontalPanel();
        final FormLabel scoreLabel = new FormLabel();
        scoreLabel.setText(MEASURE_SCORING);
        scoreLabel.setTitle(MEASURE_SCORING);
        scoreLabel.getElement().setTabIndex(0);
        scoreheader.add(scoreLabel);
        final FormLabel helpText = new FormLabel();
        helpText.setText(CHECK_ALL_THAT_APPLY);
        helpText.setTitle(CHECK_ALL_THAT_APPLY);
        helpText.setStylePrimaryName("helpText");
        helpText.getElement().setTabIndex(0);
        helpText.getElement().setAttribute(STYLE, "font-size: 10px;");
        scoreheader.add(helpText);
        final HorizontalPanel scoreRow1 = new HorizontalPanel();
        proportionCheckbox = new CheckBox(ConstantMessages.PROPORTION_SCORING);
        proportionCheckbox.setTitle(ConstantMessages.PROPORTION_SCORING);
        proportionCheckbox.getElement().setAttribute(STYLE, PADDING_LEFT_TWENTY_PIXEL);
        ratioCheckbox = new CheckBox(ConstantMessages.RATIO_SCORING);
        ratioCheckbox.setTitle(ConstantMessages.RATIO_SCORING);
        ratioCheckbox.getElement().setAttribute(STYLE, PADDING_LEFT_TWENTY_PIXEL);
        cohortCheckbox = new CheckBox(ConstantMessages.COHORT_SCORING);
        cohortCheckbox.setTitle(ConstantMessages.COHORT_SCORING);
        contVariableCheckbox = new CheckBox(ConstantMessages.CONTINUOUS_VARIABLE_SCORING);
        contVariableCheckbox.setTitle(ConstantMessages.CONTINUOUS_VARIABLE_SCORING);
        contVariableCheckbox.getElement().setAttribute(STYLE, PADDING_LEFT_TWENTY_PIXEL);
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
        final FormLabel daysLabel = new FormLabel();
        daysLabel.setText(type + " Last Modified Within");
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

        modifiedWithinGroup = new FormGroup();
        modifiedWithinGroup.add(daysLabel);
        modifiedWithinGroup.add(modifiedOnList);
        modifiedWithinGroup.setStyleName(ADVANCED_SEARCH_STYLE);
    }

    private void buildModifiedBySection(String type) {
        final FormLabel modifiedByLabel = new FormLabel();
        modifiedByLabel.setText(type + " Last Modified By");
        modifiedByLabel.setTitle(type + " Last Modified By");
        modifiedByLabel.setFor("modifiedById");

        modifiedBy = new Input(InputType.TEXT);
        modifiedBy.setWidth(WIDTH_OF_BOXES);
        modifiedBy.setHeight(HEIGHT_OF_BOXES);
        modifiedBy.setId("modifiedById");
        modifiedBy.setPlaceholder(OWNER_PLACEHOLDER);
        modifiedBy.setTitle(OWNER_PLACEHOLDER);

        modifiedByGroup = new FormGroup();
        modifiedByGroup.add(modifiedByLabel);
        modifiedByGroup.add(modifiedBy);
        modifiedByGroup.setStyleName(ADVANCED_SEARCH_STYLE);
    }

    private void buildOwnedBySection(String type) {
        final FormLabel ownedByLabel = new FormLabel();
        ownedByLabel.setText(type + OWNED_BY);
        ownedByLabel.setTitle(type + OWNED_BY);
        ownedByLabel.setFor("ownedById");

        ownedBy = new Input(InputType.TEXT);
        ownedBy.setWidth(WIDTH_OF_BOXES);
        ownedBy.setHeight(HEIGHT_OF_BOXES);
        ownedBy.setId("ownedById");
        ownedBy.setPlaceholder(OWNER_PLACEHOLDER);
        ownedBy.setTitle(OWNER_PLACEHOLDER);

        ownedByGroup = new FormGroup();
        ownedByGroup.add(ownedByLabel);
        ownedByGroup.add(ownedBy);
        ownedByGroup.setStyleName(ADVANCED_SEARCH_STYLE);
    }

    private void buildLibraryNameSection() {
        final FormLabel libraryNameLabel = new FormLabel();
        libraryNameLabel.setText(SEARCH_BY_LIBRARY_NAME);
        libraryNameLabel.setTitle(SEARCH_BY_LIBRARY_NAME);
        libraryNameLabel.setFor("libraryName");

        cqlLibraryName = new Input(InputType.TEXT);
        cqlLibraryName.setWidth("740px");
        cqlLibraryName.setHeight(HEIGHT_OF_BOXES);
        cqlLibraryName.setId("libraryName");
        cqlLibraryName.setPlaceholder(SEARCH_BY_LIBRARY_NAME);
        cqlLibraryName.setTitle(SEARCH_BY_LIBRARY_NAME);

        libraryNameGroup = new FormGroup();
        libraryNameGroup.add(libraryNameLabel);
        libraryNameGroup.add(cqlLibraryName);
        libraryNameGroup.setStyleName(ADVANCED_SEARCH_STYLE);
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

    public VersionType getSearchStateValue() {
        return VersionType.valueOf(searchStateListBox.getSelectedValue());
    }

    public PatientBasedType getPatientBasedValue() {
        return PatientBasedType.valueOf(patientIndicatorListBox.getSelectedValue());
    }

    public ModelType getModelTypeValue() {
        return ModelType.valueOf(modelTypeListBox.getSelectedValue());
    }

    public List<String> getScoringTypeList() {
        final List<String> scoringTypes = new ArrayList<>();
        if (proportionCheckbox.getValue()) {
            scoringTypes.add(ConstantMessages.PROPORTION_SCORING);
        }
        if (ratioCheckbox.getValue()) {
            scoringTypes.add(ConstantMessages.RATIO_SCORING);
        }
        if (cohortCheckbox.getValue()) {
            scoringTypes.add(ConstantMessages.COHORT_SCORING);
        }
        if (contVariableCheckbox.getValue()) {
            scoringTypes.add(ConstantMessages.CONTINUOUS_VARIABLE_SCORING);
        }

        return scoringTypes;
    }

    public String getModifiedWithinValue() {
        return modifiedOnList.getSelectedValue();
    }

    public Input getCqlLibraryName() {
        return cqlLibraryName;
    }

    public void setCqlLibraryName(Input cqlLibraryName) {
        this.cqlLibraryName = cqlLibraryName;
    }

    public String getCqlLibraryNameByValue() {
        return cqlLibraryName.getValue();
    }

    public String getModifiedByValue() {
        return modifiedBy.getValue();
    }

    public String getOwnedByValue() {
        return ownedBy.getValue();
    }


    public void resetDisplay(boolean isMeasure) {
        if (isMeasure) {
            resetMeasureFields();
        }
        searchStateListBox.setSelectedIndex(0);
        modifiedOnList.setSelectedIndex(0);
        modifiedBy.setValue("");
        ownedBy.setValue("");
    }

    private void resetMeasureFields() {
        cqlLibraryName.setValue("");
        patientIndicatorListBox.setSelectedIndex(0);
        proportionCheckbox.setValue(false);
        ratioCheckbox.setValue(false);
        cohortCheckbox.setValue(false);
        contVariableCheckbox.setValue(false);
    }

}
