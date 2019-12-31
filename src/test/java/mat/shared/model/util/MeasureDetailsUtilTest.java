package mat.shared.model.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeasureDetailsUtilTest {

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

}
