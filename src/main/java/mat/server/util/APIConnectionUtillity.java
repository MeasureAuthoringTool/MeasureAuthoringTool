package mat.server.util;

import mat.server.logging.LogFactory;
import mat.shared.FileInfomationObject;
import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

@Component
public class APIConnectionUtillity {
	
	private static final Log logger = LogFactory.getLog(APIConnectionUtillity.class);
	
	public HttpURLConnection createGETHTTPConnection(String uri, Map<String, String> requestPropertyMap) throws IOException {
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Set<String> keyValues = requestPropertyMap.keySet();
		for(String keyValue : keyValues) {
			String property = requestPropertyMap.get(keyValue);
			connection.addRequestProperty(keyValue, property);
		}
		connection.setRequestMethod("GET");
		connection.connect();
		return connection;
	}
	
	public HttpPut createPutConnection(String uri, String boundary, Map<String, String> requestHeaderMap, Map<String, String> textBuilderMap, Map<String, FileInfomationObject> filebuilderMap) {
		HttpPut putRequest = new HttpPut(uri);
		
		createGenericRequest(putRequest, boundary, requestHeaderMap, textBuilderMap, filebuilderMap);
		return putRequest;
	}
	
	public HttpPost createPostConnection(String uri, String boundary, Map<String, String> requestHeaderMap, Map<String, String> textBuilderMap, Map<String, FileInfomationObject> filebuilderMap) {
		HttpPost postRequest = new HttpPost(uri);
		
		createGenericRequest(postRequest, boundary, requestHeaderMap, textBuilderMap, filebuilderMap);
		return postRequest;
	}
	private void createGenericRequest(HttpEntityEnclosingRequestBase request, String boundary, Map<String, String> requestHeaderMap, Map<String, String> textBuilderMap, Map<String, FileInfomationObject> filebuilderMap) {
		logger.info("Connecting " + request.getURI());
		createHeaderOfConnection(request, requestHeaderMap);
		
		createMultiPartBuilderOfConnection(request, boundary, textBuilderMap, filebuilderMap);
	}
	
	private void createHeaderOfConnection(HttpEntityEnclosingRequestBase request, Map<String, String> requestHeaderMap) {
		Set<String> keyValues = requestHeaderMap.keySet();
		
		for(String keyValue : keyValues) {
			String property = requestHeaderMap.get(keyValue);
			request.addHeader(keyValue, property);
		}
		
	}
	
	private void createMultiPartBuilderOfConnection(HttpEntityEnclosingRequestBase request, String boundary, Map<String, String> textBuilderMap, Map<String, FileInfomationObject> filebuilderMap) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setBoundary(boundary);
		
		Set<String> textKeyValues = textBuilderMap.keySet();
		
		for(String keyValue : textKeyValues) {
			String property = textBuilderMap.get(keyValue);
			builder.addTextBody(keyValue, property);
		}
		
		Set<String> binaryKeyValues = filebuilderMap.keySet();
		
		for(String keyValue : binaryKeyValues) {
			FileInfomationObject property = filebuilderMap.get(keyValue);
			builder.addBinaryBody(
					keyValue,
				    property.getFileContents(),
				    property.getFileType(),
				    property.getFileName()
				);
		}
		
		HttpEntity multipart = builder.build();
		request.setEntity(multipart);
		
	}
}
