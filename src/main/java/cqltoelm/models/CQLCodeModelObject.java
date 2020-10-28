package cqltoelm.models;

import org.hl7.elm.r1.CodeDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmeyer on 1/16/2017.
 */
public class CQLCodeModelObject {

    private List<CQLExpressionModelObject> referredByExpressions;

    private List<CQLFunctionModelObject> referredByFunctions;

    private List<CQLIncludeModelObject> refersToLibrary;

    /**
     * The name of the code in dot notation, library.codeName
     **/
    private String name;

    /**
     * The tokens of the code from the CQLtoELM Visitor
     **/
    private List<String> tokens;

    /**
     * The code definition object from the CQL to ELM parser
     **/
    private CodeDef code;

    public CQLCodeModelObject(String name, List<String> tokens, CodeDef codeRef) {
        this.referredByExpressions = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();

        this.code = codeRef;
        this.name = name;
        this.tokens = tokens;
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

    public CodeDef getCode() {
        return code;
    }

    public void setCode(CodeDef code) {
        this.code = code;
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
