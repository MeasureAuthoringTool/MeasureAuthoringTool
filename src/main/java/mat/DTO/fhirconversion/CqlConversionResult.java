package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class CqlConversionResult {
    ConversionType type;
    Boolean result;
    List<String> errors = new ArrayList();
    @Transient
    String cql;
    @Transient
    String elm;
    Set<CqlConversionError> cqlConversionErrors = new HashSet();
    Set<MatCqlConversionException> matCqlConversionErrors = new HashSet();
    @Transient
    String fhirCql;
    @Transient
    String fhirElm;
    Set<CqlConversionError> fhirCqlConversionErrors = new HashSet();
    Set<MatCqlConversionException> fhirMatCqlConversionErrors = new HashSet();
}
