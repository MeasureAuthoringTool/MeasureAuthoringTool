package mat.client.measure.measuredetails.components;

import java.util.List;

import mat.DTO.StewardDTO;
import mat.model.Author;

public class MeasureStewardDeveloperComponent {
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
}
