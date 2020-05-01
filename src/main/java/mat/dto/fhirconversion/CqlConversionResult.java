package mat.dto.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class CqlConversionResult {
    private ConversionType type;
    private Boolean result;
    private List<String> errors = new ArrayList();
    private String cql;
    private String elm;
    private Set<CqlConversionError> cqlConversionErrors = new HashSet();
    private Set<MatCqlConversionException> matCqlConversionErrors = new HashSet();
    private String fhirCql;
    private String fhirElm;
    private Set<CqlConversionError> fhirCqlConversionErrors = new HashSet();
    private Set<MatCqlConversionException> fhirMatCqlConversionErrors = new HashSet();
}
