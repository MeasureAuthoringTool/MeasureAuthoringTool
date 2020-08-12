package mat.model.cql;

import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ShareLevel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CQL_LIBRARY_SHARE")
public class CQLLibraryShare {

	private String id;
	
	private CQLLibrary cqlLibrary;
	
	private ShareLevel shareLevel;
	
	private User owner;
	
	private User shareUser;
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "CQL_LIBRARY_SHARE_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHARE_LEVEL_ID", nullable = false)
	public ShareLevel getShareLevel() {
		return shareLevel;
	}
	
	public void setShareLevel(ShareLevel shareLevel) {
		this.shareLevel = shareLevel;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CQL_LIBRARY_OWNER_USER_ID")
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHARE_USER_ID")
	public User getShareUser() {
		return shareUser;
	}
	
	public void setShareUser(User shareUser) {
		this.shareUser = shareUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CQL_LIBRARY_ID", nullable = false)
	public CQLLibrary getCqlLibrary() {
		return cqlLibrary;
	}

	public void setCqlLibrary(CQLLibrary cqlLibrary) {
		this.cqlLibrary = cqlLibrary;
	}
}
