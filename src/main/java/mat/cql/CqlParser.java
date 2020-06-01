package mat.cql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mat.client.shared.MatException;
import mat.server.util.MATPropertiesService;

import static java.lang.Integer.max;
import static mat.cql.CqlUtils.areValidAscendingIndexes;
import static mat.cql.CqlUtils.chomp1;
import static mat.cql.CqlUtils.indexOf;
import static mat.cql.CqlUtils.isValidVersion;
import static mat.cql.CqlUtils.nextBlock;
import static mat.cql.CqlUtils.nextCharMatching;
import static mat.cql.CqlUtils.nextCharNotMatching;
import static mat.cql.CqlUtils.nextNonWhitespace;
import static mat.cql.CqlUtils.nextQuotedString;
import static mat.cql.CqlUtils.nextTickedString;
import static mat.cql.CqlUtils.parseNextLine;
import static mat.cql.CqlUtils.parsePrecedingComment;
import static mat.cql.CqlUtils.removeCQLLineComments;
import static mat.cql.CqlUtils.removeCqlBlockComments;
import static mat.cql.CqlUtils.removeLastCqlBlockComment;
import static mat.cql.CqlUtils.startsWith;

/**
 * A service used to parse cql. To use it invoke the parse method with a visitor implementation and the cql to convert.
 * MatExceptions are thrown if invalid cql is encountered.
 */
@Service
@Slf4j
public class CqlParser {
    private static final char NEWLINE = '\n';
    private static final char COMMA = ',';
    private static final char COLON = ':';
    private static final char QUOTE = '"';
    private static final char SPACE = ' ';
    private static final char OPEN_PAREN = '(';
    private static final char CLOSE_PAREN = ')';
    private static final char OPEN_BRACE = '{';
    private static final char CLOSE_BRACE = '}';

    private static final String LIB_TOKEN = "library ";
    private static final String INCLUDE_TOKEN = NEWLINE + "include ";
    private static final String FHIR_VERSION_TOKEN = NEWLINE + "using FHIR ";
    private static final String CONTEXT_TOKEN = NEWLINE + "context ";
    private static final String CODESYSTEM_TOKEN = NEWLINE + "codesystem ";
    private static final String VALUESET_TOKEN = NEWLINE + "valueset ";
    private static final String CODE_TOKEN = NEWLINE + "code ";
    private static final String PARAMETER_TOKEN = NEWLINE + "parameter ";
    private static final String DEFINE_TOKEN = NEWLINE + "define ";
    private static final String FUNCTION_TOKEN = "function";
    private static final String DEFINE_FUNCTION_TOKEN = DEFINE_TOKEN + FUNCTION_TOKEN;

    private interface RepeatingLineProducer {
        void parse(String s);
    }

    public void parse(String cql, CqlVisitor v) throws MatException {
        try {
            String workingCql = cql;
            // Make sure the visitor has everything it needs to start.
            // Validate will throw an exception if there are any issues.
            v.validate();

            //Remove all comments.
            if (v.isRemovingBlockComments()) {
                workingCql = removeCqlBlockComments(cql);
            }
            if (v.isRemovingLineComments()) {
                workingCql = removeCQLLineComments(workingCql);
            }

            if (log.isTraceEnabled()) {
                log.trace("workingCql after comments removed: \n" + workingCql);
            }

            //Populate root object first:
            processLibraryTag(workingCql, v);
            processContext(workingCql, v);
            processFhirVersion(workingCql, v);

            //Now populate child nodes:
            processIncludeLibs(workingCql, v);
            processCodeSystems(workingCql, v);
            processValueSets(workingCql, v);
            processCodes(workingCql, v);
            processParameters(workingCql, v);
            processDefinitions(workingCql, v);
            processFunctions(workingCql, v);
        } catch (RuntimeException e) {
            log.warn("RuntimeException encountered in CqlParser", e);
            throw new MatException(e.getMessage(), e);
        }
    }


    /**
     * Parses out the CQLLib from the cql.
     */
    private void processLibraryTag(String cql, CqlVisitor v) {
        //library CWP_HEDIS_2020_CARSON version '1.0.000'
        int endFirstLine = indexOf(cql, NEWLINE, 0);
        if (areValidAscendingIndexes(endFirstLine)) {
            String firstLine = cql.substring(0, endFirstLine);
            if (!firstLine.startsWith(LIB_TOKEN)) {
                throw new IllegalArgumentException("First line did not start with " + LIB_TOKEN);
            } else {
                String[] words = firstLine.split(" ");

                if (words.length == 4) {
                    v.libraryTag(words[1], chomp1(words[3]));
                } else {
                    throw new IllegalArgumentException("Invalid library encountered: " + firstLine + ". Verify the version is a valid mat version, e.g. '1.0.000'.");
                }
            }
        } else {
            throw new IllegalArgumentException("First line did not start with " + LIB_TOKEN);
        }
    }

