package mat.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import mat.client.bonnie.BonnieService;
import mat.dao.UserDAO;
import mat.dao.UserBonnieAccessInfoDAO;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;

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
	
	@Override
	public String getBonnieTokens(String code) {
		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setClientSecret(getClientSecret());
        resourceDetails.setClientId(getClientId());
        resourceDetails.setAccessTokenUri("https://bonnie-prior.ahrqstg.org/oauth/token");

        
		
        OAuth2RestTemplate oAuthRestTemplate = new OAuth2RestTemplate(resourceDetails);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        
        OAuth2AccessToken token = oAuthRestTemplate.getAccessToken();

        System.out.println(oAuthRestTemplate.getResource());
        System.out.println(oAuthRestTemplate.getOAuth2ClientContext());
        System.out.println(token);
        
        User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
        
        UserBonnieAccessInfo tokens = new UserBonnieAccessInfo();
        tokens.setAccessToken("1234567890");
        tokens.setRefreshToken("0987654321");
        tokens.setUser(user);
        
        userBonnieAccessInfoDAO.saveUserBonnieAccessDetails(tokens);
        //UserBonnieAccessInfo user2 = userBonnieAccessInfoDAO.findByUserId(user.getId());
        //System.out.println(user2.getAccessToken());
		return "";
	}
}
