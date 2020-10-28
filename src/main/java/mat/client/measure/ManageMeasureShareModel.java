package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.clause.MeasureShareDTO;

import java.util.List;

/**
 * The Class ManageMeasureShareModel.
 */
public class ManageMeasureShareModel implements IsSerializable {
	
	/** The data. */
	private List<MeasureShareDTO> data;
	
	/** The is private. */
	private boolean isPrivate;
	/** The measure id. */
	private String measureId;
	
	/** The measure name. */
	private String measureName;
	
	/** The results total. */
	private int resultsTotal;
	
	/** The start index. */
	private int startIndex;
	
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
	 * @return the data
	 */
	public List<MeasureShareDTO> getData() {
		return data;
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
	 * Gets the measure id.
	 * 
	 * @return the measure id
	 */
	public String getMeasureId() {
		return measureId;
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
	 * Gets the number of rows.
	 * 
	 * @return the number of rows
	 */
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
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
	 * Gets the start index.
	 * 
	 * @return the start index
	 */
	public int getStartIndex() {
		return startIndex;
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
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(List<MeasureShareDTO> data) {
		this.data = data;
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
	 * Sets the measure name.
	 * 
	 * @param measureName
	 *            the new measure name
	 */
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
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
	 * Sets the start index.
	 * 
	 * @param startIndex
	 *            the new start index
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
}
