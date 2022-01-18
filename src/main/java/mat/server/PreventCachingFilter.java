package mat.server;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * The Class PreventCachingFilter.
 */
public class PreventCachingFilter implements Filter{
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(PreventCachingFilter.class);
	
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
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		String requestURI = httpRequest.getRequestURI();
		
		if(requestURI.endsWith("/")) {
			httpResponse.setStatus(302);
			logger.debug("Redirecting request for " + httpRequest.getRequestURI() + " to Mat in session " + httpRequest.getSession().getId());
			String url = "Mat.html";
			if(httpRequest.getQueryString() != null) {
				url = url + "?" + httpRequest.getQueryString();
			}
			httpResponse.setHeader("Location", url);
		}
		else if(requestURI.contains("/Mat.html") || requestURI.contains("/Bonnie.html")) {
			logger.debug("PreventCachingFilter");
			
			//
			// prevent the mat.html and bonnie.html file from being cached somewhere
			//
			Date now = new Date();
			httpResponse.setDateHeader("Date", now.getTime());
			// one day old
			httpResponse.setDateHeader("Expires", 0);
			httpResponse.setHeader("Pragma", "no-cache");
			httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
			if(LoggedInUserUtil.getLoggedInUser() == null) {
				logger.debug("Redirecting request for " + httpRequest.getRequestURI() + " to Login in session " + httpRequest.getSession().getId());
				httpResponse.setStatus(302);
				String url = "Login.html";
				if(httpRequest.getQueryString() != null) {
					url = url + "?" + httpRequest.getQueryString();
				}
				httpResponse.setHeader("Location", url);
			}
			else {
				chain.doFilter(request, response);
			}
		}
		else if (requestURI.contains("mat.nocache.") || requestURI.contains("bonnie.nocache.")) {
			   Date now = new Date();
			   httpResponse.setDateHeader("Date", now.getTime());
			   // one day old
			   httpResponse.setDateHeader("Expires", 0);
			   httpResponse.setHeader("Pragma", "no-cache");
			   httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
				chain.doFilter(request, response);
		}
		else {
			chain.doFilter(request, response);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
