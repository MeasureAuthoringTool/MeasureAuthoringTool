package org.ifmc.mat.dao.clause;

import java.util.List;

import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.model.clause.Decision;

public interface DecisionDAO extends IDAO<Decision, String> {
	public void save(Decision parent);
	public Decision find(String parentId);
	public List<Decision> findByParentId(String parentId);
	public List<Decision> findByOperator(String operator);
	public void deleteAndUpdateParent(String[] ids);
}
