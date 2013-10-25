package mat.client.codelist;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay;
import mat.client.codelist.events.AddCodeToCodeListEvent;
import mat.client.codelist.events.AddQDSElementEvent;
import mat.client.codelist.events.CancelAddCodeEvent;
import mat.client.codelist.events.CancelEditCodeListEvent;
import mat.client.codelist.events.CreateNewCodeListEvent;
import mat.client.codelist.events.CreateNewGroupedCodeListEvent;
import mat.client.codelist.events.EditCodeListEvent;
import mat.client.codelist.events.EditGroupedCodeListEvent;
import mat.client.codelist.events.ExternalViewerEvent;
import mat.client.codelist.events.OnChangeValueSetDraftOptionsEvent;
import mat.client.event.UploadRefreshViewEvent;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.util.ClientConstants;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class CodeListController.
 */
public class CodeListController implements MatPresenter {
	
	/*USTod*/
	/** The Constant MY_VALUE_SETS_VALUE_SET_CREATE. */
	public static final String MY_VALUE_SETS_VALUE_SET_CREATE = "My Value Sets  > Create a Value Set";
	
	/** The Constant MY_VALUE_SETS_VALUE_SET_MANAGE. */
	public static final String MY_VALUE_SETS_VALUE_SET_MANAGE = "My Value Sets  > Update a Value Set  >  Manage Codes";
	
	/** The Constant MY_VALUE_SETS_MANAGE_VALUE_SETS_ADD_CODES. */
	public static final String MY_VALUE_SETS_MANAGE_VALUE_SETS_ADD_CODES = "My Value Sets  > Manage Value Set  >  Add Codes";
	
	/** The Constant MY_VALUE_SETS_VALUE_SET_UPDATE. */
	public static final String MY_VALUE_SETS_VALUE_SET_UPDATE = "My Value Sets  > Update a Value Set";
	
	/** The type. */
	private String type = MatContext.get().getLoggedInUserRole();;
	
	/** The contents. */
	private SimplePanel contents = new SimplePanel();
	
	/** The code list search presenter. */
	private ManageCodeListSearchPresenter codeListSearchPresenter;
	
	/** The code list detail presenter. */
	private ManageCodeListDetailPresenter codeListDetailPresenter;
	
	/** The grouped code list presenter. */
	private ManageGroupedCodeListPresenter groupedCodeListPresenter;
	
	/** The list box code provider. */
	private ListBoxCodeProvider listBoxCodeProvider;
	
	/** The empty widget. */
	private SimplePanel emptyWidget = new SimplePanel();
	
	/** The draft display. */
	private DraftDisplay draftDisplay;
	
