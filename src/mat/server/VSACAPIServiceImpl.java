package mat.server;

import mat.client.umls.service.VSACAPIService;

import org.telligen.vsac.service.VSACTicketService;




public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements VSACAPIService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645961609626183169L;

	@Override
	public String validateVsacUser(String userName, String password) {
		String eightHourTicketForUser = new VSACTicketService().getTicketGrantingTicket(userName,password);
		return eightHourTicketForUser;
	}

}
