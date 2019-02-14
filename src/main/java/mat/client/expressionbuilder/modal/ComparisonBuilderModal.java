package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ComparisonOperatorType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ComparisonModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.shared.ListBoxMVP;

public class ComparisonBuilderModal extends SubExpressionBuilderModal {

	private static final String SELECT_COMPARISON = "--Select comparison--";
	private static final String WHAT_TYPE_OF_COMPARISON_WOULD_YOU_LIKE_TO_DO = "What type of comparison would you like to do?";
	private BuildButtonObserver leftHandSideBuildButtonObserver;
	private BuildButtonObserver rightHandSideBuildButtonObserver;
	private ComparisonModel comparisonModel;
	private ListBoxMVP operatorListBox;
	private int selectedOperatorIndex = 0;
	private int leftHandSideIndex = 0;
	private int rightHandSideIndex = 0;
	private ExpressionTypeSelectorList leftHandSideOfComparisonSelectorList;
	private ExpressionTypeSelectorList rightHandSideOfComparisonSelectorList;

	public ComparisonBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Comparison", parent, parentModel, mainModel);
		
		comparisonModel = new ComparisonModel();
		leftHandSideBuildButtonObserver = new BuildButtonObserver(this, comparisonModel.getLeftHandSide(), mainModel);
		rightHandSideBuildButtonObserver = new BuildButtonObserver(this, comparisonModel.getRightHandSide(), mainModel);

		
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		display();
	}
	
	private void onComparisonOperatorListBoxChange() {
		this.selectedOperatorIndex = operatorListBox.getSelectedIndex();
	}

	@Override
	public void display() {
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		this.operatorListBox.setSelectedIndex(selectedOperatorIndex);
		
		if(this.leftHandSideOfComparisonSelectorList.getSelector() != null) {
			this.leftHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.leftHandSideIndex);
			this.leftHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event -> {
				this.leftHandSideIndex = this.leftHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex();
			});
		}
		
		if(this.rightHandSideOfComparisonSelectorList.getSelector() != null) {
			this.rightHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.rightHandSideIndex);
			this.rightHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event -> {
				this.rightHandSideIndex = this.rightHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex();
			});
		}
		
		this.updateCQLDisplay();
	}
	
	private Widget buildContentPanel() {
		this.getContentPanel().clear();
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		List<ExpressionType> availableExpressionForLeftSideOfComparison = new ArrayList<>();
		availableExpressionForLeftSideOfComparison.add(ExpressionType.DEFINITION);
		
		
		leftHandSideOfComparisonSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForLeftSideOfComparison, new ArrayList<>(), leftHandSideBuildButtonObserver, comparisonModel.getLeftHandSide(), 
				"What is the first type of expression you would like to compare?"
		);
		
		panel.add(leftHandSideOfComparisonSelectorList);

		panel.add(buildComparisonOperatorListBox());
		
		List<ExpressionType> availableExpressionForRightSideOfComparison = new ArrayList<>();
		availableExpressionForRightSideOfComparison.add(ExpressionType.CODE);
		availableExpressionForRightSideOfComparison.add(ExpressionType.DEFINITION);
		rightHandSideOfComparisonSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForRightSideOfComparison, new ArrayList<>(), rightHandSideBuildButtonObserver, comparisonModel.getRightHandSide(), 
				"What is the second type of expression you would like to compare?"
		);
		
		panel.add(rightHandSideOfComparisonSelectorList);
		
		return panel;
	}

	private Widget buildComparisonOperatorListBox() {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setStyleName("selectorsPanel");

		FormGroup group = new FormGroup();
		
		FormLabel label = new FormLabel();
		label.setText(WHAT_TYPE_OF_COMPARISON_WOULD_YOU_LIKE_TO_DO);
		label.setTitle(WHAT_TYPE_OF_COMPARISON_WOULD_YOU_LIKE_TO_DO);
		
		group.add(label);
		
		operatorListBox = new ListBoxMVP();
		operatorListBox.setWidth("36%");
		operatorListBox.insertItem(SELECT_COMPARISON, SELECT_COMPARISON, SELECT_COMPARISON);
		operatorListBox.insertItem(ComparisonOperatorType.EQUALS.getDisplay(), ComparisonOperatorType.EQUALS.getValue(), ComparisonOperatorType.EQUALS.getDisplay());
		operatorListBox.insertItem(ComparisonOperatorType.NOT_EQUALS.getDisplay(), ComparisonOperatorType.NOT_EQUALS.getValue(), ComparisonOperatorType.NOT_EQUALS.getDisplay());
		operatorListBox.insertItem(ComparisonOperatorType.EQUIVALENT.getDisplay(), ComparisonOperatorType.EQUIVALENT.getValue(), ComparisonOperatorType.EQUIVALENT.getDisplay());
		operatorListBox.insertItem(ComparisonOperatorType.NOT_EQUIVALENT.getDisplay(), ComparisonOperatorType.NOT_EQUIVALENT.getValue(), ComparisonOperatorType.NOT_EQUIVALENT.getDisplay());
		operatorListBox.insertItem(ComparisonOperatorType.GREATER_THAN.getDisplay(), ComparisonOperatorType.GREATER_THAN.getValue(), ComparisonOperatorType.GREATER_THAN.getDisplay());
		operatorListBox.insertItem(ComparisonOperatorType.LESS_THAN.getDisplay(), ComparisonOperatorType.LESS_THAN.getValue(), ComparisonOperatorType.LESS_THAN.getDisplay());
		operatorListBox.insertItem(ComparisonOperatorType.GREATER_THAN_OR_EQUAL_TO.getDisplay(), ComparisonOperatorType.GREATER_THAN_OR_EQUAL_TO.getValue(), ComparisonOperatorType.GREATER_THAN_OR_EQUAL_TO.getDisplay());		
		operatorListBox.insertItem(ComparisonOperatorType.LESS_THAN_OR_EQUAL_TO.getDisplay(), ComparisonOperatorType.LESS_THAN_OR_EQUAL_TO.getValue(), ComparisonOperatorType.LESS_THAN_OR_EQUAL_TO.getDisplay());
		
		this.operatorListBox.addChangeHandler(event -> onComparisonOperatorListBoxChange());
		group.add(operatorListBox);
		horizontalPanel.add(group);

		return group;
	}

	private void applyButtonClickHandler() {
		if(comparisonModel.getRightHandSide().getChildModels().size() == 0 ||
				comparisonModel.getLeftHandSide().getChildModels().size() == 0 ||
				operatorListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("All fields required.");
		} else {
			this.getParentModel().appendExpression(comparisonModel);
			comparisonModel.setComparisonOperator(operatorListBox.getSelectedValue());
			this.getExpressionBuilderParent().showAndDisplay();
		}
	}
}
