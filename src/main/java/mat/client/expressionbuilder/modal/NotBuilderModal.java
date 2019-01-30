package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.NotModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;

public class NotBuilderModal extends SubExpressionBuilderModal {

	private NotModel notModel;
	private BuildButtonObserver buildButtonObserver;

	public NotBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Negation (not)", parent, parentModel, mainModel);
		
		notModel = new NotModel();
		this.getParentModel().appendExpression(notModel);
		buildButtonObserver = new BuildButtonObserver(this, notModel, mainModel);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}

	private void onApplyButtonClick() {
		
		if(notModel.getChildModels().size() < 1) {
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
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.EXISTS);
		
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		availableOperatorTypes.addAll(OperatorTypeUtil.getBooleanOperators());
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		ExpressionTypeSelectorList selectors = new ExpressionTypeSelectorList(availableExpressionTypes, availableOperatorTypes, buildButtonObserver, notModel);
		panel.add(selectors);
		
		return panel;
	}

}
