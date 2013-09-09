package mat.server;

import mat.client.umls.service.VSACAPIService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.telligen.vsac.service.VSACTicketService;




public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements VSACAPIService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645961609626183169L;
	private static final Log logger = LogFactory.getLog(VSACAPIServiceImpl.class);

	@Override
	public String validateVsacUser(String userName, String password) {
		logger.info("Start validateVsacUser =====");
		String eightHourTicketForUser = new VSACTicketService().getTicketGrantingTicket(userName,password);
		logger.info("End validateVsacUser ===== eightHourTicketForUser =====" + eightHourTicketForUser);
		return eightHourTicketForUser;
	}

}
