package mat.server.hqmf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
		
		if(matVersion >= 5.6) {
			logger.info("HQMF Generator Factory selected QDM v5.4 HQMF Generator");
			return new mat.server.hqmf.qdm_5_4.HQMFGenerator();
		} else if(matVersion >= 5.0 && matVersion < 5.6) {
			logger.info("HQMF Generator Factory selected QDM v5.3 HQMF Generator");
			return new mat.server.hqmf.qdm_5_3.HQMFGenerator();
		} else {
			logger.info("HQMF Generator Factory selected QDM v4.x HQMF Generator");
			return new mat.server.hqmf.qdm.HQMFGenerator();
		}
	}
}
