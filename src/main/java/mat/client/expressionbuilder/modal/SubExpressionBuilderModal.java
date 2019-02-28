package mat.client.expressionbuilder.modal;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.expressionbuilder.model.ComparisonModel;
import mat.client.expressionbuilder.model.ComputationModel;
import mat.client.expressionbuilder.model.ExistsModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.FunctionModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.IntervalModel;
import mat.client.expressionbuilder.model.AttributeModel;
import mat.client.expressionbuilder.model.IsNullModel;
import mat.client.expressionbuilder.model.IsTrueFalseModel;
import mat.client.expressionbuilder.model.MembershipInModel;
import mat.client.expressionbuilder.model.NotModel;
import mat.client.expressionbuilder.model.OperatorModel;
import mat.client.expressionbuilder.model.QueryModel;
import mat.client.expressionbuilder.model.TimeBoundaryModel;

public abstract class SubExpressionBuilderModal extends ExpressionBuilderModal {
	private Button cancelButton;
	private Button applyButton;
	private ExpressionBuilderModal parent;
	
	public SubExpressionBuilderModal(
			String title, 
			ExpressionBuilderModal parent, 
			ExpressionBuilderModel parentModel, 
			ExpressionBuilderModel mainModel) {
		super(parent.getModalTitle() + " > " + title, parentModel, mainModel);
		this.parent = parent;
		this.getFooter().add(buildFooter());
	}
		
	public Button getCancelButton() {
		return cancelButton;
	}

	public Button getApplyButton() {
		return applyButton;
	}
	
	public ExpressionBuilderModal getExpressionBuilderParent() {
		return parent;
	}
	
	private HorizontalPanel buildFooter() {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
				
		panel.add(buildCancelButton());
		panel.add(buildApplyButton());
		
		return panel;
	}
	
	private Button buildCancelButton() {
		cancelButton = new Button();
		cancelButton.setText("Cancel");
		cancelButton.setTitle("Cancel");
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.setSize(ButtonSize.LARGE);
		cancelButton.addClickHandler(event -> onCancelButtonClick());
		cancelButton.getElement().setAttribute("aria-label", "Clicking this button will cancel building the current expression and take you to the previous page");
		return cancelButton;
	}
	
	private Button buildApplyButton() {
		applyButton = new Button();
		applyButton.setText("Apply");
		applyButton.setTitle("Apply");
		applyButton.setType(ButtonType.PRIMARY);
		applyButton.setPull(Pull.RIGHT);
		applyButton.setSize(ButtonSize.LARGE);
		applyButton.getElement().setAttribute("aria-label", "Clicking this button will save your changes, apply the CQL to your expression, and take you to the previous page.");
		return applyButton;
	}
	
	protected void onCancelButtonClick() {		
		if(!this.getParentModel().getChildModels().isEmpty()) {
			int size = this.getParentModel().getChildModels().size() - 1;
			IExpressionBuilderModel lastModel = this.getParentModel().getChildModels().get(size);
			
			if(lastModel instanceof ExistsModel || 
					lastModel instanceof NotModel ||
					lastModel instanceof IsNullModel ||
					lastModel instanceof IsTrueFalseModel ||
					lastModel instanceof ComparisonModel ||
					lastModel instanceof IntervalModel ||
					lastModel instanceof QueryModel ||
					lastModel instanceof MembershipInModel ||
					lastModel instanceof ComputationModel ||
					lastModel instanceof TimeBoundaryModel ||
					lastModel instanceof AttributeModel ||
					lastModel instanceof FunctionModel) {
				this.getParentModel().getChildModels().remove(size);
				int newSize = this.getParentModel().getChildModels().size() - 1;
				
				if(newSize > 0) {
					lastModel = this.getParentModel().getChildModels().get(newSize);
					size = newSize;
				}
			}
			
			if(lastModel instanceof OperatorModel) {
				this.getParentModel().getChildModels().remove(size);
			}
		}		
		
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public abstract void display();
}
