package mat.cql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import mat.client.shared.MatException;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static mat.cql.CqlStringUtils.areValidAscendingIndexes;
import static mat.cql.CqlStringUtils.chomp1;
import static mat.cql.CqlStringUtils.indexOf;
import static mat.cql.CqlStringUtils.newGuid;
import static mat.cql.CqlStringUtils.nextCharMatching;
import static mat.cql.CqlStringUtils.nextCharNotMatching;
import static mat.cql.CqlStringUtils.nextNonWhitespace;
import static mat.cql.CqlStringUtils.nextQuotedString;
import static mat.cql.CqlStringUtils.nextTickedString;
import static mat.cql.CqlStringUtils.removeCqlBlockComments;
import static mat.cql.CqlStringUtils.removeLineComments;
import static mat.cql.CqlStringUtils.trimUrn;

/**
 * A parser that parses CQL and converts it into Mat XML.
 */
@Getter
@Setter
@ToString
@Slf4j
public class CqlToMatXml {
    private static final Map<String, String> nameToGlobalLibId = new HashMap<>();

    static {
        //TO DO move this into properties config or a DB lookup eventually.
        nameToGlobalLibId.put("NCQA_Common", "NCQA-Common-FHIR4-5-1-000");
        nameToGlobalLibId.put("NCQA_Common_FHIR4", "NCQA-Common-FHIR4-5-1-000");
        nameToGlobalLibId.put("MATGlobalCommonFunctions_FHIR4", "MATGlobalCommonFunctions-FHIR4-4-0-000");
        nameToGlobalLibId.put("AdultOutpatientEncounters_FHIR4", "AdultOutpatientEncounters-FHIR4-1-1-000");
        nameToGlobalLibId.put("AdvancedIllnessandFrailtyExclusion_FHIR4", "AdvancedIllnessandFrailtyExclusion-FHIR4-4-0-000");
        nameToGlobalLibId.put("FHIRHelpers", "FHIRHelpers-4-0-0");
        nameToGlobalLibId.put("Hospice_FHIR4", "Hospice-FHIR4-1-0-000");
        nameToGlobalLibId.put("SupplementalDataElements_FHIR4", "SupplementalDataElements-FHIR4-1-0-0");
        nameToGlobalLibId.put("TJCOverall_FHIR4", "TJCOverall-FHIR4-4-0-000");
        nameToGlobalLibId.put("VTEICU_FHIR4", "VTEICU-FHIR4-3-1-000");
    }

    private static final char NEWLINE = '\n';
    private static final char COMMA = ',';
    private static final char COLON = ':';
    private static final char QUOTE = '"';
    private static final char SPACE = ' ';
    private static final char OPEN_PAREN = '(';
    private static final char CLOSE_PAREN = ')';

    private static final String LIB_TOKEN = "library ";
    private static final String INCLUDE_TOKEN = NEWLINE + "include ";
    private static final String QDM_TOKEN = NEWLINE + "using FHIR ";
    private static final String CONTEXT_TOKEN = NEWLINE + "context ";
    private static final String CODESYSTEM_TOKEN = NEWLINE + "codesystem ";
    private static final String VALUESET_TOKEN = NEWLINE + "valueset ";
    private static final String CODE_TOKEN = NEWLINE + "code ";
    private static final String PARAMETER_TOKEN = NEWLINE + "parameter ";
    private static final String DEFINE_TOKEN = NEWLINE + "define ";
    private static final String FUNCTION_TOKEN = "function";
    private static final String DEFINE_FUNCTION_TOKEN = DEFINE_TOKEN + FUNCTION_TOKEN;

    private static final String WHITESPACE_REGEX = "\\s";

    private String convertedCql;
    private CQLModel sourceModel;
    private CQLModel destinationModel = new CQLModel();
    private int index;

    public interface RepeatingLineProducer<T> {
        T parse(String s);
    }

    public CqlToMatXml(CQLModel sourceCqlModel, String convertedCql) {
        this.convertedCql = convertedCql;
        this.sourceModel = sourceCqlModel;
    }

    public CQLModel convert() throws MatException {
        try {
            //Remove all comments.
            convertedCql = removeCqlBlockComments(convertedCql);
            convertedCql = removeLineComments(convertedCql);

            //Populate root object first:
            processLibraryTag();
            processContext();
            processFhirVersion();

            //Now populate child nodes:
            processIncludeLibs();
            processCodeSystems();
            processValueSets();
            processCodes();
            processParameters();
            processCQLDefinitions();
            processFunctions();
            return destinationModel;
        } catch (RuntimeException e) {
            throw new MatException("Error converting mat xml.", e);
        }
    }

