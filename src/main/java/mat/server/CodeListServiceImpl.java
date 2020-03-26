package mat.server;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import mat.DTO.OperatorDTO;
import mat.DTO.UnitDTO;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataSetDTO;
import mat.server.service.CodeListService;
import mat.shared.ConstantMessages;

/**
 * The Class CodeListServiceImpl.
 */
@SuppressWarnings("serial")
public class CodeListServiceImpl extends SpringRemoteServiceServlet
implements mat.client.codelist.service.CodeListService {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CodeListServiceImpl.class);
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getAllDataTypes()
	 */
	@Override
	public List<? extends HasListBox> getAllDataTypes() {
		List<? extends HasListBox> ret = getCodeListService().getAllDataTypes();
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getAllOperators()
	 */
	@Override
	public List<OperatorDTO> getAllOperators() {
		return getCodeListService().getAllOperators();
	}	
		
	/**
	 * Gets the code list service.
	 * 
	 * @return the code list service
	 */
	public CodeListService getCodeListService() {
		return (CodeListService)context.getBean("codeListService");
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getListBoxData()
	 */
	@Override
	public mat.client.codelist.service.CodeListService.ListBoxData getListBoxData() {
		
		logger.info("getListBoxData");
		mat.client.codelist.service.CodeListService.ListBoxData data =
				new mat.client.codelist.service.CodeListService.ListBoxData();
		data = getCodeListService().getListBoxData();
		return data;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getQDSDataTypeForCategory(java.lang.String)
	 */
	@Override
	public List<? extends HasListBox> getQDSDataTypeForCategory(String category) {
		return getCodeListService().getQDSDataTypeForCategory(category);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getQDSElements(java.lang.String, java.lang.String)
	 */
	@Override
	public List<QualityDataSetDTO> getQDSElements(String measureId,
			String version) {
		List<QualityDataSetDTO> qdsElements = getCodeListService().getQDSElements(measureId, version);
		List<QualityDataSetDTO> filteredQDSElements = new ArrayList<QualityDataSetDTO>();
		for(QualityDataSetDTO dataSet : qdsElements) {
			if((dataSet.getOid() != null) && !dataSet.getOid().equals(ConstantMessages.GENDER_OID)
					&& !dataSet.getOid().equals(ConstantMessages.RACE_OID) && !dataSet.getOid().equals(ConstantMessages.ETHNICITY_OID)
					&& !dataSet.getOid().equals(ConstantMessages.PAYER_OID)){
				filteredQDSElements.add(dataSet);
			} else {
				System.out.println();
			}
			
		}
		Collections.sort(filteredQDSElements, new Comparator<QualityDataSetDTO>() {
			@Override
			public int compare(QualityDataSetDTO o1, QualityDataSetDTO o2) {
				return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
			}
		});
		return filteredQDSElements;
	}
	
	

	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	/*private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}*/
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#saveQDStoMeasure(mat.model
	 * .MatValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCodeListResult saveQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		return getCodeListService().saveQDStoMeasure(matValueSetTransferObject);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#saveUserDefinedQDStoMeasure
	 * (mat.model.MatValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		return getCodeListService().saveUserDefinedQDStoMeasure(matValueSetTransferObject);
	}
	

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#updateCodeListToMeasure(mat
	 * .model.MatValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCodeListResult updateCodeListToMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		matValueSetTransferObject.scrubForMarkUp();
		return getCodeListService().updateQDStoMeasure(matValueSetTransferObject);
	}
	

	@Override
	public List<UnitDTO> getAllCqlUnits() {
		logger.info("getAllCqlUnits");
		List<UnitDTO> data =  getCodeListService().getAllUnits();
		
		return data;
	}
}