package mat.client.expressionbuilder.modal;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.inapphelp.message.InAppHelpMessages;
import mat.client.shared.MatContext;

public class InAppHelpModal extends SubExpressionBuilderModal {

	private FocusPanel messageFocusPanel = new FocusPanel();
	
	public InAppHelpModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("InAppHelp", parent, parentModel, mainModel);
		this.setHideOtherModals(false);
		this.setClosable(true);
		this.setRemoveOnHide(true);
		this.setWidth("60%");
		this.setCQLPanelVisible(false);
		this.getElement().getStyle().setZIndex(9999);
		this.getElement().setTabIndex(-1);
		display();
	}

	@Override
	public void display() {
		
		this.getContentPanel().add(buildContentPanel());
		
		ModalFooter footer = new ModalFooter();
		Button closeButton = new Button("Close");
		closeButton.setTitle("Close");
		closeButton.setType(ButtonType.PRIMARY);
		closeButton.addClickHandler(event -> hideModal());
		closeButton.setPull(Pull.RIGHT);
		footer.add(closeButton);
		footer.getElement().setAttribute("style", "border-color: transparent");
		this.add(footer);
		
		this.getFooter().setVisible(false);
		this.getHeader().setVisible(false);
	}
	
	private void hideModal() {
		MatContext.get().restartTimeoutWarning();
		this.hide();
		this.getExpressionBuilderParent().showWithoutSuccess();
	}

	public void showModal() {
		this.show();
		messageFocusPanel.getElement().focus();
	}

	private Widget buildContentPanel() {
		HTML messageHTML  = new HTML();
		messageHTML.setWidth("95%");
		messageHTML.setStyleName("inAppHelp");
		
		messageHTML.setHTML(InAppHelpMessages.EXPRESSION_BUILDER);
		messageFocusPanel = new FocusPanel();
		messageFocusPanel.add(messageHTML);

		return messageFocusPanel;
	}
}
