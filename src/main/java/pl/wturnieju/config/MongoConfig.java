package pl.wturnieju.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import lombok.extern.log4j.Log4j2;
import pl.wturnieju.converter.DateToTimestampConverter;
import pl.wturnieju.converter.TimestampToDateConverter;


@Configuration
@Log4j2
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        log.info("Register custom converters");
        return new MongoCustomConversions(Arrays.asList(
                new TimestampToDateConverter(),
                new DateToTimestampConverter()
        ));
    }
}
