package mat.client.measure;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;

//import mat.client.util.RichTextToolbar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
//import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
//import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.DTO.MeasureNoteDTO;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;

/**
 * The Class MeasureNotesView.
 */
public class MeasureNotesView implements MeasureNotesPresenter.NotesDisplay{
	
	/** The container panel. */
	private VerticalPanel containerPanel = new VerticalPanel();
	
	/** The simple panel. */
	private SimplePanel simplePanel = new SimplePanel();
	
	/** The flow panel. */
	private FlowPanel flowPanel = new FlowPanel ();
	
	/** The measure note composer. */
	//private TextAreaWithMaxLength measureNoteComposer = new TextAreaWithMaxLength();
	
	/** The measure note title. */
	private TextBox measureNoteTitle = new TextBox();
	
	/** The export button. */
	private Button exportButton = new Button("Export Notes");
	
	SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("measureNotes");
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The success message display. */
	public MessageAlert successMessageDisplay = new SuccessMessageAlert();
	
	/** The notes result. */
	MeasureNotesModel notesResult = new MeasureNotesModel();
	
	/** The editable. */
	private boolean editable = false;
	
	/** The click handler. */
	private ClickHandler clickHandler = buildClickHandler();
	
	/** The observer. */
	private Observer observer;
	
	//private RichTextArea textArea = new RichTextArea();
	
	//private RichTextToolbar toolBar = new RichTextToolbar(textArea);
	
	private Summernote toolBar = new Summernote();
	@Override
	public Summernote getToolBar() {
		return toolBar;
	}

	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		
		/**
		 * On delete clicked.
		 * 
		 * @param result
		 *            the result
		 */
		public void onDeleteClicked(MeasureNoteDTO result);
		
