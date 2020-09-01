package mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.dto.UnitDTO;
import mat.model.cql.CQLKeywords;
import mat.shared.cql.model.FunctionSignature;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CQLConstantContainer implements IsSerializable {
    private String currentQDMVersion;
    private String currentFhirVersion;
    private String currentReleaseVersion;
    private List<String> fhirCqlDataTypeList;
    private List<String> cqlAttributeList;
    private List<String> cqlDatatypeList;
    private List<String> qdmDatatypeList;
    private List<UnitDTO> cqlUnitDTOList;
    private List<String> cqlTimingList;
    private Map<String, String> cqlUnitMap;
    private CQLKeywords cqlKeywordList;
    private QDMContainer qdmContainer = new QDMContainer();
    private CQLTypeContainer cqlTypeContainer = new CQLTypeContainer();
    private List<FunctionSignature> functionSignatures = new ArrayList<>();
    // Sorted by FHIR resource name
    private Map<String, FhirDataType> fhirDataTypes = new TreeMap<>();
    private List<FhirDatatypeAttributeAssociation> attributeAssociations;
    private List<String> compoundFhirDataTypes; // uniq set of DataTypes from attributeAssociations
    private List<String> populationBasisValidValues;

    public List<String> getPopulationBasisValidValues() {
        return populationBasisValidValues;
    }

    public void setPopulationBasisValidValues(List<String> populationBasisValidValues) {
        this.populationBasisValidValues = populationBasisValidValues;
    }

    public List<String> getCompoundFhirDataTypes() {
        return compoundFhirDataTypes;
    }

    public void setCompoundFhirDataTypes(List<String> compoundFhirDataTypes) {
        this.compoundFhirDataTypes = compoundFhirDataTypes;
    }

    public Set<String> getAllFhirAttributes() {
        Set<String> allAttributes = new TreeSet<>();

        for (FhirDatatypeAttributeAssociation a : attributeAssociations) {
            allAttributes.add(a.getAttribute());
        }

        return allAttributes;
    }

    public List<FhirDatatypeAttributeAssociation> getAttributeAssociations() {
        return attributeAssociations;
    }

    public void setAttributeAssociations(List<FhirDatatypeAttributeAssociation> attributeAssociations) {
        this.attributeAssociations = attributeAssociations;
    }

    public List<String> getFhirCqlDataTypeList() {
        return fhirCqlDataTypeList;
    }

    public void setFhirCqlDataTypeList(List<String> fhirCqlDataTypeList) {
        this.fhirCqlDataTypeList = fhirCqlDataTypeList;
    }

    public List<String> getCqlAttributeList() {
        return cqlAttributeList;
    }

    public void setCqlAttributeList(List<String> cqlAttributeList) {
        this.cqlAttributeList = cqlAttributeList;
    }

    public List<String> getCqlDatatypeList() {
        return cqlDatatypeList;
    }

    public void setCqlDatatypeList(List<String> cqlDatatypeList) {
        this.cqlDatatypeList = cqlDatatypeList;
    }

    public Map<String, String> getCqlUnitMap() {
        return cqlUnitMap;
    }

    public void setCqlUnitMap(Map<String, String> cqlUnitMap) {
        this.cqlUnitMap = cqlUnitMap;
    }

    public List<UnitDTO> getCqlUnitDTOList() {
        return cqlUnitDTOList;
    }

    public void setCqlUnitDTOList(List<UnitDTO> cqlUnitDTOList) {
        this.cqlUnitDTOList = cqlUnitDTOList;
    }

    public List<String> getQdmDatatypeList() {
        return qdmDatatypeList;
    }

    public void setQdmDatatypeList(List<String> qdmDatatypeList) {
        this.qdmDatatypeList = qdmDatatypeList;
    }

    public CQLKeywords getCqlKeywordList() {
        return cqlKeywordList;
    }

    public void setCqlKeywordList(CQLKeywords keywordList) {
        this.cqlKeywordList = keywordList;
    }

    public List<String> getCqlTimingList() {
        return cqlTimingList;
    }

    public void setCqlTimingList(List<String> cqlTimingList) {
        this.cqlTimingList = cqlTimingList;
    }

    public String getCurrentQDMVersion() {
        return currentQDMVersion;
    }

    public void setCurrentQDMVersion(String currentQDMVersion) {
        this.currentQDMVersion = currentQDMVersion;
    }

    public String getCurrentFhirVersion() {
        return currentFhirVersion;
    }

    public void setCurrentFhirVersion(String currentFhirVersion) {
        this.currentFhirVersion = currentFhirVersion;
    }

    public String getCurrentReleaseVersion() {
        return currentReleaseVersion;
    }

    public void setCurrentReleaseVersion(String currentReleaseVersion) {
        this.currentReleaseVersion = currentReleaseVersion;
    }

    public QDMContainer getQdmContainer() {
        return qdmContainer;
    }

    public void setQdmContainer(QDMContainer qdmContainer) {
        this.qdmContainer = qdmContainer;
    }

    public CQLTypeContainer getCqlTypeContainer() {
        return cqlTypeContainer;
    }

    public void setCqlTypeContainer(CQLTypeContainer cqlTypeContainer) {
        this.cqlTypeContainer = cqlTypeContainer;
    }

    public List<FunctionSignature> getFunctionSignatures() {
        return functionSignatures;
    }

    public void setFunctionSignatures(List<FunctionSignature> functionSignatures) {
        this.functionSignatures = functionSignatures;
    }

    public List<String> getFunctionNames() {
        Set<String> nameSet = new HashSet<>();

        this.functionSignatures.forEach(f -> {
            nameSet.add(f.getName());
        });

        List<String> names = new ArrayList<>(nameSet);
        names.sort(Comparator.naturalOrder());
        return names;
    }

    public Map<String, FhirDataType> getFhirDataTypes() {
        return fhirDataTypes;
    }

    public void setFhirDataTypes(Map<String, FhirDataType> fhirDataTypes) {
        this.fhirDataTypes = fhirDataTypes;
    }
}
