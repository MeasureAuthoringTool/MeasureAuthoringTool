package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;

public class DateTimeModel extends ExpressionBuilderModel {

	public DateTimeModel(final ExpressionBuilderModel parent) {
		super(parent);
	}

	private String dateTimeString = "";

	@Override
	public String getCQL(final String indentation) {
		final StringBuilder builder = new StringBuilder();
		builder.append(dateTimeString);

		for(int i = 0; i < getChildModels().size(); i++) {
			builder.append(getChildModels().get(i).getCQL(indentation));
		}

		builder.append(" ");
		return builder.toString();
	}

	@Override
	public CQLType getType() {
		return CQLType.BOOLEAN;
	}

	@Override
	public String getDisplayName() {
		return ExpressionType.DATE_TIME.getDisplayName();
	}

	public String getDateTimeString() {
		return dateTimeString;
	}

	public void setDateTimeString(final String dateTimeString) {
		this.dateTimeString = dateTimeString;
	}
}
