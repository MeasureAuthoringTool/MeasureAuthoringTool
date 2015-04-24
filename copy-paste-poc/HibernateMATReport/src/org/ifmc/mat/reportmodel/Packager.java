package mat.reportmodel;


public class Packager {
	private String id;
	private Measure measure;
	private Clause clause;
	private int sequence;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Measure getMeasure() {
		return measure;
	}
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}
	public Clause getClause() {
		return clause;
	}
	public void setClause(Clause clause) {
		this.clause = clause;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	
}
