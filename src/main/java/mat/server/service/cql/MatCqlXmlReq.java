package mat.server.service.cql;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import mat.model.cql.CQLModel;

@Data
@NoArgsConstructor
public class MatCqlXmlReq extends MatXmlReq {

    @NotBlank
    private String cql;
    @JsonSerialize(using = CqlModelSerializer.class)
    @JsonDeserialize(using = CqlModelDeserializer.class)
    private CQLModel sourceModel;

}
