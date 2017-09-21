package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLLibraryShareDTO;

public interface CQLLibraryDAO extends IDAO<CQLLibrary, String>{
	
		public List<CQLLibraryShareDTO> search(String searchText, int pageSize, User user, int filter);

		public boolean isLibraryLocked(String id);

		public void updateLockedOutDate(CQLLibrary existingLibrary);

		public String findMaxVersion(String setId);

		public String findMaxOfMinVersion(String setId, String version);

		List<CQLLibraryShareDTO> getLibraryShareInfoForLibrary(String cqlId, String searchText);

		ShareLevel findShareLevelForUser(String cqlLibraryId, String userID, String cqlLibrarySetId);

		CQLLibraryShareDTO extractDTOFromCQLLibrary(CQLLibrary cqlLibrary);

		List<CQLLibrary> searchForIncludes(String setId, String searchText, boolean filter);
		
		String getAssociatedMeasureId(String measureId);
		
		String getSetIdForCQLLibrary(String cqlLibraryId);

		List<CQLLibrary> getLibraryListForLibraryOwner(User user);

		List<CQLLibrary> getAllLibrariesInSet(List<CQLLibrary> libraries);

		List<CQLLibraryShare> getLibraryShareInforForLibrary(String libId);

		void refresh(CQLLibrary libObject);

		CQLLibrary getLibraryByMeasureId(String measureId);

		List<CQLLibrary> searchForReplaceLibraries(String setId, boolean filter);
}
