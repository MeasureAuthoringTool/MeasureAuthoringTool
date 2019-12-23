package mat.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.client.featureFlag.service.FeatureFlagService;
import mat.dao.FeatureFlagDAO;
import mat.model.FeatureFlag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeatureFlagServiceImpl extends SpringRemoteServiceServlet implements FeatureFlagService {

	private static final long serialVersionUID = 1L;

	Map<String, Boolean> featureFlagMap = new HashMap<>();

	@Autowired
	FeatureFlagDAO featureFlagDAO;

	@Override
	public Map<String, Boolean> findFeatureFlags() {
		List<FeatureFlag> featureFlagList = featureFlagDAO.findAllFeatureFlags();
		featureFlagMap  = featureFlagList.stream()
				.collect(Collectors.toMap(f -> f.getFlagName(), v -> v.getFlagOn()));
		return featureFlagMap;
	}

}
