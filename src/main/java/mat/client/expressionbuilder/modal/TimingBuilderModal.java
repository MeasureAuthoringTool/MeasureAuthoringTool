package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpandCollapseCQLExpressionPanel;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.TimingModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.ArrayList;
import java.util.List;

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
				timingModel.getLeftHandSide().getChildModels().size() == 0 ||
				timingModel.getIntervalOperatorPhrase().getChildModels().size() == 0) {
			
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
		availableExpressionForLeftSideOfTiming.add(ExpressionType.DATE_TIME);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.DEFINITION);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.FUNCTION);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.INTERVAL);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.PARAMETER);
		availableExpressionForLeftSideOfTiming.add(ExpressionType.TIME_BOUNDARY);

		
		leftHandSideOfTimingSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForLeftSideOfTiming, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.timingModel),
				leftHandSideBuildButtonObserver, timingModel.getLeftHandSide(), 
				"What expression would you like to use on the left-hand side of your timing?", this
		);
		
		panel.add(leftHandSideOfTimingSelectorList);
		
		
		panel.add(buildTimingButtonPanel());
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());

		
		List<ExpressionType> availableExpressionForRightSideOfTiming = new ArrayList<>();
		availableExpressionForRightSideOfTiming.add(ExpressionType.ATTRIBUTE);		
		availableExpressionForRightSideOfTiming.add(ExpressionType.COMPUTATION);
		availableExpressionForRightSideOfTiming.add(ExpressionType.DATE_TIME);
		availableExpressionForRightSideOfTiming.add(ExpressionType.DEFINITION);
		availableExpressionForRightSideOfTiming.add(ExpressionType.FUNCTION);
		availableExpressionForRightSideOfTiming.add(ExpressionType.INTERVAL);
		availableExpressionForRightSideOfTiming.add(ExpressionType.PARAMETER);
		
		rightHandSideOfTimingSelectorList = new ExpressionTypeSelectorList(
				availableExpressionForRightSideOfTiming, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.timingModel),
				rightHandSideBuildButtonObserver, timingModel.getRightHandSide(), 
				"What expression would you like to use on the right-hand side of your timing?", this);
		panel.add(rightHandSideOfTimingSelectorList);
		
		return panel;
	}

	private Widget buildTimingButtonPanel() {
		VerticalPanel timingButtonPanel = new VerticalPanel();
		timingButtonPanel.setWidth("100%");
		
		FormLabel label = new FormLabel();
		label.setText(BUILD_YOUR_TIMING_PHRASE);
		label.setTitle(BUILD_YOUR_TIMING_PHRASE);
		timingButtonPanel.add(label);

		if(this.timingModel.getIntervalOperatorPhrase().getChildModels().isEmpty()) {
			Button timingBuildButton = buildTimingBuildButton();
			timingBuildButton.addClickHandler(event -> onTimingBuildButtonClick());
			timingButtonPanel.add(timingBuildButton);
		} else {
			ExpandCollapseCQLExpressionPanel expandCollapsePanel = new ExpandCollapseCQLExpressionPanel("Timing Phrase", this.timingModel.getIntervalOperatorPhrase().getCQL(""));
			expandCollapsePanel.getDeleteButton().addClickHandler(event -> onTimingDeleteButtonClick());
			timingButtonPanel.add(expandCollapsePanel);
		}
		
		return timingButtonPanel;
	}
	
	private void onTimingDeleteButtonClick() {
		this.timingModel.getIntervalOperatorPhrase().getChildModels().clear();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}

	private void onTimingBuildButtonClick() {
		TimingPhraseBuilderModal modal = new TimingPhraseBuilderModal(this, timingModel.getIntervalOperatorPhrase(), this.getMainModel());
		modal.show();
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
