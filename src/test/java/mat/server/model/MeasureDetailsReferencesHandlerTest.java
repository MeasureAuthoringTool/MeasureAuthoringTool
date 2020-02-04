package mat.server.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mat.client.measure.ReferenceTextAndType;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;

public class MeasureDetailsReferencesHandlerTest {
    @Test
    public void testType() {
        MeasureDetailsReferencesHandler handler = new MeasureDetailsReferencesHandler();
        Object result = handler.convertUponSet(" a reference ");
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ReferenceTextAndType);
        ReferenceTextAndType ref = (ReferenceTextAndType) result;
        Assertions.assertTrue(result instanceof ReferenceTextAndType);
        Assertions.assertEquals(" a reference ", ref.getReferenceText());
        Assertions.assertEquals(MeasureReferenceType.UNKNOWN, ref.getReferenceType());
    }
}
