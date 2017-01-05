package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	
	/**
	 * Textbox aliasNameTxtArea.
	 */
	private TextBox aliasNameTxtArea = new TextBox();
	
	public CQLIncludeLibraryView(){
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().setId("vPanel_VerticalPanel");
		verticalPanel.add(new SpacerWidget());
		
		HorizontalPanel aliasNameFP = new HorizontalPanel();
		HorizontalPanel aliasFP = new HorizontalPanel();
		
		Label aliasLabel = new Label(LabelType.INFO, "Alias Name");
		aliasLabel.setMarginTop(5);
		aliasLabel.setId("Alias_Label");
		aliasNameTxtArea.setText("");
		aliasNameTxtArea.setSize("260px", "25px");
		aliasNameTxtArea.getElement().setId("aliasNameField");
		aliasNameTxtArea.setName("aliasName");
		aliasLabel.setText("Alias Name");
		
		aliasNameFP.add(aliasLabel);
		aliasNameFP.add(new SpacerWidget());
		aliasFP.add(aliasNameTxtArea);
		aliasFP.setStyleName("cqlRightContainer");
		
		verticalPanel.add(aliasNameFP);
		verticalPanel.add(aliasFP);
		containerPanel.getElement().setAttribute("id",
				"subQDMAPPliedListContainerPanel");
		containerPanel.add(verticalPanel);
		containerPanel.setStyleName("cqlqdsContentPanel");
		
		
		
	}

	public Widget asWidget() {
		return containerPanel;
	}

	public TextBox getAliasNameTxtArea() {
		return aliasNameTxtArea;
	}

	public void setAliasNameTxtArea(String string) {
		this.aliasNameTxtArea.setText("");
		
	}
	
	
}
