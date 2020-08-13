package mat.client.shared;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.shared.ConstantMessages;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

/** CreateNewItemWidget.java. */
public class CreateNewItemWidget extends Composite {
	/** createItemButton button . */
	private Button createItemButton;
	/** The options. */
	private ListBox options = new ListBox();

	/** Default Constructor. */
	public CreateNewItemWidget(String forView) {

		VerticalPanel topPanel = new VerticalPanel();
		createItemButton = new Button("Create");
		createItemButton.setType(ButtonType.PRIMARY);
		createItemButton.setSize(ButtonSize.SMALL);
		createItemButton.setTitle("Create");
		createItemButton.setPull(Pull.RIGHT);
		createItemButton.setMarginLeft(5.0);
		createItemButton.setHeight("30px");
		options.setWidth("200px");
		options.setHeight("32px");
		createItemButton.getElement().setId("createItem_Button_" + forView);
		options.setId("CreateItemWidget_" + forView);
		topPanel.getElement().setId("CreateItemWidget_verticalPanel" + forView);
		// Set Id's based on view.
		if (forView.equalsIgnoreCase("forMeasureLibrary")) {
			options.setName("Create Measure Options");
			/*
			 * DOM.setElementAttribute(options.getElement(), "id",
			 * "CreateMeasureWidget");
			 */

		} else {
			options.setName("Create CQL Options");
			/*
			 * DOM.setElementAttribute(options.getElement(), "id",
			 * "CreateCQLWidget");
			 */
		}
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.getElement().setId("CreateItemeWidget_horizontalPanel" + forView);
		
		loadListBoxOptions(forView);
		horizontalPanel.add(options);
		horizontalPanel.add(createItemButton);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
				new Label("CreateWidgetDisplayedSelectOptionsFromComboBox"+forView),
				"CreateWidgetDisplayedSelectOptionsFromComboBox"+forView);
		topPanel.add(invisibleLabel);
		topPanel.add(horizontalPanel);
		horizontalPanel.setStylePrimaryName("createNewItemWidget");
		// All composites must call initWidget() in their constructors.
		Element element = topPanel.getElement();
		element.setAttribute("aria-role", "panel");
		element.setAttribute("aria-labelledby", "CreateWidgetDisplayedSelectOptionsFromComboBox"+forView);
		element.setAttribute("aria-live", "assertive");
		element.setAttribute("aria-atomic", "true");
		element.setAttribute("aria-relevant", "all");
		element.setAttribute("role", "alert");
		initWidget(topPanel);
	}

	/**
	 * Gets the create Item Button .
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
		if (forView.equalsIgnoreCase("forMeasureLibrary")) {
			options.addItem(ConstantMessages.DEFAULT_SELECT);
			options.addItem(ConstantMessages.CREATE_NEW_MEASURE);
			/*options.addItem(ConstantMessages.CREATE_NEW_VERSION);
			options.addItem(ConstantMessages.CREATE_NEW_DRAFT);*/
		} else if (forView.equalsIgnoreCase("forCqlLibrary")) {
			options.addItem(ConstantMessages.DEFAULT_SELECT);
			options.addItem(ConstantMessages.CREATE_NEW_CQL);
			/*options.addItem(ConstantMessages.CREATE_NEW_CQL_VERSION);
			options.addItem(ConstantMessages.CREATE_NEW_CQL_DRAFT);*/
		}
	}

	/**
	 * Sets the create Item Button .
	 * 
	 * @param createItemButton
	 *            the createItemButton to set
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
