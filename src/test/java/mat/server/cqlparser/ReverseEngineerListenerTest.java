package mat.server.cqlparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;

public class ReverseEngineerListenerTest {
	
	@Test
	public void testLibrary() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testlibrary.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		ReverseEngineerListener listener = new ReverseEngineerListener(cql, new CQLModel());	
		
		CQLModel model = listener.getCQLModel();
		
		assertEquals("a library comment", model.getLibraryComment());
		assertEquals("test", model.getLibraryName());
		assertEquals("0.0.1", model.getVersionUsed());
		assertEquals("QDM", model.getUsingName());
		assertEquals("5.4", model.getQdmVersion());
	}
	
	@Test
	public void testParameters() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testparameters.cql").getFile());
		
		String cql = new String(Files.readAllBytes(file.toPath()));
		ReverseEngineerListener listener = new ReverseEngineerListener(cql, new CQLModel());
	
		CQLModel model = listener.getCQLModel();
		
		CQLParameter parameter1 = model.getCqlParameters().get(0);
		CQLParameter parameter2 = model.getCqlParameters().get(1);
		
		assertEquals("Measurement Period", parameter1.getName());
		assertEquals("Interval<DateTime> /* measurement period logic comment */", parameter1.getLogic());
		assertEquals("measurement period comment", parameter1.getCommentString());
		
		assertEquals("Other Parameter", parameter2.getName());
		assertEquals("Interval<DateTime> /* other parameter logic comment */", parameter2.getLogic());
		assertEquals("other parameter comment", parameter2.getCommentString(), "other parameter comment");
	}
	
	@Test
	public void testDefinitions() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testdefinitions.cql").getFile());
		String cql = new String(Files.readAllBytes(file.toPath()));
		
		ReverseEngineerListener listener = new ReverseEngineerListener(cql, new CQLModel());

		CQLModel model = listener.getCQLModel();
		
		CQLDefinition definition1 = model.getDefinitionList().get(0);
		CQLDefinition definition2 = model.getDefinitionList().get(1);
		CQLDefinition definition3 = model.getDefinitionList().get(2);

		
		assertEquals("test", definition1.getName());
		assertEquals("/* test comment before logic */\n" + 
				"  true", definition1.getLogic());
		
		assertEquals("test comment", definition1.getCommentString());
		
		
		assertEquals("testwithnocomment", definition2.getName());
		assertEquals("true\n" + 
				"\n" + 
				"  /* testwithnocomment comment in logic */", definition2.getLogic());
		assertEquals("", definition2.getCommentString());
		
		
		assertEquals("testpopulation", definition3.getName());
		assertEquals("", definition3.getCommentString());
		assertEquals("true\n" + 
				"\n" + 
				"  /* last comment */", definition3.getLogic());
		assertEquals("Population", definition3.getContext());
	}

}
