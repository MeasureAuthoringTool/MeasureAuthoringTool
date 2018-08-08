package mat.client;

import org.gwtbootstrap3.client.ui.NavTabs;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.TabContent;
import org.gwtbootstrap3.client.ui.TabListItem;
import org.gwtbootstrap3.client.ui.TabPane;
import com.google.gwt.user.client.ui.Widget;

import mat.client.util.ClientConstants;

public class TopTabView {
	private Panel mainPanel = new Panel();
	private NavTabs tabs;
	private TabContent content;
	private boolean isAdmin;
	
	TabListItem adminPresenterItem;
	TabListItem adminReportingItem;
	TabListItem measureLibItem;
	TabListItem measureComposerItem;
	TabListItem cqlLibraryItem;
	TabListItem cqlComposerItem;
	TabListItem matAccountItem;
	TabListItem compositeMeasureEditItem;
	
	TabPane adminPresenterPane;
	TabPane adminReportingPane;
	TabPane measureLibPane;
	TabPane measureComposerPane;
	TabPane cqlLibraryPane;
	TabPane cqlComposerPane;
	TabPane matAccountPane;
	TabPane compositeMeasureEditPane;
	
	public TopTabView(boolean isAdmin){
		this.isAdmin = isAdmin;
		tabs = new NavTabs();
		content = new TabContent();
		if(isAdmin) {
			buildAdminTabs();
		} else {
			buildTabs();
		}
	}
	
	private void buildAdminTabs() {
		createAdminPresenterTab();
		createMeasureLibraryTab();
		createCqlLibraryTab();
		createAdminReportingTab();
		createMatAccountTab();
	}
	
	private void buildTabs() {
		createMeasureLibraryTab();
		createMeasureComposerTab();
		createCqlLibraryTab();
		createCqlComposerTab();
		createMatAccountTab();
		createCompositeMeasureEditTab();
	}

	private void createAdminPresenterTab() {
		adminPresenterItem = new TabListItem();
		adminPresenterItem.setActive(true);
		adminPresenterItem.setDataTarget("adminPresenterTab");
		adminPresenterItem.setTitle("Select "+ClientConstants.TITLE_ADMIN);
		adminPresenterItem.setText(ClientConstants.TITLE_ADMIN);
		
		adminPresenterPane = new TabPane();
		adminPresenterPane.setActive(true);
		adminPresenterPane.setId("adminPresenterTab");
		
		tabs.add(adminPresenterItem);
		content.add(adminPresenterPane);
	}
	
	private void createAdminReportingTab() {
		adminReportingItem = new TabListItem();
		adminReportingItem.setActive(true);
		adminReportingItem.setDataTarget("adminReportingTab");
		adminReportingItem.setTitle("Select Administrator Reports");
		adminReportingItem.setText(ClientConstants.ADMIN_REPORTS);
		
		adminReportingPane = new TabPane();
		adminReportingPane.setActive(true);
		adminReportingPane.setId("adminReportingTab");
		
		tabs.add(adminReportingItem);
		content.add(adminReportingPane);
	}
	
	private void createMeasureLibraryTab() {
		measureLibItem = new TabListItem();
		measureLibItem.setActive(true);
		measureLibItem.setDataTarget("measureLibTab");
		measureLibItem.setTitle("Select Measure Library");
		measureLibItem.setText(isAdmin ? ClientConstants.TITLE_MEASURE_LIB_CHANGE_OWNERSHIP : ClientConstants.TITLE_MEASURE_LIB);
		
		measureLibPane = new TabPane();
		measureLibPane.setActive(true);
		measureLibPane.setId("measureLibTab");
		
		tabs.add(measureLibItem);
		content.add(measureLibPane);
	}
	

	private void createMeasureComposerTab() {
		measureComposerItem = new TabListItem();
		measureComposerItem.setDataTarget("measureComposerTab");
		measureComposerItem.setTitle("Select Measure Composer");
		measureComposerItem.setText(ClientConstants.TITLE_MEASURE_COMPOSER);
		
		measureComposerPane = new TabPane();
		measureComposerPane.setId("measureComposerTab");
		
		tabs.add(measureComposerItem);
		content.add(measureComposerPane);
	}
	
