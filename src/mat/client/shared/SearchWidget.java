package mat.client.shared;

import mat.client.util.MatTextBox;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

// TODO: Auto-generated Javadoc
/** MeasureSearchFilterWidget.java. */
public class SearchWidget extends Composite implements ClickHandler {
	/** Search Button Left Margin Value. */
	private static final int SEARCH_BTN_MRGN_LEFT = 5;
	/** Text Box height Value. */
	private static final int TEXT_BOX_HT = 20;
	/** Search button - {@link PrimaryButton}. */
	private PrimaryButton searchButton;
	
	/** The search input. {@link WatermarkedTextBox}. */
	private TextBox searchInput = new TextBox();
	
	/** Default Constructor. */
//	public SearchWidget() {
//		// searchInput.setWatermark("Search");
//		searchButton = new PrimaryButton("Search", "primaryButton");
//		searchButton.addClickHandler(this);
//		searchButton.getElement().setId("searchButton_PrimaryButton");
//		searchInput.getElement().getStyle().setHeight(TEXT_BOX_HT, Unit.PX);
//		searchInput.getElement().getStyle().setMarginLeft(10, Unit.PX);
//		searchInput.getElement().getStyle().setMarginRight(5, Unit.PX);
//		searchInput.getElement().getStyle().setMarginBottom(7, Unit.PX);
//		searchInput.getElement().setPropertyString("placeholder", "Search");
//		
//		searchButton.getElement().getStyle().setMarginLeft(SEARCH_BTN_MRGN_LEFT, Unit.PX);
//		VerticalPanel topPanel = new VerticalPanel();
//		topPanel.getElement().setId("SearchFilterWidget_verticalPanel");
//		HorizontalPanel horizontalPanel = new HorizontalPanel();
//		
//		horizontalPanel.add(searchInput);
//		searchInput.getElement().setId("searchInput_TextBox");
//		horizontalPanel.add(searchButton);
//		searchButton.getElement().setId("searchButton_PrimaryButton");
//		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("SearchWidgetDisplayed"),
//				"SearchWidgetDisplayed");
//		topPanel.add(invisibleLabel);
//		topPanel.add(horizontalPanel);
//		
//		horizontalPanel.setStylePrimaryName("searchWidget");
//		Element element = topPanel.getElement();
//		element.setAttribute("aria-role", "panel");
//		element.setAttribute("aria-labelledby", "SearchWidgetDisplayed");
//		element.setAttribute("aria-live", "assertive");
//		element.setAttribute("aria-atomic", "true");
//		element.setAttribute("aria-relevant", "all");
//		element.setAttribute("role", "alert");
//		// All composites must call initWidget() in their constructors.
//		initWidget(topPanel);
//	}
	
	/**
	 * Instantiates a new search widget.
	 * which takes button label, placeHolder and 
	 * style for the search widget as parameters
	 *
	 * @param btnStr the btn str
	 * @param placeHolder the place holder
	 * @param style the style
	 * @param width the width
	 */
	public SearchWidget(String btnLabel, String placeHolder, String style) {
		// searchInput.setWatermark("Search");
		searchButton = new PrimaryButton(btnLabel, "primaryButton");
		searchButton.addClickHandler(this);
		searchButton.getElement().setId("searchButton_PrimaryButton");
		searchInput.getElement().getStyle().setMarginLeft(10, Unit.PX);
		searchInput.getElement().getStyle().setMarginRight(5, Unit.PX);
		searchInput.getElement().getStyle().setMarginBottom(7, Unit.PX);
		searchInput.getElement().setPropertyString("placeholder", placeHolder);
		
		searchButton.getElement().getStyle().setMarginLeft(SEARCH_BTN_MRGN_LEFT, Unit.PX);
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.getElement().setId("SearchFilterWidget_verticalPanel");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		
		horizontalPanel.add(searchInput);
		searchInput.getElement().setId("searchInput_TextBox");
		horizontalPanel.add(searchButton);
		searchButton.getElement().setId("searchButton_PrimaryButton");
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("SearchWidgetDisplayed"),
				"SearchWidgetDisplayed");
		topPanel.add(invisibleLabel);
		topPanel.add(horizontalPanel);
		
		horizontalPanel.setStylePrimaryName(style);
		Element element = topPanel.getElement();
		element.setAttribute("aria-role", "panel");
		element.setAttribute("aria-labelledby", "SearchWidgetDisplayed");
		element.setAttribute("aria-live", "assertive");
		element.setAttribute("aria-atomic", "true");
		element.setAttribute("aria-relevant", "all");
		element.setAttribute("role", "alert");
		// All composites must call initWidget() in their constructors.
		initWidget(topPanel);
	}
	
	
	/** Gets the search button - {@link PrimaryButton}.
	 * 
	 * @return the button - {@link Button}. */
	public final PrimaryButton getSearchButton() {
		return searchButton;
	}
	
	/** Gets the search input.
	 * 
	 * @return the textBox {@link TextBox}. */
	public final TextBox getSearchInput() {
		return searchInput;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		
	}
	/** Sets the search button - {@link PrimaryButton}.
	 * @param searchButton the searchButton to set. */
	public final void setSearchButton(PrimaryButton searchButton) {
		this.searchButton = searchButton;
	}
	
	
	
}
