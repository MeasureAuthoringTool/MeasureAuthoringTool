package mat.client.diagramObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.model.ObjectStatus;
import mat.model.QualityDataSetDTO;
import mat.model.clause.Clause;
import mat.shared.Attribute;
import mat.shared.model.AttributeTerm;
import mat.shared.model.Decision;
import mat.shared.model.FunctionTerm;
import mat.shared.model.MeasurementTerm;
import mat.shared.model.QDSMeasurementTerm;
import mat.shared.model.QDSTerm;
import mat.shared.model.StatementTerm;
import mat.shared.model.QDSMeasurementTerm.QDSOperator;
import mat.shared.model.StatementTerm.Operator;
import mat.shared.model.util.OperatorConverter;

import com.google.gwt.user.client.Window;

public class SimpleStatementToCentralPojoFactory {
	private SimpleStatement measurePhrase;
	private Clause clause;
	private List<Decision> decisions;

	// This list holds all clauses as these get created from simple statement.
	// mainly used for referencing clauses within clauses
	private Map<String, Clause> clauses = new HashMap<String, Clause>();

	// The helper class to resolve the UI data captions with backend entities.
	//private Map<String, QualityDataSetDTO> qdsMap;
	
	private List<QualityDataSetDTO> qdsList;

	public SimpleStatementToCentralPojoFactory(Map<String, Clause> clauses) {
		this.clauses = clauses;
	}

	private static final Map<String, StatementTerm.Operator> comparisonStringToOperator = new HashMap<String, StatementTerm.Operator>() {
		{
			put("Less Than", Operator.LESS_THAN);
			put("LESS_THAN", Operator.LESS_THAN);
			put("Less Than or Equal To", Operator.LESS_THAN_OR_EQUAL_TO);
			put("LESS_THAN_OR_EQUAL_TO", Operator.LESS_THAN_OR_EQUAL_TO);
			put("Equal To", Operator.EQUAL_TO);
			put("EQUAL_TO", Operator.EQUAL_TO);
			put("Greater Than or Equal To", Operator.GREATER_THAN_OR_EQUAL_TO);
			put("GREATER_THAN_OR_EQUAL_TO", Operator.GREATER_THAN_OR_EQUAL_TO);
			put("Greater Than", Operator.GREATER_THAN);
			put("GREATER_THAN", Operator.GREATER_THAN);
			put("NOT_EQUAL_TO", Operator.NOT_EQUAL_TO);

			put("Not Equal To", Operator.NOT_EQUAL_TO);
			put("Added To", Operator.PLUS);
			put("Subtracted From", Operator.MINUS);
			put("Times", Operator.TIMES);
			put("Divided By", Operator.DIVIDE_BY);
			put("Is Not Null", Operator.IS_NOT_NULL);
			put("Is Null", Operator.IS_NULL);
			put("Like", Operator.LIKE);

		}
	};

	
	public Clause getClauseFor(SimpleStatement measurePhrase) throws Exception {
		this.measurePhrase = measurePhrase;
		clause = new Clause();
		// implicit assumption, the clause if populated and returned in this
		// method.
		clauses.put(measurePhrase.getIdentity().trim(), clause);
		clause.setName(measurePhrase.getIdentity());
		//fixing the description persisting problem
		clause.setDescription(measurePhrase.getDescription());
		clause.setId(measurePhrase.getmPhraseUniqueIdentity());
		//US 602 Fix for Status dropdown persisting problem
		ObjectStatus obj = new ObjectStatus();
		obj.setId(measurePhrase.getStatusSelectedValue());
		obj.setDescription(measurePhrase.getStatusItemText());
		clause.setStatusId(obj);
		clause.setChangedName(measurePhrase.getChangedName());
		decisions = clause.getDecisions();
		switch (measurePhrase.mode) {
		case SINGLE:
			return getSingle();
		case LOGICAL:
			return getLogical();
		case COMPARISON:
			return getComparison();
		case FUNCTION:
			return getQDSQuantityOperations();
		default:
			return null;
		}
	}

	private Clause getSingle() throws Exception {
		if (measurePhrase.condition.equals(SimpleStatement.NONE)) {
			return getSinglePhrase();
		} else if (isQuantityComparison()) {
			return getQDSQuantityOperations();
		} else {
			return getSingleComparison();
		}
	}

	private boolean isQuantityComparison() {
		return this.measurePhrase.phrase2 == null &&
			this.measurePhrase.conditionalOperator != null && 
			!this.measurePhrase.conditionalOperator.trim().isEmpty();
	}

