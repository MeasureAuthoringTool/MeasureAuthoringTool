package mat.server.service.cql;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatXmlReq {
    private boolean isLinting = true;
    private ValidationRequest validationRequest;
}
