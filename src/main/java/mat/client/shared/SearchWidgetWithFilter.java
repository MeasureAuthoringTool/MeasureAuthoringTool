package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.advancedSearch.AdvancedSearchPanel;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.util.MatTextBox;

/**
 * SearchWidgetWithFilter.java.
 */
public class SearchWidgetWithFilter extends Composite implements ClickHandler{
	/**
	 * ALL measure filter value.
	 */
	public static final int ALL = 1;
	/**
	 * My Measures Filter value.
	 */
	public static final int MY = 0;
	
	
	/**
	 * My Measures Check Box.
	 */
	private CustomCheckBox myMeasuresCheckBox = new CustomCheckBox("Filter by My Measures", "Filter by My Measures", false);

	private CustomCheckBox myLibrariesCheckBox = new CustomCheckBox("My Libraries", "My Measures", false);
	/**
	 * Search button - {@link PrimaryButton}.
	 */
	private Button searchButton;
	
	private AdvancedSearchPanel advancedSearchPanel;

	/** The search input. {@link WatermarkedTextBox}. */
	private MatTextBox searchInput = new MatTextBox();
	/**
	 * Selected filter value - {@link Integer}.
	 */
	private int selectedFilter;
	private Panel mainFocusPanel ;

	/**
	 * Default Constructor.
	 * 
	 * @param cssStyleTopPanel
	 *            the css style top panel
	 * @param cssStyleDisclosurePanel
	 *            the css style disclosure panel
	 */
	public SearchWidgetWithFilter(String cssStyleTopPanel, String cssStyleDisclosurePanel, String forView) {
		mainFocusPanel = new HorizontalPanel();
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.getElement().setId("SearchFilterWidget_verticalPanel_" + forView);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.getElement().setAttribute("style","padding-top:8px; padding-left:8px; padding-right:8px");
		HorizontalPanel searchInputHPanel = new HorizontalPanel();
		searchInputHPanel.getElement().setId("SearchFilterWidget_SearchInputHPanel_" + forView);
		searchInputHPanel.add(searchInput);
		
		mainFocusPanel.add(searchInputHPanel);
		horizontalPanel.add(mainFocusPanel);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(buildSearchHeader());
		mainPanel.add(horizontalPanel);
		mainPanel.setStylePrimaryName(cssStyleTopPanel);

		horizontalPanel.add(buildFilterByPanel(forView));
				
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.add(buildSearchButton(forView));
		
		VerticalPanel advancedSearchVerticalPanel = new VerticalPanel();
		advancedSearchVerticalPanel.setWidth("100%");
		
		advancedSearchPanel = new AdvancedSearchPanel(forView);
		
		advancedSearchVerticalPanel.add(advancedSearchPanel.getAnchorPanel());
		advancedSearchVerticalPanel.add(advancedSearchPanel.getCollapsePanel());
		advancedSearchVerticalPanel.add(buttonPanel);
		
		
		mainPanel.add(advancedSearchVerticalPanel);

		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("SearchWidgetDisplayed_" + forView), "SearchWidgetDisplayed_" + forView);
		topPanel.add(invisibleLabel);
		topPanel.add(mainPanel);
		