	private Clause getSinglePhrase() throws Exception {
		if (this.clauses.get(measurePhrase.phrase1.getText()) != null) {
			decisions.add(this.clauses.get(measurePhrase.phrase1.getText()
					.trim()));
		} else {
			if (measurePhrase.phrase1 != null
					&& measurePhrase.phrase1.getText() != null) {
				//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase1.getText().trim());
				QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase1.getText().trim());
				QDSTerm qdsTerm = createQDSTerm(qds.getId(),
						measurePhrase.phrase1);
				decisions.add(qdsTerm);
			} 

		}
		return clause;
	}

	private QDSTerm createQDSTerm(String qdsId, Phrase p) {
		QDSTerm qdsTerm = new QDSTerm();
		qdsTerm.setqDSRef(qdsId);
		setAttrToCP(qdsTerm, p);
		return qdsTerm;
	}

	private void setAttrToCP(QDSTerm qt, Phrase p) {
		// get constants from central location
		String COMPARISON = "Comparison";
		String QDS_ELEMENT = "Value Set";
		String PRESENT = "Check if Present";

		List<Attribute> aList = p.getAttributes();
		List<Decision> listOfD = new ArrayList<Decision>();

		if (aList == null || aList.size() < 1)
			return;
		for (Attribute a : aList) {
			AttributeTerm at = new AttributeTerm();

			if (a.getType() == null || a.getType().equals(PRESENT)) {
				a.setType(PRESENT);
			}

			// attribute name
			at.setLfTerm(a.getAttribute());

			// MeasurementTerm
			if (a.getType().equalsIgnoreCase(COMPARISON)) {
				OperatorConverter oc = new OperatorConverter();
				at.setOperator(oc.clientToAttributeTermOp(a
						.getComparisonOperator()));
				MeasurementTerm mt = new MeasurementTerm();
				mt.setQuantity(a.getQuantity());
				mt.setUnit(a.getUnit());
				at.setRtTerm(mt);
			} else if (a.getType().equalsIgnoreCase(QDS_ELEMENT)) {
				at.setOperator(AttributeTerm.Operator.A_EQUAL_TO);

				QDSTerm qdsTerm = new QDSTerm();
				QualityDataSetDTO qds = getQDMFromTheList(a.getTerm());
				qdsTerm.setqDSRef(qds.getId());
				// qdsTerm.setqDSRef(a.getTerm());
				at.setRtTerm(qdsTerm);
			} else if (a.getType().equalsIgnoreCase(PRESENT)) {
				at.setOperator(AttributeTerm.Operator.A_NOT_EMPTY);
			}
			listOfD.add(at);
		}
		qt.setAttributes(listOfD);
	}

	private Clause getSingleComparison() throws Exception {
		StatementTerm statementTerm = new StatementTerm();

		if (this.clauses.get(measurePhrase.phrase1.getText().trim()) != null) {
			statementTerm.setLfTerm(this.clauses.get(measurePhrase.phrase1
					.getText().trim()));
		} else {
			//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase1.getText().trim());
			QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase1.getText().trim());
			statementTerm.setLfTerm(createQDSTerm(qds.getId(),
					measurePhrase.phrase1));
		}
		statementTerm
				.setOperator(getComparisonStringToOperator(measurePhrase.condition));
		String phrase2 = measurePhrase.phrase2.getText().trim();

		if (phrase2.equals(""))
			throw new IllegalArgumentException(
					"Phrase2 cannot be blank in a comparison!");

		//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase2.getText().trim());
		QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase2.getText().trim());
		if (qds == null)
			throw new IllegalArgumentException(
					"QDM is null in SimpleStatementToCentralPojoFactory.getSingleComparison");
		statementTerm.setRtTerm(createQDSTerm(qds.getId(),
				measurePhrase.phrase2));
		decisions.add(statementTerm);
		return clause;
	}

	private Clause getLogical() throws Exception {
		mat.shared.model.Conditional conditional = measurePhrase.condition
				.equalsIgnoreCase("AND") ? new mat.shared.model.And()
				: new mat.shared.model.Or();
		List<Decision> conditionalDecisions = conditional.getDecisions();

		if (this.clauses.get(measurePhrase.phrase1.getText().trim()) != null) {
			conditionalDecisions.add(this.clauses.get(measurePhrase.phrase1
					.getText()));
		} else {
			//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase1.getText().trim());
			QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase1.getText().trim());
			conditionalDecisions.add(createQDSTerm(qds.getId(),
					measurePhrase.phrase1));
		}

		if (this.clauses.get(measurePhrase.phrase2.getText()) != null) {
			conditionalDecisions.add(this.clauses.get(measurePhrase.phrase2
					.getText()));
		} else {
			if (measurePhrase.phrase2 != null
					&& measurePhrase.phrase2.getText() != null) {
				//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase2.getText().trim());
				QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase2.getText().trim());
				conditionalDecisions.add(createQDSTerm(qds.getId(),
						measurePhrase.phrase2));
			} 
		}

		if (measurePhrase.additionalPhraseList != null) {
			for (Phrase phrase : measurePhrase.additionalPhraseList) {
				if (this.clauses.get(phrase.getText()) != null) {
					conditionalDecisions
							.add(this.clauses.get(phrase.getText()));
				} else {
					if (phrase != null && phrase.getText() != null) {
						//QualityDataSetDTO qds = qdsMap.get(phrase.getText().trim());
						QualityDataSetDTO qds = getQDMFromTheList(phrase.getText().trim());
						conditionalDecisions.add(createQDSTerm(qds.getId(),
								phrase));
					} 
					// what phrase is here?
					// replace with below code...if phrase is known
					// conditionalDecisions.add(createQDSTerm(qds.getId(),
					// measurePhrase.phrase1));
				}
			}
		}

		if (hasFunction())
			decisions.add(getFunctionTerm(conditional));
		else
			decisions.add(conditional);
		return clause;
	}

	private Clause getComparison() throws Exception {
		/*
		 * If these are set in measurePhrase, then you need to use them in the
		 * correct pojo protected String conditionalOperator; protected String
		 * quantity; protected String unit;
		 */

		Decision decision = null;
		if (hasQDSTimeRelativity()) {
			// A QDS measurement term is created for QDS timing relativities.
			QDSMeasurementTerm qdsMeasurementTerm = getQDSMeasurementTerm();
			decision = qdsMeasurementTerm;
		} else {
			// A statement term is created for Arithmatic Operations.
			decision = getStatementTerm();
		}

		// A statement term is created for additional quantity comparisons.
		if (hasComparisonOperator()) {
			decision = getStatementTerm(decision);
		}

		if (hasFunction()) {
			decision = getFunctionTerm(decision);
		}

		decisions.add(decision);
		return clause;
	}

	private Clause getQDSQuantityOperations() throws Exception {

		Decision decision = null;
		decision = processLeftDecision();

		if (hasComparisonOperator() && emptyCondition()) {
			decision = getStatementTerm(decision);
		}

		//If you have function selected, wrap decision as a FunctionTerm
		if (hasFunction()) {
			decision = getFunctionTerm(decision);
		}

		decisions.add(decision);
		return clause;
	}

	private boolean emptyCondition() {
		return measurePhrase.phrase2 == null || 
		measurePhrase.phrase2.getText() == null ||
		measurePhrase.phrase2.getText().trim().isEmpty();
	}

	private Decision processLeftDecision() {
		Decision decision;
		// A statement term is created for additional quantity comparisons.
		if (this.clauses.get(measurePhrase.phrase1.getText()) != null) {
			decision = this.clauses.get(measurePhrase.phrase1.getText().trim());
		} else {
			//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase1.getText().trim());
			QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase1.getText().trim());
			decision = createQDSTerm(qds.getId(),
					measurePhrase.phrase1);
		}
		return decision;
	}
	
	
	private boolean hasQDSTimeRelativity() {
		return (this.measurePhrase.condition != null
				&& !this.measurePhrase.condition.trim().isEmpty() && Rel
				.longToShortIdentity(this.measurePhrase.condition) != null);

	}

	private QDSMeasurementTerm getQDSMeasurementTerm() throws Exception {
		QDSMeasurementTerm qdsMeasurementTerm = new QDSMeasurementTerm();
		// The phrase definition names preceed the QDM elements names.
		if (this.clauses.get(measurePhrase.phrase1.getText()) != null) {
			qdsMeasurementTerm.setLfTerm(this.clauses.get(measurePhrase.phrase1
					.getText().trim()));
		} else {
			//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase1.getText().trim());
			QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase1.getText().trim());
			qdsMeasurementTerm.setLfQDS(createQDSTerm(qds.getId(),
					measurePhrase.phrase1));
		}
		qdsMeasurementTerm
				.setQDSOperator(getTimingStringToQDSOperator(measurePhrase.condition));

		if (this.clauses.get(measurePhrase.phrase2.getText()) != null) {
			qdsMeasurementTerm.setRtTerm(this.clauses.get(measurePhrase.phrase2
					.getText()));
		} else {
			if (measurePhrase.phrase2 != null
					&& measurePhrase.phrase2.getText() != null) {
				//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase2.getText().trim());
				QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase2.getText().trim());
				qdsMeasurementTerm.setRtQDS(createQDSTerm(qds.getId(),
						measurePhrase.phrase2));
			} else {
				Window.alert("Null pointer exception in SimpleStatementToCentralPojoFactory on line 286.");
			}
		}
		return qdsMeasurementTerm;
	}

	private StatementTerm getStatementTerm() throws Exception {
		StatementTerm statementTerm = new StatementTerm();
		// The phrase definition names preceed the QDM elements names.
		if (this.clauses.get(measurePhrase.phrase1.getText()) != null) {
			statementTerm.setLfTerm(this.clauses.get(measurePhrase.phrase1
					.getText().trim()));
		} else {
			//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase1.getText().trim());
			QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase1.getText().trim());
			statementTerm.setLfTerm(createQDSTerm(qds.getId(),
					measurePhrase.phrase1));
		}
		statementTerm.setOperator(comparisonStringToOperator
				.get(measurePhrase.condition));

		if (this.clauses.get(measurePhrase.phrase2.getText()) != null) {
			statementTerm.setRtTerm(this.clauses.get(measurePhrase.phrase2
					.getText()));
		} else {
			if (measurePhrase.phrase2 != null
					&& measurePhrase.phrase2.getText() != null) {
				//QualityDataSetDTO qds = qdsMap.get(measurePhrase.phrase2.getText().trim());
				QualityDataSetDTO qds = getQDMFromTheList(measurePhrase.phrase2
						.getText().trim());
				statementTerm.setRtTerm(createQDSTerm(qds.getId(),
						measurePhrase.phrase2));
			} else {
				Window.alert("Null pointer exception in SimpleStatementToCentralPojoFactory on line 325.");
			}
		}
		return statementTerm;
	}

	private QualityDataSetDTO getQDMFromTheList(String selectedText){
		for(QualityDataSetDTO dto : qdsList){
			if(dto.toString().equalsIgnoreCase(selectedText)){
				return dto;
			}
		}
		return null;
	}
	
	private Decision getStatementTerm(Decision inpDecision) {
		Decision decision;
		// set StatementTerm if data is entered.
		StatementTerm stmtTrm = null;
		stmtTrm = new StatementTerm();
		stmtTrm.setOperator(comparisonStringToOperator
				.get(measurePhrase.conditionalOperator));
		stmtTrm.setLfTerm(inpDecision);

		MeasurementTerm mTerm = new MeasurementTerm();
		mTerm.setQuantity(measurePhrase.quantity);
		if(measurePhrase.functionUnit != null && !measurePhrase.functionUnit.trim().equals("")){
			mTerm.setUnit(measurePhrase.functionUnit);
		}else{
			mTerm.setUnit(measurePhrase.unit);
		}
		stmtTrm.setRtTerm(mTerm);
		decision = stmtTrm;
		return decision;
	}

	private boolean hasFunction() {
		return measurePhrase.function != null
				&& !measurePhrase.function.trim().equals("");
	}

	private boolean hasComparisonOperator() {
		return (this.measurePhrase.conditionalOperator != null && !this.measurePhrase.conditionalOperator
				.trim().isEmpty());
	}

	private Decision getFunctionTerm(mat.shared.model.Decision decision)
			throws Exception {

		FunctionTerm functionTerm = new FunctionTerm();
		functionTerm.setName(measurePhrase.function);
		functionTerm.setTerm(decision);

		// wrap function inside the statement.
		if (measurePhrase.functionOperator != null
				&& !measurePhrase.functionOperator.trim().isEmpty()) {
			StatementTerm statementTerm = new StatementTerm();
			statementTerm.setLfTerm(functionTerm);
			statementTerm
					.setOperator(getComparisonStringToOperator(measurePhrase.functionOperator));
			if(measurePhrase.functionUnit != null && !measurePhrase.functionUnit.isEmpty()){
				statementTerm.setRtTerm(new MeasurementTerm(
						measurePhrase.functionQuantity, measurePhrase.functionUnit));
			}else{
				statementTerm.setRtTerm(new MeasurementTerm(
						measurePhrase.functionQuantity, measurePhrase.unit));
			}
			return statementTerm;
		} else {
			return functionTerm;
		}
	}

	private Operator getComparisonStringToOperator(String condition)
			throws Exception {
		Operator operator = comparisonStringToOperator.get(condition);
		if (operator == null)
			throw new IllegalArgumentException("Operator missing!");
		return operator;
	}

	private QDSOperator getTimingStringToQDSOperator(String timing)
			throws Exception {
		String shortTiming = Rel.longToShortIdentity(timing);
		QDSOperator qdsOperator = new OperatorConverter()
				.toQDSMeasurementTermOp(shortTiming);
		if (qdsOperator == null)
			throw new IllegalArgumentException("Timing missing!");
		return qdsOperator;
	}

	/*public void setDataResolver(Map<String, QualityDataSetDTO> qdsMap) {
		this.qdsMap = qdsMap;
	}*/
	
	public void setDataResolver(List<QualityDataSetDTO> qdsLists) {
		this.qdsList = qdsLists;
	}
}
