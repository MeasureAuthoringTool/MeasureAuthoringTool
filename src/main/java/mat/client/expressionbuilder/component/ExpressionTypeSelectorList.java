package mat.client.expressionbuilder.component;

import java.util.ArrayList;
import java.util.List;

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
import mat.client.expressionbuilder.model.DefinitionModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.ModelAndOperatorTypeUtil;
import mat.client.expressionbuilder.model.OperatorModel;
import mat.client.expressionbuilder.model.RetrieveModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;

public class ExpressionTypeSelectorList extends Composite {

	private ExpressionBuilderModel model;
	private List<ExpressionType> availableExpressionTypes;
	private BuildButtonObserver buildButtonObserver;
	private boolean hasNoSelections;

	public ExpressionTypeSelectorList(List<ExpressionType> availableExpressionTypes, 
			BuildButtonObserver observer, 
			ExpressionBuilderModel model) {
		this.availableExpressionTypes = availableExpressionTypes;
		this.buildButtonObserver = observer;
		this.model = model;
		this.hasNoSelections = this.model.getChildModels().size() == 0;

		initWidget(buildPanel());
	}
	
	private VerticalPanel buildPanel() {
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		panel.setWidth("100%");
		FormLabel label = new FormLabel();
		label.setText("What type of expression would you like to build?");		
		panel.add(label);
		
		// set available operators based on the type of the first model
		if(this.model.getChildModels().size() >= 1) {
			CQLType firstType = this.model.getChildModels().get(0).getType();
			availableOperatorTypes.addAll(OperatorTypeUtil.getAvailableOperatorsCQLType(firstType));
		}
		
		for(int i = 0; i < this.model.getChildModels().size(); i++) {
			IExpressionBuilderModel currentChildModel = this.model.getChildModels().get(i);
			
			// operators should not display with the collapsable panel, but should be formatted with code.
			if(currentChildModel instanceof OperatorModel) {
				FocusPanel operatorFocusPanel = new FocusPanel();
				operatorFocusPanel.getElement().setAttribute("aria-label", currentChildModel.getCQL());
				
				Code code = new Code(); 
				code.setText(currentChildModel.getCQL());
				code.setTitle(currentChildModel.getCQL());
				code.setColor("black");
				code.setStyleName("expressionBuilderCode");
				
				operatorFocusPanel.add(code);
				panel.add(operatorFocusPanel);
				
				// all subsequent available operators should be equivalent to the previously selected operator
				availableOperatorTypes.clear();
				availableOperatorTypes.add(ModelAndOperatorTypeUtil.getOperatorModel(currentChildModel));
				
			} else {
				PanelGroup expressionPanelGroup = buildExpressionCollapsePanel(i, currentChildModel);
				
				if(i != 0) {
					expressionPanelGroup.setMarginTop(15.0);
				}
				
				
				panel.add(expressionPanelGroup);
			}
		}
		
		ExpressionTypeSelector selector = new ExpressionTypeSelector(availableExpressionTypes, availableOperatorTypes, buildButtonObserver, hasNoSelections);
		panel.add(selector);
		
		return panel;
	}

	private PanelGroup buildExpressionCollapsePanel(int index, IExpressionBuilderModel model) {		
		PanelGroup expressionPanelGroup = new PanelGroup();
		expressionPanelGroup.setWidth("100%");
		expressionPanelGroup.setId("accordion");
		
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
		anchor.setDataParent("#accordion");
		anchor.setDataToggle(Toggle.COLLAPSE);
		anchor.setDataTarget("#collapse" + index);
		expressionPanelHeader.add(anchor);
		
		PanelCollapse expressionPanelCollapse = new PanelCollapse();
		expressionPanelCollapse.setId("collapse" + index);
		PanelBody expressionPanelBody = new PanelBody();		
		
		FocusPanel cqlLogicFocusPanel = new FocusPanel();
		cqlLogicFocusPanel.getElement().setAttribute("aria-label", model.getCQL());
		Pre cqlPre = new Pre();
		cqlPre.setText(model.getCQL());
		cqlPre.setTitle(model.getCQL());
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
		}
		
		return "";
	}
	
}
