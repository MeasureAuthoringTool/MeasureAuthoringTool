package mat.shared.error;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AuthenticationException extends Exception implements IsSerializable {
	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
		
	}
	
	public AuthenticationException(String message) {
		super(message);
	}
}