    /**
     * Parses out the CQLLib from the cql.
     */
    private void processLibraryTag() {
        //library CWP_HEDIS_2020_CARSON version '1.0.000'
        int endFirstLine = indexOf(convertedCql, NEWLINE, 0);
        if (areValidAscendingIndexes(endFirstLine)) {
            String firstLine = convertedCql.substring(0, endFirstLine);
            if (!firstLine.startsWith(LIB_TOKEN)) {
                throw new IllegalArgumentException("First line did not start with " + LIB_TOKEN);
            } else {
                String[] words = firstLine.split(" ");

                if (words.length == 4) {
                    destinationModel.setLibraryName(words[1]);
                    destinationModel.setUsingModel(destinationModel.getUsingModel());
                    destinationModel.setVersionUsed(destinationModel.getVersionUsed());
                    destinationModel.setVersionUsed(chomp1(words[3]));
                } else {
                    throw new IllegalArgumentException("Invalid library encountered: " + firstLine);
                }
            }
        } else {
            throw new IllegalArgumentException("First line did not start with " + LIB_TOKEN);
        }
    }

    /**
     * Parses out the QDM version from the cql.
     */
    private void processFhirVersion() {
        //using FHIR version '4.0.0'
        String qdm = parseSingletonLine(QDM_TOKEN);
        ParseResult version = nextTickedString(qdm, 0);

        if (areValidAscendingIndexes(version)) {
            destinationModel.setUsingModelVersion(version.getString());
            destinationModel.setUsingModel("FHIR");
        } else {
            throw new IllegalArgumentException("Invalid " + QDM_TOKEN + " encounter: " + qdm);
        }
    }

    /**
     * Parses out all the includes from the cql.
     */
    private void processIncludeLibs() {
        destinationModel.setCqlIncludeLibrarys(parseRepeatingLine(INCLUDE_TOKEN, s -> {
            // include NCQA_Common version '5.1.000' called Common
            String[] words = s.split(" ");
            if (words.length != 6) {
                throw new IllegalArgumentException("Invalid include encountered: " + s);
            }
            CQLIncludeLibrary result = new CQLIncludeLibrary();
            result.setId(newGuid());
            result.setCqlLibraryName(words[1]);
            result.setVersion(chomp1(words[3]));
            result.setAliasName(words[5]);
            result.setQdmVersion(destinationModel.getUsingModelVersion());
            result.setLibraryModelType("FHIR");

            result.setCqlLibraryId(nameToGlobalLibId.get(result.getCqlLibraryName()));
            if (result.getCqlLibraryId() == null) {
                //Non global library case.
                //We are not handling this in this release so return null for now.
                //Eventually this will have to convert the library also.
                result = null;
            }
            return result;
        }));
    }

    /**
     * Parses all of the code systems found in the cql.
     */
    private void processCodeSystems() {
        destinationModel.setCodeSystemList(parseRepeatingLine(CODESYSTEM_TOKEN, s -> {
            //codesystem "LOINC": 'urn:oid:2.16.840.1.113883.6.1'
            ParseResult systemName = nextQuotedString(s, 0);
            ParseResult version = nextTickedString(s, systemName.getEndIndex());

            if (areValidAscendingIndexes(systemName, version)) {
                //For now these should be identical to the sourceCqlModel versions.
                //Just find the corresponding one by code system name and use that.
                String sysName = systemName.getString();
                String ver = trimUrn(version.getString());
                log.info("sysName=" + sysName + " version=" + ver);
                return findExisting(sourceModel.getCodeSystemList(),
                        cs -> StringUtils.equals(systemName.getString(), cs.getCodeSystemName()),
                        "Could not find sourceCqlModel.codeSystemList for " + sysName);
            } else {
                throw new IllegalArgumentException("Invalid include encountered: " + s);
            }
        }));
    }

    /**
     * Parses all of the valuesets found in the cql.
     */
    private void processValueSets() {
        destinationModel.setValueSetList(parseRepeatingLine(VALUESET_TOKEN, s -> {
            //valueset "Ethnicity": 'urn:oid:2.16.840.1.114222.4.11.837'
            ParseResult type = nextQuotedString(s, 0);
            ParseResult version = nextTickedString(s, type.getEndIndex());

            if (areValidAscendingIndexes(type, version)) {
                //For now these should be identical to the sourceCqlModel versions.
                //Just find the corresponding one by type and use that.
                return findExisting(sourceModel.getValueSetList(),
                        vs -> StringUtils.equals(type.getString(), vs.getCodeListName()),
                        "Could not find sourceCqlModel.valueSetList for " + type.getString());
            } else {
                throw new IllegalArgumentException("Invalid valueset encountered: '" + s);
            }
        }));
    }

