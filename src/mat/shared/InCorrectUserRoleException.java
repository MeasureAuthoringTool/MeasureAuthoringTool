package mat.shared;

import java.io.Serializable;

/**
 * The Class InCorrectUserRoleException.
 */
public class InCorrectUserRoleException extends Exception implements Serializable{
	private static final long serialVersionUID = 3700593282032578549L;

	/**
	 * Instantiates a new in correct user role exception.
	 */
	public InCorrectUserRoleException(){
		super();
	}
	
	/**
	 * Instantiates a new in correct user role exception.
	 * 
	 * @param message
	 *            the message
	 */
	public InCorrectUserRoleException(String message){
		super(message);
	}
}
