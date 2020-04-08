package mat.server.service.impl;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import mat.client.util.FeatureFlagConstant;
import mat.dao.impl.FeatureFlagDAOImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceImplTest {

    @Mock
    private FeatureFlagDAOImpl featureFlagDAO;

    @InjectMocks
    private FeatureFlagServiceImpl featureFlagServiceImpl;

    @Test
    void findFeatureFlagTest() {
        Map<String, Boolean> featureFlags = Map.of(
                "MAT_ON_FHIR", false,
                FeatureFlagConstant.FHIR_EDIT, true,
                "FHIR_DELETE", false);
        Mockito.when(featureFlagDAO.findFeatureFlags()).thenReturn(featureFlags);
        Map<String, Boolean> featureFlagMap = featureFlagServiceImpl.findFeatureFlags();
        assertEquals(3, featureFlagMap.size());
        Mockito.verify(featureFlagDAO, times(1)).findFeatureFlags();
        assertTrue(featureFlagServiceImpl.isFhirEditEnabled());
    }
}
