package mat.client.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveMeasureResult;

public class MeasureLockService {

	private boolean resettingLock = false;

	public void setMeasureLock() {
		String currentMeasureId = getCurrentMeasureId();
		if (currentMeasureId != null && !currentMeasureId.equals("")) {
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

	public void releaseMeasureLock() {
		String currentMeasureId = getCurrentMeasureId();
		MatContext.get().stopMeasureLockUpdate();
		if (currentMeasureId != null && !currentMeasureId.isEmpty() && !MatContext.get().isMeasureDeleted()) {
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
		} else {
            resettingLock = true;
        }
	}

	public boolean isResettingLock() {
		return resettingLock;
	}

	private boolean isCurrentMeasureLocked() {
		if (getCurrentMeasureInfo() != null) {
			return getCurrentMeasureInfo().isLocked();
		} else {
			return false;
		}
	}

	public boolean checkForEditPermission() {
		if (isCurrentMeasureLocked()) {
		     if (!getLoggedinUserId().equalsIgnoreCase(getCurrentMeasureInfo().getLockedUserId()))
				return false;
		} else if (!isCurrentMeasureEditable()){
			    return false;
		}
		return true;
	}

	private String getCurrentMeasureId() {
		return MatContext.get().getCurrentMeasureId();
	}

	private String getLoggedinUserId() {
		return MatContext.get().getLoggedinUserId();
	}

	private MeasureServiceAsync getMeasureService() {
		return MatContext.get().getMeasureService();
	}

	private MeasureSelectedEvent getCurrentMeasureInfo() {
	   return MatContext.get().getCurrentMeasureInfo();
	}

	private boolean isCurrentMeasureEditable() {
		return MatContext.get().isCurrentMeasureEditable();
	}

	public void isMeasureLocked(String id) {
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
