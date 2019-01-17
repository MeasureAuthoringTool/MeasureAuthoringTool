package mat.client.expressionbuilder.modal;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.OperatorModel;

public abstract class SubExpressionBuilderModal extends ExpressionBuilderModal {
	private Button cancelButton;
	private Button applyButton;
	private ExpressionBuilderModal parent;
	
	public SubExpressionBuilderModal(String title, ExpressionBuilderModal parent, ExpressionBuilderModel model) {
		super(title, model);
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
		return cancelButton;
	}
	
	private Button buildApplyButton() {
		applyButton = new Button();
		applyButton.setText("Apply");
		applyButton.setTitle("Apply");
		applyButton.setType(ButtonType.PRIMARY);
		applyButton.setPull(Pull.RIGHT);
		applyButton.setSize(ButtonSize.LARGE);
		return applyButton;
	}
	
	private void onCancelButtonClick() {		
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public abstract void display();
}
