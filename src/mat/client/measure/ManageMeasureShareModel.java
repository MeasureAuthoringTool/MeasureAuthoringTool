package mat.client.measure;

import java.util.List;

import mat.model.clause.MeasureShareDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class ManageMeasureShareModel.
 */
public class ManageMeasureShareModel implements IsSerializable {
	
	/** The data. */
	private List<MeasureShareDTO> data;
	
	/** The measure id. */
	private String measureId;
	
	/** The measure name. */
	private String measureName;
	
	/** The start index. */
	private int startIndex;
	
	/** The results total. */
	private int resultsTotal;
	
	/** The is private. */
	private boolean isPrivate;
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(List<MeasureShareDTO> data) {
		this.data = data;
	}
	

	/**
	 * Gets the number of rows.
	 * 
	 * @return the number of rows
	 */
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	/**
	 * Gets the start index.
	 * 
	 * @return the start index
	 */
	public int getStartIndex() {
		return startIndex;
	}
	
	/**
	 * Sets the start index.
	 * 
	 * @param startIndex
	 *            the new start index
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	/**
	 * Gets the results total.
	 * 
	 * @return the results total
	 */
	public int getResultsTotal() {
		return resultsTotal;
	}
	
	/**
	 * Sets the results total.
	 * 
	 * @param resultsTotal
	 *            the new results total
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}

	/**
	 * Gets the key.
	 * 
	 * @param row
	 *            the row
	 * @return the key
	 */
	public String getKey(int row) {
		return data.get(row).getUserId();
	}

	/**
	 * Gets the.
	 * 
	 * @param row
	 *            the row
	 * @return the measure share dto
	 */
	public MeasureShareDTO get(int row) {
		return data.get(row);
	}


	/**
	 * Gets the measure id.
	 * 
	 * @return the measure id
	 */
	public String getMeasureId() {
		return measureId;
	}


	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the new measure id
	 */
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}


	/**
	 * Gets the measure name.
	 * 
	 * @return the measure name
	 */
	public String getMeasureName() {
		return measureName;
	}


	/**
	 * Sets the measure name.
	 * 
	 * @param measureName
	 *            the new measure name
	 */
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}


	/**
	 * Checks if is private.
	 * 
	 * @return true, if is private
	 */
	public boolean isPrivate() {
		return isPrivate;
	}


	/**
	 * Sets the private.
	 * 
	 * @param isPrivate
	 *            the new private
	 */
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

}
