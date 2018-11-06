package mat.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.SecurityQuestions;

@Repository
public class SecurityQuestionsDAOImpl extends GenericDAO<SecurityQuestions, String> implements mat.dao.SecurityQuestionsDAO{
	
	public SecurityQuestionsDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<SecurityQuestions> getSecurityQuestions(){
		return this.find();
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
