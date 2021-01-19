package mat.server;

import mat.client.measure.service.CheckForConversionResult;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.server.service.FhirCqlLibraryService;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
class CQLLibraryServiceTest {

    @Mock
    private CQLLibraryDAO cqlLibraryDAO;

    @Mock
    private FhirCqlLibraryService fhirCqlLibraryService;

    @InjectMocks
    private CQLLibraryService cqlLibraryService;


    @Test
    public void testConvertCqlLibrary() throws MatException, MarshalException, MappingException, ValidationException, IOException {
        CQLLibraryDataSetObject cqlLibraryDataSetObject = new CQLLibraryDataSetObject();
        FhirConvertResultResponse fhirConvertResultResponse = new FhirConvertResultResponse();
        LoggedInUserUtil.setLoggedInUser("LoggedInUser");
        Mockito.when(fhirCqlLibraryService.convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.anyString())).thenReturn(fhirConvertResultResponse);
        FhirConvertResultResponse result = cqlLibraryService.convertCqlLibrary(cqlLibraryDataSetObject);

        Assertions.assertNotNull(result);

        Mockito.verify(fhirCqlLibraryService, Mockito.times(1)).convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.any());
    }

    @Test
    public void testMatException() throws MatException, MarshalException, MappingException, ValidationException, IOException {
        LoggedInUserUtil.setLoggedInUser("LoggedInUser");
        Mockito.when(fhirCqlLibraryService.convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.anyString())).thenThrow(new MatException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            cqlLibraryService.convertCqlLibrary(new CQLLibraryDataSetObject());
        });
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirCqlLibraryService, Mockito.times(1)).convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.any());
    }

    @Test
    public void testMatExceptionOnOtherException() throws MatException, MarshalException, MappingException, ValidationException, IOException {
        LoggedInUserUtil.setLoggedInUser("LoggedInUser");
        Mockito.when(fhirCqlLibraryService.convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.anyString())).thenThrow(new IllegalArgumentException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            cqlLibraryService.convertCqlLibrary(new CQLLibraryDataSetObject());
        });
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirCqlLibraryService, Mockito.times(1)).convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.any());
    }
}