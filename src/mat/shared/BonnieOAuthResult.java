package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BonnieOAuthResult implements IsSerializable{
	private String accessToken;
	private String refreshToken;
	private Long expiresIn;
	private String body;
	
	public BonnieOAuthResult() {
		
	}
	
	public BonnieOAuthResult(String accessToken, String refreshToken, long expiresIn, String body){
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
		this.setBody(body);
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public Long getExpiresIn() {
		return expiresIn;
	}
	
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}	
}
