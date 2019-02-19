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
import mat.client.advancedsearch.AdvancedSearchPillPanel;
import mat.client.buttons.CustomButton;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.measure.MeasureSearchView.AdminObserver;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.util.ClientConstants;
import mat.shared.MeasureSearchModel;

public class ManageMeasureSearchView implements SearchDisplay {

	private final Button bulkExportButton = new Button("Export Selected");	

	Button createMeasureButton = new Button("New Measure"); 
	
	Button createCompositeMeasureButton = new Button("New Composite Measure");

	CellTable<ManageMeasureSearchModel.Result> table;
	
	private final MessageAlert errorMessagesForTransferOS = new ErrorMessageAlert();
	
	String currentUserRole = MatContext.get().getLoggedInUserRole();
	
	private MessageAlert errorMeasureDeletion = new ErrorMessageAlert();
	
	private final Button clearButton = new Button("Clear All");
	
	private final MessageAlert errorMessages = new ErrorMessageAlert();
	
	private final MessageAlert errorMessagesForBulkExport = new ErrorMessageAlert();
	
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	
	private final FormPanel form = new FormPanel();
	
	public VerticalPanel measureVpanel = new VerticalPanel();

	private final FlowPanel mainPanel = new FlowPanel();
	
	private SearchWidgetWithFilter measureSearchFilterWidget = new SearchWidgetWithFilter("searchFilter", "measureLibraryFilterDisclosurePanel","forMeasure");
	
	SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search");
	
	private MostRecentMeasureWidget mostRecentMeasureWidget = new MostRecentMeasureWidget();
	
	VerticalPanel mostRecentVerticalPanel = new VerticalPanel();
	
	private MessageAlert successMeasureDeletion = new SuccessMessageAlert();
	
	private final MessageAlert successMessages = new SuccessMessageAlert();
	
	EditConfirmationDialogBox draftConfirmationDialogBox = new EditConfirmationDialogBox();
	
	private final Button transferButton = new Button("Transfer");
	
	MeasureSearchView searchView;
	
	AdvancedSearchPillPanel pillPanel = new AdvancedSearchPillPanel("Measure");
	
	MeasureSearchView measureSearchView = new MeasureSearchView("Measures");
	
	CustomButton zoomButton = (CustomButton) getImage("Search", ImageResources.INSTANCE.search_zoom(), "Search" , "MeasureSearchButton");
	
