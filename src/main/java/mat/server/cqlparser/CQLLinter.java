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
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringUtils;
import org.cqframework.cql.gen.cqlBaseListener;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.cqframework.cql.gen.cqlParser.CodeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ConceptDefinitionContext;
import org.cqframework.cql.gen.cqlParser.IncludeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.LibraryDefinitionContext;
import org.cqframework.cql.gen.cqlParser.UsingDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ValuesetDefinitionContext;

import mat.model.cql.CQLCode;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.CQLUtilityClass;
import mat.shared.CQLError;

public class CQLLinter extends cqlBaseListener {
	
	private static final String VALUESET_OID_PREFIX = "urn:oid:";
	
	private CQLLinterConfig config;
	private List<String> errorMessages;
	private List<String> missingIncludedLibraries;
	private List<String> missingValuesets;
	private List<String> missingCodes;
	private List<CQLError> errors;
	
	private int libraryDefinitionStartLine = 0;
	private int noCommentZoneStartLine = 0;
	private int noCommentZoneEndLine = 0;
	
	public CQLLinter(String cql, CQLLinterConfig config) throws IOException {
		this.config = config;
		this.errors = new ArrayList<>();
		this.errorMessages = new ArrayList<>();
		this.missingIncludedLibraries = new ArrayList<>();
		this.missingValuesets = new ArrayList<>();
		this.missingCodes = new ArrayList<>();
		
		InputStream stream = new ByteArrayInputStream(cql.getBytes());
		cqlLexer lexer = new cqlLexer(new ANTLRInputStream(stream));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		cqlParser parser = new cqlParser(tokens);
        parser.setBuildParseTree(true);    
    	ParseTree tree = parser.library();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, tree);
		doPostProcessing(parser, tokens);
	} 
	
	private void doPostProcessing(cqlParser parser, CommonTokenStream tokens) {
		if(isCommentInNoCommentZone(tokens)) {
			this.errorMessages.add("A comment was added in an incorrect location and could not be saved. Comments are permitted between the CQL Library declaration and the Model declaration, directly above a parameter, definition, or function.");
		}
	}
	
	/**
	 * the "no comment zone" goes from the beginning of the file to the library declaration statement and then
	 * the using statement to the end of the concept definition section
	 * any comment that appears in between here will not be saved and thus we need to throw an error
	 * @return
	 */
	private boolean isCommentInNoCommentZone(CommonTokenStream tokens) {
		List<Token> comments = new ArrayList<>();
		for(Token token: tokens.getTokens()) {
			if(token.getText().startsWith("//") || token.getText().startsWith("/*")) {
				comments.add(token);
			}
		}
		
		return isCommentBeforeLibraryDeclaration(comments) || isCommentBetweenUsingDeclarationAndConceptDeclaration(comments);
	}
	
	private boolean isCommentBetweenUsingDeclarationAndConceptDeclaration(List<Token> comments) {
		// the "no comment zone" goes from the beginning of the using statement to the end of the concept definition section
		// any comment that appears in between here will not be saved and thus we need to throw an error		
		for(Token comment : comments) {
			if(comment.getLine() >= noCommentZoneStartLine && comment.getLine() <= noCommentZoneEndLine) {
				return true;
			}
		}		
		return false;
	}
	
	private boolean isCommentBeforeLibraryDeclaration(List<Token> comments) {
		for(Token comment : comments) {
			if(comment.getLine() <= libraryDefinitionStartLine) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void enterLibraryDefinition(LibraryDefinitionContext ctx) {
		libraryDefinitionStartLine = ctx.getStart().getLine();
		String name = CQLParserUtil.parseString(ctx.identifier().getText());
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		
		if(!name.equals(config.getLibraryName()) || !version.equals(config.getLibraryVersion())) {
				String message = String.format("The library name and version must match what is on file for this CQL file: %s version '%s'.", 
					config.getLibraryName(), config.getLibraryVersion());
			
			errorMessages.add(message);
			errors.add(createError(message, ctx));
		}
	}
	
	@Override
	public void enterUsingDefinition(UsingDefinitionContext ctx) {
		String model = CQLParserUtil.parseString(ctx.modelIdentifier().getText());
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		
		if (!StringUtils.equals(model, config.getModelIdentifier()) || !StringUtils.equals(version, config.getModelVersion())) {
			StringBuilder sb = new StringBuilder();
			sb.append("The model and version declaration must match the current model and version used by the MAT: ");
			sb.append(config.getModelIdentifier()).append(CQLUtilityClass.VERSION).append(config.getModelVersion());

			String annotation = model + " version '" + version + "' does not match what is on file for the MAT.";

			errorMessages.add(sb.toString());
			errors.add(createError(annotation, ctx));
		}
		
		noCommentZoneStartLine = ctx.getStart().getLine();
		noCommentZoneEndLine = ctx.getStop().getLine();
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
	
	@Override
	public void enterCodeDefinition(CodeDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String codeId = CQLParserUtil.parseString(ctx.codeId().getText());	
		
		// find all codes that have a matching identifier and code id
		// in MAT the identifier is mapped the displayName field (which is also called the descriptor)
		if(config.getPreviousCQLModel().getCodeList() != null) {
			List<CQLCode> potentialMatches = config.getPreviousCQLModel().getCodeList().stream().filter(c -> 
				(c.getDisplayName().equals(identifier) 
				&& c.getCodeOID().equals(codeId))
			).collect(Collectors.toList());
			
			
			// if there were no matches in the previous model, or all matches do not have a code identifier, then return an error. 
			// not having a code identifier means that it has never been fetched through the codes section.
			// if there were no matches in the previous model, that also means it has never been fetched
			if(potentialMatches.isEmpty() 
					|| potentialMatches.stream().filter(c -> StringUtils.isEmpty(c.getCodeIdentifier())).count() > 0) {
				createCodeError(ctx, identifier);
			}
		} else {
			createCodeError(ctx, identifier);
		}
		
		noCommentZoneEndLine = ctx.getStop().getLine();
	}
	
	private void createCodeError(CodeDefinitionContext ctx, String identifier) {
		this.missingCodes.add(identifier);
		this.errors.add(createError(identifier + " does not exist in the Codes section of the MAT.", ctx));
	}

	@Override
	public void enterValuesetDefinition(ValuesetDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String valuesetId = CQLParserUtil.parseString(ctx.valuesetId().getText()).replace(VALUESET_OID_PREFIX, "");

		// find all libraries that have a matching identifier, alias, and version
		if(config.getPreviousCQLModel().getValueSetList() != null) {
			List<CQLQualityDataSetDTO> potentialMatches = config.getPreviousCQLModel().getValueSetList().stream().filter(v -> (
					identifier.equals(v.getName())
					&& valuesetId.equals(v.getOid())
				)).collect(Collectors.toList());
				
				// if there were no matches in the previous model, or all matches do not have an id, then return an error. 
				// not having an original code list name means that it has never been fetched through the valueset section.
				// if there were no matches in the previous model, that also means it has never been fetched
				if(potentialMatches.isEmpty() || potentialMatches.stream().filter(v -> StringUtils.isEmpty(v.getOriginalCodeListName())).count() > 0) {
					createValuesetError(ctx, identifier, valuesetId);
				}
		} else {
			createValuesetError(ctx, identifier, valuesetId);
		}
		
		noCommentZoneEndLine = ctx.getStop().getLine();
	}
	
	public void enterConceptDefinition(ConceptDefinitionContext ctx) {
		noCommentZoneEndLine = ctx.getStop().getLine();
	}

	private void createValuesetError(ValuesetDefinitionContext ctx, String identifier, String oid) {
		String valueset = identifier + " (" + oid + ")";
		this.missingValuesets.add(valueset);
		this.errors.add(createError(valueset + " does not exist in the Value Sets section of the MAT.", ctx));
	}
	
	public List<String> getErrorMessages() {
		
		if(!missingIncludedLibraries.isEmpty()) {
			this.errorMessages.add(createMissingIncludedLibraryErrorMessage());
		}
		
		if(!missingValuesets.isEmpty()) {
			this.errorMessages.add(createMissingValuesetErrorMessage());
		}
		
		if(!this.missingCodes.isEmpty()) {
			this.errorMessages.add(createMissingCodeErrorMessage());
		}
		
		return this.errorMessages;
	}
	
	private String createMissingCodeErrorMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("The following code(s) does not exist in the Codes section of the MAT: ");
		builder.append(String.join(", ", this.missingCodes));
		builder.append(". Please navigate to the Codes section and add the code(s) there to correct the error.");
		return builder.toString();
	}
	
	private String createMissingValuesetErrorMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("The following value set(s) does not exist in the Value Set section of the MAT: ");
		builder.append(String.join(", ", this.missingValuesets));
		builder.append(".  Please navigate to the Value Set section and add the value set(s) there to correct the error.");
		return builder.toString();
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
