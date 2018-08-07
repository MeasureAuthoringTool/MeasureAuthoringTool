package mat.dao.impl.clause;

import mat.dao.clause.ComponentMeasuresDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.ComponentMeasure;

public class ComponentMeasureDAO extends GenericDAO<ComponentMeasure, String> implements ComponentMeasuresDAO{

	@Override
	public void saveComponentMeasure(ComponentMeasure componentMeasure) {
		super.save(componentMeasure);
	}

}
