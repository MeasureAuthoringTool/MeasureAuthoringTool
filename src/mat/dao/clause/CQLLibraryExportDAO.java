package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.CQLLibraryExport;

public interface CQLLibraryExportDAO extends IDAO<CQLLibraryExport, String>{

	CQLLibraryExport findByLibraryId(String libraryId);
	
}
