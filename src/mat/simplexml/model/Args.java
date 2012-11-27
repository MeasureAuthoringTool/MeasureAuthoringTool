package mat.simplexml.model;


public class Args {

	private Qdsel qdsel;
	private And and;
	private Or or;

	public Qdsel getQdsel() {
		return qdsel;
	}
	
	public void setQdsel(Qdsel qdsel) {
		this.qdsel = qdsel;
	}

	public And getAnd() {
		return and;
	}
	public void setAnd(And and) {
		this.and = and;
	}
	public Or getOr() {
		return or;
	}
	public void setOr (Or or) {
		this.or = or;
	}
}