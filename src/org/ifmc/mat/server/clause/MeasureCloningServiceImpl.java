package org.ifmc.mat.server.clause;

import org.ifmc.mat.client.measure.ManageMeasureDetailModel;
import org.ifmc.mat.client.measure.ManageMeasureSearchModel;
import org.ifmc.mat.client.measure.service.MeasureCloningService;
import org.ifmc.mat.client.shared.MatException;
import org.ifmc.mat.dao.clause.MeasureDAO;
import org.ifmc.mat.model.clause.MeasureShareDTO;
import org.ifmc.mat.server.SpringRemoteServiceServlet;
import org.ifmc.mat.server.util.MeasureUtility;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class MeasureCloningServiceImpl extends SpringRemoteServiceServlet implements MeasureCloningService {

	@Autowired
	private MeasureDAO measureDAO;
	
	@Override
	public ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, String loggedinUserId,boolean creatingDraft) throws MatException{
		measureDAO = (MeasureDAO)context.getBean("measureDAO");
		try {
			MeasureShareDTO dto = measureDAO.clone(currentDetails, loggedinUserId,creatingDraft, context);
			ManageMeasureSearchModel.Result result = new ManageMeasureSearchModel.Result();
			result.setId(dto.getMeasureId());
			result.setName(dto.getMeasureName());
			result.setShortName(dto.getShortName());
			result.setScoringType(dto.getScoringType());
			String formattedVersion = MeasureUtility.getVersionText(dto.getVersion(), dto.isDraft());
			result.setVersion(formattedVersion);
			result.setEditable(true);
			result.setClonable(true);
			return result;
		} catch (Exception e) {
			log(e.getMessage(), e);
			throw new MatException(e.getMessage());
		}
	}

}
