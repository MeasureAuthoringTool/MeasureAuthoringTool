package mat.client.expressionbuilder.component;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.shared.ListBoxMVP;

public class ExpressionTypeSelector extends Composite {

	private static final String WHAT_WOULD_YOU_LIKE_TO_ADD = "What would you like to add?";
	private static final String SELECT_AN_OPERATOR = "Select an operator";
	private static final String SELECT_AN_OPERATOR_PLACEHOLDER = "-- Select operator --";

	private static final String DO_YOU_NEED_TO_ADD_ON_TO_THIS_EXPRESSION = "Do you need to add on to this expression?";

	private static final String SELECT_EXPRESSION_TYPE = "-- Select Expression Type --";
	
	private boolean isFirstSelection = true;
	private List<ExpressionType> availableExpressionTypes;
	private List<OperatorType> availableOperatorTypes;
	private ListBoxMVP expressionTypeSelectorListBox;
	private ListBoxMVP operatorTypeSelectorListBox;

	private BuildButtonObserver observer;

	private VerticalPanel contentPanel;
	private List<String> availableAliases;
	
	public ExpressionTypeSelector(List<ExpressionType> availableExpressionTypes, List<OperatorType> availableOperatorTypes, List<String> availableAliases, BuildButtonObserver observer, boolean isFirstSelection) {
		this.availableExpressionTypes = availableExpressionTypes;
		this.availableOperatorTypes = availableOperatorTypes;
		this.availableAliases = availableAliases;
		this.observer = observer;
		this.isFirstSelection = isFirstSelection;
		
		contentPanel = new VerticalPanel();
		contentPanel.setWidth("50%");
		contentPanel.add(buildContent());
		initWidget(contentPanel);
	}
				
	public ListBoxMVP getExpressionTypeSelectorListBox() {
		return expressionTypeSelectorListBox;
	}

	private Panel buildContent() {
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		
		if(isFirstSelection) {
			panel.add(buildExpressionTypeSelectorPanel());
		} else {
			Panel addMorePanel = buildAddMorePanel();
			panel.add(addMorePanel);
		}
		
		return panel;
	}
	
	private Panel buildOperatorTypeSelectorPanel() {
		FormGroup group = new FormGroup();
		group.setWidth("73%");
		
		FormLabel selectAnOperatorLabel = new FormLabel();
		selectAnOperatorLabel.setText(SELECT_AN_OPERATOR);
		selectAnOperatorLabel.setTitle(SELECT_AN_OPERATOR);
		selectAnOperatorLabel.setId("selectAnOperatorLabel");
		
		operatorTypeSelectorListBox = new ListBoxMVP();
		operatorTypeSelectorListBox.insertItem(SELECT_AN_OPERATOR_PLACEHOLDER, SELECT_AN_OPERATOR_PLACEHOLDER, SELECT_AN_OPERATOR_PLACEHOLDER);
		for(OperatorType type : this.availableOperatorTypes) {
			operatorTypeSelectorListBox.insertItem(type.getDisplayName(), type.getValue(), type.getDisplayName());
		}
		
		group.add(selectAnOperatorLabel);
		group.add(operatorTypeSelectorListBox);
		return group;
	}

