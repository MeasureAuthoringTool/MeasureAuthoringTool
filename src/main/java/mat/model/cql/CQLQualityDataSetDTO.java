package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class QualityDataSetDTO.
 */
public class CQLQualityDataSetDTO implements CQLExpression, IsSerializable {
	
	
	/** QDM Modified At VSAC. */
	private boolean hasModifiedAtVSAC;
	private boolean isUsed;
	/** QDM is not available in VSAC. */
	private boolean notFoundInVSAC;
	private String codeListName;
	private String suffix;
	private String originalCodeListName;
	private String codeSystemName;
	private String dataType;
	private String id;
	private String displayName;
	private String oid;
	private String codeSystemOID;
	private String codeIdentifier; 
	private boolean isReadOnly; 
	private boolean suppDataElement;
	private String taxonomy;
	private String type;
	private String uuid;
	private String version;
	private String release;
	private String program; 
	private boolean dataTypeHasRemoved;
	private String valueSetType;
	private String isValidatedWithVsac = VsacStatus.VALID.toString();

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
	
	public String getCodeListName() {
		return codeListName;
	}
	
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getOriginalCodeListName() {
		return originalCodeListName;
	}

	public void setOriginalCodeListName(String originalCodeListName) {
		this.originalCodeListName = originalCodeListName;
	}

	public String getCodeSystemName() {
		return codeSystemName;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public String getOid() {
		return oid;
	}
	
	public String getQDMElement() {
		return codeListName + ": " + dataType;
	}
	
	public String getTaxonomy() {
		return taxonomy;
	}
	
	public String getType() {
		return type;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getRelease() {
		return release;
	}
	
	public String getProgram() {
		return program; 
	}

	public boolean isSuppDataElement() {
		return suppDataElement;
	}
	
	public void setCodeListName(String codeListName) {
		this.codeListName = codeListName;
	}
	
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public void setSuppDataElement(boolean suppDataElement) {
		this.suppDataElement = suppDataElement;
	}
	
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setRelease(String release) {
		this.release = release;
	}
	
	public void setProgram(String program) {
		this.program = program;
	}
	
	public boolean isDataTypeHasRemoved() {
		return dataTypeHasRemoved;
	}

	public void setDataTypeHasRemoved(boolean dataTypeHasRemoved) {
		this.dataTypeHasRemoved = dataTypeHasRemoved;
	}

	public int compare(CQLQualityDataSetDTO o1, CQLQualityDataSetDTO o2) {
		return o1.getUuid().compareTo(o2.getUuid());
	}


	public boolean isHasModifiedAtVSAC() {
		return hasModifiedAtVSAC;
	}

	public void setHasModifiedAtVSAC(boolean hasModifiedAtVSAC) {
		this.hasModifiedAtVSAC = hasModifiedAtVSAC;
	}

	public boolean isNotFoundInVSAC() {
		return notFoundInVSAC;
	}


	public void setNotFoundInVSAC(boolean notFoundInVSAC) {
		this.notFoundInVSAC = notFoundInVSAC;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCodeSystemOID() {
		return codeSystemOID;
	}

	public void setCodeSystemOID(String codeSystemOID) {
		this.codeSystemOID = codeSystemOID;
	}

	public String getCodeIdentifier() {
		return codeIdentifier;
	}

	public void setCodeIdentifier(String codeIdentifier) {
		this.codeIdentifier = codeIdentifier;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return getCodeListName();
	}

	@Override
	public void setName(String name) {
		setCodeListName(name);
	}

	@Override
	public String getLogic() {
		return null;
	}

	@Override
	public void setLogic(String logic) {
	}

	public String getValueSetType() {
		return valueSetType;
	}

	public void setValueSetType(String valueSetType) {
		this.valueSetType = valueSetType;
	}


	public VsacStatus obtainValidatedWithVsac() {
		if (isValidatedWithVsac == null) {
			return null;
		}
		return VsacStatus.valueOf(isValidatedWithVsac);
	}

	public String getValidatedWithVsac() {
		return isValidatedWithVsac;
	}

	public void setValidatedWithVsac(String validatedWithVsac) {
		isValidatedWithVsac = validatedWithVsac;
	}

	public void addValidatedWithVsac(VsacStatus validatedWithVsac) {
		isValidatedWithVsac = validatedWithVsac.toString();
	}

	public String isValidatedWithVsac() {
		return isValidatedWithVsac;
	}

	@Override
	public String toString() {
		return "CQLQualityDataSetDTO{" +
				"isValidatedWithVsac=" + isValidatedWithVsac +
				", hasModifiedAtVSAC=" + hasModifiedAtVSAC +
				", isUsed=" + isUsed +
				", notFoundInVSAC=" + notFoundInVSAC +
				", codeListName='" + codeListName + '\'' +
				", suffix='" + suffix + '\'' +
				", originalCodeListName='" + originalCodeListName + '\'' +
				", codeSystemName='" + codeSystemName + '\'' +
				", dataType='" + dataType + '\'' +
				", id='" + id + '\'' +
				", displayName='" + displayName + '\'' +
				", oid='" + oid + '\'' +
				", codeSystemOID='" + codeSystemOID + '\'' +
				", codeIdentifier='" + codeIdentifier + '\'' +
				", isReadOnly=" + isReadOnly +
				", suppDataElement=" + suppDataElement +
				", taxonomy='" + taxonomy + '\'' +
				", type='" + type + '\'' +
				", uuid='" + uuid + '\'' +
				", version='" + version + '\'' +
				", release='" + release + '\'' +
				", program='" + program + '\'' +
				", dataTypeHasRemoved=" + dataTypeHasRemoved +
				", valueSetType='" + valueSetType + '\'' +
				'}';
	}

	public static class Comparator implements java.util.Comparator<CQLQualityDataSetDTO>, IsSerializable {

		@Override
		public int compare(CQLQualityDataSetDTO o1, CQLQualityDataSetDTO o2) {
			return o1.getQDMElement().compareTo(o2.getQDMElement());
		}

	}

}
