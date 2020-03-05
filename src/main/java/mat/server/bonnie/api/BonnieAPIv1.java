package mat.server.bonnie.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import mat.model.UserBonnieAccessInfo;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.bonnie.api.result.BonnieMeasureResult;
import mat.server.service.EncryptDecryptToken;
import mat.server.util.APIConnectionUtillity;
import mat.shared.BonnieOAuthResult;
import mat.shared.FileInfomationObject;
import mat.shared.bonnie.error.BonnieAlreadyExistsException;
import mat.shared.bonnie.error.BonnieBadParameterException;
import mat.shared.bonnie.error.BonnieDoesNotExistException;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.result.BonnieUserInformationResult;

@Configurable
@Service
public class BonnieAPIv1 implements BonnieAPI {

	private static final Log logger = LogFactory.getLog(BonnieAPIv1.class);

	private static final String UPDATE_MEASURE_URI = "/api_v1/measures";

	private static final String CALCULATE_MEASURE_RESULTS_URI = "/calculated_results";

	private static final String GET_USER_INFORMATION_URI = "/oauth/token/info";

	private static final String REVOKE_BONNIE_TOKEN_URI = "/oauth/revoke";

	private static final String BOUNDRY = "APIPIE_RECORDER_EXAMPLE_BOUNDARY";

	@Autowired
	private EncryptDecryptToken encryptDecryptToken;
	@Autowired
	private APIConnectionUtillity apiConnectionUtillity;

	public String getResponseType() {
		return System.getProperty("BONNIE_RESPONSE_TYPE");
	}

	public String getClientId() {
		return System.getProperty("BONNIE_CLIENT_ID");
	}

	public String getRedirectURI() {
		return System.getProperty("BONNIE_REDIRECT_URI");
	}

	public String getClientSecret() {
		return System.getProperty("BONNIE_CLIENT_SECRET");
	}

	public String getBonnieBaseURL() {
		return System.getProperty("BONNIE_URI");
	}
	public String getProxyPort() {
		return System.getProperty("https.proxyPort");
	}
	public String getProxyHost() {
		return System.getProperty("https.proxyHost");
	}

	public BonnieAPIv1() {
	}

	@Override
	public List<BonnieMeasureResult> getMeasuresForUser(String bearerToken) throws BonnieUnauthorizedException {
		return null;
	}

	@Override
	public BonnieMeasureResult getMeasureById(String bearerToken, String hqmfSetId)
			throws BonnieUnauthorizedException, IOException {
		BonnieMeasureResult measureResult = new BonnieMeasureResult();
		String uri = "/api_v1/measures/" + hqmfSetId.toUpperCase();

		HttpURLConnection connection = null;
		try {
			connection = getInformationConnection(encryptDecryptToken.decryptKey(bearerToken), uri);
			logger.info("GET " + connection.getURL());

			String code = Integer.toString(connection.getResponseCode());
			if (code.startsWith("2")) {
				measureResult.setMeasureExsists(true);
			} else if (code.contains("401")) {
				// if the server throws a 401, we should return unauthorized exception
				// since the measure id was not valid
				String response = getResponse(connection.getErrorStream());
				logger.error(response);
				throw new BonnieUnauthorizedException();
			} else if (code.contains("404")) {
				// if the server throws a 404, we should return bonnieNotFound exception
				// since the measure id was not valid
				String response = getResponse(connection.getErrorStream());
				logger.error(response);
				measureResult.setMeasureExsists(false);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		} finally {
			if (connection != null) {
				logger.info("Disconnecting " + connection.getURL());
				connection.disconnect();
			}
		}

		return measureResult;
	}

	@Override
	public BonnieCalculatedResult getCalculatedResultsForMeasure(String bearerToken, String hqmfSetId)
			throws BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, IOException, BonnieBadParameterException, BonnieDoesNotExistException {
		BonnieCalculatedResult calculatedResult = new BonnieCalculatedResult();
		String uri = UPDATE_MEASURE_URI + "/" + hqmfSetId.toUpperCase() + CALCULATE_MEASURE_RESULTS_URI;

		HttpURLConnection connection = null;
		try {
			connection = getCalculationInformationConnection(encryptDecryptToken.decryptKey(bearerToken), uri);
			logger.info("GET " + connection.getURL());
			String code = Integer.toString(connection.getResponseCode());
			handleResponseCode(code, "Get Calculations For Measure " + hqmfSetId, hqmfSetId);

			ByteArrayOutputStream calculatedResultByteArray = readFully(connection.getInputStream());
			calculatedResult.setResult(calculatedResultByteArray.toByteArray());
			calculatedResult.setName(getFileNameFromContentDisposition(connection.getHeaderField("Content-Disposition")));
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null) {
				logger.info("Disconnecting " + connection.getURL());
				connection.disconnect();
			}
		}

		return calculatedResult;
	}

