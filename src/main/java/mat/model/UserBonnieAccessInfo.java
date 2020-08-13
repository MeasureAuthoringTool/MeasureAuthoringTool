package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USER_BONNIE_ACCESS_INFO")
public class UserBonnieAccessInfo {
	private int id;
	
	private User user;
	
	private String refreshToken;
	
	private String accessToken;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "REFRESH_TOKEN", nullable = false, length = 250)
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	@Column(name = "ACCESS_TOKEN", nullable = false, length = 250)
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
