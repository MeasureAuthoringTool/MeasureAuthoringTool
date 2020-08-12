package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.ParameterModel;
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

public class ParameterSelectorModal extends SubExpressionBuilderModal {

	private static final String SELECT_A_PARAMETER = "Select a parameter";
	private static final String SELECT_PARAMETER_PLACEHOLDER = "-- Select parameter --";
	private ListBoxMVP parameterListBox;

	public ParameterSelectorModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel,
			boolean isFirstSelection) {
		super("Parameter", parent, parentModel, mainModel);
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
		if(parameterListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("A parameter is required.");
			return;
		}
		
		String parameterIdentifier = parameterListBox.getSelectedValue();
		
		ParameterModel model = new ParameterModel(parameterIdentifier, this.getParentModel());
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
		form.add(buildParameterFormGroup());
		
		panel.add(form);
		
		return panel;
	}
	
	private FormGroup buildParameterFormGroup() {
		FormGroup group = new FormGroup();
		FormLabel label = new FormLabel();
		label.setText(SELECT_A_PARAMETER);
		label.setTitle(SELECT_A_PARAMETER);
		
		parameterListBox = new ListBoxMVP();
		parameterListBox.insertItem(SELECT_PARAMETER_PLACEHOLDER, SELECT_PARAMETER_PLACEHOLDER);

		List<CQLIdentifierObject> parameters = new ArrayList<>();

		if (this.isFirstSelection()) {
			parameters.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getParameters()));
			parameters.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getIncludedParamNames()));
		} else {
			parameters.addAll(ExpressionTypeUtil.getParametersBasedOnReturnType(this.getParentModel().getChildModels().get(0).getType()));
		}
		
		for(CQLIdentifierObject parameter : parameters) {
			parameterListBox.insertItem(parameter.getDisplay().substring(0, 90), parameter.toString(), parameter.getDisplay());
		}
		
		group.add(label);
		group.add(parameterListBox);
		
		return group;
	}
}
