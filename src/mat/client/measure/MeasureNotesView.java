package mat.client.measure;

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
	private FlowPanel flowPanel = new FlowPanel();
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
		simplePanel.setStyleName("notesContentPanel");
		//flowPanel.add(new Label("Measure Notes Section"));
		flowPanel.add(exportButton);
		flowPanel.add(new SpacerWidget());
		flowPanel.add(new SpacerWidget());
		flowPanel.add(buildMeasureComposer());
		flowPanel.add(new SpacerWidget());
		simplePanel.add(flowPanel);
		containerPanel.add(simplePanel);
		containerPanel.add(new SpacerWidget());
		
		FlowPanel fPanel = new FlowPanel();
		fPanel.add(new SpacerWidget());
		fPanel.add(successMessageDisplay);
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
		measureNoteTitle.setWidth("50%");
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
	
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		// TODO Auto-generated method stub
		return errorMessages;
	}
	
	public void cancelComposedNote() {
		getMeasureNoteComposer().setText("");
		getMeasureNoteTitle().setText("");
	}
	
	public ScrollPanel buildDataTable(MeasureNotesModel results) {
		ScrollPanel scrollPanel = new ScrollPanel();
		VerticalPanel mainPanel = new VerticalPanel();
		scrollPanel.setWidget(mainPanel);
		
		if(results != null) {
			scrollPanel.getElement().setId("measureNotesView_scrollPanel");
			for(MeasureNotesModel.Result result : results.getData()){
				createDisclosurePanel(mainPanel,result);
			}
		}
		
		return scrollPanel;
	}
	
	
	private void createDisclosurePanel(VerticalPanel mainPanel, MeasureNotesModel.Result result){
		DisclosurePanel notesDisclosurePanel = new DisclosurePanel();
		//notesDisclosurePanel.getElement().setId("measureNotesView_notesDisclosurePanel");
		notesDisclosurePanel.setAnimationEnabled(true);
		notesDisclosurePanel.setOpen(false);
		notesDisclosurePanel.setHeader(createDisclosureHeaderWidget(result));
		notesDisclosurePanel.setContent(createDisclosureContentWidget(result));
		notesDisclosurePanel.setWidth("100%");
		mainPanel.add(notesDisclosurePanel);
	}
	
	private Widget createDisclosureContentWidget(
			mat.model.MeasureNotesModel.Result result) {
		// TODO Auto-generated method stub
		TextBox title = new TextBox();
		title.setWidth("400px");
		title.setStyleName("bold");
		TextArea measureNoteDesc = new TextArea();
		measureNoteDesc.setWidth("800px");
		measureNoteDesc.setHeight("70px");
		HorizontalPanel hPanel = new HorizontalPanel();
		HTML titleLabel = new HTML("Title:");
		titleLabel.setStyleName("bold");
		hPanel.add(titleLabel);
		hPanel.add(title);
		VerticalPanel vPanel = new VerticalPanel();
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
		return vPanel;
	}

	private Widget createDisclosureHeaderWidget(
		mat.model.MeasureNotesModel.Result result) {
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setWidth("800px");
		HorizontalPanel noteTitlePanel = new HorizontalPanel();
		HTML noteTitle = new HTML(result.getTitle());
		noteTitle.setStyleName("disclosurePanelHeaderValue");
		noteTitlePanel.setWidth("160px");
		noteTitlePanel.add(noteTitle);
		headerPanel.add(noteTitlePanel);
		
		HorizontalPanel emailAddrPanel = new HorizontalPanel();
		HTML creatorEmailAddr = new HTML(result.getCreatorEmailAddr());
		creatorEmailAddr.setStyleName("disclosurePanelHeaderValue");
		emailAddrPanel.setWidth("200px");
		emailAddrPanel.add(creatorEmailAddr);
		headerPanel.add(emailAddrPanel);
		
		HorizontalPanel creationDatePanel = new HorizontalPanel();
		HTML creationDate = new HTML(result.getCreationDate().toString());
		creationDate.setStyleName("disclosurePanelHeaderValue");
		creationDatePanel.setWidth("200px");
		creationDatePanel.add(creationDate);
		headerPanel.add(creationDatePanel);
		
		Image editmg = new Image(ImageResources.INSTANCE.g_package_edit());
		Button editButton = new Button();
		editButton.getElement().appendChild(editmg.getElement());
		editButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Edit button clicked !!!");
			}
				
		});
		Image deleteImg  = new Image(ImageResources.INSTANCE.g_delete());
		Button deleteButton = new Button();
		deleteButton.getElement().appendChild(deleteImg.getElement());
		deleteButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Delete button clicked !!!");
			}
		});
		HorizontalPanel deleteButtonPanel = new HorizontalPanel();
		deleteButtonPanel.add(deleteButton);
		deleteButtonPanel.setWidth("120px");
		headerPanel.add(deleteButtonPanel);
		
		HorizontalPanel editButtonPanel = new HorizontalPanel();
		editButtonPanel.add(editButton);
		editButtonPanel.setWidth("120px");
		headerPanel.add(editButtonPanel);
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
