package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class ValidateMeasureResult.
 */
public class ValidateMeasureResult implements IsSerializable {
	
	/** The valid. */
	private boolean valid;
	
	/** The validation messages. */
	private List<String> validationMessages;
	
	/**
	 * Checks if is valid.
	 * 
	 * @return true, if is valid
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Sets the valid.
	 * 
	 * @param valid
	 *            the new valid
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * Gets the validation messages.
	 * 
	 * @return the validation messages
	 */
	public List<String> getValidationMessages() {
		return validationMessages;
	}
	
	/**
	 * Sets the validation messages.
	 * 
	 * @param validationMessages
	 *            the new validation messages
	 */
	public void setValidationMessages(List<String> validationMessages) {
		this.validationMessages = validationMessages;
	}
	
	
}
