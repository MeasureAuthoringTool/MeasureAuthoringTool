package mat.server.service.impl;

import mat.client.featureFlag.service.FeatureFlagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mat.model.clause.Measure;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MatContextServiceUtilTest {

    private MatContextServiceUtil util = new MatContextServiceUtil();
    private Measure measure = new Measure();
    private Map<String, Boolean> featureFlagMap = new HashMap<>();

    @Mock
    FeatureFlagService featureFlagService;

    @InjectMocks
    MatContextServiceUtil matContextServiceUtil;

    @Test
    public void testDefaultMeasureNotConvertible() {
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testMeasureConvertible() {
        createConvertible();
        Assertions.assertTrue(util.isMeasureConvertible(measure));
    }

    private void createConvertible() {
        measure.setMeasureModel("QDM");
        measure.setReleaseVersion("v5.8");
        measure.setQdmVersion("5.5");
    }

    @Test
    public void testFhir() {
        createConvertible();
        measure.setMeasureModel("FHIR");
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testPreCQL() {
        createConvertible();
        measure.setMeasureModel("Pre-CQL");
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testVersionFail() {
        createConvertible();
        measure.setReleaseVersion("v5.7");
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testQdmVersionFail() {
        createConvertible();
        measure.setQdmVersion("5.4");
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public  void testIsMeasureEditable() {
        featureFlagMap.put("FhirEdit", true);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        measure.setMeasureModel("QDM");

        assertEquals(true, matContextServiceUtil.isMeasureEditable(measure));
    }

    @Test
    public  void testIsMeasureEditableFail() {
        featureFlagMap.put("FhirEdit", false);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        measure.setMeasureModel("FHIR");

        assertEquals(false, matContextServiceUtil.isMeasureEditable(measure));
    }

    @Test
    public  void testIsCqlLibraryEditable() {
        featureFlagMap.put("FhirEdit", true);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        measure.setMeasureModel("QDM");

        assertEquals(true, matContextServiceUtil.isMeasureEditable(measure));
    }

    @Test
    public  void testIsCqlLibraryEditableFail() {
        featureFlagMap.put("FhirEdit", false);
        Mockito.when(featureFlagService.findFeatureFlags()).thenReturn(featureFlagMap);

        measure.setMeasureModel("FHIR");

        assertEquals(false, matContextServiceUtil.isMeasureEditable(measure));
    }
}
