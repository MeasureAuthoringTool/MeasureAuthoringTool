package cqltoelm.models;

import mat.models.CQLCodeModelObject;
import mat.models.CQLCodeSystemModelObject;
import mat.models.CQLExpressionModelObject;
import mat.models.CQLIncludeModelObject;
import mat.models.CQLParameterModelObject;
import mat.models.CQLValueSetModelObject;
import org.hl7.elm.r1.FunctionDef;
import org.hl7.elm.r1.OperandDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmeyer on 2/20/2017.
 */
public class CQLFunctionModelObject {

    private List<CQLValueSetModelObject> refersToValuesets;

    private List<CQLCodeSystemModelObject> refersToCodesystems;

    private List<CQLCodeModelObject> refersToCodes;

    private List<CQLParameterModelObject> refersToParameters;

    private List<CQLExpressionModelObject> refersToExpressions;

    private List<mat.models.CQLFunctionModelObject> refersToFunctions;

    private List<CQLIncludeModelObject> refersToLibrary;

    private List<CQLExpressionModelObject> referredByExpressions;

    private List<mat.models.CQLFunctionModelObject> referredByFunctions;

    /** The name of the function in dot notation, library.functionName **/
    private String name;

    /** The tokens from the CQL to ELM Visitor **/
    private List<String> tokens;

    /** The function definition created by the CQL to ELM Parser **/
    private FunctionDef function;

    private List<FunctionArgument> arguments;

    public CQLFunctionModelObject(String name, List<String> tokens, FunctionDef function)  {
        this.name = name;
        this.tokens = tokens;
        this.function = function;
        this.arguments = new ArrayList<>();

        for(OperandDef operand : function.getOperand()) {
            FunctionArgument argument = new FunctionArgument();
            argument.setArgumentName(operand.getName());
            argument.setArgumentType(operand.getResultType().toString());
            this.arguments.add(argument);
        }

        this.refersToValuesets = new ArrayList<>();
        this.refersToCodesystems = new ArrayList<>();
        this.refersToCodes = new ArrayList<>();
        this.refersToParameters = new ArrayList<>();
        this.refersToExpressions = new ArrayList<>();
        this.refersToLibrary = new ArrayList<>();
        this.referredByExpressions = new ArrayList<>();
        this.referredByExpressions = new ArrayList<>();
        this.refersToFunctions = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();
    }

    public void addRefersToValueset(CQLValueSetModelObject valueset) {
        this.refersToValuesets.add(valueset);
    }

    public void addRefersToCodesystem(CQLCodeSystemModelObject codeSystem) {
        this.refersToCodesystems.add(codeSystem);
    }

    public void addRefersToCode(CQLCodeModelObject code) {
        this.refersToCodes.add(code);
    }

    public void addRefersToParameter(CQLParameterModelObject parameter) {
        this.refersToParameters.add(parameter);
    }

    public void addRefersToExpression(CQLExpressionModelObject expression) {
        this.refersToExpressions.add(expression);
    }

    public void addRefersToFunction(mat.models.CQLFunctionModelObject function) {
        this.refersToFunctions.add(function);
    }

    public void addRefersToLibrary(CQLIncludeModelObject library) {
        this.refersToLibrary.add(library);
    }

    public void addReferredByExpression(CQLExpressionModelObject expression) {
        this.referredByExpressions.add(expression);
    }

    public void addReferredByFunction(mat.models.CQLFunctionModelObject function) {
        this.referredByFunctions.add(function);
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

    public List<CQLExpressionModelObject> getRefersToExpressions() {
        return refersToExpressions;
    }

    public void setRefersToExpressions(List<CQLExpressionModelObject> refersToExpressions) {
        this.refersToExpressions = refersToExpressions;
    }

    public List<mat.models.CQLFunctionModelObject> getRefersToFunctions() {
        return refersToFunctions;
    }

    public void setRefersToFunctions(List<mat.models.CQLFunctionModelObject> refersToFunctions) {
        this.refersToFunctions = refersToFunctions;
    }

    public List<CQLIncludeModelObject> getRefersToLibrary() {
        return refersToLibrary;
    }

    public void setRefersToLibrary(List<CQLIncludeModelObject> refersToLibrary) {
        this.refersToLibrary = refersToLibrary;
    }

    public List<CQLExpressionModelObject> getReferredByExpressions() {
        return referredByExpressions;
    }

    public void setReferredByExpressions(List<CQLExpressionModelObject> referredByExpressions) {
        this.referredByExpressions = referredByExpressions;
    }

    public List<mat.models.CQLFunctionModelObject> getReferredByFunctions() {
        return referredByFunctions;
    }

    public void setReferredByFunctions(List<mat.models.CQLFunctionModelObject> referredByFunctions) {
        this.referredByFunctions = referredByFunctions;
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

    public FunctionDef getFunction() {
        return function;
    }

    public void setFunction(FunctionDef function) {
        this.function = function;
    }

    public class FunctionArgument {
        private String argumentName;
        private String argumentType;

        public String getArgumentName() {
            return argumentName;
        }

        public void setArgumentName(String argumentName) {
            this.argumentName = argumentName;
        }

        public String getArgumentType() {
            return argumentType;
        }

        public void setArgumentType(String argumentType) {
            this.argumentType = argumentType;
        }
    }
}
