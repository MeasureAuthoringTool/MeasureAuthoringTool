package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class GroupedCodeListDTO.
 */
public class GroupedCodeListDTO implements IsSerializable {

	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<GroupedCodeListDTO> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(GroupedCodeListDTO o1, GroupedCodeListDTO o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	}
	
	/** The name. */
	private String name;
	
	/** The id. */
	private String id;
	
	/** The description. */
	private String description;
	
	/** The code system. */
	public  String codeSystem;
	
	/** The oid. */
	private String oid;
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
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
	 * Gets the code system.
	 * 
	 * @return the code system
	 */
	public String getCodeSystem() {
		return codeSystem;
	}
	
	/**
	 * Sets the code system.
	 * 
	 * @param codeSystem
	 *            the new code system
	 */
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	
	/**
	 * Gets the oid.
	 * 
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}
	
	/**
	 * Sets the oid.
	 * 
	 * @param oid
	 *            the new oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}
	
}
