package mat.client.shared;

import mat.shared.ConstantMessages;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
		createMeasure = new PrimaryButton("Go", "primaryButton");
		options.getElement().getStyle().setMarginLeft(15, Unit.PX);
		options.getElement().getStyle().setMarginRight(5, Unit.PX);
		options.getElement().getStyle().setMarginBottom(5, Unit.PX);
		options.getElement().getStyle().setHeight(23, Unit.PX);
		options.getElement().setId("options_ListBoxMVP");
		createMeasure.getElement().setId("createMeasure_Button");
		options.setName("Create");
		DOM.setElementAttribute(options.getElement(), "id", "Create Measure");
		VerticalPanel topPanel = new VerticalPanel();
		topPanel.getElement().setId("MeasureSearchFilterWidget_verticalPanel");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		loadListBoxOptions();
		horizontalPanel.add(options);
		horizontalPanel.add(createMeasure);
		topPanel.add(horizontalPanel);
		horizontalPanel.setStylePrimaryName("searchWidget");
		resetFilter();
		
		// All composites must call initWidget() in their constructors.
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
	
	/** Method to Reset check box to default state. */
	public final void resetFilter() {
		
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
