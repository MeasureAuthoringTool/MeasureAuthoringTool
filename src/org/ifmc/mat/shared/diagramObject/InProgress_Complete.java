package org.ifmc.mat.shared.diagramObject;

import java.util.ArrayList;
import java.util.List;

public class InProgress_Complete {
	public static enum INPROGRESS_COMPLETE {
		IN_PROGRESS("In Progress", false), COMPLETE("Complete", true);
		private String stringValue;
		private boolean booleanValue;
		
		private INPROGRESS_COMPLETE(String stringValue, boolean booleanValue) {
			this.stringValue = stringValue;
			this.booleanValue = booleanValue;
		}
		
		public String toString() {
			return stringValue;
		}

		public boolean isInProgress() {
			return !booleanValue;
		}
		
		public boolean isComplete() {
			return booleanValue;
		}
	}
	private INPROGRESS_COMPLETE inProgressComplete;
	
	public InProgress_Complete() {
		this.inProgressComplete = INPROGRESS_COMPLETE.IN_PROGRESS;
	}
	
	public InProgress_Complete(INPROGRESS_COMPLETE inProgressComplete) {
		this.inProgressComplete = inProgressComplete;
	}
	
	public InProgress_Complete(boolean isComplete) {
		this.inProgressComplete = (isComplete) ? INPROGRESS_COMPLETE.COMPLETE : INPROGRESS_COMPLETE.IN_PROGRESS;
	}
	
	public void set(String stringValue) {
		this.inProgressComplete = 
			(stringValue.equalsIgnoreCase(INPROGRESS_COMPLETE.IN_PROGRESS.toString()))
				? INPROGRESS_COMPLETE.IN_PROGRESS 
				: INPROGRESS_COMPLETE.COMPLETE;
	}
	
	public void set(boolean booleanValue) {
		this.inProgressComplete = 
			(booleanValue)
				? INPROGRESS_COMPLETE.IN_PROGRESS 
				: INPROGRESS_COMPLETE.COMPLETE;
	}
	
	public void setComplete() {
		this.inProgressComplete = INPROGRESS_COMPLETE.COMPLETE;
	}
	
	public boolean isComplete() {
		return this.inProgressComplete == INPROGRESS_COMPLETE.COMPLETE;
	}
	
	public void setInProgress() {
		this.inProgressComplete = INPROGRESS_COMPLETE.IN_PROGRESS;
	}
	
	public boolean isInProgress() {
		return this.inProgressComplete == INPROGRESS_COMPLETE.IN_PROGRESS;
	}
	
	public INPROGRESS_COMPLETE getComplete() {
		return this.inProgressComplete;
	}

	public String toString() {
		return this.inProgressComplete.toString();
	}
	
	public static List<String> getItems() {
		List<String>items = new ArrayList<String>();
		items.add(INPROGRESS_COMPLETE.IN_PROGRESS.toString());
		items.add(INPROGRESS_COMPLETE.COMPLETE.toString());
		return items;
	}
}
