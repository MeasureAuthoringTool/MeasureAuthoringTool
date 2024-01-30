package mat.server.service.impl;

import mat.client.util.FeatureFlagConstant;
import mat.dao.impl.FeatureFlagDAOImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceImplTest {

    @Mock
    private FeatureFlagDAOImpl featureFlagDAO;

    @InjectMocks
    private FeatureFlagServiceImpl featureFlagServiceImpl;

    @Test
    void findFeatureFlagTest() {
        ReflectionTestUtils.setField(featureFlagServiceImpl, "madieFeatureFlag", true);
        ReflectionTestUtils.setField(featureFlagServiceImpl, "madieQdmFeatureFlag", true);
        ReflectionTestUtils.setField(featureFlagServiceImpl, "enableQdmRepeatTransfer", true);
        Map<String, Boolean> featureFlags = new HashMap<>();
        featureFlags.put(FeatureFlagConstant.MAT_ON_FHIR, true);
        featureFlags.put(FeatureFlagConstant.FHIR_BONNIE, false);
        Mockito.when(featureFlagDAO.findFeatureFlags()).thenReturn(featureFlags);
        Map<String, Boolean> featureFlagMap = featureFlagServiceImpl.findFeatureFlags();
        assertEquals(5, featureFlagMap.size());
    }
}
