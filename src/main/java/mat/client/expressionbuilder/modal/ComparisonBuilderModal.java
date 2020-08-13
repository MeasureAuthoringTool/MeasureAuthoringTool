package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ComparisonOperatorType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ComparisonModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.ListBoxMVP;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

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
		
		comparisonModel = new ComparisonModel(this.getParentModel());
		this.getParentModel().appendExpression(comparisonModel);
		leftHandSideBuildButtonObserver = new BuildButtonObserver(this, comparisonModel.getLeftHandSide(), mainModel);
		rightHandSideBuildButtonObserver = new BuildButtonObserver(this, comparisonModel.getRightHandSide(), mainModel);

		
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		display();
	}
	
	private void onComparisonOperatorListBoxChange() {
		this.selectedOperatorIndex = operatorListBox.getSelectedIndex();
		if(this.selectedOperatorIndex == 0) {
			comparisonModel.setComparisonOperator("");
		} else {
			comparisonModel.setComparisonOperator(operatorListBox.getSelectedValue());
		}
		
		this.updateCQLDisplay();
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
		availableExpressionForLeftSideOfComparison.add(ExpressionType.ATTRIBUTE);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.CODE);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.COMPUTATION);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.DATE_TIME);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.DEFINITION);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.FUNCTION);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.INTERVAL);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.PARAMETER);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.QUANTITY);
		availableExpressionForLeftSideOfComparison.add(ExpressionType.TIME_BOUNDARY);
		
		
		leftHandSideOfComparisonSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForLeftSideOfComparison, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.comparisonModel),
				leftHandSideBuildButtonObserver, comparisonModel.getLeftHandSide(), 
				"What is the first type of expression you would like to compare?", this);
		
		panel.add(leftHandSideOfComparisonSelectorList);

		panel.add(buildComparisonOperatorListBox());
		
		List<ExpressionType> availableExpressionForRightSideOfComparison = new ArrayList<>();
		availableExpressionForRightSideOfComparison.add(ExpressionType.ATTRIBUTE);
		availableExpressionForRightSideOfComparison.add(ExpressionType.CODE);
		availableExpressionForRightSideOfComparison.add(ExpressionType.COMPUTATION);
		availableExpressionForRightSideOfComparison.add(ExpressionType.DATE_TIME);
		availableExpressionForRightSideOfComparison.add(ExpressionType.DEFINITION);
		availableExpressionForRightSideOfComparison.add(ExpressionType.FUNCTION);
		availableExpressionForRightSideOfComparison.add(ExpressionType.INTERVAL);
		availableExpressionForRightSideOfComparison.add(ExpressionType.PARAMETER);
		availableExpressionForRightSideOfComparison.add(ExpressionType.QUANTITY);
		availableExpressionForRightSideOfComparison.add(ExpressionType.TIME_BOUNDARY);


		rightHandSideOfComparisonSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForRightSideOfComparison, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.comparisonModel),
				rightHandSideBuildButtonObserver, comparisonModel.getRightHandSide(), 
				"What is the second type of expression you would like to compare?", this);		
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
			this.getExpressionBuilderParent().showAndDisplay();
		}
	}
}
