package mat.client.shared;

import mat.shared.ConstantMessages;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/** CreateMeasureWidget.java. */
public class CreateMeasureWidget extends Composite {
	/** Search button - {@link PrimaryButton}. */
	private PrimaryButton createMeasure;
	/** The options. */
	private ListBoxMVP options = new ListBoxMVP();
	/** Default Constructor. */
	public CreateMeasureWidget() {
		// searchInput.setWatermark("Search");
		createMeasure = new PrimaryButton("Create", "primaryButton");
		options.getElement().getStyle().setMarginLeft(15, Unit.PX);
		options.getElement().getStyle().setMarginRight(10, Unit.PX);
		options.getElement().getStyle().setMarginBottom(7, Unit.PX);
		options.getElement().getStyle().setHeight(25, Unit.PX);
		
		createMeasure.getElement().setId("createMeasure_Button");
		options.setName("Create Measure Options");
		DOM.setElementAttribute(options.getElement(), "id", "CreateMeasureWidget");
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.getElement().setId("CreateMeasureWidget_verticalPanel");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		topPanel.getElement().setId("CreateMeasureWidget_horizontalPanel");
		loadListBoxOptions();
		horizontalPanel.add(options);
		horizontalPanel.add(createMeasure);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("CreateWidgetDisplayedSelectOptionsFromComboBox"),
				"CreateWidgetDisplayedSelectOptionsFromComboBox");
		topPanel.add(invisibleLabel);
		topPanel.add(horizontalPanel);
		horizontalPanel.setStylePrimaryName("searchWidget");
		// All composites must call initWidget() in their constructors.
		Element element = topPanel.getElement();
		element.setAttribute("aria-role", "panel");
		element.setAttribute("aria-labelledby", "CreateWidgetDisplayedSelectOptionsFromComboBox");
		element.setAttribute("aria-live", "assertive");
		element.setAttribute("aria-atomic", "true");
		element.setAttribute("aria-relevant", "all");
		element.setAttribute("role", "alert");
		initWidget(topPanel);
	}
	/** @return the createMeasure */
	public PrimaryButton getCreateMeasure() {
		return createMeasure;
	}
	/** @return the options */
	public ListBoxMVP getOptions() {
		return options;
	}
	private void loadListBoxOptions() {
		options.addItem(ConstantMessages.DEFAULT_SELECT);
		options.addItem(ConstantMessages.CREATE_NEW_MEASURE);
		options.addItem(ConstantMessages.CREATE_NEW_VERSION);
		options.addItem(ConstantMessages.CREATE_NEW_DRAFT);
	}
	/**
	 * @param createMeasure the createMeasure to set
	 */
	public void setCreateMeasure(PrimaryButton createMeasure) {
		this.createMeasure = createMeasure;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(ListBoxMVP options) {
		this.options = options;
	}
}
