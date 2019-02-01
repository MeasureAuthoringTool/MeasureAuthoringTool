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
		// create pill for search term only if the user entered a search term
		if(measureSearchModel.getSearchTerm() != null && !measureSearchModel.getSearchTerm().isEmpty()) {
			String searchTermText = "Search Term: " + getSubstringOfText(measureSearchModel.getSearchTerm());
			makeBadge(searchTermText);
		}
		// create pill for filter by each time
		String measureSearchType = measureSearchModel.isMyMeasureSearch() == 0? "My " + searchType : "All " + searchType;
		makeBadge("Filter by: " + measureSearchType);
		
		// create pill for measure state if the user entered an option other then all measures
		if(measureSearchModel.isDraft() != null && !measureSearchModel.isDraft().equals(VersionMeasureType.ALL)) {
			String measureStateText = measureSearchModel.isDraft().equals(VersionMeasureType.DRAFT) ? "Draft" : "Versioned";
			makeBadge(searchType + " State: " + measureStateText);
		}
		// create pill for patient based if the user entered an option other then all measures
		if(measureSearchModel.isPatientBased() != null && !measureSearchModel.isPatientBased().equals(PatientBasedType.ALL)) {
			String patientBasedString = measureSearchModel.isPatientBased().equals(PatientBasedType.PATIENT) ? "Yes" : "No";
			makeBadge("Patient Based: " + patientBasedString);
		}
		//create pill for Measure Scoring if any of them are checked
		if(measureSearchModel.getScoringTypes() != null && measureSearchModel.getScoringTypes().size() > 0) {
			String measureScoringTypeString = measureSearchModel.getScoringTypes().size() == 4 ? "All" : 
				String.join(", ", measureSearchModel.getScoringTypes());
			makeBadge("Measure Score: " + measureScoringTypeString);
		}
		// create pill for measure last modified within  if an option other then all measures is checked
		if(measureSearchModel.getModifiedDate() > 0) {
			String modifiedOnText = measureSearchModel.getModifiedDate() + " days";
			makeBadge("Modified Within: " + modifiedOnText);
		}
		// create pill for last modified by only if the user entered a text in the field
		if(measureSearchModel.getModifiedOwner() != null && !measureSearchModel.getModifiedOwner().isEmpty()) {
			String modifiedByText =  getSubstringOfText(measureSearchModel.getModifiedOwner());
			makeBadge("Modified By: " + modifiedByText);
		}
		// create pill for owned by only if the user entered a text in the field
		if(measureSearchModel.getModifiedOwner() != null && !measureSearchModel.getModifiedOwner().isEmpty()) {
			String ownedByText = getSubstringOfText(measureSearchModel.getOwner());
			makeBadge("Owned By: " + ownedByText);
		}
		// create reset link
		reset = new Anchor();
		reset.getElement().setId("navPillReset");
		reset.setText("Reset");
		reset.setTitle("Reset");
		reset.setStyleName("navPillRed");
		badgePanel.add(reset);
		badgeTable.setVisible(true);
	}

	private void makeBadge(String badgeText) {
		Badge badge = new Badge(badgeText);
		badge.setTitle(badgeText);
		badge.setStyleName("navPill");
		badgePanel.add(badge);
		badge.getElement().setTabIndex(0);
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