		resetFilter(forView);
		addHandlersToCheckBox();
		Element element = topPanel.getElement();
		element.setAttribute("aria-role", "panel");
		element.setAttribute("aria-labelledby", "SearchWidgetDisplayed_" + forView);
		element.setAttribute("aria-live", "assertive");
		element.setAttribute("aria-atomic", "true");
		element.setAttribute("aria-relevant", "all");
		element.setAttribute("role", "alert");
		topPanel.setWidth("100%");
		initWidget(topPanel);
	}

	private Panel buildFilterByPanel(String forView) {
		if(forView.equalsIgnoreCase("forMeasure")) {
			return buildFilterByPanel(myMeasuresCheckBox, "Filter by My Measures", forView);
		} else {
			return buildFilterByPanel(myLibrariesCheckBox, "Filter by My Libraries", forView);
		}		
	}

	private Panel buildFilterByPanel(CustomCheckBox checkBox, String labelText, String forView) {
		VerticalPanel checkBoxPanel = new VerticalPanel();
		Label label = (Label) LabelBuilder.buildLabel(labelText, labelText);
		label.setStylePrimaryName("searchWidgetLabel");
		checkBox.setStylePrimaryName("searchWidgetCheckBox");
		HorizontalPanel labelPanel = new HorizontalPanel();
		labelPanel.getElement().setId("SearchFilterWidget_HorizontalPanel_" + forView);
		labelPanel.getElement().setAttribute("style","margin-top:10px;");
		labelPanel.add(checkBox);
		labelPanel.add(label);
		checkBoxPanel.add(labelPanel);
		checkBox.setValue(true);
		return checkBoxPanel;
	}

	private Label buildSearchHeader() {
		Label searchHeader = new Label("Search");
		searchHeader.getElement().setId("searchHeader_Label");
		searchHeader.setStyleName("recentSearchHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		return searchHeader;
	}

	private Button buildSearchButton(String forView) {
		searchButton = new Button("Search");
		searchButton.setType(ButtonType.PRIMARY);
		searchButton.setMarginRight(8.0);
		searchButton.setTitle("Search");
		searchInput.setWidth("740px");
		searchInput.setHeight("32px");
		searchInput.addStyleName("searchFilterTextBox");
		searchInput.getElement().setAttribute("placeholder", "Enter Search Text");
		searchButton.setMarginTop(-31.0);
		searchButton.setHeight("32px");
		searchButton.setId("SearchWidgetButton_" + forView);
		searchButton.setPull(Pull.RIGHT);
		return searchButton;
	}

	/**
	 * Private method to add valueChangeHandler's to check box's.
	 */
	private void addHandlersToCheckBox() {
		myLibrariesCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()){
					setSelectedFilter(MY);
				} else {
					setSelectedFilter(ALL);
				}
				
			}
		});
		
		
		myMeasuresCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()){
					setSelectedFilter(MY);
				} else {
					setSelectedFilter(ALL);
				}
				
			}
		});
	}

	/**
	 * Gets the search button - {@link PrimaryButton}.
	 * 
	 * @return the button - {@link Button}.
	 */
	public final Button getSearchButton() {
		return searchButton;
	}

	/**
	 * Gets the custom CheckBox - {@link CustomCheckBox}.
	 * 
	 * @return the CustomCheckBox - {@link CustomCheckBox}.
	 */
	public final CustomCheckBox getMeasureCustomCheckBox() {
		return myMeasuresCheckBox;
	}

	/**
	 * Gets the custom CheckBox - {@link CustomCheckBox}.
	 * 
	 * @return the CustomCheckBox - {@link CustomCheckBox}.
	 */
	public final CustomCheckBox getLibraryCustomCheckBox() {
		return myLibrariesCheckBox;
	}
	
	/**
	 * Gets the search input.
	 * 
	 * @return the textBox {@link TextBox}.
	 */
	public final TextBox getSearchInput() {
		return searchInput;
	}

	/**
	 * Gets the selected filter value - {@link Integer}.
	 * 
	 * @return the selectedFilter - {@link Integer}.
	 */
	public final int getSelectedFilter() {
		return selectedFilter;
	}


	/**
	 * Method to Reset check box to default state.
	 */
	public final void resetFilter(String forView) {
		if(forView.equalsIgnoreCase("forMeasure")) {
			myMeasuresCheckBox.setValue(true);
			setSelectedFilter(MY);
		} else {
			myLibrariesCheckBox.setValue(true);
			setSelectedFilter(MY);
		}
	}

	/**
	 * Sets the search button - {@link PrimaryButton}.
	 * 
	 * @param searchButton
	 *            the searchButton to set.
	 */
	public final void setSearchButton(Button searchButton) {
		this.searchButton = searchButton;
	}

	/**
	 * Sets the search input.
	 * 
	 * @param textBox
	 *            the textBox to set.
	 */
	public final void setSearchInput(MatTextBox textBox) {
		searchInput = textBox;
	}

	/**
	 * Sets the selected filter value - {@link Integer}.
	 * 
	 * @param selectedFilter
	 *            the selectedFilter to set.
	 */
	public final void setSelectedFilter(int selectedFilter) {
		this.selectedFilter = selectedFilter;
	}

	public Panel getMainFocusPanel() {
		return mainFocusPanel;
	}

	@Override
	public void onClick(ClickEvent event) {
		searchButton.addClickHandler(this);
	}

	public AdvancedSearchPanel getAdvancedSearchPanel() {
		return advancedSearchPanel;
	}

	public void setAdvancedSearchPanel(AdvancedSearchPanel advancedSearchPanel) {
		this.advancedSearchPanel = advancedSearchPanel;
	}
}
