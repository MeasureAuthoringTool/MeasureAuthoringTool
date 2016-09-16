package mat.server.cqlparser;

import java.util.ArrayList;
import java.util.List;

import mat.model.cql.parser.CQLBaseStatementInterface;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLParameterModelObject;
import mat.model.cql.parser.CQLValueSetModelObject;
import mat.server.cqlparser.cqlParser.AliasedQuerySourceContext;
import mat.server.cqlparser.cqlParser.ExpressionDefinitionContext;
import mat.server.cqlparser.cqlParser.FunctionDefinitionContext;
import mat.server.cqlparser.cqlParser.LogicContext;
import mat.server.cqlparser.cqlParser.OperandDefinitionContext;
import mat.server.cqlparser.cqlParser.QueryContext;
import mat.server.cqlparser.cqlParser.QuerySourceContext;
import mat.server.cqlparser.cqlParser.RetrieveContext;
import mat.server.cqlparser.cqlParser.SingleSourceClauseContext;
import mat.server.cqlparser.cqlParser.SourceClauseContext;

import org.antlr.v4.runtime.tree.ParseTree;
//import mat.cql.humanreadable.GenerateCQLHumanReadable;

public class MATCQLListener extends cqlBaseListener {

	/**
	 * 
	 */
	private final MATCQLParser matCQLListener;
	private CQLFileObject cqlFileObject = new CQLFileObject();

	public CQLFileObject getCqlFileObject() {
		return cqlFileObject;
	}

	public void setCqlFileObject(CQLFileObject cqlFileObject) {
		this.cqlFileObject = cqlFileObject;
	}

	/**
	 * @param testCQLParser
	 */
	MATCQLListener(MATCQLParser testCQLParser) {
		matCQLListener = testCQLParser;
	}

	public void exitLibraryDefinition(cqlParser.LibraryDefinitionContext ctx) {
		//System.out.println("Found library definition...");
		//System.out.println(ctx.identifier().getText());
		//System.out.println(ctx.versionSpecifier().getText());
		//System.out.println("\r\n");
	}

	public void exitParameterDefinition(cqlParser.ParameterDefinitionContext ctx) {
		//System.out.println("Found parameter definition...");
		//System.out.println(ctx.identifier().getText());
		//System.out.println("\r\n");
		extractParameterDetails(ctx);
	}

	public void exitValuesetDefinition(cqlParser.ValuesetDefinitionContext ctx) { 
		//System.out.println("Found ValueSet definition...");
		//System.out.println(ctx.identifier().getText());
		//System.out.println(ctx.valuesetId().getText());
		//System.out.println("\r\n");
		extractValueSetDetails(ctx);
	}

	public void exitStatement(cqlParser.StatementContext ctx) { 
		if(ctx.functionDefinition() != null){
			extractCQLFunctionDetails(ctx);
		}else if(ctx.expressionDefinition() != null){
			extractCQLDefinitionDetails(ctx);

		}else if(ctx.contextDefinition() != null){
			//System.out.println("Found context ...");
			//System.out.println(ctx.contextDefinition().identifier().getText());
		}
		//System.out.println("\r\n");
	}


	private void extractValueSetDetails(cqlParser.ValuesetDefinitionContext ctx){
		System.out.println("Found ValueSet definition...");
		System.out.println(ctx.identifier().getText());
		
		//System.out.println("Depth:"+ctx.expressionDefinition().expression().children.get(0).getClass().getSimpleName());

		CQLValueSetModelObject cqlValueSetModelObject = new CQLValueSetModelObject();
		cqlValueSetModelObject.setIdentifier(ctx.identifier().getText());
		
		List<String> childTokens = findValueSetChildren(ctx, cqlValueSetModelObject);
		
		if(ctx.accessModifier() != null){
			cqlValueSetModelObject.setAccessModifier(ctx.accessModifier().getText());
		}
		cqlValueSetModelObject.setChildTokens(childTokens);
		this.cqlFileObject.getValueSetsMap().put(cqlValueSetModelObject.getIdentifier(), cqlValueSetModelObject);
	}


