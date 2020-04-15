package mat.server;

import mat.client.measure.service.CheckForConversionResult;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.server.service.FhirCqlLibraryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CQLLibraryServiceTest {

    @Mock
    CQLLibraryDAO cqlLibraryDAO;

    @Mock
    FhirCqlLibraryService fhirCqlLibraryService;

    @InjectMocks
    CQLLibraryService cqlLibraryService;

    @Test
    void testCheckLibraryForConversionNoDraftLibraries() {
        CQLLibraryDataSetObject cqlLibraryDataSetObject = new CQLLibraryDataSetObject();
        cqlLibraryDataSetObject.setId("libraryId");
        cqlLibraryDataSetObject.setCqlSetId("cqlLibrarySetId");

        List<CQLLibrary> cqlLibraries = new ArrayList<>();

        Mockito.when(cqlLibraryDAO.getDraftLibraryBySet(cqlLibraryDataSetObject.getCqlSetId())).thenReturn(cqlLibraries);

        CheckForConversionResult actualResult = cqlLibraryService.checkLibraryForConversion(cqlLibraryDataSetObject);

        assertTrue(actualResult.isProceedImmediately());
    }

    @Test
    void testCheckLibraryForConversionFhirDraft() {
        CQLLibraryDataSetObject cqlLibraryDataSetObject = new CQLLibraryDataSetObject();
        cqlLibraryDataSetObject.setId("libraryId");
        cqlLibraryDataSetObject.setCqlSetId("cqlLibrarySetId");

        CQLLibrary cqlLibrary = new CQLLibrary();
        cqlLibrary.setLibraryModelType(ModelTypeHelper.FHIR);
        cqlLibrary.setSetId("cqlLibrarySetId");

        List<CQLLibrary> cqlLibraries = new ArrayList<>();
        cqlLibraries.add(cqlLibrary);

        Mockito.when(cqlLibraryDAO.getDraftLibraryBySet(cqlLibraryDataSetObject.getCqlSetId())).thenReturn(cqlLibraries);

        CheckForConversionResult actualResult = cqlLibraryService.checkLibraryForConversion(cqlLibraryDataSetObject);

        assertTrue(actualResult.isConfirmBeforeProceed());
    }

    @Test
    void testCheckLibraryForConversionQDMDraft() {
        CQLLibraryDataSetObject cqlLibraryDataSetObject = new CQLLibraryDataSetObject();
        cqlLibraryDataSetObject.setId("libraryId");
        cqlLibraryDataSetObject.setCqlSetId("cqlLibrarySetId");

        CQLLibrary cqlLibrary = new CQLLibrary();
        cqlLibrary.setLibraryModelType(ModelTypeHelper.QDM);
        cqlLibrary.setSetId("cqlLibrarySetId");

        List<CQLLibrary> cqlLibraries = new ArrayList<>();
        cqlLibraries.add(cqlLibrary);

        Mockito.when(cqlLibraryDAO.getDraftLibraryBySet(cqlLibraryDataSetObject.getCqlSetId())).thenReturn(cqlLibraries);

        CheckForConversionResult actualResult = cqlLibraryService.checkLibraryForConversion(cqlLibraryDataSetObject);

        assertFalse(actualResult.isConfirmBeforeProceed());
        assertFalse(actualResult.isProceedImmediately());
    }

    @Test
    void testConvertCqlLibrary() throws MatException {
        CQLLibraryDataSetObject cqlLibraryDataSetObject = new CQLLibraryDataSetObject();
        FhirConvertResultResponse fhirConvertResultResponse = new FhirConvertResultResponse();
        LoggedInUserUtil.setLoggedInUser("LoggedInUser");
        Mockito.when(fhirCqlLibraryService.convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.anyString())).thenReturn(fhirConvertResultResponse);
        FhirConvertResultResponse result = cqlLibraryService.convertCqlLibrary(cqlLibraryDataSetObject);

        Assertions.assertNotNull(result);

        Mockito.verify(fhirCqlLibraryService, Mockito.times(1)).convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.any());
    }

    @Test
    public void testMatException() throws MatException {
        LoggedInUserUtil.setLoggedInUser("LoggedInUser");
        Mockito.when(fhirCqlLibraryService.convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.anyString())).thenThrow(new MatException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            cqlLibraryService.convertCqlLibrary(new CQLLibraryDataSetObject());
        });
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirCqlLibraryService, Mockito.times(1)).convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.any());
    }

    @Test
    public void testMatExceptionOnOtherException() throws MatException {
        LoggedInUserUtil.setLoggedInUser("LoggedInUser");
        Mockito.when(fhirCqlLibraryService.convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.anyString())).thenThrow(new IllegalArgumentException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            cqlLibraryService.convertCqlLibrary(new CQLLibraryDataSetObject());
        });
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirCqlLibraryService, Mockito.times(1)).convertCqlLibrary(Mockito.any(CQLLibraryDataSetObject.class), Mockito.any());
    }
}