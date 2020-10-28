package mat.server.spreadsheet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class ResourceDefinition {
    private String elementId;
    private String definition;
    private String cardinality;
    private String type;
    private String isSummary;
    private String isModifier;
}
