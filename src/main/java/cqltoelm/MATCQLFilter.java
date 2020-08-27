package cqltoelm;

import mat.models.CQLGraph;
import mat.parsers.MATCQL2ELMListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.model.TranslatedLibrary;
import org.cqframework.cql.cql2elm.preprocessor.CqlPreprocessorVisitor;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.hl7.elm.r1.ExpressionDef;
import org.hl7.elm.r1.IncludeDef;
import org.hl7.elm.r1.Library;
import org.hl7.elm.r1.ParameterDef;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jmeyer on 1/5/2018.
 */
public class MATCQLFilter {

    private String parentLibraryString;
    private Map<String, String> childrenLibraries;
    private TranslatedLibrary library;
    private CqlTranslator translator;

    /**
     * Maps a valueset identifier to the datatypes it is using.
     */
    private Map<String, Set<String>> valuesetDataTypeMap = new HashMap<>();

    /**
     * Maps a code identifier to the datatypes it is using
     */
    private Map<String, Set<String>> codeDataTypeMap = new HashMap<>();

    /**
     * Maps an expression, to it's internal valueset - datatype map
     */
    private Map<String, Map<String, Set<String>>> expressionNameToValuesetDataTypeMap = new HashMap<>();

    /**
     * Maps an expression, to it's internal code - datatype map
     */
    private Map<String, Map<String, Set<String>>> expressionNameToCodeDataTypeMap = new HashMap<>();


    /**
     * Maps an expression name to its return type (only function and definitions)
     */
    private Map<String, String> nameToReturnTypeMap = new HashMap<>();

    /**
     * The list of parent expressions. Often times, these are populations from MAT. Anything that can be reached from
     * this node in the graph should be considered used.
     */
    private List<String> parentExpressions = new ArrayList<>();

    private HashMap<String, String> qdmTypeInfoMap = new HashMap<>();

    private Map<String, TranslatedLibrary> translatedLibraryMap;

    /**
     * Map in the form of <LibraryName-x.x.xxx, <ExpressionName, ReturnType>>.
     */
    private Map<String, Map<String, String>> allNamesToReturnTypeMap = new HashMap<>();
    
    private Map<String, String> expressionToReturnTypeMap = new HashMap<>();

    // used expression sets
    Set<String> usedLibraries = new HashSet<>();
    Set<String> usedCodes = new HashSet<>();
    Set<String> usedValuesets = new HashSet<>();
    Set<String> usedParameters = new HashSet<>();
    Set<String> usedDefinitions = new HashSet<>();
    Set<String> usedFunctions = new HashSet<>();
    Set<String> usedCodeSystems = new HashSet<>();


    public MATCQLFilter(
    		String parentLibraryString, 
    		Map<String, String> childrenLibraries, 
    		List<String> parentExpressions, 
    		CqlTranslator translator, 
    		Map<String, TranslatedLibrary> translatedLibraries) {
    	
        this.parentLibraryString = parentLibraryString;
        this.translator = translator;
        this.library = translator.getTranslatedLibrary();
        this.childrenLibraries = childrenLibraries;
        this.translatedLibraryMap = translatedLibraries;
        this.parentExpressions = parentExpressions;
    }

