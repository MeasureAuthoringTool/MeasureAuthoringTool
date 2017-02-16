package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.ImageResources;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.util.MatTextBox;

/**
 * SearchWidgetWithFilter.java.
 */
public class SearchWidgetWithFilter extends Composite implements ClickHandler {
	/**
	 * ALL measure filter value.
	 */
	public static final int ALL = 1;
	/**
	 * My Measures Filter value.
	 */
	public static final int MY = 0;
	
	/**
	 * All Measure's Check Box.
	 */
	private CustomCheckBox allMeasuresCheckBox = new CustomCheckBox("All Measures", "All Measures", false);

	private CustomCheckBox allLibrariesCheckBox = new CustomCheckBox("All Libraries", "All Measures", false);
	/**
	 * My Measures Check Box.
	 */
	private CustomCheckBox myMeasuresCheckBox = new CustomCheckBox("My Measures", "My Measures", false);

	private CustomCheckBox myLibrariesCheckBox = new CustomCheckBox("My Libraries", "My Measures", false);
	/**
	 * Search button - {@link PrimaryButton}.
	 */
	private Button searchButton;

	/** The search filter disclosure panel. {@link DisclosurePanel}. */
	private DisclosurePanel searchFilterDisclosurePanel = new DisclosurePanel();

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
		searchButton = new Button("Search");
		searchButton.setType(ButtonType.PRIMARY);
		searchButton.setTitle("Search");
		searchButton.addClickHandler(this);
		searchButton.setHeight("30px");
		searchButton.setMarginLeft(5.0);
		searchButton.setPaddingBottom(5.0);
		searchInput.setWidth("150px");
		searchInput.setHeight("32px");
		searchButton.setId("SearchWidgetButton_" + forView);
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.setWidth("100px");
		topPanel.getElement().setId("SearchFilterWidget_verticalPanel_" + forView);
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("SearchFilterWidget_FlowPanel_" + forView);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		fp.getElement().setId("SearchFilterWidget_HorizontalPanel_" + forView);
		createDisclosurePanel(horizontalPanel, forView);
		horizontalPanel.add(searchInput);
		horizontalPanel.add(searchButton);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("SearchWidgetDisplayed_" + forView),
				"SearchWidgetDisplayed_" + forView);
		topPanel.add(invisibleLabel);
		topPanel.add(horizontalPanel);
		fp.add(searchFilterDisclosurePanel);
		fp.setStylePrimaryName(cssStyleDisclosurePanel);
		topPanel.add(fp);
		horizontalPanel.setStylePrimaryName(cssStyleTopPanel);
		resetFilter(forView);
		addHandlersToCheckBox();
		Element element = topPanel.getElement();
		element.setAttribute("aria-role", "panel");
		element.setAttribute("aria-labelledby", "SearchWidgetDisplayed_" + forView);
		element.setAttribute("aria-live", "assertive");
		element.setAttribute("aria-atomic", "true");
		element.setAttribute("aria-relevant", "all");
		element.setAttribute("role", "alert");
		mainFocusPanel = new FocusPanel();
		mainFocusPanel.add(topPanel);
		mainFocusPanel.setStyleName("margin-left");
		// All composites must call initWidget() in their constructors.
		initWidget(mainFocusPanel);
	}

	/**
	 * Private method to add valueChangeHandler's to check box's.
	 */
	private void addHandlersToCheckBox() {
		myMeasuresCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					allMeasuresCheckBox.setValue(false);
					setSelectedFilter(MY);
				} else {
					allMeasuresCheckBox.setValue(true);
					setSelectedFilter(ALL);
				}
			}
		});
		allMeasuresCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					myMeasuresCheckBox.setValue(false);
					setSelectedFilter(ALL);
				} else {
					myMeasuresCheckBox.setValue(true);
					setSelectedFilter(MY);
				}
			}
		});

		myLibrariesCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					allLibrariesCheckBox.setValue(false);
					setSelectedFilter(MY);
				} else {
					allLibrariesCheckBox.setValue(true);
					setSelectedFilter(ALL);
				}
			}
		});

		allLibrariesCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					myLibrariesCheckBox.setValue(false);
					setSelectedFilter(ALL);
				} else {
					myLibrariesCheckBox.setValue(true);
					setSelectedFilter(MY);
				}
			}
		});
	}

	/**
	 * Method to add content in searchFiltetDisclosurePanel.
	 * 
	 * @param mainPanel
	 *            : {@link HorizontalPanel}.
	 */
	private void createDisclosurePanel(HorizontalPanel mainPanel, String forView) {

		searchFilterDisclosurePanel.setAnimationEnabled(true);
		searchFilterDisclosurePanel.setOpen(false);
		VerticalPanel contentWidget = new VerticalPanel();
		contentWidget.getElement().setId("SearchFilterWidget_VerticalPanelContentWidget_"+forView);
		HorizontalPanel myMeasurePanel = new HorizontalPanel();
		myMeasurePanel.getElement().setId("SearchFilterWidget_HorizontalPanelMyMeasure_"+forView);
		HorizontalPanel allMeasurePanel = new HorizontalPanel();
		allMeasurePanel.getElement().setId("SearchFilterWidget_HorizontalPanelAllMeasure_"+forView);
		
		HorizontalPanel headerWidget = new HorizontalPanel();
		headerWidget.getElement().setId("SearchFilterWidget_HorizontalPanelHeaderWidget_"+forView);
		
		HorizontalPanel editButtonPanel = new HorizontalPanel();
		editButtonPanel.getElement().setId("SearchFilterWidget_HorizontalPanelEditButton_"+forView);
		
		VerticalPanel widgetPanel = new VerticalPanel();
		widgetPanel.getElement().setId("SearchFilterWidget_VerticalPanelWidgetPanel_"+forView);
		
		CustomButton filterButton = (CustomButton) getImage("Click to select filter"+forView,
				ImageResources.INSTANCE.arrow_filter(), "Click to select filter"+forView);
		filterButton.getElement().setId("filterButton_"+forView);
		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchFilterDisclosurePanel.setOpen(!searchFilterDisclosurePanel.isOpen());
			}
		});
		
		if (forView.equalsIgnoreCase("forMeasure")) {
			
			
			Label myMeasuresText = (Label) LabelBuilder.buildLabel("My Measures", "My Measures");
			myMeasuresText.setStylePrimaryName("searchWidgetLabel");
			myMeasuresCheckBox.setStylePrimaryName("searchWidgetCheckBox");
			myMeasurePanel.add(myMeasuresText);
			myMeasurePanel.add(myMeasuresCheckBox);
			Label allMeasuresText = (Label) LabelBuilder.buildLabel("All Measures", "All Measures");
			allMeasuresText.setStylePrimaryName("searchWidgetLabel");
			allMeasuresCheckBox.setStylePrimaryName("searchWidgetCheckBox");
			allMeasurePanel.add(allMeasuresText);
			allMeasurePanel.add(allMeasuresCheckBox);
			contentWidget.add(myMeasurePanel);
			contentWidget.add(new SpacerWidget());
			contentWidget.add(allMeasurePanel);
			
			editButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			editButtonPanel.add(filterButton);
			headerWidget.add(editButtonPanel);
			searchFilterDisclosurePanel.setContent(contentWidget);
			
			widgetPanel.add(headerWidget);
			widgetPanel.setCellHorizontalAlignment(headerWidget, HasHorizontalAlignment.ALIGN_RIGHT);
			widgetPanel.add(new SpacerWidget());
			mainPanel.add(widgetPanel);
		} else {
			Label myLibLabel = (Label) LabelBuilder.buildLabel("My Libraries", "My Libraries");
			myLibLabel.setStylePrimaryName("searchWidgetLabel");
			myLibrariesCheckBox.setStylePrimaryName("searchWidgetCheckBox");
			myMeasurePanel.add(myLibLabel);
			myMeasurePanel.add(myLibrariesCheckBox);
			
			Label allLibLabel = (Label) LabelBuilder.buildLabel("All Libraries", "All Libraries");
			allLibLabel.setStylePrimaryName("searchWidgetLabel");
			allLibrariesCheckBox.setStylePrimaryName("searchWidgetCheckBox");
			allMeasurePanel.add(allLibLabel);
			allMeasurePanel.add(allLibrariesCheckBox);
			contentWidget.add(myMeasurePanel);
			contentWidget.add(new SpacerWidget());
			contentWidget.add(allMeasurePanel);
			
			editButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			editButtonPanel.add(filterButton);
			headerWidget.add(editButtonPanel);
			searchFilterDisclosurePanel.setContent(contentWidget);
			widgetPanel.add(headerWidget);
			widgetPanel.setCellHorizontalAlignment(headerWidget, HasHorizontalAlignment.ALIGN_RIGHT);
			widgetPanel.add(new SpacerWidget());
			mainPanel.add(widgetPanel);
		}
	}

	/**
	 * Add Image on Button with invisible text. This text will be available when
	 * css is turned off.
	 * 
	 * @param action
	 *            - {@link String}
	 * @param url
	 *            - {@link ImageResource}.
	 * @param key
	 *            - {@link String}.
	 * @return - {@link Widget}.
	 */
	private Widget getImage(String action, ImageResource url, String key) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		return image;
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
	 * Gets the search filter disclosure panel.
	 * 
	 * @return the searchFilterDisclosurePanel- {@link DisclosurePanel}.
	 */
	public final DisclosurePanel getSearchFilterDisclosurePanel() {
		return searchFilterDisclosurePanel;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent)
	 */
	@Override
	public final void onClick(ClickEvent event) {
		searchFilterDisclosurePanel.setOpen(false);
	}

	/**
	 * Method to Reset check box to default state.
	 */
	public final void resetFilter(String forView) {
		if(forView.equalsIgnoreCase("forMeasure")) {
			myMeasuresCheckBox.setValue(true);
			allMeasuresCheckBox.setValue(false);
			setSelectedFilter(MY);
		} else {
			myLibrariesCheckBox.setValue(true);
			allLibrariesCheckBox.setValue(false);
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
}
