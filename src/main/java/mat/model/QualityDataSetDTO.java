package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class QualityDataSetDTO.
 */
public class QualityDataSetDTO implements IsSerializable {
	
	
	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<QualityDataSetDTO>, IsSerializable {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(QualityDataSetDTO o1,
				QualityDataSetDTO o2) {
			return o1.getQDMElement().compareTo(o2.getQDMElement());
		}
		
	}
	
	/** The code list name. */
	private String codeListName;
	
	/** The code system name. */
	private String codeSystemName;
	
	/** The data type. */
	private String dataType;
	
	/** The effective date. */
	private String effectiveDate;
	
	/** QDM Modified At VSAC. */
	private boolean hasModifiedAtVSAC;
	/** The id. */
	private String id;
	
	/** The is used. */
	private boolean isUsed;
	
	/** QDM is not available in VSAC. */
	private boolean notFoundInVSAC;
	
	/** The occurrence text. */
	private String occurrenceText;
	
	/**
	 * The Specific Occurrence Boolean.
	 */
	private boolean specificOccurrence;
	
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
	
	/** The data type has removed. */
	private boolean dataTypeHasRemoved;
	
	/** The expansion profile. */
	private String expansionIdentifier;
	
	private String vsacExpIdentifier;
	
	private String valueSetType;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals (Object o) {
		final QualityDataSetDTO temp = (QualityDataSetDTO) o;
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
	
	/** Gets the effective date.
	 * 
	 * @return the effectiveDate */
	public String getEffectiveDate() {
		return effectiveDate;
	}
	
	/**
	 * Gets the checks for modified at mat.vsac.
	 *
	 * @return the hasModifiedAtVSAC
	 */
	public boolean getHasModifiedAtVSAC() {
		return hasModifiedAtVSAC;
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
	 * Gets the occurrence text.
	 * 
	 * @return the occurrence text
	 */
	public String getOccurrenceText() {
		return occurrenceText;
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
	 * Checks if is not found in mat.vsac.
	 *
	 * @return the notFoundInVSAC
	 */
	public boolean isNotFoundInVSAC() {
		return notFoundInVSAC;
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
	 * Checks if is used.
	 * 
	 * @return true, if is used
	 */
	public boolean isUsed() {
		return isUsed;
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
	
	/** Sets the effective date.
	 * 
	 * @param effectiveDate the effectiveDate to set */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	/**
	 * Sets the checks for modified at mat.vsac.
	 *
	 * @param hasModifiedAtVSAC the hasModifiedAtVSAC to set
	 */
	public void setHasModifiedAtVSAC(boolean hasModifiedAtVSAC) {
		this.hasModifiedAtVSAC = hasModifiedAtVSAC;
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
	 * Sets the not found in mat.vsac.
	 *
	 * @param notFoundInVSAC the notFoundInVSAC to set
	 */
	public void setNotFoundInVSAC(boolean notFoundInVSAC) {
		this.notFoundInVSAC = notFoundInVSAC;
	}
	
	/**
	 * Sets the occurrence text.
	 * 
	 * @param occurrenceText
	 *            the new occurrence text
	 */
	public void setOccurrenceText(String occurrenceText) {
		this.occurrenceText = occurrenceText;
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
	 * Sets the used.
	 * 
	 * @param isUsed
	 *            the new used
	 */
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
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
	
	/**
	 * Checks if is data type has removed.
	 *
	 * @return true, if is data type has removed
	 */
	public boolean isDataTypeHasRemoved() {
		return dataTypeHasRemoved;
	}
	
	/**
	 * Sets the data type has removed.
	 *
	 * @param dataTypeHasRemoved the new data type has removed
	 */
	public void setDataTypeHasRemoved(boolean dataTypeHasRemoved) {
		this.dataTypeHasRemoved = dataTypeHasRemoved;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if ((occurrenceText!= null) && !occurrenceText.equals("")) {
			return occurrenceText + " of " + codeListName + ": " + dataType + "-" + getOid();
		} else {
			return codeListName + ": " + dataType + "-" + getOid();
		}
	}
	
	/**
	 * Compare.
	 *
	 * @param o1 the o1
	 * @param o2 the o2
	 * @return the int
	 */
	public int compare(QualityDataSetDTO o1, QualityDataSetDTO o2) {
		final int num = o1.getUuid().compareTo(o2.getUuid());
		return num;
	}
	
	
	
	/**
	 * Checks if is specific occurrence.
	 *
	 * @return the specificOccurrence
	 */
	public boolean isSpecificOccurrence() {
		return specificOccurrence;
	}
	
	
	
	/**
	 * Sets the specific occurrence.
	 *
	 * @param specificOccurrence the specificOccurrence to set
	 */
	public void setSpecificOccurrence(boolean specificOccurrence) {
		this.specificOccurrence = specificOccurrence;
	}
	
	
	
	/**
	 * Gets the expansion Identifier.
	 *
	 * @return the expansion Identifier
	 */
	public String getExpansionIdentifier() {
		return expansionIdentifier;
	}
	
	
	
	/**
	 * Sets the expansion Identifier.
	 *
	 * @param expansionProfile the new expansion Identifier
	 */
	public void setExpansionIdentifier(String expansionIdentifier) {
		this.expansionIdentifier = expansionIdentifier;
	}
	
	
	
	public String getVsacExpIdentifier() {
		return vsacExpIdentifier;
	}
	
	
	
	public void setVsacExpIdentifier(String vsacExpIdentifier) {
		this.vsacExpIdentifier = vsacExpIdentifier;
	}



	public String getValueSetType() {
		return valueSetType;
	}



	public void setValueSetType(String valueSetType) {
		this.valueSetType = valueSetType;
	}

	public QualityDataSetDTO() {
		super();
	}

	public QualityDataSetDTO(String id, String dataType, String codeListName, String occurrenceText) {
		super();
		this.id = id;
		this.dataType = dataType;
		this.codeListName = codeListName;
		this.occurrenceText = occurrenceText;
	}

	public QualityDataSetDTO(String id, String dataType, String codeListName, String occurrenceText, String oid,
			boolean suppDataElement) {
		super();
		this.id = id;
		this.dataType = dataType;
		this.codeListName = codeListName;
		this.occurrenceText = occurrenceText;
		this.oid = oid;
		this.suppDataElement = suppDataElement;
	}
	
}
