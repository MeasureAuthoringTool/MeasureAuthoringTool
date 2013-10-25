package mat.client.codelist;


import mat.client.Mat;
import mat.client.event.UploadRefreshViewEvent;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasSelectAllHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.Code;
import mat.shared.DynamicTabBarFormatter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class AddCodeView.
 */
public class AddCodeView extends AddBaseView implements  ManageCodeListDetailPresenter.AddCodeDisplay{
	
	/** The code input. */
	private TextBox codeInput;
	
	/** The view. */
	private ManageCodesSearchView view;
	
	/** The uploadsuccess messages. */
	private SuccessMessageDisplay uploadsuccessMessages = new SuccessMessageDisplay();
	
	/** The uploaderror messages. */
	private ErrorMessageDisplay uploaderrorMessages = new ErrorMessageDisplay();
	
	/** The excel viewer widget. */
	private ExcelViewerWidget excelViewerWidget = new ExcelViewerWidget();
	
	/** The selected index. */
	private int selectedIndex = 0;
	
	/**
	 * Instantiates a new adds the code view.
	 */
	public AddCodeView(){
		super("Add Code");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.AddBaseView#getValueInput()
	 */
	@Override
	protected Widget getValueInput() {
		if(codeInput == null) {
			codeInput = new TextBox();
			codeInput.setMaxLength(32);
		}
		return codeInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.AddBaseView#getValueInputLabel()
	 */
	@Override
	protected String getValueInputLabel() {
		return "Code";
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.AddBaseView#getSearchView()
	 */
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null) {
			view = new ManageCodesSearchView();
			
		}
		return view;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#setCodeListDetailModel(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public void setCodeListDetailModel(ManageCodeListDetailModel currentDetails) {
		setParentName("Value Set Name:", currentDetails.getName());
	}


	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#getCodeName()
	 */
	@Override
	public HasValue<String> getCodeName() {
		return codeInput;
	}


	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseAddDisplay#buildDataTable(mat.client.shared.search.SearchResults, boolean, int, int, int)
	 */
	@Override
	public void buildDataTable(SearchResults<Code> results,boolean isChecked,int totalNumberofCodes,int totalPages,int currentPage) {
		view.buildManageCodesDataTable(results, true, isChecked,totalNumberofCodes,totalPages,currentPage);
		buildPageSelectionView(totalPages);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#buildUploadForm(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public void buildUploadForm(ManageCodeListDetailModel currentDetails) {
		    // Create a FormPanel and point it at a service.
		    final FormPanel form = new FormPanel();
		    form.setAction(GWT.getModuleBaseURL()+"uploadFormHandler?codeListID="+currentDetails.getID());
		    form.setEncoding(FormPanel.ENCODING_MULTIPART);
		    form.setMethod(FormPanel.METHOD_POST);

		    // Create a panel to hold all of the form widgets.
		    VerticalPanel vPanel = new VerticalPanel();
		    form.setWidget(vPanel);

            FlowPanel messageHolder = new FlowPanel();
            messageHolder.add(uploadsuccessMessages);
            messageHolder.add(uploaderrorMessages);
            
            //create a message Holder
            vPanel.add(messageHolder);
            // Create a FileUpload widget.
		    final FileUpload uploadField = new FileUpload();
            vPanel.add(LabelBuilder.buildLabel(uploadField, "Browse for completed template"));
		    uploadField.setName("uploadFormElement");
		    uploadField.setHeight("20px");
		    vPanel.add(uploadField);

		    Button importButton = new Button("Import", new ClickHandler() {
		    	public void onClick(ClickEvent event) {
			        form.submit();
			      }
			    });
		    importButton.setTitle("Import");
		    importButton.setStyleName("marginTop");
		    vPanel.add(importButton);

		    form.addSubmitHandler(new FormPanel.SubmitHandler() {
		      public void onSubmit(SubmitEvent event) {
		        // This event is fired just before the form is submitted. 
		        if (uploadField.getFilename().length() == 0) {
		        	uploadsuccessMessages.clear();
		    		uploaderrorMessages.clear();
		    		uploaderrorMessages.setMessage(MatContext.get().getMessageDelegate().getFileNotSelectedMessage());
		        	event.cancel();
		        }else{
		        	if(!(uploadField.getFilename().contains(".xls") || uploadField.getFilename().contains(".xlsx"))){
		        		uploadsuccessMessages.clear();
			    		uploaderrorMessages.clear();
			    		uploaderrorMessages.setMessage(MatContext.get().getMessageDelegate().getExcelFileTypeMessage());
		        		event.cancel();
		        	}
		        }
		      }
		    });
		    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
		      public void onSubmitComplete(SubmitCompleteEvent event) {
		        // When the form submission is successfully completed, this event is fired.
		    	String resultHtml = event.getResults();
		    	if(!resultHtml.contains("Success")){
		    		uploadsuccessMessages.clear();
		    		uploaderrorMessages.clear();
		    		uploaderrorMessages.setMessage(resultHtml);
		    	}else{
		    		uploadsuccessMessages.clear();
		    		uploaderrorMessages.clear();
		    		uploadsuccessMessages.setMessage(resultHtml);
		    		MatContext.get().getEventBus().fireEvent(new UploadRefreshViewEvent());
		    	}
		        
		      }
		    });
			uploadFormHolder.clear();
			uploadFormHolder.add(form);
		}
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#buildImportTab()
	 */
	@Override
	public void buildImportTab() {
		    SimplePanel importCodePanel = new SimplePanel();
			importCodePanel.setStyleName("addCodeTab");
			VerticalPanel importCodeWidget = new VerticalPanel();
			importCodeWidget.add(new Label("To import a Value Set:"));
			HorizontalPanel downloadSnippet = new HorizontalPanel();
			downloadSnippet.add(new Label("1. Download the "));
			Anchor downloadAnchor = new Anchor(" preformatted template","import/ImportTemplate.xls");
			downloadAnchor.addStyleName("addCodeTab");
			downloadSnippet.add(downloadAnchor);
			importCodeWidget.add(downloadSnippet);
			importCodeWidget.add(new Label("2. Insert codes with code descriptors (both are required)."));
			importCodeWidget.add(new Label("3. Save the completed template."));
			importCodeWidget.add(new Label("4. Click Browse to browse for the saved template."));
			importCodeWidget.add(new Label("5. Click Import to import the value set template."));
			importCodeWidget.add(new SpacerWidget());
			importCodeWidget.add(new SpacerWidget());
			importCodeWidget.add(uploadFormHolder);
			importCodeWidget.add(new SpacerWidget());
			importCodeWidget.add(excelViewerWidget);
			importCodePanel.add(importCodeWidget);
			MatContext.get().setAriaHidden(importCodePanel, true);//Initially hidden true so JAWS will not read it.
			String title = "Import Value Set";
			DynamicTabBarFormatter format = new DynamicTabBarFormatter();
			String aStr = format.normalTitle(title);
			String bStr = format.selectedTitle(title);
			tabPanelMap.put(aStr,bStr);
			tabPanelMap.put(bStr, aStr);	
			
			
			tabPanel.add(importCodePanel,aStr);
			tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
				
				@Override
				public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
					setAriaHiddenTrueForClosingTab();
					uploadsuccessMessages.clear();
		    		uploaderrorMessages.clear();
		    		successMessages.clear();
		    		errorMessages.clear();
		    	
		    		//Strip arrow off of the tab we are leaving
		    		String tabtitle =tabPanel.getTabBar().getTabHTML(selectedIndex);
		    		String newTitle = tabPanelMap.get(tabtitle);
					tabPanel.getTabBar().setTabHTML(selectedIndex, newTitle);
		    		
					//Add arrow to new tab that is selected
					selectedIndex = event.getItem();
					MatContext.get().setAriaHidden(tabPanel.getWidget(selectedIndex),false);//set Aria-hidden false when the tab has been selected.
		    		tabtitle= tabPanel.getTabBar().getTabHTML(selectedIndex);
		    		newTitle = tabPanelMap.get(tabtitle);
		    		tabPanel.getTabBar().setTabHTML(selectedIndex,newTitle );
		    		Mat.focusSkipLists("MeasureComposer");
				}
			});
	}
	
	/**
	 * Sets the aria hidden true for closing tab.
	 */
	private void setAriaHiddenTrueForClosingTab() {
		MatContext.get().setAriaHidden(tabPanel.getWidget(selectedIndex),true);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#getUploadSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getUploadSuccessMessageDisplay() {
		return uploadsuccessMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#getUploadErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getUploadErrorMessageDisplay() {
		return uploaderrorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#getExternalViewerLink()
	 */
	@Override
	public HasClickHandlers getExternalViewerLink() {
		return excelViewerWidget.getExportViewAnchor();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseAddDisplay#getSelectAllTool()
	 */
	@Override
	public HasSelectAllHandler getSelectAllTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#getCurrentPage()
	 */
	@Override
	public int getCurrentPage() {
		return psv.getCurrentPage();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddCodeDisplay#setCurrentPage(int)
	 */
	@Override
	public void setCurrentPage(int pageNumber) {
		psv.setCurrentPage(pageNumber);
	}
	
	/**
	 * Builds the page selection view.
	 * 
	 * @param totalPagesCount
	 *            the total pages count
	 */
	private void buildPageSelectionView(int totalPagesCount){
		psv.buildPageSelector(totalPagesCount);
		
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseAddDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return psv;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.AddBaseView#getSearchButton()
	 */
	@Override
	protected Button getSearchButton() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
