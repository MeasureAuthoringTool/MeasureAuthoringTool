package cqltoelm.models;

import org.hl7.elm.r1.CodeSystemDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmeyer on 1/16/2017.
 */
public class CQLCodeSystemModelObject {

    private List<CQLExpressionModelObject> referredByExpressions;

    private List<CQLFunctionModelObject> referredByFunctions;

    private List<CQLIncludeModelObject> refersToLibrary;

    /**
     * The name of the code system in dot notation, library.codeSystem
     **/
    private String name;

    /**
     * The tokens from the CQLtoELM Visitor
     **/
    private List<String> tokens;

    /**
     * The code system definition object created by the CQL to ELM parser
     **/
    private CodeSystemDef codeSystem;

    public CQLCodeSystemModelObject(String name, List<String> tokens, CodeSystemDef codeSystemDef) {
        this.referredByExpressions = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();

        this.name = name;
        this.tokens = tokens;
        this.codeSystem = codeSystemDef;
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

    public CodeSystemDef getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(CodeSystemDef codeSystem) {
        this.codeSystem = codeSystem;
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
