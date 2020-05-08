package mat.server.spreadsheet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class MatAttribute {
    private String dataTypeDescription;
    private String matAttributeName;
    private String fhirQicoreMapping;
    private String fhirResource;
    private String fhirType;
    private String fhirElement;
    private String helpWording;
    private List<String> dropDown;
}
