package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;


public class CQLParameter implements IsSerializable{
	private String parameterName;
	private String cqlType;
	private String defaultValue;
	private String parameterLogic;
	private String id;

	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
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
		return parameterLogic;
	}
	public void setParameterLogic(String parameterLogic) {
		this.parameterLogic = parameterLogic;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


}
