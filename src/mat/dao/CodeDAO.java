package mat.dao;

import java.util.List;

import mat.model.Code;

public interface CodeDAO extends IDAO<Code, String> {
	public void deleteCodes(List<Code> codes);
	public List<Code> searchCodes(String codeListId,int startIndex, int pageSize);
}
