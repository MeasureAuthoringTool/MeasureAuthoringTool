package mat.shared.model;

/**
 * Lightweight Clause data store used by DiagramViewImpl
 * @author aschmidt
 *
 */
public class ClauseLT {

	private String measureID;
	//will be of the form '('<<'D' or 'v'>><<version>>')'<<clause name>>
	private String text;
	
	public String getMeasureID() {
		return measureID;
	}
	
	public void setMeasureID(String measureID) {
		this.measureID = measureID;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getVersion(){
		int len = text.indexOf(")");
		String version = text.substring(2, len);
		return version;
	}
	
	public boolean isDraft(){
		boolean isDraft = text.substring(1, 2).equalsIgnoreCase("d");
		return isDraft;
	} 
	
	public String getClauseName(){
		int len = text.indexOf(")");
		if(len < 0)
			return text;
		String name = text.substring(len+1, text.length());
		return name;
	}
	
}
