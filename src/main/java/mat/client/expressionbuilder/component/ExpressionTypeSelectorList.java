package mat.client.expressionbuilder.component;

import java.util.List;

import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Pre;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.RetrieveModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;

public class ExpressionTypeSelectorList extends Composite {

	private ExpressionBuilderModel model;
	private List<ExpressionType> availableExpressionTypes;
	private List<OperatorType> availableOperatorTypes;
	private BuildButtonObserver buildButtonObserver;
	private boolean hasNoSelections;

	public ExpressionTypeSelectorList(List<ExpressionType> availableExpressionTypes, 
			List<OperatorType> availableOperatorTypes, 
			BuildButtonObserver observer, 
			ExpressionBuilderModel model) {
		this.availableExpressionTypes = availableExpressionTypes;
		this.availableOperatorTypes = availableOperatorTypes;
		this.buildButtonObserver = observer;
		this.model = model;
		this.hasNoSelections = this.model.getChildModels().size() == 0;

		initWidget(buildPanel());
	}
	
	private VerticalPanel buildPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		panel.setWidth("100%");
		FormLabel label = new FormLabel();
		label.setText("What type of expression would you like to build?");		
		panel.add(label);
		
		for(int i = 0; i < this.model.getChildModels().size(); i++) {
			ExpressionBuilderModel model = this.model.getChildModels().get(i);
			
			PanelGroup expressionPanelGroup = buildExpressionCollapsePanel(i, model);
			panel.add(expressionPanelGroup);
		}
		
		ExpressionTypeSelector selector = new ExpressionTypeSelector(availableExpressionTypes, availableOperatorTypes, buildButtonObserver, hasNoSelections);
		panel.add(selector);
		
		return panel;
	}

	private PanelGroup buildExpressionCollapsePanel(int index, ExpressionBuilderModel model) {
		PanelGroup expressionPanelGroup = new PanelGroup();
		expressionPanelGroup.setWidth("100%");
		expressionPanelGroup.setId("accordion");
		
		Panel expressionPanel = new Panel();
		expressionPanel.setType(PanelType.INFO);
		expressionPanel.setWidth("100%");
		expressionPanel.setMarginLeft(0.0);
		PanelHeader expressionPanelHeader = new PanelHeader();
		expressionPanelHeader.setText(panelHeader(model));
		expressionPanelHeader.setDataToggle(Toggle.COLLAPSE);
		expressionPanelHeader.setDataParent("#accordion");
		expressionPanelHeader.setDataTarget("#collapse" + index);
		
		PanelCollapse expressionPanelCollapse = new PanelCollapse();
		expressionPanelCollapse.setId("collapse" + index);
		PanelBody expressionPanelBody = new PanelBody();
		
		Pre cqlPre = new Pre();
		cqlPre.setText(model.getCQL());
		
		expressionPanelBody.add(cqlPre);
		expressionPanelCollapse.add(expressionPanelBody);

		expressionPanel.add(expressionPanelHeader);
		expressionPanel.add(expressionPanelCollapse);
		
		expressionPanelGroup.add(expressionPanel);
		return expressionPanelGroup;
	}
	
	private String panelHeader(ExpressionBuilderModel model) {
		if(model instanceof RetrieveModel) {
			return ExpressionType.RETRIEVE.getDisplayName();
		}
		
		return "";
	}
	
}
