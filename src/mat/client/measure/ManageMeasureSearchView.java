package mat.client.measure;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.ImageResources;
import mat.client.advancedSearch.AdvancedSearchModel;
import mat.client.advancedSearch.MeasureLibraryAdvancedSearch;
import mat.client.clause.cqlworkspace.EditConfirmationDialogBox;
import mat.client.measure.MeasureSearchView.AdminObserver;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.util.ClientConstants;

/**
 * The Class ManageMeasureSearchView.
 */
public class ManageMeasureSearchView implements
ManageMeasurePresenter.SearchDisplay {
	
	/** The bulk export button. */
	private Button bulkExportButton = new Button("Export Selected");	
	
	/** The create measure button. */
	Button createMeasureButton = new Button("New Measure"); 
	
	/** The table. */
	CellTable<ManageMeasureSearchModel.Result> table;
	
	/** The error messages for transfer os. */
	private MessageAlert errorMessagesForTransferOS = new ErrorMessageAlert();
	
	/** The current user role. */
	String currentUserRole = MatContext.get().getLoggedInUserRole();
	
	/** The error measure deletion. */
	private MessageAlert errorMeasureDeletion = new ErrorMessageAlert();
	
	/** The clear button. */
	private Button clearButton = new Button("Clear All");
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The error messages for bulk export. */
	private MessageAlert errorMessagesForBulkExport = new ErrorMessageAlert();
	
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	
	/** The form. */
	private final FormPanel form = new FormPanel();
	
	/** The measure vpanel. */
	public VerticalPanel measureVpanel = new VerticalPanel();
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The measure search filter widget. */
	private SearchWidgetWithFilter measureSearchFilterWidget = new SearchWidgetWithFilter("searchFilter",
			"measureLibraryFilterDisclosurePanel","forMeasure");
	
	SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search");
	
	/** The most recent measure widget. */
	private MostRecentMeasureWidget mostRecentMeasureWidget = new MostRecentMeasureWidget();
	
	/** The most recent vertical panel. */
	VerticalPanel mostRecentVerticalPanel = new VerticalPanel();
	
	/** The success measure deletion. */
	private MessageAlert successMeasureDeletion = new SuccessMessageAlert();
	
	/** The success message display. */
	private MessageAlert successMessages = new SuccessMessageAlert();
	
	/**  The delete confirmation box. */
	EditConfirmationDialogBox draftConfirmationDialogBox = new EditConfirmationDialogBox();
	
	/** The transfer button. */
	private Button transferButton = new Button("Transfer");
	
	private MeasureLibraryAdvancedSearch measureLibraryAdvancedSearchBuilder = new MeasureLibraryAdvancedSearch();
	
	/** The search view. */
	MeasureSearchView searchView;
	
	/** The view. */
	MeasureSearchView measureSearchView = new MeasureSearchView("Measures");
	
	/** The zoom button. */
	CustomButton zoomButton = (CustomButton) getImage("Search", ImageResources.INSTANCE.search_zoom(), "Search" , "MeasureSearchButton");
	
	/** Instantiates a new manage measure search view. */
	public ManageMeasureSearchView() {
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get()
				.getLoggedInUserRole())){
			mainPanel.add(new SpacerWidget());
			mainPanel.add(buildSearchWidget());
			mainPanel.add(new SpacerWidget());
			mainPanel.add(measureSearchView.asWidget());
			mainPanel.setStyleName("contentPanel");
			mainPanel.add(new SpacerWidget());			
			mainPanel.add(adminBuildBottomButtonWidget( transferButton, clearButton,
					errorMessagesForTransferOS));
		}else{
		mainPanel.add(buildBottomButtonWidget(bulkExportButton,
				errorMessagesForBulkExport));
		
		HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
		mainHorizontalPanel.getElement().setId("panel_MainHorizontalPanel");
		bulkExportButton.getElement().setId("bulkExportButton_Button");
		mainPanel.getElement().setId("measureLibrary_MainPanel");
		mainPanel.setStyleName("contentPanel");
		VerticalPanel measureFilterVP = new VerticalPanel();
		measureFilterVP.setWidth("100px");
		measureFilterVP.getElement().setId("panel_measureFilterVP");
		measureFilterVP.add(measureSearchFilterWidget);
		measureFilterVP.add(measureLibraryAdvancedSearchBuilder.asWidget());
		buildMostRecentWidget();
		mainHorizontalPanel.add(mostRecentVerticalPanel);
		mainHorizontalPanel.add(measureFilterVP);
		mainPanel.add(mainHorizontalPanel);
		mainPanel.add(successMeasureDeletion);
		mainPanel.add(errorMeasureDeletion);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessages);
		mainPanel.add(errorMessages);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(measureSearchView.asWidget());
		mainPanel.add(ManageLoadingView.buildLoadingPanel("loadingPanelExport"));
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildBottomButtonWidget( bulkExportButton,
				errorMessagesForBulkExport));
		}
		MatContext.get().setManageMeasureSearchView(this);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	/**
	 * Builds the bottom button widget.
	 *
	 * @param bulkExportButton the bulk export button
	 * @param errorMessageDisplay the error message display
	 * @return the widget
	 */
	private Widget buildBottomButtonWidget(Button bulkExportButton,
			MessageAlert errorMessageDisplay) {
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.getElement().setId("measureLibrary_bottomPanel");
		flowPanel.add(errorMessageDisplay);
		flowPanel.setStyleName("rightAlignButton");
		bulkExportButton.setTitle("Bulk Export");
		bulkExportButton.setType(ButtonType.PRIMARY);
		bulkExportButton.setIcon(IconType.DOWNLOAD);
		bulkExportButton.setTitle(bulkExportButton.getText());
		flowPanel.add(bulkExportButton);
		form.setWidget(flowPanel);
		form.getElement().setId("measureLibrary_bottomPanelForm");
		return form;
	}
	
	/**
	 * Admin build bottom button widget.
	 *
	 * @param transferButton the transfer button
	 * @param clearButton the clear button
	 * @param errorMessageDisplay the error message display
	 * @return the widget
	 */
	public Widget adminBuildBottomButtonWidget(Button transferButton, Button clearButton,
			MessageAlert errorMessageDisplay) {
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(errorMessageDisplay);
		transferButton.setTitle("Transfer");
		clearButton.setTitle("Clear All");
		transferButton.setType(ButtonType.PRIMARY);
		clearButton.setType(ButtonType.DANGER);
		clearButton.setMarginLeft(10.00);
		flowPanel.add(transferButton);
		flowPanel.add(clearButton);
		form.setWidget(flowPanel);
		return form;
	}
	
	/**
	 * Builds the search widget.
	 *
	 * @return the widget
	 */
	public Widget buildSearchWidget() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(searchWidgetBootStrap.getSearchWidget());

		return hp;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#buildDataTable(mat.client.measure.ManageMeasureSearchModel, int, java.lang.String)
	 */
	@Override
	public void buildDataTable(ManageMeasureSearchModel
			manageMeasureSearchModel, int filter, String searchText){
		measureSearchView.buildCellTable(manageMeasureSearchModel,filter,searchText);		
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildCellTable(ManageMeasureSearchModel
			manageMeasureSearchModel, int filter, String searchText) {
		measureSearchView.getCellTablePanel().clear();
		measureSearchView.buildCellTable(manageMeasureSearchModel,filter,searchText);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#buildMostRecentWidget()
	 */
	@Override
	public void buildMostRecentWidget() {
		mostRecentVerticalPanel.clear();
		mostRecentVerticalPanel.add(mostRecentMeasureWidget.buildMostRecentWidget());
	}
	@Override
	public VerticalPanel getCellTablePanel(){
		return measureSearchView.getCellTablePanel();
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#clearBulkExportCheckBoxes(mat.client.measure.metadata.Grid508)
	 */
	@Override
	public void clearBulkExportCheckBoxes(Grid508 dataTable) {
		int rows = dataTable.getRowCount();
		int cols = dataTable.getColumnCount();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Widget w = dataTable.getWidget(i, j);
				if ((i == 0) && (j == 8)) {
					if (w instanceof FlowPanel) {
						FlowPanel panel = (FlowPanel) w;
						HorizontalPanel hp = (HorizontalPanel) panel
								.getWidget(0);
						int childCount = hp.getWidgetCount();
						for (int childNumber = 0; childNumber < childCount; childNumber++) {
							Widget widget = hp.getWidget(childNumber);
							if (widget instanceof Anchor) {
								widget.getElement().setAttribute("id",
										"clearlink");
								widget.getElement().setAttribute("aria-role",
										"link");
								widget.getElement().setAttribute(
										"aria-labelledby", "LiveRegion");
								widget.getElement().setAttribute("aria-live",
										"assertive");
								widget.getElement().setAttribute("aria-atomic",
										"true");
								widget.getElement().setAttribute(
										"aria-relevant", "all");
								widget.getElement().setAttribute("role",
										"alert");
								widget.getElement().setAttribute("tabIndex",
										"1");
							}
						}
					}
				}
				if (w instanceof HorizontalPanel) {
					HorizontalPanel hPanel = (HorizontalPanel) w;
					int count = hPanel.getWidgetCount();
					for (int k = 0; k < count; k++) {
						Widget widget = hPanel.getWidget(k);
						if (widget instanceof CustomCheckBox) {
							CustomCheckBox checkBox = ((CustomCheckBox) widget);
							checkBox.setValue(false);
						}
					}
				}
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getBulkExportButton()
	 */
	@Override
	public HasClickHandlers getBulkExportButton() {
		return bulkExportButton;
	}	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getErrorMeasureDeletion()
	 */
	@Override
	public MessageAlert getErrorMeasureDeletion() {
		return errorMeasureDeletion;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getErrorMessageDisplayForBulkExport()
	 */
	@Override
	public MessageAlert getErrorMessageDisplayForBulkExport() {
		return errorMessagesForBulkExport;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getExportSelectedButton()
	 */
	@Override
	public Button getExportSelectedButton() {
		return bulkExportButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getForm()
	 */
	@Override
	public FormPanel getForm() {
		return form;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getMeasureDataTable()
	 */
	/**
	 * Add Image on Button with invisible text. This text will be available when
	 * css is turned off.
	 *
	 * @param action - {@link String}
	 * @param url - {@link ImageResource}.
	 * @param key - {@link String}.
	 * @param id the id
	 * @return - {@link Widget}.
	 */
	private Widget getImage(String action, ImageResource url, String key , String id) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		image.getElement().setAttribute("id", id);
		return image;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getPageSelectionTool()
	 */
	/**
	 * Gets the measure search filter widget.
	 * 
	 * @return the dropDown
	 */
	@Override
	public SearchWidgetWithFilter getMeasureSearchFilterWidget() {
		return measureSearchFilterWidget;
	}
	
	/** Gets the most recent measure widget.
	 * 
	 * @return the mostRecentMeasureWidget */
	@Override
	public MostRecentMeasureWidget getMostRecentMeasureWidget() {
		return mostRecentMeasureWidget;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getMeasureSearchView()
	 */
	@Override
	public MeasureSearchView getMeasureSearchView() {
		return measureSearchView;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSearchString()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return measureSearchFilterWidget.getSearchButton();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedFilter()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return measureSearchFilterWidget.getSearchInput();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedFilter()
	 */
	@Override
	public int getSelectedFilter() {
		return measureSearchFilterWidget.getSelectedFilter();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectIdForEditTool()
	 */	
	@Override
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return measureSearchView;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSuccessMeasureDeletion()
	 */
	@Override
	public MessageAlert getSuccessMeasureDeletion() {
		return successMeasureDeletion;
	}
	
	@Override
	/** @return the zoomButton */
	public CustomButton getZoomButton() {
		return zoomButton;
	}
	
	/** Sets the create button.
	 * 
	 * @param createMeasureButton the createMeasureButton to set */
	public void setCreateMeasureButton(Button createMeasureButton) {
		this.createMeasureButton = createMeasureButton;
	}
		
	/**
	 * Sets the error measure deletion.
	 * 
	 * @param errorMeasureDeletion
	 *            the new error measure deletion
	 */
	public void setErrorMeasureDeletion(MessageAlert errorMeasureDeletion) {
		this.errorMeasureDeletion = errorMeasureDeletion;
	}
	
	/** Sets the measure search filter widget.
	 * 
	 * @param measureSearchFilterWidget the measureSearchFilterWidget to set */
	public void setMeasureSearchFilterWidget(SearchWidgetWithFilter measureSearchFilterWidget) {
		this.measureSearchFilterWidget = measureSearchFilterWidget;
	}
	
	/** Sets the success measure deletion.
	 * 
	 * @param successMeasureDeletion the new success measure deletion */
	public void setSuccessMeasureDeletion(
			MessageAlert successMeasureDeletion) {
		this.successMeasureDeletion = successMeasureDeletion;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#clearTransferCheckBoxes()
	 */
	@Override
	public void clearTransferCheckBoxes() {
		
		measureSearchView.clearTransferCheckBoxes();
		
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getClearButton()
	 */
	@Override
	public HasClickHandlers getClearButton() {
		return clearButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getErrorMessagesForTransferOS()
	 */
	@Override
	public MessageAlert getErrorMessagesForTransferOS() {
		return errorMessagesForTransferOS;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getTransferButton()
	 */
	@Override
	public HasClickHandlers getTransferButton() {
		return transferButton;
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public ManageMeasureSearchModel getData() {
		return data;
	}
	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(ManageMeasureSearchModel data) {
		this.data = data;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#setAdminObserver(mat.client.measure.MeasureSearchView.AdminObserver)
	 */
	@Override
	public void setAdminObserver(AdminObserver adminObserver) {
		measureSearchView.setAdminObserver(adminObserver);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getAdminSearchButton()
	 */
	@Override
	public HasClickHandlers getAdminSearchButton() {		
		return searchWidgetBootStrap.getGo();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getAdminSearchString()
	 */
	@Override
	public HasValue<String> getAdminSearchString() {		
		return searchWidgetBootStrap.getSearchBox();
	}

	@Override
	public Button getCreateMeasureButton() {
		return createMeasureButton;
	}
	
	@Override
	public String getSelectedOption() {
		return null;
	}

	/**
	 * @return the draftConfirmationDialogBox
	 */
	public EditConfirmationDialogBox getDraftConfirmationDialogBox() {
		return draftConfirmationDialogBox;
	}

	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return successMessages;
	}

	@Override
	public void resetMessageDisplay() {
		getErrorMessageDisplayForBulkExport().clearAlert();
		getErrorMessageDisplay().clearAlert();
		getErrorMeasureDeletion().clearAlert();
		getSuccessMeasureDeletion().clearAlert();
		getSuccessMessageDisplay().clearAlert();
	}

}
