package mat.client.expressionbuilder.observer;

import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.modal.DefinitionSelectorModal;
import mat.client.expressionbuilder.modal.ExpressionBuilderModal;
import mat.client.expressionbuilder.modal.RetrieveBuilderModal;
import mat.client.expressionbuilder.model.AndModel;
import mat.client.expressionbuilder.model.ExceptModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.IntersectModel;
import mat.client.expressionbuilder.model.OrModel;
import mat.client.expressionbuilder.model.UnionModel;

public class BuildButtonObserver {
	private ExpressionBuilderModal parent;
	private ExpressionBuilderModel model;
	
	public BuildButtonObserver(ExpressionBuilderModal parent, ExpressionBuilderModel model) {
		this.model = model;
		this.parent = parent;
	}

	public void onBuildButtonClick(String expression, String operator) {
		if(operator != null && !operator.isEmpty()) {
			this.model.appendExpression(operatorModel(operator));
		}
		
		if(expression.equals(ExpressionType.RETRIEVE.getValue())) {
			ExpressionBuilderModal retrieveModal = new RetrieveBuilderModal(this.parent, this.model);
			retrieveModal.show();
		}
		
		else if(expression.equals(ExpressionType.DEFINITION.getValue())) {
			ExpressionBuilderModal definitionModal = new DefinitionSelectorModal(this.parent, this.model);
			definitionModal.show();
		}
	}
	
	private IExpressionBuilderModel operatorModel(String operator) {
		if(operator.equals(OperatorType.UNION.getValue())) {
			return new UnionModel();
		} else if(operator.equals(OperatorType.EXCEPT.getValue())) {
			return new ExceptModel();
		} else if(operator.equals(OperatorType.INTERSECT.getValue())) {
			return new IntersectModel();
		} else if(operator.equals(OperatorType.AND.getValue())) {
			return new AndModel();
		} else if(operator.equals(OperatorType.OR.getValue())) {
			return new OrModel();
		}
		
		return null;
	}
}
