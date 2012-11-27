package mat.shared.model;

import java.util.List;

@SuppressWarnings("serial")
public class Or extends Conditional {
	public Or() {
		super(Conditional.Operator.OR);
	}

	public Or(Decision decision) {
		super(Conditional.Operator.OR, decision);
	}
	
	public Or(List<Decision> decisions) {
		super(Conditional.Operator.OR, decisions);
	}
	
	public Or(String properties, Decision decision) {
		super(Conditional.Operator.OR, properties, decision);
	}

	public Or(String properties, List<Decision>decisions) {
		super(Conditional.Operator.OR, properties, decisions);
	}
}
