package mat.client.expressionbuilder.component;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Pre;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import java.util.Random;

public class ExpandCollapseCQLExpressionPanel extends Composite {

	private String header;
	private String cql;
	private PanelGroup expressionPanelGroup;
	private Button deleteButton;

	public ExpandCollapseCQLExpressionPanel(String header, String cql) {
		this.header = header;
		this.cql = cql;
		initWidget(buildExpressionCollapsePanel());
	}
	
	private PanelGroup buildExpressionCollapsePanel() {	
		Random rand = new Random();
		int index = rand.nextInt(Integer.MAX_VALUE);
		
		expressionPanelGroup = new PanelGroup();
		expressionPanelGroup.setWidth("100%");
		expressionPanelGroup.setId("accordion" + index);
		
		Panel expressionPanel = new Panel();
		expressionPanel.setType(PanelType.SUCCESS);
		expressionPanel.setWidth("100%");
		expressionPanel.setMarginLeft(0.0);
		PanelHeader expressionPanelHeader = new PanelHeader();
		Anchor anchor = new Anchor();
		anchor.setText(header);
		anchor.setTitle(header);
		anchor.setIcon(IconType.PLUS);
		anchor.setColor("black");
		anchor.setDataParent("#accordion" + index);
		anchor.setDataToggle(Toggle.COLLAPSE);
		anchor.setDataTarget("#collapse" + index);
		expressionPanelHeader.add(anchor);
		
		PanelCollapse expressionPanelCollapse = new PanelCollapse();
		expressionPanelCollapse.setId("collapse" + index);
		PanelBody expressionPanelBody = new PanelBody();		
		
		FocusPanel cqlLogicFocusPanel = new FocusPanel();
		cqlLogicFocusPanel.getElement().setAttribute("aria-label", cql);
		Pre cqlPre = new Pre();
		cqlPre.setText(cql);
		cqlPre.setTitle(cql);
		cqlLogicFocusPanel.add(cqlPre);

		expressionPanelBody.add(cqlLogicFocusPanel);
		
		deleteButton = new Button();
		deleteButton.setTitle("Delete");
		deleteButton.setText("Delete");
		deleteButton.setPull(Pull.RIGHT);
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.setType(ButtonType.DANGER);
		expressionPanelBody.add(deleteButton);
		
		expressionPanelCollapse.add(expressionPanelBody);

		expressionPanel.add(expressionPanelHeader);
		expressionPanel.add(expressionPanelCollapse);
		
		
		
		expressionPanelGroup.add(expressionPanel);
		
		
		
		anchor.addClickHandler(event -> onAnchorClick(anchor));
		

		return expressionPanelGroup;
	}
	
	public Button getDeleteButton() {
		return this.deleteButton;
	}
		
	private void onAnchorClick(Anchor anchor) {
		if(anchor.getIcon() == IconType.PLUS) {
			anchor.setIcon(IconType.MINUS);
		} else {
			anchor.setIcon(IconType.PLUS);
		}
	}	
}
