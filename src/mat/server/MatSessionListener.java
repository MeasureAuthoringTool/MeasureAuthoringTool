package mat.server;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MatSessionListener implements HttpSessionListener {
	private static final Log logger = LogFactory.getLog(MatSessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		logger.debug("Session created: " + arg0.getSession().getId());
		if(logger.isDebugEnabled()) {
			Thread.dumpStack();
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		logger.debug("Session destroyed: " + arg0.getSession().getId());
		if(logger.isDebugEnabled()) {
			Thread.dumpStack();
		}
	}

	
	
	

}
