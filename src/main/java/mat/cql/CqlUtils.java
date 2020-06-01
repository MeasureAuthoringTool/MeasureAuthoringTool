package mat.cql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Integer.max;

/**
 * This class contains methods broken out of CqlToMatXml to make it easier to test.
 * This class throws IllegalArgumentExceptions on all errors.
 */
@Slf4j
public class CqlUtils {
    public static final char TICK = '\'';
    public static final char QUOTE = '"';
    public static final char NEW_LINE = '\n';
    public static final String BLOCK_COMMENT_START = "/*";
    public static final String BLOCK_COMMENT_END = "*/";
    public static final String LINE_COMMENT = "//";
    public static final String OID_URL_TOKEN = "urn:oid:";

    private static final Map<String, String> nameToGlobalLibId = new HashMap<>();

    static {
        //TO DO move this into properties config or a DB lookup eventually.
        nameToGlobalLibId.put("MATGlobalCommonFunctions_FHIR4", "MATGlobalCommonFunctions-FHIR4-5-0-000");
        nameToGlobalLibId.put("AdultOutpatientEncounters_FHIR4", "AdultOutpatientEncounters-FHIR4-2-0-000");
        nameToGlobalLibId.put("AdvancedIllnessandFrailtyExclusion_FHIR4", "AdvancedIllnessandFrailtyExclusion-FHIR4-5-0-000");
        nameToGlobalLibId.put("FHIRHelpers", "FHIRHelpers-4-0-001");
        nameToGlobalLibId.put("Hospice_FHIR4", "Hospice-FHIR4-2-0-000");
        nameToGlobalLibId.put("SupplementalDataElements_FHIR4", "SupplementalDataElements-FHIR4-2-0-000");
        nameToGlobalLibId.put("TJCOverall_FHIR4", "TJCOverall-FHIR4-5-0-000");
        nameToGlobalLibId.put("VTEICU_FHIR4", "VTEICU-FHIR4-4-0-000");
    }

    /**
     * @param libName The include library name.
     * @return Returns the global fhir id for the specified include name. Returns null if includeName is not a global fhir lib.
     */
    public static String getGlobalLibId(String libName) {
        return nameToGlobalLibId.get(libName);
    }

    /**
     * @param libName The include library name.
     * @return Returns true if libName is a global fhir id.
     */
    public static boolean isGlobalLib(String libName) {
        return nameToGlobalLibId.containsKey(libName);
    }

