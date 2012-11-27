package org.ifmc.mat.simplexml.model;

import java.util.UUID;


public class Propel {
	private String id;
	private String oid;
	private String name;
	private String uuid = UUID.randomUUID().toString();
	private String datatype;
	private String taxonomy;
	private String codeSystem;
	private String codeSystemName;
	private String mode;
	private String typeCode;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	private String code;
	
	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getCodeSystemName() {
		return codeSystemName;
	}

	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public void diagram(PrettyPrinter pp) {
		pp.concat("ID", id);
		pp.concat("OID", oid);
		pp.concat("NAME", name);
		pp.concat("UUID", uuid);
		pp.concat("DATATYPE", datatype);
	}
	
	public String getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}
	
	public String getId() {
		return id;
	}
	public void setId (String id ) {
		this.id = id;
	}
	public String getOid() {
		return oid;
	}
	public void setOid (String oid ) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName (String name ) {
		this.name = name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid (String uuid ) {
		this.uuid = uuid;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype (String datatype ) {
		this.datatype = datatype;
	}
}