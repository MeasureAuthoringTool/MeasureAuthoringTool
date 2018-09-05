package mat.client.populationworkspace.model;

public class PopulationClauseObject {
	
	private String displayName = "";
	private String type = "";
	private String uuid = "";
	private String cqlExpressionDisplayName = "";
	private String cqlExpressionUUID = "";
	private String cqlExpressionType = "";
	private String aggFunctionName = "";
	private Integer sequenceNumber;
	
	public PopulationClauseObject(PopulationClauseObject population) {
		this.displayName = population.getDisplayName(); 
		this.type = population.getType(); 
		this.uuid = population.getUuid();
		this.cqlExpressionDisplayName = population.getCqlExpressionDisplayName(); 
		this.cqlExpressionUUID = population.getCqlExpressionUUID(); 
		this.cqlExpressionType = population.getCqlExpressionType(); 
	}
	
	public PopulationClauseObject() {
		
	}
	
	public PopulationClauseObject(String uuidstr) {
		this.uuid = uuidstr;
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getCqlExpressionDisplayName() {
		return cqlExpressionDisplayName;
	}
	public void setCqlExpressionDisplayName(String cqlDefinitionDisplayName) {
		this.cqlExpressionDisplayName = cqlDefinitionDisplayName;
	}
	public String getCqlExpressionUUID() {
		return cqlExpressionUUID;
	}
	public void setCqlExpressionUUID(String cqlDefinitionUUID) {
		this.cqlExpressionUUID = cqlDefinitionUUID;
	}
	
	public String getCqlExpressionType() {
		return cqlExpressionType;
	}


	public void setCqlExpressionType(String cqlExpressionType) {
		this.cqlExpressionType = cqlExpressionType;
	}


	public String toXML() {
		StringBuilder builder = new StringBuilder(); 
		builder.append("<clause "); 
		builder.append("displayName=\"" + this.getDisplayName() + "\" "); 
		builder.append("type=\"" + this.getType() + "\" "); 
		builder.append("uuid=\"" + this.getUuid() + "\" "); 
		builder.append(">");
		
		if(this.getAggFunctionName() != null && this.getAggFunctionName().trim().length() > 0) {
			builder.append("<cqlaggfunction ");
			builder.append("displayName=\"" + this.getAggFunctionName() + "\" ");
			builder.append(">");
		}
		
		if(this.cqlExpressionType != null && this.cqlExpressionType.trim().length() > 0) {
			builder.append("<" + this.getCqlExpressionType() + " ");
			builder.append("displayName=\"" + this.getCqlExpressionDisplayName() + "\" "); 
			builder.append("uuid=\"" + this.getCqlExpressionUUID() + "\"/>");
		}
	
		if(this.getAggFunctionName() != null && this.getAggFunctionName().trim().length() > 0) {
			builder.append("</cqlaggfunction>");
		}
	
		builder.append("</clause>"); 
		
		return builder.toString(); 
	}

	public String getAggFunctionName() {
		return aggFunctionName;
	}

	public void setAggFunctionName(String aggFunctionName) {
		this.aggFunctionName = aggFunctionName;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
}
