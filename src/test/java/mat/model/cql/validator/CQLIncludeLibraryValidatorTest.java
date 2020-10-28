package mat.model.cql.validator;

import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CQLIncludeLibraryValidatorTest {

    private String LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION = "Invalid Library Alias. Must be unique, start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";
    private String DUPLICATE_CQL_KEYWORD = "A library alias can not be an exact match to a defined CQL keyword.";

    @Test
    public void testDoesAliasNameHaveSpecialCharacter() {
        testInvalidSpecialCharacters("bad*name");
        testInvalidSpecialCharacters("badname>");
        testInvalidSpecialCharacters("bad(name");
        testInvalidSpecialCharacters("!badname");
        testInvalidSpecialCharacters("bad name");


        testValidSpecialCharacters("good");
        testValidSpecialCharacters("_good");
        testValidSpecialCharacters("good99");
    }

    private void testValidSpecialCharacters(String name) {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        includedLibrary.setAliasName(name);
        includedLibrary.setLibraryModelType(ModelTypeHelper.QDM);
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(true, validator.isValid());
    }

    private void testInvalidSpecialCharacters(String name) {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        includedLibrary.setAliasName(name);
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(false, validator.isValid());
        assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
    }

    @Test
    public void testDoesAliasNameFollowCQLAliasNamingConvention() {
        testBadNamingConventions("9bad");
        testBadNamingConventions("!bad");
        testGoodNamingConventions("_good");
        testGoodNamingConventions("good");
    }

    private void testBadNamingConventions(String name) {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        includedLibrary.setAliasName(name);
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(false, validator.isValid());
        assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
    }

    private void testGoodNamingConventions(String name) {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        includedLibrary.setAliasName(name);
        includedLibrary.setLibraryModelType(ModelTypeHelper.QDM);
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(true, validator.isValid());
    }

    @Test
    public void testIsDuplicateIdentifierName() {
        testIsDuplicateAsDefinition();
        testIsDuplicateAsFunction();
        testIsDuplicateAsParameter();
        testIsDuplicateAsValueset();
        testIsDuplicateAsCode();
    }

    private void testIsDuplicateAsDefinition() {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        CQLDefinition definition = new CQLDefinition();
        definition.setName("duplicate");
        model.getDefinitionList().add(definition);
        includedLibrary.setAliasName("duplicate");
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(false, validator.isValid());
        assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
    }

    private void testIsDuplicateAsFunction() {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        CQLFunctions function = new CQLFunctions();
        function.setName("duplicate");
        model.getCqlFunctions().add(function);
        includedLibrary.setAliasName("duplicate");
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(false, validator.isValid());
        assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
    }

    private void testIsDuplicateAsParameter() {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        CQLParameter parameter = new CQLParameter();
        parameter.setName("duplicate");
        model.getCqlParameters().add(parameter);
        includedLibrary.setAliasName("duplicate");
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(false, validator.isValid());
        assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
    }

    private void testIsDuplicateAsValueset() {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        CQLQualityDataSetDTO valueset = new CQLQualityDataSetDTO();
        valueset.setName("duplicate");
        model.getValueSetList().add(valueset);
        includedLibrary.setAliasName("duplicate");
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(false, validator.isValid());
        assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
    }

    private void testIsDuplicateAsCode() {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        CQLCode code = new CQLCode();
        code.setDisplayName("duplicate");
        model.getCodeList().add(code);
        includedLibrary.setAliasName("duplicate");
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(false, validator.isValid());
        assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
    }

    @Test
    public void testIsDuplicateCQLLibraryName() {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        model.setLibraryName("duplicate");
        includedLibrary.setAliasName("duplicate");
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertEquals(false, validator.isValid());
        assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
    }

    @Test
    public void testIsCQLReservedKeyword() {
        testIsKeyword("expand");
        testIsKeyword("per");
        testIsKeyword("true");
        testIsKeyword("false");
    }

    private void testIsKeyword(String name) {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        includedLibrary.setAliasName(name);
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertFalse(validator.isValid());
        assertTrue(validator.getMessages().contains(DUPLICATE_CQL_KEYWORD));
    }

    @Test
    public void testValidateModelTypes() {
        CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
        CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
        CQLModel model = new CQLModel();
        includedLibrary.setAliasName("test");
        includedLibrary.setLibraryModelType(ModelTypeHelper.QDM);

        //both model types same, no error
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertTrue(validator.isValid());
        assertEquals(validator.getMessages().size(), 0);

        //models don't match, should give error
        validator = new CQLIncludeLibraryValidator();
        includedLibrary.setLibraryModelType(ModelTypeHelper.QDM);
        validator.validate(includedLibrary, model, ModelTypeHelper.FHIR);
        assertFalse(validator.isValid());
        assertTrue(validator.getMessages().contains("Only FHIR library items may be added to FHIR measures."));

        validator = new CQLIncludeLibraryValidator();
        includedLibrary.setLibraryModelType(ModelTypeHelper.FHIR);
        validator.validate(includedLibrary, model, ModelTypeHelper.QDM);
        assertFalse(validator.isValid());
        assertTrue(validator.getMessages().contains("Only QDM library items may be added to QDM measures."));

        // for pre-cql, null model types
        validator = new CQLIncludeLibraryValidator();
        includedLibrary.setLibraryModelType(null);
        validator.validate(includedLibrary, model, null);
        assertTrue(validator.isValid());
    }
}
