package mat.client.advancedSearch;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedSearchPanel {

	
	private static final String NO_NOT_PATIENT_BASED = "No, Not Patient-based";
	private static final String YES_PATIENT_BASED = "Yes, Patient-based";
	private static final String ALL_MEASURES = "All Measures";
	private final String HEIGHT_OF_BOXES = "30px";
	private final String WIDTH_OF_BOXES = "200px";
	
	private Anchor advanceSearchAnchor = new Anchor();
	HorizontalPanel anchorPanel;
	PanelCollapse panelCollapse;
	
	private ListBox searchStateListBox;
	private FormGroup searchStateGroup;
	private FormGroup patientIndicatorGroup;
	private ListBox patientIndicatorListBox;
	
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
		
		VerticalPanel advancedSearchContentPanel = new VerticalPanel();
		if("forMeasure".equals(forView)) {
			advancedSearchContentPanel.add(createAdvancedSearchMeasureContent());
		}
		advancedSearchContentPanel.setWidth("100%");
		advancedSearchContentPanel.setHeight("250px");
		panelCollapse.add(advancedSearchContentPanel);

		return panelCollapse;
	}
	
	private Widget createAdvancedSearchMeasureContent() {
		String pluralType = "Measures";
		HorizontalPanel advancedSearchRow1 = new HorizontalPanel();
		buildStateSection(pluralType);
		buildPatientSection();
		
		advancedSearchRow1.add(searchStateGroup);
		advancedSearchRow1.add(patientIndicatorGroup);
		return advancedSearchRow1;
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
		searchStateListBox.addItem("All " + pluralType, "All " + pluralType);
		searchStateListBox.addItem("Draft " + pluralType, "Draft " + pluralType);
		searchStateListBox.addItem("Versioned " + pluralType, "Versioned " + pluralType);

		searchStateGroup = new FormGroup();
		searchStateGroup.add(stateLabel);
		searchStateGroup.add(searchStateListBox);
		searchStateGroup.setStyleName("advancedSearchLabels");
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
		patientIndicatorListBox.addItem(ALL_MEASURES, ALL_MEASURES);
		patientIndicatorListBox.addItem(YES_PATIENT_BASED, YES_PATIENT_BASED);
		patientIndicatorListBox.addItem(NO_NOT_PATIENT_BASED, NO_NOT_PATIENT_BASED);

		patientIndicatorGroup = new FormGroup();
		patientIndicatorGroup.add(patientLabel);
		patientIndicatorGroup.add(patientIndicatorListBox);
		patientIndicatorGroup.setStyleName("advancedSearchLabels");
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
	
	public String getSearchStateValue() {
		return searchStateListBox.getSelectedValue();
	}
	
	public String getPatientBasedValue() {
		return patientIndicatorListBox.getSelectedValue();
	}
	
	
}
