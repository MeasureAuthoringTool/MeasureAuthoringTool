package mat.client.login.service;

import mat.client.shared.GenericResult;

public class LoginResult extends GenericResult {
	public static final int SERVER_SIDE_VALIDATION_SECURITY_QUESTIONS = 1;
	public static final int SERVER_SIDE_VALIDATION_PASSWORD = 2;
	public static final int DICTIONARY_EXCEPTION=3;

}
