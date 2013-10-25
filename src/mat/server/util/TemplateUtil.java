package mat.server.util;

import java.io.StringWriter;
import java.util.HashMap;

import mat.shared.ConstantMessages;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;


/**
 * Template Util used to generate runtime content.
 * 	1) Uses velocity to generate the template.
 *
 */
public class TemplateUtil {
	
	/** The instance. */
	private static TemplateUtil instance = new TemplateUtil();
	
	/** The engine. */
	private VelocityEngine engine;
	
	/**
	 * Instantiates a new template util.
	 */
	private TemplateUtil() {
		initVelocity();
	}
	
	/**
	 * Gets the single instance of TemplateUtil.
	 * 
	 * @return single instance of TemplateUtil
	 */
	public static TemplateUtil getInstance(){
		return instance;
	}
	
	/**
	 * Initializes velocity engine.
	 */
	private void initVelocity(){
		engine = new VelocityEngine();
		engine.setProperty("resource.loader", "file");
		engine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		engine.init();
	}
	
	/**
	 * Merges the template with the context parameters.
	 * 
	 * @param template
	 *            the template
	 * @param paramsMap
	 *            the params map
	 * @return the string
	 */
	public String mergeTemplate(String template, HashMap<String, Object> paramsMap){
		Template vm = engine.getTemplate(ConstantMessages.ROOT_PATH + template);		
		VelocityContext context = new VelocityContext();
		context.put("paramsMap", paramsMap);
		StringWriter writer = new StringWriter();		
		vm.merge(context, writer);
		return writer.getBuffer().toString();
	}
}
