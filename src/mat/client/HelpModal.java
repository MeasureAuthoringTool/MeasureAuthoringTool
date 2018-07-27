package mat.client;

import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import com.google.gwt.user.client.ui.Label;

import mat.client.buttons.YesButton;


public class HelpModal {
	private YesButton closeButton = new YesButton("helpblock");
	private Modal panel = new Modal();
	private ModalBody modalBody = new ModalBody(); 
	private Label messageLabel = new Label();

	public HelpModal(){
		closeButton.setText("Close");
		closeButton.setTitle("Close");
		panel.setClosable(false);
		panel.setTitle("Help");
		panel.setFade(true);
		panel.getElement().setTabIndex(0);
		panel.setDataBackdrop(ModalBackdrop.STATIC);
		panel.setSize(ModalSize.MEDIUM);
		panel.getElement().getStyle().setZIndex(1000);
		panel.setRemoveOnHide(true);
		modalBody.getElement().setTabIndex(0);
		
		modalBody.add(messageLabel);
		messageLabel.getElement().setTabIndex(0);
		
		ModalFooter modalFooter = new ModalFooter();
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		closeButton.setTabIndex(0);
		modalFooter.add(closeButton);
		
		panel.add(modalBody);
		panel.add(modalFooter);
		panel.getElement().setAttribute("role", "dialog");
	}

	public void show(){
		panel.show();	
		messageLabel.getElement().focus();
	}
	
	public void setMessage(String message) {
		messageLabel.setText(message);
	}
}