	private void extractParameterDetails(cqlParser.ParameterDefinitionContext ctx){
		System.out.println("Found Parameter definition...");
		System.out.println(ctx.identifier().getText());
		
		//System.out.println("Depth:"+ctx.expressionDefinition().expression().children.get(0).getClass().getSimpleName());

		CQLParameterModelObject cqlParameterModelObject = new CQLParameterModelObject();
		cqlParameterModelObject.setIdentifier(ctx.identifier().getText());
		List<String> childTokens = findParameterChildren(ctx, cqlParameterModelObject);
		
		if(ctx.accessModifier() != null){
			cqlParameterModelObject.setAccessModifier(ctx.accessModifier().getText());
		}
		cqlParameterModelObject.setChildTokens(childTokens);
		this.cqlFileObject.getParametersMap().put(cqlParameterModelObject.getIdentifier(), cqlParameterModelObject);
	}


	private void extractCQLFunctionDetails(cqlParser.StatementContext ctx) {
		System.out.println("Found Function definition...");
		System.out.println(ctx.functionDefinition().identifier().getText());

		

		CQLFunctionModelObject cqlFunctionModelObject = new CQLFunctionModelObject();
		List<String> childTokens = findFunctionChildren(ctx, cqlFunctionModelObject);
		cqlFunctionModelObject.setIdentifier(ctx.functionDefinition().identifier().getText());

		if(ctx.functionDefinition().accessModifier() != null){
			cqlFunctionModelObject.setAccessModifier(ctx.functionDefinition().accessModifier().getText());
		}

		cqlFunctionModelObject.setChildTokens(childTokens);

		FunctionDefinitionContext functionDefinitionContext = ctx.functionDefinition();
		List<OperandDefinitionContext> operandDefinitionContextList = functionDefinitionContext.operandDefinition();

		for(OperandDefinitionContext operandDefinitionContext:operandDefinitionContextList){
			String argumentName = operandDefinitionContext.identifier().getText();
			String argumentType = operandDefinitionContext.typeSpecifier().getText();
			System.out.println("With arguments:"+argumentName + " and type:"+argumentType);

			CQLFunctionModelObject.FunctionArgument functionArgument = cqlFunctionModelObject.new FunctionArgument();
			functionArgument.setArgumentName(argumentName);
			functionArgument.setArgumentType(argumentType);

			cqlFunctionModelObject.getArguments().add(functionArgument);
		}


		this.cqlFileObject.getFunctionsMap().put(cqlFunctionModelObject.getIdentifier(), cqlFunctionModelObject);
	}

	private void extractCQLDefinitionDetails(cqlParser.StatementContext ctx) {
		//System.out.println("Found definition...");
		//System.out.println(ctx.expressionDefinition().identifier().getText());
		
		//System.out.println("Depth:"+ctx.expressionDefinition().expression().children.get(0).getClass().getSimpleName());

		CQLDefinitionModelObject cqlDefinitionModelObject = new CQLDefinitionModelObject();
		cqlDefinitionModelObject.setIdentifier(ctx.expressionDefinition().identifier().getText());

		//boolean possibleSupplementalDef = checkForSupplimentalDefinitions(ctx);
		//cqlDefinitionModelObject.setPossibleSupplementalDef(possibleSupplementalDef);

		if(ctx.expressionDefinition().accessModifier() != null){
			cqlDefinitionModelObject.setAccessModifier(ctx.expressionDefinition().accessModifier().getText());
		}
		List<String> childTokens = findDefinitionChildren(ctx, cqlDefinitionModelObject);
		cqlDefinitionModelObject.setChildTokens(childTokens);
		this.cqlFileObject.getDefinitionsMap().put(cqlDefinitionModelObject.getIdentifier(), cqlDefinitionModelObject);
		System.out.println(cqlDefinitionModelObject.getIdentifier() + ":Referred to value sets:"+cqlDefinitionModelObject.getReferredToValueSets());
	}

