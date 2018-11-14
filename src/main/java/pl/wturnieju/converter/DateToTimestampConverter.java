package pl.wturnieju.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;

import pl.wturnieju.model.Timestamp;

@ReadingConverter
public class DateToTimestampConverter implements Converter<Date, Timestamp> {
    @Override
    public Timestamp convert(@Nullable Date source) {
        return Optional.ofNullable(source)
                .map(Date::toInstant)
                .map(instant -> LocalDateTime.ofInstant(instant, ZoneId.of("UTC")))
                .map(Timestamp::new).orElse(null);
    }
}