	private String getFileNameFromContentDisposition(String contentDispositionValue) {

		contentDispositionValue = StringUtils.remove(contentDispositionValue, "attachment; filename=");
		contentDispositionValue = StringUtils.remove(contentDispositionValue, "\"");

		return contentDispositionValue;

	}

	private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }

	@Override
	public void uploadMeasureToBonnie(String bearerToken, byte[] zipFileContents, String fileName,
			String measureType, String calculationType, String vsacTicketGrantingTicket, String vsacTicketExpiration)
			throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieAlreadyExistsException,
			BonnieServerException, IOException, BonnieDoesNotExistException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse postResponse = null;
		try {
			httpClient = HttpClients.createDefault();
			FileInfomationObject fileObject = new FileInfomationObject(zipFileContents, ContentType.create("application/zip"), fileName);
			HttpPost postRequest = createHttpPostRequest(UPDATE_MEASURE_URI, encryptDecryptToken.decryptKey(bearerToken), calculationType, vsacTicketGrantingTicket, vsacTicketExpiration, fileObject);
			setRequestConfigProxy(postRequest);
			postResponse = httpClient.execute(postRequest);


			String code = String.valueOf(postResponse.getStatusLine().getStatusCode());
			handleResponseCode(code, "Mesure Upload", null);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		} finally {
			if (httpClient != null) {
				logger.info("Disconnecting post /api_v1/measures httpClient");
				httpClient.close();
			}
			if (postResponse != null) {
				logger.info("Disconnecting post /api_v1/measures postResponse");
				postResponse.close();
			}
		}
	}

	@Override
	public void updateMeasureInBonnie(String bearerToken, String hqmfSetId, byte[] zipFileContents,
			String fileName, String measureType, String calculationType, String vsacTicketGrantingTicket,
			String vsacTicketExpiration) throws BonnieUnauthorizedException, BonnieBadParameterException,
			BonnieDoesNotExistException, BonnieServerException, IOException {
		CloseableHttpResponse putResponse = null;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClients.createDefault();
			FileInfomationObject fileObject = new FileInfomationObject(zipFileContents, ContentType.create("application/zip"), fileName);
			HttpPut putRequest = createHttpPutRequest(UPDATE_MEASURE_URI + "/" + hqmfSetId.toUpperCase(), encryptDecryptToken.decryptKey(bearerToken), calculationType, vsacTicketGrantingTicket, vsacTicketExpiration, fileObject);
			setRequestConfigProxy(putRequest);
			putResponse = httpClient.execute(putRequest);

			String code = Integer.toString(putResponse.getStatusLine().getStatusCode());
			handleResponseCode(code, "Mesure Update", hqmfSetId);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		} finally {
			if (httpClient != null) {
				logger.error("Disconecting httpClient /api_v1/measures/" + hqmfSetId);
				httpClient.close();
			}
			if (putResponse != null) {
				putResponse.close();
				logger.error("Disconecting putResponse /api_v1/measures/" + hqmfSetId);
			}
		}
	}

	private void setRequestConfigProxy(HttpEntityEnclosingRequestBase request) {
		if(!StringUtils.isEmpty(getProxyHost()) && !StringUtils.isEmpty(getProxyPort())) {
			HttpHost proxy = new HttpHost(getProxyHost(), Integer.valueOf(getProxyPort()));
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			request.setConfig(config);
		}
	}

	private HttpPut createHttpPutRequest(String uri, String token, String calculationType, String vsacTicketGrantingTicket, String vsacTicketExpiration, FileInfomationObject fileInfomation) {
		String requestUri = getBonnieBaseURL() + uri;

		Map<String, String> headerMap = createHeader(token);
		Map<String, String> textInputMap = createTextMap(calculationType, vsacTicketGrantingTicket, vsacTicketExpiration);

		Map<String, FileInfomationObject> binaryInputMap = createFileMap(fileInfomation);


		return apiConnectionUtillity.createPutConnection(requestUri, BOUNDRY, headerMap, textInputMap, binaryInputMap);
	}

	private HttpPost createHttpPostRequest(String uri, String token, String calculationType, String vsacTicketGrantingTicket, String vsacTicketExpiration, FileInfomationObject fileInfomation) {
		String requestUri = getBonnieBaseURL() + uri;

		Map<String, String> headerMap = createHeader(token);
		Map<String, String> textInputMap = createTextMap(calculationType, vsacTicketGrantingTicket, vsacTicketExpiration);

		Map<String, FileInfomationObject> binaryInputMap = createFileMap(fileInfomation);


		return apiConnectionUtillity.createPostConnection(requestUri, BOUNDRY, headerMap, textInputMap, binaryInputMap);
	}

	private Map<String, String> createHeader(String token){
		String bearerTokenString = "Bearer " + token;
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", bearerTokenString);
		headerMap.put("boundary", BOUNDRY);
		headerMap.put("'Content-Type'", "multipart/form-data");
		headerMap.put("'Content-Transfer'", "binary");
		return headerMap;
	}

	private Map<String, String> createTextMap(String calculationType, String vsacTicketGrantingTicket, String vsacTicketExpiration){
		Map<String, String> textInputMap = new HashMap<>();
		textInputMap.put("calculation_type", calculationType);
		textInputMap.put("vsac_tgt", vsacTicketGrantingTicket);
		textInputMap.put("vsac_tgt_expires_at", vsacTicketExpiration);
		return textInputMap;
	}

	private Map<String, FileInfomationObject> createFileMap(FileInfomationObject fileInfomation){
		Map<String, FileInfomationObject> binaryInputMap = new HashMap<>();
		binaryInputMap.put("measure_file", fileInfomation);
		return binaryInputMap;
	}

	@Override
	public BonnieUserInformationResult getUserInformationByToken(String token)
			throws BonnieUnauthorizedException, BonnieServerException, IOException, BonnieBadParameterException, BonnieDoesNotExistException {
		BonnieUserInformationResult userInformationResult = new BonnieUserInformationResult();
		HttpURLConnection connection = null;
		try {
			connection = getInformationConnection(encryptDecryptToken.decryptKey(token), GET_USER_INFORMATION_URI);

			logger.info("GET " + connection.getURL());

			String code = Integer.toString(connection.getResponseCode());
			handleResponseCode(code, "Get User Information", null);

			String response = getResponse(connection.getInputStream());
			JSONObject jsonObject = new JSONObject(response);
			String email = jsonObject.getString("user_email");
			userInformationResult.setBonnieUsername(email);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		} finally {
			if (connection != null) {
				logger.info("Disconnecting " + connection.getURL());
				connection.disconnect();
			}
		}

		return userInformationResult;
	}

	@Override
	public void revokeBonnieToken(String bearerToken, String refreshToken)
			throws BonnieServerException, BonnieUnauthorizedException, Exception {
		URLConnectionClient urlConnection = new URLConnectionClient();
		try {
			OAuthClient client = new OAuthClient(urlConnection);
			logger.info("Connecting to refresh bonnie oauth");
			String authString = getClientId() + ":" + getClientSecret();
			String authStringEnc = DatatypeConverter.printBase64Binary(authString.getBytes());

			OAuthClientRequest request = OAuthClientRequest.tokenLocation(getBonnieBaseURL() + REVOKE_BONNIE_TOKEN_URI)
					.setClientId(getClientId()).setClientSecret(getClientSecret())
					.setRedirectURI(getRedirectURI())
					.setParameter("token", encryptDecryptToken.decryptKey(bearerToken))
					.setParameter("token_type_hint", "access_token")
					.buildBodyMessage();
			request.setHeader("Authorization", "Basic " + authStringEnc);
			OAuthResourceResponse resp = client.resource(request, "POST", OAuthResourceResponse.class);
	    	handleResponseCode(String.valueOf(resp.getResponseCode()), "Revoke Bonnie Token", null);

		} finally {
			if(urlConnection != null) {
				urlConnection.shutdown();
				logger.info("Disconnected from refresh bonnie oauth");
			}
		}

	}

	private String getResponse(InputStream stream) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(stream));) {
			String inputLine;
			StringBuilder builder = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				builder.append(inputLine);
			}

			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private HttpURLConnection getInformationConnection(String token, String uri) throws IOException {
		String RequestUrl = getBonnieBaseURL() + uri;
		Map<String, String> requestProperty = new HashMap<String, String>();
		getBearerToken(requestProperty, token);

		return apiConnectionUtillity.createGETHTTPConnection(RequestUrl, requestProperty);
	}

	private HttpPost getRevokeInromationConnection(String token, String uri, String refreshToken) throws IOException {
		String requestUri = getBonnieBaseURL() + uri;
		String bearerTokenString = "Bearer " + token;

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", bearerTokenString);
		headerMap.put("boundary", BOUNDRY);
		headerMap.put("Host", "bonnie.healthit.gov");
		headerMap.put("'Content-Type'", "application/x-www-form-urlencoded");

		Map<String, String> textInputMap = new HashMap<>();
		textInputMap.put("client_id", getClientId());
		textInputMap.put("client_secret", getClientSecret());
		textInputMap.put("token", refreshToken);

		Map<String, FileInfomationObject> binaryInputMap = new HashMap<>();

		return apiConnectionUtillity.createPostConnection(requestUri, BOUNDRY, headerMap, textInputMap, binaryInputMap);
	}

	private HttpURLConnection getCalculationInformationConnection(String token, String uri) throws IOException {
		String RequestUrl = getBonnieBaseURL() + uri;

		Map<String, String> requestProperty = new HashMap<String, String>();
		getBearerToken(requestProperty, token);
		requestProperty.put("Accept", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		return apiConnectionUtillity.createGETHTTPConnection(RequestUrl, requestProperty);
	}

	private void getBearerToken(Map<String, String> connectionMap, String token) {
		String bearerToken =  "Bearer " + token;
		connectionMap.put("Authorization", bearerToken);
	}

	public BonnieOAuthResult getBonnieRefreshResult(UserBonnieAccessInfo userBonnieAccessInfo) throws BonnieUnauthorizedException {
		URLConnectionClient urlConnection = new URLConnectionClient();
		try {
			OAuthClient client = new OAuthClient(urlConnection);
			logger.info("Connecting to refresh bonnie oauth");
			OAuthClientRequest request = OAuthClientRequest.tokenLocation(getBonnieBaseURL() + "/oauth/token")
					.setClientId(getClientId()).setGrantType(GrantType.REFRESH_TOKEN)
					.setClientSecret(getClientSecret()).setRefreshToken(encryptDecryptToken.decryptKey(userBonnieAccessInfo.getRefreshToken()))
					.setRedirectURI(getRedirectURI()).buildQueryMessage();

			OAuthJSONAccessTokenResponse token = client.accessToken(request, OAuthJSONAccessTokenResponse.class);
			logger.info("Received Bonnie refresh tokens");
			BonnieOAuthResult result = new BonnieOAuthResult(token.getAccessToken(), token.getRefreshToken(),
					token.getExpiresIn(), token.getBody());
			return result;
		} catch (OAuthProblemException e) {
			e.printStackTrace();
			throw new BonnieUnauthorizedException();
		}
		catch (Exception exn) {
			exn.printStackTrace();
			return null;
		} finally {
			if(urlConnection != null) {
				urlConnection.shutdown();
				logger.info("Disconnected from refresh bonnie oauth");
			}
		}
	}

	public BonnieOAuthResult getBonnieOAuthResult(String code) {
		URLConnectionClient urlConnection = new URLConnectionClient();
		try {
			OAuthClient client = new OAuthClient(urlConnection);
			logger.info("Connecting to bonnie oauth");
			OAuthClientRequest request = OAuthClientRequest
					.tokenLocation(getBonnieBaseURL() + "/oauth/token").setClientId(getClientId())
					.setGrantType(GrantType.AUTHORIZATION_CODE).setClientSecret(getClientSecret()).setCode(code)
					.setRedirectURI(getRedirectURI()).buildQueryMessage();

			OAuthJSONAccessTokenResponse token = client.accessToken(request, OAuthJSONAccessTokenResponse.class);
			logger.info("Received Bonnie tokens");
			BonnieOAuthResult result = new BonnieOAuthResult(token.getAccessToken(), token.getRefreshToken(),
					token.getExpiresIn(), token.getBody());
			return result;
		} catch (Exception exn) {
			logger.info("Error occurred while bonnie Auth results");
			logger.error(exn.getMessage());
			return null;
		} finally {
			if(urlConnection != null) {
				urlConnection.shutdown();
				logger.info("Disconnected from bonnie oauth");
			}
		}
	}

	private void handleResponseCode(String code, String method, String hqmfSetId) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieDoesNotExistException, BonnieServerException{
		if(code.startsWith("2")) {
			logger.info(method + " successful");
		} else if (code.contains("401")) {
			//401 Unauthorized
			logger.error("401: user unauthorized - " + method);
			throw new BonnieUnauthorizedException();
		} else if (code.contains("400")) {
			//400 we sent bad parameters
			logger.error("400: MAT sent bad parameters - " + method);
			throw new BonnieBadParameterException();
		} else if (code.contains("404")) {
			//404 Measure does not exist to update
			logger.error("404: Measure Set ID " + hqmfSetId + " does not exsist in Bonnie database - " + method);
			throw new BonnieDoesNotExistException();
		} else if (code.startsWith("5")) {
			//500 server error occurred
			logger.error("500: Error interfacing with Bonnie - " + method);
			throw new BonnieServerException();
		} else {
			logger.error("Generic Error - " + method);
			throw new BonnieServerException();
		}
	}

}
