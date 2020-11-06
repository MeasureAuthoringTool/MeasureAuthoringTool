package mat.client.advancedsearch;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.shared.SpacerWidget;
import mat.shared.MeasureSearchModel;
import mat.shared.MeasureSearchModel.PatientBasedType;
import mat.shared.SearchModel;
import mat.shared.SearchModel.VersionType;
import mat.shared.StringUtility;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Badge;

import java.util.List;

public class AdvancedSearchPillPanel {

	private FlowPanel badgePanel = new FlowPanel();
	
	private VerticalPanel badgeTable = new VerticalPanel();
	
	private Anchor reset;
	
	private Label badgeHeader;
	
	public AdvancedSearchPillPanel(String forView) {
		
		badgeTable.clear();
		badgeTable.getElement().setId("badgePanel_horizontalPanel");
		badgeTable.setStyleName("recentSearchPanel");
		
		badgeHeader = new Label("Search Criteria");
		badgeHeader.getElement().setId("searchHeader_Label");
		badgeHeader.setStyleName("recentSearchHeader");
		badgeHeader.getElement().setAttribute("tabIndex", "-1");
		badgeHeader.getElement().setAttribute("aria-label", "Search Criteria This panel displays the search criteria you selected for your " + forView + " search");

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

	public void setSearchedByPills(SearchModel searchModel, String searchType) {
		badgePanel.clear();
		final boolean isMeasure = (searchModel instanceof MeasureSearchModel);
		// create pill for search term only if the user entered a search term
		if(searchModel.getSearchTerm() != null && !searchModel.getSearchTerm().isEmpty()) {
			final String searchTermText = "Search Term: " + getSubstringOfText(searchModel.getSearchTerm());
			makeBadge(searchTermText);
		}
		// create pill for filter by each time
		final String measureSearchType = searchModel.getIsMyMeasureSearch() == 0? "My " + searchType : "All " + searchType;
		makeBadge("Filter by: " + measureSearchType);
		
		if (isMeasure) {
			final String libraryName = ((MeasureSearchModel)searchModel).getCqlLibraryName();
			if (StringUtility.isNotBlank(libraryName)) {
				makeBadge("CQL Library Name: " + libraryName);
			}
		}
		
		// create pill for measure state if the user entered an option other then all measures
		if(searchModel.isDraft() != null && !searchModel.isDraft().equals(VersionType.ALL)) {
			final String measureStateText = searchModel.isDraft().equals(VersionType.DRAFT) ? "Draft" : "Versioned";
			makeBadge(searchType + " State: " + measureStateText);
		}
		
		if (isMeasure) {
			createPatientBasedAndScoringBadge(searchModel);
		}
		// create pill for measure last modified within  if an option other then all measures is checked
		if(searchModel.getModifiedDate() > 0) {
			final String modifiedOnText = searchModel.getModifiedDate() + " days";
			makeBadge("Modified Within: " + modifiedOnText);
		}
		// create pill for last modified by only if the user entered a text in the field
		if(searchModel.getModifiedOwner() != null && !searchModel.getModifiedOwner().isEmpty()) {
			final String modifiedByText =  getSubstringOfText(searchModel.getModifiedOwner());
			makeBadge("Modified By: " + modifiedByText);
		}
		// create pill for owned by only if the user entered a text in the field
		if(searchModel.getOwner() != null && !searchModel.getOwner().isEmpty()) {
			final String ownedByText = getSubstringOfText(searchModel.getOwner());
			makeBadge("Owned By: " + ownedByText);
		}
		// create reset link
		reset = new Anchor();
		reset.getElement().setId("navPillReset");
		reset.setText("Reset");
		reset.setStyleName("navPillReset");
		reset.getElement().setAttribute("aria-label", "Reset Search Fields. Clicking will reset all your search values to default search. "
				+ "The focus will also be placed on the search text field for you to start another search");
		badgePanel.add(reset);

		if (searchModel.getTotalResults() != null) {
			String resultsText = "(Search returned " + searchModel.getTotalResults() + " result(s))";
			String resultsTextWithStyle = "Search Criteria&nbsp;&nbsp;<span style=\"font-size:12px;font-style: italic;\" >" + resultsText + "</span>";
			badgeHeader.getElement().setInnerHTML(resultsTextWithStyle);
		}
		badgeTable.setVisible(true);
	}

	private void createPatientBasedAndScoringBadge(SearchModel measureSearchModel) {
		// create pill for patient based if the user entered an option other then all measures
		final PatientBasedType patientBasedType = ((MeasureSearchModel) measureSearchModel).isPatientBased();
		if(patientBasedType != null && !patientBasedType.equals(PatientBasedType.ALL)) {
			final String patientBasedString = patientBasedType.equals(PatientBasedType.PATIENT) ? "Yes" : "No";
			makeBadge("Patient Based: " + patientBasedString);
		}
		
		//create pill for Measure Scoring if any of them are checked
		final List<String> scoringTypes = ((MeasureSearchModel) measureSearchModel).getScoringTypes();
		if(scoringTypes != null && !scoringTypes.isEmpty()) {
			final String measureScoringTypeString = scoringTypes.size() == 4 ? "All" : String.join(", ", scoringTypes);
			makeBadge("Measure Score: " + measureScoringTypeString);
		}
	}

	private void makeBadge(String badgeText) {
		final Badge badge = new Badge(badgeText);
		badge.setTitle(badgeText);
		badge.setStyleName("navPill");
		badgePanel.add(badge);
		badge.getElement().setTabIndex(-1);
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

	public Label getBadgeHeader() {
		return badgeHeader;
	}

	public void setBadgeHeader(Label badgeHeader) {
		this.badgeHeader = badgeHeader;
	}
}
