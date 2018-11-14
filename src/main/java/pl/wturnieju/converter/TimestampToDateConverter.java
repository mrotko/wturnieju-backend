package pl.wturnieju.converter;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;

import pl.wturnieju.model.Timestamp;

@WritingConverter
public class TimestampToDateConverter implements Converter<Timestamp, Date> {
    @Override
    public Date convert(@Nullable Timestamp source) {
        return Optional.ofNullable(source)
                .map(Timestamp::getValue)
                .map(localDateTime -> localDateTime.toInstant(ZoneOffset.UTC))
                .map(Date::from)
                .orElse(null);
    }
}
