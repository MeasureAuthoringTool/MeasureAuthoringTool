package org.ifmc.mat.client.measure;

import org.ifmc.mat.client.shared.ErrorMessageDisplay;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.FocusableWidget;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.PrimaryButton;
import org.ifmc.mat.client.shared.SecondaryButton;
import org.ifmc.mat.client.shared.SpacerWidget;
import org.ifmc.mat.client.shared.search.HasPageSelectionHandler;
import org.ifmc.mat.client.shared.search.HasPageSizeSelectionHandler;
import org.ifmc.mat.client.shared.search.SearchResults;
import org.ifmc.mat.client.shared.search.SearchView;
import org.ifmc.mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
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
	
	public ManageMeasureSearchView() {
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(errorMessages);
		mainPanel.add(new SpacerWidget());
		loadListBoxOptions();
		mainPanel.add(new Label("Create:"));
		mainPanel.add(options);
		options.setName("Create:");
		mainPanel.add(createButton);
		createButton.setTitle("Create");
		mainPanel.add(new SpacerWidget());
		
		Widget searchText = LabelBuilder.buildLabel(searchInput, "Search for a Measure");
		searchFocusHolder = new FocusableWidget(searchText);
		mainPanel.add(searchFocusHolder);
		mainPanel.add(buildSearchWidget());
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(view.asWidget());
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
	
	private void loadListBoxOptions(){
		options.addItem(ConstantMessages.DEFAULT_SELECT);
		options.addItem(ConstantMessages.CREATE_NEW_MEASURE);
		options.addItem(ConstantMessages.CREATE_NEW_VERSION);
		options.addItem(ConstantMessages.CREATE_NEW_DRAFT);
	}

	/* (non-Javadoc)
	 * @see org.ifmc.mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSelectedOption()
	 */
	@Override
	public String getSelectedOption() {
		return options.getItemText(options.getSelectedIndex());
	}

	
	@Override
	public void clearSelections() {
		options.setSelectedIndex(0);
	}
}
