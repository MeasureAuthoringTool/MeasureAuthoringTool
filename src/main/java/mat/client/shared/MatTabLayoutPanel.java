package mat.client.shared;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import mat.client.Enableable;
import mat.client.TabObserver;
import mat.client.shared.ui.MATTabPanel;
import mat.shared.DynamicTabBarFormatter;

public class MatTabLayoutPanel extends MATTabPanel implements BeforeSelectionHandler<Integer>, Enableable {
	private DynamicTabBarFormatter dynamicTabBarFormatter = new DynamicTabBarFormatter();
	private int selectedIndex = 0;
	private int targetSelection;
	private TabObserver tabObserver;
	
	public MatTabLayoutPanel(TabObserver tabObserver) {
		addBeforeSelectionHandler(this);
		this.tabObserver = tabObserver;
	}
	
	public Integer getSelectedIndex() {
		return selectedIndex;
	}
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	public void selectPreviousTab() {
		selectTab(selectedIndex - 1);
	}
	
	public void selectNextTab() {
		selectTab(selectedIndex + 1);
	}
	
	public boolean hasNextTab() {
		return selectedIndex < (getWidgetCount() - 1);
	}
	
	public boolean hasPreviousTab() {
		return selectedIndex != 0;
	}
	
	public void updateHeaderSelection(int index) {
		//Strip arrow off of the tab we are leaving
		int currentTab = selectedIndex;
		getTabBar().setTabHTML(currentTab, dynamicTabBarFormatter.getTitle(currentTab));
		//Add arrow to new tab that is selected
		getTabBar().setTabHTML(index, dynamicTabBarFormatter.getSelectedTitle(index));
	}
	
	@Override
	public void add(Widget w, String title, boolean isVisible) {
		String tabText = dynamicTabBarFormatter.normalTitle(title);
		insert(w, tabText, true, getWidgetCount(), isVisible);
		int index = getWidgetCount() - 1;
		dynamicTabBarFormatter.insertTitle(index, tabText);
	}
	
	@Override
	public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
		targetSelection =  event.getItem();
		if (!getSelectedIndex().equals(targetSelection)) {
			if(tabObserver.isValid()) {
				updateHeaderSelection(targetSelection);
				tabObserver.notifyCurrentTabOfClosing();
				setSelectedIndex(targetSelection);
				tabObserver.updateOnBeforeSelection();
			} else {
				tabObserver.showUnsavedChangesError();
				MatContext.get().setErrorTabIndex(getSelectedIndex());
				MatContext.get().setErrorTab(true);
			}
		}
	}
	
	public void setIndexFromTargetSelection() {
		updateHeaderSelection(targetSelection);
		setSelectedIndex(targetSelection);
		selectTab(getSelectedIndex());
	}
	
	public int getTargetSelection() {
		return targetSelection;
	}
}
