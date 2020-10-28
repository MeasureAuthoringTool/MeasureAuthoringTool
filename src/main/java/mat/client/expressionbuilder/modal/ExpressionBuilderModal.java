package mat.client.expressionbuilder.modal;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ViewCQLExpressionWidget;
import mat.client.expressionbuilder.constant.ExpressionBuilderUserAssistText;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

public abstract class ExpressionBuilderModal extends Modal {
	private String modalTitle;
	private ModalHeader header;
	private ModalBody body;
	private ModalFooter footer;
	private VerticalPanel contentPanel;
	private ExpressionBuilderModel parentModel;
	private ErrorMessageAlert errorAlert;
	private ExpressionBuilderModel mainModel;
	private HelpBlock helpBlock;
	private ViewCQLExpressionWidget viewCQLExpressionModal;
	private ClickHandler handler;
	private FormLabel formLabel;
	
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
		viewCQLExpressionModal = new ViewCQLExpressionWidget();
		
		this.setId("expressionBuilderModal");
		contentPanel.setWidth("100%");
		
		this.modalTitle = title;
		header.setTitle(title);
		
		body.add(buildHelpBlock(""));
		body.add(buildErrorAlert());
		String exprName = title.substring(title.lastIndexOf('>') + 2);
		 if (!(ExpressionType.QUERY.getDisplayName().equals(exprName) || RelationshipBuilderModal.SOURCE.equals(exprName)) 
					&& ExpressionBuilderUserAssistText.isKeyPresent(exprName)) {
			body.add(buildLabel());
			body.add(new SpacerWidget());
			body.add(new SpacerWidget());
		}
		body.add(contentPanel);
		body.add(this.viewCQLExpressionModal);
		
		this.add(header);
		this.add(body);
		this.add(footer);
		
		if(handler == null) {
			handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
		}
		
		this.addDomHandler(handler, ClickEvent.getType());
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
	
	public String getModalTitle() {
		return this.modalTitle;
	}
	
	public void setModalTitle(String title) {
		this.modalTitle = title;
	}
	
	public ModalHeader getHeader() {
		return header;
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
		viewCQLExpressionModal.setCQLDisplay(mainModel.getCQL(""));
	}
	
	
	
	public MessageAlert getErrorAlert() {
		return errorAlert;
	}
	
	public void setCQLPanelVisible(boolean isVisible) {
		this.viewCQLExpressionModal.setCQLPanelVisible(isVisible);
	}
	
	public void setFooterVisible(boolean isVisible) {
		this.footer.setVisible(isVisible);
	}
	
	private MessageAlert buildErrorAlert() {
		errorAlert = new ErrorMessageAlert();
		return errorAlert;
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
	
	public void showAndDisplayWithoutSuccess() {
		helpBlock.clearError();
		this.hide();
		this.show();
		display();
	}
	
	public void showWithoutSuccess() {
		helpBlock.clearError();
		this.hide();
		this.show();
	}
	
	private Widget buildLabel() {
		formLabel = new FormLabel();
		formLabel.setStyleName("attr-Label");
		return formLabel;
	}

	public void setLabel(String label) {
		formLabel.setText(label);
		formLabel.setTitle(label);
	}
	
}
