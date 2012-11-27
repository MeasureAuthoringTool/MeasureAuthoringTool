package org.ifmc.mat.dao.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HibernateService {

	private String[] springConfigurations;
	private ClassPathXmlApplicationContext context;
	
	public HibernateService(String[] springConfigurations) {
		this.springConfigurations = springConfigurations;
		
		context = new ClassPathXmlApplicationContext(this.springConfigurations);
	}
	
	public String[] getSpringConfigurations() {
		return springConfigurations;
	}
	
	public void setSpringConfigurations(String[] springConfigurations) {
		this.springConfigurations = springConfigurations;
	}
	
	public ClassPathXmlApplicationContext getContext() {
		return context;
	}
	
	public void setContext(ClassPathXmlApplicationContext context) {
		this.context = context;
	}
}
