package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.QuantityWidget;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.QuantityModel;
import mat.shared.StringUtility;

public class QuantityBuilderModal extends SubExpressionBuilderModal {
	private QuantityModel quantityModel;
	private QuantityWidget quantityWidget;
	
	public QuantityBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Quantity", parent, parentModel, mainModel);
		quantityModel = new QuantityModel(parentModel);
		parentModel.appendExpression(quantityModel);
		this.setClosable(false);
		this.setRemoveOnHide(true);
		this.getElement().getStyle().setZIndex(9999);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}

	private void onApplyButtonClick() {
		if(StringUtility.isEmptyOrNull(quantityModel.getQuantity())) {
			this.getErrorAlert().createAlert("Value is required.");
			return;
		}
		
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}

	private Widget buildContentPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("quantityPanel");
		panel.setWidth("100%");
		quantityWidget = new QuantityWidget();
		quantityWidget.getValueTextBox().addValueChangeHandler(event -> textChanged(quantityWidget.getValueTextBox().getValue()));
		quantityWidget.getUnitsListBox().addChangeHandler(event -> listBoxChanged(quantityWidget.getUnitsListBox().getSelectedValue()));
		panel.add(quantityWidget);
		
		return panel;
	}

	private void listBoxChanged(String units) {
		if(quantityWidget.getUnitsListBox().getSelectedIndex() == 0) {
			quantityModel.setUnit("");
		} else {
			quantityModel.setUnit(units);
		}
		this.updateCQLDisplay();
	}

	private void textChanged(String quantity) {
		quantityModel.setQuantity(quantity);
		this.updateCQLDisplay();
	}
}
