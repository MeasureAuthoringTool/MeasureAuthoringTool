package mat.server.cqlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cqframework.cql.gen.cqlBaseListener;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.cqframework.cql.gen.cqlParser.CodeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.CodesystemDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ContextDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ExpressionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.FunctionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.IncludeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.LibraryDefinitionContext;
import org.cqframework.cql.gen.cqlParser.OperandDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ParameterDefinitionContext;
import org.cqframework.cql.gen.cqlParser.UsingDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ValuesetDefinitionContext;

import mat.client.shared.CQLWorkSpaceConstants;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.CQLKeywordsUtil;
import mat.server.util.QDMUtil;
import mat.shared.CQLError;

public class ReverseEngineerListener extends cqlBaseListener {
	
	private static final String DEFINE = "define";
	private static final String VALUESET_OID_PREFIX = "urn:oid:";
	private static final String CONTEXT = "context";
	private static final String PARAMETER = "parameter";
	private cqlParser parser;
	private CommonTokenStream tokens;
	private CQLModel cqlModel;
	private String currentContext = "Patient";
	private boolean hasSyntaxErrors;
	private List<CQLError> syntaxErrors;
	private CQLModel previousModel;
	
	private static class SyntaxErrorListener extends BaseErrorListener {
		private List<CQLError> errors = new ArrayList<>();

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			CQLError error = new CQLError();
			error.setErrorMessage(msg);
			error.setErrorInLine(line - 1);
			error.setErrorAtOffeset(charPositionInLine);
			error.setStartErrorInLine(line - 1);
			error.setEndErrorInLine(line - 1);			
			errors.add(error);
		}
	}
	
	public ReverseEngineerListener(String cql, CQLModel previousModel) throws IOException {
		this.previousModel = previousModel;
		syntaxErrors = new ArrayList<>();
		cqlModel = new CQLModel();
		cqlModel.setContext(currentContext);
		InputStream stream = new ByteArrayInputStream(cql.getBytes());
		cqlLexer lexer = new cqlLexer(new ANTLRInputStream(stream));
		tokens = new CommonTokenStream(lexer);
		tokens.fill();
        parser = new cqlParser(tokens);
        parser.addErrorListener(new SyntaxErrorListener());        
        parser.setBuildParseTree(true);      
        
    	ParseTree tree = parser.library();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, tree);
	
        syntaxErrors.addAll(((SyntaxErrorListener) parser.getErrorListeners().get(1)).errors);
		hasSyntaxErrors = !syntaxErrors.isEmpty();	
	}

	@Override
	public void enterLibraryDefinition(LibraryDefinitionContext ctx) {	
		String identifier = "";
		if(ctx.qualifiedIdentifier() != null) {
			identifier = CQLParserUtil.parseString(ctx.qualifiedIdentifier().getText());
		}
		
		String version  = "";
		if(ctx.versionSpecifier() != null) {
			version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		}
		
		String comment = getLibraryComment(ctx);

		cqlModel.setLibraryName(identifier);
		cqlModel.setVersionUsed(version);		
		cqlModel.setLibraryComment(comment);
	}
	
	public String getLibraryComment(LibraryDefinitionContext ctx) {
		int index = tokens.size() - 1; // Initialize to the last token
		List<Token> ts = tokens.getTokens(ctx.stop.getTokenIndex(), tokens.size() - 1);
		for(Token t : ts) {
			if(t.getText().equals("using")) {
				index = t.getTokenIndex();
				break;
			}
		}
		
		ts = tokens.getTokens(ctx.stop.getTokenIndex() + 1, index - 1);
		StringBuilder builder = new StringBuilder();
		for(Token t : ts) {
			builder.append(t.getText());
		}
		
		return trimComment(builder.toString());
	}
	
	@Override
	public void enterUsingDefinition(UsingDefinitionContext ctx) {
		String identifier = "";
		if(ctx.modelIdentifier() != null) {
			identifier = CQLParserUtil.parseString(ctx.modelIdentifier().getText());
		}
		
		String version  = "";
		if(ctx.versionSpecifier() != null) {
			version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		}		
		
		cqlModel.setUsingName(identifier);
		cqlModel.setQdmVersion(version);
	}
	
	@Override
	public void enterIncludeDefinition(IncludeDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.qualifiedIdentifier().getText());
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		String alias = CQLParserUtil.parseString(ctx.localIdentifier().getText());
		
		
		// check and see if there was a model the name, version, and alias as before
		Optional<CQLIncludeLibrary> previousLibrary = previousModel.getCqlIncludeLibrarys().stream().filter(l -> (
				identifier.equals(l.getCqlLibraryName())
				&& alias.equals(l.getAliasName())
				&& version.equals(l.getVersion())
			
		)).findFirst();
		
		if(previousLibrary.isPresent()) {
			cqlModel.getCqlIncludeLibrarys().add(previousLibrary.get());
		} else {
			CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
			includedLibrary.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
			includedLibrary.setCqlLibraryName(identifier);
			includedLibrary.setAliasName(alias);
			includedLibrary.setVersion(version);
			cqlModel.getCqlIncludeLibrarys().add(includedLibrary);
		}
	}
	
	@Override
	public void enterCodesystemDefinition(CodesystemDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String codesystemId = CQLParserUtil.parseString(ctx.codesystemId().getText());
		
		String version = "";
		if(ctx.versionSpecifier() != null) {
			version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		}

		CQLCodeSystem codeSystem = new CQLCodeSystem();
		codeSystem.setCodeSystemName(identifier);
		codeSystem.setCodeSystemVersion(version);
		codeSystem.setCodeSystem(codesystemId.replace(VALUESET_OID_PREFIX, ""));
		codeSystem.setId(UUID.randomUUID().toString());
		
		cqlModel.getCodeSystemList().add(codeSystem);
	}
	
	@Override
	public void enterCodeDefinition(CodeDefinitionContext ctx) {
		CQLCode code = new CQLCode();
		
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String codeId = CQLParserUtil.parseString(ctx.codeId().getText());
		String codeSystemName = CQLParserUtil.parseString(ctx.codesystemIdentifier().getText());
		String displayClause = CQLParserUtil.parseString(ctx.displayClause().STRING().getText());
	
		
		Optional<CQLCode> previousCode = previousModel.getCodeList().stream().filter(c -> (
				c.getDisplayName().equals(identifier)
				&& c.getCodeOID().equals(codeId)
			)).findFirst();
		
		if(previousCode.isPresent()) {
			cqlModel.getCodeList().add(previousCode.get());
		} else {
			Optional<CQLCodeSystem> codeSystem = cqlModel.getCodeSystemList().stream().filter(cs -> cs.getCodeSystemName().equals(codeSystemName)).findFirst();

			code.setId(UUID.randomUUID().toString());
			code.setDisplayName(identifier);
			code.setCodeName(displayClause);
			code.setCodeOID(codeId);
			code.setCodeSystemName(codeSystemName);

			if(codeSystem.isPresent()) {
				code.setCodeSystemOID(codeSystem.get().getCodeSystem());
				code.setCodeSystemVersion(codeSystem.get().getCodeSystemVersion());
			} 
			
			cqlModel.getCodeList().add(code);
		}
	}
	
	@Override
	public void enterValuesetDefinition(ValuesetDefinitionContext ctx) {
		
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String valuesetId = CQLParserUtil.parseString(ctx.valuesetId().getText()).replace(VALUESET_OID_PREFIX, "");
		
		String version = "";
		if(ctx.versionSpecifier() != null) {
			version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		}
		
		// check and see if there was a value set with the same identifier and oid as before
		Optional<CQLQualityDataSetDTO> previousValueset = previousModel.getValueSetList().stream().filter(v -> (
				identifier.equals(v.getName())
				&& valuesetId.equals(v.getOid())			
		)).findFirst();
		
		if(previousValueset.isPresent()) {
			cqlModel.getValueSetList().add(previousValueset.get());
		} else {
			CQLQualityDataSetDTO valueset = new CQLQualityDataSetDTO();
			valueset.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
			valueset.setUuid(UUID.randomUUID().toString());
			valueset.setName(identifier);
			valueset.setVersion(version);
			valueset.setSuffix("");
			
			valueset.setOriginalCodeListName("");
			valueset.setProgram("");
			valueset.setRelease("");
			valueset.setDataType("");
			valueset.setTaxonomy("");
			valueset.setSuppDataElement(false);
			valueset.setValueSetType("");
			valueset.setOid(valuesetId);
			cqlModel.getValueSetList().add(valueset);
		}
	}
	
	@Override
	public void enterContextDefinition(ContextDefinitionContext ctx) {
		currentContext = ctx.identifier().getText();
	}
	
	@Override
	public void enterParameterDefinition(ParameterDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String comment = getExpressionComment(ctx).trim();		
		String logic = getParameterLogic(ctx, ctx.identifier().getText()).trim();
		
		Optional<CQLParameter> existingParameter = previousModel.getCqlParameters().stream().filter(d -> d.getParameterName().equals(identifier)).findFirst();
		CQLParameter parameter = new CQLParameter();
		if(existingParameter.isPresent()) {
			parameter = existingParameter.get();
		} else {
			parameter.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
			parameter.setName(identifier);
		}
		
		parameter.setCommentString(comment);
		parameter.setLogic(logic);
		
		cqlModel.getCqlParameters().add(parameter);	
	}
	
	private String getParameterLogic(ParameterDefinitionContext ctx, String identifier) {	
		List<Token> ts = tokens.getTokens(ctx.start.getTokenIndex(), findExpressionLogicStop(ctx));
		
		StringBuilder builder = new StringBuilder();
		for(Token t : ts) {
			builder.append(t.getText());
		}
		
		return builder.toString().replaceFirst(PARAMETER, "").replace(identifier, "").trim();
	}
	
	@Override
	public void enterExpressionDefinition(ExpressionDefinitionContext ctx) {		
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String logic = getDefinitionAndFunctionLogic(ctx).trim();
	    String comment = getExpressionComment(ctx).trim();		
						
		
		Optional<CQLDefinition> existingDefinition = previousModel.getDefinitionList().stream().filter(d -> d.getDefinitionName().equals(identifier)).findFirst();
		CQLDefinition definition = new CQLDefinition();
		if(existingDefinition.isPresent()) {
			definition = existingDefinition.get();
		} else {
			definition.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
			definition.setName(identifier);
		}
		
		definition.setContext(currentContext);
		definition.setCommentString(comment);
		definition.setLogic(logic);

		cqlModel.getDefinitionList().add(definition);
	}
	
	@Override
	public void enterFunctionDefinition(FunctionDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());	
		String logic = getDefinitionAndFunctionLogic(ctx).trim();
		String comment = getExpressionComment(ctx).trim();
		
		List<CQLFunctionArgument> functionArguments = new ArrayList<>();
		if(ctx.operandDefinition() != null) {
			for(OperandDefinitionContext operand : ctx.operandDefinition()) {
				String name = "";
				String type = "";
				if(operand.identifier() != null) {
					name = operand.identifier().getText();
				}
				
				if(operand.typeSpecifier() != null) {
					type = operand.typeSpecifier().getText();
				}

				CQLFunctionArgument functionArgument = new CQLFunctionArgument();
				functionArgument.setId(UUID.nameUUIDFromBytes(name.getBytes()).toString());
				functionArgument.setArgumentName(name);
				
				if(QDMUtil.getQDMContainer().getDatatypes().contains(CQLParserUtil.parseString(type))) {
					functionArgument.setArgumentType("QDM Datatype");
					functionArgument.setQdmDataType(CQLParserUtil.parseString(type));
				} else if (CQLKeywordsUtil.getCQLKeywords().getCqlDataTypeList().contains(type)) {
					functionArgument.setArgumentType(type);
				} else {
					functionArgument.setArgumentType(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE);
					functionArgument.setOtherType(type);
				}
				
				
				functionArguments.add(functionArgument);
			}
		}
		
		Optional<CQLFunctions> existingFunction = previousModel.getCqlFunctions().stream().filter(d -> d.getFunctionName().equals(identifier)).findFirst();
		CQLFunctions function = new CQLFunctions();
		if(existingFunction.isPresent()) {
			function = existingFunction.get();
		} else {
			function.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
			function.setName(identifier);
		}
		
		function.setCommentString(comment);
		function.setLogic(logic);
		function.setArgumentList(functionArguments);
		function.setContext(currentContext);

		cqlModel.getCqlFunctions().add(function);
	}

	private String getExpressionComment(ParserRuleContext ctx) {
		Token previous = tokens.get(ctx.start.getTokenIndex() - 2);
		if(previous.getType() == cqlLexer.COMMENT) {
		    String comment = previous.getText();
			return trimComment(comment);
		}
		
		return "";
	}

	private String getDefinitionAndFunctionLogic(ParserRuleContext ctx) {
		return getTextBetweenTokenIndexes(ctx.start.getTokenIndex(), findExpressionLogicStop(ctx));
	}
	
	private String getLogicForParameter(int startTokenIndex, int stopTokenIndex, String identifier) {
		List<Token> ts = tokens.getTokens(startTokenIndex, stopTokenIndex);
		
		StringBuilder builder = new StringBuilder();
		for(Token t : ts) {
			builder.append(t.getText());
		}
		
		return builder.toString().replaceFirst(PARAMETER, "").replace(identifier, "").trim();
	}
	
	private String getTextBetweenTokenIndexes(int startTokenIndex, int stopTokenIndex) {
		List<Token> ts = tokens.getTokens(startTokenIndex, stopTokenIndex);
		
		boolean startAdding = false;
		String logic = "";
		for(Token t : ts) {
			if(startAdding) {
				logic += t.getText();
			}
			
			if(t.getText().equals(":")) {
				startAdding = true;
			}		
		}
		
		return logic;
	}
	
	private String trimComment(String comment) {
		return comment.replace("/*", "").replace("*/", "").trim();
	}
	
	public CQLModel getCQLModel() {
		return this.cqlModel;
	}
		
	/**
	 * A definition or function body should be considered done when it reaches the next define statement
	 * or it reaches a comment for the next expression. 
	 * @param ctx the context to find the end of the body of
	 * @return the index of the last token in the body
	 */
	private int findExpressionLogicStop(ParserRuleContext ctx) {
		int index = tokens.size() - 1; // Initialize to the last token
		List<Token> ts = tokens.getTokens(ctx.start.getTokenIndex(), tokens.size() - 1);
		
		// find the next define statement
		boolean startAdding = false;
		for(Token t : ts) {
			if((t.getText().equals(DEFINE) || t.getText().contentEquals(PARAMETER) || t.getText().equals(CONTEXT)) && startAdding) {
				index = t.getTokenIndex();
				break;
			}
			
			// wait until the first define or parameter
			if(t.getText().equals(DEFINE) || t.getText().equals(PARAMETER)) {
				startAdding = true;
			}
		}
		
		
		if(tokens.get(index).getText().equals(CONTEXT)) {
			return index - 1;
		}
		
	    Token twoTokensBeforeToken = tokens.get(index - 2);
	    // check if the expression has a comment associated to it
	    // if it does, return the token before it
    	if(twoTokensBeforeToken.getType() == cqlLexer.COMMENT) {
    		return twoTokensBeforeToken.getTokenIndex() -1 ;
    	} else {
        	return index - 1;
    	}		
	}

	public boolean hasSyntaxErrors() {
		return hasSyntaxErrors;
	}

	public List<CQLError> getSyntaxErrors() {
		return syntaxErrors;
	}

	public void setSyntaxErrors(List<CQLError> syntaxErrors) {
		this.syntaxErrors = syntaxErrors;
	}
}
