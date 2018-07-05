package mat.client.bonnie;

import org.springframework.beans.factory.annotation.Autowired;

import mat.dao.impl.BonnieLinkDAO;

public class BonnieLink{
	
	private String responseType;
	private String clientID;
	private String redirectURI;
	
	//@Autowired 
	//private BonnieLinkDAO linkDAO;
	
	public BonnieLink() {
		//responseType = linkDAO.getResponseType();
		//clientID = linkDAO.getClientID();
		//redirectURI = linkDAO.getRedirectURI();
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
