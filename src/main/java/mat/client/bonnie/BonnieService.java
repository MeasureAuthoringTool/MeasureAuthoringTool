package mat.client.bonnie;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.umls.service.VsacTicketInformation;
import mat.shared.BonnieOAuthResult;
import mat.shared.bonnie.error.BonnieAlreadyExistsException;
import mat.shared.bonnie.error.BonnieBadParameterException;
import mat.shared.bonnie.error.BonnieDoesNotExistException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.error.UMLSNotActiveException;
import mat.shared.bonnie.result.BonnieUserInformationResult;

import java.io.IOException;

@RemoteServiceRelativePath("bonnieService")
public interface BonnieService extends RemoteService {
	String getBonnieAccessLink();

	BonnieOAuthResult exchangeCodeForTokens(String code);

	BonnieUserInformationResult getBonnieUserInformationForUser(String userId) throws BonnieUnauthorizedException, BonnieServerException, Exception;

	Boolean updateOrUploadMeasureToBonnie(String measureId, String userId, VsacTicketInformation vsacTicket) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieDoesNotExistException, BonnieServerException, IOException, BonnieAlreadyExistsException, UMLSNotActiveException;
	
	public void revokeBonnieAccessTokenForUser(String userId) throws BonnieServerException, Exception;
	
	public void revokeAllBonnieAccessTokens(String userId, String reason) throws BonnieServerException, Exception;
}