    /**
     * Parses all of the codes out of the cql.
     */
    private void processCodes() {
        destinationModel.setCodeList(parseRepeatingLine(CODE_TOKEN, s -> {
            //code "Birth date": '21112-8' from "LOINC" display 'Birth date'
            ParseResult name = nextQuotedString(s, 0);
            ParseResult code = nextTickedString(s, name.getEndIndex() + 1);
            ParseResult codeSystem = nextQuotedString(s, code.getEndIndex() + 1);
            ParseResult displayName = nextTickedString(s, codeSystem.getEndIndex() + 1);

            if (areValidAscendingIndexes(name, code, codeSystem, displayName)) {
                //For now these should be identical to the sourceCqlModel versions.
                //Just find the corresponding one by name and use that.
                return findExisting(sourceModel.getCodeList(),
                        c -> StringUtils.equals(name.getString(), c.getName()),
                        "Could not find sourceCqlModel.code " + name.getString());
            } else {
                throw new IllegalArgumentException("Invalid code encountered: " + s);
            }
        }));
    }

    /**
     * Parses all of the CQLParameters out of the cql.
     */
    private void processParameters() {
        destinationModel.setCqlParameters(parseRepeatingLine(PARAMETER_TOKEN, s -> {
            //parameter "Measurement Period" Interval<DateTime>
            //parameter "Demographics" Tuple { address String, city String, zip String }
            //parameter "ChoiceValue" Choice<Integer, String>

            ParseResult firstQuotedString = nextQuotedString(s, 0);

            if (areValidAscendingIndexes(firstQuotedString)) {
                String name = firstQuotedString.getString();
                String logic = s.substring(firstQuotedString.getEndIndex() + 2);

                var existingParam = findExisting(sourceModel.getCqlParameters(),
                        p -> StringUtils.equals(p.getName(), name),
                        "Could not find parameter for " + name + " in existingCqlModel");
                CQLParameter result = new CQLParameter();
                result.setId(existingParam.getId());
                result.setName(name);
                result.setParameterLogic(logic);
                result.setReadOnly(existingParam.isReadOnly());
                return result;
            } else {
                throw new IllegalArgumentException("Invalid parameter encountered: " + s);
            }
        }));
    }

    /**
     * Parses the context out of the cql.
     */
    private void processContext() {
        //context Patient
        String context = parseSingletonLine(CONTEXT_TOKEN);
        String[] words = context.split(" ");
        if (words.length == 2) {
            destinationModel.setContext(words[1]);
        } else {
            throw new IllegalArgumentException("Invalid context encountered: " + context);
        }
    }

    /**
     * Parses all cql definitions out of the cql.
     */
    private void processCQLDefinitions() {
        //define "Antibiotic Active Overlaps Episode and Starts More than 30 Days Prior to Episode":
        //  "Episode Date" EpisodeDate
        //    with ["Medication, Active": "CWP Antibiotic Medications"] ActiveAntibiotic
        //      such that ActiveAntibiotic.relevantPeriod starts 31 days or more before day of start of EpisodeDate.relevantPeriod
        //        and ActiveAntibiotic.relevantPeriod overlaps day of EpisodeDate.relevantPeriod
        List<CQLDefinition> defs = new ArrayList<>();
        int nextDefineStart;
        int parsed = 0;
        while ((nextDefineStart = indexOf(convertedCql, DEFINE_TOKEN, parsed)) > 0) {
            if (!isDefineFunction(nextDefineStart)) {
                ParseResult firstQuotedString = nextQuotedString(convertedCql, nextDefineStart);
                int colon = indexOf(convertedCql, COLON, firstQuotedString.getEndIndex() + 1);
                int logicStart = indexOf(convertedCql, NEWLINE, colon + 1);
                int defineEnd = indexOf(convertedCql, "" + NEWLINE + NEWLINE, logicStart + 1);

                if (areValidAscendingIndexes(firstQuotedString.getEndIndex(), colon, logicStart)) {
                    String title = firstQuotedString.getString();
                    String logic = defineEnd == -1 ?
                            convertedCql.substring(logicStart + 1) : //End of file.
                            convertedCql.substring(logicStart + 1, defineEnd);  //Not end of file.
                    defs.add(buildCQLDef(title, logic));
                } else {
                    throw new IllegalArgumentException("Encountered invalid define around index: " +
                            nextDefineStart);
                }
                parsed = defineEnd == -1 ? convertedCql.length() : defineEnd;
            } else {
                parsed += DEFINE_FUNCTION_TOKEN.length();
            }
        }
        destinationModel.setDefinitionList(defs);
    }

