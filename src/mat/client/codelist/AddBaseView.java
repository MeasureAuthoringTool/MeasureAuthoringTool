package mat.client.codelist;


import java.util.HashMap;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.TextAreaWithMaxLength;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchView;
import mat.shared.DynamicTabBarFormatter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class AddBaseView.
 */
public abstract class AddBaseView {
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	/** The code input. */
	private Widget codeInput = getValueInput();
	
	/** The vssfp. */
	protected ValueSetSearchFilterPanel vssfp;
	
	/** The search button. */
	private Button searchButton = getSearchButton();
	
	/** The description input. */
	private TextAreaWithMaxLength descriptionInput = new TextAreaWithMaxLength();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/** The success messages. */
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The error messages. */
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The view. */
	private SearchView<?> view = getSearchView();
	
	/** The remove button. */
	private Button removeButton = new PrimaryButton("Remove Selected");
	
	/** The return link. */
	private Anchor returnLink = new Anchor(" ");
	
	/** The label holder. */
	private HTML labelHolder = new HTML();
	
	/** The return to link holder. */
	private SimplePanel returnToLinkHolder = new SimplePanel();
	
	/** The psv. */
	protected PageSelectionView psv = new PageSelectionView();
	
	/** The upload form holder. */
	protected VerticalPanel uploadFormHolder = new VerticalPanel();
	
	/** The tab panel. */
	protected TabPanel tabPanel = new TabPanel();
	
	/** The tab panel map. */
	protected HashMap<String,String> tabPanelMap = new HashMap<String,String>();
	
	/**
	 * Gets the value input.
	 * 
	 * @return the value input
	 */
	protected abstract Widget getValueInput();
	
	/**
	 * Gets the search button.
	 * 
	 * @return the search button
	 */
	protected abstract Button getSearchButton();
	
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
	
	/**
	 * Gets the vssfp.
	 * 
	 * @return the vssfp
	 */
	public ValueSetSearchFilterPanel getVSSFP(){
		return vssfp;
	}
	
	/**
	 * Instantiates a new adds the base view.
	 * 
	 * @param title
	 *            the title
	 */
	public AddBaseView(String title){
		
		buttonBar.getCancelButton().setText("Clear");
		buttonBar.getCancelButton().setTitle("Clear");
		
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		
		HorizontalPanel mainContentPanel  = new HorizontalPanel();
		mainContentPanel.getElement().setId("mainContentPanel_HorizontalPanel");
		mainContentPanel.setWidth("100%");
		mainContentPanel.setHeight("300px");
		Widget leftForm = buildLeftSideForm();
		mainContentPanel.add(leftForm);
		
		SimplePanel emptyPanelForSpace = new SimplePanel();
		emptyPanelForSpace.getElement().setAttribute("style", "padding-left:10px");
		mainContentPanel.add(emptyPanelForSpace);
		
		SimplePanel p = new SimplePanel();
		p.setStyleName("decorator");
		p.setWidth("457px");
		p.setHeight("500px");
		
		DynamicTabBarFormatter format = new DynamicTabBarFormatter();
		String aStr = format.normalTitle(title);
		String bStr = format.selectedTitle(title);
		tabPanelMap.put(aStr,bStr);
		tabPanelMap.put(bStr, aStr);	
		
		if(title.equalsIgnoreCase("Add Value Set")){
			tabPanel.add(buildAddValueSetTab(),bStr,true);
		}
		else
			tabPanel.add(buildAddCodeTab(),bStr,true);
		
		tabPanel.setWidth("457px");
		tabPanel.setHeight("300px");
		tabPanel.selectTab(0);
		MatContext.get().setAriaHidden(tabPanel.getWidget(0), "false");//Initial Tab with Aria-hidden false.
		p.add(tabPanel);
		
		mainContentPanel.add(p);
		mainContentPanel.setCellWidth(leftForm, "50%");
		mainContentPanel.setCellWidth(p, "50%");
		
		mainPanel.add(mainContentPanel);
		mainPanel.setStyleName("contentPanel");
		
		descriptionInput.setMaxLength(1000);
		descriptionInput.setSize("300px","50px");
	}
	
	
	/**
	 * Builds the add value set tab.
	 * 
	 * @return the widget
	 */
	private Widget buildAddValueSetTab() {
		SimplePanel addCodePanel = new SimplePanel();
		addCodePanel.setStyleName("addCodeTab");
		VerticalPanel addCodeWidget = new VerticalPanel();
		addCodeWidget.add(new Label("All fields are required"));

		addCodeWidget.add(new SpacerWidget());
		addCodeWidget.add(new Label("Search for a Value Set"));
		vssfp = new ValueSetSearchFilterPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		
		searchButton.setText("Search");
		searchButton.setTitle("Search");
		hPanel.add(vssfp.getPanel());
		SimplePanel spacerPanel = new SimplePanel();
		spacerPanel.setWidth("10px");
		
		hPanel.add(spacerPanel);
		hPanel.add(searchButton);
		addCodeWidget.add(hPanel);
		
		addCodeWidget.add(new SpacerWidget());
//		addCodeWidget.add(successMessages);
//		addCodeWidget.add(errorMessages);
		addCodeWidget.add(new SpacerWidget());
		addCodeWidget.add(LabelBuilder.buildLabel(codeInput, getValueInputLabel()));
		String width ="420px";
		
		codeInput.setWidth(width);
		addCodeWidget.add(codeInput);
		addCodeWidget.add(new SpacerWidget());
		addCodeWidget.add(LabelBuilder.buildLabel(descriptionInput, "Descriptor"));
		
		//descriptionInput.setWidth(width);
		addCodeWidget.add(descriptionInput);
		addCodeWidget.add(new SpacerWidget());
		addCodeWidget.add(buttonBar);
		addCodePanel.add(addCodeWidget);
		return addCodePanel;
	}
	
	
	/**
	 * Builds the add code tab.
	 * 
	 * @return the widget
	 */
	private Widget buildAddCodeTab() {
		SimplePanel addCodePanel = new SimplePanel();
		addCodePanel.setStyleName("addCodeTab");
		VerticalPanel addCodeWidget = new VerticalPanel();
		addCodeWidget.add(new Label("All fields are required"));
		addCodeWidget.add(new SpacerWidget());
//		addCodeWidget.add(successMessages);
//		addCodeWidget.add(errorMessages);
		addCodeWidget.add(new SpacerWidget());
		addCodeWidget.add(LabelBuilder.buildLabel(codeInput, getValueInputLabel()));
		addCodeWidget.add(codeInput);
		addCodeWidget.add(new SpacerWidget());
		addCodeWidget.add(LabelBuilder.buildLabel(descriptionInput, "Descriptor"));
		addCodeWidget.add(descriptionInput);
		addCodeWidget.add(new SpacerWidget());
		addCodeWidget.add(buttonBar);
		addCodePanel.add(addCodeWidget);
		return addCodePanel;
	}
	
	
	
