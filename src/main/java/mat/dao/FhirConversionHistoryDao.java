package mat.dao;

import mat.model.clause.FhirConversionHistory;

public interface FhirConversionHistoryDao extends  IDAO<FhirConversionHistory, String> {
    FhirConversionHistory lookupByQdmSetId(String qdmSetId);
}