	private void createCqlLibraryTab() {
		cqlLibraryItem = new TabListItem();
		cqlLibraryItem.setDataTarget("cqlLibraryTab");
		cqlLibraryItem.setTitle("Select CQL Library");
		cqlLibraryItem.setText(isAdmin ? ClientConstants.CQL_LIBRARY_OWNERSHIP : ClientConstants.TITLE_CQL_LIB);
		
		cqlLibraryPane = new TabPane();
		cqlLibraryPane.setId("cqlLibraryTab");
		
		tabs.add(cqlLibraryItem);
		content.add(cqlLibraryPane);
	}

	private void createCqlComposerTab() {
		cqlComposerItem = new TabListItem();
		cqlComposerItem.setDataTarget("cqlComposerTab");
		cqlComposerItem.setTitle("Select CQL Composer");
		cqlComposerItem.setText(ClientConstants.TITLE_CQL_COMPOSER);
		
		cqlComposerPane = new TabPane();
		cqlComposerPane.setId("cqlComposerTab");
		
		tabs.add(cqlComposerItem);
		content.add(cqlComposerPane);
	}
	
	private void createMatAccountTab() {
		matAccountItem = new TabListItem();
		matAccountItem.setDataTarget("matAccountTab");
		matAccountItem.setTitle("Select MAT Account");
		matAccountItem.setText( isAdmin ? ClientConstants.TITLE_ADMIN_ACCOUNT : ClientConstants.TITLE_MY_ACCOUNT);
		
		matAccountPane = new TabPane();
		matAccountPane.setId("matAccountTab");
		
		tabs.add(matAccountItem);
		content.add(matAccountPane);
	}
	
	private void createCompositeMeasureEditTab() {
		compositeMeasureEditItem = new TabListItem();
		compositeMeasureEditItem.setDataTarget("compositeMeasureEditTab");
		compositeMeasureEditItem.setText(ClientConstants.COMPOSITE_MEASURE_EDIT);
		compositeMeasureEditItem.setTitle("Select " + ClientConstants.COMPOSITE_MEASURE_EDIT);
		compositeMeasureEditPane = new TabPane();
		compositeMeasureEditPane.setId("compositeMeasureEditTab");
		
		tabs.add(compositeMeasureEditItem);
		content.add(compositeMeasureEditPane);
	}

	public NavTabs getTabs() {
		return tabs;
	}

	public void setTabs(NavTabs tabs) {
		this.tabs = tabs;
	}

	public TabContent getContent() {
		return content;
	}

	public void setContent(TabContent content) {
		this.content = content;
	}
	
	public Widget asWidget() {
		return mainPanel;
	}
	
	public TabPane getMeasureLibraryPane() {
		return measureLibPane;
	}

	public TabListItem getMeasureLibItem() {
		return measureLibItem;
	}

	public TabListItem getMeasureComposerItem() {
		return measureComposerItem;
	}

	public TabListItem getCqlLibraryItem() {
		return cqlLibraryItem;
	}

	public TabListItem getCqlComposerItem() {
		return cqlComposerItem;
	}

	public TabListItem getMatAccountItem() {
		return matAccountItem;
	}

	public TabListItem getCompositeMeasureEditItem() {
		return compositeMeasureEditItem;
	}

	public TabPane getMeasureComposerPane() {
		return measureComposerPane;
	}

	public TabPane getCqlLibraryPane() {
		return cqlLibraryPane;
	}

	public TabPane getCqlComposerPane() {
		return cqlComposerPane;
	}

	public TabPane getMatAccountPane() {
		return matAccountPane;
	}

	public TabPane getCompositeMeasureEditPane() {
		return compositeMeasureEditPane;
	}

	public TabListItem getAdminPresenterItem() {
		return adminPresenterItem;
	}

	public TabListItem getAdminReportingItem() {
		return adminReportingItem;
	}

	public TabPane getAdminPresenterPane() {
		return adminPresenterPane;
	}

	public TabPane getAdminReportingPane() {
		return adminReportingPane;
	}

	public TabPane getMeasureLibPane() {
		return measureLibPane;
	}
}