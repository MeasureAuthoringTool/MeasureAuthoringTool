package mat.server.cqlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
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

import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;

public class ReverseEngineerListener extends cqlBaseListener {
	
	private cqlParser parser;
	private CommonTokenStream tokens;
	private CQLModel cqlModel;
	private String currentContext = "Patient";
	
	public ReverseEngineerListener(String cql) throws IOException {
		cqlModel = new CQLModel();
		InputStream stream = new ByteArrayInputStream(cql.getBytes());
		cqlLexer lexer = new cqlLexer(new ANTLRInputStream(stream));
		tokens = new CommonTokenStream(lexer);
		tokens.fill();
        parser = new cqlParser(tokens);
		ParseTree tree = parser.library();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, tree);
	}

	@Override
	public void enterLibraryDefinition(LibraryDefinitionContext ctx) {
		String identifier = parseString(ctx.identifier().getText());
		String version = parseString(ctx.versionSpecifier().getText());
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
		String comment = "";
		for(Token t : ts) {
			System.out.println(t.getText());
			comment += t.getText();
		}
		
		return trimComment(comment);
	}
	
	@Override
	public void enterUsingDefinition(UsingDefinitionContext ctx) {
		String identifier = parseString(ctx.modelIdentifier().identifier().getText());
		String version = parseString(ctx.versionSpecifier().getText());
		
		cqlModel.setUsingName(identifier);
		cqlModel.setQdmVersion(version);
	}
	
	public void enterIncludeDefinition(IncludeDefinitionContext ctx) {
		String identifier = parseString(ctx.identifier().getText());
		String version = parseString(ctx.versionSpecifier().getText());
		String alias = parseString(ctx.localIdentifier().getText());
		
		CQLIncludeLibrary includedLibrary = new CQLIncludeLibrary();
		includedLibrary.setCqlLibraryName(identifier);
		includedLibrary.setAliasName(alias);
		includedLibrary.setVersion(version);
		
		cqlModel.getCqlIncludeLibrarys().add(includedLibrary);
	}
	
	public void enterCodesystemDefinition(CodesystemDefinitionContext ctx) {
		String identifier = parseString(ctx.identifier().getText());
		String codesystemId = parseString(ctx.codesystemId().getText());
		
		String version = "";
		if(ctx.versionSpecifier() != null) {
			version = parseString(ctx.versionSpecifier().getText());
		}

		CQLCodeSystem codeSystem = new CQLCodeSystem();
		codeSystem.setCodeSystemName(identifier);
		codeSystem.setCodeSystemVersion(version);
		codeSystem.setCodeSystem(codesystemId.replace("urn:oid:", ""));
		codeSystem.setId(UUID.randomUUID().toString());
		cqlModel.getCodeSystemList().add(codeSystem);
	}
	
	public void enterCodeDefinition(CodeDefinitionContext ctx) {
		CQLCode code = new CQLCode();
		
		String identifier = parseString(ctx.identifier().getText());
		String codeId = parseString(ctx.codeId().getText());
		String codeSystemName = parseString(ctx.codesystemIdentifier().getText());
		String displayClause = parseString(ctx.displayClause().getText());
		CQLCodeSystem codeSystem = cqlModel.getCodeSystemList().stream().filter(cs -> cs.getCodeSystemName().equals(codeSystemName)).findFirst().get();
	
		code.setId(UUID.randomUUID().toString());
		code.setDisplayName(identifier);
		code.setCodeName(displayClause);
		code.setCodeOID(codeId);
		code.setCodeSystemName(codeSystemName);
		code.setCodeSystemOID(codeSystem.getCodeSystem());
		code.setCodeSystemVersion(codeSystem.getCodeSystemVersion());
	}
	
	@Override
	public void enterValuesetDefinition(ValuesetDefinitionContext ctx) {
		CQLQualityDataSetDTO valueset = new CQLQualityDataSetDTO();
		
		String identifier = parseString(ctx.identifier().getText());
		String valuesetId = parseString(ctx.valuesetId().getText());
		
		String version = "";
		if(ctx.versionSpecifier() != null) {
			version = parseString(ctx.versionSpecifier().getText());
		}

		valueset.setName(identifier);
		valueset.setSuffix(identifier);
		valueset.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
		valueset.setOid(valuesetId.replace("urn:oid:", ""));
		valueset.setVersion(version);
		valueset.setUuid(UUID.randomUUID().toString());
		
		cqlModel.getValueSetList().add(valueset);
	}
	
	@Override
	public void enterContextDefinition(ContextDefinitionContext ctx) {
		currentContext = ctx.identifier().getText();
	}
	
	public void enterParameterDefinition(ParameterDefinitionContext ctx) {
		String identifier = parseString(ctx.identifier().getText());
		String comment = getExpressionComment(ctx);		
		String logic = getParameterLogic(ctx, ctx.identifier().getText());
		
		CQLParameter parameter = new CQLParameter();
		parameter.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
		parameter.setName(identifier);
		parameter.setCommentString(comment);
		parameter.setLogic(logic);
		
		cqlModel.getCqlParameters().add(parameter);		
	}
	
	private String getParameterLogic(ParameterDefinitionContext ctx, String identifier) {
		int index = tokens.size() - 1; // Initialize to the last token
		List<Token> ts = tokens.getTokens(ctx.start.getTokenIndex(), tokens.size() - 1);
		
		// find the next parameter or context statement
		boolean startAdding = false;
		for(Token t : ts) {
			if((t.getText().equals("context") || t.getText().equals("parameter")) && startAdding) {
				index = t.getTokenIndex();
				break;
			}
			
			// wait until the first parameter 
			if(t.getText().equals("parameter")) {
				startAdding = true;
			}
		}
		
		return getLogicForParameter(ctx.start.getTokenIndex(), index - 1, identifier);
	}
	
	public void enterExpressionDefinition(ExpressionDefinitionContext ctx) {		
		String identifier = parseString(ctx.identifier().getText());
		String logic = getDefinitionAndFunctionLogic(ctx);
	    String comment = getExpressionComment(ctx);		
						
		CQLDefinition definition = new CQLDefinition();
		
		definition.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
		definition.setContext(currentContext);
		definition.setName(identifier);
		definition.setCommentString(comment.trim());
		definition.setLogic(logic.trim());
			
		cqlModel.getDefinitionList().add(definition);
	}
	
	public void enterFunctionDefinition(FunctionDefinitionContext ctx) {
		String identifier = parseString(ctx.identifier().getText());	
		String logic = getDefinitionAndFunctionLogic(ctx);
		String comment = getExpressionComment(ctx);
		
		List<CQLFunctionArgument> functionArguments = new ArrayList<>();
		if(ctx.operandDefinition() != null) {
			for(OperandDefinitionContext operand : ctx.operandDefinition()) {
				CQLFunctionArgument functionArgument = new CQLFunctionArgument();
				functionArgument.setArgumentName(operand.identifier().getText());
				functionArgument.setArgumentType(operand.typeSpecifier().getText());
				functionArguments.add(functionArgument);
			}
		}

		CQLFunctions function = new CQLFunctions();
		
		function.setId(UUID.nameUUIDFromBytes(identifier.getBytes()).toString());
		function.setName(identifier);
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
		return getTextBetweenTokenIndexes(ctx.start.getTokenIndex(), findDefinitionAndFunctionBodyStop(ctx));
	}
	
	private String getLogicForParameter(int startTokenIndex, int stopTokenIndex, String identifier) {
		List<Token> ts = tokens.getTokens(startTokenIndex, stopTokenIndex);
		
		String logic = "";
		for(Token t : ts) {
			logic += t.getText();
		}
		
		return logic.replace("parameter", "").replace(identifier, "").trim();
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
	
	private String parseString(String identifier) {
		if(Character.toString(identifier.charAt(0)).equals("\"") || Character.toString(identifier.charAt(0)).equals("'")) {
			identifier = identifier.substring(1, identifier.length() - 1);
		}
		
		if(Character.toString(identifier.charAt(identifier.length() - 1)).equals("\"") || Character.toString(identifier.charAt(0)).equals("'")) {
			identifier = identifier.substring(0, identifier.length() - 2);
		}
		
		return identifier;
	}
	
	/**
	 * A definition or function body should be considered done when it reaches the next define statement
	 * or it reaches a comment for the next expression. 
	 * @param ctx the conext to find the end of the body of
	 * @return the index of the last token in the body
	 */
	private int findDefinitionAndFunctionBodyStop(ParserRuleContext ctx) {
		int index = tokens.size() - 1; // Initialize to the last token
		List<Token> ts = tokens.getTokens(ctx.start.getTokenIndex(), tokens.size() - 1);
		
		// find the next define statement
		boolean startAdding = false;
		for(Token t : ts) {
			if(t.getText().equals("define") && startAdding) {
				index = t.getTokenIndex();
				break;
			}
			
			// wait until the first define 
			if(t.getText().equals("define")) {
				startAdding = true;
			}
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
	
}
