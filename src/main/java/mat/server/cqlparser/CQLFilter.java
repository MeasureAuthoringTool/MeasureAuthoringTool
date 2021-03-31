package mat.server.cqlparser;

import cqltoelm.models.LibraryHolder;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.shared.CQLExpressionObject;
import mat.shared.CQLExpressionOprandObject;
import mat.shared.CQLObject;
import org.cqframework.cql.cql2elm.QdmModelInfoProvider;
import org.hl7.cql.model.ClassType;
import org.hl7.cql.model.DataType;
import org.hl7.cql.model.ListType;
import org.hl7.elm.r1.AggregateExpression;
import org.hl7.elm.r1.AliasedQuerySource;
import org.hl7.elm.r1.BinaryExpression;
import org.hl7.elm.r1.ByExpression;
import org.hl7.elm.r1.Case;
import org.hl7.elm.r1.CaseItem;
import org.hl7.elm.r1.CodeRef;
import org.hl7.elm.r1.CodeSystemRef;
import org.hl7.elm.r1.Combine;
import org.hl7.elm.r1.Expression;
import org.hl7.elm.r1.ExpressionDef;
import org.hl7.elm.r1.ExpressionRef;
import org.hl7.elm.r1.Filter;
import org.hl7.elm.r1.First;
import org.hl7.elm.r1.ForEach;
import org.hl7.elm.r1.FunctionDef;
import org.hl7.elm.r1.FunctionRef;
import org.hl7.elm.r1.If;
import org.hl7.elm.r1.InCodeSystem;
import org.hl7.elm.r1.InValueSet;
import org.hl7.elm.r1.IncludeDef;
import org.hl7.elm.r1.IndexOf;
import org.hl7.elm.r1.Instance;
import org.hl7.elm.r1.InstanceElement;
import org.hl7.elm.r1.Interval;
import org.hl7.elm.r1.Last;
import org.hl7.elm.r1.LetClause;
import org.hl7.elm.r1.Library;
import org.hl7.elm.r1.NaryExpression;
import org.hl7.elm.r1.OperandDef;
import org.hl7.elm.r1.ParameterDef;
import org.hl7.elm.r1.ParameterRef;
import org.hl7.elm.r1.PositionOf;
import org.hl7.elm.r1.Property;
import org.hl7.elm.r1.Query;
import org.hl7.elm.r1.RelationshipClause;
import org.hl7.elm.r1.Retrieve;
import org.hl7.elm.r1.Round;
import org.hl7.elm.r1.Sort;
import org.hl7.elm.r1.SortByItem;
import org.hl7.elm.r1.TernaryExpression;
import org.hl7.elm.r1.ToList;
import org.hl7.elm.r1.Tuple;
import org.hl7.elm.r1.TupleElement;
import org.hl7.elm.r1.UnaryExpression;
import org.hl7.elm.r1.ValueSetRef;
import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.elm_modelinfo.r1.ModelInfo;
import org.hl7.elm_modelinfo.r1.ProfileInfo;
import org.hl7.elm_modelinfo.r1.TypeInfo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CQLFilter {

	private Library library;

	private CQLModel cqlModel;

	private List<String> populationList;

	private List<String> usedExpressions;

	private List<String> usedFunctions;

	private List<String> usedValuesets;

	private List<String> usedParameters;

	private List<String> usedCodeSystems;

	private String currentDefinitionName = "";
	private String currentFunctionName = "";
	private String currentParameterName = "";

	private Map<String, List<String>> definitionToDefinitionMap = new HashMap<>();

	private Map<String, List<String>> definitionToFunctionMap = new HashMap<>();

	private Map<String, List<String>> functionToDefinitionMap = new HashMap<>();

	private Map<String, List<String>> functionToFunctionMap = new HashMap<>();

	private List<String> usedCodes;

	private Map<String, List<String>> valueSetDataTypeMap;

	private Map<String, List<String>> codeDataTypeMap;

	private Map<String, LibraryInfoHolder> includedLibraries;

	private LibraryInfoHolder currentLibraryHolder;

	private static Map<String, String> qdmTypeInfoMap = new HashMap<>();

	private CQLObject cqlObject = new CQLObject();

	private Map<String, LibraryHolder> libraryHolderMapping;

	public CQLFilter(Library library, List<String> populationList,
					 Map<String, LibraryHolder> map, CQLModel cqlModel) {

		this.libraryHolderMapping = map;
		this.library = library;
		this.cqlModel = cqlModel;
		this.currentLibraryHolder = new LibraryInfoHolder(this.library, "", "", "");
		this.populationList = populationList;
		this.usedExpressions = new ArrayList<>();
		this.usedFunctions = new ArrayList<>();
		this.usedValuesets = new ArrayList<>();
		this.usedParameters = new ArrayList<>();
		this.usedCodeSystems = new ArrayList<>();
		this.usedCodes = new ArrayList<>();
		this.includedLibraries = new HashMap<>();
		this.valueSetDataTypeMap = new HashMap<>();
		this.codeDataTypeMap = new HashMap<>();
	}

	/**
	 * Filters out the unused cql to
	 */
	public void filter() {

		// add all of the base populations into the used expressions list
		this.usedExpressions.addAll(this.populationList);

		for (String expressionName : this.populationList) {
			// assuming that definitions/functions attached to populations will
			// always come from the primary CQL library
			this.currentLibraryHolder = new LibraryInfoHolder(this.library, "", "", "");

			checkForUsedStatements(expressionName);
		}
	}

	/**
	 * This is similar to the filter() method except that it does not add
	 * populations List to used expressions. It can be used by CQL Workspace UI
	 * classes which need to find If a specific expression is being used.
	 */
	public void findUsedExpressions() {
		for (String expressionName : this.populationList) {
			// assuming that definitions/functions attached to populations will
			// always come from the primary CQL library
			this.currentLibraryHolder = new LibraryInfoHolder(this.library, "", "", "");

			checkForUsedStatements(expressionName);

			ExpressionDef expression = findExpressionByName(expressionName);
			if (expression != null) {
				createCQLExpressionObject(expression);
			}
		}
		collectUsed();
	}

	/**
	 * Combine all of the expressions. This is useful for packaging and export
	 * when we want all of the used object information together.
	 */
	private void collectUsed() {
		List<CQLExpressionObject> allExpressionList = cqlObject.getAllExpressionList();
		for (CQLExpressionObject expressionObject : allExpressionList) {
			this.getUsedCodes().addAll(expressionObject.getUsedCodes());
			this.getUsedCodeSystems().addAll(expressionObject.getUsedCodeSystems());
			this.getUsedExpressions().addAll(expressionObject.getUsedExpressions());
			this.getUsedFunctions().addAll(expressionObject.getUsedFunctions());
			this.getUsedParameters().addAll(expressionObject.getUsedParameters());
			this.getUsedValuesets().addAll(expressionObject.getUsedValuesets());
			CQLExpressionObject.mergeValueSetMap(this.getValueSetDataTypeMap(),
					expressionObject.getValueSetDataTypeMap());
			CQLExpressionObject.mergeCodeMap(this.getCodeDataTypeMap(), expressionObject.getCodeDataTypeMap());
		}
	}

	/**
	 * Entry point for getting used statements. This takes in an expression
	 * name, which is passed in to the Filter class. This name should be
	 * associated with a population. Then, it finds the expression object
	 * associated with that name, and then checks for the used statements on
	 * that object.
	 * 
	 * @param expressionName
	 *            the expression name
	 */
	private void checkForUsedStatements(String expressionName) {
		ExpressionDef expression = findExpressionByName(expressionName);

		if (expression == null || expression.getExpression() == null) {
			return;
		}

		String expressionNameWithLib = expressionName;
		if (this.currentLibraryHolder != null && this.currentLibraryHolder.getLibraryAlias().length() > 0) {
			expressionNameWithLib = this.currentLibraryHolder.getLibraryName() + "-"
					+ this.currentLibraryHolder.getLibraryVersion() + "|" + this.currentLibraryHolder.getLibraryAlias()
					+ "|" + expressionName;
		}

		if (expression.getClass().equals(FunctionDef.class)) {
			this.currentFunctionName = expressionNameWithLib;
			this.currentDefinitionName = "";
			this.currentParameterName = "";
		} else if (expression.getClass().equals(ParameterDef.class)) {
			this.currentParameterName = expressionNameWithLib;
			this.currentFunctionName = "";
			this.currentDefinitionName = "";
		} else if (expression.getClass().equals(ExpressionDef.class)) {
			this.currentDefinitionName = expressionNameWithLib;
			this.currentParameterName = "";
			this.currentFunctionName = "";
		}

		checkForUsedStatements(expression.getExpression());
	}

	private void createCQLExpressionObject(ExpressionDef expression) {

		if (expression.getClass().equals(ExpressionDef.class)) {
			CQLExpressionObject definitionObject = new CQLExpressionObject("Definition", expression.getName());
			definitionObject
					.setReturnType((expression.getResultType() == null) ? "" : expression.getResultType().toString());
			definitionObject.getUsedCodes().addAll(this.getUsedCodes());
			definitionObject.getUsedCodeSystems().addAll(this.getUsedCodeSystems());
			definitionObject.getUsedExpressions().addAll(this.getUsedExpressions());
			definitionObject.getUsedFunctions().addAll(this.getUsedFunctions());
			definitionObject.getUsedParameters().addAll(this.getUsedParameters());
			definitionObject.getUsedValuesets().addAll(this.getUsedValuesets());
			definitionObject.getValueSetDataTypeMap().putAll(this.getValueSetDataTypeMap());
			definitionObject.getCodeDataTypeMap().putAll(this.getCodeDataTypeMap());
			cqlObject.getCqlDefinitionObjectList().add(definitionObject);

			clearUsed();
		}

		else if (expression.getClass().equals(FunctionDef.class)) {
			CQLExpressionObject funcObject = new CQLExpressionObject("Function", expression.getName());

			FunctionDef functionRef = (FunctionDef) expression;
			// Populate Function Argument name and return type.
			for (OperandDef expressionDef : functionRef.getOperand()) {
				CQLExpressionOprandObject cqlExpressionOprandObject = new CQLExpressionOprandObject();
				cqlExpressionOprandObject.setName(expressionDef.getName());
				cqlExpressionOprandObject.setReturnType(expressionDef.getResultType().toString());
				funcObject.getOprandList().add(cqlExpressionOprandObject);
			}

			funcObject.setReturnType((expression.getResultType() == null) ? "" : expression.getResultType().toString());
			funcObject.getUsedCodes().addAll(this.getUsedCodes());
			funcObject.getUsedCodeSystems().addAll(this.getUsedCodeSystems());
			funcObject.getUsedExpressions().addAll(this.getUsedExpressions());
			funcObject.getUsedFunctions().addAll(this.getUsedFunctions());
			funcObject.getUsedParameters().addAll(this.getUsedParameters());
			funcObject.getUsedValuesets().addAll(this.getUsedValuesets());
			funcObject.getValueSetDataTypeMap().putAll(this.getValueSetDataTypeMap());
			funcObject.getCodeDataTypeMap().putAll(this.getCodeDataTypeMap());

			cqlObject.getCqlFunctionObjectList().add(funcObject);

			clearUsed();
		}

		// check for parameters
		else if (expression.getClass().equals(ParameterDef.class)) {
			CQLExpressionObject paramObject = new CQLExpressionObject("Parameter", expression.getName());
			paramObject.setReturnType((expression.getResultType() == null) ? "" : expression.getResultType().toString());
			paramObject.getUsedCodes().addAll(this.getUsedCodes());
			paramObject.getUsedCodeSystems().addAll(this.getUsedCodeSystems());
			paramObject.getUsedExpressions().addAll(this.getUsedExpressions());
			paramObject.getUsedFunctions().addAll(this.getUsedFunctions());
			paramObject.getUsedParameters().addAll(this.getUsedParameters());
			paramObject.getUsedValuesets().addAll(this.getUsedValuesets());
			paramObject.getValueSetDataTypeMap().putAll(this.getValueSetDataTypeMap());
			paramObject.getCodeDataTypeMap().putAll(this.getCodeDataTypeMap());

			cqlObject.getCqlParameterObjectList().add(paramObject);

			clearUsed();
		}
	}

	private void clearUsed() {
		this.getUsedCodes().clear();
		this.getUsedCodeSystems().clear();
		this.getUsedExpressions().clear();
		this.getUsedFunctions().clear();
		this.getUsedParameters().clear();
		this.getUsedValuesets().clear();
		this.getValueSetDataTypeMap().clear();
		this.getCodeDataTypeMap().clear();
	}

	/**
	 * Checks for the used statements for the given expression.
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForUsedStatements(Expression expression) {

		// TODO Determine if there is any unecessary checks below
		// TODO Determine if there is any missing checks below
		// TODO Testing to make sure it is completely functioning

		if (expression == null) {
			return;
		}
		
		// check for parameter, definition, function, valueset references,
		// codesystems //
		// check for expression references
		if (expression.getClass().equals(ExpressionRef.class)) {
			checkforExpressionRef(expression);
		}

		// check for function references
		else if (expression.getClass().equals(FunctionRef.class)) {
			checkForFunctionRef(expression);
		}

		// check for parameters
		else if (expression.getClass().equals(ParameterRef.class)) {
			checkForParameterRef(expression);
		}

		// check for in value sets
		else if (expression.getClass().equals(InValueSet.class)) {
			checkForInValuesets(expression);
		}

		// check for valueset references
		else if (expression.getClass().equals(ValueSetRef.class)) {
			checkForValuesetRef(expression);
		}

		// check for in code systems
		else if (expression.getClass().equals(InCodeSystem.class)) {
			checkForInCodeSystem(expression);
		}

		// check for code system ref
		else if (expression.getClass().equals(CodeSystemRef.class)) {
			checkForCodeSystemRef(expression);
		}

		else if (expression.getClass().equals(CodeRef.class)) {
			checkForCodeRef(expression);
		}

		// check for property references
		else if (expression.getClass().equals(Property.class)) {
			checkForPropertyReference(expression);
		}

		// check for **nary expressions //
		// check for unary expressions
		else if (expression.getClass().getSuperclass().equals(UnaryExpression.class)) {
			checkForUnaryExpression(expression);
		}

		// check for binary expressions
		else if (expression.getClass().getSuperclass().equals(BinaryExpression.class)) {
			checkForBinaryExpression(expression);
		}

		// check for ternary expressions
		else if (expression.getClass().getSuperclass().equals(TernaryExpression.class)) {
			checkForTernaryExpression(expression);
		}

		// check for nary expressions
		else if (expression.getClass().getSuperclass().equals(NaryExpression.class)) {
			checkForNaryExpression(expression);
		}

		// check for aggregate expression
		else if (expression.getClass().getSuperclass().equals(AggregateExpression.class)) {
			checkForAggregateExpression(expression);
		}

		// check for case expressions
		else if (expression.getClass().equals(Case.class)) {
			checkForCaseExpression(expression);
		}

		// check for combine expressions
		else if (expression.getClass().equals(Combine.class)) {
			checkForCombineExpression(expression);
		}

		// check for filter expressions
		else if (expression.getClass().equals(Filter.class)) {
			checkForFilterExpression(expression);
		}

		// check for first expressions
		else if (expression.getClass().equals(First.class)) {
			checkForFirstExpression(expression);
		}

		// check for each expressions
		else if (expression.getClass().equals(ForEach.class)) {
			checkForEachExpression(expression);
		}

		// check for if expressions
		else if (expression.getClass().equals(If.class)) {
			checkForIfExpression(expression);
		}

		// check for index of expression
		else if (expression.getClass().equals(IndexOf.class)) {
			checkForIndexOfExpression(expression);
		}

		// check for instance expression
		else if (expression.getClass().equals(Instance.class)) {
			checkForInstanceExpression(expression);
		}

		// check for interval expression
		else if (expression.getClass().equals(Interval.class)) {
			checkForIntervalExpression(expression);
		}

		// check for last expression
		else if (expression.getClass().equals(Last.class)) {
			checkForLastExpression(expression);
		}

		// check for list expression
		else if (expression.getClass().equals(org.hl7.elm.r1.List.class)) {
			checkForList(expression);
		}

		// check for position of expression
		else if (expression.getClass().equals(PositionOf.class)) {
			checkForPositionOf(expression);
		}

		// check for round expression
		else if (expression.getClass().equals(Round.class)) {
			checkForRoundExpression(expression);
		}

		// check for sort expression
		else if (expression.getClass().equals(Sort.class)) {
			checkForSortExpression(expression);
		}

		// check for tuple expressions
		else if (expression.getClass().equals(Tuple.class)) {
			checkForTupleExpression(expression);
		}

		// check for retrieve expressions
		else if (expression.getClass().equals(Retrieve.class)) {
			checkForRetrieveExpression(expression);
		}

		// check for query expressions
		else if (expression.getClass().equals(Query.class)) {
			checkForQueryExpression(expression);
		}

		else {
			System.err.println("Not Found: " + expression.getClass());
		}
	}
	
	/* check for expression, parameter, valueset references */
	/**
	 * Checks for an expression references. If there is an expression
	 * references, recursively call checkForUsedStatements on the expression so
	 * we can find any expressions inside it. Also, add the used expression to
	 * the used expression list.
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkforExpressionRef(Expression expression) {
		ExpressionRef expressionRef = (ExpressionRef) expression;

		// since we found an expression ref, we want to get the details of it.
		// so we will recursively call get used statements on the expression ref
		String includedLibraryAlias = expressionRef.getLibraryName();
		// System.out.println("Expression name:"+expressionRef.getName());
		// System.out.println("Included library:"+includedLibraryAlias+":");

		String expressionName = expressionRef.getName();

		LibraryInfoHolder existingLibrary = null;
		String finalExpressionName = expressionName;

		if (includedLibraryAlias != null) {
			LibraryInfoHolder includedLibrary = getIncludedLibrary(includedLibraryAlias);
			// System.out.println(this.includedLibraries);
			includedLibraryAlias = includedLibrary.getLibraryName() + "-" + includedLibrary.getLibraryVersion() + "|"
					+ includedLibraryAlias;
			existingLibrary = this.currentLibraryHolder;
			this.currentLibraryHolder = includedLibrary;
			finalExpressionName = includedLibraryAlias + "|" + expressionRef.getName();
			// this.addUsedExpression(includedLibraryAlias + "|" +
			// expressionRef.getName());
		} else {
			if (this.currentLibraryHolder.getLibraryAlias().length() > 0) {

				finalExpressionName = this.currentLibraryHolder.getLibraryName() + "-"
						+ this.currentLibraryHolder.getLibraryVersion() + "|"
						+ this.currentLibraryHolder.getLibraryAlias() + "|" + expressionRef.getName();

				// this.addUsedExpression(this.currentLibraryHolder.getLibraryName()
				// + "-" + this.currentLibraryHolder.getLibraryVersion() + "|"
				// + this.currentLibraryHolder.getLibraryAlias() + "|" +
				// expressionRef.getName());
			} else {
				finalExpressionName = expressionRef.getName();
				// this.addUsedExpression(expressionRef.getName());
			}
		}

		this.addUsedExpression(finalExpressionName);

		if (!expressionName.equals("Patient")) {
			// addToDirectRefDefMap(this.getUsedExpressions().get(this.getUsedExpressions().size()
			// - 1));
			addToDirectRefDefMap(finalExpressionName);
		}
		String tempCurrentDef = this.currentDefinitionName;
		String tempCurrentFunc = this.currentFunctionName;
		String tempCurrentParam = this.currentParameterName;

		checkForUsedStatements(expressionName);

		this.currentDefinitionName = tempCurrentDef;
		this.currentFunctionName = tempCurrentFunc;
		this.currentParameterName = tempCurrentParam;

		if (existingLibrary != null) {
			this.currentLibraryHolder = existingLibrary;
		}
	}

	private void addToDirectRefDefMap(String expressionName) {

		if (this.currentDefinitionName.length() > 0) {

			if (this.definitionToDefinitionMap.get(this.currentDefinitionName) == null) {
				this.definitionToDefinitionMap.put(this.currentDefinitionName, new ArrayList<String>());
			}

			if (!this.definitionToDefinitionMap.get(this.currentDefinitionName).contains(expressionName)) {
				this.definitionToDefinitionMap.get(this.currentDefinitionName).add(expressionName);
			}

		} else if (this.currentFunctionName.length() > 0) {

			if (this.functionToDefinitionMap.get(this.currentFunctionName) == null) {
				this.functionToDefinitionMap.put(this.currentFunctionName, new ArrayList<String>());
			}

			if (!this.functionToDefinitionMap.get(this.currentFunctionName).contains(expressionName)) {
				this.functionToDefinitionMap.get(this.currentFunctionName).add(expressionName);
			}

		}

	}

	private void checkForFunctionRef(Expression expression) {
		FunctionRef functionRef = (FunctionRef) expression;

		// since we found a function ref, we want to get the details of it.
		// so we will recursively call get used statements on the function ref.
		String includedLibraryAlias = functionRef.getLibraryName();
		// System.out.println("Included library:"+includedLibraryAlias+":");

		String expressionName = functionRef.getName();

		LibraryInfoHolder existingLibrary = null;
		String finalExpressionName = expressionName;

		if (includedLibraryAlias != null) {
			LibraryInfoHolder includedLibrary = getIncludedLibrary(includedLibraryAlias);
			includedLibraryAlias = includedLibrary.getLibraryName() + "-" + includedLibrary.getLibraryVersion() + "|"
					+ includedLibraryAlias;
			existingLibrary = this.currentLibraryHolder;
			this.currentLibraryHolder = includedLibrary;
			finalExpressionName = includedLibraryAlias + "|" + functionRef.getName();
		} else {
			if (this.currentLibraryHolder.getLibraryAlias().length() > 0) {
				finalExpressionName = this.currentLibraryHolder.getLibraryName() + "-"
						+ this.currentLibraryHolder.getLibraryVersion() + "|" + this.currentLibraryHolder.libraryAlias
						+ "|" + functionRef.getName();
			} else {
				finalExpressionName = functionRef.getName();
			}
		}
		this.addUsedFunction(finalExpressionName);
		// addToDirectRefFuncMap(this.getUsedFunctions().get(this.getUsedFunctions().size()
		// - 1));
		addToDirectRefFuncMap(finalExpressionName);

		String tempCurrentDef = this.currentDefinitionName;
		String tempCurrentFunc = this.currentFunctionName;
		String tempCurrentParam = this.currentParameterName;
		
		for(Expression operand : functionRef.getOperand()) {
			checkForUsedStatements(operand);
		}

		checkForUsedStatements(expressionName);

		this.currentDefinitionName = tempCurrentDef;
		this.currentFunctionName = tempCurrentFunc;
		this.currentParameterName = tempCurrentParam;

		if (existingLibrary != null) {
			this.currentLibraryHolder = existingLibrary;
		}
	}

	private void addToDirectRefFuncMap(String expressionName) {

		if (this.currentDefinitionName.length() > 0) {

			if (this.definitionToFunctionMap.get(this.currentDefinitionName) == null) {
				this.definitionToFunctionMap.put(this.currentDefinitionName, new ArrayList<String>());
			}

			if (!this.definitionToFunctionMap.get(this.currentDefinitionName).contains(expressionName)) {
				this.definitionToFunctionMap.get(this.currentDefinitionName).add(expressionName);
			}

		} else if (this.currentFunctionName.length() > 0) {

			if (this.functionToFunctionMap.get(this.currentFunctionName) == null) {
				this.functionToFunctionMap.put(this.currentFunctionName, new ArrayList<String>());
			}

			if (!this.functionToFunctionMap.get(this.currentFunctionName).contains(expressionName)) {
				this.functionToFunctionMap.get(this.currentFunctionName).add(expressionName);
			}

		}

	}

	/**
	 * Check for parameter references and add it to the used parameter reference
	 * list
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForParameterRef(Expression expression) {
		ParameterRef parameterRef = (ParameterRef) expression;
		String name = parameterRef.getName();
		String libraryAlias = parameterRef.getLibraryName();

		if (libraryAlias != null) {
			LibraryInfoHolder libHolder = getIncludedLibrary(libraryAlias);
			name = libHolder.getLibraryName() + "-" + libHolder.getLibraryVersion() + "|" + libraryAlias + "|" + name;
		} else if (this.currentLibraryHolder.getLibraryAlias().length() > 0) {
			// name = this.currentLibraryHolder.getLibraryAlias() + "." + name;
			name = this.currentLibraryHolder.getLibraryName() + "-" + this.currentLibraryHolder.getLibraryVersion()
					+ "|" + this.currentLibraryHolder.getLibraryAlias() + "|" + name;
		}

		this.addUsedParameter(name);
	}

	/**
	 * Check for in valueset references. It gets the in value set reference,
	 * then gets the valueset its references, then gets the name and adds it to
	 * the used valueset list.
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForInValuesets(Expression expression) {
		InValueSet inValueSet = (InValueSet) expression;
//		String name = inValueSet.getValueset().getName();

//		String libraryAlias = inValueSet.getValueset().getLibraryName();
        String libraryAlias = " ";
        String name = " ";
		if (libraryAlias != null) {
			LibraryInfoHolder libHolder = getIncludedLibrary(libraryAlias);
			name = libHolder.getLibraryName() + "-" + libHolder.getLibraryVersion() + "|" + libraryAlias + "|" + name;
		} else if (this.currentLibraryHolder.getLibraryAlias().length() > 0) {
			name = this.currentLibraryHolder.getLibraryName() + "-" + this.currentLibraryHolder.getLibraryVersion()
					+ "|" + this.currentLibraryHolder.getLibraryAlias() + "|" + name;
		}
		this.addUsedValueset(name);
	}

	/**
	 * Check for valueset references. Adds the valueset reference to the used
	 * valueset list.
	 * 
	 * @param expression
	 */
	private void checkForValuesetRef(Expression expression) {
		ValueSetRef valueSetRef = (ValueSetRef) expression;
		String name = valueSetRef.getName();

		String libraryAlias = valueSetRef.getLibraryName();

		if (libraryAlias != null) {
			LibraryInfoHolder libHolder = getIncludedLibrary(libraryAlias);
			name = libHolder.getLibraryName() + "-" + libHolder.getLibraryVersion() + "|" + libraryAlias + "|" + name;
		} else if (this.currentLibraryHolder.getLibraryAlias().length() > 0) {
			name = this.currentLibraryHolder.getLibraryName() + "-" + this.currentLibraryHolder.getLibraryVersion()
					+ "|" + this.currentLibraryHolder.getLibraryAlias() + "|" + name;
		}

		this.addUsedValueset(name);
	}

	/**
	 * Checks for code system reference. Adds the codesystem to the used
	 * codesystem list
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForCodeSystemRef(Expression expression) {
		CodeSystemRef codeSystemRef = (CodeSystemRef) expression;
		// System.out.println("\t" + codeSystemRef.getName());
		this.addUsedCodeSystem(codeSystemRef.getName());
	}

	private void checkForCodeRef(Expression expression) {
		CodeRef codeRef = (CodeRef) expression;
		// System.out.println("\t" + codeRef.getName());
		String name = codeRef.getName();

		String libraryAlias = codeRef.getLibraryName();

		if (libraryAlias != null) {
			LibraryInfoHolder libHolder = getIncludedLibrary(libraryAlias);
			name = libHolder.getLibraryName() + "-" + libHolder.getLibraryVersion() + "|" + libraryAlias + "|" + name;
		} else if (this.currentLibraryHolder.getLibraryAlias().length() > 0) {
			// name = this.currentLibraryHolder.getLibraryAlias() + "." + name;
			name = this.currentLibraryHolder.getLibraryName() + "-" + this.currentLibraryHolder.getLibraryVersion()
					+ "|" + this.currentLibraryHolder.getLibraryAlias() + "|" + name;
		}

		this.addUsedCode(name);
	}

	/**
	 * Check for in code sytem. Gets the codesystem, then adds the name to the
	 * used codesystem list.
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForInCodeSystem(Expression expression) {
		InCodeSystem inCodeSystem = (InCodeSystem) expression;
		
//		String name = inCodeSystem.getCodesystem().getName();
		String name = " ";

		if (this.currentLibraryHolder.getLibraryAlias().length() > 0) {
			name =  this.currentLibraryHolder.getLibraryName() + "-" + this.currentLibraryHolder.getLibraryVersion() + 
					"|" + this.currentLibraryHolder.getLibraryAlias() + "|"
					+ name;
		}
		this.addUsedCodeSystem(name);
	}

	/**
	 * Check for property reference
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForPropertyReference(Expression expression) {
		Property property = (Property) expression;	
		checkForUsedStatements(property.getSource());
	}

	/* check **nary expressions */
	/**
	 * Check unary expressions
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForUnaryExpression(Expression expression) {
		UnaryExpression unaryExpression = (UnaryExpression) expression;
		checkForUsedStatements(unaryExpression.getOperand());
	}

	/**
	 * Check binrary expressions
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForBinaryExpression(Expression expression) {
		BinaryExpression binaryExpression = (BinaryExpression) expression;
		List<Expression> operands = binaryExpression.getOperand();
		for (Expression e : operands) {
			checkForUsedStatements(e);
		}
	}

	/**
	 * Check ternary expressions
	 * 
	 * @param expression
	 */
	private void checkForTernaryExpression(Expression expression) {
		TernaryExpression ternaryExpression = (TernaryExpression) expression;
		List<Expression> operands = ternaryExpression.getOperand();
		for (Expression e : operands) {
			checkForUsedStatements(e);
		}
	}

	/**
	 * Check for nary expressions
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForNaryExpression(Expression expression) {
		NaryExpression naryExpression = (NaryExpression) expression;
		List<Expression> operands = naryExpression.getOperand();
		for (Expression e : operands) {
			checkForUsedStatements(e);
		}
	}

	/**
	 * Check for aggregate expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForAggregateExpression(Expression expression) {
		AggregateExpression aggregateExpression = (AggregateExpression) expression;
		checkForUsedStatements(aggregateExpression.getSource());
	}

	/**
	 * Check for case expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForCaseExpression(Expression expression) {
		Case caseExpression = (Case) expression;

		checkForUsedStatements(caseExpression.getComparand());
		checkForUsedStatements(caseExpression.getElse());

		List<CaseItem> caseItems = caseExpression.getCaseItem();
		for (CaseItem item : caseItems) {
			checkForUsedStatements(item.getThen());
			checkForUsedStatements(item.getWhen());
		}
	}

	/**
	 * Check for combine expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForCombineExpression(Expression expression) {
		Combine combine = (Combine) expression;
		checkForUsedStatements(combine.getSource());
		checkForUsedStatements(combine.getSeparator());
	}

	/**
	 * Check for filter expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForFilterExpression(Expression expression) {
		Filter filter = (Filter) expression;
		checkForUsedStatements(filter.getSource());
		checkForUsedStatements(filter.getCondition());
	}

	/**
	 * Checks for first expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForFirstExpression(Expression expression) {
		First first = (First) expression;
		checkForUsedStatements(first.getSource());
	}

	/**
	 * Check for each expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForEachExpression(Expression expression) {
		ForEach forEach = (ForEach) expression;
		checkForUsedStatements(forEach.getSource());
		checkForUsedStatements(forEach.getElement());
	}

	/**
	 * Check for if expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForIfExpression(Expression expression) {
		If ifExpression = (If) expression;
		checkForUsedStatements(ifExpression.getCondition());
		checkForUsedStatements(ifExpression.getElse());
		checkForUsedStatements(ifExpression.getThen());
	}

	/**
	 * Check for index of expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForIndexOfExpression(Expression expression) {
		IndexOf indexOf = (IndexOf) expression;
		checkForUsedStatements(indexOf.getSource());
		checkForUsedStatements(indexOf.getElement());
	}

	/**
	 * Check for instance expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForInstanceExpression(Expression expression) {
		Instance instance = (Instance) expression;
		List<InstanceElement> instanceElements = instance.getElement();
		for (InstanceElement element : instanceElements) {
			checkForUsedStatements(element.getValue());
		}
	}

	/**
	 * Check for interval expressions
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForIntervalExpression(Expression expression) {
		Interval interval = (Interval) expression;
		checkForUsedStatements(interval.getHighClosedExpression());
		checkForUsedStatements(interval.getHigh());
		checkForUsedStatements(interval.getLow());
		checkForUsedStatements(interval.getLowClosedExpression());
	}

	/**
	 * Checks for last expression
	 * 
	 * @param expression
	 *            the xpression
	 */
	private void checkForLastExpression(Expression expression) {
		Last last = (Last) expression;
		checkForUsedStatements(last.getSource());
	}

	/**
	 * Check for lists
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForList(Expression expression) {
		org.hl7.elm.r1.List list = (org.hl7.elm.r1.List) expression;
		List<Expression> expressions = list.getElement();
		for (Expression e : expressions) {
			checkForUsedStatements(e);
		}
	}

	/**
	 * Check for position of expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForPositionOf(Expression expression) {
		PositionOf positionOf = (PositionOf) expression;
		checkForUsedStatements(positionOf.getPattern());
		checkForUsedStatements(positionOf.getString());
	}

	/**
	 * Check for round expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForRoundExpression(Expression expression) {
		Round round = (Round) expression;
		checkForUsedStatements(round.getOperand());
		checkForUsedStatements(round.getPrecision());
	}

	/**
	 * Check for sort expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForSortExpression(Expression expression) {
		Sort sort = (Sort) expression;
		
		// sort clauses has sort by items. These items can contain expressions
		// only if the item is ByExpression. The following loops through the 
		// sort by items, check if it is ByExpression and if it is, checks for 
		// used statements
		List<SortByItem> items = sort.getBy();
		for(SortByItem item : items) {
			if(item.getClass().equals(ByExpression.class)) {
				ByExpression byExpression = (ByExpression) item; 
				checkForUsedStatements(byExpression.getExpression());
			}
		}
		
		checkForUsedStatements(sort.getSource());
	}

	/**
	 * Check for tuple expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForTupleExpression(Expression expression) {
		Tuple tuple = (Tuple) expression;
		List<TupleElement> tupleElements = tuple.getElement();
		for (TupleElement element : tupleElements) {
			checkForUsedStatements(element.getValue());
		}
	}

	/**
	 * Check for retrieve expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForRetrieveExpression(Expression expression) {
		Retrieve retrieve = (Retrieve) expression;
		checkForUsedStatements(retrieve.getCodes());
		saveValuesetOrCode(retrieve);
		checkForUsedStatements(retrieve.getDateRange());
	}

	private void saveValuesetOrCode(Retrieve retrieve) {
		Expression expr = retrieve.getCodes();
		if (expr == null) {
			return;
		}

		if (expr instanceof ToList) {
			ToList listOfCodes = (ToList) retrieve.getCodes();
			
			if(listOfCodes.getOperand() instanceof CodeRef) {
				CodeRef codeRef = (CodeRef) listOfCodes.getOperand();
				saveCode(codeRef, retrieve);
			}
		} else if (expr instanceof ValueSetRef) {
			ValueSetRef valueSetRef = (ValueSetRef) retrieve.getCodes();
			saveValueSet(valueSetRef, retrieve);
		}
	}

	/**
	 * Save the valueset
	 * 
	 * @param valueset
	 *            the valuset to save
	 * @param retrieve
	 *            the associated retrieve
	 */
	private void saveValueSet(ValueSetRef valueset, Retrieve retrieve) {
		String valueSetName = valueset.getName();
		String libraryAlias = valueset.getLibraryName();
		valueSetName = createLongName(valueSetName, libraryAlias);
		
		String dataTypeName = findDataType(retrieve);

		if (dataTypeName != null && dataTypeName.length() > 0) {
			List<String> dataTypeList = this.valueSetDataTypeMap.get(valueSetName);
			if (dataTypeList == null) {
				dataTypeList = new ArrayList<String>();
				this.valueSetDataTypeMap.put(valueSetName, dataTypeList);
			}

			if (!dataTypeList.contains(dataTypeName)) {
				dataTypeList.add(dataTypeName);
			}
		}
	}

	/**
	 * save the code to the code/datatype map
	 * 
	 * @param code
	 *            the code to save
	 * @param retrieve
	 *            the associated retrieve
	 */
	private void saveCode(CodeRef code, Retrieve retrieve) {
		String codeName = code.getName();
		String libraryAlias = code.getLibraryName();
		codeName = createLongName(codeName, libraryAlias);

		String dataTypeName = findDataType(retrieve);

		if (dataTypeName != null && dataTypeName.length() > 0) {
			List<String> dataTypeList = this.codeDataTypeMap.get(codeName);
			if (dataTypeList == null) {
				dataTypeList = new ArrayList<String>();
				this.codeDataTypeMap.put(codeName, dataTypeList);
			}

			if (!dataTypeList.contains(dataTypeName)) {
				dataTypeList.add(dataTypeName);
			}
		}

	}

	/**
	 * Finds the datatype associated with the retieve
	 * 
	 * @param retrieve
	 *            the retrieve to find the datatype for
	 * @return the data type name
	 */
	private String findDataType(Retrieve retrieve) {
		String dataTypeTemplateId = retrieve.getTemplateId();
		String dataTypeName = "";

		if (dataTypeTemplateId != null) {
			dataTypeName = getDataTypeName(dataTypeTemplateId);
		} else {
			// If you can find the templateId for the datatype for the retrieve,
			// try this.
			DataType dataType = retrieve.getResultType();
			if (dataType instanceof ListType) {
				ListType listType = (ListType) dataType;
				if (listType.getElementType() instanceof ClassType) {
					dataTypeName = ((ClassType) listType.getElementType()).getLabel();
				}
			}
		}

		return dataTypeName;
	}

	/**
	 * Creates a long name for the valueset/code.
	 * 
	 * @param name
	 *            the name of the code or valueset
	 * @param libraryAlias
	 *            the library alias
	 * @return a string in the format of AliasName-vx.x.x|name
	 */
	private String createLongName(String name, String libraryAlias) {
		String longName = name;

		if (libraryAlias != null) {
			LibraryInfoHolder libHolder = getIncludedLibrary(libraryAlias);
			longName = libHolder.getLibraryName() + "-" + libHolder.getLibraryVersion() + "|" + libraryAlias + "|"
					+ name;
		} else if (this.currentLibraryHolder.getLibraryAlias().length() > 0) {
			longName = this.currentLibraryHolder.getLibraryName() + "-" + this.currentLibraryHolder.getLibraryVersion()
					+ "|" + this.currentLibraryHolder.getLibraryAlias() + "|" + name;
		}

		return longName;
	}

	/**
	 * Check for query expression
	 * 
	 * @param expression
	 *            the expression
	 */
	private void checkForQueryExpression(Expression expression) {
		Query query = (Query) expression;
		List<AliasedQuerySource> sources = query.getSource();
		for (AliasedQuerySource source : sources) {
			Expression sourceEpxression = source.getExpression();
			checkForUsedStatements(sourceEpxression);
		}
		
		List<RelationshipClause> relationships = query.getRelationship();
		for (RelationshipClause relationship : relationships) {

			Expression relationshipExpression = relationship.getExpression();
			checkForUsedStatements(relationshipExpression);

			Expression suchThatExpression = relationship.getSuchThat();
			checkForUsedStatements(suchThatExpression);
		}

		List<LetClause> lets = query.getLet();
		for (LetClause letClause : lets) {
			Expression letExpression = letClause.getExpression();
			checkForUsedStatements(letExpression);
		}
		
		// in a sort, you can actually sort by expressions, below handles
		// that situation. it loops through the sort by items, checks
		// if its ByExprsesion, and if it is, checks for used statements in it.
		//List<SortByItem> items = query.getSort().getBy();
		//List<SortByItem> items = query.getSort();
		if(query.getSort() != null){
			List<SortByItem> items = query.getSort().getBy();
			for(SortByItem item : items) {
				if(item.getClass().equals(ByExpression.class)) {
					ByExpression byExpression = (ByExpression) item; 
					checkForUsedStatements(byExpression.getExpression());
				}
			}
		}
		Expression whereExpression = query.getWhere();
		checkForUsedStatements(whereExpression);
	}

	/**
	 * Finds the expression by name
	 * 
	 * @param expressionName
	 *            the expression name
	 * @return the expression assoicated with the given name, null if none are
	 *         found
	 */
	private ExpressionDef findExpressionByName(String expressionName) {

		ExpressionDef expression = findExpression(expressionName, this.currentLibraryHolder);

		// String s =
		// this.library.getIncludes().getDef().get(0).getLocalIdentifier();
		// System.out.println("sssssssss:"+s);
		// this.library.getIncludes().getDef().get(0).

		return expression;

	}

	public ExpressionDef findExpression(String expressionName, LibraryInfoHolder libraryHolder) {
		if (libraryHolder.getLibrary() != null) {
			List<ExpressionDef> expressionDefs = new ArrayList<>(); 
			if(libraryHolder.getLibrary().getStatements() != null) {
				expressionDefs = libraryHolder.getLibrary().getStatements().getDef(); 
			}
			
			for (ExpressionDef expressionDef : expressionDefs) {
				if (expressionDef.getName().equals(expressionName)) {
					return expressionDef;
				}
			}
		}
		return null;
	}

	/**
	 * Finds the included library via the alias name. This is needed when we
	 * come across a definition that is not a part of the parent library. We
	 * need to go into the child and start finding used definitions there.
	 * 
	 * @param libraryAliasName
	 *            the library alias name.
	 * @return the included library associated to the alias name.
	 */
	private LibraryInfoHolder getIncludedLibrary(String libraryAliasName) {

		LibraryInfoHolder includedLibrary = null;

		// loop through the included definitions of the current library, until
		// we find one matches the alias name that was passed in. We need to do
		// this because we have no
		// method of knowing which library the alias is associated with.
		// TODO: Find a way to associate library aliases with libraries before
		// cql filter
		List<IncludeDef> includeDefs = this.currentLibraryHolder.getLibrary().getIncludes().getDef();
		for (IncludeDef includeDef : includeDefs) {
			if (includeDef.getLocalIdentifier().equals(libraryAliasName)) {
				includedLibrary = this.includedLibraries
						.get(includeDef.getPath() + "-" + includeDef.getVersion() + "|" + libraryAliasName);

				// if the included library is not already loaded, try finding it
				// from the library mapping that
				// was passed into the parser. The parser passed us the
				// LibraryHolder object, so we should find
				// that object and then convert it to a LibraryInfoHolder
				// object, finally update the include mapping
				if (includedLibrary == null) {

					String libraryPathName = includeDef.getPath() + "-" + includeDef.getVersion();

					// get library holder object passed in by the parser
					LibraryHolder libraryHolder = this.libraryHolderMapping.get(libraryPathName);

					// convert library holder to library info holder
					includedLibrary = new LibraryInfoHolder(libraryHolder.getLibrary(), libraryAliasName,
							libraryHolder.getLibrary().getIdentifier().getId(),
							libraryHolder.getLibrary().getIdentifier().getVersion());

					// update the included mapping.
					CQLIncludeLibrary cqlIncludeLibrary = this.cqlModel.getIncludedCQLLibXMLMap()
							.get(includeDef.getPath() + "-" + includeDef.getVersion() + "|" + libraryAliasName)
							.getCqlLibrary();
					includedLibrary.setCqlIncludeLibraryObject(cqlIncludeLibrary);
					this.includedLibraries.put(libraryPathName + "|" + libraryAliasName, includedLibrary);
				}

				break;
			}

		}

		return includedLibrary;

	}

	/**
	 * Add a used expression to the used expression list
	 * 
	 * @param expressionName
	 *            the expression name to add
	 */
	public void addUsedExpression(String expressionName) {
		if (!this.usedExpressions.contains(expressionName) && !expressionName.equals("Patient")) {
			this.usedExpressions.add(expressionName);
		}
	}

	/**
	 * Add a used function to the used function list
	 * 
	 * @param functionName
	 *            the function name to add
	 */
	public void addUsedFunction(String functionName) {
		if (!this.usedFunctions.contains(functionName)) {
			this.usedFunctions.add(functionName);
		}
	}

	/**
	 * Add a used valueset to the used valueset list
	 * 
	 * @param valuesetName
	 */
	public void addUsedValueset(String valuesetName) {
		if (!this.usedValuesets.contains(valuesetName)) {
			this.usedValuesets.add(valuesetName);
		}
	}

	/**
	 * Add a used parameter to the used parameter list
	 * 
	 * @param parameterName
	 *            the parameter name to add
	 */
	public void addUsedParameter(String parameterName) {
		if (!this.usedParameters.contains(parameterName)) {
			this.usedParameters.add(parameterName);
		}
	}

	/**
	 * Add a used codesystem to the used codesystem list
	 * 
	 * @param codeSystemName
	 *            the codesystem name to add
	 */
	public void addUsedCodeSystem(String codeSystemName) {
		if (!this.usedCodeSystems.contains(codeSystemName)) {
			this.usedCodeSystems.add(codeSystemName);
		}
	}

	/**
	 * Add a used code to the used code list
	 * 
	 * @param codeName
	 *            the code name to add
	 */
	public void addUsedCode(String codeName) {
		if (!this.usedCodes.contains(codeName)) {
			this.usedCodes.add(codeName);
		}
	}

	public CQLObject getCqlObject() {
		return cqlObject;
	}

	public void setCqlObject(CQLObject cqlObject) {
		this.cqlObject = cqlObject;
	}

	/**
	 * Gets used expressions
	 * 
	 * @return used expressions list
	 */
	public List<String> getUsedExpressions() {
		return usedExpressions;
	}

	/**
	 * Gets the used functions
	 * 
	 * @return the used functions
	 */
	public List<String> getUsedFunctions() {
		return usedFunctions;
	}

	/**
	 * Gets used valuesets
	 * 
	 * @return used valuesets list
	 */
	public List<String> getUsedValuesets() {
		return usedValuesets;
	}

	/**
	 * Gets used parameters
	 * 
	 * @return the parameters list
	 */
	public List<String> getUsedParameters() {
		return usedParameters;
	}

	/**
	 * Gets the used code systems
	 * 
	 * @return the code systems list
	 */
	public List<String> getUsedCodeSystems() {
		return usedCodeSystems;
	}

	public List<String> getUsedCodes() {
		return usedCodes;
	}

	public List<String> getUsedLibraries() {
		return new ArrayList<String>(this.includedLibraries.keySet());
	}

	public Map<String, CQLIncludeLibrary> getUsedLibrariesMap() {
		Map<String, CQLIncludeLibrary> usedLibMap = new HashMap<String, CQLIncludeLibrary>();

		for (String libName : this.includedLibraries.keySet()) {
			LibraryInfoHolder libHolder = this.includedLibraries.get(libName);
			usedLibMap.put(libName, libHolder.getCqlIncludeLibraryObject());
		}

		return usedLibMap;
	}

	public Map<String, List<String>> getValueSetDataTypeMap() {
		return valueSetDataTypeMap;
	}

	public static String getDataTypeName(String dataTypeIdentifier) {
		if (CollectionUtils.isEmpty(qdmTypeInfoMap)) {
			QdmModelInfoProvider qdmModelInfoProvider = new QdmModelInfoProvider();
            VersionedIdentifier versionedIdentifier = new VersionedIdentifier();
            versionedIdentifier.setId("QDM");
			ModelInfo info = qdmModelInfoProvider.load(versionedIdentifier);

			List<TypeInfo> typeInfoList = info.getTypeInfo();
			for (TypeInfo typeInfo : typeInfoList) {
				if (typeInfo instanceof ProfileInfo) {
					ProfileInfo profileInfo = (ProfileInfo) typeInfo;
					qdmTypeInfoMap.put(profileInfo.getIdentifier(), profileInfo.getLabel());
				}
			}
		}
        return qdmTypeInfoMap.get(dataTypeIdentifier);
	}

	public Map<String, List<String>> getFunctionToFunctionMap() {
		return functionToFunctionMap;
	}

	public Map<String, List<String>> getDefinitionToDefinitionMap() {
		return definitionToDefinitionMap;
	}

	public Map<String, List<String>> getDefinitionToFunctionMap() {
		return definitionToFunctionMap;
	}

	public Map<String, List<String>> getFunctionToDefinitionMap() {
		return functionToDefinitionMap;
	}

	/**
	 * The library holder class. Essentially a data container for library
	 * information.
	 * 
	 * @author Jack Meyer
	 */
	public class LibraryInfoHolder {
		private Library library;
		private String libraryAlias;
		private String libraryName;
		private String libraryVersion;
		private CQLIncludeLibrary cqlIncludeLibraryObject;

		public LibraryInfoHolder(Library library, String alias, String libraryName, String libraryVer) {
			this.setLibrary(library);
			this.setLibraryAlias(alias);
			this.setLibraryName(libraryName);
			this.setLibraryVersion(libraryVer);
		}

		public Library getLibrary() {
			return library;
		}

		public void setLibrary(Library library) {
			this.library = library;
		}

		public String getLibraryAlias() {
			return libraryAlias;
		}

		public void setLibraryAlias(String libraryAlias) {
			this.libraryAlias = libraryAlias;
		}

		public String getLibraryName() {
			return libraryName;
		}

		public void setLibraryName(String libraryName) {
			this.libraryName = libraryName;
		}

		public String getLibraryVersion() {
			return libraryVersion;
		}

		public void setLibraryVersion(String libraryVersion) {
			this.libraryVersion = libraryVersion;
		}

		public CQLIncludeLibrary getCqlIncludeLibraryObject() {
			return cqlIncludeLibraryObject;
		}

		public void setCqlIncludeLibraryObject(CQLIncludeLibrary cqlIncludeLibraryObject) {
			this.cqlIncludeLibraryObject = cqlIncludeLibraryObject;
		}
	}

	// public static void main(String[] args) {
	// test2();
	//
	// }

	// public static void test2(){
	// try {
	// //File f = new File("C:\\chinmay\\stan_CQL_For_JSON.cql");
	// File f = new File("C:\\chinmay\\ANewMeasure-0.0.008.cql");
	// CQLtoELM cqlToElm = new CQLtoELM(f);
	// //MyCQLtoELM cqlToElm = new MyCQLtoELM(f);
	// cqlToElm.doTranslation(true, false, false);
	//
	// List<String> defList = new ArrayList<String>();
	// defList.add("Anesthetic Procedures");
	//// defList.add("Union Diagnoses");
	//// defList.add("Depression Office Visit Encounter 1");
	//// defList.add("Depression Office Visit Encounter 2");
	//// defList.add("Depression Office Visit Encounter 3");
	//// defList.add("Depression Face to Face Encounter 1");
	//// defList.add("Depression Behavioral Health Encounter 1");
	//
	// if(cqlToElm.getErrors().size() == 0){
	// CQLFilter cqlFilter = new CQLFilter(cqlToElm.getLibrary(), defList,
	// f.getParentFile().getAbsolutePath());
	// cqlFilter.filter();
	//
	// System.out.println("Used expressions:"+cqlFilter.getUsedExpressions());
	// System.out.println("Used functions:"+cqlFilter.getUsedFunctions());
	// System.out.println("Used valueSets:"+cqlFilter.getUsedValuesets());
	// System.out.println("Used codesystems:"+cqlFilter.getUsedCodeSystems());
	// System.out.println("Used parameters:"+cqlFilter.getUsedParameters());
	// System.out.println("Used codes:"+cqlFilter.getUsedCodes());
	// System.out.println("ValueSet - DataType
	// map:"+cqlFilter.getValueSetDataTypeMap());
	// System.out.println("Included Libraries:"+cqlFilter.includedLibraries);
	// }else{
	// System.out.println(cqlToElm.getErrors());
	// }
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// public static void test1(){
	// try {
	// File f = File.createTempFile("test", ".cql");
	// FileWriter fw = new FileWriter(f);
	// fw.write(getCQL());
	// fw.close();
	//
	// CQLtoELM cqlToElm = new CQLtoELM(f);
	// //MyCQLtoELM cqlToElm = new MyCQLtoELM(f);
	// cqlToElm.doTranslation(true, false, true);
	// List<String> defList = new ArrayList<String>();
	// defList.add("testInclude");
	// defList.add("test");
	//// defList.add("birthdateDefn");
	//// defList.add("test1.teq");
	//// defList.add("SDE Race");
	// defList.add("SDE Sex");
	//// defList.add("test2");
	//// defList.add("SDE Ethnicity");
	//
	// if(cqlToElm.getErrors().size() == 0){
	// CQLFilter cqlFilter = new CQLFilter(cqlToElm.getLibrary(), defList,
	// f.getParentFile().getAbsolutePath());
	// cqlFilter.filter();
	//
	// System.out.println("Used expressions:"+cqlFilter.getUsedExpressions());
	// System.out.println("Used functions:"+cqlFilter.getUsedFunctions());
	// System.out.println("Used valueSets:"+cqlFilter.getUsedValuesets());
	// System.out.println("Used codesystems:"+cqlFilter.getUsedCodeSystems());
	// System.out.println("Used parameters:"+cqlFilter.getUsedParameters());
	// System.out.println("Used codes:"+cqlFilter.getUsedCodes());
	// System.out.println("ValueSet - DataType
	// map:"+cqlFilter.getValueSetDataTypeMap());
	// System.out.println("Included Libraries:"+cqlFilter.includedLibraries);
	// }else{
	// System.out.println(cqlToElm.getErrors());
	// }
	//
	// f.delete();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static String getCQL() {
		String s = "" + "library birthdate_Dead version '0.0.000' "

				+ "using QDM version '5.0' "

				// + "include Sprint94TMMAT7204P2 version '0.1.000' called t1 "

				+ "codesystem \"LOINC:2.46\": 'urn:oid:2.16.840.1.113883.6.1' version 'urn:hl7:version:2.46' "

				+ "codesystem \"SNOMEDCT:2016-03\": 'urn:oid:2.16.840.1.113883.6.96' version 'urn:hl7:version:2016-03' "

				+ "valueset \"ONC Administrative Sex\": 'urn:oid:2.16.840.1.113762.1.4.1'  "

				+ "valueset \"Race\": 'urn:oid:2.16.840.1.114222.4.11.836' "

				+ "valueset \"Ethnicity\": 'urn:oid:2.16.840.1.114222.4.11.837' "

				+ "valueset \"Payer\": 'urn:oid:2.16.840.1.114222.4.11.3591' "

				+ "code \"Birthdate\": '21112-8' from \"LOINC:2.46\" display 'Birthdate' "

				+ "code \"Dead\": '419099009' from \"SNOMEDCT:2016-03\" display 'Dead' "

				+ "parameter \"Measurement Period\" " + "           Interval<DateTime> "

				+ "context Patient "

				+ "define \"SDE Ethnicity\": [\"Patient Characteristic Ethnicity\": \"Ethnicity\"] "

				+ " define \"SDE Payer\": [\"Patient Characteristic Payer\": \"Payer\"] "

				+ " define \"SDE Race\": [\"Patient Characteristic Race\": \"Race\"] "

				+ " define \"SDE Sex\": [\"Patient Characteristic Sex\": \"ONC Administrative Sex\"] "

				+ " define \"birthdateDefn\": [\"Patient Characteristic Birthdate\": \"Birthdate\"] b where b.code in \"Race\""

				+ " define \"test\": \"birthdateDefn\" "

				+ " define \"test()\": \"birthdateDefn\" "

				+ " define \"test1.teq\": (([\"Medication, Not Dispensed\"] b where b.recorder in \"Payer\")) "

				+ " define \"test1\": \"test1.teq\" "

				+ " define \"test2\": \"Measurement Period\" "

				+ " define \"testInclude\": [\"Medication, Not Dispensed\": \"Birthdate\"] ";

		return s;
	}

	public Map<String, List<String>> getCodeDataTypeMap() {
		return codeDataTypeMap;
	}

	public void setCodeDataTypeMap(Map<String, List<String>> codeDataTypeMap) {
		this.codeDataTypeMap = codeDataTypeMap;
	}

}
