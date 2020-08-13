package mat.dao.clause;


import mat.dao.IDAO;
import mat.model.clause.ComponentMeasure;

import java.util.List;

public interface ComponentMeasuresDAO extends IDAO<ComponentMeasure, String> {
	
	public void saveComponentMeasures(List<ComponentMeasure> componentMeasuresList);
	
	public void updateComponentMeasures(String measureId, List<ComponentMeasure> componentMeasuresList);

	public List<ComponentMeasure> findByComponentMeasureId(String measureId);

}
