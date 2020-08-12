package mat.client.cqlworkspace;


import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import mat.client.buttons.NoButton;
import mat.client.buttons.YesButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;



public class DeleteConfirmationDialogBox {
	private  final Button yesButton = new YesButton("DeleteConfirmationBox"); 
	private final Button noButton = new NoButton("DeleteConfirmationBox");
	private ErrorMessageAlert messageAlert = new ErrorMessageAlert();
	private ClickHandler handler;
	
	public ErrorMessageAlert getMessageAlert() {
		return messageAlert;
	}

	public void setMessageAlert(ErrorMessageAlert messageAlert) {
		this.messageAlert = messageAlert;
	}

	public DeleteConfirmationDialogBox() {
	}
	
	public void show() {
		
		Modal panel = new Modal();
		ModalBody modalBody = new ModalBody(); 

		modalBody.clear();
		modalBody.remove(messageAlert);
		panel.remove(modalBody);
		panel.setTitle("Warning");
		panel.setDataKeyboard(true);
		panel.setClosable(true);
		panel.setFade(true);
		panel.setDataBackdrop(ModalBackdrop.STATIC);
		panel.setSize(ModalSize.MEDIUM);
		panel.getElement().getStyle().setZIndex(9999);
		panel.setRemoveOnHide(true);
		
		if(handler == null) {
			handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
		}
		
		panel.addDomHandler(handler, ClickEvent.getType());
		
		messageAlert.getElement().getStyle().setMarginTop(0.0, Style.Unit.PX);
		messageAlert.getElement().getStyle().setMarginBottom(0.0, Style.Unit.PX);
		modalBody.add(messageAlert);
		
		
		ModalFooter modalFooter = new ModalFooter(); 
		ButtonToolBar buttonToolBar = new ButtonToolBar(); 
		yesButton.setDataDismiss(ButtonDismiss.MODAL);
		noButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(yesButton);
		buttonToolBar.add(noButton);
	
		modalFooter.add(buttonToolBar);
		
		panel.add(modalBody);
		
		panel.add(modalFooter);
		panel.getElement().focus();
		panel.show();
		panel.addHiddenHandler(event -> onModalHidden());
	}
	
	
	private void onModalHidden() {
		noButton.click();
	}

	public void hide() {}
	
	
	public Button getYesButton() {
		return yesButton; 
	}
	
	public Button getNoButton() {
		return noButton; 
	}

}
