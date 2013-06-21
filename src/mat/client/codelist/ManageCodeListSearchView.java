package mat.client.codelist;

import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.VerticalFlowPanel;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.HasSortHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.client.util.ClientConstants;
import mat.model.CodeListSearchDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class ManageCodeListSearchView implements ManageCodeListSearchPresenter.ValueSetSearchDisplay {
	private FlowPanel searchCriteriaPanel = new FlowPanel();
	private Panel createNewPanel;
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	private TextBox searchInput = new TextBox();
	Button transferButton = new PrimaryButton("Transfer");
	private SearchView<CodeListSearchDTO> view;
	
	public Grid508 getDataTable() {
		return view.getDataTable();
	}
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	protected ErrorMessageDisplay transferErrorMessages = new ErrorMessageDisplay();
	private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
	
	/*US537*/
	private Button createButton = new SecondaryButton("Create");
	private ListBoxMVP options = new ListBoxMVP();

	public ManageCodeListSearchView() {	
		String currentUserRole = MatContext.get().getLoggedInUserRole();
		/*US537*/
		if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			view = new ValueSetSearchView();
			view.buildDataTable(new ManageCodeListSearchModel());
			Widget searchText = LabelBuilder.buildLabelWithEmbeddedLink(searchInput, "Search for a Value Set","CodeList");
			searchCriteriaPanel.add(errorMessages);
			searchCriteriaPanel.add(new SpacerWidget());
			loadListBoxOptions();
			searchCriteriaPanel.add(LabelBuilder.buildLabel("Create", "Create Value Set"));
			searchCriteriaPanel.add(options);
			options.setName("Create");
			DOM.setElementAttribute(options.getElement(), "id", "Create Value Set");
			searchCriteriaPanel.add(createButton);
			createButton.setTitle("Create");
			searchCriteriaPanel.add(new SpacerWidget());
			
			searchCriteriaPanel.addStyleName("leftAligned");
			searchCriteriaPanel.add(searchText);
			
			searchCriteriaPanel.add(vssfp.getPanel());
			searchCriteriaPanel.add(new SpacerWidget());
			searchCriteriaPanel.add(buildSearchWidget());
			
			searchCriteriaPanel.add(view.asWidget());
			searchCriteriaPanel.setStyleName("contentPanel");
		}else{
			view = new AdminValueSetSearchView();
			view.buildDataTable(new AdminManageCodeListSearchModel());
			searchCriteriaPanel.add(errorMessages);
			searchCriteriaPanel.add(new SpacerWidget());
			searchCriteriaPanel.add(new SpacerWidget());
			searchCriteriaPanel.add(buildSearchWidget());
			
			searchCriteriaPanel.add(view.asWidget());
			searchCriteriaPanel.setStyleName("contentPanel");
			searchCriteriaPanel.add(transferErrorMessages);
			searchCriteriaPanel.add(new SpacerWidget());
			searchCriteriaPanel.add(buildTransferWidget());
			searchCriteriaPanel.add(new SpacerWidget()); 
		}
		MatContext.get().setManageCodeListSearchView(this);
		
	}
	
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
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

	private Widget buildSearchWidget(){
		HorizontalPanel hp = new HorizontalPanel();
		FlowPanel fp1 = new FlowPanel();
		fp1.add(searchInput);
		fp1.add(searchButton);
		fp1.add(new SpacerWidget());
		hp.add(fp1);
		return hp;
	}
	
	private Widget buildTransferWidget(){
		FlowPanel hpT = new FlowPanel();
		hpT.setStylePrimaryName("rightAlignButton");
		hpT.add(transferButton);
		return hpT;
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
	public HasClickHandlers getTransferButton() {
		return transferButton;
	}

	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForEditTool() {
		return view;
	}
	@Override
	public void buildDataTable(SearchResults<CodeListSearchDTO> results) {
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
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement() {
		return view;
	}

	@Override
	public HasSortHandler getPageSortTool() {
		return view;
	}

	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}

	@Override
	public void buildDataTable(SearchResults<CodeListSearchDTO> results, boolean isAscending) {
		view.buildDataTable(results,isAscending,false);
	}

	
	private void loadListBoxOptions(){
		options.addItem(ConstantMessages.DEFAULT_SELECT);
		options.addItem(ConstantMessages.CREATE_NEW_GROUPED_VALUE_SET);
		options.addItem(ConstantMessages.CREATE_NEW_VALUE_SET);
		options.addItem(ConstantMessages.CREATE_VALUE_SET_DRAFT);
	}
	
	/*US537*/
	@Override
	public HasClickHandlers getCreateButton() {
		return createButton;
	}

	/*US537*/
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedOption()
	 */
	@Override
	public String getSelectedOption() {
		return options.getItemText(options.getSelectedIndex());
	}

	/*US537*/
	@Override
	public void clearSelections() {
		options.setSelectedIndex(0);
	}

	/*US566*/
	@Override
	public ValueSetSearchFilterPanel getValueSetSearchFilterPanel() {
		return vssfp;
	}

	@Override
	public ErrorMessageDisplayInterface getTransferErrorMessageDisplay() {
		return transferErrorMessages;
	}

	
}
