package mat.server;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The listener interface for receiving matSession events. The class that is
 * interested in processing a matSession event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addMatSessionListener<code> method. When
 * the matSession event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see MatSessionEvent
 */
public class MatSessionListener implements HttpSessionListener {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(MatSessionListener.class);

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		logger.debug("Session created: " + arg0.getSession().getId());
		if(logger.isDebugEnabled()) {
			Thread.dumpStack();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		logger.debug("Session destroyed: " + arg0.getSession().getId());
		if(logger.isDebugEnabled()) {
			Thread.dumpStack();
		}
	}

	
	
	

}
