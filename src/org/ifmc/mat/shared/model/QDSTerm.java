package org.ifmc.mat.shared.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class QDSTerm extends IQDSTerm {
	private String qDSRef;
	private List<Property>properties;
	private String id;
	private String decisionId;
	public List<Decision> attributes = null;
	
	public List<Decision> getAttributes() {
		if(attributes==null) attributes = new ArrayList<Decision>();
		return attributes;
	}

	public void setAttributes(List<Decision> attributes) {
		this.attributes = attributes;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public String getqDSRef() {
		return qDSRef;
	}

	public void setqDSRef(String qDSRef) {
		this.qDSRef = qDSRef;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDecisionId() {
		return decisionId;
	}

	public void setDecisionId(String decisionId) {
		this.decisionId = decisionId;
	}

	public QDSTerm() {
	}
	
	public QDSTerm(String qDSRef) {
		this.qDSRef = qDSRef;
	}
	
//	public String getQDSRef() {
//		return qDSRef;
//	}
//
//	public void setQDSRef(String ref) {
//		qDSRef = ref;
//	}
	
	public String getProperty() {
		if (properties == null)
			return null;
		return properties.get(0).getName();
	}
	
	public List<Property> getProperties() {
		if (properties == null)
			properties = new ArrayList<Property>();
		return properties;
	}
	
	public void addProperty(String p) {
		getProperties().add(new Property(p));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QDSTerm ").append(qDSRef);
		if (properties != null)
			sb.append(".").append(properties);
		return sb.toString();
	}
}
