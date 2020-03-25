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
import java.util.UUID;
import java.util.function.Predicate;

/**
 * A parser that parses CQL and converts it into Mat XML.
 */
@Getter
@Setter
@ToString
@Slf4j
public class CqlToMatXml {
    private static final char NEWLINE = '\n';

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

    private static final char COLON = ':';
    private static final char QUOTE = '"';
    private static final char SPACE = ' ';
    private static final char TICK = '\'';
    private static final char OPEN_PAREN = '(';
    private static final char CLOSE_PAREN = ')';
    private static final char COMMA = ',';

    private static final String WHITESPACE = "\\s";
    private static final String QDM_VERSION = "5.5";

    private String convertedCql;
    private CQLModel sourceModel;
    private CQLModel destinationModel = new CQLModel();

    public interface RepeatingLineProducer<T> {
        T parse(String s);
    }

    @Getter
    @ToString
    public static class ParseResult {
        private String string;
        private int endIndex;

        public ParseResult(String string, int endIndex) {
            this.string = string;
            this.endIndex = endIndex;
        }
    }

    public CqlToMatXml(CQLModel sourceCqlModel, String convertedCql) {
        this.convertedCql = convertedCql;
        this.sourceModel = sourceCqlModel;
    }

    public CQLModel convert() throws MatException {
        try {
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
            throw new MatException("Error converting mat xml.",e);
        }
    }

