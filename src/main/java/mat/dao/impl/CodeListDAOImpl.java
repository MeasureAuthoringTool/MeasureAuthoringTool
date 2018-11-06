package mat.dao.impl;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.DTO.CodesDTO;
import mat.dao.search.GenericDAO;
import mat.model.Code;
import mat.model.CodeList;


@Repository
public class CodeListDAOImpl extends GenericDAO<CodeList, String> implements mat.dao.CodeListDAO {
	
	public CodeListDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
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
	
	
}
