package mat.server.model;

import mat.client.measure.ReferenceTextAndType;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeasureDetailsReferencesHandlerTest {
    @Test
    public void testType() {
        MeasureDetailsReferencesHandler handler = new MeasureDetailsReferencesHandler();
        Object result = handler.convertUponSet(" a reference ");
        assertNotNull(result);
        assertTrue(result instanceof ReferenceTextAndType);
        ReferenceTextAndType ref = (ReferenceTextAndType) result;
        assertTrue(result instanceof ReferenceTextAndType);
        assertEquals(" a reference ", ref.getReferenceText());
        assertEquals(MeasureReferenceType.UNKNOWN, ref.getReferenceType());
    }
}
