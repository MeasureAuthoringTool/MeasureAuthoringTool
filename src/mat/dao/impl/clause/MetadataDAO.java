package mat.dao.impl.clause;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.Author;
import mat.model.MeasureType;
import mat.model.clause.Metadata;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class MetadataDAO extends GenericDAO<Metadata, String> implements mat.dao.MetadataDAO {

	@Override
	public void batchSave(List<Metadata> metadataList) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		   
		for(int i=0;i<metadataList.size();i++){
			//TODO validation here?
			//if(metadataList.get(i).getValue().length()>150000)
			//	metadataList.get(i).setValue(metadataList.get(i).getValue().substring(0, 15000-1));
			
			save(metadataList.get(i));
			if ( i % 20 == 0 ) { //20, same as the JDBC batch size
		        //flush a batch of inserts and release memory:
		        session.flush();
		        session.clear();
			}
		}
		tx.commit();
		session.close();
	}

	@Override
	public List<Metadata> getMeasureDetails(String id) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Metadata.class);
		List<Metadata> results = criteria.add(Restrictions.eq("measure.id",id)).list();
		return results;
	}

	@Override
	public List<Metadata> getMeasureDetails(String id, String name) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Metadata.class);
		criteria.add(Restrictions.eq("measure.id",id));
		criteria.add(Restrictions.eq("name",name));
		List<Metadata> results = criteria.list();
		return results;
	}

	@Override
	public void deleteAllMetaData(List<Metadata> metadataList){
		Session session = getSessionFactory().getCurrentSession();
	                String hql = "delete from org.ifmc.mat.model.clause.Metadata m where m in (:metadataList)";
	    	        Query query = session.createQuery(hql);
	    	        query.setParameterList("metadataList", metadataList);
	    	        int row = query.executeUpdate();
	    	        if (row == 0){
	    	          System.out.println("No Metadata Rows Deleted");
	    	        }
	    	        else{
	    	          System.out.println("Number of Metadata Rows Deleted: " + row);
	    	        }
		}
	
	@Override
	public void deleteAuthor(List<Author> authorList,String measureId) {
		Session session = getSessionFactory().getCurrentSession(); 
		for(Author author:authorList){
			String authorname = author.getAuthorName();
			String hql = "delete from org.ifmc.mat.model.clause.Metadata m where m.measure.id = :id and m.value = :value)";
			Query query = session.createQuery(hql);
			query.setParameter("id", measureId);
			query.setParameter("value", authorname);
			int row = query.executeUpdate();
			if(row == 0){
				System.out.println("No Metadata Author Rows Deleted");
			}else{
				System.out.println("Number of Metadata Author Rows Deleted:" + row);
			}
		}	
	}

	@Override
	public void deleteMeasureTypes(List<MeasureType> measureTypeList, String measureId) {
		Session session = getSessionFactory().getCurrentSession(); 
		for(MeasureType mt:measureTypeList){
			String measureTypeDescription = mt.getDescription();
			String hql = "delete from org.ifmc.mat.model.clause.Metadata m where m.measure.id = :id and m.value = :value)";
			Query query = session.createQuery(hql);
			query.setParameter("id", measureId);
			query.setParameter("value", measureTypeDescription);
			int row = query.executeUpdate();
			if(row == 0){
				System.out.println("No Metadata MeasureType Rows Deleted");
			}else{
				System.out.println("Number of Metadata MeasureType Rows Deleted:" + row);
			}
		}
		
	}
}

