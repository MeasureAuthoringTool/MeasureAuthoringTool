package mat.model;

public class UserBonnieConnection {
<<<<<<< HEAD
	private int id;
=======
	private String id;
>>>>>>> 7e1ad7903c31cf81221d971c56aa43c6dbd126a6
	
	private User user;
	
	private String refreshToken;
	
	private String accessToken;
	
<<<<<<< HEAD
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
=======
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
>>>>>>> 7e1ad7903c31cf81221d971c56aa43c6dbd126a6
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
