package mat.simplexml.model;

import mat.shared.model.AttributeTerm;
import mat.shared.model.MeasurementTerm;
import mat.shared.model.StatementTerm;
import mat.shared.model.StatementTerm.Operator;

public class Value {
	private String ttext;
	private High high;
	private Low low;
	private Equal equal;
	private String type;

	public void diagram(PrettyPrinter pp) {
		pp.incrementIndentation();
		pp.concat("VALUE");
		pp.incrementIndentation();
		pp.concat("TYPE", getType());
		if (getHigh() != null)
			getHigh().diagram(pp);
		if (getLow() != null)
			getLow().diagram(pp);
		pp.decrementIndentation();
		pp.decrementIndentation();
	}

	public Value(String term, Operator operator) {
		switch (operator) {
			case LESS_THAN:	
				setHigh(new High(term, "", false));
				break;
			case LESS_THAN_OR_EQUAL_TO:
				setHigh(new High(term, "", true));
				break;					
			case GREATER_THAN:
				setLow(new Low(term, "", false));
				break;
			case GREATER_THAN_OR_EQUAL_TO:
				setLow(new Low(term, "", true));
				break;
			case EQUAL_TO:
				setEqual(new Equal(term, ""));
				setType("PQ");
				break;
			case NOT_EQUAL_TO:
				break;
		}
		if(getType() == null)
			setType("IVL_PQ");	
	}

	public Value(MeasurementTerm rightTerm, StatementTerm.Operator operator) {	
		switch (operator) {
		case LESS_THAN:	
			setHigh(new High(rightTerm.getQuantity(), rightTerm.getUnit(), false));
			break;
		case LESS_THAN_OR_EQUAL_TO:
			setHigh(new High(rightTerm.getQuantity(), rightTerm.getUnit(), true));
			break;					
		case GREATER_THAN:
			setLow(new Low(rightTerm.getQuantity(), rightTerm.getUnit(), false));
			break;
		case GREATER_THAN_OR_EQUAL_TO:
			setLow(new Low(rightTerm.getQuantity(), rightTerm.getUnit(), true));
			break;
		case EQUAL_TO:
			setEqual(new Equal(rightTerm.getQuantity(), rightTerm.getUnit()));
			setType("PQ");
			break;
		case NOT_EQUAL_TO:
			break;
		}
		if(getType() == null)
			setType("IVL_PQ");
	}

	public Value(MeasurementTerm rightTerm, AttributeTerm.Operator operator) {	
		switch (operator) {
		case A_LESS_THAN:	
			setHigh(new High(rightTerm.getQuantity(), rightTerm.getUnit(), false));
			break;
		case A_LESS_THAN_OR_EQUAL_TO:
			setHigh(new High(rightTerm.getQuantity(), rightTerm.getUnit(), true));
			break;					
		case A_GREATER_THAN:
			setLow(new Low(rightTerm.getQuantity(), rightTerm.getUnit(), false));
			break;
		case A_GREATER_THAN_OR_EQUAL_TO:
			setLow(new Low(rightTerm.getQuantity(), rightTerm.getUnit(), true));
			break;
		case A_EQUAL_TO:
			setEqual(new Equal(rightTerm.getQuantity(), rightTerm.getUnit()));
			setType("PQ");
			break;
		case A_NOT_EQUAL_TO:
			break;
		}
		if(getType() == null)
			setType("IVL_PQ");
	}

	public Value(String quantity, String unit) {
		setHigh(quantity, unit);
		setType("IVL_PQ");
	}

	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public High getHigh() {
		return high;
	}
	public void setHigh (High high ) {
		this.high = high;
	}
	private void setHigh(String quantity, String unit) {
		this.high = new High(quantity, unit);
	}
	public Low getLow() {
		return low;
	}
	public void setLow(Low low) {
		this.low = low;
	}
	public String getType() {
		return type;
	}
	public void setEqual(Equal equal){
		this.equal = equal;
	}
	public Equal getEqual(){
		return equal;
	}
	public void setType (String type ) {
		this.type = type;
	}
}