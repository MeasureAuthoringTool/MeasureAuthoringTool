package mat.server.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import mat.model.QualityDataSet;
import mat.model.clause.Clause;
import mat.server.export.criteriontointerim.PropelDelegate;
import mat.server.service.impl.CriterionToInterimUtility;
import mat.shared.ConstantMessages;
import mat.shared.model.AttributeTerm;
import mat.shared.model.Conditional;
import mat.shared.model.Decision;
import mat.shared.model.FunctionTerm;
import mat.shared.model.IDecision;
import mat.shared.model.IQDSTerm;
import mat.shared.model.MeasurementTerm;
import mat.shared.model.PrimitiveTerm;
import mat.shared.model.QDSMeasurementTerm;
import mat.shared.model.QDSTerm;
import mat.shared.model.StatementTerm;
import mat.shared.model.StatementTerm.Operator;
import mat.simplexml.model.And;
import mat.simplexml.model.Criterion;
import mat.simplexml.model.Denominator;
import mat.simplexml.model.Exceptions;
import mat.simplexml.model.Exclusions;
import mat.simplexml.model.First;
import mat.simplexml.model.Function;
import mat.simplexml.model.FunctionHolder;
import mat.simplexml.model.IPhrase;
import mat.simplexml.model.Id;
import mat.simplexml.model.Last;
import mat.simplexml.model.LogicOp;
import mat.simplexml.model.MeasureObservation;
import mat.simplexml.model.MeasurePopulation;
import mat.simplexml.model.Not;
import mat.simplexml.model.Numerator;
import mat.simplexml.model.NumeratorExclusions;
import mat.simplexml.model.Or;
import mat.simplexml.model.Population;
import mat.simplexml.model.Propel;
import mat.simplexml.model.Properties;
import mat.simplexml.model.Property;
import mat.simplexml.model.Qdsel;
import mat.simplexml.model.Reference;
import mat.simplexml.model.Second;
import mat.simplexml.model.Stratification;
import mat.simplexml.model.Third;
import mat.simplexml.model.To;
import mat.simplexml.model.Value;

import org.springframework.context.ApplicationContext;


public class CriterionToInterim {
	//TODO Need to update funcNames and timeUnits array with the updated List
	final String[] funcNames = new String[]  {
			"ABS","ADDDATE","ADDTIME","AVG","COUNTDISTINCT","COUNT","CURDATE","CURTIME","DATEDIFF","DAYOFMONTH","DAYOFWEEK","DAYOFYEAR","HOUR","MAX",
			"MEDIAN","MIN",
			"MINUTE","MONTH","NOW","POSITION","ROUND","SEC","STDDEV","SUBDATE","SUBTIME","SUM","TIME",
			"TIMEDIFF","VARIANCE","WEEK"," WEEKDAY","WEEKOFYEAR","YEAR","YEARWEEK","FIRST","SECOND",
			"THIRD","FOURTH","FIFTH","LAST","RELATIVEFIRST","RELATIVESECOND", "NOT"};
	final String[] timeUnits = {
		"SECOND", "SECONDS", "MINUTE", "MINUTES", "HOUR", "HOURS",
		"DAY", "DAYS", "MONTH", "MONTHS", "YEAR", "YEARS", "WEEKS"};

	private List<QualityDataSet> qds = new ArrayList<QualityDataSet>();
	
	private List<String> qdmRefs = new ArrayList<String>();

	private List<Function> functions = new ArrayList<Function>();
	
	private List<Propel> propels = new ArrayList<Propel>();

	private List<Qdsel> qdsels = new ArrayList<Qdsel>();

	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

	private AtomicLong sequenceNumber = new AtomicLong(0);

	private ApplicationContext context;
	
	public CriterionToInterim(List<QualityDataSet> qds, ApplicationContext context) {
		this.qds = qds;
		this.context = context;
	}

	public Criterion getCriterion(Clause cl) throws Exception {
		Criterion criterion = getCriterion(cl.getContextId());
		LogicOp interim = getInterim(cl);

		if (interim != null) {
			criterion.setLogicOp(interim);
		}

		return criterion;
	}
	
	private Criterion getCriterion(String contextId) {
		if (contextId.equals(ConstantMessages.POPULATION_CONTEXT_ID)) {
			return new Population();
		} else if (contextId.equals(ConstantMessages.NUMERATOR_CONTEXT_ID)) {
			return new Numerator();	
		} else if (contextId.equals(ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID)) {
			return new NumeratorExclusions();
		} else if (contextId.equals(ConstantMessages.DENOMINATOR_CONTEXT_ID)) {
			return new Denominator();
		} else if (contextId.equals(ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID)) {
			return new Exclusions();
		} else if (contextId.equals(ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID)) {
			return new Exceptions();
		} else if(contextId.equals(ConstantMessages.MEASURE_POPULATION_CONTEXT_ID)){
			return new MeasurePopulation();
		} else if(contextId.equals(ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID)){
			return new MeasureObservation();
		}else if(contextId.equals(ConstantMessages.STRATIFICATION_CONTEXT_ID)){
			return new Stratification();
		}else {
			return null;
		}
	}
	
	public LogicOp getInterim(Clause c) throws Exception {
		functions = new ArrayList<Function>();
		/* propels = new ArrayList<Propel>();
		 * do not re-init propels,
		 * the state must be preserved across system clauses 
		 * so we can avoid duplicate propels
		 */
		qdsels = new ArrayList<Qdsel>();

		if( c.getDecisions() != null && !c.getDecisions().isEmpty()) {
			 //Assumption that there will be only one decision in an exportable clause.
			 //and the decision will be a conditional.
			 Clause cl = (Clause)c.getDecisions().get(0);
			 if(!cl.getDecisions().isEmpty()) {
				 /*Conditional cond = new Conditional();
				 if (cl.getDecisions().get(0) instanceof Conditional) {
					 cond = (Conditional)cl.getDecisions().get(0);
			 		 LogicOp op = doCond(cond);
			 		LogicOp and = LogicOp.logicOpFactory(mat.shared.model.Conditional.Operator.AND);
			 		and.addLogicOp(op);
			 		return and;
				 } else {
					 cond.setOperator(mat.shared.model.Conditional.Operator.AND);
					 for(Decision d : cl.getDecisions())
						 cond.addDecision(d);
					 return doCond(cond);
				 }*/
				 return doCond((Conditional) peel(c.getDecisions().get(0)));
			 }else {
				 return LogicOp.logicOpFactory(mat.shared.model.Conditional.Operator.AND);
			 }
		 } 
		return null;
	}

	//Initial method.
	private LogicOp doCond(Conditional cond) throws Exception {
		LogicOp parentLogicOp = LogicOp.logicOpFactory(cond.getOperator());

		for (IDecision decision: cond.getDecisions()) {
			processEachChild(parentLogicOp, decision);
		}
		return parentLogicOp;
	}

