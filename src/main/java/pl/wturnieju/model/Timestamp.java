package pl.wturnieju.model;

import java.beans.Transient;
import java.time.LocalDateTime;

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
        value = LocalDateTime.parse(text);
    }

    @Transient
    public static Timestamp now() {
        return new Timestamp(LocalDateTime.now());
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }
}
