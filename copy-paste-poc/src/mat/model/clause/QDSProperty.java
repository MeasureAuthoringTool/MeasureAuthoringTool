package mat.model.clause;

/**
 * The Class QDSProperty.
 */
public class QDSProperty {
	
	/** The id. */
	private String id;
	
	/** The name. */
	private String name;
	
	/** The value. */
	private String value;
	
	/** The type. */
	private String type;
	
	/** The q ds element. */
	private QDSElement qDSElement;
	
	/**
	 * Gets the q ds element.
	 * 
	 * @return the q ds element
	 */
	public QDSElement getqDSElement() {
		if (qDSElement==null) return new QDSElement();
		return qDSElement;
	}
	
	/**
	 * Sets the q ds element.
	 * 
	 * @param qDSElement
	 *            the new q ds element
	 */
	public void setqDSElement(QDSElement qDSElement) {
		this.qDSElement = qDSElement;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
