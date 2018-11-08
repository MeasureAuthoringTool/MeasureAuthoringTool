package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.Widget;

public interface ComponentDetailView {
	public Widget getWidget();
	public void buildDetailView();
	public boolean isComplete();
	public boolean hasUnsavedChanges();
}
