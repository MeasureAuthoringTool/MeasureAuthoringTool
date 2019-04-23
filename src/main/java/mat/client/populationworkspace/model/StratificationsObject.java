package mat.client.populationworkspace.model;

/**
 * class StratificationsObject.
 *
 */
public class StratificationsObject extends PopulationsObject{
	
	/**
	 * Constructor.
	 */
	public StratificationsObject(String name, String uuid) {
		super(name);
		this.uuid = uuid;
	}
	
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
