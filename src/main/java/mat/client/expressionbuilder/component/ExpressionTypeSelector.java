package mat.client.expressionbuilder.component;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.observer.BuildButtonObserver;

public class ExpressionTypeSelector extends Composite {

	private static final String SELECT_EXPRESSION_TYPE = "-- Select Expression Type --";
	
	private boolean isOperatorVisible;
	private List<ExpressionType> availableExpressionTypes;
	private List<OperatorType> availableOperatorTypes;
	private ListBox expressionTypeSelector;
	private Button buildButton;
	private BuildButtonObserver observer;
	
	public ExpressionTypeSelector(List<ExpressionType> availableExpressionTypes, List<OperatorType> availableOperatorTypes, BuildButtonObserver observer) {
		this.availableExpressionTypes = availableExpressionTypes;
		this.availableOperatorTypes = availableOperatorTypes;
		this.observer = observer;
		
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("50%");
		panel.add(buildExpressionTypeSelectorBuildPanel());
		initWidget(panel);
	}
	
	private Panel buildExpressionTypeSelectorBuildPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		
		FormLabel label = new FormLabel();
		label.setText("What type of expression would you like to build?");		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		expressionTypeSelector = new ListBox();
		expressionTypeSelector.setWidth("100%");
		
		expressionTypeSelector.addItem(SELECT_EXPRESSION_TYPE, SELECT_EXPRESSION_TYPE);
		for(ExpressionType type : this.availableExpressionTypes) {
			expressionTypeSelector.addItem(type.getDisplayName(), type.getValue());
		}
		
		buildButton = new Button();
		buildButton.setText("Build");
		buildButton.setTitle("Build");
		buildButton.setType(ButtonType.PRIMARY);
		buildButton.setMarginLeft(5.0);
		buildButton.setIcon(IconType.WRENCH);
		buildButton.addClickHandler(event -> onBuildButtonClick());
		
		hp.add(expressionTypeSelector);
		hp.add(buildButton);
		
		panel.add(label);
		panel.add(hp);
		
		return panel;
	}
	
	private void onBuildButtonClick() {
		observer.onBuildButtonClick(expressionTypeSelector.getSelectedValue());
	}

	public void setIsOperatorVisible(boolean isOperatorVisible ) {
		this.isOperatorVisible = isOperatorVisible;
	}
}
