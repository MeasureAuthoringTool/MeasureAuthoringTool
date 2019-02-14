package mat.client.expressionbuilder.observer;

import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.modal.CodeSelectorModal;
import mat.client.expressionbuilder.modal.ComparisonBuilderModal;
import mat.client.expressionbuilder.modal.DefinitionSelectorModal;
import mat.client.expressionbuilder.modal.ExistsBuilderModal;
import mat.client.expressionbuilder.modal.ExpressionBuilderModal;
import mat.client.expressionbuilder.modal.IntervalBuilderModal;
import mat.client.expressionbuilder.modal.IsNullBuilderModal;
import mat.client.expressionbuilder.modal.IsTrueFalseBuilderModal;
import mat.client.expressionbuilder.modal.NotBuilderModal;
import mat.client.expressionbuilder.modal.ParameterSelectorModal;
import mat.client.expressionbuilder.modal.QueryBuilderModal;
import mat.client.expressionbuilder.modal.RetrieveBuilderModal;
import mat.client.expressionbuilder.modal.ValuesetSelectorModal;
import mat.client.expressionbuilder.model.AndModel;
import mat.client.expressionbuilder.model.CodeModel;
import mat.client.expressionbuilder.model.ExceptModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.IntersectModel;
import mat.client.expressionbuilder.model.OrModel;
import mat.client.expressionbuilder.model.UnionModel;

public class BuildButtonObserver {
	private ExpressionBuilderModal parentModal;
	private ExpressionBuilderModel parentModel;
	private ExpressionBuilderModel mainModel;
	
	public BuildButtonObserver(ExpressionBuilderModal parentModal, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		this.parentModel = parentModel;
		this.parentModal = parentModal;
		this.mainModel = mainModel;
	}

	public void onBuildButtonClick(String expression, String operator) {
		if(operator != null && !operator.isEmpty()) {
			this.parentModel.appendExpression(operatorModel(operator));
		}
		
		if(expression.equals(ExpressionType.RETRIEVE.getValue())) {
			ExpressionBuilderModal retrieveModal = new RetrieveBuilderModal(this.parentModal, this.parentModel, this.mainModel);
			retrieveModal.show();
		}
		
		else if(expression.equals(ExpressionType.DEFINITION.getValue())) {
			ExpressionBuilderModal definitionModal = new DefinitionSelectorModal(this.parentModal, this.parentModel, this.mainModel);
			definitionModal.show();
		}
		
		else if(expression.equals(ExpressionType.EXISTS.getValue())) {
			ExpressionBuilderModal existsModal = new ExistsBuilderModal(this.parentModal, this.parentModel, this.mainModel);
			existsModal.show();
		}	
		
		else if(expression.equals(ExpressionType.NOT.getValue())) {
			ExpressionBuilderModal notModal = new NotBuilderModal(this.parentModal, this.parentModel, this.mainModel);
			notModal.show();
		} 
		
		else if(expression.equals(ExpressionType.IS_NULL_NOT_NULL.getValue())) {
			ExpressionBuilderModal isNullModal = new IsNullBuilderModal(this.parentModal, this.parentModel, this.mainModel);
			isNullModal.show();
		}
		
		else if(expression.equals(ExpressionType.IS_TRUE_FALSE.getValue())) {
			ExpressionBuilderModal isTrueFalseModal = new IsTrueFalseBuilderModal(this.parentModal, this.parentModel, this.mainModel);
			isTrueFalseModal.show();
		} 
		
		else if(expression.equals(ExpressionType.COMPARISON.getValue())) {
			ExpressionBuilderModal comparisonModal = new ComparisonBuilderModal(this.parentModal, this.parentModel, this.mainModel);
			comparisonModal.show();
		}
		
		else if(expression.equals(ExpressionType.INTERVAL.getValue())) {
			ExpressionBuilderModal intervalModal = new IntervalBuilderModal(this.parentModal, this.parentModel, this.mainModel);
			intervalModal.show();
		}
		
		else if(expression.equals(ExpressionType.QUERY.getValue())) {
			QueryBuilderModal queryModal = new QueryBuilderModal(this.parentModal, this.parentModel, this.mainModel);
			queryModal.show();
		}
		
		else if(expression.equals(ExpressionType.PARAMETER.getValue())) {
			ParameterSelectorModal parameterModal = new ParameterSelectorModal(this.parentModal, this.parentModel, this.mainModel);
			parameterModal.show();
		}
		
		else if(expression.equals(ExpressionType.VALUESET.getValue())) {
			ValuesetSelectorModal valuesetModal = new ValuesetSelectorModal(this.parentModal, this.parentModel, this.mainModel);
			valuesetModal.show();
		}
		
		else if(expression.equals(ExpressionType.CODE.getValue())) {
			CodeSelectorModal codeModal = new CodeSelectorModal(this.parentModal, this.parentModel, this.mainModel);
			codeModal.show();
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
