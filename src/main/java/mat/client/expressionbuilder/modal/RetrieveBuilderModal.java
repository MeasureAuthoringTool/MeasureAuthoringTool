package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.RetrieveModel;
import mat.client.expressionbuilder.util.IdentifierSortUtil;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.shared.CQLIdentifierObject;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

public class RetrieveBuilderModal extends SubExpressionBuilderModal {

	private static final String CHOOSE_A_VALUE_SET_OR_CODE_TO_GO_WITH_YOUR_DATATYPE_OPTIONAL = "Choose a value set or code to go with your datatype (Optional)";
	private static final String CHOOSE_A_DATATYPE = "Choose a datatype";
	private static final String SELECT_DATATYPE = "-- Select datatype --";
	private static final String SELECT_VALUE_SET_OR_CODE = " -- Select value set/code --";
	
	private ListBoxMVP valuesetCodeListBox;
	private ListBoxMVP dataTypeListBox;
	
	public RetrieveBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Data Element or Retrieve", parent, parentModel, mainModel);
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		display();
	}
	
	@Override
	public void display() {
		this.getErrorAlert().clearAlert();
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
		
		RetrieveModel retrieve = new RetrieveModel(dataTypeValue, terminologyValue, this.getParentModel());
		this.getParentModel().appendExpression(retrieve);
		this.getExpressionBuilderParent().showAndDisplay();
	}

	private VerticalPanel buildContentPanel() {
		this.getContentPanel().clear();
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("36%");
		
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
				
		dataTypeListBox = new ListBoxMVP();
		dataTypeListBox.setId("chooseDataTypeListBox");
		dataTypeListBox.insertItem(SELECT_DATATYPE, SELECT_DATATYPE, SELECT_DATATYPE);
		
		List<String> dataTypes = MatContext.get().getCqlConstantContainer().getQdmDatatypeList();
		dataTypes.remove("attribute"); // we don't want to show attribute in this list
		for(String datatype : dataTypes) {
			dataTypeListBox.insertItem(datatype, datatype, datatype);
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
		
		valuesetCodeListBox = new ListBoxMVP();
		valuesetCodeListBox.setId("chooseValuesetCodeListBox");
		valuesetCodeListBox.insertItem(SELECT_VALUE_SET_OR_CODE, SELECT_VALUE_SET_OR_CODE, SELECT_VALUE_SET_OR_CODE);
		
		List<CQLIdentifierObject> terminologies = new ArrayList<>();
		
		// add valuesets and codes from parent
		terminologies.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getValuesets()));
		
		// and valuesets and codes from included libraries
		List<CQLIdentifierObject> includedValueSetAndCodesList = new ArrayList<>();
		includedValueSetAndCodesList.addAll(MatContext.get().getIncludedValueSetNames());
		includedValueSetAndCodesList.addAll(MatContext.get().getIncludedCodeNames());
		
		terminologies.addAll(IdentifierSortUtil.sortIdentifierList(includedValueSetAndCodesList));
		
		
		for(CQLIdentifierObject o : terminologies) {
			valuesetCodeListBox.insertItem(o.getDisplay().substring(0, 90), o.toString(), o.getDisplay());
		}		
		
		valuesetCodeGroup.add(valuesetCodeListBoxLabel);
		valuesetCodeGroup.add(valuesetCodeListBox);
		
		return valuesetCodeGroup;
	}
	

}
