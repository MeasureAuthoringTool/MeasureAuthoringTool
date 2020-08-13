package mat.server.service.cql;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@NoArgsConstructor
public class MatXmlReq {
    private boolean isLinting = true;
    @Valid
    private ValidationRequest validationRequest = new ValidationRequest();
}
