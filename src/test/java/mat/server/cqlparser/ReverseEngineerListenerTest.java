package mat.server.cqlparser;

import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ReverseEngineerListenerTest {

    @Test
    public void testLibrary() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test-cql/testlibrary.cql").getFile());

        CQLModel previousModel = new CQLModel();
        previousModel.setLibraryName("test2");
        previousModel.setVersionUsed("0.0.2");
        previousModel.setUsingModel("QDM");
        previousModel.setUsingModelVersion("5.4");
        previousModel.setLibraryComment("a library comment");

        String cql = new String(Files.readAllBytes(file.toPath()));
        ReverseEngineerListener listener = new ReverseEngineerListener(cql, previousModel);

        CQLModel model = listener.getCQLModel();

        assertEquals("a library comment", model.getLibraryComment());
        assertEquals("test2", model.getLibraryName());
        assertEquals("0.0.2", model.getVersionUsed());
        assertEquals("QDM", model.getUsingModel());
        assertEquals("5.4", model.getUsingModelVersion());
    }

    @Test
    public void testFhirLibrary() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test-cql/testlibrary_fhir.cql").getFile());

        CQLModel previousModel = new CQLModel();
        previousModel.setLibraryName("test2");
        previousModel.setVersionUsed("0.0.2");
        previousModel.setUsingModel("FHIR");
        previousModel.setUsingModelVersion("4.0.1");
        previousModel.setLibraryComment("a library comment");

        String cql = new String(Files.readAllBytes(file.toPath()));
        ReverseEngineerListener listener = new ReverseEngineerListener(cql, previousModel);

        CQLModel model = listener.getCQLModel();

        assertEquals("a library comment", model.getLibraryComment());
        assertEquals("test2", model.getLibraryName());
        assertEquals("0.0.2", model.getVersionUsed());
        assertEquals("FHIR", model.getUsingModel());
        assertEquals("4.0.1", model.getUsingModelVersion());
    }

    @Test
    public void testParameters() throws IOException {
        testValidParameter();
        testParameterCopy();
    }

    private void testParameterCopy() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test-cql/testexistingexpressions.cql").getFile());

        CQLModel existingCqlModel = new CQLModel();
        CQLParameter parameter = new CQLParameter();
        parameter.setName("TestParameter");
        parameter.setParameterLogic("Interval<DateTime>");
        parameter.setId("123456");
        existingCqlModel.setCqlParameters(Arrays.asList(parameter));

        String cql = new String(Files.readAllBytes(file.toPath()));
        ReverseEngineerListener listener = new ReverseEngineerListener(cql, existingCqlModel);

        CQLParameter parameterAfterReverseEngineer = listener.getCQLModel().getCqlParameters().get(0);
        assertEquals("TestParameter", parameterAfterReverseEngineer.getParameterName());
        assertEquals("Boolean", parameterAfterReverseEngineer.getParameterLogic());
        assertEquals("123456", parameterAfterReverseEngineer.getId());

    }

    private void testValidParameter() throws IOException {
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
        assertEquals("other parameter comment", parameter2.getCommentString());
    }

    @Test
    public void testDefinitions() throws IOException {
        testValidDefinition();
        testDefinitionCopy();
    }

    private void testDefinitionCopy() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test-cql/testexistingexpressions.cql").getFile());

        CQLModel existingCqlModel = new CQLModel();
        CQLDefinition definition = new CQLDefinition();
        definition.setName("testdefinition");
        definition.setDefinitionLogic("false");
        definition.setId("123456");
        existingCqlModel.setDefinitionList(Arrays.asList(definition));

        String cql = new String(Files.readAllBytes(file.toPath()));
        ReverseEngineerListener listener = new ReverseEngineerListener(cql, existingCqlModel);

        CQLDefinition definitionAfterReverseEngineer = listener.getCQLModel().getDefinitionList().get(0);
        assertEquals("testdefinition", definitionAfterReverseEngineer.getDefinitionName());
        assertEquals("true", definitionAfterReverseEngineer.getDefinitionLogic());
        assertEquals("123456", definitionAfterReverseEngineer.getId());
    }

    private void testValidDefinition() throws IOException {
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
                "/* testwithnocomment comment in logic */", definition2.getLogic().replaceAll("\\r\\n", "\n"));
        assertEquals("", definition2.getCommentString());


        assertEquals("testpopulation", definition3.getName());
        assertEquals("", definition3.getCommentString());
        assertEquals("true\n" +
                "\n" +
                "/* last comment */", definition3.getLogic().replaceAll("\\r\\n", "\n"));
        assertEquals("Patient", definition3.getContext());
    }

    @Test
    public void testFunctions() throws IOException {
        testValidFuntions();
        testFunctionsWithSyntaxErrors();
        testExistingFunctionCopy();
    }

    private void testExistingFunctionCopy() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test-cql/testexistingexpressions.cql").getFile());

        CQLModel existingCqlModel = new CQLModel();
        CQLFunctions function = new CQLFunctions();
        function.setName("testfunction");
        function.setFunctionLogic("false");
        function.setId("123456");
        CQLFunctionArgument argument = new CQLFunctionArgument();
        argument.setArgumentName("arg");
        argument.setArgumentType("String");
        function.setArgumentList(Arrays.asList(argument));
        existingCqlModel.setCqlFunctions(Arrays.asList(function));

        String cql = new String(Files.readAllBytes(file.toPath()));
        ReverseEngineerListener listener = new ReverseEngineerListener(cql, existingCqlModel);

        CQLFunctions functionAfterReverseEngineer = listener.getCQLModel().getCqlFunctions().get(0);
        assertEquals("testfunction", functionAfterReverseEngineer.getFunctionName());
        assertEquals("true", functionAfterReverseEngineer.getFunctionLogic());
        assertEquals("123456", functionAfterReverseEngineer.getId());
        assertEquals(1, functionAfterReverseEngineer.getArgumentList().size());
        assertEquals("Boolean", functionAfterReverseEngineer.getArgumentList().get(0).getArgumentType());
        assertEquals("arg1", functionAfterReverseEngineer.getArgumentList().get(0).getArgumentName());
    }

    private void testValidFuntions() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test-cql/testfunctions.cql").getFile());
        String cql = new String(Files.readAllBytes(file.toPath()));
        ReverseEngineerListener listener = new ReverseEngineerListener(cql, new CQLModel());
        CQLModel model = listener.getCQLModel();

        CQLFunctions function1 = model.getCqlFunctions().get(0);
        CQLFunctions function2 = model.getCqlFunctions().get(1);

        assertEquals("test", function1.getName());
        assertEquals("Patient", function1.getContext());
        assertEquals("true", function1.getLogic());
        assertEquals("test comment", function1.getCommentString());
        assertEquals(3, function1.getArgumentList().size(), 3);
        assertEquals("arg1", function1.getArgumentList().get(0).getArgumentName(), "arg1");
        assertEquals("Boolean", function1.getArgumentList().get(0).getArgumentType());
        assertEquals("arg2", function1.getArgumentList().get(1).getArgumentName());
        assertEquals("Interval<DateTime>", function1.getArgumentList().get(1).getOtherType());
        assertEquals("arg3", function1.getArgumentList().get(2).getArgumentName());
        assertEquals("Encounter, Performed", function1.getArgumentList().get(2).getQdmDataType());

        assertEquals("testpopulationfunction", function2.getName());
        assertEquals("Patient", function2.getContext());
        assertEquals("true", function2.getLogic());
        assertEquals("testpouplationfunction comment", function2.getCommentString());
        assertEquals(0, function2.getArgumentList().size());
    }

    private void testFunctionsWithSyntaxErrors() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test-cql/testfunctionwithsyntaxerror.cql").getFile());
        String cql = new String(Files.readAllBytes(file.toPath()));
        ReverseEngineerListener listener = new ReverseEngineerListener(cql, new CQLModel());
        assertEquals(false, listener.getSyntaxErrors().isEmpty());
    }

}
