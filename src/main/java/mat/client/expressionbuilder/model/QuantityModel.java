package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.shared.StringUtility;

public class QuantityModel extends ExpressionBuilderModel {
	private String quantity;
	private String units;
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	@Override
	public String getCQL(String identation) {
		return this.quantity + (StringUtility.isEmptyOrNull(this.units) ? "" : " '" + this.units + "'");
	}
	
	@Override
	public CQLType getType() {
		return CQLType.ANY;
	}
	
	@Override
	public String getDisplayName() {
		return ExpressionType.QUANTITY.getDisplayName();
	}
}
