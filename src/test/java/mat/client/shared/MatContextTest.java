package mat.client.shared;

import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.event.MeasureSelectedEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


public class MatContextTest {

    private MatContext mathContext;

    @Before
    public void setup() {
        mathContext = MatContext.get();
        MeasureSelectedEvent measureSelectedEvent =
                new MeasureSelectedEvent("getdm45mflhos", "v1.2",
                        "TestFhirMeasure", "TestName", "cohort", true, false,
                        "1232", true, true, "FHIR");
        mathContext.setCurrentMeasureInfo(measureSelectedEvent);

        CQLLibrarySelectedEvent.Builder builder = new CQLLibrarySelectedEvent.Builder()
                .withCqlLibraryVersion("v1.1")
                .withDraft(true)
                .withLibraryName("QDMTestLib")
                .withEditable(true)
                .withLibraryType("QDM");
        mathContext.setCurrentLibraryInfo(new CQLLibrarySelectedEvent(builder));
    }

    @Test
    public void testGetCurrentMeasureModel() {
        Assertions.assertEquals(mathContext.getCurrentMeasureModel(), "FHIR");

        //null test
        mathContext.setCurrentMeasureInfo(null);
        Assertions.assertEquals(mathContext.getCurrentMeasureModel(), "");
    }

    @Test
    public void testGetCurrentCQLLibraryModelType() {
        Assertions.assertEquals(mathContext.getCurrentCQLLibraryModelType(), "QDM");

        //null test
        mathContext.setCurrentLibraryInfo(null);
        Assertions.assertEquals(mathContext.getCurrentCQLLibraryModelType(), "");
    }
}
