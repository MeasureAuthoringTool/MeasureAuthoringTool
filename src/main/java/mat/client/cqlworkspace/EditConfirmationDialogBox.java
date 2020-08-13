package mat.client.cqlworkspace;


import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageAlert;
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



public class EditConfirmationDialogBox {

	private  final Button yesButton = new Button("Yes"); 
	private ClickHandler handler;
	
	public EditConfirmationDialogBox() {
		yesButton.getElement().setId("yes_Button");
	}
	
	public void show(String message) {
		
		Modal panel = new Modal();
		ModalBody modalBody = new ModalBody(); 
		SuccessMessageAlert messageAlert = new SuccessMessageAlert();

		modalBody.clear();
		messageAlert.clear();
		modalBody.remove(messageAlert);
		panel.remove(modalBody);
		panel.setTitle("Confirmation");
		panel.setDataKeyboard(true);
		panel.setClosable(false);
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
		messageAlert.createAlert(message);
		modalBody.add(messageAlert);
		
		
		ModalFooter modalFooter = new ModalFooter(); 
		ButtonToolBar buttonToolBar = new ButtonToolBar(); 
		yesButton.setType(ButtonType.PRIMARY);
		yesButton.setSize(ButtonSize.SMALL);
		yesButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(yesButton);
	
		modalFooter.add(buttonToolBar);
		
		panel.add(modalBody);
		
		panel.add(modalFooter);
		panel.getElement().setAttribute("role", "dialog");
		panel.getElement().focus();
		panel.show();
	}
	
	public void hide() {

	}
	
	
	public Button getYesButton() {
		return yesButton; 
	}
	
}
