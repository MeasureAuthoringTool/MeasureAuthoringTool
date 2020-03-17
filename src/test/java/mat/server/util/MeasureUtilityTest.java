package mat.server.util;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MeasureUtilityTest {

    @Test
    public void testVersionTextIfDraftable() {
        String version = "4.002";
        String revisionNumber = "005";
        boolean isDraft = true;
        assertEquals("Draft based on v4.2", MeasureUtility.getVersionText(version, isDraft));
        assertEquals( "Draft v4.2.005", MeasureUtility.getVersionTextWithRevisionNumber(version, revisionNumber, isDraft));
        assertEquals("4.2.005", MeasureUtility.formatVersionText(revisionNumber, version));
        assertEquals("4.2.005", MeasureUtility.formatVersionText(version + "." + revisionNumber));
        assertEquals("4.2", MeasureUtility.formatVersionText(version));
    }

    @Test
    public void testVersionTextIfNotDraftable() {
        String version = "4.002";
        String revisionNumber = "005";
        boolean isDraft = false;
        assertEquals("v4.2", MeasureUtility.getVersionText(version, isDraft));
        assertEquals("v4.2", MeasureUtility.getVersionTextWithRevisionNumber(version, revisionNumber, isDraft));
    }
}