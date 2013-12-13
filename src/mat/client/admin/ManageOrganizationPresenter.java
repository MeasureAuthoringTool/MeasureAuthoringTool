package mat.client.admin;

import java.util.ArrayList;
import java.util.List;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.admin.service.SaveUpdateOrganizationResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.search.SearchResults;
import mat.shared.AdminManageOrganizationModelValidator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/** The Class ManageOrganizationPresenter. */
public class ManageOrganizationPresenter implements MatPresenter {
	/** The Interface DetailDisplay. */
	public static interface DetailDisplay {
		/** As widget.
		 * @return the widget */
		Widget asWidget();
		/** Gets the cancel button.
		 * @return the cancel button */
		HasClickHandlers getCancelButton();
		/** Gets the error message display.
		 * @return the error message display */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		/** Gets the oid.
		 * @return the oid */
		HasValue<String> getOid();
		/** Gets the organization.
		 * @return the organization */
		HasValue<String> getOrganization();
		/** Gets the save button.
		 * @return the save button */
		HasClickHandlers getSaveButton();
		/** Gets the success message display.
		 * @return the success message display */
		SuccessMessageDisplayInterface getSuccessMessageDisplay();
		/** Sets the title.
		 * @param title the new title */
		void setTitle(String title);
	}
	/** The Interface SearchDisplay. */
	public static interface SearchDisplay {
		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		Widget asWidget();
		/** Builds the data table.
		 * @param results the results */
		void buildDataTable(SearchResults<ManageOrganizationSearchModel.Result> results);
		/** Gets the creates the new button.
		 * @return the creates the new button */
		HasClickHandlers getCreateNewButton();
		/**
		 * Gets the search button.
		 *
		 * @return the search button
		 */
		HasClickHandlers getSearchButton();
		/**
		 * Gets the search string.
		 *
		 * @return the search string
		 */
		HasValue<String> getSearchString();
		/** Gets the select id for edit tool.
		 * @return the select id for edit tool */
		HasSelectionHandlers<ManageOrganizationSearchModel.Result> getSelectIdForEditTool();
	}
	/** The current details. */
	private ManageOrganizationDetailModel currentDetails;
	/** The detail display. */
	private DetailDisplay detailDisplay;
	/** The last search key. */
	private String lastSearchKey;
	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	/** The search display. */
	private SearchDisplay searchDisplay;
	/** ManageOrganizationDetailModel updatedDetails. */
	private ManageOrganizationDetailModel updatedDetails;
	/** Instantiates a new manage Organizations presenter.
	 * @param sDisplayArg the SearchDisplay
	 * @param dDisplayArg the DetailDisplay */
	public ManageOrganizationPresenter(SearchDisplay sDisplayArg, DetailDisplay dDisplayArg) {
		searchDisplay = sDisplayArg;
		detailDisplay = dDisplayArg;
		displaySearch();
		searchDisplay.getCreateNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNew();
			}
		});
		TextBox searchWidget = (TextBox) (searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button) searchDisplay.getSearchButton()).click();
				}
			}
		});
		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				update();
			}
		});
		detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displaySearch();
			}
		});
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String key = searchDisplay.getSearchString().getValue();
				search(key);
			}
		});
		searchDisplay.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<ManageOrganizationSearchModel.Result>() {
			@Override
			public void onSelection(SelectionEvent<ManageOrganizationSearchModel.Result> event) {
				edit(event.getSelectedItem().getOid());
			}
		});
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		displaySearch();
		Mat.focusSkipLists("Manage Organizations");
	}
	/** Creates the new. */
	private void createNew() {
		detailDisplay.setTitle("Add an Organization");
		currentDetails = new ManageOrganizationDetailModel();
		displayDetail();
	}
	/** Display detail. */
	private void displayDetail() {
		resetMessages();
		setOrganizationDetailsToView();
		panel.clear();
		panel.add(detailDisplay.asWidget());
		Mat.focusSkipLists("Manage Organizations");
	}
	/** Display search. */
	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		search("");
	}
	/** Edits the.
	 * @param key the key */
	private void edit(String key) {
		detailDisplay.setTitle("Update an Organization");
		MatContext.get().getAdminService().getOrganization(key,
				new AsyncCallback<ManageOrganizationDetailModel>() {
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay()
				.setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null,
						null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
			}
			@Override
			public void onSuccess(ManageOrganizationDetailModel result) {
				currentDetails = result;
				displayDetail();
			}
		});
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
	/** Gets the widget with heading.
	 * @param widget the widget
	 * @param heading the heading
	 * @return the widget with heading */
	public Widget getWidgetWithHeading(Widget widget, String heading) {
		FlowPanel vPanel = new FlowPanel();
		Label h = new Label(heading);
		h.addStyleName("myAccountHeader");
		h.addStyleName("leftAligned");
		vPanel.add(h);
		vPanel.add(widget);
		vPanel.addStyleName("myAccountPanel");
		widget.addStyleName("myAccountPanelContent");
		return vPanel;
	}
	/** @param model - ManageOrganizationDetailModel.
	 * @return boolean - isValid */
	private boolean isValid(ManageOrganizationDetailModel model) {
		AdminManageOrganizationModelValidator adminManageOrganizationModelValidator = new AdminManageOrganizationModelValidator();
		List<String> message = adminManageOrganizationModelValidator.isValidOrganizationDetail(model);
		boolean valid = message.size() == 0;
		if (!valid) {
			detailDisplay.getErrorMessageDisplay().setMessages(message);
		} else {
			detailDisplay.getErrorMessageDisplay().clear();
		}
		return valid;
	}
	/** Reset messages. */
	private void resetMessages() {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
	}
	/** Search.
	 * @param key the key
	 */
	private void search(String key) {
		lastSearchKey = key;
		showSearchingBusy(true);
		MatContext.get().getAdminService().searchOrganization(key, new AsyncCallback<ManageOrganizationSearchModel>() {
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay()
				.setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null,
						null, "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
				showSearchingBusy(false);
			}
			@Override
			public void onSuccess(ManageOrganizationSearchModel result) {
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, (TextBox) searchDisplay.getSearchString(), lastSearchKey);
				sru = null;
				searchDisplay.buildDataTable(result);
				showSearchingBusy(false);
				Mat.focusSkipLists("Manage Organizations");
			}
		});
	}
	/** Sets the user details to view. */
	private void setOrganizationDetailsToView() {
		detailDisplay.getOid().setValue(currentDetails.getOid());
		detailDisplay.getOrganization().setValue(currentDetails.getOrganization());
	}
	/** Show searching busy.
	 * @param busy the busy */
	private void showSearchingBusy(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		((Button) searchDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox) (searchDisplay.getSearchString())).setEnabled(!busy);
	}
	/** Update. */
	private void update() {
		resetMessages();
		updateOrganizationDetailsFromView();
		if (isValid(updatedDetails)) {
			MatContext.get().getAdminService().saveUpdateOrganization(currentDetails, updatedDetails,
					new AsyncCallback<SaveUpdateOrganizationResult>() {
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
				}
				@Override
				public void onSuccess(SaveUpdateOrganizationResult result) {
					if (result.isSuccess()) {
						displaySearch();
					} else {
						List<String> messages = new ArrayList<String>();
						switch (result.getFailureReason()) {
							case
							SaveUpdateOrganizationResult.OID_NOT_UNIQUE:
								messages.add("OID already exists.");
								break;
							case
							SaveUpdateOrganizationResult.SERVER_SIDE_VALIDATION:
								messages = result.getMessages();
								break;
							default:
								messages.add(MatContext.get().getMessageDelegate()
										.getUnknownErrorMessage(result.getFailureReason()));
						}
						detailDisplay.getErrorMessageDisplay().setMessages(messages);
					}
				}
			});
		}
	}
	/** Update Organization details from view. */
	private void updateOrganizationDetailsFromView() {
		updatedDetails = new ManageOrganizationDetailModel();
		updatedDetails.setOid(detailDisplay.getOid().getValue());
		updatedDetails.setOrganization(detailDisplay.getOrganization().getValue());
	}
}
