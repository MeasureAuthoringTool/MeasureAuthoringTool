package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.shared.StringUtility;

public class QuantityModel extends ExpressionBuilderModel {
	public QuantityModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	private String value;
	private String unit;
	private String cqlString;
	
	public String getQuantity() {
		return value;
	}

	public void setQuantity(String value) {
		this.value = value;
		updateCQLString();
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
		updateCQLString();
	}
	
	@Override
	public String getCQL(String identation) {
		return this.cqlString;
	}
	
	private void updateCQLString() {
		this.cqlString = value + (StringUtility.isEmptyOrNull(this.unit) ? "" : " '" + this.unit + "'");
	}
	
	@Override
	public CQLType getType() {
		return CQLType.QUANTITY;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.QUANTITY.getDisplayName();
	}
}
