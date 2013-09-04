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
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;


public class AdminValueSetSearchView implements ManageCodeListSearchPresenter.AdminValueSetSearchDisplay {
	private FlowPanel searchCriteriaPanel = new FlowPanel();
	final FormPanel form = new FormPanel();
	
	private SearchView<CodeListSearchDTO> view = new SearchView<CodeListSearchDTO>(true);
	private AdminCodeListSearchResultsAdapter adapter;
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	private TextBox searchInput = new TextBox();
	Button transferButton = new PrimaryButton("Transfer","primaryGreyButton");
	private Button clearButton = new PrimaryButton("Clear All","primaryGreyLeftButton");
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	protected ErrorMessageDisplay transferErrorMessages = new ErrorMessageDisplay();
	private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
	public List<CodeListSearchDTO> codeListResults;
	//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
	MatSimplePager spager;
	
	
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
	
	public void clearTransferCheckBoxes(){
		for(CodeListSearchDTO result : codeListResults){
				result.setTransferable(false);
		}
		AdminManageCodeListSearchModel model = new AdminManageCodeListSearchModel();
		model.setData(codeListResults);
		buildDataTable(model);
	}
	
	@Override
	public void buildDataTable(SearchResults<CodeListSearchDTO> results) {		
		buildCodeListDataTable((AdminManageCodeListSearchModel) results);//Default value for isAscending is true and isChecked is false.
	}
	
	
	private void buildCodeListDataTable(AdminManageCodeListSearchModel results){
		if(results == null) {
			return;
		}
		CellTable<CodeListSearchDTO> cellTable = new CellTable<CodeListSearchDTO>();
		ListDataProvider<CodeListSearchDTO> sortProvider = new ListDataProvider<CodeListSearchDTO>();
		sortProvider = new ListDataProvider<CodeListSearchDTO>();
		codeListResults = new ArrayList<CodeListSearchDTO>();
		codeListResults.addAll(results.getData());
		// Display 50 rows in one page or all records.
		cellTable.setPageSize(50);
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		cellTable = adapter.addColumnToTable(cellTable,results.getData());
		
		cellTable.redraw();
		sortProvider.refresh();
		sortProvider.setList(codeListResults);
		
		sortProvider.addDataDisplay(cellTable);
		
        spager.setDisplay(cellTable);
        
        spager.setToolTipAndTabIndex(spager);
        view.getvPanelForQDMTable().clear();
        view.getvPanelForQDMTable().add(cellTable);
		view.getvPanelForQDMTable().add(new SpacerWidget());
		view.getvPanelForQDMTable().add(spager);
		

	}

	
	public SearchView<CodeListSearchDTO> getView() {
		return view;
	}

	@Override
	public Widget asWidget() {
		return searchCriteriaPanel;
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
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public ErrorMessageDisplayInterface getTransferErrorMessageDisplay() {
		return transferErrorMessages;
	}

	@Override
	public HasClickHandlers getTransferButton() {
		return transferButton;
	}

	

	@Override
	public int getPageSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	public ValueSetSearchFilterPanel getValueSetSearchFilterPanel() {
		return vssfp;
	}

	

	public AdminCodeListSearchResultsAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(AdminCodeListSearchResultsAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public HasClickHandlers getClearButton() {
		return clearButton;
	}

	public MatSimplePager getSpager() {
		return spager;
	}

	public void setSpager(MatSimplePager spager) {
		this.spager = spager;
	}

	
}
