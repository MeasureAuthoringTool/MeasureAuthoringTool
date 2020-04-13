package mat.server.service;

import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;
import mat.model.cql.CQLLibraryDataSetObject;

public interface FhirCqlLibraryService {
    FhirConvertResultResponse convertCqlLibrary(CQLLibraryDataSetObject sourceLibrary, String loggedInUser) throws MatException;
}
