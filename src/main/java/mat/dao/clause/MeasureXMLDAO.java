package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.QualityDataModelWrapper;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLQualityDataModelWrapper;

import java.util.HashMap;
import java.util.List;

/**
 * The Interface MeasureXMLDAO.
 */
public interface MeasureXMLDAO extends IDAO<MeasureXML, String> {
	
	/**
	 * Find for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure xml
	 */
	public MeasureXML findForMeasure(String measureId);

	/**
	 * Creates the supplimental qdm.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isClone
	 *            the is clone
	 * @param uuidMap
	 *            the uuid map
	 * @return the quality data model wrapper
	 */
	public CQLQualityDataModelWrapper createSupplimentalQDM(String measureId,
			boolean isClone, HashMap<String, String> uuidMap);

	/**
	 * Creates the timing element qd ms.
	 * 
	 * @param elementQDM_OID_List
	 *            the element qd m_ oi d_ list
	 * @return the quality data model wrapper
	 */
	QualityDataModelWrapper createTimingElementQDMs(List<String> elementQDM_OID_List);
}
