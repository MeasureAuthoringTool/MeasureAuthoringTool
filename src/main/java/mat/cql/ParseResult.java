package mat.cql;

import lombok.Getter;
import lombok.ToString;

/**
 * Used while parsing CQL to note position and string encountered for utility methods in CqlUtils.
 */
@Getter
@ToString
public class ParseResult {
    private String string;
    private int endIndex;

    public ParseResult(String string, int endIndex) {
        this.string = string;
        this.endIndex = endIndex;
    }
}