	/**
	 * This method will look into the definition context and determine If this is a potential 
	 * supplemental definition.
	 * @param ctx
	 */
	//This code has been commented out as a part of MAT-7839 User Story which is
	//not included in MAT 5.0 release
	/*private boolean checkForSupplimentalDefinitions(cqlParser.StatementContext ctx) {
		try 
		{
			if(ctx.expressionDefinition().expression().children.size() == 1)
			{

				if(ctx.expressionDefinition().expression().children.get(0).getClass().getSimpleName().equals("QueryContext"))
				{
					QueryContext queryContext = (QueryContext) ctx.expressionDefinition().expression().children.get(0);

					SourceClauseContext sourceClauseContext = queryContext.sourceClause();
					//WhereClauseContext whereClauseContext = queryContext.whereClause();

					boolean sourceClauseCheck = false;
					//boolean whereClauseCheck = false;

					//Check the SourceClauseContext.
					if (sourceClauseContext.children.size() == 1 && 
							sourceClauseContext.children.get(0).getClass().getSimpleName().equals("SingleSourceClauseContext"))
					{
						SingleSourceClauseContext singleSourceClauseContext = (SingleSourceClauseContext) sourceClauseContext.children.get(0);

						if(singleSourceClauseContext.children.size() == 1 && 
								singleSourceClauseContext.children.get(0).getClass().getSimpleName().equals("AliasedQuerySourceContext"))
						{
							AliasedQuerySourceContext aliasedQuerySourceContext = (AliasedQuerySourceContext)singleSourceClauseContext.children.get(0);
							String alias = aliasedQuerySourceContext.alias().getText();
							QuerySourceContext querySourceContext = aliasedQuerySourceContext.querySource();

							if(querySourceContext.children.size() == 1 && 
									querySourceContext.children.get(0).getClass().
									getSimpleName().equals("RetrieveContext"))
							{						
								RetrieveContext retrieveContext = (RetrieveContext) querySourceContext.children.get(0);

								if(retrieveContext.valueset() != null) {
									sourceClauseCheck = true;
								}

							}
						}				
					}

					//Check the WhereClauseContext

					if (whereClauseContext.expression().getClass().getSimpleName().equals("BooleanExpressionContext"))
				{
					BooleanExpressionContext booleanExpressionContext = (BooleanExpressionContext)whereClauseContext.expression();
					//System.out.println("where clause expression:"+booleanExpressionContext.getText());

					List exprChildren = booleanExpressionContext.children;

					if(exprChildren.size() > 1) 
					{
						if (exprChildren.get(exprChildren.size() - 2).getClass().getSimpleName().equals("TerminalNodeImpl")
								&&
								exprChildren.get(exprChildren.size() - 1).getClass().getSimpleName().equals("TerminalNodeImpl"))
						{
							if (((TerminalNode)exprChildren.get(exprChildren.size() - 2)).getText().trim().equals("is") 
									&&
									((TerminalNode)exprChildren.get(exprChildren.size() - 1)).getText().trim().equals("null"))
							{

								TermExpressionContext termExpressionContext = (TermExpressionContext)exprChildren.get(0);
								//System.out.println("termExpressionContext:"+termExpressionContext.getText());

								AccessorExpressionTermContext accessorExpressionTermContext = (AccessorExpressionTermContext)termExpressionContext.children.get(0);
								if (accessorExpressionTermContext.children.get(accessorExpressionTermContext.children.size() - 1).getClass().getSimpleName().equals("IdentifierContext"))
								{
									//System.out.println(accessorExpressionTermContext.children.get(accessorExpressionTermContext.children.size() - 1).getText());

									if(accessorExpressionTermContext.children.get(accessorExpressionTermContext.children.size() - 1).getText().equalsIgnoreCase("negationrationale")){
										whereClauseCheck = true;
									}
								}

							}
						}
					}

				}


					//if(sourceClauseCheck && whereClauseCheck){
					if(sourceClauseCheck){
						System.out.println("Possible supplemental definition:"+ctx.expressionDefinition().identifier().getText());
						return true;
					}
				}else if (ctx.expressionDefinition().expression().children.get(0).getClass().getSimpleName().equals("RetrieveContext")){
					RetrieveContext retrieveContext = (RetrieveContext) ctx.expressionDefinition().expression().children.get(0);

					if(retrieveContext.namedTypeSpecifier() != null && retrieveContext.valueset() != null){
						System.out.println("Possible supplemental definition:"+ctx.expressionDefinition().identifier().getText());
						return true;
					}
				}
			}
		}
		catch(Exception er){
			return false;
		}

		return false;
	}*/

