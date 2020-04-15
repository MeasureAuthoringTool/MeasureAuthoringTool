package mat.server;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import mat.model.cql.CQLModel;
import mat.shared.CQLError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirCQLResultParserTest {

    private static final String ERR_JSON =
            "{" +
                    "'libraryId': 'libraryId', " +
                    "'startLine': 1," +
                    "'startChar': 2," +
                    "'endLine': 3," +
                    "'endChar': 4," +
                    "'message': 'error message'," +
                    "'errorSeverity': 'Error'" +
                    "}";

    private static final String INC_ERR_JSON =
            "{" +
                    "'libraryId': 'libraryId', " +
                    "'targetIncludeLibraryId': 'includeTargetIncludeLibraryId', " +
                    "'targetIncludeLibraryVersionId': '0.1.1', " +
                    "'startLine': 0," +
                    "'startChar': 0," +
                    "'endLine': 0," +
                    "'endChar': 0," +
                    "'errorSeverity': 'Error'" +
                    "}";

    private static final String INC_WARM_JSON =
            "{" +
                    "'libraryId': 'libraryId', " +
                    "'targetIncludeLibraryId': 'includeTargetIncludeLibraryId', " +
                    "'targetIncludeLibraryVersionId': '0.1.1', " +
                    "'startLine': 0," +
                    "'startChar': 0," +
                    "'endLine': 0," +
                    "'endChar': 0," +
                    "'errorSeverity': 'Warning'" +
                    "}";

    private FhirCQLResultParser parser = new FhirCQLResultParserImpl();

    @Test
    public void testEmpty() {
        var cqlModel = new CQLModel();
        cqlModel.setLibraryName("lib");
        cqlModel.setVersionUsed("1.1.1");
        var result = parser.generateParsedCqlObject("{}", cqlModel);
        assertNotNull(result);
    }

    @Test
    public void testErrorExceptions() {
        var cqlModel = new CQLModel();
        cqlModel.setLibraryName("lib");
        cqlModel.setVersionUsed("1.1.1");

        var result = parser.generateParsedCqlObject("{ \"errorExceptions\": [" + ERR_JSON + "] }", cqlModel);
        assertNotNull(result);
        assertTrue(result.getIncludeLibrariesWithErrors().isEmpty());
        assertFalse(result.getLibraryNameErrorsMap().isEmpty());
        assertEquals(1, result.getLibraryNameErrorsMap().values().size());
        List<CQLError> errors = result.getLibraryNameErrorsMap().values().stream().findFirst().get();
        assertEquals(1, errors.size());
        CQLError cqlError = errors.get(0);
        assertEquals("error message", cqlError.getErrorMessage());
        assertEquals("Error", cqlError.getSeverity());
        assertEquals(1, cqlError.getErrorInLine());
        assertEquals(1, cqlError.getStartErrorInLine());
        assertEquals(2, cqlError.getErrorAtOffeset());
        assertEquals(3, cqlError.getEndErrorInLine());
        assertEquals(4, cqlError.getEndErrorAtOffset());
    }

    @Test
    public void testErrorInInclExceptions() {
        var cqlModel = new CQLModel();
        cqlModel.setLibraryName("lib");
        cqlModel.setVersionUsed("1.1.1");

        var result = parser.generateParsedCqlObject("{ \"errorExceptions\": [" + INC_ERR_JSON + "] }", cqlModel);
        assertNotNull(result);
        assertFalse(result.getLibraryNameErrorsMap().isEmpty());
        assertEquals(Collections.singleton("includeTargetIncludeLibraryId(0.1.1)"), result.getIncludeLibrariesWithErrors());
    }

    @Test
    public void testLibrary() {
        var cqlModel = new CQLModel();
        cqlModel.setLibraryName("lib");
        cqlModel.setVersionUsed("1.1.1");

        var result = parser.generateParsedCqlObject("{ 'library': { 'annotation': [" + ERR_JSON + "] } }", cqlModel);
        assertNotNull(result);
        assertFalse(result.getLibraryNameErrorsMap().isEmpty());
        assertTrue(result.getIncludeLibrariesWithErrors().isEmpty());
    }


    @Test
    public void testInclLibrary() {
        var cqlModel = new CQLModel();
        cqlModel.setLibraryName("lib");
        cqlModel.setVersionUsed("1.1.1");

        var result = parser.generateParsedCqlObject("{ 'library': { 'annotation': [" + INC_ERR_JSON + "] } }", cqlModel);
        assertNotNull(result);
        assertFalse(result.getLibraryNameErrorsMap().isEmpty());
        assertEquals(Collections.singleton("includeTargetIncludeLibraryId(0.1.1)"), result.getIncludeLibrariesWithErrors());
    }

    @Test
    public void testInclWarnLibrary() {
        var cqlModel = new CQLModel();
        cqlModel.setLibraryName("lib");
        cqlModel.setVersionUsed("1.1.1");

        var result = parser.generateParsedCqlObject("{ 'library': { 'annotation': [" + INC_WARM_JSON + "] } }", cqlModel);
        assertNotNull(result);
        assertTrue(result.getLibraryNameErrorsMap().isEmpty());
    }

}
