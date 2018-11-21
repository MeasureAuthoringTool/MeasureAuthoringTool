package mat.client.measure.measuredetails.components;

import java.util.List;

import mat.model.Author;
import mat.model.MeasureSteward;

public class MeasureStewardDeveloperModel implements MeasureDetailsComponentModel{
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
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void updateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
