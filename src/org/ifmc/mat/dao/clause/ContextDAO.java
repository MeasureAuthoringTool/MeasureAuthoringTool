package org.ifmc.mat.dao.clause;

import java.util.List;

import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.model.clause.Context;

public interface ContextDAO extends IDAO<Context, String> {
	public List<Context> getAll();
	public Context getContext(String description);
}
