package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShareDTO;

public interface CQLLibraryDAO extends IDAO<CQLLibrary, String>{
	
		public List<CQLLibraryShareDTO> search(String searchText, String searchFrom, int pageSize, User user, int filter);

		public boolean isLibraryLocked(String id);

		public void updateLockedOutDate(CQLLibrary existingLibrary);

		public String findMaxVersion(String setId);

		public String findMaxOfMinVersion(String setId, String version);

		List<CQLLibraryShareDTO> getLibraryShareInfoForLibrary(String cqlId, String searchText);

		ShareLevel findShareLevelForUser(String cqlLibraryId, String userID, String cqlLibrarySetId);

		CQLLibraryShareDTO extractDTOFromCQLLibrary(CQLLibrary cqlLibrary);

		List<CQLLibrary> searchForIncludes(String searchText);
}
