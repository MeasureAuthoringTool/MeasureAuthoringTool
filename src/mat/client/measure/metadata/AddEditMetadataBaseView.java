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

/**
 * The Class AddEditMetadataBaseView.
 */
public abstract class AddEditMetadataBaseView {
	
	/** The container panel. */
	protected ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	/** The code input. */
	private Widget codeInput = getValueInput();
	
	/** The empty text box holder. */
	protected SimplePanel emptyTextBoxHolder = new SimplePanel();
	
	/** The description input. */
	private TextBox descriptionInput = new TextBox();
	
	/** The button bar. */
	protected SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/** The success messages. */
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The error messages. */
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The view. */
	private SearchView<?> view = getSearchView();
	
	/** The remove button. */
	protected Button removeButton = new PrimaryButton("Remove Selected");
	
	/** The other specify box. */
	protected TextBox otherSpecifyBox = new TextBox();
	
	/** The panel. */
	private FlowPanel fPanel = new FlowPanel();
	
	/** The right side form holder. */
	private SimplePanel rightSideFormHolder = new SimplePanel();
	
	/**
	 * Gets the value input.
	 * 
	 * @return the value input
	 */
	protected abstract Widget getValueInput();
	
	/**
	 * Gets the value input label.
	 * 
	 * @return the value input label
	 */
	protected abstract String getValueInputLabel();
	
	/**
	 * Gets the search view.
	 * 
	 * @return the search view
	 */
	protected abstract SearchView<?> getSearchView();
	
	/** The return button. */
	protected Button returnButton = new PrimaryButton();
	
	/**
	 * Instantiates a new adds the edit metadata base view.
	 */
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
	
	/**
	 * Builds the left side form.
	 * 
	 * @return the widget
	 */
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
	
	/**
	 * Builds the remove button.
	 * 
	 * @return the widget
	 */
	private Widget buildRemoveButton(){
		 HorizontalPanel removePanel = new HorizontalPanel();
		removePanel.getElement().setId("removePanel_HorizontalPanel");
	     removePanel.addStyleName("RemoveButton");
	     removePanel.add(removeButton);
	     return removePanel;
	}

	/**
	 * Builds the right side form.
	 */
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
	
	/**
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget() {
		return containerPanel;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		containerPanel.setHeading(title,"");
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
	 * Sets the return to link.
	 * 
	 * @param s
	 *            the new return to link
	 */
	public void setReturnToLink(String s){
		returnButton.setText(s);
		returnButton.setTitle(s);
		containerPanel.setFooter(returnButton);
	}
   
	/**
	 * Gets the code description.
	 * 
	 * @return the code description
	 */
	public HasValue<String> getCodeDescription() {
		return descriptionInput;
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
}
