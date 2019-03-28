package mat.client.inapphelp.component;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

import mat.client.buttons.InAppHelpButton;
import mat.client.shared.MatContext;

public class InAppHelp extends Composite {
	
	private InAppHelpButton inAppHelpButton;
	
	private Modal helpModal = new Modal();
	
	private String message;
	
	private ButtonGroup infoButtonGroup = new ButtonGroup();

	private HTML messageHTML;
	
	private FocusPanel messageFocusPanel = new FocusPanel();
	
	private ClickHandler handler;
	
	public InAppHelp(String message) {
		this.message = message;
		
		initWidget(createInAppHelp());
		
	}
	
	private ButtonGroup createInAppHelp() {
		inAppHelpButton = new InAppHelpButton();
		inAppHelpButton.addClickHandler(event -> showHelpModal());
		createPopUpElement();
		
		infoButtonGroup.add(inAppHelpButton);
		infoButtonGroup.add(helpModal);
		infoButtonGroup.getElement().setAttribute("class", "btn-group");
		return infoButtonGroup;
	}
	
	private void showHelpModal() {
		if(handler == null) {
			handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
		}
		
		helpModal.addDomHandler(handler, ClickEvent.getType());

		helpModal.show();
		messageFocusPanel.getElement().focus();
	}

	private void createPopUpElement() {
		messageHTML = new HTML();
		messageHTML.setHTML(this.message);
		messageHTML.setWidth("760px");
		messageHTML.setStyleName("inAppHelp");
		
		ModalHeader header = new ModalHeader();
		header.setHeight("16px");
		header.getElement().setAttribute("style", "border-color: transparent");
		helpModal.add(header);
		
		ModalBody body = new ModalBody();
		messageFocusPanel = new FocusPanel();
		messageFocusPanel.add(messageHTML);
		body.add(messageFocusPanel);
		helpModal.add(body);
		
		ModalFooter footer = new ModalFooter();
		Button closeButton = new Button("Close");
		closeButton.setTitle("Close");
		closeButton.setType(ButtonType.PRIMARY);
		closeButton.addClickHandler(event -> hideHelpModal());
		closeButton.setPull(Pull.RIGHT);
		footer.add(closeButton);
		footer.getElement().setAttribute("style", "border-color: transparent");
		helpModal.add(footer);
		
		
		helpModal.setDataKeyboard(true);
		
		helpModal.setWidth("800px");
	}
	
	private void hideHelpModal() {
		 helpModal.hide();
	}

	public void setMessage(String message) {
		this.messageHTML.setHTML(message);
	}

	public void show(Boolean visible) {
		inAppHelpButton.setVisible(visible);
	}

	public Modal getHelpModal() {
		return helpModal;
	}

	public void setHelpModal(Modal helpModal) {
		this.helpModal = helpModal;
	}

	public InAppHelpButton getInAppHelpButton() {
		return inAppHelpButton;
	}

	public void setInAppHelpButton(InAppHelpButton inAppHelpButton) {
		this.inAppHelpButton = inAppHelpButton;
	}
}
