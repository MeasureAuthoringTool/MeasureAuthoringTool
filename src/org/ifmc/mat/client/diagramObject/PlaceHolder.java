package org.ifmc.mat.client.diagramObject;

public class PlaceHolder extends DiagramObject {
	protected SimpleStatement simpleStatement;
	
	public PlaceHolder(SimpleStatement simpleStatement) {
		super(simpleStatement.getIdentity(), "white", "blue", true);
		this.simpleStatement = simpleStatement;
		this.expanded = false;
	}
	
	public SimpleStatement getSimpleStatement() {
		return simpleStatement;
	}
	
	public String getSimpleStatementFullText() {
		return simpleStatement.getFullText();
	}
	
	public String getSimpleStatementFullTooltipText() {
		return simpleStatement.getPrettyPrintedFullText();
	}
	
	public String getSimpleStatementFirstLine() {
		return simpleStatement.getFirstLine();
	}
	
	@Override
	public boolean hasPropertyEditor() {
		return true;
	}
	
	public PlaceHolder clone() {
		return new PlaceHolder(simpleStatement);
	}
	
	@Override
	public boolean canHaveChildren() {
		return false;
	}
}
