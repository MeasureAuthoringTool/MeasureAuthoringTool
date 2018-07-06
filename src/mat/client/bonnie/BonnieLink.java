package mat.client.bonnie;

import mat.server.service.impl.BonnieUtility;

public class BonnieLink{
	
	private String responseType;
	private String clientID;
	private String redirectURI;

	public BonnieLink() {
		BonnieUtility.setLink();
		responseType = System.getProperty("BonnieResponseType");
		clientID = System.getProperty("BonnieClientID");
		redirectURI = System.getProperty("BonnieRedirectURI");
	}

	public String getResponseType() {
		return responseType;
	}

	public String getClientId() {
		return clientID;
	}

	public String getRedirectURI() {
		return redirectURI;
	}
}
