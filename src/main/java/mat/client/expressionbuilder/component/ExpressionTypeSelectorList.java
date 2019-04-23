package mat.client.expressionbuilder.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwtbootstrap3.client.ui.Code;
import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.buttons.InAppHelpButton;
import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.modal.ExpressionBuilderModal;
import mat.client.expressionbuilder.modal.InAppHelpModal;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.ModelAndOperatorTypeUtil;
import mat.client.expressionbuilder.model.OperatorModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;

public class ExpressionTypeSelectorList extends Composite {

	private ExpressionBuilderModel model;
	private List<ExpressionType> availableExpressionTypes;
	private List<OperatorType> availableOperatorTypes;
	private List<String> availableAliases;

	private BuildButtonObserver buildButtonObserver;
	private boolean hasNoSelections;
	private boolean canOnlyMakeOneSelection;
	private String labelText;
	private ExpressionTypeSelector selector;
	private ExpressionBuilderModal parentModal;
	
	public ExpressionTypeSelectorList(List<ExpressionType> availableExpressionTypes, List<OperatorType> availableOperatorTypes,
			BuildButtonObserver observer, ExpressionBuilderModel model, String labelText, ExpressionBuilderModal parentModal) {
		this(availableExpressionTypes, availableOperatorTypes, new ArrayList<>(), observer, model, labelText, parentModal);
	}
	
	public ExpressionTypeSelectorList(
			List<ExpressionType> availableExpressionTypes, List<OperatorType> availableOperatorTypes,
			List<String> availableAliases, 
			BuildButtonObserver observer, ExpressionBuilderModel model, String labelText, ExpressionBuilderModal parentModal) {
		this.availableExpressionTypes = availableExpressionTypes;
		this.availableAliases = availableAliases;
		this.availableOperatorTypes = new ArrayList<>();
		this.availableOperatorTypes.addAll(availableOperatorTypes);
		this.buildButtonObserver = observer;
		this.model = model;
		this.hasNoSelections = this.model.getChildModels().isEmpty();
		canOnlyMakeOneSelection = this.availableOperatorTypes.isEmpty();
		this.labelText = labelText;
		this.parentModal = parentModal;
		initWidget(buildPanel());
	}
		
	public ExpressionTypeSelector getSelector() {
		return this.selector;
	}
	
	private VerticalPanel buildPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		panel.setWidth("100%");
		HorizontalPanel labelPanel = new HorizontalPanel();
		FormLabel label = new FormLabel();
		label.setText(this.labelText);
		label.setTitle(this.labelText);
		labelPanel.add(label);
		if(isInitialModalScreen()) {
			InAppHelpButton inAppHelpButton = new InAppHelpButton();
			labelPanel.add(inAppHelpButton);
			inAppHelpButton.setMarginTop(-9);
			inAppHelpButton.addClickHandler(event -> handleOpen());
		}
		
		panel.add(labelPanel);
				
		// filter available operators based on first expression type selected
		if(!this.model.getChildModels().isEmpty()) {
			CQLType firstType = this.model.getChildModels().get(0).getType();
			
			List<OperatorType> availableOperatorTypesForFirstExpressionType = new ArrayList<>();
			availableOperatorTypesForFirstExpressionType.addAll(OperatorTypeUtil.getAvailableOperatorsCQLType(firstType));
			
			List<OperatorType> newAvailableOperators = new ArrayList<>();
			for(OperatorType type : this.availableOperatorTypes) {
				if(availableOperatorTypesForFirstExpressionType.contains(type)) {
					newAvailableOperators.add(type);
				}
			}
			
			this.availableOperatorTypes = newAvailableOperators;
		}
		
		for(int i = 0; i < this.model.getChildModels().size(); i++) {
			IExpressionBuilderModel currentChildModel = this.model.getChildModels().get(i);
			
			// operators should not display with the collapsable panel, but should be formatted with code.
			if(currentChildModel instanceof OperatorModel) {
				FocusPanel operatorFocusPanel = new FocusPanel();
				operatorFocusPanel.getElement().setAttribute("aria-label", currentChildModel.getCQL(""));
				
				Code code = new Code(); 
				code.setText(currentChildModel.getCQL(""));
				code.setTitle(currentChildModel.getCQL(""));
				code.setColor("black");
				code.setStyleName("expressionBuilderCode");
				
				operatorFocusPanel.add(code);
				panel.add(operatorFocusPanel);
				
				// all subsequent available operators should be equivalent to the previously selected operator
				availableOperatorTypes.clear();
				availableOperatorTypes.add(ModelAndOperatorTypeUtil.getOperatorModel(currentChildModel));
				
			} else {
				ExpandCollapseCQLExpressionPanel expressionPanelGroup = buildExpressionCollapsePanel(currentChildModel);
				
				if(i != 0) {
					expressionPanelGroup.getElement().setAttribute("style", "margin-top: 15px");
				}
				
				panel.add(expressionPanelGroup);
			}
		}
		
		boolean cannotAddMoreFunctionality = areSingleSelectScreensOnly(this.model.getChildModels());
		
		boolean canAddAnother = hasNoSelections || canOnlyMakeOneSelection || cannotAddMoreFunctionality;
		
		if (!(!this.model.getChildModels().isEmpty() && (canOnlyMakeOneSelection || cannotAddMoreFunctionality))) {
			selector = new ExpressionTypeSelector(availableExpressionTypes, availableOperatorTypes, availableAliases, buildButtonObserver, canAddAnother, this.parentModal);		
			panel.add(selector);
		}
		
	
		return panel;
	}
	
	private void handleOpen() {
		ExpressionBuilderModal inAppHelpModal = new InAppHelpModal(parentModal, null, null);
		((InAppHelpModal) inAppHelpModal).showModal();
	}

	private boolean isInitialModalScreen() {
		return "CQL Expression Builder".equals(this.parentModal.getModalTitle());
	}

	private ExpandCollapseCQLExpressionPanel buildExpressionCollapsePanel(IExpressionBuilderModel model) {	
		return new ExpandCollapseCQLExpressionPanel(model.getDisplayName(), model.getCQL(""));
	}
	
	private boolean areSingleSelectScreensOnly(List<IExpressionBuilderModel> childModels) {
		//Screens to hide Add more functionality
		List<String> singleModels = Arrays.asList(ExpressionType.COMPUTATION.getDisplayName(), ExpressionType.DATE_TIME.getDisplayName(),
				ExpressionType.QUANTITY.getDisplayName(), ExpressionType.TIME_BOUNDARY.getDisplayName());
		
		return childModels.stream().anyMatch(e -> singleModels.stream().anyMatch(e.getDisplayName()::contains));
		
	}
}
