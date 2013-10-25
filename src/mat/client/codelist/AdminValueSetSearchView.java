package mat.client.codelist;


import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.CodeListSearchDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;


/**
 * The Class AdminValueSetSearchView.
 */
public class AdminValueSetSearchView implements ManageCodeListSearchPresenter.AdminValueSetSearchDisplay {
	
	/** The search criteria panel. */
	private FlowPanel searchCriteriaPanel = new FlowPanel();
	
	/** The form. */
	final FormPanel form = new FormPanel();
	
	/** The view. */
	private SearchView<CodeListSearchDTO> view = new SearchView<CodeListSearchDTO>(true);
	
	/** The adapter. */
	private AdminCodeListSearchResultsAdapter adapter;
	
	/** The search button. */
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	
	/** The search input. */
	private TextBox searchInput = new TextBox();
	
	/** The transfer button. */
	Button transferButton = new PrimaryButton("Transfer","primaryGreyButton");
	
	/** The clear button. */
	private Button clearButton = new PrimaryButton("Clear All","primaryGreyLeftButton");
	
	/** The error messages. */
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The transfer error messages. */
	protected ErrorMessageDisplay transferErrorMessages = new ErrorMessageDisplay();
	
	/** The vssfp. */
	private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
	
	/** The code list results. */
	public List<CodeListSearchDTO> codeListResults;
	//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
	/** The spager. */
	MatSimplePager spager;
	
	
	/**
	 * Instantiates a new admin value set search view.
	 */
	AdminValueSetSearchView (){
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
	    spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
	    spager.setPageStart(0);
	    
	    view.buildDataTable(new AdminManageCodeListSearchModel());
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(buildSearchWidget());
		searchCriteriaPanel.add(view.asWidget());
		searchCriteriaPanel.setStyleName("contentPanel");
		searchCriteriaPanel.add(errorMessages);
		searchCriteriaPanel.add(transferErrorMessages);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(buildTransferWidget());
		searchCriteriaPanel.add(new SpacerWidget()); 
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
	 * Builds the transfer widget.
	 * 
	 * @return the widget
	 */
	private Widget buildTransferWidget(){
		FlowPanel hpT = new FlowPanel();
		hpT.add(errorMessages);
		transferButton.setTitle("Transfer");
		clearButton.setTitle("Clear");
		hpT.add(transferButton);
		hpT.add(clearButton);
		form.setWidget(hpT);
		return form;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#clearTransferCheckBoxes()
	 */
	public void clearTransferCheckBoxes(){
		for(CodeListSearchDTO result : codeListResults){
				result.setTransferable(false);
		}
		AdminManageCodeListSearchModel model = new AdminManageCodeListSearchModel();
		model.setData(codeListResults);
		buildDataTable(model);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<CodeListSearchDTO> results) {		
		buildCodeListDataTable((AdminManageCodeListSearchModel) results);//Default value for isAscending is true and isChecked is false.
	}
	
	
	/**
	 * Builds the code list data table.
	 * 
	 * @param results
	 *            the results
	 */
	private void buildCodeListDataTable(AdminManageCodeListSearchModel results){
		if(results == null) {
			return;
		}
		CellTable<CodeListSearchDTO> cellTable = new CellTable<CodeListSearchDTO>();
		ListDataProvider<CodeListSearchDTO> sortProvider = new ListDataProvider<CodeListSearchDTO>();
		
		codeListResults = new ArrayList<CodeListSearchDTO>();
		codeListResults.addAll(results.getData());
		// Display 50 rows in one page or all records.
		cellTable.setPageSize(50);
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		cellTable.redraw();
		sortProvider.refresh();
		sortProvider.getList().addAll(results.getData());
		ListHandler<CodeListSearchDTO> sortHandler = new ListHandler<CodeListSearchDTO>(sortProvider.getList());
		cellTable.addColumnSortHandler(sortHandler);
		cellTable = adapter.addColumnToTable(cellTable,sortHandler);
		sortProvider.addDataDisplay(cellTable);
		spager.setDisplay(cellTable);
        spager.setToolTipAndTabIndex(spager);
        view.getvPanelForQDMTable().clear();
        view.getvPanelForQDMTable().add(cellTable);
		view.getvPanelForQDMTable().add(new SpacerWidget());
		view.getvPanelForQDMTable().add(spager);
		

	}

	
	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	public SearchView<CodeListSearchDTO> getView() {
		return view;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return searchCriteriaPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#getSearchButton()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#getTransferErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getTransferErrorMessageDisplay() {
		return transferErrorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#getTransferButton()
	 */
	@Override
	public HasClickHandlers getTransferButton() {
		return transferButton;
	}

	

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return Integer.MAX_VALUE;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#getValueSetSearchFilterPanel()
	 */
	@Override
	public ValueSetSearchFilterPanel getValueSetSearchFilterPanel() {
		return vssfp;
	}

	

	/**
	 * Gets the adapter.
	 * 
	 * @return the adapter
	 */
	public AdminCodeListSearchResultsAdapter getAdapter() {
		return adapter;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#setAdapter(mat.client.codelist.AdminCodeListSearchResultsAdapter)
	 */
	public void setAdapter(AdminCodeListSearchResultsAdapter adapter) {
		this.adapter = adapter;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.AdminValueSetSearchDisplay#getClearButton()
	 */
	@Override
	public HasClickHandlers getClearButton() {
		return clearButton;
	}

	/**
	 * Gets the spager.
	 * 
	 * @return the spager
	 */
	public MatSimplePager getSpager() {
		return spager;
	}

	/**
	 * Sets the spager.
	 * 
	 * @param spager
	 *            the new spager
	 */
	public void setSpager(MatSimplePager spager) {
		this.spager = spager;
	}

	
}
