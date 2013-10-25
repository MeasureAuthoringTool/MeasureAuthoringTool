package mat.model.clause;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class Context.
 */
public class Context implements IsSerializable {
	
	/** The id. */
	private String id;
	
	/** The description. */
	private String description;
	
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
		this.id = id;
	}
	
	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Instantiates a new context.
	 */
	public Context() {}
	
	/**
	 * Instantiates a new context.
	 * 
	 * @param id
	 *            the id
	 * @param desc
	 *            the desc
	 */
	public Context (String id, String desc) {
		this.id = id;
		this.description = desc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object temp) {
		
		if (temp == null || !(temp instanceof Context)) return false;
		Context ctxTemp = (Context)temp;
		if (ctxTemp.getDescription() != null && getDescription() != null && ctxTemp.getDescription().equals(getDescription())) return true;
		if (ctxTemp.getId() != null && getId() != null && ctxTemp.getId().equals(getId())) return true;
		return false;
		
		
	}
}
