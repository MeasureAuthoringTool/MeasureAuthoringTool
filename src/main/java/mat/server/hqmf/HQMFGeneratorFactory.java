package mat.server.hqmf;

import mat.server.hqmf.qdm_5_6.HQMFGenerator;
import mat.server.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

/**
 * Factory class to select the proper HQMF generator based on version of MAT. This class specifically excludes v3 MAT HQMF files
 * due to the fact that they do not follow the same pattern. This class would not suit the needs of v3 MAT HQMF files.  
 */
@Component
public class HQMFGeneratorFactory {
	
	private static final Log logger = LogFactory.getLog(HQMFGeneratorFactory.class);

	
	public Generator getHQMFGenerator(String matVersionNumber) {
		matVersionNumber = matVersionNumber.replace("v", "");
		double matVersion = Double.parseDouble(matVersionNumber);
		if (matVersion >= 6.10) {
			logger.debug("HQMF Generator Factory selected QDM v5.6 HQMF Generator");
			return new HQMFGenerator();
		}
		throw new IllegalArgumentException("Unknown MAT Version: " + matVersionNumber);
	}
}
