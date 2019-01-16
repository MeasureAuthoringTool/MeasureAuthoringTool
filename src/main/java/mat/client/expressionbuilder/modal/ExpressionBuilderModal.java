package mat.client.expressionbuilder.modal;

import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.PanelType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;

public class ExpressionBuilderModal extends Modal {
	private static final String CQL_EXPRESSION = "CQL Expression";
	private ModalHeader header;
	private ModalBody body;
	private ModalFooter footer;
	private VerticalPanel contentPanel;
	private AceEditor editor;
	private ExpressionBuilderModel model;
	private ErrorMessageAlert errorAlert;
	
	public ExpressionBuilderModal(String title, ExpressionBuilderModel model) {
		this.model = model;
		this.setDataBackdrop(ModalBackdrop.STATIC);
		this.setDataKeyboard(false);
		this.setClosable(false);
		
		header = new ModalHeader();
		header.setClosable(false);
		body = new ModalBody();
		footer = new ModalFooter();
		contentPanel = new VerticalPanel();
		
		this.setId("expressionBuilderModal");
		contentPanel.setWidth("100%");
		
		header.setTitle(title);
		
		body.add(buildErrorAlert());
		body.add(contentPanel);
		body.add(buildAceEditorPanel());
		
		this.add(header);
		this.add(body);
		this.add(footer);
	}
	
	public ModalBody getBody() {
		return body;
	}
	
	public ModalFooter getFooter() {
		return footer;
	}

	public ExpressionBuilderModel getModel() {
		return model;
	}

	public VerticalPanel getContentPanel() {
		return contentPanel;
	}
	
	public void updateCQLDisplay() {
		this.editor.setText(model.getCQL());
	}
	
	public MessageAlert getErrorAlert() {
		return errorAlert;
	}
	
	private MessageAlert buildErrorAlert() {
		errorAlert = new ErrorMessageAlert();
		return errorAlert;
	}

	private Panel buildAceEditorPanel() {
		Panel cqlExpressionPanel = new Panel();
		cqlExpressionPanel.setMarginLeft(0.0);
		cqlExpressionPanel.setMarginRight(0.0);
		cqlExpressionPanel.setType(PanelType.PRIMARY);
		
		PanelHeader cqlExpressionPanelHeader = new PanelHeader();
		cqlExpressionPanelHeader.setText(CQL_EXPRESSION);
		cqlExpressionPanelHeader.setTitle(CQL_EXPRESSION);
		cqlExpressionPanelHeader.setStyleName("expressionBuilderExpressionPanel", true);
		PanelBody cqlExpressionPanelBody = new PanelBody();
		cqlExpressionPanelBody.add(buildAceEditor());
		
		cqlExpressionPanel.add(cqlExpressionPanelHeader);
		cqlExpressionPanel.add(cqlExpressionPanelBody);
		return cqlExpressionPanel;
	}
	
	private AceEditor buildAceEditor() {
		editor = new AceEditor();
		editor.startEditor();
		editor.setText("");
		editor.setMode(AceEditorMode.CQL);
		editor.setTheme(AceEditorTheme.ECLIPSE);
		editor.getElement().getStyle().setFontSize(14, Unit.PX);
		editor.setHeight("150px");
		editor.setAutocompleteEnabled(true);
		editor.addAutoCompletions();
		editor.setUseWrapMode(true);
		editor.removeAllMarkers();
		editor.clearAnnotations();
		editor.getElement().setAttribute("id", "cql_expression_editor");
		editor.setTitle(CQL_EXPRESSION);
		editor.setReadOnly(true);
		return editor; 
	}
	
	public void display() {
		
	}
}