    private void processFunctions() {
        // define function "Hospitalization, Potentially Starting in Emergency Department and or with Observation"(Inpatient_Encounter "Encounter, Performed" ):
        //   Inpatient_Encounter QualifyingInpatientEncounter
        //     let LastObservationVisit: Last(["Encounter, Performed": "Observation Services"] ObservationVisit
        //       where ObservationVisit.relevantPeriod ends 1 hour or less on or before start of QualifyingInpatientEncounter.relevantPeriod
        //       sort by
        //       end of relevantPeriod
        //     ),
        //     HospitalVisitStart: Coalesce(start of LastObservationVisit.relevantPeriod, start of QualifyingInpatientEncounter.relevantPeriod),
        //     LastEDVisit: Last(["Encounter, Performed": "Emergency Department Visit"] EDVisit
        //         where EDVisit.relevantPeriod ends 1 hour or less on or before HospitalVisitStart
        //         sort by
        //         end of relevantPeriod
        // return Interval[Coalesce(start of LastEDVisit.relevantPeriod, HospitalVisitStart),
        // end of QualifyingInpatientEncounter.relevantPeriod]
        List<CQLFunctions> funcs = new ArrayList<>();
        int nextFuncStart;
        int parsed = 0;
        while ((nextFuncStart = indexOf(convertedCql, DEFINE_FUNCTION_TOKEN, parsed)) > 0) {
            ParseResult firstQuotedString = nextQuotedString(convertedCql, nextFuncStart + DEFINE_FUNCTION_TOKEN.length());
            int parenStart = indexOf(convertedCql, OPEN_PAREN, firstQuotedString.getEndIndex() + 1);
            int parenEnd = indexOf(convertedCql, CLOSE_PAREN, parenStart + 1);
            int colon = indexOf(convertedCql, COLON, firstQuotedString.getEndIndex() + 1);
            int logicStart = indexOf(convertedCql, NEWLINE, colon + 1);
            int defineEnd = indexOf(convertedCql, "" + NEWLINE + NEWLINE, logicStart + 1);

            if (areValidAscendingIndexes(firstQuotedString.getEndIndex(), parenStart, parenEnd, colon, logicStart)) {
                String title = firstQuotedString.getString();
                String logic = defineEnd == -1 ?
                        convertedCql.substring(logicStart + 1) : //End of file.
                        convertedCql.substring(logicStart + 1, defineEnd);  //Not end of file.
                var f = new CQLFunctions();
                f.setArgumentList(parseArguments(convertedCql.substring(parenStart + 1, parenEnd)));
                f.setLogic(logic);
                f.setName(title);
                f.setId(newGuid());
                f.setContext(destinationModel.getContext());
                funcs.add(f);
            } else {
                throw new IllegalArgumentException("Encountered invalid define around index: " +
                        nextFuncStart);
            }
            parsed = defineEnd == -1 ? convertedCql.length() : defineEnd;
        }
        destinationModel.setCqlFunctions(funcs);
    }

    private List<CQLFunctionArgument> parseArguments(String argumentString) {
        //Inpatient_Encounter "Encounter, Performed" , ARG_2 "ARG 2 NAME"
        //x List<String> , y List<Integer>
        //define function "test3"(Test_4 "Test ,\" 4" , b List<“Medication, Order”> ,c "C"):
        var result = new ArrayList<CQLFunctionArgument>();

        int parsed = 0;
        while (parsed != -1 &&
                parsed < argumentString.length()) {
            int firstSpace = indexOf(argumentString, SPACE, parsed);
            String argName = argumentString.substring(parsed, firstSpace);
            ParseResult qdmType = parseNextQdmType(argumentString, firstSpace + 1);

            if (areValidAscendingIndexes(firstSpace, qdmType.getEndIndex())) {
                CQLFunctionArgument argument = new CQLFunctionArgument();
                argument.setId(newGuid());
                argument.setArgumentName(argName);
                argument.setQdmDataType(qdmType.getString());
                argument.setArgumentType(argumentTypeFromQdmType(argument.getQdmDataType()));
                result.add(argument);
            } else {
                throw new IllegalArgumentException("Invalid argument encountered: " + argumentString);
            }

            parsed = nextCharNotMatching(argumentString,qdmType.getEndIndex(),COMMA,SPACE).getEndIndex();
        }
        return result;
    }

