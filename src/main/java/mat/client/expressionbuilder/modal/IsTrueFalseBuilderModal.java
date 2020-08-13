package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IsTrueFalseModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.ListBoxMVP;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

public class IsTrueFalseBuilderModal extends SubExpressionBuilderModal {

	private static final String IS_NOT_FALSE = "is not false";
	private static final String IS_NOT_TRUE = "is not true";
	private static final String IS_FALSE = "is false";
	private static final String IS_TRUE = "is true";
	private static final String SELECT_A_TRUE_FALSE_STATEMENT_PLACEHOLDER = "-- Select a true/false statement --";
	private static final String CHOOSE_A_TRUE_FALSE_STATEMENT = "Choose a true/false statement.";
	private IsTrueFalseModel isTrueFalseModel;
	private BuildButtonObserver buildButtonObserver;
	private ListBoxMVP isTrueFalseListBox;
	private int selectedIndex;

	public IsTrueFalseBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("True/False (is true/false)", parent, parentModel, mainModel);
		isTrueFalseModel = new IsTrueFalseModel(parentModel);
		this.getParentModel().appendExpression(isTrueFalseModel);
		buildButtonObserver = new BuildButtonObserver(this, isTrueFalseModel, mainModel);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}

	private void onApplyButtonClick() {
		if(isTrueFalseModel.getChildModels().isEmpty() || isTrueFalseListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("All fields are required.");
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
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.COMPARISON);
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.EXISTS);
		availableExpressionTypes.add(ExpressionType.FUNCTION);
		availableExpressionTypes.add(ExpressionType.IN);
		availableExpressionTypes.add(ExpressionType.NOT);
		availableExpressionTypes.add(ExpressionType.IS_NULL_NOT_NULL);
		availableExpressionTypes.add(ExpressionType.TIMING);
		
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		
		String label = "What type of expression would you like to use for your true or false statement?";
		ExpressionTypeSelectorList selectors = 
				new ExpressionTypeSelectorList(
						availableExpressionTypes, availableOperatorTypes, QueryFinderHelper.findAliasNames(this.isTrueFalseModel),
						buildButtonObserver, isTrueFalseModel, label, this);
		
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
		
		isTrueFalseListBox = new ListBoxMVP();
		isTrueFalseListBox.insertItem(SELECT_A_TRUE_FALSE_STATEMENT_PLACEHOLDER, SELECT_A_TRUE_FALSE_STATEMENT_PLACEHOLDER, SELECT_A_TRUE_FALSE_STATEMENT_PLACEHOLDER);
		isTrueFalseListBox.insertItem(IS_TRUE, IS_TRUE, IS_TRUE);
		isTrueFalseListBox.insertItem(IS_FALSE, IS_FALSE, IS_FALSE);
		isTrueFalseListBox.insertItem(IS_NOT_TRUE, IS_NOT_TRUE, IS_NOT_TRUE);
		isTrueFalseListBox.insertItem(IS_NOT_FALSE, IS_NOT_FALSE, IS_NOT_FALSE);

		isTrueFalseListBox.setSelectedIndex(selectedIndex);
		isTrueFalseListBox.addChangeHandler(event -> onIsTrueFalseListBoxChange(isTrueFalseListBox.getSelectedIndex()));
		
		isTrueFalseFormGroup.add(isTrueFalseFormLabel);
		isTrueFalseFormGroup.add(isTrueFalseListBox);
		
		return isTrueFalseFormGroup;
	}

	private void onIsTrueFalseListBoxChange(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		
		if(selectedIndex == 0) {
			isTrueFalseModel.setOperatorText("");
		} else  {
			isTrueFalseModel.setOperatorText(this.isTrueFalseListBox.getSelectedValue());
		}
		
		
		this.updateCQLDisplay();
	}

}
