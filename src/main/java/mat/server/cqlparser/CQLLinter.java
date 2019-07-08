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
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cqframework.cql.gen.cqlBaseListener;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.cqframework.cql.gen.cqlParser.AccessModifierContext;
import org.cqframework.cql.gen.cqlParser.CodeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.CodesystemDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ContextDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ExpressionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.FunctionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.IncludeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.LibraryDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ParameterDefinitionContext;
import org.cqframework.cql.gen.cqlParser.UsingDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ValuesetDefinitionContext;

import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLError;

public class CQLLinter extends cqlBaseListener {
	private static final String OID_PREFIX = "urn:oid:";

	
	private CQLLinterConfig config;
	private List<String> errorMessages;
	private Set<String> warningMessages;
	private List<CQLError> errors;
	
	private List<CQLCodeSystem> codeSystems;
	private List<CQLCode> matchedCodes;
	
	/**
	 * An invalid edit means that the library declaration, using declaration, included library declarations,
	 * valueset declarations, code declarations, or codesysetm declarations have been edited and do not match
	 * what was in the existing model. 
	 */
	private boolean hasInvalidEdits = false;
	private int libraryDefinitionStartLine;
	private int noCommentZoneStartLine;
	private int noCommentZoneEndLine;
	
	int numberOfIncludedLibraries = 0;
	int numberOfValuesets = 0;
	int numberOfCodes = 0;
	int numberOfCodesystems = 0;
		
	public CQLLinter(String cql, CQLLinterConfig config) throws IOException {
		this.config = config;
		this.errors = new ArrayList<>();
		this.errorMessages = new ArrayList<>();
		this.warningMessages = new HashSet<>();	
		
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
			hasInvalidEdits = true;
		}
		
		if(hasExtraneousCodesystem() || hasMissingCodesystem()) {
			hasInvalidEdits = true;
		}
		
		if((CollectionUtils.size(config.getPreviousCQLModel().getCqlIncludeLibrarys()) != numberOfIncludedLibraries)
				|| (CollectionUtils.size(config.getPreviousCQLModel().getValueSetList()) != numberOfValuesets)
				|| (CollectionUtils.size(config.getPreviousCQLModel().getCodeSystemList()) != numberOfCodesystems)
				|| (CollectionUtils.size(config.getPreviousCQLModel().getCodeList()) != numberOfCodes)) {
			hasInvalidEdits = true;
		}
		
		if(hasInvalidEdits) {
			this.warningMessages.add("Changes made to the CQL Library Name, Included Libraries, Value Sets, Codes, Codesystems, "
					+ "and/or Codesystem versions, can not be saved through the CQL Library Editor. "
					+ "Please make those changes in the appropriate areas of the CQL Workspace.");
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
	public void enterLibraryDefinition(LibraryDefinitionContext ctx) {
		libraryDefinitionStartLine = ctx.getStart().getLine();
		String name = CQLParserUtil.parseString(ctx.qualifiedIdentifier().getText());
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		
		if(!name.equals(config.getLibraryName()) || !version.equals(config.getLibraryVersion())) {
			hasInvalidEdits = true;
		}
	}
	
	@Override
	public void enterUsingDefinition(UsingDefinitionContext ctx) {
		String model = CQLParserUtil.parseString(ctx.modelIdentifier().getText());
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		
		if (!StringUtils.equals(model, config.getModelIdentifier()) || !StringUtils.equals(version, config.getModelVersion())) {
			hasInvalidEdits = true;
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

			// if there is an included library that does not match one that was in the previous model,
			// throw an invalid edit error message.
			if(potentialMatches.isEmpty()) {
				hasInvalidEdits = true;
			}
		}

		numberOfIncludedLibraries++;
	}
	
	@Override
	public void enterCodesystemDefinition(CodesystemDefinitionContext ctx) {
		CQLCodeSystem codesystem = new CQLCodeSystem();
		String codeSystemId = CQLParserUtil.parseString(ctx.codesystemId().getText());
		String codeSystemName = CQLParserUtil.parseString(ctx.identifier().getText());
		
		codesystem.setCodeSystem(codeSystemId);
		codesystem.setCodeSystemName(codeSystemName);
		
		if(ctx.versionSpecifier() != null) {
			codesystem.setCodeSystemVersion(CQLParserUtil.parseString(ctx.versionSpecifier().getText()));
		} else {
			codesystem.setCodeSystemVersion("");
		}
		
		numberOfCodesystems++;
		this.codeSystems.add(codesystem);
	}
	
	@Override
	public void enterValuesetDefinition(ValuesetDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String valuesetId = CQLParserUtil.parseString(ctx.valuesetId().getText());		
		String parsedValuesetId = valuesetId.replace(OID_PREFIX, "");

		// find all valuesets that have a matching identifier and oid
		if(config.getPreviousCQLModel().getValueSetList() != null) {
			List<CQLQualityDataSetDTO> potentialMatches = config.getPreviousCQLModel().getValueSetList().stream().filter(v -> (
					identifier.equals(v.getName())
					&& parsedValuesetId.equals(v.getOid())
					)).collect(Collectors.toList());

			// if there is an valueset that does not match one that was in the previous model,
			// throw an invalid edit error message.				
			if(potentialMatches.isEmpty()) {
				hasInvalidEdits = true;
			}
		} 

		numberOfValuesets++;
		noCommentZoneEndLine = ctx.getStop().getLine();
	}

	@Override
	public void enterCodeDefinition(CodeDefinitionContext ctx) {
		String identifier = CQLParserUtil.parseString(ctx.identifier().getText());
		String codeId = CQLParserUtil.parseString(ctx.codeId().getText());	
		String codesystemIdentifier = CQLParserUtil.parseString(ctx.codesystemIdentifier().getText());
		String displayClause = ctx.displayClause() != null ? CQLParserUtil.parseString(ctx.displayClause().STRING().getText()) : "";
		
		// find all codes that have a matching identifier and code id
		// in MAT the identifier is mapped the displayName field (which is also called the descriptor)
		if(config.getPreviousCQLModel().getCodeList() != null) {
			List<CQLCode> potentialMatches = config.getPreviousCQLModel().getCodeList().stream().filter(c -> 
				(c.getDisplayName().equals(identifier) 
				&& c.getCodeOID().equals(codeId) 
				&& c.getName().equals(displayClause)
				&& getCodeSystemIdentifier(c).equals(codesystemIdentifier))
			).collect(Collectors.toList());
			
			if(potentialMatches.isEmpty()) {
				hasInvalidEdits = true;
			}  else {
				this.matchedCodes.addAll(potentialMatches);
			}
		} 
		
		numberOfCodes++;
		noCommentZoneEndLine = ctx.getStop().getLine();
	}
	
	@Override
	public void enterAccessModifier(AccessModifierContext ctx) {
		this.warningMessages.add("The MAT does not support access modifiers tied to expressions. Any entered access modifiers have been removed from the CQL file.");
	}
	
	@Override
	public void enterContextDefinition(ContextDefinitionContext ctx) {
		if(!"Patient".equals(CQLParserUtil.parseString(ctx.identifier().getText()))) {
			this.warningMessages.add("The MAT only supports expressions with a context of patient. All expressions have been placed under context patient.");
		}
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
		return this.errorMessages;
	}
	
	public List<CQLError> getErrors() {
		return this.errors;
	}
	
	public List<String> getWarningMessages() {
		return new ArrayList<>(this.warningMessages);
	}

	public void setWarningMessages(List<String> warningMessages) {
		this.warningMessages = new HashSet<>(warningMessages);
	}
}