    /**
     * Parses out the QDM version from the cql.
     */
    protected void processFhirVersion(String cql, CqlVisitor v) {
        //using FHIR version '4.0.1'
        String fhir = parseSingletonLine(cql, FHIR_VERSION_TOKEN, true);
        ParseResult version = nextTickedString(fhir, 0);

        if (areValidAscendingIndexes(version)) {
            v.fhirVersion(version.getString());
        } else {
            throw new IllegalArgumentException("Invalid " + FHIR_VERSION_TOKEN + " encounter: " + fhir);
        }
    }


    /**
     * Parses out all the includes from the cql.
     */
    private void processIncludeLibs(String cql, CqlVisitor v) {
        // destinationModel.setCqlIncludeLibrarys(
        parseRepeatingLine(cql, INCLUDE_TOKEN, s -> {
            // include NCQA_Common version '5.1.000' called Common
            String[] words = s.split(" ");
            if (words.length != 6) {
                throw new IllegalArgumentException("Invalid include encountered: " + s);
            }
            String name = words[1];
            String version = chomp1(words[3]);
            if (!isValidVersion(version)) {
                throw new IllegalArgumentException("Invalid version , " + version + ", for lib " + name +
                        ". Must be in MAT version format, e.g. '1.0.000'.");
            }
            v.includeLib(name, version, words[5], "FHIR", MATPropertiesService.get().getFhirVersion());
        });
    }

    /**
     * Parses all of the code systems found in the cql.
     */
    private void processCodeSystems(String cql, CqlVisitor v) {
        parseRepeatingLine(cql, CODESYSTEM_TOKEN, s -> {
            //codesystem "SNOMEDCT:2017-09": 'http://snomed.info/sct/731000124108' version 'http://snomed.info/sct/731000124108/version/201709'
            //codesystem "LOINC": 'urn:oid:2.16.840.1.113883.6.1'
            ParseResult systemName = nextQuotedString(s, 0);
            ParseResult uri = nextTickedString(s, systemName.getEndIndex() + 1);
            ParseResult versionUri = nextTickedString(s, uri.getEndIndex() + 1);

            if (areValidAscendingIndexes(systemName, uri)) {
                //For now these should be identical to the sourceCqlModel versions.
                //Just find the corresponding one by code system name and use that.
                v.codeSystem(systemName.getString(),
                        uri.getString(),
                        StringUtils.defaultString(versionUri.getString(), null));
            } else {
                throw new IllegalArgumentException("Invalid include encountered: " + s);
            }
        });
    }

    /**
     * Parses all of the valuesets found in the cql.
     */
    private void processValueSets(String cql, CqlVisitor v) {
        parseRepeatingLine(cql, VALUESET_TOKEN, s -> {
            //valueset "Ethnicity": 'urn:oid:2.16.840.1.114222.4.11.837'
            //FHIR4 valueset "Encounter Inpatient": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.666.5.307'
            ParseResult type = nextQuotedString(s, 0);
            ParseResult uri = nextTickedString(s, type.getEndIndex());

            if (areValidAscendingIndexes(type, uri)) {
                v.valueSet(type.getString(), uri.getString());
            } else {
                throw new IllegalArgumentException("Invalid valueset encountered: '" + s);
            }
        });
    }

    /**
     * Parses all of the codes out of the cql.
     */
    private void processCodes(String cql, CqlVisitor v) {
        parseRepeatingLine(cql, CODE_TOKEN, s -> {
            //code "Birth date": '21112-8' from "LOINC" display 'Birth date'
            //code "active": 'active' from "ConditionClinicalStatusCodes"
            ParseResult name = nextQuotedString(s, 0);
            ParseResult code = nextTickedString(s, name.getEndIndex() + 1);
            ParseResult from = parseNextIdentifier(s, code.getEndIndex() + 1);
            ParseResult codeSystem = parseNextIdentifier(s, from.getEndIndex() + 1);
            ParseResult displayName = nextTickedString(s, codeSystem.getEndIndex() + 1);

            if (areValidAscendingIndexes(name, code, from, codeSystem)) {
                v.code(name.getString(),
                        code.getString(),
                        codeSystem.getString(),
                        displayName.getEndIndex() == -1 ? null : displayName.getString());
            } else {
                throw new IllegalArgumentException("Invalid code encountered: " + s);
            }
        });
    }

