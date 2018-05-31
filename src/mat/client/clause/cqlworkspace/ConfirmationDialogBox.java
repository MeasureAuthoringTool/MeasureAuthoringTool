package mat.client.clause.cqlworkspace;


import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import mat.client.cql.ConfirmationObserver;
import mat.client.shared.ErrorMessageAlert;



public class ConfirmationDialogBox {
	private  final Button yesButton = new Button("Yes"); 
	private final Button noButton = new Button("No");
	private ErrorMessageAlert messageAlert = new ErrorMessageAlert();

	
	public ErrorMessageAlert getMessageAlert() {
		return messageAlert;
	}

	public void setMessageAlert(ErrorMessageAlert messageAlert) {
		this.messageAlert = messageAlert;
	}

	public ConfirmationDialogBox() {
		yesButton.getElement().setId("yes_Button");
		noButton.getElement().setId("no_Button");
	}
	
	public ConfirmationDialogBox(String messageText, String yesButtonText, String noButtonText, ConfirmationObserver observer) {
		getMessageAlert().createAlert(messageText);
		getYesButton().setText(yesButtonText);
		getYesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				observer.onYesButtonClicked();
			}
		});
		getNoButton().setText(noButtonText);
		getNoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				observer.onNoButtonClicked();
			}
		});
		getYesButton().setFocus(true);
	}

	public void show() {
		Modal panel = new Modal();
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
		
		messageAlert.getElement().getStyle().setMarginTop(0.0, Style.Unit.PX);
		messageAlert.getElement().getStyle().setMarginBottom(0.0, Style.Unit.PX);
		messageAlert.getElement().setAttribute("role", "alert");
		modalBody.add(messageAlert);
		
		
		ModalFooter modalFooter = new ModalFooter(); 
		ButtonToolBar buttonToolBar = new ButtonToolBar(); 
		yesButton.setType(ButtonType.PRIMARY);
		yesButton.setSize(ButtonSize.SMALL);
		noButton.setType(ButtonType.DANGER);
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

	}
	
	
	public Button getYesButton() {
		return yesButton; 
	}
	
	public Button getNoButton() {
		return noButton; 
	}

}
