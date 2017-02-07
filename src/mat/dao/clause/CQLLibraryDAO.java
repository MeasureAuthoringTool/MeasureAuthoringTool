package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLLibraryModel;

public interface CQLLibraryDAO extends IDAO<CQLLibrary, String>{
	
		public List<CQLLibrary> search(String searchText, String searchFrom, int startIndex, int pageSize);

}
