package mat.shared.model;

import java.util.List;

@SuppressWarnings("serial")
public class And extends Conditional {
	public And() {
		super(Conditional.Operator.AND);
	}
	
	public And(Decision decision) {
		super(Conditional.Operator.AND, decision);
	}
	
	public And(List<Decision> decisions) {
		super(Conditional.Operator.AND, decisions);
	}
	
	public And(String properties, Decision decision) {
		super(Conditional.Operator.AND, properties, decision);
	}

	public And(String properties, List<Decision>decisions) {
		super(Conditional.Operator.AND, properties, decisions);
	}
}
