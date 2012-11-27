package mat.model;

public class GroupedCodeList {
	private String id;
	private String description;
	private CodeList codeList;
	
	
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
	public CodeList getCodeList() {
		return codeList;
	}
	public void setCodeList(CodeList codeList) {
		this.codeList = codeList;
	}
	
	public GroupedCodeList clone(){
		GroupedCodeList clone = new GroupedCodeList();
		clone.setCodeList(this.getCodeList());
		clone.setDescription(this.getDescription());
		return clone;
	}
	
}
