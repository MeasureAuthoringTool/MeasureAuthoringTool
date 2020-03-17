package mat.server.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.Measure;
import mat.model.cql.CQLLibraryShareDTO;
import mat.server.LoggedInUserUtil;
import mat.server.service.FeatureFlagService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggedInUserUtil.class})
public class MatContextServiceUtilTest {

    private static final String OWNER_USER_1 = "user1";
    private MatContextServiceUtil util = new MatContextServiceUtil();
    private Measure measure = new Measure();
    private CQLLibrary cqlLibrary = new CQLLibrary();
    private Map<String, Boolean> featureFlagMap = new HashMap<>();

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private MatContextServiceUtil matContextServiceUtil;

    @Before
    public void setup() {
        PowerMockito.mockStatic(LoggedInUserUtil.class);
    }

    @Test
    public void testDefaultMeasureNotConvertible() {
        assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testMeasureConvertible() {
        setUserAndRole();
        createConvertibleMeasure();
        assertTrue(util.isMeasureConvertible(measure));
    }

    @Test
    public void testMeasureCannotBeConvertedByOtherRegularUser() {
        PowerMockito.when(LoggedInUserUtil.getLoggedInUser()).thenReturn("OTHER_USER");
        PowerMockito.when(LoggedInUserUtil.getLoggedInUserRole()).thenReturn(SecurityRole.USER_ROLE);
        createConvertibleMeasure();
        assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testMeasureCannotBeConvertedByOtherSuperUser() {
        PowerMockito.when(LoggedInUserUtil.getLoggedInUser()).thenReturn("OTHER_USER");
        PowerMockito.when(LoggedInUserUtil.getLoggedInUserRole()).thenReturn(SecurityRole.SUPER_USER_ROLE);
        createConvertibleMeasure();
        assertTrue(util.isMeasureConvertible(measure));
    }


    private void createConvertibleMeasure() {
        measure.setMeasureModel("QDM");
        measure.setReleaseVersion("v5.8");
        measure.setQdmVersion("5.5");
        User owner = new User();
        owner.setId(OWNER_USER_1);
        measure.setOwner(owner);
    }

    @Test
    public void testFhirMeasureNotConvertible() {
        createConvertibleMeasure();
        measure.setMeasureModel("FHIR");
        assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testPreCQL() {
        createConvertibleMeasure();
        measure.setMeasureModel("Pre-CQL");
        assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testVersionFail() {
        createConvertibleMeasure();
        measure.setReleaseVersion("v5.7");
        assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testQdmVersionFail() {
        createConvertibleMeasure();
        measure.setQdmVersion("5.4");
        assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testIsMeasureEditable() {
        featureFlagMap.put("FhirEdit", true);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        measure.setMeasureModel("QDM");

        assertEquals(true, matContextServiceUtil.isMeasureModelEditable(measure.getMeasureModel()));
    }

    @Test
    public void testIsMeasureEditableFail() {
        featureFlagMap.put("FhirEdit", false);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        measure.setMeasureModel("QDM");

        assertEquals(true, matContextServiceUtil.isMeasureModelEditable(measure.getMeasureModel()));
    }

    @Test
    public void testIsMeasureEditableNullCheck() {
        featureFlagMap.put("FhirEdit", false);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        measure.setMeasureModel("");

        assertEquals(true, matContextServiceUtil.isMeasureModelEditable(measure.getMeasureModel()));
    }

    @Test
    public void testIsCqlLibraryEditable() {
        featureFlagMap.put("FhirEdit", true);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        cqlLibrary.setLibraryModelType("QDM");

        assertEquals(true, matContextServiceUtil.isCqlLibraryModelEditable(cqlLibrary.getLibraryModelType()));
    }

    @Test
    public void testIsCqlLibraryEditableFail() {
        featureFlagMap.put("FhirEdit", false);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        cqlLibrary.setLibraryModelType("QDM");

        assertEquals(true, matContextServiceUtil.isCqlLibraryModelEditable(cqlLibrary.getLibraryModelType()));
    }

    @Test
    public void testIsCqlLibraryEditableNullCheck() {
        featureFlagMap.put("FhirEdit", false);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        cqlLibrary.setLibraryModelType("");

        assertEquals(true, matContextServiceUtil.isCqlLibraryModelEditable(cqlLibrary.getLibraryModelType()));
    }

    @Test
    public void testCqlLibraryObjectConvertible() {
        setUserAndRole();

        CQLLibrary cqlLibrary = new CQLLibrary();
        cqlLibrary.setLibraryModelType("QDM");
        cqlLibrary.setQdmVersion("5.5");
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
        cqlLibrary.setQdmVersion("5.5");
        cqlLibrary.setReleaseVersion("5.8");
        cqlLibrary.setOwnerUserId(OWNER_USER_1);
        cqlLibrary.setDraft(false);
        assertTrue(matContextServiceUtil.isCqlLibraryConvertible(cqlLibrary));
    }

    private void setUserAndRole() {
        PowerMockito.when(LoggedInUserUtil.getLoggedInUser()).thenReturn(OWNER_USER_1);
        PowerMockito.when(LoggedInUserUtil.getLoggedInUserRole()).thenReturn(SecurityRole.USER_ROLE);
    }

}
