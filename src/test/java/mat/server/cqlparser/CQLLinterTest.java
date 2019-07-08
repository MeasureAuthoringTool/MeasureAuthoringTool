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
	
	private String invalidEditMessage = "Changes made to the CQL Library Name, Included Libraries, Value Sets, Codes, Codesystems, "
			+ "and/or Codesystem versions, can not be saved through the CQL Library Editor. "
			+ "Please make those changes in the appropriate areas of the CQL Workspace.";

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
	
	
	private void testWarningForMissingURNORIDCodeSystem() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testmissingurnoidcodesystem.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		CQLLinterConfig config = new CQLLinterConfig("testmissingurnoidcodesystem", "0.0.000", "QDM", "5.4");
		config.setPreviousCQLModel(model);
		CQLLinter linter = new CQLLinter(cql, config);
		
		assertEquals(true, linter.getWarningMessages().contains(invalidEditMessage));
	}
	
	private void testCommentsBetweenValueset() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testwrongcommentsbetweenvaluesets.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		CQLLinterConfig config = new CQLLinterConfig("testwrongcommentsbetweenvaluesets", "0.0.000", "QDM", "5.4");
		config.setPreviousCQLModel(model);
		CQLLinter linter = new CQLLinter(cql, config);
		
		assertEquals(1, linter.getWarningMessages().size());
		assertEquals(invalidEditMessage, linter.getWarningMessages().get(0));
	}
	
	private void testWrongCommentsValuesetsBetweenModelDeclarationAndValuesets() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testwrongcomments.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		CQLLinterConfig config = new CQLLinterConfig("testwrongcomments", "0.0.000", "QDM", "5.4");
		config.setPreviousCQLModel(model);
		CQLLinter linter = new CQLLinter(cql, config);
		
		assertEquals(1, linter.getErrorMessages().size());
		assertEquals(invalidEditMessage, linter.getErrorMessages().get(0));
	}
	
	private void addValuesetInfoToModel(String name, String oid, String codeName, CQLModel model) {
		CQLQualityDataSetDTO dto = new CQLQualityDataSetDTO();
		dto.setName(name);
		dto.setOid(oid);
		dto.setOriginalCodeListName(codeName);
		model.getValueSetList().add(dto);
	}
}
