package mat.client.admin;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.CancelButton;
import mat.client.cql.CQLLibrarySearchView;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.model.cql.CQLLibraryDataSetObject;

public class ManageCQLLibraryAdminView implements ManageCQLLibraryAdminPresenter.ViewDisplay {
	
	/** The Insert button. */
	private Button transferButton = new Button();
	
	/** The save button. */
	private Button clearAllButton = new CancelButton("managecqllibadminview");
	
	/** The button layout. */
	private HorizontalPanel buttonLayout = new HorizontalPanel();
	
	List<String> selectedId = new ArrayList<String>();
	
	List<CQLLibraryDataSetObject> selectedLibraries = new ArrayList<CQLLibraryDataSetObject>();
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();

	VerticalPanel widgetVP = new VerticalPanel();
	/**
	 * ALL measure filter value.
	 */
	public static final int ALL = 1;

	private CQLLibrarySearchView cqlLibrarySearchView = new CQLLibrarySearchView();

	SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search");

	/** The CQL error message. */
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();

	public CQLLibrarySearchView getCqlLibrarySearchView() {
		return cqlLibrarySearchView;
	}

	public void setCqlLibrarySearchView(CQLLibrarySearchView cqlLibrarySearchView) {
		this.cqlLibrarySearchView = cqlLibrarySearchView;
	}

	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}

	public void setErrorMessageAlert(MessageAlert errorMessageAlert) {
		this.errorMessageAlert = errorMessageAlert;
	}

	public ManageCQLLibraryAdminView() {
		mainPanel.setWidth("100%");
		buttonLayout.getElement().setId("cql_buttonLayout_HorizontalPanel");
		transferButton.setType(ButtonType.PRIMARY);
		transferButton.getElement().setId("transferButton");
		transferButton.setTitle("Transfer");
		transferButton.setText("Transfer");
		transferButton.getElement().setAttribute("aria-label", "Transfer");
		
		clearAllButton.setMarginLeft(10.00);
		clearAllButton.setTitle("Clear All");
		clearAllButton.setText("Clear All");
		clearAllButton.getElement().setAttribute("aria-label", "clearAllButton");
		
		buttonLayout.add(transferButton);
		buttonLayout.add(clearAllButton);
	}

	@Override
	public Widget asWidget() {
		widgetVP.clear();
		widgetVP.add(searchWidgetBootStrap.getSearchWidget());
		return mainPanel;
	}

	@Override
	public void buildDefaultView() {
		mainPanel.clear();
		widgetVP.clear();
		errorMessageAlert.clearAlert();
		searchWidgetBootStrap.getSearchBox().setWidth("232px");
		
		HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
		mainHorizontalPanel.getElement().setId("panel_MainHorizontalPanel_CQL");
		mainPanel.getElement().setId("CQLLibrary_MainPanel");
		mainPanel.setStyleName("contentPanel");
		
		widgetVP.getElement().setAttribute("style", "width:100px;margin-top:20px;");
		widgetVP.getElement().setId("panel_VP_CQL");
		mainHorizontalPanel.add(widgetVP);
		mainHorizontalPanel.add(new SpacerWidget());
		mainPanel.add(mainHorizontalPanel);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(cqlLibrarySearchView.buildCQLLibraryCellTable());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(errorMessageAlert);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buttonLayout);
	}

	@Override
	public VerticalPanel getWidgetVP() {
		return widgetVP;
	}

	@Override
	public CQLLibrarySearchView getCQLLibrarySearchView() {

		return cqlLibrarySearchView;
	}

	@Override
	public int getSelectedFilter() {
		return ALL;

	}

	@Override
	public HasValue<String> getSearchString() {
		return searchWidgetBootStrap.getSearchBox();

	}

	@Override
	public HasClickHandlers getSearchButton() {
		return searchWidgetBootStrap.getGo();

	}

	@Override
	public void buildCellTable(SaveCQLLibraryResult result, String searchText, int filter) {
		cqlLibrarySearchView.buildCellTable(result, searchText, filter);
	}
	
	@Override
	public SearchWidgetBootStrap getSearchWidgetBootStrap() {
		return searchWidgetBootStrap;
	}
	@Override
	public List<String> getSelectedId() {
		return selectedId;
	}
	@Override
	public void setSelectedId(List<String> selectedId) {
		this.selectedId = selectedId;
	}
	@Override
	public List<CQLLibraryDataSetObject> getSelectedLibraries() {
		return selectedLibraries;
	}
	@Override
	public void setSelectedLibraries(List<CQLLibraryDataSetObject> selectedLibraries) {
		this.selectedLibraries = selectedLibraries;
	}
	@Override
	public Button getClearAllButton() {
		return clearAllButton;
	}
	@Override
	public Button getTransferButton() {
		return transferButton;
	}

}
