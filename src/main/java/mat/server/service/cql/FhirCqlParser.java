package mat.server.service.cql;

import java.util.List;

import mat.model.cql.CQLModel;
import mat.shared.SaveUpdateCQLResult;

public interface FhirCqlParser {
    MatXmlResponse parse(String cql, CQLModel sourceModel);

    SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, List<LibraryErrors> libraryErrors);

    SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString);
}
