package mat.server;

import mat.client.bonnie.BonnieService;

@SuppressWarnings("serial")
public class BonnieServiceImpl extends SpringRemoteServiceServlet implements BonnieService{

	private String getResponseType() {
		return System.getProperty("BONNIE_RESPONSE_TYPE");
	}

	private String getClientId() {
		return System.getProperty("BONNIE_CLIENT_ID");
	}

	private String getRedirectURI() {
		return System.getProperty("BONNIE_REDIRECT_URI");
	}

	@Override
	public String getBonnieLink() {
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

}