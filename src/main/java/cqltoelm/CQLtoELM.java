package cqltoelm;

import mat.models.CQLExpressionError;
import mat.models.LibraryHolder;
import mat.parsers.TrackbackListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.DefaultLibrarySourceProvider;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.TranslatedLibrary;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.hl7.elm.r1.Element;
import org.hl7.elm.r1.ExpressionDef;
import org.hl7.elm.r1.IncludeDef;
import org.hl7.elm.r1.Library;
import org.hl7.elm.r1.ParameterDef;
import org.hl7.elm.r1.VersionedIdentifier;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Jack Meyer
 *
 * The Measure Authoring Tool CQL to ELM Wrapper.
 */
public class CQLtoELM {
    /**
     * The parent cql library string
     */
    private String parentCQLLibraryString;

    /**
     * The cql library mapping. It should follow the format <String, String>, where the key is in the format
     * LibraryName-x.x.xxx and the value should be the cql library.
     */
    private Map<String, String> cqlLibraryMapping;

    /**
     * The parent library cql file
     */
    private File parentCQLLibraryFile;

    /**
     * The parent elm string from cql-to-elm translation
     */
    private String parentElmString;

    /**
     * This list of elm strings from cql-to-elm translation
     */
    private List<String> elmStrings = new ArrayList<>();

    /**
     * The parent json elm string
     */
    private String parentJsonString;

    /**
     * The list of json elm strings from the cql-to-elm translation
     */
    private List<String> jsonStrings = new ArrayList<>();

    /**
     * The cql library holder mapping. It should follow the format. <String, String>, where the key is in the format
     * LibraryName-x.x.xxx and the value should be the cql library.
     */
    private Map<String, LibraryHolder> libraryHolderMap = new HashMap<>();

    /**
     * The parent cql library
     */
    private Library parentLibrary;

    /**
     * The messagse from cql-to-elm translation
     */
    private List<CqlTranslatorException> messages = new ArrayList<>();

    /**
     * The warnings from cql-to-elm translation
     */
    private List<CqlTranslatorException> warnings = new ArrayList<>();

    /**
     * The errors from cql-to-elm translation
     */
    private List<CqlTranslatorException> errors = new ArrayList<>();

    private Map<String, TrackBack> trackBackMap = new HashMap<>();

    private CqlTranslator translator;

    private LibraryManager libraryManager;
    
    private Map<String, TranslatedLibrary> translatedLibraries = new HashMap<>(); 


    /**
     * CQL to ELM constructor from strings.
     * @param parentCQLLibraryString the parent cql library string
     * @param cqlLibraryMapping the cql library mapping. It should follow the format <String, String> where the key is
     *                          in the format LibraryName-x.x.xxx and the value should be the cql library string.
     */
    public CQLtoELM(String parentCQLLibraryString, Map<String, String> cqlLibraryMapping) {
        this.parentCQLLibraryString = parentCQLLibraryString;
        this.cqlLibraryMapping = cqlLibraryMapping;
        this.parentCQLLibraryFile = null;
    }

    /**
     * CQL to ELM constructor from files
     * @param parentCQLLibraryFile the parent cql library file
     */
    public CQLtoELM(File parentCQLLibraryFile) {
        this.parentCQLLibraryFile = parentCQLLibraryFile;
        this.parentCQLLibraryString = null;
        this.cqlLibraryMapping = null;
    }

    /**
     * The basic do translation method
     * @param validationOnly validation only flag, when this flag is set to true, no exports will be generated
     */
    public void doTranslation(boolean validationOnly) {

        // MAT-8665
            // list path traversal should be 'on'
            // method style invocation should be 'off'
            // list demotion should be 'off'

            // annotations should be 'on'
            // locators should be 'on'
            // result types should be 'off'
            // detailed errors should be 'off'

        // MAT-8702
            // messages should not come out in the ELM, therefore we will have our error level as 'Error'

        // MAT-9295
            // list demotion should be disabled
            // list promotion should be disabled
            // interval demotion should be disabled
            // interval promotion should be disabled

        // by default MAT will produce both
        List<String> formats = new ArrayList<>();
        formats.add("XML");
        formats.add("JSON");
        doTranslation(false, true, true, false, false, false,
                true, true, false, false,
                true, true, CqlTranslatorException.ErrorSeverity.Error, validationOnly, formats);
    }

