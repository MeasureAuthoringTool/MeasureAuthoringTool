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
        assertEquals(MeasureUtility.getVersionText(version, isDraft), "Draft based on v4.2");
        assertEquals(MeasureUtility.getVersionTextWithRevisionNumber(version, revisionNumber, isDraft), "Draft v4.2.005");
        assertEquals(MeasureUtility.formatVersionText(revisionNumber, version), "4.2.005");
        assertEquals(MeasureUtility.formatVersionText(version + "." + revisionNumber), "4.2.005");
        assertEquals(MeasureUtility.formatVersionText(version), "4.2");
    }

    @Test
    public void testVersionTextIfNotDraftable() {
        String version = "4.002";
        String revisionNumber = "005";
        boolean isDraft = false;
        assertEquals(MeasureUtility.getVersionText(version, isDraft), "v4.2");
        assertEquals(MeasureUtility.getVersionTextWithRevisionNumber(version, revisionNumber, isDraft), "v4.2");
    }
}