    /**
     * The CQL Filter Entry Point.
     *
     * This function will find all of the used CQL expressions, create a valueset - datatype map and code - datatype map,
     * and find return types for each expression.
     * @throws IOException
     */
    public void filter() throws IOException {
        InputStream stream = new ByteArrayInputStream(this.parentLibraryString.getBytes(StandardCharsets.UTF_8.name()));
        cqlLexer lexer = new cqlLexer(new ANTLRInputStream(stream));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cqlParser parser = new cqlParser(tokens);
        Set<String> librariesSet = new HashSet<>();
        Set<String> valuesetsSet = new HashSet<>();
        Set<String> codesSet = new HashSet<>();
        Set<String> codesystemsSet = new HashSet<>();
        Set<String> parametersSet = new HashSet<>();
        Set<String> definitionsSet = new HashSet<>();
        Set<String> functionsSet = new HashSet<>();
        Map<String, Map<String, Set<String>>> valuesetMap = new HashMap<>();
        Map<String, Map<String, Set<String>>> codeMap = new HashMap<>();


        CQLGraph graph = new CQLGraph();
        MATCQL2ELMListener listener = new MATCQL2ELMListener(graph, library, translatedLibraryMap, childrenLibraries);
        ParseTree tree = parser.library();
        CqlPreprocessorVisitor preprocessor = new CqlPreprocessorVisitor();
        preprocessor.visit(tree);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        librariesSet.addAll(listener.getLibraries());
        valuesetsSet.addAll(listener.getValuesets());
        codesSet.addAll(listener.getCodes());
        codesystemsSet.addAll(listener.getCodesystems());
        parametersSet.addAll(listener.getParameters());
        definitionsSet.addAll(listener.getDefinitions());
        functionsSet.addAll(listener.getFunctions());
        valuesetMap.putAll(listener.getValueSetDataTypeMap());
        codeMap.putAll(listener.getCodeDataTypeMap());

        collectUsedExpressions(graph, librariesSet, valuesetsSet, codesSet, codesystemsSet, parametersSet, definitionsSet, functionsSet);
        collectValueSetCodeDataType(valuesetMap, codeMap);
        collectReturnTypeMap();
    }

    private void collectUsedExpressions(CQLGraph graph, Set<String> librariesSet, Set<String> valuesetsSet, Set<String> codesSet,
                                        Set<String> codesystemsSet, Set<String> parametersSet, Set<String> definitionsSet,
                                        Set<String> functionsSet) {
        List<String> libraries = new ArrayList<>(librariesSet);
        List<String> valuesets = new ArrayList<>(valuesetsSet);
        List<String> codes = new ArrayList<>(codesSet);
        List<String> codesytems = new ArrayList<>(codesystemsSet);
        List<String> parameters = new ArrayList<>(parametersSet);
        List<String> definitions = new ArrayList<>(definitionsSet);
        List<String> functions = new ArrayList<>(functionsSet);

        for(String parentExpression : parentExpressions) {
            collectUsedLibraries(graph, libraries, parentExpression);
            collectUsedValuesets(graph, valuesets, parentExpression);
            collectUsedCodes(graph, codes, parentExpression);
            collectUsedCodeSystems(graph, codesytems, parentExpression);
            collectUsedParameters(graph, parameters, parentExpression);
            collectUsedDefinitions(graph, definitions, parentExpression);
            collectUsedFunctions(graph, functions, parentExpression);
        }
    }

    /**
     * For every function reference from the listener, checks if the parent expression and the function make a path.
     * If it does make a path, that means the function is used and should therefore be added to the used functions list.
     * @param graph the graph
     * @param functions the function references from the listener
     * @param parentExpression the parent expression to check
     */
    private void collectUsedFunctions(CQLGraph graph, List<String> functions, String parentExpression) {
        for(String function : functions) {
            if(graph.isPath(parentExpression, function)) {
                usedFunctions.add(function);
            }
        }
    }

    /**
     * For every definition reference from the listener, checks if the parent expression and the definition make a path.
     * If it does make a path, that means the definition is used and should therefore be added to the used definitions list.
     * @param graph the graph
     * @param definitions the definition references from the listener
     * @param parentExpression the parent expression to check
     */
    private void collectUsedDefinitions(CQLGraph graph, List<String> definitions, String parentExpression) {
        for(String definition : definitions) {
            if(graph.isPath(parentExpression, definition) && !definition.equalsIgnoreCase("Patient") && !definition.equalsIgnoreCase("Population")) {
                usedDefinitions.add(definition);
            }
        }
    }

    /**
     * For every parameter reference from the listener, checks if the parent expression and the parameter make a path.
     * If it does make a path, that means the parameter is used and should therefore be added to the used parameters list.
     * @param graph the graph
     * @param parameters the parameter references from the listener
     * @param parentExpression the parent expression to check
     */
    private void collectUsedParameters(CQLGraph graph, List<String> parameters, String parentExpression) {
        for(String parameter : parameters) {
            if(graph.isPath(parentExpression, parameter)) {
                usedParameters.add(parameter);
            }
        }
    }

