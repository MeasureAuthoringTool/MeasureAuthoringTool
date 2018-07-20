package mat.server.bonnie.api;

import java.util.List;

import mat.server.bonnie.api.error.BonnieAlreadyExistsException;
import mat.server.bonnie.api.error.BonnieBadParameterException;
import mat.server.bonnie.api.error.BonnieNotFoundException;
import mat.server.bonnie.api.error.BonnieServerException;
import mat.server.bonnie.api.error.BonnieUnauthorizedException;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.bonnie.api.result.BonnieMeasureResult;
import mat.server.bonnie.api.result.BonnieMeasureUploadResult;
import mat.server.bonnie.api.result.BonnieUserInformationResult;

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
	 */
	BonnieMeasureResult getMeasureById(String bearerToken, String hqmfSetId) throws BonnieUnauthorizedException, BonnieNotFoundException;
	
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
	 */
	BonnieCalculatedResult getCalculatedResultsForMeasure(String bearerToken, String hqmfSetId) throws BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException;
	
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
	 * @param vsacTicketGrantingTicket the 8 hour VSAC ticket for a user
	 * @param vsacTicketExpiration the expiration date of the 8 hour VSAC ticket
	 * 
	 * @return the upload to bonnie result
	 * 
	 * @throws BonnieUnauthorizedException will throw a Bonnie Unauthorized Exception if the user's tokens are invalid 
	 * @throws BonnieBadParameterException will throw a Bonnie Bad Parameter Exception if the content of the parameters being sent to Bonnie are bad, unrecognizable, or missing. 
	 * @throws BonnieAlreadyExistsException will throw a Bonnie Already Exists Exception if the measure trying to be uploaded already exists in the Bonnie System. 
	 * @throws BonnieServerException will throw  Bonnie Server Exception is the Bonnie server was not able to handle the request, likely this is not an issue with the connection with MAT
	 */
	BonnieMeasureUploadResult uploadMeasureToBonnie(String bearerToken, byte[] zipFileContents, String fileName, String measureType, String calculationType, String vsacTicketGrantingTicket, String vsacTicketExpiration) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieAlreadyExistsException, BonnieServerException;

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
	 * @param vsacTicketGrantingTicket the 8 hour VSAC ticket for a user
	 * @param vsacTicketExpiration the expiration date of the 8 hour VSAC ticket
	 * 
	 * @return the update bonnie measure result
	 * 
	 * @throws BonnieUnauthorizedException will throw a Bonnie Unauthorized Exception if the user's tokens are invalid 
	 * @throws BonnieBadParameterException will throw a Bonnie Bad Parameter Excpetion if the content of the parameters being sent to Bonnie are bad, unrecognizable, or missing. 
	 * @throws BonnieNotFoundException will throw a Bonnie Not Found Exception if the measure with the given setId does not exist 
	 * @throws BonnieServerException will throw  Bonnie Server Exception is the Bonnie server was not able to handle the request, likely this is not an issue with the connection with MAT
	 */
	BonnieMeasureUploadResult updateMeasureInBonnie(String bearerToken, String hqmfSetId, byte[] zipFileContents, String fileName, String measureType, String calculationType, String vsacTicketGrantingTicket, String vsacTicketExpiration) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieAlreadyExistsException, BonnieServerException;

	/**
	 * Gets user information from the access token
	 * @param bearerToken the bearer authentication token for the user
	 * @return the user information which was fetched by the token
	 */
	BonnieUserInformationResult getUserInformationByToken(String bearerToken);
}
