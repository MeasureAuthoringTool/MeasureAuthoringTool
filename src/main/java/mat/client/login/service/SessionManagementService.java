package mat.client.login.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface SessionManagementService.
 */
@RemoteServiceRelativePath("sessionService")
public interface SessionManagementService extends RemoteService {

    CurrentUserInfo getCurrentUser();

    String getCurrentReleaseVersion();
}
