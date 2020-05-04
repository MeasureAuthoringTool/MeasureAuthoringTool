package mat.dto.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ValueSetConversionResults {
    private String oid;
    private String reason;
    private Boolean success;
    private String link;
    private String json;
    private List<FhirValidationResult> valueSetFhirValidationResults = new ArrayList();
}
