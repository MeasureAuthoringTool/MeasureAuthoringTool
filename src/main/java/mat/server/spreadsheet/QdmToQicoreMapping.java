package mat.server.spreadsheet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class QdmToQicoreMapping {
    private String title;
    private String matDataType;
    private String matAttributeType;
    private String fhirQICoreMapping;
    private String type;
    private String cardinality;
}

