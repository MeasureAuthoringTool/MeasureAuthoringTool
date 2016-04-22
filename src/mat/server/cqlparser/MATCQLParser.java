package mat.server.cqlparser;

import mat.model.cql.parser.CQLFileObject;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;


public class MATCQLParser {
	
	public CQLFileObject parseCQL(String cqlString){
		
		cqlLexer lexer = new cqlLexer(new ANTLRInputStream(cqlString));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
    	CQLErrorListener cqlErrorListener = new CQLErrorListener();
    	cqlParser parser = new cqlParser(tokens);
    	parser.setBuildParseTree(true);
    	parser.addErrorListener(cqlErrorListener);
    	MATCQLListener matcqlListener = new MATCQLListener(this);
    	parser.addParseListener(matcqlListener);
    	ParserRuleContext tree = parser.logic();
    	
    	System.out.println(parser.getNumberOfSyntaxErrors());
    	System.out.println(cqlErrorListener.getErrors());
    	
    	return matcqlListener.getCqlFileObject();
	}
		
    public static void main(String[] args) throws Exception {
    	
    	//cqlLexer lexer = new cqlLexer(new ANTLRFileStream("CMS146v2_QDM.cql")); 
    	cqlLexer lexer = new cqlLexer(new ANTLRFileStream("test_valueset_define.cql"));
    	//cqlLexer lexer = new cqlLexer(new ANTLRInputStream(""));
    	CommonTokenStream tokens = new CommonTokenStream(lexer);
    	CQLErrorListener cqlErrorListener = new CQLErrorListener();
    	cqlParser parser = new cqlParser(tokens);
    	parser.setBuildParseTree(true);
    	parser.addErrorListener(cqlErrorListener);
    	parser.addParseListener(new MATCQLListener(new MATCQLParser()));
    	//ParseTree parseTree = parser.logic();
    	ParserRuleContext tree = parser.logic();
    	//int childCount = tree.getChildCount();
//    	for(int i=0;i<childCount;i++){
//    		System.out.println(tree.getChild(i).getPayload().getClass().getName());
//    	}
    	//System.out.println("child count:"+childCount);
    	tree.inspect(parser);
    	
//    	cqlBaseVisitor<String> visitor = new cqlBaseVisitor<String>();
//    	visitor.visit(parseTree);
//    	
    	System.out.println(parser.getNumberOfSyntaxErrors());
    	System.out.println(cqlErrorListener.getErrors());
    	
    	//ExpressionDefinitionContext expressionDefinitionContext = parser.expressionDefinition();
    	//expressionDefinitionContext.children.get(0).getText();
    	
//    	LibraryDefinitionContext libraryDefinitionContext = parser.libraryDefinition();
//    	String text = libraryDefinitionContext.children.get(0).getText();
//    	System.out.println(text);
    	
    	
    }
	
	
}
