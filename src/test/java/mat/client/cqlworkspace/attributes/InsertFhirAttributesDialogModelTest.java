package mat.client.cqlworkspace.attributes;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mat.client.shared.FhirDataType;

public class InsertFhirAttributesDialogModelTest {

    @Test
    public void testDataTypesRequired() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new InsertFhirAttributesDialogModel(null);
        });
    }

    @Test
    public void testEmptyDataTypes() {
        InsertFhirAttributesDialogModel model = new InsertFhirAttributesDialogModel(Collections.emptyMap());
        Assertions.assertTrue(model.getDataTypes().isEmpty());
    }

    @Test
    public void testDataTypes() {
        InsertFhirAttributesDialogModel model = new InsertFhirAttributesDialogModel(Collections.singletonMap("id1", new FhirDataType("id1", "res1")));
        Assertions.assertFalse(model.getDataTypes().isEmpty());
        Assertions.assertEquals(1, model.getDataTypes().size());
        Assertions.assertTrue(model.getDataTypes().containsKey("id1"));
    }

}
