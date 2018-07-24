package mat.client.bonnie;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.shared.BonnieOAuthResult;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.result.BonnieUserInformationResult;

@RemoteServiceRelativePath("bonnieService")
public interface BonnieService extends RemoteService {
	String getBonnieAccessLink();

	BonnieOAuthResult exchangeCodeForTokens(String code);

	BonnieUserInformationResult getBonnieUserInformationForUser(String userId) throws BonnieUnauthorizedException, BonnieServerException, Exception;
}
