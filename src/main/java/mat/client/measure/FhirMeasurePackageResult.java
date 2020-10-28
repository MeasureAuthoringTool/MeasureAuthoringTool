package mat.client.measure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FhirMeasurePackageResult {
    private String measureJson;
    private String measureXml;
    private String measureLibJson;
    private String measureLibXml;
    private String measureLibCql;
    private String measureLibElmJson;
    private String measureLibElmXml;
    private String inludedLibsJson;
    private String inludedLibsXml;
}
