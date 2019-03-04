package mat.client.expressionbuilder.component;

import java.util.Map;
import java.util.Set;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;

import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;

public class QuantityWidget extends Composite {
	private static Map<String, String> allCqlUnits = MatContext.get().getCqlConstantContainer().getCqlUnitMap();
	private ListBoxMVP unitsListBox;
	private CustomQuantityTextBox quantityTextBox;
	private static String WHAT_IS_YOUR_VALUE = "What is your value?";
	private static String SELECT_UNIT = "--Select unit--";
	
	public QuantityWidget() {
		initWidget(buildQuantityWidget());
	}

	private Panel buildQuantityWidget() {
		Grid grid = new Grid(1, 2);
		quantityTextBox = new CustomQuantityTextBox(30);
		quantityTextBox.getElement().setId("Quantity_TextBox");
		
		FormLabel quantityLabel = new FormLabel();
		quantityLabel.setText(WHAT_IS_YOUR_VALUE);
		quantityLabel.setTitle(WHAT_IS_YOUR_VALUE);
		quantityLabel.setFor("Quantity_TextBox");
		
		FormGroup quantityFormGroup = new FormGroup();
		quantityFormGroup.add(quantityLabel);
		quantityFormGroup.add(quantityTextBox);
		grid.setWidget(0, 0, quantityFormGroup);
		
		unitsListBox = new ListBoxMVP();
		unitsListBox.addItem(SELECT_UNIT, "");
		Set<String> allUnits = allCqlUnits.keySet();
		for(String unit : allUnits) {
			if(unit.equals(MatContext.PLEASE_SELECT)) {
				continue;
			}
			unitsListBox.addItem(unit, unit);
		}
		
		unitsListBox.setStyleName("form-control");
		unitsListBox.getElement().setId("Units_listBox");
		
		FormGroup unitFormGroup = new FormGroup();
		FormLabel unitsLabel = new FormLabel();
		unitsLabel.setText("Choose a unit to go with your quantity. (optional)");
		unitsLabel.setTitle("Select Unit");
		unitsLabel.setFor("Units_listBox");

		unitFormGroup.add(unitsLabel);
		unitFormGroup.add(unitsListBox);
		grid.setWidget(0, 1, unitFormGroup);
		grid.setWidth("100%");
		grid.getCellFormatter().setStyleName(0, 0, "quantityWidgetCellLeft");
		grid.getCellFormatter().setStyleName(0, 1, "quantityWidgetCellRight");
		return grid;
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
