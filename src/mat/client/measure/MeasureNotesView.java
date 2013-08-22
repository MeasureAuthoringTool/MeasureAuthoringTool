package mat.client.measure;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.client.ImageResources;
import mat.client.codelist.TransferOwnerShipModel;
import mat.client.codelist.TransferOwnerShipModel.Result;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.CodeListSearchDTO;
import mat.model.MeasureNotesModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
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
	private static final int[] PAGE_SIZES= new int[] {50, PAGE_SIZE_ALL};
	private VerticalPanel containerPanel = new VerticalPanel();
	private SimplePanel simplePanel = new SimplePanel();
	private FlowPanel flowPanel = new FlowPanel ();
	private TextArea measureNoteComposer = new TextArea();
	private TextBox measureNoteTitle = new TextBox();
	private Button exportButton = new PrimaryButton("Export Notes","primaryButton");
	private Button saveButton = new PrimaryButton("Save","primaryButton");
	private Button cancelButton = new PrimaryButton("Cancel","rightAlignSecondaryButton");
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	public Grid508 dataTable = new Grid508();
	protected Panel pageSizeSelector = new FlowPanel();
	public SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	protected Panel pageSelector = new HorizontalPanel();
	MeasureNotesModel notesResult = new MeasureNotesModel();
		
	private SearchView<mat.model.MeasureNotesModel.Result> view = new SearchView<MeasureNotesModel.Result>("Users");
	
	public HasClickHandlers getExportButton() {
		return exportButton;
	}
	
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
	
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}
	
	public Grid508 getDataTable() {
		return dataTable;
	}

	public void setDataTable(Grid508 dataTable) {
		this.dataTable = dataTable;
	}

	public TextArea getMeasureNoteComposer() {
		return measureNoteComposer;
	}

	public TextBox getMeasureNoteTitle() {
		return measureNoteTitle;
	}

	private HTML viewingNumber = new HTML();
	public HTML getViewingNumber() {
		return viewingNumber;
	}
	public void setViewingNumber(HTML viewingNumber) {
		this.viewingNumber = viewingNumber;
	}
		

	public MeasureNotesView(){
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
		notesResult.setData(getDataList());
		fPanel.add(buildDataTable(notesResult));
		//fPanel.add(dataTable);	
		fPanel.add(new SpacerWidget());
		containerPanel.add(fPanel);
	}
	
	private Widget buildMeasureComposer(){
		FlowPanel composerPanel = new FlowPanel();
		Label composerLabel = new Label("Measure Notes Composer");
		composerLabel.setStyleName("measureComposerLabel");
		composerPanel.add(composerLabel);
		composerPanel.add(new SpacerWidget());
		HorizontalPanel titlePanel = new HorizontalPanel();
		Label titleLabel= new Label("Title:");
		titleLabel.setStyleName("bold");
		measureNoteTitle.setWidth("400px");
		titlePanel.add(titleLabel);
		titlePanel.add(measureNoteTitle);
		composerPanel.add(titlePanel);
		composerPanel.add(new SpacerWidget());
		measureNoteComposer.setHeight("70px");
		measureNoteComposer.setWidth("90%");
		composerPanel.add(measureNoteComposer);
		composerPanel.add(new SpacerWidget());
		HorizontalPanel bottomButtonPanel = new HorizontalPanel();
		composerPanel.add(new SpacerWidget());
		bottomButtonPanel.add(saveButton);
		bottomButtonPanel.add(cancelButton);
		bottomButtonPanel.setWidth("100px");
		bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		composerPanel.add(bottomButtonPanel);
		
		return composerPanel;
	}
	
	public Widget asWidget() {
		return containerPanel;
	}
	
	public ErrorMessageDisplay getErrorMessageDisplay() {
		// TODO Auto-generated method stub
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
		mainPanel.setStylePrimaryName("right_left_border_outset");
		
		VerticalPanel tableHeaderPanel = new VerticalPanel();
		tableHeaderPanel.setWidth("875px");
		tableHeaderPanel.setStylePrimaryName("header_background");
		Widget tableHeaderWidget = createTableHeaderWidget();
		tableHeaderPanel.add(tableHeaderWidget);
		tableHeaderPanel.setCellHorizontalAlignment(tableHeaderWidget, HasHorizontalAlignment.ALIGN_RIGHT);
		
		mainPanel.add(tableHeaderPanel);
		
		if(results != null) {			
			for(MeasureNotesModel.Result result : results.getData()){
				createDisclosurePanel(mainPanel,result);
			}
		}		
		return scrollPanel;
	}
	
	private Widget createTableHeaderWidget() {			
			HorizontalPanel headerPanel = new HorizontalPanel();
			headerPanel.setWidth("800px");
			HorizontalPanel noteTitlePanel = new HorizontalPanel();
			noteTitlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			String title = "Title";
			HTML noteTitle = new HTML(title);
			noteTitle.setTitle(title);
			noteTitle.setWidth("100%");
			noteTitlePanel.add(noteTitle);
			
			noteTitlePanel.setCellWidth(noteTitle, "100%");
			headerPanel.add(noteTitlePanel);
			headerPanel.setCellWidth(noteTitlePanel, "20%");
			
			HorizontalPanel emailAddrPanel = new HorizontalPanel();
			emailAddrPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			HTML creatorEmailAddr = new HTML("Email Address");
			creatorEmailAddr.setTitle("Email Address");
			creatorEmailAddr.setWidth("100%");
			emailAddrPanel.add(creatorEmailAddr);
			
			emailAddrPanel.setCellWidth(creatorEmailAddr, "100%");
			headerPanel.add(emailAddrPanel);
			headerPanel.setCellWidth(emailAddrPanel, "25%");
			
			HorizontalPanel creationDatePanel = new HorizontalPanel();
			creationDatePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			
			String dateString = "Creation Date";
			HTML creationDate = new HTML(dateString);
			creationDate.setTitle(dateString);
			creationDate.setWidth("100%");
			creationDatePanel.add(creationDate);
			
			creationDatePanel.setCellWidth(creationDate, "100%");
			headerPanel.add(creationDatePanel);
			headerPanel.setCellWidth(creationDatePanel, "25%");

			HorizontalPanel deleteButtonPanel = new HorizontalPanel();
			deleteButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			deleteButtonPanel.add(new HTML("Delete"));
			headerPanel.add(deleteButtonPanel);
			headerPanel.setCellWidth(deleteButtonPanel, "15%");
			
			HorizontalPanel editButtonPanel = new HorizontalPanel();
			editButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			editButtonPanel.add(new HTML("Edit"));
			headerPanel.add(editButtonPanel);
			headerPanel.setCellWidth(editButtonPanel, "15%");
			return headerPanel;
	}
	
	
	private void createDisclosurePanel(VerticalPanel mainPanel, MeasureNotesModel.Result result){
		DisclosurePanel notesDisclosurePanel = new DisclosurePanel();
		notesDisclosurePanel.setAnimationEnabled(true);
		notesDisclosurePanel.setOpen(false);
		Widget contentWidget = createDisclosureContentWidget(result);
		Widget headerWidget = createDisclosureHeaderWidget(result,notesDisclosurePanel);
		//notesDisclosurePanel.setHeader(createDisclosureHeaderWidget(result));
		notesDisclosurePanel.setContent(contentWidget);
		notesDisclosurePanel.setWidth("100%");
		
		VerticalPanel widgetPanel = new VerticalPanel();
		widgetPanel.setWidth("875px");
		widgetPanel.setStylePrimaryName("bottom_border_inset");		
		widgetPanel.add(headerWidget);
		widgetPanel.setCellHorizontalAlignment(headerWidget, HasHorizontalAlignment.ALIGN_RIGHT);
		widgetPanel.add(new SpacerWidget());
		widgetPanel.add(notesDisclosurePanel);
		widgetPanel.add(new SpacerWidget());
		
		mainPanel.add(widgetPanel);				
	}
	
	private Widget createDisclosureContentWidget(
			mat.model.MeasureNotesModel.Result result) {
		// TODO Auto-generated method stub
		TextBox title = new TextBox();
		title.setTitle("Measure Notes Title");
		title.setWidth("400px");
		TextArea measureNoteDesc = new TextArea();
		measureNoteDesc.setTitle("Measure Notes Description");
		measureNoteDesc.setWidth("800px");
		measureNoteDesc.setHeight("70px");
		HorizontalPanel hPanel = new HorizontalPanel();
		HTML titleLabel = new HTML("Title:");
		titleLabel.setStyleName("bold");
		hPanel.add(titleLabel);
		hPanel.add(title);
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("850px");
		vPanel.add(hPanel);
		vPanel.add(new SpacerWidget());
		vPanel.add(measureNoteDesc);
		vPanel.add(new SpacerWidget());
		vPanel.add(new SpacerWidget());
		HorizontalPanel bottomButtonPanel = new HorizontalPanel();
		Button saveButton = new PrimaryButton("Save","primaryButton");
		Button cancelButton = new PrimaryButton("Cancel","rightAlignSecondaryButton");
		bottomButtonPanel.add(saveButton);
		bottomButtonPanel.add(cancelButton);
		bottomButtonPanel.setWidth("150px");
		bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		vPanel.add(bottomButtonPanel);
		//vPanel.setCellHorizontalAlignment(bottomButtonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		return vPanel;
	}

	private Widget createDisclosureHeaderWidget(
		mat.model.MeasureNotesModel.Result result, final DisclosurePanel notesDisclosurePanel) {
		
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setWidth("800px");
		HorizontalPanel noteTitlePanel = new HorizontalPanel();
		noteTitlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		String title = result.getTitle();
		HTML noteTitle;
		if(title.length() > 30){
			title = title.substring(0,31)+"....";
			noteTitle = new HTML(title);
		}
		else{
			noteTitle = new HTML(result.getTitle());
		}
		noteTitle.setTitle(result.getTitle());
		noteTitle.setWidth("100%");
		//noteTitle.setStyleName("disclosurePanelHeaderValue");
//		noteTitlePanel.setWidth("160px");
		noteTitlePanel.add(noteTitle);
		
		noteTitlePanel.setCellWidth(noteTitle, "100%");
		headerPanel.add(noteTitlePanel);
		headerPanel.setCellWidth(noteTitlePanel, "20%");
		
		HorizontalPanel emailAddrPanel = new HorizontalPanel();
		emailAddrPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		HTML creatorEmailAddr = new HTML(result.getCreatorEmailAddr());
		creatorEmailAddr.setTitle(result.getCreatorEmailAddr());
		creatorEmailAddr.setWidth("100%");
		
		//creatorEmailAddr.setStyleName("disclosurePanelHeaderValue");
//		emailAddrPanel.setWidth("200px");
		emailAddrPanel.add(creatorEmailAddr);
		
		emailAddrPanel.setCellWidth(creatorEmailAddr, "100%");
		headerPanel.add(emailAddrPanel);
		headerPanel.setCellWidth(emailAddrPanel, "25%");
		
		HorizontalPanel creationDatePanel = new HorizontalPanel();
		creationDatePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		String dateString = result.getCreationDate().toString();
		HTML creationDate = new HTML(dateString);
		creationDate.setTitle(dateString);
		creationDate.setWidth("100%");
		//creationDate.setStyleName("disclosurePanelHeaderValue");
//		creationDatePanel.setWidth("200px");
		creationDatePanel.add(creationDate);
		
		creationDatePanel.setCellWidth(creationDate, "100%");
		headerPanel.add(creationDatePanel);
		headerPanel.setCellWidth(creationDatePanel, "25%");
		
		Image editmg = new Image(ImageResources.INSTANCE.g_package_edit());
		Button editButton = new Button();
		editButton.setTitle("Edit");
		editButton.getElement().appendChild(editmg.getElement());
		editButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Edit button clicked !!!");
				notesDisclosurePanel.setOpen(!notesDisclosurePanel.isOpen());
			}
				
		});
		Image deleteImg  = new Image(ImageResources.INSTANCE.g_delete());
		Button deleteButton = new Button();
		deleteButton.setTitle("Delete");
		deleteButton.getElement().appendChild(deleteImg.getElement());
		deleteButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Delete button clicked !!!");
			}
		});
		HorizontalPanel deleteButtonPanel = new HorizontalPanel();
		deleteButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		deleteButtonPanel.add(deleteButton);
