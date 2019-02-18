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

	private static final String REVIEW_QUERY = "Review Query";
	private static final String SORT = "Sort";
	private static final String FILTER = "Filter";
	private static final String SOURCE = "Source";
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
	private String currentScreen = SOURCE;
	
	private String alias = "";
	private BuildButtonObserver filterBuildButtonObserver;
	private NavPills pills;
	private ExpressionTypeSelectorList sourceSelector;
	private ExpressionTypeSelectorList filterSelector;

	public QueryBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("Query", parent, parentModel, mainModel);
		queryModel = new QueryModel();
		
		sourceBuildButtonObserver = new BuildButtonObserver(this, queryModel.getSource(), mainModel);
		filterBuildButtonObserver = new BuildButtonObserver(this, queryModel.getFilter(), mainModel);
		
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
			this.getExpressionBuilderParent().showAndDisplay();
		}
	}

	@Override
	public void display() {
		this.getErrorAlert().clearAlert();	
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
		navigate(currentScreen);
	}

	private Widget buildContentPanel() {
		this.getContentPanel().clear();
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		
		HorizontalPanel navPillsAndContentPanel = new HorizontalPanel();
		navPillsAndContentPanel.setWidth("100%");		
		
		queryBuilderContentPanel = new VerticalPanel();
		queryBuilderContentPanel.setStyleName("selectorsPanel");
		
		navPillsAndContentPanel.add(buildNavPanel());
		pills.getElement().getParentElement().setAttribute("style", "vertical-align: top; width: 20%");
		navPillsAndContentPanel.add(queryBuilderContentPanel);
		
		ButtonToolBar buttonPanel = new ButtonToolBar();
		previousButton = new Button("Previous");
		previousButton.setType(ButtonType.LINK);
		nextButton = new Button("Next");
		nextButton.setPull(Pull.RIGHT);
		nextButton.setType(ButtonType.LINK);
		
		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		
		mainPanel.add(navPillsAndContentPanel);	
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buttonPanel);
		return mainPanel;
	}
	
	private Widget buildNavPanel() {
		pills = new NavPills();
		pills.setWidth("150px");
		pills.setMarginRight(15.0);
		sourceListItem = new AnchorListItem(SOURCE);
		sourceListItem.addClickHandler(event -> navigate(SOURCE));
		sourceListItem.setActive(true);
		
		filterListItem = new AnchorListItem(FILTER);
		filterListItem.addClickHandler(event -> navigate(FILTER));
		
		sortListItem = new AnchorListItem(SORT);
		sortListItem.addClickHandler(event -> navigate(SORT));
		
		reviewQueryListItem = new AnchorListItem(REVIEW_QUERY);
		reviewQueryListItem.addClickHandler(event -> navigate(REVIEW_QUERY));

		pills.add(sourceListItem);
		pills.add(filterListItem);
		pills.add(sortListItem);
		pills.add(reviewQueryListItem);
		
		pills.setStacked(true);
		return pills;
	}
	
	private Widget buildSourceWidget() {
		VerticalPanel sourcePanel = new VerticalPanel();
		sourcePanel.setStyleName("selectorsPanel");
		List<ExpressionType> availableExpressionsForSouce = new ArrayList<>();
		availableExpressionsForSouce.add(ExpressionType.RETRIEVE);
		availableExpressionsForSouce.add(ExpressionType.DEFINITION);
		List<OperatorType> availableOperatorsForSource = new ArrayList<>(OperatorTypeUtil.getSetOperators());
		
		sourceSelector = new ExpressionTypeSelectorList(availableExpressionsForSouce, availableOperatorsForSource, 
				sourceBuildButtonObserver, queryModel.getSource(), 
				"What type of expression would you like to use as your data source?");
		
		
		sourcePanel.add(sourceSelector);
		
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(buildAliasNameGroup());
		
		return sourcePanel;
	}
	
	private Widget buildFilterWidget() {
		VerticalPanel filterPanel = new VerticalPanel();
		filterPanel.setStyleName("selectorsPanel");
		List<ExpressionType> availableExpressionsForFilter = new ArrayList<>();
		availableExpressionsForFilter.add(ExpressionType.COMPARISON);
		availableExpressionsForFilter.add(ExpressionType.DEFINITION);
		availableExpressionsForFilter.add(ExpressionType.EXISTS);
		availableExpressionsForFilter.add(ExpressionType.IN);
		availableExpressionsForFilter.add(ExpressionType.IS_NULL_NOT_NULL);
		availableExpressionsForFilter.add(ExpressionType.IS_TRUE_FALSE);
		
		List<OperatorType> availableOperatorsForFilter = new ArrayList<>(OperatorTypeUtil.getBooleanOperators());
		
		filterSelector = new ExpressionTypeSelectorList(availableExpressionsForFilter, availableOperatorsForFilter, 
				filterBuildButtonObserver, queryModel.getFilter(), 
				 "What would you like to use to filter your source?");
		
		filterPanel.add(filterSelector);		
		return filterPanel;
	}
	
	private FormGroup buildAliasNameGroup() {
		FormGroup group = new FormGroup();
		
		FormLabel label = new FormLabel();
		label.setText(ALIAS_TEXT_BOX_LABEL);
		label.setTitle(ALIAS_TEXT_BOX_LABEL);
		group.add(label);
		
		TextBox aliasTextBox = new TextBox();
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
	
	private void displaySource() {
		updateTitle(SOURCE);
		previousButton.setVisible(false);
		updateNextButton(FILTER, event -> navigate(FILTER));
		queryBuilderContentPanel.clear();
		queryBuilderContentPanel.add(buildSourceWidget());
	}

	private void displayFilter() {
		updateTitle(FILTER);
		updatePreviousButton(SOURCE, event -> navigate(SOURCE));
		updateNextButton(SORT, event -> navigate(SORT));
		queryBuilderContentPanel.clear();
		queryBuilderContentPanel.add(buildFilterWidget());
	}

	private void displaySort() {
		updateTitle(SORT);
		updatePreviousButton(FILTER, event -> navigate(FILTER));
		updateNextButton(REVIEW_QUERY, event -> navigate(REVIEW_QUERY));
		queryBuilderContentPanel.clear();
	}

	private void displayReviewQuery() {
		updateTitle(REVIEW_QUERY);
		reviewQueryListItem.setActive(true);
		this.getApplyButton().setVisible(true);

		nextButton.setVisible(false);
		updatePreviousButton(SORT, event -> navigate(SORT));
		
		queryBuilderContentPanel.clear();
	}
	
	private void navigate(String text) {
		unActivateTabs();
		this.getApplyButton().setVisible(false);
		displayCurrentTab(text);
		this.getErrorAlert().clearAlert();
		this.setCQLPanelVisible(false);
		this.updateCQLDisplay();
	}
	
	private void displayCurrentTab(String tab) {
		this.currentScreen = tab;
		if(tab.equals(SOURCE)) {
			sourceListItem.setActive(true);
			displaySource();
			
			if(sourceSelector.getSelector().getExpressionTypeSelectorListBox() != null) {
				sourceSelector.getSelector().getExpressionTypeSelectorListBox().setFocus(true);
			}
			
		} else if(tab.equals(FILTER)) {
			filterListItem.setActive(true);
			displayFilter();
			
			if(filterSelector.getSelector().getExpressionTypeSelectorListBox() != null) {
				filterSelector.getSelector().getExpressionTypeSelectorListBox().setFocus(true);
			}
			
		} else if(tab.equals(SORT)) {
			sortListItem.setActive(true);
			displaySort();
 		} else if(tab.equals(REVIEW_QUERY)) {
 			reviewQueryListItem.setActive(true);
 			displayReviewQuery();
 		}
	}

	private void unActivateTabs() {
		sourceListItem.setActive(false);
		filterListItem.setActive(false);
		sortListItem.setActive(false);
		reviewQueryListItem.setActive(false);
	}
	
	private void updateTitle(String text) {
		this.setTitle("Query > " + text);
	}
}
