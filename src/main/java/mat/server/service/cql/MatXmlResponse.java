package mat.server.service.cql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mat.model.cql.CQLModel;
import mat.shared.CQLObject;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MatXmlResponse {

    private List<LibraryErrors> errors = new ArrayList<>();
    private CQLModel cqlModel;
    private String cql;
    private CQLObject cqlObject;

}
