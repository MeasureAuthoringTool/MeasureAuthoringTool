package mat.shared.model.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mat.client.measure.ReferenceTextAndType;
import mat.model.clause.Measure;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;

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
    public void testGetTrimmedListNull() {
        Assertions.assertEquals(Collections.emptyList(), measureDetailsUtil.getTrimmedList(null));
    }

    @Test
    public void testGetTrimmedListEmpty() {
        Assertions.assertEquals(Collections.emptyList(), measureDetailsUtil.getTrimmedList(Collections.emptyList()));
    }

    @Test
    public void testGetTrimmedSingleEntry() {
        List<ReferenceTextAndType> expectedList = Collections.singletonList(new ReferenceTextAndType("one", MeasureReferenceType.CITATION));
        List<ReferenceTextAndType> giveList = Collections.singletonList(new ReferenceTextAndType(" one ", MeasureReferenceType.CITATION));
        Assertions.assertEquals(expectedList, measureDetailsUtil.getTrimmedList(giveList));
    }


    @Test
    public void testGetTrimmedList() {
        List<ReferenceTextAndType> expectedList = Arrays.asList(
                new ReferenceTextAndType("one", MeasureReferenceType.CITATION),
                new ReferenceTextAndType("two", MeasureReferenceType.JUSTIFICATION),
                new ReferenceTextAndType("three", MeasureReferenceType.DOCUMENTATION)
        );
        List<ReferenceTextAndType> giveList = Arrays.asList(
                new ReferenceTextAndType(" one     ", MeasureReferenceType.CITATION),
                new ReferenceTextAndType("     two ", MeasureReferenceType.JUSTIFICATION),
                new ReferenceTextAndType(" three ", MeasureReferenceType.DOCUMENTATION)
        );
        Assertions.assertEquals(expectedList, measureDetailsUtil.getTrimmedList(giveList));
    }

}