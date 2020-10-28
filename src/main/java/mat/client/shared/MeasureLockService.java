/**
 * 
 */
package mat.client.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveMeasureResult;

/**
 * The Class MeasureLockService.
 * 
 * @author vandavar This service class has api to setTheMeasureLock and release
 *         the MeasureLock whenever needed.
 */
public class MeasureLockService {
	
	
	/** The resetting lock. */
	private boolean resettingLock = false;
	
	
	
	/*
	 * Client side method to set the measureLock. 
	 * This method makes RPC call by passing the current MeasureId.
	 */
	/**
	 * Sets the measure lock.
	 */
	public void setMeasureLock(){
		String currentMeasureId = getCurrentMeasureId();
		if(currentMeasureId != null && !currentMeasureId.equals("")){
			getMeasureService().updateLockedDate(currentMeasureId,getLoggedinUserId(), new AsyncCallback<SaveMeasureResult>() {
				
				@Override
				public void onSuccess(SaveMeasureResult result) {
					if(result.isSuccess())
						MatContext.get().startMeasureLockUpdate();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					//TODO need to change this alert as inline message?
					//Window.alert("Server error while setting the lockedoutDate");
				}
			});
			}
	}
	
	/*
	 * Client side method to  release measureLock. 
	 * This method makes RPC call by passing the current MeasureId.
	 */
	/**
	 * Release measure lock.
	 */
	public void releaseMeasureLock(){
		String currentMeasureId = getCurrentMeasureId();
		MatContext.get().stopMeasureLockUpdate();
		if(currentMeasureId != null && !currentMeasureId.equals("")){
			resettingLock = true;
			getMeasureService().resetLockedDate(currentMeasureId,getLoggedinUserId(), new AsyncCallback<SaveMeasureResult>() {
				
				@Override
				public void onSuccess(SaveMeasureResult result) {
					resettingLock = false;
				}
				
				@Override
				public void onFailure(Throwable caught) {
					//TODO need to change this alert as inline message?
					//Window.alert("Server error while releasing the lockedoutDate");
				}
			});
		}
	}
	
	/**
	 * Checks if is resetting lock.
	 * 
	 * @return true, if is resetting lock
	 */
	public boolean isResettingLock(){
		return resettingLock;
	}
	
	/**
	 * Checks if is current measure locked.
	 * 
	 * @return true, if is current measure locked
	 */
	private boolean isCurrentMeasureLocked(){
		if(getCurrentMeasureInfo() != null) {
			return getCurrentMeasureInfo().isLocked();
		}else {
			return false;
		}
	}
	
	/**
	 * Check for edit permission.
	 * 
	 * @return true, if successful
	 */
	public boolean checkForEditPermission(){
		if(isCurrentMeasureLocked()){
		     if(!getLoggedinUserId().equalsIgnoreCase(getCurrentMeasureInfo().getLockedUserId()))
				return false;
		}else if (!isCurrentMeasureEditable()){
			    return false;
		}
		return true;
	}
	
	/**
	 * Gets the current measure id.
	 * 
	 * @return the current measure id
	 */
	private String getCurrentMeasureId(){
		return MatContext.get().getCurrentMeasureId();
	}
	
	/**
	 * Gets the loggedin user id.
	 * 
	 * @return the loggedin user id
	 */
	private String getLoggedinUserId(){
		return MatContext.get().getLoggedinUserId();
	}
	
	/**
	 * Gets the measure service.
	 * 
	 * @return the measure service
	 */
	private MeasureServiceAsync getMeasureService(){
		return MatContext.get().getMeasureService();
	}
	
	/**
	 * Gets the current measure info.
	 * 
	 * @return the current measure info
	 */
	private MeasureSelectedEvent getCurrentMeasureInfo(){
	   return MatContext.get().getCurrentMeasureInfo();
	}
	
	/**
	 * Checks if is current measure editable.
	 * 
	 * @return true, if is current measure editable
	 */
	private boolean isCurrentMeasureEditable(){
		return MatContext.get().isCurrentMeasureEditable();
	}
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 */
	public void isMeasureLocked(String id){
		MatContext.get().getSynchronizationDelegate().setCheckingLock(true);
		getMeasureService().isMeasureLocked(id, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean isLocked) {
				
				SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
				synchDel.setMeasureIsLocked(isLocked);
				synchDel.setCheckingLock(false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
				synchDel.setMeasureIsLocked(false);
				synchDel.setCheckingLock(false);
			}
		});
	}
	
}