    /**
     * Parses out the CQLLib from the cql.
     */
    private void processLibraryTag() {
        //library CWP_HEDIS_2020_CARSON version '1.0.000'
        String lib = parseSingletonLine(LIB_TOKEN);
        String[] words = lib.split(" ");

        if (words.length == 4) {
            destinationModel.setLibraryName(words[1]);
            destinationModel.setUsingModel(destinationModel.getUsingModel());
            destinationModel.setVersionUsed(destinationModel.getVersionUsed());
            destinationModel.setVersionUsed(chomp1(words[3]));
        } else {
            throw new IllegalArgumentException("Invalid library encountered: " + lib);
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
                int colon = indexOf(convertedCql, COLON, firstQuotedString.endIndex + 1);
                int logicStart = indexOf(convertedCql, NEWLINE, colon + 1);
                int defineEnd = indexOf(convertedCql, "" + NEWLINE + NEWLINE, logicStart + 1);

                if (areValidAscendingIndexes(firstQuotedString.endIndex, colon, logicStart)) {
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
            int parenStart = indexOf(convertedCql, OPEN_PAREN, firstQuotedString.endIndex + 1);
            int parenEnd = indexOf(convertedCql, CLOSE_PAREN, parenStart + 1);
            int colon = indexOf(convertedCql, COLON, firstQuotedString.endIndex + 1);
            int logicStart = indexOf(convertedCql, NEWLINE, colon + 1);
            int defineEnd = indexOf(convertedCql, "" + NEWLINE + NEWLINE, logicStart + 1);

            if (areValidAscendingIndexes(firstQuotedString.endIndex, parenStart, parenEnd, colon, logicStart)) {
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
        var result = new ArrayList<CQLFunctionArgument>();

        int parsed = 0;
        while (parsed < argumentString.length()) {
            int firstSpace = indexOf(argumentString, ' ', parsed);
            int commaIndex = argumentString.indexOf(COMMA);
            if (areValidAscendingIndexes(firstSpace)) {
                String qdmType = commaIndex == -1 ?
                        argumentString.substring(firstSpace + 1).trim() :
                        argumentString.substring(firstSpace + 1,commaIndex).trim();

                CQLFunctionArgument argument = new CQLFunctionArgument();
                argument.setId(newGuid());
                argument.setArgumentName(argumentString.substring(parsed, firstSpace));
                argument.setQdmDataType(qdmType);
                argument.setArgumentType("FHIR Datatype");
                result.add(argument);

                parsed = commaIndex == -1 ?
                        argumentString.length() :
                        commaIndex + 1;
            } else {
                throw new IllegalArgumentException("Invalid arguments encountered: " + argumentString);
            }
        }
        return result;
    }

    /**
     * The same as string.indexOf(search,indexStart) but this method returns
     * -1 instead of throwing an exception if indexStart is negative.
     * It allows for cleaner parsing code without a bunch of branching ifs.
     *
     * @param source     The string to index.
     * @param search     The search string.
     * @param indexStart The index to start at.
     * @return The result.
     */
    private int indexOf(String source, String search, int indexStart) {
        return indexStart < 0 ? -1 : source.indexOf(search, indexStart);
    }

    /**
     * The same as string.indexOf(search,indexStart) but this method returns
     * -1 instead of throwing an exception if indexStart is negative.
     * It allows for cleaner parsing code without a bunch of branching ifs.
     *
     * @param source     The string to index.
     * @param search     The search string.
     * @param indexStart The index to start at.
     * @return The result.
     */
    private int indexOf(String source, char search, int indexStart) {
        return indexStart < 0 ? -1 : source.indexOf(search, indexStart);
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
     * Validates that all the specified indexes are non-negative and they are in ascending order.
     * This is very useful when parsing because it eliminates a lot of if/else branching code.
     *
     * @param indexes The indexes to check.
     * @return Returns true if all the indexes are in ascending order non negative.
     */
    private boolean areValidAscendingIndexes(int... indexes) {
        boolean result = true;
        int last = Integer.MAX_VALUE;
        for (int i : indexes) {
            if (i < 0 || (last != Integer.MAX_VALUE && i < last)) {
                result = false;
                break;
            }
            last = i;
        }
        return result;
    }

    /**
     * Same as areValidAscendingIndexes(int... indexes) except this one uses ParsedResult.endIndex for the indexes.
     *
     * @param indexes The indexes to check.
     * @return Returns true if all the indexes are in ascending order and non negative.
     */
    private boolean areValidAscendingIndexes(ParseResult... indexes) {
        int[] intIndexes = new int[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            intIndexes[i] = indexes[i].getEndIndex();
        }
        return areValidAscendingIndexes(intIndexes);
    }

    /**
     * @param s The string to chomp.
     * @return Removes 1 character from the front end and end of the string.
     */
    private String chomp1(String s) {
        return s.length() >= 2 ? s.substring(1, s.length() - 1) : s;
    }

    /**
     * @param source     The source.
     * @param startIndex The start index.
     * @return nextCharBoundary with QUOTE for the boundary.
     */
    private ParseResult nextQuotedString(String source, int startIndex) {
        return nextCharBoundary(source, "" + QUOTE, startIndex);
    }

    /**
     * @param source     The source.
     * @param startIndex The start index.
     * @return nextCharBoundary with TICK for the boundary.
     */
    private ParseResult nextTickedString(String source, int startIndex) {
        return nextCharBoundary(source, "" + TICK, startIndex);
    }

    /**
     * @param source     The source.
     * @param startIndex The start index.
     * @return A ParseResult where the string is the contents of the next string encountered bounded by
     * boundary and the endIndex is the endBoundary.If a quoted string can not be found then a ParseResult
     * is returned with a null string and a -1 endIndex.
     */
    private ParseResult nextCharBoundary(String source, String boundary, int startIndex) {
        int start = indexOf(source, boundary, startIndex);
        int end = indexOf(source, boundary, start + 1);
        return areValidAscendingIndexes(start, end) ?
                new ParseResult(source.substring(start + 1, end), end) :
                new ParseResult(null, -1);
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

    private String newGuid() {
        return UUID.randomUUID().toString().toLowerCase();
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
        return functionIndex > 0 && firstQuotedString.endIndex > functionIndex;
    }

    /**
     * Removes the urn:oid: from the specified code system name.
     * @param codeSystemName The code system name.
     * @return codeSystemName with urn:oid: removed.
     */
    private String trimUrn(String codeSystemName) {
        return StringUtils.removeStart(codeSystemName,"urn:oid:");
    }
}
