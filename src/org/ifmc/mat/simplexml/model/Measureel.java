package org.ifmc.mat.simplexml.model;


public class Measureel {
	private String id;
	private String oid;
	private String name;
	private String uuid;
	private String datatype;
	private String taxonomy;

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