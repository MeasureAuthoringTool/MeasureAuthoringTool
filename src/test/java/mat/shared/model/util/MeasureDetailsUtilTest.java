package mat.shared.model.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeasureDetailsUtilTest {

    MeasureDetailsUtil measureDetailsUtil = new MeasureDetailsUtil();

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
    public void testIsValidatable() {
        assertEquals(false, measureDetailsUtil.isValidatable("v5.8", "5.5", true, true, null));
        assertEquals(true, measureDetailsUtil.isValidatable("v5.8", "5.5", true, true, "FHIR"));
        assertEquals(false, measureDetailsUtil.isValidatable("v5.8", "5.5", false, true, "FHIR"));
        assertEquals(true, measureDetailsUtil.isValidatable("v5.8", "5.5", false, true, "QDM"));
        assertEquals(false, measureDetailsUtil.isValidatable("v5.7", "5.5", false, true, "QDM"));
        assertEquals(false, measureDetailsUtil.isValidatable("v5.8", "5.4", false, true, "QDM"));
        assertEquals(false, measureDetailsUtil.isValidatable("v5.8", "5.5", false, false, "QDM"));
        assertEquals(false, measureDetailsUtil.isValidatable("v5.8", "5.5", false, true, null));
    }
}
