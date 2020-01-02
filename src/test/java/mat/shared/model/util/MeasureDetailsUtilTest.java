package mat.shared.model.util;

import mat.model.clause.Measure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeasureDetailsUtilTest {

    MeasureDetailsUtil measureDetailsUtil = new MeasureDetailsUtil();
    private Measure measure = new Measure();

    @Test
    public void testTypeNotBlank() {
        assertEquals("NOT_EMPTY", MeasureDetailsUtil.defaultTypeIfBlank("NOT_EMPTY"));
    }

    @Test
    public void testTypeBlank() {
        assertEquals("Pre-CQL", MeasureDetailsUtil.defaultTypeIfBlank(""));
    }

    @Test
    public void testTypeNull() {
        assertEquals("Pre-CQL", MeasureDetailsUtil.defaultTypeIfBlank(null));
    }

    public void createMeasure() {
        measure.setReleaseVersion("v5.8");
        measure.setQdmVersion("5.5");
        measure.setDraft(true);
        measure.setMeasureModel("FHIR");
    }

    @Test
    public void draftFhir() {
        createMeasure();

        Assertions.assertTrue(measureDetailsUtil.isValidatable(measure));
    }

    @Test
    public void notDraftFhir() {
        createMeasure();
        measure.setDraft(false);

        Assertions.assertFalse(measureDetailsUtil.isValidatable(measure));
    }

    @Test
    public void versionedQdm() {
        createMeasure();
        measure.setDraft(false);
        measure.setMeasureModel("QDM");

        Assertions.assertTrue(measureDetailsUtil.isValidatable(measure));
    }

    @Test
    public void notVersionedQdm() {
        createMeasure();
        measure.setMeasureModel("QDM");

        Assertions.assertFalse(measureDetailsUtil.isValidatable(measure));
    }

    @Test
    public void notReleaseVersion() {
        createMeasure();
        measure.setDraft(false);
        measure.setMeasureModel("QDM");
        measure.setReleaseVersion("5.6");

        Assertions.assertFalse(measureDetailsUtil.isValidatable(measure));
    }

    @Test
    public void notQdmVersion() {
        createMeasure();
        measure.setDraft(false);
        measure.setMeasureModel("QDM");
        measure.setQdmVersion("5.4");

        Assertions.assertFalse(measureDetailsUtil.isValidatable(measure));
    }
}
