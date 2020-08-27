package cqltoelm.parsers;

import mat.models.CQLGraph;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.collections.CollectionUtils;
import org.cqframework.cql.cql2elm.model.TranslatedLibrary;
import org.cqframework.cql.cql2elm.preprocessor.CqlPreprocessorVisitor;
import org.cqframework.cql.gen.cqlBaseListener;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.cqframework.cql.gen.cqlParser.AliasedQuerySourceContext;
import org.cqframework.cql.gen.cqlParser.LetClauseContext;
import org.cqframework.cql.gen.cqlParser.QualifiedFunctionContext;
import org.cqframework.cql.gen.cqlParser.QualifiedIdentifierExpressionContext;
import org.cqframework.cql.gen.cqlParser.ReferentialIdentifierContext;
import org.cqframework.cql.gen.cqlParser.ReturnClauseContext;
import org.cqframework.cql.gen.cqlParser.SortClauseContext;
import org.cqframework.cql.gen.cqlParser.WhereClauseContext;
import org.cqframework.cql.gen.cqlParser.WithClauseContext;
import org.cqframework.cql.gen.cqlParser.WithoutClauseContext;
import org.hl7.elm.r1.CodeDef;
import org.hl7.elm.r1.CodeSystemDef;
import org.hl7.elm.r1.Element;
import org.hl7.elm.r1.ExpressionDef;
import org.hl7.elm.r1.IncludeDef;
import org.hl7.elm.r1.ParameterDef;
import org.hl7.elm.r1.ValueSetDef;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class MATCQL2ELMListener extends cqlBaseListener {

    /**
     * The child CQL strings
     */
    private Map<String, String> childrenLibraries = new HashMap<>();

    /**
     * The identifier of the current library, relative to the library that brought us here. Will be in the form of
     * libraryName|alias
     */
    private String libraryIdentifier;

    /**
     * The include def object which we are current parsing, relative to the library that brought us here.
     */
    IncludeDef libraryAccessor = null;

    /**
     * The current library object from the parser
     */
    private TranslatedLibrary library;

    /**
     * The map of the other libraries in the current library
     */
    Map<String, TranslatedLibrary> translatedLibraryMap;

    /**
     * The current context, aka which expression are we currently in.
     */
    private String currentContext;

    private Set<String> libraries = new HashSet<>();
    private Set<String> valuesets = new HashSet<>();
    private Set<String> codes = new HashSet<>();
    private Set<String> codesystems = new HashSet<>();
    private Set<String> parameters = new HashSet<>();
    private Set<String> definitions = new HashSet<>();
    private Set<String> functions = new HashSet<>();

    private Map<String, Map<String, Set<String>>> valueSetDataTypeMap = new HashMap<>();
    private Map<String, Map<String, Set<String>>> codeDataTypeMap = new HashMap<>();

    private Stack<String> namespace = new Stack<>();

    private CQLGraph graph;

    public MATCQL2ELMListener(CQLGraph graph, TranslatedLibrary library, Map<String, TranslatedLibrary> translatedLibraryMap,
                       Map<String, String> childrenLibraries) {
        this.graph = graph;
        this.library = library;
        this.translatedLibraryMap = translatedLibraryMap;
        this.libraryIdentifier = "";
        this.childrenLibraries = childrenLibraries;
    }

    public MATCQL2ELMListener(String libraryIdentifier, CQLGraph graph,
                       TranslatedLibrary library,
                       Map<String, TranslatedLibrary> translatedLibraryMap,
                       Map<String, String> childrenLibraries) {
        this.graph = graph;
        this.library = library;
        this.translatedLibraryMap = translatedLibraryMap;
        this.libraryIdentifier = libraryIdentifier;
        this.childrenLibraries = childrenLibraries;
    }

    private TranslatedLibrary getCurrentLibraryContext() {
        if(libraryAccessor != null) {
            return this.translatedLibraryMap.get(libraryAccessor.getPath() + "-" + libraryAccessor.getVersion());
        }

        return this.library;
    }
        
    @Override
    public void enterQualifiedFunction(QualifiedFunctionContext ctx) {    
    	if(ctx.identifierOrFunctionIdentifier() != null
    			&& ctx.identifierOrFunctionIdentifier().identifier() != null) {
    		String identifer = parseString(ctx.identifierOrFunctionIdentifier().identifier().getText());

    		if(shouldResolve(identifer)) {
                resolve(identifer, getCurrentLibraryContext());
        	}
    	}
  
    }
        
    @Override
    public void enterQualifiedIdentifierExpression(QualifiedIdentifierExpressionContext ctx) {
    	String identifier = parseString(ctx.referentialIdentifier().getText());
    	String qualifier = "";    
             
         if(shouldResolve(identifier)) {
             // a qualified identifier can take on the form (qualifier) '.')* identifier. If there is only one qualifier,
             // then that could be a library. Resolve the qualifier to check if it's a library.
             if(CollectionUtils.isNotEmpty(ctx.qualifierExpression()) && ctx.qualifierExpression().get(0) != null) {
                 qualifier = parseString(ctx.qualifierExpression().get(0).getText());
             	if(shouldResolve(qualifier)) {
                     resolve(qualifier, getCurrentLibraryContext());
             	}
             }
             
             resolve(identifier, getCurrentLibraryContext());
         }
    }
    
    @Override
    public void enterReferentialIdentifier(ReferentialIdentifierContext ctx) {
        String identifier = parseString(ctx.getText()); 
        if(shouldResolve(identifier)) {
            resolve(identifier, getCurrentLibraryContext());
        }
    }
    
    @Override
    public void enterQualifiedIdentifier(@NotNull cqlParser.QualifiedIdentifierContext ctx) {
        String identifier = parseString(ctx.identifier().getText());
        String qualifier = "";    
        
        if(shouldResolve(identifier)) {
            // a qualified identifier can take on the form (qualifier) '.')* identifier. If there is only one qualifier,
            // then that could be a library. Resolve the qualifier to check if it's a library.
            if(ctx.qualifier(0) != null) {
                qualifier = parseString(ctx.qualifier(0).getText());
            	if(shouldResolve(qualifier)) {
                    resolve(qualifier, getCurrentLibraryContext());
            	}
            }
            
            resolve(identifier, getCurrentLibraryContext());
        }
    }

    private boolean shouldResolve(String identifier) {
        // if the namespace contains the identifier, that means its a local identifier and should not be treated
        // as an identifier for an expression.

        // an identifier can also not be equal to patient.
    	return !namespace.contains(identifier) && !identifier.equalsIgnoreCase("patient");
    }

    @Override
    public void enterFunction(@NotNull cqlParser.FunctionContext ctx) {
        String identifier = parseString(ctx.referentialIdentifier().getText());
        resolve(identifier, getCurrentLibraryContext());
        libraryAccessor = null;
    }

    @Override
    public void enterExpressionDefinition(@NotNull cqlParser.ExpressionDefinitionContext ctx) {
        String identifier = parseString(ctx.identifier().getText());
        this.currentContext = libraryIdentifier + identifier;
        graph.addNode(currentContext);
    }

    @Override
    public void enterFunctionDefinition(@NotNull cqlParser.FunctionDefinitionContext ctx) {
        String identifier = parseString(ctx.identifierOrFunctionIdentifier().getText());
        this.currentContext = libraryIdentifier + identifier;
        for(cqlParser.OperandDefinitionContext operand : ctx.operandDefinition()) {
            namespace.push(operand.referentialIdentifier().getText());
        }

        graph.addNode(currentContext);
    }

    @Override
    public void exitFunctionDefinition(@NotNull cqlParser.FunctionDefinitionContext ctx) {
    	for(cqlParser.OperandDefinitionContext operand : ctx.operandDefinition()) {
            namespace.pop();
        }
    }

    @Override
    public void enterParameterDefinition(@NotNull cqlParser.ParameterDefinitionContext ctx) {
        String identifier = parseString(ctx.identifier().getText());
        this.currentContext = libraryIdentifier + identifier;
        graph.addNode(currentContext);
    }

    @Override
    public void enterRetrieve(@NotNull cqlParser.RetrieveContext ctx) {

        // we only care about entering a retrieve if it has a terminology
        if (ctx.terminology() == null || ctx.codePath() != null) {
            return;
        }
        
        // if the valueset is in the form alias.name, get the alias and resolve it so we can switch to the other
        // libraries context
        String identifier = "";
        if(CollectionUtils.isNotEmpty(ctx.terminology().qualifiedIdentifierExpression().qualifierExpression()) && ctx.terminology().qualifiedIdentifierExpression().qualifierExpression().get(0) != null) {
        	resolve(ctx.terminology().qualifiedIdentifierExpression().qualifierExpression().get(0).getText(), getCurrentLibraryContext());
            identifier = parseString(ctx.terminology().qualifiedIdentifierExpression().referentialIdentifier().getText());
        } else {
            identifier = parseString(ctx.terminology().getText());
        }


        String formattedIdentifier = formatIdentifier(identifier);

        // we need to resolve based on the identifier since it will be looking in the proper library but we need
        // to put the formatted identifier into the maps since this is the format MAT is looking for
        String dataType = parseString(ctx.namedTypeSpecifier().referentialOrTypeNameIdentifier().getText());
        Element element = resolve(identifier, getCurrentLibraryContext());
        if(element instanceof ValueSetDef) {
            Map<String, Set<String>> current = valueSetDataTypeMap.get(currentContext);
            if(current == null) {
                valueSetDataTypeMap.put(currentContext, new HashMap<>());
            }

            current = valueSetDataTypeMap.get(currentContext);
            Set<String> currentSet = current.get(formattedIdentifier);
            if(currentSet == null) {
                currentSet = new HashSet<>();
                current.put(formattedIdentifier, currentSet);
            }

            current.get(formattedIdentifier).add(dataType);
        }

        else if(element instanceof CodeDef) {
            Map<String, Set<String>> current = codeDataTypeMap.get(currentContext);
            if(current == null) {
                codeDataTypeMap.put(currentContext, new HashMap<>());
            }

            current = codeDataTypeMap.get(currentContext);
            Set<String> currentSet = current.get(formattedIdentifier);
            if(currentSet == null) {
                currentSet = new HashSet<>();
                current.put(formattedIdentifier, currentSet);
            }

            current.get(formattedIdentifier).add(dataType);
        }
    }
        
    private void pushAliasesOntoStackForQueries(cqlParser.QueryContext ctx) {
		for(AliasedQuerySourceContext source : ctx.sourceClause().aliasedQuerySource()) {
			namespace.push(source.alias().getText());
		}
    }

    private void popAliasesOffStackForQueries(cqlParser.QueryContext ctx) {
		for(AliasedQuerySourceContext source : ctx.sourceClause().aliasedQuerySource()) {
			namespace.pop();
		}
    }

    @Override
    public void enterWhereClause(WhereClauseContext ctx) {
    	if(ctx.parent instanceof cqlParser.QueryContext) {
    		pushAliasesOntoStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}
    }

    @Override
    public void exitWhereClause(WhereClauseContext ctx) {
    	if(ctx.parent instanceof cqlParser.QueryContext) {
    		popAliasesOffStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}
    }

    @Override
	public void enterWithClause(WithClauseContext ctx) {
    	if(ctx.parent instanceof cqlParser.QueryContext) {
    		pushAliasesOntoStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}

		namespace.push(ctx.aliasedQuerySource().alias().getText());
	}

	@Override
	public void exitWithClause(WithClauseContext ctx) {
    	if(ctx.parent instanceof cqlParser.QueryContext) {
    		popAliasesOffStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}

		namespace.pop();
	}

	@Override
	public void enterWithoutClause(WithoutClauseContext ctx) {
		namespace.push(ctx.aliasedQuerySource().alias().getText());
	}

	@Override
	public void exitWithoutClause(WithoutClauseContext ctx) {
		namespace.pop();
	}

    @Override
    public void enterLetClause(LetClauseContext ctx) {
    	if(ctx.parent instanceof cqlParser.QueryContext) {
    		pushAliasesOntoStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}
    }

    @Override
    public void exitLetClause(LetClauseContext ctx) {
    	if(ctx.parent instanceof cqlParser.QueryContext) {
    		popAliasesOffStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}
    }

    @Override
    public void enterReturnClause(ReturnClauseContext ctx) {
    	if(ctx.parent instanceof cqlParser.QueryContext) {
    		pushAliasesOntoStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}
    }

    @Override
    public void exitReturnClause(ReturnClauseContext ctx) {
       	if(ctx.parent instanceof cqlParser.QueryContext) {
    		popAliasesOffStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}
    }

	@Override
	public void enterSortClause(SortClauseContext ctx) {
	   	if(ctx.parent instanceof cqlParser.QueryContext) {
    		pushAliasesOntoStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}
	}

	@Override
	public void exitSortClause(SortClauseContext ctx) {
	   	if(ctx.parent instanceof cqlParser.QueryContext) {
    		popAliasesOffStackForQueries(((cqlParser.QueryContext) ctx.parent));
    	}
	}
    
    public String parseString(String s) {
        return s.replace("\"", "");
    }

    /**
     * Formats the identifier based on where it is relative to the parent library
     * @param identifier the identifier to format
     * @return
     */
    private String formatIdentifier(String identifier) {
        String formattedIdentifier = "";

        // if we are looking at an expression from some child library (aka an expression alias.name) build the
        // formatted expression based on the library it came from.
        if(libraryAccessor != null) {
            String path = libraryAccessor.getPath() + "-" + libraryAccessor.getVersion();
            String alias = libraryAccessor.getLocalIdentifier();
            formattedIdentifier = path + "|" + alias + "|" + identifier;
        } else {
            // if the expression is in a child library or grandchild library (relative to the parent) then format and
            // then format that identifier with the details of the current library
            if(this.libraryIdentifier != null) {
                formattedIdentifier = libraryIdentifier + identifier;
            } else {
                // if the expression is in the parent library, then make the formatted identifier the current identifier.
                formattedIdentifier = identifier;
            }
        }

        return formattedIdentifier;
    }

    private Element resolve(String identifier, TranslatedLibrary library) {
        Element element = library.resolve(identifier);
        String formattedIdentifier = formatIdentifier(identifier);
        libraryAccessor = null; // we've done all we need to do with the accessor, so set it equal to null so it can be
                                // updated again if need be.
        if(element instanceof IncludeDef) {
            IncludeDef def = (IncludeDef) element;
            libraries.add(def.getPath() + "-" + def.getVersion() + "|" + def.getLocalIdentifier());
            graph.addEdge(currentContext, def.getPath() + "-" + def.getVersion() + "|" + def.getLocalIdentifier());
            libraryAccessor = def;
            try {
                parseChildLibraries(def);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(element instanceof CodeDef) {
            codes.add(formattedIdentifier);
            graph.addEdge(currentContext, formattedIdentifier);
        }

        else if(element instanceof CodeSystemDef) {
            codesystems.add(identifier);
            graph.addEdge(currentContext, formattedIdentifier);
        }

        else if(element instanceof ValueSetDef) {
            valuesets.add(formattedIdentifier);
            graph.addEdge(currentContext, formattedIdentifier);
        }

        else if(element instanceof ParameterDef) {
            parameters.add(formattedIdentifier);
            graph.addEdge(currentContext, formattedIdentifier);
        }

        else if(element instanceof ExpressionDef) {
            definitions.add(formattedIdentifier);
            graph.addEdge(currentContext, formattedIdentifier);
        }

        else {
            final String finalFormattedIdentifier = formattedIdentifier;
            library.getLibrary().getStatements().getDef().forEach((def) -> {
                if(def.getName().equals(identifier)) {
                    functions.add(finalFormattedIdentifier);
                    graph.addEdge(currentContext, finalFormattedIdentifier);
                }
            });
        }

        return element;
    }

    private void parseChildLibraries(IncludeDef def) throws IOException {
        String childCQLString = this.childrenLibraries.get(def.getPath() + "-" + def.getVersion());

        InputStream stream = new ByteArrayInputStream(childCQLString.getBytes(StandardCharsets.UTF_8.name()));
        cqlLexer lexer = new cqlLexer(new ANTLRInputStream(stream));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cqlParser parser = new cqlParser(tokens);

        TranslatedLibrary childLibrary = this.translatedLibraryMap.get(def.getPath() + "-" + def.getVersion());
        mat.parsers.MATCQL2ELMListener listener = new mat.parsers.MATCQL2ELMListener(def.getPath() + "-" + def.getVersion() + "|" + def.getLocalIdentifier() + "|", graph, childLibrary, translatedLibraryMap, childrenLibraries);
        ParseTree tree = parser.library();
        CqlPreprocessorVisitor preprocessor = new CqlPreprocessorVisitor();
        preprocessor.visit(tree);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        libraries.addAll(listener.getLibraries());
        valuesets.addAll(listener.getValuesets());
        codes.addAll(listener.getCodes());
        codesystems.addAll(listener.getCodesystems());
        parameters.addAll(listener.getParameters());
        definitions.addAll(listener.getDefinitions());
        functions.addAll(listener.getFunctions());
        valueSetDataTypeMap.putAll(listener.getValueSetDataTypeMap());
        codeDataTypeMap.putAll(listener.getCodeDataTypeMap());
    }

    public Set<String> getLibraries() {
        return libraries;
    }

    public Set<String> getValuesets() {
        return valuesets;
    }

    public Set<String> getCodes() {
        return codes;
    }

    public Set<String> getCodesystems() {
        return codesystems;
    }

    public Set<String> getParameters() {
        return parameters;
    }

    public Set<String> getDefinitions() {
        return definitions;
    }

    public Set<String> getFunctions() {
        return functions;
    }

    public Map<String, Map<String, Set<String>>> getValueSetDataTypeMap() {
        return valueSetDataTypeMap;
    }

    public Map<String, Map<String, Set<String>>> getCodeDataTypeMap() {
        return codeDataTypeMap;
    }

    public CQLGraph getGraph() {
        return graph;
    }
}
