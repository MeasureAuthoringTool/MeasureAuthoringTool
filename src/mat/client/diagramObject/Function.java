package mat.client.diagramObject;

public class Function extends DiagramObject {
	public String[] identities = {"COUNT" , "COUNTUNIQUEBYDATE" , "FIRST" , "SECOND" , "LAST" , "NOT" , "EMPTY" , "NOTEMPTY" , "MAX" , "MIN"};
	
	public Function() {
		super();
	}
	
	public Function(String identity) {
		super(identity);
	}
}
