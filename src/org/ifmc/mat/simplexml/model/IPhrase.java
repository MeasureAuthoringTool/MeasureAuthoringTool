package org.ifmc.mat.simplexml.model;

public interface IPhrase {
	public void setTo(To to);
	public void setToAttr(String toAttr);
	
	public final String PLUS_OP = "+";
	public final String MINUS_OP = "-";
	public final String MULTIPLY_OP = "x";
	public final String DIVIDE_OP = "/";
}
