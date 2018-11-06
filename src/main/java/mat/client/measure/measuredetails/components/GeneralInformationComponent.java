package mat.client.measure.measuredetails.components;

import java.util.Date;

public class GeneralInformationComponent {
	private Date finalizedDate;
	private boolean patientBased;
	private String guid;
	private String eCQMAbbreviatedTitle;
	private String eCQMVersionNumber;
	public Date getFinalizedDate() {
		return finalizedDate;
	}
	public void setFinalizedDate(Date finalizedDate) {
		this.finalizedDate = finalizedDate;
	}
	public boolean isPatientBased() {
		return patientBased;
	}
	public void setPatientBased(boolean patientBased) {
		this.patientBased = patientBased;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String geteCQMAbbreviatedTitle() {
		return eCQMAbbreviatedTitle;
	}
	public void seteCQMAbbreviatedTitle(String eCQMAbbreviatedTitle) {
		this.eCQMAbbreviatedTitle = eCQMAbbreviatedTitle;
	}
	public String geteCQMVersionNumber() {
		return eCQMVersionNumber;
	}
	public void seteCQMVersionNumber(String eCQMVersionNumber) {
		this.eCQMVersionNumber = eCQMVersionNumber;
	}
}
