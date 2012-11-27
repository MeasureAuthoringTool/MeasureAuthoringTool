package org.ifmc.mat.shared.model;


@SuppressWarnings("serial")
public class AttributeTerm extends IQDSTerm {
	protected String lfTerm;
	private String QDSOper;
	protected Decision rtTerm;

	public enum Operator {
		A_LESS_THAN, 
		A_GREATER_THAN, 
		A_LESS_THAN_OR_EQUAL_TO, 
		A_GREATER_THAN_OR_EQUAL_TO,
		A_EQUAL_TO,
		A_NOT_EQUAL_TO,
		A_ADDED_TO,
		A_SUBTRACTED_FROM,
		A_TIMES,
		A_DIVIDED_BY,
		A_IS_NOT_NULL,
		A_IS_NULL,
		A_LIKE,
		A_NOT_EMPTY
		};
	
	Operator operator;

	public String getLfTerm() {
		return lfTerm;
	}
	
	public void setLfTerm(String lfTerm) {
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
	
	
	public Operator getOperator() {
		return operator;
	}
	
	public void setOperator(Operator operator) {
		this.operator = operator;
	}	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AttributeTerm");
		if (operator != null)
			sb.append("Operator: " + operator);
		sb.append("\n");
		sb.append(lfTerm.toString());
		if (QDSOper != null)
			sb.append(QDSOper).append(" ");
		sb.append(rtTerm.toString());
		return sb.toString();
	}
}