    /**
     * Parses all of the CQLParameters out of the cql.
     */
    private void processParameters(String cql, CqlVisitor v) {
        //parameter "Measurement Period" Interval<DateTime>
        //parameter "Demographics" Tuple { address String, city String, zip String }
        //parameter "ChoiceValue" Choice<Integer, String>
        //parameter "Measurement Period" Interval<DateTime>
        //  default Interval[@2019-01-01T00:00:00.0, @2020-01-01T00:00:00.0)
        //parameter "Measurement Period" Interval<DateTime> default Interval[@2019-01-01T00:00:00.0, @2020-01-01T00:00:00.0)
        //parameter "Measurement Period" default Interval[@2019-01-01T00:00:00.0, @2020-01-01T00:00:00.0)
        int nextDefineStart;
        int parsed = 0;
        while ((nextDefineStart = indexOf(cql, PARAMETER_TOKEN, parsed)) > 0) {
            ParseResult nameResult = nextQuotedString(cql, nextDefineStart);
            int logicStart = indexOf(cql, NEWLINE, nameResult.getEndIndex());
            int logicEnd = getDoubleNewLineEndIndex(cql, logicStart);

            if (areValidAscendingIndexes(nameResult.getEndIndex())) {

                String name = nameResult.getString();
                String type = cql.substring(nameResult.getEndIndex() + 1, logicStart).trim();

                if (logicEnd > logicStart + 1) {
                    type += " " + cql.substring(logicStart + 1, logicEnd).trim();
                }
                if (StringUtils.isBlank(type)) {
                    throw new IllegalArgumentException("Empty parameter type encountered for " +
                            name);
                }
                String comment = parsePrecedingComment(cql, nextDefineStart);
                v.parameter(name, type, comment);
            } else {
                throw new IllegalArgumentException("Invalid parameter type encountered around index " +
                        nextDefineStart);
            }
            parsed = max(logicStart, logicEnd) + 1;
        }
    }

    private void processContext(String cql, CqlVisitor v) {
        //context Patient
        String context = parseSingletonLine(cql, CONTEXT_TOKEN, false);
        if (StringUtils.isNotBlank(context)) {
            String[] words = context.split(" ");
            if (words.length == 2) {
                v.context(words[1]);
            } else {
                throw new IllegalArgumentException("Invalid context encountered: " + context);
            }
        }
    }

    /**
     * Parses all cql definitions out of the cql.
     */
    private void processDefinitions(String cql, CqlVisitor v) {
        //define "Antibiotic Active Overlaps Episode and Starts More than 30 Days Prior to Episode":
        //  "Episode Date" EpisodeDate
        //    with ["Medication, Active": "CWP Antibiotic Medications"] ActiveAntibiotic
        //      such that ActiveAntibiotic.relevantPeriod starts 31 days or more before day of start of EpisodeDate.relevantPeriod
        //        and ActiveAntibiotic.relevantPeriod overlaps day of EpisodeDate.relevantPeriod
        int nextDefineStart;
        int parsed = 0;
        while ((nextDefineStart = indexOf(cql, DEFINE_TOKEN, parsed)) > 0) {
            if (!isDefineFunction(cql, nextDefineStart)) {

                ParseResult name = parseNextIdentifier(cql, nextDefineStart + DEFINE_TOKEN.length());
                int colon = indexOf(cql, COLON, name.getEndIndex() + 1);
                ParseResult logic = parseLogic(cql, colon + 1, new String[]{"define", "context"});

                if (areValidAscendingIndexes(name.getEndIndex(), colon, logic.getEndIndex())) {
                    String comment = parsePrecedingComment(cql, nextDefineStart);
                    String title = name.getString();
                    boolean last = logic.getEndIndex() == cql.length();
                    String logicText = last ? logic.getString() : removeLastCqlBlockComment(logic.getString());
                    v.definition(title, logicText, comment);
                } else {
                    throw new IllegalArgumentException("Encountered invalid define around index: " +
                            nextDefineStart);
                }
                parsed = logic.getEndIndex();
            } else {
                parsed += nextDefineStart + DEFINE_TOKEN.length();
            }
        }
    }

