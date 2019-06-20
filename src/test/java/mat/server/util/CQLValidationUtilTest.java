package mat.server.util;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import mat.client.shared.MatContext;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.CQLKeywordsUtil;

public class CQLValidationUtilTest {

	@Mock
	private CQLModel testCQLModel;
	@Mock
	private MatContext testMatContext;
	@Mock
	private CQLKeywordsUtil testCQLKeywords;


	
	@Before
	public void setUp() {
		testCQLModel = mock(CQLModel.class);
		testMatContext = mock(MatContext.class);
		testCQLKeywords = mock(CQLKeywordsUtil.class);
	}
	

	@Test
	public void testCQLModel() {
		
		boolean duplicatesFound =  CQLValidationUtil.doesModelHaveDuplicateIdentifierOrIdentifierAsKeyword(buildTestCQLModel());

		assertTrue(duplicatesFound);

	}
	
	
	@SuppressWarnings({ "null", "static-access" })
	private CQLModel buildTestCQLModel() {

		
		List<CQLQualityDataSetDTO> valueSetList = new ArrayList<>();
		CQLQualityDataSetDTO dto = new CQLQualityDataSetDTO();
		CQLQualityDataSetDTO dto2 = new CQLQualityDataSetDTO();
		dto.setName("val");
		dto2.setName("value");
		valueSetList.add(dto);
		valueSetList.add(dto2);
		when(testCQLModel.getValueSetList()).thenReturn(valueSetList);

		List<CQLCode> codeList = new ArrayList<>();
		CQLCode codename = new CQLCode();
		CQLCode codename2 = new CQLCode();
		codename.setName("code");
		codename2.setName("coding");
		codeList.add(codename);
		codeList.add(codename2);
		when(testCQLModel.getCodeList()).thenReturn(codeList);

		List<CQLDefinition> definitionList = new ArrayList<>();
		CQLDefinition definitionName = new CQLDefinition();
		CQLDefinition definitionName2 = new CQLDefinition();
		definitionName.setName("def");
		definitionName2.setName("def");
		definitionList.add(definitionName);
		definitionList.add(definitionName2);
		when(testCQLModel.getDefinitionList()).thenReturn(definitionList);

		List<CQLParameter> parameterList = new ArrayList<>();
		CQLParameter parameterName = new CQLParameter();
		CQLParameter parameterName2 = new CQLParameter();
		parameterName.setName("par");
		parameterName2.setName("parameter");
		parameterList.add(parameterName);
		parameterList.add(parameterName2);
		when(testCQLModel.getCqlParameters()).thenReturn(parameterList);
		
		List<CQLFunctions> functionList = new ArrayList<>();
		CQLFunctions functionName = new CQLFunctions();
		CQLFunctions functionName2 = new CQLFunctions();
		functionName.setName("func");
		functionName2.setName("function");
		functionList.add(functionName);
		functionList.add(functionName2);
		when(testCQLModel.getCqlFunctions()).thenReturn(functionList);
		
		List<CQLIncludeLibrary> includeLibraryList = new ArrayList<>();
		CQLIncludeLibrary includeLibraryName = new CQLIncludeLibrary();
		CQLIncludeLibrary includeLibraryName2 = new CQLIncludeLibrary();
		includeLibraryName.setAliasName("include");
		includeLibraryName2.setAliasName("library");
		includeLibraryList.add(includeLibraryName);
		includeLibraryList.add(includeLibraryName2);
		when(testCQLModel.getCqlIncludeLibrarys()).thenReturn(includeLibraryList);

		List<String> cqlKeywordList = new ArrayList<>();
		cqlKeywordList.add("key");
		cqlKeywordList.add("keyword");
		testCQLKeywords.getCQLKeywords().setCqlKeywordsList(cqlKeywordList);
		
		return testCQLModel;
	}
}
