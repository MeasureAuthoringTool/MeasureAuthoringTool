package mat.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * The Class SpringRemoteServiceServlet.
 */
public class SpringRemoteServiceServlet extends RemoteServiceServlet {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(SpringRemoteServiceServlet.class);
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8359364426336388916L;
	
	/** The context. */
	protected ApplicationContext context;
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("SpringRemoteServiceServlet init()");
		super.init(config);
		if(this.context == null) { 
			this.context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
			this.context.getAutowireCapableBeanFactory().autowireBean(this);
		}
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
		
		try {
			return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
		}
		catch(Exception exc) {	
			String uri = request.getRequestURI();
			String base = request.getScheme() + "://" + request.getServerName() + uri;
			base = base.substring(0,base.lastIndexOf("/") + 1);
			return super.doGetSerializationPolicy(request, base, strongName);
		}
	}

	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}
	
	/**
	 * Sets the context.
	 * 
	 * @param context
	 *            the new context
	 */
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	/**
	 * Load user by username.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user details
	 */
	public UserDetails loadUserByUsername(String userId) {
		return null;
	}
	
}
