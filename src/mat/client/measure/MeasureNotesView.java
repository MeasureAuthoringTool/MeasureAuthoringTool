package mat.client.measure;

import mat.DTO.MeasureNoteDTO;
import mat.client.ImageResources;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.TextAreaWithMaxLength;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MeasureNotesView implements MeasureNotesPresenter.NotesDisplay{
	public static final int PAGE_SIZE_ALL = Integer.MAX_VALUE;
	@SuppressWarnings("unused")
	private static final int[] PAGE_SIZES= new int[] {50, PAGE_SIZE_ALL};
	private VerticalPanel containerPanel = new VerticalPanel();
	
	private SimplePanel simplePanel = new SimplePanel();
	private FlowPanel flowPanel = new FlowPanel ();
	private TextAreaWithMaxLength measureNoteComposer = new TextAreaWithMaxLength();
	private TextBox measureNoteTitle = new TextBox();
	private Button exportButton = new PrimaryButton("Export Notes","primaryButton");
	private Button saveButton = new PrimaryButton("Save","primaryButton");
	private Button cancelButton = new SecondaryButton("Cancel");
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	protected Panel pageSizeSelector = new FlowPanel();
	public SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	protected Panel pageSelector = new HorizontalPanel();
	MeasureNotesModel notesResult = new MeasureNotesModel();
	
	private ClickHandler clickHandler = buildClickHandler();
	
	public static interface Observer {
		public void onDeleteClicked(MeasureNoteDTO result);
		public void onSaveClicked(MeasureNoteDTO measureNoteDTO);
	}
	private Observer observer;
	
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	public HasClickHandlers getExportButton() {
		return exportButton;
	}
	
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
	
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}
	
	public TextAreaWithMaxLength getMeasureNoteComposer() {
		return measureNoteComposer;
	}

	public TextBox getMeasureNoteTitle() {
		return measureNoteTitle;
	}

	public MeasureNotesModel getNotesResult() {
		return notesResult;
	}
	
	public void setNotesResult(MeasureNotesModel notesResult) {
		this.notesResult = notesResult;
	}

	private HTML viewingNumber = new HTML();
	public HTML getViewingNumber() {
		return viewingNumber;
	}
	public void setViewingNumber(HTML viewingNumber) {
		this.viewingNumber = viewingNumber;
	}

	public MeasureNotesView(){
		//displayView();
	}
	
	public void displayView() {
		flowPanel.clear();
		simplePanel.clear();
		containerPanel.clear();
		containerPanel.setStyleName("contentPanel");
		
		simplePanel.getElement().setId("MeasureNotesView_simplePanel");
		
		flowPanel.add(exportButton);
		flowPanel.add(new SpacerWidget());
		flowPanel.add(new SpacerWidget());
		flowPanel.add(successMessageDisplay);
		flowPanel.add(errorMessages);
		flowPanel.add(new SpacerWidget());
		flowPanel.add(buildMeasureComposer());
		flowPanel.add(new SpacerWidget());
		simplePanel.add(flowPanel);
		containerPanel.add(simplePanel);
		containerPanel.add(new SpacerWidget());
		
		FlowPanel fPanel = new FlowPanel();
		
		fPanel.add(new SpacerWidget());
		fPanel.add(new SpacerWidget());
		//notesResult.setData(getDataList());
		fPanel.add(buildDataTable(notesResult));
		fPanel.add(new SpacerWidget());
		containerPanel.add(fPanel);
	}
	
	private Widget buildMeasureComposer(){
		  FlowPanel composerPanel = new FlowPanel();
		  composerPanel.setWidth("875px");
		  composerPanel.setStylePrimaryName("notes_composer_background");
		  Label composerLabel = new Label("Measure Notes Composer");
		  composerLabel.setStyleName("measureComposerLabel");
		  composerPanel.add(composerLabel);
		  composerPanel.add(new SpacerWidget());
		  HorizontalPanel titlePanel = new HorizontalPanel();
		  Label titleLabel= new Label("Title:");
		  titleLabel.setStyleName("bold");
		  Label descLabel= new Label("Description:");
		  descLabel.setStyleName("bold");
		  measureNoteTitle.setWidth("400px");
		  measureNoteTitle.setMaxLength(50);
		  titlePanel.add(measureNoteTitle);
		  composerPanel.add(titleLabel);
		  composerPanel.add(new SpacerWidget());
		  composerPanel.add(titlePanel);
		  composerPanel.add(new SpacerWidget());
		  measureNoteComposer.setHeight("70px");
		  measureNoteComposer.setWidth("80%");
		  measureNoteComposer.setMaxLength(3000);
		  
		  composerPanel.add(descLabel);
		  composerPanel.add(new SpacerWidget());
		  composerPanel.add(measureNoteComposer);
		  composerPanel.add(new SpacerWidget());
		  HorizontalPanel bottomButtonPanel = new HorizontalPanel();
		  composerPanel.add(new SpacerWidget());
		  bottomButtonPanel.add(saveButton);
		  bottomButtonPanel.add(cancelButton);
		  bottomButtonPanel.setWidth("100px");
		  bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		  composerPanel.add(bottomButtonPanel);
		  composerPanel.add(new SpacerWidget());
		  return composerPanel;
		 }
	
	
	public Widget asWidget() {
		return containerPanel;
	}
	
	public ErrorMessageDisplay getErrorMessageDisplay() {
		return errorMessages;
	}
		
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessageDisplay;
	}

	public void setSuccessMessageDisplay(SuccessMessageDisplay successMessageDisplay) {
		this.successMessageDisplay = successMessageDisplay;
	}

	public void cancelComposedNote() {
		getMeasureNoteComposer().setText("");
		getMeasureNoteTitle().setText("");
	}
	
	public ScrollPanel buildDataTable(MeasureNotesModel results) {
		ScrollPanel scrollPanel = new ScrollPanel();
		VerticalPanel mainPanel = new VerticalPanel();
		scrollPanel.setWidget(mainPanel);
		mainPanel.setSpacing(20);
		mainPanel.setWidth("875px");
		//mainPanel.setStylePrimaryName("right_left_border_outset");
		
		VerticalPanel tableHeaderPanel = new VerticalPanel();
		tableHeaderPanel.setWidth("875px");
		tableHeaderPanel.setStylePrimaryName("header_background");
		Widget tableHeaderWidget = createTableHeaderWidget();
		tableHeaderPanel.add(tableHeaderWidget);
		tableHeaderPanel.setCellHorizontalAlignment(tableHeaderWidget, HasHorizontalAlignment.ALIGN_RIGHT);
		
		mainPanel.add(tableHeaderPanel);
		
		if(results != null && results.getData()!=null) {
			
			for(MeasureNoteDTO result : results.getData()){
				createDisclosurePanel(mainPanel,result);
			}
		}		
		return scrollPanel;
	}
	
	private Widget createTableHeaderWidget() {			
			HorizontalPanel headerPanel = new HorizontalPanel();
			headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			headerPanel.setWidth("800px");
			HorizontalPanel noteTitlePanel = new HorizontalPanel();
			//noteTitlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			String title = "Title";
			HTML noteTitle = new HTML(title);
			noteTitle.setTitle(title);
			noteTitle.setWidth("100%");
			noteTitle.setStyleName("bold");
			noteTitlePanel.getElement().setAttribute("tabIndex", "0");
			noteTitlePanel.add(noteTitle);
			
			noteTitlePanel.setCellWidth(noteTitle, "100%");
			headerPanel.add(noteTitlePanel);
			headerPanel.setCellWidth(noteTitlePanel, "25%");
			headerPanel.setCellHorizontalAlignment(noteTitlePanel, HasHorizontalAlignment.ALIGN_LEFT);
			
			HorizontalPanel emailAddrPanel = new HorizontalPanel();
			emailAddrPanel.getElement().setAttribute("tabIndex", "0");
			emailAddrPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			HTML lastModifiedBy = new HTML("Last Modified By");
			lastModifiedBy.setTitle("Last Modified By");
			lastModifiedBy.setWidth("100%");
			lastModifiedBy.setStyleName("bold");
			emailAddrPanel.add(lastModifiedBy);
			
			emailAddrPanel.setCellWidth(lastModifiedBy, "100%");
			headerPanel.add(emailAddrPanel);
			headerPanel.setCellWidth(emailAddrPanel, "30%");
			
			HorizontalPanel lastModifiedDatePanel = new HorizontalPanel();
			lastModifiedDatePanel.getElement().setAttribute("tabIndex", "0");
			lastModifiedDatePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			
			String dateString = "Last Modified Date";
			HTML lastModifiedDate = new HTML(dateString);
			lastModifiedDate.setTitle(dateString);
			lastModifiedDate.setWidth("100%");
			lastModifiedDate.setStyleName("bold");
			lastModifiedDatePanel.add(lastModifiedDate);
			
			lastModifiedDatePanel.setCellWidth(lastModifiedDate, "100%");
			headerPanel.add(lastModifiedDatePanel);
			headerPanel.setCellWidth(lastModifiedDatePanel, "25%");

			HorizontalPanel deleteButtonPanel = new HorizontalPanel();
			deleteButtonPanel.getElement().setAttribute("tabIndex", "0");
			deleteButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			HTML delete = new HTML("Delete");
			delete.setStyleName("bold");
			deleteButtonPanel.add(delete);
			headerPanel.add(deleteButtonPanel);
			headerPanel.setCellWidth(deleteButtonPanel, "10%");
			
			HorizontalPanel editButtonPanel = new HorizontalPanel();
			editButtonPanel.getElement().setAttribute("tabIndex", "0");
			editButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			HTML edit = new HTML("Edit");
			edit.setStyleName("bold");
			editButtonPanel.add(edit);
			headerPanel.add(editButtonPanel);
			headerPanel.setCellWidth(editButtonPanel, "10%");
			return headerPanel;
	}
	
	
	private void createDisclosurePanel(VerticalPanel mainPanel, MeasureNoteDTO result){
		DisclosurePanel notesDisclosurePanel = new DisclosurePanel();
		notesDisclosurePanel.setAnimationEnabled(true);
		notesDisclosurePanel.setOpen(false);
		Widget contentWidget = createDisclosureContentWidget(result);
		Widget headerWidget = createDisclosureHeaderWidget(result,notesDisclosurePanel);
		
		notesDisclosurePanel.setContent(contentWidget);
		notesDisclosurePanel.setWidth("100%");
		
		VerticalPanel widgetPanel = new VerticalPanel();
		widgetPanel.setWidth("875px");
		widgetPanel.setStylePrimaryName("bottom_border_inset");		
		widgetPanel.add(headerWidget);
		widgetPanel.setCellHorizontalAlignment(headerWidget, HasHorizontalAlignment.ALIGN_RIGHT);
		widgetPanel.add(notesDisclosurePanel);
		widgetPanel.add(new SpacerWidget());
		widgetPanel.setStylePrimaryName("notes_table_background");
		mainPanel.add(widgetPanel);				
	}
	
	private Widget createDisclosureContentWidget(MeasureNoteDTO result) {
		TextBox title = new TextBox();
		title.setTitle("Measure Notes Title");
		title.getElement().setAttribute("id", "NoteTitle_"+result.getId());
		title.setWidth("400px");
		title.setMaxLength(50);
		TextAreaWithMaxLength measureNoteDesc = new TextAreaWithMaxLength();
		measureNoteDesc.setTitle("Measure Notes Description");
		measureNoteDesc.getElement().setAttribute("id", "NoteDesc_"+result.getId());
		measureNoteDesc.setWidth("800px");
		measureNoteDesc.setHeight("70px");
		measureNoteDesc.setMaxLength(3000);
		
		HTML titleLabel = new HTML("Title:");
		titleLabel.setStyleName("bold");
		HTML descLabel = new HTML("Description:");
		descLabel.setStyleName("bold");
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("875px");
		vPanel.add(titleLabel);
		vPanel.add(new SpacerWidget());
		title.setText(result.getNoteTitle());
		measureNoteDesc.setText(result.getNoteDesc());
		vPanel.add(title);
		vPanel.add(new SpacerWidget());
		vPanel.add(descLabel);
		vPanel.add(new SpacerWidget());
		vPanel.add(measureNoteDesc);
		vPanel.add(new SpacerWidget());
		vPanel.add(new SpacerWidget());
		HorizontalPanel bottomButtonPanel = new HorizontalPanel();
		
		Button saveButton = new PrimaryButton("Save","primaryButton");
		setId(saveButton, "Save", result.getId());
		saveButton.addClickHandler(clickHandler);
		
		Button cancelButton = new SecondaryButton("Cancel");
		cancelButton.getElement().setAttribute("id", result.getId());
		cancelButton.addClickHandler(cancelClickHandler());
		
		
		bottomButtonPanel.add(saveButton);
		bottomButtonPanel.add(cancelButton);
		bottomButtonPanel.setWidth("150px");
		bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		vPanel.add(bottomButtonPanel);
		vPanel.add(new SpacerWidget());
		return vPanel;
	}
	
	private Widget createDisclosureHeaderWidget(MeasureNoteDTO result, final DisclosurePanel notesDisclosurePanel) {
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		headerPanel.setWidth("800px");
		
		HorizontalPanel noteTitlePanel = new HorizontalPanel();
		
		String title = result.getNoteTitle();
		HTML noteTitle;
		if(title.length() > 25){
			title = title.substring(0,26)+"...";
			noteTitle = new HTML(title);
		}
		else{
			noteTitle = new HTML(result.getNoteTitle());
		}
		noteTitle.setTitle(result.getNoteTitle());
		noteTitle.setWidth("100%");
		noteTitlePanel.add(noteTitle);
		noteTitlePanel.getElement().setAttribute("tabIndex", "0");
		
		noteTitlePanel.setCellWidth(noteTitle, "100%");
		headerPanel.add(noteTitlePanel);
		headerPanel.setCellWidth(noteTitlePanel, "25%");
		headerPanel.setCellHorizontalAlignment(noteTitlePanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		HorizontalPanel emailAddrPanel = new HorizontalPanel();
		emailAddrPanel.getElement().setAttribute("tabIndex", "0");
		
		HTML lastModifiedBy = new HTML(result.getLastModifiedByEmailAddress());
		lastModifiedBy.setTitle(result.getLastModifiedByEmailAddress());
		lastModifiedBy.setWidth("100%");
		lastModifiedBy.getElement().setAttribute("tabIndex", "0");
		
		emailAddrPanel.add(lastModifiedBy);
		emailAddrPanel.setCellWidth(lastModifiedBy, "100%");
		headerPanel.add(emailAddrPanel);
		headerPanel.setCellWidth(emailAddrPanel, "30%");
		
		HorizontalPanel lastModifiedDatePanel = new HorizontalPanel();
		lastModifiedDatePanel.getElement().setAttribute("tabIndex", "0");
		String dateString = result.getLastModifiedDate();
		HTML lastModifiedDate = new HTML(dateString);
		lastModifiedDate.setTitle(dateString);
		lastModifiedDate.setWidth("100%");
		lastModifiedDatePanel.add(lastModifiedDate);
		
		lastModifiedDatePanel.setCellWidth(lastModifiedDate, "100%");
		headerPanel.add(lastModifiedDatePanel);
		headerPanel.setCellWidth(lastModifiedDatePanel, "25%");
		
		CustomButton editButton = (CustomButton) getImage("edit", ImageResources.INSTANCE.g_openPanel(), result.getId());
		editButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				successMessageDisplay.clear();
				errorMessages.clear();
				CustomButton myEditButton = (CustomButton)event.getSource();
				System.out.println("Edit button clicked !!!");
				if(notesDisclosurePanel.isOpen()){
					Image img = myEditButton.getImage();
					img.setResource(ImageResources.INSTANCE.g_openPanel());
				}else{
					Image img = myEditButton.getImage();
					img.setResource(ImageResources.INSTANCE.g_closePanel());
				}
				notesDisclosurePanel.setOpen(!notesDisclosurePanel.isOpen());
			}
				
		});
		
		CustomButton deleteButton =(CustomButton) getImage("Delete",ImageResources.INSTANCE.g_trash(), result.getId()); /*new CustomButton();
		deleteButton.removeStyleName("gwt-button");
		deleteButton.setStylePrimaryName("invisibleButtonText");
		deleteButton.setTitle("Delete");
		deleteButton.setResource(ImageResources.INSTANCE.g_trash(),"Delete");
		deleteButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				System.out.println("Delete button clicked !!!");
				selectedMeasureNote.setDeleted(true);
			}
		});*/
		HorizontalPanel deleteButtonPanel = new HorizontalPanel();
		deleteButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		deleteButtonPanel.add(deleteButton);
		headerPanel.add(deleteButtonPanel);
		headerPanel.setCellWidth(deleteButtonPanel, "10%");
		
		HorizontalPanel editButtonPanel = new HorizontalPanel();
		editButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		editButtonPanel.add(editButton);
		
		headerPanel.add(editButtonPanel);
		headerPanel.setCellWidth(editButtonPanel, "10%");
		
		return headerPanel;
	}
	
	private Widget getImage(String action, ImageResource url, String key) {
		/*SimplePanel holder = new SimplePanel();
		holder.setStyleName("searchTableCenteredHolder");
		holder.getElement().getStyle().setCursor(Cursor.POINTER);*/
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonText");
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		//508 fix - Read only and locked icons do not do anything but they appear to show hand pointer on mouse hover.
		if(!action.equalsIgnoreCase("Read-Only") && !(action.contains("Measure in use")))
			addListener(image);
		else
			image.setEnabled(false);
		/*holder.add(image)*/;
		return image;
	}
	
	private void setId(Widget image, String action, String key) {
		String id = action + "_" + key;
		image.getElement().setAttribute("id", id);
	}
	
	private void addListener(CustomButton image) {
		image.addClickHandler(clickHandler);
	}
	
	private ClickHandler buildClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String id = ((Widget)event.getSource()).getElement().getId();
				int index = id.indexOf('_');
				String action = id.substring(0, index);
				String measureNoteId= id.substring(index+1);
				MeasureNoteDTO measureNoteDTO = getResultForId(measureNoteId);
				if(observer != null) {
					if("Delete".equals(action)) {
						observer.onDeleteClicked(measureNoteDTO);
					}
					if("Save".equals(action)) {
						String noteTitle = "";
						String noteDesc = "";
						VerticalPanel vpanel = (VerticalPanel) ((Widget)event.getSource()).getParent().getParent();
						for(int i=0; i<vpanel.getWidgetCount(); i++) {
							Widget widget = vpanel.getWidget(i);
							if(widget.getElement().getAttribute("id").equalsIgnoreCase("NoteTitle_"+measureNoteId)) {
								noteTitle = ((TextBox)widget).getText();
							}
							if(widget.getElement().getAttribute("id").equalsIgnoreCase("NoteDesc_"+measureNoteId)) {
								noteDesc = ((TextArea)widget).getText();
							}
						}
						if(noteTitle!=null && !noteTitle.trim().isEmpty() && noteDesc!=null && !noteDesc.trim().isEmpty()) {
							measureNoteDTO.setNoteTitle(noteTitle);
							measureNoteDTO.setNoteDesc(noteDesc);
							observer.onSaveClicked(measureNoteDTO);
						}
						else {
							successMessageDisplay.clear();
							errorMessages.setMessage("Text required in Title and Description fields.");
						}
					}
				}
			}
		};
	}
	
	private ClickHandler cancelClickHandler() {		
		return new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				successMessageDisplay.clear();
				errorMessages.clear();
				
				String measureNoteId = ((Widget)event.getSource()).getElement().getId();
				MeasureNoteDTO measureNoteDTO = getResultForId(measureNoteId);
				
				VerticalPanel vpanel = (VerticalPanel) ((Widget)event.getSource()).getParent().getParent();
				for(int i=0; i<vpanel.getWidgetCount(); i++) {
					Widget widget = vpanel.getWidget(i);
					if(widget.getElement().getAttribute("id").equalsIgnoreCase("NoteTitle_"+measureNoteId)) {
						((TextBox)widget).setText(measureNoteDTO.getNoteTitle());
					}
					if(widget.getElement().getAttribute("id").equalsIgnoreCase("NoteDesc_"+measureNoteId)) {
						((TextArea)widget).setText(measureNoteDTO.getNoteDesc());
					}
				}
			}
		};
	}
	
	private MeasureNoteDTO getResultForId(String id) {
		MeasureNoteDTO result = null;
		for(MeasureNoteDTO measureNoteDTO : notesResult.getData()){
			if(id.equals(measureNoteDTO.getId())) {
				result= measureNoteDTO;
				break;
			}
		}
		return result;
	}
	
}
