package mat.client.expressionbuilder.modal;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.component.ViewCQLExpressionWidget;
import mat.client.expressionbuilder.constant.ExpressionBuilderUserAssistText;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.RelationshipModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;
import mat.client.shared.SpacerWidget;
import mat.shared.CQLModelValidator;
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

import java.util.ArrayList;
import java.util.List;

public class RelationshipBuilderModal extends SubExpressionBuilderModal {

	public static final String SOURCE = "Related Source";

	public static final String CRITERIA = "Relationship Criteria";
	public static final String REVIEW = "Review Relationship";
	private static final String EXIT_RELATIONSHIP = "Exit Relationship";
	
	private static final String STYLE = "style";
	private static final String SELECTORS_PANEL = "selectorsPanel";
	private static final String NAV_PILL_BACKGROUND_COLOR = "background-color: #F1F1F1";
	
	private static final String ALIAS_TEXT_BOX_LABEL = "What would you like to name (alias) your related source?";
	private static final String RELATED_SOURCE_TEXT_LABEL = "What expression type would you like to use as your related source?";
	private static final String RELATED_CRITERIA_TEXT_LABEL = "What would you like to use to show relationship between the sources?";
	private static final String ERROR_MESSAGE = "A Related Source, a Related Source Alias, and Relationship Criteria are required.";

	private String alias = "";
	private String currentScreen = SOURCE;

	private AnchorListItem sourceListItem;
	private AnchorListItem criteriaListItem;
	private AnchorListItem reviewListItem;

	private Button previousButton;
	private Button nextButton;

	private HandlerRegistration previousHandler;
	private HandlerRegistration nextHandler;

	private ExpressionTypeSelectorList sourceSelector;
	private ExpressionTypeSelectorList criteriaSelector;

	private final BuildButtonObserver sourceBuildButtonObserver;
	private final BuildButtonObserver criteriaBuildButtonObserver;

	private NavPills pills;

	private final RelationshipModel relationshipModel;

	private VerticalPanel relationshipBuilderContentPanel;
	
	private String clause;
	
	private DescriptionData description;
	private FocusPanel focusPanel;
	
