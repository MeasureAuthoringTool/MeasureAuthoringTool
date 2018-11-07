package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.DTO.AuthorDTO;
import mat.dao.search.GenericDAO;
import mat.model.Author;

@Repository("authorDAO")
public class AuthorDAOImpl extends GenericDAO<Author, String> implements mat.dao.AuthorDAO {
	
	private static final Log logger = LogFactory.getLog(AuthorDAOImpl.class);
	
	
	public AuthorDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}


	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<AuthorDTO> getAllAuthors(){
		
		List<AuthorDTO> AuthorDTOList = new ArrayList<AuthorDTO>();
		logger.info("Getting all the rows from the Steward table");
		Session session = getSessionFactory().getCurrentSession();
		Criteria authorCriteria = session.createCriteria(Author.class);
		authorCriteria.add(Restrictions.ne("authorName", "other"));
		List<Author> AuthorList = authorCriteria.list();
		for(Author author: AuthorList){
			AuthorDTO authorDTO =  new AuthorDTO();			
			authorDTO.setAuthorName(author.getAuthorName());
			authorDTO.setId(author.getId());
			AuthorDTOList.add(authorDTO);
		}
		return AuthorDTOList;
	}
}
