package mat.dao.impl.clause;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mat.dao.ListObjectDAO;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.search.GenericDAO;
import mat.model.ListObject;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.clause.Measure;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.LoggedInUserUtil;
import mat.shared.ConstantMessages;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class MeasureXMLDAO extends GenericDAO<MeasureXML, String> implements mat.dao.clause.MeasureXMLDAO {

	/**ListObjectDAO.**/
	@Autowired
	private ListObjectDAO listObjectDAO;

	/**DataTypeDAO.**/
	@Autowired
	private mat.dao.DataTypeDAO dataTypeDAO;
	
	@Autowired
	private MeasureDAO measureDAO; 
	
	@Autowired
	private UserDAO userDAO; 

	private static Map<String, String> suppDataOidAndDataTypeNameMap = new HashMap<String, String>();

	static {
		suppDataOidAndDataTypeNameMap.put("2.16.840.1.113762.1.4.1", ConstantMessages.PATIENT_CHARACTERISTIC_GENDER);
		suppDataOidAndDataTypeNameMap.put("2.16.840.1.114222.4.11.836", ConstantMessages.PATIENT_CHARACTERISTIC_RACE);
		suppDataOidAndDataTypeNameMap.put("2.16.840.1.114222.4.11.837", ConstantMessages.PATIENT_CHARACTERISTIC_ETHNICITY);
		suppDataOidAndDataTypeNameMap.put("2.16.840.1.114222.4.11.3591", ConstantMessages.PATIENT_CHARACTERISTIC_PAYER);
	}

	@Override
	public final MeasureXML findForMeasure(final String measureId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MeasureXML.class);

		criteria.add(Restrictions.eq("measureId", measureId));
		List<MeasureXML> results = criteria.list();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return null;
		}
	}

	@Override
	public final QualityDataModelWrapper createTimingElementQDMs(
			final List<String> qdmOidList) {
		List<ListObject> elementList = listObjectDAO
				.getElementCodeListByOID(qdmOidList);
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		ArrayList<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();
		for (ListObject lo : elementList) {
			QualityDataSetDTO qds = new QualityDataSetDTO();
			qds.setOid(lo.getOid());
			qds.setCodeListName(lo.getName());
			qds.setTaxonomy(lo.getCodeSystem().getDescription());
			qds.setVersion("1.0");
			qds.setId(lo.getId());
			qds.setUuid(UUID.randomUUID().toString());
			qds.setSuppDataElement(false);
            //for Patient Characteristic Expired
			if(lo.getName().equals(ConstantMessages.DEAD)) {
				qds.setDataType(dataTypeDAO
						.findByDataTypeName(ConstantMessages.PATIENT_CHARACTERISTIC_EXPIRED)
						.getDescription());
				
				//Patient Characteristic BirthDate
			} else if(lo.getName().equals(ConstantMessages.BIRTHDATE)) {
				qds.setDataType(dataTypeDAO
						.findByDataTypeName(ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE)
						.getDescription());
				//For Timing Element
			} else {
				qds.setDataType((dataTypeDAO
					.findByDataTypeName(ConstantMessages.TIMING_ELEMENT)
					.getDescription()));
			}
			qdsList.add(qds);
		}
		wrapper.setQualityDataDTO(qdsList);
		return wrapper;
	}

	@Override
	public final CQLQualityDataModelWrapper createSupplimentalQDM(final String measureId,
			final boolean isClone, final HashMap<String, String> uuidMap) {
		// Get the Supplimental ListObject from the list_object table
		List<ListObject> listOfSuppElements = listObjectDAO
				.getSupplimentalCodeList();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<CQLQualityDataSetDTO>();
		wrapper.setQualityDataDTO(qdsList);
		for (ListObject lo : listOfSuppElements) {
			CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
			qds.setOid(lo.getOid());
			qds.setName(lo.getName());
			qds.setTaxonomy(lo.getCodeSystem().getDescription());
			qds.setVersion("1.0");
			qds.setId(lo.getId());
			qds.setDataType(findDataTypeForOID(lo.getOid(), lo.getCategory().getId())); 
			if (isClone && uuidMap != null) {
				qds.setUuid(uuidMap.get(lo.getName()));
			} else {
				qds.setUuid(UUID.randomUUID().toString());
			}
			qds.setSuppDataElement(true);
			wrapper.getQualityDataDTO().add(qds);
		}
		return wrapper;
	}

	private String findDataTypeForOID(final String oid, final String categoryId) {
		String dataType = null;
		String dataTypeName = suppDataOidAndDataTypeNameMap.get(oid);
		dataType = (dataTypeDAO.findDataTypeForSupplimentalCodeList(dataTypeName, categoryId)).getDescription();
		return dataType;
	}
	
	@Override
	public void save(MeasureXML measureXML) {
		String measureId = measureXML.getMeasureId();
		Measure measure = measureDAO.find(measureId);
		measure.setLastModifiedOn(Timestamp.valueOf(LocalDateTime.now()));
		measure.setLastModifiedBy(userDAO.findByLoginId(LoggedInUserUtil.getLoggedInLoginId()));
		measureDAO.save(measure);
		super.save(measureXML);
	}
}
