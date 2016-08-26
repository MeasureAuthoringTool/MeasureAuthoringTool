package mat.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Enableable;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.presenter.ClauseWorkSpacePresenter;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkspacePresenter;
import mat.client.clause.clauseworkspace.presenter.XmlTreePresenter;
import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.metadata.MetaDataPresenter;
import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackagePresenter;
import mat.client.shared.ui.MATTabPanel;
import mat.shared.ConstantMessages;
import mat.shared.DynamicTabBarFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class MatTabLayoutPanel.
 */
public class MatTabLayoutPanel extends MATTabPanel implements BeforeSelectionHandler<Integer>, Enableable {
	
	/** The presenter map. */
	private Map<Integer, MatPresenter> presenterMap = new HashMap<Integer, MatPresenter>();
	
	/** The selected index. */
	private int selectedIndex = 0;
	
	/** The fmt. */
	public DynamicTabBarFormatter fmt = new DynamicTabBarFormatter();
	
	/** The update header title. */
	private boolean updateHeaderTitle = false;
	
	/** The is unsaved data. */
	private boolean isUnsavedData = false;
	
	/** The current selection. */
	private int currentSelection;
	
	/** The save error message. */
	private ErrorMessageDisplay saveErrorMessage;
	
	/** The save button. */
	private Button saveButton;
	
	/**
	 * NOTE: do not use this constructor use MatTabLayoutPanel(boolean
	 * updateHeaderTitle) instead setting flags as needed TODO: either remove
	 * this constructor or prevent its use.
	 * 
	 * @param barHeight
	 *            the bar height
	 * @param barUnit
	 *            the bar unit
	 */
	@Deprecated
	public MatTabLayoutPanel(double barHeight, Unit barUnit) {
		//super(barHeight, barUnit);
		addBeforeSelectionHandler(this);
	}
	
