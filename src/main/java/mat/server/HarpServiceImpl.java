package mat.server;

import mat.client.login.service.HarpService;
import mat.server.util.ServerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HarpServiceImpl extends SpringRemoteServiceServlet implements HarpService {
//    private static final Log logger = LogFactory.getLog(HarpServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean revoke(String accessToken) {
        return false;
    }

    @Override
    public String getHarpUrl() {
        return ServerConstants.getHarpUrl();
    }

    @Override
    public String getHarpBaseUrl() {
        return ServerConstants.getHarpBaseUrl();
    }
}
