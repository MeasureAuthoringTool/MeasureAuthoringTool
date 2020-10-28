package mat.server.service.cql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@Nullable
@Builder
@NoArgsConstructor
public class ValidationRequest {

    private int timeoutSeconds = -1;
    private boolean validateValueSets = true;
    private boolean validateCodeSystems = true;
    private boolean validateSyntax = true;
    private boolean validateCqlToElm = true;
    private boolean validateReturnType = false;
}
