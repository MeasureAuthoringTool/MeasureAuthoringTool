package mat.server.cqlparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Test;

import mat.model.cql.CQLModel;
import mat.model.cql.CQLQualityDataSetDTO;

public class CQLLinterTest {
	
	private CQLModel model;

	@Before
	public void before() {
		model = new CQLModel();
		addValuesetInfoToModel("Ethnicity", "2.16.840.1.114222.4.11.837", "Ethnicity", model);
		addValuesetInfoToModel("ONC Administrative Sex", "2.16.840.1.113762.1.4.1", "ONC Administrative Sex", model);
		addValuesetInfoToModel("Payer", "2.16.840.1.114222.4.11.3591", "Payer", model);
		addValuesetInfoToModel("Race", "2.16.840.1.114222.4.11.836", "Race", model);
	}

	@Test
	public void testCommentError() throws IOException {
		testCommentsBetweenValueset();
		testWrongCommentsValuesetsBetweenModelDeclarationAndValuesets();
	}
	
	@Test
	public void testWarningForMissingURNOID() throws IOException {
		testWarningForMissingURNOIDValueset();
		testWarningForMissingURNORIDCodeSystem();
	}
	
	private void testWarningForMissingURNOIDValueset() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testmissingurnoidvalueset.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		CQLLinterConfig config = new CQLLinterConfig("testmissingurnoidvalueset", "0.0.000", "QDM", "5.4");
		config.setPreviousCQLModel(model);
		CQLLinter linter = new CQLLinter(cql, config);
		
		assertEquals(1, linter.getWarningMessages().size());
		assertEquals("The MAT has added \"urn:oid:\" to value set and/or codesystem declarations where necessary to ensure items are in the correct format.", linter.getWarningMessages().get(0));
	}
	
	private void testWarningForMissingURNORIDCodeSystem() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testmissingurnoidcodesystem.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		CQLLinterConfig config = new CQLLinterConfig("testmissingurnoidcodesystem", "0.0.000", "QDM", "5.4");
		config.setPreviousCQLModel(model);
		CQLLinter linter = new CQLLinter(cql, config);
		
		assertEquals(true, linter.getWarningMessages().contains("The MAT has added \"urn:oid:\" to value set and/or codesystem declarations where necessary to ensure items are in the correct format."));
	}
	
	private void testCommentsBetweenValueset() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testwrongcommentsbetweenvaluesets.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		CQLLinterConfig config = new CQLLinterConfig("testwrongcommentsbetweenvaluesets", "0.0.000", "QDM", "5.4");
		config.setPreviousCQLModel(model);
		CQLLinter linter = new CQLLinter(cql, config);
		
		assertEquals(1, linter.getWarningMessages().size());
		assertEquals("A comment was added in an incorrect location and could not be saved. Comments are permitted between the CQL Library declaration and the Model declaration, directly above a parameter, definition, or function.", linter.getWarningMessages().get(0));
	}
	
	private void testWrongCommentsValuesetsBetweenModelDeclarationAndValuesets() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testwrongcomments.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		CQLLinterConfig config = new CQLLinterConfig("testwrongcomments", "0.0.000", "QDM", "5.4");
		config.setPreviousCQLModel(model);
		CQLLinter linter = new CQLLinter(cql, config);
		
		assertEquals(1, linter.getErrorMessages().size());
		assertEquals("A comment was added in an incorrect location and could not be saved. "
				+ "Comments are permitted between the CQL Library declaration and the Model declaration, "
				+ "directly above a parameter or define statement, or within a parameter or define statement.", linter.getErrorMessages().get(0));
	}
	
	private void addValuesetInfoToModel(String name, String oid, String codeName, CQLModel model) {
		CQLQualityDataSetDTO dto = new CQLQualityDataSetDTO();
		dto.setName(name);
		dto.setOid(oid);
		dto.setOriginalCodeListName(codeName);
		model.getValueSetList().add(dto);
	}
}
