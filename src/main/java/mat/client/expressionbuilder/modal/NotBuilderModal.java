package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.NotModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;
import mat.client.expressionbuilder.util.QueryFinderHelper;

import java.util.ArrayList;
import java.util.List;

public class NotBuilderModal extends SubExpressionBuilderModal {

	private NotModel notModel;
	private BuildButtonObserver buildButtonObserver;

	public NotBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Negation (not)", parent, parentModel, mainModel);
		
		notModel = new NotModel(parentModel);
		this.getParentModel().appendExpression(notModel);
		buildButtonObserver = new BuildButtonObserver(this, notModel, mainModel);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}

	private void onApplyButtonClick() {
		
		if(notModel.getChildModels().isEmpty()) {
			this.getErrorAlert().createAlert("Building an expression is required.");
			return;
		}
		
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}
	
	public Widget buildContentPanel() {
		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.COMPARISON);
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.EXISTS);
		availableExpressionTypes.add(ExpressionType.FUNCTION);
		availableExpressionTypes.add(ExpressionType.IN);
		availableExpressionTypes.add(ExpressionType.IS_NULL_NOT_NULL);
		availableExpressionTypes.add(ExpressionType.TIMING);
		availableExpressionTypes.add(ExpressionType.IS_TRUE_FALSE);
		
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		availableOperatorTypes.addAll(OperatorTypeUtil.getBooleanOperators());
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		String label = "What type of expression would you like to negate?";
		ExpressionTypeSelectorList selectors = 
			new ExpressionTypeSelectorList(
					availableExpressionTypes, availableOperatorTypes, QueryFinderHelper.findAliasNames(this.notModel),
					buildButtonObserver, notModel, label, this);
		panel.add(selectors);
		
		return panel;
	}

}
