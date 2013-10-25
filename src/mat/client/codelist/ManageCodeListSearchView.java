package mat.client.codelist;

import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.HasSortHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.client.util.ClientConstants;
import mat.model.CodeListSearchDTO;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * The Class ManageCodeListSearchView.
 */
public class ManageCodeListSearchView implements ManageCodeListSearchPresenter.ValueSetSearchDisplay {
	
	/** The search criteria panel. */
	private FlowPanel searchCriteriaPanel = new FlowPanel();
	
	/** The search button. */
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	
	/** The search input. */
	private TextBox searchInput = new TextBox();
	
	/** The view. */
	private SearchView<CodeListSearchDTO> view;
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#getDataTable()
	 */
	public Grid508 getDataTable() {
		return view.getDataTable();
	}
	
	/** The error messages. */
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The vssfp. */
	private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
	
	// Code commented for User Story MAT-2372 : Remove Value Set Creation.
	//private Button createButton = new SecondaryButton("Create");
	//private ListBoxMVP options = new ListBoxMVP();

	/**
	 * Instantiates a new manage code list search view.
	 */
	public ManageCodeListSearchView() {	
		String currentUserRole = MatContext.get().getLoggedInUserRole();
		/*US537*/
		if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			view = new ValueSetSearchView();
			view.buildDataTable(new ManageCodeListSearchModel());
			Widget searchText = LabelBuilder.buildLabelWithEmbeddedLink(searchInput, "Search for a Value Set","CodeList");
			searchCriteriaPanel.add(errorMessages);
			// Code commented for User Story MAT-2372 : Remove Value Set Creation.
			//searchCriteriaPanel.add(new SpacerWidget());
		//	loadListBoxOptions();
		//	searchCriteriaPanel.add(LabelBuilder.buildLabel("Create", "Create Value Set"));
		//	searchCriteriaPanel.add(options);
		//	options.setName("Create");
		//	DOM.setElementAttribute(options.getElement(), "id", "Create Value Set");
			//searchCriteriaPanel.add(createButton);
			//createButton.setTitle("Create");
			searchCriteriaPanel.add(new SpacerWidget());
			
			searchCriteriaPanel.addStyleName("leftAligned");
			searchCriteriaPanel.add(searchText);
			
			searchCriteriaPanel.add(vssfp.getPanel());
			searchCriteriaPanel.add(new SpacerWidget());
			searchCriteriaPanel.add(buildSearchWidget());
			
			searchCriteriaPanel.add(view.asWidget());
			searchCriteriaPanel.setStyleName("contentPanel");
		}/*else{
			AdminValueSetSearchView adminValueSetSearchView = new AdminValueSetSearchView();
			view = adminValueSetSearchView.getView();
			view.buildDataTable(new AdminManageCodeListSearchModel());
			searchCriteriaPanel.add(errorMessages);
			searchCriteriaPanel.add(new SpacerWidget());
			searchCriteriaPanel.add(new SpacerWidget());
			searchCriteriaPanel.add(buildSearchWidget());
			
			searchCriteriaPanel.add(view.asWidget());
			searchCriteriaPanel.setStyleName("contentPanel");
			
			searchCriteriaPanel.add(new SpacerWidget());
			searchCriteriaPanel.add(buildTransferWidget());
			searchCriteriaPanel.add(new SpacerWidget()); 
		}*/
		MatContext.get().setManageCodeListSearchView(this);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#clearAllCheckBoxes(mat.client.measure.metadata.Grid508)
	 */
	@Override	
	public void clearAllCheckBoxes(Grid508 dataTable){
			int rows = dataTable.getRowCount();
			int cols = dataTable.getColumnCount();
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					Widget w = getDataTable().getWidget(i, j);
					if(w instanceof CustomCheckBox){
						CustomCheckBox checkBox = ((CustomCheckBox)w);	
						if(checkBox.getValue()){
							checkBox.setValue(false);										
						}
					}
				}
			}
	}

	/**
	 * Builds the search widget.
	 * 
	 * @return the widget
	 */
	private Widget buildSearchWidget(){
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		FlowPanel fp1 = new FlowPanel();
		fp1.add(searchInput);
		fp1.add(searchButton);
		fp1.add(new SpacerWidget());
		hp.add(fp1);
		return hp;
	}
	
	/*private Widget buildTransferWidget(){
		FlowPanel hpT = new FlowPanel();
		hpT.setStylePrimaryName("rightAlignButton");
		hpT.add(transferButton);
		return hpT;
	}*/
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return searchCriteriaPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getSearchButton()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#getSelectIdForEditTool()
	 */
	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForEditTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<CodeListSearchDTO> results) {
		view.buildDataTable(results);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#getPageSize()
	 */
	@Override 
	public int getPageSize() {
		return view.getPageSize();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#getPageSizeSelectionTool()
	 */
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#getSelectIdForQDSElement()
	 */
	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement() {
		return view;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#getPageSortTool()
	 */
	@Override
	public HasSortHandler getPageSortTool() {
		return view;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.ValueSetSearchDisplay#buildDataTable(mat.client.shared.search.SearchResults, boolean)
	 */
	@Override
	public void buildDataTable(SearchResults<CodeListSearchDTO> results, boolean isAscending) {
		view.buildDataTable(results,isAscending,false);
	}
	// Code commented for User Story MAT-2372 : Remove Value Set Creation.
	
	/*private void loadListBoxOptions(){
		options.addItem(ConstantMessages.DEFAULT_SELECT);
		options.addItem(ConstantMessages.CREATE_NEW_GROUPED_VALUE_SET);
		options.addItem(ConstantMessages.CREATE_NEW_VALUE_SET);
		options.addItem(ConstantMessages.CREATE_VALUE_SET_DRAFT);
	}*/
	
	// Code commented for User Story MAT-2372 : Remove Value Set Creation.
	//@Override
	/**
	 * Gets the creates the button.
	 * 
	 * @return the creates the button
	 */
	public HasClickHandlers getCreateButton() {
		//return createButton;
		return null;
	}

	// Code commented for User Story MAT-2372 : Remove Value Set Creation.
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedOption()
	 */
	/*@Override
	public String getSelectedOption() {
		return options.getItemText(options.getSelectedIndex());
	}

	US537
	@Override
	public void clearSelections() {
		options.setSelectedIndex(0);
	}*/

	/*US566*/
	@Override
	public ValueSetSearchFilterPanel getValueSetSearchFilterPanel() {
		return vssfp;
	}

	/*@Override
	public ErrorMessageDisplayInterface getTransferErrorMessageDisplay() {
		return transferErrorMessages;
	}*/

	
}
