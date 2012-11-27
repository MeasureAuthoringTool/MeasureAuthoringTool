package mat.simplexml.model;


public class Rationales {
	private String  ttext;
	private Rationale rationale;

	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public Rationale getRationale() {
		if(rationale == null)
			rationale = new Rationale();
		return rationale;
	}
	public void setRationale(Rationale rationales) {
		this.rationale = rationales;
	}
	
}