	private Panel buildExpressionTypeSelectorPanel() {
		VerticalPanel expressionTypeSelectorPanel = new VerticalPanel();
		expressionTypeSelectorPanel.setWidth("100%");
		
		if(!isFirstSelection) {
			FormLabel label = new FormLabel();
			label.setText(WHAT_WOULD_YOU_LIKE_TO_ADD);
			label.setTitle(WHAT_WOULD_YOU_LIKE_TO_ADD);
			expressionTypeSelectorPanel.add(label);
		}
		
		
		HorizontalPanel expressionTypeSelectorBuildButtonPanel = new HorizontalPanel();
		expressionTypeSelectorBuildButtonPanel.setWidth("100%");

		
		expressionTypeSelectorListBox = new ListBoxMVP();
		expressionTypeSelectorListBox.setWidth("100%");
		
		expressionTypeSelectorListBox.insertItem(SELECT_EXPRESSION_TYPE, SELECT_EXPRESSION_TYPE, SELECT_EXPRESSION_TYPE);
		for(ExpressionType type : this.availableExpressionTypes) {
			expressionTypeSelectorListBox.insertItem(type.getDisplayName(), type.getValue(), type.getDisplayName());
		}
		
		for(String alias : this.availableAliases) {
			expressionTypeSelectorListBox.insertItem(alias, alias, alias);
		}
		
		Button buildButton = new Button();
		buildButton.setText("Build");
		buildButton.setTitle("Build");
		buildButton.setType(ButtonType.PRIMARY);
		buildButton.setMarginLeft(5.0);
		buildButton.setIcon(IconType.WRENCH);
		buildButton.addClickHandler(event -> onBuildButtonClick());
		
		expressionTypeSelectorBuildButtonPanel.add(expressionTypeSelectorListBox);
		expressionTypeSelectorBuildButtonPanel.add(buildButton);
		expressionTypeSelectorPanel.add(expressionTypeSelectorBuildButtonPanel);
		
		return expressionTypeSelectorPanel;
	}
	
	private VerticalPanel buildExpressionAndOperatorTypeSelector() {
		VerticalPanel expressionAndOperatorSelectorPanel = new VerticalPanel();
		expressionAndOperatorSelectorPanel.setWidth("100%");
		
		expressionAndOperatorSelectorPanel.add(buildOperatorTypeSelectorPanel());
		expressionAndOperatorSelectorPanel.add(buildExpressionTypeSelectorPanel());
		
		
		return expressionAndOperatorSelectorPanel;
	}

	private Panel buildAddMorePanel() {
		VerticalPanel addMorePanel = new VerticalPanel();
		
		addMorePanel.add(buildAddMoreLabel());
		addMorePanel.add(buildAddMoreButton(addMorePanel));
		return addMorePanel;
	}

	private FormLabel buildAddMoreLabel() {
		FormLabel addMoreLabel = new FormLabel();
		addMoreLabel.setText(DO_YOU_NEED_TO_ADD_ON_TO_THIS_EXPRESSION);
		addMoreLabel.setTitle(DO_YOU_NEED_TO_ADD_ON_TO_THIS_EXPRESSION);
		return addMoreLabel;
	}
	
	private Button buildAddMoreButton(VerticalPanel addMorePanel) {
		Button addMoreButton = new Button();
		addMoreButton.setText("Yes, Add More");
		addMoreButton.setTitle("Yes, Add More");
		addMoreButton.setIcon(IconType.PLUS);
		addMoreButton.setType(ButtonType.PRIMARY);
		addMoreButton.addClickHandler(event -> onAddMoreButtonClick(addMorePanel));
		return addMoreButton;
	}
	
	private void onAddMoreButtonClick(VerticalPanel addMorePanel) {
		addMorePanel.clear();
		addMorePanel.add(buildExpressionAndOperatorTypeSelector());
		addMorePanel.setWidth("100%");
		operatorTypeSelectorListBox.setFocus(true);
	}
	
	private void onBuildButtonClick() {
		if(isFirstSelection) {
			if(expressionTypeSelectorListBox.getSelectedIndex() > 0) {
				String expressionType = expressionTypeSelectorListBox.getSelectedValue();
				observer.onBuildButtonClick(expressionType, null);
			}
		} else {
			if(expressionTypeSelectorListBox.getSelectedIndex() > 0 && operatorTypeSelectorListBox.getSelectedIndex() > 0) {
				String expressionType = expressionTypeSelectorListBox.getSelectedValue();
				String operatorType = operatorTypeSelectorListBox.getSelectedValue();
				observer.onBuildButtonClick(expressionType, operatorType);
			}
		}
	}
}
