package mat.server.service;

import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirLibraryPackageResult;
import mat.client.shared.MatException;
import mat.model.cql.CQLLibraryDataSetObject;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;

public interface FhirCqlLibraryService {
    FhirConvertResultResponse convertCqlLibrary(CQLLibraryDataSetObject sourceLibrary, String loggedInUser) throws MatException, MarshalException, MappingException, IOException, ValidationException;

    void pushCqlLib(String libId);

    FhirLibraryPackageResult packageCqlLib(String libId);
}
