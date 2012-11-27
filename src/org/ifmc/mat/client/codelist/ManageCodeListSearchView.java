package org.ifmc.mat.client.codelist;

import org.ifmc.mat.client.shared.ErrorMessageDisplay;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.PrimaryButton;
import org.ifmc.mat.client.shared.SecondaryButton;
import org.ifmc.mat.client.shared.SpacerWidget;
import org.ifmc.mat.client.shared.VerticalFlowPanel;
import org.ifmc.mat.client.shared.search.HasPageSelectionHandler;
import org.ifmc.mat.client.shared.search.HasPageSizeSelectionHandler;
import org.ifmc.mat.client.shared.search.HasSortHandler;
import org.ifmc.mat.client.shared.search.SearchResults;
import org.ifmc.mat.client.shared.search.SearchView;
import org.ifmc.mat.model.CodeListSearchDTO;
import org.ifmc.mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class ManageCodeListSearchView implements ManageCodeListSearchPresenter.ValueSetSearchDisplay {
	private FlowPanel searchCriteriaPanel = new FlowPanel();
	/*private Anchor createNewAnchor = new Anchor("Create Value Set");
	private Anchor createNewGroupedAnchor = new Anchor("Create Grouped Value Set");*/
	private Button searchButton = new PrimaryButton("Search");
	private TextBox searchInput = new TextBox();
	private SearchView<CodeListSearchDTO> view = new ValueSetSearchView();
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private Panel createNewPanel;
	
	private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
	
	/*US537*/
	private Button createButton = new SecondaryButton("Create");
	private ListBoxMVP options = new ListBoxMVP();

	public ManageCodeListSearchView() {		
		view.buildDataTable(new ManageCodeListSearchModel());
		Widget searchText = LabelBuilder.buildLabelWithEmbeddedLink(searchInput, "Search for a Value Set","CodeList");
		searchCriteriaPanel.add(errorMessages);
		searchCriteriaPanel.add(new SpacerWidget());
		
		/*US537*/
		loadListBoxOptions();
		searchCriteriaPanel.add(new Label("Create:"));
		searchCriteriaPanel.add(options);
		options.setName("Create:");
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
		
	}
	
	
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}


	private Widget buildSearchWidget(){
		HorizontalPanel hp = new HorizontalPanel();
		SimplePanel sp1 = new SimplePanel();
		sp1.addStyleName("codeListLink");
		sp1.add(searchInput);
		SimplePanel sp2 = new SimplePanel();
		sp2.add(searchButton);
		hp.add(sp1);
		hp.add(sp2);
		return hp;
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
	 * @see org.ifmc.mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedOption()
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
}
