package mat.server.util;

import java.io.IOException;

import java.io.StringReader;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import mat.client.measure.ManageCompositeMeasureDetailModel;

@Component
public class CompositeMeasureDetailUtil {
	
	private final javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	public ManageCompositeMeasureDetailModel convertXMLIntoCompositeMeasureDetailModel(String xml) throws IOException, MappingException, MarshalException, ValidationException, XPathExpressionException {
		XmlProcessor processor = new XmlProcessor(xml);
		String componentMeasuresXPath = "/measure/measureDetails";
		Node measureDetailsNode = (Node) xPath.evaluate(componentMeasuresXPath, processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
		String measureDetailsXML = processor.transform(measureDetailsNode);
		
		Mapping mapping = new Mapping();
		mapping.loadMapping(new ResourceLoader().getResourceAsURL("CompositeMeasureDetailsModelMapping.xml"));
		Unmarshaller unmarshaller = new Unmarshaller(mapping);
		unmarshaller.setClass(ManageCompositeMeasureDetailModel.class);
		unmarshaller.setWhitespacePreserve(true);
		unmarshaller.setValidation(false);
		return (ManageCompositeMeasureDetailModel) unmarshaller.unmarshal(new InputSource(new StringReader(measureDetailsXML)));
	}
}
