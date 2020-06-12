package mat.server.service.cql;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatXmlReq {
    private boolean isLinting = true;
    @Valid
    private ValidationRequest validationRequest = new ValidationRequest();
}
