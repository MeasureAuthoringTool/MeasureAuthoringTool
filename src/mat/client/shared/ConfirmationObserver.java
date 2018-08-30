package mat.client.shared;

public interface ConfirmationObserver {
	void onYesButtonClicked();
	void onNoButtonClicked();
	void onClose();
}
