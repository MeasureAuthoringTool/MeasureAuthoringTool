package mat.client.shared;

/**
 * Delegate all synchronization flags through one point for centralized refactoring/maintenance
 * @author aschmidt
 *
 */
public class SynchronizationDelegate {
	
	private boolean savingMeasureDetails;
	private boolean savingClauses;
	private boolean isCheckingLock = false;
	private boolean measureIsLocked = false;
	private boolean logOffFlag = false;
	
	public boolean isSavingMeasureDetails() {
		return savingMeasureDetails;
	}
	public void setSavingMeasureDetails(boolean savingMeasureDetails) {
		this.savingMeasureDetails = savingMeasureDetails;
	}
	public boolean isSavingClauses() {
		return savingClauses;
	}
	public void setSavingClauses(boolean savingClauses) {
		this.savingClauses = savingClauses;
	}
	public boolean isCheckingLock() {
		return isCheckingLock;
	}
	public void setCheckingLock(boolean isCheckingLock) {
		this.isCheckingLock = isCheckingLock;
	}
	public boolean measureIsLocked() {
		return measureIsLocked;
	}
	public void setMeasureIsLocked(boolean measureIsLocked) {
		this.measureIsLocked = measureIsLocked;
	}
	public void setLogOffFlag() {
		this.logOffFlag = true;
	}
	public boolean getLogOffFlag() {
		return logOffFlag;
	}
	
	
}
