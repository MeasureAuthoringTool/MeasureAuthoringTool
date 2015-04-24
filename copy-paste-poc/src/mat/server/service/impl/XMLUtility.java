package mat.server.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

/**
 * The Class XMLUtility.
 */
public class XMLUtility {

	/**
	 * Apply xsl.
	 * 
	 * @param input
	 *            the input
	 * @param xslt
	 *            the xslt
	 * @return the string
	 */
	public String applyXSL(String input, String xslt) 
	{
		//This system property sets the TransformFactory to use the Saxon TransformerFactoryImpl method
		//This line is needed to prevent issues with XSLT transform.
		System.setProperty("javax.xml.transform.TransformerFactory","net.sf.saxon.TransformerFactoryImpl"); 
		String result = null;
		ByteArrayOutputStream outStream = null;
		outStream = new ByteArrayOutputStream();
		TransformerFactory tFactory = TransformerFactoryImpl.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer(new StreamSource(xslt));
			transformer.transform(new StreamSource(new StringReader(input)), new StreamResult(outStream));
		}catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Configuring XML Transformer:", e.getCause());
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Transforming XML:", e.getCause());
		}catch(Exception e){
			e.printStackTrace();
		}
		result = new String(outStream.toByteArray());
		return result;
	}
	
	/**
	 * Gets the xML resource.
	 * 
	 * @param name
	 *            the name
	 * @return the xML resource
	 */
	public String getXMLResource(String name) {
		return getClass().getResource(name).toExternalForm();
	}
}
