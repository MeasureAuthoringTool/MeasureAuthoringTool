package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.SearchView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**
 * The Class AdminManageMeasureSearchView.
 */
public class AdminManageMeasureSearchView implements ManageMeasurePresenter.AdminSearchDisplay {
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The form. */
	final FormPanel form = new FormPanel();
	
	/** The search button. */
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	
	/** The transfer button. */
	private Button transferButton = new PrimaryButton("Transfer","primaryGreyButton");
	
	/** The clear button. */
	private Button clearButton = new PrimaryButton("Clear All","primaryGreyLeftButton");
	
	/** The search input. */
	private TextBox searchInput = new TextBox();
	
	/** The selected measure list. */
	private List<Result> selectedMeasureList;
	
	/** The view. */
	private SearchView<ManageMeasureSearchModel.Result> view = new SearchView<ManageMeasureSearchModel.Result>(true);
	
	/** The error message panel. */
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	
	/** The error messages for transfer os. */
	private ErrorMessageDisplay errorMessagesForTransferOS = new ErrorMessageDisplay();
	
	
	//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
	/** The spager. */
	MatSimplePager spager;
	
	/**
	 * Instantiates a new admin manage measure search view.
	 */
	public AdminManageMeasureSearchView() {
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);	
		 spager.setPageStart(0);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildSearchWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(view.asWidget());
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildBottomButtonWidget((PrimaryButton) transferButton, (PrimaryButton) clearButton,errorMessagesForTransferOS));
		MatContext.get().setAdminManageMeasureSearchView(this);
	}

	
	/**
	 * Builds the search widget.
	 * 
	 * @return the widget
	 */
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
	
	/**
	 * Builds the bottom button widget.
	 * 
	 * @param transferButton
	 *            the transfer button
	 * @param clearButton
	 *            the clear button
	 * @param errorMessageDisplay
	 *            the error message display
	 * @return the widget
	 */
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
	

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#buildDataTable(mat.client.measure.AdminMeasureSearchResultAdaptor)
	 */
	@Override
	public void buildDataTable(AdminMeasureSearchResultAdaptor results){
		buildMeasureDataTable(results);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#clearTransferCheckBoxes()
	 */
	public void clearTransferCheckBoxes(){
		for(ManageMeasureSearchModel.Result result : selectedMeasureList){
				result.setTransferable(false);
		}
		AdminMeasureSearchResultAdaptor adapter = new AdminMeasureSearchResultAdaptor();
		adapter.getData().setData(selectedMeasureList);
		buildDataTable(adapter);
		
	}
	
	/**
	 * Builds the measure data table.
	 * 
	 * @param results
	 *            the results
	 */
	private void buildMeasureDataTable(AdminMeasureSearchResultAdaptor results){
		if(results == null) {
			return;
		}
		errorMessagesForTransferOS.clear();
		CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
		/*cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);*/
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
		
		selectedMeasureList = new ArrayList<Result>(); 
		selectedMeasureList.addAll(results.getData().getData());	
		// Display 50 rows on a page 
		cellTable.setPageSize(50);
		cellTable.redraw();
		cellTable.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(results.getData().getData());
		ListHandler<ManageMeasureSearchModel.Result> sortHandler = new ListHandler<ManageMeasureSearchModel.Result>(sortProvider.getList());
		cellTable.addColumnSortHandler(sortHandler);
		cellTable = results.addColumnToTable(cellTable,sortHandler);
		sortProvider.addDataDisplay(cellTable);
		spager.setDisplay(cellTable);
        spager.setPageSize(50);
        spager.setToolTipAndTabIndex(spager);
        view.getvPanelForQDMTable().clear();
        view.getvPanelForQDMTable().add(cellTable);
		view.getvPanelForQDMTable().add(new SpacerWidget());
		view.getvPanelForQDMTable().add(spager);
			
	
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getSearchButton()
	 */
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getErrorMessagesForTransferOS()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessagesForTransferOS() {
		return errorMessagesForTransferOS;
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getTransferButton()
	 */
	@Override
	public HasClickHandlers getTransferButton() {
		return transferButton;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getClearButton()
	 */
	public HasClickHandlers getClearButton() {
		return clearButton;
	}

	/**
	 * Sets the clear button.
	 * 
	 * @param clearButton
	 *            the new clear button
	 */
	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}


	
}
