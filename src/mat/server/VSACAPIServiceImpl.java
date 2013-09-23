package mat.server;

import java.util.ArrayList;
import java.util.List;

import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VsacApiResult;
import mat.model.MatConcept;
import mat.model.MatConceptList;
import mat.model.MatValueSet;
import mat.server.util.UMLSSessionTicket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.telligen.vsac.dao.ValueSetsResponseDAO;
import org.telligen.vsac.object.Concept;
import org.telligen.vsac.object.ConceptList;
import org.telligen.vsac.object.ValueSetsResponse;
import org.telligen.vsac.service.VSACTicketService;




public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements VSACAPIService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645961609626183169L;
	private static final Log logger = LogFactory.getLog(VSACAPIServiceImpl.class);

	@Override
	public boolean validateVsacUser(String userName, String password) {
		logger.info("Start validateVsacUser =====");
		String eightHourTicketForUser = new VSACTicketService().getTicketGrantingTicket(userName,password);
		logger.info("End validateVsacUser ===== eightHourTicketForUser =====" + eightHourTicketForUser);
		UMLSSessionTicket.getUmlssessionmap().put(getThreadLocalRequest().getSession().getId(), eightHourTicketForUser);
		if(eightHourTicketForUser!=null)
			return true;
		else
			return false;
	}
	
	
	@Override
	public void inValidateVsacUser() {
		logger.info("Start inValidateVsacUser =====");
		if(UMLSSessionTicket.getUmlssessionmap().size()>0){
			UMLSSessionTicket.getUmlssessionmap().remove(getThreadLocalRequest().getSession().getId());
		}
		logger.info("End inValidateVsacUser =====");
		
	}
	
	@SuppressWarnings("static-access")
	@Override
	public VsacApiResult getValueSetBasedOIDAndVersion(String OID, String version){
		VsacApiResult result = new VsacApiResult();
		if(UMLSSessionTicket.getUmlssessionmap().size()>0){
			String eightHourTicket = UMLSSessionTicket.getUmlssessionmap().get(getThreadLocalRequest().getSession().getId());
			if(eightHourTicket!=null){
				if(OID!=null){
					ValueSetsResponseDAO dao = new ValueSetsResponseDAO(eightHourTicket);
					ValueSetsResponse vsr = dao.getMultipleValueSetsResponseByOID(OID);
					result.setSuccess(true);
					MatValueSet valueset = new MatValueSet();
					org.telligen.vsac.object.ValueSet vsrValueSet = vsr.getValueSetList().get(0);
					
					valueset.setRevisionDate(vsrValueSet.getRevisionDate());
					valueset.setDisplayName(vsrValueSet.getDisplayName());
					valueset.setBinding(vsrValueSet.getBinding());
					valueset.setType(vsrValueSet.getType());
					valueset.setID(vsrValueSet.getID());
					valueset.setSource(vsrValueSet.getSource());
					valueset.setStatus(vsrValueSet.getStatus());
					
					MatConceptList conceptList = new MatConceptList();
					ConceptList vsrConceptList = vsrValueSet.getConceptList();
					List<MatConcept> conceptsList = new ArrayList<MatConcept>(vsrConceptList.getConceptList().size()); 
					for(Concept concept:vsrConceptList.getConceptList() ){
						MatConcept matConcept = new MatConcept();
						matConcept.setCode(concept.getCode());
						matConcept.setCodeSystem(concept.getCodeSystem());
						matConcept.setCodeSystemName(concept.getCodeSystemName());
						matConcept.setCodeSystemVersion(concept.getCodeSystemVersion());
						conceptsList.add(matConcept);
					}
					conceptList.setConceptList(conceptsList);
					valueset.setConceptList(conceptList);
					result.setVsacResponse(valueset);
					
				}else{
					result.setSuccess(false);
					result.setFailureReason(result.OID_REQUIRED);
				}
			}else{
				result.setSuccess(false);
				result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			}
		}else{
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
		}
		return result;
	}
}
