package mat.shared;

import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DateUtilityTest {
    private Date date;

    @Before
    public void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Instant instant = Instant.parse("2019-12-23T19:02:02.329Z");
        date = Date.from(instant);
    }

    @Test
    public void testConvertDateToString() {
        assertEquals(DateUtility.convertDateToString(date), "12/23/2019 07:02 PM");
        //null test
        assertEquals(DateUtility.convertDateToString(null), "");
    }

    @Test
    public void testConvertDateToStringNoTime() {
        assertEquals(DateUtility.convertDateToStringNoTime(date), "12/23/2019");
        //null test
        assertEquals(DateUtility.convertDateToStringNoTime(null), "");
    }

    @Test
    public void testConvertDateToStringNoTime2() {
        Instant instant = Instant.parse("2019-12-23T19:02:02.329Z");
        Date date = Date.from(instant);
        assertEquals(DateUtility.convertDateToStringNoTime2(date), "12232019");
        //null test
        assertEquals(DateUtility.convertDateToStringNoTime2(null), "");
    }

    @Test
    public void testConvertStringToDate() {
        assertNotNull(DateUtility.convertStringToDate("12/23/2019"));
    }

    @Test
    public void testConvertStringToDateWithTime() {
        assertNotNull(DateUtility.convertStringToDate("12/23/2019T19:02:02.329Z"));
    }

    @Test
    public void testFormatInstant() {
        Instant instant = Instant.parse("2019-12-23T19:02:02.329Z");
        assertEquals(DateUtility.formatInstant(instant, "dd-MMM-YYYY"), "23-Dec-2019");
        assertEquals(DateUtility.formatInstant(instant, "hh:mm aa"), "07:02 PM");
        assertEquals(DateUtility.formatInstant(null, "hh:mm aa"), "");
    }
}
