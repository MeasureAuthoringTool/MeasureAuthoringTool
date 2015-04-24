package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MeasureType.
 */
public class MeasureType implements IsSerializable{
	
	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<MeasureType>, IsSerializable {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(MeasureType o1, MeasureType o2) {
			return o1.getDescription().compareTo(o2.getDescription());
		}
		
	}
	
	/** The id. */
	private String id;
	
	/** The abbr desc. */
	private String abbrDesc;//Added for XML conversion.
	
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

	/** The description. */
	private String description;

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
	
	/**
	 * Compare.
	 * 
	 * @param o1
	 *            the o1
	 * @param o2
	 *            the o2
	 * @return the int
	 */
	public int compare(MeasureType o1, MeasureType o2) {
		return o1.getDescription().compareTo(o2.getDescription());
	}

	/**
	 * Gets the abbr desc.
	 * 
	 * @return the abbrDesc
	 */
	public String getAbbrDesc() {
		return abbrDesc;
	}

	/**
	 * Sets the abbr desc.
	 * 
	 * @param abbrDesc
	 *            the abbrDesc to set
	 */
	public void setAbbrDesc(String abbrDesc) {
		this.abbrDesc = abbrDesc;
	}

}
