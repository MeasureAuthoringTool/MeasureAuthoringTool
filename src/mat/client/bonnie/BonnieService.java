package mat.client.bonnie;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

<<<<<<< HEAD
import mat.shared.BonnieOAuthResult;

@RemoteServiceRelativePath("bonnieService")
public interface BonnieService extends RemoteService {
	String getBonnieAccessLink();
	BonnieOAuthResult exchangeCodeForTokens(String code);
=======
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.result.BonnieUserInformationResult;

@RemoteServiceRelativePath("bonnieService")
public interface BonnieService extends RemoteService {
	
	String getBonnieLink();
	
	BonnieUserInformationResult getBonnieUserInformationForUser(String userId) throws BonnieUnauthorizedException;
>>>>>>> MAT-9083 cleaned up UI Code and stubbed out Bonnie Service calls
}
