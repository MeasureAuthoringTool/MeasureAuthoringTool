package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IntervalModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

public class IntervalBuilderModal extends SubExpressionBuilderModal {	
	private static final String UPPER_BOUND_INCLUSIVE_LABEL = "Would you like this upper boundary to be included in or excluded from your interval?";
	private static final String LOWER_BOUND_EXCLUSIVE_LABEL = "Would you like this lower boundary to be included in or excluded from your interval?";
	private BuildButtonObserver lowerBoundBuildButtonObserver;
	private BuildButtonObserver upperBoundBuildButtonObserver;
	private IntervalModel intervalModel;
	private boolean isLowerBoundInclusive = true;
	private boolean isUpperBoundInclusive = true;
	private ExpressionTypeSelectorList upperBoundExpressionTypeSelector;
	private ExpressionTypeSelectorList lowerBoundExpressionTypeSelector;
	private int lowerBoundIndex;
	private int upperBoundIndex;

	public IntervalBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("Interval", parent, parentModel, mainModel);
		
		intervalModel = new IntervalModel(parentModel);
		this.getParentModel().appendExpression(intervalModel);		
		lowerBoundBuildButtonObserver = new BuildButtonObserver(this, intervalModel.getLowerBound(), mainModel);
		upperBoundBuildButtonObserver = new BuildButtonObserver(this, intervalModel.getUpperBound(), mainModel);
		
		this.getApplyButton().addClickHandler(event -> onApplyButtonClickHandler());
		
