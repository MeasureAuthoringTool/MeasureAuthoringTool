package mat.client.myAccount.service;

import mat.client.shared.GenericResult;

/**
 * The Class SaveMyAccountResult.
 */
public class SaveMyAccountResult extends GenericResult {
	public static final int SERVER_SIDE_VALIDATION = 1;
	public static final int DICTIONARY_EXCEPTION=2;
	public static final int ID_NOT_UNIQUE = 3;

}
