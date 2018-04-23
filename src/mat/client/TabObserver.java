package mat.client;

public interface TabObserver {
	public boolean isValid();
	public void showUnsavedChangesError();
	public void updateOnBeforeSelection();
	public void notifyCurrentTabOfClosing();
}
