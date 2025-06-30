package hexlet.code.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    @Bean
    Jackson2ObjectMapperBuilder objectMapperBuilder() {
        var builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializers(new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE)) // ADVAI
                //.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss",
                //    Locale.of("ru-ru"))))
                .modulesToInstall(new JsonNullableModule());
        return builder;
    }
}
