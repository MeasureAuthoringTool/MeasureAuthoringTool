package mat.server.cqlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cqframework.cql.gen.cqlBaseListener;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.cqframework.cql.gen.cqlParser.LibraryDefinitionContext;

import mat.shared.CQLError;

public class CQLLinter extends cqlBaseListener {
	
	private CQLLinterConfig config;
	private List<CQLError> errors;

	public CQLLinter(String cql, CQLLinterConfig config) throws IOException {
		this.config = config;
		this.errors = new ArrayList<>();
		
		InputStream stream = new ByteArrayInputStream(cql.getBytes());
		cqlLexer lexer = new cqlLexer(new ANTLRInputStream(stream));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		cqlParser parser = new cqlParser(tokens);
        parser.setBuildParseTree(true);      
        
    	ParseTree tree = parser.library();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, tree);
	} 

	@Override
	public void enterLibraryDefinition(LibraryDefinitionContext ctx) {
		String name = CQLParserUtil.parseString(ctx.identifier().getText());
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		
		if(!name.equals(config.getLibraryName()) || !version.equals(config.getLibraryVersion())) {
			errors.add(createError("The library name and version must match what is one file for this CQL file: "
					+ config.getLibraryName() + " version '" + config.getLibraryVersion() + "'", ctx));
		}
	}
	
	public List<CQLError> getErrors() {
		return this.errors;
	}
	
	private CQLError createError(String message, ParserRuleContext ctx) {
		CQLError error = new CQLError();
		error.setErrorMessage(message);
		error.setStartErrorInLine(ctx.getStart().getLine());
		error.setStartErrorAtOffset(ctx.getStart().getCharPositionInLine());
		error.setErrorInLine(ctx.getStart().getLine());
		error.setErrorAtOffeset(ctx.getStart().getCharPositionInLine());
		error.setEndErrorInLine(ctx.getStop().getLine());
		error.setErrorAtOffeset(ctx.getStop().getCharPositionInLine());
		return error;
	}
}
