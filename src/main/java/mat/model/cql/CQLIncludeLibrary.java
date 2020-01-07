package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class CQLIncludeLibrary.
 */
public class CQLIncludeLibrary implements IsSerializable {

	/** The id. */
	private String id;

	/** The Alias name. */
	private String aliasName;

	/** The cql library id. */
	private String cqlLibraryId;

	/** The version. */
	private String version;

	private String cqlLibraryName;

	private String qdmVersion;

	private String setId;

	private String isComponent;

	private String measureId;

	private String libraryModelType;

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public CQLIncludeLibrary(CQLLibraryDataSetObject dto){
		this.cqlLibraryId = dto.getId();
		this.version =dto.getVersion().replace("v", "") + "."+ dto.getRevisionNumber();
		this.cqlLibraryName = dto.getCqlName();
		this.qdmVersion = dto.getQdmVersion();
		this.setId = dto.getCqlSetId();
		this.libraryModelType = dto.getLibraryModelType();
	}

	public CQLIncludeLibrary(CQLIncludeLibrary includeLibrary){
		this.aliasName = includeLibrary.getAliasName();
		this.id = includeLibrary.getId();
		this.cqlLibraryId = includeLibrary.getCqlLibraryId();
		this.version = includeLibrary.getVersion();
		this.cqlLibraryName = includeLibrary.getCqlLibraryName();
		this.qdmVersion = includeLibrary.getQdmVersion();
		this.setId = includeLibrary.getSetId();
		this.libraryModelType = includeLibrary.getLibraryModelType();
	}

	public CQLIncludeLibrary(){

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
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the alias name.
	 *
	 * @return the alias name
	 */
	public String getAliasName() {
		return aliasName;
	}

	/**
	 * Sets the alias name.
	 *
	 * @param aliasName
	 *            the new alias name
	 */
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	/**
	 * Gets the cql library id.
	 *
	 * @return the cql library id
	 */
	public String getCqlLibraryId() {
		return cqlLibraryId;
	}

	/**
	 * Sets the cql library id.
	 *
	 * @param cqlLibraryId
	 *            the new cql library id
	 */
	public void setCqlLibraryId(String cqlLibraryId) {
		this.cqlLibraryId = cqlLibraryId;
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
	 * Sets the version.
	 *
	 * @param version
	 *            the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public String getCqlLibraryName() {
		return cqlLibraryName;
	}

	public void setCqlLibraryName(String cqlLibraryName) {
		this.cqlLibraryName = cqlLibraryName;
	}

	public String getQdmVersion() {
		return qdmVersion;
	}

	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}

	public String getLibraryModelType() {
		return libraryModelType;
	}

	public void setLibraryModelType(String libraryModelType) {
		this.libraryModelType = libraryModelType;
	}

	@Override
	public boolean equals(Object arg0) {
		CQLIncludeLibrary cqlIncludeLibrary = (CQLIncludeLibrary)arg0;

		if(cqlIncludeLibrary == null){
			return false;
		}

		if(cqlIncludeLibrary.cqlLibraryId.equals(cqlLibraryId) &&
				cqlIncludeLibrary.aliasName.equals(aliasName) &&
				cqlIncludeLibrary.cqlLibraryName.equals(cqlLibraryName) &&
				cqlIncludeLibrary.version.equals(version) &&
				cqlIncludeLibrary.libraryModelType.equals(libraryModelType)){
			return true;
		}
		return false;
	}


	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<CQLIncludeLibrary>, IsSerializable {

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(CQLIncludeLibrary o1, CQLIncludeLibrary o2) {
			return o1.getAliasName().compareTo(o2.getAliasName());
		}

	}

	public String toString(){
		return this.id + "|" + this.cqlLibraryId + "|" + this.cqlLibraryName + "|" + this.aliasName + "|"
				+ this.version + "|" + this.libraryModelType;
	}

	public String getIsComponent() {
		return isComponent;
	}

	public void setIsComponent(String isComponent) {
		this.isComponent = isComponent;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

}
