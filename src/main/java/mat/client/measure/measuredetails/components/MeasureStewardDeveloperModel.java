package mat.client.measure.measuredetails.components;

import java.util.List;

import mat.DTO.StewardDTO;
import mat.model.Author;

public class MeasureStewardDeveloperModel implements MeasureDetailsComponentModel{
	private StewardDTO measureSteward;
	private List<Author> measureDeveloperList;
	
	public StewardDTO getMeasureSteward() {
		return measureSteward;
	}
	public void setMeasureSteward(StewardDTO measureSteward) {
		this.measureSteward = measureSteward;
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
}
