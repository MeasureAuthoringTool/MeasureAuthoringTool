/**
 * 
 */
package mat.client.measure;

import mat.client.ImageResources;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SearchWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageMeasureVersionView.
 * 
 * @author vandavar
 * 
 *         An view class to manage the widgets for the Version creation.
 */
public class ManageMeasureVersionView implements ManageMeasurePresenter.VersionDisplay{
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The major radio. */
	private RadioButton majorRadio = new RadioButton("group","Major");
	
	/** The minor radio. */
	private RadioButton minorRadio = new RadioButton("group","Minor");
	
	/** The measure search filter widget. */
	private SearchWidget searchWidget = new SearchWidget();
	
	/** The view. */
	private SearchView<ManageMeasureSearchModel.Result> view =
			new SearchView<ManageMeasureSearchModel.Result>("Measures");
	
	/** Zoom Button for Showing Search Widget. */
	private CustomButton zoomButton = (CustomButton) getImage("Search",
			ImageResources.INSTANCE.search_zoom(), "Search");
	
	/**
	 * Instantiates a new manage measure version view.
	 */
	public ManageMeasureVersionView() {
		zoomButton.getElement().getStyle().setMarginLeft(30, Unit.PX);
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.add(searchWidget);
		mainPanel.add(new Label("Select a Draft to create a Measure Version."));
		mainPanel.add(new SpacerWidget());
		mainPanel.add(view.asWidget());
		mainPanel.add(errorMessages);
		
		VerticalPanel radioPanel = new VerticalPanel();
		radioPanel.add(new Label("Select Version Type"));
		radioPanel.add(new SpacerWidget());
		radioPanel.add(majorRadio);
		radioPanel.add(minorRadio);
		
		
		mainPanel.add(radioPanel);
		SimplePanel buttonPanel = new SimplePanel();
		buttonBar.getSaveButton().setText("Save and Continue");
		buttonBar.getSaveButton().setTitle("Save and Continue");
		buttonBar.getCancelButton().setTitle("Cancel");
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		mainPanel.add(buttonBar);
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#buildDataTable(mat.client.shared.search.SearchResults, int, long, int, int)
	 */
	@Override
	public void buildDataTable(SearchResults<Result> results,int pageCount,long totalResults,int currentPage,int pageSize) {
		view.buildVersionDataTable(results,pageCount,totalResults,currentPage,pageSize);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getCurrentPage()
	 */
	@Override
	public int getCurrentPage(){
		return view.getCurrentPage();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/** Add Image on Button with invisible text. This text will be available when css is turned off.
	 * @param action - {@link String}
	 * @param url - {@link ImageResource}.
	 * @param key - {@link String}.
	 * @return - {@link Widget}. */
	private Widget getImage(String action, ImageResource url, String key) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		return image;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getMajorRadioButton()
	 */
	@Override
	public RadioButton getMajorRadioButton() {
		return majorRadio;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getMinorRadioButton()
	 */
	@Override
	public RadioButton getMinorRadioButton() {
		return minorRadio;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getPageSizeSelectionTool()
	 */
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	
	
	@Override
	public HasClickHandlers getSearchButton() {
		return searchWidget.getSearchButton();
	}
	
	
	
	/**
	 * @return the searchWidget
	 */
	@Override
	public SearchWidget getSearchWidget() {
		return searchWidget;
	}
	
	
	
	@Override
	public CustomButton getZoomButton() {
		return zoomButton;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#setCurrentPage(int)
	 */
	@Override
	public void setCurrentPage(int pageNumber){
		view.setCurrentPage(pageNumber);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#setPageSize(int)
	 */
	@Override
	public void setPageSize(int pageSize){
		view.setPageSize(pageSize);
	}
}
