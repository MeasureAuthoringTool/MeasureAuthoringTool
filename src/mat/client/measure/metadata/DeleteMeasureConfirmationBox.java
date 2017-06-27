package mat.client.measure.metadata;

import mat.client.shared.DialogBoxWithCloseButton;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SpacerWidget;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class DeleteMeasureConfirmationBox.
 */
public class DeleteMeasureConfirmationBox {

	/** The dialog box. */
	private static DialogBoxWithCloseButton dialogBox = new DialogBoxWithCloseButton(
			StringUtils.EMPTY);
	
	/** The handler registration. */
	static HandlerRegistration handlerRegistration;

	/**
	 * Gets the dialog box.
	 * 
	 * @return the dialog box
	 */
	public static DialogBox getDialogBox() {
		return dialogBox;
	}

	/** The confirm. */
	public static Button confirm;
	
	/** The password entered. */
	public static String passwordEntered;

	/**
	 * Gets the password entered.
	 * 
	 * @return the password entered
	 */
	public static String getPasswordEntered() {
		return passwordEntered;
	}

	/**
	 * Sets the password entered.
	 * 
	 * @param passwordEntered
	 *            the new password entered
	 */
	public static void setPasswordEntered(String passwordEntered) {
		DeleteMeasureConfirmationBox.passwordEntered = passwordEntered;
	}

	/**
	 * Gets the confirm.
	 * 
	 * @return the confirm
	 */
	public static Button getConfirm() {
		return confirm;
	}

	/**
	 * Sets the confirm.
	 * 
	 * @param confirm
	 *            the new confirm
	 */
	public static void setConfirm(Button confirm) {
		DeleteMeasureConfirmationBox.confirm = confirm;
	}

	/**
	 * Show deletion confimation dialog.
	 */
	public static void showDeletionConfimationDialog() {

		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Warning");
		confirm = new Button("Confirm");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogContents.setWidth("28em");
		dialogContents.setSpacing(5);
		dialogBox.setWidget(dialogContents);

		ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();
		errorMessageDisplay.setMessage(MatContext.get().getMessageDelegate()
				.getDELETE_MEASURE_WARNING_MESSAGE());
		VerticalPanel passwordPanel = new VerticalPanel();
		passwordPanel.getElement().setId("passwordPanel_VerticalPanel");
		final HTML passwordText = new HTML(
				"To confirm deletion enter your password below:");
		final PasswordTextBox password = new PasswordTextBox();
		password.getElement().setId("password_PasswordTextBox");
		passwordPanel.add(passwordText);
		passwordPanel.add(new SpacerWidget());
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		HTML required = new HTML(RequiredIndicator.get());
		hp.add(password);
		hp.add(required);
		passwordPanel.add(hp);

		dialogContents.add(errorMessageDisplay);
		dialogContents.setCellHorizontalAlignment(errorMessageDisplay,
				HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(passwordPanel);
		dialogContents.setCellHorizontalAlignment(passwordPanel,
				HasHorizontalAlignment.ALIGN_LEFT);
		confirm.setEnabled(false);
		
		password.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(final KeyUpEvent event) {
				if (password.getText() != null
						&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB
						&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE) {
					confirm.setEnabled(true);
					setPasswordEntered(password.getText());
				} else if (password.getText() == null
						|| password.getText().trim().length() == 0) {
					confirm.setEnabled(false);
				}

			}
		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.getElement().setId("buttonPanel_HorizontalPanel");
		buttonPanel.setSpacing(10);
		confirm.getElement().setId("save_Button");
		buttonPanel.add(confirm);
		buttonPanel.setCellHorizontalAlignment(confirm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		dialogContents.add(buttonPanel);
		dialogBox.center();
		dialogBox.show();

		if (handlerRegistration != null) {
			handlerRegistration.removeHandler();
		}
		handlerRegistration = dialogBox
				.addCloseHandler(new CloseHandler<PopupPanel>() {
					@Override
					public void onClose(final CloseEvent<PopupPanel> event) {

					}
				});

	}

}
