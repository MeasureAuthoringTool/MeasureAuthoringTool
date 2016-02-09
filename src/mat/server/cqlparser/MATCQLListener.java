package mat.server.cqlparser;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import mat.model.cql.CQLDefinitionModelObject;
import mat.model.cql.CQLLibraryModelObject;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameterModelObject;
import mat.model.cql.CQLValueSetModelObject;


public class MATCQLListener extends cqlBaseListener {
	
	CQLModel cqlModel = new CQLModel();

	MATCQLListener parser;
	
	cqlLexer lexer;
	
	CommonTokenStream tokens;

	public cqlLexer getLexer() {
		return lexer;
	}

	public void setLexer(cqlLexer lexer) {
		this.lexer = lexer;
	}

	public MATCQLListener getParser() {
		return parser;
	}

	public void setParser(MATCQLListener parser) {
		this.parser = parser;
	}

	
	public CQLModel getCqlModel() {
		return cqlModel;
	}

	public void setCqlModel(CQLModel cqlModel) {
		this.cqlModel = cqlModel;
	}
	
	public CommonTokenStream getTokens() {
		return tokens;
	}

	public void setTokens(CommonTokenStream tokens) {
		this.tokens = tokens;
	}


	public void exitLibraryDefinition(cqlParser.LibraryDefinitionContext ctx) {
		
		CQLLibraryModelObject library = new CQLLibraryModelObject();
		library.setIdentifier(ctx.identifier().getText());
		System.out.println("Found library definition...");
		System.out.println(ctx.identifier().getText());
		if (ctx.versionSpecifier() != null) {
			library.setVersion(ctx.versionSpecifier().getText());
			System.out.println(ctx.versionSpecifier().getText());
		}
	//	cqlModel.setLibrary(library);
		System.out.println("\r\n");
		
	}
	
	public void exitParameterDefinition(cqlParser.ParameterDefinitionContext ctx) {
		CQLParameterModelObject paramModel = new CQLParameterModelObject();
		paramModel.setIdentifier(ctx.identifier().getText());
		if (ctx.typeSpecifier() != null) {
			paramModel.setTypeSpecifier(ctx.typeSpecifier().getText());
		}
	//	List<CQLParameterModelObject> parameters = cqlModel.getCqlParameters();
		//parameters.add(paramModel);
		//cqlModel.setCqlParameters(parameters);
		System.out.println("Found parameter definition...");
		System.out.println(ctx.identifier().getText());
		if (ctx.typeSpecifier() != null) {
			System.out.println(ctx.typeSpecifier().getText());
		}
		System.out.println("\r\n");
	}
	
	public void exitValuesetDefinition(cqlParser.ValuesetDefinitionContext ctx) { 
		System.out.println("Found ValueSet definition...");
		System.out.println(ctx.identifier().getText());
		System.out.println(ctx.valuesetId().getText());
		System.out.println("\r\n");
		CQLValueSetModelObject valueSet = new CQLValueSetModelObject();
		valueSet.setIdentifier(ctx.identifier().getText());
		valueSet.setValueSetId(ctx.valuesetId().getText());
		//List<CQLValueSetModelObject> valueSets = cqlModel.getValueSetList(); 
		//valueSets.add(valueSet);
		//cqlModel.setValueSetList(valueSets);

	}
	
/*	public void exitDefineClause(cqlParser.DefineClauseContext ctx) {
		System.out.println("Found Function definition...");
		List<DefineClauseItemContext> defines = ctx.defineClauseItem();
		for (DefineClauseItemContext define: defines) {
			
			 System.out.println("Identifier: " + define.identifier());
			 System.out.println("Rule Context: " + define.getRuleContext().getText());
		}
	}
	*/
	
	public void exitStatement(cqlParser.StatementContext ctx) { 
		if(ctx.functionDefinition() != null){
			System.out.println("Found Function definition...");
			System.out.println(ctx.functionDefinition().identifier().getText());
			// The whitespace and newline characters are being sent to a HIDDEN buffer
			// so use the CommonTokenStream tokens to get the characters from all buffers together
			// this will be what was actually read in from the CQL file
			List<Token> allTokens = tokens.get(ctx.getStart().getTokenIndex(), ctx.getStop().getTokenIndex());
			StringBuffer buffer = new StringBuffer();
			if (allTokens != null) {
				for (Token token:  allTokens) {
					buffer.append(token.getText());
				}
			}
			// delete the define xxx:/n from the string
			String expressionDefinition = buffer.toString().substring(buffer.toString().indexOf(':') + 2);
			System.out.println("Expression Definition:  " + expressionDefinition);

//			CQLFunctionModelObject function = new CQLFunctionModelObject();
//			function.setIdentifier(ctx.expressionDefinition().identifier().getText());
//			function.s(expressionDefinition);
//			List<CQLDefinitionModelObject> definitions = cqlModel.getDefinitionList();
//			definitions.add(function);
//			cqlModel.setDefinitionList(definitions);

			
			
			
			System.out.println(ctx.functionDefinition().functionBody().getText());
		}else if(ctx.expressionDefinition() != null){
			System.out.println("Found definition...");
			System.out.println("Identifier:  " + ctx.expressionDefinition().identifier().getText());
			// The whitespace and newline characters are being sent to a HIDDEN buffer
			// so use the CommonTokenStream tokens to get the characters from all buffers together
			// this will be what was actually read in from the CQL file
			List<Token> allTokens = tokens.get(ctx.getStart().getTokenIndex(), ctx.getStop().getTokenIndex());
			StringBuffer buffer = new StringBuffer();
			if (allTokens != null) {
				for (Token token:  allTokens) {
					buffer.append(token.getText());
				}
			}
			// delete the define xxx:/n from the string
			String expressionDefinition = buffer.toString().substring(buffer.toString().indexOf(':') + 2);
			System.out.println("Expression Definition:  " + expressionDefinition);

			CQLDefinitionModelObject definition = new CQLDefinitionModelObject();
			definition.setIdentifier(ctx.expressionDefinition().identifier().getText());
			definition.setExpression(expressionDefinition);
			//List<CQLDefinitionModelObject> definitions = cqlModel.getDefinitionList();
			//definitions.add(definition);
			//cqlModel.setDefinitionList(definitions);
		}else if(ctx.contextDefinition() != null){
			System.out.println("Found context ...");
			System.out.println(ctx.contextDefinition().identifier().getText());
			System.out.println(ctx.contextDefinition().getRuleContext().getText());
		}
		System.out.println("\r\n");
	}
	
}