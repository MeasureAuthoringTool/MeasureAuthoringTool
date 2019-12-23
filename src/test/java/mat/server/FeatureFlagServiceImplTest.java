package mat.server;

import mat.dao.impl.FeatureFlagDAOImpl;
import mat.model.FeatureFlag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceImplTest {

    @Mock
    FeatureFlagDAOImpl featureFlagDAO;

    @InjectMocks
    FeatureFlagServiceImpl featureFlagServiceImpl;

    @Test
    void findFeatureFlagTest() {
        List<FeatureFlag> featureFlaglist = new ArrayList<>();
        featureFlaglist.add(new FeatureFlag(1, "MAT_ON_FHIR", false));
        featureFlaglist.add(new FeatureFlag(2, "FHIR_EDIt", true));
        featureFlaglist.add(new FeatureFlag(3, "FHIR_DELETE", false));

        Mockito.when(featureFlagDAO.findAllFeatureFlags()).thenReturn(featureFlaglist);
        Map<String, Boolean> featureFlagMap = featureFlagServiceImpl.findFeatureFlags();
        assertEquals(3,featureFlagMap.size());
        Mockito.verify(featureFlagDAO, times(1)).findAllFeatureFlags();
    }
}