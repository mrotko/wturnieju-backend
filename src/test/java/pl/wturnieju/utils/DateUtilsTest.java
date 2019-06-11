package pl.wturnieju.utils;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pl.wturnieju.model.Timestamp;

class DateUtilsTest {

    @Test
    public void shouldReturnLastSecOfDay() {
        var expected = new Timestamp(LocalDateTime.of(2019, 2, 10, 23, 59, 59));
        var dateWithDayAcc = new Timestamp(LocalDateTime.of(2019, 2, 10, 0, 0));

        Assertions.assertEquals(expected, DateUtils.toEndOfDay(dateWithDayAcc));
    }

    @Test
    public void shouldReturnGivenDate() {
        var expected = Timestamp.now();

        Assertions.assertEquals(expected, DateUtils.getMax(expected));
    }

    @Test
    public void shouldReturnLatestDate() {
        var now = LocalDateTime.now();
        var first = new Timestamp(now);
        var second = new Timestamp(now.plusMinutes(1));

        Assertions.assertNotEquals(first, second);
        Assertions.assertEquals(second, DateUtils.getMax(first, second));
        Assertions.assertEquals(second, DateUtils.getMax(second, first));
    }
}