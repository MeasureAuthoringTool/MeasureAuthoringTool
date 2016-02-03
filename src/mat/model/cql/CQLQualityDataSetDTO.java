package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class QualityDataSetDTO.
 */
public class CQLQualityDataSetDTO implements IsSerializable {
	
	
	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<CQLQualityDataSetDTO>, IsSerializable {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(CQLQualityDataSetDTO o1,
				CQLQualityDataSetDTO o2) {
			return o1.getQDMElement().compareTo(o2.getQDMElement());
		}
		
	}
	
	/** The code list name. */
	private String codeListName;
	
	/** The code system name. */
	private String codeSystemName;
	
	/** The data type. */
	private String dataType;
	
	
	/** The id. */
	private String id;
	
	
	/** The oid. */
	private String oid;
	
	/** The supp data element. */
	private boolean suppDataElement;
	
	/** The taxonomy. */
	private String taxonomy;
	
	/** The type. */
	private String type;
	
	/** The uuid. */
	private String uuid;
	
	/** The version. */
	private String version;
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals (Object o) {
		CQLQualityDataSetDTO temp = (CQLQualityDataSetDTO) o;
		if (temp.getId().equals(getId())) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * Gets the code list name.
	 * 
	 * @return the code list name
	 */
	public String getCodeListName() {
		return codeListName;
	}
	
	/**
	 * Gets the code system name.
	 * 
	 * @return the code system name
	 */
	public String getCodeSystemName() {
		return codeSystemName;
	}
	
	/**
	 * Gets the data type.
	 * 
	 * @return the data type
	 */
	public String getDataType() {
		return dataType;
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
	 * Gets the oid.
	 * 
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}
	
	/**
	 * Gets the qDM element.
	 * 
	 * @return the qDM element
	 */
	public String getQDMElement() {
		return codeListName + ": " + dataType;
	}
	
	/**
	 * Gets the taxonomy.
	 * 
	 * @return the taxonomy
	 */
	public String getTaxonomy() {
		return taxonomy;
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
	 * Gets the uuid.
	 * 
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	
	
	/**
	 * Checks if is supp data element.
	 * 
	 * @return true, if is supp data element
	 */
	public boolean isSuppDataElement() {
		return suppDataElement;
	}
	
	
	
	/**
	 * Sets the code list name.
	 * 
	 * @param codeListName
	 *            the new code list name
	 */
	public void setCodeListName(String codeListName) {
		this.codeListName = codeListName;
	}
	
	/**
	 * Sets the code system name.
	 * 
	 * @param codeSystemName
	 *            the new code system name
	 */
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}
	
	/**
	 * Sets the data type.
	 * 
	 * @param dataType
	 *            the new data type
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
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
	 * Sets the oid.
	 * 
	 * @param oid
	 *            the new oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	/**
	 * Sets the supp data element.
	 * 
	 * @param suppDataElement
	 *            the new supp data element
	 */
	public void setSuppDataElement(boolean suppDataElement) {
		this.suppDataElement = suppDataElement;
	}
	
	/**
	 * Sets the taxonomy.
	 * 
	 * @param taxonomy
	 *            the new taxonomy
	 */
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}
	
	
	
	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	/**
	 * Sets the uuid.
	 * 
	 * @param uuid
	 *            the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	
	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return codeListName + ": " + dataType + "-" + getOid();
		
	}
	
	/**
	 * Compare.
	 *
	 * @param o1 the o1
	 * @param o2 the o2
	 * @return the int
	 */
	public int compare(CQLQualityDataSetDTO o1, CQLQualityDataSetDTO o2) {
		int num = o1.getUuid().compareTo(o2.getUuid());
		return num;
	}
	
	
	
	
	
	
}
