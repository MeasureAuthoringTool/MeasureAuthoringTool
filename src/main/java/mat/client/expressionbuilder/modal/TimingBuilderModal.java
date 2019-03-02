package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.TimingModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.shared.SpacerWidget;

public class TimingBuilderModal extends SubExpressionBuilderModal {
	
	private static final String BUILD_YOUR_TIMING_PHRASE = "Build your timing phrase.";
	private BuildButtonObserver leftHandSideBuildButtonObserver;
	private BuildButtonObserver rightHandSideBuildButtonObserver;
	private int leftHandSideIndex = 0;
	private int rightHandSideIndex = 0;
	private ExpressionTypeSelectorList leftHandSideOfTimingSelectorList;
	private ExpressionTypeSelectorList rightHandSideOfTimingSelectorList;
	private TimingModel timingModel;

	public TimingBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Timing", parent, parentModel, mainModel);
		timingModel = new TimingModel(parentModel);
		this.getParentModel().appendExpression(timingModel);
		
		leftHandSideBuildButtonObserver = new BuildButtonObserver(this, timingModel.getLeftHandSide(), mainModel);
		rightHandSideBuildButtonObserver = new BuildButtonObserver(this, timingModel.getRightHandSide(), mainModel);
		
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());

		
		display();
	}

	private void applyButtonClickHandler() {
		if(timingModel.getRightHandSide().getChildModels().size() == 0 ||
				timingModel.getLeftHandSide().getChildModels().size() == 0) {
			
			this.getErrorAlert().createAlert("All sections of this page must be completed.");
			return;
		}
	
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		
		if(this.leftHandSideOfTimingSelectorList.getSelector() != null) {
			this.leftHandSideOfTimingSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.leftHandSideIndex);
			this.leftHandSideOfTimingSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event -> {
				this.leftHandSideIndex = this.leftHandSideOfTimingSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex();
			});
		}
		
		if(this.rightHandSideOfTimingSelectorList.getSelector() != null) {
			this.rightHandSideOfTimingSelectorList.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.rightHandSideIndex);
			this.rightHandSideOfTimingSelectorList.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event -> {
				this.rightHandSideIndex = this.rightHandSideOfTimingSelectorList.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex();
			});
		}
		
		this.updateCQLDisplay();		
	}

	private Widget buildContentPanel() {
		this.getContentPanel().clear();
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		List<ExpressionType> availableExpressionForLeftSideOfTiming = new ArrayList<>();
		availableExpressionForLeftSideOfTiming.add(ExpressionType.ATTRIBUTE);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.COMPUTATION);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.DEFINITION);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.FUNCTION);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.INTERVAL);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.PARAMETER);

		
		leftHandSideOfTimingSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForLeftSideOfTiming, new ArrayList<>(), leftHandSideBuildButtonObserver, timingModel.getLeftHandSide(), 
				"What expression would you like to use on the left-hand side of your timing?"
		);
		panel.add(leftHandSideOfTimingSelectorList);
		
		
		panel.add(buildTimingButtonPanel());
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());

		
		List<ExpressionType> availableExpressionForRightSideOfTiming = new ArrayList<>();
		availableExpressionForRightSideOfTiming.add(ExpressionType.ATTRIBUTE);
		availableExpressionForRightSideOfTiming.add(ExpressionType.COMPUTATION);
		availableExpressionForRightSideOfTiming.add(ExpressionType.DEFINITION);
		availableExpressionForRightSideOfTiming.add(ExpressionType.FUNCTION);
		availableExpressionForRightSideOfTiming.add(ExpressionType.INTERVAL);
		availableExpressionForRightSideOfTiming.add(ExpressionType.PARAMETER);
		
		rightHandSideOfTimingSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForRightSideOfTiming, new ArrayList<>(), rightHandSideBuildButtonObserver, timingModel.getRightHandSide(), 
				"What expression would you like to use on the right-hand side of your timing?"
		);
		panel.add(rightHandSideOfTimingSelectorList);
		
		return panel;
	}

	private Widget buildTimingButtonPanel() {
		VerticalPanel timingButtonPanel = new VerticalPanel();
		
		FormLabel label = new FormLabel();
		label.setText(BUILD_YOUR_TIMING_PHRASE);
		label.setTitle(BUILD_YOUR_TIMING_PHRASE);
		
		timingButtonPanel.add(label);
		timingButtonPanel.add(buildTimingBuildButton());
		
		return timingButtonPanel;
	}
	
	private Button buildTimingBuildButton() {
		Button buildButton = new Button();
		buildButton.setText("Build");
		buildButton.setTitle("Build");
		buildButton.setType(ButtonType.PRIMARY);
		buildButton.setIcon(IconType.WRENCH);
		return buildButton;
	}
}
