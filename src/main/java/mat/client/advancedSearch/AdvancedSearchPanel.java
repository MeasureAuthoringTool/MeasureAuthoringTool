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

	
	private final String HEIGHT_OF_BOXES = "30px";
	private final String WIDTH_OF_BOXES = "200px";
	
	private Anchor advanceSearchAnchor = new Anchor();
	HorizontalPanel anchorPanel;
	PanelCollapse panelCollapse;
	
	private ListBox searchStateList;
	private FormGroup searchStateGroup;
	
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
		buildStateSection(pluralType);
		return searchStateGroup;
	}
	
	private void buildStateSection(String pluralType) {
		FormLabel stateLabel = new FormLabel();
		VerticalPanel statePanel = new VerticalPanel();
		stateLabel.setText("Show Only:");
		stateLabel.setTitle("Show Only");
		stateLabel.setFor("stateId");
		stateLabel.setFor("stateGroup");
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
		statePanel.setStyleName("advancedSearchLabels");
		searchStateGroup = new FormGroup();
		searchStateGroup.add(statePanel);
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
		return searchStateList.getSelectedValue();
	}
	
	
}
