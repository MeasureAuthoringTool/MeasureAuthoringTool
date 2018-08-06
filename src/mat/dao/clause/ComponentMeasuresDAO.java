package mat.dao.clause;


import mat.dao.IDAO;
import mat.model.clause.ComponentMeasure;

public interface ComponentMeasuresDAO extends IDAO<ComponentMeasure, String> {
	
	public void saveComponentMeasure(ComponentMeasure componentMeasure);

}