	/**
	 * A general purpose method for processing children of a rule.
	 *
	 **/
	private IDecision processEachChild(LogicOp parentLogicOp, IDecision decision)
			throws Exception {
		
		if (decision instanceof Clause) {
			decision = peel(decision);
		}
		if (decision instanceof Conditional) {
			processDecision(parentLogicOp, (Conditional) decision);
		}else if (decision instanceof QDSMeasurementTerm) {
			processDecision(parentLogicOp, (QDSMeasurementTerm) decision);
		}else if (decision instanceof MeasurementTerm) {
			measurementTerm(parentLogicOp, (MeasurementTerm)decision);
		}else if (isArithMaticOp(decision)) {
			processDecision(parentLogicOp, decision);
		}else if (decision instanceof StatementTerm) {
			statementTerm(parentLogicOp, (StatementTerm)decision);
		}else if (decision instanceof FunctionTerm) {
			functionTerm(parentLogicOp, (FunctionTerm)decision);
		}else {
			if (parentLogicOp.getQdsel() == null)
				parentLogicOp.setQdsel(new ArrayList<Qdsel>());
			Qdsel qdsel = new Qdsel();
			parentLogicOp.addQdsel(getQdsel(decision, qdsel));
		}
		return decision;
	}
	
	private IQDSTerm peel(IDecision decision) {
		CriterionToInterimUtility ctiu = new CriterionToInterimUtility();
		return ctiu.peel(decision);
	}

	private Qdsel term(QDSTerm term, Qdsel qdsel) {
		if (qdsel.getId() == null || qdsel.getIdAttr() == null)
			setQdsel(qdsel, term);
		else{
			//TODO handle: this is not a valid state, in the interim, every qdsel must have qdsel/@id populated
			addToAttrToPhrase(qdsel, getQDSSeqNum(term.getqDSRef()));
			addToQdmRefs(term.getqDSRef());
		}
		return qdsel;
	}

	private Qdsel primitiveTerm(PrimitiveTerm primitiveTerm, Qdsel qdsel) {
		qdsel.setTtext((String)(primitiveTerm.getObject()));
		return qdsel;
	}

	private void measurementTerm(LogicOp logicOp, MeasurementTerm measurementTerm) {
		Qdsel qdsel = new Qdsel();
		setValue(qdsel, measurementTerm);
	}

	private Qdsel qdsMeasurementTerm(QDSMeasurementTerm measurementTerm, Qdsel parentQdsel) throws Exception {
		parentQdsel.setRel(measurementTerm.getQDSOperator().name());
		IQDSTerm leftTerm = measurementTerm.getLfQDS();
		IQDSTerm rightTerm = measurementTerm.getRtQDS();

		processLeftTerm(leftTerm, parentQdsel);
		processRightTerm(rightTerm, parentQdsel);

		return parentQdsel;
	}

	private LogicOp qdsMeasurementTermWithConditionalRel(QDSMeasurementTerm measurementTerm) throws Exception {
		IQDSTerm leftTerm = measurementTerm.getLfQDS();
		IQDSTerm rightTerm = measurementTerm.getRtQDS();
		LogicOp logicOp = null;
		
		leftTerm = peel(leftTerm);
		rightTerm = peel(rightTerm);

		if (leftTerm instanceof Conditional && rightTerm instanceof Conditional)
			logicOp = condRelBoth(measurementTerm, leftTerm, rightTerm);			
		else if (leftTerm instanceof Conditional)
			logicOp = condRelLeft(measurementTerm, leftTerm, rightTerm);
		else if (rightTerm instanceof Conditional) {
			logicOp = condRelRight(measurementTerm, leftTerm, rightTerm);
		}
		return logicOp;
	}

	private LogicOp condRelRight(QDSMeasurementTerm measurementTerm, IQDSTerm leftTerm, IQDSTerm rightTerm) throws Exception {
		LogicOp logicOp;
		Conditional cond;
		//			[1] SBOD ([2] OR [3])
		//			<or rel="SBOD">
		//				<qdsel id="1" />
		//				<to>
		//					<qdsel id="2" />
		//					<qdsel id="3" />
		//				</to>
		//			</or>
		
		leftTerm = peel(leftTerm);
		rightTerm = peel(rightTerm);

		cond = (Conditional)rightTerm;
		logicOp = getCondRel(measurementTerm, cond);	

		if (leftTerm instanceof QDSTerm) {
			Qdsel qds = new Qdsel();
			setQdsel(qds,(QDSTerm)leftTerm);
			logicOp.addQdsel(qds);
		}
		else processCondrelDecision(leftTerm, logicOp);
		
		To to = new To();
		to.setQdsel(new ArrayList<Qdsel>());

		for (IDecision decision : cond.getDecisions()) {
			processEachChild(logicOp, decision);
		}

		logicOp.setTo(to);
		return logicOp;
	}

	private LogicOp condRelLeft(QDSMeasurementTerm measurementTerm,
			IQDSTerm leftTerm, IQDSTerm rightTerm) throws Exception {
		LogicOp logicOp;
		Conditional cond;
		//			([1] OR [2]) SBOD [3]
		//			<or rel="SBOD" to="3">
		//				<qdsel idattr="1">
		//				<qdsel idattr="2" />
		//			</or>

		leftTerm = peel(leftTerm);
		rightTerm = peel(rightTerm);

		cond = (Conditional)leftTerm;
		logicOp = getCondRel(measurementTerm, cond);	

		for (IDecision decision : cond.getDecisions()) {
			processEachChild(logicOp, decision);
		}
		processRightTerm(rightTerm, logicOp);
		return logicOp;
	}
	
	private void processCondrelDecision(IDecision decision, LogicOp logicOp) throws Exception{
		if (decision instanceof Conditional)
			logicOp.addLogicOp(doCond((Conditional)decision));
		else if (decision instanceof StatementTerm)
			statementTerm(logicOp, (StatementTerm)decision);
		else if (decision instanceof FunctionTerm)
			functionTerm(logicOp, (FunctionTerm)decision);
		else if (decision instanceof PrimitiveTerm) {
			Qdsel property = new Qdsel();
			property.setProperty((String)((PrimitiveTerm)decision).getObject());
			logicOp.addQdsel(property);
		}
		else
			throw new IllegalArgumentException(error("condRelLeft: decision", decision.getClass().getSimpleName()));
	}

