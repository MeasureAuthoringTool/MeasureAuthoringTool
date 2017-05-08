package mat.dao.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mat.DTO.CodesDTO;
import mat.dao.search.GenericDAO;
import mat.model.Code;
import mat.model.CodeList;


/**
 * The Class CodeListDAO.
 */
public class CodeListDAO extends GenericDAO<CodeList, String> implements mat.dao.CodeListDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CodeListDAO.class);
	

	/* (non-Javadoc)
	 * @see mat.dao.CodeListDAO#getCodeListsForCategoryByMeasure(java.lang.String, mat.model.clause.Measure)
	 */
	/*@Override
	public List<CodeList> getCodeListsForCategoryByMeasure(String categoryId,
			Measure measure) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CodeList.class);
		criteria.add(Restrictions.eq("category.id", categoryId));
		criteria.add(Restrictions.eq("objectOwner.id", measure.getOwner().getId()));
		criteria.add(Restrictions.or(Restrictions.isNull("measureId"),
				Restrictions.or(Restrictions.eq("measureId", measure.getId()),
				Restrictions.eq("qds.measureId", measure))));
		criteria.createAlias("qualityDataSets", "qds", Criteria.LEFT_JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}*/

	//US 413. Include condition for Steward Other value.
	/* (non-Javadoc)
	 * @see mat.dao.CodeListDAO#getCodeList(mat.client.codelist.ManageCodeListDetailModel, java.lang.String)
	 */
	//@Override
	/*public CodeList getCodeList(ManageCodeListDetailModel currentDetails, String userid) {
		CodeList cl = find(currentDetails.getID());
		String newOID = currentDetails.getOid();
		//determine family oid to ensure no dups outside of family
		String famOid = cl == null ? newOID :
			cl.getOid();		
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CodeList.class);
		criteria.add(Restrictions.eq("name", currentDetails.getName()));
		criteria.add(Restrictions.eq("steward.id", currentDetails.getSteward()));
		criteria.add(Restrictions.eq("category.id", currentDetails.getCategory()));
		criteria.add(Restrictions.eq("codeSystem.id", currentDetails.getCodeSystem()));
		criteria.add(Restrictions.eq("objectOwner.id", userid));
		criteria.add(Restrictions.eq("stewardOther", currentDetails.getStewardOther()));
		criteria.add(Restrictions.ne("oid", famOid));
		//US216,uniqueResult hibernate method was throwing error only in IE. So switched to handle this way.
		if(criteria.list() != null && criteria.list().size() > 0){
			return (CodeList)criteria.list().get(0);
		}else{
			return null;
		}
	}*/

	/* (non-Javadoc)
	 * @see mat.dao.CodeListDAO#getValueSetsForCategory(java.lang.String)
	 */
	/*@Override
	public List<CodeList> getValueSetsForCategory(String categoryId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CodeList.class);
		criteria.add(Restrictions.eq("category.id", categoryId));
		
		return only the most recent name
		criteria.addOrder(Order.desc("oid")).addOrder(Order.desc("draft")).addOrder(Order.desc("lastModified"));
		List<CodeList> cls = criteria.list();
		ArrayList<CodeList> ret = new ArrayList<CodeList>();
		String oid = "";
		for(CodeList cl : cls){
			String coid = cl.getOid();
			if(!coid.equalsIgnoreCase(oid)){
				ret.add(cl);
			}
			oid = coid;
		}
		return ret;
	}*/

	/* (non-Javadoc)
	 * @see mat.dao.CodeListDAO#getCodes(java.lang.String)
	 */
	public Set<CodesDTO> getCodes(String codeListId){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CodeList.class);
		CodeList codeList = (CodeList)criteria.add(Restrictions.eq("id",codeListId)).uniqueResult();
		Set<Code> setofCodes = codeList.getCodes();
		Set<CodesDTO> setDTO = new HashSet<CodesDTO>();
		for(Code code: setofCodes){
			CodesDTO codesDTO = new CodesDTO();
			codesDTO.setCode(code.getCode());
			codesDTO.setDescription(code.getDescription());
			codesDTO.setId(code.getId());
			setDTO.add(codesDTO);
		}
		return setDTO;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.CodeListDAO#getCodeList(mat.client.codelist.ManageCodeListDetailModel, java.sql.Timestamp)
	 */
	/*@Override
	public List<CodeList> getCodeList(ManageCodeListDetailModel currentDetails, Timestamp ts) {
		CodeList cl = find(currentDetails.getID());
		String newOID = currentDetails.getOid();
		String famOid = cl == null ? newOID :
			cl.getOid();		
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CodeList.class);
		criteria.add(Restrictions.ne("id", currentDetails.getID()));
		criteria.add(Restrictions.eq("oid", famOid));
		criteria.add(Restrictions.eq("lastModified", ts));
		List<CodeList> cls = criteria.list();
		return cls;
	}*/
	
}
