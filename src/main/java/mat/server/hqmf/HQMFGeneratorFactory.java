package mat.server.hqmf;

import mat.server.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

/**
 * Factory class to select the proper HQMF generator based on version of MAT. This class specifically excludes v3 MAT HQMF files
 * due to the fact that they do not follow the same pattern. This class would not suit the needs of v3 MAT HQMF files.
 *
 * DO NOT DELETE OLDER GENERATORS.
 *
 * On Export, MAT will serve up the latest pre-existing instance in the MEASURE_EXPORT table. If no HQMF exists,
 * then MAT will generate the HQMF during the Export operation and save it to the table. Older versions of the Generators
 * are kept to ensure the HQMF can be generated on-demand.
 */
@Component
public class HQMFGeneratorFactory {

	private static final Log logger = LogFactory.getLog(HQMFGeneratorFactory.class);

	public Generator getHQMFGenerator(String matVersionNumber) {
		matVersionNumber = matVersionNumber.replace("v", "");
		double matVersion = Double.parseDouble(matVersionNumber);
		if (matVersion >= 6.05) {
			logger.debug("HQMF Generator Factory selected QDM v5.6 HQMF Generator");
			return new mat.server.hqmf.qdm_5_6.HQMFGenerator();
		} else if (matVersion >= 5.8) {
			logger.debug("HQMF Generator Factory selected QDM v5.5 HQMF Generator");
			return new mat.server.hqmf.qdm_5_5.HQMFGenerator();
		} else if (matVersion == 5.6 || matVersion == 5.7) {
			logger.debug("HQMF Generator Factory selected QDM v5.4 HQMF Generator");
			return new mat.server.hqmf.qdm_5_4.HQMFGenerator();
		} else if (matVersion >= 5.0 && matVersion < 5.6) {
			logger.debug("HQMF Generator Factory selected QDM v5.3 HQMF Generator");
			return new mat.server.hqmf.qdm_5_3.HQMFGenerator();
		} else {
			logger.debug("HQMF Generator Factory selected QDM v4.x HQMF Generator");
			return new mat.server.hqmf.qdm.HQMFGenerator();
		}
	}
}
