package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.FunctionModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.shared.cql.model.FunctionSignature;

public class FunctionArgumentsBuilderModal extends SubExpressionBuilderModal {

	private FunctionSignature functionSignature;
	private FunctionModel functionModel;

	public FunctionArgumentsBuilderModal(FunctionSignature functionSignature, ExpressionBuilderModal parent,
			FunctionModel parentModel, ExpressionBuilderModel mainModel) {
		super(functionSignature.getName() + " Arguments", parent, parentModel, mainModel);
		this.functionModel = parentModel;
		this.functionSignature = functionSignature;

		// initialize the arguments array so that we have expression builder
		// models to append to.
		List<ExpressionBuilderModel> argumentModels = new ArrayList<>();
		for (int i = 0; i < functionSignature.getArguments().size(); i++) {
			argumentModels.add(new ExpressionBuilderModel(this.getParentModel()));
		}

		functionModel.setArguments(argumentModels);

		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}

	@Override
	public void onCancelButtonClick() {
		functionModel.setArguments(new ArrayList<>());
		this.getExpressionBuilderParent().showAndDisplay();
	}

	private void onApplyButtonClick() {
		for (int i = 0; i < functionModel.getArguments().size(); i++) {
			if (functionModel.getArguments().get(i).getChildModels().isEmpty()) {
				this.getErrorAlert().createAlert("All fields are required.");
				return;
			}
		}

		// instead of going back to the parent (aka the function screen) we should go
		// back to the
		// function screen's parent.
		((SubExpressionBuilderModal) this.getExpressionBuilderParent()).getExpressionBuilderParent().showAndDisplay();
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
		panel.setStyleName("selectorsPanel");

		String label = functionSignature.getSignature() + " requires " + functionSignature.getArguments().size()
				+ " Argument(s).";
		FormLabel formLabel = new FormLabel();
		formLabel.setText(label);
		formLabel.setTitle(label);
		formLabel.setStyleName("attr-Label");

		panel.add(formLabel);
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());

		for (int i = 0; i < functionSignature.getArguments().size(); i++) {
			int argumentNumber = i + 1;
			List<ExpressionType> availableExpressionsForArgument = new ArrayList<>();
			String returnType = functionSignature.getArguments().get(i).getReturnType();
			availableExpressionsForArgument.addAll(getExpressionTypesForType(returnType));

			String argumentLabel = "Argument " + argumentNumber + " (" + returnType + ")";

			BuildButtonObserver observer = new BuildButtonObserver(this, this.functionModel.getArguments().get(i),
					this.getMainModel());
			ExpressionTypeSelectorList selector = new ExpressionTypeSelectorList(availableExpressionsForArgument,
					new ArrayList<>(), new ArrayList<>(), observer, this.functionModel.getArguments().get(i),
					argumentLabel);

			panel.add(selector);
		}

		return panel;
	}

	private List<ExpressionType> getExpressionTypesForType(String type) {
		Set<ExpressionType> expressionTypes = new HashSet<>();
		if(isListQDMType(type)) {
			type = "List<QDM>";
		} else if(isQDMType(type)) {
			type = "QDM";
		} else if(isUnkownType(type)) {
			type = "Any";
		}
	
		for (ExpressionType expressionType : ExpressionType.values()) {
			for (CQLType cqlType : expressionType.getRelevantCQLTypes()) {
				if (cqlType.getName().equalsIgnoreCase(type)) {
					expressionTypes.add(expressionType);
				}
			}
		}
					
		List<ExpressionType> expressionTypeList = new ArrayList<>(expressionTypes);
		expressionTypeList.sort((e1, e2) -> e1.getDisplayName().compareTo(e2.getDisplayName()));
		return expressionTypeList;
	}
	
	private boolean isUnkownType(String type) {
		for(CQLType cqlType : CQLType.values()) {
			if(cqlType.getName().equals(type)) {
				return false;
			}
		}
		
		return true;
		
	}
	
	private boolean isQDMType(String type) {
		boolean isQDMType = false;
		List<String> datatypes = MatContext.get().getCqlConstantContainer().getQdmDatatypeList();
		for (String d : datatypes) {
			String listType = d;

			if (listType.equalsIgnoreCase(type)) {
				isQDMType = true;
			}
		}
		
		return isQDMType;
	}

	private boolean isListQDMType(String type) {
		boolean isQDMType = false;
		List<String> datatypes = MatContext.get().getCqlConstantContainer().getQdmDatatypeList();
		for (String d : datatypes) {
			String listType = "List<\"" + d + "\">";

			if (listType.equalsIgnoreCase(type)) {
				isQDMType = true;
			}
		}

		return isQDMType;
	}
}
