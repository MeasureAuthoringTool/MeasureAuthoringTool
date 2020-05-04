package mat.dto.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MeasureConversionResults {
    private List<FieldConversionResult> measureResults = new ArrayList();
    private ConversionType measureConversionType;
    private List<FhirValidationResult> measureFhirValidationResults = new ArrayList();
    private String reason;
    private Boolean success;
    private String link;
    private String fhirMeasureJson;
}
