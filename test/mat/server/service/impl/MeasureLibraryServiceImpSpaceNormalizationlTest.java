package mat.server.service.impl;

import static org.junit.Assert.assertTrue;

import javax.xml.xpath.XPathExpressionException;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.server.MeasureLibraryServiceImpl;

import org.junit.Test;

public class MeasureLibraryServiceImpSpaceNormalizationlTest {
	

	@Test
	public void test1() {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		MeasureLibraryServiceImpl measureLibraryServiceImpl = new MeasureLibraryServiceImpl();
		measureXmlModel.setXml("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><subTree displayName=\"C a          d          e       t\" uuid=\"348C1486-CA35-44B9-AE8A-4E3B7AC32091\" qdmVariable=\"false\"/>");
		String clauseName = "Sp a c e s";
		try {
			String someXml = measureLibraryServiceImpl.clauseNameNormalizeSpaces(measureXmlModel, clauseName);
			measureXmlModel.setXml(someXml);
			assertTrue(measureXmlModel.getXml().contains("displayName=\"Sp a c e s\""));
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test2() {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		MeasureLibraryServiceImpl measureLibraryServiceImpl = new MeasureLibraryServiceImpl();
		measureXmlModel.setXml("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><subTree displayName=\"C a          d          e       t\" uuid=\"348C1486-CA35-44B9-AE8A-4E3B7AC32091\" qdmVariable=\"false\"/>");
		String clauseName = "C a d e t";
		try {
			String someXml = measureLibraryServiceImpl.clauseNameNormalizeSpaces(measureXmlModel, clauseName);
			measureXmlModel.setXml(someXml);
			assertTrue(measureXmlModel.getXml().contains("displayName=\"C a d e t\""));
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
