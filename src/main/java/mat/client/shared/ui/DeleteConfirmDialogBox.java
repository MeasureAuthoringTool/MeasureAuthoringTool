package mat.client.shared.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

/**
 * The Class DeleteConfirmDialogBox.
 */
public class DeleteConfirmDialogBox {
	private Button confirmButton;
	private Modal panel;
	private FocusPanel focusPanel = new FocusPanel();
	private ChangePasswordWidget changePasswordWidget = new ChangePasswordWidget();
	private ErrorMessageAlert messageAlert;
	private ClickHandler handler;
	
	public void showDeletionConfimationDialog(String message, String deleteButtonText) {
		focusPanel.clear();
	    panel = new Modal();
		Button cancelButton = new Button("Do Not Delete");
	    confirmButton = new Button(deleteButtonText);
		ModalBody modalBody = new ModalBody();
		messageAlert = new ErrorMessageAlert();

		modalBody.clear();
		messageAlert.clear();
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
		messageAlert.createAlert(message);
		
		VerticalPanel passwordPanel = new VerticalPanel();
		passwordPanel.getElement().setId("passwordPanel_VerticalPanel");
		HTML passwordText = new HTML(
				"<h4>To confirm deletion, enter DELETE:<h4>");
		
		
		changePasswordWidget.getPassword().setId("password_PasswordTextBox");
		changePasswordWidget.getPassword().setPlaceholder("Enter DELETE");
		changePasswordWidget.getPassword().setTitle( message + " To confirm deletion, enter your DELETE. Required");
		changePasswordWidget.getPassword().setFocus(true);
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		HTML required = new HTML(RequiredIndicator.get());
		hp.add(changePasswordWidget.getPassword());
		hp.add(required);
		passwordPanel.add(new SpacerWidget());
		passwordPanel.add(passwordText);
		passwordPanel.add(new SpacerWidget());
		passwordPanel.add(hp);
		
		getPassword().addKeyUpHandler(c ->
				confirmButton.setEnabled(getPassword().getText() != null &&
						"DELETE".equals(getPassword().getText()) ? true : false));

		ModalFooter modalFooter = new ModalFooter();

		confirmButton.setType(ButtonType.PRIMARY);
		confirmButton.setSize(ButtonSize.DEFAULT);
		confirmButton.setEnabled(false);

		cancelButton.setType(ButtonType.PRIMARY);
		cancelButton.setSize(ButtonSize.DEFAULT);
		cancelButton.setEnabled(true);
		cancelButton.addClickHandler(e -> closeDialogBox());

		modalFooter.add(cancelButton);
		modalFooter.add(confirmButton);
		VerticalPanel vp = new VerticalPanel();
		vp.add(messageAlert);
		vp.add(passwordPanel);
		focusPanel.add(vp);
		modalBody.add(focusPanel);
		panel.add(modalBody);

		panel.add(modalFooter);
		panel.getElement().focus();
		panel.show();
	}
	
	public void closeDialogBox() {
		panel.hide();
	}

	public Button getConfirmButton() {
		return confirmButton;
	}


	public Input getPassword() {
		return changePasswordWidget.getPassword();
	}

	public void setMessage(String message) {
		messageAlert.clear();
		messageAlert.createAlert(message);
	}
}
