package mat.client.expressionbuilder.model;

public class RetrieveModel extends ExpressionBuilderModel {

	private String datatype;
	private String terminology;
	
	public RetrieveModel() {
		this.datatype = "";
		this.terminology = "";
	}
	
	public RetrieveModel(String datatype, String terminology) {
		this();
		this.datatype = datatype;
		this.terminology = terminology;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getTerminology() {
		return terminology;
	}
	public void setTerminology(String terminology) {
		this.terminology = terminology;
	}
	
	@Override
	public String getCQL() {
		String cql = "";
		cql = cql + "[\"" + datatype + "\""; 
		
		if(!terminology.isEmpty()) {
			cql = cql + ": \"" + terminology + "\"";
		}
		
		cql = cql + "]";
		
		return cql;
	}
}
