package mat.client.measure;

import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MeasureSearchFilterWidget;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManageMeasureSearchView implements
ManageMeasurePresenter.SearchDisplay {

	private FlowPanel mainPanel = new FlowPanel();
	private Button searchButton = new PrimaryButton("Search",
			"primaryGreyLeftButton");
	private TextBox searchInput = new TextBox();
	private FocusableWidget searchFocusHolder;
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private MeasureSearchFilterWidget measureSearchFilterWidget = new MeasureSearchFilterWidget();
	
	SearchView<ManageMeasureSearchModel.Result> view = new MeasureSearchView(
			"Measures");
	private Button createButton = new SecondaryButton("Create");
	private ListBoxMVP options = new ListBoxMVP();
	private MeasureSearchFilterPanel msfp = new MeasureSearchFilterPanel();
	private Button bulkExportButton = new PrimaryButton("Export Selected");
	final FormPanel form = new FormPanel();

	private ErrorMessageDisplay errorMessagesForBulkExport = new ErrorMessageDisplay();
	private ErrorMessageDisplay errorMeasureDeletion = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMeasureDeletion = new SuccessMessageDisplay();
	String currentUserRole = MatContext.get().getLoggedInUserRole();

	public ManageMeasureSearchView() {
		HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
		mainHorizontalPanel.getElement().setId("panel_MainHorizontalPanel");
		options.getElement().setId("options_ListBoxMVP");
		createButton.getElement().setId("createButton_Button");
		searchInput.getElement().setId("searchInput_TextBox");
		searchButton.getElement().setId("searchButton_Button");
		bulkExportButton.getElement().setId("bulkExportButton_Button");
		mainPanel.getElement().setId("measureLibrary_MainPanel");
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(errorMessages);
		mainPanel.add(new SpacerWidget());
		loadListBoxOptions();

		VerticalPanel createVP = new VerticalPanel();
		createVP.getElement().setId("panel_createVP");
		createVP.add(LabelBuilder.buildLabel("Create", "Create Measure"));
		createVP.add(new SpacerWidget());
		createVP.add(options);
		options.setName("Create");
		DOM.setElementAttribute(options.getElement(), "id", "Create Measure");
		/*createVP.setStylePrimaryName("createMeasurePanelMeasureLib");*/
		HorizontalPanel createHP = new HorizontalPanel();
		createHP.getElement().setId("panel_createHP");
		createHP.add(createVP);
		VerticalPanel createButtonVP = new VerticalPanel();
		createButtonVP.getElement().setId("panel_createButtonVP");
		createButton.setTitle("Create");
		createButtonVP.add(createButton);
		createButtonVP.setStylePrimaryName("searchWidgetMeasureLibrary");
		//createHP.add(dropDown);
		createHP.add(createButtonVP);
		VerticalPanel measureFilterVP = new VerticalPanel();
		measureFilterVP.getElement().setId("panel_measureFilterVP");
		measureFilterVP.add(new SpacerWidget());
		/*measureFilterVP.add(msfp.getPanel());*/
		measureFilterVP.add(measureSearchFilterWidget);
		/*mainHorizontalPanel.add(new SpacerWidget());
		mainHorizontalPanel.add(buildSearchWidget());*/
		mainHorizontalPanel.add(createHP);
		mainHorizontalPanel.add(measureFilterVP);
		mainPanel.add(mainHorizontalPanel);

		mainPanel.add(successMeasureDeletion);
		mainPanel.add(errorMeasureDeletion);
		mainPanel.add(new SpacerWidget());
		/*mainPanel.add(dropDown);*/
		mainPanel.add(new SpacerWidget());
		mainPanel.add(view.asWidget());
		mainPanel
		.add(ManageLoadingView.buildLoadingPanel("loadingPanelExport"));
		mainPanel.add(buildBottomButtonWidget((PrimaryButton) bulkExportButton,
				errorMessagesForBulkExport));
		MatContext.get().setManageMeasureSearchView(this);

	}

	private Widget buildSearchWidget() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		hp.getElement().setId("measureLibrary_searchWidgetHPanel");
		FlowPanel fp1 = new FlowPanel();
		fp1.getElement().setId("measureLibrary_searchWidgetFlowPanel");
		fp1.add(searchInput);
		searchButton.setTitle("Search");
		fp1.add(searchButton);
		fp1.add(new SpacerWidget());
		hp.add(fp1);
		hp.setStylePrimaryName("searchWidgetMeasureLibrary");
		return hp;
	}

	private Widget buildBottomButtonWidget(PrimaryButton button,
			ErrorMessageDisplay errorMessageDisplay) {
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.getElement().setId("measureLibrary_bottomPanel");
		flowPanel.add(errorMessageDisplay);
		flowPanel.setStyleName("rightAlignButton");
		flowPanel.add(button);
		form.setWidget(flowPanel);
		form.getElement().setId("measureLibrary_bottomPanelForm");
		return form;
	}

	@Override
	public HasClickHandlers getSearchButton() {
		return measureSearchFilterWidget.getSearchButton();
	}

	@Override
	public HasValue<String> getSearchString() {
		return measureSearchFilterWidget.getSearchInput();
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public HasClickHandlers getCreateButton() {
		return createButton;
	}

	@Override
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return view;
	}

	@Override
	public void buildDataTable(
			SearchResults<ManageMeasureSearchModel.Result> results) {
		view.buildDataTable(results);
	}

	@Override
	public int getPageSize() {
		return view.getPageSize();
	}

	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}

	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplayForBulkExport() {
		return errorMessagesForBulkExport;
	}

	@Override
	public HasClickHandlers getBulkExportButton() {
		return bulkExportButton;
	}

	@Override
	public FormPanel getForm() {
		return form;
	}

	private void loadListBoxOptions() {
		options.addItem(ConstantMessages.DEFAULT_SELECT);
		options.addItem(ConstantMessages.CREATE_NEW_MEASURE);
		options.addItem(ConstantMessages.CREATE_NEW_VERSION);
		options.addItem(ConstantMessages.CREATE_NEW_DRAFT);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedOption
	 * 
	 */
	@Override
	public String getSelectedOption() {
		return options.getItemText(options.getSelectedIndex());
	}

	@Override
	public void clearSelections() {
		options.setSelectedIndex(0);
	}

	@Override
	public Grid508 getMeasureDataTable() {
		return view.getDataTable();
	}

	@Override
	public Button getExportSelectedButton() {
		return bulkExportButton;
	}

	@Override
	public void clearBulkExportCheckBoxes(Grid508 dataTable) {
		int rows = dataTable.getRowCount();
		int cols = dataTable.getColumnCount();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Widget w = dataTable.getWidget(i, j);
				if (i == 0 && j == 8) {
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

	/*@Override
	public MeasureSearchFilterPanel getMeasureSearchFilterPanel() {
		return msfp;
	}*/

	public ErrorMessageDisplay getErrorMeasureDeletion() {
		return errorMeasureDeletion;
	}

	public void setErrorMeasureDeletion(ErrorMessageDisplay errorMeasureDeletion) {
		this.errorMeasureDeletion = errorMeasureDeletion;
	}

	public SuccessMessageDisplay getSuccessMeasureDeletion() {
		return successMeasureDeletion;
	}

	public void setSuccessMeasureDeletion(
			SuccessMessageDisplay successMeasureDeletion) {
		this.successMeasureDeletion = successMeasureDeletion;
	}
	
	@Override
	public int getSelectedFilter() {
		return measureSearchFilterWidget.getSelectedFilter();
	}

	/**
	 * @return the dropDown
	 */
	
	public MeasureSearchFilterWidget getMeasureSearchFilterWidget() {
		return measureSearchFilterWidget;
	}

	
	/**
	 * @param measureSearchFilterWidget the measureSearchFilterWidget to set
	 */
	public void setMeasureSearchFilterWidget(MeasureSearchFilterWidget measureSearchFilterWidget) {
		this.measureSearchFilterWidget = measureSearchFilterWidget;
	}
	

}