		/**
		 * On save clicked.
		 * 
		 * @param measureNoteDTO
		 *            the measure note dto
		 */
		public void onSaveClicked(MeasureNoteDTO measureNoteDTO);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#setObserver(mat.client.measure.MeasureNotesView.Observer)
	 */
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#getExportButton()
	 */
	@Override
	public HasClickHandlers getExportButton() {
		return exportButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#getMeasureNoteComposer()
	 */
//	@Override
//	public RichTextArea getMeasureNoteComposer() {
//		//return measureNoteComposer;
//		return textArea;
//	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#getMeasureNoteTitle()
	 */
	@Override
	public TextBox getMeasureNoteTitle() {
		return measureNoteTitle;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#getNotesResult()
	 */
	@Override
	public MeasureNotesModel getNotesResult() {
		return notesResult;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#setNotesResult(mat.client.measure.MeasureNotesModel)
	 */
	@Override
	public void setNotesResult(MeasureNotesModel notesResult) {
		this.notesResult = notesResult;
	}
	
	/**
	 * Instantiates a new measure notes view.
	 */
	public MeasureNotesView(){
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#displayView()
	 */
	@Override
	public void displayView() {
		editable = MatContext.get().getMeasureLockService().checkForEditPermission();
		flowPanel.clear();
		simplePanel.clear();
		containerPanel.clear();
		containerPanel.setStyleName("contentPanel");
		
		simplePanel.getElement().setId("MeasureNotesView_simplePanel");
		exportButton.setMarginTop(5.00);
		exportButton.setMarginLeft(5.00);
		exportButton.setType(ButtonType.PRIMARY);
		exportButton.setIcon(IconType.DOWNLOAD);
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
		
		HorizontalPanel noteTitlePanel = new HorizontalPanel();
		
		
		fPanel.add(noteTitlePanel);
		
		fPanel.add(buildDataTable(notesResult));
		fPanel.add(new SpacerWidget());
		containerPanel.add(fPanel);
		containerPanel.getElement().setId("MeasureNoteContainerPanel");
	}
	
	/**
	 * Builds the measure composer.
	 * 
	 * @return the widget
	 */
	private Widget buildMeasureComposer(){
		FlowPanel composerPanel = new FlowPanel();
		composerPanel.setWidth("875px");
		composerPanel.setStylePrimaryName("notes_composer_background");
		Label composerLabel = new Label("Measure Notes Composer");
		composerLabel.setStyleName("measureComposerLabel");
		composerPanel.add(composerLabel);
		composerPanel.add(new SpacerWidget());
		HorizontalPanel titlePanel = new HorizontalPanel();
		Label titleLabel = (Label) LabelBuilder.buildRequiredLabel(new Label(), "Title");
		titleLabel.setStyleName("bold");
		Label descLabel = (Label) LabelBuilder.buildRequiredLabel(new Label(), "Description");
		descLabel.setStyleName("bold");
		measureNoteTitle.setWidth("400px");
		measureNoteTitle.setMaxLength(50);
		measureNoteTitle.addFocusHandler(getFocusHandler());
		measureNoteTitle.getElement().setId("MeasureNoteTitle");
		measureNoteTitle.setTitle("Title");
		titlePanel.add(measureNoteTitle);
		composerPanel.add(titleLabel);
		composerPanel.add(new SpacerWidget());
		composerPanel.add(titlePanel);
		composerPanel.add(new SpacerWidget());
		/* measureNoteComposer.setHeight("70px");
		  measureNoteComposer.setWidth("80%");
		  measureNoteComposer.setMaxLength(3000);
		  measureNoteComposer.addFocusHandler(getFocusHandler());*/
		
		
		composerPanel.add(descLabel);
		composerPanel.add(new SpacerWidget());
		//composerPanel.add(measureNoteComposer);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("70%");
		vp.setStylePrimaryName("recentSearchHeader");
//		textArea.setHeight("100px");
//		textArea.setWidth("100%");
//		textArea.setTitle("Description");
		vp.setTitle("Description Text area");
		vp.add(toolBar);
	//	vp.add(textArea);
		composerPanel.add(vp);
		
		composerPanel.add(new SpacerWidget());
		HorizontalPanel bottomButtonPanel = new HorizontalPanel();
		composerPanel.add(new SpacerWidget());
		
		bottomButtonPanel.add(buttonBar);
		/*bottomButtonPanel.setWidth("100px");*/
		bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		composerPanel.add(bottomButtonPanel);
		composerPanel.add(new SpacerWidget());
		
		measureNoteTitle.setReadOnly(!editable);
		//measureNoteComposer.setReadOnly(!editable);
		buttonBar.getSaveButton().setText("Save");
		buttonBar.getSaveButton().setTitle("Save");
		buttonBar.getSaveButton().setEnabled(editable);
		buttonBar.getCancelButton().setEnabled(editable);
		
		return composerPanel;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return successMessageDisplay;
	}
	
	/**
	 * Sets the success message display.
	 * 
	 * @param successMessageDisplay
	 *            the new success message display
	 */
	public void setSuccessMessageDisplay(MessageAlert successMessageDisplay) {
		this.successMessageDisplay = successMessageDisplay;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.MeasureNotesPresenter.NotesDisplay#cancelComposedNote()
	 */
	@Override
	public void cancelComposedNote() {
		//getMeasureNoteComposer().setText("");
		getMeasureNoteTitle().setText("");
	}
	
	/**
	 * Builds the data table.
	 * 
	 * @param results
	 *            the results
	 * @return the scroll panel
	 */
	public ScrollPanel buildDataTable(MeasureNotesModel results) {
		ScrollPanel scrollPanel = new ScrollPanel();
		VerticalPanel mainPanel = new VerticalPanel();
		scrollPanel.setWidget(mainPanel);
		mainPanel.setSpacing(20);
		mainPanel.setWidth("875px");
		
		VerticalPanel tableHeaderPanel = new VerticalPanel();
		tableHeaderPanel.setWidth("875px");
		tableHeaderPanel.setStylePrimaryName("header_background");
		Widget tableHeaderWidget = createTableHeaderWidget();
		
		tableHeaderPanel.add(tableHeaderWidget);
		tableHeaderPanel.setCellHorizontalAlignment(tableHeaderWidget, HasHorizontalAlignment.ALIGN_RIGHT);
		
		mainPanel.add(tableHeaderPanel);
		
		if((results != null) && (results.getData()!=null)) {
			
			for(MeasureNoteDTO result : results.getData()){
				createDisclosurePanel(mainPanel,result);
			}
		}
		return scrollPanel;
	}
	
	/**
	 * Creates the table header widget.
	 * 
	 * @return the widget
	 */
	private Widget createTableHeaderWidget() {
		
		HorizontalPanel headerPanel = new HorizontalPanel();
		HorizontalPanel subHeaderPanel = new HorizontalPanel();
		subHeaderPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		subHeaderPanel.setWidth("800px");
		HorizontalPanel noteTitlePanel = new HorizontalPanel();
		
		String title = "Title";
		HTML noteTitle = new HTML(title);
		noteTitle.getElement().setAttribute("id", "Title");
		noteTitle.setTitle(title);
		noteTitle.setWidth("100%");
		noteTitle.setStyleName("bold");
		noteTitlePanel.getElement().setAttribute("tabIndex", "0");
		noteTitlePanel.add(noteTitle);
		
		noteTitlePanel.setCellWidth(noteTitle, "100%");
		subHeaderPanel.add(noteTitlePanel);
		subHeaderPanel.setCellWidth(noteTitlePanel, "25%");
		subHeaderPanel.setCellHorizontalAlignment(noteTitlePanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		HorizontalPanel emailAddrPanel = new HorizontalPanel();
		emailAddrPanel.getElement().setAttribute("tabIndex", "0");
		emailAddrPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HTML lastModifiedBy = new HTML("Last Modified By");
		lastModifiedBy.setTitle("Last Modified By");
		lastModifiedBy.setWidth("100%");
		lastModifiedBy.setStyleName("bold");
		emailAddrPanel.add(lastModifiedBy);
		
		emailAddrPanel.setCellWidth(lastModifiedBy, "100%");
		subHeaderPanel.add(emailAddrPanel);
		subHeaderPanel.setCellWidth(emailAddrPanel, "30%");
		
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
		subHeaderPanel.add(lastModifiedDatePanel);
		subHeaderPanel.setCellWidth(lastModifiedDatePanel, "25%");
		
		HorizontalPanel deleteButtonPanel = new HorizontalPanel();
		deleteButtonPanel.getElement().setAttribute("tabIndex", "0");
		deleteButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HTML delete = new HTML("Delete");
		delete.setStyleName("bold");
		deleteButtonPanel.add(delete);
		subHeaderPanel.add(deleteButtonPanel);
		subHeaderPanel.setCellWidth(deleteButtonPanel, "10%");
		
		HorizontalPanel editButtonPanel = new HorizontalPanel();
		editButtonPanel.getElement().setAttribute("tabIndex", "0");
		editButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HTML edit = new HTML("Edit");
		edit.setStyleName("bold");
		editButtonPanel.add(edit);
		subHeaderPanel.add(editButtonPanel);
		subHeaderPanel.setCellWidth(editButtonPanel, "10%");
		/*Label invisibleLabel = new Label("In the following Recent Activity table, Measure Name is given in first column,"
								+ " Version in second column and Export in third column.");
		invisibleLabel.getElement().setAttribute("id","measureNotesSummary");
		invisibleLabel.setStyleName("invisible");
	
		
			headerPanel.add(invisibleLabel);
			subHeaderPanel.getElement().setAttribute("id", "NoteTableTitle");
			subHeaderPanel.getElement().setAttribute("aria-label", "Measure Notes Table");
			subHeaderPanel.getElement().setAttribute("aria-describedby", "measureNotesSummary");
			subHeaderPanel.getElement().setAttribute("aria-role", "panel");
			subHeaderPanel.getElement().setAttribute("aria-live", "assertive");
			subHeaderPanel.getElement().setAttribute("aria-atomic", "true");
			subHeaderPanel.getElement().setAttribute("aria-relevant", "all");
			subHeaderPanel.getElement().setAttribute("role", "alert");*/
		headerPanel.add(subHeaderPanel);
		return headerPanel;
	}
	
	
	/**
	 * Creates the disclosure panel.
	 * 
	 * @param mainPanel
	 *            the main panel
	 * @param result
	 *            the result
	 */
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
	
	/**
	 * Creates the disclosure content widget.
	 * 
	 * @param result
	 *            the result
	 * @return the widget
	 */
	private Widget createDisclosureContentWidget(MeasureNoteDTO result) {
		TextBox title = new TextBox();
		title.setTitle("Title");
		title.setTitle("Measure Notes Title");
		title.getElement().setAttribute("id", "NoteTitle_"+result.getId());
		title.setWidth("400px");
		title.setMaxLength(50);
		//TextAreaWithMaxLength measureNoteDesc = new TextAreaWithMaxLength();
		/*measureNoteDesc.setTitle("Measure Notes Description");
		measureNoteDesc.getElement().setAttribute("id", "NoteDesc_"+result.getId());
		measureNoteDesc.setWidth("800px");
		measureNoteDesc.setHeight("70px");
		measureNoteDesc.setMaxLength(3000);*/
		
		//RichTextArea editTextArea = new RichTextArea();
		//RichTextToolbar editToolbar = new RichTextToolbar(editTextArea);
		Summernote editToolbar = new Summernote();
		Label titleLabel = (Label) LabelBuilder.buildRequiredLabel(new Label(), "Title");
		titleLabel.setStyleName("bold");
		Label descLabel = (Label) LabelBuilder.buildRequiredLabel(new Label(), "Description");
		descLabel.setStyleName("bold");
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("875px");
		vPanel.add(titleLabel);
		vPanel.add(new SpacerWidget());
		title.setText(result.getNoteTitle());
		//editTextArea.setHTML(result.getNoteDesc());
		//editTextArea.setTitle("Decription");
		//editToolbar.setText(result.getNoteDesc());
		editToolbar.setCode(result.getNoteDesc());
		vPanel.setTitle("Description Area");
		//measureNoteDesc.setText(result.getNoteDesc());
		vPanel.add(title);
		vPanel.add(new SpacerWidget());
		vPanel.add(descLabel);
		vPanel.add(new SpacerWidget());
		//vPanel.add(measureNoteDesc);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("70%");
		vp.setStylePrimaryName("recentSearchHeader");
		vp.getElement().setAttribute("id", "NoteDesc_"+result.getId());
		editToolbar.setWidth("100%");
		editToolbar.setHeight("100px");
		vp.add(editToolbar);
//		vp.add(editTextArea);
		vPanel.add(vp);
		
		vPanel.add(new SpacerWidget());
		vPanel.add(new SpacerWidget());
		HorizontalPanel bottomButtonPanel = new HorizontalPanel();
		
		Button saveButton = new Button("Save");
		saveButton.setType(ButtonType.PRIMARY);
		setId(saveButton, "Save", result.getId());
		saveButton.addClickHandler(clickHandler);
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.setTitle("Cancel");
		cancelButton.getElement().setAttribute("id", result.getId());
		cancelButton.addClickHandler(cancelClickHandler());
		
		
		bottomButtonPanel.add(saveButton);
		bottomButtonPanel.add(cancelButton);
		bottomButtonPanel.setWidth("150px");
		bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		vPanel.add(bottomButtonPanel);
		vPanel.add(new SpacerWidget());
		
		title.setReadOnly(!editable);
		//measureNoteDesc.setReadOnly(!editable);
		saveButton.setEnabled(editable);
		cancelButton.setEnabled(editable);
		
		return vPanel;
	}
	
	/**
	 * Creates the disclosure header widget.
	 * 
	 * @param result
	 *            the result
	 * @param notesDisclosurePanel
	 *            the notes disclosure panel
	 * @return the widget
	 */
	private Widget createDisclosureHeaderWidget(MeasureNoteDTO result, final DisclosurePanel notesDisclosurePanel) {
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		headerPanel.setWidth("800px");
		
		HorizontalPanel noteTitlePanel = new HorizontalPanel();
		
		HTML noteTitle;
		noteTitle = new HTML(result.getNoteTitle());
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
		
		
		
		Button editButton = new Button(); 
		editButton.setTitle("Edit");
		editButton.getElement().setId("editButton_Measurenotes");
		editButton.setSize("70px", "30px");
		editButton.getElement().setAttribute("aria-label", "Edit");
		editButton.setType(ButtonType.LINK);
		editButton.setIcon(IconType.ARROW_RIGHT);
		editButton.setIconSize(IconSize.LARGE);
		editButton.setColor("#0964A2");
		editButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				Button myEditButton = (Button)event.getSource();
				if(notesDisclosurePanel.isOpen()){
					myEditButton.setIcon(IconType.ARROW_RIGHT);
				}else{
					myEditButton.setIcon(IconType.ARROW_DOWN);
				}
				notesDisclosurePanel.setOpen(!notesDisclosurePanel.isOpen());
			}
			
		});
		
		/** The delete button. */
		Button deleteButton = new Button();
		deleteButton.setType(ButtonType.LINK);
		deleteButton.getElement().setId("deleteButton_Measurenotes");
		deleteButton.setTitle("Delete");
		deleteButton.setSize("70px", "30px");
		deleteButton.getElement().setAttribute("aria-label", "Delete");
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.setIconSize(IconSize.LARGE);
		deleteButton.setColor("#0964A2");
		
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
		
		deleteButton.setEnabled(editable);
		
		return headerPanel;
	}
	
	/**
	 * Gets the image.
	 * 
	 * @param action
	 *            the action
	 * @param url
	 *            the url
	 * @param key
	 *            the key
	 * @return the image
	 */
	private Widget getImage(String action, ImageResource url, String key) {
		
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonText");
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		addListener(image);
		return image;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param image
	 *            the image
	 * @param action
	 *            the action
	 * @param key
	 *            the key
	 */
	private void setId(Widget image, String action, String key) {
		String id = action + "_" + key;
		image.getElement().setAttribute("id", id);
	}
	
	/**
	 * Adds the listener.
	 * 
	 * @param image
	 *            the image
	 */
	private void addListener(CustomButton image) {
		image.addClickHandler(clickHandler);
	}
	
	/**
	 * Builds the click handler.
	 * 
	 * @return the click handler
	 */
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
							//System.out.println("IN THE LOOP!");
							Widget widget = vpanel.getWidget(i);
							if(widget.getElement().getAttribute("id").equalsIgnoreCase("NoteTitle_"+measureNoteId)) {
								noteTitle = ((TextBox)widget).getText();
							}
							if(widget.getElement().getAttribute("id").equalsIgnoreCase("NoteDesc_"+measureNoteId)) {
								VerticalPanel vp = (VerticalPanel)widget;
								System.out.println("Vertical Panel Widget Count: " + vp.getWidgetCount());
								Widget wid = vp.getWidget(0);
								noteDesc = ((Summernote)wid).getCode();
							}
						}
						//System.out.println("TITLE: " + noteTitle);
						//System.out.println("Desc: " + noteDesc);
						if((noteTitle!=null) && !noteTitle.trim().isEmpty() && (noteDesc!=null) && !noteDesc.trim().isEmpty()) {
							measureNoteDTO.setNoteTitle(noteTitle);
							measureNoteDTO.setNoteDesc(noteDesc);
							observer.onSaveClicked(measureNoteDTO);
						}
						else {
							clearMessages();
							errorMessages.createAlert(MatContext.get().getMessageDelegate().getMEASURE_NOTES_REQUIRED_MESSAGE());
							/*errorMessages.setFocus();*/
						}
					}
				}
			}
		};
	}
	
	/**
	 * Cancel click handler.
	 * 
	 * @return the click handler
	 */
	private ClickHandler cancelClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				
				String measureNoteId = ((Widget)event.getSource()).getElement().getId();
				MeasureNoteDTO measureNoteDTO = getResultForId(measureNoteId);
				
				VerticalPanel vpanel = (VerticalPanel) ((Widget)event.getSource()).getParent().getParent();
				for(int i=0; i<vpanel.getWidgetCount(); i++) {
					Widget widget = vpanel.getWidget(i);
					if(widget.getElement().getAttribute("id").equalsIgnoreCase("NoteTitle_"+measureNoteId)) {
						((TextBox)widget).setText(measureNoteDTO.getNoteTitle());
					}
					if(widget.getElement().getAttribute("id").equalsIgnoreCase("NoteDesc_"+measureNoteId)) {
						VerticalPanel vp = (VerticalPanel)widget;
						Widget wid = vp.getWidget(0);
						((Summernote)wid).reconfigure();
						((Summernote)wid).setCode(measureNoteDTO.getNoteDesc());
					}
				}
			}
		};
	}
	
	/**
	 * Gets the result for id.
	 * 
	 * @param id
	 *            the id
	 * @return the result for id
	 */
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
	
	/**
	 * Clear messages.
	 */
	private void clearMessages() {
		successMessageDisplay.clearAlert();
		errorMessages.clearAlert();
	}
	
	/**
	 * Gets the focus handler.
	 * 
	 * @return the focus handler
	 */
	private FocusHandler getFocusHandler() {
		return new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				clearMessages();
			}
		};
	}
}
