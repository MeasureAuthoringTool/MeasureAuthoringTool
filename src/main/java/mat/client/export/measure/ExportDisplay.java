package mat.client.export.measure;

import com.google.gwt.event.dom.client.HasClickHandlers;

import mat.client.measure.BaseDisplay;

public interface ExportDisplay extends BaseDisplay {

	HasClickHandlers getCancelButton();

	HasClickHandlers getOpenButton();

	HasClickHandlers getSaveButton();

	boolean isHQMF();
	
	boolean isHumanReadable();

	boolean isELM();

	boolean isJSON();

	boolean isEMeasurePackage();

	boolean isSimpleXML();

	boolean isCQLLibrary();

	void setExportOptionsBasedOnVersion(String releaseVersion, boolean isCompositeMeasure, String measureModel);

	boolean isCompositeMeasurePackageRadio();

	void showCompositeMeasure(boolean isComposite);

	boolean isXml();

	boolean isAll();
}
