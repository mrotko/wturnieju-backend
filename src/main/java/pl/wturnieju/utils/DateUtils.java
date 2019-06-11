package pl.wturnieju.utils;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.stream.Stream;

import org.springframework.lang.NonNull;

import pl.wturnieju.model.Timestamp;

public class DateUtils {

    public static Timestamp toEndOfDay(@NonNull Timestamp date) {
        return new Timestamp(date.getValue().toLocalDate().atTime(LocalTime.MAX));
    }

    public static Timestamp toMidnight(@NonNull Timestamp date) {
        return new Timestamp(date.getValue().toLocalDate().atTime(LocalTime.MIDNIGHT));
    }

    public static Timestamp getMax(Timestamp... timestamps) {
        return Stream.of(timestamps)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }
}
