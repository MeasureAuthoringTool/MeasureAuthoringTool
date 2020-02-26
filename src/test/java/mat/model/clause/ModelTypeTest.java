package mat.model.clause;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTypeTest {

    @Test
    public void testTypeNotBlank() {
        assertEquals("NOT_EMPTY", ModelType.defaultTypeIfBlank("NOT_EMPTY"));
    }

    @Test
    public void testTypeBlank() {
        assertEquals("Pre-CQL", ModelType.defaultTypeIfBlank(""));
    }

    @Test
    public void testTypeNull() {
        assertEquals("Pre-CQL", ModelType.defaultTypeIfBlank(null));
    }

    @Test
    public void testFhir() {
        assertTrue(ModelType.isFhir(ModelType.FHIR));
    }

    @Test
    public void testNotFhir() {
        assertFalse(ModelType.isFhir("NotFHIR"));
    }

    @Test
    public void testPreCql() {
        assertTrue(ModelType.isPreQL(ModelType.PRE_CQL));
    }

    @Test
    public void testNotPreCql() {
        assertFalse(ModelType.isPreQL("NotPre-CQL"));
    }

    @Test
    public void testQdm() {
        assertTrue(ModelType.isQdm(ModelType.QDM));
    }

    @Test
    public void testNotQdm() {
        assertFalse(ModelType.isQdm("Notqdm"));
    }

}
