package mat.simplexml.model;


public class Reference {
	private Qdsel qdsel;
	private String ttext;

	public void diagram(PrettyPrinter pp) {
		pp.incrementIndentation();
		pp.concat("REFERENCE");
		getQdsel().diagram(pp);
		if (getTtext() != null)
			pp.concat("TTEXT", getTtext());
		pp.decrementIndentation();
	}
	
	public Reference() {
	}
	
	public Reference(Qdsel qdsel) {
		setQdsel(qdsel);
	}
	
	public Qdsel getQdsel() {
		return qdsel;
	}
	
	public void setQdsel(Qdsel qdsel) {
		this.qdsel = qdsel;
	}
	
	public String getTtext() {
		return ttext;
	}
	
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
}