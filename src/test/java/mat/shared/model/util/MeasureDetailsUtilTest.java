package mat.shared.model.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import mat.client.measure.ReferenceTextAndType;
import mat.model.clause.Measure;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeasureDetailsUtilTest {

    private MeasureDetailsUtil measureDetailsUtil = new MeasureDetailsUtil();
    private Measure measure = new Measure();

    public void createMeasure() {
        measure.setReleaseVersion("v5.8");
        measure.setQdmVersion("5.5");
        measure.setDraft(true);
        measure.setMeasureModel("FHIR");
    }

    @Test
    public void draftFhir() {
        createMeasure();
        assertTrue(measureDetailsUtil.isValidatable(measure));
    }

    @Test
    public void draftFhirMeasureWithNoQdmVersion() {
        createMeasure();
        measure.setQdmVersion(null);
        assertTrue(measureDetailsUtil.isValidatable(measure));
    }


    @Test
    public void notDraftFhir() {
        createMeasure();
        measure.setDraft(false);
        assertFalse(measureDetailsUtil.isValidatable(measure));
    }

    @Test
    public void versionedQdm() {
        createMeasure();
        measure.setDraft(false);
        measure.setMeasureModel("QDM");
        assertTrue(measureDetailsUtil.isValidatable(measure));
    }

    @Test
    public void notVersionedQdm() {
        createMeasure();
        measure.setMeasureModel("QDM");
        assertFalse(measureDetailsUtil.isValidatable(measure));
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

    @Test
    public void testGetTrimmedListNull() {
        assertEquals(Collections.emptyList(), measureDetailsUtil.getTrimmedList(null));
    }

    @Test
    public void testGetTrimmedListEmpty() {
        assertEquals(Collections.emptyList(), measureDetailsUtil.getTrimmedList(Collections.emptyList()));
    }

    @Test
    public void testGetTrimmedSingleEntry() {
        List<ReferenceTextAndType> expectedList = Collections.singletonList(new ReferenceTextAndType("one", MeasureReferenceType.CITATION));
        List<ReferenceTextAndType> giveList = Collections.singletonList(new ReferenceTextAndType(" one ", MeasureReferenceType.CITATION));
        assertEquals(expectedList, measureDetailsUtil.getTrimmedList(giveList));
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
        assertEquals(expectedList, measureDetailsUtil.getTrimmedList(giveList));
    }

    @Test
    public void testIsPackageable() {
        assertFalse(MeasureDetailsUtil.isPackageable("FHIR", true));
        assertFalse(MeasureDetailsUtil.isPackageable("QDM", true));
        assertTrue(MeasureDetailsUtil.isPackageable("FHIR", false));
        assertFalse(MeasureDetailsUtil.isPackageable("QDM", false));
        assertFalse(MeasureDetailsUtil.isPackageable("", false));
    }

}