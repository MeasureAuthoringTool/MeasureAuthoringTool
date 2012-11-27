package org.ifmc.mat.simplexml.model;

import java.util.UUID;

public class Criterion {
	private And and;
	private Or or;
	private String ttext;
	private String uuid;

	public Criterion() {
		uuid = UUID.randomUUID().toString().toUpperCase();
	}
	
	public void setLogicOp(LogicOp op) {
		if (op instanceof And)
			setAnd((And)op);
		else if (op instanceof Or)
			setOr((Or)op);
	}	
	
	public And getAnd() {
		return and;
	}

	public void setAnd (And and ) {
		this.and = and;
	}

	public Or getOr() {
		return or;
	}

	public void setOr(Or or) {
		this.or = or;
	}
	
	public String getTtext() {
		return ttext;
	}

	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid (String uuid ) {
		this.uuid = uuid;
	}
}
