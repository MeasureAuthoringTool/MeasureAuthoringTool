package mat.client.admin.service;

import mat.client.shared.GenericResult;

/**
 * The Class SaveUpdateUserResult.
 */
public class SaveUpdateUserResult extends GenericResult {

    public static final int USER_EMAIL_NOT_UNIQUE = 1;
    public static final int SERVER_SIDE_VALIDATION = 2;
}
