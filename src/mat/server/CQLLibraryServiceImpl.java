package mat.server;

import java.util.List;

import mat.client.measure.service.CQLLibraryService;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryModel;
import mat.server.service.CQLLibraryServiceInterface;

public class CQLLibraryServiceImpl extends SpringRemoteServiceServlet implements CQLLibraryService{
	private static final long serialVersionUID = -2412573290030426288L;

	@Override
	public List<CQLLibraryDataSetObject> search(String searchText,String searchFrom) {
		return this.getCQLLibraryService().search(searchText,searchFrom);
	}
	
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public CQLLibraryServiceInterface getCQLLibraryService(){
		return (CQLLibraryServiceInterface) context.getBean("cqlLibraryService");
	}
	

}
