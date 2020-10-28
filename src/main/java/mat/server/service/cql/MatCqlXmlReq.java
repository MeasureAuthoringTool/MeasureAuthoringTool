package mat.server.service.cql;

import lombok.Data;
import lombok.NoArgsConstructor;
import mat.model.cql.CQLModel;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class MatCqlXmlReq extends MatXmlReq {

    @NotBlank
    private String cql;
    private CQLModel sourceModel;

}
