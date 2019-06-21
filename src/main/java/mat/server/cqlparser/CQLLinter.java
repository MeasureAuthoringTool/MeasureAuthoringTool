package mat.server.cqlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import org.cqframework.cql.gen.cqlParser.AccessModifierContext;
import org.cqframework.cql.gen.cqlParser.CodeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.CodesystemDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ConceptDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ContextDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ExpressionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.FunctionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.IdentifierOrFunctionIdentifierContext;
import org.cqframework.cql.gen.cqlParser.IncludeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.LibraryDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ParameterDefinitionContext;
import org.cqframework.cql.gen.cqlParser.UsingDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ValuesetDefinitionContext;

import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.CQLUtilityClass;
import mat.shared.CQLError;

public class CQLLinter extends cqlBaseListener {
	
	private static final String OID_PREFIX = "urn:oid:";
	
	private CQLLinterConfig config;
	private List<String> errorMessages;
	private Set<String> warningMessages;
	private List<String> missingIncludedLibraries;
	private List<String> missingValuesets;
	private List<String> missingCodes;
	private List<CQLError> errors;
	
	private int libraryDefinitionStartLine = 0;
	private int noCommentZoneStartLine = 0;
	private int noCommentZoneEndLine = 0;
	
	
	private List<CQLCodeSystem> codeSystems;
	private List<CQLCode> matchedCodes;

	
	public CQLLinter(String cql, CQLLinterConfig config) throws IOException {
		this.config = config;
		this.errors = new ArrayList<>();
		this.errorMessages = new ArrayList<>();
		this.warningMessages = new HashSet<>();
		this.missingIncludedLibraries = new ArrayList<>();
		this.missingValuesets = new ArrayList<>();
		this.missingCodes = new ArrayList<>();
		this.codeSystems = new ArrayList<>();
		this.matchedCodes = new ArrayList<>();
		
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
			this.warningMessages.add("A comment was added in an incorrect location and could not be saved. Comments are permitted between the CQL Library declaration and the Model declaration, directly above a parameter, definition, or function.");
		}
		
