package mat.client.measure;

import java.util.List;

import mat.model.clause.MeasureShareDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ManageMeasureShareModel implements IsSerializable {
	private List<MeasureShareDTO> data;
	private String measureId;
	private String measureName;
	private int startIndex;
	private int resultsTotal;
	private boolean isPrivate;
	
	public void setData(List<MeasureShareDTO> data) {
		this.data = data;
	}
	

	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public int getResultsTotal() {
		return resultsTotal;
	}
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}

	public String getKey(int row) {
		return data.get(row).getUserId();
	}

	public MeasureShareDTO get(int row) {
		return data.get(row);
	}


	public String getMeasureId() {
		return measureId;
	}


	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}


	public String getMeasureName() {
		return measureName;
	}


	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}


	public boolean isPrivate() {
		return isPrivate;
	}


	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

}
