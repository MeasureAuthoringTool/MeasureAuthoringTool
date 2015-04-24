package mat.client.measure;

import mat.client.history.HistoryBaseView;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * The Class ManageMeasureHistoryView.
 */
public class ManageMeasureHistoryView  extends HistoryBaseView implements ManageMeasurePresenter.HistoryDisplay{

	
	/** The measure id. */
	private String measureId;
	
	/** The measure name. */
	private String measureName;
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.HistoryDisplay#setMeasureName(java.lang.String)
	 */
	@Override
	public void setMeasureName(String name) {
		this.measureName = name;
		nameText.setText("Measure: " + name);
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.HistoryDisplay#setMeasureId(java.lang.String)
	 */
	@Override
	public void setMeasureId(String id) {
		this.measureId = id;
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.HistoryDisplay#getMeasureId()
	 */
	@Override
	public String getMeasureId() {
		return measureId;
	}



	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.HistoryDisplay#getMeasureName()
	 */
	@Override
	public String getMeasureName() {
		return measureName;
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.HistoryDisplay#getReturnToLink()
	 */
	@Override
	public HasClickHandlers getReturnToLink() {
		return goBackLink;
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.HistoryDisplay#setReturnToLinkText(java.lang.String)
	 */
	@Override
	public void setReturnToLinkText(String s) {
		goBackLink.setText(s);
	}
	
}
