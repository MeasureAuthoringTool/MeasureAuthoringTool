package mat.client.expressionbuilder.modal;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.QueryModel;
import mat.client.shared.SpacerWidget;

public class QueryBuilderModal extends SubExpressionBuilderModal {

	private AnchorListItem reviewQueryListItem;
	private AnchorListItem filterListItem;
	private AnchorListItem sortListItem;
	private AnchorListItem sourceListItem;
	private Button previousButton;
	private Button nextButton;
	private QueryModel queryModel;

	public QueryBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("Query", parent, parentModel, mainModel);
		queryModel = new QueryModel();
		this.setCQLPanelVisible(false);
		this.getApplyButton().setVisible(false);
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
		display();
	}
	
	private void onApplyButtonClick() {
		if(queryModel.getSource().size() == 0 || queryModel.getFilter().size() == 0) {
			this.getErrorAlert().createAlert("A source and filter are required for a query.");
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
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(buildNavPanel());
		
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
		
		sourceListItem = new AnchorListItem("Source");
		sourceListItem.addClickHandler(event -> onSourceAnchorItemClick());
		sourceListItem.setActive(true);
		
		filterListItem = new AnchorListItem("Filter");
		filterListItem.addClickHandler(event -> onFilterListItemClick());
		
		sortListItem = new AnchorListItem("Sort");
		sortListItem.addClickHandler(event -> onSortListItemClick());
		
		reviewQueryListItem = new AnchorListItem("Review Query");
		reviewQueryListItem.addClickHandler(event -> onReviewQueryListItemClick());

		pills.add(sourceListItem);
		pills.add(filterListItem);
		pills.add(sortListItem);
		pills.add(reviewQueryListItem);
		
		pills.setStacked(true);
		
		return pills;
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

	private void onSourceAnchorItemClick() {
		navigateToSource();
	}

	private void navigateToSource() {
		unActivateTabs();
		updateTitle("Source");
		sourceListItem.setActive(true);
		
		previousButton.setVisible(false);
		updateNextButton("Filter", event -> navigateToFilter());
	}
	
	private void onFilterListItemClick() {
		navigateToFilter();
	}

	private void navigateToFilter() {
		unActivateTabs();
		updateTitle("Filter");
		filterListItem.setActive(true);
		
		updatePreviousButton("Source", event -> navigateToSource());
		updateNextButton("Sort", event -> navigateToSort());
	}

	private void onSortListItemClick() {
		navigateToSort();
	}

	private void navigateToSort() {
		unActivateTabs();
		updateTitle("Sort");
		sortListItem.setActive(true);
		
		updatePreviousButton("Filter", event -> navigateToFilter());
		updateNextButton("Review Query", event -> navigateToReview());
	}

	private void onReviewQueryListItemClick() {
		navigateToReview();
	}

	private void navigateToReview() {
		this.getApplyButton().setVisible(true);
		unActivateTabs();
		updateTitle("Review Query");
		reviewQueryListItem.setActive(true);
		
		nextButton.setVisible(false);
		updatePreviousButton("Sort", event -> navigateToSort());
	}
	
	private void updateTitle(String title) {
		this.setTitle("Query > " + title);
	}

	private void unActivateTabs() {
		sourceListItem.setActive(false);
		filterListItem.setActive(false);
		sortListItem.setActive(false);
		reviewQueryListItem.setActive(false);
	}
}
