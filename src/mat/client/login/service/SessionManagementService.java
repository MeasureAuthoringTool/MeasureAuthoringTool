package mat.client.login.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sessionService")
public interface SessionManagementService extends RemoteService{
	public class Result implements IsSerializable {
		public String userId;
		public String userEmail;
		public String userRole;
		public Date signInDate;
		public Date signOutDate;
	}
	
	public SessionManagementService.Result getCurrentUserRole();
	public void renewSession();
}
