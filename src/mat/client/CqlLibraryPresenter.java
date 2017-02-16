package mat.client;

import org.gwtbootstrap3.client.ui.TextArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.clause.cqlworkspace.CQLLibraryDetailView;
import mat.client.cql.CQLLibrarySearchView;
import mat.client.event.CQLLibraryEditEvent;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.CreateNewItemWidget;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.MessageAlert;
import mat.client.shared.MostRecentCQLLibraryWidget;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.SkipListBuilder;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
/**
 * @author jnarang
 *
 */
public class CqlLibraryPresenter implements MatPresenter {

	/** The panel. */
	// private SimplePanel panel = new SimplePanel();

	/** The panel. */
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();

	/** The sub skip content holder. */
	private static FocusableWidget subSkipContentHolder;

	ViewDisplay cqlLibraryView;
	DetailDisplay detailDisplay;

	/** The is create measure widget visible. */
	boolean isCreateNewItemWidgetVisible = false;
	
	/** The is measure search filter visible. */
	boolean isSearchFilterVisible = true;

	private CQLModel cqlModel;

	CQLModelValidator validator = new CQLModelValidator();

	/**
	 * The Interface ViewDisplay.
	 */
	public static interface ViewDisplay {

		/**
		 * Top Main panel of CQL Workspace Tab.
		 * 
		 * @return HorizontalPanel
		 */
		FlowPanel getMainPanel();

		/**
		 * Generates View for CQLWorkSpace tab.
		 */
		void buildDefaultView();

		CreateNewItemWidget getCreateNewItemWidget();

		CustomButton getAddNewFolderButton();

		CustomButton getZoomButton();

		Widget asWidget();

		void buildCellTable(SaveCQLLibraryResult searchModel, String searchText,int filter);

		CQLLibrarySearchView getCQLLibrarySearchView();

		String getSelectedOption();

		MessageAlert getErrorMessageAlert();

		void buildCreateNewView();

		void clearSelections();

		HasSelectionHandlers<CQLLibraryDataSetObject> getSelectIdForEditTool();

		VerticalPanel getWidgetVP();

		SearchWidgetWithFilter getSearchFilterWidget();

		int getSelectedFilter();

		HasValue<String> getSearchString();

		HasClickHandlers getSearchButton();

		MostRecentCQLLibraryWidget getMostRecentLibraryWidget();

		void buildMostRecentWidget();

	}

	public static interface DetailDisplay {
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public HasValue<String> getName();

		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();

		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();

		public void resetAll();

		Widget asWidget();

		TextArea getNameField();

		ErrorMessageAlert getErrorMessage();
	}

	public CqlLibraryPresenter(CqlLibraryView cqlLibraryView, CQLLibraryDetailView detailDisplay) {
		this.cqlLibraryView = cqlLibraryView;
		this.detailDisplay = detailDisplay;
		addCQLLibraryViewHandlers();
		addDetailDisplayViewHandlers();
		addCQLLibrarySelectionHandlers();
		addMostRecentWidgetSelectionHandler();
	}

