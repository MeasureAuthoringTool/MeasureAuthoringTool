package mat.server;


import mat.client.featureFlag.service.FeatureFlagService;
import mat.dao.FeatureFlagDAO;
import mat.model.FeatureFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeatureFlagServiceImpl extends SpringRemoteServiceServlet implements FeatureFlagService {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FeatureFlagDAO featureFlagDAO;

	@Override
	public Map<String, Boolean> findFeatureFlags() {
		List<FeatureFlag> featureFlagList = featureFlagDAO.findAllFeatureFlags();
		return featureFlagList.stream()
				.collect(Collectors.toMap(FeatureFlag::getFlagName, FeatureFlag::isFlagOn));
	}

}
