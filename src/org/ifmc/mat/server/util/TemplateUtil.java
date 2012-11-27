package org.ifmc.mat.server.util;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.ifmc.mat.shared.ConstantMessages;


/**
 * Template Util used to generate runtime content.
 * 	1) Uses velocity to generate the template.
 *
 */
public class TemplateUtil {
	
	private static TemplateUtil instance = new TemplateUtil();
	private VelocityEngine engine;
	
	private TemplateUtil() {
		initVelocity();
	}
	
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
	 * @param template
	 * @param paramsMap
	 * @return
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
