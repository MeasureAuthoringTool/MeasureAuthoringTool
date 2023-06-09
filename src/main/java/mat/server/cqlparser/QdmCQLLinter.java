package mat.server.cqlparser;

import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLQualityDataSetDTO;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.cqframework.cql.gen.cqlParser.AccessModifierContext;
import org.cqframework.cql.gen.cqlParser.CodeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.CodesystemDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ConceptDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ContextDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ExpressionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.FunctionDefinitionContext;
import org.cqframework.cql.gen.cqlParser.IncludeDefinitionContext;
import org.cqframework.cql.gen.cqlParser.LibraryDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ParameterDefinitionContext;
import org.cqframework.cql.gen.cqlParser.UsingDefinitionContext;
import org.cqframework.cql.gen.cqlParser.ValuesetDefinitionContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QdmCQLLinter extends CQLLinter {
	private static final String VERSION_PREFIX = "urn:hl7:version:";
	private static final String OID_PREFIX = "urn:oid:";
		
	public QdmCQLLinter(String cql, CQLLinterConfig config) throws IOException {
		this.config = config;
		this.errors = new ArrayList<>();
		this.errorMessages = new ArrayList<>();
		this.warningMessages = new HashSet<>();
		
		this.codeSystems = new ArrayList<>();
		this.matchedCodes = new ArrayList<>();
		
		InputStream stream = new ByteArrayInputStream(cql.getBytes());
		cqlLexer lexer = new cqlLexer(new ANTLRInputStream(stream));
		tokens = new CommonTokenStream(lexer);
		tokens.fill();
		cqlParser parser = new cqlParser(tokens);
        parser.setBuildParseTree(true);    
    	ParseTree tree = parser.library();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, tree);
		doPostProcessing(tokens);
	} 
		
	private void doPostProcessing(CommonTokenStream tokens) {
		if(isCommentInNoCommentZone(tokens)) {
			hasInvalidEdits = true;
		}
		
		if(isLineCommentInIncorrectSpot(tokens)) {
			this.warningMessages.add("End-line comments are not permitted immediately preceding the "
					+ "parameter or definition sections of the CQL library and have been removed.");
		}
				
		if(hasExtraneousCodesystem() || hasMissingCodesystem()) {
			hasInvalidEdits = true;
		}
		
		if(hasInvalidSizes()) {
			hasInvalidEdits = true;
		}
		
		if(!enteredLibraryDefinition || !enteredUsingDefinition) {
			hasInvalidEdits = true;
		}
		
		if(hasInvalidEdits) {
			this.warningMessages.add("Changes made to the CQL library declaration, model declaration, "
					+ "included libraries, value sets, codes, codesystems, and/or codesystem versions, can not be saved through the CQL Library Editor. "
					+ "Please make those changes in the appropriate areas of the CQL Workspace.");
		}
	}

	private boolean hasInvalidSizes() {
		return (CollectionUtils.size(config.getPreviousCQLModel().getCqlIncludeLibrarys()) != numberOfIncludedLibraries)
				|| (CollectionUtils.size(config.getPreviousCQLModel().getValueSetList()) != numberOfValuesets)
				|| (CollectionUtils.size(config.getPreviousCQLModel().getCodeList()) != numberOfCodes);
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
		
		String codeSystemOIDFromCodeSystem = codeSystem.getCodeSystem().replaceFirst(OID_PREFIX, "");
		String codeSystemOIDFromCode = code.getCodeSystemOID();
		
		boolean isCodeSystemOIDNotMatching = !codeSystemOIDFromCodeSystem.equals(codeSystemOIDFromCode);
		
		String codeSystemNameFromCodeSystem = codeSystem.getCodeSystemName();
		String codeSystemNameFromCode = getCodeSystemIdentifier(code);
		boolean isCodeSystemNameNotMatching =  !codeSystemNameFromCodeSystem.equals(codeSystemNameFromCode);
		
		String codeSystemVersionFromCodeSystem = codeSystem.getCodeSystemVersion().replaceFirst(VERSION_PREFIX, "");
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
	
	private boolean isLineCommentInIncorrectSpot(CommonTokenStream tokens) {
		List<Token> comments = new ArrayList<>();
		for(Token token: tokens.getTokens()) {
			if(token.getText().startsWith("//")) {
				comments.add(token);
			}
		}
		
		return isLineCommentBetweenContextAndFirstDefinition(comments) || isLineCommentBetweenTerminologyAndFirstParameter(comments);
	}
	
	private boolean isLineCommentBetweenTerminologyAndFirstParameter(List<Token> comments) {
		for(Token comment : comments) {
			if(comment.getLine() >= terminologyEndLine && comment.getLine() <= parameterStartLine) {
				return true;
			}
		}		
		
		return false;
	}

	private boolean isLineCommentBetweenContextAndFirstDefinition(List<Token> comments) {
		for(Token comment : comments) {
			if(comment.getLine() >= contextLine && comment.getLine() <= definitionStartLine) {
				return true;
			}
		}		
		
		return false;
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
	
	private String getExpectedIdentifier(String identifier) {
		return "\"" + identifier + "\"";
	}
	
	@Override
	public void enterLibraryDefinition(LibraryDefinitionContext ctx) {
		enteredLibraryDefinition = true;
		
		if(ctx.versionSpecifier() == null) {
			hasInvalidEdits = true;
			return;
		}
		
		libraryDefinitionStartLine = ctx.getStart().getLine();
		String name = ctx.qualifiedIdentifier().getText();
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		String comment = getLibraryComment(ctx);
		String previousComment = config.getPreviousCQLModel().getLibraryComment();
		
		if(StringUtils.isEmpty(comment)) {
			comment = null;
		}
		
		if(StringUtils.isEmpty(previousComment)) {
			previousComment = null;
		}
		
		if(!StringUtils.equals(name, config.getLibraryName()) || !StringUtils.equals(version, config.getLibraryVersion()) 
				|| !StringUtils.equals(comment, previousComment)) {
			hasInvalidEdits = true;
		}		
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
	
	private String trimComment(String comment) {
		return comment.replace("/*", "").replace("*/", "").trim();
	}
	
	@Override
	public void enterUsingDefinition(UsingDefinitionContext ctx) {
		enteredUsingDefinition = true;
		if(ctx.versionSpecifier() == null) {
			hasInvalidEdits = true;
			return;
		}

        String model = ctx.qualifiedIdentifier().getText();
		String version = CQLParserUtil.parseString(ctx.versionSpecifier().getText());
		if (!StringUtils.equals(model, config.getModelIdentifier()) || !StringUtils.equals(version, config.getModelVersion())) {
			hasInvalidEdits = true;
		}
		
		noCommentZoneStartLine = ctx.getStart().getLine();
		noCommentZoneEndLine = ctx.getStop().getLine();
	}
	
	@Override
	public void enterIncludeDefinition(IncludeDefinitionContext ctx) {
		String identifier = ctx.qualifiedIdentifier().getText();
		String alias = ctx.localIdentifier().getText();
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
		
		if(!getExpectedIdentifier(codeSystemName).equals(ctx.identifier().getText())) {
			hasInvalidEdits = true;
		}
		
		if(ctx.codesystemId() != null && !CQLParserUtil.parseString(ctx.codesystemId().getText()).startsWith(OID_PREFIX)) {
			hasInvalidEdits = true;
		}
		
		if(ctx.versionSpecifier() != null && !CQLParserUtil.parseString(ctx.versionSpecifier().getText()).startsWith(VERSION_PREFIX)) {
			hasInvalidEdits = true;
		}
		
		if(ctx.accessModifier() != null) {
			hasInvalidEdits = true;
		}
		
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
		String identifier = ctx.identifier().getText();
		String valuesetId = CQLParserUtil.parseString(ctx.valuesetId().getText());		
		
		if(ctx.accessModifier() != null) {
			hasInvalidEdits = true;
		}
		
		if(ctx.versionSpecifier() != null) {
			hasInvalidEdits = true;
		}
		
		if(ctx.codesystems() != null) {
			hasInvalidEdits = true;
		}

		// find all valuesets that have a matching identifier and oid
		if(config.getPreviousCQLModel().getValueSetList() != null) {
			List<CQLQualityDataSetDTO> potentialMatches = config.getPreviousCQLModel().getValueSetList().stream().filter(v -> (
					identifier.equals(getExpectedIdentifier(v.getName()))
					&& valuesetId.equals(OID_PREFIX + v.getOid())
					)).collect(Collectors.toList());

			// if there is an valueset that does not match one that was in the previous model,
			// throw an invalid edit error message.				
			if(potentialMatches.isEmpty()) {
				hasInvalidEdits = true;
			}
		} 

		numberOfValuesets++;
		terminologyEndLine = ctx.getStop().getLine();
		noCommentZoneEndLine = ctx.getStop().getLine();
	}

	@Override
	public void enterCodeDefinition(CodeDefinitionContext ctx) {
		String identifier = ctx.identifier().getText();
		String codeId = CQLParserUtil.parseString(ctx.codeId().getText());	
		String codesystemIdentifier =  CQLParserUtil.parseString(ctx.codesystemIdentifier().getText());
		String displayClause = ctx.displayClause() != null ? CQLParserUtil.parseString(ctx.displayClause().STRING().getText()) : "";
		
		if(ctx.accessModifier() != null) {
			hasInvalidEdits = true;
		}
		
		if(!getExpectedIdentifier(codesystemIdentifier).equals(ctx.codesystemIdentifier().getText())) {
			hasInvalidEdits = true;
		}
		
		// find all codes that have a matching identifier and code id
		// in MAT the identifier is mapped the displayName field (which is also called the descriptor)
		if(config.getPreviousCQLModel().getCodeList() != null) {
			List<CQLCode> potentialMatches = config.getPreviousCQLModel().getCodeList().stream().filter(c -> 
				(getExpectedIdentifier(c.getDisplayName()).equals(identifier) 
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
		terminologyEndLine = ctx.getStop().getLine();
		noCommentZoneEndLine = ctx.getStop().getLine();
	}
	
	@Override
	public void enterConceptDefinition(ConceptDefinitionContext ctx) {
		if(ctx != null) {
			this.warningMessages.add("The MAT does not support concept declarations. Any entered concept declarations have been removed from the CQL file.");
		}
		
		terminologyEndLine = ctx.getStop().getLine();
	}
	
	private void createErrorIfAccessModifier(AccessModifierContext ctx) {
		if(ctx != null) {
			this.warningMessages.add("The MAT does not support access modifiers tied to expressions. Any entered access modifiers have been removed from the CQL file.");
		}
	}
	
	@Override
	public void enterContextDefinition(ContextDefinitionContext ctx) {
		if(contextLine == 0) {
			contextLine = ctx.getStart().getLine();
		}
		
		if(!"Patient".equals(CQLParserUtil.parseString(ctx.identifier().getText()))) {
			this.warningMessages.add("The MAT only supports expressions with a context of patient. All expressions have been placed under context patient.");
		}
	}
	
	@Override
	public void enterParameterDefinition(ParameterDefinitionContext ctx) {
		if(parameterStartLine == 0) {
			parameterStartLine = ctx.getStart().getLine();
		}
		
		createErrorIfAccessModifier(ctx.accessModifier());
		createErrorIfIdentifierIsUnquoted(ctx.identifier().getText());
	}
	
	@Override
	public void enterExpressionDefinition(ExpressionDefinitionContext ctx) {
		if(definitionStartLine == 0) {
			definitionStartLine = ctx.getStart().getLine();
		}
		
		createErrorIfAccessModifier(ctx.accessModifier());
		createErrorIfIdentifierIsUnquoted(ctx.identifier().getText());
	}
	
	@Override
	public void enterFunctionDefinition(FunctionDefinitionContext ctx) {
		createErrorIfAccessModifier(ctx.accessModifier());
		createErrorIfIdentifierIsUnquoted(ctx.identifierOrFunctionIdentifier().getText());

	}
}
