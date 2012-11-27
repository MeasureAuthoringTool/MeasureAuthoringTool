package org.ifmc.mat.client.history;

import org.ifmc.mat.DTO.AuditLogDTO;
import org.ifmc.mat.client.shared.ContentWithHeadingWidget;
import org.ifmc.mat.client.shared.ErrorMessageDisplay;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.SaveCancelButtonBar;
import org.ifmc.mat.client.shared.SpacerWidget;
import org.ifmc.mat.client.shared.SuccessMessageDisplay;
import org.ifmc.mat.client.shared.SuccessMessageDisplayInterface;
import org.ifmc.mat.client.shared.search.HasPageSelectionHandler;
import org.ifmc.mat.client.shared.search.HasPageSizeSelectionHandler;
import org.ifmc.mat.client.shared.search.SearchResults;
import org.ifmc.mat.client.shared.search.SearchView;

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

public abstract class HistoryBaseView {
	
	
	private FlowPanel mainPanel = new FlowPanel();
	Label logEntryLabel = new Label();
	Label addComment = new Label();
	private Button saveButton;
	private Button clearButton;
	protected Anchor goBackLink = new Anchor("");
	protected HorizontalPanel horizontalPanel = new HorizontalPanel();
	protected TextArea	inputText = new TextArea();
	protected VerticalPanel addCommentPanel = new VerticalPanel();
	private SaveCancelButtonBar commentButtons = new SaveCancelButtonBar();
	protected Label nameText = new Label("");
	private VerticalPanel logEntryPanel = new VerticalPanel();
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	protected SearchView<AuditLogDTO> view = getSearchView();
	private ErrorMessageDisplay errMsg = new ErrorMessageDisplay();
	
	
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
	public Widget asWidget() {
		return mainPanel;
	}

	protected void setParentName(String label, String name) {
		Label parentLabel = new Label(label);
		Label parentName = new Label(name);
		containerPanel.setCodeListInfo(parentLabel);
		containerPanel.setCodeListInfo(parentName);
	}
	
	
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	
	public HasClickHandlers getClearButton() {
		return clearButton;
	}

	
	public HasValue<String> getUserComment() {
		return inputText;
	}
	

	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}

	
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}

	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}

	public int getPageSize() {
		return view.getPageSize();
	}

	public void setPageSize(int pageSize){
		view.setPageSize(pageSize);
	}
	
	
	public int getCurrentPage(){
		return view.getCurrentPage();
	}

	
	public void setCurrentPage(int pageNumber){
		view.setCurrentPage(pageNumber);
	}
	
	
	public void buildDataTable(SearchResults<AuditLogDTO> results,int pageCount,long totalResults,int currentPage,int pageSize) {
		view.buildHistoryDataTable(results,pageCount,totalResults,currentPage,pageSize);
	}
	

	public SearchView<AuditLogDTO> getSearchView() {
		if(view == null) {
			view = new SearchView<AuditLogDTO>();
		}
		return view;
	}
	public void setErrorMessage(String s){
		errMsg.clear();
		errMsg.setMessage(s);
	}
	public void clearErrorMessage(){
		errMsg.clear();
	}
	
	public void reset(){
		clearErrorMessage();
		inputText.setText("");
		view.setCurrentPage(SearchView.DEFAULT_PAGE);
	}
	
	public void setUserCommentsReadOnly(boolean b){
		inputText.setReadOnly(b);
		saveButton.setEnabled(!b);
		clearButton.setEnabled(!b);
	}

}

	

