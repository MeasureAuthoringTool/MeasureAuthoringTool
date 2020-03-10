package mat.client.export.measure;

import com.google.gwt.event.dom.client.HasClickHandlers;

import mat.client.measure.BaseDisplay;
import mat.client.measure.ManageMeasureSearchModel;

public interface ExportDisplay extends BaseDisplay {

	public HasClickHandlers getCancelButton();

	public HasClickHandlers getOpenButton();

	public HasClickHandlers getSaveButton();

	public boolean isHQMF();
	
	public boolean isHumanReadable();

	public boolean isELM();

	public boolean isJSON();

	boolean isEMeasurePackage();

	public boolean isSimpleXML();

	boolean isCQLLibrary();

	public void setExportOptionsBasedOnVersion(ManageMeasureSearchModel.Result measure);

	boolean isCompositeMeasurePackageRadio();

	void showCompositeMeasure(boolean isComposite);
}
