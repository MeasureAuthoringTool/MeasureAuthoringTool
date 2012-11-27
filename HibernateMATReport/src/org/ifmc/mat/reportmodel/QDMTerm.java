package org.ifmc.mat.reportmodel;


@SuppressWarnings("serial")
public class QDMTerm  {
	private String qDSRef;
	private String id;
	private String decisionId;
	
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

	public QDMTerm() {
	}
	
	public QDMTerm(String qDSRef) {
		this.qDSRef = qDSRef;
	}

}
