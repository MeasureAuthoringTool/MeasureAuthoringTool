package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.Author;
import mat.model.MeasureSteward;

public class MeasureStewardDeveloperModel implements MeasureDetailsComponentModel, IsSerializable{
	private List<MeasureSteward> measureStewardList;
	private List<Author> measureDeveloperList;
	
	public List<MeasureSteward> getMeasureStewardList() {
		return measureStewardList;
	}
	public void setMeasureSteward(List<MeasureSteward> measureStewardList) {
		this.measureStewardList = measureStewardList;
	}
	public List<Author> getMeasureDeveloperList() {
		return measureDeveloperList;
	}
	public void setMeasureDeveloperList(List<Author> measureDeveloperList) {
		this.measureDeveloperList = measureDeveloperList;
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
	@Override
	public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.isDirty(this);
	}
}
