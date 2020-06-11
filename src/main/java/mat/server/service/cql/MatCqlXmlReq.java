package mat.server.service.cql;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;
import mat.model.cql.CQLModel;

@Data
@NoArgsConstructor
public class MatCqlXmlReq extends MatXmlReq {

    @NotBlank
    private String cql;
    private CQLModel sourceModel;

}
