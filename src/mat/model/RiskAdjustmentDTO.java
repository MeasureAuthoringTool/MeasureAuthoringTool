/**
 * 
 */
package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class RiskAdjustmentDTO.
 *
 * @author hyadav
 */
public class RiskAdjustmentDTO implements IsSerializable {

	/** The Name. */
	private  String name;
	
	/** The uuid. */
	private String uuid;
	
	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<RiskAdjustmentDTO>, IsSerializable {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(RiskAdjustmentDTO o1,
				RiskAdjustmentDTO o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	}
	
	
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
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