    private ParseResult parseNextQdmType(String arguments, int startIndex) {
        StringBuilder result = new StringBuilder();
        ParseResult nextNonSpace = nextNonWhitespace(arguments, startIndex);

        boolean openQuote = false;
        int endIndex = -1;
        outer: for (int i = nextNonSpace.getEndIndex(); i < arguments.length(); i++) {
            char c = arguments.charAt(i);
            switch (c){
                case QUOTE:
                    ParseResult pr = nextQuotedString(arguments, i - 1);
                    if (result.length() != 0) {
                        result.append(QUOTE + pr.getString() + QUOTE);
                    } else {
                        result.append(pr.getString());
                    }
                    i = pr.getEndIndex();
                    break;
                case SPACE:
                    endIndex = i;
                    break outer;
                default:
                    result.append(c);
                    break;
            }
        }
        return new ParseResult(result.toString(), endIndex == -1 ? arguments.length() : endIndex);
    }

    /**
     * @param title The title.
     * @param logic The logic.
     * @return The CQLDefinition with all the defaults and params populated.
     */
    private CQLDefinition buildCQLDef(String title, String logic) {
        CQLDefinition result = new CQLDefinition();
        result.setId(newGuid());
        result.setName(title);
        result.setLogic(logic);
        result.setSupplDataElement(false);
        result.setPopDefinition(false);
        return result;
    }

    /**
     * Invokes producer.parse on every line in the cql that starts with the specified token.
     *
     * @param token    The token.
     * @param producer The producer.
     * @param <T>      The type of producer.
     * @return A list of all the objects the producer returned. If a producer returns null that
     * object is NOT added to the result.
     */
    private <T> List<T> parseRepeatingLine(String token, RepeatingLineProducer<T> producer) {
        List<T> result = new ArrayList<>();

        int nextStart;
        int parsed = 0;

        while ((nextStart = indexOf(convertedCql, token, parsed)) > 0) {
            int newline = indexOf(convertedCql, NEWLINE, nextStart + token.length());
            if (areValidAscendingIndexes(nextStart, newline)) {
                T t = producer.parse(convertedCql.substring(nextStart, newline));
                if (t != null) {
                    result.add(t);
                }
            } else {
                throw new IllegalArgumentException("Encountered invalid " + token + " around index: " +
                        nextStart);
            }
            parsed = newline;
        }
        return result;
    }

    /**
     * Parses the cql for a single line that matches the token.
     * If multiple lines are found a RuntimeException is raised.
     *
     * @param token The token.
     * @return The line matching the token.
     */
    private String parseSingletonLine(String token) {
        String result;
        int lineStart = indexOf(convertedCql, token, 0);
        int lineEnd = indexOf(convertedCql, NEWLINE, lineStart + token.length());

        if (areValidAscendingIndexes(lineStart, lineEnd)) {
            result = convertedCql.substring(lineStart, lineEnd);
            if (indexOf(convertedCql, token, lineEnd + 1) > 0) {
                throw new IllegalArgumentException("Encountered more than one " + token);
            }
        } else {
            throw new IllegalArgumentException("Could not find " + token);
        }
        return result;
    }

    private <T> T findExisting(List<T> collection, Predicate<T> filter, String messageIfNotFound) {
        var vs = collection.stream().filter(filter).findFirst();
        if (vs.isPresent()) {
            return vs.get();
        } else {
            throw new IllegalArgumentException(messageIfNotFound);
        }
    }

    /**
     * @param startIndex The start index.
     * @return True if the define starting at start index in the convertedCql is a function define. False if it is not.
     */
    private boolean isDefineFunction(int startIndex) {
        ParseResult firstQuotedString = nextQuotedString(convertedCql, startIndex);
        int functionIndex = indexOf(convertedCql, FUNCTION_TOKEN, startIndex);
        return functionIndex > 0 && firstQuotedString.getEndIndex() > functionIndex;
    }

    private String argumentTypeFromQdmType(String qdmType) {
        //TO DO: This needs some refinement once we figure out exactly that this is. Need stories for it.
        switch (qdmType) {
            case "Date":
            case "DateTime":
            case "Decimal":
            case "Integer":
            case "Ratio":
            case "String":
            case "Time":
                return qdmType;
            default:
                return "FHIR Datatype";
        }
    }
}
