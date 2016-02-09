package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLDefinition implements IsSerializable{
	private String id;
	private String definitionName;
	private String definitionLogic;
	private String context;
	
	
	
public static class Comparator implements java.util.Comparator<CQLDefinition>, IsSerializable {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(CQLDefinition o1,
				CQLDefinition o2) {
			return o1.getDefinitionName().compareTo(o2.getDefinitionName());
		}
		
	}
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDefinitionName() {
		return definitionName.replaceAll(" ", "").trim();
	}
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName.replaceAll(" ", "").trim();
	}
	public String getDefinitionLogic() {
		return definitionLogic;
	}
	public void setDefinitionLogic(String definitionLogic) {
		//this.definitionLogic = "<![CDATA[" + definitionLogic + "]]>";
		this.definitionLogic = definitionLogic;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
