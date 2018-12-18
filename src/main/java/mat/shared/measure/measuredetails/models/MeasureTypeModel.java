package mat.shared.measure.measuredetails.models;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.MeasureType;
public class MeasureTypeModel implements MeasureDetailsComponentModel, IsSerializable{
	private List<MeasureType> measureTypeList;

	public MeasureTypeModel() {
		measureTypeList = new ArrayList<>(); 
	}
	
	public MeasureTypeModel(MeasureTypeModel originalMeasureTypeModel) {
		measureTypeList = new ArrayList<>(); 
		
		if(originalMeasureTypeModel.getMeasureTypeList() != null) {
			for(MeasureType type : originalMeasureTypeModel.getMeasureTypeList())  {
				MeasureType measureType = new MeasureType(); 
				measureType.setAbbrName(type.getAbbrName());
				measureType.setDescription(type.getDescription());
				measureType.setId(type.getId());
				measureTypeList.add(measureType);
			}
		}
	}

	public List<MeasureType> getMeasureTypeList() {
		return measureTypeList;
	}

	public void setMeasureTypeList(List<MeasureType> measureTypeList) {
		this.measureTypeList = measureTypeList;
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		MeasureTypeModel measureTypeModel = (MeasureTypeModel) model; 

		if(measureTypeModel.getMeasureTypeList() == null && this.measureTypeList == null) {
			return true; 
		} else if(measureTypeModel.getMeasureTypeList() == null && this.measureTypeList != null) {
			return false; 
		} else if(measureTypeModel.getMeasureTypeList() != null && this.measureTypeList == null) {
			return false; 
		} else {
			this.measureTypeList.sort(Comparator.comparing(MeasureType::getDescription));
			measureTypeModel.getMeasureTypeList().sort(Comparator.comparing(MeasureType::getDescription));
			
			if(measureTypeList.size() != measureTypeModel.getMeasureTypeList().size()) {
				return false; 
			}
			
			for(int i = 0; i < measureTypeList.size(); i++) {
				if(!measureTypeList.get(i).getDescription().equalsIgnoreCase(measureTypeModel.getMeasureTypeList().get(i).getDescription())) {
					return false; 
				}
			}
		}
		
		return true; 
	}

	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}

	@Override
	public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.isDirty(this);
	}
}
