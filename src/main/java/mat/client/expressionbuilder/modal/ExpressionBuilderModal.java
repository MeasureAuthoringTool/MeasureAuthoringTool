package mat.client.expressionbuilder.modal;

import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Pre;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.PanelType;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;

public abstract class ExpressionBuilderModal extends Modal {
	private static final String CQL_EXPRESSION = "CQL Expression";
	private ModalHeader header;
	private ModalBody body;
	private ModalFooter footer;
	private VerticalPanel contentPanel;
	private ExpressionBuilderModel parentModel;
	private ErrorMessageAlert errorAlert;
	private Pre pre;
	private FocusPanel logicFocusPanel;
	private ExpressionBuilderModel mainModel;
	private HelpBlock helpBlock;
	private Panel cqlExpressionPanel;
	
	public ExpressionBuilderModal(String title, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		this.parentModel = parentModel;
		this.mainModel = mainModel;
		this.setDataBackdrop(ModalBackdrop.STATIC);
		this.setDataKeyboard(false);
		this.getElement().setTabIndex(-1);
		this.setClosable(false);
		this.setRemoveOnHide(true);
		this.setHideOtherModals(true);
		
		header = new ModalHeader();
		header.setClosable(false);
		body = new ModalBody();
		footer = new ModalFooter();
		contentPanel = new VerticalPanel();
		
		this.setId("expressionBuilderModal");
		contentPanel.setWidth("100%");
		
		header.setTitle(title);
		
		
		body.add(buildHelpBlock(""));
		body.add(buildErrorAlert());
		body.add(contentPanel);
		body.add(buildAceEditorPanel());
		
		this.add(header);
		this.add(body);
		this.add(footer);
	}
	
	private Widget buildHelpBlock(String message) {
		helpBlock = new HelpBlock();
		helpBlock.setText(message);
		helpBlock.setColor("transparent");
		helpBlock.setHeight("0px");
		helpBlock.setPaddingBottom(0.0);
		helpBlock.setPaddingBottom(0.0);
		return helpBlock;
	}

	public ModalBody getBody() {
		return body;
	}
	
	public ModalFooter getFooter() {
		return footer;
	}

	public ExpressionBuilderModel getParentModel() {
		return parentModel;
	}

	public ExpressionBuilderModel getMainModel() {
		return mainModel;
	}
	
	public VerticalPanel getContentPanel() {
		return contentPanel;
	}
	
	public void updateCQLDisplay() {		
		setCQLDisplay(mainModel.getCQL(""));
	}
	
	public void setCQLDisplay(String text) {
		this.logicFocusPanel.getElement().setAttribute("aria-label", "Generated CQL Expression " + text);
		this.pre.setText(text);
	}
	
	public MessageAlert getErrorAlert() {
		return errorAlert;
	}
	
	public void setCQLPanelVisible(boolean isVisible) {
		cqlExpressionPanel.setVisible(isVisible);
	}
	
	public void setFooterVisible(boolean isVisible) {
		this.footer.setVisible(isVisible);
	}
	
	private MessageAlert buildErrorAlert() {
		errorAlert = new ErrorMessageAlert();
		return errorAlert;
	}

	private Panel buildAceEditorPanel() {
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

	public abstract void display();
	
	/**
	 * This method should be called when returning from a child screen
	 */
	public void showAndDisplay() {
		this.hide();
		this.show();
		helpBlock.setText("Successfully applied expression.");
		helpBlock.getElement().setAttribute("role", "alert");
		helpBlock.getElement().focus();
		display();
	}


}
