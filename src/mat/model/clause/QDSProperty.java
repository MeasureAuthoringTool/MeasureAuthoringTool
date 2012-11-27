package mat.model.clause;

public class QDSProperty {
	private String id;
	private String name;
	private String value;
	private String type;
	private QDSElement qDSElement;
	public QDSElement getqDSElement() {
		if (qDSElement==null) return new QDSElement();
		return qDSElement;
	}
	public void setqDSElement(QDSElement qDSElement) {
		this.qDSElement = qDSElement;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
