package cqltoelm.models;

import mat.models.CQLExpressionModelObject;
import mat.models.CQLFunctionModelObject;
import mat.models.CQLIncludeModelObject;
import org.hl7.elm.r1.ValueSetDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmeyer on 1/16/2017.
 */
public class CQLValueSetModelObject {

    private List<CQLExpressionModelObject> referredByExpressions;

    private List<CQLFunctionModelObject> referredByFunctions;

    private List<CQLIncludeModelObject> refersToLibrary;

    /** The value set definition created by the CQL to ELM Parser **/
    private ValueSetDef valueset;

    /** The name of the valueset in dot notation, library.valueset **/
    private String name;

    /** The tokens from the CQl to ELM Visitor **/
    private List<String> tokens;

    public CQLValueSetModelObject(String name, List<String> tokens, ValueSetDef valueset) {
        this.referredByExpressions = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();

        this.name = name;
        this.tokens = tokens;
        this.valueset = valueset;
    }

    public CQLValueSetModelObject() {
        this.referredByExpressions = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();
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

    public ValueSetDef getValueset() {
        return valueset;
    }

    public void setValueset(ValueSetDef valueset) {
        this.valueset = valueset;
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
