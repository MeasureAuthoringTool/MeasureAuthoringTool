package mat.server.cqlparser;

import org.hl7.elm.r1.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CQLFilter {

    /**
     * The cql library
     */
    private Library library;

    /**
     * The lists of populations that are included in MAT
     */
    private List<String> populationList;

    /**
     * The used expressions list
     */
    private List<String> usedExpressions;

    /**
     * The used functions list
     */
    private List<String> usedFunctions;

    /**
     * The used cql valuesets
     */
    private List<String> usedValuesets;

    /**
     * THe used parameters list
     */
    private List<String> usedParameters;

    /**
     * The used code systems list
     */
    private List<String> usedCodeSystems;


    /**
     * The cql filter
     * @param library the library of the CQL
     * @param populationList the lists of populations that are included in MAT
     */
    public CQLFilter(Library library, List<String> populationList) {
        this.library = library;
        this.populationList = populationList;

        this.usedExpressions = new ArrayList<>();
        this.usedFunctions = new ArrayList<>();
        this.usedValuesets = new ArrayList<>();
        this.usedParameters = new ArrayList<>();
        this.usedCodeSystems = new ArrayList<>();
    }

    /**
     * Filters out the unused cql to
     */
    public void filter() {

        // add all of the base populations into the used expressions list
        this.usedExpressions.addAll(this.populationList);

        for(String expressionName : this.populationList) {
            checkForUsedStatements(expressionName);
        }


    }

    /**
     * Entry point for getting used statements. This takes in an expression name, which is passed in to the Filter class.
     * This name should be associated with a population. Then, it finds the expression object associated with that name,
     * and then checks for the used statements on that object.
     * @param expressionName the expression name
     */
    private void checkForUsedStatements(String expressionName) {

        System.out.println("<<<<<Getting expressions for "  + expressionName + ">>>>>");


        Expression expression = findExpressionByName(expressionName);

        if(expression == null) {
            System.err.println(String.format("Expression %s not found.", expressionName));
            return;
        }

        checkForUsedStatements(expression);
    }

    /**
     * Checks for the used statements for the given expression.
     * @param expression the expression
     */
    private void checkForUsedStatements(Expression expression) {


        // TODO Determine if there is any unecessary checks below
    	// TODO Determine if there is any missing checks below
    	// TODO Testing to make sure it is completely functioning 

        if(expression == null) {
            return;
        }

        System.out.println(expression.getClass());

        // check for parameter, definition, function, valueset references, codesystems //
        // check for expression references
        if(expression.getClass().equals(ExpressionRef.class)) {
            checkforExpressionRef(expression);
        }

        else if(expression.getClass().equals(FunctionRef.class)) {
            checkForFunctionRef(expression);
        }

        // check for parameters
        else if(expression.getClass().equals(ParameterRef.class)) {
            checkForParameterRef(expression);

        }

        // check for in value sets
        else if(expression.getClass().equals(InValueSet.class)) {
            checkForInValuesets(expression);
        }

        // check for valueset references
        else if (expression.getClass().equals(ValueSetRef.class)) {
            checkForValuesetRef(expression);
        }

        // check for in code systems
        else if(expression.getClass().equals(InCodeSystem.class)) {
            checkForInCodeSystem(expression);
        }

        // check for code system ref
        else if(expression.getClass().equals(CodeSystemRef.class)) {
            checkForCodeSystemRef(expression);
        }

        // check for property references
        else if(expression.getClass().equals(Property.class)) {
            checkForPropertyReference(expression);
        }

        // check for **nary expressions //
        // check for unary expressions
        else if(expression.getClass().getSuperclass().equals(UnaryExpression.class)) {
            checkForUnaryExpression(expression);
        }

        // check for binary expressions
        else if(expression.getClass().getSuperclass().equals(BinaryExpression.class)) {
            checkForBinaryExpression(expression);
        }

        // check for ternary expressions
        else if(expression.getClass().getSuperclass().equals(TernaryExpression.class)) {
            checkForTernaryExpression(expression);
        }

        // check for nary expressions
        else if(expression.getClass().getSuperclass().equals(NaryExpression.class)) {
            checkForNaryExpression(expression);
        }

        // check for aggregate expression
        else if(expression.getClass().getSuperclass().equals(AggregateExpression.class)) {
            checkForAggregateExpression(expression);
        }

        // check for case expressions
        else if(expression.getClass().equals(Case.class)) {
            checkForCaseExpression(expression);
        }

        // check for combine expressions
        else if(expression.getClass().equals(Combine.class)) {
            checkForCombineExpression(expression);
        }

        // check for filter expressions
        else if(expression.getClass().equals(Filter.class)) {
            checkForFilterExpression(expression);
        }

        // check for first expressions
        else if(expression.getClass().equals(First.class)) {
            checkForFirstExpression(expression);
        }

        // check for each expressions
        else if(expression.getClass().equals(ForEach.class)) {
            checkForEachExpression(expression);
        }

        // check for if expressions
        else if(expression.getClass().equals(If.class)) {
            checkForIfExpression(expression);
        }

        // check for index of expression
        else if(expression.getClass().equals(IndexOf.class)) {
            checkForIndexOfExpression(expression);
        }

        // check for instance expression
        else if(expression.getClass().equals(Instance.class)) {
            checkForInstanceExpression(expression);
        }

        // check for interval expression
        else if(expression.getClass().equals(Interval.class)) {
            checkForIntervalExpression(expression);
        }

        // check for last expression
        else if(expression.getClass().equals(Last.class)) {
            checkForLastExpression(expression);
        }

        // check for list expression
        else if(expression.getClass().equals(org.hl7.elm.r1.List.class)) {
            checkForList(expression);
        }

        // check for position of expression
        else if(expression.getClass().equals(PositionOf.class)) {
            checkForPositionOf(expression);
        }

        // check for round expression
        else if(expression.getClass().equals(Round.class)) {
            checkForRoundExpression(expression);
        }

        // check for sort expression
        else if(expression.getClass().equals(Sort.class)) {
            checkForSortExpression(expression);
        }

        // check for tuple expressions
        else if(expression.getClass().equals(Tuple.class)) {
            checkForTupleExpression(expression);
        }

        // check for retrieve expressions
        else if(expression.getClass().equals(Retrieve.class)) {
            checkForRetrieveExpression(expression);
        }

        // check for query expressions
        else if(expression.getClass().equals(Query.class)) {
            checkForQueryExpression(expression);
        }

        else {
            System.err.println("Not Found: " + expression.getClass());
        }
    }

    /* check for expression, parameter, valueset references */
    /**
     * Checks for an expression references. If there is an expression references, recursively call
     * checkForUsedStatements on the expression so we can find any expressions inside it. Also, add the used expression
     * to the used expression list.
     * @param expression the expression
     */
    private void checkforExpressionRef(Expression expression) {
        ExpressionRef expressionRef = (ExpressionRef) expression;
        this.addUsedExpression(expressionRef.getName());

        // since we found an expression ref, we want to get the details of it.
        // so we will recursively call get used statements on the expresson ref
        checkForUsedStatements(expressionRef.getName());
    }

    private void checkForFunctionRef(Expression expression) {
        FunctionRef functionRef = (FunctionRef) expression;
        this.addUsedFunction(functionRef.getName());

        // since we found a function ref, we want to get the details of it.
        // so we will recursively call get used statements on the function ref.
        checkForUsedStatements(functionRef.getName());
    }

    /**
     * Check for parameter references and add it to the used parameter reference list
     * @param expression the expression
     */
    private void checkForParameterRef(Expression expression) {
        ParameterRef parameterRef = (ParameterRef) expression;
        this.addUsedParameter(parameterRef.getName());
    }

    /**
     * Check for in valueset references. It gets the in value set reference, then gets the valueset its
     * references, then gets the name and adds it to the used valueset list.
     * @param expression the expression
     */
    private void checkForInValuesets(Expression expression) {
        InValueSet inValueSet = (InValueSet) expression;
        System.out.println("\t" + inValueSet.getValueset().getName());
        this.addUsedValueset(inValueSet.getValueset().getName());
    }

    /**
     * Check for valueset references. Adds the valueset reference to the used valueset list.
     * @param expression
     */
    private void checkForValuesetRef(Expression expression) {
        ValueSetRef valueSetRef = (ValueSetRef) expression;
        System.out.println("\t" + valueSetRef.getName());
        this.addUsedValueset(valueSetRef.getName());
    }

    /**
     * Checks for code system reference. Adds the codesystem to the used codesystem list
     * @param expression the expression
     */
    private void checkForCodeSystemRef(Expression expression) {
        CodeSystemRef codeSystemRef = (CodeSystemRef) expression;
        System.out.println("\t" + codeSystemRef.getName());
        this.addUsedCodeSystem(codeSystemRef.getName());
    }

    /**
     * Check for in code sytem. Gets the codesystem, then adds the name to the used codesystem list.
     * @param expression the expression
     */
    private void checkForInCodeSystem(Expression expression) {
        InCodeSystem inCodeSystem = (InCodeSystem) expression;
        System.out.println("\t" + inCodeSystem.getCodesystem().getName());
        this.addUsedCodeSystem(inCodeSystem.getCodesystem().getName());
    }

    /**
     * Check for property reference
     * @param expression the expression
     */
    private void checkForPropertyReference(Expression expression) {
        Property property = (Property) expression;
        checkForUsedStatements(property.getSource());
    }

    /* check **nary expressions */
    /**
     * Check unary expressions
     * @param expression the expression
     */
    private void checkForUnaryExpression(Expression expression) {
        UnaryExpression unaryExpression = (UnaryExpression) expression;
        checkForUsedStatements(unaryExpression.getOperand());
    }

    /**
     * Check binrary expressions
     * @param expression the expression
     */
    private void checkForBinaryExpression(Expression expression) {
        BinaryExpression binaryExpression = (BinaryExpression) expression;
        List<Expression> operands = binaryExpression.getOperand();
        for(Expression e : operands) {
            checkForUsedStatements(e);
        }
    }

    /**
     * Check ternary expressions
     * @param expression
     */
    private void checkForTernaryExpression(Expression expression) {
        TernaryExpression ternaryExpression = (TernaryExpression) expression;
        List<Expression> operands = ternaryExpression.getOperand();
        for(Expression e : operands) {
            checkForUsedStatements(e);
        }
    }

    /**
     * Check for nary expressions
     * @param expression the expression
     */
    private void checkForNaryExpression(Expression expression) {
        NaryExpression naryExpression = (NaryExpression) expression;
        List<Expression> operands = naryExpression.getOperand();
        for(Expression e : operands) {
            checkForUsedStatements(e);
        }
    }

    /**
     * Check for aggregate expression
     * @param expression the expression
     */
    private void checkForAggregateExpression(Expression expression) {
        AggregateExpression aggregateExpression = (AggregateExpression) expression;
        checkForUsedStatements(aggregateExpression.getSource());
    }

    /**
     * Check for case expression
     * @param expression the expression
     */
    private void checkForCaseExpression(Expression expression) {
        Case caseExpression = (Case) expression;

        checkForUsedStatements(caseExpression.getComparand());
        checkForUsedStatements(caseExpression.getElse());

        List<CaseItem> caseItems = caseExpression.getCaseItem();
        for(CaseItem item : caseItems) {
            checkForUsedStatements(item.getThen());
            checkForUsedStatements(item.getWhen());
        }
    }

    /**
     * Check for combine expression
     * @param expression the expression
     */
    private void checkForCombineExpression(Expression expression) {
        Combine combine = (Combine) expression;
        checkForUsedStatements(combine.getSource());
        checkForUsedStatements(combine.getSeparator());
    }



    /**
     * Check for filter expression
     * @param expression the expression
     */
    private void checkForFilterExpression(Expression expression) {
        Filter filter = (Filter) expression;
        checkForUsedStatements(filter.getSource());
        checkForUsedStatements(filter.getCondition());
    }

    /**
     * Checks for first expression
     * @param expression the expression
     */
    private void checkForFirstExpression(Expression expression) {
        First first = (First) expression;
        checkForUsedStatements(first.getSource());
    }

    /**
     * Check for each expression
     * @param expression the expression
     */
    private void checkForEachExpression(Expression expression) {
        ForEach forEach = (ForEach) expression;
        checkForUsedStatements(forEach.getSource());
        checkForUsedStatements(forEach.getElement());
    }

    /**
     * Check for if expression
     * @param expression the expression
     */
    private void checkForIfExpression(Expression expression) {
        If ifExpression = (If) expression;
        checkForUsedStatements(ifExpression.getCondition());
        checkForUsedStatements(ifExpression.getElse());
        checkForUsedStatements(ifExpression.getThen());
    }

    /**
     * Check for index of expression
     * @param expression the expression
     */
    private void checkForIndexOfExpression(Expression expression) {
        IndexOf indexOf = (IndexOf) expression;
        checkForUsedStatements(indexOf.getSource());
        checkForUsedStatements(indexOf.getElement());
    }

    /**
     * Check for instance expression
     * @param expression the expression
     */
    private void checkForInstanceExpression(Expression expression) {
        Instance instance = (Instance) expression;
        List<InstanceElement> instanceElements =  instance.getElement();
        for(InstanceElement element : instanceElements) {
            checkForUsedStatements(element.getValue());
        }
    }

    /**
     * Check for interval expressions
     * @param expression the expression
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
     * @param expression the xpression
     */
    private void checkForLastExpression(Expression expression) {
        Last last = (Last) expression;
        checkForUsedStatements(last.getSource());
    }

    /**
     * Check for lists
     * @param expression the expression
     */
    private void checkForList(Expression expression) {
        org.hl7.elm.r1.List list = (org.hl7.elm.r1.List) expression;
        List<Expression> expressions = list.getElement();
        for(Expression e: expressions) {
            checkForUsedStatements(e);
        }
    }

    /**
     * Check for position of expression
     * @param expression the expression
     */
    private void checkForPositionOf(Expression expression) {
        PositionOf positionOf = (PositionOf) expression;
        checkForUsedStatements(positionOf.getPattern());
        checkForUsedStatements(positionOf.getString());
    }

    /**
     * Check for round expression
     * @param expression the expression
     */
    private void checkForRoundExpression(Expression expression) {
        Round round = (Round) expression;
        checkForUsedStatements(round.getOperand());
        checkForUsedStatements(round.getPrecision());
    }

    /**
     * Check for sort expression
     * @param expression the expression
     */
    private void checkForSortExpression(Expression expression) {
        Sort sort = (Sort) expression;
        checkForUsedStatements(sort.getSource());
    }

    /**
     * Check for tuple expression
     * @param expression the expression
     */
    private void checkForTupleExpression(Expression expression) {
        Tuple tuple = (Tuple) expression;
        List<TupleElement> tupleElements = tuple.getElement();
        for(TupleElement element : tupleElements) {
            checkForUsedStatements(element.getValue());
        }
    }

    /**
     * Check for retrieve expression
     * @param expression the expression
     */
    private void checkForRetrieveExpression(Expression expression) {
        Retrieve retrieve = (Retrieve) expression;
        checkForUsedStatements(retrieve.getCodes());
        checkForUsedStatements(retrieve.getDateRange());
    }

    /**
     * Check for query expression
     * @param expression the expression
     */
    private void checkForQueryExpression(Expression expression) {
        Query query = (Query) expression;
        List<AliasedQuerySource> sources = query.getSource();
        for(AliasedQuerySource source : sources) {
            Expression sourceEpxression = source.getExpression();
            checkForUsedStatements(sourceEpxression);
        }

        List<RelationshipClause> relationships = query.getRelationship();
        for(RelationshipClause relationship : relationships) {
            Expression relationshipExpression = relationship.getExpression();
            checkForUsedStatements(relationshipExpression);
        }

        Expression whereExpression = query.getWhere();
        checkForUsedStatements(whereExpression);
    }


    /**
     * Finds the expression by name
     * @param expressionName the expression name
     * @return the expression assoicated with the given name, null if none are found
     */
    private Expression findExpressionByName(String expressionName) {
        List<ExpressionDef> expressionDefs = this.library.getStatements().getDef();

        for(ExpressionDef expressionDef : expressionDefs) {
            if(expressionDef.getName().equals(expressionName)) {
                return expressionDef.getExpression();
            }
        }

        return null;

    }

    /**
     * Add a used expression to the used expression list
     * @param expressionName the expression name to add
     */
    public void addUsedExpression(String expressionName) {
        if(!this.usedExpressions.contains(expressionName) && !expressionName.equals("Patient")) {
            this.usedExpressions.add(expressionName);
        }
    }

    /**
     * Add a used function to the used function list
     * @param functionName the function name to add
     */
    public void addUsedFunction(String functionName) {
        if(!this.usedFunctions.contains(functionName)) {
            this.usedFunctions.add(functionName);
        }
    }

    /**
     * Add a used valueset to the used valueset list
     * @param valuesetName
     */
    public void addUsedValueset(String valuesetName) {
        if(!this.usedValuesets.contains(valuesetName)) {
            this.usedValuesets.add(valuesetName);
        }
    }

    /**
     * Add a used parameter to the used parameter list
     * @param parameterName the parameter name to add
     */
    public void addUsedParameter(String parameterName) {
        if(!this.usedParameters.contains(parameterName)) {
            this.usedParameters.add(parameterName);
        }
    }

    /**
     * Add a used codesystem to the used codesystem list
     * @param codeSystemName the codesystem name to add
     */
    public void addUsedCodeSystem(String codeSystemName) {
        if(!this.usedCodeSystems.contains(codeSystemName)) {
            this.usedCodeSystems.add(codeSystemName);
        }
    }

    /**
     * Gets used expressions
     * @return used expressions list
     */
    public List<String> getUsedExpressions() {
        return usedExpressions;
    }

    /**
     * Gets the used functions
     * @return the used functions
     */
    public List<String> getUsedFunctions() {
        return usedFunctions;
    }

    /**
     * Gets used valuesets
     * @return used valuesets list
     */
    public List<String> getUsedValuesets() {
        return usedValuesets;
    }

    /**
     * Gets used parameters
     * @return the parameters list
     */
    public List<String> getUsedParameters() {
        return usedParameters;
    }

    /**
     * Gets the used code systems
     * @return the code systems list
     */
    public List<String> getUsedCodeSystems() {
        return usedCodeSystems;
    }
}
