package mat.client.shared;

import java.util.HashMap;
import java.util.Map;

import mat.client.Enableable;
import mat.client.MatPresenter;
import mat.client.shared.ui.MATTabPanel;
import mat.shared.DynamicTabBarFormatter;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class MatTabLayoutPanel extends MATTabPanel implements BeforeSelectionHandler<Integer>, Enableable {
	private Map<Integer, MatPresenter> presenterMap = new HashMap<Integer, MatPresenter>();
	private int selectedIndex = 0;
	public DynamicTabBarFormatter fmt = new DynamicTabBarFormatter();
	private boolean updateHeaderTitle = false;
	
	/**
	 * NOTE: do not use this constructor
	 * use MatTabLayoutPanel(boolean updateHeaderTitle) instead
	 * setting flags as needed
	 * TODO: either remove this constructor or prevent its use
	 * @param barHeight
	 * @param barUnit
	 */
	@Deprecated
	public MatTabLayoutPanel(double barHeight, Unit barUnit) {
		//super(barHeight, barUnit);
		addBeforeSelectionHandler(this);
	}
	
	/**
	 * @param updateHeaderTitle perform automatic header title updates 
	 * i.e. add right-facing arrow to the title of the new selection and remove it from the title of the old selection 
	 */
	public MatTabLayoutPanel(boolean updateHeaderTitle) {
		addBeforeSelectionHandler(this);
		this.updateHeaderTitle = updateHeaderTitle;
	}
	
	public void setId(String id) {
		DOM.setElementAttribute(getElement(), "id", id);
	}
	
	String getId() {
		return DOM.getElementAttribute(getElement(), "id");
	}
	
	public Integer getSelectedIndex() {
		return selectedIndex;
	}
	
	public void setSelectedIndex(Integer selectedIndex){
		this.selectedIndex = selectedIndex;
	}
	public void close() {
		notifyCurrentTabOfClosing();
//		selectedIndex = -1;
	}
	
	private void notifyCurrentTabOfClosing() {
		Integer oldIndex = getSelectedIndex();
		MatPresenter oldPresenter = presenterMap.get(oldIndex);
		if(oldPresenter != null) {
			MatContext.get().setAriaHidden(oldPresenter.getWidget(),  "true");
			oldPresenter.beforeClosingDisplay();
		}
	}
	@Override
	public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
		if(!getSelectedIndex().equals(event.getItem())) {
			updateHeaderSelection(event.getItem());
			//Mat.showLoadingMessage();
			notifyCurrentTabOfClosing();
			setSelectedIndex(event.getItem());
			MatPresenter presenter = presenterMap.get(selectedIndex);
			if(presenter != null) {
				MatContext.get().setAriaHidden(presenter.getWidget(),  "false");
				presenter.beforeDisplay();
			}
		}
	}
	
//TODO refactor some methods so we can 	pass through the two strings without overwriting GWT's interface
//	public void add(Widget w, String tabText, String abbrText) {
//		int index = getWidgetCount() - 1;
//		fmt.insertTitle(index, tabText, abbrText);
//		add(w, tabText);
//	}
	
	@Override
	public void add(Widget w, String tabText) {
	    insert(w, tabText, true, getWidgetCount());
	    int index = getWidgetCount() - 1;
	    fmt.insertTitle(index, tabText);
	}
	
	public int addPresenter(MatPresenter presenter, String title) {
		MatContext.get().setAriaHidden(presenter.getWidget(),  "true");
		add(presenter.getWidget(), title);
		int index = getWidgetCount() - 1;
		presenterMap.put(index, presenter);
		fmt.insertTitle(index, title);
		return index;
	}
	
	public boolean isBeingDisplayed(Widget widget) {
		return getWidgetIndex(widget) == selectedIndex;
	}
	
	//TODO need to remove this method once everything has been tested, keeping this  still if something is  breaking.
	/*public void selectTab(Widget widget) {
		int widgetIndex = getWidgetIndex(widget);
		if(widgetIndex == -1){//TODO;- why it is -1 here???
			widgetIndex = 0;
		}
		selectTab(widgetIndex);
		getTabBar().setTabHTML(widgetIndex, fmt.getSelectedTitle(widgetIndex));
	}*/
	public void selectTab(MatPresenter presenter) {
		MatContext.get().setAriaHidden(presenter.getWidget(), "false");
		int widgetIndex = 0;
		for (Map.Entry<Integer, MatPresenter> entry :presenterMap.entrySet()) {
			 if(entry.getValue().equals(presenter)){
				 widgetIndex = entry.getKey();
				 break;
			 }
		} 
		selectTab(widgetIndex);
		getTabBar().setTabHTML(widgetIndex, fmt.getSelectedTitle(widgetIndex));
	}
	public void selectNextTab() {
		selectTab(selectedIndex + 1);
	}
	public void selectThisTab() {
		selectTab(selectedIndex);
	}
	
	public void selectPreviousTab() {
		selectTab(selectedIndex - 1);
	}
	public boolean hasNextTab() {
		return selectedIndex < getWidgetCount() - 1;
	}
	public boolean hasPreviousTab() {
		return selectedIndex != 0;
	}
	
	/*
	 * TODO may not need
	 */
	public void forceSelectTab(int index){
		updateHeaderSelection(index);
		super.selectTab(index);
		setSelectedIndex(index);
	}
	
	private void updateHeaderSelection(int index){
		if(updateHeaderTitle){
			//Strip arrow off of the tab we are leaving
			int currentTab = selectedIndex;
			getTabBar().setTabHTML(currentTab, fmt.getTitle(currentTab));
			//Add arrow to new tab that is selected
			getTabBar().setTabHTML(index, fmt.getSelectedTitle(index));
		}
	}
	
}
