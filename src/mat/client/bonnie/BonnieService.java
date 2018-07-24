package mat.client.bonnie;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.shared.BonnieOAuthResultAgain;

@RemoteServiceRelativePath("bonnieService")
public interface BonnieService extends RemoteService {
	String getBonnieAccessLink();

	BonnieOAuthResultAgain exchangeCodeForTokens(String code);
}
