package mat.client.expressionbuilder.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Code;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Pre;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.CodeModel;
import mat.client.expressionbuilder.model.ComparisonModel;
import mat.client.expressionbuilder.model.DefinitionModel;
import mat.client.expressionbuilder.model.ExistsModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.IntervalModel;
import mat.client.expressionbuilder.model.IsNullModel;
import mat.client.expressionbuilder.model.IsTrueFalseModel;
import mat.client.expressionbuilder.model.ModelAndOperatorTypeUtil;
import mat.client.expressionbuilder.model.NotModel;
import mat.client.expressionbuilder.model.OperatorModel;
import mat.client.expressionbuilder.model.ParameterModel;
import mat.client.expressionbuilder.model.QueryModel;
import mat.client.expressionbuilder.model.RetrieveModel;
import mat.client.expressionbuilder.model.ValuesetModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;

public class ExpressionTypeSelectorList extends Composite {

	private ExpressionBuilderModel model;
	private List<ExpressionType> availableExpressionTypes;
	private BuildButtonObserver buildButtonObserver;
	private boolean hasNoSelections;
	private List<OperatorType> availableOperatorTypes;
	private boolean canOnlyMakeOneSelection;
	private String labelText;
	private ExpressionTypeSelector selector;

	public ExpressionTypeSelectorList(List<ExpressionType> availableExpressionTypes, List<OperatorType> availableOperatorTypes,
			BuildButtonObserver observer, ExpressionBuilderModel model, String labelText) {
		this.availableExpressionTypes = availableExpressionTypes;
		this.availableOperatorTypes = new ArrayList<>();
		this.availableOperatorTypes.addAll(availableOperatorTypes);
		this.buildButtonObserver = observer;
		this.model = model;
		this.hasNoSelections = this.model.getChildModels().size() == 0;
		canOnlyMakeOneSelection = this.availableOperatorTypes.isEmpty();
		this.labelText = labelText;

		initWidget(buildPanel());
	}
		
	public ExpressionTypeSelector getSelector() {
		return this.selector;
	}
	
	private VerticalPanel buildPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		panel.setWidth("100%");
		FormLabel label = new FormLabel();
		label.setText(this.labelText);
		label.setTitle(this.labelText);
		
		panel.add(label);
				
		// filter available operators based on first expression type selected
		if(this.model.getChildModels().size() >= 1) {
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
				PanelGroup expressionPanelGroup = buildExpressionCollapsePanel(currentChildModel);
				
				if(i != 0) {
					expressionPanelGroup.setMarginTop(15.0);
				}
				
				panel.add(expressionPanelGroup);
			}
		}
		
		boolean canAddAnother = hasNoSelections || canOnlyMakeOneSelection;
		
		if(!this.model.getChildModels().isEmpty() && canOnlyMakeOneSelection) {
			
		} else {
			selector = new ExpressionTypeSelector(availableExpressionTypes, availableOperatorTypes, buildButtonObserver, canAddAnother);		
			panel.add(selector);
		}
		
	
		return panel;
	}

	private PanelGroup buildExpressionCollapsePanel(IExpressionBuilderModel model) {	
		Random rand = new Random();
		int index = rand.nextInt(Integer.MAX_VALUE);
		
		PanelGroup expressionPanelGroup = new PanelGroup();
		expressionPanelGroup.setWidth("100%");
		expressionPanelGroup.setId("accordion" + index);
		
		Panel expressionPanel = new Panel();
		expressionPanel.setType(PanelType.SUCCESS);
		expressionPanel.setWidth("100%");
		expressionPanel.setMarginLeft(0.0);
		PanelHeader expressionPanelHeader = new PanelHeader();
		Anchor anchor = new Anchor();
		anchor.setText(panelHeader(model));
		anchor.setTitle(panelHeader(model));
		anchor.setIcon(IconType.PLUS);
		anchor.setColor("black");
		anchor.setDataParent("#accordion" + index);
		anchor.setDataToggle(Toggle.COLLAPSE);
		anchor.setDataTarget("#collapse" + index);
		expressionPanelHeader.add(anchor);
		
		PanelCollapse expressionPanelCollapse = new PanelCollapse();
		expressionPanelCollapse.setId("collapse" + index);
		PanelBody expressionPanelBody = new PanelBody();		
		
		FocusPanel cqlLogicFocusPanel = new FocusPanel();
		cqlLogicFocusPanel.getElement().setAttribute("aria-label", model.getCQL(""));
		Pre cqlPre = new Pre();
		cqlPre.setText(model.getCQL(""));
		cqlPre.setTitle(model.getCQL(""));
		cqlLogicFocusPanel.add(cqlPre);

		expressionPanelBody.add(cqlLogicFocusPanel);
		expressionPanelCollapse.add(expressionPanelBody);

		expressionPanel.add(expressionPanelHeader);
		expressionPanel.add(expressionPanelCollapse);
		
		expressionPanelGroup.add(expressionPanel);
		
		anchor.addClickHandler(event -> onAnchorClick(anchor));

		return expressionPanelGroup;
	}
	
	private void onAnchorClick(Anchor anchor) {
		if(anchor.getIcon() == IconType.PLUS) {
			anchor.setIcon(IconType.MINUS);
		} else {
			anchor.setIcon(IconType.PLUS);
		}
	}

	private String panelHeader(IExpressionBuilderModel model) {
		if(model instanceof RetrieveModel) {
			return ExpressionType.RETRIEVE.getDisplayName();
		} else if(model instanceof DefinitionModel) {
			return ExpressionType.DEFINITION.getDisplayName();
		} else if(model instanceof ExistsModel) {
			return ExpressionType.EXISTS.getDisplayName();
		} else if(model instanceof NotModel) {
			return ExpressionType.NOT.getDisplayName();
		} else if(model instanceof IsNullModel) {
			return ExpressionType.IS_NULL_NOT_NULL.getDisplayName();
		} else if(model instanceof IsTrueFalseModel) {
			return ExpressionType.IS_TRUE_FALSE.getDisplayName();
		} else if(model instanceof ComparisonModel) {
			return ExpressionType.COMPARISON.getDisplayName();
		} else if(model instanceof IntervalModel) {
			return ExpressionType.INTERVAL.getDisplayName();
		} else if(model instanceof QueryModel) {
			return ExpressionType.QUERY.getDisplayName();
		} else if(model instanceof ParameterModel) {
			return ExpressionType.PARAMETER.getDisplayName();
		} else if(model instanceof ValuesetModel) {
			return ExpressionType.VALUESET.getDisplayName();
		} else if(model instanceof CodeModel) {
			return ExpressionType.CODE.getDisplayName();
		}
		
		return "";
	}
	
}
