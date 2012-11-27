package org.ifmc.mat.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PreventCachingFilter implements Filter{
	private static final Log logger = LogFactory.getLog(PreventCachingFilter.class);
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		String requestURI = httpRequest.getRequestURI();
		
		if(requestURI.endsWith("/")) {
			httpResponse.setStatus(302);
			logger.info("Redirecting request for " + httpRequest.getRequestURI() + " to Mat in session " + httpRequest.getSession().getId());
			String url = "Mat.html";
			if(httpRequest.getQueryString() != null) {
				url = url + "?" + httpRequest.getQueryString();
			}
			httpResponse.setHeader("Location", url);
		}
		else if(requestURI.indexOf("/Mat.html") >= 0) {
			logger.info("PreventCachingFilter");
			
			//
			// prevent the mat.html file from being cached somewhere
			//
			httpResponse.addHeader("Cache-Control", "no-store");
			if(LoggedInUserUtil.getLoggedInUser() == null) {
				logger.info("Redirecting request for " + httpRequest.getRequestURI() + " to Login in session " + httpRequest.getSession().getId());
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
		else {
			chain.doFilter(request, response);
		}
//		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
