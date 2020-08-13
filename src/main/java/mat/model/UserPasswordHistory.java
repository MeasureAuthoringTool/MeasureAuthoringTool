package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USER_PASSWORD_HISTORY")
public class UserPasswordHistory implements IsSerializable, Serializable{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2972696565028280765L;

	private String id;
	
	private User user;
	
	private String password;
	
	private String salt;
	
	private Date createdDate;
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "USER_PASSWORD_HISTORY_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PASSWORD", nullable = false, length = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "SALT", nullable = false, length = 100)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", nullable = false, length = 10)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
