package mat.server.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mat.model.clause.Measure;

public class MatContextServiceUtilTest {

    private MatContextServiceUtil util = new MatContextServiceUtil();
    private Measure measure = new Measure();

    @Test
    public void testDefaultMeasureNotConvertible() {
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testMeasureConvertible() {
        createConvertible();
        Assertions.assertTrue(util.isMeasureConvertible(measure));
    }

    private void createConvertible() {
        measure.setMeasureModel("QDM");
        measure.setReleaseVersion("v5.8");
        measure.setQdmVersion("5.5");
    }

    @Test
    public void testFhir() {
        createConvertible();
        measure.setMeasureModel("FHIR");
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testPreCQL() {
        createConvertible();
        measure.setMeasureModel("Pre-CQL");
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testVersionFail() {
        createConvertible();
        measure.setReleaseVersion("v5.7");
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

    @Test
    public void testQdmVersionFail() {
        createConvertible();
        measure.setQdmVersion("5.4");
        Assertions.assertFalse(util.isMeasureConvertible(measure));
    }

}
