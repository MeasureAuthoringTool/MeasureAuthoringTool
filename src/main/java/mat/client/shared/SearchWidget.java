package mat.client.shared;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.util.MatTextBox;
import mat.shared.UUIDUtilClient;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

/** MeasureSearchFilterWidget.java. */
public class SearchWidget extends Composite implements ClickHandler {
	/** Search button - {@link Button}. */
	private Button searchButton;
	
	/** The search input. {@link MatTextBox}. */
	private MatTextBox searchInput = new MatTextBox();
	
	private FocusPanel searchInputFocusPanel = new FocusPanel();
	
	/** Default Constructor. */
	
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
		searchButton = new Button();
		searchButton.setType(ButtonType.PRIMARY);
		searchButton.setText(btnLabel);
		searchButton.setTitle(btnLabel);
		searchButton.addClickHandler(this);
		searchButton.getElement().setId("searchButton_Button"+UUIDUtilClient.uuid(4));
		
		searchButton.setHeight("32px");
		searchButton.setMarginLeft(5.0);
		searchButton.setPaddingBottom(5.0);
		searchInput.setWidth("150px");
		searchInput.setHeight("32px");
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.getElement().setId("SearchFilterWidget_verticalPanel"+UUIDUtilClient.uuid(4));
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		
		searchInputFocusPanel.getElement().setId("SearchFilterWidget_SearchInputFP"+UUIDUtilClient.uuid(4));
		searchInputFocusPanel.add(searchInput);
		horizontalPanel.add(searchInputFocusPanel);
		searchInput.getElement().setId("searchInput_TextBox"+UUIDUtilClient.uuid(4));
		horizontalPanel.add(searchButton);
		
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
	public final Button getSearchButton() {
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
	public final void setSearchButton(Button searchButton) {
		this.searchButton = searchButton;
	}


	public FocusPanel getSearchInputFocusPanel() {
		return searchInputFocusPanel;
	}
	
	
	
}
