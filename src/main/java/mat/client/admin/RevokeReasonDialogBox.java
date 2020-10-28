package mat.client.admin;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.shared.event.ModalHideEvent;
import org.gwtbootstrap3.client.shared.event.ModalHideHandler;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

public class RevokeReasonDialogBox extends ConfirmationDialogBox {
	TextArea reasonTextArea = new TextArea();
	public RevokeReasonDialogBox(String messageText, String yesButtonText, String noButtonText, ConfirmationObserver confirmationObserver) {
		super(messageText, yesButtonText, noButtonText, confirmationObserver);
		getMessageAlert().getErrorMessageAlert().clearAlert();;
	}
	
	public void show() {
		ModalBody modalBody = new ModalBody(); 

		modalBody.clear();
		panel.remove(modalBody);
		panel.setTitle("Revoke All");
		
		panel.setClosable(true);
		panel.setFade(true);
		panel.setDataBackdrop(ModalBackdrop.STATIC);
		panel.setSize(ModalSize.MEDIUM);
		panel.getElement().getStyle().setZIndex(9999);
		panel.setRemoveOnHide(true);
		
		panel.addHideHandler(new ModalHideHandler() {
			@Override
			public void onHide(ModalHideEvent evt) {
				if(!isClosingOnContinue) {
					observer.onClose();
				}
			}
		});
		
		getYesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isClosingOnContinue = true; 
				observer.onYesButtonClicked();
			}
		});
		getNoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isClosingOnContinue = false; 
				observer.onNoButtonClicked();
			}
		});
		
		getMessageAlert().getErrorMessageAlert().getElement().getStyle().setMarginTop(0.0, Style.Unit.PX);
		getMessageAlert().getErrorMessageAlert().getElement().getStyle().setMarginBottom(0.0, Style.Unit.PX);
		getMessageAlert().getErrorMessageAlert().getElement().setAttribute("role", "alert");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().getStyle().setProperty("width", "100%");
		verticalPanel.add(getMessageAlert().getErrorMessageAlert());
		verticalPanel.add(new SpacerWidget());
		Label reasonLabel = new Label("Reason");
		reasonLabel.addStyleName("bold");
		verticalPanel.add(reasonLabel);
		verticalPanel.add(new SpacerWidget());

		reasonTextArea.setTitle("reason required");
		reasonTextArea.getElement().getStyle().setProperty("width", "95%");
		verticalPanel.add(reasonTextArea);
		modalBody.add(verticalPanel);
		
		ModalFooter modalFooter = new ModalFooter(); 
		ButtonToolBar buttonToolBar = new ButtonToolBar(); 
		yesButton.setSize(ButtonSize.SMALL);
		noButton.setSize(ButtonSize.SMALL);
		noButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(yesButton);
		buttonToolBar.add(noButton);
	
		modalFooter.add(buttonToolBar);
		
		panel.add(modalBody);
		
		panel.add(modalFooter);
		panel.getElement().focus();
		panel.show();
	}
	
	public String getReasonText() {
		return reasonTextArea.getText();
	}

	public void setReasonText(String reasonText) {
		reasonTextArea.setText(reasonText);
	}
}
