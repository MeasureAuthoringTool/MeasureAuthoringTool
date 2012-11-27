package mat.simplexml.model;

public class Version {
	private String  ttext;

	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		int pivot = ttext.indexOf(".");
		if(pivot > 0){
			ttext = ttext.substring(0, pivot);
		}
		this.ttext = ttext;
	}
}