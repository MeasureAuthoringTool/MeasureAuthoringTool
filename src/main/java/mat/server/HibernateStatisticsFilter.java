package mat.server;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * The Class HibernateStatisticsFilter.
 */
public class HibernateStatisticsFilter implements Filter {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(HibernateStatisticsFilter.class);

	/** The context. */
	public static ApplicationContext context;
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		httpRequest.getSession();
		String uri = httpRequest.getRequestURI();
		logger.debug("Requesting " + uri + " in session " + httpRequest.getSession().getId());
		
		if(logger.isDebugEnabled()) {
			if(!uri.endsWith(".png") && !uri.endsWith(".gif") && !uri.endsWith(".html") &&
					!uri.endsWith(".css") && !uri.endsWith(".js")) {
				SessionFactory sessionFactory = context.getBean(SessionFactory.class);
				
				Statistics stats = sessionFactory.getStatistics();
				logger.debug("Requesting " + httpRequest.getRequestURL());
				logger.debug("Open database sessions: " + stats.getSessionOpenCount());
				logger.debug("Closed database sessions: " + stats.getSessionCloseCount());
			}
		}
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		HibernateStatisticsFilter.context =
			WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		HibernateStatisticsFilter.context = context;
	}

}