    /**
     * The do translation method that
     * @param validationOnly
     * @param format the format string "XML" or "JSON"
     */
    public void doTranslation(boolean validationOnly, String format) {

        List<String> formats = new ArrayList<>();
        formats.add(format);
        doTranslation(false, true, true, false, false, false,
                true, true, false, false,
                true, true, CqlTranslatorException.ErrorSeverity.Error, validationOnly, formats);
    }

    /**
     * The do translation method with all of the flags
     * @param enableDateRangeOptimization flag for enabling data range optimizations, when true, data range optimization
     *                                    will happen
     * @param enableAnnotations flag for enabling annotations, when true, annotations will appear in ELM output
     * @param enableLocators flag for enabling locators, when true, locators will appear in ELM output
     * @param enableResultTypes flag for enabling result types, when true, result types will appear in ELM output
     * @param enableDetailedErrors flag for enabling detailed errors, when true, translator will give detailed errors
     * @param disableListTraversal flag for disabling list traversal, when true, list traversal will not happen
     * @param disableListDemotion flag for disabling list demotion, when true, list demotion will not happen
     * @param disableListPromotion flag for disabling list promotion, when true, list promotion will not happen
     * @param enableIntervalDemotion flag for enabling interval promotion, when true, interval promotion will happen
     * @param enableIntervalPromotion flag for enabling interval promotion, when true, interval promotion will happen
     * @param disableMethodInvocation flag for disabling method invocation, when true, method invocation will not happen
     * @param errorSeverity flag for the error severity level
     * @param validationOnly flag for running in validation only mode, when true, no exports will be generated
     * @param validateUnits flag for determining if the translator should validate units based on the ucum units
     * @param formats a list of strings to format
     */
    public void doTranslation(boolean enableDateRangeOptimization, boolean enableAnnotations, boolean enableLocators,
                              boolean enableResultTypes, boolean enableDetailedErrors, boolean disableListTraversal,
                              boolean disableListDemotion, boolean disableListPromotion, boolean enableIntervalDemotion, boolean enableIntervalPromotion,
                              boolean disableMethodInvocation, boolean validateUnits,
                              CqlTranslatorException.ErrorSeverity errorSeverity, boolean validationOnly, List<String> formats) {

        // add in all of the flags
        List<CqlTranslator.Options> options = new ArrayList<>();
        if(enableDateRangeOptimization) {
            options.add(CqlTranslator.Options.EnableDateRangeOptimization);
        }

        if(enableAnnotations) {
            options.add(CqlTranslator.Options.EnableAnnotations);
        }

        if(enableLocators) {
            options.add(CqlTranslator.Options.EnableLocators);
        }

        if(enableResultTypes) {
            options.add(CqlTranslator.Options.EnableResultTypes);
        }

        if(enableDetailedErrors) {
            options.add(CqlTranslator.Options.EnableDetailedErrors);
        }

        if(disableListTraversal) {
            options.add(CqlTranslator.Options.DisableListTraversal);
        }

        if(disableListDemotion) {
            options.add(CqlTranslator.Options.DisableListDemotion);
        }

        if(disableListPromotion) {
            options.add(CqlTranslator.Options.DisableListPromotion);
        }

        if(enableIntervalDemotion) {
            options.add(CqlTranslator.Options.EnableIntervalDemotion);
        }

        if(enableIntervalPromotion) {
            options.add(CqlTranslator.Options.EnableIntervalPromotion);
        }

        if(disableMethodInvocation) {
            options.add(CqlTranslator.Options.DisableMethodInvocation);
        }

        // parse from string
        ModelManager modelManager = new ModelManager();
        libraryManager = new LibraryManager(modelManager);
        UcumService ucumService = null;
        if (validateUnits) {
            try {
                ucumService = new UcumEssenceService(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));

            } catch (UcumException e) {
                e.printStackTrace();
            }
        }


