package mat.client.clause.clauseworkspace.model;

import com.google.gwt.user.client.rpc.IsSerializable;


// TODO: Auto-generated Javadoc
/**
 * The Class MeasureXmlModel.
 */
public class MeasureXmlModel implements IsSerializable {

	/** The meausre export id. */
	private String meausreExportId;

	/** The measure id. */
	private String measureId;

	/** The xml. */
	private String xml;
	

	/** The to replace node. */
	private String toReplaceNode;

	/** The parent node. */
	private String parentNode;
	
	/**
	 * Gets the meausre export id.
	 * 
	 * @return the meausreExportId
	 */
	public String getMeausreExportId() {
		return meausreExportId;
	}

	/**
	 * Sets the meausre export id.
	 * 
	 * @param meausreExportId
	 *            the meausreExportId to set
	 */
	public void setMeausreExportId(String meausreExportId) {
		this.meausreExportId = meausreExportId;
	}

	/**
	 * Gets the measure id.
	 * 
	 * @return the measureId
	 */
	public String getMeasureId() {
		return measureId;
	}

	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the measureId to set
	 */
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	/**
	 * Gets the xml.
	 * 
	 * @return the xml
	 */
	public String getXml() {
		return xml;
	}

	/**
	 * Sets the xml.
	 * 
	 * @param xml
	 *            the xml to set
	 */
	public void setXml(String xml) {
		this.xml = xml;
	}

	/**
	 * Gets the to replace node.
	 * 
	 * @return the to replace node
	 */
	public String getToReplaceNode() {
		return toReplaceNode;
	}

	/**
	 * Sets the to replace node.
	 * 
	 * @param toReplaceNode
	 *            the new to replace node
	 */
	public void setToReplaceNode(String toReplaceNode) {
		this.toReplaceNode = toReplaceNode;
	}

	/**
	 * Gets the parent node.
	 * 
	 * @return the parentNode
	 */
	public String getParentNode() {
		return parentNode;
	}

	/**
	 * Sets the parent node.
	 * 
	 * @param parentNode
	 *            the parentNode to set
	 */
	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}

}
