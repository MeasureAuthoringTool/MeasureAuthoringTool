package mat.client.measure;

import java.awt.List;

import mat.client.CustomPager;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.search.SearchView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class AdminManageMeasureSearchView implements ManageMeasurePresenter.AdminSearchDisplay {
	private FlowPanel mainPanel = new FlowPanel();
	final FormPanel form = new FormPanel();
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	private Button transferButton = new PrimaryButton("Transfer","primaryGreyButton");
	private Button clearButton = new PrimaryButton("Clear","primaryGreyLeftButton");
	private TextBox searchInput = new TextBox();
	ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
	private SearchView<ManageMeasureSearchModel.Result> view = new SearchView<ManageMeasureSearchModel.Result>(true);
	private MeasureSearchFilterPanel msfp = new MeasureSearchFilterPanel();
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	private ErrorMessageDisplay errorMessagesForTransferOS = new ErrorMessageDisplay();
	private ErrorMessageDisplay errorMessagesForBulkExport = new ErrorMessageDisplay();
	private ErrorMessageDisplay errorMeasureDeletion = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMeasureDeletion = new SuccessMessageDisplay();
	public CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
		
	public AdminManageMeasureSearchView() {
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildSearchWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(view.asWidget());
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildBottomButtonWidget((PrimaryButton) transferButton, (PrimaryButton) clearButton,errorMessagesForTransferOS));
		MatContext.get().setAdminManageMeasureSearchView(this);
	}

	public CellTable<ManageMeasureSearchModel.Result> getCellTable() {
		return cellTable;
	}


	public void setCellTable(CellTable<ManageMeasureSearchModel.Result> cellTable) {
		this.cellTable = cellTable;
	}


	private Widget buildSearchWidget(){
		HorizontalPanel hp = new HorizontalPanel();
		FlowPanel fp1 = new FlowPanel();
		fp1.add(searchInput);
		searchButton.setTitle("Search");
		fp1.add(searchButton);
		fp1.add(new SpacerWidget());
		hp.add(fp1);
		return hp;
	}
	
	private Widget buildBottomButtonWidget(PrimaryButton transferButton,PrimaryButton clearButton ,ErrorMessageDisplay errorMessageDisplay){
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(errorMessageDisplay);
		//flowPanel.setStyleName("rightAlignButton");
		transferButton.setTitle("Transfer");
		clearButton.setTitle("Clear");
		flowPanel.add(transferButton);
		flowPanel.add(clearButton);
		form.setWidget(flowPanel);
		return form;
	}
	

	@Override
	public void buildMeasureDataTable(AdminMeasureSearchResultAdaptor results){
		buildDataTable(results);
	}
	
	public void clearTransferCheckBoxes(){
		for(ManageMeasureSearchModel.Result result : sortProvider.getList()){
				result.setTransferable(false);
		}
		AdminMeasureSearchResultAdaptor adapter = new AdminMeasureSearchResultAdaptor();
		adapter.getData().setData(sortProvider.getList());
		buildDataTable(adapter);
		
	}
	
	private void buildDataTable(AdminMeasureSearchResultAdaptor results){
		if(results == null) {
			return;
		}
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
		
		
		// Display 50 rows on a page 
		cellTable.setPageSize(50);
		cellTable = results.addColumnToTable(cellTable);
		
		cellTable.redraw();
		sortProvider.refresh();
		sortProvider.setList(results.getData().getData());
	
		sortProvider.addDataDisplay(cellTable);
		//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
		MatSimplePager spager;
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
	    spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
        spager.setDisplay(cellTable);
        spager.setPageStart(0);
        spager.setPageSize(50);
        spager.setToolTipAndTabIndex(spager);
        view.getvPanelForQDMTable().clear();
        view.getvPanelForQDMTable().add(cellTable);
		view.getvPanelForQDMTable().add(new SpacerWidget());
		view.getvPanelForQDMTable().add(spager);
			
	
	}

	public HasClickHandlers getSearchButton() {
		return searchButton;
	}

	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}

	
	@Override
	public ErrorMessageDisplayInterface getErrorMessagesForTransferOS() {
		return errorMessagesForTransferOS;
	}


	@Override
	public HasClickHandlers getTransferButton() {
		// TODO Auto-generated method stub
		return transferButton;
	}
	
	
	public HasClickHandlers getClearButton() {
		return clearButton;
	}

	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}

	@Override
	public MeasureSearchFilterPanel getMeasureSearchFilterPanel() {
		return msfp;
	}

	@Override
	public SuccessMessageDisplay getSuccessMeasureDeletion() {
		return successMeasureDeletion;
	}

	@Override
	public ErrorMessageDisplay getErrorMeasureDeletion() {
		return errorMeasureDeletion;
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public HasSelectionHandlers<Result> getSelectedMeasureOption() {
		return null;
	}

	
	@Override
	public FocusableWidget getMsgFocusWidget() {
		return null;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}


	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
	
	}
	
}
