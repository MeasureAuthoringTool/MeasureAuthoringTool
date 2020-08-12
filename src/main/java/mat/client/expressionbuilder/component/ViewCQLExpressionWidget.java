package mat.client.expressionbuilder.component;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Pre;
import org.gwtbootstrap3.client.ui.constants.PanelType;

public class ViewCQLExpressionWidget extends Composite{
	private static final String CQL_EXPRESSION = "CQL Expression";
	private Panel cqlExpressionPanel;
	private Pre pre;
	private FocusPanel logicFocusPanel;
	
	public ViewCQLExpressionWidget() {
		initWidget(buildAceEditorPanel());
	}
	
	public Panel buildAceEditorPanel() {
		cqlExpressionPanel = new Panel();
		cqlExpressionPanel.setMarginLeft(0.0);
		cqlExpressionPanel.setMarginRight(0.0);
		cqlExpressionPanel.setType(PanelType.PRIMARY);
		PanelHeader cqlExpressionPanelHeader = new PanelHeader();
		cqlExpressionPanelHeader.setText(CQL_EXPRESSION);
		cqlExpressionPanelHeader.setTitle(CQL_EXPRESSION);
		cqlExpressionPanelHeader.setStyleName("expressionBuilderExpressionPanel", true);
		PanelBody cqlExpressionPanelBody = new PanelBody();
		cqlExpressionPanelBody.add(buildEditor());
		cqlExpressionPanel.add(cqlExpressionPanelHeader);
		cqlExpressionPanel.add(cqlExpressionPanelBody);
		return cqlExpressionPanel;
	}
	
	private FocusPanel buildEditor() {
		logicFocusPanel = new FocusPanel();
		pre = new Pre();
		logicFocusPanel.add(pre);
		return logicFocusPanel;
	}
	
	public void setCQLDisplay(String text) {
		this.logicFocusPanel.getElement().setAttribute("aria-label", "Generated CQL Expression " + text);
		this.pre.setText(text);
	}
	
	public void setCQLPanelVisible(boolean isVisible) {
		cqlExpressionPanel.setVisible(isVisible);
	}	
	
}
