package mat.client.login.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface SessionManagementServiceAsync.
 */
@RemoteServiceRelativePath("sessionService")
public interface SessionManagementServiceAsync {

	/**
	 * Gets the current user role.
	 * 
	 * @param callback
	 *            the callback
	 * @return the current user role
	 */
	void getCurrentUserRole(AsyncCallback<SessionManagementService.Result> callback);
	
	/**
	 * Renew session.
	 * 
	 * @param callback
	 *            the callback
	 */
	void renewSession(AsyncCallback<Void> callback);

	/**
	 * Get current release version.
	 * 
	 * @param callback
	 *            the callback
	 */
	void getCurrentReleaseVersion(AsyncCallback<String> Callback);
}
