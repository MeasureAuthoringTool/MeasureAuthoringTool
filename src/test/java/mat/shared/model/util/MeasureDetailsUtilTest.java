package mat.shared.model.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import mat.client.measure.ReferenceTextAndType;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeasureDetailsUtilTest {

    private MeasureDetailsUtil measureDetailsUtil = new MeasureDetailsUtil();

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