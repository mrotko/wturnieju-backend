package pl.wturnieju.utils;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pl.wturnieju.exception.DateFormatException;
import pl.wturnieju.model.Timestamp;

class DateUtilsTest {

    @Test
    public void parseYYYYMMDDShouldReturnExpectedDate() {
        var dateYYYYMMDD = "20180218";
        var expectedDate = new Timestamp(LocalDateTime.of(2018, 2, 18, 0, 0));

        Assertions.assertEquals(expectedDate, DateUtils.parseYYYYMMDD(dateYYYYMMDD));
    }

    @Test
    public void parseYYYYMMDDShouldThrowExceptionWhenLengthIsLowerThan8() {
        var date = "2018028";
        Assertions.assertThrows(DateFormatException.class, () -> DateUtils.parseYYYYMMDD(date));
    }

    @Test
    public void parseYYYYMMDDShouldThrowExceptionWhenLengthIsHigherThan8() {
        var date = "201802181";
        Assertions.assertThrows(DateFormatException.class, () -> DateUtils.parseYYYYMMDD(date));
    }

    @Test
    public void shouldReturnLastSecOfDay() {
        var expected = new Timestamp(LocalDateTime.of(2019, 2, 10, 23, 59, 59));
        var dateWithDayAcc = new Timestamp(LocalDateTime.of(2019, 2, 10, 0, 0));

        Assertions.assertEquals(expected, DateUtils.getDateWithLastSecOfDay(dateWithDayAcc));
    }
}