	/**
	 * Instantiates a new mat tab layout panel.
	 * 
	 * @param updateHeaderTitle
	 *            perform automatic header title updates i.e. add right-facing
	 *            arrow to the title of the new selection and remove it from the
	 *            title of the old selection
	 */
	public MatTabLayoutPanel(boolean updateHeaderTitle) {
		addBeforeSelectionHandler(this);
		this.updateHeaderTitle = updateHeaderTitle;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		DOM.setElementAttribute(getElement(), "id", id);
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	String getId() {
		return DOM.getElementAttribute(getElement(), "id");
	}
	
	/**
	 * Gets the selected index.
	 * 
	 * @return the selected index
	 */
	public Integer getSelectedIndex() {
		return selectedIndex;
	}
	
	/**
	 * Sets the selected index.
	 * 
	 * @param selectedIndex
	 *            the new selected index
	 */
	public void setSelectedIndex(Integer selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	/**
	 * Close.
	 */
	public void close() {
		notifyCurrentTabOfClosing();
		//		selectedIndex = -1;
	}
	
	/**
	 * Notify current tab of closing.
	 */
	private void notifyCurrentTabOfClosing() {
		Integer oldIndex = getSelectedIndex();
		MatPresenter oldPresenter = presenterMap.get(oldIndex);
		if (oldPresenter != null) {
			MatContext.get().setAriaHidden(oldPresenter.getWidget(),  "true");
			oldPresenter.beforeClosingDisplay();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.BeforeSelectionHandler#onBeforeSelection(com.google.gwt.event.logical.shared.BeforeSelectionEvent)
	 */
	@Override
	public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
		currentSelection =  event.getItem();
		if (!getSelectedIndex().equals(currentSelection)) {
			if (!isUnsavedDataOnTab(getSelectedIndex(), currentSelection)) {
				updateOnBeforeSelection();
			} else {
				setSelectedIndex(getSelectedIndex());
			}
			
		}
	}
	
	/**
	 * Update on before selection.
	 */
	private void updateOnBeforeSelection() {
		updateHeaderSelection(currentSelection);
		notifyCurrentTabOfClosing();
		setSelectedIndex(currentSelection);
		MatPresenter presenter = presenterMap.get(selectedIndex);
		if (presenter != null) {
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
	
	/* (non-Javadoc)
	 * @see mat.client.shared.ui.MATTabPanel#add(com.google.gwt.user.client.ui.Widget, java.lang.String)
	 */
	@Override
	public void add(Widget w, String tabText) {
		insert(w, tabText, true, getWidgetCount());
		int index = getWidgetCount() - 1;
		fmt.insertTitle(index, tabText);
	}
	
	/**
	 * Adds the presenter.
	 * 
	 * @param presenter
	 *            the presenter
	 * @param title
	 *            the title
	 * @return the int
	 */
	public int addPresenter(MatPresenter presenter, String title) {
		MatContext.get().setAriaHidden(presenter.getWidget(),  "true");
		add(presenter.getWidget(), title);
		int index = getWidgetCount() - 1;
		presenterMap.put(index, presenter);
		fmt.insertTitle(index, title);
		return index;
	}
	
	/**
	 * Checks if is being displayed.
	 * 
	 * @param widget
	 *            the widget
	 * @return true, if is being displayed
	 */
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
	/**
	 * Select tab.
	 * 
	 * @param presenter
	 *            the presenter
	 */
	public void selectTab(MatPresenter presenter) {
		MatContext.get().setAriaHidden(presenter.getWidget(), "false");
		int widgetIndex = 0;
		for (Map.Entry<Integer, MatPresenter> entry :presenterMap.entrySet()) {
			if (entry.getValue().equals(presenter)){
				widgetIndex = entry.getKey();
				break;
			}
		}
		selectTab(widgetIndex);
		getTabBar().setTabHTML(widgetIndex, fmt.getSelectedTitle(widgetIndex));
	}
	
	/**
	 * Select next tab.
	 */
	public void selectNextTab() {
		selectTab(selectedIndex + 1);
	}
	
	/**
	 * Select this tab.
	 */
	public void selectThisTab() {
		selectTab(selectedIndex);
	}
	
	/**
	 * Select previous tab.
	 */
	public void selectPreviousTab() {
		selectTab(selectedIndex - 1);
	}
	
	/**
	 * Checks for next tab.
	 * 
	 * @return true, if successful
	 */
	public boolean hasNextTab() {
		return selectedIndex < (getWidgetCount() - 1);
	}
	
	/**
	 * Checks for previous tab.
	 * 
	 * @return true, if successful
	 */
	public boolean hasPreviousTab() {
		return selectedIndex != 0;
	}
	
	/*
	 * TODO may not need
	 */
	/**
	 * Force select tab.
	 * 
	 * @param index
	 *            the index
	 */
	public void forceSelectTab(int index) {
		updateHeaderSelection(index);
		super.selectTab(index);
		setSelectedIndex(index);
	}
	
	/**
	 * Update header selection.
	 * 
	 * @param index
	 *            the index
	 */
	public void updateHeaderSelection(int index) {
		if (updateHeaderTitle) {
			//Strip arrow off of the tab we are leaving
			int currentTab = selectedIndex;
			getTabBar().setTabHTML(currentTab, fmt.getTitle(currentTab));
			//Add arrow to new tab that is selected
			getTabBar().setTabHTML(index, fmt.getSelectedTitle(index));
		}
	}
	
	
	/**
	 * On Tab Change, before the tab change happens, this method will be checked
	 * to see, if the selected tab is "Measure Composer" Tab and sub Tab is
	 * "Measure Details" Tab.
	 * 
	 * @param selectedIndex
	 *            the selected index
	 * @param currentIndex
	 *            the current index
	 * @return true, if is unsaved data on tab
	 */
	private boolean isUnsavedDataOnTab(int selectedIndex, int currentIndex) {
		MatContext.get().setErrorTabIndex(-1);
		
		MatPresenter previousPresenter = presenterMap.get(selectedIndex);
		if ((selectedIndex == 1) && (previousPresenter instanceof MeasureComposerPresenter)) {
			MeasureComposerPresenter composerPresenter = (MeasureComposerPresenter) previousPresenter;
			if (composerPresenter.getMeasureComposerTabLayout().getSelectedIndex() == 0) {
				MetaDataPresenter metaDataPresenter = composerPresenter.getMetaDataPresenter();
				validateMeasureDetailsTab(selectedIndex, metaDataPresenter);
			} 
			/*else if (composerPresenter.getMeasureComposerTabLayout().getSelectedIndex() == 2) {
				int clauseWorkspaceTab = 2;
				ClauseWorkSpacePresenter clauseWorkspacePresenter = (ClauseWorkSpacePresenter)
						composerPresenter.getMeasureComposerTabLayout().presenterMap.get(clauseWorkspaceTab);
				validateClauseWorkspaceTab(clauseWorkspacePresenter, selectedIndex);
			}*/ 
			
			else if (composerPresenter.getMeasureComposerTabLayout().getSelectedIndex() == 2) {
				int CQLWorkspaceTab = 2;
				CQLWorkSpacePresenter cqlWorkspacePresenter = (CQLWorkSpacePresenter)
						composerPresenter.getMeasureComposerTabLayout().presenterMap.get(CQLWorkspaceTab);
				validateCQLWorkspaceTab(cqlWorkspacePresenter, selectedIndex);
			} else if (composerPresenter.getMeasureComposerTabLayout().getSelectedIndex() == 3) {
				int populationWorkspaceTab = 3;
				PopulationWorkspacePresenter clauseWorkspacePresenter = (PopulationWorkspacePresenter)
						composerPresenter.getMeasureComposerTabLayout().presenterMap.get(populationWorkspaceTab);
				validateClauseWorkspaceTab(clauseWorkspacePresenter.getSelectedTreePresenter(), selectedIndex);
			}
			else if (composerPresenter.getMeasureComposerTabLayout().getSelectedIndex() == 4) {
				int measurePackagerTab = 4;
				MeasurePackagePresenter measurePackagerPresenter = (MeasurePackagePresenter)
						composerPresenter.getMeasureComposerTabLayout().presenterMap.get(measurePackagerTab);
				validateNewMeasurePackageTab(selectedIndex, measurePackagerPresenter);
			}
			
			
		} else if ((selectedIndex == 0) && (previousPresenter instanceof MetaDataPresenter)) {
			MetaDataPresenter metaDataPresenter = (MetaDataPresenter) previousPresenter;
			validateMeasureDetailsTab(selectedIndex, metaDataPresenter);
		} else if ((selectedIndex == 2) && (previousPresenter instanceof CQLWorkSpacePresenter)) {
			CQLWorkSpacePresenter cqlPresenter = (CQLWorkSpacePresenter) previousPresenter;
			validateCQLWorkspaceTab(cqlPresenter, selectedIndex);
		} else if ((selectedIndex == 3) && (previousPresenter instanceof PopulationWorkspacePresenter)) {
			PopulationWorkspacePresenter clauseWorkspacePresenter = (PopulationWorkspacePresenter) previousPresenter;
			validateClauseWorkspaceTab(clauseWorkspacePresenter.getSelectedTreePresenter(), selectedIndex);
		} 
		/*else if (previousPresenter instanceof XmlTreePresenter) {
			validateClauseWorkspaceTab((XmlTreePresenter) previousPresenter, selectedIndex);
		}*/
		else if (previousPresenter instanceof MeasurePackagePresenter) {
			MeasurePackagePresenter measurePackagerPresenter = (MeasurePackagePresenter) previousPresenter;
			validateNewMeasurePackageTab(selectedIndex, measurePackagerPresenter);
		}
		return isUnsavedData;
	}
	
	/**
	 * checks if the Measure Details Page data and the Measure Details DB data
	 * are the same. If Not Same shows Error Message with Buttons
	 * 
	 * @param selectedIndex
	 *            the selected index
	 * @param metaDataPresenter
	 *            the meta data presenter
	 */
	private void validateMeasureDetailsTab(int selectedIndex,
			MetaDataPresenter metaDataPresenter) {
		if ((MatContext.get().getCurrentMeasureId() != null) && !MatContext.get().getCurrentMeasureId().equals("")
				&&!isMeasureDetailsSame(metaDataPresenter)) {
			saveErrorMessage = metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg();
			saveErrorMessage.clear();
			saveButton = metaDataPresenter.getMetaDataDisplay().getSaveBtn();
			if (metaDataPresenter.isSubView()) {
				metaDataPresenter.backToDetail();
				metaDataPresenter.getMetaDataDisplay().setSaveButtonEnabled(
						MatContext.get().getMeasureLockService().checkForEditPermission());
				metaDataPresenter.getComponentMeasures();
				metaDataPresenter.setStewardAndMeasureDevelopers();
			}
			showErrorMessage(metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg());
			metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg().getButtons().get(0).setFocus(true);
			handleClickEventsOnUnsavedErrorMsg(selectedIndex, metaDataPresenter.getMetaDataDisplay()
					.getSaveErrorMsg().getButtons(), metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg(), null);
		} else {
			isUnsavedData = false;
		}
	}
	
	/**
	 * Validate new measure package tab.
	 *
	 * @param selectedIndex the selected index
	 * @param measurePackagerPresenter the measure packager presenter
	 */
	private void validateNewMeasurePackageTab(int selectedIndex,
			MeasurePackagePresenter measurePackagerPresenter) {
		if (!isMeasurePackageDetailsSame(measurePackagerPresenter)) {
			saveErrorMessage = measurePackagerPresenter.getView().getSaveErrorMessageDisplay();
			saveErrorMessage.clear();
			saveButton = measurePackagerPresenter.getView().getPackageGroupingWidget().getSaveGrouping();
			//saveButton = (PrimaryButton)measurePackagerPresenter.getView().getAddQDMElementsToMeasureButton();
			showErrorMessage(measurePackagerPresenter.getView().getSaveErrorMessageDisplay());
			measurePackagerPresenter.getView().getSaveErrorMessageDisplay().getButtons().get(0).setFocus(true);
			handleClickEventsOnUnsavedErrorMsg(selectedIndex, measurePackagerPresenter.getView().getSaveErrorMessageDisplay().getButtons(),
					measurePackagerPresenter.getView().getSaveErrorMessageDisplay(), null);
		} else {
			isUnsavedData = false;
		}
		
	}
	
	/**
	 * Validate cql workspace tab.
	 * 
	 * @param cqlWorkSpacePresenter
	 *            the cql presenter
	 * @param selectedIndex
	 *            the selected index
	 */
	private void validateCQLWorkspaceTab(CQLWorkSpacePresenter cqlWorkSpacePresenter, int selectedIndex) {
		
		cqlWorkSpacePresenter.getSearchDisplay().resetMessageDisplay();
		if (cqlWorkSpacePresenter.getSearchDisplay().getIsPageDirty()) {
			isUnsavedData = true;
			cqlWorkSpacePresenter.getSearchDisplay().getGlobalWarningConfirmationMessageAlert().createAlert();
			cqlWorkSpacePresenter.getSearchDisplay().getGlobalWarningConfirmationMessageAlert().getWarningConfirmationYesButton().setFocus(true);
			String auditMessage = cqlWorkSpacePresenter.getSearchDisplay().getClickedMenu().toUpperCase() + "_TAB_YES_CLICKED";
			handleClickEventsOnUnsavedChangesMsg(selectedIndex, 
					cqlWorkSpacePresenter.getSearchDisplay().getGlobalWarningConfirmationMessageAlert(), auditMessage);
		} else {
			isUnsavedData = false;
		}
	}

	
	/**
	 * Validate clause workspace tab.
	 * 
	 * @param xmlTreePresenter
	 *            the xml tree presenter
	 * @param selectedIndex
	 *            the selected index
	 */
	private void validateClauseWorkspaceTab(XmlTreePresenter xmlTreePresenter, int selectedIndex) {
		if (null ==	xmlTreePresenter.getXmlTreeDisplay()) {// this will happen when there is any errors on Clause Workspace Tabs
			return;
		}
		xmlTreePresenter.getXmlTreeDisplay().clearMessages();
		if (xmlTreePresenter.getXmlTreeDisplay().isDirty()
				|| xmlTreePresenter.getXmlTreeDisplay().isQdmVariableDirty()) {
			isUnsavedData = true;
			saveButton = xmlTreePresenter.getXmlTreeDisplay().getSaveButton();
			showErrorMessage(xmlTreePresenter.getXmlTreeDisplay().getErrorMessageDisplay());
			xmlTreePresenter.getXmlTreeDisplay().getErrorMessageDisplay().getButtons().get(0).setFocus(true);
			String auditMessage = xmlTreePresenter.getRootNode().toUpperCase() + "_TAB_YES_CLICKED";
			handleClickEventsOnUnsavedErrorMsg(selectedIndex, xmlTreePresenter.getXmlTreeDisplay().getErrorMessageDisplay().getButtons(),
					xmlTreePresenter.getXmlTreeDisplay().getErrorMessageDisplay(),auditMessage);
		} else {
			isUnsavedData = false;
		}
	}

	
	/**
	 * On Click Events.
	 * 
	 * @param selIndex
	 *            the sel index
	 * @param btns
	 *            the btns
	 * @param saveErrorMessage
	 *            the save error message
	 * @param auditMessage
	 *            the audit message
	 */
	private void handleClickEventsOnUnsavedChangesMsg(int selIndex, final WarningConfirmationMessageAlert globalWarningAlert, final String auditMessage) {
		isUnsavedData = true;
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isUnsavedData = false;
				org.gwtbootstrap3.client.ui.Button button = (org.gwtbootstrap3.client.ui.Button) event.getSource();
				if ("Yes".equals(button.getText())) { // navigate to the tab select
					//Audit If Yes is clicked and changes are discarded on cqlWorkspace.
					if (auditMessage != null) {
						MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(),
								null, auditMessage, auditMessage, ConstantMessages.DB_LOG);
					}
					globalWarningAlert.clearAlert();
					updateOnBeforeSelection();
					selectTab(selectedIndex);
				} else if ("No".equals(button.getText())) { // do not navigate, set focus to the Save button on the Page
					selectTab(selectedIndex);
					globalWarningAlert.clearAlert();
				}
			}
		};
		
		globalWarningAlert.getWarningConfirmationYesButton().addClickHandler(clickHandler);
		globalWarningAlert.getWarningConfirmationNoButton().addClickHandler(clickHandler);
		
		if (isUnsavedData) {
			MatContext.get().setErrorTabIndex(selIndex);
			MatContext.get().setErrorTab(true);
		}
	}
	
	/**
	 * On Click Events.
	 * 
	 * @param selIndex
	 *            the sel index
	 * @param btns
	 *            the btns
	 * @param saveErrorMessage
	 *            the save error message
	 * @param auditMessage
	 *            the audit message
	 */
	private void handleClickEventsOnUnsavedErrorMsg(int selIndex, List<SecondaryButton> btns,
			final ErrorMessageDisplay saveErrorMessage, final String auditMessage) {
		isUnsavedData = true;
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isUnsavedData = false;
				SecondaryButton button = (SecondaryButton) event.getSource();
				if ("Yes".equals(button.getText())) { // navigate to the tab select
					//Audit If Yes is clicked and changes are discarded on clauseWorkspace.
					if (auditMessage != null) {
						MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(),
								null, auditMessage, auditMessage, ConstantMessages.DB_LOG);
					}
					saveErrorMessage.clear();
					updateOnBeforeSelection();
					selectTab(selectedIndex);
				} else if ("No".equals(button.getText())) { // do not navigate, set focus to the Save button on the Page
					saveErrorMessage.clear();
					saveButton.setFocus(true);
				}
			}
		};
		for (SecondaryButton secondaryButton : btns) {
			secondaryButton.addClickHandler(clickHandler);
		}
		
