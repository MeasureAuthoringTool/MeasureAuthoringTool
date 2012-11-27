package mat.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class HibernateStatisticsFilter implements Filter {
	private static final Log logger = LogFactory.getLog(HibernateStatisticsFilter.class);

	private ApplicationContext context;
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		httpRequest.getSession();
		String uri = httpRequest.getRequestURI();
		logger.info("Requesting " + uri + " in session " + httpRequest.getSession().getId());
		
		if(logger.isDebugEnabled()) {
			if(!uri.endsWith(".png") && !uri.endsWith(".gif") && !uri.endsWith(".html") &&
					!uri.endsWith(".css") && !uri.endsWith(".js")) {
				SessionFactory sessionFactory = (SessionFactory)context.getBean("sessionFactory");
				
				Statistics stats = sessionFactory.getStatistics();
				logger.debug("Requesting " + httpRequest.getRequestURL());
				logger.debug("Open database sessions: " + stats.getSessionOpenCount());
				logger.debug("Closed database sessions: " + stats.getSessionCloseCount());
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.context =
			WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
	}

}
