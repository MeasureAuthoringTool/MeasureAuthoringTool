package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.MembershipInModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import org.gwtbootstrap3.client.ui.Code;

import java.util.ArrayList;
import java.util.List;

public class MembershipInModal extends SubExpressionBuilderModal {

	private static final String IN = "in";

	private int leftHandSideIndex = 0;
	private int rightHandSideIndex = 0;

	private final MembershipInModel inModel;

	private final BuildButtonObserver leftHandSideBuildButtonObserver;
	private final BuildButtonObserver rightHandSideBuildButtonObserver;
	
	private ExpressionTypeSelectorList leftHandSideOfExpressionSelectorList;
	private ExpressionTypeSelectorList rightHandSideOfExpressionSelectorList;

	public MembershipInModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Membership (In)", parent, parentModel, mainModel);
		
		inModel = new MembershipInModel(parentModel);
		this.getParentModel().appendExpression(inModel);
		leftHandSideBuildButtonObserver = new BuildButtonObserver(this, inModel.getLeftHandSide(), mainModel);
		rightHandSideBuildButtonObserver = new BuildButtonObserver(this, inModel.getRightHandSide(), mainModel);
		
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		display();
	}

	@Override
	public void display() {
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		
		if(this.leftHandSideOfExpressionSelectorList.getSelector() != null) {
			this.leftHandSideOfExpressionSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.leftHandSideIndex);
			this.leftHandSideOfExpressionSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event ->
				this.leftHandSideIndex = this.leftHandSideOfExpressionSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex());
		}
		
		if(this.rightHandSideOfExpressionSelectorList.getSelector() != null) {
			this.rightHandSideOfExpressionSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.rightHandSideIndex);
			this.rightHandSideOfExpressionSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event ->
				this.rightHandSideIndex = this.rightHandSideOfExpressionSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex());
		}
		
		this.updateCQLDisplay();
	}
	
	private Widget buildContentPanel() {
		this.getContentPanel().clear();
		
		final VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		buildFirstExpressionList();
		buildSecondExpressionList();
		
		panel.add(leftHandSideOfExpressionSelectorList);
		panel.add(buildInLabel());
		panel.add(rightHandSideOfExpressionSelectorList);
		
		return panel;
	}

	private void buildFirstExpressionList() {
		final List<ExpressionType> availableExpressionForLeftSideOfIn = new ArrayList<>();
		availableExpressionForLeftSideOfIn.add(ExpressionType.ATTRIBUTE);
		availableExpressionForLeftSideOfIn.add(ExpressionType.COMPUTATION);
		availableExpressionForLeftSideOfIn.add(ExpressionType.RETRIEVE);
		availableExpressionForLeftSideOfIn.add(ExpressionType.DATE_TIME);
		availableExpressionForLeftSideOfIn.add(ExpressionType.DEFINITION);
		availableExpressionForLeftSideOfIn.add(ExpressionType.FUNCTION);
		availableExpressionForLeftSideOfIn.add(ExpressionType.INTERVAL);
		availableExpressionForLeftSideOfIn.add(ExpressionType.PARAMETER);
		availableExpressionForLeftSideOfIn.add(ExpressionType.QUANTITY);
		availableExpressionForLeftSideOfIn.add(ExpressionType.QUERY);
		availableExpressionForLeftSideOfIn.add(ExpressionType.TIME_BOUNDARY);
		
		leftHandSideOfExpressionSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForLeftSideOfIn, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.inModel),
				leftHandSideBuildButtonObserver, inModel.getLeftHandSide(), 
				"What type of expression are you looking for?", this);
	}
	
	private void buildSecondExpressionList() {
		final List<ExpressionType> availableExpressionForRightSideOfIn = new ArrayList<>();
		availableExpressionForRightSideOfIn.add(ExpressionType.ATTRIBUTE);		
		availableExpressionForRightSideOfIn.add(ExpressionType.RETRIEVE);
		availableExpressionForRightSideOfIn.add(ExpressionType.DEFINITION);
		availableExpressionForRightSideOfIn.add(ExpressionType.FUNCTION);
		availableExpressionForRightSideOfIn.add(ExpressionType.INTERVAL);
		availableExpressionForRightSideOfIn.add(ExpressionType.PARAMETER);
		availableExpressionForRightSideOfIn.add(ExpressionType.QUERY);
		availableExpressionForRightSideOfIn.add(ExpressionType.VALUESET);

		rightHandSideOfExpressionSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForRightSideOfIn, new ArrayList<>(), rightHandSideBuildButtonObserver, inModel.getRightHandSide(), 
				"In which expression would you like to find that data?", this
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
			this.getExpressionBuilderParent().showAndDisplay();
		}
	}
}