	private LogicOp condRelBoth(QDSMeasurementTerm measurementTerm,
			IQDSTerm leftTerm, IQDSTerm rightTerm) throws Exception {
		LogicOp logicOp;
		Conditional cond;
		//			([1] OR [2]) SBOD ([3] AND [4])
		//			<or rel="SBOD" >
		//				<qdsel idattr="1" />
		//				<qdsel idattr="2" />
		//				<to>
		//					<and>
		//						<qdsel idattr="3"/>
		//						<qdsel idattr="4"/>
		//					</and>
		//				</to>
		//			</or>
		
		leftTerm = peel(leftTerm);
		rightTerm = peel(rightTerm);

		cond = (Conditional)leftTerm;
		logicOp = getCondRel(measurementTerm, cond);

		for (IDecision decision : cond.getDecisions()) {
			processEachChild(logicOp, decision);
		}

		cond = (Conditional)rightTerm;
		To to = new To();
		LogicOp toLogicOp = null;
		toLogicOp = LogicOp.logicOpFactory(cond.getOperator());
		toLogicOp.setQdsel(new ArrayList<Qdsel>());

		for (IDecision decision : cond.getDecisions()) {
			processEachChild(toLogicOp, decision);
		}

		to.setLogicOp(toLogicOp);
		logicOp.setTo(to);
		return logicOp;
	}

	private void linkedTo(QDSMeasurementTerm measurementTerm, Qdsel qdsel) throws Exception {
		IQDSTerm leftTerm = measurementTerm.getLfQDS();
		IQDSTerm rightTerm = measurementTerm.getRtQDS();

		processLeftTerm(leftTerm, qdsel);
		Qdsel referenceQdsel = new Qdsel();
		//not so sure why calling processLeftTerm(...) twice
		processLeftTerm(rightTerm, referenceQdsel);
		
		qdsel.setReference(new Reference(referenceQdsel));
	}

	private void statementTerm(FunctionHolder functionHolder, StatementTerm statementTerm) throws Exception {
		String funcName = "";
		String property = "";
		String primitiveTerm = null;
		Qdsel qdsel = new Qdsel();
		MeasurementTerm mTerm = null;
		StatementTerm.Operator operator = statementTerm.getOperator();
		IDecision leftTerm = statementTerm.getLfTerm();
		IDecision rightTerm = statementTerm.getRtTerm();
		boolean isCondRelLeftWithMeasurementTermRight = false;
		boolean isFunction = false;

		
		if (leftTerm instanceof Clause) {
			leftTerm = peel(leftTerm);
		}
		if (rightTerm instanceof Clause) {
			rightTerm = peel(rightTerm);
		}

		if (leftTerm instanceof FunctionTerm) {
			isFunction = true;
			FunctionTerm funcTerm = (FunctionTerm)leftTerm;
			funcName = funcTerm.getName();
			property = funcTerm.getProperty();
			leftTerm = funcTerm.getTerm();

			
			if (leftTerm instanceof Clause) {
				leftTerm = peel(leftTerm);
			}
		}

		if (leftTerm instanceof QDSTerm) {
			QDSTerm leftQDSTerm = (QDSTerm)leftTerm;
			setQdsel(qdsel, leftQDSTerm);
		}
		//Can we get rid of this if?? 
		else if (leftTerm instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)leftTerm;
			if (isLinkedTo(qdsMeasurementTerm))
				setLinkedToID(qdsel, qdsMeasurementTerm);				 
			else {
				if (isCondRel(qdsMeasurementTerm)) {
					if (rightTerm instanceof MeasurementTerm) {
						setCondRelLeftWithMeasurementTermRight(
								qdsel, qdsMeasurementTerm, operator, (MeasurementTerm)rightTerm);
						isCondRelLeftWithMeasurementTermRight = true;
					}
					else
						setCondRelID(qdsel, qdsMeasurementTerm);
				}
				else {
					qdsel = getQdselFromMeasurementTerm(qdsel, (QDSMeasurementTerm)leftTerm);
					String qdsProperty = qdsMeasurementTerm.getProperty();
					if (qdsProperty != null && qdsProperty.length() > 0)
						qdsel.setProperty(qdsProperty);
				}
			}
		}
		else if (leftTerm instanceof Conditional) {
			getConditionalIdFromConditionalTerm(qdsel, (Conditional)leftTerm);
		}
		else if (leftTerm instanceof FunctionTerm) {
			Id id = new Id();
			functionTerm(id, (FunctionTerm)leftTerm);
			qdsel.setId(id);
		}
		else if (isArithMaticOp(leftTerm)) {
			Id id = new Id();
			And and = new And();
			id.setAnd(and);
			qdsel.setId(id);
			qdsel = createQdselForArithmaticOp(leftTerm, qdsel, and);
		} else if (leftTerm instanceof StatementTerm) {
			Id id = new Id();
			statementTerm(id, (StatementTerm)leftTerm);
			qdsel.setId(id);
		}
		else
			throw new IllegalArgumentException(error("statementTerm: leftTerm", leftTerm.getClass().getSimpleName()));

