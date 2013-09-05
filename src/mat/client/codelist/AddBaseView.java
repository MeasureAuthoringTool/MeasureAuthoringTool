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
import mat.client.shared.search.HasPageSelectionHandler;
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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AddBaseView {
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	private SimplePanel mainPanel = new SimplePanel();
	
	private Widget codeInput = getValueInput();
	protected ValueSetSearchFilterPanel vssfp;
	private Button searchButton = getSearchButton();
	
	private TextAreaWithMaxLength descriptionInput = new TextAreaWithMaxLength();
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SearchView<?> view = getSearchView();
	private Button removeButton = new PrimaryButton("Remove Selected");
	private Anchor returnLink = new Anchor(" ");
	private HTML labelHolder = new HTML();
	private SimplePanel returnToLinkHolder = new SimplePanel();
	protected PageSelectionView psv = new PageSelectionView();
	
	protected VerticalPanel uploadFormHolder = new VerticalPanel();
	protected TabPanel tabPanel = new TabPanel();
	protected HashMap<String,String> tabPanelMap = new HashMap<String,String>();
	protected abstract Widget getValueInput();
	protected abstract Button getSearchButton();
	protected abstract String getValueInputLabel();
	protected abstract SearchView<?> getSearchView();
	
	public ValueSetSearchFilterPanel getVSSFP(){
		return vssfp;
	}
	
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
	
	private Widget buildRemoveButton(){
		 HorizontalPanel removePanel = new HorizontalPanel();
	     removePanel.addStyleName("RemoveButton");
	     removePanel.add(removeButton);
	     return removePanel;
	}

	
	public Widget asWidget() {
		return mainPanel;
	}

	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	public void setTitle(String title) {
		labelHolder.setHTML(LabelBuilder.buildPlainLabelWithAnchor(title,"CodeList"));
	}
	
	public void setReturnToLink(String s){
		returnToLinkHolder.clear();
		//OVERFLOW
		returnLink.setText("<< "+ s);
		returnToLinkHolder.addStyleName("codeListLink");
		returnToLinkHolder.add(returnLink);
	}
	protected void setParentName(String label, String name) {
		Label parentLabel = new Label(label);
		Label parentName = new Label(name);
		containerPanel.setCodeListInfo(parentLabel);
		containerPanel.setCodeListInfo(parentName);
	}
	



	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}

	public HasValue<String> getCodeDescription() {
		return descriptionInput;
	}

	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	

	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}

	public int getPageSize() {
		return view.getPageSize();
	}
	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}
	
	public HasClickHandlers getReturnButton(){
		return returnLink;
	}
	
}
