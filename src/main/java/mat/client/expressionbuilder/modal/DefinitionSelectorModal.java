package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.model.DefinitionModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.util.IdentifierSortUtil;
import mat.client.shared.MatContext;
import mat.shared.CQLIdentifierObject;

public class DefinitionSelectorModal extends SubExpressionBuilderModal {

	private static final String SELECT_A_DEFINITION = "Select a definition";
	private static final String SELECT_DEFINITION_PLACEHOLDER = "-- Select definition --";
	private ListBox definitionListBox;

	public DefinitionSelectorModal(ExpressionBuilderModal parent, ExpressionBuilderModel model) {
		super("Definition", parent, model);
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		display();
	}
	
	public void applyButtonClickHandler() {
		if(definitionListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("A definition is required.");
			return;
		}
		
		String definitionIdentifier = definitionListBox.getSelectedValue();
		
		DefinitionModel model = new DefinitionModel(definitionIdentifier);
		this.getModel().appendExpression(model);
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public void display() {
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}

	private Widget buildContentPanel() {
		this.getContentPanel().clear();
		
		VerticalPanel panel = new VerticalPanel();
		
		Form form = new Form();
		form.add(buildDefinitionFormGroup());
		
		panel.add(form);
		
		return panel;
	}
	
	private FormGroup buildDefinitionFormGroup() {
		FormGroup group = new FormGroup();
		FormLabel label = new FormLabel();
		label.setText(SELECT_A_DEFINITION);
		label.setTitle(SELECT_A_DEFINITION);
		
		
		definitionListBox = new ListBox();
		definitionListBox.addItem(SELECT_DEFINITION_PLACEHOLDER, SELECT_DEFINITION_PLACEHOLDER);
		List<CQLIdentifierObject> definitions = new ArrayList<>();
		definitions.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getDefinitions()));
		definitions.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getIncludedDefNames()));
		
		
		for(CQLIdentifierObject definition : definitions) {
			definitionListBox.addItem(definition.getDisplay(), definition.toString());
		}
		
		group.add(label);
		group.add(definitionListBox);
		
		return group;
	}
}