	/**
	 * Builds the left side form.
	 * 
	 * @return the widget
	 */
	private Widget buildLeftSideForm(){
		 VerticalPanel leftSideForm = new VerticalPanel();
		 leftSideForm.addStyleName("manageCodeLeftForm");
		 leftSideForm.add(new SpacerWidget());
		 leftSideForm.add(labelHolder);
		 leftSideForm.add(new SpacerWidget());
		 leftSideForm.add(successMessages);
		 leftSideForm.add(errorMessages);
		 Widget searchResults = view.asWidget();
		 searchResults.setSize("470px","400px");
		 leftSideForm.add(new SpacerWidget());
	     leftSideForm.add(view.asWidget());
	     leftSideForm.add(new SpacerWidget());
	     leftSideForm.add(psv.asWidget());
	     leftSideForm.add(new SpacerWidget());
	     leftSideForm.add(buildRemoveButton());
	     leftSideForm.add(new SpacerWidget());
	     leftSideForm.add(returnToLinkHolder);
	     return leftSideForm;
	}
	
	/**
	 * Builds the remove button.
	 * 
	 * @return the widget
	 */
	private Widget buildRemoveButton(){
		 HorizontalPanel removePanel = new HorizontalPanel();
	     removePanel.addStyleName("RemoveButton");
	     removePanel.add(removeButton);
	     return removePanel;
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
	 * Gets the save button.
	 * 
	 * @return the save button
	 */
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	/**
	 * Gets the cancel button.
	 * 
	 * @return the cancel button
	 */
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		labelHolder.setHTML(LabelBuilder.buildPlainLabelWithAnchor(title,"CodeList"));
	}
	
	/**
	 * Sets the return to link.
	 * 
	 * @param s
	 *            the new return to link
	 */
	public void setReturnToLink(String s){
		returnToLinkHolder.clear();
		//OVERFLOW
		returnLink.setText("<< "+ s);
		returnToLinkHolder.addStyleName("codeListLink");
		returnToLinkHolder.add(returnLink);
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
	 * Gets the success message display.
	 * 
	 * @return the success message display
	 */
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
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
	 * Gets the error message display.
	 * 
	 * @return the error message display
	 */
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
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
	 * Gets the removes the button.
	 * 
	 * @return the removes the button
	 */
	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}
	
	/**
	 * Gets the return button.
	 * 
	 * @return the return button
	 */
	public HasClickHandlers getReturnButton(){
		return returnLink;
	}
	
}