    /**
     * For every code reference from the listener, checks if the parent expression and the code make a path.
     * If it does make a path, that means the code is used and should therefore be added to the used codes list.
     * @param graph the graph
     * @param codes the code references from the listener
     * @param parentExpression the parent expression to check
     */
    private void collectUsedCodes(CQLGraph graph, List<String> codes, String parentExpression) {
        for(String code : codes) {
            if(graph.isPath(parentExpression, code)) {
                usedCodes.add(code);
            }
        }
    }

    /**
     * For every codesystem reference from the listener, checks if the parent expression and the codesystem make a path.
     * If it does make a path, that means the codesystem is used and should therefore be added to the used codesystems list.
     * @param graph the graph
     * @param codesystems the code references from the listener
     * @param parentExpression the parent expression to check
     */
    private void collectUsedCodeSystems(CQLGraph graph, List<String> codesystems, String parentExpression) {
        for(String codesystem : codesystems) {
            if(graph.isPath(parentExpression, codesystem)) {
                usedCodeSystems.add(codesystem);
            }
        }
    }

    /**
     * For every valueset reference from the listener, checks if the parent expression and the valueset make a path.
     * If it does make a path, that means the valueset is used and should therefore be added to the used valuesets list.
     * @param graph the graph
     * @param valuesets the valueset references from the listener
     * @param parentExpression the parent expression to check
     */
    private void collectUsedValuesets(CQLGraph graph, List<String> valuesets, String parentExpression) {
        for(String valueset: valuesets) {
            if(graph.isPath(parentExpression, valueset)) {
                usedValuesets.add(valueset);
            }
        }
    }

    /**
     * For every library reference from the listener, checks if the parent expression and the library make a path.
     * If it does make a path, that means the library is used and should therefore be added to the used libraries list.
     * @param graph the graph
     * @param libraries the library references from the listener
     * @param parentExpression the parent expression to check
     */
    private void collectUsedLibraries(CQLGraph graph, List<String> libraries, String parentExpression) {
        for(String library : libraries) {
            if(graph.isPath(parentExpression, library)) {
                usedLibraries.add(library);
            }
        }
    }

    /**
     * Collects and creates a mapping of expression names to return types.
     */
    private void collectReturnTypeMap() {
        // the following makes an assumption that a library can not have any duplicate libraries declared in it.

        // statements contain all function and definitions.
        Library.Statements statements = this.library.getLibrary().getStatements();
        Library.Parameters parameters = this.library.getLibrary().getParameters();
        String libraryName = this.library.getIdentifier().getId();
        String libraryVersion = this.library.getIdentifier().getVersion();
        this.allNamesToReturnTypeMap.put(libraryName + "-" + libraryVersion, new HashMap<>());
        
        for(ExpressionDef expression : statements.getDef()) {
            this.allNamesToReturnTypeMap.get(libraryName + "-" + libraryVersion).put(expression.getName(), expression.getResultType().toString());
            this.nameToReturnTypeMap.put(expression.getName(), expression.getResultType().toString());
            this.expressionToReturnTypeMap.put(expression.getName(), expression.getResultType().toString());
        }
        
        if(parameters != null) {
            for(ParameterDef parameter : parameters.getDef()) {
                this.allNamesToReturnTypeMap.get(libraryName + "-" + libraryVersion).put(parameter.getName(), parameter.getResultType().toString());
                this.nameToReturnTypeMap.put(parameter.getName(), parameter.getResultType().toString());
                this.expressionToReturnTypeMap.put(parameter.getName(), parameter.getResultType().toString());
            }
        }

        
        if (null != this.library.getLibrary().getIncludes()) {
        	for(IncludeDef include : this.library.getLibrary().getIncludes().getDef()) {
        		TranslatedLibrary lib = this.translatedLibraryMap.get(include.getPath() + "-" + include.getVersion());

        		Library.Statements statementsFromIncludedLibrary = lib.getLibrary().getStatements();
        		Library.Parameters parametersFromIncludedLibrary = lib.getLibrary().getParameters();
        		String includedLibraryName = lib.getIdentifier().getId();
        		String includedLibraryVersion = lib.getIdentifier().getVersion();
        		this.allNamesToReturnTypeMap.put(includedLibraryName + "-" + includedLibraryVersion, new HashMap<>());

        		for(ExpressionDef expression : statementsFromIncludedLibrary.getDef()) {
        			this.allNamesToReturnTypeMap.get(includedLibraryName + "-" + includedLibraryVersion).put(expression.getName(), expression.getResultType().toString());
        			this.expressionToReturnTypeMap.put(include.getLocalIdentifier() + "." + expression.getName(), expression.getResultType().toString());
        		}

        		if(parametersFromIncludedLibrary != null) {
            		for(ParameterDef parameter : parametersFromIncludedLibrary.getDef()) {
            			this.allNamesToReturnTypeMap.get(includedLibraryName + "-" + includedLibraryVersion).put(parameter.getName(), parameter.getResultType().toString());
            			this.expressionToReturnTypeMap.put(include.getLocalIdentifier() + "." + parameter.getName(), parameter.getResultType().toString());
            		}
        		}
        	}        	
        }
    }

