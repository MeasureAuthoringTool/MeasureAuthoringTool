package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;


public class CQLParameter implements IsSerializable{
	private String parameterName;
	private String cqlType;
	private String defaultValue;
	private String parameterLogic;
	private String id;
	private boolean readOnly;
	private String commentString = "";
	
	
	public String getParameterName() {
		return parameterName.trim();
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName.trim();
	}
	public String getCqlType() {
		return cqlType;
	}
	public void setCqlType(String cqlType) {
		this.cqlType = cqlType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getParameterLogic() {
		return parameterLogic.trim();
	}
	public void setParameterLogic(String parameterLogic) {
		this.parameterLogic = parameterLogic.trim();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly;
	}
	public String getCommentString() {
		return commentString;
	}
	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}
	
	
}
