package mat.model.cql;

import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ShareLevel;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLLibraryShare.
 */
public class CQLLibraryShare {

	/** The id. */
	private String id;
	
	/** The cql library. */
	private CQLLibrary cqlLibrary;
	
	/** The share level. */
	private ShareLevel shareLevel;
	
	/** The owner. */
	private User owner;
	
	/** The share user. */
	private User shareUser;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the share level.
	 *
	 * @return the share level
	 */
	public ShareLevel getShareLevel() {
		return shareLevel;
	}
	
	/**
	 * Sets the share level.
	 *
	 * @param shareLevel the new share level
	 */
	public void setShareLevel(ShareLevel shareLevel) {
		this.shareLevel = shareLevel;
	}
	
	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}
	
	/**
	 * Sets the owner.
	 *
	 * @param owner the new owner
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	/**
	 * Gets the share user.
	 *
	 * @return the share user
	 */
	public User getShareUser() {
		return shareUser;
	}
	
	/**
	 * Sets the share user.
	 *
	 * @param shareUser the new share user
	 */
	public void setShareUser(User shareUser) {
		this.shareUser = shareUser;
	}

	public CQLLibrary getCqlLibrary() {
		return cqlLibrary;
	}

	public void setCqlLibrary(CQLLibrary cqlLibrary) {
		this.cqlLibrary = cqlLibrary;
	}
}
