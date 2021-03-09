package mat.server.service.impl;

import mat.dao.FhirConversionHistoryDao;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.model.cql.CQLLibraryShareDTO;
import mat.server.LoggedInUserUtil;
import mat.server.service.FeatureFlagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggedInUserUtil.class})
public class MatContextServiceUtilTest {

    private static final String OWNER_USER_1 = "user1";
    private Measure measure = new Measure();
    private CQLLibrary cqlLibrary = new CQLLibrary();
    private Map<String, Boolean> featureFlagMap = new HashMap<>();

    @Mock
    private FeatureFlagService featureFlagService;

    @Mock
    private FhirConversionHistoryDao fhirConversionHistoryDao;

    @InjectMocks
    private MatContextServiceUtil matContextServiceUtil;

    @Before
    public void setup() {
        PowerMockito.mockStatic(LoggedInUserUtil.class);
    }

    @Test
    public void testDefaultMeasureNotConvertible() {
        assertFalse(matContextServiceUtil.isMeasureConvertible(measure));
    }

    @Test
    public void testMeasureConvertible() {
        setUserAndRole();
        createConvertibleMeasure();
        assertTrue(matContextServiceUtil.isMeasureConvertible(measure));
    }

    @Test
    public void testMeasureCannotBeConvertedByOtherRegularUser() {
        PowerMockito.when(LoggedInUserUtil.getLoggedInUser()).thenReturn("OTHER_USER");
        PowerMockito.when(LoggedInUserUtil.getLoggedInUserRole()).thenReturn(SecurityRole.USER_ROLE);
        createConvertibleMeasure();
        assertFalse(matContextServiceUtil.isMeasureConvertible(measure));
    }

    @Test
    public void testMeasureCannotBeConvertedByOtherSuperUser() {
        PowerMockito.when(LoggedInUserUtil.getLoggedInUser()).thenReturn("OTHER_USER");
        PowerMockito.when(LoggedInUserUtil.getLoggedInUserRole()).thenReturn(SecurityRole.SUPER_USER_ROLE);
        createConvertibleMeasure();
        assertTrue(matContextServiceUtil.isMeasureConvertible(measure));
    }


    private void createConvertibleMeasure() {
        measure.setMeasureModel("QDM");
        measure.setReleaseVersion("v5.8");
        measure.setQdmVersion("5.6");
        measure.setMeasureSet(new MeasureSet());
        measure.getMeasureSet().setId("FUBAR");
        User owner = new User();
        owner.setId(OWNER_USER_1);
        measure.setOwner(owner);
    }

    @Test
    public void testFhirMeasureNotConvertible() {
        createConvertibleMeasure();
        measure.setMeasureModel("FHIR");
        assertFalse(matContextServiceUtil.isMeasureConvertible(measure));
    }

    @Test
    public void testCompositeMeasureIsNotConvertible() {
        createConvertibleMeasure();
        measure.setIsCompositeMeasure(true);
        assertFalse(matContextServiceUtil.isMeasureConvertible(measure));
    }

    @Test
    public void testPreCQL() {
        createConvertibleMeasure();
        measure.setMeasureModel("Pre-CQL");
        assertFalse(matContextServiceUtil.isMeasureConvertible(measure));
    }

    @Test
    public void testVersionFail() {
        createConvertibleMeasure();
        measure.setReleaseVersion("v5.7");
        assertFalse(matContextServiceUtil.isMeasureConvertible(measure));
    }

    @Test
    public void testQdmVersionFail() {
        createConvertibleMeasure();
        measure.setQdmVersion("5.4");
        assertFalse(matContextServiceUtil.isMeasureConvertible(measure));
    }

    @Test
    public void testCqlLibraryObjectConvertible() {
        setUserAndRole();

        CQLLibrary cqlLibrary = new CQLLibrary();
        cqlLibrary.setLibraryModelType("QDM");
        cqlLibrary.setQdmVersion("5.6");
        cqlLibrary.setReleaseVersion("5.8");
        cqlLibrary.setOwnerId(new User());
        cqlLibrary.getOwnerId().setId(OWNER_USER_1);
        cqlLibrary.setDraft(false);
        assertTrue(matContextServiceUtil.isCqlLibraryConvertible(cqlLibrary));
    }

    @Test
    public void testCqlLibraryDtoConvertible() {
        setUserAndRole();

        CQLLibraryShareDTO cqlLibrary = new CQLLibraryShareDTO();
        cqlLibrary.setLibraryModelType("QDM");
        cqlLibrary.setQdmVersion("5.6");
        cqlLibrary.setReleaseVersion("5.8");
        cqlLibrary.setOwnerUserId(OWNER_USER_1);
        cqlLibrary.setDraft(false);
        assertTrue(matContextServiceUtil.isCqlLibraryConvertible(cqlLibrary));
    }

    private void setUserAndRole() {
        PowerMockito.when(LoggedInUserUtil.getLoggedInUser()).thenReturn(OWNER_USER_1);
        PowerMockito.when(LoggedInUserUtil.getLoggedInUserRole()).thenReturn(SecurityRole.USER_ROLE);
    }

    // Validation
    public void createValidationMeasure() {
        measure.setReleaseVersion("v5.8");
        measure.setQdmVersion("5.6");
        measure.setDraft(true);
        measure.setMeasureModel("FHIR");
    }

    @Test
    public void testValidationDraftFhir() {
        createValidationMeasure();
        assertTrue(matContextServiceUtil.isValidatable(measure));
    }

    @Test
    public void testValidationDraftFhirMeasureWithNoQdmVersion() {
        createValidationMeasure();
        measure.setQdmVersion(null);
        assertTrue(matContextServiceUtil.isValidatable(measure));
    }

    @Test
    public void testValidationNotValidatableDraftFhir() {
        createValidationMeasure();
        measure.setDraft(false);
        assertFalse(matContextServiceUtil.isValidatable(measure));
    }

    @Test
    public void testValidationVersionedQdm() {
        createValidationMeasure();
        measure.setDraft(false);
        measure.setMeasureModel("QDM");
        assertTrue(matContextServiceUtil.isValidatable(measure));
    }

    @Test
    public void testValidationVersionedQdmComposite() {
        createValidationMeasure();
        measure.setDraft(false);
        measure.setMeasureModel("QDM");
        measure.setIsCompositeMeasure(true);
        assertFalse(matContextServiceUtil.isValidatable(measure));
    }


    @Test
    public void testValidationNotVersionedQdm() {
        createValidationMeasure();
        measure.setMeasureModel("QDM");
        assertFalse(matContextServiceUtil.isValidatable(measure));
    }

    @Test
    public void testValidationNotReleaseVersion() {
        createValidationMeasure();
        measure.setDraft(false);
    }

}
