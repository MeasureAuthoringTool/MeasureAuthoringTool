package cqltoelm.models;

import mat.models.CQLCodeModelObject;
import mat.models.CQLCodeSystemModelObject;
import mat.models.CQLFunctionModelObject;
import mat.models.CQLIncludeModelObject;
import mat.models.CQLParameterModelObject;
import mat.models.CQLValueSetModelObject;
import org.hl7.elm.r1.ExpressionDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmeyer on 1/13/2017.
 */
public class CQLExpressionModelObject {

    private List<CQLValueSetModelObject> refersToValuesets;

    private List<CQLCodeSystemModelObject> refersToCodesystems;

    private List<CQLCodeModelObject> refersToCodes;

    private List<CQLParameterModelObject> refersToParameters;

    private List<mat.models.CQLExpressionModelObject> refersToExpressions;

    private List<CQLFunctionModelObject> refersToFunctions;

    private List<CQLIncludeModelObject> refersToLibrary;

    private List<mat.models.CQLExpressionModelObject> referredByExpressions;

    private List<CQLFunctionModelObject> referredByFunctions;

    /** The expression definition created by the CQL to ELM parser **/
    private ExpressionDef expression;

    /** The name of the epxression in dot notation, library.expression **/
    private String name;

    /** The tokens from the CQL to ELM Visitor **/
    private List<String> tokens;

    public CQLExpressionModelObject(String name, List<String> tokens, ExpressionDef expression) {

        this.expression = expression;
        this.name = name;
        this.tokens = tokens;

        this.refersToValuesets = new ArrayList<>();
        this.refersToCodesystems = new ArrayList<>();
        this.refersToParameters = new ArrayList<>();
        this.refersToExpressions = new ArrayList<>();
        this.refersToCodes = new ArrayList<>();
        this.referredByExpressions = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();
        this.refersToFunctions = new ArrayList<>();
    }

    public CQLExpressionModelObject() {
        this.refersToValuesets = new ArrayList<>();
        this.refersToCodesystems = new ArrayList<>();
        this.refersToParameters = new ArrayList<>();
        this.refersToExpressions = new ArrayList<>();
        this.refersToCodes = new ArrayList<>();
        this.referredByExpressions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();
        this.refersToFunctions = new ArrayList<>();
    }

    public void addRefersToValueSet(CQLValueSetModelObject valueset) {
        this.refersToValuesets.add(valueset);
    }

    public void addRefersToCodesystem(CQLCodeSystemModelObject codeSystem) {
        this.refersToCodesystems.add(codeSystem);
    }

    public void addRefersToParameter(CQLParameterModelObject parameter) {
        this.refersToParameters.add(parameter);
    }

    public void addRefersToExpression(mat.models.CQLExpressionModelObject expression) {
        this.refersToExpressions.add(expression);
    }

    public void addRefersToLibrary(CQLIncludeModelObject library) {
        this.refersToLibrary.add(library);
    }

    public void addReferredByExpression(mat.models.CQLExpressionModelObject expression) {
        this.referredByExpressions.add(expression);
    }

    public void addRefersToFunction(CQLFunctionModelObject function) {
        this.refersToFunctions.add(function);
    }

    public void addReferredByFunction(CQLFunctionModelObject function) {
        this.referredByFunctions.add(function);
    }

    public void addRefersToCode(CQLCodeModelObject code) {
        this.refersToCodes.add(code);
    }

    public List<CQLValueSetModelObject> getRefersToValuesets() {
        return refersToValuesets;
    }

    public void setRefersToValuesets(List<CQLValueSetModelObject> refersToValuesets) {
        this.refersToValuesets = refersToValuesets;
    }

    public List<CQLCodeSystemModelObject> getRefersToCodesystems() {
        return refersToCodesystems;
    }

    public void setRefersToCodesystems(List<CQLCodeSystemModelObject> refersToCodesystems) {
        this.refersToCodesystems = refersToCodesystems;
    }

    public List<CQLCodeModelObject> getRefersToCodes() {
        return refersToCodes;
    }

    public void setRefersToCodes(List<CQLCodeModelObject> refersToCodes) {
        this.refersToCodes = refersToCodes;
    }

    public List<CQLParameterModelObject> getRefersToParameters() {
        return refersToParameters;
    }

    public void setRefersToParameters(List<CQLParameterModelObject> refersToParameters) {
        this.refersToParameters = refersToParameters;
    }

    public List<mat.models.CQLExpressionModelObject> getRefersToExpressions() {
        return refersToExpressions;
    }

    public void setRefersToExpressions(List<mat.models.CQLExpressionModelObject> refersToExpressions) {
        this.refersToExpressions = refersToExpressions;
    }

    public List<CQLFunctionModelObject> getRefersToFunctions() {
        return refersToFunctions;
    }

    public void setRefersToFunctions(List<CQLFunctionModelObject> refersToFunctions) {
        this.refersToFunctions = refersToFunctions;
    }

    public List<CQLIncludeModelObject> getRefersToLibrary() {
        return refersToLibrary;
    }

    public void setRefersToLibrary(List<CQLIncludeModelObject> refersToLibrary) {
        this.refersToLibrary = refersToLibrary;
    }

    public List<mat.models.CQLExpressionModelObject> getReferredByExpressions() {
        return referredByExpressions;
    }

    public void setReferredByExpressions(List<mat.models.CQLExpressionModelObject> referredByExpressions) {
        this.referredByExpressions = referredByExpressions;
    }

    public List<CQLFunctionModelObject> getReferredByFunctions() {
        return referredByFunctions;
    }

    public void setReferredByFunctions(List<CQLFunctionModelObject> referredByFunctions) {
        this.referredByFunctions = referredByFunctions;
    }

    public ExpressionDef getExpression() {
        return expression;
    }

    public void setExpression(ExpressionDef expression) {
        this.expression = expression;
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
