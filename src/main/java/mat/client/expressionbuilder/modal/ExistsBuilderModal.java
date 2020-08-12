package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExistsModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;
import mat.client.expressionbuilder.util.QueryFinderHelper;

import java.util.ArrayList;
import java.util.List;

public class ExistsBuilderModal extends SubExpressionBuilderModal {

	private BuildButtonObserver buildButtonObserver;
	private ExistsModel existsModel;

	public ExistsBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Exists", parent, parentModel, mainModel);
		existsModel = new ExistsModel(this.getParentModel());
		this.getParentModel().appendExpression(existsModel);
		buildButtonObserver = new BuildButtonObserver(this, existsModel, mainModel);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}
	
	private void onApplyButtonClick() {
		
		if(existsModel.getChildModels().isEmpty()) {
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
	
	private Widget buildContentPanel() { 
		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		
		availableExpressionTypes.add(ExpressionType.ATTRIBUTE);		
		availableExpressionTypes.add(ExpressionType.RETRIEVE);
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.FUNCTION);
		availableExpressionTypes.add(ExpressionType.QUERY);
		
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		availableOperatorTypes.addAll(OperatorTypeUtil.getSetOperators());
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		String label = "For what type of expression would you like to determine existence?";
		ExpressionTypeSelectorList selectors = 
				new ExpressionTypeSelectorList(availableExpressionTypes, availableOperatorTypes, QueryFinderHelper.findAliasNames(this.existsModel),
						buildButtonObserver, existsModel, label, this);
		panel.add(selectors);
		
		return panel;
	}
}
