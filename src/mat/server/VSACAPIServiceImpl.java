package mat.server;

import mat.client.umls.service.VSACAPIService;
import mat.model.CodeListSearchDTO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.telligen.vsac.dao.ValueSetsResponseDAO;
import org.telligen.vsac.object.ValueSet;
import org.telligen.vsac.object.ValueSetsResponse;
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
	
	@Override
	public CodeListSearchDTO getValueSetBasedOIDAndVersion(String eightHourTicket,String OID, String version){
		CodeListSearchDTO result = new CodeListSearchDTO();
		//To Do - To be removed.
		String[] testIDs = { "2.16.840.1.113883.3.526.2.39",
				"2.16.840.1.113883.3.666.5.1738",
				"2.16.840.1.113883.3.464.1003.199.11.1005" };
		String id = null;
		
		if(OID==null || StringUtils.isBlank(OID)){
			id= testIDs[0];
		}else{
			id=OID;
		}
		
		ValueSetsResponseDAO dao = new ValueSetsResponseDAO(eightHourTicket);
		
		if(version!=null && StringUtils.isNotBlank(version)){
		
			ValueSetsResponse vsr =dao.getSpecifiedValueSetsResponseByIDAndVersion(id, version);
			System.out.println("XML Returned====");
			System.out.println(vsr.getXmlPayLoad());
			result.setVsacXMLPayload(vsr.getXmlPayLoad());
			for(ValueSet list : vsr.getValueSetList()){
				result.setId(list.getID());
				result.setCodeSystem(list.getConceptList().getConceptList().get(0).getCodeSystemName());
				result.setName(list.getDisplayName());
				result.setOid(list.getID());
				result.setLastModified(list.getRevisionDate());
				if(list.getGroupList()!=null){
					result.setGroupedCodeList(true);
				}
				
			}
		}else{
			//ValueSetsResponse vsr = dao.getLatestVersionValueSetsResponseByID(id);
			ValueSetsResponse vsr = dao.getMultipleValueSetsResponseByOID(id);
			System.out.println("XML Returned====");
			System.out.println(vsr.getXmlPayLoad());
			result.setVsacXMLPayload(vsr.getXmlPayLoad());
			for(ValueSet list : vsr.getValueSetList()){
				result.setId(list.getID());
				result.setCodeSystem(list.getConceptList().getConceptList().get(0).getCodeSystemName());
				result.setName(list.getDisplayName());
				result.setOid(list.getID());
				result.setLastModified(list.getRevisionDate());
				if(list.getGroupList()!=null){
					result.setGroupedCodeList(true);
				}
			}
		}
		return result;
	}

}
