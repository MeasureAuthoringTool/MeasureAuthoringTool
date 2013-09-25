/**
 * mat.server package.
 * **/
package mat.server;

import java.io.IOException;
import java.io.StringReader;

import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VsacApiResult;
import mat.model.MatValueSet;
import mat.model.VSACValueSetWrapper;
import mat.server.util.ResourceLoader;
import mat.server.util.UMLSSessionTicket;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.telligen.vsac.dao.ValueSetsResponseDAO;
import org.telligen.vsac.object.ValueSetsResponse;
import org.telligen.vsac.service.VSACTicketService;
import org.xml.sax.InputSource;



/** VSACAPIServiceImpl class.**/
public class VSACAPIServiceImpl extends SpringRemoteServiceServlet
implements VSACAPIService {
	/**serialVersionUID  for VSACAPIServiceImpl class.**/
	private static final long serialVersionUID = -6645961609626183169L;
	/**Logger for VSACAPIServiceImpl class.**/
	private static final Log LOGGER = LogFactory.getLog(
			VSACAPIServiceImpl.class);

	@Override
	public final boolean validateVsacUser(final String
			userName, final String password) {
		LOGGER.info("Start validateVsacUser =====");
		String eightHourTicketForUser = new VSACTicketService().getTicketGrantingTicket(userName,password);
		LOGGER.info("End validateVsacUser ====="+ " eightHourTicketForUser ====="+ eightHourTicketForUser);
		UMLSSessionTicket.getUmlssessionmap().put(getThreadLocalRequest().getSession().getId(), eightHourTicketForUser);
		return eightHourTicketForUser != null;
	}


	@Override
	public final void inValidateVsacUser() {
		LOGGER.info("Start inValidateVsacUser =====");
		if (UMLSSessionTicket.getUmlssessionmap().size() > 0) {
			UMLSSessionTicket.getUmlssessionmap().remove(getThreadLocalRequest().getSession().getId());
		}
		LOGGER.info("End inValidateVsacUser =====");

	}

	@SuppressWarnings("static-access")
	@Override
	public final VsacApiResult getValueSetBasedOIDAndVersion(final String oid, final String version) {
		VsacApiResult result = new VsacApiResult();
		if (UMLSSessionTicket.getUmlssessionmap().size() > 0) {
			String eightHourTicket = UMLSSessionTicket.getUmlssessionmap().get(getThreadLocalRequest().getSession().getId());
			if (eightHourTicket != null) {
				if (oid != null && StringUtils.isEmpty(oid)) {
					ValueSetsResponseDAO dao = new ValueSetsResponseDAO(eightHourTicket);
					ValueSetsResponse vsr = dao.getMultipleValueSetsResponseByOID(oid);
					result.setSuccess(true);
					VSACValueSetWrapper wrapper = convertXmltoValueSet(vsr.getXmlPayLoad());
					for(MatValueSet valueSet : wrapper.getValueSetList()){
						if(valueSet.getType().equalsIgnoreCase("grouping")){
							String definitation = valueSet.getDefinition();
							if(definitation != null && StringUtils.isNotBlank(definitation)){
								definitation = definitation.replace("(", "");
								definitation = definitation.replace(")", "");
								String[] newDefinitation = definitation.split(",");
								for(int i=0;i<newDefinitation.length;i++){
									String[] groupedValueSetOid = newDefinitation[i].split(":");
									if(groupedValueSetOid.length ==2) {
										ValueSetsResponseDAO daoGroupped = new ValueSetsResponseDAO(eightHourTicket);
										ValueSetsResponse vsrGrouped = daoGroupped.getMultipleValueSetsResponseByOID(groupedValueSetOid[0].trim());
										VSACValueSetWrapper wrapperGrouped = convertXmltoValueSet(vsrGrouped.getXmlPayLoad());
										valueSet.setGrouppedValueSet(wrapperGrouped.getValueSetList());
									}
								}
							}
						}
					}
					result.setVsacResponse(wrapper.getValueSetList());

				} else {
					result.setSuccess(false);
					result.setFailureReason(result.OID_REQUIRED);
				}
			} else {
				result.setSuccess(false);
				result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
		}
		return result;
	}
	
	/**
	 * Private method to Covert VSAC xml payload into Java object through Castor.
	 * 
	 * */
	private VSACValueSetWrapper convertXmltoValueSet(final String xmlPayLoad){
		LOGGER.info("In VSACAPIServiceImpl convertXmltoValueSet");
		VSACValueSetWrapper details = null;
		String xml = xmlPayLoad;
		if(xml != null && StringUtils.isNotBlank(xml)){
			LOGGER.info("xml To reterive RetrieveMultipleValueSetsResponse tag is not null ");
		}
		try {
			if(xml == null){
				LOGGER.info("xml is null or xml doesn't contain RetrieveMultipleValueSetsResponse tag");
			}else{
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("MultiValueSetMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(VSACValueSetWrapper.class);
				unmar.setWhitespacePreserve(true);
				LOGGER.info("unmarshalling xml..RetrieveMultipleValueSetsResponse ");
	            details = (VSACValueSetWrapper)unmar.unmarshal(new InputSource(new StringReader(xml)));
	            LOGGER.info("unmarshalling complete..RetrieveMultipleValueSetsResponse" + details.getValueSetList().get(0).getDefinition());
	       }
		
		} catch (Exception e) {
			if(e instanceof IOException){
				LOGGER.info("Failed to load MultiValueSetMapping.xml" + e);
			}else if(e instanceof MappingException){
				LOGGER.info("Mapping Failed" + e);
			}else if(e instanceof MarshalException){
				LOGGER.info("Unmarshalling Failed" + e);
			}else{
				LOGGER.info("Other Exception" + e);
			}
		} 
		return details;
	}
}
