package mat.client.login.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("harpService")
public interface HarpService extends RemoteService {
    boolean logout(String idToken);

    boolean revoke(String accessToken);
}