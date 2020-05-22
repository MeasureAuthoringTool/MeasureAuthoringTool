package mat.client.login.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ShortUserInfo implements IsSerializable {
    public String userId;
    public String userEmail;
    public String userFirstName;
    public String userLastName;
    public String organizationName;
    public String harpId;
    public String loginId;
    public String role;
}
