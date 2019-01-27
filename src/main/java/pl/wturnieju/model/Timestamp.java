package pl.wturnieju.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.Transient;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;

@Data
public class Timestamp {

    private final LocalDateTime value;

    public Timestamp(@NonNull LocalDateTime value) {
        this.value = value.withNano(0);
    }

    @JsonCreator
    public Timestamp(String text) {
        value = ZonedDateTime.parse(text).toLocalDateTime();
    }

    @Transient
    public static Timestamp now() {
        return new Timestamp(LocalDateTime.now());
    }

    @Transient
    public boolean isBetweenIncluded(Timestamp lower, Timestamp upper) {
        if (isBetween(lower, upper)) {
            return true;
        }
        return value.isEqual(lower.value) || value.isEqual(upper.value);
    }

    @Transient
    public boolean isBetween(Timestamp lower, Timestamp upper) {
        return value.isAfter(lower.value) && value.isBefore(upper.value);
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toInstant(ZoneOffset.UTC).toString();
    }
}
