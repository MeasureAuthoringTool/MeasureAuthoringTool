package mat.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;

/**
 * The Class PasswordEditInfoWidget.
 */
public class PasswordEditInfoWidget extends Composite {
	
	/** The password. */
	private Input password = new Input(InputType.TEXT);
	FormGroup passwordExistingGroup = new FormGroup();
	private boolean isCtrlKey = false;
	private Map<String, String> passwords = new HashMap<String, String>();
	private List<Integer> validKeysList = new ArrayList<Integer>();
	
	/**
	 * Instantiates a new password edit info widget.
	 */
	public PasswordEditInfoWidget() {
		passwords.put("exisitingPassword", "");
		buildValidKeysList();
		
		FormLabel label = new FormLabel();
		label.setText("Enter existing password to confirm changes");
		label.setTitle("Enter existing password to confirm changes");
		label.setId("existingPasswordLabel");
		label.setFor("exisitingPasswordInput");
		label.setShowRequiredIndicator(true);
		
		password.setWidth("200px");
		password.setTitle("Enter existing password to confirm changes");
		password.setId("exisitingPasswordInput");
		passwordExistingGroup.add(label);
		passwordExistingGroup.add(password);
		
		getPassword().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				getPassword().setText(MessageDelegate.EMPTY_VALUE);
				passwords.put("exisitingPassword", MessageDelegate.EMPTY_VALUE);
			}
		});
		
		getPassword().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//Replacing the last character of the string with '*'
				String passwordText = getPassword().getText().replaceAll(".", "*");
				getPassword().setText(passwordText);
			}
		});
		
		getPassword().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				handleBackSpace("exisitingPassword", event.getNativeKeyCode());
				getPassword().setText(displayValueAsAsterix(getPassword().getText(), "exisitingPassword", event.getNativeKeyCode()));
			}
		});
		
		getPassword().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_CTRL == event.getNativeKeyCode()) {
					isCtrlKey = true;
				}
			}
		});
	}
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public Input getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(Input password) {
		this.password = password ;
	}
	
	public FormGroup getPasswordExistingGroup() {
		return passwordExistingGroup;
	}
	
	public Map<String, String> getPasswords() {
		return passwords;
	}

	private List<Integer> buildValidKeysList() {
		Integer[] keys = {KeyCodes.KEY_A, KeyCodes.KEY_B, KeyCodes.KEY_C, KeyCodes.KEY_D, KeyCodes.KEY_E, KeyCodes.KEY_F, KeyCodes.KEY_G, 
				KeyCodes.KEY_H, KeyCodes.KEY_I, KeyCodes.KEY_J, KeyCodes.KEY_K, KeyCodes.KEY_L, KeyCodes.KEY_M, KeyCodes.KEY_N, KeyCodes.KEY_O, 
				KeyCodes.KEY_P, KeyCodes.KEY_Q, KeyCodes.KEY_R, KeyCodes.KEY_S, KeyCodes.KEY_T, KeyCodes.KEY_U, KeyCodes.KEY_V, KeyCodes.KEY_W, 
				KeyCodes.KEY_X, KeyCodes.KEY_Y, KeyCodes.KEY_Z, KeyCodes.KEY_ONE, KeyCodes.KEY_TWO, KeyCodes.KEY_THREE, KeyCodes.KEY_FOUR, KeyCodes.KEY_FIVE, 
				KeyCodes.KEY_SIX, KeyCodes.KEY_SEVEN, KeyCodes.KEY_EIGHT, KeyCodes.KEY_NINE, KeyCodes.KEY_ZERO};
		
		for (int index=0; index< keys.length; index++) {
			validKeysList.add(keys[index]);
		}
		return validKeysList;
	}
	
	private void handleBackSpace(String currentField, int charCode) {
		if (KeyCodes.KEY_BACKSPACE == charCode) {
			passwords.put(currentField, MessageDelegate.EMPTY_VALUE);
		}
	}
	
	private String displayValueAsAsterix(String temporaryValue, String currentField, int charCode) {
		String formattedValue = "";
		if (validKeysList.contains(charCode)) {
			if (isCtrlKey) {
				passwords.put(currentField, temporaryValue);
				formattedValue = temporaryValue;
				isCtrlKey = false;
			}else {
				char newChar = temporaryValue.charAt(temporaryValue.length() - 1);
				String securityAnswer = passwords.get(currentField); 
				securityAnswer += newChar;
				passwords.put(currentField, securityAnswer);
				formattedValue = temporaryValue.length() == 2 ? "*" : "";
				if(temporaryValue.length() > 2) {
					formattedValue = temporaryValue.substring(0, securityAnswer.length()-2);
					formattedValue += "*";
				}
				formattedValue += newChar;
			}
		}else if (KeyCodes.KEY_RIGHT == charCode || KeyCodes.KEY_LEFT == charCode) {
			formattedValue = temporaryValue.substring(0, temporaryValue.length()-1).concat("*");
		}else if (KeyCodes.KEY_SHIFT == charCode) {
			formattedValue = temporaryValue;
		}else if (KeyCodes.KEY_CTRL == charCode) {
			formattedValue = temporaryValue.replaceAll(".", "*");
		}

		return formattedValue;
	}
}
