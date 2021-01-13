package mat.dao.impl;

import mat.dao.FhirConversionHistoryDao;
import mat.dao.search.GenericDAO;
import mat.model.clause.FhirConversionHistory;
import org.springframework.stereotype.Repository;

@Repository
public class FhirConversionHistoryDaoImpl extends GenericDAO<FhirConversionHistory, String> implements FhirConversionHistoryDao {
}
