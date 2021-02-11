package mat.server.humanreadable.cql;

import java.util.List;

public class HumanReadableModel {

    private HumanReadableMeasureInformationModel measureInformation;
    private List<HumanReadablePopulationCriteriaModel> populationCriterias;
    private List<HumanReadableExpressionModel> supplementalDataElements;
    private List<HumanReadableExpressionModel> riskAdjustmentVariables;
    private List<HumanReadableTerminologyModel> codeTerminologyList;
    private List<HumanReadableTerminologyModel> valuesetTerminologyList;
    private List<HumanReadableCodeModel> codeDataCriteriaList;
    private List<HumanReadableValuesetModel> valuesetDataCriteriaList;
    private List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList;

    private List<HumanReadableExpressionModel> definitions;
    private List<HumanReadableExpressionModel> functions;

    private int numberOfGroups = 1;

    public HumanReadableModel() {
    }

    public HumanReadableMeasureInformationModel getMeasureInformation() {
        return measureInformation;
    }

    public void setMeasureInformation(HumanReadableMeasureInformationModel measureInformation) {
        this.measureInformation = measureInformation;
    }

    public List<HumanReadablePopulationCriteriaModel> getPopulationCriterias() {
        return populationCriterias;
    }

    public void setPopulationCriterias(List<HumanReadablePopulationCriteriaModel> groups) {
        this.populationCriterias = groups;
        this.setNumberOfGroups(groups.size());
    }

    public int getNumberOfGroups() {
        return populationCriterias.size();
    }

    public void setNumberOfGroups(int numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
    }

    public List<HumanReadableExpressionModel> getSupplementalDataElements() {
        return supplementalDataElements;
    }

    public void setSupplementalDataElements(List<HumanReadableExpressionModel> supplementalDataElements) {
        this.supplementalDataElements = supplementalDataElements;
    }

    public List<HumanReadableExpressionModel> getRiskAdjustmentVariables() {
        return riskAdjustmentVariables;
    }

    public void setRiskAdjustmentVariables(List<HumanReadableExpressionModel> riskAdjustmentVariables) {
        this.riskAdjustmentVariables = riskAdjustmentVariables;
    }

    public List<HumanReadableTerminologyModel> getCodeTerminologyList() {
        return codeTerminologyList;
    }

    public void setCodeTerminologyList(List<HumanReadableTerminologyModel> codeTerminologyList) {
        this.codeTerminologyList = codeTerminologyList;
    }

    public List<HumanReadableTerminologyModel> getValuesetTerminologyList() {
        return valuesetTerminologyList;
    }

    public void setValuesetTerminologyList(List<HumanReadableTerminologyModel> valuesetTerminologyList) {
        this.valuesetTerminologyList = valuesetTerminologyList;
    }

    public List<HumanReadableCodeModel> getCodeDataCriteriaList() {
        return codeDataCriteriaList;
    }

    public void setCodeDataCriteriaList(List<HumanReadableCodeModel> codeDataCriteriaList) {
        this.codeDataCriteriaList = codeDataCriteriaList;
    }

    public List<HumanReadableValuesetModel> getValuesetDataCriteriaList() {
        return valuesetDataCriteriaList;
    }

    public void setValuesetDataCriteriaList(List<HumanReadableValuesetModel> valuesetDataCriteriaList) {
        this.valuesetDataCriteriaList = valuesetDataCriteriaList;
    }

    public List<HumanReadableExpressionModel> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<HumanReadableExpressionModel> definitions) {
        this.definitions = definitions;
    }

    public List<HumanReadableExpressionModel> getFunctions() {
        return functions;
    }

    public void setFunctions(List<HumanReadableExpressionModel> functions) {
        this.functions = functions;
    }

    public List<HumanReadableTerminologyModel> getValuesetAndCodeDataCriteriaList() {
        return valuesetAndCodeDataCriteriaList;
    }

    public void setValuesetAndCodeDataCriteriaList(List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList) {
        this.valuesetAndCodeDataCriteriaList = valuesetAndCodeDataCriteriaList;
    }
}
