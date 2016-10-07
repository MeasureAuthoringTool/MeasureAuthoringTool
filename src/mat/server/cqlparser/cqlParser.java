package mat.server.cqlparser;

// Generated from cql.g4 by ANTLR 4.5
import java.util.List;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

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
		T__125=126, T__126=127, T__127=128, T__128=129, T__129=130, T__130=131, 
		T__131=132, T__132=133, T__133=134, T__134=135, IDENTIFIER=136, QUANTITY=137, 
		DATETIME=138, TIME=139, QUOTEDIDENTIFIER=140, STRING=141, WS=142, NEWLINE=143, 
		COMMENT=144, LINE_COMMENT=145;
	public static final int
		RULE_logic = 0, RULE_libraryDefinition = 1, RULE_usingDefinition = 2, 
		RULE_includeDefinition = 3, RULE_localIdentifier = 4, RULE_accessModifier = 5, 
		RULE_parameterDefinition = 6, RULE_codesystemDefinition = 7, RULE_valuesetDefinition = 8, 
		RULE_codesystems = 9, RULE_codesystemIdentifier = 10, RULE_libraryIdentifier = 11, 
		RULE_codeDefinition = 12, RULE_conceptDefinition = 13, RULE_codeIdentifier = 14, 
		RULE_codesystemId = 15, RULE_valuesetId = 16, RULE_versionSpecifier = 17, 
		RULE_codeId = 18, RULE_typeSpecifier = 19, RULE_namedTypeSpecifier = 20, 
		RULE_modelIdentifier = 21, RULE_listTypeSpecifier = 22, RULE_intervalTypeSpecifier = 23, 
		RULE_tupleTypeSpecifier = 24, RULE_tupleElementDefinition = 25, RULE_statement = 26, 
		RULE_expressionDefinition = 27, RULE_contextDefinition = 28, RULE_functionDefinition = 29, 
		RULE_operandDefinition = 30, RULE_functionBody = 31, RULE_querySource = 32, 
		RULE_aliasedQuerySource = 33, RULE_alias = 34, RULE_queryInclusionClause = 35, 
		RULE_withClause = 36, RULE_withoutClause = 37, RULE_retrieve = 38, RULE_valuesetPathIdentifier = 39, 
		RULE_valueset = 40, RULE_qualifier = 41, RULE_query = 42, RULE_sourceClause = 43, 
		RULE_singleSourceClause = 44, RULE_multipleSourceClause = 45, RULE_letClause = 46, 
		RULE_letClauseItem = 47, RULE_whereClause = 48, RULE_returnClause = 49, 
		RULE_sortClause = 50, RULE_sortDirection = 51, RULE_sortByItem = 52, RULE_qualifiedIdentifier = 53, 
		RULE_expression = 54, RULE_dateTimePrecision = 55, RULE_dateTimeComponent = 56, 
		RULE_pluralDateTimePrecision = 57, RULE_expressionTerm = 58, RULE_caseExpressionItem = 59, 
		RULE_dateTimePrecisionSpecifier = 60, RULE_relativeQualifier = 61, RULE_offsetRelativeQualifier = 62, 
		RULE_quantityOffset = 63, RULE_intervalOperatorPhrase = 64, RULE_term = 65, 
		RULE_intervalSelector = 66, RULE_tupleSelector = 67, RULE_tupleElementSelector = 68, 
		RULE_instanceSelector = 69, RULE_instanceElementSelector = 70, RULE_listSelector = 71, 
		RULE_displayClause = 72, RULE_codeSelector = 73, RULE_conceptSelector = 74, 
		RULE_literal = 75, RULE_nullLiteral = 76, RULE_booleanLiteral = 77, RULE_stringLiteral = 78, 
		RULE_dateTimeLiteral = 79, RULE_timeLiteral = 80, RULE_quantityLiteral = 81, 
		RULE_unit = 82, RULE_identifier = 83;
	public static final String[] ruleNames = {
		"logic", "libraryDefinition", "usingDefinition", "includeDefinition", 
		"localIdentifier", "accessModifier", "parameterDefinition", "codesystemDefinition", 
		"valuesetDefinition", "codesystems", "codesystemIdentifier", "libraryIdentifier", 
		"codeDefinition", "conceptDefinition", "codeIdentifier", "codesystemId", 
		"valuesetId", "versionSpecifier", "codeId", "typeSpecifier", "namedTypeSpecifier", 
		"modelIdentifier", "listTypeSpecifier", "intervalTypeSpecifier", "tupleTypeSpecifier", 
		"tupleElementDefinition", "statement", "expressionDefinition", "contextDefinition", 
		"functionDefinition", "operandDefinition", "functionBody", "querySource", 
		"aliasedQuerySource", "alias", "queryInclusionClause", "withClause", "withoutClause", 
		"retrieve", "valuesetPathIdentifier", "valueset", "qualifier", "query", 
		"sourceClause", "singleSourceClause", "multipleSourceClause", "letClause", 
		"letClauseItem", "whereClause", "returnClause", "sortClause", "sortDirection", 
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
		"'codesystems'", "'{'", "','", "'}'", "'.'", "'code'", "'from'", "'concept'", 
		"'List'", "'<'", "'>'", "'Interval'", "'Tuple'", "'define'", "'context'", 
		"'function'", "'('", "')'", "'with'", "'such that'", "'without'", "'['", 
		"'in'", "']'", "'let'", "'where'", "'return'", "'all'", "'distinct'", 
		"'sort'", "'by'", "'asc'", "'ascending'", "'desc'", "'descending'", "'is'", 
		"'not'", "'null'", "'true'", "'false'", "'as'", "'cast'", "'exists'", 
		"'properly'", "'between'", "'and'", "'difference'", "'<='", "'>='", "'='", 
		"'!='", "'~'", "'!~'", "'contains'", "'or'", "'xor'", "'union'", "'intersect'", 
		"'except'", "'year'", "'month'", "'week'", "'day'", "'hour'", "'minute'", 
		"'second'", "'millisecond'", "'date'", "'time'", "'timezone'", "'years'", 
		"'months'", "'weeks'", "'days'", "'hours'", "'minutes'", "'seconds'", 
		"'milliseconds'", "'convert'", "'to'", "'+'", "'-'", "'start'", "'end'", 
		"'of'", "'duration'", "'width'", "'successor'", "'predecessor'", "'singleton'", 
		"'minimum'", "'maximum'", "'^'", "'*'", "'/'", "'div'", "'mod'", "'if'", 
		"'then'", "'else'", "'case'", "'collapse'", "'flatten'", "'when'", "'or before'", 
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
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, "IDENTIFIER", "QUANTITY", "DATETIME", "TIME", 
		"QUOTEDIDENTIFIER", "STRING", "WS", "NEWLINE", "COMMENT", "LINE_COMMENT"
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
		public List<CodeDefinitionContext> codeDefinition() {
			return getRuleContexts(CodeDefinitionContext.class);
		}
		public CodeDefinitionContext codeDefinition(int i) {
			return getRuleContext(CodeDefinitionContext.class,i);
		}
		public List<ConceptDefinitionContext> conceptDefinition() {
			return getRuleContexts(ConceptDefinitionContext.class);
		}
		public ConceptDefinitionContext conceptDefinition(int i) {
			return getRuleContext(ConceptDefinitionContext.class,i);
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
	}

	public final LogicContext logic() throws RecognitionException {
		LogicContext _localctx = new LogicContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_logic);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(168);
				libraryDefinition();
				}
			}

			setState(174);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(171);
				usingDefinition();
				}
				}
				setState(176);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(177);
				includeDefinition();
				}
				}
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(186);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(183);
					codesystemDefinition();
					}
					} 
				}
				setState(188);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(192);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(189);
					valuesetDefinition();
					}
					} 
				}
				setState(194);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(198);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(195);
					codeDefinition();
					}
					} 
				}
				setState(200);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(204);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(201);
					conceptDefinition();
					}
					} 
				}
				setState(206);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7))) != 0)) {
				{
				{
				setState(207);
				parameterDefinition();
				}
				}
				setState(212);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(214); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(213);
				statement();
				}
				}
				setState(216); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__25 || _la==T__26 );
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
	}

	public final LibraryDefinitionContext libraryDefinition() throws RecognitionException {
		LibraryDefinitionContext _localctx = new LibraryDefinitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_libraryDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(T__0);
			setState(219);
			identifier();
			setState(222);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(220);
				match(T__1);
				setState(221);
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
		public ModelIdentifierContext modelIdentifier() {
			return getRuleContext(ModelIdentifierContext.class,0);
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
	}

	public final UsingDefinitionContext usingDefinition() throws RecognitionException {
		UsingDefinitionContext _localctx = new UsingDefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_usingDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			match(T__2);
			setState(225);
			modelIdentifier();
			setState(228);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(226);
				match(T__1);
				setState(227);
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
	}

	public final IncludeDefinitionContext includeDefinition() throws RecognitionException {
		IncludeDefinitionContext _localctx = new IncludeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_includeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(T__3);
			setState(231);
			identifier();
			setState(234);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(232);
				match(T__1);
				setState(233);
				versionSpecifier();
				}
			}

			setState(236);
			match(T__4);
			setState(237);
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
	}

	public final LocalIdentifierContext localIdentifier() throws RecognitionException {
		LocalIdentifierContext _localctx = new LocalIdentifierContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_localIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
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
	}

	public final AccessModifierContext accessModifier() throws RecognitionException {
		AccessModifierContext _localctx = new AccessModifierContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_accessModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(241);
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
	}

	public final ParameterDefinitionContext parameterDefinition() throws RecognitionException {
		ParameterDefinitionContext _localctx = new ParameterDefinitionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_parameterDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(243);
				accessModifier();
				}
			}

			setState(246);
			match(T__7);
			setState(247);
			identifier();
			setState(249);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__17) | (1L << T__19) | (1L << T__20) | (1L << T__23) | (1L << T__24))) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & ((1L << (T__79 - 80)) | (1L << (T__80 - 80)) | (1L << (T__81 - 80)) | (1L << (T__132 - 80)) | (1L << (T__133 - 80)) | (1L << (T__134 - 80)) | (1L << (IDENTIFIER - 80)) | (1L << (QUOTEDIDENTIFIER - 80)))) != 0)) {
				{
				setState(248);
				typeSpecifier();
				}
			}

			setState(253);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(251);
				match(T__8);
				setState(252);
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
	}

	public final CodesystemDefinitionContext codesystemDefinition() throws RecognitionException {
		CodesystemDefinitionContext _localctx = new CodesystemDefinitionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_codesystemDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(255);
				accessModifier();
				}
			}

			setState(258);
			match(T__9);
			setState(259);
			identifier();
			setState(260);
			match(T__10);
			setState(261);
			codesystemId();
			setState(264);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(262);
				match(T__1);
				setState(263);
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
	}

	public final ValuesetDefinitionContext valuesetDefinition() throws RecognitionException {
		ValuesetDefinitionContext _localctx = new ValuesetDefinitionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_valuesetDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(266);
				accessModifier();
				}
			}

			setState(269);
			match(T__11);
			setState(270);
			identifier();
			setState(271);
			match(T__10);
			setState(272);
			valuesetId();
			setState(275);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(273);
				match(T__1);
				setState(274);
				versionSpecifier();
				}
			}

			setState(278);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(277);
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
	}

	public final CodesystemsContext codesystems() throws RecognitionException {
		CodesystemsContext _localctx = new CodesystemsContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_codesystems);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280);
			match(T__12);
			setState(281);
			match(T__13);
			setState(282);
			codesystemIdentifier();
			setState(287);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(283);
				match(T__14);
				setState(284);
				codesystemIdentifier();
				}
				}
				setState(289);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(290);
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
	}

	public final CodesystemIdentifierContext codesystemIdentifier() throws RecognitionException {
		CodesystemIdentifierContext _localctx = new CodesystemIdentifierContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_codesystemIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(292);
				libraryIdentifier();
				setState(293);
				match(T__16);
				}
				break;
			}
			setState(297);
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
	}

	public final LibraryIdentifierContext libraryIdentifier() throws RecognitionException {
		LibraryIdentifierContext _localctx = new LibraryIdentifierContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_libraryIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
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

	public static class CodeDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public CodeIdContext codeId() {
			return getRuleContext(CodeIdContext.class,0);
		}
		public CodesystemIdentifierContext codesystemIdentifier() {
			return getRuleContext(CodesystemIdentifierContext.class,0);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public DisplayClauseContext displayClause() {
			return getRuleContext(DisplayClauseContext.class,0);
		}
		public CodeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodeDefinition(this);
		}
	}

	public final CodeDefinitionContext codeDefinition() throws RecognitionException {
		CodeDefinitionContext _localctx = new CodeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_codeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(301);
				accessModifier();
				}
			}

			setState(304);
			match(T__17);
			setState(305);
			identifier();
			setState(306);
			match(T__10);
			setState(307);
			codeId();
			setState(308);
			match(T__18);
			setState(309);
			codesystemIdentifier();
			setState(311);
			_la = _input.LA(1);
			if (_la==T__132) {
				{
				setState(310);
				displayClause();
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

	public static class ConceptDefinitionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<CodeIdentifierContext> codeIdentifier() {
			return getRuleContexts(CodeIdentifierContext.class);
		}
		public CodeIdentifierContext codeIdentifier(int i) {
			return getRuleContext(CodeIdentifierContext.class,i);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public DisplayClauseContext displayClause() {
			return getRuleContext(DisplayClauseContext.class,0);
		}
		public ConceptDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterConceptDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitConceptDefinition(this);
		}
	}

	public final ConceptDefinitionContext conceptDefinition() throws RecognitionException {
		ConceptDefinitionContext _localctx = new ConceptDefinitionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_conceptDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(314);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(313);
				accessModifier();
				}
			}

			setState(316);
			match(T__19);
			setState(317);
			identifier();
			setState(318);
			match(T__10);
			setState(319);
			match(T__13);
			setState(320);
			codeIdentifier();
			setState(325);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(321);
				match(T__14);
				setState(322);
				codeIdentifier();
				}
				}
				setState(327);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(328);
			match(T__15);
			setState(330);
			_la = _input.LA(1);
			if (_la==T__132) {
				{
				setState(329);
				displayClause();
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

	public static class CodeIdentifierContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public LibraryIdentifierContext libraryIdentifier() {
			return getRuleContext(LibraryIdentifierContext.class,0);
		}
		public CodeIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codeIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodeIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodeIdentifier(this);
		}
	}

	public final CodeIdentifierContext codeIdentifier() throws RecognitionException {
		CodeIdentifierContext _localctx = new CodeIdentifierContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_codeIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(332);
				libraryIdentifier();
				setState(333);
				match(T__16);
				}
				break;
			}
			setState(337);
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
	}

	public final CodesystemIdContext codesystemId() throws RecognitionException {
		CodesystemIdContext _localctx = new CodesystemIdContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_codesystemId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
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
	}

	public final ValuesetIdContext valuesetId() throws RecognitionException {
		ValuesetIdContext _localctx = new ValuesetIdContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_valuesetId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
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
	}

	public final VersionSpecifierContext versionSpecifier() throws RecognitionException {
		VersionSpecifierContext _localctx = new VersionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_versionSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
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

	public static class CodeIdContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cqlParser.STRING, 0); }
		public CodeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterCodeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitCodeId(this);
		}
	}

	public final CodeIdContext codeId() throws RecognitionException {
		CodeIdContext _localctx = new CodeIdContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_codeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
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
	}

	public final TypeSpecifierContext typeSpecifier() throws RecognitionException {
		TypeSpecifierContext _localctx = new TypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_typeSpecifier);
		try {
			setState(351);
			switch (_input.LA(1)) {
			case T__1:
			case T__17:
			case T__19:
			case T__79:
			case T__80:
			case T__81:
			case T__132:
			case T__133:
			case T__134:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(347);
				namedTypeSpecifier();
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 2);
				{
				setState(348);
				listTypeSpecifier();
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(349);
				intervalTypeSpecifier();
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 4);
				{
				setState(350);
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
	}

	public final NamedTypeSpecifierContext namedTypeSpecifier() throws RecognitionException {
		NamedTypeSpecifierContext _localctx = new NamedTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_namedTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(353);
				modelIdentifier();
				setState(354);
				match(T__16);
				}
				break;
			}
			setState(358);
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
	}

	public final ModelIdentifierContext modelIdentifier() throws RecognitionException {
		ModelIdentifierContext _localctx = new ModelIdentifierContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_modelIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
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
	}

	public final ListTypeSpecifierContext listTypeSpecifier() throws RecognitionException {
		ListTypeSpecifierContext _localctx = new ListTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_listTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			match(T__20);
			setState(363);
			match(T__21);
			setState(364);
			typeSpecifier();
			setState(365);
			match(T__22);
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
	}

	public final IntervalTypeSpecifierContext intervalTypeSpecifier() throws RecognitionException {
		IntervalTypeSpecifierContext _localctx = new IntervalTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_intervalTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			match(T__23);
			setState(368);
			match(T__21);
			setState(369);
			typeSpecifier();
			setState(370);
			match(T__22);
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
	}

	public final TupleTypeSpecifierContext tupleTypeSpecifier() throws RecognitionException {
		TupleTypeSpecifierContext _localctx = new TupleTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_tupleTypeSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			match(T__24);
			setState(373);
			match(T__13);
			setState(374);
			tupleElementDefinition();
			setState(379);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(375);
				match(T__14);
				setState(376);
				tupleElementDefinition();
				}
				}
				setState(381);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(382);
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
	}

	public final TupleElementDefinitionContext tupleElementDefinition() throws RecognitionException {
		TupleElementDefinitionContext _localctx = new TupleElementDefinitionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_tupleElementDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			identifier();
			setState(385);
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
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_statement);
		try {
			setState(390);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(387);
				expressionDefinition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(388);
				contextDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(389);
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
	}

	public final ExpressionDefinitionContext expressionDefinition() throws RecognitionException {
		ExpressionDefinitionContext _localctx = new ExpressionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_expressionDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			match(T__25);
			setState(394);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(393);
				accessModifier();
				}
			}

			setState(396);
			identifier();
			setState(397);
			match(T__10);
			setState(398);
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
	}

	public final ContextDefinitionContext contextDefinition() throws RecognitionException {
		ContextDefinitionContext _localctx = new ContextDefinitionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_contextDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(400);
			match(T__26);
			setState(401);
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
	}

	public final FunctionDefinitionContext functionDefinition() throws RecognitionException {
		FunctionDefinitionContext _localctx = new FunctionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_functionDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			match(T__25);
			setState(405);
			_la = _input.LA(1);
			if (_la==T__5 || _la==T__6) {
				{
				setState(404);
				accessModifier();
				}
			}

			setState(407);
			match(T__27);
			setState(408);
			identifier();
			setState(409);
			match(T__28);
			setState(418);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__17) | (1L << T__19))) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & ((1L << (T__79 - 80)) | (1L << (T__80 - 80)) | (1L << (T__81 - 80)) | (1L << (T__132 - 80)) | (1L << (T__133 - 80)) | (1L << (T__134 - 80)) | (1L << (IDENTIFIER - 80)) | (1L << (QUOTEDIDENTIFIER - 80)))) != 0)) {
				{
				setState(410);
				operandDefinition();
				setState(415);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14) {
					{
					{
					setState(411);
					match(T__14);
					setState(412);
					operandDefinition();
					}
					}
					setState(417);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(420);
			match(T__29);
			setState(421);
			match(T__10);
			setState(422);
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
	}

	public final OperandDefinitionContext operandDefinition() throws RecognitionException {
		OperandDefinitionContext _localctx = new OperandDefinitionContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_operandDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			identifier();
			setState(425);
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
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_functionBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
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
	}

	public final QuerySourceContext querySource() throws RecognitionException {
		QuerySourceContext _localctx = new QuerySourceContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_querySource);
		try {
			setState(435);
			switch (_input.LA(1)) {
			case T__33:
				enterOuterAlt(_localctx, 1);
				{
				setState(429);
				retrieve();
				}
				break;
			case T__1:
			case T__17:
			case T__19:
			case T__79:
			case T__80:
			case T__81:
			case T__132:
			case T__133:
			case T__134:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(430);
				qualifiedIdentifier();
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 3);
				{
				setState(431);
				match(T__28);
				setState(432);
				expression(0);
				setState(433);
				match(T__29);
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
	}

	public final AliasedQuerySourceContext aliasedQuerySource() throws RecognitionException {
		AliasedQuerySourceContext _localctx = new AliasedQuerySourceContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_aliasedQuerySource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(437);
			querySource();
			setState(438);
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
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(440);
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
	}

	public final QueryInclusionClauseContext queryInclusionClause() throws RecognitionException {
		QueryInclusionClauseContext _localctx = new QueryInclusionClauseContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_queryInclusionClause);
		try {
			setState(444);
			switch (_input.LA(1)) {
			case T__30:
				enterOuterAlt(_localctx, 1);
				{
				setState(442);
				withClause();
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 2);
				{
				setState(443);
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
	}

	public final WithClauseContext withClause() throws RecognitionException {
		WithClauseContext _localctx = new WithClauseContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_withClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			match(T__30);
			setState(447);
			aliasedQuerySource();
			setState(448);
			match(T__31);
			setState(449);
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
	}

	public final WithoutClauseContext withoutClause() throws RecognitionException {
		WithoutClauseContext _localctx = new WithoutClauseContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_withoutClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			match(T__32);
			setState(452);
			aliasedQuerySource();
			setState(453);
			match(T__31);
			setState(454);
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
	}

	public final RetrieveContext retrieve() throws RecognitionException {
		RetrieveContext _localctx = new RetrieveContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_retrieve);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(456);
			match(T__33);
			setState(457);
			namedTypeSpecifier();
			setState(465);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(458);
				match(T__10);
				setState(462);
				switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
				case 1:
					{
					setState(459);
					valuesetPathIdentifier();
					setState(460);
					match(T__34);
					}
					break;
				}
				setState(464);
				valueset();
				}
			}

			setState(467);
			match(T__35);
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
	}

	public final ValuesetPathIdentifierContext valuesetPathIdentifier() throws RecognitionException {
		ValuesetPathIdentifierContext _localctx = new ValuesetPathIdentifierContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_valuesetPathIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(469);
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
	}

	public final ValuesetContext valueset() throws RecognitionException {
		ValuesetContext _localctx = new ValuesetContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_valueset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
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
	}

	public final QualifierContext qualifier() throws RecognitionException {
		QualifierContext _localctx = new QualifierContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_qualifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(473);
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
		public LetClauseContext letClause() {
			return getRuleContext(LetClauseContext.class,0);
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
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_query);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(475);
			sourceClause();
			setState(477);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(476);
				letClause();
				}
				break;
			}
			setState(482);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(479);
					queryInclusionClause();
					}
					} 
				}
				setState(484);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			}
			setState(486);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(485);
				whereClause();
				}
				break;
			}
			setState(489);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(488);
				returnClause();
				}
				break;
			}
			setState(492);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(491);
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
	}

	public final SourceClauseContext sourceClause() throws RecognitionException {
		SourceClauseContext _localctx = new SourceClauseContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_sourceClause);
		try {
			setState(496);
			switch (_input.LA(1)) {
			case T__1:
			case T__17:
			case T__19:
			case T__28:
			case T__33:
			case T__79:
			case T__80:
			case T__81:
			case T__132:
			case T__133:
			case T__134:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(494);
				singleSourceClause();
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 2);
				{
				setState(495);
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
	}

	public final SingleSourceClauseContext singleSourceClause() throws RecognitionException {
		SingleSourceClauseContext _localctx = new SingleSourceClauseContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_singleSourceClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
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
	}

	public final MultipleSourceClauseContext multipleSourceClause() throws RecognitionException {
		MultipleSourceClauseContext _localctx = new MultipleSourceClauseContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_multipleSourceClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(500);
			match(T__18);
			setState(501);
			aliasedQuerySource();
			setState(506);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(502);
					match(T__14);
					setState(503);
					aliasedQuerySource();
					}
					} 
				}
				setState(508);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
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

	public static class LetClauseContext extends ParserRuleContext {
		public List<LetClauseItemContext> letClauseItem() {
			return getRuleContexts(LetClauseItemContext.class);
		}
		public LetClauseItemContext letClauseItem(int i) {
			return getRuleContext(LetClauseItemContext.class,i);
		}
		public LetClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterLetClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitLetClause(this);
		}
	}

	public final LetClauseContext letClause() throws RecognitionException {
		LetClauseContext _localctx = new LetClauseContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_letClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			match(T__36);
			setState(510);
			letClauseItem();
			setState(515);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(511);
					match(T__14);
					setState(512);
					letClauseItem();
					}
					} 
				}
				setState(517);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,47,_ctx);
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

	public static class LetClauseItemContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LetClauseItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letClauseItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).enterLetClauseItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cqlListener ) ((cqlListener)listener).exitLetClauseItem(this);
		}
	}

	public final LetClauseItemContext letClauseItem() throws RecognitionException {
		LetClauseItemContext _localctx = new LetClauseItemContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_letClauseItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(518);
			identifier();
			setState(519);
			match(T__10);
			setState(520);
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
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(522);
			match(T__37);
			setState(523);
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
	}

	public final ReturnClauseContext returnClause() throws RecognitionException {
		ReturnClauseContext _localctx = new ReturnClauseContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_returnClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(525);
			match(T__38);
			setState(527);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(526);
				_la = _input.LA(1);
				if ( !(_la==T__39 || _la==T__40) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			}
			setState(529);
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
	}

	public final SortClauseContext sortClause() throws RecognitionException {
		SortClauseContext _localctx = new SortClauseContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_sortClause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(531);
			match(T__41);
			setState(542);
			switch (_input.LA(1)) {
			case T__43:
			case T__44:
			case T__45:
			case T__46:
				{
				setState(532);
				sortDirection();
				}
				break;
			case T__42:
				{
				{
				setState(533);
				match(T__42);
				setState(534);
				sortByItem();
				setState(539);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(535);
						match(T__14);
						setState(536);
						sortByItem();
						}
						} 
					}
					setState(541);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
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
	}

	public final SortDirectionContext sortDirection() throws RecognitionException {
		SortDirectionContext _localctx = new SortDirectionContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_sortDirection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(544);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__43) | (1L << T__44) | (1L << T__45) | (1L << T__46))) != 0)) ) {
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
	}

	public final SortByItemContext sortByItem() throws RecognitionException {
		SortByItemContext _localctx = new SortByItemContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_sortByItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(546);
			expressionTerm(0);
			setState(548);
			switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
			case 1:
				{
				setState(547);
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
	}

	public final QualifiedIdentifierContext qualifiedIdentifier() throws RecognitionException {
		QualifiedIdentifierContext _localctx = new QualifiedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_qualifiedIdentifier);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(555);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(550);
					qualifier();
					setState(551);
					match(T__16);
					}
					} 
				}
				setState(557);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			}
			setState(558);
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
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 108;
		enterRecursionRule(_localctx, 108, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(587);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				_localctx = new NotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(561);
				match(T__48);
				setState(562);
				expression(12);
				}
				break;
			case 2:
				{
				_localctx = new ExistenceExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(563);
				match(T__54);
				setState(564);
				expression(11);
				}
				break;
			case 3:
				{
				_localctx = new TermExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(565);
				expressionTerm(0);
				}
				break;
			case 4:
				{
				_localctx = new RetrieveExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(566);
				retrieve();
				}
				break;
			case 5:
				{
				_localctx = new QueryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(567);
				query();
				}
				break;
			case 6:
				{
				_localctx = new CastExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(568);
				match(T__53);
				setState(569);
				expression(0);
				setState(570);
				match(T__52);
				setState(571);
				typeSpecifier();
				}
				break;
			case 7:
				{
				_localctx = new DurationBetweenExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(573);
				pluralDateTimePrecision();
				setState(574);
				match(T__56);
				setState(575);
				expressionTerm(0);
				setState(576);
				match(T__57);
				setState(577);
				expressionTerm(0);
				}
				break;
			case 8:
				{
				_localctx = new DifferenceBetweenExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(579);
				match(T__58);
				setState(580);
				match(T__34);
				setState(581);
				pluralDateTimePrecision();
				setState(582);
				match(T__56);
				setState(583);
				expressionTerm(0);
				setState(584);
				match(T__57);
				setState(585);
				expressionTerm(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(634);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(632);
					switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
					case 1:
						{
						_localctx = new InequalityExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(589);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(590);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__21) | (1L << T__22) | (1L << T__59) | (1L << T__60))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(591);
						expression(8);
						}
						break;
					case 2:
						{
						_localctx = new TimingExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(592);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(593);
						intervalOperatorPhrase();
						setState(594);
						expression(7);
						}
						break;
					case 3:
						{
						_localctx = new EqualityExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(596);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(597);
						_la = _input.LA(1);
						if ( !(((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & ((1L << (T__61 - 62)) | (1L << (T__62 - 62)) | (1L << (T__63 - 62)) | (1L << (T__64 - 62)))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(598);
						expression(6);
						}
						break;
					case 4:
						{
						_localctx = new MembershipExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(599);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(600);
						_la = _input.LA(1);
						if ( !(_la==T__34 || _la==T__65) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(602);
						switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
						case 1:
							{
							setState(601);
							dateTimePrecisionSpecifier();
							}
							break;
						}
						setState(604);
						expression(5);
						}
						break;
					case 5:
						{
						_localctx = new AndExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(605);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(606);
						match(T__57);
						setState(607);
						expression(4);
						}
						break;
					case 6:
						{
						_localctx = new OrExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(608);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(609);
						_la = _input.LA(1);
						if ( !(_la==T__66 || _la==T__67) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(610);
						expression(3);
						}
						break;
					case 7:
						{
						_localctx = new InFixSetExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(611);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(612);
						_la = _input.LA(1);
						if ( !(((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (T__68 - 69)) | (1L << (T__69 - 69)) | (1L << (T__70 - 69)))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(613);
						expression(2);
						}
						break;
					case 8:
						{
						_localctx = new BooleanExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(614);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(615);
						match(T__47);
						setState(617);
						_la = _input.LA(1);
						if (_la==T__48) {
							{
							setState(616);
							match(T__48);
							}
						}

						setState(619);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__49) | (1L << T__50) | (1L << T__51))) != 0)) ) {
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
						setState(620);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(621);
						_la = _input.LA(1);
						if ( !(_la==T__47 || _la==T__52) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(622);
						typeSpecifier();
						}
						break;
					case 10:
						{
						_localctx = new BetweenExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(623);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(625);
						_la = _input.LA(1);
						if (_la==T__55) {
							{
							setState(624);
							match(T__55);
							}
						}

						setState(627);
						match(T__56);
						setState(628);
						expressionTerm(0);
						setState(629);
						match(T__57);
						setState(630);
						expressionTerm(0);
						}
						break;
					}
					} 
				}
				setState(636);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,58,_ctx);
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
	}

	public final DateTimePrecisionContext dateTimePrecision() throws RecognitionException {
		DateTimePrecisionContext _localctx = new DateTimePrecisionContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_dateTimePrecision);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			_la = _input.LA(1);
			if ( !(((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (T__71 - 72)) | (1L << (T__72 - 72)) | (1L << (T__73 - 72)) | (1L << (T__74 - 72)) | (1L << (T__75 - 72)) | (1L << (T__76 - 72)) | (1L << (T__77 - 72)) | (1L << (T__78 - 72)))) != 0)) ) {
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
	}

	public final DateTimeComponentContext dateTimeComponent() throws RecognitionException {
		DateTimeComponentContext _localctx = new DateTimeComponentContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_dateTimeComponent);
		try {
			setState(643);
			switch (_input.LA(1)) {
			case T__71:
			case T__72:
			case T__73:
			case T__74:
			case T__75:
			case T__76:
			case T__77:
			case T__78:
				enterOuterAlt(_localctx, 1);
				{
				setState(639);
				dateTimePrecision();
				}
				break;
			case T__79:
				enterOuterAlt(_localctx, 2);
				{
				setState(640);
				match(T__79);
				}
				break;
			case T__80:
				enterOuterAlt(_localctx, 3);
				{
				setState(641);
				match(T__80);
				}
				break;
			case T__81:
				enterOuterAlt(_localctx, 4);
				{
				setState(642);
				match(T__81);
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
	}

	public final PluralDateTimePrecisionContext pluralDateTimePrecision() throws RecognitionException {
		PluralDateTimePrecisionContext _localctx = new PluralDateTimePrecisionContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_pluralDateTimePrecision);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(645);
			_la = _input.LA(1);
			if ( !(((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (T__82 - 83)) | (1L << (T__83 - 83)) | (1L << (T__84 - 83)) | (1L << (T__85 - 83)) | (1L << (T__86 - 83)) | (1L << (T__87 - 83)) | (1L << (T__88 - 83)) | (1L << (T__89 - 83)))) != 0)) ) {
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
	}

	public final ExpressionTermContext expressionTerm() throws RecognitionException {
		return expressionTerm(0);
	}

	private ExpressionTermContext expressionTerm(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionTermContext _localctx = new ExpressionTermContext(_ctx, _parentState);
		ExpressionTermContext _prevctx = _localctx;
		int _startState = 116;
		enterRecursionRule(_localctx, 116, RULE_expressionTerm, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(724);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				{
				_localctx = new PolarityExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(648);
				_la = _input.LA(1);
				if ( !(_la==T__92 || _la==T__93) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(649);
				expressionTerm(15);
				}
				break;
			case 2:
				{
				_localctx = new TimeBoundaryExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(650);
				_la = _input.LA(1);
				if ( !(_la==T__94 || _la==T__95) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(651);
				match(T__96);
				setState(652);
				expressionTerm(14);
				}
				break;
			case 3:
				{
				_localctx = new TimeUnitExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(653);
				dateTimeComponent();
				setState(654);
				match(T__18);
				setState(655);
				expressionTerm(13);
				}
				break;
			case 4:
				{
				_localctx = new DurationExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(657);
				match(T__97);
				setState(658);
				match(T__34);
				setState(659);
				pluralDateTimePrecision();
				setState(660);
				match(T__96);
				setState(661);
				expressionTerm(12);
				}
				break;
			case 5:
				{
				_localctx = new WidthExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(663);
				match(T__98);
				setState(664);
				match(T__96);
				setState(665);
				expressionTerm(11);
				}
				break;
			case 6:
				{
				_localctx = new SuccessorExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(666);
				match(T__99);
				setState(667);
				match(T__96);
				setState(668);
				expressionTerm(10);
				}
				break;
			case 7:
				{
				_localctx = new PredecessorExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(669);
				match(T__100);
				setState(670);
				match(T__96);
				setState(671);
				expressionTerm(9);
				}
				break;
			case 8:
				{
				_localctx = new ElementExtractorExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(672);
				match(T__101);
				setState(673);
				match(T__18);
				setState(674);
				expressionTerm(8);
				}
				break;
			case 9:
				{
				_localctx = new TermExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(675);
				term();
				}
				break;
			case 10:
				{
				_localctx = new InvocationExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(679);
				switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
				case 1:
					{
					setState(676);
					qualifier();
					setState(677);
					match(T__16);
					}
					break;
				}
				setState(681);
				identifier();
				setState(682);
				match(T__28);
				setState(691);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__13) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__23) | (1L << T__24) | (1L << T__28) | (1L << T__33) | (1L << T__40) | (1L << T__48) | (1L << T__49) | (1L << T__50) | (1L << T__51) | (1L << T__53) | (1L << T__54) | (1L << T__58))) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (T__71 - 72)) | (1L << (T__72 - 72)) | (1L << (T__73 - 72)) | (1L << (T__74 - 72)) | (1L << (T__75 - 72)) | (1L << (T__76 - 72)) | (1L << (T__77 - 72)) | (1L << (T__78 - 72)) | (1L << (T__79 - 72)) | (1L << (T__80 - 72)) | (1L << (T__81 - 72)) | (1L << (T__82 - 72)) | (1L << (T__83 - 72)) | (1L << (T__84 - 72)) | (1L << (T__85 - 72)) | (1L << (T__86 - 72)) | (1L << (T__87 - 72)) | (1L << (T__88 - 72)) | (1L << (T__89 - 72)) | (1L << (T__90 - 72)) | (1L << (T__92 - 72)) | (1L << (T__93 - 72)) | (1L << (T__94 - 72)) | (1L << (T__95 - 72)) | (1L << (T__97 - 72)) | (1L << (T__98 - 72)) | (1L << (T__99 - 72)) | (1L << (T__100 - 72)) | (1L << (T__101 - 72)) | (1L << (T__102 - 72)) | (1L << (T__103 - 72)) | (1L << (T__109 - 72)) | (1L << (T__112 - 72)) | (1L << (T__113 - 72)) | (1L << (T__114 - 72)) | (1L << (T__132 - 72)) | (1L << (T__133 - 72)) | (1L << (T__134 - 72)))) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & ((1L << (IDENTIFIER - 136)) | (1L << (QUANTITY - 136)) | (1L << (DATETIME - 136)) | (1L << (TIME - 136)) | (1L << (QUOTEDIDENTIFIER - 136)) | (1L << (STRING - 136)))) != 0)) {
					{
					setState(683);
					expression(0);
					setState(688);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__14) {
						{
						{
						setState(684);
						match(T__14);
						setState(685);
						expression(0);
						}
						}
						setState(690);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(693);
				match(T__29);
				}
				break;
			case 11:
				{
				_localctx = new ConversionExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(695);
				match(T__90);
				setState(696);
				expression(0);
				setState(697);
				match(T__91);
				setState(698);
				typeSpecifier();
				}
				break;
			case 12:
				{
				_localctx = new TypeExtentExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(700);
				_la = _input.LA(1);
				if ( !(_la==T__102 || _la==T__103) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(701);
				namedTypeSpecifier();
				}
				break;
			case 13:
				{
				_localctx = new IfThenElseExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(702);
				match(T__109);
				setState(703);
				expression(0);
				setState(704);
				match(T__110);
				setState(705);
				expression(0);
				setState(706);
				match(T__111);
				setState(707);
				expression(0);
				}
				break;
			case 14:
				{
				_localctx = new CaseExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(709);
				match(T__112);
				setState(711);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__13) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__23) | (1L << T__24) | (1L << T__28) | (1L << T__33) | (1L << T__40) | (1L << T__48) | (1L << T__49) | (1L << T__50) | (1L << T__51) | (1L << T__53) | (1L << T__54) | (1L << T__58))) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (T__71 - 72)) | (1L << (T__72 - 72)) | (1L << (T__73 - 72)) | (1L << (T__74 - 72)) | (1L << (T__75 - 72)) | (1L << (T__76 - 72)) | (1L << (T__77 - 72)) | (1L << (T__78 - 72)) | (1L << (T__79 - 72)) | (1L << (T__80 - 72)) | (1L << (T__81 - 72)) | (1L << (T__82 - 72)) | (1L << (T__83 - 72)) | (1L << (T__84 - 72)) | (1L << (T__85 - 72)) | (1L << (T__86 - 72)) | (1L << (T__87 - 72)) | (1L << (T__88 - 72)) | (1L << (T__89 - 72)) | (1L << (T__90 - 72)) | (1L << (T__92 - 72)) | (1L << (T__93 - 72)) | (1L << (T__94 - 72)) | (1L << (T__95 - 72)) | (1L << (T__97 - 72)) | (1L << (T__98 - 72)) | (1L << (T__99 - 72)) | (1L << (T__100 - 72)) | (1L << (T__101 - 72)) | (1L << (T__102 - 72)) | (1L << (T__103 - 72)) | (1L << (T__109 - 72)) | (1L << (T__112 - 72)) | (1L << (T__113 - 72)) | (1L << (T__114 - 72)) | (1L << (T__132 - 72)) | (1L << (T__133 - 72)) | (1L << (T__134 - 72)))) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & ((1L << (IDENTIFIER - 136)) | (1L << (QUANTITY - 136)) | (1L << (DATETIME - 136)) | (1L << (TIME - 136)) | (1L << (QUOTEDIDENTIFIER - 136)) | (1L << (STRING - 136)))) != 0)) {
					{
					setState(710);
					expression(0);
					}
				}

				setState(714); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(713);
					caseExpressionItem();
					}
					}
					setState(716); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__115 );
				setState(718);
				match(T__111);
				setState(719);
				expression(0);
				setState(720);
				match(T__95);
				}
				break;
			case 15:
				{
				_localctx = new AggregateExpressionTermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(722);
				_la = _input.LA(1);
				if ( !(_la==T__40 || _la==T__113 || _la==T__114) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(723);
				expression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(745);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(743);
					switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
					case 1:
						{
						_localctx = new PowerExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(726);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(727);
						match(T__104);
						setState(728);
						expressionTerm(7);
						}
						break;
					case 2:
						{
						_localctx = new MultiplicationExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(729);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(730);
						_la = _input.LA(1);
						if ( !(((((_la - 106)) & ~0x3f) == 0 && ((1L << (_la - 106)) & ((1L << (T__105 - 106)) | (1L << (T__106 - 106)) | (1L << (T__107 - 106)) | (1L << (T__108 - 106)))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(731);
						expressionTerm(6);
						}
						break;
					case 3:
						{
						_localctx = new AdditionExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(732);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(733);
						_la = _input.LA(1);
						if ( !(_la==T__92 || _la==T__93) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(734);
						expressionTerm(5);
						}
						break;
					case 4:
						{
						_localctx = new AccessorExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(735);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(736);
						match(T__16);
						setState(737);
						identifier();
						}
						break;
					case 5:
						{
						_localctx = new IndexedExpressionTermContext(new ExpressionTermContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expressionTerm);
						setState(738);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(739);
						match(T__33);
						setState(740);
						expression(0);
						setState(741);
						match(T__35);
						}
						break;
					}
					} 
				}
				setState(747);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,67,_ctx);
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
	}

	public final CaseExpressionItemContext caseExpressionItem() throws RecognitionException {
		CaseExpressionItemContext _localctx = new CaseExpressionItemContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_caseExpressionItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(748);
			match(T__115);
			setState(749);
			expression(0);
			setState(750);
			match(T__110);
			setState(751);
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
	}

	public final DateTimePrecisionSpecifierContext dateTimePrecisionSpecifier() throws RecognitionException {
		DateTimePrecisionSpecifierContext _localctx = new DateTimePrecisionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_dateTimePrecisionSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(753);
			dateTimePrecision();
			setState(754);
			match(T__96);
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
	}

	public final RelativeQualifierContext relativeQualifier() throws RecognitionException {
		RelativeQualifierContext _localctx = new RelativeQualifierContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_relativeQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(756);
			_la = _input.LA(1);
			if ( !(_la==T__116 || _la==T__117) ) {
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
	}

	public final OffsetRelativeQualifierContext offsetRelativeQualifier() throws RecognitionException {
		OffsetRelativeQualifierContext _localctx = new OffsetRelativeQualifierContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_offsetRelativeQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(758);
			_la = _input.LA(1);
			if ( !(_la==T__118 || _la==T__119) ) {
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
	}

	public final QuantityOffsetContext quantityOffset() throws RecognitionException {
		QuantityOffsetContext _localctx = new QuantityOffsetContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_quantityOffset);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(760);
			quantityLiteral();
			setState(762);
			_la = _input.LA(1);
			if (_la==T__118 || _la==T__119) {
				{
				setState(761);
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
	}

	public final IntervalOperatorPhraseContext intervalOperatorPhrase() throws RecognitionException {
		IntervalOperatorPhraseContext _localctx = new IntervalOperatorPhraseContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_intervalOperatorPhrase);
		int _la;
		try {
			setState(845);
			switch ( getInterpreter().adaptivePredict(_input,92,_ctx) ) {
			case 1:
				_localctx = new ConcurrentWithIntervalOperatorPhraseContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(765);
				_la = _input.LA(1);
				if (((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (T__120 - 121)) | (1L << (T__121 - 121)) | (1L << (T__122 - 121)))) != 0)) {
					{
					setState(764);
					_la = _input.LA(1);
					if ( !(((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (T__120 - 121)) | (1L << (T__121 - 121)) | (1L << (T__122 - 121)))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(767);
				match(T__123);
				setState(769);
				_la = _input.LA(1);
				if (((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (T__71 - 72)) | (1L << (T__72 - 72)) | (1L << (T__73 - 72)) | (1L << (T__74 - 72)) | (1L << (T__75 - 72)) | (1L << (T__76 - 72)) | (1L << (T__77 - 72)) | (1L << (T__78 - 72)))) != 0)) {
					{
					setState(768);
					dateTimePrecision();
					}
				}

				setState(773);
				switch (_input.LA(1)) {
				case T__116:
				case T__117:
					{
					setState(771);
					relativeQualifier();
					}
					break;
				case T__52:
					{
					setState(772);
					match(T__52);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(776);
				switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
				case 1:
					{
					setState(775);
					_la = _input.LA(1);
					if ( !(_la==T__94 || _la==T__95) ) {
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
				setState(779);
				_la = _input.LA(1);
				if (_la==T__55) {
					{
					setState(778);
					match(T__55);
					}
				}

				setState(781);
				match(T__124);
				setState(783);
				switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
				case 1:
					{
					setState(782);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				setState(786);
				switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
				case 1:
					{
					setState(785);
					_la = _input.LA(1);
					if ( !(_la==T__94 || _la==T__95) ) {
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
				setState(789);
				_la = _input.LA(1);
				if (((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (T__120 - 121)) | (1L << (T__121 - 121)) | (1L << (T__122 - 121)))) != 0)) {
					{
					setState(788);
					_la = _input.LA(1);
					if ( !(((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (T__120 - 121)) | (1L << (T__121 - 121)) | (1L << (T__122 - 121)))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(792);
				_la = _input.LA(1);
				if (_la==T__55) {
					{
					setState(791);
					match(T__55);
					}
				}

				setState(794);
				_la = _input.LA(1);
				if ( !(_la==T__125 || _la==T__126) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(796);
				switch ( getInterpreter().adaptivePredict(_input,78,_ctx) ) {
				case 1:
					{
					setState(795);
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
				setState(799);
				_la = _input.LA(1);
				if (((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (T__120 - 121)) | (1L << (T__121 - 121)) | (1L << (T__122 - 121)))) != 0)) {
					{
					setState(798);
					_la = _input.LA(1);
					if ( !(((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (T__120 - 121)) | (1L << (T__121 - 121)) | (1L << (T__122 - 121)))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(802);
				_la = _input.LA(1);
				if (_la==QUANTITY) {
					{
					setState(801);
					quantityOffset();
					}
				}

				setState(804);
				_la = _input.LA(1);
				if ( !(_la==T__127 || _la==T__128) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(806);
				switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
				case 1:
					{
					setState(805);
					dateTimePrecisionSpecifier();
					}
					break;
				}
				setState(809);
				switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
				case 1:
					{
					setState(808);
					_la = _input.LA(1);
					if ( !(_la==T__94 || _la==T__95) ) {
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
				setState(812);
				_la = _input.LA(1);
				if (((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (T__120 - 121)) | (1L << (T__121 - 121)) | (1L << (T__122 - 121)))) != 0)) {
					{
					setState(811);
					_la = _input.LA(1);
					if ( !(((((_la - 121)) & ~0x3f) == 0 && ((1L << (_la - 121)) & ((1L << (T__120 - 121)) | (1L << (T__121 - 121)) | (1L << (T__122 - 121)))) != 0)) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(815);
				_la = _input.LA(1);
				if (_la==T__55) {
					{
					setState(814);
					match(T__55);
					}
				}

				setState(817);
				match(T__129);
				setState(818);
				quantityLiteral();
				setState(819);
				match(T__96);
				setState(821);
				switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
				case 1:
					{
					setState(820);
					_la = _input.LA(1);
					if ( !(_la==T__94 || _la==T__95) ) {
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
				setState(823);
				match(T__130);
				setState(825);
				_la = _input.LA(1);
				if (_la==T__127 || _la==T__128) {
					{
					setState(824);
					_la = _input.LA(1);
					if ( !(_la==T__127 || _la==T__128) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(828);
				switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
				case 1:
					{
					setState(827);
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
				setState(830);
				match(T__131);
				setState(832);
				_la = _input.LA(1);
				if (_la==T__127 || _la==T__128) {
					{
					setState(831);
					_la = _input.LA(1);
					if ( !(_la==T__127 || _la==T__128) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
				}

				setState(835);
				switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
				case 1:
					{
					setState(834);
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
				setState(837);
				match(T__120);
				setState(839);
				switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
				case 1:
					{
					setState(838);
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
				setState(841);
				match(T__121);
				setState(843);
				switch ( getInterpreter().adaptivePredict(_input,91,_ctx) ) {
				case 1:
					{
					setState(842);
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
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_term);
		try {
			setState(859);
			switch ( getInterpreter().adaptivePredict(_input,93,_ctx) ) {
			case 1:
				_localctx = new IdentifierTermContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(847);
				identifier();
				}
				break;
			case 2:
				_localctx = new LiteralTermContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(848);
				literal();
				}
				break;
			case 3:
				_localctx = new IntervalSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(849);
				intervalSelector();
				}
				break;
			case 4:
				_localctx = new TupleSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(850);
				tupleSelector();
				}
				break;
			case 5:
				_localctx = new InstanceSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(851);
				instanceSelector();
				}
				break;
			case 6:
				_localctx = new ListSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(852);
				listSelector();
				}
				break;
			case 7:
				_localctx = new CodeSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(853);
				codeSelector();
				}
				break;
			case 8:
				_localctx = new ConceptSelectorTermContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(854);
				conceptSelector();
				}
				break;
			case 9:
				_localctx = new ParenthesizedTermContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(855);
				match(T__28);
				setState(856);
				expression(0);
				setState(857);
				match(T__29);
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
	}

	public final IntervalSelectorContext intervalSelector() throws RecognitionException {
		IntervalSelectorContext _localctx = new IntervalSelectorContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_intervalSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(861);
			match(T__23);
			setState(862);
			_la = _input.LA(1);
			if ( !(_la==T__28 || _la==T__33) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(863);
			expression(0);
			setState(864);
			match(T__14);
			setState(865);
			expression(0);
			setState(866);
			_la = _input.LA(1);
			if ( !(_la==T__29 || _la==T__35) ) {
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
	}

	public final TupleSelectorContext tupleSelector() throws RecognitionException {
		TupleSelectorContext _localctx = new TupleSelectorContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_tupleSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(869);
			_la = _input.LA(1);
			if (_la==T__24) {
				{
				setState(868);
				match(T__24);
				}
			}

			setState(871);
			match(T__13);
			setState(881);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(872);
				match(T__10);
				}
				break;
			case T__1:
			case T__17:
			case T__19:
			case T__79:
			case T__80:
			case T__81:
			case T__132:
			case T__133:
			case T__134:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				{
				{
				setState(873);
				tupleElementSelector();
				setState(878);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14) {
					{
					{
					setState(874);
					match(T__14);
					setState(875);
					tupleElementSelector();
					}
					}
					setState(880);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(883);
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
	}

	public final TupleElementSelectorContext tupleElementSelector() throws RecognitionException {
		TupleElementSelectorContext _localctx = new TupleElementSelectorContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_tupleElementSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(885);
			identifier();
			setState(886);
			match(T__10);
			setState(887);
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
	}

	public final InstanceSelectorContext instanceSelector() throws RecognitionException {
		InstanceSelectorContext _localctx = new InstanceSelectorContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_instanceSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(889);
			namedTypeSpecifier();
			setState(890);
			match(T__13);
			setState(900);
			switch (_input.LA(1)) {
			case T__10:
				{
				setState(891);
				match(T__10);
				}
				break;
			case T__1:
			case T__17:
			case T__19:
			case T__79:
			case T__80:
			case T__81:
			case T__132:
			case T__133:
			case T__134:
			case IDENTIFIER:
			case QUOTEDIDENTIFIER:
				{
				{
				setState(892);
				instanceElementSelector();
				setState(897);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14) {
					{
					{
					setState(893);
					match(T__14);
					setState(894);
					instanceElementSelector();
					}
					}
					setState(899);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(902);
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
	}

	public final InstanceElementSelectorContext instanceElementSelector() throws RecognitionException {
		InstanceElementSelectorContext _localctx = new InstanceElementSelectorContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_instanceElementSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(904);
			identifier();
			setState(905);
			match(T__10);
			setState(906);
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
	}

	public final ListSelectorContext listSelector() throws RecognitionException {
		ListSelectorContext _localctx = new ListSelectorContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_listSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(915);
			_la = _input.LA(1);
			if (_la==T__20) {
				{
				setState(908);
				match(T__20);
				setState(913);
				_la = _input.LA(1);
				if (_la==T__21) {
					{
					setState(909);
					match(T__21);
					setState(910);
					typeSpecifier();
					setState(911);
					match(T__22);
					}
				}

				}
			}

			setState(917);
			match(T__13);
			setState(926);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__13) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__23) | (1L << T__24) | (1L << T__28) | (1L << T__33) | (1L << T__40) | (1L << T__48) | (1L << T__49) | (1L << T__50) | (1L << T__51) | (1L << T__53) | (1L << T__54) | (1L << T__58))) != 0) || ((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (T__71 - 72)) | (1L << (T__72 - 72)) | (1L << (T__73 - 72)) | (1L << (T__74 - 72)) | (1L << (T__75 - 72)) | (1L << (T__76 - 72)) | (1L << (T__77 - 72)) | (1L << (T__78 - 72)) | (1L << (T__79 - 72)) | (1L << (T__80 - 72)) | (1L << (T__81 - 72)) | (1L << (T__82 - 72)) | (1L << (T__83 - 72)) | (1L << (T__84 - 72)) | (1L << (T__85 - 72)) | (1L << (T__86 - 72)) | (1L << (T__87 - 72)) | (1L << (T__88 - 72)) | (1L << (T__89 - 72)) | (1L << (T__90 - 72)) | (1L << (T__92 - 72)) | (1L << (T__93 - 72)) | (1L << (T__94 - 72)) | (1L << (T__95 - 72)) | (1L << (T__97 - 72)) | (1L << (T__98 - 72)) | (1L << (T__99 - 72)) | (1L << (T__100 - 72)) | (1L << (T__101 - 72)) | (1L << (T__102 - 72)) | (1L << (T__103 - 72)) | (1L << (T__109 - 72)) | (1L << (T__112 - 72)) | (1L << (T__113 - 72)) | (1L << (T__114 - 72)) | (1L << (T__132 - 72)) | (1L << (T__133 - 72)) | (1L << (T__134 - 72)))) != 0) || ((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & ((1L << (IDENTIFIER - 136)) | (1L << (QUANTITY - 136)) | (1L << (DATETIME - 136)) | (1L << (TIME - 136)) | (1L << (QUOTEDIDENTIFIER - 136)) | (1L << (STRING - 136)))) != 0)) {
				{
				setState(918);
				expression(0);
				setState(923);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14) {
					{
					{
					setState(919);
					match(T__14);
					setState(920);
					expression(0);
					}
					}
					setState(925);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(928);
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
	}

	public final DisplayClauseContext displayClause() throws RecognitionException {
		DisplayClauseContext _localctx = new DisplayClauseContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_displayClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(930);
			match(T__132);
			setState(931);
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
	}

	public final CodeSelectorContext codeSelector() throws RecognitionException {
		CodeSelectorContext _localctx = new CodeSelectorContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_codeSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(933);
			match(T__133);
			setState(934);
			stringLiteral();
			setState(935);
			match(T__18);
			setState(936);
			codesystemIdentifier();
			setState(938);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				{
				setState(937);
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
	}

	public final ConceptSelectorContext conceptSelector() throws RecognitionException {
		ConceptSelectorContext _localctx = new ConceptSelectorContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_conceptSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(940);
			match(T__134);
			setState(941);
			match(T__13);
			setState(942);
			codeSelector();
			setState(947);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(943);
				match(T__14);
				setState(944);
				codeSelector();
				}
				}
				setState(949);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(950);
			match(T__15);
			setState(952);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(951);
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
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_literal);
		try {
			setState(960);
			switch (_input.LA(1)) {
			case T__49:
				enterOuterAlt(_localctx, 1);
				{
				setState(954);
				nullLiteral();
				}
				break;
			case T__50:
			case T__51:
				enterOuterAlt(_localctx, 2);
				{
				setState(955);
				booleanLiteral();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(956);
				stringLiteral();
				}
				break;
			case DATETIME:
				enterOuterAlt(_localctx, 4);
				{
				setState(957);
				dateTimeLiteral();
				}
				break;
			case TIME:
				enterOuterAlt(_localctx, 5);
				{
				setState(958);
				timeLiteral();
				}
				break;
			case QUANTITY:
				enterOuterAlt(_localctx, 6);
				{
				setState(959);
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
	}

	public final NullLiteralContext nullLiteral() throws RecognitionException {
		NullLiteralContext _localctx = new NullLiteralContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_nullLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962);
			match(T__49);
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
	}

	public final BooleanLiteralContext booleanLiteral() throws RecognitionException {
		BooleanLiteralContext _localctx = new BooleanLiteralContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_booleanLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(964);
			_la = _input.LA(1);
			if ( !(_la==T__50 || _la==T__51) ) {
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
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(966);
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
	}

	public final DateTimeLiteralContext dateTimeLiteral() throws RecognitionException {
		DateTimeLiteralContext _localctx = new DateTimeLiteralContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_dateTimeLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(968);
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
	}

	public final TimeLiteralContext timeLiteral() throws RecognitionException {
		TimeLiteralContext _localctx = new TimeLiteralContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_timeLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(970);
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
	}

	public final QuantityLiteralContext quantityLiteral() throws RecognitionException {
		QuantityLiteralContext _localctx = new QuantityLiteralContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_quantityLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(972);
			match(QUANTITY);
			setState(974);
			switch ( getInterpreter().adaptivePredict(_input,107,_ctx) ) {
			case 1:
				{
				setState(973);
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
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_unit);
		try {
			setState(979);
			switch (_input.LA(1)) {
			case T__71:
			case T__72:
			case T__73:
			case T__74:
			case T__75:
			case T__76:
			case T__77:
			case T__78:
				enterOuterAlt(_localctx, 1);
				{
				setState(976);
				dateTimePrecision();
				}
				break;
			case T__82:
			case T__83:
			case T__84:
			case T__85:
			case T__86:
			case T__87:
			case T__88:
			case T__89:
				enterOuterAlt(_localctx, 2);
				{
				setState(977);
				pluralDateTimePrecision();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(978);
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
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(981);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__17) | (1L << T__19))) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & ((1L << (T__79 - 80)) | (1L << (T__80 - 80)) | (1L << (T__81 - 80)) | (1L << (T__132 - 80)) | (1L << (T__133 - 80)) | (1L << (T__134 - 80)) | (1L << (IDENTIFIER - 80)) | (1L << (QUOTEDIDENTIFIER - 80)))) != 0)) ) {
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
		case 54:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 58:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\u0093\u03da\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\3\2\5\2\u00ac\n\2\3\2\7\2\u00af\n\2\f\2\16\2\u00b2\13\2\3\2\7\2"+
		"\u00b5\n\2\f\2\16\2\u00b8\13\2\3\2\7\2\u00bb\n\2\f\2\16\2\u00be\13\2\3"+
		"\2\7\2\u00c1\n\2\f\2\16\2\u00c4\13\2\3\2\7\2\u00c7\n\2\f\2\16\2\u00ca"+
		"\13\2\3\2\7\2\u00cd\n\2\f\2\16\2\u00d0\13\2\3\2\7\2\u00d3\n\2\f\2\16\2"+
		"\u00d6\13\2\3\2\6\2\u00d9\n\2\r\2\16\2\u00da\3\3\3\3\3\3\3\3\5\3\u00e1"+
		"\n\3\3\4\3\4\3\4\3\4\5\4\u00e7\n\4\3\5\3\5\3\5\3\5\5\5\u00ed\n\5\3\5\3"+
		"\5\3\5\3\6\3\6\3\7\3\7\3\b\5\b\u00f7\n\b\3\b\3\b\3\b\5\b\u00fc\n\b\3\b"+
		"\3\b\5\b\u0100\n\b\3\t\5\t\u0103\n\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u010b"+
		"\n\t\3\n\5\n\u010e\n\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u0116\n\n\3\n\5\n\u0119"+
		"\n\n\3\13\3\13\3\13\3\13\3\13\7\13\u0120\n\13\f\13\16\13\u0123\13\13\3"+
		"\13\3\13\3\f\3\f\3\f\5\f\u012a\n\f\3\f\3\f\3\r\3\r\3\16\5\16\u0131\n\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u013a\n\16\3\17\5\17\u013d\n"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\7\17\u0146\n\17\f\17\16\17\u0149"+
		"\13\17\3\17\3\17\5\17\u014d\n\17\3\20\3\20\3\20\5\20\u0152\n\20\3\20\3"+
		"\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\25\5\25\u0162"+
		"\n\25\3\26\3\26\3\26\5\26\u0167\n\26\3\26\3\26\3\27\3\27\3\30\3\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\7\32\u017c"+
		"\n\32\f\32\16\32\u017f\13\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\5"+
		"\34\u0189\n\34\3\35\3\35\5\35\u018d\n\35\3\35\3\35\3\35\3\35\3\36\3\36"+
		"\3\36\3\37\3\37\5\37\u0198\n\37\3\37\3\37\3\37\3\37\3\37\3\37\7\37\u01a0"+
		"\n\37\f\37\16\37\u01a3\13\37\5\37\u01a5\n\37\3\37\3\37\3\37\3\37\3 \3"+
		" \3 \3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\5\"\u01b6\n\"\3#\3#\3#\3$\3$\3%\3%"+
		"\5%\u01bf\n%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\5(\u01d1"+
		"\n(\3(\5(\u01d4\n(\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\5,\u01e0\n,\3,\7,\u01e3"+
		"\n,\f,\16,\u01e6\13,\3,\5,\u01e9\n,\3,\5,\u01ec\n,\3,\5,\u01ef\n,\3-\3"+
		"-\5-\u01f3\n-\3.\3.\3/\3/\3/\3/\7/\u01fb\n/\f/\16/\u01fe\13/\3\60\3\60"+
		"\3\60\3\60\7\60\u0204\n\60\f\60\16\60\u0207\13\60\3\61\3\61\3\61\3\61"+
		"\3\62\3\62\3\62\3\63\3\63\5\63\u0212\n\63\3\63\3\63\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\7\64\u021c\n\64\f\64\16\64\u021f\13\64\5\64\u0221\n\64\3\65"+
		"\3\65\3\66\3\66\5\66\u0227\n\66\3\67\3\67\3\67\7\67\u022c\n\67\f\67\16"+
		"\67\u022f\13\67\3\67\3\67\38\38\38\38\38\38\38\38\38\38\38\38\38\38\3"+
		"8\38\38\38\38\38\38\38\38\38\38\38\38\58\u024e\n8\38\38\38\38\38\38\3"+
		"8\38\38\38\38\38\38\58\u025d\n8\38\38\38\38\38\38\38\38\38\38\38\38\3"+
		"8\58\u026c\n8\38\38\38\38\38\38\58\u0274\n8\38\38\38\38\38\78\u027b\n"+
		"8\f8\168\u027e\138\39\39\3:\3:\3:\3:\5:\u0286\n:\3;\3;\3<\3<\3<\3<\3<"+
		"\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<"+
		"\3<\3<\3<\3<\5<\u02aa\n<\3<\3<\3<\3<\3<\7<\u02b1\n<\f<\16<\u02b4\13<\5"+
		"<\u02b6\n<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\5<\u02ca"+
		"\n<\3<\6<\u02cd\n<\r<\16<\u02ce\3<\3<\3<\3<\3<\3<\5<\u02d7\n<\3<\3<\3"+
		"<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\7<\u02ea\n<\f<\16<\u02ed\13"+
		"<\3=\3=\3=\3=\3=\3>\3>\3>\3?\3?\3@\3@\3A\3A\5A\u02fd\nA\3B\5B\u0300\n"+
		"B\3B\3B\5B\u0304\nB\3B\3B\5B\u0308\nB\3B\5B\u030b\nB\3B\5B\u030e\nB\3"+
		"B\3B\5B\u0312\nB\3B\5B\u0315\nB\3B\5B\u0318\nB\3B\5B\u031b\nB\3B\3B\5"+
		"B\u031f\nB\3B\5B\u0322\nB\3B\5B\u0325\nB\3B\3B\5B\u0329\nB\3B\5B\u032c"+
		"\nB\3B\5B\u032f\nB\3B\5B\u0332\nB\3B\3B\3B\3B\5B\u0338\nB\3B\3B\5B\u033c"+
		"\nB\3B\5B\u033f\nB\3B\3B\5B\u0343\nB\3B\5B\u0346\nB\3B\3B\5B\u034a\nB"+
		"\3B\3B\5B\u034e\nB\5B\u0350\nB\3C\3C\3C\3C\3C\3C\3C\3C\3C\3C\3C\3C\5C"+
		"\u035e\nC\3D\3D\3D\3D\3D\3D\3D\3E\5E\u0368\nE\3E\3E\3E\3E\3E\7E\u036f"+
		"\nE\fE\16E\u0372\13E\5E\u0374\nE\3E\3E\3F\3F\3F\3F\3G\3G\3G\3G\3G\3G\7"+
		"G\u0382\nG\fG\16G\u0385\13G\5G\u0387\nG\3G\3G\3H\3H\3H\3H\3I\3I\3I\3I"+
		"\3I\5I\u0394\nI\5I\u0396\nI\3I\3I\3I\3I\7I\u039c\nI\fI\16I\u039f\13I\5"+
		"I\u03a1\nI\3I\3I\3J\3J\3J\3K\3K\3K\3K\3K\5K\u03ad\nK\3L\3L\3L\3L\3L\7"+
		"L\u03b4\nL\fL\16L\u03b7\13L\3L\3L\5L\u03bb\nL\3M\3M\3M\3M\3M\3M\5M\u03c3"+
		"\nM\3N\3N\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\5S\u03d1\nS\3T\3T\3T\5T\u03d6"+
		"\nT\3U\3U\3U\2\4nvV\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60"+
		"\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086"+
		"\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e"+
		"\u00a0\u00a2\u00a4\u00a6\u00a8\2\34\3\2\b\t\3\2*+\3\2.\61\4\2\30\31>?"+
		"\3\2@C\4\2%%DD\3\2EF\3\2GI\3\2\64\66\4\2\62\62\67\67\3\2JQ\3\2U\\\3\2"+
		"_`\3\2ab\3\2ij\4\2++tu\3\2lo\3\2wx\3\2yz\3\2{}\3\2\u0080\u0081\3\2\u0082"+
		"\u0083\4\2\37\37$$\4\2  &&\3\2\65\66\b\2\4\4\24\24\26\26RT\u0087\u008a"+
		"\u008e\u008e\u0429\2\u00ab\3\2\2\2\4\u00dc\3\2\2\2\6\u00e2\3\2\2\2\b\u00e8"+
		"\3\2\2\2\n\u00f1\3\2\2\2\f\u00f3\3\2\2\2\16\u00f6\3\2\2\2\20\u0102\3\2"+
		"\2\2\22\u010d\3\2\2\2\24\u011a\3\2\2\2\26\u0129\3\2\2\2\30\u012d\3\2\2"+
		"\2\32\u0130\3\2\2\2\34\u013c\3\2\2\2\36\u0151\3\2\2\2 \u0155\3\2\2\2\""+
		"\u0157\3\2\2\2$\u0159\3\2\2\2&\u015b\3\2\2\2(\u0161\3\2\2\2*\u0166\3\2"+
		"\2\2,\u016a\3\2\2\2.\u016c\3\2\2\2\60\u0171\3\2\2\2\62\u0176\3\2\2\2\64"+
		"\u0182\3\2\2\2\66\u0188\3\2\2\28\u018a\3\2\2\2:\u0192\3\2\2\2<\u0195\3"+
		"\2\2\2>\u01aa\3\2\2\2@\u01ad\3\2\2\2B\u01b5\3\2\2\2D\u01b7\3\2\2\2F\u01ba"+
		"\3\2\2\2H\u01be\3\2\2\2J\u01c0\3\2\2\2L\u01c5\3\2\2\2N\u01ca\3\2\2\2P"+
		"\u01d7\3\2\2\2R\u01d9\3\2\2\2T\u01db\3\2\2\2V\u01dd\3\2\2\2X\u01f2\3\2"+
		"\2\2Z\u01f4\3\2\2\2\\\u01f6\3\2\2\2^\u01ff\3\2\2\2`\u0208\3\2\2\2b\u020c"+
		"\3\2\2\2d\u020f\3\2\2\2f\u0215\3\2\2\2h\u0222\3\2\2\2j\u0224\3\2\2\2l"+
		"\u022d\3\2\2\2n\u024d\3\2\2\2p\u027f\3\2\2\2r\u0285\3\2\2\2t\u0287\3\2"+
		"\2\2v\u02d6\3\2\2\2x\u02ee\3\2\2\2z\u02f3\3\2\2\2|\u02f6\3\2\2\2~\u02f8"+
		"\3\2\2\2\u0080\u02fa\3\2\2\2\u0082\u034f\3\2\2\2\u0084\u035d\3\2\2\2\u0086"+
		"\u035f\3\2\2\2\u0088\u0367\3\2\2\2\u008a\u0377\3\2\2\2\u008c\u037b\3\2"+
		"\2\2\u008e\u038a\3\2\2\2\u0090\u0395\3\2\2\2\u0092\u03a4\3\2\2\2\u0094"+
		"\u03a7\3\2\2\2\u0096\u03ae\3\2\2\2\u0098\u03c2\3\2\2\2\u009a\u03c4\3\2"+
		"\2\2\u009c\u03c6\3\2\2\2\u009e\u03c8\3\2\2\2\u00a0\u03ca\3\2\2\2\u00a2"+
		"\u03cc\3\2\2\2\u00a4\u03ce\3\2\2\2\u00a6\u03d5\3\2\2\2\u00a8\u03d7\3\2"+
		"\2\2\u00aa\u00ac\5\4\3\2\u00ab\u00aa\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac"+
		"\u00b0\3\2\2\2\u00ad\u00af\5\6\4\2\u00ae\u00ad\3\2\2\2\u00af\u00b2\3\2"+
		"\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b6\3\2\2\2\u00b2"+
		"\u00b0\3\2\2\2\u00b3\u00b5\5\b\5\2\u00b4\u00b3\3\2\2\2\u00b5\u00b8\3\2"+
		"\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00bc\3\2\2\2\u00b8"+
		"\u00b6\3\2\2\2\u00b9\u00bb\5\20\t\2\u00ba\u00b9\3\2\2\2\u00bb\u00be\3"+
		"\2\2\2\u00bc\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00c2\3\2\2\2\u00be"+
		"\u00bc\3\2\2\2\u00bf\u00c1\5\22\n\2\u00c0\u00bf\3\2\2\2\u00c1\u00c4\3"+
		"\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c8\3\2\2\2\u00c4"+
		"\u00c2\3\2\2\2\u00c5\u00c7\5\32\16\2\u00c6\u00c5\3\2\2\2\u00c7\u00ca\3"+
		"\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00ce\3\2\2\2\u00ca"+
		"\u00c8\3\2\2\2\u00cb\u00cd\5\34\17\2\u00cc\u00cb\3\2\2\2\u00cd\u00d0\3"+
		"\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d4\3\2\2\2\u00d0"+
		"\u00ce\3\2\2\2\u00d1\u00d3\5\16\b\2\u00d2\u00d1\3\2\2\2\u00d3\u00d6\3"+
		"\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d8\3\2\2\2\u00d6"+
		"\u00d4\3\2\2\2\u00d7\u00d9\5\66\34\2\u00d8\u00d7\3\2\2\2\u00d9\u00da\3"+
		"\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\3\3\2\2\2\u00dc\u00dd"+
		"\7\3\2\2\u00dd\u00e0\5\u00a8U\2\u00de\u00df\7\4\2\2\u00df\u00e1\5$\23"+
		"\2\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\5\3\2\2\2\u00e2\u00e3"+
		"\7\5\2\2\u00e3\u00e6\5,\27\2\u00e4\u00e5\7\4\2\2\u00e5\u00e7\5$\23\2\u00e6"+
		"\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\7\3\2\2\2\u00e8\u00e9\7\6\2\2"+
		"\u00e9\u00ec\5\u00a8U\2\u00ea\u00eb\7\4\2\2\u00eb\u00ed\5$\23\2\u00ec"+
		"\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00ef\7\7"+
		"\2\2\u00ef\u00f0\5\n\6\2\u00f0\t\3\2\2\2\u00f1\u00f2\5\u00a8U\2\u00f2"+
		"\13\3\2\2\2\u00f3\u00f4\t\2\2\2\u00f4\r\3\2\2\2\u00f5\u00f7\5\f\7\2\u00f6"+
		"\u00f5\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\7\n"+
		"\2\2\u00f9\u00fb\5\u00a8U\2\u00fa\u00fc\5(\25\2\u00fb\u00fa\3\2\2\2\u00fb"+
		"\u00fc\3\2\2\2\u00fc\u00ff\3\2\2\2\u00fd\u00fe\7\13\2\2\u00fe\u0100\5"+
		"n8\2\u00ff\u00fd\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\17\3\2\2\2\u0101\u0103"+
		"\5\f\7\2\u0102\u0101\3\2\2\2\u0102\u0103\3\2\2\2\u0103\u0104\3\2\2\2\u0104"+
		"\u0105\7\f\2\2\u0105\u0106\5\u00a8U\2\u0106\u0107\7\r\2\2\u0107\u010a"+
		"\5 \21\2\u0108\u0109\7\4\2\2\u0109\u010b\5$\23\2\u010a\u0108\3\2\2\2\u010a"+
		"\u010b\3\2\2\2\u010b\21\3\2\2\2\u010c\u010e\5\f\7\2\u010d\u010c\3\2\2"+
		"\2\u010d\u010e\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0110\7\16\2\2\u0110"+
		"\u0111\5\u00a8U\2\u0111\u0112\7\r\2\2\u0112\u0115\5\"\22\2\u0113\u0114"+
		"\7\4\2\2\u0114\u0116\5$\23\2\u0115\u0113\3\2\2\2\u0115\u0116\3\2\2\2\u0116"+
		"\u0118\3\2\2\2\u0117\u0119\5\24\13\2\u0118\u0117\3\2\2\2\u0118\u0119\3"+
		"\2\2\2\u0119\23\3\2\2\2\u011a\u011b\7\17\2\2\u011b\u011c\7\20\2\2\u011c"+
		"\u0121\5\26\f\2\u011d\u011e\7\21\2\2\u011e\u0120\5\26\f\2\u011f\u011d"+
		"\3\2\2\2\u0120\u0123\3\2\2\2\u0121\u011f\3\2\2\2\u0121\u0122\3\2\2\2\u0122"+
		"\u0124\3\2\2\2\u0123\u0121\3\2\2\2\u0124\u0125\7\22\2\2\u0125\25\3\2\2"+
		"\2\u0126\u0127\5\30\r\2\u0127\u0128\7\23\2\2\u0128\u012a\3\2\2\2\u0129"+
		"\u0126\3\2\2\2\u0129\u012a\3\2\2\2\u012a\u012b\3\2\2\2\u012b\u012c\5\u00a8"+
		"U\2\u012c\27\3\2\2\2\u012d\u012e\5\u00a8U\2\u012e\31\3\2\2\2\u012f\u0131"+
		"\5\f\7\2\u0130\u012f\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0132\3\2\2\2\u0132"+
		"\u0133\7\24\2\2\u0133\u0134\5\u00a8U\2\u0134\u0135\7\r\2\2\u0135\u0136"+
		"\5&\24\2\u0136\u0137\7\25\2\2\u0137\u0139\5\26\f\2\u0138\u013a\5\u0092"+
		"J\2\u0139\u0138\3\2\2\2\u0139\u013a\3\2\2\2\u013a\33\3\2\2\2\u013b\u013d"+
		"\5\f\7\2\u013c\u013b\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013e\3\2\2\2\u013e"+
		"\u013f\7\26\2\2\u013f\u0140\5\u00a8U\2\u0140\u0141\7\r\2\2\u0141\u0142"+
		"\7\20\2\2\u0142\u0147\5\36\20\2\u0143\u0144\7\21\2\2\u0144\u0146\5\36"+
		"\20\2\u0145\u0143\3\2\2\2\u0146\u0149\3\2\2\2\u0147\u0145\3\2\2\2\u0147"+
		"\u0148\3\2\2\2\u0148\u014a\3\2\2\2\u0149\u0147\3\2\2\2\u014a\u014c\7\22"+
		"\2\2\u014b\u014d\5\u0092J\2\u014c\u014b\3\2\2\2\u014c\u014d\3\2\2\2\u014d"+
		"\35\3\2\2\2\u014e\u014f\5\30\r\2\u014f\u0150\7\23\2\2\u0150\u0152\3\2"+
		"\2\2\u0151\u014e\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0153\3\2\2\2\u0153"+
		"\u0154\5\u00a8U\2\u0154\37\3\2\2\2\u0155\u0156\7\u008f\2\2\u0156!\3\2"+
		"\2\2\u0157\u0158\7\u008f\2\2\u0158#\3\2\2\2\u0159\u015a\7\u008f\2\2\u015a"+
		"%\3\2\2\2\u015b\u015c\7\u008f\2\2\u015c\'\3\2\2\2\u015d\u0162\5*\26\2"+
		"\u015e\u0162\5.\30\2\u015f\u0162\5\60\31\2\u0160\u0162\5\62\32\2\u0161"+
		"\u015d\3\2\2\2\u0161\u015e\3\2\2\2\u0161\u015f\3\2\2\2\u0161\u0160\3\2"+
		"\2\2\u0162)\3\2\2\2\u0163\u0164\5,\27\2\u0164\u0165\7\23\2\2\u0165\u0167"+
		"\3\2\2\2\u0166\u0163\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u0168\3\2\2\2\u0168"+
		"\u0169\5\u00a8U\2\u0169+\3\2\2\2\u016a\u016b\5\u00a8U\2\u016b-\3\2\2\2"+
		"\u016c\u016d\7\27\2\2\u016d\u016e\7\30\2\2\u016e\u016f\5(\25\2\u016f\u0170"+
		"\7\31\2\2\u0170/\3\2\2\2\u0171\u0172\7\32\2\2\u0172\u0173\7\30\2\2\u0173"+
		"\u0174\5(\25\2\u0174\u0175\7\31\2\2\u0175\61\3\2\2\2\u0176\u0177\7\33"+
		"\2\2\u0177\u0178\7\20\2\2\u0178\u017d\5\64\33\2\u0179\u017a\7\21\2\2\u017a"+
		"\u017c\5\64\33\2\u017b\u0179\3\2\2\2\u017c\u017f\3\2\2\2\u017d\u017b\3"+
		"\2\2\2\u017d\u017e\3\2\2\2\u017e\u0180\3\2\2\2\u017f\u017d\3\2\2\2\u0180"+
		"\u0181\7\22\2\2\u0181\63\3\2\2\2\u0182\u0183\5\u00a8U\2\u0183\u0184\5"+
		"(\25\2\u0184\65\3\2\2\2\u0185\u0189\58\35\2\u0186\u0189\5:\36\2\u0187"+
		"\u0189\5<\37\2\u0188\u0185\3\2\2\2\u0188\u0186\3\2\2\2\u0188\u0187\3\2"+
		"\2\2\u0189\67\3\2\2\2\u018a\u018c\7\34\2\2\u018b\u018d\5\f\7\2\u018c\u018b"+
		"\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e\3\2\2\2\u018e\u018f\5\u00a8U"+
		"\2\u018f\u0190\7\r\2\2\u0190\u0191\5n8\2\u01919\3\2\2\2\u0192\u0193\7"+
		"\35\2\2\u0193\u0194\5\u00a8U\2\u0194;\3\2\2\2\u0195\u0197\7\34\2\2\u0196"+
		"\u0198\5\f\7\2\u0197\u0196\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0199\3\2"+
		"\2\2\u0199\u019a\7\36\2\2\u019a\u019b\5\u00a8U\2\u019b\u01a4\7\37\2\2"+
		"\u019c\u01a1\5> \2\u019d\u019e\7\21\2\2\u019e\u01a0\5> \2\u019f\u019d"+
		"\3\2\2\2\u01a0\u01a3\3\2\2\2\u01a1\u019f\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2"+
		"\u01a5\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a4\u019c\3\2\2\2\u01a4\u01a5\3\2"+
		"\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01a7\7 \2\2\u01a7\u01a8\7\r\2\2\u01a8"+
		"\u01a9\5@!\2\u01a9=\3\2\2\2\u01aa\u01ab\5\u00a8U\2\u01ab\u01ac\5(\25\2"+
		"\u01ac?\3\2\2\2\u01ad\u01ae\5n8\2\u01aeA\3\2\2\2\u01af\u01b6\5N(\2\u01b0"+
		"\u01b6\5l\67\2\u01b1\u01b2\7\37\2\2\u01b2\u01b3\5n8\2\u01b3\u01b4\7 \2"+
		"\2\u01b4\u01b6\3\2\2\2\u01b5\u01af\3\2\2\2\u01b5\u01b0\3\2\2\2\u01b5\u01b1"+
		"\3\2\2\2\u01b6C\3\2\2\2\u01b7\u01b8\5B\"\2\u01b8\u01b9\5F$\2\u01b9E\3"+
		"\2\2\2\u01ba\u01bb\5\u00a8U\2\u01bbG\3\2\2\2\u01bc\u01bf\5J&\2\u01bd\u01bf"+
		"\5L\'\2\u01be\u01bc\3\2\2\2\u01be\u01bd\3\2\2\2\u01bfI\3\2\2\2\u01c0\u01c1"+
		"\7!\2\2\u01c1\u01c2\5D#\2\u01c2\u01c3\7\"\2\2\u01c3\u01c4\5n8\2\u01c4"+
		"K\3\2\2\2\u01c5\u01c6\7#\2\2\u01c6\u01c7\5D#\2\u01c7\u01c8\7\"\2\2\u01c8"+
		"\u01c9\5n8\2\u01c9M\3\2\2\2\u01ca\u01cb\7$\2\2\u01cb\u01d3\5*\26\2\u01cc"+
		"\u01d0\7\r\2\2\u01cd\u01ce\5P)\2\u01ce\u01cf\7%\2\2\u01cf\u01d1\3\2\2"+
		"\2\u01d0\u01cd\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d4"+
		"\5R*\2\u01d3\u01cc\3\2\2\2\u01d3\u01d4\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5"+
		"\u01d6\7&\2\2\u01d6O\3\2\2\2\u01d7\u01d8\5\u00a8U\2\u01d8Q\3\2\2\2\u01d9"+
		"\u01da\5l\67\2\u01daS\3\2\2\2\u01db\u01dc\5\u00a8U\2\u01dcU\3\2\2\2\u01dd"+
		"\u01df\5X-\2\u01de\u01e0\5^\60\2\u01df\u01de\3\2\2\2\u01df\u01e0\3\2\2"+
		"\2\u01e0\u01e4\3\2\2\2\u01e1\u01e3\5H%\2\u01e2\u01e1\3\2\2\2\u01e3\u01e6"+
		"\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e4\u01e5\3\2\2\2\u01e5\u01e8\3\2\2\2\u01e6"+
		"\u01e4\3\2\2\2\u01e7\u01e9\5b\62\2\u01e8\u01e7\3\2\2\2\u01e8\u01e9\3\2"+
		"\2\2\u01e9\u01eb\3\2\2\2\u01ea\u01ec\5d\63\2\u01eb\u01ea\3\2\2\2\u01eb"+
		"\u01ec\3\2\2\2\u01ec\u01ee\3\2\2\2\u01ed\u01ef\5f\64\2\u01ee\u01ed\3\2"+
		"\2\2\u01ee\u01ef\3\2\2\2\u01efW\3\2\2\2\u01f0\u01f3\5Z.\2\u01f1\u01f3"+
		"\5\\/\2\u01f2\u01f0\3\2\2\2\u01f2\u01f1\3\2\2\2\u01f3Y\3\2\2\2\u01f4\u01f5"+
		"\5D#\2\u01f5[\3\2\2\2\u01f6\u01f7\7\25\2\2\u01f7\u01fc\5D#\2\u01f8\u01f9"+
		"\7\21\2\2\u01f9\u01fb\5D#\2\u01fa\u01f8\3\2\2\2\u01fb\u01fe\3\2\2\2\u01fc"+
		"\u01fa\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd]\3\2\2\2\u01fe\u01fc\3\2\2\2"+
		"\u01ff\u0200\7\'\2\2\u0200\u0205\5`\61\2\u0201\u0202\7\21\2\2\u0202\u0204"+
		"\5`\61\2\u0203\u0201\3\2\2\2\u0204\u0207\3\2\2\2\u0205\u0203\3\2\2\2\u0205"+
		"\u0206\3\2\2\2\u0206_\3\2\2\2\u0207\u0205\3\2\2\2\u0208\u0209\5\u00a8"+
		"U\2\u0209\u020a\7\r\2\2\u020a\u020b\5n8\2\u020ba\3\2\2\2\u020c\u020d\7"+
		"(\2\2\u020d\u020e\5n8\2\u020ec\3\2\2\2\u020f\u0211\7)\2\2\u0210\u0212"+
		"\t\3\2\2\u0211\u0210\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0213\3\2\2\2\u0213"+
		"\u0214\5n8\2\u0214e\3\2\2\2\u0215\u0220\7,\2\2\u0216\u0221\5h\65\2\u0217"+
		"\u0218\7-\2\2\u0218\u021d\5j\66\2\u0219\u021a\7\21\2\2\u021a\u021c\5j"+
		"\66\2\u021b\u0219\3\2\2\2\u021c\u021f\3\2\2\2\u021d\u021b\3\2\2\2\u021d"+
		"\u021e\3\2\2\2\u021e\u0221\3\2\2\2\u021f\u021d\3\2\2\2\u0220\u0216\3\2"+
		"\2\2\u0220\u0217\3\2\2\2\u0221g\3\2\2\2\u0222\u0223\t\4\2\2\u0223i\3\2"+
		"\2\2\u0224\u0226\5v<\2\u0225\u0227\5h\65\2\u0226\u0225\3\2\2\2\u0226\u0227"+
		"\3\2\2\2\u0227k\3\2\2\2\u0228\u0229\5T+\2\u0229\u022a\7\23\2\2\u022a\u022c"+
		"\3\2\2\2\u022b\u0228\3\2\2\2\u022c\u022f\3\2\2\2\u022d\u022b\3\2\2\2\u022d"+
		"\u022e\3\2\2\2\u022e\u0230\3\2\2\2\u022f\u022d\3\2\2\2\u0230\u0231\5\u00a8"+
		"U\2\u0231m\3\2\2\2\u0232\u0233\b8\1\2\u0233\u0234\7\63\2\2\u0234\u024e"+
		"\5n8\16\u0235\u0236\79\2\2\u0236\u024e\5n8\r\u0237\u024e\5v<\2\u0238\u024e"+
		"\5N(\2\u0239\u024e\5V,\2\u023a\u023b\78\2\2\u023b\u023c\5n8\2\u023c\u023d"+
		"\7\67\2\2\u023d\u023e\5(\25\2\u023e\u024e\3\2\2\2\u023f\u0240\5t;\2\u0240"+
		"\u0241\7;\2\2\u0241\u0242\5v<\2\u0242\u0243\7<\2\2\u0243\u0244\5v<\2\u0244"+
		"\u024e\3\2\2\2\u0245\u0246\7=\2\2\u0246\u0247\7%\2\2\u0247\u0248\5t;\2"+
		"\u0248\u0249\7;\2\2\u0249\u024a\5v<\2\u024a\u024b\7<\2\2\u024b\u024c\5"+
		"v<\2\u024c\u024e\3\2\2\2\u024d\u0232\3\2\2\2\u024d\u0235\3\2\2\2\u024d"+
		"\u0237\3\2\2\2\u024d\u0238\3\2\2\2\u024d\u0239\3\2\2\2\u024d\u023a\3\2"+
		"\2\2\u024d\u023f\3\2\2\2\u024d\u0245\3\2\2\2\u024e\u027c\3\2\2\2\u024f"+
		"\u0250\f\t\2\2\u0250\u0251\t\5\2\2\u0251\u027b\5n8\n\u0252\u0253\f\b\2"+
		"\2\u0253\u0254\5\u0082B\2\u0254\u0255\5n8\t\u0255\u027b\3\2\2\2\u0256"+
		"\u0257\f\7\2\2\u0257\u0258\t\6\2\2\u0258\u027b\5n8\b\u0259\u025a\f\6\2"+
		"\2\u025a\u025c\t\7\2\2\u025b\u025d\5z>\2\u025c\u025b\3\2\2\2\u025c\u025d"+
		"\3\2\2\2\u025d\u025e\3\2\2\2\u025e\u027b\5n8\7\u025f\u0260\f\5\2\2\u0260"+
		"\u0261\7<\2\2\u0261\u027b\5n8\6\u0262\u0263\f\4\2\2\u0263\u0264\t\b\2"+
		"\2\u0264\u027b\5n8\5\u0265\u0266\f\3\2\2\u0266\u0267\t\t\2\2\u0267\u027b"+
		"\5n8\4\u0268\u0269\f\21\2\2\u0269\u026b\7\62\2\2\u026a\u026c\7\63\2\2"+
		"\u026b\u026a\3\2\2\2\u026b\u026c\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u027b"+
		"\t\n\2\2\u026e\u026f\f\20\2\2\u026f\u0270\t\13\2\2\u0270\u027b\5(\25\2"+
		"\u0271\u0273\f\f\2\2\u0272\u0274\7:\2\2\u0273\u0272\3\2\2\2\u0273\u0274"+
		"\3\2\2\2\u0274\u0275\3\2\2\2\u0275\u0276\7;\2\2\u0276\u0277\5v<\2\u0277"+
		"\u0278\7<\2\2\u0278\u0279\5v<\2\u0279\u027b\3\2\2\2\u027a\u024f\3\2\2"+
		"\2\u027a\u0252\3\2\2\2\u027a\u0256\3\2\2\2\u027a\u0259\3\2\2\2\u027a\u025f"+
		"\3\2\2\2\u027a\u0262\3\2\2\2\u027a\u0265\3\2\2\2\u027a\u0268\3\2\2\2\u027a"+
		"\u026e\3\2\2\2\u027a\u0271\3\2\2\2\u027b\u027e\3\2\2\2\u027c\u027a\3\2"+
		"\2\2\u027c\u027d\3\2\2\2\u027do\3\2\2\2\u027e\u027c\3\2\2\2\u027f\u0280"+
		"\t\f\2\2\u0280q\3\2\2\2\u0281\u0286\5p9\2\u0282\u0286\7R\2\2\u0283\u0286"+
		"\7S\2\2\u0284\u0286\7T\2\2\u0285\u0281\3\2\2\2\u0285\u0282\3\2\2\2\u0285"+
		"\u0283\3\2\2\2\u0285\u0284\3\2\2\2\u0286s\3\2\2\2\u0287\u0288\t\r\2\2"+
		"\u0288u\3\2\2\2\u0289\u028a\b<\1\2\u028a\u028b\t\16\2\2\u028b\u02d7\5"+
		"v<\21\u028c\u028d\t\17\2\2\u028d\u028e\7c\2\2\u028e\u02d7\5v<\20\u028f"+
		"\u0290\5r:\2\u0290\u0291\7\25\2\2\u0291\u0292\5v<\17\u0292\u02d7\3\2\2"+
		"\2\u0293\u0294\7d\2\2\u0294\u0295\7%\2\2\u0295\u0296\5t;\2\u0296\u0297"+
		"\7c\2\2\u0297\u0298\5v<\16\u0298\u02d7\3\2\2\2\u0299\u029a\7e\2\2\u029a"+
		"\u029b\7c\2\2\u029b\u02d7\5v<\r\u029c\u029d\7f\2\2\u029d\u029e\7c\2\2"+
		"\u029e\u02d7\5v<\f\u029f\u02a0\7g\2\2\u02a0\u02a1\7c\2\2\u02a1\u02d7\5"+
		"v<\13\u02a2\u02a3\7h\2\2\u02a3\u02a4\7\25\2\2\u02a4\u02d7\5v<\n\u02a5"+
		"\u02d7\5\u0084C\2\u02a6\u02a7\5T+\2\u02a7\u02a8\7\23\2\2\u02a8\u02aa\3"+
		"\2\2\2\u02a9\u02a6\3\2\2\2\u02a9\u02aa\3\2\2\2\u02aa\u02ab\3\2\2\2\u02ab"+
		"\u02ac\5\u00a8U\2\u02ac\u02b5\7\37\2\2\u02ad\u02b2\5n8\2\u02ae\u02af\7"+
		"\21\2\2\u02af\u02b1\5n8\2\u02b0\u02ae\3\2\2\2\u02b1\u02b4\3\2\2\2\u02b2"+
		"\u02b0\3\2\2\2\u02b2\u02b3\3\2\2\2\u02b3\u02b6\3\2\2\2\u02b4\u02b2\3\2"+
		"\2\2\u02b5\u02ad\3\2\2\2\u02b5\u02b6\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7"+
		"\u02b8\7 \2\2\u02b8\u02d7\3\2\2\2\u02b9\u02ba\7]\2\2\u02ba\u02bb\5n8\2"+
		"\u02bb\u02bc\7^\2\2\u02bc\u02bd\5(\25\2\u02bd\u02d7\3\2\2\2\u02be\u02bf"+
		"\t\20\2\2\u02bf\u02d7\5*\26\2\u02c0\u02c1\7p\2\2\u02c1\u02c2\5n8\2\u02c2"+
		"\u02c3\7q\2\2\u02c3\u02c4\5n8\2\u02c4\u02c5\7r\2\2\u02c5\u02c6\5n8\2\u02c6"+
		"\u02d7\3\2\2\2\u02c7\u02c9\7s\2\2\u02c8\u02ca\5n8\2\u02c9\u02c8\3\2\2"+
		"\2\u02c9\u02ca\3\2\2\2\u02ca\u02cc\3\2\2\2\u02cb\u02cd\5x=\2\u02cc\u02cb"+
		"\3\2\2\2\u02cd\u02ce\3\2\2\2\u02ce\u02cc\3\2\2\2\u02ce\u02cf\3\2\2\2\u02cf"+
		"\u02d0\3\2\2\2\u02d0\u02d1\7r\2\2\u02d1\u02d2\5n8\2\u02d2\u02d3\7b\2\2"+
		"\u02d3\u02d7\3\2\2\2\u02d4\u02d5\t\21\2\2\u02d5\u02d7\5n8\2\u02d6\u0289"+
		"\3\2\2\2\u02d6\u028c\3\2\2\2\u02d6\u028f\3\2\2\2\u02d6\u0293\3\2\2\2\u02d6"+
		"\u0299\3\2\2\2\u02d6\u029c\3\2\2\2\u02d6\u029f\3\2\2\2\u02d6\u02a2\3\2"+
		"\2\2\u02d6\u02a5\3\2\2\2\u02d6\u02a9\3\2\2\2\u02d6\u02b9\3\2\2\2\u02d6"+
		"\u02be\3\2\2\2\u02d6\u02c0\3\2\2\2\u02d6\u02c7\3\2\2\2\u02d6\u02d4\3\2"+
		"\2\2\u02d7\u02eb\3\2\2\2\u02d8\u02d9\f\b\2\2\u02d9\u02da\7k\2\2\u02da"+
		"\u02ea\5v<\t\u02db\u02dc\f\7\2\2\u02dc\u02dd\t\22\2\2\u02dd\u02ea\5v<"+
		"\b\u02de\u02df\f\6\2\2\u02df\u02e0\t\16\2\2\u02e0\u02ea\5v<\7\u02e1\u02e2"+
		"\f\25\2\2\u02e2\u02e3\7\23\2\2\u02e3\u02ea\5\u00a8U\2\u02e4\u02e5\f\24"+
		"\2\2\u02e5\u02e6\7$\2\2\u02e6\u02e7\5n8\2\u02e7\u02e8\7&\2\2\u02e8\u02ea"+
		"\3\2\2\2\u02e9\u02d8\3\2\2\2\u02e9\u02db\3\2\2\2\u02e9\u02de\3\2\2\2\u02e9"+
		"\u02e1\3\2\2\2\u02e9\u02e4\3\2\2\2\u02ea\u02ed\3\2\2\2\u02eb\u02e9\3\2"+
		"\2\2\u02eb\u02ec\3\2\2\2\u02ecw\3\2\2\2\u02ed\u02eb\3\2\2\2\u02ee\u02ef"+
		"\7v\2\2\u02ef\u02f0\5n8\2\u02f0\u02f1\7q\2\2\u02f1\u02f2\5n8\2\u02f2y"+
		"\3\2\2\2\u02f3\u02f4\5p9\2\u02f4\u02f5\7c\2\2\u02f5{\3\2\2\2\u02f6\u02f7"+
		"\t\23\2\2\u02f7}\3\2\2\2\u02f8\u02f9\t\24\2\2\u02f9\177\3\2\2\2\u02fa"+
		"\u02fc\5\u00a4S\2\u02fb\u02fd\5~@\2\u02fc\u02fb\3\2\2\2\u02fc\u02fd\3"+
		"\2\2\2\u02fd\u0081\3\2\2\2\u02fe\u0300\t\25\2\2\u02ff\u02fe\3\2\2\2\u02ff"+
		"\u0300\3\2\2\2\u0300\u0301\3\2\2\2\u0301\u0303\7~\2\2\u0302\u0304\5p9"+
		"\2\u0303\u0302\3\2\2\2\u0303\u0304\3\2\2\2\u0304\u0307\3\2\2\2\u0305\u0308"+
		"\5|?\2\u0306\u0308\7\67\2\2\u0307\u0305\3\2\2\2\u0307\u0306\3\2\2\2\u0308"+
		"\u030a\3\2\2\2\u0309\u030b\t\17\2\2\u030a\u0309\3\2\2\2\u030a\u030b\3"+
		"\2\2\2\u030b\u0350\3\2\2\2\u030c\u030e\7:\2\2\u030d\u030c\3\2\2\2\u030d"+
		"\u030e\3\2\2\2\u030e\u030f\3\2\2\2\u030f\u0311\7\177\2\2\u0310\u0312\5"+
		"z>\2\u0311\u0310\3\2\2\2\u0311\u0312\3\2\2\2\u0312\u0314\3\2\2\2\u0313"+
		"\u0315\t\17\2\2\u0314\u0313\3\2\2\2\u0314\u0315\3\2\2\2\u0315\u0350\3"+
		"\2\2\2\u0316\u0318\t\25\2\2\u0317\u0316\3\2\2\2\u0317\u0318\3\2\2\2\u0318"+
		"\u031a\3\2\2\2\u0319\u031b\7:\2\2\u031a\u0319\3\2\2\2\u031a\u031b\3\2"+
		"\2\2\u031b\u031c\3\2\2\2\u031c\u031e\t\26\2\2\u031d\u031f\5z>\2\u031e"+
		"\u031d\3\2\2\2\u031e\u031f\3\2\2\2\u031f\u0350\3\2\2\2\u0320\u0322\t\25"+
		"\2\2\u0321\u0320\3\2\2\2\u0321\u0322\3\2\2\2\u0322\u0324\3\2\2\2\u0323"+
		"\u0325\5\u0080A\2\u0324\u0323\3\2\2\2\u0324\u0325\3\2\2\2\u0325\u0326"+
		"\3\2\2\2\u0326\u0328\t\27\2\2\u0327\u0329\5z>\2\u0328\u0327\3\2\2\2\u0328"+
		"\u0329\3\2\2\2\u0329\u032b\3\2\2\2\u032a\u032c\t\17\2\2\u032b\u032a\3"+
		"\2\2\2\u032b\u032c\3\2\2\2\u032c\u0350\3\2\2\2\u032d\u032f\t\25\2\2\u032e"+
		"\u032d\3\2\2\2\u032e\u032f\3\2\2\2\u032f\u0331\3\2\2\2\u0330\u0332\7:"+
		"\2\2\u0331\u0330\3\2\2\2\u0331\u0332\3\2\2\2\u0332\u0333\3\2\2\2\u0333"+
		"\u0334\7\u0084\2\2\u0334\u0335\5\u00a4S\2\u0335\u0337\7c\2\2\u0336\u0338"+
		"\t\17\2\2\u0337\u0336\3\2\2\2\u0337\u0338\3\2\2\2\u0338\u0350\3\2\2\2"+
		"\u0339\u033b\7\u0085\2\2\u033a\u033c\t\27\2\2\u033b\u033a\3\2\2\2\u033b"+
		"\u033c\3\2\2\2\u033c\u033e\3\2\2\2\u033d\u033f\5z>\2\u033e\u033d\3\2\2"+
		"\2\u033e\u033f\3\2\2\2\u033f\u0350\3\2\2\2\u0340\u0342\7\u0086\2\2\u0341"+
		"\u0343\t\27\2\2\u0342\u0341\3\2\2\2\u0342\u0343\3\2\2\2\u0343\u0345\3"+
		"\2\2\2\u0344\u0346\5z>\2\u0345\u0344\3\2\2\2\u0345\u0346\3\2\2\2\u0346"+
		"\u0350\3\2\2\2\u0347\u0349\7{\2\2\u0348\u034a\5z>\2\u0349\u0348\3\2\2"+
		"\2\u0349\u034a\3\2\2\2\u034a\u0350\3\2\2\2\u034b\u034d\7|\2\2\u034c\u034e"+
		"\5z>\2\u034d\u034c\3\2\2\2\u034d\u034e\3\2\2\2\u034e\u0350\3\2\2\2\u034f"+
		"\u02ff\3\2\2\2\u034f\u030d\3\2\2\2\u034f\u0317\3\2\2\2\u034f\u0321\3\2"+
		"\2\2\u034f\u032e\3\2\2\2\u034f\u0339\3\2\2\2\u034f\u0340\3\2\2\2\u034f"+
		"\u0347\3\2\2\2\u034f\u034b\3\2\2\2\u0350\u0083\3\2\2\2\u0351\u035e\5\u00a8"+
		"U\2\u0352\u035e\5\u0098M\2\u0353\u035e\5\u0086D\2\u0354\u035e\5\u0088"+
		"E\2\u0355\u035e\5\u008cG\2\u0356\u035e\5\u0090I\2\u0357\u035e\5\u0094"+
		"K\2\u0358\u035e\5\u0096L\2\u0359\u035a\7\37\2\2\u035a\u035b\5n8\2\u035b"+
		"\u035c\7 \2\2\u035c\u035e\3\2\2\2\u035d\u0351\3\2\2\2\u035d\u0352\3\2"+
		"\2\2\u035d\u0353\3\2\2\2\u035d\u0354\3\2\2\2\u035d\u0355\3\2\2\2\u035d"+
		"\u0356\3\2\2\2\u035d\u0357\3\2\2\2\u035d\u0358\3\2\2\2\u035d\u0359\3\2"+
		"\2\2\u035e\u0085\3\2\2\2\u035f\u0360\7\32\2\2\u0360\u0361\t\30\2\2\u0361"+
		"\u0362\5n8\2\u0362\u0363\7\21\2\2\u0363\u0364\5n8\2\u0364\u0365\t\31\2"+
		"\2\u0365\u0087\3\2\2\2\u0366\u0368\7\33\2\2\u0367\u0366\3\2\2\2\u0367"+
		"\u0368\3\2\2\2\u0368\u0369\3\2\2\2\u0369\u0373\7\20\2\2\u036a\u0374\7"+
		"\r\2\2\u036b\u0370\5\u008aF\2\u036c\u036d\7\21\2\2\u036d\u036f\5\u008a"+
		"F\2\u036e\u036c\3\2\2\2\u036f\u0372\3\2\2\2\u0370\u036e\3\2\2\2\u0370"+
		"\u0371\3\2\2\2\u0371\u0374\3\2\2\2\u0372\u0370\3\2\2\2\u0373\u036a\3\2"+
		"\2\2\u0373\u036b\3\2\2\2\u0374\u0375\3\2\2\2\u0375\u0376\7\22\2\2\u0376"+
		"\u0089\3\2\2\2\u0377\u0378\5\u00a8U\2\u0378\u0379\7\r\2\2\u0379\u037a"+
		"\5n8\2\u037a\u008b\3\2\2\2\u037b\u037c\5*\26\2\u037c\u0386\7\20\2\2\u037d"+
		"\u0387\7\r\2\2\u037e\u0383\5\u008eH\2\u037f\u0380\7\21\2\2\u0380\u0382"+
		"\5\u008eH\2\u0381\u037f\3\2\2\2\u0382\u0385\3\2\2\2\u0383\u0381\3\2\2"+
		"\2\u0383\u0384\3\2\2\2\u0384\u0387\3\2\2\2\u0385\u0383\3\2\2\2\u0386\u037d"+
		"\3\2\2\2\u0386\u037e\3\2\2\2\u0387\u0388\3\2\2\2\u0388\u0389\7\22\2\2"+
		"\u0389\u008d\3\2\2\2\u038a\u038b\5\u00a8U\2\u038b\u038c\7\r\2\2\u038c"+
		"\u038d\5n8\2\u038d\u008f\3\2\2\2\u038e\u0393\7\27\2\2\u038f\u0390\7\30"+
		"\2\2\u0390\u0391\5(\25\2\u0391\u0392\7\31\2\2\u0392\u0394\3\2\2\2\u0393"+
		"\u038f\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0396\3\2\2\2\u0395\u038e\3\2"+
		"\2\2\u0395\u0396\3\2\2\2\u0396\u0397\3\2\2\2\u0397\u03a0\7\20\2\2\u0398"+
		"\u039d\5n8\2\u0399\u039a\7\21\2\2\u039a\u039c\5n8\2\u039b\u0399\3\2\2"+
		"\2\u039c\u039f\3\2\2\2\u039d\u039b\3\2\2\2\u039d\u039e\3\2\2\2\u039e\u03a1"+
		"\3\2\2\2\u039f\u039d\3\2\2\2\u03a0\u0398\3\2\2\2\u03a0\u03a1\3\2\2\2\u03a1"+
		"\u03a2\3\2\2\2\u03a2\u03a3\7\22\2\2\u03a3\u0091\3\2\2\2\u03a4\u03a5\7"+
		"\u0087\2\2\u03a5\u03a6\5\u009eP\2\u03a6\u0093\3\2\2\2\u03a7\u03a8\7\u0088"+
		"\2\2\u03a8\u03a9\5\u009eP\2\u03a9\u03aa\7\25\2\2\u03aa\u03ac\5\26\f\2"+
		"\u03ab\u03ad\5\u0092J\2\u03ac\u03ab\3\2\2\2\u03ac\u03ad\3\2\2\2\u03ad"+
		"\u0095\3\2\2\2\u03ae\u03af\7\u0089\2\2\u03af\u03b0\7\20\2\2\u03b0\u03b5"+
		"\5\u0094K\2\u03b1\u03b2\7\21\2\2\u03b2\u03b4\5\u0094K\2\u03b3\u03b1\3"+
		"\2\2\2\u03b4\u03b7\3\2\2\2\u03b5\u03b3\3\2\2\2\u03b5\u03b6\3\2\2\2\u03b6"+
		"\u03b8\3\2\2\2\u03b7\u03b5\3\2\2\2\u03b8\u03ba\7\22\2\2\u03b9\u03bb\5"+
		"\u0092J\2\u03ba\u03b9\3\2\2\2\u03ba\u03bb\3\2\2\2\u03bb\u0097\3\2\2\2"+
		"\u03bc\u03c3\5\u009aN\2\u03bd\u03c3\5\u009cO\2\u03be\u03c3\5\u009eP\2"+
		"\u03bf\u03c3\5\u00a0Q\2\u03c0\u03c3\5\u00a2R\2\u03c1\u03c3\5\u00a4S\2"+
		"\u03c2\u03bc\3\2\2\2\u03c2\u03bd\3\2\2\2\u03c2\u03be\3\2\2\2\u03c2\u03bf"+
		"\3\2\2\2\u03c2\u03c0\3\2\2\2\u03c2\u03c1\3\2\2\2\u03c3\u0099\3\2\2\2\u03c4"+
		"\u03c5\7\64\2\2\u03c5\u009b\3\2\2\2\u03c6\u03c7\t\32\2\2\u03c7\u009d\3"+
		"\2\2\2\u03c8\u03c9\7\u008f\2\2\u03c9\u009f\3\2\2\2\u03ca\u03cb\7\u008c"+
		"\2\2\u03cb\u00a1\3\2\2\2\u03cc\u03cd\7\u008d\2\2\u03cd\u00a3\3\2\2\2\u03ce"+
		"\u03d0\7\u008b\2\2\u03cf\u03d1\5\u00a6T\2\u03d0\u03cf\3\2\2\2\u03d0\u03d1"+
		"\3\2\2\2\u03d1\u00a5\3\2\2\2\u03d2\u03d6\5p9\2\u03d3\u03d6\5t;\2\u03d4"+
		"\u03d6\7\u008f\2\2\u03d5\u03d2\3\2\2\2\u03d5\u03d3\3\2\2\2\u03d5\u03d4"+
		"\3\2\2\2\u03d6\u00a7\3\2\2\2\u03d7\u03d8\t\33\2\2\u03d8\u00a9\3\2\2\2"+
		"o\u00ab\u00b0\u00b6\u00bc\u00c2\u00c8\u00ce\u00d4\u00da\u00e0\u00e6\u00ec"+
		"\u00f6\u00fb\u00ff\u0102\u010a\u010d\u0115\u0118\u0121\u0129\u0130\u0139"+
		"\u013c\u0147\u014c\u0151\u0161\u0166\u017d\u0188\u018c\u0197\u01a1\u01a4"+
		"\u01b5\u01be\u01d0\u01d3\u01df\u01e4\u01e8\u01eb\u01ee\u01f2\u01fc\u0205"+
		"\u0211\u021d\u0220\u0226\u022d\u024d\u025c\u026b\u0273\u027a\u027c\u0285"+
		"\u02a9\u02b2\u02b5\u02c9\u02ce\u02d6\u02e9\u02eb\u02fc\u02ff\u0303\u0307"+
		"\u030a\u030d\u0311\u0314\u0317\u031a\u031e\u0321\u0324\u0328\u032b\u032e"+
		"\u0331\u0337\u033b\u033e\u0342\u0345\u0349\u034d\u034f\u035d\u0367\u0370"+
		"\u0373\u0383\u0386\u0393\u0395\u039d\u03a0\u03ac\u03b5\u03ba\u03c2\u03d0"+
		"\u03d5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}