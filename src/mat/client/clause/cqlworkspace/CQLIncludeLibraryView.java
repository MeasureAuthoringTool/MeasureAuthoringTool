package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.SpacerWidget;

public class CQLIncludeLibraryView {
	
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	
	/** The cell table panel. */
	private Panel cellTablePanel = new Panel();
	
	private PanelBody cellTablePanelBody = new PanelBody();
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 15;
	
	
	public CQLIncludeLibraryView(){
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().setId("vPanel_VerticalPanel");
		verticalPanel.add(new SpacerWidget());
		
		
		containerPanel.getElement().setAttribute("id",
				"subQDMAPPliedListContainerPanel");
		containerPanel.add(verticalPanel);
		containerPanel.setStyleName("cqlqdsContentPanel");
		
		
		
	}

	public Widget asWidget() {
		return containerPanel;
	}
	
}
