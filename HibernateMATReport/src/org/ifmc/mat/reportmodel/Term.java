package mat.reportmodel;


public class Term {
	private String id;
	private int order;
	private Operator operator;
	private String nextTermId;
	private StaticElement staticElement;
	private QDSElement qDSElement;
	public Operator getOperator() {
		if (operator==null) return new Operator();
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	public String getNextTermId() {
		return nextTermId;
	}
	public void setNextTermId(String nextTermId) {
		this.nextTermId = nextTermId;
	}
	public StaticElement getStaticElement() {
		if (staticElement==null) return new StaticElement();
		return staticElement;
	}
	public void setStaticElement(StaticElement staticElement) {
		this.staticElement = staticElement;
	}
	public QDSElement getqDSElement() {
		if (qDSElement==null) new QDSElement();
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
