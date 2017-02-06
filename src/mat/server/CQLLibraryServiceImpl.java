package mat.server;

import java.util.List;

import mat.client.measure.service.CQLLibraryService;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.server.service.CQLLibraryServiceInterface;

public class CQLLibraryServiceImpl extends SpringRemoteServiceServlet implements CQLLibraryService{
	private static final long serialVersionUID = -2412573290030426288L;

	@Override
	public List<CQLLibraryDataSetObject> search(String searchText,String searchFrom) {
		return this.getCQLLibraryService().search(searchText,searchFrom);
	}
	
	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID){
		return this.getCQLLibraryService().findCQLLibraryByID(cqlLibraryID);
	}
	
	
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public CQLLibraryServiceInterface getCQLLibraryService(){
		return (CQLLibraryServiceInterface) context.getBean("cqlLibraryService");
	}
	
	
	public CQLModel save(CQLLibraryDataSetObject cqlLibraryDataSetObject) {
		return this.getCQLLibraryService().save(cqlLibraryDataSetObject);
	}

}
