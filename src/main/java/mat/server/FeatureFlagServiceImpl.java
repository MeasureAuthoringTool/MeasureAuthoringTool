package mat.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.client.featureFlag.service.FeatureFlagService;
import mat.dao.FeatureFlagDAO;
import mat.model.FeatureFlag;

import java.util.List;

@Service
public class FeatureFlagServiceImpl extends SpringRemoteServiceServlet implements FeatureFlagService {

	private static final long serialVersionUID = 1L;

	@Autowired
	FeatureFlagDAO featureFlagDAO;

	@Override
	public List<FeatureFlag> findFeatureFlag() {
		return featureFlagDAO.findAllFeatureFlags();
	}

}
