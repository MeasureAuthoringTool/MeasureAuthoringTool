package mat.client;

import org.gwtbootstrap3.client.ui.TextArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.clause.cqlworkspace.CQLLibraryDetailView;
import mat.client.event.CQLLibraryEditEvent;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.CreateNewItemWidget;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SkipListBuilder;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;

/**
 * @author jnarang
 *
 */
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
		VerticalPanel getMainPanel();

		/**
		 * Generates View for CQLWorkSpace tab.
		 */
		void buildDefaultView();

		CreateNewItemWidget getCreateNewItemWidget();

		CustomButton getAddNewFolderButton();

		CustomButton getZoomButton();

		Widget asWidget();

		String getSelectedOption();

		MessageAlert getErrorMessageAlert();

		void buildCreateNewView();

		void clearSelections();

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
				isCreateNewItemWidgetVisible = !isCreateNewItemWidgetVisible;
				cqlLibraryView.getCreateNewItemWidget().setVisible(isCreateNewItemWidgetVisible);

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
		fp.getElement().setId("fp_FlowPanel");
		isCreateNewItemWidgetVisible = false;
		cqlLibraryView.getCreateNewItemWidget().setVisible(false);
		panel.getButtonPanel().clear();
		panel.setButtonPanel(cqlLibraryView.getAddNewFolderButton(), cqlLibraryView.getZoomButton());
		fp.add(cqlLibraryView.asWidget());
		panel.setContent(fp);
		Mat.focusSkipLists("CQLLibrary");
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

}
