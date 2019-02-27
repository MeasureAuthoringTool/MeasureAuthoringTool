package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.shared.StringUtility;

public class QuantityModel extends ExpressionBuilderModel {
	private String value;
	private String units;
	
	public String getQuantity() {
		return value;
	}

	public void setQuantity(String value) {
		this.value = value;
	}
	
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	@Override
	public String getCQL(String identation) {
		return this.value + (StringUtility.isEmptyOrNull(this.units) ? "" : " '" + this.units + "'");
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
