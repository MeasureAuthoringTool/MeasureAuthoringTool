package cqltoelm.models;

import org.hl7.elm.r1.Library;

import java.util.List;

public class LibraryHolder {
    private Library library;
    private String path;

    private List<CQLCodeModelObject> codeModelObjectList;

    private List<CQLCodeSystemModelObject> codeSystemModelObjectList;

    private List<CQLExpressionModelObject> expressionModelObjectList;

    private List<CQLFunctionModelObject> functionModelObjectList;

    private List<CQLIncludeModelObject> includeModelObjectList;

    private List<CQLParameterModelObject> parameterModelObjectList;

    private List<CQLValueSetModelObject> valueSetModelObjectList;


    public LibraryHolder(Library library, String path,
                         List<CQLCodeModelObject> codeModelObjectList,
                         List<CQLCodeSystemModelObject> codeSystemModelObjectList,
                         List<CQLExpressionModelObject> expressionModelObjectList,
                         List<CQLFunctionModelObject> functionModelObjectList,
                         List<CQLIncludeModelObject> includeModelObjectList,
                         List<CQLParameterModelObject> parameterModelObjectList,
                         List<CQLValueSetModelObject> valueSetModelObjectList) {

        this.library = library;
        this.path = path;
        this.codeModelObjectList = codeModelObjectList;
        this.codeSystemModelObjectList = codeSystemModelObjectList;
        this.expressionModelObjectList = expressionModelObjectList;
        this.functionModelObjectList = functionModelObjectList;
        this.includeModelObjectList = includeModelObjectList;
        this.parameterModelObjectList = parameterModelObjectList;
        this.valueSetModelObjectList = valueSetModelObjectList;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<CQLCodeModelObject> getCodeModelObjectList() {
        return codeModelObjectList;
    }

    public void setCodeModelObjectList(List<CQLCodeModelObject> codeModelObjectList) {
        this.codeModelObjectList = codeModelObjectList;
    }

    public List<CQLCodeSystemModelObject> getCodeSystemModelObjectList() {
        return codeSystemModelObjectList;
    }

    public void setCodeSystemModelObjectList(List<CQLCodeSystemModelObject> codeSystemModelObjectList) {
        this.codeSystemModelObjectList = codeSystemModelObjectList;
    }

    public List<CQLExpressionModelObject> getExpressionModelObjectList() {
        return expressionModelObjectList;
    }

    public void setExpressionModelObjectList(List<CQLExpressionModelObject> expressionModelObjectList) {
        this.expressionModelObjectList = expressionModelObjectList;
    }

    public List<CQLFunctionModelObject> getFunctionModelObjectList() {
        return functionModelObjectList;
    }

    public void setFunctionModelObjectList(List<CQLFunctionModelObject> functionModelObjectList) {
        this.functionModelObjectList = functionModelObjectList;
    }

    public List<CQLIncludeModelObject> getIncludeModelObjectList() {
        return includeModelObjectList;
    }

    public void setIncludeModelObjectList(List<CQLIncludeModelObject> includeModelObjectList) {
        this.includeModelObjectList = includeModelObjectList;
    }

    public List<CQLParameterModelObject> getParameterModelObjectList() {
        return parameterModelObjectList;
    }

    public void setParameterModelObjectList(List<CQLParameterModelObject> parameterModelObjectList) {
        this.parameterModelObjectList = parameterModelObjectList;
    }

    public List<CQLValueSetModelObject> getValueSetModelObjectList() {
        return valueSetModelObjectList;
    }

    public void setValueSetModelObjectList(List<CQLValueSetModelObject> valueSetModelObjectList) {
        this.valueSetModelObjectList = valueSetModelObjectList;
    }
}