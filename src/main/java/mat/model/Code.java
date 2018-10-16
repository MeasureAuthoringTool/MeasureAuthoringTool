package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class Code.
 */
public class Code implements IsSerializable , Cloneable{
	
	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<Code>, IsSerializable {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Code o1, Code o2) {
			return o1.getCode().compareTo(o2.getCode());
		}
	}
	
	/** The id. */
	private String id;
	
	/** The code. */
	private String code;
	
	/** The description. */
	private String description;
	
	/** The code list id. */
	private String codeListID;
	
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
		if(id!=null)
			id=id.trim();
		this.id = id;
	}
	
	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code.trim();
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
		this.description = description.trim();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object aThat) {
	   
	    Code that = (Code)aThat;
	    return
	    	this.code.equalsIgnoreCase(that.code);
		}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int code = getCode().hashCode(); 
		return code;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Code clone(){
		Code clone = new Code();
		clone.setCode(this.getCode());
		clone.setDescription(this.getDescription());
		return clone;
	}
	
	/**
	 * Gets the code list id.
	 * 
	 * @return the code list id
	 */
	public String getCodeListID() {
		return codeListID;
	}
	
	/**
	 * Sets the code list id.
	 * 
	 * @param codeListID
	 *            the new code list id
	 */
	public void setCodeListID(String codeListID) {
		this.codeListID = codeListID;
	}
	
	
}
