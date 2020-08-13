package mat.client.measure;

import com.google.gwt.event.dom.client.HasClickHandlers;
import mat.client.history.HistoryBaseView;

public class ManageMeasureHistoryView  extends HistoryBaseView implements HistoryDisplay{

	private String measureId;
	private String measureName;

	@Override
	public void setMeasureName(String name) {
		this.measureName = name;
		nameText.setText("Measure: " + name);
	}

	@Override
	public void setMeasureId(String id) {
		this.measureId = id;
	}

	@Override
	public String getMeasureId() {
		return measureId;
	}

	@Override
	public String getMeasureName() {
		return measureName;
	}

	@Override
	public HasClickHandlers getReturnToLink() {
		return goBackLink;
	}

	@Override
	public void setReturnToLinkText(String s) {
		goBackLink.setText(s);
	}
}