	private List<String> findFunctionChildren(cqlParser.StatementContext ctx, CQLBaseStatementInterface cqlBaseStatement) {
		FunctionDefinitionContext functionDefinitionContext = ctx.functionDefinition();
		List<ParseTree> parseTreeList = functionDefinitionContext.children;
		return findStatementChildren(parseTreeList, cqlBaseStatement);
	}

	/**
	 * Find children for definition.
	 * @param ctx
	 * @return
	 */
	private List<String> findDefinitionChildren(cqlParser.StatementContext ctx, CQLBaseStatementInterface cqlBaseStatement) {
		ExpressionDefinitionContext expressionDefinitionContext = ctx.expressionDefinition();
		List<ParseTree> parseTreeList = expressionDefinitionContext.children;
		return findStatementChildren(parseTreeList, cqlBaseStatement);
	}


	private List<String> findValueSetChildren(cqlParser.ValuesetDefinitionContext ctx, CQLBaseStatementInterface cqlBaseStatement) {
		//ExpressionDefinitionContext expressionDefinitionContext = ctx.children;
		List<ParseTree> parseTreeList = ctx.children;
		return findStatementChildren(parseTreeList, cqlBaseStatement);
	}

	private List<String> findParameterChildren(cqlParser.ParameterDefinitionContext ctx, CQLBaseStatementInterface cqlBaseStatement) {
		//ExpressionDefinitionContext expressionDefinitionContext = ctx.children;
		List<ParseTree> parseTreeList = ctx.children;
		return findStatementChildren(parseTreeList, cqlBaseStatement);
	}


	public List<String> findStatementChildren(List<ParseTree> parseTreeList, CQLBaseStatementInterface cqlBaseStatement) {
		List<String> childTokens = new ArrayList<String>();

		for(ParseTree tree:parseTreeList){
			if(tree.getChildCount() == 0){
				childTokens.add(tree.getText());
			}else{
				findStatementChildren(tree,childTokens, cqlBaseStatement);
			}
		}

		List<String> removeTokens = new ArrayList<String>();

		for(int i=0;i<childTokens.size();i++){
			String token = childTokens.get(i).trim();
			//System.out.println("token:"+token);
			if(i==0 && token.equals("parameter")){
				removeTokens.add(childTokens.get(i));
				removeTokens.add(childTokens.get(i+1));
				break;
			}
			if(token.equals(":")){
				removeTokens.add(childTokens.get(i));
				break;
			}else{
				removeTokens.add(childTokens.get(i));
			}
		}
		//System.out.println(removeTokens);
		for(int j=0;j<removeTokens.size();j++){
			childTokens.remove(removeTokens.get(j));
		}

		//System.out.println("Tokens:"+childTokens);
		return childTokens;
	}

