package org.ifmc.mat.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class Clause implements IsSerializable {
	protected String measure;
	protected String name;
	protected String description;
	protected Decision decision;
	public String context;

	public Clause() {
	}
	
	public Clause(String measure, String context) {
		setName(measure + "_" + context);
	}
	
	public Clause(String measure, String context, int index) {
		setName(measure + "_" + context + "_" + index);
	}
	
	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CLAUSE ").append(name).append("\n").append(decision);
		return sb.toString();
	}
}