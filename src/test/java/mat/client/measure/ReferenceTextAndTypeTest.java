package mat.client.measure;

import org.junit.jupiter.api.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

public class ReferenceTextAndTypeTest {
    @Test
    public void testSettersAndGetters() {
        PojoClass pojoclass = PojoClassFactory.getPojoClass(ReferenceTextAndType.class);
        Validator validator = ValidatorBuilder
                .create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();
        validator.validate(pojoclass);
    }
}
