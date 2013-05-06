package mat.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.Enableable;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.MatClausePresenter;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.metadata.MetaDataPresenter;
import mat.client.shared.ui.MATTabPanel;
import mat.shared.DynamicTabBarFormatter;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class MatTabLayoutPanel extends MATTabPanel implements BeforeSelectionHandler<Integer>, Enableable {
	private Map<Integer, MatPresenter> presenterMap = new HashMap<Integer, MatPresenter>();
	private int selectedIndex = 0;
	public DynamicTabBarFormatter fmt = new DynamicTabBarFormatter();
	private boolean updateHeaderTitle = false;
	boolean isError = false;
	private int currentSelection;
	private ErrorMessageDisplay saveErrorMessage;
	private Button saveButton;
	
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
		currentSelection =  event.getItem();
		if(!getSelectedIndex().equals(currentSelection)) {
			if(!isSaveMeasureDetails(getSelectedIndex(), currentSelection)){
				updateOnBeforeSelection();
			}else{
				setSelectedIndex(getSelectedIndex());
			}
			
		}
	}

	private void updateOnBeforeSelection() {
		updateHeaderSelection(currentSelection);	
		notifyCurrentTabOfClosing();
		setSelectedIndex(currentSelection);
		MatPresenter presenter = presenterMap.get(selectedIndex);
		if(presenter != null) {
			MatContext.get().setAriaHidden(presenter.getWidget(),  "false");
			presenter.beforeDisplay();
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
	
	public void updateHeaderSelection(int index){
		if(updateHeaderTitle){
			//Strip arrow off of the tab we are leaving
			int currentTab = selectedIndex;
			getTabBar().setTabHTML(currentTab, fmt.getTitle(currentTab));
			//Add arrow to new tab that is selected
			getTabBar().setTabHTML(index, fmt.getSelectedTitle(index));
		}
	}
	
	
	/**
	 * On Tab Change, before the tab change happens, this method will be checked to see, 
	 * if the selected tab is  "Measure Composer" Tab and sub Tab is "Measure Details" Tab
	 * 	
	 * @param selectedIndex
	 * @param currentIndex
	 * @return
	 */
	private boolean isSaveMeasureDetails(int selectedIndex, int currentIndex){
		MatContext.get().setErrorTabIndex(-1);		
		MatPresenter previousPresenter = presenterMap.get(selectedIndex);
		if(selectedIndex == 2 && previousPresenter instanceof MeasureComposerPresenter){				
			MeasureComposerPresenter composerPresenter = (MeasureComposerPresenter)previousPresenter;
			if(composerPresenter.getMeasureComposerTabLayout().getSelectedIndex() == 0){
				MetaDataPresenter metaDataPresenter = composerPresenter.getMetaDataPresenter();
				processMeasureDetails(selectedIndex, metaDataPresenter);
			}/*else if(composerPresenter.getMeasureComposerTabLayout().getSelectedIndex() == 2){
				MatClausePresenter matClausePresenter = composerPresenter.getClauseWorkspace();
				createErrorMessageForClauseWorkSpace(matClausePresenter);
			}*/
		}else if(selectedIndex == 0 && previousPresenter instanceof MetaDataPresenter){
			MetaDataPresenter metaDataPresenter = (MetaDataPresenter)previousPresenter;
			processMeasureDetails(selectedIndex, metaDataPresenter);
		}
		else if(selectedIndex == 2 && previousPresenter instanceof MatClausePresenter){
			MatClausePresenter matClausePresenter = (MatClausePresenter)previousPresenter;
			createErrorMessageForClauseWorkSpace(matClausePresenter);
		}
		return isError;
	}

	/**
	 * checks if the Measure Details Page data and the Measure Details DB data are the same.
	 * If Not Same shows Error Message with Buttons
	 * @param selectedIndex
	 * @param metaDataPresenter
	 */
	private void processMeasureDetails(int selectedIndex,
			MetaDataPresenter metaDataPresenter) {		
		if(MatContext.get().getCurrentMeasureId() != null && !MatContext.get().getCurrentMeasureId().equals("") 
				&&!isMeasureDetailsSame(metaDataPresenter)){
			saveErrorMessage = metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg();
			saveErrorMessage.clear();
			saveButton = metaDataPresenter.getMetaDataDisplay().getSaveBtn();		
			if(metaDataPresenter.isSubView()){
				metaDataPresenter.backToDetail();
			}
			showErrorMessage(metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg());
			metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg().getButtons().get(0).setFocus(true);
			callClickEventsOnMsg(selectedIndex, metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg().getButtons(),metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg() );
		}else{
			isError = false;
		}
	}
	
	private void createErrorMessageForClauseWorkSpace(MatClausePresenter matClausePresenter ){
		if(matClausePresenter.getAppController().isCanvasModified()){
			saveErrorMessage = matClausePresenter.getAppController().getSaveErrorMessages();
			saveErrorMessage.clear();
			saveButton = matClausePresenter.getAppController().getDiagramView().getSaveButton();
			showErrorMessage(matClausePresenter.getAppController().getSaveErrorMessages());
			matClausePresenter.getAppController().getSaveErrorMessages().getButtons().get(0).setFocus(true);
			callClickEventsOnMsg(selectedIndex, matClausePresenter.getAppController().getSaveErrorMessages().getButtons(),matClausePresenter.getAppController().getSaveErrorMessages());
		}else{
			isError = false;
		}
		
		
	}
	
	/**
	 * On Click Events.
	 * @param selIndex
	 * @param btns
	 */
	private void callClickEventsOnMsg(int selIndex, List<SecondaryButton> btns, final ErrorMessageDisplay saveErrorMessage) {
		isError = true;
			ClickHandler clickHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					isError = false;
					SecondaryButton button = (SecondaryButton)event.getSource();
					if("Yes".equals(button.getText())){// navigate to the tab select
						saveErrorMessage.clear();
						updateOnBeforeSelection();
						selectTab(selectedIndex);
					}else if("No".equals(button.getText())){// do not navigate, set focus to the Save button on the Page
						saveErrorMessage.clear();
						saveButton.setFocus(true);
					}
				}
			};
			for (SecondaryButton secondaryButton : btns) {
				secondaryButton.addClickHandler(clickHandler);
			}
			
			if(isError){
				MatContext.get().setErrorTabIndex(selIndex);
				MatContext.get().setErrorTab(true);
			}
	}
	
	 
	private void showErrorMessage(ErrorMessageDisplay errorMessageDisplay){
		String msg = MatContext.get().getMessageDelegate().getSaveErrorMsg();
		List<String> btn = new ArrayList<String>();
		btn.add("Yes");
		btn.add("No");
		errorMessageDisplay.setMessageWithButtons(msg, btn);
	}
	
	
	/**
	 * Checked to see if the Measure Details page Data and the DB data are the same.
	 * @param metaDataPresenter
	 * @return
	 */
	private boolean isMeasureDetailsSame(MetaDataPresenter metaDataPresenter){
		ManageMeasureDetailModel pageData = new ManageMeasureDetailModel();
		metaDataPresenter.updateModelDetailsFromView(pageData, metaDataPresenter.getMetaDataDisplay());
		ManageMeasureDetailModel dbData = metaDataPresenter.getCurrentMeasureDetail();
		pageData.setToCompareAuthor(pageData.getAuthorList());
		pageData.setToCompareMeasure(pageData.getMeasureTypeList());
		dbData.setToCompareAuthor(metaDataPresenter.getDbAuthorList());
		dbData.setToCompareMeasure(metaDataPresenter.getDbMeasureTypeList());
		return pageData.equals(dbData);
	}

	
	/**
	 * @return the saveErrorMessage
	 */
	public ErrorMessageDisplay getSaveErrorMessage() {
		return saveErrorMessage;
	}

	/**
	 * @param saveErrorMessage the saveErrorMessage to set
	 */
	public void setSaveErrorMessage(ErrorMessageDisplay saveErrorMessage) {
		this.saveErrorMessage = saveErrorMessage;
	}
	
	
}
