package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;

import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.RetrieveModel;
import mat.client.shared.MatContext;
import mat.shared.CQLIdentifierObject;

public class RetrieveBuilderModal extends SubExpressionBuilderModal {

	private static final String CHOOSE_A_VALUE_SET_OR_CODE_TO_GO_WITH_YOUR_DATATYPE_OPTIONAL = "Choose a value set or code to go with your datatype (Optional)";
	private static final String CHOOSE_A_DATATYPE = "Choose a datatype";
	private static final String SELECT_DATATYPE = "-- Select datatype --";
	private static final String SELECT_VALUE_SET_OR_CODE = " -- Select value set/code --";
	
	private ListBox valuesetCodeListBox;
	private ListBox dataTypeListBox;
	
	public RetrieveBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel model) {
		super("Data Element or Retrieve", parent, model);
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		display();
	}
	
	@Override
	public void display() {
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}
	
	public void applyButtonClickHandler() {
		if(dataTypeListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("A datatype is required.");
			return;
		}
		
		String dataTypeValue = dataTypeListBox.getSelectedValue();
		String terminologyValue = valuesetCodeListBox.getSelectedValue();
		if(valuesetCodeListBox.getSelectedIndex() == 0) {
			terminologyValue = "";
		}
		
		RetrieveModel retrieve = new RetrieveModel(dataTypeValue, terminologyValue);
		this.getModel().appendExpression(retrieve);
		this.getExpressionBuilderParent().showAndDisplay();
	}

	private VerticalPanel buildContentPanel() {
		this.getContentPanel().clear();
		VerticalPanel panel = new VerticalPanel();
		
		Form form = new Form();
		form.add(buildDatatypeGroup());
		form.add(buildValueSetCodeGroup());
		
		panel.add(form);
		return panel;
	}

	private FormGroup buildDatatypeGroup() {
		FormGroup datatypeGroup = new FormGroup();
		FormLabel datatypeListBoxLabel = new FormLabel();
		datatypeListBoxLabel.setText(CHOOSE_A_DATATYPE);
		datatypeListBoxLabel.setText(CHOOSE_A_DATATYPE);
		datatypeListBoxLabel.setId("chooseDataTypeLabel");
		datatypeListBoxLabel.setFor("chooseDataTypeListBox");
				
		dataTypeListBox = new ListBox();
		dataTypeListBox.setId("chooseDataTypeListBox");
		dataTypeListBox.addItem(SELECT_DATATYPE, SELECT_DATATYPE);
		
		List<String> dataTypes = MatContext.get().getCqlConstantContainer().getQdmDatatypeList();
		dataTypes.remove("attribute"); // we don't want to show attribute in this list
		for(String datatype : dataTypes) {
			dataTypeListBox.addItem(datatype, datatype);
		}
		
		datatypeGroup.add(datatypeListBoxLabel);
		datatypeGroup.add(dataTypeListBox);
		
		return datatypeGroup;
	}
	
	private FormGroup buildValueSetCodeGroup() {
		FormGroup valuesetCodeGroup = new FormGroup();
		FormLabel valuesetCodeListBoxLabel = new FormLabel();
		valuesetCodeListBoxLabel.setText(CHOOSE_A_VALUE_SET_OR_CODE_TO_GO_WITH_YOUR_DATATYPE_OPTIONAL);
		valuesetCodeListBoxLabel.setTitle(CHOOSE_A_VALUE_SET_OR_CODE_TO_GO_WITH_YOUR_DATATYPE_OPTIONAL);
		valuesetCodeListBoxLabel.setId("valuesetCodeListBoxLabel");
		valuesetCodeListBoxLabel.setFor("valuesetCodeListBox");
		
		valuesetCodeListBox = new ListBox();
		valuesetCodeListBox.setId("chooseValuesetCodeListBox");
		valuesetCodeListBox.addItem(SELECT_VALUE_SET_OR_CODE, SELECT_VALUE_SET_OR_CODE);
		List<CQLIdentifierObject> valuesetCodes = new ArrayList<>();
		valuesetCodes.addAll(MatContext.get().getValuesets());
		valuesetCodes.addAll(MatContext.get().getIncludedValueSetNames());
		valuesetCodes.addAll(MatContext.get().getIncludedCodeNames());
		
		for(CQLIdentifierObject o : valuesetCodes) {
			valuesetCodeListBox.addItem(o.getDisplay(), o.getDisplay());
		}		
		
		valuesetCodeGroup.add(valuesetCodeListBoxLabel);
		valuesetCodeGroup.add(valuesetCodeListBox);
		
		return valuesetCodeGroup;
	}
}
