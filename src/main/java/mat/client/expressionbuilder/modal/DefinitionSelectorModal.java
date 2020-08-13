package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.model.DefinitionModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.util.ExpressionTypeUtil;
import mat.client.expressionbuilder.util.IdentifierSortUtil;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.shared.CQLIdentifierObject;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

public class DefinitionSelectorModal extends SubExpressionBuilderModal {

	private static final String SELECT_A_DEFINITION = "Select a definition";
	private static final String SELECT_DEFINITION_PLACEHOLDER = "-- Select definition --";
	private ListBoxMVP definitionListBox;

	public DefinitionSelectorModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel,
			boolean isFirstSelection) {
		super("Definition", parent, parentModel, mainModel);
		this.setHideOtherModals(false);
		this.setClosable(false);
		this.setRemoveOnHide(true);
		this.setWidth("35%");
		this.setCQLPanelVisible(false);
		this.getElement().getStyle().setZIndex(9999);
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		this.setFirstSelection(isFirstSelection);
		display();
	}
	
	public void applyButtonClickHandler() {
		if(definitionListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("A definition is required.");
			return;
		}
		
		String definitionIdentifier = definitionListBox.getSelectedValue();
		
		DefinitionModel model = new DefinitionModel(definitionIdentifier, this.getParentModel());
		this.getParentModel().appendExpression(model);
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
		panel.setStyleName("selectorsPanel");
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
		
		definitionListBox = new ListBoxMVP();
		definitionListBox.insertItem(SELECT_DEFINITION_PLACEHOLDER, SELECT_DEFINITION_PLACEHOLDER);
		List<CQLIdentifierObject> definitions = new ArrayList<>();
		
		if (this.isFirstSelection()) {
			definitions.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getDefinitions()));
			definitions.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getIncludedDefNames()));
		} else {			
			definitions.addAll(ExpressionTypeUtil.getDefinitionsBasedOnReturnType(this.getParentModel().getChildModels().get(0).getType()));
		}

		for(CQLIdentifierObject definition : definitions) {
			definitionListBox.insertItem(definition.getDisplay().substring(0, 90), definition.toString(), definition.getDisplay());
		}
		
		group.add(label);
		group.add(definitionListBox);
		
		return group;
	}
}