		if (!isCondRelLeftWithMeasurementTermRight) {
			if (rightTerm instanceof QDSTerm) {
				setRHSToWithPropertyCheck((QDSTerm) rightTerm, qdsel);
			}
			else if (rightTerm instanceof QDSMeasurementTerm) {
				QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)rightTerm;
				if (isLinkedTo(qdsMeasurementTerm))
					setLinkedToTo(qdsel, qdsMeasurementTerm);
				else {
					if (isCondRel(qdsMeasurementTerm))
						setCondRelTo(qdsel, qdsMeasurementTerm);
					else {
						List<Qdsel>qdselList = new ArrayList<Qdsel>();
						Qdsel toQdsel = new Qdsel();
						toQdsel = qdsMeasurementTerm((QDSMeasurementTerm)rightTerm, toQdsel);
						qdselList.add(toQdsel);
						To to = new To();
						to.setQdsel(qdselList);
						addToToPhrase(qdsel, to); 
					}
				}
			}
			else if (rightTerm instanceof MeasurementTerm ) {
				if(!isFunction) {
					 setValue(qdsel, operator, rightTerm);
				 }
			}
			else if (rightTerm instanceof Conditional)
				getConditionalToFromConditionalTerm(qdsel, (Conditional)rightTerm);
			else if (rightTerm instanceof PrimitiveTerm)
				primitiveTerm = new String((String)((PrimitiveTerm)rightTerm).getObject());
			else if (rightTerm instanceof FunctionTerm) {
				To to = new To();
				functionTerm(to, (FunctionTerm)rightTerm);
				addToToPhrase(qdsel, to);
			}
			else if (rightTerm instanceof StatementTerm) {
				To to = new To();
				statementTerm(to, (StatementTerm)rightTerm);
				addToToPhrase(qdsel, to);
			}		
			else
				throw new IllegalArgumentException(error("statementTerm: rightTerm", rightTerm.getClass().getSimpleName()));
		}

		if (primitiveTerm != null && funcName.equals("") && qdsel.getValue() == null)
			setValue(qdsel, primitiveTerm, operator);

		if (Arrays.asList(funcNames).contains(funcName.toUpperCase())) {
			//per the Simple expression editor. The Function is wrapped inside the statement 
			//Term, you need to get the primitive from the right side of the statement term.
			//primitiveTerm = retrievePrimitiveTerm(statementTerm);
			mTerm = getMeasurementTerm(statementTerm);
			//Removed unwanted code for function COUNT. as part of US 436
			boolean identified = setFunction(functionHolder, funcName, qdsel, property);
			//process custom HQMF functions.
			if (!identified) {
				createReferenceAndRegister(
								functionHolder, 
								new Function(funcName, operator, mTerm, qdsel,property));
			}
				
		}else {
			if (functionHolder instanceof LogicOp) {
				LogicOp logicOp = (LogicOp)functionHolder;
				if (logicOp.getQdsel() == null)
					logicOp.setQdsel(new ArrayList<Qdsel>());
				logicOp.addQdsel(qdsel);
			}
			else if (functionHolder instanceof Qdsel) {
//				To to = new To();
//				to.addQdsel(qdsel);
				((Qdsel)functionHolder).setQdsel(qdsel);
			}
			else if (functionHolder instanceof Id) {
				((Id)functionHolder).addQdsel(qdsel);
			}
			else if (functionHolder instanceof To) {
				((To)functionHolder).addQdsel(qdsel);
			}
		}
	}

	/**
	 * Accepts a custom function, adds its entry inside data criteria, 
	 * creates the function reference as qdsel.
	 * This qdsel is used as a proxy to this function.
	 * 
	 * FunctionHolder holds  the qdsel proxy
	 * Function that is passed
	 **/
	private Qdsel createReferenceAndRegister(FunctionHolder holder, Function function) {
		Qdsel qds = createQdselReferenceForFunction(function);

		if (holder instanceof LogicOp) {
			((LogicOp)holder).addQdsel(qds);
		}
		if (holder instanceof Qdsel) {
			((Qdsel)holder).setQdsel(qds);
		}
		if (holder instanceof To) {
			((To)holder).setQdsel(new ArrayList<Qdsel>());
			((To)holder).addQdsel(qds);
		}
		if (holder instanceof Id) {
			((Id)holder).setQdsel(new ArrayList<Qdsel>());
			((Id)holder).addQdsel(qds);
		}
		return qds;
	}

	private Qdsel createQdselReferenceForFunction(Function function) {
		String functionId = nextCalcSequence();
		function.setIdAttr(functionId);
		functions.add(function);
		Qdsel qds = new Qdsel();
		qds.setIdAttr(functionId);
		return qds;
	}

	private String retrievePrimitiveTerm(StatementTerm statementTerm) {
		MeasurementTerm quantity = (MeasurementTerm)statementTerm.getRtTerm();
		return quantity.getQuantity();
	}

	private MeasurementTerm getMeasurementTerm(StatementTerm statementTerm){
		return (MeasurementTerm)statementTerm.getRtTerm();
	}
	
	private void functionTerm(FunctionHolder functionHolder, FunctionTerm functionTerm) throws Exception {
		String funcName = functionTerm.getName();
		String property = functionTerm.getProperty();
		IDecision term = functionTerm.getTerm();
		
		Qdsel qdsel = new Qdsel();
		
		if (term instanceof Clause) {
			term = peel(term);
		}

		if (term instanceof QDSTerm) {
			QDSTerm qdsTerm = (QDSTerm)term;
			setQdsel(qdsel,qdsTerm);
			if(funcName.startsWith("RELATIVE") && functionHolder instanceof LogicOp){
				processRelativeQdsel(funcName, (LogicOp) functionHolder, qdsel);
				return;
			}
		}
		else if (term instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)term;
			if(funcName.startsWith("RELATIVE") && functionHolder instanceof LogicOp){
				qdsel = getQdselFromMeasurementTerm(qdsel, (QDSMeasurementTerm)term);
				processRelativeQdsel(funcName, (LogicOp) functionHolder, qdsel);
				return;
			}
			if (isLinkedTo(qdsMeasurementTerm))
				setLinkedToID(qdsel, qdsMeasurementTerm);				 
			else {
				if (isCondRel(qdsMeasurementTerm))
					setCondRelID(qdsel, qdsMeasurementTerm);
				else
					qdsel = getQdselFromMeasurementTerm(qdsel, (QDSMeasurementTerm)term);
			}
			
		}
		else if (term instanceof FunctionTerm) {
			Id id = new Id();
			functionTerm(id, (FunctionTerm)term);
			qdsel.setId(id);
		}
		else if (isArithMaticOp(term)) {
			Id id = new Id();
			And and = new And();
			id.setAnd(and);
			qdsel.setId(id);
			qdsel = createQdselForArithmaticOp(term, qdsel, and);
		} 
		else if (term instanceof StatementTerm) {
			Id id = new Id();
			statementTerm(id, (StatementTerm)term);
			if(funcName.startsWith("RELATIVE") && functionHolder instanceof LogicOp){
				processRelativeQdsel(funcName, (LogicOp) functionHolder, id.getQdsel().get(0));
				return;
			}
			qdsel.setId(id);
		}		
		else if (term instanceof Conditional)
			getConditionalIdFromConditionalTerm(qdsel, (Conditional)term);
		else
			throw new IllegalArgumentException(error("functionTerm: term", term.getClass().getSimpleName()));

		if (Arrays.asList(funcNames).contains(funcName.toUpperCase()))	{
			boolean identified = setFunction(functionHolder, funcName, qdsel, property);
			if (!identified) {
				qdsel = createReferenceAndRegister(functionHolder, new Function(funcName, null, null,qdsel,property));
			}

		}
	}
	private void setRelativeQdselOrder(String funcName, Qdsel qdsel){
		if(funcName.endsWith("FIRST"))
			qdsel.setOrder("1");
		if(funcName.endsWith("SECOND"))
			qdsel.setOrder("2");
	}
	private void processRelativeQdsel(String funcName, LogicOp functionHolder, Qdsel qdsel){
		setRelativeQdselOrder(funcName, qdsel);
		functionHolder.addQdsel(qdsel);
	}
	
	private Qdsel createQdselForArithmaticOp(IDecision term, Qdsel qdsel, And and) 
			throws Exception {
		String funcName;
		StatementTerm statementTerm = ((StatementTerm)term);
		List<Decision> children = statementTerm.getDecisions();

		for (Decision arithMaticSide: children) {
			processEachChild(and, arithMaticSide);
		}
 
		for(Qdsel q: and.getQdsel()){
			setValue(q, statementTerm.getOperator(), statementTerm.getRtTerm());
		}
		
		if (and.getAnd() != null) {
			for(And a: and.getAnd()){
				setValue(qdsel, a, statementTerm.getOperator(), (MeasurementTerm)statementTerm.getRtTerm());
			}
		}

		if (and.getOr() != null) {
			for(Or o: and.getOr()){
				setValue(qdsel, o, statementTerm.getOperator(), (MeasurementTerm)statementTerm.getRtTerm());
			}
		}
		if(qdsel.getId().getAnd() != null && qdsel.getId().getAnd().getQdsel().size() == 1){
			qdsel = qdsel.getId().getAnd().getQdsel().get(0);
		}
		
		funcName = statementTerm.getOperator().name();
		return createQdselReferenceForFunction(
				new Function(funcName, null, null,qdsel, null));
	}

	private boolean isArithMaticOp(IDecision term) {
		return term instanceof StatementTerm && ((StatementTerm)term).isArithmaticOp();
	}

	private QualityDataSet getQDS(String qdsRef) {
		QualityDataSet temp = new QualityDataSet();
		temp.setId(qdsRef);
		int pos = getQDS().indexOf(temp);
		if (pos >= 0) {
			addToQdmRefs(qdsRef);
			return getQDS().get(pos);
		} else {
			return null;
		}
	}

	private String getQDSSeqNum(String qdsRef) {
		QualityDataSet temp = getQDS(qdsRef);
		if (temp != null) {
			return temp.getOid();
		} else {
			return null;			
		}
	}

	private boolean isLinkedTo(QDSMeasurementTerm qdsMeasurementTerm) {
//		return qdsMeasurementTerm.getQDSOperator() == QDSMeasurementTerm.QDSOperator.LINKEDTO;
		return false;
	}

	private boolean isCondRel(QDSMeasurementTerm qdsMeasurementTerm) {
		Decision dec = null;
		
		 if (qdsMeasurementTerm.getLfTerm() instanceof Clause) {
			 dec = peel(qdsMeasurementTerm.getLfTerm());
		 } else {
			 dec = qdsMeasurementTerm.getLfTerm();
		 }
		 return (dec != null && dec instanceof Conditional);
		//|| qdsMeasurementTerm.getRtTerm() instanceof Conditional;
	}

	private Qdsel getQdsel(IDecision decision, Qdsel qdsel) throws Exception {
		if (decision instanceof Clause) {
			decision = peel(decision);
		}

		if (decision instanceof QDSTerm)
			return term((QDSTerm)decision, qdsel);
		else if (decision instanceof QDSMeasurementTerm)
			return qdsMeasurementTerm((QDSMeasurementTerm)decision, qdsel);
		else if (decision instanceof PrimitiveTerm)
			return primitiveTerm((PrimitiveTerm)decision, qdsel);
		else
			throw new IllegalArgumentException(error("getQdsel: decision", decision.getClass().getSimpleName()));
	}
		
	private void setValue(Qdsel qdsel,
			StatementTerm.Operator operator,
			IDecision term) {

		MeasurementTerm measurementTerm = (MeasurementTerm)term;
		// add measurement content to the reference qdsel if there is one
		Qdsel measurementHolderQdsel = qdsel.getReference() != null ? 
				qdsel.getReference().getQdsel() : qdsel;
		
		if (measurementTerm.getUnit() != null && Arrays.asList(timeUnits).contains(measurementTerm.getUnit().toUpperCase().trim()))
			measurementHolderQdsel.setValueAttr(measurementTerm, operator);
		else
			measurementHolderQdsel.setValue(new Value(measurementTerm, operator));
	}

	private void setValue(Qdsel qdsel, LogicOp logicOp, Operator operator, MeasurementTerm term) {
		MeasurementTerm measurementTerm = (MeasurementTerm)term;
		if (measurementTerm.getUnit() != null && 
				Arrays.asList(timeUnits).contains(measurementTerm.getUnit().toUpperCase().trim()))
			logicOp.setValueAttr(measurementTerm, operator);
		else
			qdsel.setValue(new Value(measurementTerm, operator));
	}

	private void setValue(Qdsel qdsel, MeasurementTerm term) {
		qdsel.setValue(term.getQuantity(), term.getUnit());
	}

	private void setValue(Qdsel qdsel, String primitiveTerm,
			StatementTerm.Operator operator) {
		qdsel.setValue(new Value(primitiveTerm, operator));
	}

	private void setLinkedToID(Qdsel qdsel,
			QDSMeasurementTerm qdsMeasurementTerm) throws Exception {
		Id id = new Id();
		id.setQdsel(new ArrayList<Qdsel>());
		Qdsel idQdsel = new Qdsel();
		linkedTo(qdsMeasurementTerm, idQdsel);
		id.addQdsel(idQdsel);
		qdsel.setId(id);
		String property = qdsMeasurementTerm.getProperty();
		if (property != null && property.length() > 0)
			qdsel.setProperty(property);
	}

	private void setLinkedToTo(Qdsel qdsel,
			QDSMeasurementTerm qdsMeasurementTerm) throws Exception {
		To to = new To();
		to.setQdsel(new ArrayList<Qdsel>());
		Qdsel toQdsel = new Qdsel();
		linkedTo(qdsMeasurementTerm, toQdsel);
		to.addQdsel(toQdsel);
		qdsel.setTo(to);
	}

	private LogicOp getCondRel(QDSMeasurementTerm measurementTerm, Conditional cond) {
		LogicOp logicOp;
		logicOp = LogicOp.logicOpFactory(cond.getOperator());
		logicOp.setRel(measurementTerm.getQDSOperator().name());
		logicOp.setQdsel(new ArrayList<Qdsel>());
		return logicOp;
	}

	private void getConditionalIdFromConditionalTerm(Qdsel qdsel, Conditional leftTerm) throws Exception {
		LogicOp op = doCond(leftTerm);
		Id id = new Id();
		id.setLogicOp(op);

		qdsel.setId(id);
		String condProperty = leftTerm.getProperty();
		if (condProperty != null)
			op.setProperty(condProperty);
	}

	private void getConditionalToFromConditionalTerm(IPhrase phrase, Conditional leftTerm) throws Exception {
		LogicOp op = doCond(leftTerm);
		To to = new To();
		to.setLogicOp(op);	

		addToToPhrase(phrase, to);
	}

	private void setCondRelID(Qdsel qdsel, QDSMeasurementTerm qdsMeasurementTerm) throws Exception {
		Id id = new Id();
		id.setLogicOp(qdsMeasurementTermWithConditionalRel(qdsMeasurementTerm));
		qdsel.setId(id);
		String property = qdsMeasurementTerm.getProperty();
		if (property != null && property.length() > 0)
			qdsel.setProperty(property);
	}

	private void setCondRelLeftWithMeasurementTermRight(Qdsel qdsel,
			QDSMeasurementTerm qdsMeasurementTerm, Operator operator, MeasurementTerm rightTerm) throws Exception {
		setCondRelID(qdsel, qdsMeasurementTerm);
		setValue(qdsel, qdsel.getId().getLogicOp(), operator, rightTerm);
	}

	private Qdsel getQdselFromMeasurementTerm(Qdsel qdsel, QDSMeasurementTerm measurementTerm) throws Exception {
		return getQdselFromMeasurementTerm(qdsel, measurementTerm, false);
	}

	private void setCondRelTo(IPhrase referenceQdsel,
			QDSMeasurementTerm qdsMeasurementTerm) throws Exception {
		To to = new To();
		to.setLogicOp(qdsMeasurementTermWithConditionalRel(qdsMeasurementTerm));
		addToToPhrase(referenceQdsel, to);
	}

	private Qdsel getQdselFromMeasurementTerm(Qdsel qdsel, QDSMeasurementTerm measurementTerm, boolean isReference) throws Exception {
		IQDSTerm leftTerm = measurementTerm.getLfQDS();
		qdsel.setRel(measurementTerm.getQDSOperator().toString());
		processLeftTerm(leftTerm, qdsel);
		IQDSTerm rightTerm = measurementTerm.getRtQDS();
		processRightTerm(rightTerm, qdsel);
		return qdsel;
	}

	private void setQdselIdForFunctionTerm(Qdsel qdsel, IQDSTerm leftTerm)
	throws Exception {
		
		Id id = new Id();
		functionTerm(id, (FunctionTerm)leftTerm);
		qdsel.setIdAttr(id.getQdsel().get(0).getIdAttr());
	}

	private void setQdselToForFunctionTerm(IPhrase phrase, IQDSTerm rightTerm)
	throws Exception {
		
		To parentTo = new To();
		functionTerm(parentTo, (FunctionTerm)rightTerm);
		addToToPhrase(phrase, parentTo);
	}

	/**
	 * A Function holder: directly holds the function created inside the medthod.
	 * A funcName represents the function to be created.
	 * A childQdsel is added inside the method.
	 **/
	private boolean setFunction(FunctionHolder functionHolder, String funcName, Qdsel childQdsel, String property) throws Exception {
		boolean funcIdentified = true;
		 if (funcName.equalsIgnoreCase("FIRST")) {
			functionHolder.addFirst(new First(funcName, childQdsel, property));
		} else if (funcName.equalsIgnoreCase("SECOND")) {
			functionHolder.addSecond(new Second(funcName, childQdsel, property));
		} else if (funcName.equalsIgnoreCase("THIRD")) {
			functionHolder.addThird(new Third(funcName, childQdsel, property));					
		} else if (funcName.equalsIgnoreCase("LAST")) {
			functionHolder.addLast(new Last(funcName, childQdsel, property));
		} else if (funcName.equalsIgnoreCase("NOT")) {
			functionHolder.addNot(new Not(funcName, childQdsel, property));
		} else {
			return false;
		}
		return funcIdentified;
	}

	private String error(String what, String s) {
		return what + ": " + s;
	}

	private String badFunc(String funcName) {
		funcName = funcName.trim();
		return error("func", ((funcName.length() == 0) ? "No func name" : funcName));
	}

	private void warning(String what, String s) {
		System.out.println("WARNING: " +  what + ": " + s);
	}
	
	private void setQdsel(Qdsel qdsel, QDSTerm qdsTerm) {

		qdsel.setIdAttr(getQDSSeqNum(qdsTerm.getqDSRef()));
		//process properties
		// Get all the properties.
		//for each property, find the operator
		if (qdsTerm.getAttributes() != null) {
			for (Decision attr: qdsTerm.getAttributes()) {

				AttributeTerm term = (AttributeTerm)attr;

				//add negation rationale - reference tag.
				//<reference>
				//    <qdsel id="1">
				//</reference>
				if (term.getLfTerm().equals("negation rationale")) {
					handleReferences(qdsel, term);
				} else {
					handleGenericProperties(qdsTerm, qdsel, term);
				}
			}
		}	
	}

	private void createMeasureCalc(QDSTerm qdsTerm, Qdsel qdsel, AttributeTerm attrTerm) {

		Function func = new Function();
		func.setIdAttr(nextCalcSequence());
		func.setRefid(qdsel.getIdAttr());
		func.setName(attrTerm.getLfTerm());
		func.setDatatype("Derivation Expression");
		func.setUuid(UUID.randomUUID().toString());
		if(attrTerm.getRtTerm() instanceof QDSTerm) {
			QDSTerm rtTerm = (QDSTerm)attrTerm.getRtTerm();
			QualityDataSet codelist = getQDS(rtTerm.getqDSRef());
			func.setValue(codelist.getOid());
		}

		if(attrTerm.getRtTerm() instanceof MeasurementTerm) {
			Value val = new Value((MeasurementTerm)attrTerm.getRtTerm(),attrTerm.getOperator()); 
			if(val.getHigh()!= null) {
				func.setHigh(val.getHigh());
			}
			if(val.getLow()!= null) {
				func.setLow(val.getLow());
			}
		}
		functions.add(func);
		qdsel.setIdAttr(func.getIdAttr());
	}

	private void handleReferences(Qdsel qdsel, AttributeTerm term) {
		QDSTerm qds = (QDSTerm)term.getRtTerm();
		
		
		/*else {*/
			//set reference name strategy.
			String negQDselId=null;
			if(qds==null){
				QualityDataSet temp = new QualityDataSet();
				temp.setId(UUID.randomUUID().toString()); 
				negQDselId = (getQDS().size()-1)+"";
				temp.setOid(negQDselId);
				addQDMToList(temp);
			}else
				negQDselId =getQDSSeqNum(qds.getqDSRef());
			String negQDSelIdWithStrategy = negQDselId + "-" + qdsel.getIdAttr();
			Qdsel negQDS = new Qdsel(negQDSelIdWithStrategy);
			if (term.getRtTerm() == null) {
				Properties ps = new Properties();
				Property p = new Property(term.getLfTerm());
				p.setName(p.getName()+" is present");
				ps.addProperty(p);
				negQDS.setProperties(ps);
			}
			Reference ref = new Reference();
			ref.setQdsel(negQDS);
			qdsel.setReference(ref);
			if(qds==null)
				generateQDSelNegationRationale(qdsel, negQDselId, negQDSelIdWithStrategy);
			else
				generateQDSel(qdsel, negQDselId, negQDSelIdWithStrategy);
//		}
	}

	private void handleGenericProperties(QDSTerm qdsTerm, Qdsel qdsel,
			AttributeTerm term) {

		
		String propName = term.getLfTerm();
		Property p = new Property(propName);
		QualityDataSet codelist = null;

		//1. No right side term.
		if (term.getRtTerm() == null ) {
			p.setName(propName);
			p.setValue(nextCalcSequence());
			propels.add(PropelDelegate.generatePropel(p, null, context));
			p.setName(propName + " id");
		} else if(term.getRtTerm() instanceof MeasurementTerm) {
			MeasurementTerm rtTerm = (MeasurementTerm)term.getRtTerm();
			Value val = new Value(rtTerm, term.getOperator());

			if(val.getHigh() != null) {
				p.setHigh(val.getHigh());
			} 
			if (val.getLow() != null) {
				p.setLow(val.getLow());
			}
			if (val.getEqual() != null) {
				p.setEqual(val.getEqual());
			}

			p.setName(propName);
			//setting of value must occur before invocation of PropelDelegate.generatePropel
			p.setValue(nextCalcSequence());
			propels.add(PropelDelegate.generatePropel(p, null, context));

			if (term.getLfTerm().equals("result")) {
				p.setName(p.getName() + " "+ "value");
			} else {
				p.setName(p.getName() + " "+ "id");
			}

		} else	//2. the right side is qds.
			if(term.getRtTerm() instanceof QDSTerm) {
				QDSTerm rtTerm = (QDSTerm)term.getRtTerm();
				codelist = getQDS(rtTerm.getqDSRef());
				p.setName(term.getLfTerm());

				QDSTerm qdsOrCL = (QDSTerm)term.getRtTerm();
				String qdsDBID = qdsOrCL.getqDSRef();
				QualityDataSet qds = getQDS(qdsDBID);
				
				boolean isAttribute = ConstantMessages.ATTRIBUTE.equalsIgnoreCase(qds.getListObject().getCategory().getDescription());
				
				if(isAttribute) {
					String pValue = PropelDelegate.getExistingAttribute(p, codelist, propels);
					if(pValue != null)
						p.setValue(pValue);
					else{
						p.setValue(nextCalcSequence());
						propels.add(PropelDelegate.generatePropel(p, codelist, context));
					}
					p.setName(p.getName() + " "+ "id");
				} else {
					p.setValue(qds.getOid());
					if (term.getLfTerm().equals("result")) {
						p.setName(p.getName() + " "+ "outc");					
					}

					QualityDataSet qdset = getQDS(rtTerm.getqDSRef());
					Qdsel q = createQDSel(qdset, qdset.getDataType().getDescription());
					addQdselToList(q);
					Qdsel propq = new Qdsel();
					propq.setIdAttr(q.getIdAttr());
					p.setQdsel(propq);
					p.setValue(null);
				}
		}
		if (qdsel.getProperties() == null) {
			Properties ps = new Properties();
			qdsel.setProperties(ps);			
		}
		qdsel.getProperties().addProperty(p);
	}

	private void generateQDSel(Qdsel parentQdsel, String negQDselOrigId, String negQDselId) {
		QualityDataSet negQDS = findQDS(negQDselOrigId);
		QualityDataSet parentQDS = findQDS(parentQdsel.getIdAttr());
		Qdsel qdselook = createQDSel(negQDS, parentQDS.getDataType().getDescription());
		qdselook.setIdAttr(negQDselId);
		addQdselToList(qdselook);
	}

	private Qdsel createQDSel(QualityDataSet negQDS, String datatype) {
		Qdsel qdselook = new Qdsel();
		qdselook.setIdAttr(negQDS.getOid());
		qdselook.setName(negQDS.getListObject().getName());
		qdselook.setTaxonomy(negQDS.getListObject().getCodeSystem().getDescription());
		qdselook.setDatatype(datatype + " not done");
		qdselook.setOid(negQDS.getListObject().getOid());
		qdselook.setUuid(UUID.randomUUID().toString());
		qdselook.setCodesystem(negQDS.getListObject().getObjectOwner().getOrgOID());
		qdselook.setCodesystemname(negQDS.getListObject().getSteward().getOrgName());
		return qdselook;
	}
	
	private void generateQDSelNegationRationale(Qdsel parentQdsel, String negQDselOrigId, String negQDselId) {
		QualityDataSet negQDS = findQDS(negQDselOrigId);
		QualityDataSet parentQDS = findQDS(parentQdsel.getIdAttr());
		Qdsel qdselook = createQDSelNegationRationale(negQDS, parentQDS.getDataType().getDescription());
		qdselook.setIdAttr(negQDselId);
		addQdselToList(qdselook);
	}

	private Qdsel createQDSelNegationRationale(QualityDataSet negQDS, String datatype) {
		Qdsel qdselook = new Qdsel();
		qdselook.setIdAttr(negQDS.getOid());
		/*qdselook.setName(negQDS.getListObject().getName());
		qdselook.setTaxonomy(negQDS.getListObject().getCodeSystem().getDescription());*/
		qdselook.setDatatype(datatype + " not done");
		/*qdselook.setOid(negQDS.getListObject().getOid());*/
		qdselook.setUuid(UUID.randomUUID().toString());
		/*qdselook.setCodesystem(negQDS.getListObject().getObjectOwner().getOrgOID());
		qdselook.setCodesystemname(negQDS.getListObject().getSteward().getOrgName());*/
		return qdselook;
	}
	
	
	
	
	
	
	
	
	private QualityDataSet findQDS(String idAttr) {
		for (Function f : functions) {
			if (f.getIdAttr().equals(idAttr)) {
				idAttr = f.getRefid();
				break;
			}
		}
		for (QualityDataSet aQds : getQDS()) {
			if (aQds.getOid().equals(idAttr)) {
				return aQds;
			}
		}
		
		return null;
	}
	
	private void setRHSToWithPropertyCheck(QDSTerm rightTerm, IPhrase phrase){
		if (!rightTerm.getAttributes().isEmpty()) {
			Qdsel q = new Qdsel();
			setQdsel(q, rightTerm);
			To to = new To();
			to.addQdsel(q);
			addToToPhrase(phrase, to);
		}
		else
			addToAttrToPhrase(phrase, getQDSSeqNum(rightTerm.getqDSRef()));
	}
	
	public List<Propel> getPropels() {
		return this.propels;
	}

	public String nextCalcSequence() { 
		return "1-"+ sequenceNumber.getAndIncrement(); 
	}

