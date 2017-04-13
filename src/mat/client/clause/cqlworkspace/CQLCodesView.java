package mat.client.clause.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import mat.client.CustomPager;
import mat.client.Mat;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.MatTextBox;
import mat.model.cql.CQLQualityDataSetDTO;



// TODO: Auto-generated Javadoc
/**
 * The Class QDMAppliedSelectionView.
 */
public class CQLCodesView implements HasSelectionHandlers<Boolean>{
	
	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		
		/**
		 * On modify clicked.
		 * 
		 * @param result
		 *            the result
		 */
		void onModifyClicked(CQLQualityDataSetDTO result);
		
		/**
		 * On delete clicked.
		 *
		 * @param result            the result
		 * @param index the index
		 */
		void onDeleteClicked(CQLQualityDataSetDTO result, int index);
		
	}
	
	/** The observer. */
	private Observer observer;
	
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	
	/** The vsacapi service async. */
	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();
	
	
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	
	/** The cell table panel. */
	private Panel cellTablePanel = new Panel();
	
	/** The table. */
	private CellTable<CQLQualityDataSetDTO> table;
	
	/** The sort provider. */
	private ListDataProvider<CQLQualityDataSetDTO> listDataProvider;
	
	/** The last selected object. */
	private CQLQualityDataSetDTO lastSelectedObject;
	
	/** the Code Descriptor input */
	private MatTextBox codeDescriptorInput = new MatTextBox();
	
	/** the Code */
	private MatTextBox codeInput = new MatTextBox();
	
	/** the Code system input */
	private MatTextBox codeSystemInput = new MatTextBox();
	
	/** the Code system Version input */
	private MatTextBox codeSystemVersionInput = new MatTextBox();
	
	/** The is editable. */
	private boolean isEditable;
	
	/** The spager. */
	private MatSimplePager spager;
	
	/** The save cancel button bar. */
	private Button saveCode = new Button("Apply");
	
	/** The cancel button. */
	private Button cancelButton = new Button("Cancel");
	
	/** The s widget. */
	private SearchWidgetBootStrap sWidget = new SearchWidgetBootStrap("Retrieve","Enter Code");
	
	/** The main panel. */
	private VerticalPanel mainPanel;
	
	/** The search header. */
	private PanelHeader searchHeader = new PanelHeader();
	
	/** The cell table main panel. */
	SimplePanel cellTableMainPanel = new SimplePanel();
	
	/** The cell table panel body. */
	private PanelBody cellTablePanelBody = new PanelBody();
	
	
	/**
	 * Instantiates a new VSAC profile selection view.
	 */
	public CQLCodesView() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.getElement().setId("simplePanel_SimplePanel");
		simplePanel.setWidth("5px");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		hp.add(buildElementWithCodesWidget());
		hp.add(simplePanel);
		
		verticalPanel.getElement().setId("vPanel_VerticalPanel");
		verticalPanel.add(new SpacerWidget());
		
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(hp);
		verticalPanel.add(new SpacerWidget());
			
		mainPanel.add(verticalPanel);
		containerPanel.getElement().setAttribute("id",
				"codesContainerPanel");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("cqlcodesContentPanel");
	}
	
	/**
	 * Builds the cell table widget.
	 *
	 * @return the simple panel
	 */
	public SimplePanel buildCellTableWidget(){
		cellTableMainPanel.clear();
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setStyleName("cqlqdsContentPanel");
		vPanel.getElement().setId("hPanel_HorizontalPanel");
		vPanel.setWidth("100%");
		vPanel.add(cellTablePanel);

		cellTableMainPanel.add(vPanel);
		return cellTableMainPanel;
	}
	
	/**
	 * Builds the element with vsac value set widget.
	 *
	 * @return the widget
	 */
	private Widget buildElementWithCodesWidget() {
		mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_VerticalPanel");
		
		mainPanel.add(buildSearchPanel());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		return mainPanel;
	}
	
	
	/**
	 * Builds the search panel.
	 *
	 * @return the widget
	 */
	private Widget buildSearchPanel() {
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		Panel searchPanel = new Panel();
		PanelBody searchPanelBody = new PanelBody();
		
		
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("cqlvalueSetSearchPanel");
		
		searchHeader.setStyleName("measureGroupingTableHeader");
		
		
		searchPanel.add(searchHeader);
		searchPanel.setWidth("350px");
		searchPanel.setHeight("355px");
		searchPanelBody.add(new SpacerWidget());
		
		saveCode.setText("Apply");
		saveCode.setTitle("Apply");
		saveCode.setType(ButtonType.PRIMARY);
		
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.setTitle("Cancel");
		
		Grid queryGrid = new Grid(7, 1);
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		buttonToolBar.add(saveCode);
		buttonToolBar.add(cancelButton);
		
		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.add(new SpacerWidget());
		buttonPanel.add(buttonToolBar);
		buttonPanel.add(new SpacerWidget());
		
		
		
		 VerticalPanel searchWidgetFormGroup = new VerticalPanel();
		 searchWidgetFormGroup.add(sWidget.getSearchWidget());
		 searchWidgetFormGroup.add(new SpacerWidget());

		 VerticalPanel versionFormGroup = new VerticalPanel();
		 FormLabel verLabel = new FormLabel();
		 verLabel.setText("Code System Version");
		 verLabel.setTitle("Code System Version");
		 
		 VerticalPanel codeSystemGroup = new VerticalPanel();
		 FormLabel codeSystemLabel = new FormLabel();
		 codeSystemLabel.setText("Code System");
		 codeSystemLabel.setTitle("Code System");
		 codeSystemInput.setWidth("200px");
		 
		 VerticalPanel codeGroup = new VerticalPanel();
		 FormLabel codeLabel = new FormLabel();
		 codeLabel.setText("Code");
		 codeLabel.setTitle("Code");
		 
		 VerticalPanel codeDescriptorGroup = new VerticalPanel();
		 FormLabel codeDescriptorLabel = new FormLabel();
		 codeDescriptorLabel.setText("Code Descriptor");
		 codeDescriptorLabel.setTitle("Code Descriptor");
		 codeDescriptorInput.setWidth("200px");
		 
		 codeDescriptorGroup.add(codeDescriptorLabel);
		 codeDescriptorGroup.add(codeDescriptorInput);
		 codeDescriptorGroup.add(new SpacerWidget());
		 codeGroup.add(codeLabel);
		 codeGroup.add(codeInput);
		 codeGroup.add(new SpacerWidget());
		 codeSystemGroup.add(codeSystemLabel);
		 codeSystemGroup.add(codeSystemInput);
		 codeSystemGroup.add(new SpacerWidget());
		 versionFormGroup.add(verLabel);
		 versionFormGroup.add(codeSystemVersionInput);
		 versionFormGroup.add(new SpacerWidget());
		 
		 VerticalPanel buttonFormGroup = new VerticalPanel();
		 buttonFormGroup.add(buttonToolBar);
		 buttonFormGroup.add(new SpacerWidget());
		 
		
		queryGrid.setWidget(0, 0, searchWidgetFormGroup);
		queryGrid.setWidget(1, 0, codeDescriptorGroup);
		queryGrid.setWidget(2, 0, codeGroup);
		queryGrid.setWidget(3, 0, codeSystemGroup);
		queryGrid.setWidget(4, 0, versionFormGroup);
		queryGrid.setWidget(5, 0, buttonFormGroup);
		queryGrid.setStyleName("secondLabel");
		
		 
		searchPanelBody.add(queryGrid);
			
		searchPanel.add(searchPanelBody);
		return searchPanel;
	}
	
	
	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	public Widget asWidget() {
		return containerPanel;
	}
	
	/**
	 * Gets the data type text.
	 *
	 * @param inputListBox the input list box
	 * @return the data type text
	 */
	public String getDataTypeText(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getItemText(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/**
	 * Gets the data type value.
	 *
	 * @param inputListBox the input list box
	 * @return the data type value
	 */
	public String getDataTypeValue(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/**
	 * Gets the version value.
	 *
	 * @param inputListBox the input list box
	 * @return the version value
	 */
	public String getVersionValue(ListBox inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/**
	 * Gets the expansion Profile value.
	 *
	 * @param inputListBox the input list box
	 * @return the expansion Profile value
	 */
	public String getExpansionProfileValue(ListBox inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/**
	 * Check for enable.
	 * 
	 * @return true, if successful
	 */
	private boolean checkForEnable() {
		return MatContext.get().getMeasureLockService()
				.checkForEditPermission();
	}

	/**
	 * Reset vsac value set widget.
	 */
	public void resetVSACValueSetWidget() {
		if(checkForEnable()){
			sWidget.getSearchBox().setTitle("Enter OID");
		}
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		searchHeader.clear();
		searchHeader.add(searchHeaderText);
	}
	/**
	 * Fire event.
	 *
	 * @param event the event
	 */
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	/**
	 * Adds the selection handler.
	 *
	 * @param handler the handler
	 * @return the handler registration
	 */
	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Boolean> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	
	/**
	 * Gets the observer.
	 * 
	 * @return the observer
	 */
	public Observer getObserver() {
		return observer;
	}
	
	/**
	 * Sets the observer.
	 *
	 * @param observer the new observer
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	
	/**
	 * Gets the cancel qdm button.
	 *
	 * @return the cancel qdm button
	 */
	public Button getCancelCodeButton() {
		return cancelButton;
	}
	
	/**
	 * Gets the retrieve from vsac button.
	 *
	 * @return the retrieve from vsac button
	 */
	public org.gwtbootstrap3.client.ui.Button getRetrieveFromVSACButton(){
		return sWidget.getGo();
	}
	
	/**
	 * Gets the save button.
	 *
	 * @return the save button
	 */
	public Button getSaveButton(){
		return saveCode;
	}
	
	/**
	 * Gets the selected element to remove.
	 *
	 * @return the selected element to remove
	 */
	public CQLQualityDataSetDTO getSelectedElementToRemove() {
		return lastSelectedObject;
	}
	
	/**
	 * Gets the code input.
	 *
	 * @return the code input
	 */
	public TextBox getCodeInput() {
		return sWidget.getSearchBox();
	}
	
	/**
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return isEditable;
	}
	
	
	/**
	 * Sets the editable.
	 *
	 * @param isEditable the new editable
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	/**
	 * Gets the search header.
	 *
	 * @return the search header
	 */
	public PanelHeader getSearchHeader() {
		return searchHeader;
	}
	
	
	/**
	 * Gets the list data provider.
	 *
	 * @return the list data provider
	 */
	public ListDataProvider<CQLQualityDataSetDTO> getListDataProvider(){
		return listDataProvider;
	}
	
	/**
	 * Gets the simple pager.
	 *
	 * @return the simple pager
	 */
	public MatSimplePager getSimplePager(){
		return spager;
	}
	
	/**
	 * Gets the celltable.
	 *
	 * @return the celltable
	 */
	public CellTable<CQLQualityDataSetDTO> getCelltable(){
		return table;
	}
	
	/**
	 * Gets the pager.
	 *
	 * @return the pager
	 */
	public MatSimplePager getPager(){
		return spager;
	}
	
	/**
	 * Gets the main panel.
	 *
	 * @return the main panel
	 */
	public VerticalPanel getMainPanel(){
		return mainPanel;
	}
	
	
	
	/**
	 * Sets the widgets read only.
	 *
	 * @param editable the new widgets read only
	 */
	public void setWidgetsReadOnly(boolean editable){
		
		getCodeInput().setEnabled(editable);
		
		getCancelCodeButton().setEnabled(editable);
		getRetrieveFromVSACButton().setEnabled(editable);
		getSaveButton().setEnabled(false);
		
	}
	
	/**
	 * Sets the widget to default.
	 */
	public void setWidgetToDefault() {
		getCodeInput().setValue("");
		getSaveButton().setEnabled(false);
	}
	
	/**
	 * This method enable/disable's reterive and updateFromVsac button
	 * and hide/show loading please wait message.
	 *
	 * @param busy the busy
	 */
	public void showSearchingBusyOnCodes(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		getRetrieveFromVSACButton().setEnabled(!busy);
	}
	
	/**
	 * Gets the cell table main panel.
	 *
	 * @return the cell table main panel
	 */
	public SimplePanel getCellTableMainPanel(){
		return cellTableMainPanel;
	}
	
	/**
	 * Clear cell table main panel.
	 */
	public void clearCellTableMainPanel(){
		cellTableMainPanel.clear();
	}
	
	/**
	 * Convert message.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	public String convertMessage(final int id) {
		String message;
		switch (id) {
		case VsacApiResult.UMLS_NOT_LOGGEDIN:
			message = MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN();
			break;
		case VsacApiResult.OID_REQUIRED:
			message = MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED();
			break;
		default:
			message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED();
		}
		return message;
	}
	
	
	/**
	 * Reset QDM search panel.
	 */
	public void resetCQLCodesSearchPanel() {
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		getSearchHeader().clear();
		getSearchHeader().add(searchHeaderText);
		
		getCodeInput().setEnabled(true);
		getCodeInput().setValue("");
		getCodeInput().setTitle("Enter OID");
		
		getSaveButton().setEnabled(false);
		
	}

	public void buildCodesCellTable(List<CQLQualityDataSetDTO> codesTableList, boolean checkForEditPermission) {
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		cellTablePanel.setWidth("95%");
		PanelHeader codesElementsHeader = new PanelHeader();
		codesElementsHeader.getElement().setId("searchHeader_Label");
		codesElementsHeader.setStyleName("measureGroupingTableHeader");
		codesElementsHeader.getElement().setAttribute("tabIndex", "0");
		
		HTML searchHeaderText = new HTML("<strong>Codes</strong>");
		codesElementsHeader.add(searchHeaderText);
		cellTablePanel.add(codesElementsHeader);
		HTML desc = new HTML("<p> No Codes.</p>");
		cellTablePanelBody.add(desc);
		cellTablePanel.add(cellTablePanelBody);
	
		
	}
	
}
