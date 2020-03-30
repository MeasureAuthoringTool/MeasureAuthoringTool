package mat.cql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.UUID;

/**
 * This class contains methods broken out of CqlToMatXml to make it easier to test.
 */
@Slf4j
public class CqlStringUtils {
    public static final char TICK = '\'';
    public static final char QUOTE = '"';
    public static final char NEW_LINE = '\n';
    public static final String BLOCK_COMMENT_START = "/*";
    public static final String BLOCK_COMMENT_END = "*/";
    public static final String LINE_COMMENT = "//";

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

    public static ParseResult nextCurlyBraceBlock(String source, int startIndex) {
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
        try (StringReader sr = new StringReader(cql);) {
            BufferedReader reader = new BufferedReader(sr);
            String line;
            while ((line = reader.readLine()) != null) {
                int commentStart = line.indexOf(LINE_COMMENT);
                boolean ignore = false;
                if (commentStart != -1) {
                    line = line.substring(0, commentStart);
                    if (StringUtils.isBlank(line)) {
                        ignore = true;
                    }
                }
                if (!ignore) {
                    result.append((result.length() == 0 ? "" : NEW_LINE) + line);
                }
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("IOException occurred in removeLineComments.");
        }
        return result.toString();
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
}
