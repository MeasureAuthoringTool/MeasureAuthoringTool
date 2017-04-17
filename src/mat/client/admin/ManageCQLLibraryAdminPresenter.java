package mat.client.admin;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.DTO.SearchHistoryDTO;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.cql.CQLLibraryHistoryView;
import mat.client.cql.CQLLibrarySearchView;
import mat.client.measure.TransferOwnerShipModel;
import mat.client.measure.TransferOwnershipView;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.CustomButton;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.search.SearchResultUpdate;
import mat.model.cql.CQLLibraryDataSetObject;

public class ManageCQLLibraryAdminPresenter implements MatPresenter {

	/** The panel. */
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();

	/** The sub skip content holder. */
	private static FocusableWidget subSkipContentHolder;
	boolean isSearchBarVisible = true;

	/** The cql library view. */
	ViewDisplay cqlLibraryAdminView;
	/** The history display. */
	CQLLibraryHistoryView historyDisplay;

	TransferOwnershipView transferOwnershipView;

	/** The model. */
	private TransferOwnerShipModel model = null;

	public static interface ViewDisplay {
		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		Widget asWidget();

		/**
		 * Gets the CQL library search view.
		 *
		 * @return the CQL library search view
		 */
		CQLLibrarySearchView getCQLLibrarySearchView();

		/**
		 * Gets the error message alert.
		 *
		 * @return the error message alert
		 */
		MessageAlert getErrorMessageAlert();

		int getSelectedFilter();

		HasValue<String> getSearchString();

		HasClickHandlers getSearchButton();

		void buildCellTable(SaveCQLLibraryResult result, String searchText, int filter);

		void buildDefaultView();

		VerticalPanel getWidgetVP();

		//CustomButton getZoomButton();

		SearchWidgetBootStrap getSearchWidgetBootStrap();

		List<String> getSelectedId();

		void setSelectedId(List<String> selectedId);

		List<CQLLibraryDataSetObject> getSelectedLibraries();

		void setSelectedLibraries(List<CQLLibraryDataSetObject> selectedLibraries);

		Button getClearAllButton();

		Button getTransferButton();

		void clearTransferCheckBoxes();

	}

	public ManageCQLLibraryAdminPresenter(ViewDisplay cqlLibraryAdminView, CQLLibraryHistoryView historyView,
			TransferOwnershipView transferOwnershipView) {
		super();
		this.cqlLibraryAdminView = cqlLibraryAdminView;
		this.historyDisplay = historyView;
		this.transferOwnershipView = transferOwnershipView;
		addCQLLibraryAdminViewEvents();
		addObserverHandler();
		addHistoryDisplayHandlers();
		addTransferOwnershipViewHandlers();

	}

	private void displaySearch() {
		cqlLibraryAdminView.getErrorMessageAlert().clearAlert();
		String heading = "CQL Library";
		panel.setHeading(heading, "CQLLibraryOwnership");
		setSubSkipEmbeddedLink("CQLSearchView_mainPanel");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("fp_FlowPanel_CQL");

		int filter = cqlLibraryAdminView.getSelectedFilter();
		search(cqlLibraryAdminView.getSearchString().getValue(), filter, 1, Integer.MAX_VALUE);

		panel.getButtonPanel().clear();
	//	panel.setButtonPanel(null, null, cqlLibraryAdminView.getZoomButton(), "searchButton_cqlLib");
		isSearchBarVisible = true;
		fp.add(cqlLibraryAdminView.asWidget());

		panel.setContent(fp);
		Mat.focusSkipLists("CQLLibraryOwnership");
	}

