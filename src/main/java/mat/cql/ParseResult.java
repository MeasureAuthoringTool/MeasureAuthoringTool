package mat.cql;

import lombok.Getter;
import lombok.ToString;

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