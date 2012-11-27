package org.ifmc.mat.shared.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Conditional extends IQDSTerm {
	public List<Decision> decisions;
	public enum Operator {AND, OR};
	private List<Property>properties;
	private Operator operator;

	public Conditional() {		
	}
	
	public Conditional(Operator operator) {
		this.operator = operator;
	}

	public Conditional(Operator operator, Decision decision) {
		this.operator = operator;
		addDecision(decision);
	}
	
	public Conditional(Operator operator, List<Decision>decisions) {
		this.operator = operator;
		addDecision(decisions);
	}
	
	public Conditional(Operator operator, String p, Decision decision) {
		this.operator = operator;
		this.properties = new ArrayList<Property>();
		this.properties.add(new Property(p));
		addDecision(decision);
	}
	
	public Conditional(Operator operator,String p, List<Decision>decisions) {
		this.operator = operator;
		this.properties = new ArrayList<Property>();
		this.properties.add(new Property(p));
		addDecision(decisions);
	}
		
	public Operator getOperator() {
		return operator;
	}
	
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public List<Decision> getDecisions() {
		if (decisions == null)
			decisions = new ArrayList<Decision>();
		return decisions;
	}
	
	public void setDecisions(List<Decision> decisions) {
		this.decisions = decisions;
	}
	
	public void addDecision(Decision decision) {
		if (decisions == null)
			decisions = new ArrayList<Decision>();
		decisions.add(decision);
	}
	
	private void addDecision(List<Decision> decisions) {
		for (Decision decision : decisions)
			addDecision(decision);
	}
	
	public String getProperty() {
		if (properties == null)
			return null;
		return properties.get(0).getName();
	}
	
	public String getProperty(int i) {
		if (properties == null)
			return null;
		return (i >= 0 && i < properties.size()) 
		? properties.get(i).getName()
		: null;
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
		sb.append("Conditional ").append(operator);
		if (properties != null)
			sb.append(".").append(properties);
		sb.append("\n");
		if (decisions != null)
			for (Decision decision : decisions)
				sb.append(decision.toString()).append("\n");
		return sb.toString();
	}
}
