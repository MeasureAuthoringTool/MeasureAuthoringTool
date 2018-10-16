package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.StewardDTO;
import mat.dao.search.GenericDAO;
import mat.model.MeasureSteward;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;


/**
 * The Class StewardDAO.
 */
public class StewardDAO extends GenericDAO<MeasureSteward, String> implements mat.dao.StewardDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(StewardDAO.class);
	
	/* (non-Javadoc)
	 * @see mat.dao.StewardDAO#getAllStewardOrg()
	 */
	public List<StewardDTO> getAllStewardOrg(){
		
		List<StewardDTO> StewardDTOList = new ArrayList<StewardDTO>();
		logger.info("Getting all the rows from the Steward table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<MeasureSteward> StewardList = session.createCriteria(MeasureSteward.class).list();
		for(MeasureSteward Steward: StewardList){
			StewardDTO StewardDTO =  new StewardDTO();
			StewardDTO.setOrgName(Steward.getOrgName());
			StewardDTO.setId(Steward.getId());
			StewardDTOList.add(StewardDTO);
		}
		return StewardDTOList;
	}
}
