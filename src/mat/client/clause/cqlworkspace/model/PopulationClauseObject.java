package mat.client.clause.cqlworkspace.model;

public class PopulationClauseObject {
	
	private String displayName = "";
	private String type = "";
	private String uuid = "";
	private String cqlDefinitionDisplayName = "";
	private String cqlDefinitionUUID = "";
	private String cqlExpressionType = "";
	
	public PopulationClauseObject(PopulationClauseObject population) {
		this.displayName = population.getDisplayName(); 
		this.type = population.getType(); 
		this.uuid = population.getUuid();
		this.cqlDefinitionDisplayName = population.getCqlDefinitionDisplayName(); 
		this.cqlDefinitionUUID = population.getCqlDefinitionUUID(); 
		this.cqlExpressionType = population.getCqlExpressionType(); 
	}
	
	public PopulationClauseObject() {
		
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
	public String getCqlDefinitionDisplayName() {
		return cqlDefinitionDisplayName;
	}
	public void setCqlDefinitionDisplayName(String cqlDefinitionDisplayName) {
		this.cqlDefinitionDisplayName = cqlDefinitionDisplayName;
	}
	public String getCqlDefinitionUUID() {
		return cqlDefinitionUUID;
	}
	public void setCqlDefinitionUUID(String cqlDefinitionUUID) {
		this.cqlDefinitionUUID = cqlDefinitionUUID;
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
		
		builder.append("<" + this.getCqlExpressionType() + " ");
		builder.append("displayName=\"" + this.getCqlDefinitionDisplayName() + "\" "); 
		builder.append("uuid=\"" + this.getCqlDefinitionUUID() + "\"/>"); 
	
		builder.append("</clause>"); 
		
		return builder.toString(); 
	}
	
}
