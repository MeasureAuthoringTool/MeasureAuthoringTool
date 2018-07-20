package mat.client.export;

import org.gwtbootstrap3.client.ui.NavTabs;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.TabContent;
import org.gwtbootstrap3.client.ui.TabListItem;
import org.gwtbootstrap3.client.ui.TabPane;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.BaseDisplay;
import mat.client.shared.MessageAlert;

public class ManageExportView implements BaseDisplay {

	private Panel mainPanel = new Panel(); 
	
	private NavTabs tabs; 
	private TabContent content; 

	private TabListItem exportItem; 
	private TabListItem bonnieExportItem; 
	
	private TabPane exportPane; 
	private TabPane bonnieExportPane; 
		
	public ManageExportView() {	
		createNavTabs();
	}
	
	private void createNavTabs() {
		mainPanel = new Panel();
		PanelBody panelBody = new PanelBody();
		
		tabs = new NavTabs();
		content = new TabContent();
		
		createExportTab(tabs, content);
		createBonnieExportTab(tabs, content);

		panelBody.add(tabs);
		panelBody.add(content);

		mainPanel.add(panelBody);
	}
	
	private void createExportTab(NavTabs tabs, TabContent content) {
		exportItem = new TabListItem();
		exportItem.setActive(true);
		exportItem.setDataTarget("exportTab");
		exportItem.setText("Export");
		exportItem.setTitle("Select Exports");
		
		exportPane = new TabPane();
		exportPane.setActive(true);
		exportPane.setId("exportTab");
		
		tabs.add(exportItem);
		content.add(exportPane);
	}
	
	private void createBonnieExportTab(NavTabs tabs, TabContent content) {
		bonnieExportItem = new TabListItem();
		bonnieExportItem.setDataTarget("exportBonnieTab");
		bonnieExportItem.setText("Upload to Bonnie");
		bonnieExportItem.setTitle("Upload to Bonnie");
		
		bonnieExportPane = new TabPane();
		bonnieExportPane.setId("exportBonnieTab");
		
		tabs.add(bonnieExportItem);
		content.add(bonnieExportPane);
	}
	
	
	public Widget asWidget() {
		return mainPanel; 
	}

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return null;
	}

	public TabPane getExportPane() {
		return exportPane;
	}

	public void setExportPane(TabPane exportPane) {
		this.exportPane = exportPane;
	}

	public TabPane getBonnieExportPane() {
		return bonnieExportPane;
	}

	public void setBonnieExportPane(TabPane bonnieExportPane) {
		this.bonnieExportPane = bonnieExportPane;
	}
	
	public TabListItem getExportItem() {
		return exportItem;
	}

	public TabListItem getBonnieExportItem() {
		return bonnieExportItem;
	}
}
