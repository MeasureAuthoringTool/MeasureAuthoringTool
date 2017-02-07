package mat.client;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.cql.CQLLibrarySearchView;
import mat.client.cql.ManageCQLLibrarySearchModel;
import mat.client.shared.CreateNewItemWidget;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.model.cql.CQLLibraryDataSetObject;

/**
 * The Class CqlLibraryView.
 */
public class CqlLibraryView implements CqlLibraryPresenter.ViewDisplay {

	/** The main panel. */
	private VerticalPanel mainPanel = new VerticalPanel();

	/** The create measure widget. */
	private CreateNewItemWidget createNewItemWidget = new CreateNewItemWidget("cqlLib");

	CustomButton addNewFolderButton = (CustomButton) getImage("Create New Item",
			ImageResources.INSTANCE.createMeasure(), "Create New Item", "createNewItemPlusButton");

	CustomButton zoomButton = (CustomButton) getImage("Search", ImageResources.INSTANCE.search_zoom(), "Search",
			"CQLSearchButton");

	/** The most recent vertical panel. */
	VerticalPanel mostRecentVerticalPanel = new VerticalPanel();
	/** VerticalPanel Instance which hold's View for Most Recent Measure. */
	private VerticalPanel mostRecentVPanel = new VerticalPanel();
	
	private CQLLibrarySearchView cqlLibrarySearchView = new CQLLibrarySearchView();
	
	/** The CQL error message. */
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();
	

	@Override
	public VerticalPanel getMainPanel() {
		// TODO Auto-generated method stub
		return mainPanel;
	}

	public CqlLibraryView() {
		
		mainPanel.setWidth("98.5%");

	}

	/*
	 * public void buildMostRecentWidget() { mostRecentVerticalPanel.clear();
	 * mostRecentVerticalPanel.add(mostRecentMeasureWidget.buildMostRecentWidget
	 * ()); }
	 */

	/**
	 * Builds the most recent widget.
	 * 
	 * @return VerticalPanel.
	 */
	public VerticalPanel buildMostRecentWidget() {
		mostRecentVPanel.clear();
		mostRecentVPanel.getElement().setId("mostRecentVPanel_VerticalPanel");
		mostRecentVPanel.setStyleName("recentSearchPanel");

		Label recentActivityHeader = new Label("Recent Activity");
		recentActivityHeader.getElement().setId("mostRecentVPanelHeader_Label");
		recentActivityHeader.setStyleName("recentSearchHeader");
		recentActivityHeader.getElement().setAttribute("tabIndex", "0");
		HTML desc = new HTML("<p> No Recent Activity</p>");
		mostRecentVPanel.add(recentActivityHeader);
		mostRecentVPanel.add(new SpacerWidget());
		mostRecentVPanel.add(desc);
		return mostRecentVPanel;
	}
	
	/*public FlowPanel buildCQLLibraryCellTable(){
		
		cellTablePanel.getElement().setId("cqlLibrarySearchView_mainPanel");
		cellTablePanel.setStylePrimaryName("measureSearchResultsContainer");
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.getElement().setId("cqlCellTablePanel_VerticalPanel");
		cellTablePanel.add(getCellTablePanel());
		cellTablePanel.setStyleName("serachView_mainPanel");
		return cellTablePanel;
	}*/
	

	@Override
	public void buildDefaultView() {
		
		mainPanel.clear();
		errorMessageAlert.clearAlert();
		HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
		mainHorizontalPanel.getElement().setId("panel_MainHorizontalPanel");
		mainPanel.getElement().setId("CQLLibrary_MainPanel");
		mainPanel.setStyleName("contentPanel");
		VerticalPanel widgetVP = new VerticalPanel();
		widgetVP.setWidth("100px");
		widgetVP.getElement().setId("panel_VP");
		widgetVP.add(createNewItemWidget);
		// measureFilterVP.add(measureSearchFilterWidget);
		// buildMostRecentWidget();
		// mostRecentVerticalPanel.setWidth("80%");
		mostRecentVerticalPanel.clear();
		mostRecentVerticalPanel.add(buildMostRecentWidget());
		
		mainHorizontalPanel.add(new SpacerWidget());
		mainHorizontalPanel.add(mostRecentVerticalPanel);
		mainHorizontalPanel.add(widgetVP);
		mainHorizontalPanel.add(new SpacerWidget());
		mainHorizontalPanel.add(new SpacerWidget());
		mainHorizontalPanel.add(new SpacerWidget());
		mainPanel.add(errorMessageAlert);
		mainPanel.add(mainHorizontalPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(cqlLibrarySearchView.buildCQLLibraryCellTable());
		

	}
	
	@Override
	public void buildCreateNewView(){
		mainPanel.clear();
		
	}
	
	@Override
	public void buildCellTable(ManageCQLLibrarySearchModel searchModel, String searchText) {
		cqlLibrarySearchView.buildCellTable(searchModel, searchText);
	}

	@Override
	public CreateNewItemWidget getCreateNewItemWidget() {
		return createNewItemWidget;
	}

	public void setCreateNewItemWidget(CreateNewItemWidget CreateNewItemWidget) {
		this.createNewItemWidget = CreateNewItemWidget;
	}

	public void setMainPanel(VerticalPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	@Override
	public CustomButton getAddNewFolderButton() {
		return addNewFolderButton;
	}

	@Override
	/** @return the zoomButton */
	public CustomButton getZoomButton() {
		return zoomButton;
	}

	/**
	 * Add Image on Button with invisible text. This text will be available when
	 * css is turned off.
	 *
	 * @param action
	 *            - {@link String}
	 * @param url
	 *            - {@link ImageResource}.
	 * @param key
	 *            - {@link String}.
	 * @param id
	 *            the id
	 * @return - {@link Widget}.
	 */
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
		return mainPanel;
	}
	
	@Override
	public String getSelectedOption() {
		return createNewItemWidget.getOptions().getItemText(createNewItemWidget.getOptions().getSelectedIndex());
	}
	@Override
	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}

	public void setErrorMessageAlert(MessageAlert errorMessageAlert) {
		this.errorMessageAlert = errorMessageAlert;
	}
	
	@Override
	public void clearSelections() {
		createNewItemWidget.getOptions().setSelectedIndex(0);
	}
	
	@Override
	public CQLLibrarySearchView getCQLLibrarySearchView() {
		return cqlLibrarySearchView;
	}

	@Override
	public HasSelectionHandlers<CQLLibraryDataSetObject> getSelectIdForEditTool() {
		return cqlLibrarySearchView;
	}
	
}
