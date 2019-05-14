package mat.server.cqlparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
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
		assertEquals("true", definition1.getLogic());
		assertEquals("test comment", definition1.getCommentString());
		
		
		assertEquals("testwithnocomment", definition2.getName());
		assertEquals("true\n" + 
				"\n" + 
				"  /* testwithnocomment comment in logic */", definition2.getLogic().replaceAll("\\r\\n", "\n"));
		assertEquals("", definition2.getCommentString());
		
		
		assertEquals("testpopulation", definition3.getName());
		assertEquals("", definition3.getCommentString());
		assertEquals("true\n" + 
				"\n" + 
				"  /* last comment */", definition3.getLogic().replaceAll("\\r\\n", "\n"));
		assertEquals("Population", definition3.getContext());
	}
	
	@Test
	public void testFunctions() throws IOException {
		testValidFuntions();
		testFunctionsWithSyntaxErrors();
	}
	
	private void testValidFuntions() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testfunctions.cql").getFile());
		String cql = new String(Files.readAllBytes(file.toPath()));
		ReverseEngineerListener listener = new ReverseEngineerListener(cql, new CQLModel());
		CQLModel model = listener.getCQLModel();
		
		CQLFunctions function1 = model.getCqlFunctions().get(0);
		CQLFunctions function2 = model.getCqlFunctions().get(1);
		
		assertEquals(function1.getName(), "test");
		assertEquals(function1.getContext(), "Patient");
		assertEquals(function1.getLogic(), "true");
		assertEquals(function1.getCommentString(), "test comment");
		assertEquals(function1.getArgumentList().size(), 3);
		assertEquals(function1.getArgumentList().get(0).getArgumentName(), "arg1");
		assertEquals(function1.getArgumentList().get(0).getArgumentType(), "Boolean");
		assertEquals(function1.getArgumentList().get(1).getArgumentName(), "arg2");
		assertEquals(function1.getArgumentList().get(1).getOtherType(), "Interval<DateTime>");
		assertEquals(function1.getArgumentList().get(2).getArgumentName(), "arg3");
		assertEquals(function1.getArgumentList().get(2).getQdmDataType(), "Encounter, Performed");

		
		assertEquals(function2.getName(), "testpouplationfunction");
		assertEquals(function2.getContext(), "Population");
		assertEquals(function2.getLogic(), "true");
		assertEquals(function2.getCommentString(), "testpouplationfunction comment");
		assertEquals(function2.getArgumentList().size(), 0);
	}
	
	private void testFunctionsWithSyntaxErrors() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("test-cql/testfunctionwithsyntaxerror.cql").getFile());
		String cql = new String(Files.readAllBytes(file.toPath()));
		ReverseEngineerListener listener = new ReverseEngineerListener(cql, new CQLModel());		
		assertEquals(listener.getSyntaxErrors().isEmpty(), false);
	}

}
