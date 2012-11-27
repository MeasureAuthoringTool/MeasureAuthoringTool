package mat.client.login.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sessionService")
public interface SessionManagementServiceAsync {

	void getCurrentUserRole(AsyncCallback<SessionManagementService.Result> callback);
	void renewSession(AsyncCallback<Void> callback);
}
