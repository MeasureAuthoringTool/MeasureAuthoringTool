package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.shared.StringUtility;

public class QuantityModel extends ExpressionBuilderModel {
	public QuantityModel(ExpressionBuilderModel parent) {
		super(parent);
	}

	private String value = "";
	private String unit;
	
	public String getQuantity() {
		return value;
	}

	public void setQuantity(String value) {
		this.value = value;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Override
	public String getCQL(String identation) {
		return this.value + (StringUtility.isEmptyOrNull(this.unit) ? "" : " '" + this.unit + "'");
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
