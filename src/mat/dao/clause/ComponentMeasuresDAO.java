package mat.dao.clause;


import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.ComponentMeasure;

public interface ComponentMeasuresDAO extends IDAO<ComponentMeasure, String> {
	
	public void saveComponentMeasures(List<ComponentMeasure> componentMeasuresList);
	
	public void updateComponentMeasures(String measureId, List<ComponentMeasure> componentMeasuresList);

	void deleteComponentMeasures(List<ComponentMeasure> componentMeasuresToDelete);

	public List<ComponentMeasure> findByMeasureId(String measureId);

}