    /**
     * Collects the valueset - datatype map and code - datatype map.
     *
     * It loos through each translator object from the parser, and then for each translator it loops through the retrieves.
     * It then puts the valueset/code and it's corresponding data type into the correct map.
     */
    private void collectValueSetCodeDataType(Map<String, Map<String, Set<String>>> valuesetMap,
                                             Map<String, Map<String, Set<String>>> codeMap) {
        this.expressionNameToValuesetDataTypeMap = valuesetMap;
        this.expressionNameToCodeDataTypeMap = codeMap;
        this.valuesetDataTypeMap = flattenMap(valuesetMap);
        this.codeDataTypeMap = flattenMap(codeMap);
    }

    /**
     * The valueset/code - datatype map will come to us in a format of <ExpressionName, <Valueset/Code Name, [DataType]>>.
     * We want to also have a flattened map which will be in the format of <Valueset/Code Name, [DataType]>
     *
     * @return a map in the above format
     */
    private Map<String, Set<String>> flattenMap(Map<String, Map<String, Set<String>>> mapToFlatten) {
        Map<String, Set<String>> flattenedMap = new HashMap<>();

        Set<String> keys = mapToFlatten.keySet();
        for(String key : keys) {
            Map<String, Set<String>> innerMap = mapToFlatten.get(key);

            Set<String> innerKeys = innerMap.keySet();
            for(String innerKey : innerKeys) {
                if(flattenedMap.get(innerKey) == null) {
                    flattenedMap.put(innerKey, new HashSet());
                }

                flattenedMap.get(innerKey).addAll(innerMap.get(innerKey));
            }
        }

        return flattenedMap;
    }

    public Map<String, List<String>> getValuesetDataTypeMap() {
        Map<String, List<String>> valuesetDataTypeMapWithList = new HashMap<>();

        List<String> keySet = new ArrayList<>(valuesetDataTypeMap.keySet());

        for(String key : keySet) {
            List<String> dataTypes = new ArrayList<>(valuesetDataTypeMap.get(key));
            valuesetDataTypeMapWithList.put(key, dataTypes);
        }

        return valuesetDataTypeMapWithList;
    }

    public Map<String, List<String>> getCodeDataTypeMap() {
        Map<String, List<String>> codeDataTypeMapWithList = new HashMap<>();

        List<String> keySet = new ArrayList<>(codeDataTypeMap.keySet());

        for(String key : keySet) {
            List<String> dataTypes = new ArrayList<>(codeDataTypeMap.get(key));
            codeDataTypeMapWithList.put(key, dataTypes);
        }

        return codeDataTypeMapWithList;
    }

    public Map<String, String> getNameToReturnTypeMap() {
        return nameToReturnTypeMap;
    }

    public void setNameToReturnTypeMap(Map<String, String> nameToReturnTypeMap) {
        this.nameToReturnTypeMap = nameToReturnTypeMap;
    }

    public List<String> getUsedLibraries() {
        return new ArrayList<>(usedLibraries);
    }

