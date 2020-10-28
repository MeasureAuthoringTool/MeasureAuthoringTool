package cqltoelm.models;

import org.hl7.elm.r1.IncludeDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmeyer on 1/18/2017.
 */
public class CQLIncludeModelObject {

    private IncludeDef include;

    private String name;

    private List<CQLCodeModelObject> referredByCodes;

    private List<CQLCodeSystemModelObject> referredByCodeSystems;

    private List<CQLExpressionModelObject> referredByExpressions;

    private List<CQLParameterModelObject> referredByParameters;

    private List<CQLValueSetModelObject> referredByValuesets;

    private List<CQLFunctionModelObject> referredByFunctions;

    public CQLIncludeModelObject(String name, IncludeDef include) {
        this.referredByCodes = new ArrayList<>();
        this.referredByExpressions = new ArrayList<>();
        this.referredByCodeSystems = new ArrayList<>();
        this.referredByParameters = new ArrayList<>();
        this.referredByValuesets = new ArrayList<>();
        this.referredByFunctions = new ArrayList<>();

        this.name = name;
        this.include = include;
    }

    public IncludeDef getInclude() {
        return include;
    }

    public void addReferredByCode(CQLCodeModelObject code) {
        this.referredByCodes.add(code);
    }

    public void addReferredByCodeSystem(CQLCodeSystemModelObject codeSystem) {
        this.referredByCodeSystems.add(codeSystem);
    }

    public void addReferredByExpession(CQLExpressionModelObject expression) {
        this.referredByExpressions.add(expression);
    }

    public void addReferredByFunction(CQLFunctionModelObject function) {
        this.referredByFunctions.add(function);
    }

    public void addReferredByParameter(CQLParameterModelObject parameter) {
        this.referredByParameters.add(parameter);
    }

    public void addReferredByValueSet(CQLValueSetModelObject valueset) {
        this.referredByValuesets.add(valueset);
    }

    public List<CQLCodeModelObject> getReferredByCodes() {
        return referredByCodes;
    }

    public List<CQLCodeSystemModelObject> getReferredByCodeSystems() {
        return referredByCodeSystems;
    }

    public List<CQLExpressionModelObject> getReferredByExpressions() {
        return referredByExpressions;
    }

    public List<CQLParameterModelObject> getReferredByParameters() {
        return referredByParameters;
    }

    public List<CQLValueSetModelObject> getReferredByValuesets() {
        return referredByValuesets;
    }

    public List<CQLFunctionModelObject> getReferredByFunctions() {
        return referredByFunctions;
    }

    public void setInclude(IncludeDef include) {
        this.include = include;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
