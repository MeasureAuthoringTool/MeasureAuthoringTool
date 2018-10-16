package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class PeriodModel.
 */
public class PeriodModel implements IsSerializable{

	/** The uuid. */
	private String uuid;
	
	/** The start date. */
	private String startDate;
	
	/** The stop date. */
	private String stopDate;
	
	/** The start date uuid. */
	//private String startDateUuid;
	
	/** The stop date uuid. */
	//private String stopDateUuid;
	
	private boolean calenderYear;

	/**
	 * Checks if is calender year.
	 *
	 * @return true, if is calender year
	 */
	public boolean isCalenderYear() {
		return calenderYear;
	}

	/**
	 * Sets the calender year.
	 *
	 * @param isCalenderYear the new calender year
	 */
	public void setCalenderYear(boolean calenderYear) {
		this.calenderYear = calenderYear;
	}

	/**
	 * Gets the uuid.
	 * 
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid.
	 * 
	 * @param uuid
	 *            the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the start date.
	 * 
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the stop date.
	 * 
	 * @return the stopDate
	 */
	public String getStopDate() {
		return stopDate;
	}

	/**
	 * Sets the stop date.
	 * 
	 * @param stopDate
	 *            the stopDate to set
	 */
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	/**
	 * Gets the start date uuid.
	 * 
	 * @return the startDateUuid
	 */
//	public String getStartDateUuid() {
//		return startDateUuid;
//	}
//
//	/**
//	 * Sets the start date uuid.
//	 * 
//	 * @param startDateUuid
//	 *            the startDateUuid to set
//	 */
//	public void setStartDateUuid(String startDateUuid) {
//		this.startDateUuid = startDateUuid;
//	}
//
//	/**
//	 * Gets the stop date uuid.
//	 * 
//	 * @return the stopDateUuid
//	 */
//	public String getStopDateUuid() {
//		return stopDateUuid;
//	}
//
//	/**
//	 * Sets the stop date uuid.
//	 * 
//	 * @param stopDateUuid
//	 *            the stopDateUuid to set
//	 */
//	public void setStopDateUuid(String stopDateUuid) {
//		this.stopDateUuid = stopDateUuid;
//	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PeriodModel [uuid=" + uuid + ", startDate=" + startDate
				+ ", stopDate=" + stopDate + "]";
	}
	
}
