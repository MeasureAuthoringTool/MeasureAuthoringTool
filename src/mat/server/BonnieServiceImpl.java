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
		return "https://bonnie-prior.ahrqstg.org/oauth/authorize?response_type="
			+ getResponseType() + "&client_id=" + getClientId() + "&redirect_uri=" + getRedirectURI();
	}

}
