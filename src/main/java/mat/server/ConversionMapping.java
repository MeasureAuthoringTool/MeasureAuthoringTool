package mat.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionMapping {

    private String title;

    private String matDataTypeDescription;

    private String matAttributeName;

    private String fhirR4QiCoreMapping;

    private String fhirResource;

    private String fhirElement;

    private String fhirType;

    private String hashValue;

}
