package mat.client;

import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.event.MeasureSelectedEvent;
import mat.client.shared.MatContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(GwtMockitoTestRunner.class)
public class MeasureComposerPresenterTest {

    private Map<String, Boolean> featureFlagMap = new HashMap<>();

    @Test
    public void testHeadingFlagOn() {
        featureFlagMap.put("MAT_ON_FHIR",true);
        MatContext.get().setFeatureFlags(featureFlagMap);

        MeasureSelectedEvent evt = new MeasureSelectedEvent("measureId", "v1", "measure1", "name1", "sctyp1", true,
                true, "", true, false, "TYPE1");
        MatContext.get().setCurrentMeasureInfo(evt);

        String headingValue = MeasureComposerPresenter.buildMeasureHeading("anyId");
        Assert.assertEquals("measure1 v1 (QDM / QDM)", headingValue);
    }

    @Test
    public void testHeadingFlagOff() {
        featureFlagMap.put("MAT_ON_FHIR",false);
        MatContext.get().setFeatureFlags(featureFlagMap);

        MeasureSelectedEvent evt = new MeasureSelectedEvent("measureId", "v1", "measure1", "name1", "sctyp1", true,
                true, "", true, false, "TYPE1");
        MatContext.get().setCurrentMeasureInfo(evt);
        String headingValue = MeasureComposerPresenter.buildMeasureHeading("anyId");
        Assert.assertEquals("measure1 v1", headingValue);
    }
}
