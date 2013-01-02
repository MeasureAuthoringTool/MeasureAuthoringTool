package mat.client.admin.service;

import mat.client.shared.SuccessFailureHolder;

public class SaveUpdateUserResult extends SuccessFailureHolder {
	public static final int ID_NOT_UNIQUE = 1;
	public static final int SERVER_SIDE_VALIDATION = 2;
}
