package mat.client.measure;

import mat.client.ImageResources;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.CreateMeasureWidget;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.MeasureSearchFilterWidget;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageMeasureSearchView.
 */
public class ManageMeasureSearchView implements
ManageMeasurePresenter.SearchDisplay {
	
	/** The bulk export button. */
	private Button bulkExportButton = new PrimaryButton("Export Selected");
	
	/** The create button. */
	/*
	 * private Button createButton = new SecondaryButton("Create");
	 */
	
	CustomButton createMeasureButton = (CustomButton) getImage("Create Measure",
			ImageResources.INSTANCE.createMeasure(), "Create Measure");
	
	/** The create measure widget. */
	private CreateMeasureWidget createMeasureWidget = new CreateMeasureWidget();
	
	/** The current user role. */
	String currentUserRole = MatContext.get().getLoggedInUserRole();
	
	/** The error measure deletion. */
	private ErrorMessageDisplay errorMeasureDeletion = new ErrorMessageDisplay();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The error messages for bulk export. */
	private ErrorMessageDisplay errorMessagesForBulkExport = new ErrorMessageDisplay();
	
	/** The form. */
	final FormPanel form = new FormPanel();
	
	
	
	/** The msfp. */
	/*
	 * private MeasureSearchFilterPanel msfp = new MeasureSearchFilterPanel();
	 */
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The measure search filter widget. */
	private MeasureSearchFilterWidget measureSearchFilterWidget = new MeasureSearchFilterWidget("measureLibrarySearchWidget",
			"measureLibraryFilterDisclosurePanel");
	
	/** The most recent measure widget. */
	private MostRecentMeasureWidget mostRecentMeasureWidget = new MostRecentMeasureWidget();
	
	/** The most recent vertical panel. */
	VerticalPanel mostRecentVerticalPanel = new VerticalPanel();
	
	//MeasureSearchView measureSearchView;
	
	/** The options. */
	/*
	 * private ListBoxMVP options = new ListBoxMVP();
	 */
	/** The search button. */
	private Button searchButton = new PrimaryButton("Search",
			"primaryGreyLeftButton");
	
	/** The search focus holder. */
	/*
	 * private FocusableWidget searchFocusHolder;
	 */
	/** The search input. */
	private TextBox searchInput = new TextBox();
	
	/** The success measure deletion. */
	private SuccessMessageDisplay successMeasureDeletion = new SuccessMessageDisplay();
	
	/** The view. */
	private SearchView<ManageMeasureSearchModel.Result> view  = new SearchView<ManageMeasureSearchModel.Result>();
	
	/** The search view. */
	MeasureSearchView searchView;
	
	/** The view. */
	MeasureSearchView measureSearchView = new MeasureSearchView("Measures");
	
	/** The zoom button. */
	CustomButton zoomButton = (CustomButton) getImage("Search",
			ImageResources.INSTANCE.search_zoom(), "Search");
	/** Instantiates a new manage measure search view. */
	public ManageMeasureSearchView() {
		HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
		mainHorizontalPanel.getElement().setId("panel_MainHorizontalPanel");
		/*
		 * options.getElement().setId("options_ListBoxMVP"); createButton.getElement().setId("createButton_Button");
		 */
		searchInput.getElement().setId("searchInput_TextBox");
		searchButton.getElement().setId("searchButton_Button");
		bulkExportButton.getElement().setId("bulkExportButton_Button");
		mainPanel.getElement().setId("measureLibrary_MainPanel");
		mainPanel.setStyleName("contentPanel");
		VerticalPanel measureFilterVP = new VerticalPanel();
		measureFilterVP.setWidth("100px");
		measureFilterVP.getElement().setId("panel_measureFilterVP");
		measureFilterVP.add(createMeasureWidget);
		measureFilterVP.add(measureSearchFilterWidget);
		buildMostRecentWidget();
		mainHorizontalPanel.add(mostRecentVerticalPanel);
		mainHorizontalPanel.add(measureFilterVP);
		mainPanel.add(mainHorizontalPanel);
		mainPanel.add(successMeasureDeletion);
		mainPanel.add(errorMeasureDeletion);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(errorMessages);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(measureSearchView.asWidget());
		mainPanel
		.add(ManageLoadingView.buildLoadingPanel("loadingPanelExport"));
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildBottomButtonWidget((PrimaryButton) bulkExportButton,
				errorMessagesForBulkExport));
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
	private Widget buildBottomButtonWidget(PrimaryButton bulkExportButton,
			ErrorMessageDisplay errorMessageDisplay) {
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.getElement().setId("measureLibrary_bottomPanel");
		flowPanel.add(errorMessageDisplay);
		flowPanel.setStyleName("rightAlignButton");
		bulkExportButton.setTitle("Bulk Export");
		flowPanel.add(bulkExportButton);
		form.setWidget(flowPanel);
		form.getElement().setId("measureLibrary_bottomPanelForm");
		return form;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(ManageMeasureSearchModel
			manageMeasureSearchModel) {
		measureSearchView.buildCellTable(manageMeasureSearchModel);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#buildMostRecentWidget()
	 */
	@Override
	public void buildMostRecentWidget() {
		mostRecentVerticalPanel.clear();
		mostRecentVerticalPanel.add(mostRecentMeasureWidget.buildMostRecentWidget());
	}
	
	/** Builds the search widget.
	 * 
	 * @param dataTable the data table
	 * @return the widget */
	/*
	 * private Widget buildSearchWidget() { HorizontalPanel hp = new
	 * HorizontalPanel(); hp.getElement().setId("hp_HorizontalPanel");
	 * hp.getElement().setId("measureLibrary_searchWidgetHPanel"); FlowPanel fp1
	 * = new FlowPanel();
	 * fp1.getElement().setId("measureLibrary_searchWidgetFlowPanel");
	 * fp1.add(searchInput); searchButton.setTitle("Search");
	 * fp1.add(searchButton); fp1.add(new SpacerWidget()); hp.add(fp1);
	 * hp.setStylePrimaryName("searchWidgetMeasureLibrary"); return hp; }
	 */
	
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
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#clearSelections()
	 */
	@Override
	public void clearSelections() {
		createMeasureWidget.getOptions().setSelectedIndex(0);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getBulkExportButton()
	 */
	@Override
	public HasClickHandlers getBulkExportButton() {
		return bulkExportButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getCreateButton()
	 */
	@Override
	public HasClickHandlers getCreateButton() {
		return createMeasureWidget.getCreateMeasure();
	}
	
	/** Gets the create button.
	 * 
	 * @return the createMeasureButton */
	@Override
	public CustomButton getCreateMeasureButton() {
		return createMeasureButton;
	}
	
	/** Gets the creates the measure widget.
	 * 
	 * @return the createMeasureWidget */
	@Override
	public CreateMeasureWidget getCreateMeasureWidget() {
		return createMeasureWidget;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getErrorMeasureDeletion()
	 */
	@Override
	public ErrorMessageDisplay getErrorMeasureDeletion() {
		return errorMeasureDeletion;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getErrorMessageDisplayForBulkExport()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplayForBulkExport() {
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
	 * @param action
	 *            - {@link String}
	 * @param url
	 *            - {@link ImageResource}.
	 * @param key
	 *            - {@link String}.
	 * @return - {@link Widget}.
	 */
	private Widget getImage(String action, ImageResource url, String key) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		image.getElement().setAttribute("id", "MeasureSearchButton");
		return image;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getMeasureDataTable()
	 */
	@Override
	public Grid508 getMeasureDataTable() {
		return view.getDataTable();
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
	public MeasureSearchFilterWidget getMeasureSearchFilterWidget() {
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
	//
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getPageSize()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getPageSizeSelectionTool()
	 */
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}
	
	/*@Override
	public MeasureSearchFilterPanel getMeasureSearchFilterPanel() {
		return msfp;
	}*/
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSearchButton()
	 */
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
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
	/**
	 * (non-Javadoc).
	 * 
	 * @return the selected option
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedOption
	 */
	@Override
	public String getSelectedOption() {
		return createMeasureWidget.getOptions().getItemText(createMeasureWidget.getOptions().getSelectedIndex());
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSuccessMeasureDeletion()
	 */
	@Override
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return measureSearchView;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSuccessMeasureDeletion()
	 */
	@Override
	public SuccessMessageDisplay getSuccessMeasureDeletion() {
		return successMeasureDeletion;
	}
	
	
	/** Load list box options.
	 * 
	 * @return the zoom button */
	/*
	 * private void loadListBoxOptions() { options.addItem(ConstantMessages.DEFAULT_SELECT);
	 * options.addItem(ConstantMessages.CREATE_NEW_MEASURE); options.addItem(ConstantMessages.CREATE_NEW_VERSION);
	 * options.addItem(ConstantMessages.CREATE_NEW_DRAFT); }
	 */
	
	@Override
	/** @return the zoomButton */
	public CustomButton getZoomButton() {
		return zoomButton;
	}
	
	/** Sets the create button.
	 * 
	 * @param createMeasureButton the createMeasureButton to set */
	public void setCreateMeasureButton(CustomButton createMeasureButton) {
		this.createMeasureButton = createMeasureButton;
	}
	
	/** Sets the creates the measure widget.
	 * 
	 * @param createMeasureWidget the createMeasureWidget to set */
	public void setCreateMeasureWidget(CreateMeasureWidget createMeasureWidget) {
		this.createMeasureWidget = createMeasureWidget;
	}
	
	/**
	 * Sets the error measure deletion.
	 * 
	 * @param errorMeasureDeletion
	 *            the new error measure deletion
	 */
	public void setErrorMeasureDeletion(ErrorMessageDisplay errorMeasureDeletion) {
		this.errorMeasureDeletion = errorMeasureDeletion;
	}
	
	/** Sets the measure search filter widget.
	 * 
	 * @param measureSearchFilterWidget the measureSearchFilterWidget to set */
	public void setMeasureSearchFilterWidget(MeasureSearchFilterWidget measureSearchFilterWidget) {
		this.measureSearchFilterWidget = measureSearchFilterWidget;
	}
	
	/** Sets the success measure deletion.
	 * 
	 * @param successMeasureDeletion the new success measure deletion */
	public void setSuccessMeasureDeletion(
			SuccessMessageDisplay successMeasureDeletion) {
		this.successMeasureDeletion = successMeasureDeletion;
	}
	
	
	
}
