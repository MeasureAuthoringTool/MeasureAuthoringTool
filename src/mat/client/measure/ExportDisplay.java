package mat.client.measure;

import com.google.gwt.event.dom.client.HasClickHandlers;

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

	public void setMeasureName(String name);

	boolean isCQLLibrary();

	public void setVersion_Based_ExportOptions(String releaseVersion);
}
