package mat.shared.model.util;

import mat.model.clause.Measure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void testFhir() {
        assertTrue(MeasureDetailsUtil.isFhir(MeasureDetailsUtil.FHIR));
    }

    @Test
    public void testNotFhir() {
        assertFalse(MeasureDetailsUtil.isFhir("NotFHIR"));
    }

    @Test
    public void testPreCql() {
        assertTrue(MeasureDetailsUtil.isPreQL(MeasureDetailsUtil.PRE_CQL));
    }

    @Test
    public void testNotPreCql() {
        assertFalse(MeasureDetailsUtil.isPreQL("NotPre-CQL"));
    }

    @Test
    public void testQdm() {
        assertTrue(MeasureDetailsUtil.isQdm(MeasureDetailsUtil.QDM));
    }

    @Test
    public void testNotQdm() {
        assertFalse(MeasureDetailsUtil.isQdm("Notqdm"));
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
    }

    @Test
    public void getModelTypeDisplayNameTest() {
        assertEquals("QDM / QDM", MeasureDetailsUtil.getModelTypeDisplayName(null));
        assertEquals("QDM / CQL", MeasureDetailsUtil.getModelTypeDisplayName("QDM"));
        assertEquals("FHIR / CQL", MeasureDetailsUtil.getModelTypeDisplayName("FHIR"));
    }
}