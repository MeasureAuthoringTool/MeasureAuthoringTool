package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExistsModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;

public class ExistsBuilderModal extends SubExpressionBuilderModal {

	private BuildButtonObserver buildButtonObserver;
	private ExistsModel existsModel;

	public ExistsBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Exists", parent, parentModel, mainModel);
		existsModel = new ExistsModel();
		this.getParentModel().appendExpression(existsModel);
		buildButtonObserver = new BuildButtonObserver(this, existsModel, mainModel);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}
	
	private void onApplyButtonClick() {
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}
	
	private Widget buildContentPanel() { 
		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.RETRIEVE);
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		ExpressionTypeSelectorList selectors = new ExpressionTypeSelectorList(availableExpressionTypes, buildButtonObserver, existsModel);
		panel.add(selectors);
		
		return panel;
	}
}
