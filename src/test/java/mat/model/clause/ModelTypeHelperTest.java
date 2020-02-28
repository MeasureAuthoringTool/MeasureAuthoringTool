package mat.model.clause;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTypeHelperTest {

    @Test
    public void testTypeNotBlank() {
        assertEquals("NOT_EMPTY", ModelTypeHelper.defaultTypeIfBlank("NOT_EMPTY"));
    }

    @Test
    public void testTypeBlank() {
        assertEquals("Pre-CQL", ModelTypeHelper.defaultTypeIfBlank(""));
    }

    @Test
    public void testTypeNull() {
        assertEquals("Pre-CQL", ModelTypeHelper.defaultTypeIfBlank(null));
    }

    @Test
    public void testFhir() {
        assertTrue(ModelTypeHelper.isFhir(ModelTypeHelper.FHIR));
    }

    @Test
    public void testNotFhir() {
        assertFalse(ModelTypeHelper.isFhir("NotFHIR"));
    }

    @Test
    public void testPreCql() {
        assertTrue(ModelTypeHelper.isPreQL(ModelTypeHelper.PRE_CQL));
    }

    @Test
    public void testNotPreCql() {
        assertFalse(ModelTypeHelper.isPreQL("NotPre-CQL"));
    }

    @Test
    public void testQdm() {
        assertTrue(ModelTypeHelper.isQdm(ModelTypeHelper.QDM));
    }

    @Test
    public void testNotQdm() {
        assertFalse(ModelTypeHelper.isQdm("Notqdm"));
    }

}
