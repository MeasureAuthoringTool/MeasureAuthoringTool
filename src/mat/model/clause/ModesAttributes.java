package mat.model.clause;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class ModesAttributes.
 */
public class ModesAttributes implements IsSerializable {
	
	
	
  public static class Comparator implements java.util.Comparator<ModesAttributes>, IsSerializable {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(ModesAttributes o1,
				ModesAttributes o2) {
			return o1.getAttributeId().compareTo(o2.getAttributeId());
		}
		
	}
	
	
	/** The id. */
	private String id;
	
	/** The attribute id. */
	private String attributeId;
	
	/** The data type id. */
	private String modeId;
	
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
	 * @return the attributeId
	 */
	public String getAttributeId() {
		return attributeId;
	}

	/**
	 * @param attributeId the attributeId to set
	 */
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * @return the modeId
	 */
	public String getModeId() {
		return modeId;
	}

	/**
	 * @param modeId the modeId to set
	 */
	public void setModeId(String modeId) {
		this.modeId = modeId;
	}

}
