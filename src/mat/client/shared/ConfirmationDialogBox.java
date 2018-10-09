package mat.client.shared;


import org.gwtbootstrap3.client.shared.event.ModalHideEvent;
import org.gwtbootstrap3.client.shared.event.ModalHideHandler;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import mat.client.buttons.NoButton;
import mat.client.buttons.YesButton;



public class ConfirmationDialogBox {
	protected  final Button yesButton = new YesButton("ConfirmDialogBox"); 
	protected final Button noButton = new NoButton("ConfirmDialogBox");
	protected ConfirmationObserver observer; 
	protected ErrorMessageAlert messageAlert = new ErrorMessageAlert();
	protected Modal panel = new Modal();
	protected boolean isClosingOnContinue = false; 
	
	public ErrorMessageAlert getMessageAlert() {
		return messageAlert;
	}

	public void setMessageAlert(ErrorMessageAlert messageAlert) {
		this.messageAlert = messageAlert;
	}
	
	public ConfirmationDialogBox() {
		
	}
	
	public ConfirmationDialogBox(String messageText, String yesButtonText, String noButtonText, ConfirmationObserver observer) {
		this.observer = observer; 
		getMessageAlert().createAlert(messageText);
		getNoButton().setText(noButtonText);
		getYesButton().setText(yesButtonText);
		getYesButton().setFocus(true);
	}

	public void show() {
		ModalBody modalBody = new ModalBody(); 

		modalBody.clear();
		modalBody.remove(messageAlert);
		panel.remove(modalBody);
		panel.setTitle("Warning");
		
		panel.setClosable(true);
		panel.setFade(true);
		panel.setDataBackdrop(ModalBackdrop.STATIC);
		panel.setSize(ModalSize.MEDIUM);
		panel.getElement().getStyle().setZIndex(1000);
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
		
		messageAlert.getElement().getStyle().setMarginTop(0.0, Style.Unit.PX);
		messageAlert.getElement().getStyle().setMarginBottom(0.0, Style.Unit.PX);
		messageAlert.getElement().setAttribute("role", "alert");
		modalBody.add(messageAlert);
		
		
		ModalFooter modalFooter = new ModalFooter(); 
		ButtonToolBar buttonToolBar = new ButtonToolBar(); 
		yesButton.setSize(ButtonSize.SMALL);
		noButton.setSize(ButtonSize.SMALL);
		yesButton.setDataDismiss(ButtonDismiss.MODAL);
		noButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(yesButton);
		buttonToolBar.add(noButton);
	
		modalFooter.add(buttonToolBar);
		
		panel.add(modalBody);
		
		panel.add(modalFooter);
		panel.getElement().focus();
		panel.show();
	}
	
	
	public void hide() {
		panel.hide();
	}
	
	
	public Button getYesButton() {
		return yesButton; 
	}
	
	public Button getNoButton() {
		return noButton; 
	}
	
	public void setObserver(ConfirmationObserver observer) {
		this.observer = observer; 
	}

}
