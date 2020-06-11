package mat.server.service.cql;

import mat.model.cql.CQLModel;

public interface FhirCqlParser {
    MatXmlResponse parse(String cql, CQLModel sourceModel);
}
