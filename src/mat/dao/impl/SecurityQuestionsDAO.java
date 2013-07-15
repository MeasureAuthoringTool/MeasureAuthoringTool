package mat.dao.impl;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.SecurityQuestions;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class SecurityQuestionsDAO extends GenericDAO<SecurityQuestions, String> implements mat.dao.SecurityQuestionsDAO{
	
		
	@Override
	public List<SecurityQuestions> getSecurityQuestions(){
		return this.find();
//		Session session = getSessionFactory().getCurrentSession();
//		Criteria criteria = session.createCriteria(SecurityQuestions.class);
//		List<SecurityQuestions> results = criteria.list();
//		return results;
	}
	
	@Override 
	public SecurityQuestions getSecurityQuestionObj(String question){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SecurityQuestions.class);
		criteria.add(Restrictions.ilike("question", question));
		List<SecurityQuestions> results = criteria.list();
		return results.get(0);
	}
	
}
