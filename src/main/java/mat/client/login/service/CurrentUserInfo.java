package mat.client.login.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.dto.UserPreferenceDTO;

public class CurrentUserInfo implements IsSerializable {

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
    public String organizationName;
    public List<ShortUserInfo> users = Collections.emptyList();
    public String harpId;

}
