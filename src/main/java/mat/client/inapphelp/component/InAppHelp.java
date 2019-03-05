package mat.client.inapphelp.component;

import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

import mat.client.buttons.InAppHelpButton;

public class InAppHelp extends Composite {
	
	private InAppHelpButton inAppHelpButton;
	
	private Modal helpModal = new Modal();
	
	private String message;
	
	private ButtonGroup infoButtonGroup = new ButtonGroup();

	private HTML messageHTML;
	
	private FocusPanel messageFocusPanel = new FocusPanel();
	
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
		helpModal.show();
		messageFocusPanel.getElement().focus();
	}

	private void createPopUpElement() {
		messageHTML = new HTML();
		messageHTML.setHTML(this.message);
		messageHTML.setWidth("500px");
		messageHTML.setStyleName("inAppHelp");
		
		ModalHeader header = new ModalHeader();
		header.setHeight("0px");
		header.setStyleName("inAppHelpHeader");
		helpModal.add(header);
		
		ModalBody body = new ModalBody();
		messageFocusPanel = new FocusPanel();
		messageFocusPanel.add(messageHTML);
		body.add(messageFocusPanel);
		helpModal.add(body);
		
		helpModal.setWidth("550px");
	}
	
	public void setMessage(String message) {
		this.messageHTML.setHTML(message);
	}

	public void show(Boolean visible) {
		inAppHelpButton.setVisible(visible);
	}
}