    private List<String> formatUsedLibraries() {
        Set<String> usedLibraryFormatted = new HashSet<>();
        for(String usedLibrary : usedLibraries) {
            IncludeDef def = (IncludeDef) this.library.resolve(usedLibrary);
            usedLibraryFormatted.add(def.getPath() + "-" + def.getVersion() + "|" + usedLibrary);
        }

        return new ArrayList<>(usedLibraryFormatted);
    }

    public List<String> getUsedCodes() {
        return new ArrayList<>(usedCodes);
    }

    public List<String> getUsedCodeSystems() {
        return new ArrayList<>(usedCodeSystems);
    }

    public List<String> getUsedValuesets() {
        return new ArrayList<>(usedValuesets);
    }

    public List<String> getUsedParameters() {
        return new ArrayList<>(usedParameters);
    }

    public List<String> getUsedDefinitions() {
        return new ArrayList<>(usedDefinitions);
    }

    public List<String> getUsedFunctions() {
        return new ArrayList<>(usedFunctions);
    }

    public Map<String, Map<String, Set<String>>> getExpressionNameToValuesetDataTypeMap() {
        return expressionNameToValuesetDataTypeMap;
    }

    public Map<String, Map<String, Set<String>>> getExpressionNameToCodeDataTypeMap() {
        return expressionNameToCodeDataTypeMap;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\jmeyer\\git\\test-cql\\test-0.0.000.cql");
        String cqlString = cqlFileToString(file);
        // you could also create a string like String cqlString = <cql library string here>
        Map<String, String> childLibraries = new HashMap<>();
        childLibraries.put("test2-0.0.000", cqlFileToString(new File("C:\\Users\\jmeyer\\git\\test-cql\\test2-0.0.000.cql")));
        String[] formats = {"XML"};
        CQLtoELM cqLtoELM = new CQLtoELM(cqlString, childLibraries);
        cqLtoELM.doTranslation(true, "XML");
        List<String> parentExpressions = new ArrayList<>();
        parentExpressions.add("test");
        if(!cqLtoELM.getErrors().isEmpty()) {
            System.out.println(cqLtoELM.getErrors());
            return;
        }

        MATCQLFilter filter = new MATCQLFilter(cqlString, cqLtoELM.getCqlLibraryMapping(), parentExpressions, cqLtoELM.getTranslator(), cqLtoELM.getTranslatedLibraries());
        filter.filter();
        System.out.println(filter);
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("RETURN TYPE MAP: " + this.getAllNamesToReturnTypeMap());
        builder.append("\n");
        builder.append("VALUSET-DATATYPE MAP: " + this.getValuesetDataTypeMap());
        builder.append("\n");
        builder.append("CODE-DATATYPE MAP: " + this.getCodeDataTypeMap());
        builder.append("\n");
        builder.append("EXPRESSION NAME - VALUSET-DATATYPE MAP: " + this.getExpressionNameToValuesetDataTypeMap());
        builder.append("\n");
        builder.append("EXPRESSION NAME - CODE-DATATYPE MAP: " + this.getExpressionNameToCodeDataTypeMap());
        builder.append("\n");
        builder.append("USED LIBRARIES: " + this.getUsedLibraries());
        builder.append("\n");
        builder.append("USED VALUESETS: " + this.getUsedValuesets());
        builder.append("\n");
        builder.append("USED CODESYSTEMS: " + this.getUsedCodeSystems());
        builder.append("\n");
        builder.append("USED CODES: " + this.getUsedCodes());
        builder.append("\n");
        builder.append("USED PARAMETERS: " + this.getUsedParameters());
        builder.append("\n");
        builder.append("USED DEFINITIONS: " + this.getUsedDefinitions());
        builder.append("\n");
        builder.append("USED FUNCTIONS " + this.getUsedFunctions());

        return builder.toString();
    }


    /**
     * Converts a cql file to a cql string
     * @param file the file to convert
     * @return the cql string
     */
    private static String cqlFileToString(File file) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Map<String, String>> getAllNamesToReturnTypeMap() {
        return allNamesToReturnTypeMap;
    }

	public Map<String, String> getExpressionToReturnTypeMap() {
		return expressionToReturnTypeMap;
	}

}
