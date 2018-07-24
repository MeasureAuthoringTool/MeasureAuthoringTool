package mat.client.bonnie;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.shared.BonnieOauthResult;

@RemoteServiceRelativePath("bonnieService")
public interface BonnieService extends RemoteService {
	String getBonnieAccessLink();

	BonnieOauthResult authenticateBonnieUser(String code);
}
