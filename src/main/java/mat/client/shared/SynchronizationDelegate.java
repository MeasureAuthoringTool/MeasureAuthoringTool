package mat.client.shared;

/**
 * Delegate all synchronization flags through one point for centralized
 * refactoring/maintenance.
 * 
 * @author aschmidt
 */
public class SynchronizationDelegate {
	
	/** The saving measure details. */
	private boolean savingMeasureDetails;
	
	/** The saving clauses. */
	private boolean savingClauses;
	
	/** The is checking lock. */
	private boolean isCheckingLock = false;
	
	/** The measure is locked. */
	private boolean measureIsLocked = false;
	
	/** The log off flag. */
	private boolean logOffFlag = false;
	
	private boolean libraryIsLocked = false;
	
	/**
	 * Checks if is saving measure details.
	 * 
	 * @return true, if is saving measure details
	 */
	public boolean isSavingMeasureDetails() {
		return savingMeasureDetails;
	}
	
	/**
	 * Sets the saving measure details.
	 * 
	 * @param savingMeasureDetails
	 *            the new saving measure details
	 */
	public void setSavingMeasureDetails(boolean savingMeasureDetails) {
		this.savingMeasureDetails = savingMeasureDetails;
	}
	
	/**
	 * Checks if is saving clauses.
	 * 
	 * @return true, if is saving clauses
	 */
	public boolean isSavingClauses() {
		return savingClauses;
	}
	
	/**
	 * Sets the saving clauses.
	 * 
	 * @param savingClauses
	 *            the new saving clauses
	 */
	public void setSavingClauses(boolean savingClauses) {
		this.savingClauses = savingClauses;
	}
	
	/**
	 * Checks if is checking lock.
	 * 
	 * @return true, if is checking lock
	 */
	public boolean isCheckingLock() {
		return isCheckingLock;
	}
	
	/**
	 * Sets the checking lock.
	 * 
	 * @param isCheckingLock
	 *            the new checking lock
	 */
	public void setCheckingLock(boolean isCheckingLock) {
		this.isCheckingLock = isCheckingLock;
	}
	
	/**
	 * Measure is locked.
	 * 
	 * @return true, if successful
	 */
	public boolean measureIsLocked() {
		return measureIsLocked;
	}
	
	/**
	 * Sets the measure is locked.
	 * 
	 * @param measureIsLocked
	 *            the new measure is locked
	 */
	public void setMeasureIsLocked(boolean measureIsLocked) {
		this.measureIsLocked = measureIsLocked;
	}
	
	/**
	 * Sets the log off flag.
	 */
	public void setLogOffFlag() {
		this.logOffFlag = true;
	}
	
	/**
	 * Gets the log off flag.
	 * 
	 * @return the log off flag
	 */
	public boolean getLogOffFlag() {
		return logOffFlag;
	}

	public boolean isLibraryIsLocked() {
		return libraryIsLocked;
	}

	public void setLibraryIsLocked(boolean libraryIsLocked) {
		this.libraryIsLocked = libraryIsLocked;
	}
	
	
}
