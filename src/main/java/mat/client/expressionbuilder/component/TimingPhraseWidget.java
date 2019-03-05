package mat.client.expressionbuilder.component;

import java.util.List;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.constant.TimingOperator;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.QuantityModel;
import mat.client.expressionbuilder.model.TimingOperatorModel;
import mat.client.shared.ListBoxMVP;

public class TimingPhraseWidget extends Composite {

	private List<TimingOperator> timings;
	private String label;
	private String placeholder;
	private ListBoxMVP timingListBox;
	private ExpressionBuilderModel parent;
	private boolean isOptional;
	private QuantityWidget quantityWidget;

	public TimingPhraseWidget(List<TimingOperator> timings, String label, String placeholder, ExpressionBuilderModel parent, boolean isOptional) {
		this.timings = timings;
		this.label = label;
		this.placeholder = placeholder;
		this.parent = parent;
		this.isOptional = isOptional;
		initWidget(buildContent());
	}
	
	public boolean isOptional() {
		return isOptional;
	}

	public ExpressionBuilderModel getValue() {
		if(timingListBox.getSelectedIndex() != 0) {
			String timing = timingListBox.getSelectedValue();
			
			if(isQuantity(timingListBox.getSelectedValue())) {
				String value = quantityWidget.getValueTextBox().getText();				
				String unit = "";
				if(quantityWidget.getUnitsListBox().getSelectedIndex() != 0) {
					unit = quantityWidget.getUnitsListBox().getSelectedValue();
				}
				
				QuantityModel quantity = new QuantityModel(null);
				quantity.setQuantity(value);
				quantity.setUnit(unit);
				
				timing = timing.replace("quantity", quantity.getCQL(""));
			} 
			
			return new TimingOperatorModel(parent, timing, timings.get(timingListBox.getSelectedIndex() - 1));
		}
		
		return null;
	}
	
	public boolean isComplete() {
		if(isQuantity(timingListBox.getSelectedValue())) {
			String value = quantityWidget.getValueTextBox().getText();
			return !value.isEmpty();
		} else {
			return timingListBox.getSelectedIndex() != 0;
		}
	}
	
	private Widget buildContent() {
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("50%");
		FormGroup group = new FormGroup();
		group.setWidth("50");
		
		FormLabel formLabel = new FormLabel();
		String label = this.label;
		
		if(this.isOptional) {
			label = label + " (Optional)";
		}
		
		formLabel.setText(label);
		formLabel.setTitle(label);
		
		timingListBox = new ListBoxMVP();
		timingListBox.insertItem(placeholder, placeholder);		
		timings.forEach(t -> timingListBox.insertItem(t.getDisplayName(), t.getDisplayName()));
		
		timingListBox.addChangeHandler(event -> {
			String selectedValue = timingListBox.getSelectedValue();
			if(isQuantity(selectedValue)) {
				quantityWidget.setVisible(true);
			} else {
				quantityWidget.setVisible(false);
			}
		});
		
		group.add(formLabel);
		group.add(timingListBox);
		panel.add(group);
		panel.add(buildQuantityPanel());
		
		return panel;
	}
	
	private boolean isQuantity(String selectedValue) {
		return selectedValue.equals(TimingOperator.LESS_THAN_QUANTITY.getDisplayName()) || 
				selectedValue.equals(TimingOperator.MORE_THAN_QUANTITY.getDisplayName()) ||
				selectedValue.equals(TimingOperator.QUANTITY_OR_MORE.getDisplayName()) ||
				selectedValue.equals(TimingOperator.QUANTITY_OR_LESS.getDisplayName()) ||
				selectedValue.equals(TimingOperator.QUANTITY_OF.getDisplayName());
	}
	
	private Widget buildQuantityPanel() {
		quantityWidget = new QuantityWidget();
		quantityWidget.getElement().setAttribute("style", "margin-left: 20px;");
		quantityWidget.setVisible(false);
		return quantityWidget;
	}
	
	public void addChangeHandler(ChangeHandler handler) {
		this.timingListBox.addChangeHandler(handler);
	}
	
	public void addQuantityUnitChangeHandler(ChangeHandler handler) {
		this.quantityWidget.getUnitsListBox().addChangeHandler(handler);
	}
	
	public void addQuantityValueChangeHandler(ValueChangeHandler handler) {
		this.quantityWidget.getValueTextBox().addValueChangeHandler(handler);
	}
}
