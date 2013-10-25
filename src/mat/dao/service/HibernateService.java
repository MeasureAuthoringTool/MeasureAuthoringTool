package mat.dao.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The Class HibernateService.
 */
public class HibernateService {

	/** The spring configurations. */
	private String[] springConfigurations;
	
	/** The context. */
	private ClassPathXmlApplicationContext context;
	
	/**
	 * Instantiates a new hibernate service.
	 * 
	 * @param springConfigurations
	 *            the spring configurations
	 */
	public HibernateService(String[] springConfigurations) {
		this.springConfigurations = springConfigurations;
		
		context = new ClassPathXmlApplicationContext(this.springConfigurations);
	}
	
	/**
	 * Gets the spring configurations.
	 * 
	 * @return the spring configurations
	 */
	public String[] getSpringConfigurations() {
		return springConfigurations;
	}
	
	/**
	 * Sets the spring configurations.
	 * 
	 * @param springConfigurations
	 *            the new spring configurations
	 */
	public void setSpringConfigurations(String[] springConfigurations) {
		this.springConfigurations = springConfigurations;
	}
	
	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public ClassPathXmlApplicationContext getContext() {
		return context;
	}
	
	/**
	 * Sets the context.
	 * 
	 * @param context
	 *            the new context
	 */
	public void setContext(ClassPathXmlApplicationContext context) {
		this.context = context;
	}
}