	public RelationshipBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel,
			String selectedClause) {
		super(SOURCE, parent, parentModel, mainModel);
		clause = selectedClause;

		relationshipModel = new RelationshipModel(parentModel);
		getParentModel().appendExpression(relationshipModel);

		sourceBuildButtonObserver = new BuildButtonObserver(this, relationshipModel.getSource(), mainModel);
		criteriaBuildButtonObserver = new BuildButtonObserver(this, relationshipModel.getCriteria(), mainModel);

		getApplyButton().setVisible(false);
		getApplyButton().addClickHandler(event -> onApplyButtonClick());

		display();
	}

	@Override
	public void display() {
		getErrorAlert().clearAlert();	
		getContentPanel().add(buildContentPanel());
		updateCQLDisplay();
		navigate(currentScreen);
	}
	
	private Widget buildContentPanel() {
		getCancelButton().setTitle(EXIT_RELATIONSHIP);
		getCancelButton().setText(EXIT_RELATIONSHIP);
		getContentPanel().clear();
		
		final VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		
		final HorizontalPanel navPillsAndContentPanel = new HorizontalPanel();
		navPillsAndContentPanel.setWidth("100%");		
		
		relationshipBuilderContentPanel = new VerticalPanel();
		relationshipBuilderContentPanel.setStyleName(SELECTORS_PANEL);
		
		navPillsAndContentPanel.add(buildNavPanel());
		pills.getElement().getParentElement().setAttribute(STYLE, "vertical-align: top; width: 20%");
		navPillsAndContentPanel.add(relationshipBuilderContentPanel);
		
		final ButtonToolBar buttonPanel = new ButtonToolBar();
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

		buildSourceNav();
		buildCriteriaNav();
		buildReviewNav();
		
		return buildQueryNavPills();
	}
	
	private void buildSourceNav() {
		sourceListItem = new AnchorListItem(SOURCE);
		sourceListItem.addClickHandler(event -> navigate(SOURCE));
		sourceListItem.getElement().setAttribute(STYLE, NAV_PILL_BACKGROUND_COLOR);
		sourceListItem.setActive(true);
	}

	private void buildCriteriaNav() {
		criteriaListItem = new AnchorListItem(CRITERIA);
		criteriaListItem.addClickHandler(event -> navigate(CRITERIA));
		criteriaListItem.getElement().setAttribute(STYLE, NAV_PILL_BACKGROUND_COLOR);
	}

	private void buildReviewNav() {
		reviewListItem = new AnchorListItem(REVIEW);
		reviewListItem.addClickHandler(event -> navigate(REVIEW));
		reviewListItem.getElement().setAttribute(STYLE, NAV_PILL_BACKGROUND_COLOR);
	}
	
	private Widget buildQueryNavPills() {
		pills = new NavPills();
		pills.setWidth("163px");
		pills.setMarginRight(15.0);

		pills.add(sourceListItem);
		pills.add(criteriaListItem);
		pills.add(reviewListItem);
		pills.setStacked(true);
		
		return pills;
	}
	
	private void onApplyButtonClick() {
		final CQLModelValidator validator = new CQLModelValidator();
		
		if (relationshipModel.getSource().getChildModels().isEmpty() || relationshipModel.getCriteria().getChildModels().isEmpty()
				|| relationshipModel.getAlias().isEmpty()) {
			getErrorAlert().createAlert(ERROR_MESSAGE);
		} else if(relationshipModel.getAlias().isEmpty() || !validator.isValidQDMName(relationshipModel.getAlias())) {
			getErrorAlert().createAlert("The name of your source must start with an alpha character and can not contain spaces or special characters other than an underscore.");
		} else {
			getExpressionBuilderParent().showAndDisplay();
		}
	}
	
	private void navigate(String text) {
		unActivateTabs();
		getApplyButton().setVisible(false);
		displayCurrentTab(text);
		getErrorAlert().clearAlert();
		updateCQLDisplay();
	}
	
	private void unActivateTabs() {
		sourceListItem.setActive(false);		
		criteriaListItem.setActive(false);
		reviewListItem.setActive(false);
	}
	
	private void displayCurrentTab(String tab) {
		currentScreen = tab;
		setCQLPanelVisible(true);

		switch(tab) {
		case SOURCE:
			displaySource();
			break;

		case CRITERIA:
			displayCriteria();
			break;

		case REVIEW:
			displayReviewQuery();
			break;	

		default: break;
		}

	}

	private void displaySource() {
		updateTitle(SOURCE);
		previousButton.setVisible(false);
		updateNextButton(CRITERIA, event -> navigate(CRITERIA));
		relationshipBuilderContentPanel.clear();
		relationshipBuilderContentPanel.add(buildSourceWidget());
		
		sourceListItem.setActive(true);
		if (sourceSelector.getSelector() != null && sourceSelector.getSelector().getExpressionTypeSelectorListBox() != null) {
			sourceSelector.getSelector().getExpressionTypeSelectorListBox().setFocus(true);
		}
	}
	
	private void displayCriteria() {
		updateTitle(CRITERIA);
		updatePreviousButton(SOURCE, event -> navigate(SOURCE));
		updateNextButton(REVIEW, event -> navigate(REVIEW));
		relationshipBuilderContentPanel.clear();
		relationshipBuilderContentPanel.add(buildCriteriaWidget());
		
		criteriaListItem.setActive(true);
		if (criteriaSelector.getSelector() != null && criteriaSelector.getSelector().getExpressionTypeSelectorListBox() != null) {
			criteriaSelector.getSelector().getExpressionTypeSelectorListBox().setFocus(true);
		}
	}
	
	private void displayReviewQuery() {
		updateTitle(REVIEW);
		reviewListItem.setActive(true);
		getApplyButton().setVisible(true);

		nextButton.setVisible(false);
		updatePreviousButton(CRITERIA, event -> navigate(CRITERIA));
		
		relationshipBuilderContentPanel.clear();
		relationshipBuilderContentPanel.add(buildReviewQueryWidget());
		
		setCQLPanelVisible(false);
	}
	
	private Widget buildSourceWidget() {
		VerticalPanel sourcePanel = buildFocusPanel();
		
		sourcePanel.setStyleName(SELECTORS_PANEL);
		final List<ExpressionType> availableExpressionsForSouce = new ArrayList<>();
		availableExpressionsForSouce.add(ExpressionType.RETRIEVE);
		availableExpressionsForSouce.add(ExpressionType.DEFINITION);
		availableExpressionsForSouce.add(ExpressionType.FUNCTION);
		availableExpressionsForSouce.add(ExpressionType.QUERY);
		final List<OperatorType> availableOperatorsForSource = new ArrayList<>(OperatorTypeUtil.getSetOperators());
		
		sourceSelector = new ExpressionTypeSelectorList(availableExpressionsForSouce, availableOperatorsForSource, sourceBuildButtonObserver, relationshipModel.getSource(), 
				RELATED_SOURCE_TEXT_LABEL, this);
		
		sourcePanel.add(sourceSelector);
		
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(new SpacerWidget());
		sourcePanel.add(buildAliasNameGroup());
		
		return sourcePanel;
	}
	
	private Widget buildCriteriaWidget() {
		VerticalPanel filterPanel = buildFocusPanel();
		
		filterPanel.setStyleName(SELECTORS_PANEL);
		final List<ExpressionType> availableExpressionsForFilter = new ArrayList<>();
		availableExpressionsForFilter.add(ExpressionType.COMPARISON);
		availableExpressionsForFilter.add(ExpressionType.DEFINITION);
		availableExpressionsForFilter.add(ExpressionType.EXISTS);
		availableExpressionsForFilter.add(ExpressionType.FUNCTION);
		availableExpressionsForFilter.add(ExpressionType.IN);
		availableExpressionsForFilter.add(ExpressionType.NOT);
		availableExpressionsForFilter.add(ExpressionType.IS_NULL_NOT_NULL);
		availableExpressionsForFilter.add(ExpressionType.TIMING);
		availableExpressionsForFilter.add(ExpressionType.IS_TRUE_FALSE);
		
		final List<OperatorType> availableOperatorsForFilter = new ArrayList<>(OperatorTypeUtil.getBooleanOperators());
		
		criteriaSelector = new ExpressionTypeSelectorList(availableExpressionsForFilter, availableOperatorsForFilter, criteriaBuildButtonObserver, relationshipModel.getCriteria(), 
				RELATED_CRITERIA_TEXT_LABEL, this);
		
		filterPanel.add(criteriaSelector);		
		return filterPanel;
	}
	
	private Widget buildReviewQueryWidget() {
		VerticalPanel viewPanel = buildFocusPanel();
		
		viewPanel.setStyleName(SELECTORS_PANEL);
		final ViewCQLExpressionWidget cqlExpressionModal = new ViewCQLExpressionWidget();
		cqlExpressionModal.setCQLDisplay(getMainModel().getCQL(""));
		viewPanel.add(cqlExpressionModal);		
		return viewPanel;
	}
	
	private FormGroup buildAliasNameGroup() {
		final FormGroup group = new FormGroup();
		
		final FormLabel label = new FormLabel();
		label.setText(ALIAS_TEXT_BOX_LABEL);
		label.setTitle(ALIAS_TEXT_BOX_LABEL);
		group.add(label);
		
		final TextBox aliasTextBox = new TextBox();
		aliasTextBox.addChangeHandler(event -> {
			alias = aliasTextBox.getValue();
			relationshipModel.setAlias(alias);
			updateCQLDisplay();
		});
		
		aliasTextBox.setTitle("Enter an Alias");
		aliasTextBox.setText(alias);
		group.add(aliasTextBox);
		group.setWidth("37%");
		
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

	private void updateTitle(String text) {
		setTitle(getExpressionBuilderParent().getModalTitle() + " > " + text);
		setModalTitle(getExpressionBuilderParent().getModalTitle() + " > " + text);
		setLabelText(ExpressionBuilderUserAssistText.getEnumByTitle(text).getValue());
	}
	
	public void setLabelText(String label) {		
		description.setText(label);
		description.setTitle(label);
	}
	
	private Widget buildLabel() {
		description = new DescriptionData();
		description.setStyleName("attr-Label");
		description.setId("queryLabel");
		return description;
	}
	
	private VerticalPanel buildFocusPanel() {
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName(SELECTORS_PANEL);
		
		focusPanel = new FocusPanel(description);
		
		verticalPanel.add(focusPanel);
		verticalPanel.add(new SpacerWidget());
		
		return verticalPanel;
	}

	
	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

}
