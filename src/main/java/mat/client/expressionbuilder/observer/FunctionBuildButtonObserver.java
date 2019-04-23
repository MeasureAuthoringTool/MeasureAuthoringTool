package mat.client.expressionbuilder.observer;

import mat.client.expressionbuilder.modal.ExpressionBuilderModal;
import mat.client.expressionbuilder.modal.FunctionArgumentsBuilderModal;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.FunctionModel;
import mat.shared.cql.model.FunctionSignature;

public class FunctionBuildButtonObserver {

	private ExpressionBuilderModal parentModal;
	private FunctionModel functionModel;
	private FunctionSignature functionSignature;
	private ExpressionBuilderModel mainModel;

	public FunctionBuildButtonObserver(ExpressionBuilderModal parentModal, FunctionModel functionModel, ExpressionBuilderModel mainModel, FunctionSignature functionSignature) {
		this.mainModel = mainModel;
		this.setParentModal(parentModal);
		this.setFunctionModel(functionModel);
		this.setFunctionSignature(functionSignature);
	}
	
	public void onBuildButtonClick() {
		FunctionArgumentsBuilderModal modal = new FunctionArgumentsBuilderModal(functionSignature, parentModal, functionModel, mainModel);
		modal.show();	
	}

	public ExpressionBuilderModal getParentModal() {
		return parentModal;
	}

	public void setParentModal(ExpressionBuilderModal parentModal) {
		this.parentModal = parentModal;
	}

	public FunctionModel getFunctionModel() {
		return functionModel;
	}

	public void setFunctionModel(FunctionModel functionModel) {
		this.functionModel = functionModel;
	}

	public FunctionSignature getFunctionSignature() {
		return functionSignature;
	}

	public void setFunctionSignature(FunctionSignature functionSignature) {
		this.functionSignature = functionSignature;
	}
}
