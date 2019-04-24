package mat.server.util;

import java.io.IOException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.server.service.impl.XMLMarshalUtil;

@Component
public class CompositeMeasureDetailUtil {
	
	private final javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	public ManageCompositeMeasureDetailModel convertXMLIntoCompositeMeasureDetailModel(String xml) throws IOException, MappingException, MarshalException, ValidationException, XPathExpressionException {
		XmlProcessor processor = new XmlProcessor(xml);
		String componentMeasuresXPath = "/measure/measureDetails";
		Node measureDetailsNode = (Node) xPath.evaluate(componentMeasuresXPath, processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
		String measureDetailsXML = processor.transform(measureDetailsNode);
		
		XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
		
		return (ManageCompositeMeasureDetailModel) xmlMarshalUtil.convertXMLToObject("CompositeMeasureDetailsModelMapping.xml", measureDetailsXML, ManageCompositeMeasureDetailModel.class);
		
	}
}
