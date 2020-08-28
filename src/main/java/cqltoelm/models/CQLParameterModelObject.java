package cqltoelm.models;

import org.hl7.elm.r1.ParameterDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmeyer on 1/16/2017.
 */
public class CQLParameterModelObject {

    private List<CQLExpressionModelObject> referredByExpressions;

    private List<CQLFunctionModelObject> referredByFunctions;

    private List<CQLIncludeModelObject> refersToLibrary;

    /**
     * The parameter definition created by the CQL to ELM Parser
     **/
    private ParameterDef parameter;

    /**
     * The name of the parameter in dot notation, library.parameter
     **/
    private String name;

    /**
     * The tokens form the CQL to ELM Visitor
     **/
    private List<String> tokens;

    public CQLParameterModelObject(String name, List<String> tokens, ParameterDef parameter) {
        this.referredByExpressions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();

        this.name = name;
        this.tokens = tokens;
        this.parameter = parameter;
    }

    public CQLParameterModelObject() {
        this.referredByExpressions = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();
    }

    public boolean isUsed() {
        return !this.referredByExpressions.isEmpty();
    }

    public void addReferredByExpression(CQLExpressionModelObject expression) {
        this.referredByExpressions.add(expression);
    }

    public void addReferredByFunction(CQLFunctionModelObject function) {
        this.referredByFunctions.add(function);
    }

    public void addRefersToLibrary(CQLIncludeModelObject library) {
        this.refersToLibrary.add(library);

    }

    public List<CQLExpressionModelObject> getReferredByExpressions() {
        return referredByExpressions;
    }

    public void setReferredByExpressions(List<CQLExpressionModelObject> referredByExpressions) {
        this.referredByExpressions = referredByExpressions;
    }

    public List<CQLFunctionModelObject> getReferredByFunctions() {
        return referredByFunctions;
    }

    public void setReferredByFunctions(List<CQLFunctionModelObject> referredByFunctions) {
        this.referredByFunctions = referredByFunctions;
    }

    public List<CQLIncludeModelObject> getRefersToLibrary() {
        return refersToLibrary;
    }

    public void setRefersToLibrary(List<CQLIncludeModelObject> refersToLibrary) {
        this.refersToLibrary = refersToLibrary;
    }

    public ParameterDef getParameter() {
        return parameter;
    }

    public void setParameter(ParameterDef parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
