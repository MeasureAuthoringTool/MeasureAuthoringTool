package org.ifmc.mat.client.diagramObject;

public class CriterionParent extends DiagramObject {
	public CriterionParent(String name) {
		super(name);
		expanded = true;
	}
	
	public CriterionParent(String name,String customName){
		super(name,customName);
		expanded = true;
	}
	
	@Override
	public boolean canHaveChildren() {
		return false;
	}
}