		if (isUnsavedData) {
			MatContext.get().setErrorTabIndex(selIndex);
			MatContext.get().setErrorTab(true);
		}
	}
	
	/**
	 * Show error message.
	 * 
	 * @param errorMessageDisplay
	 *            the error message display
	 */
	private void showErrorMessage(ErrorMessageDisplay errorMessageDisplay) {
		String msg = MatContext.get().getMessageDelegate().getSaveErrorMsg();
		List<String> btn = new ArrayList<String>();
		btn.add("Yes");
		btn.add("No");
		errorMessageDisplay.setMessageWithButtons(msg, btn);
	}
	
	
	/**
	 * Checked to see if the Measure Details page Data and the DB data are the
	 * same.
	 * 
	 * @param metaDataPresenter
	 *            the meta data presenter
	 * @return true, if is measure details same
	 */
	private boolean isMeasureDetailsSame(MetaDataPresenter metaDataPresenter) {
		ManageMeasureDetailModel pageData = new ManageMeasureDetailModel();
		metaDataPresenter.updateModelDetailsFromView(pageData, metaDataPresenter.getMetaDataDisplay());
		ManageMeasureDetailModel dbData = metaDataPresenter.getCurrentMeasureDetail();
		if (dbData.isDeleted() || !dbData.isEditable()) {
			//dont show dirty check message when measure is deleted.
			return true;
		} else {
			pageData.setToCompareAuthor(pageData.getAuthorSelectedList());
			pageData.setToCompareMeasure(pageData.getMeasureTypeSelectedList());
			pageData.setToCompareComponentMeasures(pageData.getComponentMeasuresSelectedList());
			dbData.setToCompareAuthor(metaDataPresenter.getDbAuthorList());
			dbData.setToCompareMeasure(metaDataPresenter.getDbMeasureTypeList());
			dbData.setToCompareComponentMeasures(metaDataPresenter.getDbComponentMeasuresSelectedList());
			return pageData.equals(dbData);
		}
	}
	
	/**
	 * Checks if is measure package details same.
	 *
	 * @param measurePackagePresenter the measure package presenter
	 * @return true, if is measure package details same
	 */
	private boolean isMeasurePackageDetailsSame(MeasurePackagePresenter measurePackagePresenter){
		
		MeasurePackageDetail pageData = new MeasurePackageDetail();
		measurePackagePresenter.updateDetailsFromView(pageData);
		measurePackagePresenter.updateSuppDataDetailsFromView(pageData);
		measurePackagePresenter.updateRiskAdjFromView(pageData);
		MeasurePackageDetail dbData = measurePackagePresenter.getCurrentDetail();
		pageData.setToComparePackageClauses(pageData.getPackageClauses());
		dbData.setToComparePackageClauses(measurePackagePresenter.getDbPackageClauses());
		pageData.setToCompareSuppDataElements(pageData.getSuppDataElements());
		dbData.setToCompareSuppDataElements(measurePackagePresenter.getDbSuppDataElements());
		pageData.setToCompareCqlSuppDataElements(pageData.getCqlSuppDataElements());
		dbData.setToCompareCqlSuppDataElements(measurePackagePresenter.getDbCQLSuppDataElements());
		pageData.setToCompareRiskAdjVars(pageData.getRiskAdjVars());
		dbData.setToCompareRiskAdjVars(measurePackagePresenter.getDbRiskAdjVars());
		return pageData.equals(dbData);
	}
	
	
	/**
	 * Gets the save error message.
	 * 
	 * @return the saveErrorMessage
	 */
	public ErrorMessageDisplay getSaveErrorMessage() {
		return saveErrorMessage;
	}
	
	/**
	 * Sets the save error message.
	 * 
	 * @param saveErrorMessage
	 *            the saveErrorMessage to set
	 */
	public void setSaveErrorMessage(ErrorMessageDisplay saveErrorMessage) {
		this.saveErrorMessage = saveErrorMessage;
	}
	
	/**
	 * Gets the presenter map.
	 * 
	 * @return the presenter map
	 */
	public Map<Integer, MatPresenter> getPresenterMap() {
		return presenterMap;
	}
	
	/**
	 * Sets the presenter map.
	 * 
	 * @param presenterMap
	 *            the presenter map
	 */
	public void setPresenterMap(Map<Integer, MatPresenter> presenterMap) {
		this.presenterMap = presenterMap;
	}
	
	
}
