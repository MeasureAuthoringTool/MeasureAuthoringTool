package mat.client.login.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.DTO.UserPreferenceDTO;

/**
 * The Interface SessionManagementService.
 */
@RemoteServiceRelativePath("sessionService")
public interface SessionManagementService extends RemoteService{
	
	/**
	 * The Class Result.
	 * 
	 * Note: sessionCreationTimestamp tracks the abruptly closed browser tab session issue
	 */
	public class Result implements IsSerializable {
		
		public String userId;
		
		public String userEmail;
		
		public String userRole;
		
		public Date signInDate;
		
		public Date signOutDate;
		
		public String loginId;
		
		public String userFirstName;
		
		public String userLastName;
		
		public String currentSessionId;
		
		public String activeSessionId;
		
		public UserPreferenceDTO userPreference;
	}
	
	public SessionManagementService.Result getCurrentUser();
	
	public void renewSession();

	String getCurrentReleaseVersion();
}
