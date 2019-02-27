package mat.client.expressionbuilder.modal;

import java.util.Map;
import java.util.Set;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.QuantityModel;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.shared.StringUtility;

public class QuantityBuilderModal extends SubExpressionBuilderModal {
	private QuantityModel quantityModel;
	private static Map<String, String> allCqlUnits = MatContext.get().getCqlConstantContainer().getCqlUnitMap();
	
	public QuantityBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Quantity", parent, parentModel, mainModel);
		quantityModel = new QuantityModel();
		this.setClosable(false);
		this.setRemoveOnHide(true);
		this.setCQLPanelVisible(false);
		this.getElement().getStyle().setZIndex(9999);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}

	private void onApplyButtonClick() {
		if(StringUtility.isEmptyOrNull(quantityModel.getQuantity())) {
			this.getErrorAlert().createAlert("Value is required.");
			return;
		}
		
		this.getParentModel().appendExpression(quantityModel);
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
		
		CustomQuantityTextBox quantityTextBox = new CustomQuantityTextBox(30);
		quantityTextBox.clear();
		quantityTextBox.setWidth("18em");
		quantityTextBox.getElement().setId("Quantity_TextBox");
		quantityTextBox.addValueChangeHandler(event -> textChanged(quantityTextBox.getValue()));
		
		FormLabel quantityLabel = new FormLabel();
		quantityLabel.setText("What is your value?");
		quantityLabel.setTitle("Select Quantity");
		quantityLabel.setStyleName("attr-Label");
		quantityLabel.setFor("Qauntity_TextBox");
		
		FormGroup quantityFormGroup = new FormGroup();
		quantityFormGroup.add(quantityLabel);
		quantityFormGroup.add(quantityTextBox);
		panel.add(quantityFormGroup);
		
		ListBoxMVP unitsListBox = new ListBoxMVP();
		Set<String> allUnits = allCqlUnits.keySet();
		for(String unit : allUnits) {
			unitsListBox.addItem(unit, unit);
		}
		
		unitsListBox.setVisibleItemCount(1);
		unitsListBox.setStyleName("form-control");
		unitsListBox.getElement().setId("Units_listBox");
		unitsListBox.addChangeHandler(event -> listBoxChanged(unitsListBox.getSelectedValue()));
		
		FormGroup unitFormGroup = new FormGroup();
		FormLabel unitsLabel = new FormLabel();
		unitsLabel.setText("Choose a unit to go with your quantity. (optional)");
		unitsLabel.setTitle("Select Units");
		unitsLabel.setStyleName("attr-Label");
		unitsLabel.setFor("Units_listBox");
		
		unitFormGroup.clear();
		unitFormGroup.add(unitsLabel);
		unitFormGroup.add(unitsListBox);
		panel.add(unitFormGroup);
		
		return panel;
	}

	private void listBoxChanged(String units) {
		quantityModel.setUnits(units);
	}

	private void textChanged(String quantity) {
		quantityModel.setQuantity(quantity);
	}
}
