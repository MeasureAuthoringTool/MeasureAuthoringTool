package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLLibraryShareDTO;
import mat.shared.LibrarySearchModel;

import java.util.List;

public interface CQLLibraryDAO extends IDAO<CQLLibrary, String>{

	List<CQLLibraryShareDTO> search(LibrarySearchModel librarySearchModel, int pageSize, User user);

	boolean isLibraryLocked(String id);

	void updateLockedOutDate(CQLLibrary existingLibrary);

	String findMaxVersion(String setId, String ownerId);

    CQLLibrary findByNameAndVersion(String name, double version, int revisionNumber);

	String findMaxOfMinVersion(String setId, String version);

	List<CQLLibraryShareDTO> getLibraryShareInfoForLibrary(String cqlId, String searchText);

	ShareLevel findShareLevelForUser(String cqlLibraryId, String userID, String cqlLibrarySetId);

	CQLLibraryShareDTO extractDTOFromCQLLibrary(CQLLibrary cqlLibrary);

	List<CQLLibrary> searchForIncludes(String setId, String libraryName, String searchText, String modelType);

	List<CQLLibrary> getLibraryListForLibraryOwner(User user);

	List<CQLLibrary> getAllLibrariesInSet(List<CQLLibrary> libraries);

	List<CQLLibraryShare> getLibraryShareInforForLibrary(String libId);

	void refresh(CQLLibrary libObject);

	CQLLibrary getLibraryByMeasureId(String measureId);

	List<CQLLibrary> searchForReplaceLibraries(String setId);

	boolean isLibraryNameExists(String name, String setId);

	String getLibraryNameIfDraftAlreadyExists(String setId);

    List<CQLLibrary> getDraftLibraryBySet(String cqlSetId);

    int deleteDraftFhirLibrariesBySetId(String setId);
}
