package mat.server.bonnie;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.client.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.client.bonnie.BonnieService;
import mat.client.umls.service.VsacTicketInformation;
import mat.dao.UserBonnieAccessInfoDAO;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;
import mat.model.clause.Measure;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;
import mat.server.VSACAPIServiceImpl;
import mat.server.bonnie.api.BonnieAPI;
import mat.server.bonnie.api.BonnieAPIv1;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.bonnie.api.result.BonnieMeasureResult;
import mat.server.export.ExportResult;
import mat.server.service.SimpleEMeasureService;
import mat.server.service.impl.SimpleEMeasureServiceImpl;
import mat.shared.BonnieOAuthResult;
import mat.shared.FileNameUtility;
import mat.shared.bonnie.error.BonnieAlreadyExistsException;
import mat.shared.bonnie.error.BonnieBadParameterException;
import mat.shared.bonnie.error.BonnieDoesNotExistException;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.error.UMLSNotActiveException;
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
	
	@Autowired
	private MeasureDAO measureDAO;
	
	@Autowired
	private VSACAPIServiceImpl vasacAPI;
	
	@Autowired
	private SimpleEMeasureServiceImpl simpleEMeasureService;

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
	
	private UserBonnieAccessInfo getUserBonnieAccessInfo(String userId) throws BonnieUnauthorizedException {
		UserBonnieAccessInfo userInformation = null;
		try {
			userInformation = validateOrRefreshBonnieTokensForUser(userId);
		} catch (BonnieUnauthorizedException e) {
			handleBonnieUnauthorizedException(userInformation);
			throw e;
		}
		return userInformation;
	}
	
	@Override
	public String getUpdateOrUploadMeasureToBonnie(String measureId, String userId, VsacTicketInformation vsacTicket) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieDoesNotExistException, BonnieServerException, IOException, BonnieAlreadyExistsException, UMLSNotActiveException {
		
		String successMessage = "";
		
		//see if user valid
		UserBonnieAccessInfo userInformation = getUserBonnieAccessInfo(userId);
		String userAccessToken = userInformation.getAccessToken();
		
		//Get measure and check if it is in bonnie
		Measure measure = measureDAO.find(measureId);
		String measureSetId = measure.getMeasureSet().getId();
		BonnieMeasureResult bonnieMeasureResults = null;
		try {
			bonnieMeasureResults = bonnieApi.getMeasureById(userAccessToken, measureSetId);
		} catch (BonnieUnauthorizedException e) {
			handleBonnieUnauthorizedException(userInformation);
			throw e;
		}
		
		Date exportDate = new Date();
		ExportResult export = null;
		try {
			export = simpleEMeasureService.getEMeasureZIP(measureId, exportDate);
		} catch (Exception e1) {
			throw new BonnieServerException();
		}
		
		String currentReleaseVersion = StringUtils.replace(measure.getReleaseVersion(), ".", "_");
		
		byte[] zipFileContents = export.zipbarr;
		String fileName = export.measureName + "_" + currentReleaseVersion + ".zip";
		String calculationType = measure.getPatientBased() ? "patient" : "episode";
		String vsacTicketGrantingTicket = vsacTicket.getTicket();
		String vsacTicketExpiration = String.valueOf(vsacTicket.getTimeout().getTime());
		
		
		if(bonnieMeasureResults != null && bonnieMeasureResults.getMeasureExsists()) {
			//Measure exists in Bonnie Update
			bonnieApi.updateMeasureInBonnie(userAccessToken, measureSetId, zipFileContents, fileName, null, calculationType, vsacTicketGrantingTicket, vsacTicketExpiration);
			successMessage = export.measureName +  " has successfully updated the existing measure in Bonnie. Please click open or save below to view the results.";
		} else {
			//Measure doesn't exist in Bonnie upload measure
			bonnieApi.uploadMeasureToBonnie(userAccessToken, zipFileContents, fileName, null, calculationType, vsacTicketGrantingTicket, vsacTicketExpiration);
			successMessage = export.measureName + " has been successfully uploaded as a new measure Bonnie. Please go to the Bonnie tool to create test cases for this measure.";
		}
		
		return successMessage;
		
	}
	
	public BonnieCalculatedResult getBonnieExportForMeasure(String userId, String measureId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException{
		UserBonnieAccessInfo userInformation = getUserBonnieAccessInfo(userId);
		String userAccessToken = userInformation.getAccessToken();
		BonnieCalculatedResult caluclatedResult = null;
		try {
			caluclatedResult = bonnieApi.getCalculatedResultsForMeasure(userAccessToken, measureId);
		} catch (BonnieUnauthorizedException e) {
			handleBonnieUnauthorizedException(userInformation);
			throw e;
		} catch (BonnieNotFoundException e) {
			throw e;
		} catch (BonnieServerException e) {
			throw e;
		}
		return caluclatedResult;
	}

	/*private SimpleEMeasureService getService(){
		SimpleEMeasureService service = (SimpleEMeasureService) context.getBean("eMeasureService");
		return service;
	}*/
	
	private BonnieOAuthResult refreshBonnieTokens(String userId) throws BonnieUnauthorizedException {
		BonnieOAuthResult result = null;
		User user = userDAO.find(userId);
		try {
			result = bonnieApi.getBonnieRefreshResult(user.getUserBonnieAccessInfo());
		} catch (BonnieUnauthorizedException e) {
			handleBonnieUnauthorizedException(user.getUserBonnieAccessInfo());
			throw e;
		}
		
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
		
		UserBonnieAccessInfo bonnieAccessInfo = validateOrRefreshBonnieTokensForUser(userId);
		BonnieUserInformationResult bonnieInformationResult = bonnieApi.getUserInformationByToken(bonnieAccessInfo.getAccessToken());
		return bonnieInformationResult;
	}

	private UserBonnieAccessInfo validateOrRefreshBonnieTokensForUser(String userId) throws BonnieUnauthorizedException {
		UserBonnieAccessInfo bonnieAccessInfo = userBonnieAccessInfoDAO.findByUserId(userId);
		if (bonnieAccessInfo == null) {
			// if they have no credentials in the database, then they are not authorized
			// with bonnie
			throw new BonnieUnauthorizedException();
		}
		
		refreshBonnieTokens(userId);
		return bonnieAccessInfo;
	}

	private void handleBonnieUnauthorizedException(UserBonnieAccessInfo bonnieAccessInfo) {
		// if an unauthorized exception is thrown and the user had credentials in the
		// database, delete them because they
		// are invalid, and the surface the error
		userBonnieAccessInfoDAO.delete(Integer.toString(bonnieAccessInfo.getId()));
	}

	public BonnieAPI getBonnieApi() {
		return bonnieApi;
	}

	public void setBonnieApi(BonnieAPIv1 bonnieApi) {
		this.bonnieApi = bonnieApi;
	}
}
