package mat.server.cqlparser;

// Generated from cql.g4 by ANTLR 4.5
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class cqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66, 
		T__66=67, T__67=68, T__68=69, T__69=70, T__70=71, T__71=72, T__72=73, 
		T__73=74, T__74=75, T__75=76, T__76=77, T__77=78, T__78=79, T__79=80, 
		T__80=81, T__81=82, T__82=83, T__83=84, T__84=85, T__85=86, T__86=87, 
		T__87=88, T__88=89, T__89=90, T__90=91, T__91=92, T__92=93, T__93=94, 
		T__94=95, T__95=96, T__96=97, T__97=98, T__98=99, T__99=100, T__100=101, 
		T__101=102, T__102=103, T__103=104, T__104=105, T__105=106, T__106=107, 
		T__107=108, T__108=109, T__109=110, T__110=111, T__111=112, T__112=113, 
		T__113=114, T__114=115, T__115=116, T__116=117, T__117=118, T__118=119, 
		T__119=120, T__120=121, T__121=122, T__122=123, T__123=124, T__124=125, 
		T__125=126, T__126=127, T__127=128, T__128=129, IDENTIFIER=130, QUANTITY=131, 
		DATETIME=132, TIME=133, QUOTEDIDENTIFIER=134, STRING=135, WS=136, NEWLINE=137, 
		COMMENT=138, LINE_COMMENT=139;
	public static final int
		RULE_logic = 0, RULE_libraryDefinition = 1, RULE_usingDefinition = 2, 
		RULE_includeDefinition = 3, RULE_localIdentifier = 4, RULE_accessModifier = 5, 
		RULE_parameterDefinition = 6, RULE_codesystemDefinition = 7, RULE_valuesetDefinition = 8, 
		RULE_codesystems = 9, RULE_codesystemIdentifier = 10, RULE_libraryIdentifier = 11, 
		RULE_codesystemId = 12, RULE_valuesetId = 13, RULE_versionSpecifier = 14, 
		RULE_typeSpecifier = 15, RULE_namedTypeSpecifier = 16, RULE_modelIdentifier = 17, 
		RULE_listTypeSpecifier = 18, RULE_intervalTypeSpecifier = 19, RULE_tupleTypeSpecifier = 20, 
		RULE_tupleElementDefinition = 21, RULE_statement = 22, RULE_expressionDefinition = 23, 
		RULE_contextDefinition = 24, RULE_functionDefinition = 25, RULE_operandDefinition = 26, 
		RULE_functionBody = 27, RULE_querySource = 28, RULE_aliasedQuerySource = 29, 
		RULE_alias = 30, RULE_queryInclusionClause = 31, RULE_withClause = 32, 
		RULE_withoutClause = 33, RULE_retrieve = 34, RULE_valuesetPathIdentifier = 35, 
		RULE_valueset = 36, RULE_qualifier = 37, RULE_query = 38, RULE_sourceClause = 39, 
		RULE_singleSourceClause = 40, RULE_multipleSourceClause = 41, RULE_defineClause = 42, 
		RULE_defineClauseItem = 43, RULE_whereClause = 44, RULE_returnClause = 45, 
		RULE_sortClause = 46, RULE_sortDirection = 47, RULE_sortByItem = 48, RULE_qualifiedIdentifier = 49, 
		RULE_expression = 50, RULE_dateTimePrecision = 51, RULE_dateTimeComponent = 52, 
		RULE_pluralDateTimePrecision = 53, RULE_expressionTerm = 54, RULE_caseExpressionItem = 55, 
		RULE_dateTimePrecisionSpecifier = 56, RULE_relativeQualifier = 57, RULE_offsetRelativeQualifier = 58, 
		RULE_quantityOffset = 59, RULE_intervalOperatorPhrase = 60, RULE_term = 61, 
		RULE_intervalSelector = 62, RULE_tupleSelector = 63, RULE_tupleElementSelector = 64, 
		RULE_instanceSelector = 65, RULE_instanceElementSelector = 66, RULE_listSelector = 67, 
		RULE_displayClause = 68, RULE_codeSelector = 69, RULE_conceptSelector = 70, 
		RULE_literal = 71, RULE_nullLiteral = 72, RULE_booleanLiteral = 73, RULE_stringLiteral = 74, 
		RULE_dateTimeLiteral = 75, RULE_timeLiteral = 76, RULE_quantityLiteral = 77, 
		RULE_unit = 78, RULE_identifier = 79;
	public static final String[] ruleNames = {
		"logic", "libraryDefinition", "usingDefinition", "includeDefinition", 
		"localIdentifier", "accessModifier", "parameterDefinition", "codesystemDefinition", 
		"valuesetDefinition", "codesystems", "codesystemIdentifier", "libraryIdentifier", 
		"codesystemId", "valuesetId", "versionSpecifier", "typeSpecifier", "namedTypeSpecifier", 
		"modelIdentifier", "listTypeSpecifier", "intervalTypeSpecifier", "tupleTypeSpecifier", 
		"tupleElementDefinition", "statement", "expressionDefinition", "contextDefinition", 
		"functionDefinition", "operandDefinition", "functionBody", "querySource", 
		"aliasedQuerySource", "alias", "queryInclusionClause", "withClause", "withoutClause", 
		"retrieve", "valuesetPathIdentifier", "valueset", "qualifier", "query", 
		"sourceClause", "singleSourceClause", "multipleSourceClause", "defineClause", 
		"defineClauseItem", "whereClause", "returnClause", "sortClause", "sortDirection", 
		"sortByItem", "qualifiedIdentifier", "expression", "dateTimePrecision", 
		"dateTimeComponent", "pluralDateTimePrecision", "expressionTerm", "caseExpressionItem", 
		"dateTimePrecisionSpecifier", "relativeQualifier", "offsetRelativeQualifier", 
		"quantityOffset", "intervalOperatorPhrase", "term", "intervalSelector", 
		"tupleSelector", "tupleElementSelector", "instanceSelector", "instanceElementSelector", 
		"listSelector", "displayClause", "codeSelector", "conceptSelector", "literal", 
		"nullLiteral", "booleanLiteral", "stringLiteral", "dateTimeLiteral", "timeLiteral", 
		"quantityLiteral", "unit", "identifier"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'library'", "'version'", "'using'", "'include'", "'called'", "'public'", 
		"'private'", "'parameter'", "'default'", "'codesystem'", "':'", "'valueset'", 
		"'codesystems'", "'('", "','", "')'", "'.'", "'List'", "'<'", "'>'", "'Interval'", 
		"'Tuple'", "'{'", "'}'", "'define'", "'context'", "'function'", "'with'", 
		"'such that'", "'without'", "'['", "'in'", "']'", "'from'", "'where'", 
		"'return'", "'all'", "'distinct'", "'sort'", "'by'", "'asc'", "'ascending'", 
		"'desc'", "'descending'", "'is'", "'not'", "'null'", "'true'", "'false'", 
		"'as'", "'cast'", "'exists'", "'properly'", "'between'", "'and'", "'difference'", 
		"'<='", "'>='", "'='", "'<>'", "'matches'", "'contains'", "'or'", "'xor'", 
		"'union'", "'intersect'", "'except'", "'year'", "'month'", "'day'", "'hour'", 
		"'minute'", "'second'", "'millisecond'", "'date'", "'time'", "'timezone'", 
		"'years'", "'months'", "'days'", "'hours'", "'minutes'", "'seconds'", 
		"'milliseconds'", "'convert'", "'to'", "'+'", "'-'", "'start'", "'end'", 
		"'of'", "'duration'", "'width'", "'successor'", "'predecessor'", "'singleton'", 
		"'minimum'", "'maximum'", "'^'", "'*'", "'/'", "'div'", "'mod'", "'if'", 
		"'then'", "'else'", "'case'", "'collapse'", "'expand'", "'when'", "'or before'", 
		"'or after'", "'or more'", "'or less'", "'starts'", "'ends'", "'occurs'", 
		"'same'", "'includes'", "'during'", "'included in'", "'before'", "'after'", 
		"'within'", "'meets'", "'overlaps'", "'display'", "'Code'", "'Concept'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, "IDENTIFIER", 
		"QUANTITY", "DATETIME", "TIME", "QUOTEDIDENTIFIER", "STRING", "WS", "NEWLINE", 
		"COMMENT", "LINE_COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "cql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public cqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class LogicContext extends ParserRuleContext {
		public LibraryDefinitionContext libraryDefinition() {
			return getRuleContext(LibraryDefinitionContext.class,0);
		}
		public List<UsingDefinitionContext> usingDefinition() {
			return getRuleContexts(UsingDefinitionContext.class);
		}
		public UsingDefinitionContext usingDefinition(int i) {
			return getRuleContext(UsingDefinitionContext.class,i);
		}
		public List<IncludeDefinitionContext> includeDefinition() {
			return getRuleContexts(IncludeDefinitionContext.class);
		}
		public IncludeDefinitionContext includeDefinition(int i) {
			return getRuleContext(IncludeDefinitionContext.class,i);
		}
		public List<CodesystemDefinitionContext> codesystemDefinition() {
			return getRuleContexts(CodesystemDefinitionContext.class);
		}
		public CodesystemDefinitionContext codesystemDefinition(int i) {
			return getRuleContext(CodesystemDefinitionContext.class,i);
		}
		public List<ValuesetDefinitionContext> valuesetDefinition() {
			return getRuleContexts(ValuesetDefinitionContext.class);
		}
		public ValuesetDefinitionContext valuesetDefinition(int i) {
			return getRuleContext(ValuesetDefinitionContext.class,i);
		}
		public List<ParameterDefinitionContext> parameterDefinition() {
			return getRuleContexts(ParameterDefinitionContext.class);
		}
		public ParameterDefinitionContext parameterDefinition(int i) {
			return getRuleContext(ParameterDefinitionContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public LogicContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logic; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterLogic(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitLogic(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitLogic(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicContext logic() throws RecognitionException {
		LogicContext _localctx = new LogicContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_logic);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(160);
				libraryDefinition();
				}
			}

			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(163);
				usingDefinition();
				}
				}
				setState(168);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(169);
				includeDefinition();
				}
				}
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(178);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(175);
					codesystemDefinition();
					}
					} 
				}
				setState(180);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(184);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(181);
					valuesetDefinition();
					}
					} 
				}
				setState(186);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7))) != 0)) {
				{
				{
				setState(187);
				parameterDefinition();
				}
				}
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(194); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(193);
				statement();
				}
				}
				setState(196); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__24 || _la==T__25 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LibraryDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public VersionSpecifierContext versionSpecifier() {
			return getRuleContext(VersionSpecifierContext.class,0);
		}
		public LibraryDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_libraryDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterLibraryDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitLibraryDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitLibraryDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryDefinitionContext libraryDefinition() throws RecognitionException {
		LibraryDefinitionContext _localctx = new LibraryDefinitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_libraryDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			match(T__0);
			setState(199);
			identifier();
			setState(202);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(200);
				match(T__1);
				setState(201);
				versionSpecifier();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UsingDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public VersionSpecifierContext versionSpecifier() {
			return getRuleContext(VersionSpecifierContext.class,0);
		}
		public UsingDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_usingDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterUsingDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitUsingDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitUsingDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UsingDefinitionContext usingDefinition() throws RecognitionException {
		UsingDefinitionContext _localctx = new UsingDefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_usingDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			match(T__2);
			setState(205);
			identifier();
			setState(208);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(206);
				match(T__1);
				setState(207);
				versionSpecifier();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncludeDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public LocalIdentifierContext localIdentifier() {
			return getRuleContext(LocalIdentifierContext.class,0);
		}
		public VersionSpecifierContext versionSpecifier() {
			return getRuleContext(VersionSpecifierContext.class,0);
		}
		public IncludeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_includeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIncludeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIncludeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIncludeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IncludeDefinitionContext includeDefinition() throws RecognitionException {
		IncludeDefinitionContext _localctx = new IncludeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_includeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			match(T__3);
			setState(211);
			identifier();
			setState(214);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(212);
				match(T__1);
				setState(213);
				versionSpecifier();
				}
			}

			setState(216);
			match(T__4);
			setState(217);
			localIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public LocalIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterLocalIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitLocalIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitLocalIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocalIdentifierContext localIdentifier() throws RecognitionException {
		LocalIdentifierContext _localctx = new LocalIdentifierContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_localIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AccessModifierContext extends ParserRuleContext {
		public AccessModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accessModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterAccessModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitAccessModifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitAccessModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccessModifierContext accessModifier() throws RecognitionException {
		AccessModifierContext _localctx = new AccessModifierContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_accessModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			_la = _input.LA(1);
			if ( !(_la==T__5 || _la==T__6) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParameterDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterParameterDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitParameterDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitParameterDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDefinitionContext parameterDefinition() throws RecognitionException {
		ParameterDefinitionContext _localctx = new ParameterDefinitionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_parameterDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(223);
				accessModifier();
				}
			}

			setState(226);
			match(T__7);
			setState(227);
			identifier();
			setState(229);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__17) | (1L << T__20) | (1L << T__21))) != 0) || ((((_la - 75)) & ~0x3f) == 0 && ((1L << (_la - 75)) & ((1L << (T__74 - 75)) | (1L << (T__75 - 75)) | (1L << (T__76 - 75)) | (1L << (T__126 - 75)) | (1L << (T__127 - 75)) | (1L << (T__128 - 75)) | (1L << (IDENTIFIER - 75)) | (1L << (QUOTEDIDENTIFIER - 75)))) != 0)) {
				{
				setState(228);
				typeSpecifier();
				}
			}

			setState(233);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(231);
				match(T__8);
				setState(232);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodesystemDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public CodesystemIdContext codesystemId() {
			return getRuleContext(CodesystemIdContext.class,0);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public VersionSpecifierContext versionSpecifier() {
			return getRuleContext(VersionSpecifierContext.class,0);
		}
		public CodesystemDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codesystemDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodesystemDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodesystemDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCodesystemDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodesystemDefinitionContext codesystemDefinition() throws RecognitionException {
		CodesystemDefinitionContext _localctx = new CodesystemDefinitionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_codesystemDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(235);
				accessModifier();
				}
			}

			setState(238);
			match(T__9);
			setState(239);
			identifier();
			setState(240);
			match(T__10);
			setState(241);
			codesystemId();
			setState(244);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(242);
				match(T__1);
				setState(243);
				versionSpecifier();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValuesetDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ValuesetIdContext valuesetId() {
			return getRuleContext(ValuesetIdContext.class,0);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public VersionSpecifierContext versionSpecifier() {
			return getRuleContext(VersionSpecifierContext.class,0);
		}
		public CodesystemsContext codesystems() {
			return getRuleContext(CodesystemsContext.class,0);
		}
		public ValuesetDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valuesetDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterValuesetDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitValuesetDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitValuesetDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValuesetDefinitionContext valuesetDefinition() throws RecognitionException {
		ValuesetDefinitionContext _localctx = new ValuesetDefinitionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_valuesetDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(246);
				accessModifier();
				}
			}

			setState(249);
			match(T__11);
			setState(250);
			identifier();
			setState(251);
			match(T__10);
			setState(252);
			valuesetId();
			setState(255);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(253);
				match(T__1);
				setState(254);
				versionSpecifier();
				}
			}

			setState(258);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(257);
				codesystems();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodesystemsContext extends ParserRuleContext {
		public List<CodesystemIdentifierContext> codesystemIdentifier() {
			return getRuleContexts(CodesystemIdentifierContext.class);
		}
		public CodesystemIdentifierContext codesystemIdentifier(int i) {
			return getRuleContext(CodesystemIdentifierContext.class,i);
		}
		public CodesystemsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codesystems; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodesystems(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodesystems(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCodesystems(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodesystemsContext codesystems() throws RecognitionException {
		CodesystemsContext _localctx = new CodesystemsContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_codesystems);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			match(T__12);
			setState(261);
			match(T__13);
			setState(262);
			codesystemIdentifier();
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(263);
				match(T__14);
				setState(264);
				codesystemIdentifier();
				}
				}
				setState(269);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(270);
			match(T__15);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodesystemIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public LibraryIdentifierContext libraryIdentifier() {
			return getRuleContext(LibraryIdentifierContext.class,0);
		}
		public CodesystemIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codesystemIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodesystemIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodesystemIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCodesystemIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodesystemIdentifierContext codesystemIdentifier() throws RecognitionException {
		CodesystemIdentifierContext _localctx = new CodesystemIdentifierContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_codesystemIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(275);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				setState(272);
				libraryIdentifier();
				setState(273);
				match(T__16);
				}
				break;
			}
			setState(277);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LibraryIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public LibraryIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_libraryIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterLibraryIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitLibraryIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitLibraryIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryIdentifierContext libraryIdentifier() throws RecognitionException {
		LibraryIdentifierContext _localctx = new LibraryIdentifierContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_libraryIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodesystemIdContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cqlParser.STRING, 0); }
		public CodesystemIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codesystemId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodesystemId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodesystemId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCodesystemId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodesystemIdContext codesystemId() throws RecognitionException {
		CodesystemIdContext _localctx = new CodesystemIdContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_codesystemId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValuesetIdContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cqlParser.STRING, 0); }
		public ValuesetIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valuesetId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterValuesetId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitValuesetId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitValuesetId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValuesetIdContext valuesetId() throws RecognitionException {
		ValuesetIdContext _localctx = new ValuesetIdContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_valuesetId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VersionSpecifierContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cqlParser.STRING, 0); }
		public VersionSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_versionSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterVersionSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitVersionSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitVersionSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VersionSpecifierContext versionSpecifier() throws RecognitionException {
		VersionSpecifierContext _localctx = new VersionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_versionSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeSpecifierContext extends ParserRuleContext {
		public NamedTypeSpecifierContext namedTypeSpecifier() {
			return getRuleContext(NamedTypeSpecifierContext.class,0);
		}
		public ListTypeSpecifierContext listTypeSpecifier() {
			return getRuleContext(ListTypeSpecifierContext.class,0);
		}
		public IntervalTypeSpecifierContext intervalTypeSpecifier() {
			return getRuleContext(IntervalTypeSpecifierContext.class,0);
		}
		public TupleTypeSpecifierContext tupleTypeSpecifier() {
			return getRuleContext(TupleTypeSpecifierContext.class,0);
		}
		public TypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSpecifierContext typeSpecifier() throws RecognitionException {
		TypeSpecifierContext _localctx = new TypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_typeSpecifier);
		try {
			setState(291);
			switch (_input.LA(1)) {
			case T__1:
			case T__74:
			case T__75:
			case T__76:
			case T__126:
			case T__127:
			case T__128:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(287);
				namedTypeSpecifier();
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 2);
				{
				setState(288);
				listTypeSpecifier();
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 3);
				{
				setState(289);
				intervalTypeSpecifier();
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 4);
				{
				setState(290);
				tupleTypeSpecifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NamedTypeSpecifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ModelIdentifierContext modelIdentifier() {
			return getRuleContext(ModelIdentifierContext.class,0);
		}
		public NamedTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterNamedTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitNamedTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitNamedTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedTypeSpecifierContext namedTypeSpecifier() throws RecognitionException {
		NamedTypeSpecifierContext _localctx = new NamedTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_namedTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(293);
				modelIdentifier();
				setState(294);
				match(T__16);
				}
				break;
			}
			setState(298);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModelIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ModelIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterModelIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitModelIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitModelIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelIdentifierContext modelIdentifier() throws RecognitionException {
		ModelIdentifierContext _localctx = new ModelIdentifierContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_modelIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListTypeSpecifierContext extends ParserRuleContext {
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public ListTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterListTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitListTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitListTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListTypeSpecifierContext listTypeSpecifier() throws RecognitionException {
		ListTypeSpecifierContext _localctx = new ListTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_listTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(T__17);
			setState(303);
			match(T__18);
			setState(304);
			typeSpecifier();
			setState(305);
			match(T__19);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntervalTypeSpecifierContext extends ParserRuleContext {
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public IntervalTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intervalTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIntervalTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIntervalTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIntervalTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalTypeSpecifierContext intervalTypeSpecifier() throws RecognitionException {
		IntervalTypeSpecifierContext _localctx = new IntervalTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_intervalTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			match(T__20);
			setState(308);
			match(T__18);
			setState(309);
			typeSpecifier();
			setState(310);
			match(T__19);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TupleTypeSpecifierContext extends ParserRuleContext {
		public List<TupleElementDefinitionContext> tupleElementDefinition() {
			return getRuleContexts(TupleElementDefinitionContext.class);
		}
		public TupleElementDefinitionContext tupleElementDefinition(int i) {
			return getRuleContext(TupleElementDefinitionContext.class,i);
		}
		public TupleTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTupleTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTupleTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTupleTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleTypeSpecifierContext tupleTypeSpecifier() throws RecognitionException {
		TupleTypeSpecifierContext _localctx = new TupleTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_tupleTypeSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			match(T__21);
			setState(313);
			match(T__22);
			setState(314);
			tupleElementDefinition();
			setState(319);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(315);
				match(T__14);
				setState(316);
				tupleElementDefinition();
				}
				}
				setState(321);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(322);
			match(T__23);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TupleElementDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public TupleElementDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleElementDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTupleElementDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTupleElementDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTupleElementDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleElementDefinitionContext tupleElementDefinition() throws RecognitionException {
		TupleElementDefinitionContext _localctx = new TupleElementDefinitionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_tupleElementDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			identifier();
			setState(325);
			typeSpecifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public ExpressionDefinitionContext expressionDefinition() {
			return getRuleContext(ExpressionDefinitionContext.class,0);
		}
		public ContextDefinitionContext contextDefinition() {
			return getRuleContext(ContextDefinitionContext.class,0);
		}
		public FunctionDefinitionContext functionDefinition() {
			return getRuleContext(FunctionDefinitionContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_statement);
		try {
			setState(330);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(327);
				expressionDefinition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(328);
				contextDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(329);
				functionDefinition();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public ExpressionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterExpressionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitExpressionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitExpressionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionDefinitionContext expressionDefinition() throws RecognitionException {
		ExpressionDefinitionContext _localctx = new ExpressionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_expressionDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(T__24);
			setState(334);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(333);
				accessModifier();
				}
			}

			setState(336);
			identifier();
			setState(337);
			match(T__10);
			setState(338);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContextDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ContextDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterContextDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitContextDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitContextDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextDefinitionContext contextDefinition() throws RecognitionException {
		ContextDefinitionContext _localctx = new ContextDefinitionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_contextDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			match(T__25);
			setState(341);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public List<OperandDefinitionContext> operandDefinition() {
			return getRuleContexts(OperandDefinitionContext.class);
		}
		public OperandDefinitionContext operandDefinition(int i) {
			return getRuleContext(OperandDefinitionContext.class,i);
		}
		public FunctionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterFunctionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitFunctionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitFunctionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDefinitionContext functionDefinition() throws RecognitionException {
		FunctionDefinitionContext _localctx = new FunctionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_functionDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			match(T__24);
			setState(345);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(344);
				accessModifier();
				}
			}

			setState(347);
			match(T__26);
			setState(348);
			identifier();
			setState(349);
			match(T__13);
			setState(358);
			_la = _input.LA(1);
			if (_la==T__1 || ((((_la - 75)) & ~0x3f) == 0 && ((1L << (_la - 75)) & ((1L << (T__74 - 75)) | (1L << (T__75 - 75)) | (1L << (T__76 - 75)) | (1L << (T__126 - 75)) | (1L << (T__127 - 75)) | (1L << (T__128 - 75)) | (1L << (IDENTIFIER - 75)) | (1L << (QUOTEDIDENTIFIER - 75)))) != 0)) {
				{
				setState(350);
				operandDefinition();
				setState(355);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14) {
					{
					{
					setState(351);
					match(T__14);
					setState(352);
					operandDefinition();
					}
					}
					setState(357);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(360);
			match(T__15);
			setState(361);
			match(T__10);
			setState(362);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperandDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public OperandDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operandDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterOperandDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitOperandDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitOperandDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandDefinitionContext operandDefinition() throws RecognitionException {
		OperandDefinitionContext _localctx = new OperandDefinitionContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_operandDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			identifier();
			setState(365);
			typeSpecifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionBodyContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterFunctionBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitFunctionBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitFunctionBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_functionBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuerySourceContext extends ParserRuleContext {
		public RetrieveContext retrieve() {
			return getRuleContext(RetrieveContext.class,0);
		}
		public QualifiedIdentifierContext qualifiedIdentifier() {
			return getRuleContext(QualifiedIdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public QuerySourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_querySource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterQuerySource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitQuerySource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitQuerySource(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuerySourceContext querySource() throws RecognitionException {
		QuerySourceContext _localctx = new QuerySourceContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_querySource);
		try {
			setState(375);
			switch (_input.LA(1)) {
			case T__30:
				enterOuterAlt(_localctx, 1);
				{
				setState(369);
				retrieve();
				}
				break;
			case T__1:
			case T__74:
			case T__75:
			case T__76:
			case T__126:
			case T__127:
			case T__128:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(370);
				qualifiedIdentifier();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 3);
				{
				setState(371);
				match(T__13);
				setState(372);
				expression(0);
				setState(373);
				match(T__15);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasedQuerySourceContext extends ParserRuleContext {
		public QuerySourceContext querySource() {
			return getRuleContext(QuerySourceContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public AliasedQuerySourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aliasedQuerySource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterAliasedQuerySource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitAliasedQuerySource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitAliasedQuerySource(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasedQuerySourceContext aliasedQuerySource() throws RecognitionException {
		AliasedQuerySourceContext _localctx = new AliasedQuerySourceContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_aliasedQuerySource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(377);
			querySource();
			setState(378);
			alias();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryInclusionClauseContext extends ParserRuleContext {
		public WithClauseContext withClause() {
			return getRuleContext(WithClauseContext.class,0);
		}
		public WithoutClauseContext withoutClause() {
			return getRuleContext(WithoutClauseContext.class,0);
		}
		public QueryInclusionClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryInclusionClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterQueryInclusionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitQueryInclusionClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitQueryInclusionClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryInclusionClauseContext queryInclusionClause() throws RecognitionException {
		QueryInclusionClauseContext _localctx = new QueryInclusionClauseContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_queryInclusionClause);
		try {
			setState(384);
			switch (_input.LA(1)) {
			case T__27:
				enterOuterAlt(_localctx, 1);
				{
				setState(382);
				withClause();
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 2);
				{
				setState(383);
				withoutClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WithClauseContext extends ParserRuleContext {
		public AliasedQuerySourceContext aliasedQuerySource() {
			return getRuleContext(AliasedQuerySourceContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public WithClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_withClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterWithClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitWithClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitWithClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WithClauseContext withClause() throws RecognitionException {
		WithClauseContext _localctx = new WithClauseContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_withClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(386);
			match(T__27);
			setState(387);
			aliasedQuerySource();
			setState(388);
			match(T__28);
			setState(389);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WithoutClauseContext extends ParserRuleContext {
		public AliasedQuerySourceContext aliasedQuerySource() {
			return getRuleContext(AliasedQuerySourceContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public WithoutClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_withoutClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterWithoutClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitWithoutClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitWithoutClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WithoutClauseContext withoutClause() throws RecognitionException {
		WithoutClauseContext _localctx = new WithoutClauseContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_withoutClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
			match(T__29);
			setState(392);
			aliasedQuerySource();
			setState(393);
			match(T__28);
			setState(394);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RetrieveContext extends ParserRuleContext {
		public NamedTypeSpecifierContext namedTypeSpecifier() {
			return getRuleContext(NamedTypeSpecifierContext.class,0);
		}
		public ValuesetContext valueset() {
			return getRuleContext(ValuesetContext.class,0);
		}
		public ValuesetPathIdentifierContext valuesetPathIdentifier() {
			return getRuleContext(ValuesetPathIdentifierContext.class,0);
		}
		public RetrieveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_retrieve; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterRetrieve(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitRetrieve(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitRetrieve(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RetrieveContext retrieve() throws RecognitionException {
		RetrieveContext _localctx = new RetrieveContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_retrieve);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
			match(T__30);
			setState(397);
			namedTypeSpecifier();
			setState(405);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(398);
				match(T__10);
				setState(402);
				switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
				case 1:
					{
					setState(399);
					valuesetPathIdentifier();
					setState(400);
					match(T__31);
					}
					break;
				}
				setState(404);
				valueset();
				}
			}

			setState(407);
			match(T__32);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValuesetPathIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ValuesetPathIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valuesetPathIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterValuesetPathIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitValuesetPathIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitValuesetPathIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValuesetPathIdentifierContext valuesetPathIdentifier() throws RecognitionException {
		ValuesetPathIdentifierContext _localctx = new ValuesetPathIdentifierContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_valuesetPathIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValuesetContext extends ParserRuleContext {
		public QualifiedIdentifierContext qualifiedIdentifier() {
			return getRuleContext(QualifiedIdentifierContext.class,0);
		}
		public ValuesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterValueset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitValueset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitValueset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValuesetContext valueset() throws RecognitionException {
		ValuesetContext _localctx = new ValuesetContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_valueset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			qualifiedIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public QualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterQualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitQualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifierContext qualifier() throws RecognitionException {
		QualifierContext _localctx = new QualifierContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_qualifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryContext extends ParserRuleContext {
		public SourceClauseContext sourceClause() {
			return getRuleContext(SourceClauseContext.class,0);
		}
		public DefineClauseContext defineClause() {
			return getRuleContext(DefineClauseContext.class,0);
		}
		public List<QueryInclusionClauseContext> queryInclusionClause() {
			return getRuleContexts(QueryInclusionClauseContext.class);
		}
		public QueryInclusionClauseContext queryInclusionClause(int i) {
			return getRuleContext(QueryInclusionClauseContext.class,i);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public ReturnClauseContext returnClause() {
			return getRuleContext(ReturnClauseContext.class,0);
		}
		public SortClauseContext sortClause() {
			return getRuleContext(SortClauseContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_query);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(415);
			sourceClause();
			setState(417);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(416);
				defineClause();
				}
				break;
			}
			setState(422);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(419);
					queryInclusionClause();
					}
					} 
				}
				setState(424);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
			}
			setState(426);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(425);
				whereClause();
				}
				break;
			}
			setState(429);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(428);
				returnClause();
				}
				break;
			}
			setState(432);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(431);
				sortClause();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SourceClauseContext extends ParserRuleContext {
		public SingleSourceClauseContext singleSourceClause() {
			return getRuleContext(SingleSourceClauseContext.class,0);
		}
		public MultipleSourceClauseContext multipleSourceClause() {
			return getRuleContext(MultipleSourceClauseContext.class,0);
		}
		public SourceClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterSourceClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitSourceClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitSourceClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceClauseContext sourceClause() throws RecognitionException {
		SourceClauseContext _localctx = new SourceClauseContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_sourceClause);
		try {
			setState(436);
			switch (_input.LA(1)) {
			case T__1:
			case T__13:
			case T__30:
			case T__74:
			case T__75:
			case T__76:
			case T__126:
			case T__127:
			case T__128:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(434);
				singleSourceClause();
				}
				break;
			case T__33:
				enterOuterAlt(_localctx, 2);
				{
				setState(435);
				multipleSourceClause();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SingleSourceClauseContext extends ParserRuleContext {
		public AliasedQuerySourceContext aliasedQuerySource() {
			return getRuleContext(AliasedQuerySourceContext.class,0);
		}
		public SingleSourceClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleSourceClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterSingleSourceClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitSingleSourceClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitSingleSourceClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleSourceClauseContext singleSourceClause() throws RecognitionException {
		SingleSourceClauseContext _localctx = new SingleSourceClauseContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_singleSourceClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
			aliasedQuerySource();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultipleSourceClauseContext extends ParserRuleContext {
		public List<AliasedQuerySourceContext> aliasedQuerySource() {
			return getRuleContexts(AliasedQuerySourceContext.class);
		}
		public AliasedQuerySourceContext aliasedQuerySource(int i) {
			return getRuleContext(AliasedQuerySourceContext.class,i);
		}
		public MultipleSourceClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multipleSourceClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterMultipleSourceClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitMultipleSourceClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitMultipleSourceClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultipleSourceClauseContext multipleSourceClause() throws RecognitionException {
		MultipleSourceClauseContext _localctx = new MultipleSourceClauseContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_multipleSourceClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(440);
			match(T__33);
			setState(441);
			aliasedQuerySource();
			setState(446);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(442);
					match(T__14);
					setState(443);
					aliasedQuerySource();
					}
					} 
				}
				setState(448);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefineClauseContext extends ParserRuleContext {
		public List<DefineClauseItemContext> defineClauseItem() {
			return getRuleContexts(DefineClauseItemContext.class);
		}
		public DefineClauseItemContext defineClauseItem(int i) {
			return getRuleContext(DefineClauseItemContext.class,i);
		}
		public DefineClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defineClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDefineClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDefineClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDefineClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefineClauseContext defineClause() throws RecognitionException {
		DefineClauseContext _localctx = new DefineClauseContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_defineClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(449);
			match(T__24);
			setState(450);
			defineClauseItem();
			setState(455);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(451);
					match(T__14);
					setState(452);
					defineClauseItem();
					}
					} 
				}
				setState(457);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefineClauseItemContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DefineClauseItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defineClauseItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDefineClauseItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDefineClauseItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDefineClauseItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefineClauseItemContext defineClauseItem() throws RecognitionException {
		DefineClauseItemContext _localctx = new DefineClauseItemContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_defineClauseItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
			identifier();
			setState(459);
			match(T__10);
			setState(460);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhereClauseContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitWhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(462);
			match(T__34);
			setState(463);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnClauseContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterReturnClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitReturnClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitReturnClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnClauseContext returnClause() throws RecognitionException {
		ReturnClauseContext _localctx = new ReturnClauseContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_returnClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
			match(T__35);
			setState(467);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(466);
				_la = _input.LA(1);
				if ( !(_la==T__36 || _la==T__37) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			}
			setState(469);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SortClauseContext extends ParserRuleContext {
		public SortDirectionContext sortDirection() {
			return getRuleContext(SortDirectionContext.class,0);
		}
		public List<SortByItemContext> sortByItem() {
			return getRuleContexts(SortByItemContext.class);
		}
		public SortByItemContext sortByItem(int i) {
			return getRuleContext(SortByItemContext.class,i);
		}
		public SortClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterSortClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitSortClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitSortClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortClauseContext sortClause() throws RecognitionException {
		SortClauseContext _localctx = new SortClauseContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_sortClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
			match(T__38);
			setState(482);
			switch (_input.LA(1)) {
			case T__40:
			case T__41:
			case T__42:
			case T__43:
				{
				setState(472);
				sortDirection();
				}
				break;
			case T__39:
				{
				{
				setState(473);
				match(T__39);
				setState(474);
				sortByItem();
				setState(479);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(475);
						match(T__14);
						setState(476);
						sortByItem();
						}
						} 
					}
					setState(481);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
				}
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SortDirectionContext extends ParserRuleContext {
		public SortDirectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortDirection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterSortDirection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitSortDirection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitSortDirection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortDirectionContext sortDirection() throws RecognitionException {
		SortDirectionContext _localctx = new SortDirectionContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_sortDirection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SortByItemContext extends ParserRuleContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public SortDirectionContext sortDirection() {
			return getRuleContext(SortDirectionContext.class,0);
		}
		public SortByItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortByItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterSortByItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitSortByItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitSortByItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortByItemContext sortByItem() throws RecognitionException {
		SortByItemContext _localctx = new SortByItemContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_sortByItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486);
			expressionTerm(0);
			setState(488);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(487);
				sortDirection();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<QualifierContext> qualifier() {
			return getRuleContexts(QualifierContext.class);
		}
		public QualifierContext qualifier(int i) {
			return getRuleContext(QualifierContext.class,i);
		}
		public QualifiedIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterQualifiedIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitQualifiedIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitQualifiedIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedIdentifierContext qualifiedIdentifier() throws RecognitionException {
		QualifiedIdentifierContext _localctx = new QualifiedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_qualifiedIdentifier);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(495);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(490);
					qualifier();
					setState(491);
					match(T__16);
					}
					} 
				}
				setState(497);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			}
			setState(498);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DurationBetweenExpressionContext extends ExpressionContext {
		public PluralDateTimePrecisionContext pluralDateTimePrecision() {
			return getRuleContext(PluralDateTimePrecisionContext.class,0);
		}
		public List<ExpressionTermContext> expressionTerm() {
			return getRuleContexts(ExpressionTermContext.class);
		}
		public ExpressionTermContext expressionTerm(int i) {
			return getRuleContext(ExpressionTermContext.class,i);
		}
		public DurationBetweenExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDurationBetweenExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDurationBetweenExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDurationBetweenExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InFixSetExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InFixSetExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterInFixSetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitInFixSetExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitInFixSetExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RetrieveExpressionContext extends ExpressionContext {
		public RetrieveContext retrieve() {
			return getRuleContext(RetrieveContext.class,0);
		}
		public RetrieveExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterRetrieveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitRetrieveExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitRetrieveExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TimingExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public IntervalOperatorPhraseContext intervalOperatorPhrase() {
			return getRuleContext(IntervalOperatorPhraseContext.class,0);
		}
		public TimingExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTimingExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTimingExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTimingExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NotExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterNotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitNotExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitNotExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class QueryExpressionContext extends ExpressionContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public QueryExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterQueryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitQueryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitQueryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BooleanExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterBooleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitBooleanExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitBooleanExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public OrExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CastExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public CastExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCastExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCastExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCastExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AndExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BetweenExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<ExpressionTermContext> expressionTerm() {
			return getRuleContexts(ExpressionTermContext.class);
		}
		public ExpressionTermContext expressionTerm(int i) {
			return getRuleContext(ExpressionTermContext.class,i);
		}
		public BetweenExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterBetweenExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitBetweenExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitBetweenExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MembershipExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() {
			return getRuleContext(DateTimePrecisionSpecifierContext.class,0);
		}
		public MembershipExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterMembershipExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitMembershipExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitMembershipExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DifferenceBetweenExpressionContext extends ExpressionContext {
		public PluralDateTimePrecisionContext pluralDateTimePrecision() {
			return getRuleContext(PluralDateTimePrecisionContext.class,0);
		}
		public List<ExpressionTermContext> expressionTerm() {
			return getRuleContexts(ExpressionTermContext.class);
		}
		public ExpressionTermContext expressionTerm(int i) {
			return getRuleContext(ExpressionTermContext.class,i);
		}
		public DifferenceBetweenExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDifferenceBetweenExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDifferenceBetweenExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDifferenceBetweenExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InequalityExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InequalityExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterInequalityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitInequalityExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitInequalityExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqualityExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public EqualityExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterEqualityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitEqualityExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitEqualityExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExistenceExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExistenceExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterExistenceExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitExistenceExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitExistenceExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TermExpressionContext extends ExpressionContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public TermExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTermExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTermExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTermExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeExpressionContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public TypeExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTypeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTypeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTypeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 100;
		enterRecursionRule(_localctx, 100, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(527);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				_localctx = new NotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(501);
				match(T__45);
				setState(502);
				expression(12);
				}
				break;
			case 2:
				{
				_localctx = new ExistenceExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(503);
				match(T__51);
				setState(504);
				expression(11);
				}
				break;
			case 3:
				{
				_localctx = new TermExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(505);
				expressionTerm(0);
				}
				break;
			case 4:
				{
				_localctx = new RetrieveExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(506);
				retrieve();
				}
				break;
			case 5:
				{
				_localctx = new QueryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(507);
				query();
				}
				break;
			case 6:
				{
				_localctx = new CastExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(508);
				match(T__50);
				setState(509);
				expression(0);
				setState(510);
				match(T__49);
				setState(511);
				typeSpecifier();
				}
				break;
			case 7:
				{
				_localctx = new DurationBetweenExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(513);
				pluralDateTimePrecision();
				setState(514);
				match(T__53);
				setState(515);
				expressionTerm(0);
				setState(516);
				match(T__54);
				setState(517);
				expressionTerm(0);
				}
				break;
			case 8:
				{
				_localctx = new DifferenceBetweenExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(519);
				match(T__55);
				setState(520);
				match(T__31);
				setState(521);
				pluralDateTimePrecision();
				setState(522);
				match(T__53);
				setState(523);
				expressionTerm(0);
				setState(524);
				match(T__54);
				setState(525);
				expressionTerm(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(574);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(572);
					switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
					case 1:
						{
						_localctx = new InequalityExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(529);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(530);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__18) | (1L << T__19) | (1L << T__56) | (1L << T__57))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(531);
						expression(8);
						}
						break;
					case 2:
						{
						_localctx = new TimingExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(532);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(533);
						intervalOperatorPhrase();
						setState(534);
						expression(7);
						}
						break;
					case 3:
						{
						_localctx = new EqualityExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(536);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(537);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__58) | (1L << T__59) | (1L << T__60))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(538);
						expression(6);
						}
						break;
					case 4:
						{
						_localctx = new MembershipExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(539);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(540);
						_la = _input.LA(1);
						if ( !(_la==T__31 || _la==T__61) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(542);
						switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
						case 1:
							{
							setState(541);
							dateTimePrecisionSpecifier();
							}
							break;
						}
						setState(544);
						expression(5);
						}
						break;
					case 5:
						{
						_localctx = new AndExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(545);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(546);
						match(T__54);
						setState(547);
						expression(4);
						}
						break;
					case 6:
						{
						_localctx = new OrExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(548);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(549);
						_la = _input.LA(1);
						if ( !(_la==T__62 || _la==T__63) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(550);
						expression(3);
						}
						break;
					case 7:
						{
						_localctx = new InFixSetExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(551);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(552);
						_la = _input.LA(1);
						if ( !(((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__66 - 65)))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(553);
						expression(2);
						}
						break;
					case 8:
						{
						_localctx = new BooleanExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(554);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(555);
						match(T__44);
						setState(557);
						_la = _input.LA(1);
						if (_la==T__45) {
							{
							setState(556);
							match(T__45);
							}
						}

						setState(559);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__46) | (1L << T__47) | (1L << T__48))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						}
						break;
					case 9:
						{
						_localctx = new TypeExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(560);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(561);
						_la = _input.LA(1);
						if ( !(_la==T__44 || _la==T__49) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(562);
						typeSpecifier();
						}
						break;
					case 10:
						{
						_localctx = new BetweenExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(563);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(565);
						_la = _input.LA(1);
						if (_la==T__52) {
							{
							setState(564);
							match(T__52);
							}
						}

						setState(567);
						match(T__53);
						setState(568);
						expressionTerm(0);
						setState(569);
						match(T__54);
						setState(570);
						expressionTerm(0);
						}
						break;
					}
					} 
				}
				setState(576);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class DateTimePrecisionContext extends ParserRuleContext {
		public DateTimePrecisionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateTimePrecision; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDateTimePrecision(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDateTimePrecision(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDateTimePrecision(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateTimePrecisionContext dateTimePrecision() throws RecognitionException {
		DateTimePrecisionContext _localctx = new DateTimePrecisionContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_dateTimePrecision);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(577);
			_la = _input.LA(1);
			if ( !(((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (T__67 - 68)) | (1L << (T__68 - 68)) | (1L << (T__69 - 68)) | (1L << (T__70 - 68)) | (1L << (T__71 - 68)) | (1L << (T__72 - 68)) | (1L << (T__73 - 68)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateTimeComponentContext extends ParserRuleContext {
		public DateTimePrecisionContext dateTimePrecision() {
			return getRuleContext(DateTimePrecisionContext.class,0);
		}
		public DateTimeComponentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateTimeComponent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDateTimeComponent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDateTimeComponent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDateTimeComponent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateTimeComponentContext dateTimeComponent() throws RecognitionException {
		DateTimeComponentContext _localctx = new DateTimeComponentContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_dateTimeComponent);
		try {
			setState(583);
			switch (_input.LA(1)) {
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case T__72:
			case T__73:
				enterOuterAlt(_localctx, 1);
				{
				setState(579);
				dateTimePrecision();
				}
				break;
			case T__74:
				enterOuterAlt(_localctx, 2);
				{
				setState(580);
				match(T__74);
				}
				break;
			case T__75:
				enterOuterAlt(_localctx, 3);
				{
				setState(581);
				match(T__75);
				}
				break;
			case T__76:
				enterOuterAlt(_localctx, 4);
				{
				setState(582);
				match(T__76);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PluralDateTimePrecisionContext extends ParserRuleContext {
		public PluralDateTimePrecisionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pluralDateTimePrecision; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterPluralDateTimePrecision(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitPluralDateTimePrecision(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitPluralDateTimePrecision(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PluralDateTimePrecisionContext pluralDateTimePrecision() throws RecognitionException {
		PluralDateTimePrecisionContext _localctx = new PluralDateTimePrecisionContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_pluralDateTimePrecision);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(585);
			_la = _input.LA(1);
			if ( !(((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & ((1L << (T__77 - 78)) | (1L << (T__78 - 78)) | (1L << (T__79 - 78)) | (1L << (T__80 - 78)) | (1L << (T__81 - 78)) | (1L << (T__82 - 78)) | (1L << (T__83 - 78)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionTermContext extends ParserRuleContext {
		public ExpressionTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionTerm; }
	 
		public ExpressionTermContext() { }
		public void copyFrom(ExpressionTermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AdditionExpressionTermContext extends ExpressionTermContext {
		public List<ExpressionTermContext> expressionTerm() {
			return getRuleContexts(ExpressionTermContext.class);
		}
		public ExpressionTermContext expressionTerm(int i) {
			return getRuleContext(ExpressionTermContext.class,i);
		}
		public AdditionExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterAdditionExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitAdditionExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitAdditionExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IndexedExpressionTermContext extends ExpressionTermContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IndexedExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIndexedExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIndexedExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIndexedExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WidthExpressionTermContext extends ExpressionTermContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public WidthExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterWidthExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitWidthExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitWidthExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TimeUnitExpressionTermContext extends ExpressionTermContext {
		public DateTimeComponentContext dateTimeComponent() {
			return getRuleContext(DateTimeComponentContext.class,0);
		}
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public TimeUnitExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTimeUnitExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTimeUnitExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTimeUnitExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfThenElseExpressionTermContext extends ExpressionTermContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public IfThenElseExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIfThenElseExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIfThenElseExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIfThenElseExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TimeBoundaryExpressionTermContext extends ExpressionTermContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public TimeBoundaryExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTimeBoundaryExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTimeBoundaryExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTimeBoundaryExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ElementExtractorExpressionTermContext extends ExpressionTermContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public ElementExtractorExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterElementExtractorExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitElementExtractorExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitElementExtractorExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConversionExpressionTermContext extends ExpressionTermContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public ConversionExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterConversionExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitConversionExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitConversionExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeExtentExpressionTermContext extends ExpressionTermContext {
		public NamedTypeSpecifierContext namedTypeSpecifier() {
			return getRuleContext(NamedTypeSpecifierContext.class,0);
		}
		public TypeExtentExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTypeExtentExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTypeExtentExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTypeExtentExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PredecessorExpressionTermContext extends ExpressionTermContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public PredecessorExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterPredecessorExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitPredecessorExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitPredecessorExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AccessorExpressionTermContext extends ExpressionTermContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public AccessorExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterAccessorExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitAccessorExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitAccessorExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MultiplicationExpressionTermContext extends ExpressionTermContext {
		public List<ExpressionTermContext> expressionTerm() {
			return getRuleContexts(ExpressionTermContext.class);
		}
		public ExpressionTermContext expressionTerm(int i) {
			return getRuleContext(ExpressionTermContext.class,i);
		}
		public MultiplicationExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterMultiplicationExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitMultiplicationExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitMultiplicationExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AggregateExpressionTermContext extends ExpressionTermContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AggregateExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterAggregateExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitAggregateExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitAggregateExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DurationExpressionTermContext extends ExpressionTermContext {
		public PluralDateTimePrecisionContext pluralDateTimePrecision() {
			return getRuleContext(PluralDateTimePrecisionContext.class,0);
		}
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public DurationExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDurationExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDurationExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDurationExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CaseExpressionTermContext extends ExpressionTermContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<CaseExpressionItemContext> caseExpressionItem() {
			return getRuleContexts(CaseExpressionItemContext.class);
		}
		public CaseExpressionItemContext caseExpressionItem(int i) {
			return getRuleContext(CaseExpressionItemContext.class,i);
		}
		public CaseExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCaseExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCaseExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCaseExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PowerExpressionTermContext extends ExpressionTermContext {
		public List<ExpressionTermContext> expressionTerm() {
			return getRuleContexts(ExpressionTermContext.class);
		}
		public ExpressionTermContext expressionTerm(int i) {
			return getRuleContext(ExpressionTermContext.class,i);
		}
		public PowerExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterPowerExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitPowerExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitPowerExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SuccessorExpressionTermContext extends ExpressionTermContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public SuccessorExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterSuccessorExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitSuccessorExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitSuccessorExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PolarityExpressionTermContext extends ExpressionTermContext {
		public ExpressionTermContext expressionTerm() {
			return getRuleContext(ExpressionTermContext.class,0);
		}
		public PolarityExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterPolarityExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitPolarityExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitPolarityExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TermExpressionTermContext extends ExpressionTermContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TermExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTermExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTermExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTermExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InvocationExpressionTermContext extends ExpressionTermContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public QualifierContext qualifier() {
			return getRuleContext(QualifierContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public InvocationExpressionTermContext(ExpressionTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterInvocationExpressionTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitInvocationExpressionTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitInvocationExpressionTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionTermContext expressionTerm() throws RecognitionException {
		return expressionTerm(0);
	}

	private ExpressionTermContext expressionTerm(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionTermContext _localctx = new ExpressionTermContext(_ctx, _parentState);
		ExpressionTermContext _prevctx = _localctx;
		int _startState = 108;
		enterRecursionRule(_localctx, 108, RULE_expressionTerm, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(664);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				{
				_localctx = new PolarityExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(588);
				_la = _input.LA(1);
				if ( !(_la==T__86 || _la==T__87) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(589);
				expressionTerm(15);
				}
				break;
			case 2:
				{
				_localctx = new TimeBoundaryExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(590);
				_la = _input.LA(1);
				if ( !(_la==T__88 || _la==T__89) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(591);
				match(T__90);
				setState(592);
				expressionTerm(14);
				}
				break;
			case 3:
				{
				_localctx = new TimeUnitExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(593);
				dateTimeComponent();
				setState(594);
				match(T__33);
				setState(595);
				expressionTerm(13);
				}
				break;
			case 4:
				{
				_localctx = new DurationExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(597);
				match(T__91);
				setState(598);
				match(T__31);
				setState(599);
				pluralDateTimePrecision();
				setState(600);
				match(T__90);
				setState(601);
				expressionTerm(12);
				}
				break;
			case 5:
				{
				_localctx = new WidthExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(603);
				match(T__92);
				setState(604);
				match(T__90);
				setState(605);
				expressionTerm(11);
				}
				break;
			case 6:
				{
				_localctx = new SuccessorExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(606);
				match(T__93);
				setState(607);
				match(T__90);
				setState(608);
				expressionTerm(10);
				}
				break;
			case 7:
				{
				_localctx = new PredecessorExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(609);
				match(T__94);
				setState(610);
				match(T__90);
				setState(611);
				expressionTerm(9);
				}
				break;
			case 8:
				{
				_localctx = new ElementExtractorExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(612);
				match(T__95);
				setState(613);
				match(T__33);
				setState(614);
				expressionTerm(8);
				}
				break;
			case 9:
				{
				_localctx = new TermExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(615);
				term();
				}
				break;
			case 10:
				{
				_localctx = new InvocationExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(619);
				switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
				case 1:
					{
					setState(616);
					qualifier();
					setState(617);
					match(T__16);
					}
					break;
				}
				setState(621);
				identifier();
				setState(622);
				match(T__13);
				setState(631);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__13) | (1L << T__17) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__30) | (1L << T__33) | (1L << T__37) | (1L << T__45) | (1L << T__46) | (1L << T__47) | (1L << T__48) | (1L << T__50) | (1L << T__51) | (1L << T__55))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (T__67 - 68)) | (1L << (T__68 - 68)) | (1L << (T__69 - 68)) | (1L << (T__70 - 68)) | (1L << (T__71 - 68)) | (1L << (T__72 - 68)) | (1L << (T__73 - 68)) | (1L << (T__74 - 68)) | (1L << (T__75 - 68)) | (1L << (T__76 - 68)) | (1L << (T__77 - 68)) | (1L << (T__78 - 68)) | (1L << (T__79 - 68)) | (1L << (T__80 - 68)) | (1L << (T__81 - 68)) | (1L << (T__82 - 68)) | (1L << (T__83 - 68)) | (1L << (T__84 - 68)) | (1L << (T__86 - 68)) | (1L << (T__87 - 68)) | (1L << (T__88 - 68)) | (1L << (T__89 - 68)) | (1L << (T__91 - 68)) | (1L << (T__92 - 68)) | (1L << (T__93 - 68)) | (1L << (T__94 - 68)) | (1L << (T__95 - 68)) | (1L << (T__96 - 68)) | (1L << (T__97 - 68)) | (1L << (T__103 - 68)) | (1L << (T__106 - 68)) | (1L << (T__107 - 68)) | (1L << (T__108 - 68)) | (1L << (T__126 - 68)) | (1L << (T__127 - 68)) | (1L << (T__128 - 68)) | (1L << (IDENTIFIER - 68)) | (1L << (QUANTITY - 68)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (DATETIME - 132)) | (1L << (TIME - 132)) | (1L << (QUOTEDIDENTIFIER - 132)) | (1L << (STRING - 132)))) != 0)) {
					{
					setState(623);
					expression(0);
					setState(628);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__14) {
						{
						{
						setState(624);
						match(T__14);
						setState(625);
						expression(0);
						}
						}
						setState(630);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(633);
				match(T__15);
				}
				break;
			case 11:
				{
				_localctx = new ConversionExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(635);
				match(T__84);
				setState(636);
				expression(0);
				setState(637);
				match(T__85);
				setState(638);
				typeSpecifier();
				}
				break;
			case 12:
				{
				_localctx = new TypeExtentExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(640);
				_la = _input.LA(1);
				if ( !(_la==T__96 || _la==T__97) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(641);
				namedTypeSpecifier();
				}
				break;
			case 13:
				{
				_localctx = new IfThenElseExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(642);
				match(T__103);
				setState(643);
				expression(0);
				setState(644);
				match(T__104);
				setState(645);
				expression(0);
				setState(646);
				match(T__105);
				setState(647);
				expression(0);
				}
				break;
			case 14:
				{
				_localctx = new CaseExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(649);
				match(T__106);
				setState(651);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__13) | (1L << T__17) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__30) | (1L << T__33) | (1L << T__37) | (1L << T__45) | (1L << T__46) | (1L << T__47) | (1L << T__48) | (1L << T__50) | (1L << T__51) | (1L << T__55))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (T__67 - 68)) | (1L << (T__68 - 68)) | (1L << (T__69 - 68)) | (1L << (T__70 - 68)) | (1L << (T__71 - 68)) | (1L << (T__72 - 68)) | (1L << (T__73 - 68)) | (1L << (T__74 - 68)) | (1L << (T__75 - 68)) | (1L << (T__76 - 68)) | (1L << (T__77 - 68)) | (1L << (T__78 - 68)) | (1L << (T__79 - 68)) | (1L << (T__80 - 68)) | (1L << (T__81 - 68)) | (1L << (T__82 - 68)) | (1L << (T__83 - 68)) | (1L << (T__84 - 68)) | (1L << (T__86 - 68)) | (1L << (T__87 - 68)) | (1L << (T__88 - 68)) | (1L << (T__89 - 68)) | (1L << (T__91 - 68)) | (1L << (T__92 - 68)) | (1L << (T__93 - 68)) | (1L << (T__94 - 68)) | (1L << (T__95 - 68)) | (1L << (T__96 - 68)) | (1L << (T__97 - 68)) | (1L << (T__103 - 68)) | (1L << (T__106 - 68)) | (1L << (T__107 - 68)) | (1L << (T__108 - 68)) | (1L << (T__126 - 68)) | (1L << (T__127 - 68)) | (1L << (T__128 - 68)) | (1L << (IDENTIFIER - 68)) | (1L << (QUANTITY - 68)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (DATETIME - 132)) | (1L << (TIME - 132)) | (1L << (QUOTEDIDENTIFIER - 132)) | (1L << (STRING - 132)))) != 0)) {
					{
					setState(650);
					expression(0);
					}
				}

				setState(654); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(653);
					caseExpressionItem();
					}
					}
					setState(656); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__109 );
				setState(658);
				match(T__105);
				setState(659);
				expression(0);
				setState(660);
				match(T__89);
				}
				break;
			case 15:
				{
				_localctx = new AggregateExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(662);
				_la = _input.LA(1);
				if ( !(_la==T__37 || _la==T__107 || _la==T__108) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(663);
				expression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(685);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,59,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(683);
					switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
					case 1:
						{
						_localctx = new PowerExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(666);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(667);
						match(T__98);
						setState(668);
						expressionTerm(7);
						}
						break;
					case 2:
						{
						_localctx = new MultiplicationExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(669);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(670);
						_la = _input.LA(1);
						if ( !(((((_la - 100)) & ~0x3f) == 0 && ((1L << (_la - 100)) & ((1L << (T__99 - 100)) | (1L << (T__100 - 100)) | (1L << (T__101 - 100)) | (1L << (T__102 - 100)))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(671);
						expressionTerm(6);
						}
						break;
					case 3:
						{
						_localctx = new AdditionExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(672);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(673);
						_la = _input.LA(1);
						if ( !(_la==T__86 || _la==T__87) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(674);
						expressionTerm(5);
						}
						break;
					case 4:
						{
						_localctx = new AccessorExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(675);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(676);
						match(T__16);
						setState(677);
						identifier();
						}
						break;
					case 5:
						{
						_localctx = new IndexedExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(678);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(679);
						match(T__30);
						setState(680);
						expression(0);
						setState(681);
						match(T__32);
						}
						break;
					}
					} 
				}
				setState(687);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,59,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CaseExpressionItemContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CaseExpressionItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseExpressionItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCaseExpressionItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCaseExpressionItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCaseExpressionItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseExpressionItemContext caseExpressionItem() throws RecognitionException {
		CaseExpressionItemContext _localctx = new CaseExpressionItemContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_caseExpressionItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(688);
			match(T__109);
			setState(689);
			expression(0);
			setState(690);
			match(T__104);
			setState(691);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateTimePrecisionSpecifierContext extends ParserRuleContext {
		public DateTimePrecisionContext dateTimePrecision() {
			return getRuleContext(DateTimePrecisionContext.class,0);
		}
		public DateTimePrecisionSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateTimePrecisionSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDateTimePrecisionSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDateTimePrecisionSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDateTimePrecisionSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() throws RecognitionException {
		DateTimePrecisionSpecifierContext _localctx = new DateTimePrecisionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_dateTimePrecisionSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(693);
			dateTimePrecision();
			setState(694);
			match(T__90);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelativeQualifierContext extends ParserRuleContext {
		public RelativeQualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relativeQualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterRelativeQualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitRelativeQualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitRelativeQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelativeQualifierContext relativeQualifier() throws RecognitionException {
		RelativeQualifierContext _localctx = new RelativeQualifierContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_relativeQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(696);
			_la = _input.LA(1);
			if ( !(_la==T__110 || _la==T__111) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OffsetRelativeQualifierContext extends ParserRuleContext {
		public OffsetRelativeQualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_offsetRelativeQualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterOffsetRelativeQualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitOffsetRelativeQualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitOffsetRelativeQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OffsetRelativeQualifierContext offsetRelativeQualifier() throws RecognitionException {
		OffsetRelativeQualifierContext _localctx = new OffsetRelativeQualifierContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_offsetRelativeQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(698);
			_la = _input.LA(1);
			if ( !(_la==T__112 || _la==T__113) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuantityOffsetContext extends ParserRuleContext {
		public QuantityLiteralContext quantityLiteral() {
			return getRuleContext(QuantityLiteralContext.class,0);
		}
		public OffsetRelativeQualifierContext offsetRelativeQualifier() {
			return getRuleContext(OffsetRelativeQualifierContext.class,0);
		}
		public QuantityOffsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantityOffset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterQuantityOffset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitQuantityOffset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitQuantityOffset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantityOffsetContext quantityOffset() throws RecognitionException {
		QuantityOffsetContext _localctx = new QuantityOffsetContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_quantityOffset);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(700);
			quantityLiteral();
			setState(702);
			_la = _input.LA(1);
			if (_la==T__112 || _la==T__113) {
				{
				setState(701);
				offsetRelativeQualifier();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntervalOperatorPhraseContext extends ParserRuleContext {
		public IntervalOperatorPhraseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intervalOperatorPhrase; }
	 
		public IntervalOperatorPhraseContext() { }
		public void copyFrom(IntervalOperatorPhraseContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class WithinIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public QuantityLiteralContext quantityLiteral() {
			return getRuleContext(QuantityLiteralContext.class,0);
		}
		public WithinIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterWithinIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitWithinIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitWithinIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IncludedInIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() {
			return getRuleContext(DateTimePrecisionSpecifierContext.class,0);
		}
		public IncludedInIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIncludedInIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIncludedInIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIncludedInIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EndsIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() {
			return getRuleContext(DateTimePrecisionSpecifierContext.class,0);
		}
		public EndsIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterEndsIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitEndsIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitEndsIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConcurrentWithIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public RelativeQualifierContext relativeQualifier() {
			return getRuleContext(RelativeQualifierContext.class,0);
		}
		public DateTimePrecisionContext dateTimePrecision() {
			return getRuleContext(DateTimePrecisionContext.class,0);
		}
		public ConcurrentWithIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterConcurrentWithIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitConcurrentWithIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitConcurrentWithIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OverlapsIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() {
			return getRuleContext(DateTimePrecisionSpecifierContext.class,0);
		}
		public OverlapsIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterOverlapsIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitOverlapsIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitOverlapsIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IncludesIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() {
			return getRuleContext(DateTimePrecisionSpecifierContext.class,0);
		}
		public IncludesIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIncludesIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIncludesIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIncludesIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BeforeOrAfterIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public QuantityOffsetContext quantityOffset() {
			return getRuleContext(QuantityOffsetContext.class,0);
		}
		public DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() {
			return getRuleContext(DateTimePrecisionSpecifierContext.class,0);
		}
		public BeforeOrAfterIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterBeforeOrAfterIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitBeforeOrAfterIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitBeforeOrAfterIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MeetsIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() {
			return getRuleContext(DateTimePrecisionSpecifierContext.class,0);
		}
		public MeetsIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterMeetsIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitMeetsIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitMeetsIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StartsIntervalOperatorPhraseContext extends IntervalOperatorPhraseContext {
		public DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() {
			return getRuleContext(DateTimePrecisionSpecifierContext.class,0);
		}
		public StartsIntervalOperatorPhraseContext(IntervalOperatorPhraseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterStartsIntervalOperatorPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitStartsIntervalOperatorPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitStartsIntervalOperatorPhrase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalOperatorPhraseContext intervalOperatorPhrase() throws RecognitionException {
		IntervalOperatorPhraseContext _localctx = new IntervalOperatorPhraseContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_intervalOperatorPhrase);
		int _la;
		try {
			setState(785);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				_localctx = new ConcurrentWithIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(705);
				_la = _input.LA(1);
				if (((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (T__114 - 115)) | (1L << (T__115 - 115)) | (1L << (T__116 - 115)))) != 0)) {
					{
					setState(704);
					_la = _input.LA(1);
					if ( !(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (T__114 - 115)) | (1L << (T__115 - 115)) | (1L << (T__116 - 115)))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(707);
				match(T__117);
				setState(709);
				_la = _input.LA(1);
				if (((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (T__67 - 68)) | (1L << (T__68 - 68)) | (1L << (T__69 - 68)) | (1L << (T__70 - 68)) | (1L << (T__71 - 68)) | (1L << (T__72 - 68)) | (1L << (T__73 - 68)))) != 0)) {
					{
					setState(708);
					dateTimePrecision();
					}
				}

				setState(713);
				switch (_input.LA(1)) {
				case T__110:
				case T__111:
					{
					setState(711);
					relativeQualifier();
					}
					break;
				case T__49:
					{
					setState(712);
					match(T__49);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(716);
				switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
				case 1:
					{
					setState(715);
					_la = _input.LA(1);
					if ( !(_la==T__88 || _la==T__89) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				}
				break;
			case 2:
				_localctx = new IncludesIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(719);
				_la = _input.LA(1);
				if (_la==T__52) {
					{
					setState(718);
					match(T__52);
					}
				}

				setState(721);
				match(T__118);
				setState(723);
				switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
				case 1:
					{
					setState(722);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				setState(726);
				switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
				case 1:
					{
					setState(725);
					_la = _input.LA(1);
					if ( !(_la==T__88 || _la==T__89) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				}
				break;
			case 3:
				_localctx = new IncludedInIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(729);
				_la = _input.LA(1);
				if (((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (T__114 - 115)) | (1L << (T__115 - 115)) | (1L << (T__116 - 115)))) != 0)) {
					{
					setState(728);
					_la = _input.LA(1);
					if ( !(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (T__114 - 115)) | (1L << (T__115 - 115)) | (1L << (T__116 - 115)))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(732);
				_la = _input.LA(1);
				if (_la==T__52) {
					{
					setState(731);
					match(T__52);
					}
				}

				setState(734);
				_la = _input.LA(1);
				if ( !(_la==T__119 || _la==T__120) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(736);
				switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
				case 1:
					{
					setState(735);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				}
				break;
			case 4:
				_localctx = new BeforeOrAfterIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(739);
				_la = _input.LA(1);
				if (((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (T__114 - 115)) | (1L << (T__115 - 115)) | (1L << (T__116 - 115)))) != 0)) {
					{
					setState(738);
					_la = _input.LA(1);
					if ( !(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (T__114 - 115)) | (1L << (T__115 - 115)) | (1L << (T__116 - 115)))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(742);
				_la = _input.LA(1);
				if (_la==QUANTITY) {
					{
					setState(741);
					quantityOffset();
					}
				}

				setState(744);
				_la = _input.LA(1);
				if ( !(_la==T__121 || _la==T__122) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(746);
				switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
				case 1:
					{
					setState(745);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				setState(749);
				switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
				case 1:
					{
					setState(748);
					_la = _input.LA(1);
					if ( !(_la==T__88 || _la==T__89) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				}
				break;
			case 5:
				_localctx = new WithinIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(752);
				_la = _input.LA(1);
				if (((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (T__114 - 115)) | (1L << (T__115 - 115)) | (1L << (T__116 - 115)))) != 0)) {
					{
					setState(751);
					_la = _input.LA(1);
					if ( !(((((_la - 115)) & ~0x3f) == 0 && ((1L << (_la - 115)) & ((1L << (T__114 - 115)) | (1L << (T__115 - 115)) | (1L << (T__116 - 115)))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(755);
				_la = _input.LA(1);
				if (_la==T__52) {
					{
					setState(754);
					match(T__52);
					}
				}

				setState(757);
				match(T__123);
				setState(758);
				quantityLiteral();
				setState(759);
				match(T__90);
				setState(761);
				switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
				case 1:
					{
					setState(760);
					_la = _input.LA(1);
					if ( !(_la==T__88 || _la==T__89) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				}
				}
				break;
			case 6:
				_localctx = new MeetsIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(763);
				match(T__124);
				setState(765);
				_la = _input.LA(1);
				if (_la==T__121 || _la==T__122) {
					{
					setState(764);
					_la = _input.LA(1);
					if ( !(_la==T__121 || _la==T__122) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(768);
				switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
				case 1:
					{
					setState(767);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				}
				break;
			case 7:
				_localctx = new OverlapsIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(770);
				match(T__125);
				setState(772);
				_la = _input.LA(1);
				if (_la==T__121 || _la==T__122) {
					{
					setState(771);
					_la = _input.LA(1);
					if ( !(_la==T__121 || _la==T__122) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(775);
				switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
				case 1:
					{
					setState(774);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				}
				break;
			case 8:
				_localctx = new StartsIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(777);
				match(T__114);
				setState(779);
				switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
				case 1:
					{
					setState(778);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				}
				break;
			case 9:
				_localctx = new EndsIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(781);
				match(T__115);
				setState(783);
				switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
				case 1:
					{
					setState(782);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	 
		public TermContext() { }
		public void copyFrom(TermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TupleSelectorTermContext extends TermContext {
		public TupleSelectorContext tupleSelector() {
			return getRuleContext(TupleSelectorContext.class,0);
		}
		public TupleSelectorTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTupleSelectorTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTupleSelectorTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTupleSelectorTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdentifierTermContext extends TermContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public IdentifierTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIdentifierTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIdentifierTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIdentifierTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralTermContext extends TermContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterLiteralTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitLiteralTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitLiteralTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConceptSelectorTermContext extends TermContext {
		public ConceptSelectorContext conceptSelector() {
			return getRuleContext(ConceptSelectorContext.class,0);
		}
		public ConceptSelectorTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterConceptSelectorTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitConceptSelectorTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitConceptSelectorTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesizedTermContext extends TermContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParenthesizedTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterParenthesizedTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitParenthesizedTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitParenthesizedTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CodeSelectorTermContext extends TermContext {
		public CodeSelectorContext codeSelector() {
			return getRuleContext(CodeSelectorContext.class,0);
		}
		public CodeSelectorTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodeSelectorTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodeSelectorTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCodeSelectorTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InstanceSelectorTermContext extends TermContext {
		public InstanceSelectorContext instanceSelector() {
			return getRuleContext(InstanceSelectorContext.class,0);
		}
		public InstanceSelectorTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterInstanceSelectorTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitInstanceSelectorTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitInstanceSelectorTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntervalSelectorTermContext extends TermContext {
		public IntervalSelectorContext intervalSelector() {
			return getRuleContext(IntervalSelectorContext.class,0);
		}
		public IntervalSelectorTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIntervalSelectorTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIntervalSelectorTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIntervalSelectorTerm(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ListSelectorTermContext extends TermContext {
		public ListSelectorContext listSelector() {
			return getRuleContext(ListSelectorContext.class,0);
		}
		public ListSelectorTermContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterListSelectorTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitListSelectorTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitListSelectorTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_term);
		try {
			setState(799);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				_localctx = new IdentifierTermContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(787);
				identifier();
				}
				break;
			case 2:
				_localctx = new LiteralTermContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(788);
				literal();
				}
				break;
			case 3:
				_localctx = new IntervalSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(789);
				intervalSelector();
				}
				break;
			case 4:
				_localctx = new TupleSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(790);
				tupleSelector();
				}
				break;
			case 5:
				_localctx = new InstanceSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(791);
				instanceSelector();
				}
				break;
			case 6:
				_localctx = new ListSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(792);
				listSelector();
				}
				break;
			case 7:
				_localctx = new CodeSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(793);
				codeSelector();
				}
				break;
			case 8:
				_localctx = new ConceptSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(794);
				conceptSelector();
				}
				break;
			case 9:
				_localctx = new ParenthesizedTermContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(795);
				match(T__13);
				setState(796);
				expression(0);
				setState(797);
				match(T__15);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntervalSelectorContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public IntervalSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intervalSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIntervalSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIntervalSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIntervalSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalSelectorContext intervalSelector() throws RecognitionException {
		IntervalSelectorContext _localctx = new IntervalSelectorContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_intervalSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(801);
			match(T__20);
			setState(802);
			_la = _input.LA(1);
			if ( !(_la==T__13 || _la==T__30) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(803);
			expression(0);
			setState(804);
			match(T__14);
			setState(805);
			expression(0);
			setState(806);
			_la = _input.LA(1);
			if ( !(_la==T__15 || _la==T__32) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TupleSelectorContext extends ParserRuleContext {
		public List<TupleElementSelectorContext> tupleElementSelector() {
			return getRuleContexts(TupleElementSelectorContext.class);
		}
		public TupleElementSelectorContext tupleElementSelector(int i) {
			return getRuleContext(TupleElementSelectorContext.class,i);
		}
		public TupleSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTupleSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTupleSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTupleSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleSelectorContext tupleSelector() throws RecognitionException {
		TupleSelectorContext _localctx = new TupleSelectorContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_tupleSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(809);
			_la = _input.LA(1);
			if (_la==T__21) {
				{
				setState(808);
				match(T__21);
				}
			}

			setState(811);
			match(T__22);
			setState(821);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(812);
				match(T__10);
				}
				break;
			case T__1:
			case T__74:
			case T__75:
			case T__76:
			case T__126:
			case T__127:
			case T__128:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				{
				{
				setState(813);
				tupleElementSelector();
				setState(818);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14) {
					{
					{
					setState(814);
					match(T__14);
					setState(815);
					tupleElementSelector();
					}
					}
					setState(820);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(823);
			match(T__23);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TupleElementSelectorContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TupleElementSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleElementSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTupleElementSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTupleElementSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTupleElementSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TupleElementSelectorContext tupleElementSelector() throws RecognitionException {
		TupleElementSelectorContext _localctx = new TupleElementSelectorContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_tupleElementSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(825);
			identifier();
			setState(826);
			match(T__10);
			setState(827);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstanceSelectorContext extends ParserRuleContext {
		public NamedTypeSpecifierContext namedTypeSpecifier() {
			return getRuleContext(NamedTypeSpecifierContext.class,0);
		}
		public List<InstanceElementSelectorContext> instanceElementSelector() {
			return getRuleContexts(InstanceElementSelectorContext.class);
		}
		public InstanceElementSelectorContext instanceElementSelector(int i) {
			return getRuleContext(InstanceElementSelectorContext.class,i);
		}
		public InstanceSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instanceSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterInstanceSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitInstanceSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitInstanceSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstanceSelectorContext instanceSelector() throws RecognitionException {
		InstanceSelectorContext _localctx = new InstanceSelectorContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_instanceSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(829);
			namedTypeSpecifier();
			setState(830);
			match(T__22);
			setState(840);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(831);
				match(T__10);
				}
				break;
			case T__1:
			case T__74:
			case T__75:
			case T__76:
			case T__126:
			case T__127:
			case T__128:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				{
				{
				setState(832);
				instanceElementSelector();
				setState(837);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14) {
					{
					{
					setState(833);
					match(T__14);
					setState(834);
					instanceElementSelector();
					}
					}
					setState(839);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(842);
			match(T__23);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstanceElementSelectorContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public InstanceElementSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instanceElementSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterInstanceElementSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitInstanceElementSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitInstanceElementSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstanceElementSelectorContext instanceElementSelector() throws RecognitionException {
		InstanceElementSelectorContext _localctx = new InstanceElementSelectorContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_instanceElementSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(844);
			identifier();
			setState(845);
			match(T__10);
			setState(846);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListSelectorContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public ListSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterListSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitListSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitListSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListSelectorContext listSelector() throws RecognitionException {
		ListSelectorContext _localctx = new ListSelectorContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_listSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(855);
			_la = _input.LA(1);
			if (_la==T__17) {
				{
				setState(848);
				match(T__17);
				setState(853);
				_la = _input.LA(1);
				if (_la==T__18) {
					{
					setState(849);
					match(T__18);
					setState(850);
					typeSpecifier();
					setState(851);
					match(T__19);
					}
				}

				}
			}

			setState(857);
			match(T__22);
			setState(866);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__13) | (1L << T__17) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__30) | (1L << T__33) | (1L << T__37) | (1L << T__45) | (1L << T__46) | (1L << T__47) | (1L << T__48) | (1L << T__50) | (1L << T__51) | (1L << T__55))) != 0) || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (T__67 - 68)) | (1L << (T__68 - 68)) | (1L << (T__69 - 68)) | (1L << (T__70 - 68)) | (1L << (T__71 - 68)) | (1L << (T__72 - 68)) | (1L << (T__73 - 68)) | (1L << (T__74 - 68)) | (1L << (T__75 - 68)) | (1L << (T__76 - 68)) | (1L << (T__77 - 68)) | (1L << (T__78 - 68)) | (1L << (T__79 - 68)) | (1L << (T__80 - 68)) | (1L << (T__81 - 68)) | (1L << (T__82 - 68)) | (1L << (T__83 - 68)) | (1L << (T__84 - 68)) | (1L << (T__86 - 68)) | (1L << (T__87 - 68)) | (1L << (T__88 - 68)) | (1L << (T__89 - 68)) | (1L << (T__91 - 68)) | (1L << (T__92 - 68)) | (1L << (T__93 - 68)) | (1L << (T__94 - 68)) | (1L << (T__95 - 68)) | (1L << (T__96 - 68)) | (1L << (T__97 - 68)) | (1L << (T__103 - 68)) | (1L << (T__106 - 68)) | (1L << (T__107 - 68)) | (1L << (T__108 - 68)) | (1L << (T__126 - 68)) | (1L << (T__127 - 68)) | (1L << (T__128 - 68)) | (1L << (IDENTIFIER - 68)) | (1L << (QUANTITY - 68)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (DATETIME - 132)) | (1L << (TIME - 132)) | (1L << (QUOTEDIDENTIFIER - 132)) | (1L << (STRING - 132)))) != 0)) {
				{
				setState(858);
				expression(0);
				setState(863);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14) {
					{
					{
					setState(859);
					match(T__14);
					setState(860);
					expression(0);
					}
					}
					setState(865);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(868);
			match(T__23);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DisplayClauseContext extends ParserRuleContext {
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public DisplayClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_displayClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDisplayClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDisplayClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDisplayClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisplayClauseContext displayClause() throws RecognitionException {
		DisplayClauseContext _localctx = new DisplayClauseContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_displayClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(870);
			match(T__126);
			setState(871);
			stringLiteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodeSelectorContext extends ParserRuleContext {
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public CodesystemIdentifierContext codesystemIdentifier() {
			return getRuleContext(CodesystemIdentifierContext.class,0);
		}
		public DisplayClauseContext displayClause() {
			return getRuleContext(DisplayClauseContext.class,0);
		}
		public CodeSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codeSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodeSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodeSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitCodeSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodeSelectorContext codeSelector() throws RecognitionException {
		CodeSelectorContext _localctx = new CodeSelectorContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_codeSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(873);
			match(T__127);
			setState(874);
			stringLiteral();
			setState(875);
			match(T__33);
			setState(876);
			codesystemIdentifier();
			setState(878);
			switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
			case 1:
				{
				setState(877);
				displayClause();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConceptSelectorContext extends ParserRuleContext {
		public List<CodeSelectorContext> codeSelector() {
			return getRuleContexts(CodeSelectorContext.class);
		}
		public CodeSelectorContext codeSelector(int i) {
			return getRuleContext(CodeSelectorContext.class,i);
		}
		public DisplayClauseContext displayClause() {
			return getRuleContext(DisplayClauseContext.class,0);
		}
		public ConceptSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterConceptSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitConceptSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitConceptSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptSelectorContext conceptSelector() throws RecognitionException {
		ConceptSelectorContext _localctx = new ConceptSelectorContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_conceptSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(880);
			match(T__128);
			setState(881);
			match(T__22);
			setState(882);
			codeSelector();
			setState(887);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(883);
				match(T__14);
				setState(884);
				codeSelector();
				}
				}
				setState(889);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(890);
			match(T__23);
			setState(892);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				{
				setState(891);
				displayClause();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public NullLiteralContext nullLiteral() {
			return getRuleContext(NullLiteralContext.class,0);
		}
		public BooleanLiteralContext booleanLiteral() {
			return getRuleContext(BooleanLiteralContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public DateTimeLiteralContext dateTimeLiteral() {
			return getRuleContext(DateTimeLiteralContext.class,0);
		}
		public TimeLiteralContext timeLiteral() {
			return getRuleContext(TimeLiteralContext.class,0);
		}
		public QuantityLiteralContext quantityLiteral() {
			return getRuleContext(QuantityLiteralContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_literal);
		try {
			setState(900);
			switch (_input.LA(1)) {
			case T__46:
				enterOuterAlt(_localctx, 1);
				{
				setState(894);
				nullLiteral();
				}
				break;
			case T__47:
			case T__48:
				enterOuterAlt(_localctx, 2);
				{
				setState(895);
				booleanLiteral();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(896);
				stringLiteral();
				}
				break;
			case DATETIME:
				enterOuterAlt(_localctx, 4);
				{
				setState(897);
				dateTimeLiteral();
				}
				break;
			case TIME:
				enterOuterAlt(_localctx, 5);
				{
				setState(898);
				timeLiteral();
				}
				break;
			case QUANTITY:
				enterOuterAlt(_localctx, 6);
				{
				setState(899);
				quantityLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NullLiteralContext extends ParserRuleContext {
		public NullLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nullLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterNullLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitNullLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NullLiteralContext nullLiteral() throws RecognitionException {
		NullLiteralContext _localctx = new NullLiteralContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_nullLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(902);
			match(T__46);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanLiteralContext extends ParserRuleContext {
		public BooleanLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterBooleanLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitBooleanLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitBooleanLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanLiteralContext booleanLiteral() throws RecognitionException {
		BooleanLiteralContext _localctx = new BooleanLiteralContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_booleanLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(904);
			_la = _input.LA(1);
			if ( !(_la==T__47 || _la==T__48) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cqlParser.STRING, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(906);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateTimeLiteralContext extends ParserRuleContext {
		public TerminalNode DATETIME() { return getToken(cqlParser.DATETIME, 0); }
		public DateTimeLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateTimeLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterDateTimeLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitDateTimeLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitDateTimeLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateTimeLiteralContext dateTimeLiteral() throws RecognitionException {
		DateTimeLiteralContext _localctx = new DateTimeLiteralContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_dateTimeLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(908);
			match(DATETIME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TimeLiteralContext extends ParserRuleContext {
		public TerminalNode TIME() { return getToken(cqlParser.TIME, 0); }
		public TimeLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timeLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterTimeLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitTimeLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitTimeLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimeLiteralContext timeLiteral() throws RecognitionException {
		TimeLiteralContext _localctx = new TimeLiteralContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_timeLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(910);
			match(TIME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuantityLiteralContext extends ParserRuleContext {
		public TerminalNode QUANTITY() { return getToken(cqlParser.QUANTITY, 0); }
		public UnitContext unit() {
			return getRuleContext(UnitContext.class,0);
		}
		public QuantityLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantityLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterQuantityLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitQuantityLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitQuantityLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantityLiteralContext quantityLiteral() throws RecognitionException {
		QuantityLiteralContext _localctx = new QuantityLiteralContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_quantityLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(912);
			match(QUANTITY);
			setState(914);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(913);
				unit();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnitContext extends ParserRuleContext {
		public DateTimePrecisionContext dateTimePrecision() {
			return getRuleContext(DateTimePrecisionContext.class,0);
		}
		public PluralDateTimePrecisionContext pluralDateTimePrecision() {
			return getRuleContext(PluralDateTimePrecisionContext.class,0);
		}
		public TerminalNode STRING() { return getToken(cqlParser.STRING, 0); }
		public UnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_unit);
		try {
			setState(919);
			switch (_input.LA(1)) {
			case T__67:
			case T__68:
			case T__69:
			case T__70:
			case T__71:
			case T__72:
			case T__73:
				enterOuterAlt(_localctx, 1);
				{
				setState(916);
				dateTimePrecision();
				}
				break;
			case T__77:
			case T__78:
			case T__79:
			case T__80:
			case T__81:
			case T__82:
			case T__83:
				enterOuterAlt(_localctx, 2);
				{
				setState(917);
				pluralDateTimePrecision();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(918);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cqlParser.IDENTIFIER, 0); }
		public TerminalNode QUOTEDIDENTIFIER() { return getToken(cqlParser.QUOTEDIDENTIFIER, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cqlVisitor ) return ((cqlVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(921);
			_la = _input.LA(1);
			if ( !(_la==T__1 || ((((_la - 75)) & ~0x3f) == 0 && ((1L << (_la - 75)) & ((1L << (T__74 - 75)) | (1L << (T__75 - 75)) | (1L << (T__76 - 75)) | (1L << (T__126 - 75)) | (1L << (T__127 - 75)) | (1L << (T__128 - 75)) | (1L << (IDENTIFIER - 75)) | (1L << (QUOTEDIDENTIFIER - 75)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 50:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 54:
			return expressionTerm_sempred((ExpressionTermContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		case 2:
			return precpred(_ctx, 5);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 2);
		case 6:
			return precpred(_ctx, 1);
		case 7:
			return precpred(_ctx, 15);
		case 8:
			return precpred(_ctx, 14);
		case 9:
			return precpred(_ctx, 10);
		}
		return true;
	}
	private boolean expressionTerm_sempred(ExpressionTermContext _localctx, int predIndex) {
		switch (predIndex) {
		case 10:
			return precpred(_ctx, 6);
		case 11:
			return precpred(_ctx, 5);
		case 12:
			return precpred(_ctx, 4);
		case 13:
			return precpred(_ctx, 19);
		case 14:
			return precpred(_ctx, 18);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\u008d\u039e\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\3\2\5\2\u00a4\n\2"+
		"\3\2\7\2\u00a7\n\2\f\2\16\2\u00aa\13\2\3\2\7\2\u00ad\n\2\f\2\16\2\u00b0"+
		"\13\2\3\2\7\2\u00b3\n\2\f\2\16\2\u00b6\13\2\3\2\7\2\u00b9\n\2\f\2\16\2"+
		"\u00bc\13\2\3\2\7\2\u00bf\n\2\f\2\16\2\u00c2\13\2\3\2\6\2\u00c5\n\2\r"+
		"\2\16\2\u00c6\3\3\3\3\3\3\3\3\5\3\u00cd\n\3\3\4\3\4\3\4\3\4\5\4\u00d3"+
		"\n\4\3\5\3\5\3\5\3\5\5\5\u00d9\n\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\5\b"+
		"\u00e3\n\b\3\b\3\b\3\b\5\b\u00e8\n\b\3\b\3\b\5\b\u00ec\n\b\3\t\5\t\u00ef"+
		"\n\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u00f7\n\t\3\n\5\n\u00fa\n\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\5\n\u0102\n\n\3\n\5\n\u0105\n\n\3\13\3\13\3\13\3\13\3\13"+
		"\7\13\u010c\n\13\f\13\16\13\u010f\13\13\3\13\3\13\3\f\3\f\3\f\5\f\u0116"+
		"\n\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\21\3\21"+
		"\5\21\u0126\n\21\3\22\3\22\3\22\5\22\u012b\n\22\3\22\3\22\3\23\3\23\3"+
		"\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3"+
		"\26\7\26\u0140\n\26\f\26\16\26\u0143\13\26\3\26\3\26\3\27\3\27\3\27\3"+
		"\30\3\30\3\30\5\30\u014d\n\30\3\31\3\31\5\31\u0151\n\31\3\31\3\31\3\31"+
		"\3\31\3\32\3\32\3\32\3\33\3\33\5\33\u015c\n\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\7\33\u0164\n\33\f\33\16\33\u0167\13\33\5\33\u0169\n\33\3\33\3\33"+
		"\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\5\36"+
		"\u017a\n\36\3\37\3\37\3\37\3 \3 \3!\3!\5!\u0183\n!\3\"\3\"\3\"\3\"\3\""+
		"\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\5$\u0195\n$\3$\5$\u0198\n$\3$\3$\3%"+
		"\3%\3&\3&\3\'\3\'\3(\3(\5(\u01a4\n(\3(\7(\u01a7\n(\f(\16(\u01aa\13(\3"+
		"(\5(\u01ad\n(\3(\5(\u01b0\n(\3(\5(\u01b3\n(\3)\3)\5)\u01b7\n)\3*\3*\3"+
		"+\3+\3+\3+\7+\u01bf\n+\f+\16+\u01c2\13+\3,\3,\3,\3,\7,\u01c8\n,\f,\16"+
		",\u01cb\13,\3-\3-\3-\3-\3.\3.\3.\3/\3/\5/\u01d6\n/\3/\3/\3\60\3\60\3\60"+
		"\3\60\3\60\3\60\7\60\u01e0\n\60\f\60\16\60\u01e3\13\60\5\60\u01e5\n\60"+
		"\3\61\3\61\3\62\3\62\5\62\u01eb\n\62\3\63\3\63\3\63\7\63\u01f0\n\63\f"+
		"\63\16\63\u01f3\13\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\3\64\3\64\3\64\5\64\u0212\n\64\3\64\3\64\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\3\64\3\64\3\64\3\64\3\64\5\64\u0221\n\64\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\5\64\u0230\n\64\3\64\3\64"+
		"\3\64\3\64\3\64\3\64\5\64\u0238\n\64\3\64\3\64\3\64\3\64\3\64\7\64\u023f"+
		"\n\64\f\64\16\64\u0242\13\64\3\65\3\65\3\66\3\66\3\66\3\66\5\66\u024a"+
		"\n\66\3\67\3\67\38\38\38\38\38\38\38\38\38\38\38\38\38\38\38\38\38\38"+
		"\38\38\38\38\38\38\38\38\38\38\38\38\38\38\58\u026e\n8\38\38\38\38\38"+
		"\78\u0275\n8\f8\168\u0278\138\58\u027a\n8\38\38\38\38\38\38\38\38\38\3"+
		"8\38\38\38\38\38\38\38\38\58\u028e\n8\38\68\u0291\n8\r8\168\u0292\38\3"+
		"8\38\38\38\38\58\u029b\n8\38\38\38\38\38\38\38\38\38\38\38\38\38\38\3"+
		"8\38\38\78\u02ae\n8\f8\168\u02b1\138\39\39\39\39\39\3:\3:\3:\3;\3;\3<"+
		"\3<\3=\3=\5=\u02c1\n=\3>\5>\u02c4\n>\3>\3>\5>\u02c8\n>\3>\3>\5>\u02cc"+
		"\n>\3>\5>\u02cf\n>\3>\5>\u02d2\n>\3>\3>\5>\u02d6\n>\3>\5>\u02d9\n>\3>"+
		"\5>\u02dc\n>\3>\5>\u02df\n>\3>\3>\5>\u02e3\n>\3>\5>\u02e6\n>\3>\5>\u02e9"+
		"\n>\3>\3>\5>\u02ed\n>\3>\5>\u02f0\n>\3>\5>\u02f3\n>\3>\5>\u02f6\n>\3>"+
		"\3>\3>\3>\5>\u02fc\n>\3>\3>\5>\u0300\n>\3>\5>\u0303\n>\3>\3>\5>\u0307"+
		"\n>\3>\5>\u030a\n>\3>\3>\5>\u030e\n>\3>\3>\5>\u0312\n>\5>\u0314\n>\3?"+
		"\3?\3?\3?\3?\3?\3?\3?\3?\3?\3?\3?\5?\u0322\n?\3@\3@\3@\3@\3@\3@\3@\3A"+
		"\5A\u032c\nA\3A\3A\3A\3A\3A\7A\u0333\nA\fA\16A\u0336\13A\5A\u0338\nA\3"+
		"A\3A\3B\3B\3B\3B\3C\3C\3C\3C\3C\3C\7C\u0346\nC\fC\16C\u0349\13C\5C\u034b"+
		"\nC\3C\3C\3D\3D\3D\3D\3E\3E\3E\3E\3E\5E\u0358\nE\5E\u035a\nE\3E\3E\3E"+
		"\3E\7E\u0360\nE\fE\16E\u0363\13E\5E\u0365\nE\3E\3E\3F\3F\3F\3G\3G\3G\3"+
		"G\3G\5G\u0371\nG\3H\3H\3H\3H\3H\7H\u0378\nH\fH\16H\u037b\13H\3H\3H\5H"+
		"\u037f\nH\3I\3I\3I\3I\3I\3I\5I\u0387\nI\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N"+
		"\3O\3O\5O\u0395\nO\3P\3P\3P\5P\u039a\nP\3Q\3Q\3Q\2\4fnR\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bd"+
		"fhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092"+
		"\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\2\34\3\2\b\t\3\2\'(\3\2+.\4"+
		"\2\25\26;<\3\2=?\4\2\"\"@@\3\2AB\3\2CE\3\2\61\63\4\2//\64\64\3\2FL\3\2"+
		"PV\3\2YZ\3\2[\\\3\2cd\4\2((no\3\2fi\3\2qr\3\2st\3\2uw\3\2z{\3\2|}\4\2"+
		"\20\20!!\4\2\22\22##\3\2\62\63\6\2\4\4MO\u0081\u0084\u0088\u0088\u03e9"+
		"\2\u00a3\3\2\2\2\4\u00c8\3\2\2\2\6\u00ce\3\2\2\2\b\u00d4\3\2\2\2\n\u00dd"+
		"\3\2\2\2\f\u00df\3\2\2\2\16\u00e2\3\2\2\2\20\u00ee\3\2\2\2\22\u00f9\3"+
		"\2\2\2\24\u0106\3\2\2\2\26\u0115\3\2\2\2\30\u0119\3\2\2\2\32\u011b\3\2"+
		"\2\2\34\u011d\3\2\2\2\36\u011f\3\2\2\2 \u0125\3\2\2\2\"\u012a\3\2\2\2"+
		"$\u012e\3\2\2\2&\u0130\3\2\2\2(\u0135\3\2\2\2*\u013a\3\2\2\2,\u0146\3"+
		"\2\2\2.\u014c\3\2\2\2\60\u014e\3\2\2\2\62\u0156\3\2\2\2\64\u0159\3\2\2"+
		"\2\66\u016e\3\2\2\28\u0171\3\2\2\2:\u0179\3\2\2\2<\u017b\3\2\2\2>\u017e"+
		"\3\2\2\2@\u0182\3\2\2\2B\u0184\3\2\2\2D\u0189\3\2\2\2F\u018e\3\2\2\2H"+
		"\u019b\3\2\2\2J\u019d\3\2\2\2L\u019f\3\2\2\2N\u01a1\3\2\2\2P\u01b6\3\2"+
		"\2\2R\u01b8\3\2\2\2T\u01ba\3\2\2\2V\u01c3\3\2\2\2X\u01cc\3\2\2\2Z\u01d0"+
		"\3\2\2\2\\\u01d3\3\2\2\2^\u01d9\3\2\2\2`\u01e6\3\2\2\2b\u01e8\3\2\2\2"+
		"d\u01f1\3\2\2\2f\u0211\3\2\2\2h\u0243\3\2\2\2j\u0249\3\2\2\2l\u024b\3"+
		"\2\2\2n\u029a\3\2\2\2p\u02b2\3\2\2\2r\u02b7\3\2\2\2t\u02ba\3\2\2\2v\u02bc"+
		"\3\2\2\2x\u02be\3\2\2\2z\u0313\3\2\2\2|\u0321\3\2\2\2~\u0323\3\2\2\2\u0080"+
		"\u032b\3\2\2\2\u0082\u033b\3\2\2\2\u0084\u033f\3\2\2\2\u0086\u034e\3\2"+
		"\2\2\u0088\u0359\3\2\2\2\u008a\u0368\3\2\2\2\u008c\u036b\3\2\2\2\u008e"+
		"\u0372\3\2\2\2\u0090\u0386\3\2\2\2\u0092\u0388\3\2\2\2\u0094\u038a\3\2"+
		"\2\2\u0096\u038c\3\2\2\2\u0098\u038e\3\2\2\2\u009a\u0390\3\2\2\2\u009c"+
		"\u0392\3\2\2\2\u009e\u0399\3\2\2\2\u00a0\u039b\3\2\2\2\u00a2\u00a4\5\4"+
		"\3\2\u00a3\u00a2\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a8\3\2\2\2\u00a5"+
		"\u00a7\5\6\4\2\u00a6\u00a5\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8\u00a6\3\2"+
		"\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00ae\3\2\2\2\u00aa\u00a8\3\2\2\2\u00ab"+
		"\u00ad\5\b\5\2\u00ac\u00ab\3\2\2\2\u00ad\u00b0\3\2\2\2\u00ae\u00ac\3\2"+
		"\2\2\u00ae\u00af\3\2\2\2\u00af\u00b4\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b1"+
		"\u00b3\5\20\t\2\u00b2\u00b1\3\2\2\2\u00b3\u00b6\3\2\2\2\u00b4\u00b2\3"+
		"\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00ba\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7"+
		"\u00b9\5\22\n\2\u00b8\u00b7\3\2\2\2\u00b9\u00bc\3\2\2\2\u00ba\u00b8\3"+
		"\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00c0\3\2\2\2\u00bc\u00ba\3\2\2\2\u00bd"+
		"\u00bf\5\16\b\2\u00be\u00bd\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3"+
		"\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c3"+
		"\u00c5\5.\30\2\u00c4\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c4\3\2"+
		"\2\2\u00c6\u00c7\3\2\2\2\u00c7\3\3\2\2\2\u00c8\u00c9\7\3\2\2\u00c9\u00cc"+
		"\5\u00a0Q\2\u00ca\u00cb\7\4\2\2\u00cb\u00cd\5\36\20\2\u00cc\u00ca\3\2"+
		"\2\2\u00cc\u00cd\3\2\2\2\u00cd\5\3\2\2\2\u00ce\u00cf\7\5\2\2\u00cf\u00d2"+
		"\5\u00a0Q\2\u00d0\u00d1\7\4\2\2\u00d1\u00d3\5\36\20\2\u00d2\u00d0\3\2"+
		"\2\2\u00d2\u00d3\3\2\2\2\u00d3\7\3\2\2\2\u00d4\u00d5\7\6\2\2\u00d5\u00d8"+
		"\5\u00a0Q\2\u00d6\u00d7\7\4\2\2\u00d7\u00d9\5\36\20\2\u00d8\u00d6\3\2"+
		"\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00da\3\2\2\2\u00da\u00db\7\7\2\2\u00db"+
		"\u00dc\5\n\6\2\u00dc\t\3\2\2\2\u00dd\u00de\5\u00a0Q\2\u00de\13\3\2\2\2"+
		"\u00df\u00e0\t\2\2\2\u00e0\r\3\2\2\2\u00e1\u00e3\5\f\7\2\u00e2\u00e1\3"+
		"\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e5\7\n\2\2\u00e5"+
		"\u00e7\5\u00a0Q\2\u00e6\u00e8\5 \21\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8"+
		"\3\2\2\2\u00e8\u00eb\3\2\2\2\u00e9\u00ea\7\13\2\2\u00ea\u00ec\5f\64\2"+
		"\u00eb\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\17\3\2\2\2\u00ed\u00ef"+
		"\5\f\7\2\u00ee\u00ed\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0"+
		"\u00f1\7\f\2\2\u00f1\u00f2\5\u00a0Q\2\u00f2\u00f3\7\r\2\2\u00f3\u00f6"+
		"\5\32\16\2\u00f4\u00f5\7\4\2\2\u00f5\u00f7\5\36\20\2\u00f6\u00f4\3\2\2"+
		"\2\u00f6\u00f7\3\2\2\2\u00f7\21\3\2\2\2\u00f8\u00fa\5\f\7\2\u00f9\u00f8"+
		"\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc\7\16\2\2"+
		"\u00fc\u00fd\5\u00a0Q\2\u00fd\u00fe\7\r\2\2\u00fe\u0101\5\34\17\2\u00ff"+
		"\u0100\7\4\2\2\u0100\u0102\5\36\20\2\u0101\u00ff\3\2\2\2\u0101\u0102\3"+
		"\2\2\2\u0102\u0104\3\2\2\2\u0103\u0105\5\24\13\2\u0104\u0103\3\2\2\2\u0104"+
		"\u0105\3\2\2\2\u0105\23\3\2\2\2\u0106\u0107\7\17\2\2\u0107\u0108\7\20"+
		"\2\2\u0108\u010d\5\26\f\2\u0109\u010a\7\21\2\2\u010a\u010c\5\26\f\2\u010b"+
		"\u0109\3\2\2\2\u010c\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2"+
		"\2\2\u010e\u0110\3\2\2\2\u010f\u010d\3\2\2\2\u0110\u0111\7\22\2\2\u0111"+
		"\25\3\2\2\2\u0112\u0113\5\30\r\2\u0113\u0114\7\23\2\2\u0114\u0116\3\2"+
		"\2\2\u0115\u0112\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u0117\3\2\2\2\u0117"+
		"\u0118\5\u00a0Q\2\u0118\27\3\2\2\2\u0119\u011a\5\u00a0Q\2\u011a\31\3\2"+
		"\2\2\u011b\u011c\7\u0089\2\2\u011c\33\3\2\2\2\u011d\u011e\7\u0089\2\2"+
		"\u011e\35\3\2\2\2\u011f\u0120\7\u0089\2\2\u0120\37\3\2\2\2\u0121\u0126"+
		"\5\"\22\2\u0122\u0126\5&\24\2\u0123\u0126\5(\25\2\u0124\u0126\5*\26\2"+
		"\u0125\u0121\3\2\2\2\u0125\u0122\3\2\2\2\u0125\u0123\3\2\2\2\u0125\u0124"+
		"\3\2\2\2\u0126!\3\2\2\2\u0127\u0128\5$\23\2\u0128\u0129\7\23\2\2\u0129"+
		"\u012b\3\2\2\2\u012a\u0127\3\2\2\2\u012a\u012b\3\2\2\2\u012b\u012c\3\2"+
		"\2\2\u012c\u012d\5\u00a0Q\2\u012d#\3\2\2\2\u012e\u012f\5\u00a0Q\2\u012f"+
		"%\3\2\2\2\u0130\u0131\7\24\2\2\u0131\u0132\7\25\2\2\u0132\u0133\5 \21"+
		"\2\u0133\u0134\7\26\2\2\u0134\'\3\2\2\2\u0135\u0136\7\27\2\2\u0136\u0137"+
		"\7\25\2\2\u0137\u0138\5 \21\2\u0138\u0139\7\26\2\2\u0139)\3\2\2\2\u013a"+
		"\u013b\7\30\2\2\u013b\u013c\7\31\2\2\u013c\u0141\5,\27\2\u013d\u013e\7"+
		"\21\2\2\u013e\u0140\5,\27\2\u013f\u013d\3\2\2\2\u0140\u0143\3\2\2\2\u0141"+
		"\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\u0144\3\2\2\2\u0143\u0141\3\2"+
		"\2\2\u0144\u0145\7\32\2\2\u0145+\3\2\2\2\u0146\u0147\5\u00a0Q\2\u0147"+
		"\u0148\5 \21\2\u0148-\3\2\2\2\u0149\u014d\5\60\31\2\u014a\u014d\5\62\32"+
		"\2\u014b\u014d\5\64\33\2\u014c\u0149\3\2\2\2\u014c\u014a\3\2\2\2\u014c"+
		"\u014b\3\2\2\2\u014d/\3\2\2\2\u014e\u0150\7\33\2\2\u014f\u0151\5\f\7\2"+
		"\u0150\u014f\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0153"+
		"\5\u00a0Q\2\u0153\u0154\7\r\2\2\u0154\u0155\5f\64\2\u0155\61\3\2\2\2\u0156"+
		"\u0157\7\34\2\2\u0157\u0158\5\u00a0Q\2\u0158\63\3\2\2\2\u0159\u015b\7"+
		"\33\2\2\u015a\u015c\5\f\7\2\u015b\u015a\3\2\2\2\u015b\u015c\3\2\2\2\u015c"+
		"\u015d\3\2\2\2\u015d\u015e\7\35\2\2\u015e\u015f\5\u00a0Q\2\u015f\u0168"+
		"\7\20\2\2\u0160\u0165\5\66\34\2\u0161\u0162\7\21\2\2\u0162\u0164\5\66"+
		"\34\2\u0163\u0161\3\2\2\2\u0164\u0167\3\2\2\2\u0165\u0163\3\2\2\2\u0165"+
		"\u0166\3\2\2\2\u0166\u0169\3\2\2\2\u0167\u0165\3\2\2\2\u0168\u0160\3\2"+
		"\2\2\u0168\u0169\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016b\7\22\2\2\u016b"+
		"\u016c\7\r\2\2\u016c\u016d\58\35\2\u016d\65\3\2\2\2\u016e\u016f\5\u00a0"+
		"Q\2\u016f\u0170\5 \21\2\u0170\67\3\2\2\2\u0171\u0172\5f\64\2\u01729\3"+
		"\2\2\2\u0173\u017a\5F$\2\u0174\u017a\5d\63\2\u0175\u0176\7\20\2\2\u0176"+
		"\u0177\5f\64\2\u0177\u0178\7\22\2\2\u0178\u017a\3\2\2\2\u0179\u0173\3"+
		"\2\2\2\u0179\u0174\3\2\2\2\u0179\u0175\3\2\2\2\u017a;\3\2\2\2\u017b\u017c"+
		"\5:\36\2\u017c\u017d\5> \2\u017d=\3\2\2\2\u017e\u017f\5\u00a0Q\2\u017f"+
		"?\3\2\2\2\u0180\u0183\5B\"\2\u0181\u0183\5D#\2\u0182\u0180\3\2\2\2\u0182"+
		"\u0181\3\2\2\2\u0183A\3\2\2\2\u0184\u0185\7\36\2\2\u0185\u0186\5<\37\2"+
		"\u0186\u0187\7\37\2\2\u0187\u0188\5f\64\2\u0188C\3\2\2\2\u0189\u018a\7"+
		" \2\2\u018a\u018b\5<\37\2\u018b\u018c\7\37\2\2\u018c\u018d\5f\64\2\u018d"+
		"E\3\2\2\2\u018e\u018f\7!\2\2\u018f\u0197\5\"\22\2\u0190\u0194\7\r\2\2"+
		"\u0191\u0192\5H%\2\u0192\u0193\7\"\2\2\u0193\u0195\3\2\2\2\u0194\u0191"+
		"\3\2\2\2\u0194\u0195\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0198\5J&\2\u0197"+
		"\u0190\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u019a\7#"+
		"\2\2\u019aG\3\2\2\2\u019b\u019c\5\u00a0Q\2\u019cI\3\2\2\2\u019d\u019e"+
		"\5d\63\2\u019eK\3\2\2\2\u019f\u01a0\5\u00a0Q\2\u01a0M\3\2\2\2\u01a1\u01a3"+
		"\5P)\2\u01a2\u01a4\5V,\2\u01a3\u01a2\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4"+
		"\u01a8\3\2\2\2\u01a5\u01a7\5@!\2\u01a6\u01a5\3\2\2\2\u01a7\u01aa\3\2\2"+
		"\2\u01a8\u01a6\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01ac\3\2\2\2\u01aa\u01a8"+
		"\3\2\2\2\u01ab\u01ad\5Z.\2\u01ac\u01ab\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad"+
		"\u01af\3\2\2\2\u01ae\u01b0\5\\/\2\u01af\u01ae\3\2\2\2\u01af\u01b0\3\2"+
		"\2\2\u01b0\u01b2\3\2\2\2\u01b1\u01b3\5^\60\2\u01b2\u01b1\3\2\2\2\u01b2"+
		"\u01b3\3\2\2\2\u01b3O\3\2\2\2\u01b4\u01b7\5R*\2\u01b5\u01b7\5T+\2\u01b6"+
		"\u01b4\3\2\2\2\u01b6\u01b5\3\2\2\2\u01b7Q\3\2\2\2\u01b8\u01b9\5<\37\2"+
		"\u01b9S\3\2\2\2\u01ba\u01bb\7$\2\2\u01bb\u01c0\5<\37\2\u01bc\u01bd\7\21"+
		"\2\2\u01bd\u01bf\5<\37\2\u01be\u01bc\3\2\2\2\u01bf\u01c2\3\2\2\2\u01c0"+
		"\u01be\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1U\3\2\2\2\u01c2\u01c0\3\2\2\2"+
		"\u01c3\u01c4\7\33\2\2\u01c4\u01c9\5X-\2\u01c5\u01c6\7\21\2\2\u01c6\u01c8"+
		"\5X-\2\u01c7\u01c5\3\2\2\2\u01c8\u01cb\3\2\2\2\u01c9\u01c7\3\2\2\2\u01c9"+
		"\u01ca\3\2\2\2\u01caW\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cc\u01cd\5\u00a0"+
		"Q\2\u01cd\u01ce\7\r\2\2\u01ce\u01cf\5f\64\2\u01cfY\3\2\2\2\u01d0\u01d1"+
		"\7%\2\2\u01d1\u01d2\5f\64\2\u01d2[\3\2\2\2\u01d3\u01d5\7&\2\2\u01d4\u01d6"+
		"\t\3\2\2\u01d5\u01d4\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6\u01d7\3\2\2\2\u01d7"+
		"\u01d8\5f\64\2\u01d8]\3\2\2\2\u01d9\u01e4\7)\2\2\u01da\u01e5\5`\61\2\u01db"+
		"\u01dc\7*\2\2\u01dc\u01e1\5b\62\2\u01dd\u01de\7\21\2\2\u01de\u01e0\5b"+
		"\62\2\u01df\u01dd\3\2\2\2\u01e0\u01e3\3\2\2\2\u01e1\u01df\3\2\2\2\u01e1"+
		"\u01e2\3\2\2\2\u01e2\u01e5\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e4\u01da\3\2"+
		"\2\2\u01e4\u01db\3\2\2\2\u01e5_\3\2\2\2\u01e6\u01e7\t\4\2\2\u01e7a\3\2"+
		"\2\2\u01e8\u01ea\5n8\2\u01e9\u01eb\5`\61\2\u01ea\u01e9\3\2\2\2\u01ea\u01eb"+
		"\3\2\2\2\u01ebc\3\2\2\2\u01ec\u01ed\5L\'\2\u01ed\u01ee\7\23\2\2\u01ee"+
		"\u01f0\3\2\2\2\u01ef\u01ec\3\2\2\2\u01f0\u01f3\3\2\2\2\u01f1\u01ef\3\2"+
		"\2\2\u01f1\u01f2\3\2\2\2\u01f2\u01f4\3\2\2\2\u01f3\u01f1\3\2\2\2\u01f4"+
		"\u01f5\5\u00a0Q\2\u01f5e\3\2\2\2\u01f6\u01f7\b\64\1\2\u01f7\u01f8\7\60"+
		"\2\2\u01f8\u0212\5f\64\16\u01f9\u01fa\7\66\2\2\u01fa\u0212\5f\64\r\u01fb"+
		"\u0212\5n8\2\u01fc\u0212\5F$\2\u01fd\u0212\5N(\2\u01fe\u01ff\7\65\2\2"+
		"\u01ff\u0200\5f\64\2\u0200\u0201\7\64\2\2\u0201\u0202\5 \21\2\u0202\u0212"+
		"\3\2\2\2\u0203\u0204\5l\67\2\u0204\u0205\78\2\2\u0205\u0206\5n8\2\u0206"+
		"\u0207\79\2\2\u0207\u0208\5n8\2\u0208\u0212\3\2\2\2\u0209\u020a\7:\2\2"+
		"\u020a\u020b\7\"\2\2\u020b\u020c\5l\67\2\u020c\u020d\78\2\2\u020d\u020e"+
		"\5n8\2\u020e\u020f\79\2\2\u020f\u0210\5n8\2\u0210\u0212\3\2\2\2\u0211"+
		"\u01f6\3\2\2\2\u0211\u01f9\3\2\2\2\u0211\u01fb\3\2\2\2\u0211\u01fc\3\2"+
		"\2\2\u0211\u01fd\3\2\2\2\u0211\u01fe\3\2\2\2\u0211\u0203\3\2\2\2\u0211"+
		"\u0209\3\2\2\2\u0212\u0240\3\2\2\2\u0213\u0214\f\t\2\2\u0214\u0215\t\5"+
		"\2\2\u0215\u023f\5f\64\n\u0216\u0217\f\b\2\2\u0217\u0218\5z>\2\u0218\u0219"+
		"\5f\64\t\u0219\u023f\3\2\2\2\u021a\u021b\f\7\2\2\u021b\u021c\t\6\2\2\u021c"+
		"\u023f\5f\64\b\u021d\u021e\f\6\2\2\u021e\u0220\t\7\2\2\u021f\u0221\5r"+
		":\2\u0220\u021f\3\2\2\2\u0220\u0221\3\2\2\2\u0221\u0222\3\2\2\2\u0222"+
		"\u023f\5f\64\7\u0223\u0224\f\5\2\2\u0224\u0225\79\2\2\u0225\u023f\5f\64"+
		"\6\u0226\u0227\f\4\2\2\u0227\u0228\t\b\2\2\u0228\u023f\5f\64\5\u0229\u022a"+
		"\f\3\2\2\u022a\u022b\t\t\2\2\u022b\u023f\5f\64\4\u022c\u022d\f\21\2\2"+
		"\u022d\u022f\7/\2\2\u022e\u0230\7\60\2\2\u022f\u022e\3\2\2\2\u022f\u0230"+
		"\3\2\2\2\u0230\u0231\3\2\2\2\u0231\u023f\t\n\2\2\u0232\u0233\f\20\2\2"+
		"\u0233\u0234\t\13\2\2\u0234\u023f\5 \21\2\u0235\u0237\f\f\2\2\u0236\u0238"+
		"\7\67\2\2\u0237\u0236\3\2\2\2\u0237\u0238\3\2\2\2\u0238\u0239\3\2\2\2"+
		"\u0239\u023a\78\2\2\u023a\u023b\5n8\2\u023b\u023c\79\2\2\u023c\u023d\5"+
		"n8\2\u023d\u023f\3\2\2\2\u023e\u0213\3\2\2\2\u023e\u0216\3\2\2\2\u023e"+
		"\u021a\3\2\2\2\u023e\u021d\3\2\2\2\u023e\u0223\3\2\2\2\u023e\u0226\3\2"+
		"\2\2\u023e\u0229\3\2\2\2\u023e\u022c\3\2\2\2\u023e\u0232\3\2\2\2\u023e"+
		"\u0235\3\2\2\2\u023f\u0242\3\2\2\2\u0240\u023e\3\2\2\2\u0240\u0241\3\2"+
		"\2\2\u0241g\3\2\2\2\u0242\u0240\3\2\2\2\u0243\u0244\t\f\2\2\u0244i\3\2"+
		"\2\2\u0245\u024a\5h\65\2\u0246\u024a\7M\2\2\u0247\u024a\7N\2\2\u0248\u024a"+
		"\7O\2\2\u0249\u0245\3\2\2\2\u0249\u0246\3\2\2\2\u0249\u0247\3\2\2\2\u0249"+
		"\u0248\3\2\2\2\u024ak\3\2\2\2\u024b\u024c\t\r\2\2\u024cm\3\2\2\2\u024d"+
		"\u024e\b8\1\2\u024e\u024f\t\16\2\2\u024f\u029b\5n8\21\u0250\u0251\t\17"+
		"\2\2\u0251\u0252\7]\2\2\u0252\u029b\5n8\20\u0253\u0254\5j\66\2\u0254\u0255"+
		"\7$\2\2\u0255\u0256\5n8\17\u0256\u029b\3\2\2\2\u0257\u0258\7^\2\2\u0258"+
		"\u0259\7\"\2\2\u0259\u025a\5l\67\2\u025a\u025b\7]\2\2\u025b\u025c\5n8"+
		"\16\u025c\u029b\3\2\2\2\u025d\u025e\7_\2\2\u025e\u025f\7]\2\2\u025f\u029b"+
		"\5n8\r\u0260\u0261\7`\2\2\u0261\u0262\7]\2\2\u0262\u029b\5n8\f\u0263\u0264"+
		"\7a\2\2\u0264\u0265\7]\2\2\u0265\u029b\5n8\13\u0266\u0267\7b\2\2\u0267"+
		"\u0268\7$\2\2\u0268\u029b\5n8\n\u0269\u029b\5|?\2\u026a\u026b\5L\'\2\u026b"+
		"\u026c\7\23\2\2\u026c\u026e\3\2\2\2\u026d\u026a\3\2\2\2\u026d\u026e\3"+
		"\2\2\2\u026e\u026f\3\2\2\2\u026f\u0270\5\u00a0Q\2\u0270\u0279\7\20\2\2"+
		"\u0271\u0276\5f\64\2\u0272\u0273\7\21\2\2\u0273\u0275\5f\64\2\u0274\u0272"+
		"\3\2\2\2\u0275\u0278\3\2\2\2\u0276\u0274\3\2\2\2\u0276\u0277\3\2\2\2\u0277"+
		"\u027a\3\2\2\2\u0278\u0276\3\2\2\2\u0279\u0271\3\2\2\2\u0279\u027a\3\2"+
		"\2\2\u027a\u027b\3\2\2\2\u027b\u027c\7\22\2\2\u027c\u029b\3\2\2\2\u027d"+
		"\u027e\7W\2\2\u027e\u027f\5f\64\2\u027f\u0280\7X\2\2\u0280\u0281\5 \21"+
		"\2\u0281\u029b\3\2\2\2\u0282\u0283\t\20\2\2\u0283\u029b\5\"\22\2\u0284"+
		"\u0285\7j\2\2\u0285\u0286\5f\64\2\u0286\u0287\7k\2\2\u0287\u0288\5f\64"+
		"\2\u0288\u0289\7l\2\2\u0289\u028a\5f\64\2\u028a\u029b\3\2\2\2\u028b\u028d"+
		"\7m\2\2\u028c\u028e\5f\64\2\u028d\u028c\3\2\2\2\u028d\u028e\3\2\2\2\u028e"+
		"\u0290\3\2\2\2\u028f\u0291\5p9\2\u0290\u028f\3\2\2\2\u0291\u0292\3\2\2"+
		"\2\u0292\u0290\3\2\2\2\u0292\u0293\3\2\2\2\u0293\u0294\3\2\2\2\u0294\u0295"+
		"\7l\2\2\u0295\u0296\5f\64\2\u0296\u0297\7\\\2\2\u0297\u029b\3\2\2\2\u0298"+
		"\u0299\t\21\2\2\u0299\u029b\5f\64\2\u029a\u024d\3\2\2\2\u029a\u0250\3"+
		"\2\2\2\u029a\u0253\3\2\2\2\u029a\u0257\3\2\2\2\u029a\u025d\3\2\2\2\u029a"+
		"\u0260\3\2\2\2\u029a\u0263\3\2\2\2\u029a\u0266\3\2\2\2\u029a\u0269\3\2"+
		"\2\2\u029a\u026d\3\2\2\2\u029a\u027d\3\2\2\2\u029a\u0282\3\2\2\2\u029a"+
		"\u0284\3\2\2\2\u029a\u028b\3\2\2\2\u029a\u0298\3\2\2\2\u029b\u02af\3\2"+
		"\2\2\u029c\u029d\f\b\2\2\u029d\u029e\7e\2\2\u029e\u02ae\5n8\t\u029f\u02a0"+
		"\f\7\2\2\u02a0\u02a1\t\22\2\2\u02a1\u02ae\5n8\b\u02a2\u02a3\f\6\2\2\u02a3"+
		"\u02a4\t\16\2\2\u02a4\u02ae\5n8\7\u02a5\u02a6\f\25\2\2\u02a6\u02a7\7\23"+
		"\2\2\u02a7\u02ae\5\u00a0Q\2\u02a8\u02a9\f\24\2\2\u02a9\u02aa\7!\2\2\u02aa"+
		"\u02ab\5f\64\2\u02ab\u02ac\7#\2\2\u02ac\u02ae\3\2\2\2\u02ad\u029c\3\2"+
		"\2\2\u02ad\u029f\3\2\2\2\u02ad\u02a2\3\2\2\2\u02ad\u02a5\3\2\2\2\u02ad"+
		"\u02a8\3\2\2\2\u02ae\u02b1\3\2\2\2\u02af\u02ad\3\2\2\2\u02af\u02b0\3\2"+
		"\2\2\u02b0o\3\2\2\2\u02b1\u02af\3\2\2\2\u02b2\u02b3\7p\2\2\u02b3\u02b4"+
		"\5f\64\2\u02b4\u02b5\7k\2\2\u02b5\u02b6\5f\64\2\u02b6q\3\2\2\2\u02b7\u02b8"+
		"\5h\65\2\u02b8\u02b9\7]\2\2\u02b9s\3\2\2\2\u02ba\u02bb\t\23\2\2\u02bb"+
		"u\3\2\2\2\u02bc\u02bd\t\24\2\2\u02bdw\3\2\2\2\u02be\u02c0\5\u009cO\2\u02bf"+
		"\u02c1\5v<\2\u02c0\u02bf\3\2\2\2\u02c0\u02c1\3\2\2\2\u02c1y\3\2\2\2\u02c2"+
		"\u02c4\t\25\2\2\u02c3\u02c2\3\2\2\2\u02c3\u02c4\3\2\2\2\u02c4\u02c5\3"+
		"\2\2\2\u02c5\u02c7\7x\2\2\u02c6\u02c8\5h\65\2\u02c7\u02c6\3\2\2\2\u02c7"+
		"\u02c8\3\2\2\2\u02c8\u02cb\3\2\2\2\u02c9\u02cc\5t;\2\u02ca\u02cc\7\64"+
		"\2\2\u02cb\u02c9\3\2\2\2\u02cb\u02ca\3\2\2\2\u02cc\u02ce\3\2\2\2\u02cd"+
		"\u02cf\t\17\2\2\u02ce\u02cd\3\2\2\2\u02ce\u02cf\3\2\2\2\u02cf\u0314\3"+
		"\2\2\2\u02d0\u02d2\7\67\2\2\u02d1\u02d0\3\2\2\2\u02d1\u02d2\3\2\2\2\u02d2"+
		"\u02d3\3\2\2\2\u02d3\u02d5\7y\2\2\u02d4\u02d6\5r:\2\u02d5\u02d4\3\2\2"+
		"\2\u02d5\u02d6\3\2\2\2\u02d6\u02d8\3\2\2\2\u02d7\u02d9\t\17\2\2\u02d8"+
		"\u02d7\3\2\2\2\u02d8\u02d9\3\2\2\2\u02d9\u0314\3\2\2\2\u02da\u02dc\t\25"+
		"\2\2\u02db\u02da\3\2\2\2\u02db\u02dc\3\2\2\2\u02dc\u02de\3\2\2\2\u02dd"+
		"\u02df\7\67\2\2\u02de\u02dd\3\2\2\2\u02de\u02df\3\2\2\2\u02df\u02e0\3"+
		"\2\2\2\u02e0\u02e2\t\26\2\2\u02e1\u02e3\5r:\2\u02e2\u02e1\3\2\2\2\u02e2"+
		"\u02e3\3\2\2\2\u02e3\u0314\3\2\2\2\u02e4\u02e6\t\25\2\2\u02e5\u02e4\3"+
		"\2\2\2\u02e5\u02e6\3\2\2\2\u02e6\u02e8\3\2\2\2\u02e7\u02e9\5x=\2\u02e8"+
		"\u02e7\3\2\2\2\u02e8\u02e9\3\2\2\2\u02e9\u02ea\3\2\2\2\u02ea\u02ec\t\27"+
		"\2\2\u02eb\u02ed\5r:\2\u02ec\u02eb\3\2\2\2\u02ec\u02ed\3\2\2\2\u02ed\u02ef"+
		"\3\2\2\2\u02ee\u02f0\t\17\2\2\u02ef\u02ee\3\2\2\2\u02ef\u02f0\3\2\2\2"+
		"\u02f0\u0314\3\2\2\2\u02f1\u02f3\t\25\2\2\u02f2\u02f1\3\2\2\2\u02f2\u02f3"+
		"\3\2\2\2\u02f3\u02f5\3\2\2\2\u02f4\u02f6\7\67\2\2\u02f5\u02f4\3\2\2\2"+
		"\u02f5\u02f6\3\2\2\2\u02f6\u02f7\3\2\2\2\u02f7\u02f8\7~\2\2\u02f8\u02f9"+
		"\5\u009cO\2\u02f9\u02fb\7]\2\2\u02fa\u02fc\t\17\2\2\u02fb\u02fa\3\2\2"+
		"\2\u02fb\u02fc\3\2\2\2\u02fc\u0314\3\2\2\2\u02fd\u02ff\7\177\2\2\u02fe"+
		"\u0300\t\27\2\2\u02ff\u02fe\3\2\2\2\u02ff\u0300\3\2\2\2\u0300\u0302\3"+
		"\2\2\2\u0301\u0303\5r:\2\u0302\u0301\3\2\2\2\u0302\u0303\3\2\2\2\u0303"+
		"\u0314\3\2\2\2\u0304\u0306\7\u0080\2\2\u0305\u0307\t\27\2\2\u0306\u0305"+
		"\3\2\2\2\u0306\u0307\3\2\2\2\u0307\u0309\3\2\2\2\u0308\u030a\5r:\2\u0309"+
		"\u0308\3\2\2\2\u0309\u030a\3\2\2\2\u030a\u0314\3\2\2\2\u030b\u030d\7u"+
		"\2\2\u030c\u030e\5r:\2\u030d\u030c\3\2\2\2\u030d\u030e\3\2\2\2\u030e\u0314"+
		"\3\2\2\2\u030f\u0311\7v\2\2\u0310\u0312\5r:\2\u0311\u0310\3\2\2\2\u0311"+
		"\u0312\3\2\2\2\u0312\u0314\3\2\2\2\u0313\u02c3\3\2\2\2\u0313\u02d1\3\2"+
		"\2\2\u0313\u02db\3\2\2\2\u0313\u02e5\3\2\2\2\u0313\u02f2\3\2\2\2\u0313"+
		"\u02fd\3\2\2\2\u0313\u0304\3\2\2\2\u0313\u030b\3\2\2\2\u0313\u030f\3\2"+
		"\2\2\u0314{\3\2\2\2\u0315\u0322\5\u00a0Q\2\u0316\u0322\5\u0090I\2\u0317"+
		"\u0322\5~@\2\u0318\u0322\5\u0080A\2\u0319\u0322\5\u0084C\2\u031a\u0322"+
		"\5\u0088E\2\u031b\u0322\5\u008cG\2\u031c\u0322\5\u008eH\2\u031d\u031e"+
		"\7\20\2\2\u031e\u031f\5f\64\2\u031f\u0320\7\22\2\2\u0320\u0322\3\2\2\2"+
		"\u0321\u0315\3\2\2\2\u0321\u0316\3\2\2\2\u0321\u0317\3\2\2\2\u0321\u0318"+
		"\3\2\2\2\u0321\u0319\3\2\2\2\u0321\u031a\3\2\2\2\u0321\u031b\3\2\2\2\u0321"+
		"\u031c\3\2\2\2\u0321\u031d\3\2\2\2\u0322}\3\2\2\2\u0323\u0324\7\27\2\2"+
		"\u0324\u0325\t\30\2\2\u0325\u0326\5f\64\2\u0326\u0327\7\21\2\2\u0327\u0328"+
		"\5f\64\2\u0328\u0329\t\31\2\2\u0329\177\3\2\2\2\u032a\u032c\7\30\2\2\u032b"+
		"\u032a\3\2\2\2\u032b\u032c\3\2\2\2\u032c\u032d\3\2\2\2\u032d\u0337\7\31"+
		"\2\2\u032e\u0338\7\r\2\2\u032f\u0334\5\u0082B\2\u0330\u0331\7\21\2\2\u0331"+
		"\u0333\5\u0082B\2\u0332\u0330\3\2\2\2\u0333\u0336\3\2\2\2\u0334\u0332"+
		"\3\2\2\2\u0334\u0335\3\2\2\2\u0335\u0338\3\2\2\2\u0336\u0334\3\2\2\2\u0337"+
		"\u032e\3\2\2\2\u0337\u032f\3\2\2\2\u0338\u0339\3\2\2\2\u0339\u033a\7\32"+
		"\2\2\u033a\u0081\3\2\2\2\u033b\u033c\5\u00a0Q\2\u033c\u033d\7\r\2\2\u033d"+
		"\u033e\5f\64\2\u033e\u0083\3\2\2\2\u033f\u0340\5\"\22\2\u0340\u034a\7"+
		"\31\2\2\u0341\u034b\7\r\2\2\u0342\u0347\5\u0086D\2\u0343\u0344\7\21\2"+
		"\2\u0344\u0346\5\u0086D\2\u0345\u0343\3\2\2\2\u0346\u0349\3\2\2\2\u0347"+
		"\u0345\3\2\2\2\u0347\u0348\3\2\2\2\u0348\u034b\3\2\2\2\u0349\u0347\3\2"+
		"\2\2\u034a\u0341\3\2\2\2\u034a\u0342\3\2\2\2\u034b\u034c\3\2\2\2\u034c"+
		"\u034d\7\32\2\2\u034d\u0085\3\2\2\2\u034e\u034f\5\u00a0Q\2\u034f\u0350"+
		"\7\r\2\2\u0350\u0351\5f\64\2\u0351\u0087\3\2\2\2\u0352\u0357\7\24\2\2"+
		"\u0353\u0354\7\25\2\2\u0354\u0355\5 \21\2\u0355\u0356\7\26\2\2\u0356\u0358"+
		"\3\2\2\2\u0357\u0353\3\2\2\2\u0357\u0358\3\2\2\2\u0358\u035a\3\2\2\2\u0359"+
		"\u0352\3\2\2\2\u0359\u035a\3\2\2\2\u035a\u035b\3\2\2\2\u035b\u0364\7\31"+
		"\2\2\u035c\u0361\5f\64\2\u035d\u035e\7\21\2\2\u035e\u0360\5f\64\2\u035f"+
		"\u035d\3\2\2\2\u0360\u0363\3\2\2\2\u0361\u035f\3\2\2\2\u0361\u0362\3\2"+
		"\2\2\u0362\u0365\3\2\2\2\u0363\u0361\3\2\2\2\u0364\u035c\3\2\2\2\u0364"+
		"\u0365\3\2\2\2\u0365\u0366\3\2\2\2\u0366\u0367\7\32\2\2\u0367\u0089\3"+
		"\2\2\2\u0368\u0369\7\u0081\2\2\u0369\u036a\5\u0096L\2\u036a\u008b\3\2"+
		"\2\2\u036b\u036c\7\u0082\2\2\u036c\u036d\5\u0096L\2\u036d\u036e\7$\2\2"+
		"\u036e\u0370\5\26\f\2\u036f\u0371\5\u008aF\2\u0370\u036f\3\2\2\2\u0370"+
		"\u0371\3\2\2\2\u0371\u008d\3\2\2\2\u0372\u0373\7\u0083\2\2\u0373\u0374"+
		"\7\31\2\2\u0374\u0379\5\u008cG\2\u0375\u0376\7\21\2\2\u0376\u0378\5\u008c"+
		"G\2\u0377\u0375\3\2\2\2\u0378\u037b\3\2\2\2\u0379\u0377\3\2\2\2\u0379"+
		"\u037a\3\2\2\2\u037a\u037c\3\2\2\2\u037b\u0379\3\2\2\2\u037c\u037e\7\32"+
		"\2\2\u037d\u037f\5\u008aF\2\u037e\u037d\3\2\2\2\u037e\u037f\3\2\2\2\u037f"+
		"\u008f\3\2\2\2\u0380\u0387\5\u0092J\2\u0381\u0387\5\u0094K\2\u0382\u0387"+
		"\5\u0096L\2\u0383\u0387\5\u0098M\2\u0384\u0387\5\u009aN\2\u0385\u0387"+
		"\5\u009cO\2\u0386\u0380\3\2\2\2\u0386\u0381\3\2\2\2\u0386\u0382\3\2\2"+
		"\2\u0386\u0383\3\2\2\2\u0386\u0384\3\2\2\2\u0386\u0385\3\2\2\2\u0387\u0091"+
		"\3\2\2\2\u0388\u0389\7\61\2\2\u0389\u0093\3\2\2\2\u038a\u038b\t\32\2\2"+
		"\u038b\u0095\3\2\2\2\u038c\u038d\7\u0089\2\2\u038d\u0097\3\2\2\2\u038e"+
		"\u038f\7\u0086\2\2\u038f\u0099\3\2\2\2\u0390\u0391\7\u0087\2\2\u0391\u009b"+
		"\3\2\2\2\u0392\u0394\7\u0085\2\2\u0393\u0395\5\u009eP\2\u0394\u0393\3"+
		"\2\2\2\u0394\u0395\3\2\2\2\u0395\u009d\3\2\2\2\u0396\u039a\5h\65\2\u0397"+
		"\u039a\5l\67\2\u0398\u039a\7\u0089\2\2\u0399\u0396\3\2\2\2\u0399\u0397"+
		"\3\2\2\2\u0399\u0398\3\2\2\2\u039a\u009f\3\2\2\2\u039b\u039c\t\33\2\2"+
		"\u039c\u00a1\3\2\2\2g\u00a3\u00a8\u00ae\u00b4\u00ba\u00c0\u00c6\u00cc"+
		"\u00d2\u00d8\u00e2\u00e7\u00eb\u00ee\u00f6\u00f9\u0101\u0104\u010d\u0115"+
		"\u0125\u012a\u0141\u014c\u0150\u015b\u0165\u0168\u0179\u0182\u0194\u0197"+
		"\u01a3\u01a8\u01ac\u01af\u01b2\u01b6\u01c0\u01c9\u01d5\u01e1\u01e4\u01ea"+
		"\u01f1\u0211\u0220\u022f\u0237\u023e\u0240\u0249\u026d\u0276\u0279\u028d"+
		"\u0292\u029a\u02ad\u02af\u02c0\u02c3\u02c7\u02cb\u02ce\u02d1\u02d5\u02d8"+
		"\u02db\u02de\u02e2\u02e5\u02e8\u02ec\u02ef\u02f2\u02f5\u02fb\u02ff\u0302"+
		"\u0306\u0309\u030d\u0311\u0313\u0321\u032b\u0334\u0337\u0347\u034a\u0357"+
		"\u0359\u0361\u0364\u0370\u0379\u037e\u0386\u0394\u0399";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}