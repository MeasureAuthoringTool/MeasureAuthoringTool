package mat.server.bonnie.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.bonnie.api.result.BonnieMeasureResult;
import mat.server.bonnie.api.result.BonnieMeasureUploadResult;
import mat.shared.bonnie.error.BonnieAlreadyExistsException;
import mat.shared.bonnie.error.BonnieBadParameterException;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.result.BonnieUserInformationResult;


@Configurable
@Service
public class BonnieAPIv1 implements BonnieAPI {

	public BonnieAPIv1() {
		// TODO Auto-generated constructor stub
	}
	
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
		System.out.println("IN GET USER INFO");
		BonnieUserInformationResult userInformationResult = new BonnieUserInformationResult();
		userInformationResult.setBonnieUsername("Jack's Bonnie Username");	
		return userInformationResult;
	}

}
