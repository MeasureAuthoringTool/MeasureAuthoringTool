package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GroupedCodeListDTO implements IsSerializable {

	public static class Comparator implements java.util.Comparator<GroupedCodeListDTO> {

		@Override
		public int compare(GroupedCodeListDTO o1, GroupedCodeListDTO o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	}
	
	private String name;
	private String id;
	private String description;
	public  String codeSystem;
	private String oid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	
}
