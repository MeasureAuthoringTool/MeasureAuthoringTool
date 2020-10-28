package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.cql.CQLLibraryAssociation;

import java.util.List;

public interface CQLLibraryAssociationDAO extends IDAO<CQLLibraryAssociation, String> {

	void deleteAssociation(CQLLibraryAssociation cqlLibraryAssociation);
	
	int findAssociationCount(String associatedWithId);

	List<CQLLibraryAssociation> getAssociations(String associatedWithId);

}
