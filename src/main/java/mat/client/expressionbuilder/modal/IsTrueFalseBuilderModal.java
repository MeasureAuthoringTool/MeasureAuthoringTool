package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IsTrueFalseModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;

public class IsTrueFalseBuilderModal extends SubExpressionBuilderModal {

	private static final String IS_NOT_FALSE = "is not false";
	private static final String IS_NOT_TRUE = "is not true";
	private static final String IS_FALSE = "is false";
	private static final String IS_TRUE = "is true";
	private static final String SELECT_A_TRUE_FALSE_STATEMENT_PLACEHOLDER = "-- Select a true/false statement --";
	private static final String CHOOSE_A_TRUE_FALSE_STATEMENT = "Choose a true/false statement.";
	private IsTrueFalseModel isTrueFalseModel;
	private BuildButtonObserver buildButtonObserver;
	private ListBox isTrueFalseListBox;
	private int selectedIndex;

	public IsTrueFalseBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("True/False (is true/false)", parent, parentModel, mainModel);
		isTrueFalseModel = new IsTrueFalseModel();
		buildButtonObserver = new BuildButtonObserver(this, isTrueFalseModel, mainModel);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}

	private void onApplyButtonClick() {
		if(isTrueFalseModel.getChildModels().isEmpty() || isTrueFalseListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("All fields are required.");
			return;
		}
		
		isTrueFalseModel.setOperatorText(this.isTrueFalseListBox.getSelectedValue());
		this.getParentModel().appendExpression(isTrueFalseModel);
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
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		
		String label = "What type of expression would you like to use for your true or false statement?";
		ExpressionTypeSelectorList selectors = 
				new ExpressionTypeSelectorList(availableExpressionTypes, availableOperatorTypes, 
						buildButtonObserver, isTrueFalseModel, label);
		
		panel.add(buildIsTrueFalseFormGroup());		
		panel.add(selectors);	
		
		return panel;
	}
	
	private FormGroup buildIsTrueFalseFormGroup() {
		FormGroup isTrueFalseFormGroup = new FormGroup();		
		isTrueFalseFormGroup.setWidth("36%");

		FormLabel isTrueFalseFormLabel = new FormLabel();
		isTrueFalseFormLabel.setText(CHOOSE_A_TRUE_FALSE_STATEMENT);
		isTrueFalseFormLabel.setTitle(CHOOSE_A_TRUE_FALSE_STATEMENT);
		
		isTrueFalseListBox = new ListBox();
		isTrueFalseListBox.addItem(SELECT_A_TRUE_FALSE_STATEMENT_PLACEHOLDER, SELECT_A_TRUE_FALSE_STATEMENT_PLACEHOLDER);
		isTrueFalseListBox.addItem(IS_TRUE, IS_TRUE);
		isTrueFalseListBox.addItem(IS_FALSE, IS_FALSE);
		isTrueFalseListBox.addItem(IS_NOT_TRUE, IS_NOT_TRUE);
		isTrueFalseListBox.addItem(IS_NOT_FALSE, IS_NOT_FALSE);

		isTrueFalseListBox.setSelectedIndex(selectedIndex);
		isTrueFalseListBox.addChangeHandler(event -> onIsTrueFalseListBoxChange(isTrueFalseListBox.getSelectedIndex()));
		
		isTrueFalseFormGroup.add(isTrueFalseFormLabel);
		isTrueFalseFormGroup.add(isTrueFalseListBox);
		
		return isTrueFalseFormGroup;
	}

	private void onIsTrueFalseListBoxChange(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

}
