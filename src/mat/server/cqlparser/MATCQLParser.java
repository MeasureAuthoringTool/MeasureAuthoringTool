package mat.server.cqlparser;

import mat.model.cql.parser.CQLFileObject;

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

}
