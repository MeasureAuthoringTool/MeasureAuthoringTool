package mat.client.measure.measuredetails.components;
import java.util.List;

import mat.model.MeasureType;
public class MeasureTypeModel implements MeasureDetailsComponentModel{
	private List<MeasureType> measureTypeList;

	public List<MeasureType> getMeasureTypeList() {
		return measureTypeList;
	}

	public void setMeasureTypeList(List<MeasureType> measureTypeList) {
		this.measureTypeList = measureTypeList;
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void updateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
