package mat.server.humanreadable;

import freemarker.template.TemplateException;
import mat.client.measure.service.CQLService;
import mat.client.shared.MatContext;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.humanreadable.cql.CQLHumanReadableGenerator;
import mat.server.humanreadable.cql.HumanReadableCodeModel;
import mat.server.humanreadable.cql.HumanReadableExpressionModel;
import mat.server.humanreadable.cql.HumanReadableModel;
import mat.server.humanreadable.cql.HumanReadablePopulationCriteriaModel;
import mat.server.humanreadable.cql.HumanReadablePopulationModel;
import mat.server.humanreadable.cql.HumanReadableTerminologyModel;
import mat.server.humanreadable.cql.HumanReadableValuesetModel;
import mat.server.humanreadable.helper.DataRequirementsNoValueSetFilter;
import mat.server.humanreadable.qdm.HQMFHumanReadableGenerator;
import mat.server.logging.LogFactory;
import mat.server.service.FhirMeasureRemoteCall;
import mat.server.service.cql.HumanReadableArtifacts;
import mat.server.service.impl.XMLMarshalUtil;
import mat.server.util.CQLUtil;
import mat.server.util.CQLUtil.CQLArtifactHolder;
import mat.server.util.XmlProcessor;
import mat.shared.LibHolderObject;
import mat.shared.MatConstants;
import mat.shared.SaveUpdateCQLResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class HumanReadableGenerator {
    private static final Log log = LogFactory.getLog(HumanReadableGenerator.class);

    private static final String[] POPULATION_NAME_ARRAY = {MatConstants.INITIAL_POPULATION,
            MatConstants.DENOMINATOR, MatConstants.DENOMINATOR_EXCLUSIONS, MatConstants.NUMERATOR,
            MatConstants.NUMERATOR_EXCLUSIONS, MatConstants.DENOMINATOR_EXCEPTIONS,
            MatConstants.MEASURE_POPULATION, MatConstants.MEASURE_POPULATION_EXCLUSIONS, MatConstants.STRATUM,
            MatConstants.MEASURE_OBSERVATION_POPULATION};

    private final String CQLFUNCTION = "cqlfunction";
    private final String CQLDEFINITION = "cqldefinition";
    private Map<String, Integer> populationCountMap = new HashMap<>();
    private Map<String, Integer> popCountMultipleMap = new HashMap<>();
    private Map<String, String> populationNameMap = new HashMap<>();

    @Autowired
    private CQLHumanReadableGenerator humanReadableGenerator;

    @Autowired
    private CQLService cqlService;

    @Autowired
    private FhirMeasureRemoteCall fhirMeasureRemoteCall;
    private Consumer<HumanReadableCodeModel> humanReadableCodeModelConsumer;

    public String generateHTMLForPopulationOrSubtree(String measureId, String subXML, String measureXML, CQLLibraryDAO cqlLibraryDAO) {

        XmlProcessor subXMLProcessor = new XmlProcessor(subXML);
        String html = "";

        if (subXMLProcessor.getOriginalDoc().getDocumentElement().hasChildNodes()) {
            String firstNodeName = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild().getNodeName();
            log.debug("firstNodeName:" + firstNodeName);

            if ("cqldefinition".equals(firstNodeName) || "cqlfunction".equals(firstNodeName)
                    || "cqlaggfunction".equals(firstNodeName)) {
                Node cqlNode = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild();
                HumanReadablePopulationModel population;
                try {
                    population = getPopulationModel(measureXML, cqlNode.getParentNode());
                    html = humanReadableGenerator.generateSinglePopulation(population, measureXML.contains("<usingModel>QDM</usingModel>"));
                } catch (XPathExpressionException | IOException | TemplateException e) {
                    e.printStackTrace();
                }
            } else {
                html = HQMFHumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId, subXML, measureXML);
            }
        } else {
            return "<html></html>";
        }

        return html;
    }

    public String generateHTMLForMeasure(String measureId,
                                         String simpleXml,
                                         String measureReleaseVersion,
                                         CQLLibraryDAO cqlLibraryDAO,
                                         List<String> dataRequirementsNoValueSet) {

        String html = "";
        log.debug("Generating human readable for ver:" + measureReleaseVersion);
        if (MatContext.get().isCQLMeasure(measureReleaseVersion)) {
            try {
                XmlProcessor processor = new XmlProcessor(simpleXml);

                CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(simpleXml);
                String cqlString = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();

                CQLArtifactHolder usedCQLArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(processor.getOriginalDoc());

                SaveUpdateCQLResult cqlResult = cqlModel.isFhir() ?
                        parseFhirCqlLibraryForErrors(cqlModel, cqlString) :
                        CQLUtil.parseQDMCQLLibraryForErrors(cqlModel, cqlLibraryDAO, getCQLIdentifiers(cqlModel));
                Map<String, XmlProcessor> includedLibraryXmlProcessors = loadIncludedLibXMLProcessors(cqlModel);

                XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();

                HumanReadableModel model = (HumanReadableModel) xmlMarshalUtil.convertXMLToObject("SimpleXMLHumanReadableModelMapping.xml", simpleXml, HumanReadableModel.class);
                List<String> measureTypes = null;
                if (model.getMeasureInformation() != null && CollectionUtils.isNotEmpty(model.getMeasureInformation().getMeasureTypes())) {
                    measureTypes = new ArrayList<>();
                    measureTypes.addAll(model.getMeasureInformation().getMeasureTypes());
                    Collections.sort(measureTypes);
                }

                model.getMeasureInformation().setMeasureTypes(measureTypes);
                model.setPopulationCriterias(getPopulationCriteriaModels(processor));
                model.setSupplementalDataElements(getSupplementalDataElements(processor));
                model.setRiskAdjustmentVariables(getRiskAdjustmentVariables(processor));

                if (cqlModel.isFhir()) {
                    // For now we are not filtering unused for FHIR.
                    // We are adding this in as part of QDM 5.6 and then they will be the same again.
                    model.setDefinitions(getDefinitionsFHIR(cqlModel, processor, includedLibraryXmlProcessors, cqlResult, usedCQLArtifactHolder));
                    model.setFunctions(getFunctionsFHIR(cqlModel, processor, includedLibraryXmlProcessors, cqlResult, usedCQLArtifactHolder));

                    updateFhirValuesetsCodesystemsDataReqs(model, measureId);

                    if ("decrease".equals(model.getMeasureInformation().getImprovementNotation())) {
                        model.getMeasureInformation().setImprovementNotation("Decreased score indicates improvement");
                    } else {
                        model.getMeasureInformation().setImprovementNotation("Increased score indicates improvement");
                    }

                    if (CollectionUtils.isNotEmpty(dataRequirementsNoValueSet)) {
                        List<HumanReadableTerminologyModel> dataRequirementsNoValueSetList =
                                processDataRequirementsNoValueSet(dataRequirementsNoValueSet, model.getValuesetAndCodeDataCriteriaList());

                        model.getValuesetAndCodeDataCriteriaList().addAll(dataRequirementsNoValueSetList);
                    }

                } else {
                    // For QDM unused is filtered.
                    model.setDefinitions(getDefinitionsQDM(cqlModel, processor, includedLibraryXmlProcessors, cqlResult, usedCQLArtifactHolder));
                    model.setFunctions(getFunctionsQDM(cqlModel, processor, includedLibraryXmlProcessors, cqlResult, usedCQLArtifactHolder));

                    List<HumanReadableTerminologyModel> valuesetTerminologyList = getValuesetTerminologyQDM(processor);
                    sortTerminologyList(valuesetTerminologyList);
                    model.setValuesetTerminologyList(valuesetTerminologyList);

                    List<HumanReadableTerminologyModel> codeTerminologyList = getCodeTerminologyQDM(processor);
                    sortTerminologyList(codeTerminologyList);
                    model.setCodeTerminologyList(codeTerminologyList);

                    model.setValuesetDataCriteriaList(getValuesetDataCriteria(processor));
                    model.setCodeDataCriteriaList(getCodeDataCriteria(processor));

                    List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList = new ArrayList<>();
                    valuesetAndCodeDataCriteriaList.addAll(model.getValuesetDataCriteriaList());
                    valuesetAndCodeDataCriteriaList.addAll(model.getCodeDataCriteriaList());

                    sortDataCriteriaList(valuesetAndCodeDataCriteriaList);
                    model.setValuesetAndCodeDataCriteriaList(valuesetAndCodeDataCriteriaList);
                }
                html = humanReadableGenerator.generate(model, cqlModel.isFhir());
            } catch (IOException | TemplateException | MappingException | MarshalException | ValidationException | XPathExpressionException e) {
                log.error("Error in HumanReadableGenerator::generateHTMLForMeasure: " + e.getMessage(), e);
            }
        } else {
            html = HQMFHumanReadableGenerator.generateHTMLForMeasure(measureId, simpleXml);
        }

        return html;
    }

    private List<HumanReadableTerminologyModel> processDataRequirementsNoValueSet(List<String> dataRequirementsNoValueSet,
                                                                                  List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList) {
        DataRequirementsNoValueSetFilter dataRequirementsNoValueSetFilter =
                new DataRequirementsNoValueSetFilter(dataRequirementsNoValueSet, valuesetAndCodeDataCriteriaList);

        return dataRequirementsNoValueSetFilter.process();
    }

    private SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString) {
        return cqlService.parseFhirCQLForErrors(cqlModel, cqlString);
    }

    private void sortDataCriteriaList(List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList) {
        valuesetAndCodeDataCriteriaList.sort((o1, o2) -> {
            String o1String = o1.getDatatype() + ": " + o1.getName();
            String o2String = o2.getDatatype() + ": " + o2.getName();
            return o1String.compareToIgnoreCase(o2String);
        });
    }

    private void sortTerminologyList(List<HumanReadableTerminologyModel> terminologyList) throws XPathExpressionException {
        terminologyList.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
    }

    private List<String> getCQLIdentifiers(CQLModel cqlModel) {
        List<String> identifiers = new ArrayList<>();
        List<CQLDefinition> cqlDefinition = cqlModel.getDefinitionList();
        for (CQLDefinition cqlDef : cqlDefinition) {
            identifiers.add(cqlDef.getName());
        }

        List<CQLFunctions> cqlFunctions = cqlModel.getCqlFunctions();
        for (CQLFunctions cqlFunc : cqlFunctions) {
            identifiers.add(cqlFunc.getName());
        }

        return identifiers;
    }

    private HumanReadablePopulationModel getPopulationModel(String measureXML, Node populationNode) throws XPathExpressionException {
        XmlProcessor processor = new XmlProcessor(measureXML);
        resetPopulationMaps();
        return getPopulationCriteria(processor, populationNode);
    }

    private Map<String, XmlProcessor> loadIncludedLibXMLProcessors(CQLModel cqlModel) {
        Map<String, XmlProcessor> returnMap = new HashMap<String, XmlProcessor>();
        Map<String, LibHolderObject> includeMap = cqlModel.getIncludedCQLLibXMLMap();
        for (String libName : includeMap.keySet()) {
            LibHolderObject lib = includeMap.get(libName);
            String xml = lib.getMeasureXML();
            XmlProcessor xmlProcessor = new XmlProcessor(xml);
            returnMap.put(libName, xmlProcessor);
        }

        return returnMap;
    }

    private String getCQLFunctionSignature(String expressionName, XmlProcessor populationOrSubtreeXMLProcessor) {

        String signature = "";

        String xPath = "//cqlLookUp//function[@name = '" + expressionName + "']/arguments";
        try {
            Node argumentsNode = populationOrSubtreeXMLProcessor.findNode(populationOrSubtreeXMLProcessor.getOriginalDoc(), xPath);
            if (argumentsNode != null) {
                NodeList children = argumentsNode.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (child.getNodeName().equals("argument")) {
                        String type = child.getAttributes().getNamedItem("type").getNodeValue();
                        if ("QDM Datatype".equals(type)) {
                            type = child.getAttributes().getNamedItem("qdmDataType").getNodeValue();
                            type = "\"" + type + "\"";
                        } else if ("Others".equals(type)) {
                            type = child.getAttributes().getNamedItem("otherType").getNodeValue();
                        }
                        String argName = child.getAttributes().getNamedItem("argumentName").getNodeValue();
                        signature += argName + " " + type + ", ";
                    }
                }
            }
        } catch (XPathExpressionException e) {
            log.error("Error in HumanReadableGenerator::getCQLFunctionSignature: " + e.getMessage(), e);
        }

        if (signature.length() > 0) {
            signature = signature.trim();
            if (signature.endsWith(",")) {
                signature = signature.substring(0, signature.length() - 1);
            }
            signature = "(" + signature + ")";
        } else {
            signature = "()";
        }

        return signature;
    }

    private List<HumanReadableExpressionModel> getDefinitionsQDM(CQLModel cqlModel, XmlProcessor parentLibraryProcessor, Map<String, XmlProcessor> includedLibraryXmlProcessors, SaveUpdateCQLResult cqlResult, CQLArtifactHolder usedCQLArtifactHolder) {
        List<HumanReadableExpressionModel> definitions = new ArrayList<>();
        List<String> usedDefinitions = cqlResult.getUsedCQLArtifacts().getUsedCQLDefinitions();
        List<String> definitionsList = new ArrayList<String>(usedCQLArtifactHolder.getCqlDefFromPopSet());
        definitionsList.removeAll(usedDefinitions);
        definitionsList.addAll(usedDefinitions);
        definitionsList = definitionsList.stream().distinct().collect(Collectors.toList());
        Collections.sort(definitionsList, String.CASE_INSENSITIVE_ORDER);

        for (String expressionName : definitionsList) {
            String statementIdentifier = expressionName;
            XmlProcessor currentProcessor = parentLibraryProcessor;
            String[] arr = expressionName.split(Pattern.quote("|"));
            if (arr.length == 3) {
                expressionName = arr[2];
                statementIdentifier = arr[1] + "." + arr[2];
                currentProcessor = includedLibraryXmlProcessors.get(arr[0] + "|" + arr[1]);
            }

            HumanReadableExpressionModel expression = new HumanReadableExpressionModel(statementIdentifier, getLogicStringFromXMLByName(expressionName, CQLDEFINITION, currentProcessor));
            definitions.add(expression);
        }

        return definitions;
    }

    private List<HumanReadableExpressionModel> getDefinitionsFHIR(CQLModel cqlModel, XmlProcessor parentLibraryProcessor, Map<String, XmlProcessor> includedLibraryXmlProcessors, SaveUpdateCQLResult cqlResult, CQLArtifactHolder usedCQLArtifactHolder) {
        List<HumanReadableExpressionModel> definitions = new ArrayList<>();
        cqlModel.getDefinitionList().stream().
                sorted((d1, d2) -> String.CASE_INSENSITIVE_ORDER.compare(d1.getName(), d2.getName())).
                forEach(d -> {
                    String name = d.getName();
                    String statementIdentifier = d.getName();
                    XmlProcessor currentProcessor = parentLibraryProcessor;
                    String[] arr = name.split(Pattern.quote("|"));
                    if (arr.length == 3) {
                        name = arr[2];
                        statementIdentifier = arr[1] + "." + arr[2];
                        currentProcessor = includedLibraryXmlProcessors.get(arr[0] + "|" + arr[1]);
                    }
                    HumanReadableExpressionModel expression = new HumanReadableExpressionModel(statementIdentifier,
                            getLogicStringFromXMLByName(name, CQLDEFINITION, currentProcessor));
                    definitions.add(expression);
                });
        return definitions;
    }

    private List<HumanReadableExpressionModel> getFunctionsQDM(CQLModel cqlModel, XmlProcessor parentLibraryProcessor, Map<String, XmlProcessor> includedLibraryXmlProcessors, SaveUpdateCQLResult cqlResult, CQLArtifactHolder usedCQLArtifactHolder) {
        List<HumanReadableExpressionModel> functions = new ArrayList<>();
        List<String> usedFunctions = cqlResult.getUsedCQLArtifacts().getUsedCQLFunctions();
        List<String> functionsList = new ArrayList<String>(usedCQLArtifactHolder.getCqlFuncFromPopSet());
        functionsList.removeAll(usedFunctions);
        functionsList.addAll(usedFunctions);
        functionsList = functionsList.stream().distinct().collect(Collectors.toList());
        Collections.sort(functionsList, String.CASE_INSENSITIVE_ORDER);

        for (String expressionName : functionsList) {
            String statementIdentifier = expressionName;
            XmlProcessor currentProcessor = parentLibraryProcessor;
            String[] arr = expressionName.split(Pattern.quote("|"));
            if (arr.length == 3) {
                expressionName = arr[2];
                statementIdentifier = arr[1] + "." + arr[2];
                currentProcessor = includedLibraryXmlProcessors.get(arr[0] + "|" + arr[1]);
            }

            HumanReadableExpressionModel expression = new HumanReadableExpressionModel(
                    statementIdentifier + getCQLFunctionSignature(expressionName, currentProcessor),
                    getLogicStringFromXMLByName(expressionName, CQLFUNCTION, currentProcessor));
            functions.add(expression);
        }

        return functions;
    }

    private List<HumanReadableExpressionModel> getFunctionsFHIR(CQLModel cqlModel,
                                                                XmlProcessor parentLibraryProcessor,
                                                                Map<String, XmlProcessor> includedLibraryXmlProcessors,
                                                                SaveUpdateCQLResult cqlResult,
                                                                CQLArtifactHolder usedCQLArtifactHolder) {
        List<HumanReadableExpressionModel> functions = new ArrayList<>();
        cqlModel.getCqlFunctions().stream().
                sorted((d1, d2) -> String.CASE_INSENSITIVE_ORDER.compare(d1.getName(), d2.getName())).
                forEach(f -> {
                    String name = f.getName();
                    String statementIdentifier = name;
                    XmlProcessor currentProcessor = parentLibraryProcessor;
                    String[] arr = name.split(Pattern.quote("|"));
                    if (arr.length == 3) {
                        name = arr[2];
                        statementIdentifier = arr[1] + "." + arr[2];
                        currentProcessor = includedLibraryXmlProcessors.get(arr[0] + "|" + arr[1]);
                    }

                    HumanReadableExpressionModel expression = new HumanReadableExpressionModel(
                            statementIdentifier + getCQLFunctionSignature(name, currentProcessor),
                            getLogicStringFromXMLByName(name, CQLFUNCTION, currentProcessor));
                    functions.add(expression);
                });
        return functions;
    }

    private String getLogicStringFromXMLByName(String cqlName, String cqlType, XmlProcessor simpleXMLProcessor) {

        String logic = "";
        String xPath = "//cqlLookUp//";

        if (cqlType.equals(CQLDEFINITION)) {
            xPath += "definition[@name='" + cqlName + "']/logic";
        } else if (cqlType.equals(CQLFUNCTION)) {
            xPath += "function[@name='" + cqlName + "']/logic";
        }

        try {
            Node logicNode = simpleXMLProcessor.findNode(simpleXMLProcessor.getOriginalDoc(), xPath);
            if (logicNode != null) {
                logic = logicNode.getTextContent();
            }
        } catch (XPathExpressionException e) {
            log.error("Error in HumanReadableGenerator::getLogicStringFromXMLByName: " + e.getMessage(), e);
        }

        return logic;
    }


    private List<HumanReadableExpressionModel> getSupplementalDataElements(XmlProcessor processor) throws XPathExpressionException {
        List<HumanReadableExpressionModel> supplementalDataElements = new ArrayList<>();
        if (processor.findNode(processor.getOriginalDoc(), "/measure/supplementalDataElements") != null) {
            NodeList supplementalDataElementNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/supplementalDataElements/cqldefinition");

            for (int i = 0; i < supplementalDataElementNodes.getLength(); i++) {
                Node sde = supplementalDataElementNodes.item(i);
                var model = getExpressionModel(processor, sde);
                if (model != null) {
                    supplementalDataElements.add(model);
                }
            }
        }
        return supplementalDataElements;
    }

    private List<HumanReadableValuesetModel> getValuesetDataCriteria(XmlProcessor processor) throws XPathExpressionException {
        List<HumanReadableValuesetModel> valuesets = new ArrayList<>();
        NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code='false'][@datatype]");

        for (int i = 0; i < elements.getLength(); i++) {

            String datatype = elements.item(i).getAttributes().getNamedItem("datatype").getNodeValue();
            String name = elements.item(i).getAttributes().getNamedItem("name").getNodeValue();
            String oid = elements.item(i).getAttributes().getNamedItem("oid").getNodeValue();

            String version = "";
            if (elements.item(i).getAttributes().getNamedItem("version") != null) {
                version = elements.item(i).getAttributes().getNamedItem("version").getNodeValue();
            }

            HumanReadableValuesetModel valueset = new HumanReadableValuesetModel(name, oid, version, datatype);
            valuesets.add(valueset);
        }

        valuesets.sort(Comparator.comparing(HumanReadableValuesetModel::getDataCriteriaDisplay));
        return valuesets;
    }

    private List<HumanReadableCodeModel> getCodeDataCriteria(XmlProcessor processor) throws XPathExpressionException {
        List<HumanReadableCodeModel> codes = new ArrayList<>();
        NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code='true'][@datatype]");

        for (int i = 0; i < elements.getLength(); i++) {
            NamedNodeMap attributeMap = elements.item(i).getAttributes();
            String datatype = attributeMap.getNamedItem("datatype").getNodeValue();
            if ("attribute".equals(datatype)) {
                datatype = "Attribute";
            }

            String name = attributeMap.getNamedItem("name").getNodeValue();
            String oid = attributeMap.getNamedItem("oid").getNodeValue();
            String codesystemVersion = attributeMap.getNamedItem("codeSystemVersion").getNodeValue();
            String codesystemName = attributeMap.getNamedItem("taxonomy").getNodeValue();
            boolean isCodeSystemVersionIncluded = true;
            Node isCodeSystemVersionIncludedNode = attributeMap.getNamedItem("isCodeSystemVersionIncluded");
            if (isCodeSystemVersionIncludedNode != null) {
                isCodeSystemVersionIncluded = Boolean.parseBoolean(isCodeSystemVersionIncludedNode.getNodeValue());
            }

            HumanReadableCodeModel code = new HumanReadableCodeModel(name, oid, codesystemName, isCodeSystemVersionIncluded, codesystemVersion, datatype);
            codes.add(code);
        }

        codes.sort(Comparator.comparing(HumanReadableCodeModel::getDataCriteriaDisplay));
        return codes;
    }

    private List<HumanReadableTerminologyModel> getValuesetTerminologyQDM(XmlProcessor processor) throws XPathExpressionException {
        Set<HumanReadableValuesetModel> valuesets = new HashSet<>();
        NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code=\"false\"]");

        for (int i = 0; i < elements.getLength(); i++) {
            Node current = elements.item(i);
            String name = current.getAttributes().getNamedItem("name").getNodeValue();
            String oid = current.getAttributes().getNamedItem("oid").getNodeValue();
            String version = current.getAttributes().getNamedItem("version").getNodeValue();
            HumanReadableValuesetModel valueset = new HumanReadableValuesetModel(name, oid, version, "");
            valuesets.add(valueset);
        }

        List<HumanReadableTerminologyModel> valuesetList = new ArrayList<>(valuesets);
        valuesetList.sort(Comparator.comparing(HumanReadableTerminologyModel::getTerminologyDisplay));
        return valuesetList;
    }

    private List<HumanReadableTerminologyModel> getCodeTerminologyQDM(XmlProcessor processor) throws XPathExpressionException {
        Set<HumanReadableCodeModel> codes = new HashSet<>();
        NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code=\"true\"]");
        for (int i = 0; i < elements.getLength(); i++) {
            Node current = elements.item(i);
            String name = current.getAttributes().getNamedItem("name").getNodeValue();
            String oid = current.getAttributes().getNamedItem("oid").getNodeValue();
            String codesystemName = current.getAttributes().getNamedItem("taxonomy").getNodeValue();
            String codesystemVersion = "";

            Node isCodeSystemVersionIncludedNode = current.getAttributes().getNamedItem("isCodeSystemVersionIncluded");
            boolean isCodeSystemVersionIncluded = true; // by default the code system should be included if the isCodeSystemIncluded tag does not exist
            if (isCodeSystemVersionIncludedNode != null) {
                isCodeSystemVersionIncluded = Boolean.parseBoolean(isCodeSystemVersionIncludedNode.getNodeValue());
            }

            if (isCodeSystemVersionIncluded) {
                codesystemVersion = current.getAttributes().getNamedItem("codeSystemVersion").getNodeValue();
            }

            HumanReadableCodeModel model = new HumanReadableCodeModel(name, oid, codesystemName, isCodeSystemVersionIncluded, codesystemVersion, "");
            codes.add(model);
        }

        List<HumanReadableTerminologyModel> codesList = new ArrayList<>(codes);
        codesList.sort(Comparator.comparing(HumanReadableTerminologyModel::getTerminologyDisplay));
        return codesList;
    }

    private void updateFhirValuesetsCodesystemsDataReqs(HumanReadableModel model, String measureId) throws XPathExpressionException {
        //Retrieve from microservices.
        HumanReadableArtifacts artifacts = fhirMeasureRemoteCall.getHumanReadableArtifacts(measureId);

        model.setValuesetTerminologyList(new ArrayList<>());
        model.setCodeTerminologyList(new ArrayList<>());
        model.setValuesetDataCriteriaList(new ArrayList<>());
        model.setCodeDataCriteriaList(new ArrayList<>());

        //Add to lists to regenerate display fields in constructor.
        artifacts.getTerminologyCodeModels().forEach(cm ->
                model.getCodeTerminologyList().add(new HumanReadableCodeModel(cm.getName(),
                        cm.getOid(),
                        cm.getCodesystemName(),
                        cm.getIsCodesystemVersionIncluded(),
                        cm.getCodesystemVersion(),
                        cm.getDatatype()))
        );
        artifacts.getTerminologyValueSetModels().forEach(vsm ->
                model.getValuesetTerminologyList().add(new HumanReadableValuesetModel(vsm.getName(),
                        vsm.getOid(),
                        vsm.getVersion(),
                        vsm.getDatatype()))
        );
        artifacts.getDataReqCodes().forEach(drc ->
                model.getCodeDataCriteriaList().add(new HumanReadableCodeModel(drc.getName(),
                        drc.getOid(),
                        drc.getCodesystemName(),
                        drc.getIsCodesystemVersionIncluded(),
                        drc.getCodesystemVersion(),
                        drc.getDatatype()))
        );
        artifacts.getDataReqValueSets().forEach(vsm ->
                model.getValuesetDataCriteriaList().add(new HumanReadableValuesetModel(vsm.getName(),
                        vsm.getOid(),
                        vsm.getVersion(),
                        vsm.getDatatype()))
        );

        //Update combined data criteria.
        model.setValuesetAndCodeDataCriteriaList(new ArrayList<>());
        model.getValuesetAndCodeDataCriteriaList().addAll(model.getValuesetDataCriteriaList());
        model.getValuesetAndCodeDataCriteriaList().addAll(model.getCodeDataCriteriaList());

        //Sort as QDM did.
        sortTerminologyList(model.getValuesetTerminologyList());
        sortTerminologyList(model.getCodeTerminologyList());

        model.getCodeDataCriteriaList().sort(Comparator.comparing(HumanReadableCodeModel::getDataCriteriaDisplay));
        model.getValuesetDataCriteriaList().sort(Comparator.comparing(HumanReadableValuesetModel::getDataCriteriaDisplay));

        sortDataCriteriaList(model.getValuesetAndCodeDataCriteriaList());
    }

    private HumanReadableExpressionModel getExpressionModel(XmlProcessor processor, Node sde)
            throws XPathExpressionException {
        //String uuid = sde.getAttributes().getNamedItem("uuid").getNodeValue();
        String name = sde.getAttributes().getNamedItem("displayName").getNodeValue();
        Node node = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//definition[@name='" + name + "']/logic");
        if (node != null) {
            String logic = node.getTextContent();
            return new HumanReadableExpressionModel(name, logic);
        } else {
            return null;
        }
    }

    private List<HumanReadableExpressionModel> getRiskAdjustmentVariables(XmlProcessor processor) throws XPathExpressionException {
        List<HumanReadableExpressionModel> riskAdjustmentVariables = new ArrayList<>();

        if (processor.findNode(processor.getOriginalDoc(), "/measure/riskAdjustmentVariables") != null) {
            NodeList riskAdjustmentVariableNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/riskAdjustmentVariables/cqldefinition");
            for (int i = 0; i < riskAdjustmentVariableNodes.getLength(); i++) {
                Node rav = riskAdjustmentVariableNodes.item(i);
                riskAdjustmentVariables.add(getExpressionModel(processor, rav));
            }
        }

        return riskAdjustmentVariables;
    }

    private List<HumanReadablePopulationCriteriaModel> getPopulationCriteriaModels(XmlProcessor processor) throws XPathExpressionException {
        List<HumanReadablePopulationCriteriaModel> groups = new ArrayList<>();

        NodeList groupNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/measureGrouping/group");
        for (int i = 0; i < groupNodes.getLength(); i++) {
            resetPopulationMaps();
            Node group = groupNodes.item(i);

            int populationCriteriaNumber = Integer.parseInt(group.getAttributes().getNamedItem("sequence").getNodeValue());

            List<HumanReadablePopulationModel> populations = new ArrayList<>();

            NodeList populationNodes = group.getChildNodes();
            int popCount = populationNodes.getLength();
            for (int k = 0; k < popCount; k++) {
                Node clauseNode = populationNodes.item(k);
                String popType = clauseNode.getAttributes().getNamedItem("type").getNodeValue();
                countSimilarPopulationsInGroup(populationCriteriaNumber, popType, processor);
            }

            for (int j = 0; j < popCount; j++) {
                Node populationNode = populationNodes.item(j);
                HumanReadablePopulationModel population = getPopulationCriteria(processor, populationNode);
                populations.add(population);
            }

            populations = sortPopulations(populations);

            String displayName = "Population Criteria " + populationCriteriaNumber;
            HumanReadablePopulationCriteriaModel populationCriteria = new HumanReadablePopulationCriteriaModel(displayName, populations, populationCriteriaNumber);
            groups.add(populationCriteria);
        }


        groups.sort(Comparator.comparing(HumanReadablePopulationCriteriaModel::getSequence));
        return groups;
    }

    private void countSimilarPopulationsInGroup(int groupNo, String popTyp, XmlProcessor processor) {
        if (!populationCountMap.containsKey(popTyp)) {
            String xPathString = "count(//group[@sequence='" + groupNo + "']/clause[@type='" + popTyp + "'])";
            try {
                int count = processor.getNodeCount(processor.getOriginalDoc(), xPathString);
                populationCountMap.put(popTyp, count);
            } catch (XPathExpressionException e) {
                log.error("Error in HumanReadableGenerator::countSimilarPopulationsInGroup: " + e.getMessage(), e);
            }
        }
    }

    private List<HumanReadablePopulationModel> sortPopulations(List<HumanReadablePopulationModel> populations) {
        List<HumanReadablePopulationModel> sortedPopulations = new ArrayList<>();

        for (String populationType : POPULATION_NAME_ARRAY) {
            sortedPopulations.addAll(getPopulationsByType(populationType, populations));
        }

        return sortedPopulations;
    }

    private List<HumanReadablePopulationModel> getPopulationsByType(String type, List<HumanReadablePopulationModel> populations) {
        List<HumanReadablePopulationModel> models = new ArrayList<>();


        for (HumanReadablePopulationModel model : populations) {
            if (type.equals(model.getType())) {
                models.add(model);
            }
        }

        return models;
    }

    private String getPopulationNameByUUID(String uuid, XmlProcessor processor) throws XPathExpressionException {
        Node population = processor.findNode(processor.getOriginalDoc(), "//clause[@uuid=\"" + uuid + "\"]");
        String name = "";
        if (population != null) {
            String type = population.getAttributes().getNamedItem("type").getNodeValue();
            name = populationCountMap.containsKey(type) ? getNameForMeasurePackaging(type) : getNameForPopulationWorkspaceViewHR(type, population);
        }
        return name;
    }

    private String getNameForMeasurePackaging(String type) {
        int numberOfPopulationsWithSameType = populationCountMap.get(type);
        // if there is only one of the population kind, then we only want to display the population name (without a number attached to it)
        return (numberOfPopulationsWithSameType == 1) ? getPopulationNameByType(type) : getPopulationNameByTypeAndNum(type);

    }

    private String getNameForPopulationWorkspaceViewHR(String type, Node population) {
        int numberOfPopulationsWithSameType = countSimilarPopulationsInGroup(type, population.getParentNode());
        return (numberOfPopulationsWithSameType == 1) ? getPopulationNameByType(type) : population.getAttributes().getNamedItem("displayName").getNodeValue();
    }

    private int countSimilarPopulationsInGroup(String type, Node node) {
        int count = 0;
        NodeList childClauses = node.getChildNodes();
        if (childClauses != null) {
            int length = childClauses.getLength();
            for (int i = 0; i < length; i++) {
                Node clauseNode = childClauses.item(i);
                String popType = clauseNode.getAttributes().getNamedItem("type").getNodeValue();
                if (popType.equals(type)) {
                    count++;
                }
            }
        }
        return count;
    }

    private String getPopulationNameByTypeAndNum(String type) {
        int total = populationCountMap.get(type);
        int count = popCountMultipleMap.containsKey(type) ? popCountMultipleMap.get(type) : 0;

        if (total > count) {
            count++;
            popCountMultipleMap.put(type, count);
        }

        return getPopulationNameByType(type) + " " + count;
    }

    private String getPopulationNameByType(String type) {
        String populationName = "";
        if (MatConstants.INITIAL_POPULATION.equals(type)) {
            populationName = "Initial Population";
        } else if (MatConstants.MEASURE_POPULATION.equals(type)) {
            populationName = "Measure Population";
        } else if (MatConstants.MEASURE_POPULATION_EXCLUSIONS.equals(type)) {
            populationName = "Measure Population Exclusions";
        } else if (MatConstants.MEASURE_OBSERVATION_POPULATION.equals(type)) {
            populationName = "Measure Observation";
        } else if (MatConstants.STRATUM.equals(type)) {
            populationName = "Stratification";
        } else if (MatConstants.DENOMINATOR.equals(type)) {
            populationName = "Denominator";
        } else if (MatConstants.DENOMINATOR_EXCLUSIONS.equals(type)) {
            populationName = "Denominator Exclusions";
        } else if (MatConstants.DENOMINATOR_EXCEPTIONS.equals(type)) {
            populationName = "Denominator Exceptions";
        } else if (MatConstants.NUMERATOR.equals(type)) {
            populationName = "Numerator";
        } else if (MatConstants.NUMERATOR_EXCLUSIONS.equals(type)) {
            populationName = "Numerator Exclusions";
        }
        return populationName;
    }

    private HumanReadablePopulationModel getPopulationCriteria(XmlProcessor processor, Node populationNode)
            throws XPathExpressionException {
        String expressionName = "";
        String expressionUUID = "";
        String logic = "";
        String aggregation = null;
        String associatedPopulationName = "";
        boolean isInGroup = true;

        if (populationNode.getAttributes().getNamedItem("associatedPopulationUUID") != null) {
            String uuid = populationNode.getAttributes().getNamedItem("associatedPopulationUUID").getNodeValue();
            associatedPopulationName = populationNameMap.get(uuid);
        }


        if (populationNode.getAttributes().getNamedItem("isInGrouping") != null) {
            isInGroup = Boolean.parseBoolean(populationNode.getAttributes().getNamedItem("isInGrouping").getNodeValue());
        }

        String populationUUID = populationNode.getAttributes().getNamedItem("uuid").getNodeValue();
        String populationName = getPopulationNameByUUID(populationUUID, processor);
        populationNameMap.put(populationUUID, populationName);

        String type = populationNode.getAttributes().getNamedItem("type").getNodeValue();
        if (populationName.contains("Measure Observation")) {

            Node functionNode = null;
            Node aggregationNode = null;
            if (populationNode.getFirstChild() != null) {
                if ("cqlaggfunction".equals(populationNode.getFirstChild().getNodeName())) {
                    aggregationNode = populationNode.getFirstChild();
                    aggregation = aggregationNode.getAttributes().getNamedItem("displayName").getNodeValue();
                    functionNode = aggregationNode.getFirstChild();
                } else {
                    functionNode = populationNode.getFirstChild();
                }


                expressionName = functionNode.getAttributes().getNamedItem("displayName").getNodeValue();
                expressionName = getPopulationNameByType(expressionName);
                expressionUUID = functionNode.getAttributes().getNamedItem("uuid").getNodeValue();
                logic = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//function[@id='" + expressionUUID + "']/logic").getTextContent();


                if (aggregation != null) {

                    logic = aggregation + " (\n  " + logic + "\n)"; // for measures, add in the aggregation
                }
            }
        } else {
            Node definitionNode = populationNode.getFirstChild();
            if (populationName.contains("Stratum")) {
                if (populationName.equals("stratum")) {
                    populationName = populationName + " 1"; // we need to enumerate the the stratum, and if there is only one stratum, it doesn't have a number in the name....
                }

                populationName = populationName.replace("Stratum", "Stratification"); // for some reason the simple xml uses "stratum" but it really should be "Stratification"
            }

            if (definitionNode != null) {
                expressionName = definitionNode.getAttributes().getNamedItem("displayName").getNodeValue();
                expressionUUID = definitionNode.getAttributes().getNamedItem("uuid").getNodeValue();
                logic = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//definition[@id='" + expressionUUID + "']/logic").getTextContent();
            }
        }

        populationNode.getAttributes().getNamedItem("displayName").setNodeValue(populationName);
        HumanReadablePopulationModel population = new HumanReadablePopulationModel(populationName, logic, expressionName, expressionUUID, aggregation, associatedPopulationName, isInGroup, type);
        return population;
    }

    private void resetPopulationMaps() {
        populationCountMap.clear();
        popCountMultipleMap.clear();
        populationNameMap.clear();
    }
}
