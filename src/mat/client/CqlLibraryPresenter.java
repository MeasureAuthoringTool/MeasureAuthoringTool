package mat.client;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.CreateNewItemWidget;
import mat.client.shared.CustomButton;
import mat.client.shared.FocusableWidget;
import mat.client.shared.SkipListBuilder;

public class CqlLibraryPresenter implements MatPresenter{
	
	/** The panel. */
	//private SimplePanel panel = new SimplePanel();
	
	/** The panel. */
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	
	/** The sub skip content holder. */
	private static FocusableWidget subSkipContentHolder;
	
	ViewDisplay cqlLibraryView;
	
	/** The is create measure widget visible. */
	boolean isCreateNewItemWidgetVisible = false;
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
		void buildView();

		CreateNewItemWidget getCreateNewItemWidget();

		CustomButton getAddNewFolderButton();

		CustomButton getZoomButton();

		Widget asWidget();

	}

	public CqlLibraryPresenter(CqlLibraryView cqlLibraryView) {
		this.cqlLibraryView = cqlLibraryView;
		displaySearch();
		addCQLLibraryViewHandlers();
	}

	private void addCQLLibraryViewHandlers() {
		cqlLibraryView.getAddNewFolderButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				isCreateNewItemWidgetVisible = !isCreateNewItemWidgetVisible;
				cqlLibraryView.getCreateNewItemWidget().setVisible(isCreateNewItemWidgetVisible);
				
			}
		});
		
	}

	private void displaySearch() {
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
	 * @param name the new sub skip embedded link
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
		
	}

	@Override
	public void beforeDisplay() {
		displaySearch();
		
	}

	@Override
	public Widget getWidget() {
		return panel;
	}

}
