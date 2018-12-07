package mat.shared.measure.measuredetails.models;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.MeasureType;
public class MeasureTypeModel implements MeasureDetailsComponentModel, IsSerializable{
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

	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}
}
