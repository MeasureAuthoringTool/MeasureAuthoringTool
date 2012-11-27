package mat.model.clause;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Context implements IsSerializable {
	private String id;
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Context() {}
	
	public Context (String id, String desc) {
		this.id = id;
		this.description = desc;
	}

	@Override
	public boolean equals(Object temp) {
		
		if (temp == null || !(temp instanceof Context)) return false;
		Context ctxTemp = (Context)temp;
		if (ctxTemp.getDescription() != null && getDescription() != null && ctxTemp.getDescription().equals(getDescription())) return true;
		if (ctxTemp.getId() != null && getId() != null && ctxTemp.getId().equals(getId())) return true;
		return false;
		
		
	}
}
