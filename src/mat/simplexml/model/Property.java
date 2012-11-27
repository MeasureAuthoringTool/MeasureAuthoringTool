package mat.simplexml.model;

public class Property {
	private String name;
	private String value;
	private High high;
	private Qdsel qdsel;
	private Low low;
	private Equal equal;

	public Qdsel getQdsel() {
		return qdsel;
	}
	public void setQdsel(Qdsel qdsel) {
		this.qdsel = qdsel;
	}
	public High getHigh() {
		return high;
	}
	public void setHigh(High high) {
		this.high = high;
	}
	public Low getLow() {
		return low;
	}
	public void setLow(Low low) {
		this.low = low;
	}
	public void setEqual(Equal equal){
		this.equal = equal;
	}
	public Equal getEqual(){
		return equal;
	}
	
	public Property(String name) {
		this.name = name;
		this.value = null;
	}
	public Property(String name, String value) {
		this.name = name;
		this.value = value;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
