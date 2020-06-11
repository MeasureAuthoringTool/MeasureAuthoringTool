package mat.server.service.cql;

import javax.annotation.Nullable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Nullable
public class ValidationRequest {

    private boolean validateValueSets = true;
    private boolean validateCodeSystems = true;
    private boolean validateSyntax = true;
    private boolean validateCqlToElm = true;

}
