package pl.wturnieju;

import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonPairSerializer<L, R> extends JsonSerializer<Pair<L, R>> {
    @Override
    public void serialize(Pair<L, R> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("left");
        gen.writeObject(value.getLeft());
        gen.writeFieldName("right");
        gen.writeObject(value.getRight());
        gen.writeEndObject();
    }
}
