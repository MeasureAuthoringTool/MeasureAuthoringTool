package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.cql.CQLLibraryAssociation;

public interface CQLLibraryAssociationDAO extends IDAO<CQLLibraryAssociation, String> {

	void deleteAssociation(CQLLibraryAssociation cqlLibraryAssociation);
	
	int findAssociationCount(String associatedWithId);

}