//		deleteButtonPanel.setWidth("120px");
		
		//deleteButtonPanel.setCellWidth(spacer3, "100%");
		headerPanel.add(deleteButtonPanel);
		headerPanel.setCellWidth(deleteButtonPanel, "15%");
		
		HorizontalPanel editButtonPanel = new HorizontalPanel();
		editButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		editButtonPanel.add(editButton);
//		editButtonPanel.setWidth("120px");
		//editButtonPanel.setCellWidth(spacer4, "100%");
		headerPanel.add(editButtonPanel);
		headerPanel.setCellWidth(editButtonPanel, "15%");
		return headerPanel;
	}

	/**
	 * Method to build User Results
	 * 
	 * */
	protected void buildSearchResults(int numRows,int numColumns,final SearchResults<MeasureNotesModel.Result> results){		
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {
					String innerText = results.getValue(i, j).getElement().getInnerText();
					Label a = new Label();
					a.setText(innerText);
					dataTable.setWidget(i+1, j, a);
				}
				else {
					dataTable.setWidget(i+1, j,results.getValue(i, j));
				}
			}
			if(i % 2 == 0) {
				dataTable.getRowFormatter().addStyleName(i + 1, "odd");
			}
		}
	}
	
	
	
	
	
	private List<MeasureNotesModel.Result> getDataList(){
 		List<MeasureNotesModel.Result> notesList = new ArrayList<MeasureNotesModel.Result>();
 		MeasureNotesModel.Result note1 = new MeasureNotesModel.Result();
 		MeasureNotesModel.Result note2 = new MeasureNotesModel.Result();
 		MeasureNotesModel.Result note3 = new MeasureNotesModel.Result();
 		MeasureNotesModel.Result note4 = new MeasureNotesModel.Result();
 		note1.setNoteId("1");
 		note1.setTitle("test1");
 		note1.setDescription("Test1 Desc");
 		note1.setCreatorEmailAddr("ytawde@hcareis.com");
 		note1.setCreationDate(new Date());
 		
 		note2.setNoteId("2");
 		note2.setTitle("test2");
 		note2.setDescription("Test2 Desc");
 		note2.setCreatorEmailAddr("cbajikar@telligen.org");
 		note2.setCreationDate(new Date());
 		
 		note3.setNoteId("3");
 		note3.setTitle("test3");
 		note3.setDescription("Test3 Desc");
 		note3.setCreatorEmailAddr("jnarang@hcareis.com");
 		note3.setCreationDate(new Date());
 		
 		note4.setNoteId("4");
 		note4.setTitle("test4");
 		note4.setDescription("Test4 Desc");
 		note4.setCreatorEmailAddr("abc@def.edu");
 		note4.setCreationDate(new Date());
 		
 		notesList.add(note1);
 		notesList.add(note2);
 		notesList.add(note3);
 		notesList.add(note4);
 		return notesList;
 	}

	@Override
	public void buildDataTable(
			SearchResults<mat.model.MeasureNotesModel.Result> results) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
