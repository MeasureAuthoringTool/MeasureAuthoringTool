package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLDataModel implements IsSerializable{
	// Stan Data type to attribute mapping xml will be reterived from this model.
	private String name;
	private String qdmVersion;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the qdmVersion
	 */
	public String getQdmVersion() {
		return qdmVersion;
	}

	/**
	 * @param qdmVersion the qdmVersion to set
	 */
	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}

}
