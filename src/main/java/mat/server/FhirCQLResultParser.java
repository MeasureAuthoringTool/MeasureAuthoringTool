package mat.server;

import mat.model.cql.CQLModel;
import mat.shared.SaveUpdateCQLResult;

public interface FhirCQLResultParser {
    SaveUpdateCQLResult generateParsedCqlObject(String cqlValidationResponse, CQLModel cqlModel);
}
