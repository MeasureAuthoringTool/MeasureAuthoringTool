/**
 * 
 */
package mat.util;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

/**
 * @author vandavar
 * Test for XMl Transform
 */
public class XMLUtilityTest {
	
	public static void main(String[] av) {
		String ud = System.getProperty("user.dir");
		String fs = File.separator;
		String xslt = ud+fs+"test"+fs+"XSLT Resources"+fs+"db2hqmf_08262010_1.xsl";
		System.out.println("Printing XSLT path: " + xslt);
		//This system property sets the TransformFactory to use the Saxon TransformerFactoryImpl method
		//This line is needed to prevent issues with XSLT transform.
		System.setProperty("javax.xml.transform.TransformerFactory","net.sf.saxon.TransformerFactoryImpl"); 
		TransformerFactory tFactory = TransformerFactoryImpl.newInstance();
		Transformer transformer = null;
		try {
			transformer = tFactory.newTransformer(new StreamSource(xslt));
			//transformer.transform(new StreamSource(new StringReader(input)), new StreamResult(outStream));
		}catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Configuring XML Transformer:", e.getCause());
		} catch(Exception e){
			e.printStackTrace();
		}
		if(transformer != null)
			System.out.println("XSLT has been transformed Sucessfully.");
		else
			System.out.println("Oh No there is a problem in Transforming");
			
			
	}

}
