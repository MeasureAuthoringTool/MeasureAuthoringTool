package mat.server.hqmf.qdm;

import javax.xml.xpath.XPathExpressionException;
import mat.model.clause.MeasureExport;

/**
 * 
 */
/**
 * @author jnarang
 *
 */
public class HQMFAttributeGenerator extends HQMFDataCriteriaElementGenerator {
	/** Measure Export object**/
	private MeasureExport measureExport;
	
	/**
	 * This method will look for attributes used in the subTree logic and then generate appropriate data criteria entries.
	 *
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	/*private void createDataCriteriaForAttributes(Node qdmNode, Element dataCriteriaElem,
			XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor, Node attributeQDMNode
			) throws XPathExpressionException {
		
		String attributeName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		String attributeMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		if (!NEGATION_RATIONALE.equals(attributeName)) {
			if (START_DATETIME.equals(attributeName) || STOP_DATETIME.equals(attributeName)
					|| SIGNED_DATETIME.equals(attributeName)
					|| RECORDED_DATETIME.equals(attributeName)
					|| ABATEMENT_DATETIME.equals(attributeName)) {
				generateOrderTypeAttributes(qdmNode, dataCriteriaElem, dataCriteriaXMLProcessor,
						simpleXmlprocessor, attributeQDMNode);
			} else if (SIGNED_DATETIME.equals(attributeName)) {
				Element timeNode = dataCriteriaXMLProcessor.getOriginalDoc().createElement(TIME);
				generateDateTimeAttributesTag(timeNode, attributeQDMNode, dataCriteriaElem, dataCriteriaXMLProcessor, true);
			} else if (ADMISSION_DATETIME.equalsIgnoreCase(attributeName)
					|| DISCHARGE_DATETIME.equalsIgnoreCase(attributeName)
					|| REMOVAL_DATETIME.equalsIgnoreCase(attributeName)
					|| ACTIVE_DATETIME.equalsIgnoreCase(attributeName)
					|| TIME.equalsIgnoreCase(attributeName)
					|| DATE.equalsIgnoreCase(attributeName)
					|| ONSET_DATETIME.equalsIgnoreCase(attributeName)) {
				generateDateTimeAttributes(qdmNode, dataCriteriaElem,
						dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			} else if (FACILITY_LOCATION_ARRIVAL_DATETIME.equalsIgnoreCase(attributeName)
					|| FACILITY_LOCATION_DEPARTURE_DATETIME.equalsIgnoreCase(attributeName)) {
				generateFacilityLocationTypeAttributes(qdmNode, dataCriteriaElem,
						dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			} else if (DOSE.equalsIgnoreCase(attributeName)
					|| LENGTH_OF_STAY.equalsIgnoreCase(attributeName)) {
				generateDoseTypeAttributes(qdmNode, dataCriteriaElem,
						dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			} else if (attributeName.equalsIgnoreCase(REFILLS)) {
				generateRepeatNumber(qdmNode, dataCriteriaXMLProcessor, dataCriteriaElem, attributeQDMNode, REPEAT_NUMBER);
			} else if (attributeName.equalsIgnoreCase(DISCHARGE_STATUS)) {
				generateDischargeStatus(qdmNode, dataCriteriaXMLProcessor, dataCriteriaElem, attributeQDMNode);
			} else if (attributeName.equalsIgnoreCase(INCISION_DATETIME)) {
				generateIncisionDateTimeTypeAttributes(qdmNode, dataCriteriaElem,
						dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			} else if (attributeName.equalsIgnoreCase(PRINCIPAL_DIAGNOSIS)
					|| attributeName.equalsIgnoreCase(DIAGNOSIS)) {
				generatePrincipalAndDiagnosisAttributes(qdmNode, dataCriteriaElem,
						dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			} else if (VALUE_SET.equals(attributeMode)
					|| CHECK_IF_PRESENT.equals(attributeMode)
					|| attributeMode.startsWith(LESS_THAN)
					|| attributeMode.startsWith(GREATER_THAN)
					|| EQUAL_TO.equals(attributeMode)) {
				//handle "Value Set", "Check If Present" and comparison(less than, greater than, equals) mode
				generateOtherAttributes(qdmNode, dataCriteriaElem,
						dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			}
		}
	}
	 */
	
	
	@Override
	public String generate(MeasureExport me) throws Exception {
		
		return null;
	}
	
	/**
	 * @param measureExport
	 * @param qdmNode
	 * @param excerptElement
	 * @param dataCriteriaXMLProcessor
	 * @param simpleXmlprocessor
	 * @param attributeQDMNode
	 * @throws XPathExpressionException
	 */
	//@Override
	/*public void generateAttributeTagForFunctionalOp(MeasureExport measureExport, Node qdmNode, Element excerptElement
			, Node attributeQDMNode) throws XPathExpressionException{
		this.setMeasureExport(measureExport);
		getExtensionValueBasedOnVersion(measureExport);
		createDataCriteriaForAttributes(qdmNode, excerptElement, measureExport.getHQMFXmlProcessor()
				, measureExport.getSimpleXMLProcessor(), attributeQDMNode);
	}*/
	
	public MeasureExport getMeasureExport() {
		return measureExport;
	}
	
	public void setMeasureExport(MeasureExport measureExport) {
		this.measureExport = measureExport;
	}
	
	
}