    private void processFunctions(String cql, CqlVisitor v) {
        // Examples:
        // define function ToInterval(range FHIR.Range):
        //   if range is null then
        //     null
        //   else
        //     Interval[ToQuantity(range.low), ToQuantity(range.high)]
        //
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
        //    return Interval[Coalesce(start of LastEDVisit.relevantPeriod, HospitalVisitStart),
        //    end of QualifyingInpatientEncounter.relevantPeriod]
        //
        // define function ToString(value FHIR.uuid): value.value
        // define function ToString(value FHIR.TestScriptRequestMethodCode): value.value
        int nextFuncStart;
        int parsed = 0;
        while ((nextFuncStart = indexOf(cql, DEFINE_FUNCTION_TOKEN, parsed)) > 0) {

            ParseResult name = parseNextIdentifier(cql, nextFuncStart + DEFINE_FUNCTION_TOKEN.length());
            int parenStart = indexOf(cql, OPEN_PAREN, name.getEndIndex());
            int parenEnd = indexOf(cql, CLOSE_PAREN, parenStart + 1);
            int colon = indexOf(cql, COLON, name.getEndIndex() + 1);
            ParseResult logic = parseLogic(cql, colon + 1, new String[]{"define", "context"});

            if (areValidAscendingIndexes(name.getEndIndex(), parenStart, parenEnd, colon, logic.getEndIndex())) {
                String comment = parsePrecedingComment(cql, nextFuncStart);
                String title = name.getString();
                boolean last = logic.getEndIndex() == cql.length();
                String logicText = last ? logic.getString() : removeLastCqlBlockComment(logic.getString());
                v.function(title, parseArguments(cql.substring(parenStart + 1, parenEnd)), logicText, comment);
            } else {
                throw new IllegalArgumentException("Encountered invalid define around index: " +
                        nextFuncStart);
            }
            parsed = logic.getEndIndex();
        }
    }

    private ParseResult parseNextIdentifier(String cql, int startIndex) {
        ParseResult nextNonWS = nextNonWhitespace(cql, startIndex);
        ParseResult result;
        if (areValidAscendingIndexes(nextNonWS)) {
            if (StringUtils.equals(nextNonWS.getString(), "" + QUOTE)) {
                result = nextQuotedString(cql, startIndex);
            } else {
                ParseResult nameEnd = nextCharMatching(cql, nextNonWS.getEndIndex() + 1,
                        ' ', '(', ':', '\n');
                result = nameEnd.getEndIndex() == -1 ?
                        new ParseResult(cql.substring(nextNonWS.getEndIndex()), cql.length() - 1) :
                        new ParseResult(cql.substring(nextNonWS.getEndIndex(), nameEnd.getEndIndex()), nameEnd.getEndIndex());
            }
        } else {
            result = new ParseResult(null, -1);
        }
        return result;
    }

    private ParseResult parseLogic(String cql, int startIndex, String[] lineStartExitTokens) {
        int nextStartIndex = startIndex;
        boolean isBreak = false;
        do {
            ParseResult curLineRes = parseNextLine(cql, nextStartIndex);
            String curLine = curLineRes.getString();

            if (startsWith(curLine, lineStartExitTokens)) {
                // Encountered the next block so exit and set index before the newline.
                nextStartIndex--; // Backup before newline.
                isBreak = true;
            } else if (curLineRes.getEndIndex() != -1) {
                // Still in logic.
                nextStartIndex = nextStartIndex + curLine.length() + 1;
            } else {
                // curLineRes.getEndIndex() == -1, End of file.
                nextStartIndex = nextStartIndex + curLine.length();
                isBreak = true;
            }
        } while (!isBreak);
        return new ParseResult(StringUtils.substring(cql, startIndex, nextStartIndex), nextStartIndex);
    }

    private List<FunctionArgument> parseArguments(String argumentString) {
        //Inpatient_Encounter "Encounter, Performed" , ARG_2 "ARG 2 NAME"
        //x List<String> , y List<Integer>
        //define function "test3"(Test_4 "Test ,\" 4" , b List<“Medication, Order”> ,c "C"):
        var result = new ArrayList<FunctionArgument>();

        int parsed = 0;
        while (parsed != -1 &&
                parsed < argumentString.length()) {
            int firstSpace = indexOf(argumentString, SPACE, parsed);
            String argName = argumentString.substring(parsed, firstSpace);
            ParseResult argType = parseNextType(argumentString, firstSpace + 1);

            if (areValidAscendingIndexes(firstSpace, argType.getEndIndex())) {
                FunctionArgument argument = new FunctionArgument(argName, argType.getString());
                result.add(argument);
            } else {
                throw new IllegalArgumentException("Invalid argument encountered: " + argumentString);
            }

            parsed = nextCharNotMatching(argumentString, argType.getEndIndex(), COMMA, SPACE).getEndIndex();
        }
        return result;
    }

