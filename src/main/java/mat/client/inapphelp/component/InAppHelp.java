package mat.client.inapphelp.component;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.buttons.InAppHelpButton;
import mat.client.shared.MatContext;
import mat.client.util.ClientConstants;

public class InAppHelp extends Composite {
	
	private InAppHelpButton inAppHelpButton;
	
	private Modal helpModal = new Modal();
	
	private String message;
	
	private ButtonGroup infoButtonGroup = new ButtonGroup();

	private HTML messageHTML;
	
	private FocusPanel messageFocusPanel = new FocusPanel();
	
	private ClickHandler handler;
	
	private HorizontalPanel footerPanel = new HorizontalPanel();
	
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
		Paragraph linkToUserGuideText = new Paragraph();
        linkToUserGuideText.setText("For additional information, please see the ");
        linkToUserGuideText.setPaddingRight(8);
        linkToUserGuideText.setMarginTop(18);
        
        Button linkToUserGuideButton = new Button();
        linkToUserGuideButton.setType(ButtonType.LINK);
        linkToUserGuideButton.setDataTarget(ClientConstants.USERGUIDE_URL);
        linkToUserGuideButton.getElement().setId("MAT_User_Guide");
        linkToUserGuideButton.setTitle("Click this link to navigate to the MAT User Guide");
        linkToUserGuideButton.setText("MAT User Guide");
        linkToUserGuideButton.setId("MAT_User_Guide");
        linkToUserGuideButton.setSize(ButtonSize.SMALL);
        linkToUserGuideButton.setPaddingRight(150);
        linkToUserGuideButton.setMarginTop(12);
        linkToUserGuideButton.setMarginLeft(-16);
		linkToUserGuideButton.addClickHandler(event -> Window.open(ClientConstants.USERGUIDE_URL,"_blank",""));
		Button closeButton = new Button("Close");
		closeButton.setTitle("Close");
		closeButton.setType(ButtonType.PRIMARY);
		closeButton.addClickHandler(event -> hideHelpModal());
		linkToUserGuideButton.setMarginRight(218);
		
		footerPanel.add(linkToUserGuideText);
		footerPanel.add(linkToUserGuideButton);
		footerPanel.add(closeButton);
		footer.add(footerPanel);
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

	public FocusPanel getMessageFocusPanel() {
		return messageFocusPanel;
	}

	public void setMessageFocusPanel(FocusPanel messageFocusPanel) {
		this.messageFocusPanel = messageFocusPanel;
	}
}
