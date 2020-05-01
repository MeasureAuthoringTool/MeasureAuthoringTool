package mat.client.measure;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;

import mat.dto.AuditLogDTO;

public interface HistoryDisplay extends BaseDisplay {
	public void clearErrorMessage();

	public String getMeasureId();

	public String getMeasureName();

	public HasClickHandlers getReturnToLink();

	public void setErrorMessage(String s);

	public void setMeasureId(String id);

	public void setMeasureName(String name);

	public void setReturnToLinkText(String s);

	public void buildCellTable(List<AuditLogDTO> results);
}
