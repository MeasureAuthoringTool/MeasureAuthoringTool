package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.Context;

public interface ContextDAO extends IDAO<Context, String> {
	public List<Context> getAll();
	public Context getContext(String description);
}
