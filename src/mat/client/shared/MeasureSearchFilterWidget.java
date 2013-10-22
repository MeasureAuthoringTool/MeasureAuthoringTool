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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MeasureSearchFilterWidget extends Composite implements ClickHandler {

	private TextBox textBox = new TextBox();
		private PrimaryButton button;
	CustomCheckBox myMeasuresCheckBox = new CustomCheckBox("My Measures",
			"My Measures", false);
	CustomCheckBox allMeasuresCheckBox = new CustomCheckBox("All Measures",
			"All Measures", false);
	
	DisclosurePanel searchFilterDisclosurePanel = new DisclosurePanel();
	public static final int MY_MEASURES = 0;
	public static final int ALL_MEASURES = 1;
	
	int selectedFilter;
	public MeasureSearchFilterWidget() {
		button = new PrimaryButton("Go", "primaryButton");
		button.addClickHandler(this);
		textBox.getElement().getStyle().setHeight(20, Unit.PX);
		textBox.getElement().getStyle().setMarginLeft(5, Unit.PX);
		textBox.getElement().getStyle().setMarginRight(5, Unit.PX);
		textBox.getElement().setPropertyString("placeholder", "Search");

		button.getElement().getStyle().setMarginLeft(5, Unit.PX);
		VerticalPanel topPanel = new VerticalPanel();
		FlowPanel fp = new FlowPanel();
		HorizontalPanel panel = new HorizontalPanel();
		createDisclosurePanel(panel);
		panel.add(textBox);
		panel.add(button);
		topPanel.add(panel);
		fp.add(searchFilterDisclosurePanel);
		fp.setStylePrimaryName("filterDisclosurePanel");
		topPanel.add(fp);
		panel.setStylePrimaryName("searchWidget");
		resetFilter();
		myMeasuresCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					allMeasuresCheckBox.setValue(false);
					setSelectedFilter(MY_MEASURES);
				}
				
			}
		});

		allMeasuresCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					myMeasuresCheckBox.setValue(false);
					setSelectedFilter(ALL_MEASURES);
				}

			}
		});

		// All composites must call initWidget() in their constructors.
		initWidget(topPanel);
	}

	private void createDisclosurePanel(HorizontalPanel mainPanel) {

		searchFilterDisclosurePanel.setAnimationEnabled(true);
		searchFilterDisclosurePanel.setOpen(false);
		VerticalPanel contentWidget = new VerticalPanel();
		HorizontalPanel myMeasurePanel = new HorizontalPanel();
		myMeasurePanel.add(LabelBuilder.buildLabel("My Measures","My Measures"));
		myMeasurePanel.add(myMeasuresCheckBox);
		
		HorizontalPanel allMeasurePanel = new HorizontalPanel();
		allMeasurePanel.add(LabelBuilder.buildLabel("All Measures","All Measures"));
		allMeasurePanel.add(allMeasuresCheckBox);
		contentWidget.add(myMeasurePanel);
		contentWidget.add(new SpacerWidget());
		contentWidget.add(allMeasurePanel);

		HorizontalPanel headerWidget = new HorizontalPanel();

		CustomButton zoomButton = (CustomButton) getImage("Zoom",
				ImageResources.INSTANCE.search_zoom(), "Zoom");
		
		CustomButton filterButton = (CustomButton) getImage("Filter",
				ImageResources.INSTANCE.arrow_filter(), "Filter");
		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchFilterDisclosurePanel
						.setOpen(!searchFilterDisclosurePanel.isOpen());
			}

		});

		HorizontalPanel editButtonPanel = new HorizontalPanel();
		editButtonPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		editButtonPanel.add(zoomButton);
		editButtonPanel.add(filterButton);
		headerWidget.add(editButtonPanel);
		searchFilterDisclosurePanel.setContent(contentWidget);

		VerticalPanel widgetPanel = new VerticalPanel();
		/* widgetPanel.setStylePrimaryName("bottom_border_inset"); */
		widgetPanel.add(headerWidget);
		widgetPanel.setCellHorizontalAlignment(headerWidget,
				HasHorizontalAlignment.ALIGN_RIGHT);
		/* widgetPanel.add(searchFilterDisclosurePanel); */
		widgetPanel.add(new SpacerWidget());
		/* widgetPanel.setStylePrimaryName("notes_table_background"); */
		
		mainPanel.add(widgetPanel);
	}

	private Widget getImage(String action, ImageResource url, String key) {

		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		/*addListener(image);*/
		return image;
	}

	@Override
	public void onClick(ClickEvent event) {
		searchFilterDisclosurePanel.setOpen(false);
	}
	
	public void resetFilter(){
		myMeasuresCheckBox.setValue(true);
		allMeasuresCheckBox.setValue(false);
		setSelectedFilter(MY_MEASURES);
	}

	/**
	 * @return the button
	 */
	public PrimaryButton getButton() {
		return button;
	}
	
	/**
	 * @return the textBox
	 */
	public TextBox getTextBox() {
		return textBox;
	}

	/**
	 * @param textBox the textBox to set
	 */
	public void setTextBox(TextBox textBox) {
		this.textBox = textBox;
	}

	/**
	 * @return the selectedFilter
	 */
	public int getSelectedFilter() {
		return selectedFilter;
	}

	/**
	 * @param selectedFilter the selectedFilter to set
	 */
	public void setSelectedFilter(int selectedFilter) {
		this.selectedFilter = selectedFilter;
	}
}