		if(hasExtraneousCodesystem() || hasMissingCodesystem()) {
			this.warningMessages.add("The MAT was unable to save the change made to the codesystem or codesystem version. These items are pulled directly from what is on file from the Codes Section of the CQL Workspace/CQL Composer.");
		}
	}
	
	private boolean hasMissingCodesystem() {
		for(CQLCode code : matchedCodes) {
			Optional<CQLCodeSystem> codeSystem = this.codeSystems.stream().filter(cs -> cs.getCodeSystemName().equals(getCodeSystemIdentifier(code))).findFirst();
			if(codeSystem.isPresent()) {
				if(doesCodeCodeSystemNotMatchCodeSystemDeclaration(code, codeSystem.get())) {
					return true;
				}
			} else {
				return true;
			}
		}
		
		return false;
	}

	private boolean doesCodeCodeSystemNotMatchCodeSystemDeclaration(CQLCode code, CQLCodeSystem codeSystem) {
		
		String codeSystemOIDFromCodeSystem = codeSystem.getCodeSystem().replace("urn:oid:", "");
		String codeSystemOIDFromCode = code.getCodeSystemOID();
		
		boolean isCodeSystemOIDNotMatching = !codeSystemOIDFromCodeSystem.equals(codeSystemOIDFromCode);
		
		String codeSystemNameFromCodeSystem = codeSystem.getCodeSystemName();
		String codeSystemNameFromCode = getCodeSystemIdentifier(code);
		boolean isCodeSystemNameNotMatching =  !codeSystemNameFromCodeSystem.equals(codeSystemNameFromCode);
		
		String codeSystemVersionFromCodeSystem = codeSystem.getCodeSystemVersion().replace("urn:hl7:version:", "");
		String codeSystemVersionFromCode = code.getCodeSystemVersion();
		boolean isCodeSystemVersionNotMatching =  code.isIsCodeSystemVersionIncluded() && !codeSystemVersionFromCode.equals(codeSystemVersionFromCodeSystem);				
		
		boolean isCodeSystemVersionNotMissing = !code.isIsCodeSystemVersionIncluded() && StringUtils.isNotEmpty(codeSystemVersionFromCodeSystem);
		
		return  isCodeSystemOIDNotMatching || isCodeSystemNameNotMatching || isCodeSystemVersionNotMatching || isCodeSystemVersionNotMissing;
	}

	private boolean hasExtraneousCodesystem() {
		for(CQLCodeSystem codesystem : codeSystems) {
			Optional<CQLCode> code = this.matchedCodes.stream().filter(c -> {
				String codesystemName = getCodeSystemIdentifier(c);
				
				return codesystemName.equals(codesystem.getCodeSystemName());
			}).findFirst();
			
			if(code.isPresent()) {
				if(doesCodeCodeSystemNotMatchCodeSystemDeclaration(code.get(), codesystem)) {
					return true;
				}
			} else {
				return true;
			}
		}
		
		return false;
	}

	private String getCodeSystemIdentifier(CQLCode c) {
		String codesystemName = c.getCodeSystemName();
		if(c.isIsCodeSystemVersionIncluded()) {
			codesystemName += ":" + c.getCodeSystemVersion();
		}
		return codesystemName;
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
	
	private void createErrorIfIdentifierIsUnquoted(String ctx) {
		if(!ctx.startsWith("\"") || !ctx.endsWith("\"")) {
			this.warningMessages.add("The MAT requires quotation marks around definition, parameter, function, value set, code, and codesystem names. We have added quotation marks in the CQL file where they were missing.");
		}
	}
	
	@Override
	public void enterAccessModifier(AccessModifierContext ctx) {
		this.warningMessages.add("The MAT does not support access modifiers tied to expressions. Any entered access modifiers have been removed from the CQL file.");
	}

	@Override
	public void enterLibraryDefinition(LibraryDefinitionContext ctx) {
		libraryDefinitionStartLine = ctx.getStart().getLine();
		String name = CQLParserUtil.parseString(ctx.qualifiedIdentifier().getText());
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
		String identifier = CQLParserUtil.parseString(ctx.qualifiedIdentifier().getText());
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
	public void enterCodesystemDefinition(CodesystemDefinitionContext ctx) {
		createErrorIfIdentifierIsUnquoted(ctx.identifier().getText());
		CQLCodeSystem codesystem = new CQLCodeSystem();
		String codeSystemId = CQLParserUtil.parseString(ctx.codesystemId().getText());
		String codeSystemName = CQLParserUtil.parseString(ctx.identifier().getText());
		
		codesystem.setCodeSystem(codeSystemId);
		codesystem.setCodeSystemName(codeSystemName);
		
		
		if(!codeSystemId.startsWith(OID_PREFIX)) {
			createWarningMessageForMissingOIDPrefix();
		}
		
		if(ctx.versionSpecifier() != null) {
			codesystem.setCodeSystemVersion(CQLParserUtil.parseString(ctx.versionSpecifier().getText()));
		} else {
			codesystem.setCodeSystemVersion("");
		}
		
		this.codeSystems.add(codesystem);
	}
	
	@Override
	public void enterCodeDefinition(CodeDefinitionContext ctx) {
		createErrorIfIdentifierIsUnquoted(ctx.identifier().getText());
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String codeId = CQLParserUtil.parseString(ctx.codeId().getText());	
		
		String displayClause = ctx.displayClause() != null ? CQLParserUtil.parseString(ctx.displayClause().STRING().getText()) : "";
		
		// find all codes that have a matching identifier and code id
		// in MAT the identifier is mapped the displayName field (which is also called the descriptor)
		if(config.getPreviousCQLModel().getCodeList() != null) {
			List<CQLCode> potentialMatches = config.getPreviousCQLModel().getCodeList().stream().filter(c -> 
				(c.getDisplayName().equals(identifier) 
				&& c.getCodeOID().equals(codeId) 
				&& c.getName().equals(displayClause))
			).collect(Collectors.toList());
			
			
			// if there were no matches in the previous model, or all matches do not have a code identifier, then return an error. 
			// not having a code identifier means that it has never been fetched through the codes section.
			// if there were no matches in the previous model, that also means it has never been fetched
			if(potentialMatches.isEmpty() 
					|| potentialMatches.stream().filter(c -> StringUtils.isEmpty(c.getCodeIdentifier())).count() > 0) {
				createCodeError(ctx, identifier);
			}  else {
				this.matchedCodes.addAll(potentialMatches);
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
		createErrorIfIdentifierIsUnquoted(ctx.identifier().getText());
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String valuesetId = CQLParserUtil.parseString(ctx.valuesetId().getText());
		if(!valuesetId.startsWith(OID_PREFIX)) {
			createWarningMessageForMissingOIDPrefix();
		}
		
		String parsedValuesetId = valuesetId.replace(OID_PREFIX, "");
		

		// find all libraries that have a matching identifier, alias, and version
		if(config.getPreviousCQLModel().getValueSetList() != null) {
			List<CQLQualityDataSetDTO> potentialMatches = config.getPreviousCQLModel().getValueSetList().stream().filter(v -> (
					identifier.equals(v.getName())
					&& parsedValuesetId.equals(v.getOid())
				)).collect(Collectors.toList());
				
				// if there were no matches in the previous model, or all matches do not have an id, then return an error. 
				// not having an original code list name means that it has never been fetched through the valueset section.
				// if there were no matches in the previous model, that also means it has never been fetched
				if(potentialMatches.isEmpty() || potentialMatches.stream().filter(v -> StringUtils.isEmpty(v.getOriginalCodeListName())).count() > 0) {
					createValuesetError(ctx, identifier, parsedValuesetId);
				}
		} else {
			createValuesetError(ctx, identifier, parsedValuesetId);
		}
		
		noCommentZoneEndLine = ctx.getStop().getLine();
	}

	private void createWarningMessageForMissingOIDPrefix() {
		this.warningMessages.add("The MAT has added \"urn:oid:\" to value set and/or codesystem declarations where necessary to ensure items are in the correct format.");
	}
	
	@Override
	public void enterConceptDefinition(ConceptDefinitionContext ctx) {
		noCommentZoneEndLine = ctx.getStop().getLine();
	}
	
	@Override
	public void enterContextDefinition(ContextDefinitionContext ctx) {
		if(!"Patient".equals(CQLParserUtil.parseString(ctx.identifier().getText()))) {
			this.warningMessages.add("The MAT only supports expressions with a context of patient. All expressions have been placed under context patient.");
		}
	}

	private void createValuesetError(ValuesetDefinitionContext ctx, String identifier, String oid) {
		String valueset = identifier + " (" + oid + ")";
		this.missingValuesets.add(valueset);
		this.errors.add(createError(valueset + " does not exist in the Value Sets section of the MAT.", ctx));
	}
	
	@Override
	public void enterParameterDefinition(ParameterDefinitionContext ctx) {
		createErrorIfIdentifierIsUnquoted(ctx.identifier().getText());
	}
	
	@Override
	public void enterExpressionDefinition(ExpressionDefinitionContext ctx) {
		createErrorIfIdentifierIsUnquoted(ctx.identifier().getText());
	}
	
	@Override
	public void enterFunctionDefinition(FunctionDefinitionContext ctx) {
		createErrorIfIdentifierIsUnquoted(ctx.identifierOrFunctionIdentifier().getText());

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
		builder.append(". Please navigate to the Codes section and ensure the codes there match what is in the CQL Library Editor.");
		return builder.toString();
	}
	
	private String createMissingValuesetErrorMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append("The following value set(s) does not exist in the Value Set section of the MAT: ");
		builder.append(String.join(", ", this.missingValuesets));
		builder.append(". Please navigate to the Value Set section and ensure the value sets there match what is in the CQL Library Editor.");
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

	public List<String> getWarningMessages() {
		return new ArrayList<>(this.warningMessages);
	}

	public void setWarningMessages(List<String> warningMessages) {
		this.warningMessages = new HashSet<>(warningMessages);
	}
}
