package mat.server.cqlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cqframework.cql.gen.cqlBaseListener;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.cqframework.cql.gen.cqlParser.IncludeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.LibraryDefinitionContext;

import mat.model.cql.CQLIncludeLibrary;
import mat.shared.CQLError;

public class CQLLinter extends cqlBaseListener {
	
	private CQLLinterConfig config;
	private List<String> errorMessages;
	private List<String> missingIncludedLibraries;
	private List<CQLError> errors;

	public CQLLinter(String cql, CQLLinterConfig config) throws IOException {
		this.config = config;
		this.errors = new ArrayList<>();
		this.errorMessages = new ArrayList<>();
		this.missingIncludedLibraries = new ArrayList<>();
		
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
			
			String message = String.format("%s version '%s' does not match what is on file in the MAT.", 
					config.getLibraryName(), config.getLibraryVersion());
			
			errorMessages.add(message);
			errors.add(createError(message, ctx));
		}
	}
	
	@Override
	public void enterIncludeDefinition(IncludeDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String alias = CQLParserUtil.parseString(ctx.localIdentifier().getText());
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());

		// find all libraries that have a matching identifier, alias, and version
		if(config.getPreviousCQLModel().getCqlIncludeLibrarys() != null) {
			List<CQLIncludeLibrary> potentialMatches = config.getPreviousCQLModel().getCqlIncludeLibrarys().stream().filter(l -> (
					identifier.equals(l.getCqlLibraryName())
					&& alias.equals(l.getAliasName())
					&& version.equals(l.getVersion())
				)).collect(Collectors.toList());
				
				// if there were no matches in the previous model, or all matches do not have an id, then return an error. 
				// not having an id means that it has never been fetched through the includes section.
				// if there were no matches in the previous model, that also means it has never been fetched
				if(potentialMatches.isEmpty() || potentialMatches.stream().filter(l -> l.getCqlLibraryId() == null).count() > 0) {
					createIncludedLibraryError(ctx, identifier, version, alias);
				}
		} else {
			createIncludedLibraryError(ctx, identifier, version, alias);
		}
	}

	private void createIncludedLibraryError(IncludeDefinitionContext ctx, String identifier, String version, String alias) {
		String statement = identifier + " version '" + version + "' called " + alias;		
		this.missingIncludedLibraries.add(statement);
		this.errors.add(createError(statement + " does not exist in the Includes section of the MAT.", ctx));
	}
	
	public List<String> getErrorMessages() {
		
		if(!missingIncludedLibraries.isEmpty()) {
			this.errorMessages.add(createMissingIncludedLibraryErrorMessage());
		}
		
		return this.errorMessages;
	}
	
	private String createMissingIncludedLibraryErrorMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("The following included library statement(s) does not exist in the Includes section of the MAT: ");
		builder.append(String.join(", ", this.missingIncludedLibraries));
		builder.append(". Please navigate to the Includes section and add the library(ies) there to correct the error.");
		return builder.toString();
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
