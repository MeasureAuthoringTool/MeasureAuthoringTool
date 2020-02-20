package mat.client.cqlworkspace.attributes;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import mat.client.shared.FhirDataType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InsertFhirAttributesDialogModelTest {

    @Test
    public void testDataTypesRequired() {
        assertThrows(IllegalArgumentException.class, () -> {
            new InsertFhirAttributesDialogModel(null);
        });
    }

    @Test
    public void testEmptyDataTypes() {
        InsertFhirAttributesDialogModel model = new InsertFhirAttributesDialogModel(Collections.emptyMap());
        assertTrue(model.getDataTypesByResource().isEmpty());
    }

    @Test
    public void testDataTypes() {
        FhirDataType dt1 = new FhirDataType("id1", "res1");
        InsertFhirAttributesDialogModel model = new InsertFhirAttributesDialogModel(Collections.singletonMap("res1", dt1));
        assertFalse(model.getDataTypesByResource().isEmpty());
        assertEquals(1, model.getDataTypesByResource().size());
        assertTrue(model.getDataTypesByResource().containsKey("res1"));
    }

}
