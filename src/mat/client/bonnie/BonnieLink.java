package mat.client.bonnie;

public class BonnieLink{
	
	private String responseType;
	private String clientID;
	private String redirectURI;

	public BonnieLink() {
		/*
		BonnieUtility.setLink();
		responseType = System.getProperty("BonnieResponseType");
		clientID = System.getProperty("BonnieClientID");
		redirectURI = System.getProperty("BonnieRedirectURI");
		*/
		responseType = "code";
		clientID = "e8f926693556615ea25a3bc558f06dce95ab87a4af9d59aefbcdd2c1de027fa2";
		redirectURI = "https%3A%2F%2Flocalhost%3A8443%2FMat.html";
		
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
