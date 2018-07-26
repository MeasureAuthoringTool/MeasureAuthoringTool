package mat.server.bonnie.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import mat.server.bonnie.BonnieServiceImpl;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.bonnie.api.result.BonnieMeasureResult;
import mat.server.bonnie.api.result.BonnieMeasureUploadResult;
import mat.shared.bonnie.error.BonnieAlreadyExistsException;
import mat.shared.bonnie.error.BonnieBadParameterException;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.result.BonnieUserInformationResult;


@Configurable
@Service
public class BonnieAPIv1 implements BonnieAPI {
	
	@Autowired private BonnieServiceImpl bonnieServiceImpl;
	
	private static final Log logger = LogFactory.getLog(BonnieAPIv1.class);


	public BonnieAPIv1() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<BonnieMeasureResult> getMeasuresForUser(String bearerToken) throws BonnieUnauthorizedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieMeasureResult getMeasureById(String bearerToken, String hqmfSetId)
			throws BonnieUnauthorizedException, BonnieNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieCalculatedResult getCalculatedResultsForMeasure(String bearerToken, String hqmfSetId)
			throws BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieMeasureUploadResult uploadMeasureToBonnie(String bearerToken, byte[] zipFileContents, String fileName,
			String measureType, String calculationType, String vsacTicketGrantingTicket, String vsacTicketExpiration)
			throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieAlreadyExistsException,
			BonnieServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieMeasureUploadResult updateMeasureInBonnie(String bearerToken, String hqmfSetId, byte[] zipFileContents,
			String fileName, String measureType, String calculationType, String vsacTicketGrantingTicket,
			String vsacTicketExpiration) throws BonnieUnauthorizedException, BonnieBadParameterException,
			BonnieAlreadyExistsException, BonnieServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieUserInformationResult getUserInformationByToken(String token) throws BonnieUnauthorizedException, BonnieServerException, IOException {
		BonnieUserInformationResult userInformationResult = new BonnieUserInformationResult();
		try {
			HttpURLConnection connection = get(token, "/oauth/token/info");
			logger.info("GET " + connection.getURL());
	
			String code = Integer.toString(connection.getResponseCode());						
			if(code.startsWith("2")) {
				String response = getResponse(connection.getInputStream());
				JSONObject jsonObject = new JSONObject(response);
				String email = jsonObject.getString("user_email");
				userInformationResult.setBonnieUsername(email);			
			} else if(code.startsWith("4")) { 
				// if the server throws a 401 or 404, we should return unauthorized exception since the tokens were not valid
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
	
	
	private HttpURLConnection get(String token, String uri) throws IOException {
		String baseURL = bonnieServiceImpl.getBonnieBaseURL();
		String requestUrl = baseURL + uri;
		String bearerToken = "Bearer " + token;
		System.setProperty("https.proxyHost", getProxyUrl());
		System.setProperty("https.proxyPort", getProxyPort());
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Authorization", bearerToken);
		connection.connect();
		return connection; 
	}
}
