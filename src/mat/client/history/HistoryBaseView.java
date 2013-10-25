package mat.client.history;

import mat.DTO.AuditLogDTO;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class HistoryBaseView.
 */
public abstract class HistoryBaseView {
	
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The log entry label. */
	Label logEntryLabel = new Label();
	
	/** The add comment. */
	Label addComment = new Label();
	
	/** The save button. */
	private Button saveButton;
	
	/** The clear button. */
	private Button clearButton;
	
	/** The go back link. */
	protected Anchor goBackLink = new Anchor("");
	
	/** The horizontal panel. */
	protected HorizontalPanel horizontalPanel = new HorizontalPanel();
	
	/** The input text. */
	protected TextArea	inputText = new TextArea();
	
	/** The add comment panel. */
	protected VerticalPanel addCommentPanel = new VerticalPanel();
	
	/** The comment buttons. */
	private SaveCancelButtonBar commentButtons = new SaveCancelButtonBar();
	
	/** The name text. */
	protected Label nameText = new Label("");
	
	/** The log entry panel. */
	private VerticalPanel logEntryPanel = new VerticalPanel();
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The success messages. */
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The error messages. */
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The view. */
	protected SearchView<AuditLogDTO> view = getSearchView();
	
	/** The err msg. */
	private ErrorMessageDisplay errMsg = new ErrorMessageDisplay();
	
	
	/**
	 * Instantiates a new history base view.
	 */
	public HistoryBaseView(){
		mainPanel.add(errMsg);
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		nameText.addStyleName("labelStyling");
		logEntryPanel.add(nameText);
		logEntryPanel.add(new SpacerWidget());
		logEntryPanel.add(view.asWidget());
		logEntryPanel.add(new SpacerWidget());
		logEntryPanel.add(goBackLink);
		horizontalPanel.add(logEntryPanel);
		
		SimplePanel hspacer = new SimplePanel();
		hspacer.setWidth("10px");
		horizontalPanel.add(hspacer);
		
		inputText.setText("");
		inputText.setHeight("100px");
		inputText.setWidth("300px");
	
		saveButton = commentButtons.getSaveButton();
		clearButton = commentButtons.getCancelButton();
		
		saveButton.setText("Save");
		saveButton.setTitle("Save");
		clearButton.setText("Clear");
		clearButton.setTitle("Clear");
		
		addCommentPanel.add(new SpacerWidget());
		
		addCommentPanel.add(LabelBuilder.buildLabel( addComment, "Add Comment"));
		inputText.getElement().setAttribute("id", "Add Comment");
		addCommentPanel.add(inputText);
		
		addCommentPanel.add(commentButtons);
	
		addCommentPanel.addStyleName("contentWithHeadingPanel");
		VerticalPanel downShiftPanel = new VerticalPanel();
		SimplePanel addCommentSpacer = new SimplePanel();
		addCommentSpacer.setHeight("46px");
		downShiftPanel.add(addCommentSpacer);
		downShiftPanel.add(addCommentPanel);
		horizontalPanel.add(downShiftPanel);
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(horizontalPanel);
		
		
	}
	
	/**
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget() {
		return mainPanel;
	}

	/**
	 * Sets the parent name.
	 * 
	 * @param label
	 *            the label
	 * @param name
	 *            the name
	 */
	protected void setParentName(String label, String name) {
		Label parentLabel = new Label(label);
		Label parentName = new Label(name);
		containerPanel.setCodeListInfo(parentLabel);
		containerPanel.setCodeListInfo(parentName);
	}
	
	
	/**
	 * Gets the save button.
	 * 
	 * @return the save button
	 */
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	
	/**
	 * Gets the clear button.
	 * 
	 * @return the clear button
	 */
	public HasClickHandlers getClearButton() {
		return clearButton;
	}

	
	/**
	 * Gets the user comment.
	 * 
	 * @return the user comment
	 */
	public HasValue<String> getUserComment() {
		return inputText;
	}
	

	/**
	 * Gets the success message display.
	 * 
	 * @return the success message display
	 */
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}

	
	/**
	 * Gets the error message display.
	 * 
	 * @return the error message display
	 */
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	/**
	 * Gets the page selection tool.
	 * 
	 * @return the page selection tool
	 */
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}

	/**
	 * Gets the page size selection tool.
	 * 
	 * @return the page size selection tool
	 */
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}

	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	public int getPageSize() {
		return view.getPageSize();
	}

	/**
	 * Sets the page size.
	 * 
	 * @param pageSize
	 *            the new page size
	 */
	public void setPageSize(int pageSize){
		view.setPageSize(pageSize);
	}
	
	
	/**
	 * Gets the current page.
	 * 
	 * @return the current page
	 */
	public int getCurrentPage(){
		return view.getCurrentPage();
	}

	
	/**
	 * Sets the current page.
	 * 
	 * @param pageNumber
	 *            the new current page
	 */
	public void setCurrentPage(int pageNumber){
		view.setCurrentPage(pageNumber);
	}
	
	
	/**
	 * Builds the data table.
	 * 
	 * @param results
	 *            the results
	 * @param pageCount
	 *            the page count
	 * @param totalResults
	 *            the total results
	 * @param currentPage
	 *            the current page
	 * @param pageSize
	 *            the page size
	 */
	public void buildDataTable(SearchResults<AuditLogDTO> results,int pageCount,long totalResults,int currentPage,int pageSize) {
		view.buildHistoryDataTable(results,pageCount,totalResults,currentPage,pageSize);
	}
	

	/**
	 * Gets the search view.
	 * 
	 * @return the search view
	 */
	public SearchView<AuditLogDTO> getSearchView() {
		if(view == null) {
			view = new SearchView<AuditLogDTO>();
		}
		return view;
	}
	
	/**
	 * Sets the error message.
	 * 
	 * @param s
	 *            the new error message
	 */
	public void setErrorMessage(String s){
		errMsg.clear();
		errMsg.setMessage(s);
	}
	
	/**
	 * Clear error message.
	 */
	public void clearErrorMessage(){
		errMsg.clear();
	}
	
	/**
	 * Reset.
	 */
	public void reset(){
		clearErrorMessage();
		inputText.setText("");
		view.setCurrentPage(SearchView.DEFAULT_PAGE);
	}
	
	/**
	 * Sets the user comments read only.
	 * 
	 * @param b
	 *            the new user comments read only
	 */
	public void setUserCommentsReadOnly(boolean b){
		inputText.setReadOnly(b);
		saveButton.setEnabled(!b);
		clearButton.setEnabled(!b);
	}

}

	

