package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class Author.
 */
public class Author implements IsSerializable{
	
	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<Author>, IsSerializable {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Author o1, Author o2) {
			return o1.getAuthorName().compareTo(o2.getAuthorName());
		}
		
	}
	
	/** The id. */
	private String id;
	
	/** The author name. */
	private String authorName;
	
	/** The org id. */
	private String orgId;
	
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
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id.trim();
	}
	
	/**
	 * Gets the author name.
	 * 
	 * @return the author name
	 */
	public String getAuthorName() {
		return authorName;
	}
	
	/**
	 * Sets the author name.
	 * 
	 * @param authorName
	 *            the new author name
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName.trim();
	}
	
	/**
	 * Compare.
	 * 
	 * @param o1
	 *            the o1
	 * @param o2
	 *            the o2
	 * @return the int
	 */
	public int compare(Author o1, Author o2) {
		return o1.getAuthorName().compareTo(o2.getAuthorName());
	}
	
	/**
	 * Gets the org id.
	 * 
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	
	/**
	 * Sets the org id.
	 * 
	 * @param orgId
	 *            the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
