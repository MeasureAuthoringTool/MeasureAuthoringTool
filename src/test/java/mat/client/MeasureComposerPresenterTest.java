package mat.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.event.MeasureSelectedEvent;
import mat.client.shared.MatContext;
import mat.model.FeatureFlag;

@RunWith(GwtMockitoTestRunner.class)
public class MeasureComposerPresenterTest {

    private FeatureFlag featureFlag;

    @Before
    public void setup() {
        featureFlag = new FeatureFlag();
        MatContext.get().setMatOnFHIR(featureFlag);
    }

    @Test
    public void testHeadingFlagOn() {
        featureFlag.setFlagOn(true);
        MeasureSelectedEvent evt = new MeasureSelectedEvent("measureId", "v1", "measure1", "name1", "sctyp1", true,
                true, "", true, false, "TYPE1");
        MatContext.get().setCurrentMeasureInfo(evt);
        String headingValue = MeasureComposerPresenter.buildMeasureHeading("anyId");
        Assert.assertEquals("measure1 v1 (TYPE1)", headingValue);
    }

    @Test
    public void testHeadingFlagOff() {
        featureFlag.setFlagOn(false);
        MeasureSelectedEvent evt = new MeasureSelectedEvent("measureId", "v1", "measure1", "name1", "sctyp1", true,
                true, "", true, false, "TYPE1");
        MatContext.get().setCurrentMeasureInfo(evt);
        String headingValue = MeasureComposerPresenter.buildMeasureHeading("anyId");
        Assert.assertEquals("measure1 v1", headingValue);
    }
}
