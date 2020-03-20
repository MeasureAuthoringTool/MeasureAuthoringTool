/**
 * 
 */
package mat.client.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.measure.service.CQLLibraryServiceAsync;
import mat.client.measure.service.SaveCQLLibraryResult;

/**
 * The Class LibraryLockService.
 * 
 */
public class LibraryLockService {
	
	
	/** The resetting lock. */
	private boolean resettingLock = false;
	
	
	
	/*
	 * Client side method to set the libraryLock. 
	 * This method makes RPC call by passing the current libraryId.
	 */
	/**
	 * Sets the library lock.
	 */
	public void setLibraryLock(){
		String currentLibraryId = getCurrentLibraryId();
		if(currentLibraryId != null && !currentLibraryId.equals("")){
			getLibraryService().updateLockedDate(currentLibraryId,getLoggedinUserId(), new AsyncCallback<SaveCQLLibraryResult>() {
				
				@Override
				public void onSuccess(SaveCQLLibraryResult result) {
					if(result.isSuccess())
						MatContext.get().startLibraryLockUpdate();
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
	 * Client side method to  release libraryLock. 
	 * This method makes RPC call by passing the current LibraryId.
	 */
	/**
	 * Release measure lock.
	 */
	public void releaseLibraryLock(){
		String currentLibraryId = getCurrentLibraryId();
		MatContext.get().stopMeasureLockUpdate();
		if(currentLibraryId != null && !currentLibraryId.equals("")){
			resettingLock = true;
			getLibraryService().resetLockedDate(currentLibraryId,getLoggedinUserId(), new AsyncCallback<SaveCQLLibraryResult>() {
				
				@Override
				public void onSuccess(SaveCQLLibraryResult result) {
					resettingLock = false;
				}
				
				@Override
				public void onFailure(Throwable caught) {
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
	 * Checks if is current library locked.
	 * 
	 * @return true, if is current library locked
	 */
	private boolean isCurrentLibraryLocked(){
		if(getCurrentLibraryInfo() != null) {
			return getCurrentLibraryInfo().isLocked();
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
		if(isCurrentLibraryLocked()){
		     if(!getLoggedinUserId().equalsIgnoreCase(getCurrentLibraryInfo().getLockedUserId()))
				return false;
		}else if (!isCurrentLibraryEditable()){
			    return false;
		}
		return true;
	}
	
	/**
	 * Gets the current measure id.
	 * 
	 * @return the current measure id
	 */
	private String getCurrentLibraryId(){
		return MatContext.get().getCurrentCQLLibraryId();
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
	private CQLLibraryServiceAsync getLibraryService(){
		return MatContext.get().getCQLLibraryService();
	}
	
	/**
	 * Gets the current library info.
	 * 
	 * @return the current library info
	 */
	private CQLLibrarySelectedEvent getCurrentLibraryInfo(){
	   return MatContext.get().getCurrentLibraryInfo();
	}
	
	/**
	 * Checks if is current library editable.
	 * 
	 * @return true, if is current library editable
	 */
	private boolean isCurrentLibraryEditable(){
		return MatContext.get().isCurrentLibraryEditable();
	}
	
	/**
	 * Checks if is library locked.
	 * 
	 * @param id
	 *            the id
	 */
	public void isLibraryLocked(String id){
		MatContext.get().getSynchronizationDelegate().setCheckingLock(true);
		getLibraryService().isLibraryLocked(id, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean isLocked) {
				
				SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
				synchDel.setLibraryIsLocked(isLocked);
				synchDel.setCheckingLock(false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
				synchDel.setLibraryIsLocked(false);
				synchDel.setCheckingLock(false);
			}
		});
	}
	
}
