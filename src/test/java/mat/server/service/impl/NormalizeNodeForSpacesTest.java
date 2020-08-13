package mat.server.service.impl;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.server.util.XmlProcessor;
import org.junit.Test;

import javax.xml.xpath.XPathExpressionException;

import static org.junit.Assert.assertTrue;

public class NormalizeNodeForSpacesTest {
	

	@Test
	public void testSpaces() {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setXml("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><subTree displayName=\"The quick  brown   fox       jumps over the lazy dog.\" uuid=\"348C1486-CA35-44B9-AE8A-4E3B7AC32091\" qdmVariable=\"false\"/>");
		try {
			String normalizedXml = XmlProcessor.normalizeNodeForSpaces(measureXmlModel.getXml(), "//subTree/@displayName");
			measureXmlModel.setXml(normalizedXml);
			assertTrue(measureXmlModel.getXml().contains("displayName=\"The quick brown fox jumps over the lazy dog.\""));
		} catch (XPathExpressionException e) { 
			e.printStackTrace();
		}
	}

	@Test
	public void testTabsAndSpaces() {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setXml("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><subTree displayName=\"The quick brown	fox	 jumps \t\t over the lazy dog.\" uuid=\"348C1486-CA35-44B9-AE8A-4E3B7AC32091\" qdmVariable=\"false\"/>");
		try {
			String normalizedXml = XmlProcessor.normalizeNodeForSpaces(measureXmlModel.getXml(), "//subTree/@displayName");
			measureXmlModel.setXml(normalizedXml);
			assertTrue(measureXmlModel.getXml().contains("displayName=\"The quick brown fox jumps over the lazy dog.\""));
			} catch (XPathExpressionException e) {
				e.printStackTrace();
		}
	}

	@Test
	public void TestNewLineAndSpaces() {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setXml("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?><subTree displayName=\"The quick brown	fox	 jumps \n\n over the lazy dog.\" uuid=\"348C1486-CA35-44B9-AE8A-4E3B7AC32091\" qdmVariable=\"false\"/>");	
		try {
			String normalizedXml = XmlProcessor.normalizeNodeForSpaces(measureXmlModel.getXml(), "//subTree/@displayName");
			measureXmlModel.setXml(normalizedXml);
			assertTrue(measureXmlModel.getXml().contains("displayName=\"The quick brown fox jumps over the lazy dog.\""));
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void TestCarriageReturnAndSpaces() {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setXml("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?><subTree displayName=\"The quick brown  	  fox	 jumps \r\r over the lazy dog.\" uuid=\"348C1486-CA35-44B9-AE8A-4E3B7AC32091\" qdmVariable=\"false\"/>");	
		try {
			String normalizedXml = XmlProcessor.normalizeNodeForSpaces(measureXmlModel.getXml(), "//subTree/@displayName");
			measureXmlModel.setXml(normalizedXml);
			assertTrue(measureXmlModel.getXml().contains("displayName=\"The quick brown fox jumps over the lazy dog.\""));
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
}
