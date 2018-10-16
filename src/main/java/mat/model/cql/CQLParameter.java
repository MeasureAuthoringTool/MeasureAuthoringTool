package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;


public class CQLParameter implements CQLExpression, IsSerializable{
	private String parameterName;
	private String cqlType;
	private String defaultValue;
	private String parameterLogic;
	private String id;
	private boolean readOnly;
	private String commentString = "";
	
	
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
	public String getParameterName() {
		return parameterName.trim();
	}
	
	public void setParameterName(String name) {
		this.parameterName = name.trim();
	}
	
	public String getParameterLogic() {
		return parameterLogic.trim();
	}

	public void setParameterLogic(String logic) {
		this.parameterLogic = logic.trim();
	}
	
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return getParameterName();
	}
	@Override
	public void setName(String name) {
		setParameterName(name);
	}
	@Override
	public String getLogic() {
		return getParameterLogic();
	}
	@Override
	public void setLogic(String logic) {
		setParameterLogic(logic);
	}
	
	
}
