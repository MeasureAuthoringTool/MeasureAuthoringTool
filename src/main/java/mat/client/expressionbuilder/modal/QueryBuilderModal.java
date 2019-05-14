package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.DescriptionData;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.component.ViewCQLExpressionWidget;
import mat.client.expressionbuilder.constant.ExpressionBuilderUserAssistText;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.QueryModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.SpacerWidget;
import mat.shared.CQLModelValidator;

public class QueryBuilderModal extends SubExpressionBuilderModal {

	private static final String EXIT_QUERY = "Exit Query";
	private static final String STYLE = "style";
	private static final String NAV_PILL_BACKGROUND_COLOR = "background-color: #F1F1F1";
	private static final String HOW_WOULD_YOU_LIKE_TO_SORT_THE_DATA = "How would you like to sort the data?";
	private static final String ALIAS_TEXT_BOX_LABEL = "What would you like to name (alias) your source?";
	
	public static final String REVIEW_QUERY = "Review Query";
	public static final String SORT = "Sort (Optional)";
	public static final String FILTER = "Filter";
	public static final String SOURCE = "Source";

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
	private ExpressionTypeSelectorList sortSelector;
	private BuildButtonObserver sortBuildButtonObserver;
	
	private HandlerRegistration previousHandler;
	private HandlerRegistration nextHandler;
	
	private boolean isAscendingSort = true;

	private DescriptionData description;
	private FocusPanel focusPanel;
	
	public QueryBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("Query", parent, parentModel, mainModel);
		queryModel = new QueryModel(parentModel);
		this.getParentModel().appendExpression(queryModel);
		
		sourceBuildButtonObserver = new BuildButtonObserver(this, queryModel.getSource(), mainModel);
		filterBuildButtonObserver = new BuildButtonObserver(this, queryModel.getFilter(), mainModel);
		sortBuildButtonObserver = new BuildButtonObserver(this, queryModel.getSort().getSortExpression(), mainModel);
		
