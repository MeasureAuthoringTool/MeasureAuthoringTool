package mat.client.shared.ui;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SpacerWidget;

// TODO: Auto-generated Javadoc
/**
 * The Class DeleteConfirmDialogBox.
 */
public class DeleteConfirmDialogBox {

	/** The confirm button. */
	private  Button confirmButton;

	/** The password entered. */
	private  String passwordEntered;
	
	private  Modal panel;
	

	/**
	 * showDeletionConfimationDialog.
	 *
	 * @param message
	 *            the message
	 */
	public void showDeletionConfimationDialog(String message) {

	    panel = new Modal();
	    confirmButton = new Button("Confirm");
	    passwordEntered = "";
		ModalBody modalBody = new ModalBody();
		ErrorMessageAlert messageAlert = new ErrorMessageAlert();

		modalBody.clear();
		messageAlert.clear();
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
		messageAlert.createAlert(message);
		modalBody.add(messageAlert);
		
		VerticalPanel passwordPanel = new VerticalPanel();
		passwordPanel.getElement().setId("passwordPanel_VerticalPanel");
		final HTML passwordText = new HTML(
				"<h4>To confirm deletion enter your password below:<h4>");
		final Input password = new Input(InputType.PASSWORD);
		
		password.setId("password_PasswordTextBox");
		password.setPlaceholder("Enter Password");
		password.setTitle("Enter Password");
		password.setValidateOnBlur(true);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		HTML required = new HTML(RequiredIndicator.get());
		hp.add(password);
		hp.add(required);
		passwordPanel.add(new SpacerWidget());
		passwordPanel.add(passwordText);
		passwordPanel.add(new SpacerWidget());
		passwordPanel.add(hp);
		
		password.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(final KeyUpEvent event) {
				if (password.getText() != null
						&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB
						&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE) {
					confirmButton.setEnabled(true);
					setPasswordEntered(password.getText());
				} else if (password.getText() == null
						|| password.getText().trim().length() == 0) {
					confirmButton.setEnabled(false);
				}
			}
		});

		ModalFooter modalFooter = new ModalFooter();

		confirmButton.setType(ButtonType.PRIMARY);
		confirmButton.setSize(ButtonSize.DEFAULT);
		confirmButton.setDataDismiss(ButtonDismiss.MODAL);
		confirmButton.setEnabled(false);

		modalFooter.add(confirmButton);

		modalBody.add(passwordPanel);
		panel.add(modalBody);

		panel.add(modalFooter);
		panel.getElement().focus();
		panel.show();
	}

	/**
	 * Gets the confirm button.
	 *
	 * @return the confirm button
	 */
	public Button getConfirmButton() {
		return confirmButton;
	}

	/**
	 * Gets the password entered.
	 *
	 * @return the password entered
	 */
	public  String getPasswordEntered() {
		return passwordEntered;
	}

	/**
	 * Sets the password entered.
	 *
	 * @param passwordEntered the new password entered
	 */
	public  void setPasswordEntered(String passwordEntered) {
		this.passwordEntered = passwordEntered;
	}

	/**
	 * Gets the confirmbutton.
	 *
	 * @return the confirmbutton
	 */
	public  Button getConfirmbutton() {
		return confirmButton;
	}

}
