package mat.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.event.MeasureSelectedEvent;
import mat.client.shared.MatContext;
import mat.model.FeatureFlag;

import java.util.ArrayList;
import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class MeasureComposerPresenterTest {

    private List<FeatureFlag> featureFlaglist = new ArrayList<>();

    @Before
    public void setup() {
        featureFlaglist.add(new FeatureFlag(1, "MAT_ON_FHIR", false));
        MatContext.get().setFeatureFlags(featureFlaglist);
    }

    @Test
    public void testHeadingFlagOn() {
        featureFlaglist.get(0).setFlagOn(true);
        MeasureSelectedEvent evt = new MeasureSelectedEvent("measureId", "v1", "measure1", "name1", "sctyp1", true,
                true, "", true, false, "TYPE1");
        MatContext.get().setCurrentMeasureInfo(evt);
        String headingValue = MeasureComposerPresenter.buildMeasureHeading("anyId");
        Assert.assertEquals("measure1 v1 (TYPE1)", headingValue);
    }

    @Test
    public void testHeadingFlagOff() {
        featureFlaglist.get(0).setFlagOn(false);
        MeasureSelectedEvent evt = new MeasureSelectedEvent("measureId", "v1", "measure1", "name1", "sctyp1", true,
                true, "", true, false, "TYPE1");
        MatContext.get().setCurrentMeasureInfo(evt);
        String headingValue = MeasureComposerPresenter.buildMeasureHeading("anyId");
        Assert.assertEquals("measure1 v1", headingValue);
    }
}
