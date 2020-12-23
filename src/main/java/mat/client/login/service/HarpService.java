package mat.client.login.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.shared.MatException;

import java.util.Map;

@RemoteServiceRelativePath("harpService")
public interface HarpService extends RemoteService {

    boolean revoke(String accessToken);

    String getHarpUrl();

    String getHarpBaseUrl();

    String getHarpClientId();

    boolean validateToken(String token);

    void validateUserAndInitSession(String accessToken) throws MatException;

    Map<String, String> generateUserInfoFromAccessToken(String accessToken) throws MatException;
}
