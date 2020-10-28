package mat.server.util;

import mat.model.cql.CQLCode;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.CQLKeywordsUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CQLValidationUtilTest {

    @Mock
    private CQLModel testCQLModel;
    @Mock
    private CQLKeywordsUtil testCQLKeywords;

    @Test
    public void testCQLModelDuplicate() {

        boolean duplicatesFound = CQLValidationUtil.doesModelHaveDuplicateIdentifierOrIdentifierAsKeyword(buildTestCQLModel());

        assertTrue(duplicatesFound);

    }


    @SuppressWarnings({"null", "static-access"})
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
