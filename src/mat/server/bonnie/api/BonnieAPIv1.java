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

public class BonnieAPIv1 implements BonnieAPI {

	@Override
	public List<BonnieMeasureResult> getMeasuresForUser(String bearerToken) throws BonnieUnauthorizedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieMeasureResult getMeasureById(String bearerToken, String hqmfSetId)
			throws BonnieUnauthorizedException, BonnieNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieCalculatedResult getCalculatedResultsForMeasure(String bearerToken, String hqmfSetId)
			throws BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieMeasureUploadResult uploadMeasureToBonnie(String bearerToken, byte[] zipFileContents, String fileName,
			String measureType, String calculationType, String vsacTicketGrantingTicket, String vsacTicketExpiration)
			throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieAlreadyExistsException,
			BonnieServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieMeasureUploadResult updateMeasureInBonnie(String bearerToken, String hqmfSetId, byte[] zipFileContents,
			String fileName, String measureType, String calculationType, String vsacTicketGrantingTicket,
			String vsacTicketExpiration) throws BonnieUnauthorizedException, BonnieBadParameterException,
			BonnieAlreadyExistsException, BonnieServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BonnieUserInformationResult getUserInformationByToken(String bearerToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