		display();
	}

	private void onApplyButtonClickHandler() {
		if(intervalModel.getLowerBound().getChildModels().size() == 0 || intervalModel.getUpperBound().getChildModels().size() == 0) {
			this.getErrorAlert().createAlert("A lower and an upper boundary are required for Intervals.");
		} else {
			intervalModel.setLowerBoundInclusive(isLowerBoundInclusive);
			intervalModel.setUpperBoundInclusive(isUpperBoundInclusive);
			this.getExpressionBuilderParent().showAndDisplay();
		}
	}

	@Override
	public void display() {
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
		
		if(this.lowerBoundExpressionTypeSelector.getSelector() != null) {
			this.lowerBoundExpressionTypeSelector.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.lowerBoundIndex);
			this.lowerBoundExpressionTypeSelector.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event -> {
				this.lowerBoundIndex = this.lowerBoundExpressionTypeSelector.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex();
			});
		}
		
		if(this.upperBoundExpressionTypeSelector.getSelector() != null) {
			this.upperBoundExpressionTypeSelector.getSelector().getExpressionTypeSelectorListBox().setSelectedIndex(this.upperBoundIndex);
			this.upperBoundExpressionTypeSelector.getSelector().getExpressionTypeSelectorListBox().addChangeHandler(event -> {
				this.upperBoundIndex = this.upperBoundExpressionTypeSelector.getSelector().getExpressionTypeSelectorListBox().getSelectedIndex();
			});
		}
		
	}

	private Widget buildContentPanel() {
		this.getContentPanel().clear();		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		List<ExpressionType> availableExpressions = new ArrayList<>();
		availableExpressions.add(ExpressionType.ATTRIBUTE);
		availableExpressions.add(ExpressionType.COMPUTATION);
		availableExpressions.add(ExpressionType.DATE_TIME);
		availableExpressions.add(ExpressionType.DEFINITION);
		availableExpressions.add(ExpressionType.FUNCTION);
		availableExpressions.add(ExpressionType.PARAMETER);
		availableExpressions.add(ExpressionType.QUANTITY);
		availableExpressions.add(ExpressionType.TIME_BOUNDARY);
		
		lowerBoundExpressionTypeSelector = new ExpressionTypeSelectorList(
				availableExpressions, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.intervalModel),
				lowerBoundBuildButtonObserver, intervalModel.getLowerBound(), 
				"What would you like to use for the lower boundary of your interval?", this
		);
		
		panel.add(lowerBoundExpressionTypeSelector);
		panel.add(buildLowerBuildInclusiveRadioButtons());
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());

		upperBoundExpressionTypeSelector = new ExpressionTypeSelectorList(
				availableExpressions, new ArrayList<>(), upperBoundBuildButtonObserver, intervalModel.getUpperBound(), 
				"What would you like to use for the upper boundary of your interval?", this
		);
		
		panel.add(upperBoundExpressionTypeSelector);
		panel.add(buildUpperBoundInclusiveRadioButtons());
		
		return panel;
	}

	private Widget buildLowerBuildInclusiveRadioButtons() {
		FormGroup group = new FormGroup();
		FormLabel label = new FormLabel();
		label.setText(LOWER_BOUND_EXCLUSIVE_LABEL);
		label.setTitle(LOWER_BOUND_EXCLUSIVE_LABEL);
		group.add(label);
		
		HorizontalPanel lowerBoundInclusiveRadioButtonPanel = new HorizontalPanel();
		lowerBoundInclusiveRadioButtonPanel.setStyleName("selectorsPanel");
		lowerBoundInclusiveRadioButtonPanel.setWidth("20%");

		RadioButton includedRadioButtonLowerBound = new RadioButton("lowerBoundInclusiveRadioButton", "Included");
		RadioButton excludedRadioButtonLowerBound = new RadioButton("lowerBoundInclusiveRadioButton", "Excluded");
		includedRadioButtonLowerBound.setValue(isLowerBoundInclusive);
		excludedRadioButtonLowerBound.setValue(!isLowerBoundInclusive);
		
		includedRadioButtonLowerBound.addValueChangeHandler(event -> {
			isLowerBoundInclusive = event.getValue();
			intervalModel.setLowerBoundInclusive(isLowerBoundInclusive);
			this.updateCQLDisplay();
		});
		
		excludedRadioButtonLowerBound.addValueChangeHandler(event -> {
			isLowerBoundInclusive = !event.getValue();
			intervalModel.setLowerBoundInclusive(isLowerBoundInclusive);
			this.updateCQLDisplay();
		});
		
		lowerBoundInclusiveRadioButtonPanel.add(includedRadioButtonLowerBound);
		lowerBoundInclusiveRadioButtonPanel.add(excludedRadioButtonLowerBound);
		group.add(lowerBoundInclusiveRadioButtonPanel);
		return group;
	}
	
	private Widget buildUpperBoundInclusiveRadioButtons() {
		FormGroup group = new FormGroup();
		FormLabel label = new FormLabel();
		label.setText(UPPER_BOUND_INCLUSIVE_LABEL);
		label.setTitle(UPPER_BOUND_INCLUSIVE_LABEL);
		group.add(label);
		
		HorizontalPanel upperBoundInclusiveRadioButtonPanel = new HorizontalPanel();
		upperBoundInclusiveRadioButtonPanel.setStyleName("selectorsPanel");
		upperBoundInclusiveRadioButtonPanel.setWidth("20%");
		
		RadioButton includedRadioButtonUpperBound = new RadioButton("uperrBoundInclusiveRadioButton", "Included");
		RadioButton excludedRadioButtonUpperBound = new RadioButton("uperrBoundInclusiveRadioButton", "Excluded");		
		includedRadioButtonUpperBound.setValue(isUpperBoundInclusive);
		excludedRadioButtonUpperBound.setValue(!isUpperBoundInclusive);
		
		includedRadioButtonUpperBound.addValueChangeHandler(event -> {
			isUpperBoundInclusive = event.getValue();
			intervalModel.setUpperBoundInclusive(isUpperBoundInclusive);
			this.updateCQLDisplay();
		});
		
		excludedRadioButtonUpperBound.addValueChangeHandler(event -> {
			isUpperBoundInclusive = !event.getValue();
			intervalModel.setUpperBoundInclusive(isUpperBoundInclusive);
			this.updateCQLDisplay();
		});
		
		upperBoundInclusiveRadioButtonPanel.add(includedRadioButtonUpperBound);
		upperBoundInclusiveRadioButtonPanel.add(excludedRadioButtonUpperBound);
		group.add(upperBoundInclusiveRadioButtonPanel);		
		return group;
	}

}
