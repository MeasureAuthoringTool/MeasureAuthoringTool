package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Code;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.MembershipInModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;

public class MembershipInModal extends SubExpressionBuilderModal {

	private static final String IN = "in";

	private int leftHandSideIndex = 0;
	private int rightHandSideIndex = 0;

	private final MembershipInModel inModel;

	private final BuildButtonObserver leftHandSideBuildButtonObserver;
	private final BuildButtonObserver rightHandSideBuildButtonObserver;
	
	private ExpressionTypeSelectorList leftHandSideOfComparisonSelectorList;
	private ExpressionTypeSelectorList rightHandSideOfComparisonSelectorList;

	public MembershipInModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Membership (In)", parent, parentModel, mainModel);
		
		inModel = new MembershipInModel();
		leftHandSideBuildButtonObserver = new BuildButtonObserver(this, inModel.getLeftHandSide(), mainModel);
		rightHandSideBuildButtonObserver = new BuildButtonObserver(this, inModel.getRightHandSide(), mainModel);
		
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		display();
	}

	@Override
	public void display() {
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		
		if(this.leftHandSideOfComparisonSelectorList.getSelector() != null) {
			this.leftHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.leftHandSideIndex);
			this.leftHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event ->
				this.leftHandSideIndex = this.leftHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex());
		}
		
		if(this.rightHandSideOfComparisonSelectorList.getSelector() != null) {
			this.rightHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.rightHandSideIndex);
			this.rightHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event ->
				this.rightHandSideIndex = this.rightHandSideOfComparisonSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex());
		}
		
		this.updateCQLDisplay();
	}
	
	private Widget buildContentPanel() {
		this.getContentPanel().clear();
		
		final VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		buildFirstExpressionList();
		buildSecondExpressionList();
		
		panel.add(leftHandSideOfComparisonSelectorList);
		panel.add(buildInLabel());
		panel.add(rightHandSideOfComparisonSelectorList);
		
		return panel;
	}

	private void buildFirstExpressionList() {
		final List<ExpressionType> availableExpressionForLeftSideOfComparison = new ArrayList<>();
		availableExpressionForLeftSideOfComparison.add(ExpressionType.DEFINITION);
		
		leftHandSideOfComparisonSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForLeftSideOfComparison, new ArrayList<>(), leftHandSideBuildButtonObserver, inModel.getLeftHandSide(), 
				"What type of expression are you looking for?");
	}
	
	private void buildSecondExpressionList() {
		final List<ExpressionType> availableExpressionForRightSideOfComparison = new ArrayList<>();
		availableExpressionForRightSideOfComparison.add(ExpressionType.DEFINITION);
		availableExpressionForRightSideOfComparison.add(ExpressionType.PARAMETER);
		availableExpressionForRightSideOfComparison.add(ExpressionType.VALUESET);

		rightHandSideOfComparisonSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForRightSideOfComparison, new ArrayList<>(), rightHandSideBuildButtonObserver, inModel.getRightHandSide(), 
				"In which expression would you like to find that data?"
		);
	}
	
	private Widget buildInLabel() {
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setStyleName("selectorsPanel");
		
		final Code code = new Code();
		code.setText(IN);
		code.setTitle(IN);
		code.setColor("black");
		code.setStyleName("expressionBuilderCode");

		horizontalPanel.add(code);

		return horizontalPanel;
	}

	private void applyButtonClickHandler() {
		if(inModel.getRightHandSide().getChildModels().isEmpty() || inModel.getLeftHandSide().getChildModels().isEmpty()) {
			this.getErrorAlert().createAlert("All fields required.");
		} else {
			this.getParentModel().appendExpression(inModel);
			this.getExpressionBuilderParent().showAndDisplay();
		}
	}
}
