package mat.model.cql.validator;

import static org.junit.Assert.*;

import org.junit.Test;

import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;

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
		validator.validate(includedLibrary, model);
		assertEquals(true, validator.isValid());
	}
	
	private void testInvalidSpecialCharacters(String name) {
		CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
		CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
		CQLModel model = new CQLModel();
		includedLibrary.setAliasName(name);
		validator.validate(includedLibrary, model);
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
		validator.validate(includedLibrary, model);
		assertEquals(false, validator.isValid());
		assertEquals(true, validator.getMessages().contains(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION));
	}
	
	private void testGoodNamingConventions(String name) {
		CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
		CQLIncludeLibraryValidator validator = new CQLIncludeLibraryValidator();
		CQLModel model = new CQLModel();
		includedLibrary.setAliasName(name);
		validator.validate(includedLibrary, model);
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
		validator.validate(includedLibrary, model);
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
		validator.validate(includedLibrary, model);
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
		validator.validate(includedLibrary, model);
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
		validator.validate(includedLibrary, model);
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
		validator.validate(includedLibrary, model);
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
		validator.validate(includedLibrary, model);
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
		validator.validate(includedLibrary, model);
		assertEquals(false, validator.isValid());
		assertEquals(true, validator.getMessages().contains(DUPLICATE_CQL_KEYWORD));
	}
}
