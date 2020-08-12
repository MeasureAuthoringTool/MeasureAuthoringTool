package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.shared.StringUtility;

import java.util.HashSet;
import java.util.Set;

public class QuantityModel extends ExpressionBuilderModel {
	Set<String> timingUnits = new HashSet<String>() {
		private static final long serialVersionUID = -217586643431369976L;
		{
			add("millisecond");
			add("milliseconds");
			add("second");
			add("seconds");
			add("minute");
			add("minutes");
			add("hour");
			add("hours");
			add("day");
			add("days");
			add("week");
			add("weeks");
			add("month");
			add("months");
			add("year");
			add("years");
		}
	};
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
	public String getCQL(String indentation) {
		String timingValue = "";
		if(!StringUtility.isEmptyOrNull(this.unit) && !timingUnits.contains(this.unit)) {
			timingValue = " '" + this.unit + "'";
		} else if(!StringUtility.isEmptyOrNull(this.unit)) {
			timingValue = " " + this.unit;
		}
		return this.value + timingValue;
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
