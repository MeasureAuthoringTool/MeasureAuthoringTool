package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class RetrieveModel extends ExpressionBuilderModel {

	private IExpressionBuilderModel parentModel;
	private String datatype;
	private String terminology;
	
	public RetrieveModel(ExpressionBuilderModel parent) {
		super(parent);
		this.datatype = "";
		this.terminology = "";
	}
	
	/**
	 * Creates a new terminology model
	 * @param datatype the datatype for the left hand side of the retrieve
	 * @param terminology the right hand side of the retrieve. This value should be properly quoted when passed in. 
	 */
	public RetrieveModel(String datatype, String terminology, ExpressionBuilderModel parent) {
		this(parent);
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
	public String getCQL(String indentation) {
		String cql = "";
		cql = cql + "[\"" + datatype + "\""; 
		
		if(!terminology.isEmpty()) {
			cql = cql + ": " + terminology;
		}
		
		cql = cql + "]";
		
		return cql;
	}
	
	@Override
	public CQLType getType() {
		return CQLType.LIST;
	}

	@Override
	public String getDisplayName() {
		return ExpressionType.RETRIEVE.getDisplayName();
	}

}
