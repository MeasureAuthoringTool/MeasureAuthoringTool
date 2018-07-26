package mat.server.bonnie;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.client.bonnie.BonnieService;
import mat.dao.UserBonnieAccessInfoDAO;
import mat.dao.UserDAO;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;
import mat.server.bonnie.api.BonnieAPI;
import mat.server.bonnie.api.BonnieAPIv1;
import mat.shared.BonnieOAuthResult;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.result.BonnieUserInformationResult;

@SuppressWarnings("serial")
@Service
public class BonnieServiceImpl extends SpringRemoteServiceServlet implements BonnieService {

	@Autowired
	private BonnieAPIv1 bonnieApi;

	/** The user DAO. */
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserBonnieAccessInfoDAO userBonnieAccessInfoDAO;

	@Override
	public String getBonnieAccessLink() {
		String baseURL = bonnieApi.getBonnieBaseURL();
		String responseType = bonnieApi.getResponseType();
		String clientId = bonnieApi.getClientId();
		String redirectURI = bonnieApi.getRedirectURI();

		if (redirectURI == null && clientId == null && responseType == null) {
			return "";
		} else {
			return baseURL + "/oauth/authorize?response_type=" + responseType + "&client_id=" + clientId
					+ "&redirect_uri=" + redirectURI;
		}
	}

	@Override
	public BonnieOAuthResult exchangeCodeForTokens(String code) {
		System.out.println("EXCHANING!");
		BonnieOAuthResult result = null;
		try {
			result = bonnieApi.getBonnieOAuthResult(code);
			saveBonnieAccessInfo(result);

		} catch (Exception exn) {
			exn.printStackTrace();
			return result;
		}

		return result;
	}

	private BonnieOAuthResult refreshBonnieTokens(String userId) {
		BonnieOAuthResult result = null;
		User user = userDAO.find(userId);
		result = bonnieApi.getBonnieRefreshResult(user.getUserBonnieAccessInfo());
		saveBonnieAccessInfo(result);

		return result;
	}

	private void saveBonnieAccessInfo(BonnieOAuthResult result) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		UserBonnieAccessInfo userBonnieAccessInfo = user.getUserBonnieAccessInfo();
		if (userBonnieAccessInfo == null) {
			userBonnieAccessInfo = new UserBonnieAccessInfo();
			userBonnieAccessInfo.setAccessToken(result.getAccessToken());
			userBonnieAccessInfo.setRefreshToken(result.getRefreshToken());
			userBonnieAccessInfo.setUser(user);
		} else {
			userBonnieAccessInfo.setAccessToken(result.getAccessToken());
			userBonnieAccessInfo.setRefreshToken(result.getRefreshToken());
		}

		userBonnieAccessInfoDAO.save(userBonnieAccessInfo);
	}

	public BonnieUserInformationResult getBonnieUserInformationForUser(String userId)
			throws BonnieUnauthorizedException, BonnieServerException, IOException {
		refreshBonnieTokens(userId);
		UserBonnieAccessInfo bonnieAccessInfo = userBonnieAccessInfoDAO.findByUserId(userId);
		if (bonnieAccessInfo == null) {
			// if they have no credentials in the database, then they are not authorized
			// with bonnie
			throw new BonnieUnauthorizedException();
		}

		try {
			BonnieUserInformationResult bonnieInformationResult = bonnieApi
					.getUserInformationByToken(bonnieAccessInfo.getAccessToken());
			return bonnieInformationResult;
		} catch (BonnieUnauthorizedException e) {
			// if an unauthorized exception is thrown and the user had credentials in the
			// database, delete them because they
			// are invalid, and the surface the error
			userBonnieAccessInfoDAO.delete(Integer.toString(bonnieAccessInfo.getId()));
			throw e;
		}
	}

	public BonnieAPI getBonnieApi() {
		return bonnieApi;
	}

	public void setBonnieApi(BonnieAPIv1 bonnieApi) {
		this.bonnieApi = bonnieApi;
	}
}
