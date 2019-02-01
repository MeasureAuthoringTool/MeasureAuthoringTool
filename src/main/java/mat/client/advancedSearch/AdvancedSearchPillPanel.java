package mat.client.advancedSearch;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Badge;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.SpacerWidget;
import mat.shared.MeasureSearchModel;
import mat.shared.MeasureSearchModel.PatientBasedType;
import mat.shared.MeasureSearchModel.VersionMeasureType;

public class AdvancedSearchPillPanel {

	private FlowPanel badgePanel = new FlowPanel();
	
	private VerticalPanel badgeTable = new VerticalPanel();
	
	private Anchor reset;
	
	public AdvancedSearchPillPanel() {
		
		badgeTable.clear();
		badgeTable.getElement().setId("badgePanel_horizontalPanel");
		badgeTable.setStyleName("recentSearchPanel");
		
		Label badgeHeader = new Label("Search Criteria");
		badgeHeader.getElement().setId("searchHeader_Label");
		badgeHeader.setStyleName("recentSearchHeader");
		badgeHeader.getElement().setAttribute("tabIndex", "0");

		badgeTable.add(badgeHeader);
		badgeTable.add(new SpacerWidget());
		badgeTable.add(badgePanel);
		badgeTable.setStylePrimaryName("searchFilter");
		
		badgeTable.setVisible(false);
	}

	public FlowPanel getBadgePanel() {
		return badgePanel;
	}

	public void setBadgePanel(FlowPanel badgePanel) {
		this.badgePanel = badgePanel;
	}

	public VerticalPanel getBadgeTable() {
		return badgeTable;
	}

	public void setBadgeTable(VerticalPanel badgeTable) {
		this.badgeTable = badgeTable;
	}

	public void setSearchedByPills(MeasureSearchModel measureSearchModel, String searchType) {
		badgePanel.clear();
		//create pill for search term only if the user entered a search term
		if(measureSearchModel.getSearchTerm() != null && !measureSearchModel.getSearchTerm().isEmpty()) {
			String searchTermText = getSubstringOfText(measureSearchModel.getSearchTerm());
			Badge searchTerm = new Badge("Search Term: " + searchTermText);
			searchTerm.setTitle("Search Term: " + searchTermText);
			searchTerm.setStyleName("navPill");
			badgePanel.add(searchTerm);
			searchTerm.getElement().setTabIndex(0);
		}
		//create pill for filter by each time
		String measureSearchType = measureSearchModel.isMyMeasureSearch() == 0? "My " + searchType : "All " + searchType;
		Badge searchBy = new Badge("Filter by: " + measureSearchType);
		searchBy.setTitle("Filter by: " + measureSearchType);
		searchBy.setStyleName("navPill");
		searchBy.getElement().setTabIndex(0);
		badgePanel.add(searchBy);
		badgeTable.setVisible(true);
		//create pill for measure state if the user entered an option other then all measures
		if(measureSearchModel.isDraft() != null && !measureSearchModel.isDraft().equals(VersionMeasureType.ALL)) {
			String measureStateText = measureSearchModel.isDraft().equals(VersionMeasureType.DRAFT) ? "Draft" : "Versioned";
			Badge measureState = new Badge(searchType + " State: " + measureStateText);
			measureState.setTitle(searchType + " State: " + measureStateText);
			measureState.setStyleName("navPill");
			badgePanel.add(measureState);
			measureState.getElement().setTabIndex(0);
		}
		// create pill for patient based if the user entered an option other then all measures
		if(measureSearchModel.isPatientBased() != null && !measureSearchModel.isPatientBased().equals(PatientBasedType.ALL)) {
			String patientBasedString = measureSearchModel.isPatientBased().equals(PatientBasedType.PATIENT) ? "Yes" : "No";
			Badge patientBased = new Badge("Patient Based: " + patientBasedString);
			patientBased.setTitle("Patient Based: " + patientBasedString);
			patientBased.setStyleName("navPill");
			badgePanel.add(patientBased);
			patientBased.getElement().setTabIndex(0);
		}
		//create pill for Measure Scoring if any of them are checked
		if(measureSearchModel.getScoringTypes() != null && measureSearchModel.getScoringTypes().size() > 0) {
			String measureScoringTypeString = measureSearchModel.getScoringTypes().size() == 4 ? "All" : 
				String.join(", ", measureSearchModel.getScoringTypes());
			Badge measureScoringType = new Badge("Measure Score: " + measureScoringTypeString);
			measureScoringType.setTitle("Measure Score: " + measureScoringTypeString);
			measureScoringType.setStyleName("navPill");
			badgePanel.add(measureScoringType);
			measureScoringType.getElement().setTabIndex(0);
		}
		// create pill for measure last modified within  if an option other then all measures is checked
		if(measureSearchModel.getModifiedDate() > 0) {
			String modifiedOnText = measureSearchModel.getModifiedDate() + " days";
			Badge modifiedOn = new Badge("Modified Within: " + modifiedOnText);
			modifiedOn.setTitle("Modified Within: " + modifiedOnText);
			modifiedOn.setStyleName("navPill");
			badgePanel.add(modifiedOn);
			modifiedOn.getElement().setTabIndex(0);
		}
		// create pill for last modified by only if the user entered a text in the field
		if(measureSearchModel.getModifiedOwner() != null && !measureSearchModel.getModifiedOwner().isEmpty()) {
			String modifiedByText =  getSubstringOfText(measureSearchModel.getModifiedOwner());
			Badge modifiedBy = new Badge("Modified By: " + modifiedByText);
			modifiedBy.setTitle("Modified By: " + modifiedByText);
			modifiedBy.setStyleName("navPill");
			badgePanel.add(modifiedBy);
			modifiedBy.getElement().setTabIndex(0);
		}
		// create pill for owned by only if the user entered a text in the field
		if(measureSearchModel.getModifiedOwner() != null && !measureSearchModel.getModifiedOwner().isEmpty()) {
			String ownedByText = getSubstringOfText(measureSearchModel.getOwner());
			Badge ownedBy = new Badge("Owned By: " + ownedByText);
			ownedBy.setTitle("Owned By: " + ownedByText);
			ownedBy.setStyleName("navPill");
			badgePanel.add(ownedBy);
			ownedBy.getElement().setTabIndex(0);
		}
		// create reset link
		reset = new Anchor();
		reset.getElement().setId("navPillReset");
		reset.setText("Reset");
		reset.setTitle("Reset");
		reset.setStyleName("navPillRed");
		//reset.addClickHandler(event -> resetSearchFields(measureSearchModel,searchDisplay));
		badgePanel.add(reset);
	}
	
	private String getSubstringOfText(String text) {
		String substring =  text.substring(0, 20);
		if(text.length() > 20) {
			substring += "...";
		}
		return substring;
	}

	public Anchor getReset() {
		return reset;
	}

	public void setReset(Anchor reset) {
		this.reset = reset;
	}
}