	/**
	 * Instantiates a new code list controller.
	 */
	public CodeListController() {
		emptyWidget.add(new Label("No Measure Selected line 150"));
		listBoxCodeProvider = MatContext.get().getListBoxCodeProvider();
		ManageCodeListDetailView mcld = new ManageCodeListDetailView("Value Set Name");
		ManageCodeListSearchView mclsv = new ManageCodeListSearchView();
		AdminValueSetSearchView adminValueSetSearchView = new AdminValueSetSearchView();
		draftDisplay = new ManageValueSetDraftView();
		AddCodeView addCodeView = new AddCodeView();
		ExternalLinkDisclaimerView extDisclaimerView = new ExternalLinkDisclaimerView();
		ManageGroupedCodeListView groupedView  = new ManageGroupedCodeListView("Grouped Value Set Name");
		AddCodeListView addCodeListView = new AddCodeListView();
		QDSElementView qdsView = new QDSElementView();
		CodeListHistoryView historyView = new CodeListHistoryView();
		codeListSearchPresenter = new ManageCodeListSearchPresenter(mclsv,adminValueSetSearchView, historyView, null, draftDisplay);
		codeListDetailPresenter = new ManageCodeListDetailPresenter(mcld,addCodeView,extDisclaimerView,qdsView,listBoxCodeProvider);
		groupedCodeListPresenter = new ManageGroupedCodeListPresenter(groupedView, addCodeListView, listBoxCodeProvider);
	    
	    displayEmpty();
		
		HandlerManager eventBus = MatContext.get().getEventBus();
		addingHandlersOnEvent(eventBus);
		DOM.setElementAttribute(contents.getElement(), "id", "CodeListControler.contents");
		beforeDisplay();
	}
	
	
	/**
	 * Instantiates a new code list controller.
	 * 
	 * @param type
	 *            the type
	 */
	public CodeListController(String type) {
		if(type.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			emptyWidget.add(new Label("No Measure Selected line 150"));
			//ManageCodeListSearchView mclsv = new ManageCodeListSearchView();
			AdminValueSetSearchView adminValueSetSearchView = new AdminValueSetSearchView();
			draftDisplay = new ManageValueSetDraftView();
			CodeListHistoryView historyView = new CodeListHistoryView();
			TransferOwnershipView transferOS = new TransferOwnershipView();
			codeListSearchPresenter = new ManageCodeListSearchPresenter(null, adminValueSetSearchView,historyView, null, draftDisplay,transferOS);
			displayEmpty();
			HandlerManager eventBus = MatContext.get().getEventBus();
			addingHandlersOnEvent(eventBus);
			DOM.setElementAttribute(contents.getElement(), "id", "CodeListControler.contents");
			beforeDisplay();
		}
	}
	
	/**
	 * Adding handlers on event.
	 * 
	 * @param eventBus
	 *            the event bus
	 */
	private void addingHandlersOnEvent(HandlerManager eventBus){
		
		eventBus.addHandler(CancelEditCodeListEvent.TYPE, new CancelEditCodeListEvent.Handler() {
			@Override
			public void onCancelEditCodeList(CancelEditCodeListEvent event) {
				contents.clear();
				codeListSearchPresenter.refreshSearch();
				contents.add(codeListSearchPresenter.getWidget());
				Mat.focusSkipLists("MainContent");
			}
		});
	
		eventBus.addHandler(CancelAddCodeEvent.TYPE, new CancelAddCodeEvent.Handler() {
			@Override
			public void onCancelAddCode(CancelAddCodeEvent event) {
				displayDetail(MY_VALUE_SETS_VALUE_SET_UPDATE);
			}
		});
	
		eventBus.addHandler(CreateNewCodeListEvent.TYPE, new CreateNewCodeListEvent.Handler() {
			@Override
			public void onCreateNewCodeList(CreateNewCodeListEvent event) {
				codeListDetailPresenter.createNewCodeList();
				displayDetail(MY_VALUE_SETS_VALUE_SET_CREATE);
			}
		});
		
		eventBus.addHandler(UploadRefreshViewEvent.TYPE, new UploadRefreshViewEvent.Handler() {
			@Override
			public void onUploadRefreshView(UploadRefreshViewEvent event) {
				codeListDetailPresenter.refreshUploadedCodes();//This will refresh table with the new uploaded codes.
			}
		});
	
		eventBus.addHandler(CreateNewGroupedCodeListEvent.TYPE, new CreateNewGroupedCodeListEvent.Handler() {
			@Override
			public void onCreateNewGroupedCodeList(CreateNewGroupedCodeListEvent event) {
				groupedCodeListPresenter.createNewGroupedCodeList();
				displayGroupedDetail();
			}
		});
		
		/*eventBus.addHandler(TransferCodeListOwnershipEvent.TYPE, new TransferCodeListOwnershipEvent.Handler() {
			@Override
			public void onTransferCodeListOwnership(TransferCodeListOwnershipEvent event) {
				TransferOwnershipView transferOS = new TransferOwnershipView();
				TransferCodeListOwnershipPresenter transferCLOPresenter = new TransferCodeListOwnershipPresenter(transferOS);
				groupedCodeListPresenter.createNewGroupedCodeList();
				displayGroupedDetail();
			}
		});*/
		
		
		eventBus.addHandler(EditCodeListEvent.TYPE, new EditCodeListEvent.Handler() {
			@Override
			public void onEditCodeList(EditCodeListEvent event) {
				codeListDetailPresenter.editCodeList(event.getKey());
				displayDetail(MY_VALUE_SETS_VALUE_SET_UPDATE);
			}
		});
		eventBus.addHandler(EditGroupedCodeListEvent.TYPE, new EditGroupedCodeListEvent.Handler() {
			@Override
			public void onEditGroupedCodeList(EditGroupedCodeListEvent event) {
				groupedCodeListPresenter.editCodeList(event.getKey());
				displayGroupedDetail();
			}
		});
	
		eventBus.addHandler(AddQDSElementEvent.TYPE, new AddQDSElementEvent.Handler() {
			@Override
			public void onAddQDSElement(AddQDSElementEvent event) {
				displayQDSView();
			}
		});
	
		eventBus.addHandler(AddCodeToCodeListEvent.TYPE, new AddCodeToCodeListEvent.Handler(){

			@Override
			public void onAddCodeToCodeList(AddCodeToCodeListEvent event) {
				displayAddCodes();
			
			}
		
		});
	
		eventBus.addHandler(ExternalViewerEvent.TYPE, new ExternalViewerEvent.Handler(){

			@Override
		public void onExternalView(ExternalViewerEvent event) {
				//	buttonBar.setContinueButtonVisible(false);
				//buttonBar.setPreviousButtonVisible(false);
				contents.clear();
				contents.add(codeListDetailPresenter.getExternalLinkDisclaimer());
			}		
		});
	
		eventBus.addHandler(OnChangeValueSetDraftOptionsEvent.TYPE, new OnChangeValueSetDraftOptionsEvent.Handler() {
			@Override
			public void onChangeOptions(OnChangeValueSetDraftOptionsEvent event) {
				PrimaryButton button = (PrimaryButton) draftDisplay.getSaveButton();
				button.setFocus(true);
			}
		});

	}
	
	
	/**
	 * Display empty.
	 */
	public void displayEmpty() {
		contents.clear();
		contents.add(emptyWidget);
	}
	
