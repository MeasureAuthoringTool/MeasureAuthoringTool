package mat.client.expressionbuilder.component;

import java.util.Map;
import java.util.Set;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;

public class QuantityWidget extends Composite {
	private static Map<String, String> allCqlUnits = MatContext.get().getCqlConstantContainer().getCqlUnitMap();
	private ListBoxMVP unitsListBox;
	private CustomQuantityTextBox quantityTextBox;
	
	public QuantityWidget() {
		initWidget(buildQuantityWidget());
	}

	private VerticalPanel buildQuantityWidget() {
		VerticalPanel panel = new VerticalPanel();
		quantityTextBox = new CustomQuantityTextBox(30);
		quantityTextBox.clear();
		quantityTextBox.setWidth("18em");
		quantityTextBox.getElement().setId("Quantity_TextBox");
		
		FormLabel quantityLabel = new FormLabel();
		quantityLabel.setText("What is your value?");
		quantityLabel.setTitle("Select Quantity");
		quantityLabel.setStyleName("attr-Label");
		quantityLabel.setFor("Qauntity_TextBox");
		
		FormGroup quantityFormGroup = new FormGroup();
		quantityFormGroup.add(quantityLabel);
		quantityFormGroup.add(quantityTextBox);
		panel.add(quantityFormGroup);
		
		unitsListBox = new ListBoxMVP();
		Set<String> allUnits = allCqlUnits.keySet();
		for(String unit : allUnits) {
			unitsListBox.addItem(unit, unit);
		}
		
		unitsListBox.setVisibleItemCount(1);
		unitsListBox.setStyleName("form-control");
		unitsListBox.getElement().setId("Units_listBox");
		
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
	
	public ListBoxMVP getUnitsListBox() {
		return unitsListBox;
	}

	public void setUnitsListBox(ListBoxMVP unitsListBox) {
		this.unitsListBox = unitsListBox;
	}
	
	public CustomQuantityTextBox getQuantityTextBox() {
		return quantityTextBox;
	}

	public void setQuantityTextBox(CustomQuantityTextBox quantityTextBox) {
		this.quantityTextBox = quantityTextBox;
	}
}
