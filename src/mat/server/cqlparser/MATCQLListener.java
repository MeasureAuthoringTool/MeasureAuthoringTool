package mat.server.cqlparser;

import java.util.ArrayList;
import java.util.List;

import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.server.cqlparser.cqlParser.AccessorExpressionTermContext;
import mat.server.cqlparser.cqlParser.AliasedQuerySourceContext;
import mat.server.cqlparser.cqlParser.BooleanExpressionContext;
import mat.server.cqlparser.cqlParser.ExpressionDefinitionContext;
import mat.server.cqlparser.cqlParser.LogicContext;
import mat.server.cqlparser.cqlParser.QueryContext;
import mat.server.cqlparser.cqlParser.QuerySourceContext;
import mat.server.cqlparser.cqlParser.RetrieveContext;
import mat.server.cqlparser.cqlParser.SingleSourceClauseContext;
import mat.server.cqlparser.cqlParser.SourceClauseContext;
import mat.server.cqlparser.cqlParser.TermExpressionContext;
import mat.server.cqlparser.cqlParser.WhereClauseContext;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
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
	}
	
	public void exitValuesetDefinition(cqlParser.ValuesetDefinitionContext ctx) { 
		//System.out.println("Found ValueSet definition...");
		//System.out.println(ctx.identifier().getText());
		//System.out.println(ctx.valuesetId().getText());
		//System.out.println("\r\n");
	}
	
	public void exitStatement(cqlParser.StatementContext ctx) { 
		if(ctx.functionDefinition() != null){
			extractCQLFunctionDetails();
		}else if(ctx.expressionDefinition() != null){
			extractCQLDefinitionDetails(ctx);
			
		}else if(ctx.contextDefinition() != null){
			//System.out.println("Found context ...");
			//System.out.println(ctx.contextDefinition().identifier().getText());
		}
		//System.out.println("\r\n");
	}

	private void extractCQLFunctionDetails() {
		System.out.println("Found Function definition...");
		//System.out.println(ctx.functionDefinition().identifier().getText());
	}

	private void extractCQLDefinitionDetails(cqlParser.StatementContext ctx) {
		//System.out.println("Found definition...");
		//System.out.println(ctx.expressionDefinition().identifier().getText());
		List<String> childTokens = findDefinitionChildren(ctx);
		//System.out.println("Depth:"+ctx.expressionDefinition().expression().children.get(0).getClass().getSimpleName());
					
		CQLDefinitionModelObject cqlDefinitionModelObject = new CQLDefinitionModelObject();
		cqlDefinitionModelObject.setIdentifier(ctx.expressionDefinition().identifier().getText());
		
		checkForSupplimentalDefinitions(ctx);
		
		if(ctx.expressionDefinition().accessModifier() != null){
			cqlDefinitionModelObject.setAccessModifier(ctx.expressionDefinition().accessModifier().getText());
		}
		cqlDefinitionModelObject.setChildTokens(childTokens);
		this.cqlFileObject.getDefinitionsMap().put(cqlDefinitionModelObject.getIdentifier(), cqlDefinitionModelObject);
	}
	
	/**
	 * This method will look into the definition context and determine If this is a potential 
	 * supplemental definition.
	 * @param ctx
	 */
	private boolean checkForSupplimentalDefinitions(cqlParser.StatementContext ctx) {
		try 
		{
			if(ctx.expressionDefinition().expression().children.size() == 1 && 
					ctx.expressionDefinition().expression().children.get(0).getClass().
					getSimpleName().equals("QueryContext")){

				QueryContext queryContext = (QueryContext) ctx.expressionDefinition().expression().children.get(0);

				SourceClauseContext sourceClauseContext = queryContext.sourceClause();
				WhereClauseContext whereClauseContext = queryContext.whereClause();

				boolean sourceClauseCheck = false;
				boolean whereClauseCheck = false;

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
						//System.out.println("Found query alias:"+alias);

						QuerySourceContext querySourceContext = aliasedQuerySourceContext.querySource();

						if(querySourceContext.children.size() == 1 && 
								querySourceContext.children.get(0).getClass().
								getSimpleName().equals("RetrieveContext"))
						{						
							RetrieveContext retrieveContext = (RetrieveContext) querySourceContext.children.get(0);

							if(retrieveContext.valueset() != null) {
								/*System.out.println("data type name:"+retrieveContext.namedTypeSpecifier().getText());
								System.out.println("valueset:"+ retrieveContext.valueset().qualifiedIdentifier().identifier().getText());*/

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


				if(sourceClauseCheck && whereClauseCheck){
					System.out.println("Possible supplemental definition:"+ctx.expressionDefinition().identifier().getText());
					return true;
				}
			}
		}
		catch(Exception er){
			return false;
		}
		
		return false;
	}

	private List<String> findDefinitionChildren(cqlParser.StatementContext ctx) {
		ExpressionDefinitionContext expressionDefinitionContext = ctx.expressionDefinition();
		List<ParseTree> parseTreeList = expressionDefinitionContext.children;
		List<String> childTokens = new ArrayList<String>();
		
		for(ParseTree tree:parseTreeList){
			if(tree.getChildCount() == 0){
				childTokens.add(tree.getText());
			}else{
				findDefinitionChildren(tree,childTokens);
			}
		}
			
		List<String> removeTokens = new ArrayList<String>();
		
		for(int i=0;i<childTokens.size();i++){
			String token = childTokens.get(i).trim();
			//System.out.println("token:"+token);
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

	private void findDefinitionChildren(ParseTree tree, List<String> childTokens) {
		int childCount = tree.getChildCount();

		for(int i=0;i<childCount;i++){
			ParseTree childTree = tree.getChild(i);
			if(childTree.getChildCount() == 0){
				childTokens.add(childTree.getText());
			}else{
				findDefinitionChildren(childTree,childTokens);
			}
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
		List<CQLDefinitionModelObject> definitionsList = new ArrayList(this.cqlFileObject.getDefinitionsMap().values());
		
		//find definitions referring to other definitions
		for(CQLDefinitionModelObject cqlDefinitionModelObject:definitionsList){
			
			findDefinitionsReferences(cqlDefinitionModelObject,definitionsList);
			
		}
	}

	private void findDefinitionsReferences(CQLDefinitionModelObject cqlDefinitionModelObject,
			List<CQLDefinitionModelObject> definitionsList) {
		
		for(CQLDefinitionModelObject cqlDefnModelObject:definitionsList){
			if(cqlDefnModelObject.getChildTokens().contains(cqlDefinitionModelObject.getIdentifier())){
				cqlDefnModelObject.getReferredToDefinitions().add(cqlDefinitionModelObject);
				cqlDefinitionModelObject.getReferredByDefinitions().add(cqlDefnModelObject);
			}
		}
		
	}

	public MATCQLParser getMatCQLListener() {
		return matCQLListener;
	}
	
}