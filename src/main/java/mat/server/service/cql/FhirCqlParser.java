package mat.server.service.cql;

import mat.model.cql.CQLModel;
import mat.shared.SaveUpdateCQLResult;

import java.util.List;

public interface FhirCqlParser {
    MatXmlResponse parse(String cql, CQLModel sourceModel);

    MatXmlResponse parse(String cql, CQLModel sourceModel, ValidationRequest validationRequest);

    MatXmlResponse parseFromMeasure(String measureId);

    MatXmlResponse parseFromLib(String libId);

    SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, MatXmlResponse fhirResponse);

    SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, MatXmlResponse matXmlResponse, ValidationRequest validationRequest);

    SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString);

    SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString, ValidationRequest validationRequest);
}
