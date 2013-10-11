package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Code implements IsSerializable , Cloneable{
	public static class Comparator implements java.util.Comparator<Code>, IsSerializable {

		@Override
		public int compare(Code o1, Code o2) {
			return o1.getCode().compareTo(o2.getCode());
		}
	}
	
	private String id;
	private String code;
	private String description;
	private String codeListID;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if(id!=null)
			id=id.trim();
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code.trim();
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description.trim();
	}
	
	@Override
    public boolean equals(Object aThat) {
	   
	    Code that = (Code)aThat;
	    return
	    	this.code.equalsIgnoreCase(that.code);
		}
	
	@Override
	public int hashCode() {
		int code = getCode().hashCode(); 
		return code;
	}

	public Code clone(){
		Code clone = new Code();
		clone.setCode(this.getCode());
		clone.setDescription(this.getDescription());
		return clone;
	}
	public String getCodeListID() {
		return codeListID;
	}
	public void setCodeListID(String codeListID) {
		this.codeListID = codeListID;
	}
	
	
}
