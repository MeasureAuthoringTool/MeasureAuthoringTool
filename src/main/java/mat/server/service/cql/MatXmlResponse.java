package mat.server.service.cql;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mat.model.cql.CQLModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MatXmlResponse {

    private List<LibraryErrors> errors = new ArrayList<>();
    @JsonSerialize(using = CqlModelSerializer.class)
    @JsonDeserialize(using = CqlModelDeserializer.class)
    private CQLModel cqlModel;
    private String cql;

}
