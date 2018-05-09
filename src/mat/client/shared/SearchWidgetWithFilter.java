package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

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

	

	/** The search input. {@link WatermarkedTextBox}. */
	private MatTextBox searchInput = new MatTextBox();
	/**
	 * Selected filter value - {@link Integer}.
	 */
	private int selectedFilter;
	private FocusPanel mainFocusPanel ;

	/**
	 * Default Constructor.
	 * 
	 * @param cssStyleTopPanel
	 *            the css style top panel
	 * @param cssStyleDisclosurePanel
	 *            the css style disclosure panel
	 */
	public SearchWidgetWithFilter(String cssStyleTopPanel, String cssStyleDisclosurePanel, String forView) {
		mainFocusPanel = new FocusPanel();
		searchButton = new Button("Search");
		searchButton.setType(ButtonType.PRIMARY);
		searchButton.setTitle("Search");
		
		searchButton.setHeight("32px");
		searchButton.setMarginLeft(5.0);
		searchButton.setPaddingBottom(5.0);
		searchInput.setWidth("150px");
		searchInput.setHeight("32px");
		searchButton.setId("SearchWidgetButton_" + forView);
		VerticalPanel topPanel = new VerticalPanel();
		
		topPanel.getElement().setId("SearchFilterWidget_verticalPanel_" + forView);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
	
		HorizontalPanel searchInputHPanel = new HorizontalPanel();
		searchInputHPanel.getElement().setId("SearchFilterWidget_SearchInputHPanel_" + forView);
		searchInputHPanel.add(searchInput);
		
		mainFocusPanel.add(searchInputHPanel);
		horizontalPanel.add(mainFocusPanel);
		horizontalPanel.add(searchButton);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(horizontalPanel);
		
		if(forView.equalsIgnoreCase("forMeasure")) {

			VerticalPanel checkBoxPanel = new VerticalPanel();
			Label myMeasuresText = (Label) LabelBuilder.buildLabel("Filter by My Measures", "Filter by My Measures");
			myMeasuresText.setStylePrimaryName("searchWidgetLabel");
			myMeasuresCheckBox.setStylePrimaryName("searchWidgetCheckBox");
			HorizontalPanel myMeasurePanel = new HorizontalPanel();
			myMeasurePanel.getElement().setId("SearchFilterWidget_HorizontalPanel_"+forView);
			myMeasurePanel.getElement().setAttribute("style","margin-top:10px;");
			myMeasurePanel.add(myMeasuresCheckBox);
			myMeasurePanel.add(myMeasuresText);
			checkBoxPanel.add(myMeasurePanel);
			myMeasuresCheckBox.setValue(true);
			mainPanel.setStylePrimaryName(cssStyleTopPanel);
			
			mainPanel.add(checkBoxPanel);
		} else {
			VerticalPanel checkBoxPanel = new VerticalPanel();
			Label myLibrariesText = (Label) LabelBuilder.buildLabel("Filter by My Libraries", "Filter by My Libraries");
			myLibrariesText.setStylePrimaryName("searchWidgetLabel");
			myLibrariesCheckBox.setStylePrimaryName("searchWidgetCheckBox");
			HorizontalPanel myLibrariesPanel = new HorizontalPanel();
			myLibrariesPanel.getElement().setId("SearchFilterWidget_HorizontalPanel_"+forView);
			myLibrariesPanel.getElement().setAttribute("style","margin-top:10px;");
			myLibrariesPanel.add(myLibrariesCheckBox);
			myLibrariesPanel.add(myLibrariesText);
			checkBoxPanel.add(myLibrariesPanel);
			myLibrariesCheckBox.setValue(true);
			mainPanel.setStylePrimaryName(cssStyleTopPanel);
			mainPanel.add(checkBoxPanel);
		}
		
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("SearchWidgetDisplayed_" + forView),
				"SearchWidgetDisplayed_" + forView);
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
		topPanel.setWidth("370px");
		initWidget(topPanel);
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

	public FocusPanel getMainFocusPanel() {
		return mainFocusPanel;
	}

	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		searchButton.addClickHandler(this);
	}
}
