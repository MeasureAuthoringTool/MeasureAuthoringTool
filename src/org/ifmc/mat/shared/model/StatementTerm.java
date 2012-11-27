package org.ifmc.mat.shared.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.ifmc.mat.client.diagramObject.SimpleStatement;;

@SuppressWarnings("serial")
public class StatementTerm extends IQDSTerm {
	protected Decision lfTerm;
	private String QDSOper;
	protected Decision rtTerm;
	private List<Property>properties;
	
	/*
	 * Operator.minus is equivalent to subtracted from and Operator.plus is 
	 * equivalent to Added To.
	 */
	public enum Operator {
		LESS_THAN,
		GREATER_THAN,
		LESS_THAN_OR_EQUAL_TO,
		GREATER_THAN_OR_EQUAL_TO, 
		EQUAL_TO,
		NOT_EQUAL_TO, 
		PLUS,
		MINUS,
		TIMES,
		DIVIDE_BY,
		IS_NOT_NULL,
		IS_NULL,
		LIKE
};
	public static Operator[] statementTermOperators = {
			Operator.LESS_THAN, 
			Operator.GREATER_THAN, 
			Operator.LESS_THAN_OR_EQUAL_TO, 
			Operator.GREATER_THAN_OR_EQUAL_TO,
			Operator.EQUAL_TO, 
			Operator.NOT_EQUAL_TO, 
			Operator.PLUS, 
			Operator.MINUS, 
			Operator.TIMES, 
			Operator.DIVIDE_BY,
			Operator.IS_NOT_NULL,
			Operator.IS_NULL,
			Operator.LIKE
	};
	
	Operator operator;

	public StatementTerm() {
	}
	
	public StatementTerm(Decision lfTerm, Decision rtTerm) {
		this.lfTerm = lfTerm;
		this.rtTerm = rtTerm;
	}
	
	public StatementTerm(Decision lfTerm, String QDSOper, Decision rtTerm) {
		this.lfTerm = lfTerm;
		this.QDSOper = QDSOper;
		this.rtTerm = rtTerm;
	}
	
	public StatementTerm(String p, Decision lfTerm, String QDSOper, Decision rtTerm) {
		this.properties = new ArrayList<Property>();
		this.properties.add(new Property(p));
		this.lfTerm = lfTerm;
		this.QDSOper = QDSOper;
		this.rtTerm = rtTerm;
	}
	
	public StatementTerm(Decision lfTerm, Operator operator, Decision rtTerm) {
		this.lfTerm = lfTerm;
		this.operator = operator;
		this.rtTerm = rtTerm;
	}

	public StatementTerm(String p,Decision lfTerm, Operator operator, Decision rtTerm) {
		this.properties = new ArrayList<Property>();
		this.properties.add(new Property(p));
		this.lfTerm = lfTerm;
		this.operator = operator;
		this.rtTerm = rtTerm;
	}
	
	public Decision getLfTerm() {
		return lfTerm;
	}
	
	public void setLfTerm(Decision lfTerm) {
		this.lfTerm = lfTerm;
	}
	
	public String getQDSOper() {
		return QDSOper;
	}
	
	public Decision getRtTerm() {
		return rtTerm;
	}
	
	public void setRtTerm(Decision rtTerm) {
		this.rtTerm = rtTerm;
	}
	
	public void setQDSOper(String oper) {
		QDSOper = oper;
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
	
	public Operator getOperator() {
		return operator;
	}
	
	public void setOperator(Operator operator) {
		this.operator = operator;
	}	
	
	public void setOperator(String operator) {
		this.operator = SimpleStatement.getComparisonAsStatementTermOperator(operator);
	}
	
	public String getOperatorAsString() {
		if(operator.equals(Operator.MINUS)){
		   return "Subtracted From";
		}else if(operator.equals(Operator.PLUS)){
			return "Added To";
		}
		if(Arrays.asList(statementTermOperators).indexOf(operator)>= SimpleStatement.comparisonOperators.length)
			//US182 Means looking for something which is not in the comparisonOperator Array, so default to first one.
			return SimpleStatement.comparisonOperators[0];
		else
			return SimpleStatement.comparisonOperators[Arrays.asList(statementTermOperators).indexOf(operator)];
	}
	
	public static String getOperatorAsString(Operator operator) {
		return SimpleStatement.comparisonOperators[Arrays.asList(statementTermOperators).indexOf(operator)];
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StatementTerm");
		if (properties != null)
			sb.append(".").append(properties);
		sb.append("\n");
		if (operator != null)
			sb.append("Operator: " + operator);
		sb.append("\n");
		sb.append(lfTerm.toString());
		if (QDSOper != null)
			sb.append(QDSOper).append(" ");
		sb.append(rtTerm.toString());
		return sb.toString();
	}

	public List<Decision> getDecisions() {
		List<Decision> decs = new ArrayList<Decision>();
		decs.add(this.lfTerm);
		decs.add(this.rtTerm);
		return decs;
	}

	public boolean isArithmaticOp() {
		return (operator.equals(Operator.PLUS) || 
				operator.equals(Operator.MINUS) || 
				operator.equals(Operator.TIMES) || 
				operator.equals(Operator.DIVIDE_BY) ||
				//operator.equals(Operator.NOT_EQUAL_TO) ||
				operator.equals(Operator.IS_NOT_NULL) ||
				operator.equals(Operator.IS_NULL) ||
				operator.equals(Operator.LIKE));
	}
	
}
