
package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ArithmeticOperatorType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ComputationModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.ListBoxMVP;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

public class ComputationBuilderModal extends SubExpressionBuilderModal {

	private static final String SELECT_COMPUTATION = "-- Select computation --";
	private static final String ALL_FIELDS_ARE_REQUIRED = "All fields are required.";
	private static final String MATHEMATICAL_OPERATION_LABEL = "What type of mathematical operation would you like to do?";
	private static final String FIRST_HEADING = "What is the expression you would like to use as the first part of your computation?";
	private static final String SECOND_HEADING = "What is the expression you would like to use as the second part of your computation?";

	private int leftHandSideIndex = 0;
	private int rightHandSideIndex = 0;
	private int selectedOperatorIndex = 0;

	private ListBoxMVP operatorListBox;

	private final ComputationModel computationModel;

	private final BuildButtonObserver leftHandSideBuildButtonObserver;
	private final BuildButtonObserver rightHandSideBuildButtonObserver;

	private ExpressionTypeSelectorList leftHandSideOfComputationSelectorList;
	private ExpressionTypeSelectorList rightHandSideOfComputationSelectorList;

	public ComputationBuilderModal(final ExpressionBuilderModal parent, final ExpressionBuilderModel parentModel, final ExpressionBuilderModel mainModel) {
		super(ExpressionType.COMPUTATION.getDisplayName(), parent, parentModel, mainModel);

		computationModel = new ComputationModel(parentModel);
		getParentModel().appendExpression(computationModel);
		leftHandSideBuildButtonObserver = new BuildButtonObserver(this, computationModel.getLeftHandSide(), mainModel);
		rightHandSideBuildButtonObserver = new BuildButtonObserver(this, computationModel.getRightHandSide(), mainModel);

		getApplyButton().addClickHandler(event -> onApplyButtonClick());
		display();
	}

	private void onComputationOperatorListBoxChange() {
		selectedOperatorIndex = operatorListBox.getSelectedIndex();
		if(selectedOperatorIndex == 0) {
			computationModel.setComputationOperator("");
		} else {
			computationModel.setComputationOperator(operatorListBox.getSelectedValue());
		}

		updateCQLDisplay();
	}

	@Override
	public void display() {
		getErrorAlert().clearAlert();
		getContentPanel().add(buildContentPanel());
		operatorListBox.setSelectedIndex(selectedOperatorIndex);

		if(leftHandSideOfComputationSelectorList.getSelector() != null) {
			leftHandSideOfComputationSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(leftHandSideIndex);
			leftHandSideOfComputationSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event ->
			leftHandSideIndex = leftHandSideOfComputationSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex());
		}

		if(rightHandSideOfComputationSelectorList.getSelector() != null) {
			rightHandSideOfComputationSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(rightHandSideIndex);
			rightHandSideOfComputationSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event ->
			rightHandSideIndex = rightHandSideOfComputationSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex());
		}

		updateCQLDisplay();
	}

	private Widget buildContentPanel() {
		getContentPanel().clear();

		final VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");

		buildFirstExpressionList();
		buildSecondExpressionList();

		panel.add(leftHandSideOfComputationSelectorList);
		panel.add(buildComputationOperatorListBox());
		panel.add(rightHandSideOfComputationSelectorList);

		return panel;
	}

	private void buildFirstExpressionList() {
		final List<ExpressionType> leftSideOfComputationOptions = new ArrayList<>();
		leftSideOfComputationOptions.addAll(buildDropDownOptions());

		leftHandSideOfComputationSelectorList = new ExpressionTypeSelectorList(
				leftSideOfComputationOptions, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.computationModel),
				leftHandSideBuildButtonObserver, computationModel.getLeftHandSide(), FIRST_HEADING, this);
	}

	private void buildSecondExpressionList() {
		final List<ExpressionType> rightSideOfComputationOptions = new ArrayList<>();
		rightSideOfComputationOptions.addAll(buildDropDownOptions());

		rightHandSideOfComputationSelectorList = new ExpressionTypeSelectorList(
				rightSideOfComputationOptions, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.computationModel),
				rightHandSideBuildButtonObserver, computationModel.getRightHandSide(), SECOND_HEADING, this);
	}

	private List<ExpressionType> buildDropDownOptions(){
		final List<ExpressionType> availableExpressions = new ArrayList<>();
		availableExpressions.add(ExpressionType.ATTRIBUTE);
		availableExpressions.add(ExpressionType.COMPUTATION);
		availableExpressions.add(ExpressionType.DATE_TIME);
		availableExpressions.add(ExpressionType.DEFINITION);
		availableExpressions.add(ExpressionType.FUNCTION);
		availableExpressions.add(ExpressionType.PARAMETER);
		availableExpressions.add(ExpressionType.QUANTITY);
		availableExpressions.add(ExpressionType.TIME_BOUNDARY);
		return availableExpressions;
	}

	private Widget buildComputationOperatorListBox() {
		final FormGroup group = new FormGroup();

		buildOperatorListBox();

		group.add(buildLabel());
		group.add(operatorListBox);

		return group;
	}

	private FormLabel buildLabel() {
		final FormLabel label = new FormLabel();
		label.setText(MATHEMATICAL_OPERATION_LABEL);
		label.setTitle(MATHEMATICAL_OPERATION_LABEL);
		return label;
	}

	private void buildOperatorListBox() {
		operatorListBox = new ListBoxMVP();
		operatorListBox.setWidth("36%");

		buildComputationDropDown();

		operatorListBox.addChangeHandler(event -> onComputationOperatorListBoxChange());
	}

	private void buildComputationDropDown() {
		operatorListBox.insertItem(SELECT_COMPUTATION, SELECT_COMPUTATION);
		operatorListBox.insertItem(ArithmeticOperatorType.MULTIPLICATION.getValue(), ArithmeticOperatorType.MULTIPLICATION.getDisplay());
		operatorListBox.insertItem(ArithmeticOperatorType.ADDITION.getValue(), ArithmeticOperatorType.ADDITION.getDisplay());
		operatorListBox.insertItem(ArithmeticOperatorType.SUBTRACTION.getValue(), ArithmeticOperatorType.SUBTRACTION.getDisplay());
		operatorListBox.insertItem(ArithmeticOperatorType.DIVISION.getValue(), ArithmeticOperatorType.DIVISION.getDisplay());
		operatorListBox.insertItem(ArithmeticOperatorType.EXPONENT.getValue(), ArithmeticOperatorType.EXPONENT.getDisplay());
		operatorListBox.insertItem(ArithmeticOperatorType.TRUNCATED_DIVISION.getValue(), ArithmeticOperatorType.TRUNCATED_DIVISION.getDisplay());
		operatorListBox.insertItem(ArithmeticOperatorType.MODULO.getValue(), ArithmeticOperatorType.MODULO.getDisplay());
	}

	private void onApplyButtonClick() {
		if(computationModel.getRightHandSide().getChildModels().isEmpty() ||
				computationModel.getLeftHandSide().getChildModels().isEmpty() ||
				operatorListBox.getSelectedIndex() == 0) {
			getErrorAlert().createAlert(ALL_FIELDS_ARE_REQUIRED);
		} else {
			getExpressionBuilderParent().showAndDisplay();
		}
	}
}