	private void findStatementChildren(ParseTree tree, List<String> childTokens, CQLBaseStatementInterface baseStatementInterface) {
		int childCount = tree.getChildCount();

		for(int i=0;i<childCount;i++){
			ParseTree childTree = tree.getChild(i);
			if(childTree.getChildCount() == 0){
				childTokens.add(childTree.getText());
			}else if(childTree instanceof RetrieveContext){
				RetrieveContext retrieveContext = (RetrieveContext)childTree;
				
				if(retrieveContext.valueset() != null){
					String valueSetIdentifier = retrieveContext.valueset().qualifiedIdentifier().identifier().getText();
					
					if(this.cqlFileObject.getValueSetsMap().get(valueSetIdentifier) != null){
						baseStatementInterface.getReferredToValueSets().add(this.cqlFileObject.getValueSetsMap().get(valueSetIdentifier));
						setReferredByInValueSet(baseStatementInterface, this.cqlFileObject.getValueSetsMap().get(valueSetIdentifier));
					}
				}
				
				findStatementChildren(childTree,childTokens, baseStatementInterface);
			}else{
				findStatementChildren(childTree,childTokens, baseStatementInterface);
			}
		}

	}

	private void setReferredByInValueSet(
			CQLBaseStatementInterface baseStatementInterface,
			CQLValueSetModelObject cqlValueSetModelObject) {
		
		if(baseStatementInterface instanceof CQLDefinitionModelObject){
			cqlValueSetModelObject.getReferredByDefinitions().add((CQLDefinitionModelObject)baseStatementInterface);
		}else if(baseStatementInterface instanceof CQLFunctionModelObject){
			cqlValueSetModelObject.getReferredByFunctions().add((CQLFunctionModelObject)baseStatementInterface);
		}else if(baseStatementInterface instanceof CQLParameterModelObject){
			cqlValueSetModelObject.getReferredByParameters().add((CQLParameterModelObject)baseStatementInterface);
		}
		
	}

	@Override
	public void exitLogic(LogicContext ctx) {
		//System.out.println("exit logic...");
		buildReferenceMaps();
		//System.out.println(this.cqlFileObject);
	}

	private void buildReferenceMaps() {
		mapDefinitionReferences();

	}

	private void mapDefinitionReferences() {
		List<CQLDefinitionModelObject> definitionsList = new ArrayList<CQLDefinitionModelObject>(this.cqlFileObject.getDefinitionsMap().values());
		List<CQLFunctionModelObject> functionsList = new ArrayList<CQLFunctionModelObject>(this.cqlFileObject.getFunctionsMap().values());
		List<CQLValueSetModelObject> valueSetsList = new ArrayList<CQLValueSetModelObject>(this.cqlFileObject.getValueSetsMap().values());
		List<CQLParameterModelObject> parametersList = new ArrayList<CQLParameterModelObject>(this.cqlFileObject.getParametersMap().values());

		//find functions referring to other definitions/functions
		for(CQLFunctionModelObject cqlFunctionModelObject:functionsList){
			findFunctionsReferences(cqlFunctionModelObject, definitionsList, functionsList);
		}

		//find definitions referring to other definitions/functions
		for(CQLDefinitionModelObject cqlDefinitionModelObject:definitionsList){			
			findDefinitionsReferences(cqlDefinitionModelObject,definitionsList, functionsList);			
		}

		/*//find Valuesets referring to other definitions/functions
		for(CQLValueSetModelObject cqlValueSetModelObject:valueSetsList){			
			findValueSetReferences(cqlValueSetModelObject,definitionsList, functionsList);			
		}	*/	
		
		//find Parameters referring to other definitions/functions
		for(CQLParameterModelObject cqlParameterModelObject:parametersList){			
			findParameterReferences(cqlParameterModelObject,definitionsList, functionsList);			
		}	
	}


