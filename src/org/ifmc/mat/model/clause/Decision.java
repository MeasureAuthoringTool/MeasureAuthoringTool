package org.ifmc.mat.model.clause;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Decision implements IsSerializable {

	private String id;
	private String parentId;
	private String operator;
	private String orderNum;
	private String clauseId;
	private String attributeId;
	private Set<Decision> childDecisions = new HashSet<Decision>();
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getClauseId() {
		return clauseId;
	}
	public void setClauseId(String clauseId) {
		this.clauseId = clauseId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Set<Decision> getChildDecisions() {
		return childDecisions;
	}
	public void setChildDecisions(Set<Decision> childDecisions) {
		//this.childDecisions = childDecisions;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}