        if(parentCQLLibraryString != null && parentCQLLibraryFile == null) {
            libraryManager.getLibrarySourceLoader().registerProvider(
                    new StringLibrarySourceProvider(this.cqlLibraryMapping));
            writeToELM(options.toArray(new CqlTranslator.Options[options.size()]), errorSeverity, formats, modelManager, libraryManager, ucumService);
        }

        // parse from file
        else {
            libraryManager.getLibrarySourceLoader().registerProvider(
                    new DefaultLibrarySourceProvider(this.parentCQLLibraryFile.getParentFile().toPath()));
            writeToELM(options.toArray(new CqlTranslator.Options[options.size()]), errorSeverity, formats, modelManager, libraryManager, ucumService);
        }

    }

    /**
     * Converts the cql to elm, if validation only is false, it will create elm strings.
     * @param options the parser options
     */
    private void writeToELM(CqlTranslator.Options[] options, CqlTranslatorException.ErrorSeverity errorSeverity, List<String> formats, ModelManager modelManager,
                            LibraryManager libraryManager, UcumService ucumService) {

        CqlTranslator translator = null;

        // parse from string
        if(parentCQLLibraryString != null && parentCQLLibraryFile == null) {
            translator = CqlTranslator.fromText(this.parentCQLLibraryString, modelManager, libraryManager, ucumService, errorSeverity, options);
        }

        // parse from file
        else {
            try {
                translator = CqlTranslator.fromFile(this.parentCQLLibraryFile, modelManager, libraryManager, ucumService, errorSeverity, options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // the parent library is the one that is returned from the parent translator
        fetchTranslatedLibraries(translator.getTranslatedLibrary());
        this.parentLibrary = translator.getTranslatedLibrary().getLibrary();
        this.translator = translator;

        // set messages, warnings, errors
        this.messages = translator.getMessages();
        this.warnings = translator.getWarnings();
        this.errors = translator.getErrors();

       this.trackBackMap.putAll(getTrackBack());
       
       // TODO going to need to write a custom visitor to get the expressions     
        // output the elm strings
        if(formats.contains("XML")) {
            this.parentElmString = translator.toXml();
            for(TranslatedLibrary library : translatedLibraries.values()) {
                try {
					this.elmStrings.add(translator.convertToXml(library.getLibrary()));
				} catch (JAXBException e) {
					e.printStackTrace();
				}
            }
        }

        // output the json strings
        if(formats.contains("JSON")) {
            this.parentJsonString = translator.toJson();
            for(TranslatedLibrary library : translatedLibraries.values()) {
                try {
					this.jsonStrings.add(translator.convertToJson(library.getLibrary()));
				} catch (JAXBException e) {
					e.printStackTrace();
				}
            }
        }
    }
    
    private void fetchTranslatedLibraries(TranslatedLibrary parentLibrary) throws CqlTranslatorException{
    	this.translatedLibraries.put(parentLibrary.getIdentifier().getId() + "-" + parentLibrary.getIdentifier().getVersion(), parentLibrary);
    	
    	if(parentLibrary.getLibrary().getIncludes() != null) {
    		for(IncludeDef include : parentLibrary.getLibrary().getIncludes().getDef()) {
        		VersionedIdentifier identifier = new VersionedIdentifier();
        		identifier.setId(include.getPath());
        		identifier.setVersion(include.getVersion());
        		try {
        			TranslatedLibrary childLibrary = libraryManager.resolveLibrary(identifier, new ArrayList<>());
        			fetchTranslatedLibraries(childLibrary);
        		} catch (Exception e) {
        			System.out.println(e.getMessage());
        		}
    		}
    	}
    }
    
    private Map<String, TrackBack> getTrackBack() {
		try {
			Reader reader = null; 
			if(this.parentCQLLibraryString != null) {
				reader = new StringReader(this.parentCQLLibraryString);
			} else {
				reader = new FileReader(this.parentCQLLibraryFile);
			}
			
			cqlLexer lexer = new cqlLexer(new ANTLRInputStream(reader));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			cqlParser parser = new cqlParser(tokens);
			cqlParser.LibraryContext tree = parser.library();
			TrackbackListener listener = new TrackbackListener(this.translator.getTranslatedLibrary().getIdentifier());
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(listener, tree);
			return listener.getTrackbackMap();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }

    /**
     * Gets the parent library
     * @return returns the parent library
     */
    public Library getLibrary() {
        return parentLibrary;
    }

    /**
     * Gets the list of library holders from the library holder mapping
     * @return the list of library holders
     */
    public List<LibraryHolder> getLibraryList() {
        return new ArrayList<>(this.libraryHolderMap.values());
    }

    /**
     * Gets the library holder map. The map is in the form of <LibraryName-x.x.xxx, LibraryHolder>
     * @return the library holder map
     */
    public Map<String, LibraryHolder> getLibraryHolderMap() {
        return this.libraryHolderMap;
    }

    /**
     * Gets the messages from cql-to-elm translation
     * @return the messages
     */
    public List<CqlTranslatorException> getMessages() {
        return messages;
    }

    /**
     * Gets the warnings from cql-to-elm translation
     * @return the warnings
     */
    public List<CqlTranslatorException> getWarnings() {
        return warnings;
    }

    /**
     * Gets the errors from cql-to-elm translation
     * @return the errors
     */
    public List<CqlTranslatorException> getErrors() {
        return errors;
    }

    /**
     * Gets the parent elm string frmo cql-to-elm translation
     * @return the parent elm string
     */
    public String getElmString() {
        return this.parentElmString;
    }

    /**
     * Gets the list of elm strings from translation
     * @return the list of elm strings
     */
    public List<String> getElmStrings() {
        return elmStrings;
    }

    /**
     * Gets the parent json elm string from cql-to-elm translation
     * @return the parent json elm string
     */
    public String getParentJsonString() {
        return parentJsonString;
    }

    /**
     * Gets the list of json elm strings from the cql-to-elm translation
     * @return the list of json elm strings
     */
    public List<String> getJsonStrings() {
        return jsonStrings;
    }

    /**
     * Gets the expression definition object associated with a given name
     * @param name the name to find by
     * @return the expression definition
     */
    public Element getExpression(String name) {
        List<ExpressionDef> expressions = this.parentLibrary.getStatements().getDef();
       
        for(ExpressionDef expression : expressions) {
            if(expression.getName().equalsIgnoreCase(name)) {
                return expression;
            }
        }
        
        return null;
    }

    /**
     * Gets the expression associated with a given name's return type in a string
     * @param name the name to find by
     * @return the return type as a string
     */
    public String getExpressionReturnType(String name) {
    	
    	if(this.parentLibrary.getStatements() != null) {
            List<ExpressionDef> expressions = this.parentLibrary.getStatements().getDef();
            for(ExpressionDef expression : expressions) {
                if(expression.getName().equalsIgnoreCase(name)) {
                    return expression.getResultType().toString();
                }
            }
    	}

    	if(this.parentLibrary.getParameters() != null) {
            List<ParameterDef> parameters = this.parentLibrary.getParameters().getDef();
            for(ParameterDef parameter : parameters) {
                return parameter.getResultType().toString();
            }
            
    	}

        return null;
    }

    /**
     * Gets the list of expressions, which includes functions and definitions
     * @return the list of expressions
     */
    public List<ExpressionDef> getExpressions() {
        return this.parentLibrary.getStatements().getDef();
    }

    /**
     * Gets a list of the expression names
     * @return the list of expression names
     */
    public List<String> getExpressionNames() {
        List<String> expressionNames = new ArrayList<>();

        for(ExpressionDef expression : this.parentLibrary.getStatements().getDef()) {
            expressionNames.add(expression.getName());
        }

        return expressionNames;
    }

    public CqlTranslator getTranslator() {
        return this.translator;
    }

    public Map<String, TranslatedLibrary> getTranslatedLibraries() {
        return this.translatedLibraries;
    }

    public String getParentCQLLibraryString() {
        return parentCQLLibraryString;
    }


    public Map<String, String> getCqlLibraryMapping() {
        return cqlLibraryMapping;
    }


    /**
     * Gets the expression to error map. This method will get the errors in a CQL Library and map them to their
     * associated expressions. It will relocate the error lines to be relative to the expression. The map is in the form
     * of <identifier, CQLExpressionError>
     * @return the cql expression error mapping
     */
    public Map<String, List<CQLExpressionError>> getExpressionErrorMap() {

        Map<String, List<CQLExpressionError>> expressionErrorMap = new HashMap<>();

        List<CqlTranslatorException> errors = this.getErrors();

        // if there are no errors, nothing can be done.
        if(errors == null) {
            return null;
        }

        Iterator iterator = this.trackBackMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            TrackBack currentTrackBack = (TrackBack) pair.getValue();
            String currentIdentifier = (String) pair.getKey();

            if(currentTrackBack != null) {
                int expressionStartLine = currentTrackBack.getStartLine();
                int expressionEndLine = currentTrackBack.getEndLine();

                List<CQLExpressionError> expressionErrors = new ArrayList<>();
                for(CqlTranslatorException error : errors) {

                    int errorStartLine = error.getLocator().getStartLine();
                    int errorEndLine = error.getLocator().getEndLine();

                    if((errorStartLine >= expressionStartLine) && (errorEndLine <= expressionEndLine)) {
                        int startLine = errorStartLine - expressionStartLine;
                        int difference = errorEndLine - errorStartLine;
                        int endLine = startLine + difference;
                        int startChar = error.getLocator().getStartChar();
                        int endChar = error.getLocator().getEndChar();
                        CqlTranslatorException.ErrorSeverity severity = error.getSeverity();

                        CQLExpressionError cqlExpressionError = new CQLExpressionError(error.getMessage(), startLine,
                                endLine, startChar, endChar, severity);
                        expressionErrors.add(cqlExpressionError);
                    }

                    expressionErrorMap.put(currentIdentifier, expressionErrors);
                }
            }
        }

        return expressionErrorMap;
    }

    public Map<String, TrackBack> getTrackBackMap() {
        return trackBackMap;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\jmeyer\\git\\test-cql\\test-0.0.000.cql");
        CQLtoELM cqLtoELM = new CQLtoELM(file);
        cqLtoELM.doTranslation(true, "XML");
        CQLFormatter formatter = new CQLFormatter(true);
        formatter.format(file);
        
        System.out.println(cqLtoELM.getTranslator().toXml());
        outputExceptions(cqLtoELM.getErrors());
    }

    /**
     * Prints the output error map to the console
     * @param errors the errors to print
     */
    private static void outputErrorMap(Map<String, List<CQLExpressionError>> errors) {
        Iterator it = errors.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey());
            for(CQLExpressionError expressionError : (List<CQLExpressionError>) pair.getValue()) {
                System.out.println("\t" + expressionError);
            }
        }
    }

    private static void outputExceptions(Iterable<CqlTranslatorException> exceptions) {
        for (CqlTranslatorException error : exceptions) {
            TrackBack tb = error.getLocator();
            String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                    tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
            System.err.printf("%s:%s %s%n", error.getSeverity(), lines, error.getMessage());
        }
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
}