    /**
     * Invokes producer.parse on every line in the cql that starts with the specified token.
     *
     * @param cql      The cql.
     * @param token    The token.
     * @param producer The producer.
     */
    private void parseRepeatingLine(String cql, String token, RepeatingLineProducer producer) {
        int nextStart;
        int parsed = 0;

        while ((nextStart = indexOf(cql, token, parsed)) > 0) {
            int newline = indexOf(cql, NEWLINE, nextStart + token.length());
            if (areValidAscendingIndexes(nextStart, newline)) {
                producer.parse(cql.substring(nextStart, newline));
            } else {
                throw new IllegalArgumentException("Encountered invalid " + token + " around index: " +
                        nextStart);
            }
            parsed = newline;
        }
    }

    /**
     * Parses the cql for a single line that matches the token.
     * If multiple lines are found a RuntimeException is raised.
     *
     * @param token The token.
     * @return The line matching the token.
     */
    private String parseSingletonLine(String cql, String token, boolean isRequired) {
        String result = null;
        int lineStart = indexOf(cql, token, 0);
        int lineEnd = indexOf(cql, NEWLINE, lineStart + token.length());

        if (areValidAscendingIndexes(lineStart, lineEnd)) {
            result = cql.substring(lineStart, lineEnd);
            if (indexOf(cql, token, lineEnd + 1) > 0) {
                throw new IllegalArgumentException("Encountered more than one " + token);
            }
        } else if (isRequired) {
            throw new IllegalArgumentException("Could not find " + token);
        }
        return result;
    }

    /**
     * @param startIndex The start index.
     * @return True if the define starting at start index in the convertedCql is a function define. False if it is not.
     */
    private boolean isDefineFunction(String cql, int startIndex) {
        String token = StringUtils.substring(cql, startIndex, startIndex + DEFINE_FUNCTION_TOKEN.length());
        return StringUtils.equals(token, DEFINE_FUNCTION_TOKEN);
    }

    private ParseResult parseNextType(String arguments, int startIndex) {
        // Tuple case:
        //        List<Tuple {
        //            rxNormCode Code,
        //            doseQuantity Quantity,
        //            dosesPerDay Decimal
        //        }>

        StringBuilder result = new StringBuilder();
        ParseResult nextNonSpace = nextNonWhitespace(arguments, startIndex);

        int endIndex = -1;
        outer:
        for (int i = nextNonSpace.getEndIndex(); i < arguments.length(); i++) {
            char c = arguments.charAt(i);
            switch (c) {
                case QUOTE:
                    ParseResult pr = nextQuotedString(arguments, i - 1);
                    result.append(QUOTE);
                    result.append(pr.getString());
                    result.append(QUOTE);
                    i = pr.getEndIndex();
                    break;
                case SPACE:
                    //Handle tuples
                    if (StringUtils.endsWith(result.toString(), "Tuple") &&
                            arguments.indexOf("{", i) == i + 1) {
                        ParseResult block = nextBlock(arguments, OPEN_BRACE, CLOSE_BRACE, i);
                        if (block.getEndIndex() != -1) {
                            result.append(" ");
                            result.append(block.getString());
                            i = block.getEndIndex();
                        } else {
                            throw new IllegalArgumentException("Invalid tuple encountered: " + arguments);
                        }
                    } else {
                        endIndex = i;
                        break outer;
                    }
                    break;
                case COMMA:
                    endIndex = i;
                    break outer;
                default:
                    result.append(c);
                    break;
            }
        }
        return new ParseResult(result.toString(), endIndex == -1 ? arguments.length() : endIndex);
    }

    private int getDoubleNewLineEndIndex(String cql, int startIndex) {
        int result = -1;
        if (cql.charAt(startIndex) == '\n') {
            int nextNewLineStart = indexOf(cql, '\n', startIndex + 1);
            if (nextNewLineStart >= 0) {
                result = nextNewLineStart;
            }
        }
        return result;
    }
}
