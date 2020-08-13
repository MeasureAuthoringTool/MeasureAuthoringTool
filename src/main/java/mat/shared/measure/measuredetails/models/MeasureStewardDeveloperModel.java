package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.Author;
import mat.model.MeasureSteward;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MeasureStewardDeveloperModel implements MeasureDetailsComponentModel, IsSerializable{

	private String stewardId;
	private String stewardValue;
	
	private List<Author> measureDeveloperList = new ArrayList<>();
	private List<Author> selectedDeveloperList;
	private List<MeasureSteward> measureStewardList = new ArrayList<>();

	public MeasureStewardDeveloperModel() {

	}
	
	public MeasureStewardDeveloperModel(MeasureStewardDeveloperModel measureStewardDeveloperModel) {
		this.stewardId = measureStewardDeveloperModel.getStewardId();
		this.stewardValue = measureStewardDeveloperModel.getStewardValue();
		
		this.getMeasureStewardList().addAll(measureStewardDeveloperModel.getMeasureStewardList());
		this.getMeasureDeveloperList().addAll(measureStewardDeveloperModel.getMeasureDeveloperList());
		if (null != measureStewardDeveloperModel.getSelectedDeveloperList()) {
			this.selectedDeveloperList = new ArrayList<>();
			this.selectedDeveloperList.addAll(measureStewardDeveloperModel.getSelectedDeveloperList());	
		}
	}
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		final MeasureStewardDeveloperModel stewardDeveloperModel = (MeasureStewardDeveloperModel) model;
	
		final boolean isSameSteward = equalsIgnoreCase(stewardDeveloperModel.getStewardId(), this.getStewardId());
			
		boolean areEqualLists = false;

		final List<Author> originalList = new ArrayList<>(); 
		final List<Author> modifiedList = this.getSelectedDeveloperList();
		if(stewardDeveloperModel.getSelectedDeveloperList() != null && modifiedList != null) {
			originalList.addAll(stewardDeveloperModel.getSelectedDeveloperList());
			originalList.sort(Comparator.comparing(Author::getAuthorName));
			modifiedList.sort(Comparator.comparing(Author::getAuthorName));
			areEqualLists = originalList.equals(modifiedList);
		}
		
		return isSameSteward && areEqualLists;	
	}
	
	private boolean equalsIgnoreCase(String originalStr, String modifiedStr) {
		if(originalStr == null || modifiedStr == null) {
			return originalStr == modifiedStr;
		} else if (originalStr.equals(modifiedStr)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}

	@Override
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}
	@Override
	public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.isDirty(this);
	}

	public String getStewardId() {
		return stewardId;
	}

	public void setStewardId(String stewardId) {
		this.stewardId = stewardId;
	}

	public String getStewardValue() {
		return stewardValue;
	}

	public void setStewardValue(String stewardValue) {
		this.stewardValue = stewardValue;
	}

	public List<Author> getMeasureDeveloperList() {
		return measureDeveloperList;
	}

	public void setMeasureDeveloperList(List<Author> measureDeveloperList) {
		this.measureDeveloperList = measureDeveloperList;
	}

	public List<Author> getSelectedDeveloperList() {
		return selectedDeveloperList;
	}

	public void setSelectedDeveloperList(List<Author> selectedDeveloperList) {
		this.selectedDeveloperList = selectedDeveloperList;
	}

	public List<MeasureSteward> getMeasureStewardList() {
		return measureStewardList;
	}

	public void setMeasureStewardList(List<MeasureSteward> measureStewardList) {
		this.measureStewardList = measureStewardList;
	}

}