	private void addCQLLibrarySelectionHandlers() {
		
		cqlLibraryView.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<CQLLibraryDataSetObject>() {
			
			@Override
			public void onSelection(SelectionEvent<CQLLibraryDataSetObject> event) {
				final String mid = event.getSelectedItem().getId();
				isLibrarySelected(event.getSelectedItem());
			}

			
		});
	}
	
	private void addMostRecentWidgetSelectionHandler(){
		
		cqlLibraryView.getMostRecentLibraryWidget().addSelectionHandler(new SelectionHandler<CQLLibraryDataSetObject>() {

			@Override
			public void onSelection(SelectionEvent<CQLLibraryDataSetObject> event) {
				String cqlId = event.getSelectedItem().getId();
				isLibrarySelected(event.getSelectedItem());
				
			}
		});
		
	}
	
	/**
	 * @param event
	 */
	private void isLibrarySelected(CQLLibraryDataSetObject selectedItem) {
		
		
		fireCQLLibrarySelectedEvent(selectedItem.getId(), selectedItem.getVersion(), selectedItem.getCqlName(), true, false,
				null);
		fireCqlLibraryEditEvent();
	}

	private void addDetailDisplayViewHandlers() {
		detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				detailDisplay.resetAll();
				cqlLibraryView.clearSelections();
				displaySearch();

			}
		});

		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				detailDisplay.getErrorMessage().clearAlert();
				if (detailDisplay.getNameField().getText().isEmpty()) {
					detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
				} else {
					if (validator.validateForAliasNameSpecialChar(detailDisplay.getNameField().getText().trim())) {
						CQLLibraryDataSetObject libraryDataSetObject = new CQLLibraryDataSetObject();
						libraryDataSetObject.setCqlName(detailDisplay.getNameField().getText());
						saveCqlLibrary(libraryDataSetObject);
					} else {
						detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
					}
				}
			}
		});

	}

	private void saveCqlLibrary(CQLLibraryDataSetObject libraryDataSetObject) {
		MatContext.get().getCQLLibraryService().save(libraryDataSetObject, new AsyncCallback<SaveCQLLibraryResult>() {

			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				if(result.isSuccess()){
					fireCQLLibrarySelectedEvent(result.getId(), result.getVersionStr(), result.getCqlLibraryName(), true, false,
									null);
					fireCqlLibraryEditEvent();
				} else {
						detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessage().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});

	}

	
	private void fireCQLLibrarySelectedEvent(String id, String version,
			String name, boolean isEditable, boolean isLocked, String lockedUserId) {
		CQLLibrarySelectedEvent evt = new CQLLibrarySelectedEvent(id, version, name,isEditable, isLocked, lockedUserId);
		cqlLibraryView.getErrorMessageAlert().clearAlert();
		detailDisplay.getErrorMessage().clearAlert();
		MatContext.get().getEventBus().fireEvent(evt);
	}
	
	/**
	 * Fire measure edit event.
	 */
	private void fireCqlLibraryEditEvent() {
		CQLLibraryEditEvent evt = new CQLLibraryEditEvent();
		MatContext.get().getEventBus().fireEvent(evt);
	}
	
	
	private void addCQLLibraryViewHandlers() {
		cqlLibraryView.getAddNewFolderButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				cqlLibraryView.getSearchFilterWidget().getSearchFilterDisclosurePanel().setOpen(false);
				isCreateNewItemWidgetVisible = !isCreateNewItemWidgetVisible;
				
				if(isSearchFilterVisible){
					isSearchFilterVisible = !isSearchFilterVisible;
					cqlLibraryView.getWidgetVP().clear();
				}
				
				if(isCreateNewItemWidgetVisible){
					cqlLibraryView.getWidgetVP().add(cqlLibraryView.getCreateNewItemWidget());
				} else {
					cqlLibraryView.getWidgetVP().clear();
				}

			}
		});
		
		cqlLibraryView.getZoomButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				cqlLibraryView.getSearchFilterWidget().getSearchFilterDisclosurePanel().setOpen(false);
				if (isCreateNewItemWidgetVisible) {
					isCreateNewItemWidgetVisible = !isCreateNewItemWidgetVisible;
					cqlLibraryView.getWidgetVP().clear();
				}
				isSearchFilterVisible = !isSearchFilterVisible;
				
				if(isSearchFilterVisible){
					cqlLibraryView.getWidgetVP().add(cqlLibraryView.getSearchFilterWidget());
				} else {
					cqlLibraryView.getWidgetVP().clear();
				}
				
			}
		});

		cqlLibraryView.getCreateNewItemWidget().getCreateItemButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (cqlLibraryView.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_CQL)) {
					cqlLibraryView.getErrorMessageAlert().clearAlert();
					createNew();
				} else if (cqlLibraryView.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_CQL_DRAFT)) {
					cqlLibraryView.getErrorMessageAlert().clearAlert();
					// createNew();
				} else if (cqlLibraryView.getSelectedOption()
						.equalsIgnoreCase(ConstantMessages.CREATE_NEW_CQL_VERSION)) {
					cqlLibraryView.getErrorMessageAlert().clearAlert();
					// createVersion();
				} else {
					cqlLibraryView.getErrorMessageAlert()
							.createAlert("Please select an option from the Create list box.");
				}

			}

		});
		
		
		TextBox searchWidget = (TextBox) (cqlLibraryView.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button) cqlLibraryView.getSearchButton()).click();
				}
			}
		});
		
		
		cqlLibraryView.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int startIndex = 1;
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				int filter = cqlLibraryView.getSelectedFilter();
				search(cqlLibraryView.getSearchString().getValue(),"StandAlone", filter,startIndex,
						Integer.MAX_VALUE);
			}
		});

	}

	private void createVersion() {
		// TODO Auto-generated method stub

	}

	private void createNew() {

		cqlModel = new CQLModel();
		displayDetailForAdd();
		Mat.focusSkipLists("CQLLibrary");

	}

	private void displayDetailForAdd() {
		panel.getButtonPanel().clear();
		panel.setHeading("My CQL Library > Create New CQL Library", "CQLLibrary");
		panel.setContent(detailDisplay.asWidget());
	}

	private void displaySearch() {
		cqlLibraryView.getErrorMessageAlert().clearAlert();
		String heading = "CQL Library";
		panel.setHeading(heading, "CQLLibrary");
		setSubSkipEmbeddedLink("CQLSearchView_mainPanel");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("fp_FlowPanel_CQL");
		isCreateNewItemWidgetVisible = false;
		//cqlLibraryView.getCreateNewItemWidget().setVisible(false);
		int filter = cqlLibraryView.getSelectedFilter();
		search(cqlLibraryView.getSearchString().getValue(), "StandAlone", filter,1, Integer.MAX_VALUE);
		searchRecentLibraries();
		panel.getButtonPanel().clear();
		panel.setButtonPanel(cqlLibraryView.getAddNewFolderButton(), cqlLibraryView.getZoomButton());
		
		fp.add(cqlLibraryView.asWidget());
		isSearchFilterVisible = true;
		panel.setContent(fp);
		Mat.focusSkipLists("CQLLibrary");
	}

	private void search(final String searchText, String searchFrom, final int filter,int startIndex, int pageSize) {
		final String lastSearchText = (searchText != null) ? searchText
				.trim() : null;
				//pageSize = Integer.MAX_VALUE;
				pageSize = 25;
				showSearchingBusy(true);
				cqlLibraryView.getErrorMessageAlert().clearAlert();
				MatContext.get().getCQLLibraryService().search(lastSearchText, searchFrom, filter,startIndex, pageSize, new AsyncCallback<SaveCQLLibraryResult>() {
					
					@Override
					public void onSuccess(SaveCQLLibraryResult result) {
						if(cqlLibraryView.getSearchFilterWidget().
								getSelectedFilter()!=0){
							cqlLibraryView.getCQLLibrarySearchView().setCQLLibraryListLabel("All CQL Libraries");
						}else{
							cqlLibraryView.getCQLLibrarySearchView().setCQLLibraryListLabel("My CQL Libraries");
						}
						
						if ((result.getResultsTotal() == 0)
								&& !lastSearchText.isEmpty()) {
							cqlLibraryView.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getNoLibrarues());
						} 
						
						cqlLibraryView.buildCellTable(result, lastSearchText,filter);
						showSearchingBusy(false);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
						
					}
				});
	}
	
	
	private void searchRecentLibraries() {
		MatContext.get().getCQLLibraryService().getAllRecentCQLLibrariesForUser(MatContext.get().getLoggedinUserId(),
				new AsyncCallback<SaveCQLLibraryResult>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				cqlLibraryView.getMostRecentLibraryWidget().setResult(result);
				cqlLibraryView.buildMostRecentWidget();;
			}
		});
	}
	
	
	
	/**
	 * Sets the sub skip embedded link.
	 *
	 * @param name
	 *            the new sub skip embedded link
	 */
	public static void setSubSkipEmbeddedLink(String name) {
		if (subSkipContentHolder == null) {
			subSkipContentHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Sub Content"));
		}
		Mat.removeInputBoxFromFocusPanel(subSkipContentHolder.getElement());
		Widget w = SkipListBuilder.buildSubSkipList(name);
		subSkipContentHolder.clear();
		subSkipContentHolder.add(w);
		subSkipContentHolder.setFocus(true);
	}

	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		cqlLibraryView.getErrorMessageAlert().clearAlert();
	}

	@Override
	public void beforeDisplay() {
		
		cqlLibraryView.buildDefaultView();
		cqlLibraryView.clearSelections();
		displaySearch();

	}

	@Override
	public Widget getWidget() {
		return panel;
	}
	
	/**
	 * Show searching busy.
	 * 
	 * @param busy
	 *            the busy
	 */
	private void showSearchingBusy(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
	}

}
