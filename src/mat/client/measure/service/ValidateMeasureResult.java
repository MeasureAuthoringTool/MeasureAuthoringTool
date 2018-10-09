package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.client.shared.GenericResult;

/**
 * The Class ValidateMeasureResult.
 */
public class ValidateMeasureResult extends GenericResult implements IsSerializable {
	
	/** The valid. */
	private boolean valid;
	
	/** The validation messages. */
	private List<String> validationMessages;
	
	public static int VALIDATE_GROUP_FAIL = -1; 
	
	public static int VALIDATE_PACKAGE_GROUPING_FAIL = -2; 
		
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
