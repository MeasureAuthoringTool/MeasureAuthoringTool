package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ValueSetConversionResults {
    String oid;
    String reason;
    Boolean success;
    String link;
    String json;
    List<FhirValidationResult> valueSetFhirValidationResults = new ArrayList();
}
