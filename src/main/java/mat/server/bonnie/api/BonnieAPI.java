package mat.server.bonnie.api;

import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.bonnie.api.result.BonnieMeasureResult;
import mat.shared.bonnie.error.BonnieAlreadyExistsException;
import mat.shared.bonnie.error.BonnieBadParameterException;
import mat.shared.bonnie.error.BonnieDoesNotExistException;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.result.BonnieUserInformationResult;

import java.io.IOException;
import java.util.List;

public interface BonnieAPI {
	
	/**
	 * Returns a list of all measures that are in Bonnie for a given user. 
	 * 
	 * GET api/api_v1/measures is the endpoint in the Bonnie API. 
	 * 
	 * @param bearerToken the bearer authentication token for a user
	 * 
	 * @return a list of measures for a user in Bonnie
	 * 
	 * @throws BonnieUnauthorizedException will throw a Bonnie Unauthorized Exception if the user's tokens are invalid 
	 */
	List<BonnieMeasureResult> getMeasuresForUser(String bearerToken) throws BonnieUnauthorizedException;
	
	/**
	 * Gets a measure by HQMF Set ID. 
	 * 
	 * GET /api/api_v1/measures/:id/calculated_results
	 * 
	 * @param bearerToken the bearer authentication token for a user
	 * @param hqmfSetId the hqmf set id for the measure to be returned

	 * @return the measure with the given hqmf set id from the Bonnie API. 
	 *
	 * @throws BonnieUnauthorizedException will throw a Bonnie Unauthorized Exception if the user's tokens are invalid 
	 * @throws BonnieNotFoundException will throw a Bonnie Not Found Exception if the measure with the given setId does not exist 
	 * @throws IOException 
	 */
	BonnieMeasureResult getMeasureById(String bearerToken, String hqmfSetId) throws BonnieUnauthorizedException, BonnieNotFoundException, IOException;
	
	/**
	 * Gets calculated results for a measure
	 * 
	 * GET api/api_v1/measures/:id/calculated_results
	 * 
	 * @param bearerToken the bearer authentication token for a user
	 * @param hqmfSetId the qmf set id for the measure to get results for
	 * @return the calculated results for a measure
	 * 
	 * @throws BonnieUnauthorizedException will throw a Bonnie Unauthorized Exception if the user's tokens are invalid 
	 * @throws BonnieNotFoundException will throw a Bonnie Not Found Exception if the measure with the given setId does not exist 
	 * @throws BonnieServerException will throw  Bonnie Server Exception is the Bonnie server was not able to handle the request, likely this is not an issue with the connection with MAT
	 * @throws IOException 
	 * @throws BonnieDoesNotExistException 
	 * @throws BonnieBadParameterException 
	 */
	BonnieCalculatedResult getCalculatedResultsForMeasure(String bearerToken, String hqmfSetId) throws BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, IOException, BonnieBadParameterException, BonnieDoesNotExistException;
	
	/**
	 * Uploads a new measure to bonnie. 
	 * 
	 * POST api/api_v1/measures
	 * 
	 * @param bearerToken the bearer authentication token or a user
	 * @param zipFileContents the contents of the zip file to be uploaded to Bonnie
	 * @param fileName the name of the zip file being uploaded to Bonnie
	 * @param measureType the measure type, should be EH or EP
	 * @param calculationType the calculation type, should be patient or episode
	 * @param vsacApiKey
	 *
	 * 
	 * @throws BonnieUnauthorizedException will throw a Bonnie Unauthorized Exception if the user's tokens are invalid 
	 * @throws BonnieBadParameterException will throw a Bonnie Bad Parameter Exception if the content of the parameters being sent to Bonnie are bad, unrecognizable, or missing. 
	 * @throws BonnieAlreadyExistsException will throw a Bonnie Already Exists Exception if the measure trying to be uploaded already exists in the Bonnie System. 
	 * @throws BonnieServerException will throw  Bonnie Server Exception is the Bonnie server was not able to handle the request, likely this is not an issue with the connection with MAT
	 * @throws IOException 
	 * @throws BonnieDoesNotExistException 
	 */
	void uploadMeasureToBonnie(String bearerToken, byte[] zipFileContents, String fileName, String measureType, String calculationType, String apiKey) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieAlreadyExistsException, BonnieServerException, IOException, BonnieDoesNotExistException;

	/**
	 * Updates a measure to bonnie. 
	 * 
	 * POST api/api_v1/measures
	 * 
	 * @param bearerToken the bearer authentication token or a user
	 * @param hqmfSetId the hqmf set id for the measure to be returned
	 * @param zipFileContents the contents of the zip file to be uploaded to Bonnie
	 * @param fileName the name of the zip file being uploaded to Bonnie
	 * @param measureType the measure type, should be EH or EP
	 * @param calculationType the calculation type, should be patient or episode
	 * @param vsacApiKey
	 * 
	 * 
	 * @throws BonnieUnauthorizedException will throw a Bonnie Unauthorized Exception if the user's tokens are invalid 
	 * @throws BonnieBadParameterException will throw a Bonnie Bad Parameter Excpetion if the content of the parameters being sent to Bonnie are bad, unrecognizable, or missing. 
	 * @throws BonnieNotFoundException will throw a Bonnie Not Found Exception if the measure with the given setId does not exist 
	 * @throws BonnieServerException will throw  Bonnie Server Exception is the Bonnie server was not able to handle the request, likely this is not an issue with the connection with MAT
	 * @throws IOException 
	 */
	void updateMeasureInBonnie(String bearerToken, String hqmfSetId, byte[] zipFileContents, String fileName, String measureType, String calculationType, String apiKey) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieDoesNotExistException, BonnieServerException, IOException;
	
	/**
	 * Gets user information from the access token
	 * @param bearerToken the bearer authentication token for the user
	 * @return the user information which was fetched by the token
	 * @throws BonnieServerException 
	 * @throws BonnieUnauthorizedException 
	 * @throws Exception 
	 */
	BonnieUserInformationResult getUserInformationByToken(String bearerToken) throws BonnieServerException, BonnieUnauthorizedException, Exception;

	
	/**
	 * Immediately deactivates User access tokens biased on bearerToken
	 * @param bearerToken the bearer authentication token for the user
	 * @param refreshToken the token to revoke
	 * @return the user information which was fetched by the token
	 * @throws BonnieServerException 
	 * @throws BonnieUnauthorizedException 
	 * @throws Exception 
	 */
	void revokeBonnieToken(String bearerToken, String refreshToken) throws BonnieServerException, BonnieUnauthorizedException, Exception;
}