	private void findFunctionsReferences(CQLFunctionModelObject cqlFunctionModelObject,
			List<CQLDefinitionModelObject> definitionsList, List<CQLFunctionModelObject> functionsList) {

		String cqlFunctionName = cqlFunctionModelObject.getIdentifier();

		for(CQLFunctionModelObject cqlFuncModelObject:functionsList){
			if(cqlFuncModelObject.getChildTokens().contains(cqlFunctionName)){
				cqlFuncModelObject.getReferredToFunctions().add(cqlFunctionModelObject);
				cqlFunctionModelObject.getReferredByFunctions().add(cqlFuncModelObject);
			}
		}

		for(CQLDefinitionModelObject cqlDefnModelObject:definitionsList){
			if(cqlDefnModelObject.getChildTokens().contains(cqlFunctionName)){
				cqlDefnModelObject.getReferredToFunctions().add(cqlFunctionModelObject);
				cqlFunctionModelObject.getReferredByDefinitions().add(cqlDefnModelObject);
			}
		}

	}

	private void findDefinitionsReferences(CQLDefinitionModelObject cqlDefinitionModelObject,
			List<CQLDefinitionModelObject> definitionsList, List<CQLFunctionModelObject> functionsList) {

		String cqlDefinitionName = cqlDefinitionModelObject.getIdentifier();

		for(CQLFunctionModelObject cqlFunctionModelObject:functionsList){
			if(cqlFunctionModelObject.getChildTokens().contains(cqlDefinitionName)){
				cqlFunctionModelObject.getReferredToDefinitions().add(cqlDefinitionModelObject);
				cqlDefinitionModelObject.getReferredByFunctions().add(cqlFunctionModelObject);
			}
		}

		for(CQLDefinitionModelObject cqlDefnModelObject:definitionsList){
			if(cqlDefnModelObject.getChildTokens().contains(cqlDefinitionName)){
				cqlDefnModelObject.getReferredToDefinitions().add(cqlDefinitionModelObject);
				cqlDefinitionModelObject.getReferredByDefinitions().add(cqlDefnModelObject);
			}
		}

	}


	/*private void findValueSetReferences(CQLValueSetModelObject cqlValueSetModelObject,
	List<CQLDefinitionModelObject> definitionsList, List<CQLFunctionModelObject> functionsList) {

String cqlValueSetName = cqlValueSetModelObject.getIdentifier();

for(CQLFunctionModelObject cqlFunctionModelObject:functionsList){
	if(cqlFunctionModelObject.getChildTokens().contains(cqlValueSetName)){
		cqlValueSetModelObject.getReferredToFunctions().add(cqlFunctionModelObject);
		cqlFunctionModelObject.getReferredByValueSets().add(cqlValueSetModelObject);
	}
}

for(CQLDefinitionModelObject cqlDefnModelObject:definitionsList){
	if(cqlDefnModelObject.getChildTokens().contains(cqlValueSetName)){
		cqlValueSetModelObject.getReferredToDefinitions().add(cqlDefnModelObject);
		cqlDefnModelObject.getReferredByValueSets().add(cqlValueSetModelObject);
	}
}

}*/


	private void findParameterReferences(CQLParameterModelObject cqlParameterModelObject,
			List<CQLDefinitionModelObject> definitionsList, List<CQLFunctionModelObject> functionsList) {


		String cqlParameterName = cqlParameterModelObject.getIdentifier();

		for(CQLFunctionModelObject cqlFunctionModelObject:functionsList){
			if(cqlFunctionModelObject.getChildTokens().contains(cqlParameterName)){
				cqlFunctionModelObject.getReferredToParameters().add(cqlParameterModelObject);
				cqlParameterModelObject.getReferredByFunctions().add(cqlFunctionModelObject);
			}
		}

		for(CQLDefinitionModelObject cqlDefnModelObject:definitionsList){
			if(cqlDefnModelObject.getChildTokens().contains(cqlParameterName)){
				cqlDefnModelObject.getReferredToParameters().add(cqlParameterModelObject);
				cqlParameterModelObject.getReferredByDefinitions().add(cqlDefnModelObject);}
			}


	}



	public MATCQLParser getMatCQLListener() {
		return matCQLListener;
	}

}