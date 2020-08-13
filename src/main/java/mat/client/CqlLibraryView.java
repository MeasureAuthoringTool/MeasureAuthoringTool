package mat.client;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.advancedsearch.AdvancedSearchPillPanel;
import mat.client.buttons.CustomButton;
import mat.client.cql.CQLLibrarySearchView;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.MostRecentCQLLibraryWidget;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.LibrarySearchModel;
import org.gwtbootstrap3.client.ui.Button;

public class CqlLibraryView implements CqlLibraryPresenter.ViewDisplay {

	private FlowPanel mainPanel = new FlowPanel();
	
	private Button createNewLibraryButton = new Button("New Library");

	private SearchWidgetWithFilter searchFilterWidget = new SearchWidgetWithFilter("searchFilter", "measureLibraryFilterDisclosurePanel", "forCqlLibrary");
	

	CustomButton addNewFolderButton = (CustomButton) getImage("Create New Item",
			ImageResources.INSTANCE.createMeasure(), "Create New Item", "createNewItemPlusButton");

	CustomButton zoomButton = (CustomButton) getImage("Search", ImageResources.INSTANCE.search_zoom(), "Search",
			"CQLSearchButton");

	VerticalPanel mostRecentVerticalPanel = new VerticalPanel();

	private CQLLibrarySearchView cqlLibrarySearchView = new CQLLibrarySearchView();
	
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();
	
	VerticalPanel widgetVP = new VerticalPanel();
	
	
	private MostRecentCQLLibraryWidget mostRecentLibraryWidget = new MostRecentCQLLibraryWidget();
	
	private MessageAlert successMessageAlert = new SuccessMessageAlert();
	
	private EditConfirmationDialogBox draftConfirmationDialogBox = new EditConfirmationDialogBox();
	
	AdvancedSearchPillPanel pillPanel = new AdvancedSearchPillPanel("CQL Library");

	@Override
	public VerticalPanel getWidgetVP() {
		return widgetVP;
	}

	@Override
	public FlowPanel getMainPanel() {
		return mainPanel;
	}

	public CqlLibraryView() {
		mainPanel.setWidth("100%");
	}

	@Override
	public void buildMostRecentWidget() {
		mostRecentVerticalPanel.clear();
		mostRecentVerticalPanel.add(mostRecentLibraryWidget.buildMostRecentWidget());
	}
	
	@Override
	public void buildDefaultView() {
		
		mainPanel.clear();
		widgetVP.clear();
		errorMessageAlert.clearAlert();
		successMessageAlert.clearAlert();
		mainPanel.getElement().setId("CQLLibrary_MainPanel");
		mainPanel.setStyleName("contentPanel");
		
		widgetVP.setWidth("100px");
		widgetVP.getElement().setId("panel_VP_CQL");
		mostRecentVerticalPanel.clear();
		buildMostRecentWidget();
		mainPanel.add(mostRecentVerticalPanel);
		mainPanel.add(widgetVP);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessageAlert);
		mainPanel.add(errorMessageAlert);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(searchFilterWidget);
		mainPanel.add(pillPanel.getBadgeTable());
		mainPanel.add(cqlLibrarySearchView.buildCQLLibraryCellTable());
	}
	
	@Override
	public void buildCreateNewView(){
		mainPanel.clear();
	}
	
	@Override
	public void buildCellTable(SaveCQLLibraryResult result, LibrarySearchModel librarySearchModel,int filter) {
		cqlLibrarySearchView.buildCellTable(result, librarySearchModel, filter);
	}
	@Override
	public VerticalPanel getCellTablePanel() {
		return cqlLibrarySearchView.getCellTablePanel();
	}

	public void setMainPanel(FlowPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	@Override
	public CustomButton getAddNewFolderButton() {
		return addNewFolderButton;
	}

	@Override
	public CustomButton getZoomButton() {
		return zoomButton;
	}

	private Widget getImage(String action, ImageResource url, String key, String id) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		image.getElement().setAttribute("id", id);
		return image;
	}

	@Override
	public Widget asWidget() {
		widgetVP.clear();
		return mainPanel;
	}
	
	@Override
	public Button getCreateNewLibraryButton() {
		return createNewLibraryButton;
	}
	
	@Override
	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}

	public void setErrorMessageAlert(MessageAlert errorMessageAlert) {
		this.errorMessageAlert = errorMessageAlert;
	}
	

	
	@Override
	public CQLLibrarySearchView getCQLLibrarySearchView() {
		return cqlLibrarySearchView;
	}

	@Override
	public HasSelectionHandlers<CQLLibraryDataSetObject> getSelectIdForEditTool() {
		return cqlLibrarySearchView;
	}
	@Override
	public SearchWidgetWithFilter getSearchFilterWidget() {
		return searchFilterWidget;
	}
	
	@Override
	public int getSelectedFilter() {
		return searchFilterWidget.getSelectedFilter();
		
	}
	
	@Override
	public HasValue<String> getSearchString() {
		return searchFilterWidget.getSearchInput();
		
	}
	
	@Override
	public HasClickHandlers getSearchButton() {
		return searchFilterWidget.getSearchButton();
	}
	
	@Override
	public CustomCheckBox getCustomFilterCheckBox() {
		return searchFilterWidget.getLibraryCustomCheckBox();
	}
	
	@Override
	public MostRecentCQLLibraryWidget getMostRecentLibraryWidget() {
		return mostRecentLibraryWidget;
	}

	public void setMostRecentLibraryWidget(MostRecentCQLLibraryWidget mostRecentLibraryWidget) {
		this.mostRecentLibraryWidget = mostRecentLibraryWidget;
	}

	@Override
	public MessageAlert getSuccessMessageAlert() {
		return successMessageAlert;
	}

	public void setSuccessMessageAlert(MessageAlert successMessageAlert) {
		this.successMessageAlert = successMessageAlert;
	}

	public EditConfirmationDialogBox getDraftConfirmationDialogBox() {
		return draftConfirmationDialogBox;
	}

	@Override
	public void resetMessageDisplay() {
		getSuccessMessageAlert().clearAlert();
		getErrorMessageAlert().clearAlert();
	}
	
	@Override
	public AdvancedSearchPillPanel getSearchPillPanel() {
		return pillPanel;
	}
	
	@Override
	public void resetSearchDisplay() {
		searchFilterWidget.getLibraryCustomCheckBox().setValue(true);
		searchFilterWidget.getSearchInput().setValue("");
		searchFilterWidget.getAdvancedSearchPanel().resetDisplay(false);
		searchFilterWidget.getSearchInput().getElement().focus();
		searchFilterWidget.setSelectedFilter(SearchWidgetWithFilter.MY);
		errorMessageAlert.clearAlert();
	}
	
	@Override
	public VerticalPanel getMostRecentLibraryVerticalPanel() {
		return this.mostRecentVerticalPanel;
	}


}