	private void addCQLLibraryAdminViewEvents() {
		/*cqlLibraryAdminView.getZoomButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryAdminView.getErrorMessageAlert().clearAlert();

				isSearchBarVisible = !isSearchBarVisible;

				if (isSearchBarVisible) {
					cqlLibraryAdminView.getWidgetVP()
							.add(cqlLibraryAdminView.getSearchWidgetBootStrap().getSearchWidget());
				} else {
					cqlLibraryAdminView.getWidgetVP().clear();
				}

			}
		});*/

		cqlLibraryAdminView.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int startIndex = 1;
				cqlLibraryAdminView.getErrorMessageAlert().clearAlert();

				search(cqlLibraryAdminView.getSearchString().getValue(), cqlLibraryAdminView.getSelectedFilter(),
						startIndex, Integer.MAX_VALUE);
			}
		});

		cqlLibraryAdminView.getTransferButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				transferOwnershipView.getSearchString().setValue("");
				displayTransferView("", 1, Integer.MAX_VALUE);
			}
		});
		
		cqlLibraryAdminView.getClearAllButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				displaySearch();
				//cqlLibraryAdminView.clearTransferCheckBoxes();
			}
		});

	}

	private void addHistoryDisplayHandlers() {
		historyDisplay.getReturnToLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displaySearch();

			}
		});
	}

	private void addObserverHandler() {
		cqlLibraryAdminView.getCQLLibrarySearchView().setAdminObserver(new CQLLibrarySearchView.AdminObserver() {

			@Override
			public void onTransferSelectedClicked(CQLLibraryDataSetObject result) {
				updateTransferIDs(result);

			}

			@Override
			public void onHistoryClicked(CQLLibraryDataSetObject result) {
				historyDisplay.setReturnToLinkText("<< Return to CQL Library Ownership");
				displayHistory(result.getId(), result.getCqlName());

			}
		});
	}

	/**
	 * Transfer display handlers.
	 * 
	 * @param transferDisplay
	 *            the transfer display
	 */
	private void addTransferOwnershipViewHandlers() {

		transferOwnershipView.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				transferOwnershipView.getErrorMessageDisplay().clear();
				transferOwnershipView.getSuccessMessageDisplay().clear();
				boolean userSelected = false;
				for (int i = 0; i < model.getData().size(); i = i + 1) {
					if (model.getData().get(i).isSelected()) {
						userSelected = true;
						final String emailTo = model.getData().get(i).getEmailId();
						final int rowIndex = i;

						MatContext.get().getCQLLibraryService().transferLibraryOwnerShipToUser(
								cqlLibraryAdminView.getSelectedId(), emailTo, new AsyncCallback<Void>() {
									@Override
									public void onFailure(Throwable caught) {
										Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
										model.getData().get(rowIndex).setSelected(false);
										transferOwnershipView.clearRadioButtons();
									}

									@Override
									public void onSuccess(Void result) {
										transferOwnershipView.getSuccessMessageDisplay().createAlert(
												MatContext.get().getMessageDelegate().getTransferOwnershipSuccess()
														+ emailTo);
										model.getData().get(rowIndex).setSelected(false);
										transferOwnershipView.clearRadioButtons();
									}
								});
					}
				}
				if (userSelected == false) {
					transferOwnershipView.getSuccessMessageDisplay().clear();
					transferOwnershipView.getErrorMessageDisplay()
							.createAlert(MatContext.get().getMessageDelegate().getUserRequiredErrorMessage());
				}

			}

		});
		transferOwnershipView.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cqlLibraryAdminView.getSelectedLibraries().clear();
				cqlLibraryAdminView.getSelectedId().clear();
				transferOwnershipView.getSuccessMessageDisplay().clear();
				transferOwnershipView.getErrorMessageDisplay().clear();
				displaySearch();
			}
		});

		transferOwnershipView.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				transferOwnershipView.getSuccessMessageDisplay().clear();
				displayTransferView(transferOwnershipView.getSearchString().getValue(), 1, Integer.MAX_VALUE);

			}
		});
	}

	/**
	 * Display history.
	 *
	 * @param cqlLibraryId
	 *            the cql library id
	 * @param cqlLibraryName
	 *            the cql library name
	 */
	private void displayHistory(String cqlLibraryId, String cqlLibraryName) {
		int startIndex = 0;
		int pageSize = Integer.MAX_VALUE;
		String heading = "CQL Library > History";
		panel.getButtonPanel().clear();
		panel.setHeading(heading, "CQL Library");
		searchHistory(cqlLibraryId, startIndex, pageSize);
		historyDisplay.setCQLLibraryId(cqlLibraryId);
		historyDisplay.setCQLLibraryName(cqlLibraryName);
		panel.setContent(historyDisplay.asWidget());
		Mat.focusSkipLists("CQLLibraryOwnership");
	}

	/**
	 * Display transfer view.
	 *
	 * @param searchString
	 *            the search string
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 */
	private void displayTransferView(String searchString, int startIndex, int pageSize) {

		pageSize = Integer.MAX_VALUE;
		cqlLibraryAdminView.getErrorMessageAlert().clearAlert();
		transferOwnershipView.getErrorMessageDisplay().clear();

		if (cqlLibraryAdminView.getSelectedLibraries().size() != 0) {
			showSearchingBusy(true);
			MatContext.get().getMeasureService().searchUsers(searchString, startIndex, pageSize,
					new AsyncCallback<TransferOwnerShipModel>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showSearchingBusy(false);
						}

						@Override
						public void onSuccess(TransferOwnerShipModel result) {

							transferOwnershipView.buildHTMLForLibraries(cqlLibraryAdminView.getSelectedLibraries());
							transferOwnershipView.buildCellTable(result);
							panel.setHeading("CQL Library Ownership >  CQL Ownership Transfer", "CQLLibraryOwnership");
							panel.setContent(transferOwnershipView.asWidget());
							showSearchingBusy(false);
							model = result;
						}
					});
		} else {
			
			 cqlLibraryAdminView.getErrorMessageAlert().createAlert( MatContext.get().getMessageDelegate().getTRANSFER_CHECKBOX_ERROR_CQL_LIBRARY());
			
		}

	}

	/**
	 * Search history.
	 *
	 * @param cqlLibraryId
	 *            the cql library id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 */
	private void searchHistory(String cqlLibraryId, int startIndex, int pageSize) {
		List<String> filterList = new ArrayList<String>();

		MatContext.get().getAuditService().executeSearch(cqlLibraryId, startIndex, pageSize, filterList,
				new AsyncCallback<SearchHistoryDTO>() {
					@Override
					public void onFailure(Throwable caught) {
						historyDisplay.getErrorAlert()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null,
								"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
					}

					@Override
					public void onSuccess(SearchHistoryDTO data) {
						historyDisplay.buildCellTable(data.getLogs());
					}
				});

	}

	private void updateTransferIDs(CQLLibraryDataSetObject result) {
		List<String> selectedIdList = cqlLibraryAdminView.getSelectedId();
		if (result.isTransferable()) {

			if (!selectedIdList.contains(result.getId())) {
				cqlLibraryAdminView.getSelectedLibraries().add(result);
				selectedIdList.add(result.getId());
			}
		} else {
			for (int i = 0; i < selectedIdList.size(); i++) {
				if (result.getId().equals(cqlLibraryAdminView.getSelectedLibraries().get(i).getId())) {
					selectedIdList.remove(i);
					cqlLibraryAdminView.getSelectedLibraries().remove(i);
				}
			}

		}
	}

	/**
	 * This method reterives all Libraries in CQL Library tab based on Selected
	 * filters and Search Input.
	 *
	 * @param searchText
	 *            the search text
	 * @param filter
	 *            the filter
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 */
	private void search(final String searchText, final int filter, int startIndex, int pageSize) {
		final String lastSearchText = (searchText != null) ? searchText.trim() : null;
		// pageSize = Integer.MAX_VALUE;
		pageSize = 25;
		showSearchingBusy(true);
		cqlLibraryAdminView.getSelectedId().clear();
		cqlLibraryAdminView.getSelectedLibraries().clear();
		cqlLibraryAdminView.getErrorMessageAlert().clearAlert();
		MatContext.get().getCQLLibraryService().search(lastSearchText, filter, startIndex, pageSize,
				new AsyncCallback<SaveCQLLibraryResult>() {

					@Override
					public void onSuccess(SaveCQLLibraryResult result) {
						cqlLibraryAdminView.getCQLLibrarySearchView().setCQLLibraryListLabel("Available CQL Libraries");

						if ((result.getResultsTotal() == 0) && !lastSearchText.isEmpty()) {
							cqlLibraryAdminView.getErrorMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getNoLibrarues());
						}
						SearchResultUpdate sru = new SearchResultUpdate();
						sru.update(result, (TextBox) cqlLibraryAdminView.getSearchString(), lastSearchText);
						cqlLibraryAdminView.buildCellTable(result, lastSearchText, filter);
						showSearchingBusy(false);
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
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
		cqlLibraryAdminView.getErrorMessageAlert().clearAlert();
		isSearchBarVisible = true;
	}

	@Override
	public void beforeDisplay() {
		cqlLibraryAdminView.buildDefaultView();
		displaySearch();

	}

	@Override
	public Widget getWidget() {
		// TODO Auto-generated method stub
		return panel;
	}

}
