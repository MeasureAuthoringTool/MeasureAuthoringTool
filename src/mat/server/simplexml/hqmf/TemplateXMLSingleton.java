package mat.server.simplexml.hqmf;

import java.io.File;

import mat.server.util.XmlProcessor;

public class TemplateXMLSingleton {
	
	private final static XmlProcessor templateXMLProcessor;
	
	static {
		templateXMLProcessor = new XmlProcessor(new File("templates.xml"));
	}
	
	public static XmlProcessor getTemplateXmlProcessor(){
		return templateXMLProcessor;
	}
	
}
