package mat.server.bonnie.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import mat.model.UserBonnieAccessInfo;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.bonnie.api.result.BonnieMeasureResult;
import mat.shared.BonnieOAuthResult;
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
			connection = getInformationConnection(bearerToken, uri);
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
			throws BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, IOException {
		BonnieCalculatedResult calculatedResult = new BonnieCalculatedResult();
		String uri = UPDATE_MEASURE_URI + "/" + hqmfSetId.toUpperCase() + CALCULATE_MEASURE_RESULTS_URI;
		
		HttpURLConnection connection = null;
		try {
			connection = getCalculationInformationConnection(bearerToken, uri);
			logger.info("GET " + connection.getURL());
			String code = Integer.toString(connection.getResponseCode());
			if (code.startsWith("2")) {
				ByteArrayOutputStream calculatedResultByteArray = readFully(connection.getInputStream());
				calculatedResult.setResult(calculatedResultByteArray.toByteArray());
			} else if (code.contains("401")) {
				// if the server throws a 401, we should return unauthorized exception
				// since the tokens were not valid
				String response = getResponse(connection.getErrorStream());
				logger.error(response);
				throw new BonnieUnauthorizedException();
			} else if (code.contains("404")) {
				// if the server throws a 404, we should return bonnieNotFound exception
				// since the measure id was not valid
				String response = getResponse(connection.getErrorStream());
				logger.error(response);
				throw new BonnieNotFoundException();
			} else if (code.contains("500") || code.contains("406")) {
				// if the server throws a 500 we should return generic bonnie server exception
				String response = getResponse(connection.getErrorStream());
				logger.error(response);
				throw new BonnieServerException();
			}
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
			BonnieServerException, IOException {
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse postResponse = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpPost postRequest = new HttpPost(getBonnieBaseURL() + UPDATE_MEASURE_URI);

			getRequestConfigProxy(postRequest);
			
			String bearerTokenString = "Bearer " + bearerToken;
			postRequest.addHeader("Authorization", bearerTokenString);
			postRequest.addHeader("boundary", "APIPIE_RECORDER_EXAMPLE_BOUNDARY");
			postRequest.addHeader("'Content-Type'", "multipart/form-data");
			postRequest.addHeader("'Content-Transfer'", "binary");
			
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setBoundary("APIPIE_RECORDER_EXAMPLE_BOUNDARY");
			 
			builder.addTextBody("calculation_type", calculationType);
			builder.addTextBody("vsac_tgt", vsacTicketGrantingTicket);
			builder.addTextBody("vsac_tgt_expires_at", vsacTicketExpiration);

			builder.addBinaryBody(
				    "measure_file",
				    zipFileContents,
				    ContentType.create("application/zip"),
				    fileName
				);
			
			HttpEntity multipart = builder.build();
			postRequest.setEntity(multipart);
			postResponse = httpClient.execute(postRequest);

			
			String code = String.valueOf(postResponse.getStatusLine().getStatusCode());
			if(code.startsWith("2")) {
				logger.info("Measure Upload success");
			} else if (code.contains("401")) {
				//401 Unauthorized
				logger.error("401: user unauthorized - Measure Upload");
				throw new BonnieUnauthorizedException();
			} else if (code.contains("400")) {
				//400 we sent bad parameters
				logger.error("400: MAT sent bad parameters - Measure Upload");
				throw new BonnieBadParameterException();
			} else if (code.contains("409")) {
				//409 Measure already exists
				logger.error("409: Measure set ID already exsists in Bonnie - Measure Upload");
				throw new BonnieAlreadyExistsException();
			} else if (code.startsWith("5")) {
				//500 server error occurred
				logger.error("500: Error interfacing with Bonnie - Measure Upload");
				throw new BonnieServerException();
			} 
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
			HttpPut putRequest = new HttpPut(getBonnieBaseURL() + UPDATE_MEASURE_URI + "/" + hqmfSetId.toUpperCase());
			
			getRequestConfigProxy(putRequest);
						
			logger.info("Connecting " + putRequest.getURI());
			String bearerTokenString = "Bearer " + bearerToken;
			putRequest.addHeader("Authorization", bearerTokenString);
			putRequest.addHeader("boundary", "APIPIE_RECORDER_EXAMPLE_BOUNDARY");
			putRequest.addHeader("'Content-Type'", "multipart/form-data");
			putRequest.addHeader("'Content-Transfer'", "binary");
			
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setBoundary("APIPIE_RECORDER_EXAMPLE_BOUNDARY");
			 
			builder.addTextBody("calculation_type", calculationType);
			builder.addTextBody("vsac_tgt", vsacTicketGrantingTicket);
			builder.addTextBody("vsac_tgt_expires_at", vsacTicketExpiration);

			builder.addBinaryBody(
				    "measure_file",
				    zipFileContents,
				    ContentType.create("application/zip"),
				    fileName
				);
			
			HttpEntity multipart = builder.build();
			putRequest.setEntity(multipart);
			putResponse = httpClient.execute(putRequest);
			
			String code = Integer.toString(putResponse.getStatusLine().getStatusCode());
			if(code.startsWith("2")) {
				logger.info("Measure Update successful");
			} else if (code.contains("401")) {
				//401 Unauthorized
				logger.error("401: user unauthorized - Measure Update");
				throw new BonnieUnauthorizedException();
			} else if (code.contains("400")) {
				//400 we sent bad parameters
				logger.error("400: MAT sent bad parameters - Measure Update");
				throw new BonnieBadParameterException();
			} else if (code.contains("404")) {
				//404 Measure does not exist to update
				logger.error("404: Measure Set ID " + hqmfSetId + " does not exsist in Bonnie database - Measure Update");
				throw new BonnieDoesNotExistException();
			} else if (code.startsWith("5")) {
				//500 server error occurred
				logger.error("500: Error interfacing with Bonnie - Measure Update");
				throw new BonnieServerException();
			} else {
				logger.error("Generic Error - Measure Update");
				throw new BonnieServerException();
			}
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

	@Override
	public BonnieUserInformationResult getUserInformationByToken(String token)
			throws BonnieUnauthorizedException, BonnieServerException, IOException {
		BonnieUserInformationResult userInformationResult = new BonnieUserInformationResult();
		HttpURLConnection connection = null;
		try {
			connection = getInformationConnection(token, GET_USER_INFORMATION_URI);

			logger.info("GET " + connection.getURL());

			String code = Integer.toString(connection.getResponseCode());
			if (code.startsWith("2")) {
				String response = getResponse(connection.getInputStream());
				JSONObject jsonObject = new JSONObject(response);
				String email = jsonObject.getString("user_email");
				userInformationResult.setBonnieUsername(email);
			} else if (code.startsWith("4")) {
				// if the server throws a 401 or 404, we should return unauthorized exception
				// since the tokens were not valid
				String response = getResponse(connection.getErrorStream());
				logger.error(response);
				throw new BonnieUnauthorizedException();
			} else if (code.startsWith("5")) {
				String response = getResponse(connection.getErrorStream());
				logger.error(response);
				throw new BonnieServerException();
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

		return userInformationResult;
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

	private String getProxyUrl() {
		return System.getProperty("vsac_proxy_host");
	}

	private String getProxyPort() {
		return System.getProperty("vsac_proxy_port");
	}
	
	private HttpURLConnection getInformationConnection(String token, String uri) throws IOException {
		HttpURLConnection connection = getBaseHttpConnection(token, uri);
		connection.setRequestMethod("GET");
		connection.connect();
		return connection;
	}
	
	private HttpURLConnection getCalculationInformationConnection(String token, String uri) throws IOException {
		HttpURLConnection connection = getBaseHttpConnection(token, uri);
		connection.setRequestProperty("Accept", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		connection.setRequestMethod("GET");
		connection.connect();
		return connection;
	}
	
	private HttpURLConnection getBaseHttpConnection(String token, String uri) throws IOException {
		String baseURL = getBonnieBaseURL();
		String requestUrl = baseURL + uri;
		String bearerToken = "Bearer " + token;
		// remove when this is in JVM variables
		setProxyVMVariables();
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization", bearerToken);
		return connection;
	}

	public BonnieOAuthResult getBonnieRefreshResult(UserBonnieAccessInfo userBonnieAccessInfo) throws BonnieUnauthorizedException {
		URLConnectionClient urlConnection = new URLConnectionClient();
		try {
			// remove when this is in JVM variables
			setProxyVMVariables();
			OAuthClient client = new OAuthClient(urlConnection);
			logger.info("Connecting to refresh bonnie oauth");
			OAuthClientRequest request = OAuthClientRequest.tokenLocation(getBonnieBaseURL() + "/oauth/token")
					.setClientId(getClientId()).setGrantType(GrantType.REFRESH_TOKEN)
					.setClientSecret(getClientSecret()).setRefreshToken(userBonnieAccessInfo.getRefreshToken())
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
			// remove when this is in JVM variables
			setProxyVMVariables();
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
			exn.printStackTrace();
			return null;
		} finally {
			if(urlConnection != null) {
				urlConnection.shutdown();
				logger.info("Disconnected from bonnie oauth");
			}
		}
	}
	
	private void getRequestConfigProxy(HttpEntityEnclosingRequestBase request) {
		if(!StringUtils.isEmpty(getProxyUrl()) && !StringUtils.isEmpty(getProxyPort())) {
			HttpHost proxy = new HttpHost(getProxyUrl(), Integer.valueOf(getProxyPort()));
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			request.setConfig(config);
		}
	}
	/**
	 * We should remove when this is in JVM variables
	 */
	private void setProxyVMVariables() {
		if(!StringUtils.isEmpty(getProxyUrl()) && !StringUtils.isEmpty(getProxyPort())) {
			System.setProperty("https.proxyHost", getProxyUrl());
			System.setProperty("https.proxyPort", getProxyPort());
		}
	}
}
