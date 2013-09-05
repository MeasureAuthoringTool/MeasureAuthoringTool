package mat.client.measure.metadata;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.FocusableWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchView;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AddEditMetadataBaseView {
	protected ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	private SimplePanel mainPanel = new SimplePanel();
	
	private Widget codeInput = getValueInput();
	protected SimplePanel emptyTextBoxHolder = new SimplePanel();
	private TextBox descriptionInput = new TextBox();
	protected SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SearchView<?> view = getSearchView();
	protected Button removeButton = new PrimaryButton("Remove Selected");
	protected TextBox otherSpecifyBox = new TextBox();
	
	private FlowPanel fPanel = new FlowPanel();
	
	private SimplePanel rightSideFormHolder = new SimplePanel();
	
	protected abstract Widget getValueInput();
	protected abstract String getValueInputLabel();
	protected abstract SearchView<?> getSearchView();
	protected Button returnButton = new PrimaryButton();
	
	public AddEditMetadataBaseView(){
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		HorizontalPanel mainContentPanel  = new HorizontalPanel();
		mainContentPanel.getElement().setId("mainContentPanel_HorizontalPanel");
		mainContentPanel.add(buildLeftSideForm());
       	mainContentPanel.add(rightSideFormHolder);
		mainPanel.add(mainContentPanel);
		mainPanel.setStyleName("contentPanel");
		containerPanel.setContent(mainPanel);
		buildRightSideForm();
		view.setPageSizeVisible(false);
		view.setViewingNumberVisible(false);
		view.setPageSize(SearchView.PAGE_SIZE_ALL);
	}
	
	private Widget buildLeftSideForm(){
		 VerticalPanel leftSideForm = new VerticalPanel();
		 leftSideForm.addStyleName("manageCodeLeftForm");
		 ScrollPanel codesResults = new ScrollPanel();
	     codesResults.setSize("400px","250px");
	     leftSideForm.add(SkipListBuilder.buildEmbeddedLinkHolder("MetaData"));
	     codesResults.add(view.asWidget());
	     FocusableWidget focusw = new FocusableWidget(codesResults);
	     leftSideForm.add(new SpacerWidget());
	     leftSideForm.add(focusw);
	     leftSideForm.add(buildRemoveButton());
	     return leftSideForm;
	}
	
	private Widget buildRemoveButton(){
		 HorizontalPanel removePanel = new HorizontalPanel();
		removePanel.getElement().setId("removePanel_HorizontalPanel");
	     removePanel.addStyleName("RemoveButton");
	     removePanel.add(removeButton);
	     return removePanel;
	}

	private void buildRightSideForm(){
		descriptionInput.setMaxLength(1000);
		fPanel.addStyleName("manageCodeRightForm");
		Label addLabel = new Label("Add " + getValueInputLabel());
		fPanel.add(addLabel);
		addLabel.addStyleName("bold");
		fPanel.add(errorMessages);
		fPanel.add(successMessages);
		fPanel.add(LabelBuilder.buildLabel(codeInput, getValueInputLabel()));
		fPanel.add(codeInput);
		fPanel.add(new SpacerWidget());
		fPanel.add(emptyTextBoxHolder);
		fPanel.add(new SpacerWidget());
		buttonBar.getSaveButton().setStylePrimaryName("primaryGreyButton");
		buttonBar.getSaveButton().setText("Add to List");
		buttonBar.getSaveButton().setTitle("Add to List");
		buttonBar.getCancelButton().setText("Clear");
		buttonBar.getCancelButton().setTitle("Clear");
		fPanel.add(buttonBar);
		rightSideFormHolder.clear();
		rightSideFormHolder.add(fPanel);
		
	}
	
	public Widget asWidget() {
		return containerPanel;
	}

	public void setTitle(String title) {
		containerPanel.setHeading(title,"");
	}
	
	protected void setParentName(String label, String name) {
		Label parentLabel = new Label(label);
		Label parentName = new Label(name);
		containerPanel.setCodeListInfo(parentLabel);
		containerPanel.setCodeListInfo(parentName);
	}
	
	public void setReturnToLink(String s){
		returnButton.setText(s);
		returnButton.setTitle(s);
		containerPanel.setFooter(returnButton);
	}
   
	public HasValue<String> getCodeDescription() {
		return descriptionInput;
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
}
