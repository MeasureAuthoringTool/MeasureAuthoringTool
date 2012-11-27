package org.ifmc.mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.ifmc.mat.DTO.AuthorDTO;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.Author;

public class AuthorDAO extends GenericDAO<Author, String> implements org.ifmc.mat.dao.AuthorDAO {
	
	private static final Log logger = LogFactory.getLog(AuthorDAO.class);
	
	public List<AuthorDTO> getAllAuthors(){
		
		List<AuthorDTO> AuthorDTOList = new ArrayList<AuthorDTO>();
		logger.info("Getting all the rows from the Steward table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<Author> AuthorList = session.createCriteria(Author.class).list();
		for(Author author: AuthorList){
			AuthorDTO authorDTO =  new AuthorDTO();			
			authorDTO.setAuthorName(author.getAuthorName());
			authorDTO.setId(author.getId());
			AuthorDTOList.add(authorDTO);
		}
		return AuthorDTOList;
	}
}
