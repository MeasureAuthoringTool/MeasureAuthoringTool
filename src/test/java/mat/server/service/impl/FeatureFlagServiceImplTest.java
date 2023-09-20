package mat.server.service.impl;

import mat.client.util.FeatureFlagConstant;
import mat.dao.impl.FeatureFlagDAOImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceImplTest {

    @Mock
    private FeatureFlagDAOImpl featureFlagDAO;

    @InjectMocks
    private FeatureFlagServiceImpl featureFlagServiceImpl;

    @Test
    @Disabled //TODO Hasn't been run for a time, review.
    void findFeatureFlagTest() {
        Map<String, Boolean> featureFlags = Map.of(
                FeatureFlagConstant.MAT_ON_FHIR, true,
                FeatureFlagConstant.FHIR_BONNIE, false);
        Mockito.when(featureFlagDAO.findFeatureFlags()).thenReturn(featureFlags);
        Map<String, Boolean> featureFlagMap = featureFlagServiceImpl.findFeatureFlags();
        assertEquals(2, featureFlagMap.size());
    }
}
