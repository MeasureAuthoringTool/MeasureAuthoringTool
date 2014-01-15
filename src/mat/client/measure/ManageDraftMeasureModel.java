package mat.client.measure;

import java.util.List;

/**
 * The Class ManageDraftMeasureModel.
 */
class ManageDraftMeasureModel {
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	/**
	 * Sets the data.
	 * @param manageMeasureSearchModel
	 *            the new data
	 */
	public void setData(ManageMeasureSearchModel manageMeasureSearchModel) {
		this.data = manageMeasureSearchModel;
	}
	public List<ManageMeasureSearchModel.Result> getDataList() {
		return data.getData();
	}
}
