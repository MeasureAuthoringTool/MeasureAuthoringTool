package mat.simplexml.model;

public class Period {
	
	private Startdate startdate;
	private Stopdate stopdate;
	private String uuid;
	private String  ttext;

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Startdate getStartdate() {
		return startdate;
	}
	public void setStartdate (Startdate startdate ) {
		this.startdate = startdate;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public Stopdate getStopdate() {
		return stopdate;
	}
	public void setStopdate (Stopdate stopdate ) {
		this.stopdate = stopdate;
	}
}