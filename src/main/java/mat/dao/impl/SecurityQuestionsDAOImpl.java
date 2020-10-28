package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.model.SecurityQuestions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("securityQuestionsDAO")
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
		
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<SecurityQuestions> query = cb.createQuery(SecurityQuestions.class);
		final Root<SecurityQuestions> root = query.from(SecurityQuestions.class);
		
		query.select(root).where(cb.like(cb.lower(root.get("question")), "%" + question.toLowerCase() + "%"));
		
		final List<SecurityQuestions> results = session.createQuery(query).getResultList();

		return results.get(0);
	}
	
}
