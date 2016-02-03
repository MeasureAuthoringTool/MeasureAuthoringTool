package mat.server.cqlparser;

// Generated from cql.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link cqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface cqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link cqlParser#logic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogic(cqlParser.LogicContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#libraryDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryDefinition(cqlParser.LibraryDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#usingDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsingDefinition(cqlParser.UsingDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#includeDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludeDefinition(cqlParser.IncludeDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#localIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalIdentifier(cqlParser.LocalIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#accessModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessModifier(cqlParser.AccessModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#parameterDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDefinition(cqlParser.ParameterDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#codesystemDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodesystemDefinition(cqlParser.CodesystemDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#valuesetDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesetDefinition(cqlParser.ValuesetDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#codesystems}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodesystems(cqlParser.CodesystemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#codesystemIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodesystemIdentifier(cqlParser.CodesystemIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#libraryIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryIdentifier(cqlParser.LibraryIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#codesystemId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodesystemId(cqlParser.CodesystemIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#valuesetId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesetId(cqlParser.ValuesetIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#versionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersionSpecifier(cqlParser.VersionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#typeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpecifier(cqlParser.TypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#namedTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedTypeSpecifier(cqlParser.NamedTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#modelIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelIdentifier(cqlParser.ModelIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#listTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListTypeSpecifier(cqlParser.ListTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#intervalTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalTypeSpecifier(cqlParser.IntervalTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#tupleTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleTypeSpecifier(cqlParser.TupleTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#tupleElementDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleElementDefinition(cqlParser.TupleElementDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(cqlParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#expressionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionDefinition(cqlParser.ExpressionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#contextDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextDefinition(cqlParser.ContextDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#functionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDefinition(cqlParser.FunctionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#operandDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandDefinition(cqlParser.OperandDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(cqlParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#querySource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuerySource(cqlParser.QuerySourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#aliasedQuerySource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasedQuerySource(cqlParser.AliasedQuerySourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(cqlParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#queryInclusionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryInclusionClause(cqlParser.QueryInclusionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#withClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithClause(cqlParser.WithClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#withoutClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithoutClause(cqlParser.WithoutClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#retrieve}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetrieve(cqlParser.RetrieveContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#valuesetPathIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesetPathIdentifier(cqlParser.ValuesetPathIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#valueset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueset(cqlParser.ValuesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#qualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifier(cqlParser.QualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(cqlParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#sourceClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceClause(cqlParser.SourceClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#singleSourceClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleSourceClause(cqlParser.SingleSourceClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#multipleSourceClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultipleSourceClause(cqlParser.MultipleSourceClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#defineClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineClause(cqlParser.DefineClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#defineClauseItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineClauseItem(cqlParser.DefineClauseItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(cqlParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#returnClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnClause(cqlParser.ReturnClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#sortClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortClause(cqlParser.SortClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#sortDirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortDirection(cqlParser.SortDirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#sortByItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortByItem(cqlParser.SortByItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdentifier(cqlParser.QualifiedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code durationBetweenExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDurationBetweenExpression(cqlParser.DurationBetweenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inFixSetExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInFixSetExpression(cqlParser.InFixSetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code retrieveExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetrieveExpression(cqlParser.RetrieveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code timingExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimingExpression(cqlParser.TimingExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(cqlParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code queryExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpression(cqlParser.QueryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpression(cqlParser.BooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(cqlParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code castExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpression(cqlParser.CastExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(cqlParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code betweenExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetweenExpression(cqlParser.BetweenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code membershipExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembershipExpression(cqlParser.MembershipExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code differenceBetweenExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDifferenceBetweenExpression(cqlParser.DifferenceBetweenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inequalityExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInequalityExpression(cqlParser.InequalityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equalityExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(cqlParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code existenceExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistenceExpression(cqlParser.ExistenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code termExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermExpression(cqlParser.TermExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeExpression}
	 * labeled alternative in {@link cqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeExpression(cqlParser.TypeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#dateTimePrecision}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateTimePrecision(cqlParser.DateTimePrecisionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#dateTimeComponent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateTimeComponent(cqlParser.DateTimeComponentContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#pluralDateTimePrecision}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPluralDateTimePrecision(cqlParser.PluralDateTimePrecisionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code additionExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditionExpressionTerm(cqlParser.AdditionExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code indexedExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexedExpressionTerm(cqlParser.IndexedExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code widthExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWidthExpressionTerm(cqlParser.WidthExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code timeUnitExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeUnitExpressionTerm(cqlParser.TimeUnitExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifThenElseExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfThenElseExpressionTerm(cqlParser.IfThenElseExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code timeBoundaryExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeBoundaryExpressionTerm(cqlParser.TimeBoundaryExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code elementExtractorExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementExtractorExpressionTerm(cqlParser.ElementExtractorExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conversionExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversionExpressionTerm(cqlParser.ConversionExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeExtentExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeExtentExpressionTerm(cqlParser.TypeExtentExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predecessorExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredecessorExpressionTerm(cqlParser.PredecessorExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code accessorExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessorExpressionTerm(cqlParser.AccessorExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiplicationExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicationExpressionTerm(cqlParser.MultiplicationExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aggregateExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregateExpressionTerm(cqlParser.AggregateExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code durationExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDurationExpressionTerm(cqlParser.DurationExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code caseExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpressionTerm(cqlParser.CaseExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code powerExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowerExpressionTerm(cqlParser.PowerExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code successorExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuccessorExpressionTerm(cqlParser.SuccessorExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code polarityExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolarityExpressionTerm(cqlParser.PolarityExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code termExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermExpressionTerm(cqlParser.TermExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code invocationExpressionTerm}
	 * labeled alternative in {@link cqlParser#expressionTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvocationExpressionTerm(cqlParser.InvocationExpressionTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#caseExpressionItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpressionItem(cqlParser.CaseExpressionItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#dateTimePrecisionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateTimePrecisionSpecifier(cqlParser.DateTimePrecisionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#relativeQualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelativeQualifier(cqlParser.RelativeQualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#offsetRelativeQualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOffsetRelativeQualifier(cqlParser.OffsetRelativeQualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#quantityOffset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantityOffset(cqlParser.QuantityOffsetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code concurrentWithIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcurrentWithIntervalOperatorPhrase(cqlParser.ConcurrentWithIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code includesIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludesIntervalOperatorPhrase(cqlParser.IncludesIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code includedInIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludedInIntervalOperatorPhrase(cqlParser.IncludedInIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code beforeOrAfterIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeforeOrAfterIntervalOperatorPhrase(cqlParser.BeforeOrAfterIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code withinIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithinIntervalOperatorPhrase(cqlParser.WithinIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code meetsIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeetsIntervalOperatorPhrase(cqlParser.MeetsIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code overlapsIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverlapsIntervalOperatorPhrase(cqlParser.OverlapsIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code startsIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartsIntervalOperatorPhrase(cqlParser.StartsIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code endsIntervalOperatorPhrase}
	 * labeled alternative in {@link cqlParser#intervalOperatorPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndsIntervalOperatorPhrase(cqlParser.EndsIntervalOperatorPhraseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierTerm(cqlParser.IdentifierTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralTerm(cqlParser.LiteralTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intervalSelectorTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalSelectorTerm(cqlParser.IntervalSelectorTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tupleSelectorTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleSelectorTerm(cqlParser.TupleSelectorTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code instanceSelectorTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceSelectorTerm(cqlParser.InstanceSelectorTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listSelectorTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListSelectorTerm(cqlParser.ListSelectorTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code codeSelectorTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodeSelectorTerm(cqlParser.CodeSelectorTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conceptSelectorTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptSelectorTerm(cqlParser.ConceptSelectorTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesizedTerm}
	 * labeled alternative in {@link cqlParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedTerm(cqlParser.ParenthesizedTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#intervalSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalSelector(cqlParser.IntervalSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#tupleSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleSelector(cqlParser.TupleSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#tupleElementSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleElementSelector(cqlParser.TupleElementSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#instanceSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceSelector(cqlParser.InstanceSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#instanceElementSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceElementSelector(cqlParser.InstanceElementSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#listSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListSelector(cqlParser.ListSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#displayClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisplayClause(cqlParser.DisplayClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#codeSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodeSelector(cqlParser.CodeSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#conceptSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConceptSelector(cqlParser.ConceptSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(cqlParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#nullLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(cqlParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#booleanLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(cqlParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(cqlParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#dateTimeLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateTimeLiteral(cqlParser.DateTimeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#timeLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeLiteral(cqlParser.TimeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#quantityLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantityLiteral(cqlParser.QuantityLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit(cqlParser.UnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link cqlParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(cqlParser.IdentifierContext ctx);
}