package mat.dao.impl.clause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mat.dao.ListObjectDAO;
import mat.dao.search.GenericDAO;
import mat.model.ListObject;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.clause.MeasureXML;
import mat.shared.ConstantMessages;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class MeasureXMLDAO extends GenericDAO<MeasureXML, String>  implements mat.dao.clause.MeasureXMLDAO {
	
	@Autowired
	private ListObjectDAO listObjectDAO;
	
	@Autowired
	private mat.dao.DataTypeDAO dataTypeDAO;
	
	@Override
	public MeasureXML findForMeasure(String measureId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MeasureXML.class);
		
		criteria.add(Restrictions.eq("measure_id", measureId));
		List<MeasureXML> results =  criteria.list();
		if(!results.isEmpty()) {
			return results.get(0);
		}
		else {
			return null;
		}
	}

	@Override
	public QualityDataModelWrapper createSupplimentalQDM(String measureId, boolean isClone, HashMap<String,String> uuidMap){
		//Get the Supplimental ListObject from the list_object table
		List<ListObject> listOfSuppElements = listObjectDAO.getSupplimentalCodeList();
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		ArrayList<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();
		wrapper.setQualityDataDTO(qdsList);
		for(ListObject lo : listOfSuppElements){
			QualityDataSetDTO qds = new QualityDataSetDTO();
			qds.setOid(lo.getOid());
			qds.setCodeListName(lo.getName());
			qds.setTaxonomy(lo.getCodeSystem().getDescription());
			qds.setVersion("1");
			qds.setId(lo.getId());
			if(isClone && uuidMap != null){
				if(lo.getOid().equalsIgnoreCase("2.16.840.1.113762.1.4.1")){
					//find out patient characteristic gender dataType.
					qds.setDataType((dataTypeDAO.findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_GENDER, lo.getCategory().getId())).getDescription());
					qds.setUuid(uuidMap.get(ConstantMessages.GENDER));
				}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.836")){
					//find out patient characteristic race dataType.
					qds.setDataType((dataTypeDAO.findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_RACE, lo.getCategory().getId())).getDescription());
					qds.setUuid(uuidMap.get(ConstantMessages.RACE));
				}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.837")){
					//find out patient characteristic ethnicity dataType.
					qds.setDataType((dataTypeDAO.findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_ETHNICITY, lo.getCategory().getId())).getDescription());
					qds.setUuid(uuidMap.get(ConstantMessages.ETHNICITY));
				}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.3591")){
					//find out patient characteristic payer dataType.
					qds.setDataType((dataTypeDAO.findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_PAYER, lo.getCategory().getId())).getDescription());
					qds.setUuid(uuidMap.get(ConstantMessages.PAYER));
				}
				
			}else{	
				qds.setUuid(UUID.randomUUID().toString());
				if(lo.getOid().equalsIgnoreCase("2.16.840.1.113762.1.4.1")){
					//find out patient characteristic gender dataType.
					qds.setDataType((dataTypeDAO.findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_GENDER, lo.getCategory().getId())).getDescription());
				}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.836")){
					//find out patient characteristic race dataType.
					qds.setDataType((dataTypeDAO.findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_RACE, lo.getCategory().getId())).getDescription());
				}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.837")){
					//find out patient characteristic ethnicity dataType.
					qds.setDataType((dataTypeDAO.findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_ETHNICITY, lo.getCategory().getId())).getDescription());
				}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.3591")){
					//find out patient characteristic payer dataType.
					qds.setDataType((dataTypeDAO.findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_PAYER, lo.getCategory().getId())).getDescription());
				}
			}
			qds.setSuppDataElement(true);
			//getMeasurePackageService().saveSupplimentalQDM(qds);
			wrapper.getQualityDataDTO().add(qds);
		}		
		return wrapper;
	}
	
}
