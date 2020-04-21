package mat.client.login.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.shared.MatException;

@RemoteServiceRelativePath("harpService")
public interface HarpService extends RemoteService {

    boolean revoke(String accessToken);

    String getHarpUrl();

    String getHarpBaseUrl();

    String getHarpClientId();
}