	public ManageMeasureSearchView() {
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get()
				.getLoggedInUserRole())){
			mainPanel.add(new SpacerWidget());
			mainPanel.add(buildSearchWidget());
			mainPanel.add(new SpacerWidget());
			mainPanel.add(successMessages);
			mainPanel.add(new SpacerWidget());
			mainPanel.add(measureSearchView.asWidget());
			mainPanel.setStyleName("contentPanel");
			mainPanel.add(new SpacerWidget());			
			mainPanel.add(adminBuildBottomButtonWidget( transferButton, clearButton,
					errorMessagesForTransferOS));
		}else{
		mainPanel.add(buildBottomButtonWidget(bulkExportButton,
				errorMessagesForBulkExport));
		
		bulkExportButton.getElement().setId("bulkExportButton_Button");
		mainPanel.getElement().setId("measureLibrary_MainPanel");
		mainPanel.setStyleName("contentPanel");
		mostRecentMeasureWidget = new MostRecentMeasureWidget();
		buildMostRecentWidget();
		mainPanel.add(mostRecentVerticalPanel);
		mainPanel.add(successMeasureDeletion);
		mainPanel.add(errorMeasureDeletion);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessages);
		mainPanel.add(errorMessages);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(measureSearchFilterWidget);
		mainPanel.add(pillPanel.getBadgeTable());
		mainPanel.add(measureSearchView.asWidget());
		mainPanel.add(ManageLoadingView.buildLoadingPanel("loadingPanelExport"));
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildBottomButtonWidget( bulkExportButton,
				errorMessagesForBulkExport));
		}
		MatContext.get().setManageMeasureSearchView(this);
		
	}
	
	

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	private Widget buildBottomButtonWidget(Button bulkExportButton,
			MessageAlert errorMessageDisplay) {
		final FlowPanel flowPanel = new FlowPanel();
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

	public Widget adminBuildBottomButtonWidget(Button transferButton, Button clearButton,
			MessageAlert errorMessageDisplay) {
		final FlowPanel flowPanel = new FlowPanel();
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

	public Widget buildSearchWidget() {
		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(searchWidgetBootStrap.getSearchWidget());

		return hp;
	}

	@Override
	public void buildDataTable(ManageMeasureSearchModel
			manageMeasureSearchModel, int filter, String searchText){
		final MeasureSearchModel model = new MeasureSearchModel();
		model.setSearchTerm(searchText);
		measureSearchView.buildCellTable(manageMeasureSearchModel,filter,model);
		
	}

	@Override
	public void buildCellTable(ManageMeasureSearchModel
			manageMeasureSearchModel, int filter, MeasureSearchModel model) {
		measureSearchView.getCellTablePanel().clear();
		measureSearchView.buildCellTable(manageMeasureSearchModel,filter, model);
	}
	
	@Override
	public void buildMostRecentWidget() {
		mostRecentVerticalPanel.clear();
		mostRecentVerticalPanel.add(mostRecentMeasureWidget.buildMostRecentWidget());
		
	}
	@Override
	public VerticalPanel getCellTablePanel(){
		return measureSearchView.getCellTablePanel();
	}

	@Override
	public void clearBulkExportCheckBoxes(Grid508 dataTable) {
		final int rows = dataTable.getRowCount();
		final int cols = dataTable.getColumnCount();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				final Widget w = dataTable.getWidget(i, j);
				if ((i == 0) && (j == 8)) {
					if (w instanceof FlowPanel) {
						final FlowPanel panel = (FlowPanel) w;
						final HorizontalPanel hp = (HorizontalPanel) panel
								.getWidget(0);
						final int childCount = hp.getWidgetCount();
						for (int childNumber = 0; childNumber < childCount; childNumber++) {
							final Widget widget = hp.getWidget(childNumber);
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
					final HorizontalPanel hPanel = (HorizontalPanel) w;
					final int count = hPanel.getWidgetCount();
					for (int k = 0; k < count; k++) {
						final Widget widget = hPanel.getWidget(k);
						if (widget instanceof CustomCheckBox) {
							final CustomCheckBox checkBox = ((CustomCheckBox) widget);
							checkBox.setValue(false);
						}
					}
				}
			}
		}
		
	}

	@Override
	public HasClickHandlers getBulkExportButton() {
		return bulkExportButton;
	}	

	@Override
	public MessageAlert getErrorMeasureDeletion() {
		return errorMeasureDeletion;
	}
	
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public MessageAlert getErrorMessageDisplayForBulkExport() {
		return errorMessagesForBulkExport;
	}

	@Override
	public Button getExportSelectedButton() {
		return bulkExportButton;
	}

	@Override
	public FormPanel getForm() {
		return form;
	}

	private Widget getImage(String action, ImageResource url, String key , String id) {
		final CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		image.getElement().setAttribute("id", id);
		return image;
	}

	@Override
	public SearchWidgetWithFilter getMeasureSearchFilterWidget() {
		return measureSearchFilterWidget;
	}

	@Override
	public MostRecentMeasureWidget getMostRecentMeasureWidget() {
		return mostRecentMeasureWidget;
	}
	
	@Override
	public VerticalPanel getMostRecentMeasureVerticalPanel() {
		return mostRecentVerticalPanel;
	}

	@Override
	public MeasureSearchView getMeasureSearchView() {
		return measureSearchView;
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
	public int getSelectedFilter() {
		return measureSearchFilterWidget.getSelectedFilter();
		
	}

	@Override
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return measureSearchView;
	}

	@Override
	public MessageAlert getSuccessMeasureDeletion() {
		return successMeasureDeletion;
	}
	
	@Override
	public CustomButton getZoomButton() {
		return zoomButton;
	}
	
	public void setCreateMeasureButton(Button createMeasureButton) {
		this.createMeasureButton = createMeasureButton;
	}


	@Override
	public Button getCreateCompositeMeasureButton() {
		return createCompositeMeasureButton;
	}

	public void setCreateCompositeMeasureButton(Button createCompositeMeasureButton) {
		this.createCompositeMeasureButton = createCompositeMeasureButton;
	}
	
	public void setErrorMeasureDeletion(MessageAlert errorMeasureDeletion) {
		this.errorMeasureDeletion = errorMeasureDeletion;
	}

	public void setMeasureSearchFilterWidget(SearchWidgetWithFilter measureSearchFilterWidget) {
		this.measureSearchFilterWidget = measureSearchFilterWidget;
	}

	public void setSuccessMeasureDeletion(
			MessageAlert successMeasureDeletion) {
		this.successMeasureDeletion = successMeasureDeletion;
	}

	@Override
	public void clearTransferCheckBoxes() {
		
		measureSearchView.clearTransferCheckBoxes();
		
	}

	@Override
	public HasClickHandlers getClearButton() {
		return clearButton;
	}

	@Override
	public MessageAlert getErrorMessagesForTransferOS() {
		return errorMessagesForTransferOS;
	}

	@Override
	public HasClickHandlers getTransferButton() {
		return transferButton;
	}

	public ManageMeasureSearchModel getData() {
		return data;
	}

	public void setData(ManageMeasureSearchModel data) {
		this.data = data;
	}

	@Override
	public void setAdminObserver(AdminObserver adminObserver) {
		measureSearchView.setAdminObserver(adminObserver);
	}

	@Override
	public HasClickHandlers getAdminSearchButton() {		
		return searchWidgetBootStrap.getGo();
	}

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

	@Override
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

	@Override
	public CustomCheckBox getCustomFilterCheckBox() {
		return measureSearchFilterWidget.getMeasureCustomCheckBox();
	}
	
	@Override
	public AdvancedSearchPillPanel getSearchPillPanel() {
		return pillPanel;
	}
	
	@Override
	public void resetDisplay() {
		this.getErrorMessageDisplay().clearAlert();
		this.measureSearchFilterWidget.getSearchInput().setValue("");
		this.measureSearchFilterWidget.getMeasureCustomCheckBox().setValue(true);
		this.measureSearchFilterWidget.getAdvancedSearchPanel().resetDisplay(true);
		this.measureSearchFilterWidget.getSearchInput().getElement().focus();
		this.measureSearchFilterWidget.setSelectedFilter(SearchWidgetWithFilter.MY);
	}
}