//	public void setQdsels(List<Qdsel> qdsels) {
//		this.qdsels = qdsels;
//	}

	public List<Qdsel> getQdsels() {
		return qdsels;
	}
	
	private void processLeftTerm(IQDSTerm leftTerm, Qdsel parentQdsel)throws Exception{
		
		leftTerm = peel(leftTerm);
		
		if (leftTerm instanceof QDSTerm)
			setQdsel(parentQdsel,(QDSTerm)leftTerm);
		else if (leftTerm instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)leftTerm;
			if (isLinkedTo(qdsMeasurementTerm))
				setLinkedToID(parentQdsel, qdsMeasurementTerm);
			else
				if (isCondRel(qdsMeasurementTerm))
					setCondRelID(parentQdsel, qdsMeasurementTerm);
				else
					parentQdsel = getQdselFromMeasurementTerm(parentQdsel, (QDSMeasurementTerm)leftTerm, true);
		}
		else if (leftTerm instanceof StatementTerm) {
			Id id = new Id();
			statementTerm(id, (StatementTerm)leftTerm);
			parentQdsel.setId(id);
		}
		else if (leftTerm instanceof Conditional)
			getConditionalIdFromConditionalTerm(parentQdsel, (Conditional)leftTerm);
		else if (leftTerm instanceof FunctionTerm){
			String fname = ((FunctionTerm)leftTerm).getName();
			if(fname.startsWith("RELATIVE")){
				setQdsel(parentQdsel, (QDSTerm) ((FunctionTerm)leftTerm).getTerm());
				setRelativeQdselOrder(fname, parentQdsel);
			}
			else			
				setQdselIdForFunctionTerm(parentQdsel, (FunctionTerm)leftTerm);
		}
		else
			throw new IllegalArgumentException(error("qdsMeasurementTerm: leftTerm", leftTerm.getClass().getSimpleName()));
	}
	private void addToAttrToPhrase(IPhrase phrase, String to){
		if(phrase instanceof Qdsel && ((Qdsel)phrase).getReference() != null) {
			((Qdsel)phrase).getReference().getQdsel().setToAttr(to);
			shiftRelAttributesToReferenceQdsel(((Qdsel)phrase));
		}else
			phrase.setToAttr(to);
		
	}
	private void shiftRelAttributesToReferenceQdsel(Qdsel qdsel){
		qdsel.getReference().getQdsel().setRel(qdsel.getRel());
		qdsel.setRel(null);
		
		qdsel.getReference().getQdsel().setHighinclusive(qdsel.getHighinclusive());
		qdsel.getReference().getQdsel().setHighnum(qdsel.getHighnum());
		if(qdsel.getHighunit()!= null && !qdsel.getHighunit().trim().equals("")){
			qdsel.getReference().getQdsel().setHighunit(qdsel.getHighunit());
		}
		
		qdsel.getReference().getQdsel().setLowinclusive(qdsel.getLowinclusive());
		qdsel.getReference().getQdsel().setLownum(qdsel.getLownum());
		if(qdsel.getLowunit()!= null && !qdsel.getLowunit().trim().equals("")){
			qdsel.getReference().getQdsel().setLowunit(qdsel.getLowunit());
		}
		
		qdsel.getReference().getQdsel().setEqualnum(qdsel.getEqualnum());
		qdsel.getReference().getQdsel().setEqualunit(qdsel.getEqualunit());
		
		qdsel.setHighinclusive(null);
		qdsel.setHighnum(null);
		qdsel.setHighunit(null);
		
		qdsel.setLowinclusive(null);
		qdsel.setLownum(null);
		qdsel.setLowunit(null);
		
		qdsel.setEqualnum(null);
		qdsel.setEqualunit(null);
	}
	private void addToToPhrase(IPhrase phrase, To to){
		if(phrase instanceof Qdsel && ((Qdsel)phrase).getReference() != null) {
			((Qdsel)phrase).getReference().getQdsel().setTo(to);
			shiftRelAttributesToReferenceQdsel(((Qdsel)phrase));
		} else
			phrase.setTo(to);
	}
	
	private void processRightTerm(IQDSTerm rightTerm, IPhrase parentQdsel)throws Exception{
		
		rightTerm = peel(rightTerm);
		
		if (rightTerm instanceof QDSTerm)	
			setRHSToWithPropertyCheck((QDSTerm) rightTerm, parentQdsel);
		else if (rightTerm instanceof QDSMeasurementTerm) {
			To to = new To();
			Qdsel rightQdsel = new Qdsel();
			QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)rightTerm;
			if (isLinkedTo(qdsMeasurementTerm)){
				setLinkedToTo(rightQdsel, qdsMeasurementTerm);
				to.addQdsel(rightQdsel);
				addToToPhrase(parentQdsel, to);
			}
			else {
				if (isCondRel(qdsMeasurementTerm))
					setCondRelTo(parentQdsel, qdsMeasurementTerm);
				else{
					rightQdsel = qdsMeasurementTerm((QDSMeasurementTerm)rightTerm, rightQdsel);
					to.addQdsel(rightQdsel);
					addToToPhrase(parentQdsel, to);			
				}
			}
		}
		else if (rightTerm instanceof StatementTerm) {
			To to = new To();
			statementTerm(to, (StatementTerm)rightTerm);
			addToToPhrase(parentQdsel, to);
		}
		else if (rightTerm instanceof Conditional)
			getConditionalToFromConditionalTerm(parentQdsel, (Conditional)rightTerm);
		else if (rightTerm instanceof FunctionTerm){

			String fname = ((FunctionTerm)rightTerm).getName();
			if(fname.startsWith("RELATIVE")){
				To to = new To();
				Qdsel rightQdsel = new Qdsel();
				setQdsel(rightQdsel, (QDSTerm) ((FunctionTerm)rightTerm).getTerm());
				setRelativeQdselOrder(fname, rightQdsel);
				to.addQdsel(rightQdsel);
				addToToPhrase(parentQdsel, to);
				return;
			}

			setQdselToForFunctionTerm( parentQdsel, (FunctionTerm)rightTerm);
		}
		else
			throw new IllegalArgumentException(error("qdsMeasurementTerm: rightTerm", rightTerm.getClass().getSimpleName()));
	}
	
	private void processDecision(LogicOp parentLogicOp, Conditional decision) throws Exception{
		LogicOp childLogicOp = doCond((Conditional)decision);
		parentLogicOp.addLogicOp(childLogicOp);
	}
	
	private void processDecision(LogicOp parentLogicOp, QDSMeasurementTerm decision) throws Exception{
		Qdsel qdsel = new Qdsel();
		QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)decision;
		if (isLinkedTo(qdsMeasurementTerm)) {
			linkedTo(qdsMeasurementTerm, qdsel);
			parentLogicOp.addQdsel(qdsel);
		}
		else {
			if (isCondRel(qdsMeasurementTerm)){
				parentLogicOp.addLogicOp(qdsMeasurementTermWithConditionalRel(qdsMeasurementTerm));
			}
			else{
				Qdsel q = qdsMeasurementTerm(qdsMeasurementTerm, qdsel);
				IQDSTerm t = peel(qdsMeasurementTerm.getLfTerm());
				if(t instanceof FunctionTerm && !(((FunctionTerm)t).getName()).startsWith("RELATIVE")){
					LogicOp lop = new And();
					lop.setTo(q.getTo());
					lop.setToAttr(q.getToAttr());
					lop.setRel(q.getRel());
					q.setTo(null);
					q.setToAttr(null);
					q.setRel(null);
					lop.addQdsel(qdsel);
					parentLogicOp.addLogicOp(lop);
				}else{
					parentLogicOp.addQdsel(q);
				}
			}
		}
	}
	
	private void processDecision(LogicOp parentLogicOp, IDecision decision) throws Exception{
		Qdsel qdsel = new Qdsel();
		Id id = new Id();
		And and = new And();
		id.setAnd(and);
		qdsel.setId(id);
		qdsel = createQdselForArithmaticOp(decision, qdsel, and);
		parentLogicOp.addQdsel(qdsel);
	}
	private void addQdselToList(Qdsel qdsel){
		qdsels.add(qdsel);
		
	}
	private void addQDMToList(QualityDataSet qdm){
		addToQdmRefs(qdm.getId());
		qds.add(qdm);
	}
	private List<QualityDataSet> getQDS(){
		return this.qds;
	}
	public List<String> getQdmRefs(){
		return qdmRefs;
	}
	private void addToQdmRefs(String ref){
		if(! getQdmRefs().contains(ref))
			getQdmRefs().add(ref);
	}
}
