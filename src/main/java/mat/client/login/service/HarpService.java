package mat.client.login.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Map;

@RemoteServiceRelativePath("harpService")
public interface HarpService extends RemoteService {

    boolean revoke(String accessToken);

    String getHarpUrl();

    String getHarpBaseUrl();

    String getHarpClientId();

    Map<String, String> getUserInfo(String accessToken);

    boolean validateToken(String token);
}
