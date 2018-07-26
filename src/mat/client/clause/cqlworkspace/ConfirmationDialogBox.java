package mat.client.clause.cqlworkspace;


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

import mat.client.buttons.BlueButtonSmall;
import mat.client.buttons.RedButtonSmall;
import mat.client.cql.ConfirmationObserver;
import mat.client.shared.ErrorMessageAlert;



public class ConfirmationDialogBox {
	private  final Button yesButton = new BlueButtonSmall("ConfirmDialogBox", "Yes"); 
	private final Button noButton = new RedButtonSmall("ConfirmDialogBox", "No");
	private ConfirmationObserver observer; 
	private ErrorMessageAlert messageAlert = new ErrorMessageAlert();
	Modal panel = new Modal();
	private boolean isContinueClose = false; 
	
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
				// if the dialog box is closing because of clicking the continue button, 
				// then don't execute the close handler. Just close the thing. 
				if(!isContinueClose) {
					observer.onClose();
				}
			}
		});
		
		getYesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isContinueClose = true; 
				observer.onYesButtonClicked();
			}
		});
		getNoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isContinueClose = false; 
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
