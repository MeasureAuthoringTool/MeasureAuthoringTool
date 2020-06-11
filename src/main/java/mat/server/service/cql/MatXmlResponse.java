package mat.server.service.cql;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mat.model.cql.CQLModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MatXmlResponse {

    private List<LibraryErrors> errors = new ArrayList<>();
    private CQLModel cqlModel;
    private String cql;

}
