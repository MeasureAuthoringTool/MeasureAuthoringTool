package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.QueryModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;
import mat.client.shared.SpacerWidget;
import mat.shared.CQLModelValidator;

public class QueryBuilderModal extends SubExpressionBuilderModal {

	private static final String ALIAS_TEXT_BOX_LABEL = "What would you like to name (alias) your source?";
	private AnchorListItem reviewQueryListItem;
	private AnchorListItem filterListItem;
	private AnchorListItem sortListItem;
	private AnchorListItem sourceListItem;
	private Button previousButton;
	private Button nextButton;
	private QueryModel queryModel;
	private VerticalPanel queryBuilderContentPanel;
	private BuildButtonObserver sourceBuildButtonObserver;
	private TextBox aliasTextBox;
	
	private String alias = "";

	public QueryBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("Query", parent, parentModel, mainModel);
		queryModel = new QueryModel();
		
		sourceBuildButtonObserver = new BuildButtonObserver(this, queryModel.getSource(), mainModel);
		
		this.setCQLPanelVisible(false);
		this.getApplyButton().setVisible(false);
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
		display();
	}
	
	private void onApplyButtonClick() {
		CQLModelValidator validator = new CQLModelValidator();
		
		if(queryModel.getSource().getChildModels().size() == 0 || queryModel.getFilter().getChildModels().size() == 0) {
			this.getErrorAlert().createAlert("A source and filter are required for a query.");
		} else if(queryModel.getAlias().isEmpty() || !validator.doesAliasNameFollowCQLAliasNamingConvention(queryModel.getAlias())) {
			this.getErrorAlert().createAlert("The name of your source must start with an alpha character and can not contain spaces or special characters other than an underscore.");
		} else {
			this.getParentModel().appendExpression(queryModel);
		}
	}

	@Override
	public void display() {
		this.getErrorAlert().clearAlert();	
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
		navigateToSource();
	}

	private Widget buildContentPanel() {
		this.getContentPanel().clear();
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.add(buildNavPanel());		
		
		queryBuilderContentPanel = new VerticalPanel();
		queryBuilderContentPanel.setStyleName("selectorsPanel");
		horizontalPanel.add(queryBuilderContentPanel);
		
		ButtonToolBar buttonPanel = new ButtonToolBar();
		previousButton = new Button("Previous");
		previousButton.setType(ButtonType.LINK);
		nextButton = new Button("Next");
		nextButton.setPull(Pull.RIGHT);
		nextButton.setType(ButtonType.LINK);
		
		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		
		mainPanel.add(horizontalPanel);	
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buttonPanel);
		return mainPanel;
	}
	
	private Widget buildNavPanel() {
		NavPills pills = new NavPills();
		pills.setMarginRight(16.0);
		
		sourceListItem = new AnchorListItem("Source");
		sourceListItem.addClickHandler(event -> navigateToSource());
		sourceListItem.setActive(true);
		
		filterListItem = new AnchorListItem("Filter");
		filterListItem.addClickHandler(event -> navigateToFilter());
		
		sortListItem = new AnchorListItem("Sort");
		sortListItem.addClickHandler(event -> navigateToSort());
		
		reviewQueryListItem = new AnchorListItem("Review Query");
		reviewQueryListItem.addClickHandler(event -> navigateToReview());

		pills.add(sourceListItem);
		pills.add(filterListItem);
		pills.add(sortListItem);
		pills.add(reviewQueryListItem);
		
		pills.setStacked(true);
		
		return pills;
	}
	
	private Widget buildSourceWidget() {
		VerticalPanel sourcePanel = new VerticalPanel();
		sourcePanel.setStylePrimaryName("selectorsPanel");
		
		List<ExpressionType> availableExpressionsForSouce = new ArrayList<>();
		availableExpressionsForSouce.add(ExpressionType.RETRIEVE);
		availableExpressionsForSouce.add(ExpressionType.DEFINITION);
		List<OperatorType> availableOperatorsForSouce = new ArrayList<>(OperatorTypeUtil.getSetOperators());
		
		ExpressionTypeSelectorList sourceSelector = new ExpressionTypeSelectorList(availableExpressionsForSouce, availableOperatorsForSouce, 
				sourceBuildButtonObserver, queryModel.getSource(), 
				"What type of expression would you like to use as your data source?");
		
		sourcePanel.add(sourceSelector);
		
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(buildAliasNameGroup());
		
		return sourcePanel;
	}
	
	private FormGroup buildAliasNameGroup() {
		FormGroup group = new FormGroup();
		
		FormLabel label = new FormLabel();
		label.setText(ALIAS_TEXT_BOX_LABEL);
		label.setTitle(ALIAS_TEXT_BOX_LABEL);
		group.add(label);
		
		aliasTextBox = new TextBox();
		aliasTextBox.addChangeHandler(event -> {
			alias = aliasTextBox.getValue();
			queryModel.setAlias(alias);
		});
		
		aliasTextBox.setTitle("Enter an Alias");
		aliasTextBox.setText(alias);
		group.add(aliasTextBox);
		group.setWidth("36%");
		
		return group;
	}
	
	private void updatePreviousButton(String text, ClickHandler clickHandler) {
		previousButton.setVisible(true);
		previousButton.setText("<<< Go back to " + text);
		previousButton.setTitle(text);
		previousButton.addClickHandler(clickHandler);
	}
	
	private void updateNextButton(String text, ClickHandler clickHandler) {
		nextButton.setVisible(true);
		nextButton.setText("Continue to " + text + " >>>");
		nextButton.setTitle(text);
		nextButton.addClickHandler(clickHandler);
	}

	private void navigateToSource() {
		navigate("Source");
		sourceListItem.setActive(true);
		
		previousButton.setVisible(false);
		updateNextButton("Filter", event -> navigateToFilter());
		
		queryBuilderContentPanel.clear();
		queryBuilderContentPanel.add(buildSourceWidget());
	}

	private void navigateToFilter() {
		navigate("Filter");
		filterListItem.setActive(true);
		
		updatePreviousButton("Source", event -> navigateToSource());
		updateNextButton("Sort", event -> navigateToSort());
		
		queryBuilderContentPanel.clear();

	}

	private void navigateToSort() {
		navigate("Sort");
		sortListItem.setActive(true);
		
		updatePreviousButton("Filter", event -> navigateToFilter());
		updateNextButton("Review Query", event -> navigateToReview());
		
		queryBuilderContentPanel.clear();
	}

	private void navigateToReview() {
		navigate("Review Query");
		reviewQueryListItem.setActive(true);
		this.getApplyButton().setVisible(true);

		nextButton.setVisible(false);
		updatePreviousButton("Sort", event -> navigateToSort());
		
		queryBuilderContentPanel.clear();
	}
	
	private void navigate(String text) {
		this.setTitle("Query > " + text);
		unActivateTabs();
		this.getErrorAlert().clearAlert();
		this.getApplyButton().setVisible(false);
		this.setCQLPanelVisible(false);
		this.updateCQLDisplay();
	}

	private void unActivateTabs() {
		sourceListItem.setActive(false);
		filterListItem.setActive(false);
		sortListItem.setActive(false);
		reviewQueryListItem.setActive(false);
	}
}
