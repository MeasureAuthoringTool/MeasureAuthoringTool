package mat.cql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Used as a model for cql function arguments.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FunctionArgument {
    private String name;
    private String type;
}
