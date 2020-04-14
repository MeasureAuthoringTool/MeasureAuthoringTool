package mat.server;

import mat.client.shared.CQLWorkSpaceConstants;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.service.impl.XMLMarshalUtil;
import mat.server.util.MeasureUtility;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class CQLUtilityClass {

    private static final Log logger = LogFactory.getLog(CQLUtilityClass.class);

    private static final String PATIENT = "Patient";

    private static final String POPULATION = "Population";

    public static final String VERSION = " version ";

    private CQLUtilityClass() {
        throw new IllegalStateException("CQL Utility class");
    }

    public static String replaceFirstWhitespaceInLineForExpression(String expression) {
        Scanner scanner = new Scanner(expression);
        StringBuilder builder = new StringBuilder();

        // go through and rebuild the the format
        // this will remove the first whitespace in a line so
        // it properly displays in the ace editor.
        // without doing this, the the ace editor display
        // would be indented one too many
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.isEmpty()) {
                if (line.startsWith(CQLUtilityClass.getWhiteSpaceString(true, 2))) {
                    line = line.replaceFirst(CQLUtilityClass.getWhiteSpaceString(true, 2), "");
                }
            }

            builder.append(line + "\n");
        }

        scanner.close();
        return builder.toString();
    }

    public static String getWhiteSpaceString(boolean isSpaces, int indentSize) {
        String whiteSpaceString = "";
        for (int i = 0; i < indentSize; i++) {
            if (isSpaces) {
                whiteSpaceString += " ";
            } else {
                whiteSpaceString += "\t";
            }
        }

        return whiteSpaceString;
    }


    public static Pair<String, Integer> getCqlString(CQLModel cqlModel, String toBeInserted) {
        return getCqlString(cqlModel, toBeInserted, true, 2);
    }

    public static Pair<String, Integer> getCqlString(CQLModel cqlModel, String toBeInserted, boolean isSpaces, int indentSize) {
        boolean isFhir = StringUtils.equals(cqlModel.getUsingModel(), "FHIR");
        AtomicInteger size = new AtomicInteger(0);
        StringBuilder cqlStr = new StringBuilder();
        // library Name and Using
        cqlStr.append(CQLUtilityClass.createLibraryNameSection(cqlModel));

        //includes
        cqlStr.append(CQLUtilityClass.createIncludesSection(cqlModel.getCqlIncludeLibrarys()));

        //CodeSystems
        // Not very clean but they use the code list instead of the code system section for codes.
        // This adds all kinds of complexity but is needed for backwards compatibility.
        cqlStr.append(CQLUtilityClass.createCodeSystemsSection(cqlModel.getCodeList()));

        //Valuesets
        cqlStr.append(CQLUtilityClass.createValueSetsSection(cqlModel.getValueSetList(), isFhir));

        //Codes
        cqlStr.append(CQLUtilityClass.createCodesSection(cqlModel.getCodeList()));

        // parameters
        CQLUtilityClass.createParameterSection(cqlModel.getCqlParameters(), cqlStr, toBeInserted, size);

        // Definitions and Functions by Context
        if (!cqlModel.getDefinitionList().isEmpty() || !cqlModel.getCqlFunctions().isEmpty()) {
            getDefineAndFunctionsByContext(cqlModel.getDefinitionList(),
                    cqlModel.getCqlFunctions(),
                    cqlStr,
                    toBeInserted,
                    isSpaces,
                    indentSize,
                    size,
                    isFhir);
        } else {
            cqlStr.append("context").append(" " + PATIENT).append("\n\n");
        }
        return Pair.of(cqlStr.toString(), size.get());
    }

    private static String createLibraryNameSection(CQLModel cqlModel) {
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotBlank(cqlModel.getLibraryName())) {

            sb.append("library ").append(cqlModel.getLibraryName());
            sb.append(VERSION).append("'" + cqlModel.getVersionUsed()).append("'");
            sb.append(System.lineSeparator()).append(System.lineSeparator());

            if (StringUtils.isNotBlank(cqlModel.getLibraryComment())) {
                sb.append(createCommentString(cqlModel.getLibraryComment()));
                sb.append(System.lineSeparator()).append(System.lineSeparator());
            }

            sb.append("using ").append(cqlModel.getUsingModel());
            sb.append(VERSION);
            sb.append("'").append(cqlModel.getUsingModelVersion()).append("'");
            sb.append("\n\n");
        }

        return sb.toString();
    }

    /**
     * Gets the define and funcs by context.
     *
     * @param defineList    the define list
     * @param functionsList the functions list
     * @param cqlStr        the cql str
     * @param indentSize
     * @param isSpaces
     * @return the define and funcs by context
     */
    private static StringBuilder getDefineAndFunctionsByContext(
            List<CQLDefinition> defineList,
            List<CQLFunctions> functionsList,
            StringBuilder cqlStr,
            String toBeInserted,
            boolean isSpaces,
            int indentSize,
            AtomicInteger size,
            boolean isFhir) {
        Map<String, List<CQLDefinition>> contextToDefMap = new HashMap<>();
        Map<String, List<CQLFunctions>> funcToContextMap = new HashMap<>();

        if (!CollectionUtils.isEmpty(defineList)) {
            defineList.forEach(d -> {
                if (isFhir) {
                    addToListMap(contextToDefMap, StringUtils.defaultString(d.getContext()), d);
                } else {
                    //For some reason in QDM it defaults to population.
                    if (StringUtils.equalsIgnoreCase(d.getContext(), PATIENT)) {
                        addToListMap(contextToDefMap, PATIENT, d);
                    } else {
                        addToListMap(contextToDefMap, POPULATION, d);
                    }
                }
            });
        }
        if (!CollectionUtils.isEmpty(functionsList)) {
            functionsList.forEach(f -> {
                if (isFhir) {
                    addToListMap(funcToContextMap, StringUtils.defaultString(f.getContext()), f);
                } else {
                    //For some reason in QDM it defaults to population.
                    if (StringUtils.equalsIgnoreCase(f.getContext(), PATIENT)) {
                        addToListMap(funcToContextMap, PATIENT, f);
                    } else {
                        addToListMap(funcToContextMap, POPULATION, f);

                    }
                }
            });
        }

        Set<String> keys = new HashSet<>();
        keys.addAll(contextToDefMap.keySet());
        keys.addAll(funcToContextMap.keySet());

        keys.forEach(k -> {
            getDefineAndFunctionsByContext(contextToDefMap.get(k), funcToContextMap.get(k), k, cqlStr, toBeInserted, isSpaces, indentSize, size);
        });

        return cqlStr;
    }

    /**
     * Gets the define and functions by context.
     *
     * @param definitionList the definition list
     * @param functionsList  the functions list
     * @param context        the context
     * @param cqlStr         the cql str
     * @param indentSize
     * @param isSpaces
     * @return the define and functions by context
     */
    private static StringBuilder getDefineAndFunctionsByContext(
            List<CQLDefinition> definitionList,
            List<CQLFunctions> functionsList, String context,
            final StringBuilder cqlStr, String toBeInserted, boolean isSpaces, int indentSize, AtomicInteger size) {

        if (StringUtils.isNotBlank(context)) {
            cqlStr.append("context").append(" " + context).append("\n\n");
        }

        if (!CollectionUtils.isEmpty(definitionList)) {
            definitionList.forEach(definition -> {
                if (StringUtils.isNotBlank(definition.getCommentString())) {
                    cqlStr.append(createCommentString(definition.getCommentString()));
                    cqlStr.append(System.lineSeparator());
                }

                String def = "define " + "\"" + definition.getName() + "\"";

                cqlStr.append(def + ":\n");
                cqlStr.append(getWhiteSpaceString(isSpaces, indentSize) + definition.getLogic().replaceAll("\\n", "\n" + getWhiteSpaceString(isSpaces, indentSize)));
                cqlStr.append("\n\n");

                // if the the def we just appended is the current one, then
                // find the size of the file at that time. ;-
                // This will give us the end line of the definition we are trying to insert.
                if (def.equalsIgnoreCase(toBeInserted)) {
                    size.set(getEndLine(cqlStr.toString()));
                }
            });
        }
        if (!CollectionUtils.isEmpty(functionsList)) {
            functionsList.forEach(function -> {
                if (StringUtils.isNotBlank(function.getCommentString())) {
                    cqlStr.append(createCommentString(function.getCommentString()));
                    cqlStr.append(System.lineSeparator());
                }

                String func = "define function " + "\"" + function.getName() + "\"";

                cqlStr.append(func + "(");
                if (function.getArgumentList() != null && !function.getArgumentList().isEmpty()) {
                    for (CQLFunctionArgument argument : function.getArgumentList()) {
                        StringBuilder argumentType = new StringBuilder();
                        if (argument.getArgumentType().equalsIgnoreCase("QDM Datatype")) {
                            argumentType = argumentType.append("\"").append(argument.getQdmDataType());
                            if (argument.getAttributeName() != null) {
                                argumentType = argumentType.append(".").append(argument.getAttributeName());
                            }
                            argumentType = argumentType.append("\"");
                        } else if (argument.getArgumentType().equalsIgnoreCase(
                                CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
                            argumentType = argumentType.append(argument.getOtherType());
                        } else {
                            argumentType = argumentType.append(argument.getArgumentType());
                        }
                        cqlStr.append(argument.getArgumentName() + " " + argumentType + ", ");
                    }
                    cqlStr.deleteCharAt(cqlStr.length() - 2);
                }

                cqlStr.append("):\n" + getWhiteSpaceString(isSpaces, indentSize) + function.getLogic().replaceAll("\\n", "\n" + getWhiteSpaceString(isSpaces, indentSize)));
                cqlStr.append("\n\n");

                // if the the func we just appended is the current one, then
                // find the size of the file at that time.
                // This will give us the end line of the function we are trying to insert.
                if (func.equalsIgnoreCase(toBeInserted)) {
                    size.set(getEndLine(cqlStr.toString()));
                }
            });
        }
        return cqlStr;
    }


    public static CQLModel getCQLModelFromXML(String xmlString) {
        CQLModel cqlModel = new CQLModel();
        XmlProcessor measureXMLProcessor = new XmlProcessor(xmlString);
        String cqlLookUpXMLString = measureXMLProcessor.getXmlByTagName("cqlLookUp");

        if (StringUtils.isNotBlank(cqlLookUpXMLString)) {
            try {
                XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
                cqlModel = (CQLModel) xmlMarshalUtil.convertXMLToObject("CQLModelMapping.xml", cqlLookUpXMLString, CQLModel.class);
            } catch (Exception e) {
                logger.info("Error while getting codeystems", e);
            }
        }

        if (!cqlModel.getValueSetList().isEmpty()) {
            cqlModel.setValueSetList(filterValuesets(cqlModel.getValueSetList()));
            ArrayList<CQLQualityDataSetDTO> valueSetsList = new ArrayList<CQLQualityDataSetDTO>();
            valueSetsList.addAll(cqlModel.getValueSetList());
            cqlModel.setAllValueSetAndCodeList(valueSetsList);
        }

        if (!cqlModel.getCodeList().isEmpty()) {
            sortCQLCodeDTO(cqlModel.getCodeList());
            //Combine Codes and Value sets in allValueSetList for UI
            List<CQLQualityDataSetDTO> dtoList = convertCodesToQualityDataSetDTO(cqlModel.getCodeList());
            if (!dtoList.isEmpty()) {
                cqlModel.getAllValueSetAndCodeList().addAll(dtoList);
            }
        }
        return cqlModel;
    }

    public static String getXMLFromCQLModel(CQLModel cqlModel) {
        String xml = "";

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();) {
            Mapping mapping = new Mapping();
            mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLModelMapping.xml"));
            Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
            marshaller.setMapping(mapping);
            marshaller.marshal(cqlModel);
            xml = stream.toString();
        } catch (MarshalException | ValidationException | IOException | MappingException e) {
            e.printStackTrace();
        }


        return xml;
    }

    public static void getValueSet(CQLModel cqlModel, String cqlLookUpXMLString) {
        CQLQualityDataModelWrapper valuesetWrapper;
        try {
            XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
            valuesetWrapper = (CQLQualityDataModelWrapper) xmlMarshalUtil.convertXMLToObject("ValueSetsMapping.xml", cqlLookUpXMLString, CQLQualityDataModelWrapper.class);
            if (!valuesetWrapper.getQualityDataDTO().isEmpty()) {
                cqlModel.setValueSetList(filterValuesets(valuesetWrapper.getQualityDataDTO()));
            }
        } catch (Exception e) {
            logger.info("Error while getting valueset :" + e.getMessage());
        }

    }


    private static List<CQLQualityDataSetDTO> convertCodesToQualityDataSetDTO(List<CQLCode> codeList) {
        List<CQLQualityDataSetDTO> convertedCQLDataSetList = new ArrayList<CQLQualityDataSetDTO>();
        for (CQLCode tempDataSet : codeList) {
            CQLQualityDataSetDTO convertedCQLDataSet = new CQLQualityDataSetDTO();
            convertedCQLDataSet.setName(tempDataSet.getName());
            convertedCQLDataSet.setCodeSystemName(tempDataSet.getCodeSystemName());
            convertedCQLDataSet.setCodeSystemOID(tempDataSet.getCodeSystemOID());

            convertedCQLDataSet.setCodeIdentifier(tempDataSet.getCodeIdentifier());
            convertedCQLDataSet.setId(tempDataSet.getId());
            convertedCQLDataSet.setOid(tempDataSet.getCodeOID());
            convertedCQLDataSet.setVersion(tempDataSet.getCodeSystemVersion());
            convertedCQLDataSet.setDisplayName(tempDataSet.getDisplayName());
            convertedCQLDataSet.setSuffix(tempDataSet.getSuffix());

            convertedCQLDataSet.setReadOnly(tempDataSet.isReadOnly());

            convertedCQLDataSet.setType("code");
            convertedCQLDataSetList.add(convertedCQLDataSet);


        }
        return convertedCQLDataSetList;

    }

    private static int getEndLine(String cqlString) {

        Scanner scanner = new Scanner(cqlString);

        int endLine = -1;
        while (scanner.hasNextLine()) {
            endLine++;
            scanner.nextLine();
        }

        scanner.close();
        return endLine;
    }

    public static List<CQLQualityDataSetDTO> sortCQLQualityDataSetDto(List<CQLQualityDataSetDTO> cqlQualityDataSetDTOs) {

        cqlQualityDataSetDTOs.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
        return cqlQualityDataSetDTOs;
    }

    public static List<CQLCode> sortCQLCodeDTO(List<CQLCode> cqlCodes) {

        cqlCodes.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
        return cqlCodes;
    }

    private static List<CQLQualityDataSetDTO> filterValuesets(List<CQLQualityDataSetDTO> cqlValuesets) {

        cqlValuesets.removeIf(c -> c.getDataType() != null &&
                (c.getDataType().equalsIgnoreCase("Patient characteristic Birthdate")
                        || c.getDataType().equalsIgnoreCase("Patient characteristic Expired")));

        sortCQLQualityDataSetDto(cqlValuesets);

        return cqlValuesets;
    }

    private static String createIncludesSection(List<CQLIncludeLibrary> includeLibList) {
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(includeLibList)) {
            for (CQLIncludeLibrary includeLib : includeLibList) {
                sb.append("include ").append(includeLib.getCqlLibraryName());
                sb.append(VERSION).append("'").append(MeasureUtility.formatVersionText(includeLib.getVersion())).append("' ");
                sb.append("called ").append(includeLib.getAliasName());
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String createCodeSystemsSection(List<CQLCode> codeSystemList) {
        StringBuilder sb = new StringBuilder();

        List<String> codeSystemAlreadyUsed = new ArrayList<>();

        if (!CollectionUtils.isEmpty(codeSystemList)) {
            for (CQLCode code : codeSystemList) {
                if (code.getCodeSystemOID() != null && !code.getCodeSystemOID().isEmpty() && !"null".equals(code.getCodeSystemOID())) {
                    boolean isUrlCodeSystem = StringUtils.startsWith(code.getCodeSystemOID(), "http");
                    if (isUrlCodeSystem) {
                        if (!codeSystemAlreadyUsed.contains(code.getCodeSystemName())) {
                            // Fhir4 code system
                            // codesystem "SNOMEDCT:2017-09": 'http://snomed.info/sct/731000124108' version 'http://snomed.info/sct/731000124108/version/201709'
                            //       or
                            // codesystem "SNOMEDCT:2017-09": 'http://snomed.info/sct/731000124108'
                            String csName = code.getCodeSystemName();
                            String csUri = code.getCodeSystemOID();
                            String csVersionUri = code.getCodeSystemVersionUri();

                            if (code.isIsCodeSystemVersionIncluded()) {
                                csName = csName + ":" + code.getCodeSystemVersion();
                            }
                            sb.append("codesystem \"").append(csName).append('"').append(": ").
                                    append("'").append(csUri).append("' ");
                            if (StringUtils.isNotBlank(csVersionUri)) {
                                sb.append("version '" + csVersionUri + "'");
                            }
                            sb.append("\n");
                            codeSystemAlreadyUsed.add(csName);
                        }
                    } else {
                        // Legacy OID system.
                        String codeSysStr = code.getCodeSystemName();
                        String codeSysVersion = "";

                        if (code.isIsCodeSystemVersionIncluded()) {
                            codeSysStr = codeSysStr + ":" + code.getCodeSystemVersion().replaceAll(" ", "%20");
                            codeSysVersion = "version 'urn:hl7:version:" + code.getCodeSystemVersion() + "'";
                        }

                        if (!codeSystemAlreadyUsed.contains(codeSysStr)) {
                            sb.append("codesystem \"").append(codeSysStr).append('"').append(": ");
                            sb.append("'urn:oid:").append(code.getCodeSystemOID()).append("' ");
                            sb.append(codeSysVersion);
                            sb.append("\n");

                            codeSystemAlreadyUsed.add(codeSysStr);
                        }
                    }
                }
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private static String createValueSetsSection(List<CQLQualityDataSetDTO> valueSetList, boolean isFhir) {
        StringBuilder sb = new StringBuilder();

        List<String> valueSetAlreadyUsed = new ArrayList<>();

        if (!CollectionUtils.isEmpty(valueSetList)) {

            for (CQLQualityDataSetDTO valueset : valueSetList) {

                if (!valueSetAlreadyUsed.contains(valueset.getName())) {
                    if (isFhir) {
                        sb.append("valueset ").append('"').append(valueset.getName()).append('"');
                        sb.append(": '").append(valueset.getOid()).append("' ");
                    } else {
                        String version = valueset.getVersion().replaceAll(" ", "%20");
                        sb.append("valueset ").append('"').append(valueset.getName()).append('"');
                        sb.append(": 'urn:oid:").append(valueset.getOid()).append("' ");
                        //Check if QDM has expansion identifier or not.
                        if (StringUtils.isNotBlank(version) && !version.equals("1.0")) {
                            sb.append("version 'urn:hl7:version:").append(version).append("' ");
                        }
                    }
                    sb.append("\n");
                    valueSetAlreadyUsed.add(valueset.getName());
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String createCodesSection(List<CQLCode> codeList) {

        StringBuilder sb = new StringBuilder();

        List<String> codeAlreadyUsed = new ArrayList<String>();

        if (!CollectionUtils.isEmpty(codeList)) {

            for (CQLCode code : codeList) {
                String codeStr = '"' + code.getCodeName() + '"' + ": " + "'" + code.getCodeOID() + "'";
                String codeSysStr = code.getCodeSystemName();
                if (code.isIsCodeSystemVersionIncluded()) {
                    codeSysStr = codeSysStr + ":" + code.getCodeSystemVersion().replaceAll(" ", "%20");
                }

                if (!codeAlreadyUsed.contains(codeStr)) {
                    sb.append("code ").append(codeStr).append(" ").append("from ");
                    sb.append('"').append(codeSysStr).append('"').append(" ");
                    if (StringUtils.isNotEmpty(code.getDisplayName())) {
                        sb.append("display " + "'" + code.getDisplayName() + "'");
                    }
                    sb.append("\n");
                    codeAlreadyUsed.add(codeStr);
                }
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private static StringBuilder createParameterSection(List<CQLParameter> paramList,
                                                        StringBuilder cqlStr,
                                                        String toBeInserted,
                                                        AtomicInteger size) {
        if (!CollectionUtils.isEmpty(paramList)) {

            for (CQLParameter parameter : paramList) {

                String param = "parameter " + "\"" + parameter.getName() + "\"";

                if (StringUtils.isNotBlank(parameter.getCommentString())) {
                    cqlStr.append(createCommentString(parameter.getCommentString()));
                    cqlStr.append(System.lineSeparator());
                }

                cqlStr.append(param + " " + parameter.getLogic());
                cqlStr.append("\n");

                // if the the param we just appended is the current one, then
                // find the size of the file at that time.
                // This will give us the end line of the parameter we are trying to insert.
                if (param.equalsIgnoreCase(toBeInserted)) {
                    size.set(getEndLine(cqlStr.toString()));
                }

            }

            cqlStr.append("\n");
        }

        return cqlStr;
    }

    public static String createCommentString(String comment) {
        StringBuilder sb = new StringBuilder();
        sb.append("/*").append(comment).append("*/");
        return sb.toString();
    }

    private static <T, O> void addToListMap(Map<O, List<T>> map, O key, T newElem) {
        if (map != null) {
            List<T> l = map.get(key);
            if (CollectionUtils.isEmpty(l)) {
                l = new ArrayList<T>();
                map.put(key, l);
            }
            l.add(newElem);
        }
    }
}