    /**
     * Validates that all the specified indexes are non-negative and they are in ascending order.
     * This is very useful when parsing because it eliminates a lot of if/else branching code.
     *
     * @param indexes The indexes to check.
     * @return Returns true if all the indexes are in ascending order non negative.
     */
    public static boolean areValidAscendingIndexes(int... indexes) {
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
    public static boolean areValidAscendingIndexes(ParseResult... indexes) {
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
    public static String chomp1(String s) {
        return s.length() >= 2 ? s.substring(1, s.length() - 1) : s;
    }

    /**
     * @param source     The source.
     * @param startIndex The start index.
     * @return nextCharBoundary with QUOTE for the boundary.
     */
    public static ParseResult nextQuotedString(String source,
                                               int startIndex) {
        return nextCharBoundary(source, QUOTE, "\\" + QUOTE, startIndex);
    }

    /**
     * @param source     The source.
     * @param startIndex The start index.
     * @return nextCharBoundary with TICK for the boundary.
     */
    public static ParseResult nextTickedString(String source,
                                               int startIndex) {
        return nextCharBoundary(source, TICK, "\\" + TICK, startIndex);
    }

    public static ParseResult nextBlock(String source, char startBlockChar, char endBlockChar, int startIndex) {
        ParseResult result = new ParseResult(null, -1);
        int firstStartBlock = indexOf(source, startBlockChar, startIndex);
        int firstEndBlock = indexOf(source, endBlockChar, firstStartBlock + 1);
        int nextStartBlock = firstStartBlock;
        int nextEndBlock = firstEndBlock;

        if (areValidAscendingIndexes(firstStartBlock, firstEndBlock)) {
            do {
                nextStartBlock = indexOf(source, startBlockChar, nextStartBlock + 1);
                nextEndBlock = nextStartBlock != -1 ? indexOf(source, endBlockChar, nextStartBlock + 1) : nextEndBlock;
            }
            while (areValidAscendingIndexes(nextStartBlock, nextEndBlock));

            if (nextEndBlock != firstEndBlock) {
                firstEndBlock = indexOf(source, endBlockChar, nextEndBlock + 1);
            }
            if (areValidAscendingIndexes(firstEndBlock)) {
                result = new ParseResult(source.substring(firstStartBlock, firstEndBlock + 1), firstEndBlock);
            }
        }
        return result;
    }

    /**
     * @param source     The source.
     * @param startIndex The start index.
     * @return A ParseResult where the string is the contents of the next string encountered bounded by
     * boundary and the endIndex is the endBoundary.If a quoted string can not be found then a ParseResult
     * is returned with a null string and a -1 endIndex.
     */
    public static ParseResult nextCharBoundary(String source,
                                               char boundary,
                                               String escapeChar,
                                               int startIndex) {
        int start = indexOf(source, boundary, startIndex);
        int end;
        int parsed = start + 1;

        while ((end = indexOf(source, boundary, parsed)) != -1) {
            if (escapeChar == null) {
                break;
            } else {
                String candidateEscapeChar = source.substring(end - (escapeChar.length() - 1), end + 1);
                if (StringUtils.equals(candidateEscapeChar, escapeChar)) {
                    parsed = end + 1; //Find next boundary.
                } else {
                    break;
                }
            }
        }

        return areValidAscendingIndexes(start, end) ?
                new ParseResult(source.substring(start + 1, end), end) :
                new ParseResult(null, -1);
    }

    /**
     * @param source     The string to search.
     * @param indexStart The index to start at.
     * @return Returns a ParseResult: string=nextNonWhitespaceCharFound endIndex=indexOfNonWhitespaceCharFound.
     */
    public static ParseResult nextNonWhitespace(String source,
                                                int indexStart) {
        int index = -1;
        char c = 0;

        for (int i = indexStart; i < source.length(); i++) {
            c = source.charAt(i);
            if (!Character.isWhitespace(c)) {
                index = i;
                break;
            }
        }
        return new ParseResult("" + c, index);
    }

    /**
     * @param source     The string to search.
     * @param indexStart The index to start at.
     * @return Returns a ParseResult: string=nextNonWhitespaceCharFound endIndex=indexOfNonWhitespaceCharFound.
     */
    public static ParseResult nextCharMatching(String source,
                                               int indexStart,
                                               char... matchingChars) {
        int index = -1;
        char c = 0;

        for (int i = indexStart; i < source.length(); i++) {
            if (contains(c = source.charAt(i), matchingChars)) {
                index = i;
                break;
            }
        }
        return new ParseResult("" + c, index);
    }

    public static ParseResult nextCharNotMatching(String source,
                                                  int indexStart,
                                                  char... notMatchingChars) {
        int index = -1;
        char c = 0;

        for (int i = indexStart; i < source.length(); i++) {
            if (!contains(c = source.charAt(i), notMatchingChars)) {
                index = i;
                break;
            }
        }
        return new ParseResult("" + c, index);
    }

    /**
     * @param c     The char.
     * @param chars The chars to check.
     * @return Returns true if c is in chars, otherwise false.
     */
    public static boolean contains(char c, char[] chars) {
        boolean result = false;
        for (char cc : chars) {
            if (cc == c) {
                result = true;
                break;
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
    public static int indexOf(String source,
                              String search,
                              int indexStart) {
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
    public static int indexOf(String source,
                              char search,
                              int indexStart) {
        return indexStart < 0 ? -1 : source.indexOf(search, indexStart);
    }

    public static int previousIndexOf(String source, char search, int index) {
        int result = -1;
        for (int i = index; i >= 0; i--) {
            char c = source.charAt(i);
            if (c == search) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Removes the urn:oid: from the specified code system name.
     *
     * @param codeSystemName The code system name.
     * @return codeSystemName with urn:oid: removed.
     */
    public static String trimUrn(String codeSystemName) {
        return StringUtils.removeStart(codeSystemName, "urn:oid:");
    }


    /**
     * @return A new guid string.
     */
    public static String newGuid() {
        return UUID.randomUUID().toString().toLowerCase();
    }

    /**
     * @param cql The cql.
     * @return Removes all line comments (e.g. //sdfsdfsd ) from the cql.
     */
    public static String removeCQLLineComments(String cql) {
        StringBuilder result = new StringBuilder();
        //If line is whitespace remove line.
        //else remove from comment onwards in line.
        try (StringReader sr = new StringReader(cql)) {
            BufferedReader reader = new BufferedReader(sr);
            String line;
            while ((line = reader.readLine()) != null) {
                int commentStart = line.indexOf(LINE_COMMENT);
                boolean ignore = false;
                if (commentStart != -1 && !isInTickedOrQuotedString(line, commentStart)) {
                    line = line.substring(0, commentStart);
                    if (StringUtils.isBlank(line)) {
                        ignore = true;
                    }
                }
                if (!ignore) {
                    result.append((result.length() == 0 ? "" : NEW_LINE)).append(line);
                }
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("IOException occurred in removeLineComments.");
        }
        return result.toString();
    }

    public static boolean isInTickedOrQuotedString(String cql, int index) {
        return isInBoundary(cql, TICK, index) || isInBoundary(cql, QUOTE, index);
    }

    public static boolean isInBoundary(String cql, char boundaryChar, int index) {
        int prevNewline = max(0, previousIndexOf(cql, NEW_LINE, index));
        int prev = previousIndexOf(cql, boundaryChar, index);
        int next = indexOf(cql, boundaryChar, index);
        int nextNewLine = indexOf(cql, NEW_LINE, index);
        if (nextNewLine == -1) {
            nextNewLine = cql.length() - 1;
        }
        return areValidAscendingIndexes(prevNewline, prev, next, nextNewLine);
    }

    /**
     * @param cql the cql.
     * @return cql with all cql block comments removed.
     */
    public static String removeCqlBlockComments(String cql) {
        StringBuilder result = new StringBuilder(cql);
        int start;

        while ((start = result.indexOf(BLOCK_COMMENT_START, 0)) != -1) {
            int end = result.indexOf(BLOCK_COMMENT_END, start + BLOCK_COMMENT_START.length());
            if (areValidAscendingIndexes(end)) {
                result.replace(start, end + BLOCK_COMMENT_END.length(), "");
            } else {
                throw new IllegalArgumentException("Could not find an ending block comment.");
            }
        }
        return result.toString();
    }

    public static String removeLastCqlBlockComment(String cql) {
        StringBuilder result = new StringBuilder(cql);
        String stripped = StringUtils.strip(cql);
        int start = result.lastIndexOf(BLOCK_COMMENT_START);
        int end = result.lastIndexOf(BLOCK_COMMENT_END);
        if (areValidAscendingIndexes(start, end) && stripped.endsWith(BLOCK_COMMENT_END)) {
            return result.substring(0, start);
        }
        return cql;
    }

    public static boolean isValidVersion(String version) {
        boolean result = true;
        String[] parts = version.split("\\.");

        if (parts.length == 3) {
            for (String p : parts) {
                try {
                    Integer.parseInt(p);
                } catch (NumberFormatException nfe) {
                    result = false;
                    break;
                }
            }
            result = result && parts[2].length() == 3;
        } else {
            result = false;
        }
        return result;
    }

    public static Pair<Double, Integer> versionToVersionAndRevision(String version) {
        if (isValidVersion(version)) {
            String[] sp = version.split("\\.");
            return Pair.of(Double.parseDouble(sp[0] + "." + sp[1]), Integer.parseInt(sp[2]));
        } else {
            throw new IllegalArgumentException("Invalid version: " + version);
        }
    }

    public static boolean isOid(String uri) {
        return uri.startsWith(OID_URL_TOKEN);
    }

    public static String parseOid(String uri) {
        //urn:oid:2.16.840.1.113883.3.464.1004.1548
        //Should return 2.16.840.1.113883.3.464.1004.1548
        if (isOid(uri)) {
            return uri.substring(OID_URL_TOKEN.length());

        } else {
            throw new IllegalArgumentException("Invalid oid url: " + uri +
                    ". Should be in this format: urn:oid:2.16.840.1.113883.3.464.1004.1548");
        }
    }

    /**
     * Parses code system name into name and version parts.
     *
     * @return 'SNOMEDCT:2017-09' returns Pair: "SNOMEDCT","2017-09"
     * 'SNOMEDCT' returns Pair: "SNOMEDCT",null
     */
    public static Pair<String, String> parseCodeSystemName(String codeSystemName) {
        int i = codeSystemName.lastIndexOf(":");
        return i >= 0 ? Pair.of(codeSystemName.substring(0, i),
                codeSystemName.substring(i + 1)) : Pair.of(codeSystemName, null);
    }

    public static boolean startsWith(String s, String... tokens) {
        boolean result = false;
        for (String tok : tokens) {
            if (StringUtils.startsWith(s, tok)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Parses the next line.
     *
     * @param cql   The cql.
     * @param start The start index.
     * @return The next line. endIndex is the index of the next \n or -1 if EOF.
     */
    public static ParseResult parseNextLine(String cql, int start) {
        int nextNewline = indexOf(cql, '\n', start);
        return nextNewline == -1 ? new ParseResult(StringUtils.substring(cql, start, cql.length()), -1) :
                new ParseResult(StringUtils.substring(cql, start, nextNewline), nextNewline - 1);
    }

    public static boolean isQuoted(String s) {
        return StringUtils.isNotBlank(s) && s.startsWith("\"") && s.endsWith("\"");
    }

    public static String parsePrecedingComment(String cql) {
        return parsePrecedingComment(cql, 0, cql.length());
    }

    public static String parsePrecedingComment(String cql, int end) {
        return parsePrecedingComment(cql, 0, end);
    }

    /**
     * Parse a block comment preceding a function/parameter/definition in FHIR CQL.
     * It is supposed to work in a similar way to comment parsing in QDM CQL.
     * QDM supports only blocks comments before  function/parameter/definition.
     * Other comments are considered to be part of logic of a previous function/parameter/definition.
     *
     * @param cql   - CQL content
     * @param start - search area start index
     * @param end   - search area end index
     * @return
     */
    public static String parsePrecedingComment(String cql, int start, int end) {
        StringBuilder comment = new StringBuilder();
        if (areValidAscendingIndexes(start, end)) {
            String searchArea = cql.substring(start, end);
            String[] lines = searchArea.split("\r?\n");
            boolean inBlock = false;
            for (int i = lines.length; i-- > 0; ) {
                String line = StringUtils.stripToEmpty(lines[i]);

                boolean blankLine = StringUtils.isBlank(line);
                boolean startOfCommentBlock = line.startsWith(BLOCK_COMMENT_START);
                boolean endOfCommentBlock = line.endsWith(BLOCK_COMMENT_END);

                if (!inBlock && blankLine) {
                    // Skip empty strings Look for a block comment
                    continue;
                }

                if (!inBlock && !startOfCommentBlock && !endOfCommentBlock) {
                    // Terminate the search
                    break;
                }

                if (inBlock && endOfCommentBlock) {
                    log.warn("Block comment syntax issue.");
                    return "";
                }

                if (endOfCommentBlock) {
                    inBlock = true;
                    line = line.substring(0, line.length() - 2);
                }

                if (!inBlock && startOfCommentBlock) {
                    log.warn("Block comment syntax issue.");
                    return "";
                }

                if (startOfCommentBlock) {
                    line = StringUtils.stripStart(line.substring(2), "*");
                }

                prependCommendLine(comment, line);
                if (startOfCommentBlock) {
                    break;
                }
            }
        }
        return comment.toString();
    }

    private static void prependCommendLine(StringBuilder comment, String line) {
        if (comment.length() > 0) {
            comment.insert(0, StringUtils.LF);
        }
        comment.insert(0, line);
    }
}
