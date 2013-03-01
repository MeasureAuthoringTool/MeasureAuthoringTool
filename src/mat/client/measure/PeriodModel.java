package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PeriodModel implements IsSerializable{

	private String uuid;
	
	private String startDate;
	
	private String stopDate;
	
	private String startDateUuid;
	
	private String stopDateUuid;

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the stopDate
	 */
	public String getStopDate() {
		return stopDate;
	}

	/**
	 * @param stopDate the stopDate to set
	 */
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	/**
	 * @return the startDateUuid
	 */
	public String getStartDateUuid() {
		return startDateUuid;
	}

	/**
	 * @param startDateUuid the startDateUuid to set
	 */
	public void setStartDateUuid(String startDateUuid) {
		this.startDateUuid = startDateUuid;
	}

	/**
	 * @return the stopDateUuid
	 */
	public String getStopDateUuid() {
		return stopDateUuid;
	}

	/**
	 * @param stopDateUuid the stopDateUuid to set
	 */
	public void setStopDateUuid(String stopDateUuid) {
		this.stopDateUuid = stopDateUuid;
	}
	
}
