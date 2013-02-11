package mat.client.measure;

import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.client.util.ClientConstants;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class ManageMeasureSearchView implements ManageMeasurePresenter.SearchDisplay {

	private FlowPanel mainPanel = new FlowPanel();
	private Button searchButton = new PrimaryButton("Search");
	private TextBox searchInput = new TextBox();
	private FocusableWidget searchFocusHolder ;
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();

	SearchView<ManageMeasureSearchModel.Result> view = new MeasureSearchView("Measures");
	private Button createButton = new SecondaryButton("Create");
	private ListBoxMVP options = new ListBoxMVP();
	private MeasureSearchFilterPanel msfp = new MeasureSearchFilterPanel();
	private Button bulkExportButton = new PrimaryButton("Export Selected");
	private Button transferButton = new PrimaryButton("Transfer");
	final FormPanel form = new FormPanel();
	
	private ErrorMessageDisplay errorMessagesForBulkExport = new ErrorMessageDisplay();
	private ErrorMessageDisplay errorMessagesForTransferOS = new ErrorMessageDisplay();
	String currentUserRole = MatContext.get().getLoggedInUserRole();
	
	
	public ManageMeasureSearchView() {
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(errorMessages);
		mainPanel.add(new SpacerWidget());
		if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			loadListBoxOptions();
			mainPanel.add(new Label("Create:"));
			mainPanel.add(options);
			options.setName("Create:");
			mainPanel.add(createButton);
			createButton.setTitle("Create");
			mainPanel.add(new SpacerWidget());
			Label searchMeasureText =new Label("Search For Measure");
			mainPanel.add(searchMeasureText);
			mainPanel.add(msfp.getPanel());
			mainPanel.add(new SpacerWidget());
			Widget searchText = LabelBuilder.buildLabel(searchInput, "Search for a Measure");
			searchFocusHolder = new FocusableWidget(searchText);
			mainPanel.add(searchFocusHolder);
			mainPanel.add(buildSearchWidget());
			mainPanel.add(new SpacerWidget());
			mainPanel.add(view.asWidget());
			mainPanel.add(ManageLoadingView.buildLoadingPanel("loadingPanelExport"));
			mainPanel.add(buildBottomButtonWidget((PrimaryButton) bulkExportButton,errorMessagesForBulkExport));
		}else{
			mainPanel.add(new SpacerWidget());
			Widget searchText = LabelBuilder.buildLabel(searchInput, "Search for a Measure");
			searchFocusHolder = new FocusableWidget(searchText);
			mainPanel.add(searchFocusHolder);
			mainPanel.add(buildSearchWidget());
			mainPanel.add(new SpacerWidget());
			mainPanel.add(view.asWidget());
			mainPanel.add(buildBottomButtonWidget((PrimaryButton) transferButton,errorMessagesForTransferOS));
		}
		
		MatContext.get().setManageMeasureSearchView(this);
		
	}
	
	
	private Widget buildSearchWidget(){
		HorizontalPanel hp = new HorizontalPanel();
		SimplePanel sp1 = new SimplePanel();
		sp1.addStyleName("codeListLink");
		sp1.add(searchInput);
		SimplePanel sp2 = new SimplePanel();
		sp2.add(searchButton);
		searchButton.setTitle("Search");
		hp.add(sp1);
		hp.add(sp2);
		return hp;
	}
	
	private Widget buildBottomButtonWidget(PrimaryButton button, ErrorMessageDisplay errorMessageDisplay){
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(errorMessageDisplay);
		flowPanel.setStyleName("rightAlignButton");
		flowPanel.add(button);
		form.setWidget(flowPanel);
		return form;
	}
	
	
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	
	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
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
	public void buildDataTable(SearchResults<ManageMeasureSearchModel.Result> results) {
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
	
	private void loadListBoxOptions(){
		options.addItem(ConstantMessages.DEFAULT_SELECT);
		options.addItem(ConstantMessages.CREATE_NEW_MEASURE);
		options.addItem(ConstantMessages.CREATE_NEW_VERSION);
		options.addItem(ConstantMessages.CREATE_NEW_DRAFT);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedOption()
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
	public Button getExportSelectedButton(){
		return bulkExportButton;
	}


	@Override
	public void clearBulkExportCheckBoxes(Grid508 dataTable){
		int rows = dataTable.getRowCount();
		int cols = dataTable.getColumnCount();
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				Widget w = dataTable.getWidget(i, j);
				if(w instanceof HorizontalPanel){
					HorizontalPanel hPanel = (HorizontalPanel)w;
					int count = hPanel.getWidgetCount();
					for (int k = 0; k < count; k++) {
						Widget widget = hPanel.getWidget(k);
						if(widget instanceof CustomCheckBox){
							CustomCheckBox checkBox = ((CustomCheckBox)widget);
								checkBox.setValue(false);										
						}
					}
				}
			}
		}
	}

	@Override
	public MeasureSearchFilterPanel getMeasureSearchFilterPanel() {
		return msfp;
	}
	
	@Override
	public HasClickHandlers getTransferButton() {
		return transferButton;
	}


	/**
	 * @param errorMessagesForTransferOS the errorMessagesForTransferOS to set
	 */
	public void setErrorMessagesForTransferOS(ErrorMessageDisplay errorMessagesForTransferOS) {
		this.errorMessagesForTransferOS = errorMessagesForTransferOS;
	}


	@Override
	public ErrorMessageDisplay getErrorMessagesForTransferOS() {
		return errorMessagesForTransferOS;
	}
	
}
