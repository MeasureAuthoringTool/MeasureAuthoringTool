package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IsNullModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.ListBoxMVP;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

public class IsNullBuilderModal extends SubExpressionBuilderModal {

	private static final String IS_NOT_NULL = "is not null";
	private static final String IS_NULL = "is null";
	private static final String SELECT_A_NULL_STATEMENT_PLACEHOLDER = "-- Select a null statement --";
	private static final String CHOOSE_A_NULL_STATEMENT = "Choose a null statement.";
	private IsNullModel isNullModel;
	private BuildButtonObserver buildButtonObserver;
	private ListBoxMVP isNullNotNullListBox;
	private int selectedIndex = 0;
	
	public IsNullBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("Null (is null/not null)", parent, parentModel, mainModel);
		isNullModel = new IsNullModel(parentModel);
		this.getParentModel().appendExpression(isNullModel);
		buildButtonObserver = new BuildButtonObserver(this, isNullModel, mainModel);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
	}

	private void onApplyButtonClick() {
		if(isNullModel.getChildModels().isEmpty() || isNullNotNullListBox.getSelectedIndex() == 0) {
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
		availableExpressionTypes.add(ExpressionType.ATTRIBUTE);
		availableExpressionTypes.add(ExpressionType.RETRIEVE);
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.FUNCTION);
		availableExpressionTypes.add(ExpressionType.QUERY);
		
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		
		String label =  "What type of expression would you like to use for your Null statement?";
		ExpressionTypeSelectorList selectors = 
				new ExpressionTypeSelectorList(availableExpressionTypes, availableOperatorTypes, QueryFinderHelper.findAliasNames(this.isNullModel),
						buildButtonObserver, isNullModel, label, this);
		panel.add(buildIsNullNotNullFormGroup());		
		panel.add(selectors);	
		
		return panel;
	}
	
	private FormGroup buildIsNullNotNullFormGroup() {
		FormGroup isNullNotNullFormGroup = new FormGroup();		
		isNullNotNullFormGroup.setWidth("36%");

		FormLabel isNullNotNullLabel = new FormLabel();
		isNullNotNullLabel.setText(CHOOSE_A_NULL_STATEMENT);
		isNullNotNullLabel.setTitle(CHOOSE_A_NULL_STATEMENT);
		
		isNullNotNullListBox = new ListBoxMVP();
		isNullNotNullListBox.insertItem(SELECT_A_NULL_STATEMENT_PLACEHOLDER, SELECT_A_NULL_STATEMENT_PLACEHOLDER, SELECT_A_NULL_STATEMENT_PLACEHOLDER);
		isNullNotNullListBox.insertItem(IS_NULL, IS_NULL, IS_NULL);
		isNullNotNullListBox.insertItem(IS_NOT_NULL, IS_NOT_NULL, IS_NOT_NULL);
		isNullNotNullListBox.setSelectedIndex(selectedIndex);
		isNullNotNullListBox.addChangeHandler(event -> onIsNullNotNullListBoxChange(isNullNotNullListBox.getSelectedIndex()));
		
		isNullNotNullFormGroup.add(isNullNotNullLabel);
		isNullNotNullFormGroup.add(isNullNotNullListBox);
		
		return isNullNotNullFormGroup;
	}

	private void onIsNullNotNullListBoxChange(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		if(selectedIndex == 0) {
			isNullModel.setOperatorText("");
		} else  {
			isNullModel.setOperatorText(this.isNullNotNullListBox.getSelectedValue());
		}
		
		
		this.updateCQLDisplay();

	}
}
