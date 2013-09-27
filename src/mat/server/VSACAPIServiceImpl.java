/**
 * mat.server package.
 * **/
package mat.server;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

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

/** VSACAPIServiceImpl class. **/
public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements
		VSACAPIService {
	/** serialVersionUID for VSACAPIServiceImpl class. **/
	private static final long serialVersionUID = -6645961609626183169L;
	/** Logger for VSACAPIServiceImpl class. **/
	private static final Log LOGGER = LogFactory
			.getLog(VSACAPIServiceImpl.class);

	@Override
	public final boolean validateVsacUser(final String userName,
			final String password) {
		LOGGER.info("Start VSACAPIServiceImpl validateVsacUser");
		String eightHourTicketForUser = new VSACTicketService()
				.getTicketGrantingTicket(userName, password);
		UMLSSessionTicket.put(getThreadLocalRequest().getSession().getId(),
				eightHourTicketForUser);
		LOGGER.info("End VSACAPIServiceImpl validateVsacUser: "
				+ " Ticket issued for 8 hours: " + eightHourTicketForUser);
		return eightHourTicketForUser != null;
	}

	@Override
	public final boolean isAlreadySignedIn() {
		LOGGER.info("Start VSACAPIServiceImpl isAlreadySignedIn");
		String eightHourTicketForUser = UMLSSessionTicket
				.getTicket(getThreadLocalRequest().getSession().getId());
		LOGGER.info("End VSACAPIServiceImpl isAlreadySignedIn: ");
		return eightHourTicketForUser != null;
	}

	@Override
	public final void inValidateVsacUser() {
		LOGGER.info("Start VSACAPIServiceImpl inValidateVsacUser");
		UMLSSessionTicket.remove(getThreadLocalRequest().getSession().getId());

		LOGGER.info("End VSACAPIServiceImpl inValidateVsacUser");

	}

	@SuppressWarnings("static-access")
	@Override
	public final VsacApiResult getValueSetByOIDAndVersion(final String oid) {
		LOGGER.info("Start VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :"
				+ oid);
		VsacApiResult result = new VsacApiResult();

		String eightHourTicket = UMLSSessionTicket
				.getTicket(getThreadLocalRequest().getSession().getId());
		if (eightHourTicket != null) {
			if (oid != null && StringUtils.isNotEmpty(oid)
					&& StringUtils.isNotBlank(oid)) {
				ValueSetsResponseDAO dao = new ValueSetsResponseDAO(
						eightHourTicket);
				ValueSetsResponse vsr = dao
						.getMultipleValueSetsResponseByOID(oid.trim());
				result.setSuccess(true);
				if (!vsr.getXmlPayLoad().isEmpty()) {
					VSACValueSetWrapper wrapper = convertXmltoValueSet(vsr
							.getXmlPayLoad());
					for (MatValueSet valueSet : wrapper.getValueSetList()) {
						handleVSACGroupedValueSet(eightHourTicket, valueSet);
					}
					result.setVsacResponse(wrapper.getValueSetList());
					LOGGER.info("Successfully converted valueset object from vsac xml payload.");
				}

			} else {
				result.setSuccess(false);
				result.setFailureReason(result.OID_REQUIRED);
				LOGGER.info("OID is required");
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(result.UMLS_NOT_LOGGEDIN);
			LOGGER.info("UMLS Login is required");
		}

		LOGGER.info("End VSACAPIServiceImpl getValueSetBasedOIDAndVersion method : oid entered :"
				+ oid);
		return result;
	}

	/**
	 * @param eightHourTicket
	 * @param valueSet
	 */
	private void handleVSACGroupedValueSet(String eightHourTicket,
			MatValueSet valueSet) {

		if (!valueSet.isGrouping()) {
			return;
		}

		valueSet.setGroupedValueSet(new ArrayList<MatValueSet>());
		String definitation = valueSet.getDefinition();
		if (definitation != null && StringUtils.isNotBlank(definitation)) {
			// Definition format is
			// (oid:SourceName),(oid:sourceName).
			// Below code is removing '(' ')' and splitting
			// on ':' to find oid's.
			definitation = definitation.replace("(", "");
			definitation = definitation.replace(")", "");
			String[] newDefinitation = definitation.split(",");
			for (int i = 0; i < newDefinitation.length; i++) {
				String[] groupedValueSetOid = newDefinitation[i].split(":");
				if (groupedValueSetOid.length == 2) { // To
														// avoid
														// junk
														// data
					ValueSetsResponseDAO daoGroupped = new ValueSetsResponseDAO(
							eightHourTicket);
					ValueSetsResponse vsrGrouped = daoGroupped
							.getMultipleValueSetsResponseByOID(groupedValueSetOid[0]
									.trim());
					VSACValueSetWrapper wrapperGrouped = convertXmltoValueSet(vsrGrouped
							.getXmlPayLoad());
					valueSet.getGroupedValueSet().add(
							wrapperGrouped.getValueSetList().get(0));
				}
			}
		}

	}

	/**
	 * Private method to Covert VSAC xml payload into Java object through
	 * Castor.
	 * 
	 * @param xmlPayLoad
	 *            - String vsac payload.
	 * @return VSACValueSetWrapper.
	 * 
	 * */
	private VSACValueSetWrapper convertXmltoValueSet(final String xmlPayLoad) {
		LOGGER.info("Start VSACAPIServiceImpl convertXmltoValueSet");
		VSACValueSetWrapper details = null;
		String xml = xmlPayLoad;
		if (xml != null && StringUtils.isNotBlank(xml)) {
			LOGGER.info("xml To reterive RetrieveMultipleValueSetsResponse tag is not null ");
		}
		try {
			if (xml == null) {
				LOGGER.info("xml is null or xml doesn't contain RetrieveMultipleValueSetsResponse tag");
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader()
						.getResourceAsURL("MultiValueSetMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(VSACValueSetWrapper.class);
				unmar.setWhitespacePreserve(true);
				LOGGER.info("unmarshalling xml..RetrieveMultipleValueSetsResponse ");
				details = (VSACValueSetWrapper) unmar
						.unmarshal(new InputSource(new StringReader(xml)));
				LOGGER.info("unmarshalling complete..RetrieveMultipleValueSetsResponse"
						+ details.getValueSetList().get(0).getDefinition());
			}

		} catch (Exception e) {
			if (e instanceof IOException) {
				LOGGER.info("Failed to load MultiValueSetMapping.xml" + e);
			} else if (e instanceof MappingException) {
				LOGGER.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				LOGGER.info("Unmarshalling Failed" + e);
			} else {
				LOGGER.info("Other Exception" + e);
			}
		}
		LOGGER.info("End VSACAPIServiceImpl convertXmltoValueSet");
		return details;
	}
}