	/**
	 * Display detail.
	 * 
	 * @param heading
	 *            the heading
	 */
	public void displayDetail(String heading) {
		codeListDetailPresenter.clearMessages();
		contents.clear();
		contents.add(codeListDetailPresenter.getWidget(heading));
	}
	
	/**
	 * Display search.
	 */
	public void displaySearch() {
		codeListSearchPresenter.resetDisplay();
		contents.clear();
		contents.add(codeListSearchPresenter.getWidget());
		Mat.focusSkipLists("CodeList");
	}
	
	/**
	 * Display add codes.
	 */
	public void displayAddCodes(){
		contents.clear();
		contents.add(codeListDetailPresenter.getAddCodesToCodeList());
		Mat.focusSkipLists("MainContent");
	}
	
	/**
	 * Display external disclaimer.
	 */
	public void displayExternalDisclaimer(){
		contents.clear();
		contents.add(codeListDetailPresenter.getAddCodesToCodeList());
	}
	
	/**
	 * Display grouped detail.
	 */
	private void displayGroupedDetail() {
		contents.clear();
		contents.add(groupedCodeListPresenter.getWidget());
	}
	
	/**
	 * Display qds view.
	 */
	private void displayQDSView() {
		contents.clear();
		contents.add(codeListDetailPresenter.getAddQDSDisplay());
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	public Widget getWidget() {
		return contents;
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
	 MeasureComposerPresenter.setSubSkipEmbeddedLink("CodeList");
	 Mat.focusSkipLists("MainContent");
	 displaySearch();
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		if(!type.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			codeListDetailPresenter.clearMessages();
			groupedCodeListPresenter.clearMessages();
		}
	}
	
}
