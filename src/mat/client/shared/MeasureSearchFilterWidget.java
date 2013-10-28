package mat.client.shared;

import mat.client.ImageResources;
import mat.client.measure.metadata.CustomCheckBox;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * MeasureSearchFilterWidget.java.
 */
public class MeasureSearchFilterWidget extends Composite implements ClickHandler {
	/**
	 * ALL measure filter value.
	 */
	public static final int ALL_MEASURES = 1;
	/**
	 * My Measures Filter value.
	 */
	public static final int MY_MEASURES = 0;
	/**
	 * Search Button Left Margin Value.
	 */
	private static final int SEARCH_BTN_MRGN_LEFT = 5;
	/**
	 * Text Box height Value.
	 */
	private static final int TEXT_BOX_HT = 20;
	/**
	 * All Measure's Check Box.
	 */
	private CustomCheckBox allMeasuresCheckBox = new CustomCheckBox("All Measures",
			"All Measures", false);
	/**
	 * My Measures Check Box.
	 */
	private CustomCheckBox myMeasuresCheckBox = new CustomCheckBox("My Measures",
			"My Measures", false);
	/**
	 * Search button - {@link PrimaryButton}.
	 */
	private PrimaryButton searchButton;
	
	/** The search filter disclosure panel. {@link DisclosurePanel}. */
	private DisclosurePanel searchFilterDisclosurePanel = new DisclosurePanel();
	
	/** The search input. {@link WatermarkedTextBox}. */
	private WatermarkedTextBox searchInput = new WatermarkedTextBox();
	/**
	 * Selected filter value - {@link Integer}.
	 */
	private int selectedFilter;
	/**
	 * Default Constructor.
	 */
	public MeasureSearchFilterWidget() {
		searchInput.setWatermark("Search");
		searchButton = new PrimaryButton("Go", "primaryButton");
		searchButton.addClickHandler(this);
		searchInput.getElement().getStyle().setHeight(TEXT_BOX_HT, Unit.PX);
		/*
		 * searchInput.getElement().getStyle().setMarginLeft(5, Unit.PX);
		 * searchInput.getElement().getStyle().setMarginRight(5, Unit.PX);
		 * searchInput.getElement().setPropertyString("placeholder", "Search");
		 */
		searchButton.getElement().getStyle().setMarginLeft(SEARCH_BTN_MRGN_LEFT, Unit.PX);
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.getElement().setId("MeasureSearchFilterWidget_verticalPanel");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("MeasureSearchFilterWidget_FlowPanel");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		fp.getElement().setId("MeasureSearchFilterWidget_HorizontalPanel");
		createDisclosurePanel(horizontalPanel);
		horizontalPanel.add(searchInput);
		horizontalPanel.add(searchButton);
		topPanel.add(horizontalPanel);
		fp.add(searchFilterDisclosurePanel);
		fp.setStylePrimaryName("filterDisclosurePanel");
		topPanel.add(fp);
		horizontalPanel.setStylePrimaryName("searchWidget");
		resetFilter();
		addHandlersToCheckBox();
		// All composites must call initWidget() in their constructors.
		initWidget(topPanel);
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
					setSelectedFilter(MY_MEASURES);
				} else {
					allMeasuresCheckBox.setValue(true);
					setSelectedFilter(ALL_MEASURES);
				}
			}
		});
		allMeasuresCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					myMeasuresCheckBox.setValue(false);
					setSelectedFilter(ALL_MEASURES);
				} else {
					myMeasuresCheckBox.setValue(true);
					setSelectedFilter(MY_MEASURES);
				}
			}
		});
	}
	/**
	 * Method to add content in searchFiltetDisclosurePanel.
	 * @param mainPanel
	 *            : {@link HorizontalPanel}.
	 */
	private void createDisclosurePanel(HorizontalPanel mainPanel) {
		searchFilterDisclosurePanel.setAnimationEnabled(true);
		searchFilterDisclosurePanel.setOpen(false);
		VerticalPanel contentWidget = new VerticalPanel();
		contentWidget.getElement().setId("MeasureSearchFilterWidget_VerticalPanelContentWidget");
		HorizontalPanel myMeasurePanel = new HorizontalPanel();
		myMeasurePanel.getElement().setId("MeasureSearchFilterWidget_HorizontalPanelMyMeasure");
		Label myMeasuresText = (Label) LabelBuilder.buildLabel("My Measures", "My Measures");
		myMeasuresText.setStylePrimaryName("searchWidgetLabel");
		myMeasuresCheckBox.setStylePrimaryName("searchWidgetCheckBox");
		myMeasurePanel.add(myMeasuresText);
		myMeasurePanel.add(myMeasuresCheckBox);
		HorizontalPanel allMeasurePanel = new HorizontalPanel();
		allMeasurePanel.getElement().setId("MeasureSearchFilterWidget_HorizontalPanelAllMeasure");
		Label allMeasuresText = (Label) LabelBuilder.buildLabel("All Measures", "All Measures");
		allMeasuresText.setStylePrimaryName("searchWidgetLabel");
		allMeasuresCheckBox.setStylePrimaryName("searchWidgetCheckBox");
		allMeasurePanel.add(allMeasuresText);
		allMeasurePanel.add(allMeasuresCheckBox);
		contentWidget.add(myMeasurePanel);
		contentWidget.add(new SpacerWidget());
		contentWidget.add(allMeasurePanel);
		HorizontalPanel headerWidget = new HorizontalPanel();
		headerWidget.getElement().setId("MeasureSearchFilterWidget_HorizontalPanelHeaderWidget");
		CustomButton zoomButton = (CustomButton) getImage("Search",
				ImageResources.INSTANCE.search_zoom(), "Search");
		zoomButton.setEnabled(false);
		CustomButton filterButton = (CustomButton) getImage("Click to select filter",
				ImageResources.INSTANCE.arrow_filter(), "Click to select filter");
		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchFilterDisclosurePanel
				.setOpen(!searchFilterDisclosurePanel.isOpen());
			}
		});
		HorizontalPanel editButtonPanel = new HorizontalPanel();
		editButtonPanel.getElement().setId("MeasureSearchFilterWidget_HorizontalPanelEditButton");
		editButtonPanel
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		editButtonPanel.add(zoomButton);
		editButtonPanel.add(filterButton);
		headerWidget.add(editButtonPanel);
		searchFilterDisclosurePanel.setContent(contentWidget);
		VerticalPanel widgetPanel = new VerticalPanel();
		widgetPanel.getElement().setId("MeasureSearchFilterWidget_VerticalPanelWidgetPanel");
		widgetPanel.add(headerWidget);
		widgetPanel.setCellHorizontalAlignment(headerWidget,
				HasHorizontalAlignment.ALIGN_RIGHT);
		widgetPanel.add(new SpacerWidget());
		mainPanel.add(widgetPanel);
	}
	/**
	 * Add Image on Button with invisible text. This text will be available when
	 * css is turned off.
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
	public final PrimaryButton getSearchButton() {
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
	public final void resetFilter() {
		myMeasuresCheckBox.setValue(true);
		allMeasuresCheckBox.setValue(false);
		setSelectedFilter(MY_MEASURES);
	}
	
	/**
	 * Sets the search button - {@link PrimaryButton}.
	 * 
	 * @param searchButton
	 *            the searchButton to set.
	 */
	public final void setSearchButton(PrimaryButton searchButton) {
		this.searchButton = searchButton;
	}
	
	/**
	 * Sets the search input.
	 * 
	 * @param textBox
	 *            the textBox to set.
	 */
	public final void setSearchInput(WatermarkedTextBox textBox) {
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
}
