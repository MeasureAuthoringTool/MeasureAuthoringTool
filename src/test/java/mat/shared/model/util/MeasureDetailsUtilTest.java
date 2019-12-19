package mat.shared.model.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