		this.getApplyButton().setVisible(false);
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
		display();
	}
	
	private void onApplyButtonClick() {
		CQLModelValidator validator = new CQLModelValidator();
		
		if(queryModel.getSource().getChildModels().isEmpty() || queryModel.getFilter().getChildModels().isEmpty()) {
			this.getErrorAlert().createAlert("A source and filter are required for a query.");
		} else if(queryModel.getAlias().isEmpty() || !validator.doesAliasNameFollowCQLAliasNamingConvention(queryModel.getAlias())) {
			this.getErrorAlert().createAlert("The name of your source must start with an alpha character and can not contain spaces or special characters other than an underscore.");
		} else {
			this.getExpressionBuilderParent().showAndDisplay();
		}
	}

	@Override
	public void display() {
		this.getErrorAlert().clearAlert();	
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
		navigate(currentScreen);
		focusPanel.setFocus(true);
	}

	private Widget buildContentPanel() {
		this.getCancelButton().setTitle(EXIT_QUERY);
		this.getCancelButton().setText(EXIT_QUERY);
		this.getContentPanel().clear();
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		
		HorizontalPanel navPillsAndContentPanel = new HorizontalPanel();
		navPillsAndContentPanel.setWidth("100%");		
		
		queryBuilderContentPanel = new VerticalPanel();
		queryBuilderContentPanel.setStyleName("selectorsPanel");
		
		navPillsAndContentPanel.add(buildNavPanel());
		pills.getElement().getParentElement().setAttribute(STYLE, "vertical-align: top; width: 20%");
		navPillsAndContentPanel.add(queryBuilderContentPanel);
		
		ButtonToolBar buttonPanel = new ButtonToolBar();
		previousButton = new Button("Previous");
		previousButton.setType(ButtonType.PRIMARY);
		nextButton = new Button("Next");
		nextButton.setPull(Pull.RIGHT);
		nextButton.setType(ButtonType.PRIMARY);
		
		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		
		mainPanel.add(navPillsAndContentPanel);	
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buttonPanel);
		buildLabel();
		return mainPanel;
	}
	
	private Widget buildNavPanel() {
		pills = new NavPills();
		pills.setWidth("150px");
		pills.setMarginRight(15.0);
		sourceListItem = new AnchorListItem(SOURCE);
		sourceListItem.addClickHandler(event -> navigate(SOURCE));
		sourceListItem.getElement().setAttribute(STYLE, NAV_PILL_BACKGROUND_COLOR);
		sourceListItem.setActive(true);
		
		filterListItem = new AnchorListItem(FILTER);
		filterListItem.addClickHandler(event -> navigate(FILTER));
		filterListItem.getElement().setAttribute(STYLE, NAV_PILL_BACKGROUND_COLOR);
		
		sortListItem = new AnchorListItem(SORT);
		sortListItem.addClickHandler(event -> navigate(SORT));
		sortListItem.getElement().setAttribute(STYLE, NAV_PILL_BACKGROUND_COLOR);
		
		reviewQueryListItem = new AnchorListItem(REVIEW_QUERY);
		reviewQueryListItem.addClickHandler(event -> navigate(REVIEW_QUERY));
		reviewQueryListItem.getElement().setAttribute(STYLE, NAV_PILL_BACKGROUND_COLOR);

		pills.add(sourceListItem);
		pills.add(filterListItem);
		pills.add(sortListItem);
		pills.add(reviewQueryListItem);
		
		pills.setStacked(true);
		return pills;
	}
	
	private Widget buildSourceWidget() {
		VerticalPanel sourcePanel = buildFocusPanel();

		List<ExpressionType> availableExpressionsForSouce = new ArrayList<>();
		availableExpressionsForSouce.add(ExpressionType.ATTRIBUTE);	
		availableExpressionsForSouce.add(ExpressionType.RETRIEVE);
		availableExpressionsForSouce.add(ExpressionType.DEFINITION);
		availableExpressionsForSouce.add(ExpressionType.FUNCTION);
		availableExpressionsForSouce.add(ExpressionType.QUERY);
		List<OperatorType> availableOperatorsForSource = new ArrayList<>(OperatorTypeUtil.getSetOperators());
		
		sourceSelector = new ExpressionTypeSelectorList(
				availableExpressionsForSouce, availableOperatorsForSource, QueryFinderHelper.findAliasNames(this.getParentModel()),
				sourceBuildButtonObserver, queryModel.getSource(), 
				"What type of expression would you like to use as your data source?", this);
		
		
		sourcePanel.add(sourceSelector);
		
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(buildAliasNameGroup());
		
		return sourcePanel;
	}
	
	private Widget buildFilterWidget() {
		VerticalPanel filterPanel = buildFocusPanel();
		
		List<ExpressionType> availableExpressionsForFilter = new ArrayList<>();
		availableExpressionsForFilter.add(ExpressionType.COMPARISON);
		availableExpressionsForFilter.add(ExpressionType.DEFINITION);
		availableExpressionsForFilter.add(ExpressionType.EXISTS);
		availableExpressionsForFilter.add(ExpressionType.FUNCTION);
		availableExpressionsForFilter.add(ExpressionType.IN);
		availableExpressionsForFilter.add(ExpressionType.NOT);
		availableExpressionsForFilter.add(ExpressionType.IS_NULL_NOT_NULL);
		availableExpressionsForFilter.add(ExpressionType.TIMING);
		availableExpressionsForFilter.add(ExpressionType.IS_TRUE_FALSE);
		
		List<OperatorType> availableOperatorsForFilter = new ArrayList<>(OperatorTypeUtil.getBooleanOperators());
		
		filterSelector = new ExpressionTypeSelectorList(
				availableExpressionsForFilter, availableOperatorsForFilter, QueryFinderHelper.findAliasNames(this.queryModel),
				filterBuildButtonObserver, queryModel.getFilter(), 
				 "What would you like to use to filter your source?", this);
		
		filterPanel.add(filterSelector);		
		return filterPanel;
	}
	
	private Widget buildSortByWidget() {
		VerticalPanel sortByPanel = buildFocusPanel();
		
		List<ExpressionType> availableExpressionsForSort = new ArrayList<>();
		availableExpressionsForSort.add(ExpressionType.ATTRIBUTE);
		availableExpressionsForSort.add(ExpressionType.TIME_BOUNDARY);
		
		sortSelector = new ExpressionTypeSelectorList(availableExpressionsForSort, new ArrayList<>(), 
				sortBuildButtonObserver, queryModel.getSort().getSortExpression(), 
				"What would you like to sort?", this);
		
		sortByPanel.add(sortSelector);
		sortByPanel.add(new SpacerWidget());
		sortByPanel.add(new SpacerWidget());
		sortByPanel.add(buildSortDirectionFormGroup());
		
		return sortByPanel;
	}

	private Widget buildSortDirectionFormGroup() {
		
		FormGroup sortDirectionFormGroup = new FormGroup();
		FormLabel sortDirectionFormLabel = new FormLabel();
		sortDirectionFormLabel.setText(HOW_WOULD_YOU_LIKE_TO_SORT_THE_DATA);
		sortDirectionFormLabel.setTitle(HOW_WOULD_YOU_LIKE_TO_SORT_THE_DATA);

		
		
		RadioButton ascendingSortRadioButton = new RadioButton("sortDirectionRadioButton", "Ascending");
		RadioButton descendingSortRadioButton = new RadioButton("sortDirectionRadioButton", "Descending");
		ascendingSortRadioButton.setValue(isAscendingSort);
		descendingSortRadioButton.setValue(!isAscendingSort);
		
		ascendingSortRadioButton.addValueChangeHandler(event -> {
			isAscendingSort = event.getValue();
			queryModel.getSort().setAscendingSort(isAscendingSort);
			this.updateCQLDisplay();
		});
		
		descendingSortRadioButton.addValueChangeHandler(event -> {
			isAscendingSort = !event.getValue();
			queryModel.getSort().setAscendingSort(isAscendingSort);
			this.updateCQLDisplay();
		});
		
		
		HorizontalPanel sortDirectionHorizontalPanel = new HorizontalPanel();
		sortDirectionHorizontalPanel.setStyleName("selectorsPanel");
		sortDirectionHorizontalPanel.setWidth("250px");
		sortDirectionHorizontalPanel.add(ascendingSortRadioButton);
		sortDirectionHorizontalPanel.add(descendingSortRadioButton);
		
		sortDirectionFormGroup.add(sortDirectionFormLabel);
		sortDirectionFormGroup.add(sortDirectionHorizontalPanel);

		return sortDirectionFormGroup;
	}
	
	private Widget buildReviewQueryWidget() {
		VerticalPanel reviewPanel = buildFocusPanel();
		
		ViewCQLExpressionWidget cqlExpressionModal = new ViewCQLExpressionWidget();
		cqlExpressionModal.setCQLDisplay(this.getMainModel().getCQL(""));
		reviewPanel.add(cqlExpressionModal);		
		return reviewPanel;
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
			QueryFinderHelper.updateAliasInChildModels(queryModel, queryModel.getAlias(), alias);
			queryModel.setAlias(alias);
			this.updateCQLDisplay();
		});
		
		aliasTextBox.setTitle("Enter an Alias");
		aliasTextBox.setText(alias);
		group.add(aliasTextBox);
		group.setWidth("36%");
		
		return group;
	}
	
	private void updatePreviousButton(String text, ClickHandler clickHandler) {
		if (previousHandler != null) {
			previousHandler.removeHandler();
		}
		previousHandler = previousButton.addClickHandler(clickHandler);
		previousButton.setVisible(true);
		previousButton.setText("<<< Go back to " + text);
		previousButton.setTitle(text);
	}
	
	private void updateNextButton(String text, ClickHandler clickHandler) {
		if (nextHandler != null) {
			nextHandler.removeHandler();
		}
		nextHandler = nextButton.addClickHandler(clickHandler);
		nextButton.setVisible(true);
		nextButton.setText("Continue to " + text + " >>>");
		nextButton.setTitle(text);
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
		queryBuilderContentPanel.add(buildSortByWidget());
	}

	private void displayReviewQuery() {
		updateTitle(REVIEW_QUERY);
		reviewQueryListItem.setActive(true);
		this.getApplyButton().setVisible(true);

		nextButton.setVisible(false);
		updatePreviousButton(SORT, event -> navigate(SORT));
		
		queryBuilderContentPanel.clear();
		queryBuilderContentPanel.add(buildReviewQueryWidget());
	}
	
	private void navigate(String text) {
		unActivateTabs();
		this.getApplyButton().setVisible(false);
		displayCurrentTab(text);
		this.getErrorAlert().clearAlert();
		this.updateCQLDisplay();
		focusPanel.setFocus(true);
	}
	
	private void displayCurrentTab(String tab) {
		this.currentScreen = tab;
		this.setCQLPanelVisible(true);
		if(tab.equals(SOURCE)) {
			sourceListItem.setActive(true);
			displaySource();
			
		} else if(tab.equals(FILTER)) {
			filterListItem.setActive(true);
			displayFilter();
			
		} else if(tab.equals(SORT)) {
			sortListItem.setActive(true);
			displaySort();
			
 		} else if(tab.equals(REVIEW_QUERY)) {
 			reviewQueryListItem.setActive(true);
 			this.setCQLPanelVisible(false);
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
		this.setTitle(this.getExpressionBuilderParent().getModalTitle() + " > " +  "Query > " + text);
		this.setModalTitle(this.getExpressionBuilderParent().getModalTitle() + " > " +  "Query > " + text);
		setLabelText(ExpressionBuilderUserAssistText.getEnumByTitle(text).getValue());
	}
	
	private Widget buildLabel() {
		description = new DescriptionData();
		description.setStyleName("attr-Label");
		description.setId("queryLabel");
		return description;
	}

	public void setLabelText(String label) {		
		description.setText(label);
		description.setTitle(label);
	}
	
	private VerticalPanel buildFocusPanel() {
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("selectorsPanel");
		
		focusPanel = new FocusPanel(description);
		
		verticalPanel.add(focusPanel);
		verticalPanel.add(new SpacerWidget());
		
		return verticalPanel;
	}

}
