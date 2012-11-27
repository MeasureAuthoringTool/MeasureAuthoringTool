package mat.dao;

import java.util.List;

import mat.model.Author;
import mat.model.MeasureType;
import mat.model.clause.Metadata;

public interface MetadataDAO extends IDAO<Metadata, String> {
	public void batchSave(List<Metadata> metadataList);
	public void deleteAllMetaData(List<Metadata> metadataList);
	public List<Metadata> getMeasureDetails(String id);
	public void deleteAuthor(List<Author> authorList,String id);
	public void deleteMeasureTypes(List<MeasureType> measureTypeList,String id);
	public List<Metadata> getMeasureDetails(String id, String name);
}
