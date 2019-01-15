package mat.client.advancedSearch;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;

public class AdvancedSearchPanel {

	
	private Anchor advanceSearchAnchor = new Anchor();
	HorizontalPanel anchorPanel;
	PanelCollapse panelCollapse;
	
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
		
		HorizontalPanel advancedSearchContentPanel = new HorizontalPanel();
		advancedSearchContentPanel.setWidth("100%");
		advancedSearchContentPanel.setHeight("500px");
		panelCollapse.add(advancedSearchContentPanel);

		return panelCollapse;
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
	
	
}
