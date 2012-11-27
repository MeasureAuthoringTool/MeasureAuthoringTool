package org.ifmc.mat.simplexml.model;


public class Iqdsel extends FunctionHolder implements IPhrase{
	private Iqdsel iqdsel;
	private Id id;
	private To to;
	private String idAttr;
	private String refid;
	private String iname;
	private String name;
	private String datatype;
	private String uuid;
	
	public Iqdsel getQdsel() {
		return iqdsel;
	}
	public void setQdsel(Iqdsel qdsel) {
		this.iqdsel = qdsel;
	}
	public Id getId() {
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public String getRefid() {
		return refid;
	}
	public void setRefid(String refid) {
		this.refid = refid;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public To getTo() {
		return to;
	}
	public String getIdAttr() {
		return idAttr;
	}
	public void setIdAttr(String idAttr) {
		this.idAttr = idAttr;
	}
	
	/* (non-Javadoc)
	 * @see org.ifmc.mat.simplexml.model.IPhrase#setTo(org.ifmc.mat.simplexml.model.To)
	 */
	@Override
	public void setTo(To to) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see org.ifmc.mat.simplexml.model.IPhrase#setToAttr(java.lang.String)
	 */
	@Override
	public void setToAttr(String toAttr) {
		// TODO Auto-generated method stub
		
	}
	
}