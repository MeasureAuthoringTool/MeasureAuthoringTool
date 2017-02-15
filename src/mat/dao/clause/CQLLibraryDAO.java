package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.User;
import mat.model.clause.CQLLibrary;

public interface CQLLibraryDAO extends IDAO<CQLLibrary, String>{
	
		public List<CQLLibrary> search(String searchText, String searchFrom, int pageSize, User user, int filter);

}
