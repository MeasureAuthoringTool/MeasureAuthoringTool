package mat.model.clause;

import java.util.ArrayList;
import java.util.List;


import mat.model.ObjectStatus;
import mat.shared.model.IQDSTerm;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Clause extends IQDSTerm implements IsSerializable {
	private String id;
	private String name;
	private String description;
	private String measureId;
	private String contextId;
	private List<mat.shared.model.Decision> decisions;
	private String clauseTypeId;
	private ObjectStatus statusId;
	
	//Not used in the DB, but currently used for storing the version prefix
	//for the Clause Library.
	private String version;
	private String customName;
	private String changedName;
	private Decision decision;
	
	
	
	public String getChangedName() {
		return changedName;
	}
	public void setChangedName(String changedName) {
		this.changedName = changedName;
	}
	public Clause() {
	}
	public String getMeasureId() {
		return measureId;
	}
	public Decision getDecision() {
		return decision;
	}
	public void setDecision(Decision decision) {
		this.decision = decision;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	public List<mat.shared.model.Decision> getDecisions() {
		if (decisions == null)
			setDecisions(new ArrayList<mat.shared.model.Decision>());
		return decisions;
	}
	public void setDecisions(List<mat.shared.model.Decision> decisions) {
		this.decisions = decisions;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getContextId() {
		return contextId;
	}
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getClauseTypeId() {
		return clauseTypeId;
	}
	public void setClauseTypeId(String clauseTypeId) {
		this.clauseTypeId = clauseTypeId;
	}
	
	
	public ObjectStatus getStatusId() {
		return statusId;
	}
	public void setStatusId(ObjectStatus statusId) {
		this.statusId = statusId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
}
