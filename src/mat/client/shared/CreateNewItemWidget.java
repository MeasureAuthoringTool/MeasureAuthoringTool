package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.shared.ConstantMessages;

/** CreateNewItemWidget.java. */
public class CreateNewItemWidget extends Composite {
	/** Search button - {@link PrimaryButton}. */
	private Button createItemButton;
	/** The options. */
	private ListBox options = new ListBox();

	/** Default Constructor. */
	@SuppressWarnings("deprecation")
	public CreateNewItemWidget(String forView) {
		
		VerticalPanel topPanel = new VerticalPanel();
		if (forView.equalsIgnoreCase("measureLib")) {
			createItemButton = new Button("Create");
			createItemButton.setType(ButtonType.PRIMARY);
			createItemButton.setSize(ButtonSize.SMALL);
			createItemButton.setTitle("Create");
			createItemButton.setPull(Pull.RIGHT);
			createItemButton.setMarginLeft(5.0);
			createItemButton.setHeight("30px");
			options.setWidth("200px");
			options.setHeight("32px");
			createItemButton.getElement().setId("createMeasure_Button");
			options.setName("Create Measure Options");
			DOM.setElementAttribute(options.getElement(), "id", "CreateMeasureWidget");
			topPanel.getElement().setId("CreateMeasureWidget_verticalPanel");
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			topPanel.getElement().setId("CreateMeasureWidget_horizontalPanel");
			loadListBoxOptions(forView);
			horizontalPanel.add(options);
			horizontalPanel.add(createItemButton);
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
					new Label("CreateWidgetDisplayedSelectOptionsFromComboBox"),
					"CreateWidgetDisplayedSelectOptionsFromComboBox");
			topPanel.add(invisibleLabel);
			topPanel.add(horizontalPanel);
			horizontalPanel.setStylePrimaryName("createNewItemWidget");
			// All composites must call initWidget() in their constructors.
			Element element = topPanel.getElement();
			element.setAttribute("aria-role", "panel");
			element.setAttribute("aria-labelledby", "CreateWidgetDisplayedSelectOptionsFromComboBox");
			element.setAttribute("aria-live", "assertive");
			element.setAttribute("aria-atomic", "true");
			element.setAttribute("aria-relevant", "all");
			element.setAttribute("role", "alert");
		} else {
			createItemButton = new Button("Create");
			createItemButton.setType(ButtonType.PRIMARY);
			createItemButton.setSize(ButtonSize.SMALL);
			createItemButton.setTitle("Create");
			createItemButton.setPull(Pull.RIGHT);
			createItemButton.setMarginLeft(5.0);
			createItemButton.setHeight("30px");
			options.setWidth("200px");
			options.setHeight("32px");

			createItemButton.getElement().setId("createCQL_Button");
			options.setName("Create CQL Options");
			DOM.setElementAttribute(options.getElement(), "id", "CreateCQLWidget");
			topPanel.getElement().setId("CreateCQLWidget_verticalPanel");
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			topPanel.getElement().setId("CreateCQLWidget_horizontalPanel");
			loadListBoxOptions(forView);
			horizontalPanel.add(options);
			horizontalPanel.add(createItemButton);
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
					new Label("CreateWidgetDisplayedSelectOptionsFromComboBox"),
					"CreateWidgetDisplayedSelectOptionsFromComboBox");
			topPanel.add(invisibleLabel);
			topPanel.add(horizontalPanel);
			horizontalPanel.setStylePrimaryName("createNewItemWidget");
			// All composites must call initWidget() in their constructors.
			Element element = topPanel.getElement();
			element.setAttribute("aria-role", "panel");
			element.setAttribute("aria-labelledby", "CreateWidgetDisplayedSelectOptionsFromComboBox");
			element.setAttribute("aria-live", "assertive");
			element.setAttribute("aria-atomic", "true");
			element.setAttribute("aria-relevant", "all");
			element.setAttribute("role", "alert");
		}
		initWidget(topPanel);
	}

	/**
	 * Gets the search button - {@link PrimaryButton}.
	 * 
	 * @return the createMeasure
	 */
	public Button getCreateItemButton() {
		return createItemButton;
	}

	/**
	 * Gets the options.
	 * 
	 * @return the options
	 */
	public ListBox getOptions() {
		return options;
	}

	/** Load list box options. */
	private void loadListBoxOptions(String forView) {
		options.clear();
		if (forView.equalsIgnoreCase("measureLib")) {
			options.addItem(ConstantMessages.DEFAULT_SELECT);
			options.addItem(ConstantMessages.CREATE_NEW_MEASURE);
			options.addItem(ConstantMessages.CREATE_NEW_VERSION);
			options.addItem(ConstantMessages.CREATE_NEW_DRAFT);
		} else if(forView.equalsIgnoreCase("cqlLib")){
			options.addItem(ConstantMessages.DEFAULT_SELECT);
			options.addItem(ConstantMessages.CREATE_NEW_CQL);
			options.addItem(ConstantMessages.CREATE_NEW_CQL_VERSION);
			options.addItem(ConstantMessages.CREATE_NEW_CQL_DRAFT);
		}
	}

	/**
	 * Sets the search button - {@link PrimaryButton}.
	 * 
	 * @param createMeasure
	 *            the createMeasure to set
	 */
	public void setCreateItemButton(Button createItemButton) {
		this.createItemButton = createItemButton;
	}

	/**
	 * Sets the options.
	 * 
	 * @param options
	 *            the options to set
	 */
	public void setOptions(ListBox options) {
		this.options = options;
	}
}
