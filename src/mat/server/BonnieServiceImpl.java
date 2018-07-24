package mat.server;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;

import mat.client.bonnie.BonnieService;
import mat.dao.UserBonnieAccessInfoDAO;
import mat.dao.UserDAO;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;
import mat.shared.BonnieOauthResult;

@SuppressWarnings("serial")
public class BonnieServiceImpl extends SpringRemoteServiceServlet implements BonnieService{

	/** The user DAO. */
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserBonnieAccessInfoDAO userBonnieAccessInfoDAO;
	
	private String getResponseType() {
		return System.getProperty("BONNIE_RESPONSE_TYPE");
	}

	private String getClientId() {
		return System.getProperty("BONNIE_CLIENT_ID");
	}

	private String getRedirectURI() {
		return System.getProperty("BONNIE_REDIRECT_URI");
	}
	private String getClientSecret() {
		return System.getProperty("BONNIE_CLIENT_SECRET");
	}

	@Override
	public String getBonnieAccessLink() {
		String responseType = getResponseType();
		String clientId = getClientId();
		String redirectURI = getRedirectURI();
		
		if(redirectURI == null && clientId == null && responseType == null) {
			return "";
		} else {
			return "https://bonnie-prior.ahrqstg.org/oauth/authorize?response_type="
					+ responseType + "&client_id=" + clientId + "&redirect_uri=" + redirectURI;
		}
	}

	public BonnieOauthResult refreshBonnieTokens() {
		BonnieOauthResult result = null;
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		result = getBonnieRefreshResult(user.getUserBonnieAccessInfo());
		SaveBonnieOauthResult(result);
		
		return result;
	}
	
	private BonnieOauthResult getBonnieRefreshResult(UserBonnieAccessInfo userBonnieAccessInfo) {
		try {
			 OAuthClient client = new OAuthClient(new URLConnectionClient());

	         OAuthClientRequest request =
	                 OAuthClientRequest.tokenLocation("https://bonnie-prior.ahrqstg.org/oauth/token")
	                 .setClientId(getClientId())
	                 .setGrantType(GrantType.AUTHORIZATION_CODE)
	                 .setClientSecret(getClientSecret())
	                 .setRefreshToken(userBonnieAccessInfo.getRefreshToken())
	                 .setRedirectURI(getRedirectURI())
	                 .buildQueryMessage();
	         
	         
	         OAuthJSONAccessTokenResponse token =
	                 client.accessToken(request, OAuthJSONAccessTokenResponse.class);
	         BonnieOauthResult result = new BonnieOauthResult(token.getAccessToken(), token.getRefreshToken(), token.getExpiresIn(), token.getBody());
	         return result;
			}
			catch(Exception exn) {
				exn.printStackTrace();
				return null;
			}
	}

	@Override
	public BonnieOauthResult authenticateBonnieUser(String code) {        
		BonnieOauthResult result = null;
		try {
			result = getBonnieOauthResult(code);
			SaveBonnieOauthResult(result);
            
        } catch (Exception exn) {
            exn.printStackTrace();
            return result;
        }
		
		return result;
	}
	
	private BonnieOauthResult getBonnieOauthResult(String code) {
		try {
		 OAuthClient client = new OAuthClient(new URLConnectionClient());

         OAuthClientRequest request =
                 OAuthClientRequest.tokenLocation("https://bonnie-prior.ahrqstg.org/oauth/token")
                 .setClientId(getClientId())
                 .setGrantType(GrantType.AUTHORIZATION_CODE)
                 .setClientSecret(getClientSecret())
                 .setCode(code)
                 .setRedirectURI(getRedirectURI())
                 .buildQueryMessage();
         
         
         OAuthJSONAccessTokenResponse token =
                 client.accessToken(request, OAuthJSONAccessTokenResponse.class);
         BonnieOauthResult result = new BonnieOauthResult(token.getAccessToken(), token.getRefreshToken(), token.getExpiresIn(), token.getBody());
         return result;
		}
		catch(Exception exn) {
			exn.printStackTrace();
			return null;
		}
	}
	
	private void SaveBonnieOauthResult(BonnieOauthResult result) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
        
        UserBonnieAccessInfo tokens = new UserBonnieAccessInfo();
        tokens.setAccessToken(result.getAccessToken());
        tokens.setRefreshToken(result.getRefreshToken());
        tokens.setUser(user);
        
        userBonnieAccessInfoDAO.saveOrUpdate(tokens);
	}
}
