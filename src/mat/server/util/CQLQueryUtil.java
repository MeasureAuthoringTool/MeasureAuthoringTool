package mat.server.util;

public class CQLQueryUtil {
	
	//private static final String OPEN_PARANTHESIS = "(";
	private static final String CLOSED_PARANTHESIS = ")";
	private static final String NEWLINE = "\n";
	
	public static String buildQuery(String qdmVersion, String setId, int level) {
		
		String result = 
				/*"SELECT  OWNER_ID" + 
				NEWLINE +*/
				"  FROM mat.model.clause.CQLLibrary CQL_LIB" + 
				NEWLINE + 
				" WHERE qdmVersion = '" + qdmVersion + "'" + 
				NEWLINE +
				"   AND DRAFT = 0" + 
				NEWLINE +
				"   AND SET_ID <> '" +  setId + "'" +
				NEWLINE +
				"   AND NOT EXISTS (" +
				NEWLINE +
				"\tSELECT 1" +
				NEWLINE +
				"\t  FROM mat.model.cql.CQLLibraryAssociation" +
				NEWLINE +
				"\t WHERE associationId IN ("
				;
		
		if (level == 0) {
			result = 
					/*"SELECT  OWNER_ID" + 
					NEWLINE +*/
					"  FROM mat.model.clause.CQLLibrary CQL_LIB" + 
					NEWLINE + 
					" WHERE qdmVersion = '" + qdmVersion + "'" + 
					NEWLINE +
					"   AND DRAFT = 0" + 
					NEWLINE +
					"   AND SET_ID <> '" +  setId + "'" +
					NEWLINE +
					"   AND NOT EXISTS (" +
					NEWLINE +
					"\tSELECT 1" +
					NEWLINE +
					"\t  FROM mat.model.cql.CQLLibraryAssociation" +
					NEWLINE +
					"\t WHERE associationId = CQL_LIB.id)";
			
		} else if (level > 0) {
		
			for (int i=0;i<level;i++) {
				
				String tab = "\t\t";
				for (int j=0; j<i; j++)
					tab = tab + "\t";
				
				
				if(i == level-1) {
					result = result +
							NEWLINE +
							tab + "SELECT cqlLibraryId" +
							NEWLINE +
							tab + "  FROM mat.model.cql.CQLLibraryAssociation" +
							NEWLINE +
							tab + " WHERE associationId = CQL_LIB.id";
				} else {
					result = result +
							NEWLINE +
							tab + "SELECT cqlLibraryId" +
							NEWLINE +
							tab + "  FROM mat.model.cql.CQLLibraryAssociation" +
							NEWLINE +
							tab + " WHERE associationId IN (";
				}
				
			}
			
			for (int i=0; i<=level; i++)
				result = result + CLOSED_PARANTHESIS;
		} else {
			result = null;
		}
		
		return result;
	}

	public static String buildSearchForReplaceLibraryQuery(String qdmVersion, String setId, int level) {
		String result = 
				/*"SELECT  OWNER_ID" + 
				NEWLINE +*/
				"  FROM mat.model.clause.CQLLibrary CQL_LIB" + 
				NEWLINE + 
				" WHERE qdmVersion = '" + qdmVersion + "'" + 
				NEWLINE +
				"   AND DRAFT = 0" + 
				NEWLINE +
				"   AND SET_ID = '" +  setId + "'" +
				NEWLINE +
				"   AND NOT EXISTS (" +
				NEWLINE +
				"\tSELECT 1" +
				NEWLINE +
				"\t  FROM mat.model.cql.CQLLibraryAssociation" +
				NEWLINE +
				"\t WHERE cqlLibraryId IN ("
				;
		
		for (int i=0;i<level;i++) {
			
			String tab = "\t\t";
			for (int j=0; j<i; j++)
				tab = tab + "\t";
			
			
			if(i == level-1) {
				result = result +
						NEWLINE +
						tab + "SELECT associationId" +
						NEWLINE +
						tab + "  FROM mat.model.cql.CQLLibraryAssociation" +
						NEWLINE +
						tab + " WHERE cqlLibraryId = CQL_LIB.id";
			} else {
				result = result +
						NEWLINE +
						tab + "SELECT associationId" +
						NEWLINE +
						tab + "  FROM mat.model.cql.CQLLibraryAssociation" +
						NEWLINE +
						tab + " WHERE cqlLibraryId IN (";
			}
			
		}
		
		for (int i=0; i<=level; i++)
			result = result + CLOSED_PARANTHESIS;
		
		//result = result + SEMICOLON;
		
		return result;
	}

}
