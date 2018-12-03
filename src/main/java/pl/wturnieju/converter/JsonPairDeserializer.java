package pl.wturnieju.converter;

import java.io.IOException;

import org.apache.commons.lang3.tuple.MutablePair;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonPairDeserializer extends JsonDeserializer<MutablePair<Object, Object>> {
    @Override
    public MutablePair<Object, Object> deserialize(JsonParser p,
            DeserializationContext ctxt) throws IOException, JsonProcessingException {

        var oc = p.getCodec();
        JsonNode node = oc.readTree(p);

        MutablePair<Object, Object> pair = new MutablePair<>();

        if (node.get("left").isDouble()) {
            pair.setLeft(node.get("left").asDouble());
        } else if (node.get("left").isInt()) {
            pair.setLeft(node.get("left").asInt());
        } else if (node.get("left").isTextual()) {
            pair.setLeft(node.get("left").asText());
        }

        if (node.get("right").isDouble()) {
            pair.setRight(node.get("right").asDouble());
        } else if (node.get("right").isInt()) {
            pair.setRight(node.get("right").asInt());
        } else if (node.get("right").isTextual()) {
            pair.setRight(node.get("right").asText());
        }

        return pair;
    }
}
