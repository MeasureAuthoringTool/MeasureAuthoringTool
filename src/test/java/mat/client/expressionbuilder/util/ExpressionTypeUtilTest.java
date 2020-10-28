package mat.client.expressionbuilder.util;

import mat.client.expressionbuilder.constant.CQLType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author cthondapu
 *
 */
public class ExpressionTypeUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link mat.client.expressionbuilder.util.ExpressionTypeUtil#convertReturnTypeToCQLType(java.lang.String)}.
	 */
	@Test
	public void testConvertReturnTypeToCQLTypeBoolean() {
		assertEquals(CQLType.BOOLEAN, ExpressionTypeUtil.convertReturnTypeToCQLType("System.Boolean"));
	}

	@Test
	public void testConvertReturnTypeToCQLTypeList() {
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.Id>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.ResultComponent>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.FacilityLocation>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<System.Code>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.Component>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.Diagnosis>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.PositiveEncounterPerformed>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.PatientCharacteristicPayer>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.PatientCharacteristicRace>"));
		assertEquals(CQLType.LIST, ExpressionTypeUtil.convertReturnTypeToCQLType("list<QDM.PatientCharacteristicSex>"));
	}

	@Test
	public void testConvertReturnTypeToCQLTypeInterval() {
		assertEquals(CQLType.INTERVAL, ExpressionTypeUtil.convertReturnTypeToCQLType("interval<System.DateTime>"));
	}

}
