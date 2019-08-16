package mat.client.measurepackage;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MeasurePackageClauseDetail.
 */
public class MeasurePackageClauseDetail implements IsSerializable, Comparable<MeasurePackageClauseDetail> {
	
	/** The id. */
	private String id;
	
	/** The name. */
	private String name;
	
	/** The type. */
	private String type;
	
	/** The associated population. */
	private String  associatedPopulationUUID;
	
	/** The is associated population. */
	private boolean isAssociatedPopulation;
	
	/** The db associated population uuid. */
	private String dbAssociatedPopulationUUID;

	/** The is associated to grouping. */
	private boolean isInGrouping;
	
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
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	/**
	 * Gets the associated population uuid.
	 *
	 * @return the associatedPopulationUUID
	 */
	public String getAssociatedPopulationUUID() {
		return associatedPopulationUUID;
	}
	
	/**
	 * Sets the associated population uuid.
	 *
	 * @param associatedPopulationUUID the associatedPopulationUUID to set
	 */
	public void setAssociatedPopulationUUID(String associatedPopulationUUID) {
		this.associatedPopulationUUID = associatedPopulationUUID;
	}
	
	/**
	 * Checks if is associated population.
	 *
	 * @return the isAssociatedPopulation
	 */
	public boolean isAssociatedPopulation() {
		return isAssociatedPopulation;
	}
	
	/**
	 * Sets the associated population.
	 *
	 * @param isAssociatedPopulation the isAssociatedPopulation to set
	 */
	public void setAssociatedPopulation(boolean isAssociatedPopulation) {
		this.isAssociatedPopulation = isAssociatedPopulation;
	}
	
	
	/**
	 * Gets the db associated population uuid.
	 *
	 * @return the db associated population uuid
	 */
	public String getDbAssociatedPopulationUUID() {
		return dbAssociatedPopulationUUID;
	}

	/**
	 * Sets the db associated population uuid.
	 *
	 * @param dbAssociatedPopulationUUID the new db associated population uuid
	 */
	public void setDbAssociatedPopulationUUID(String dbAssociatedPopulationUUID) {
		this.dbAssociatedPopulationUUID = dbAssociatedPopulationUUID;
	}
	
	/**
	 * @return the isInGrouping
	 */
	public boolean isIsInGrouping() {
		return isInGrouping;
	}

	/**
	 * @param isInGrouping the isInGrouping to set
	 */
	public void setInGrouping(boolean isInGrouping) {
		this.isInGrouping = isInGrouping;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MeasurePackageClauseDetail oth) {
		Integer groupingOrder = Integer.parseInt(MeasureGroupingOrder.valueOf(type).getStatusCode());
		Integer otherGroupingOrder = Integer.parseInt(MeasureGroupingOrder.valueOf(oth.type).getStatusCode());
		
		// if they are the same population type, sort by the name
		if(groupingOrder.equals(otherGroupingOrder)) {
			return name.compareTo(oth.name);
		} else { // otherwise sort by the population type
			return groupingOrder.compareTo(otherGroupingOrder);
		}
		
	}
	
	
	
}
