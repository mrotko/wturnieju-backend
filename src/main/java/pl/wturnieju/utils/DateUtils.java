package pl.wturnieju.utils;

import java.time.LocalDateTime;

import pl.wturnieju.exception.DateFormatException;
import pl.wturnieju.model.Timestamp;

public class DateUtils {

    public static Timestamp parseYYYYMMDD(String date) {
        if (date == null || date.length() != 8) {
            throw new DateFormatException("Expected format YYYYMMDD");
        }
        var year = Integer.parseInt(date.substring(0, 4));
        var month = Integer.parseInt(date.substring(4, 6));
        var day = Integer.parseInt(date.substring(6, 8));
        return new Timestamp(LocalDateTime.of(year, month, day, 0, 0));
    }

    public static Timestamp getDateWithLastSecOfDay(Timestamp date) {
        return new Timestamp(date.getValue()
                .withHour(23)
                .withMinute(59)
                .withSecond(59));
    }
}
