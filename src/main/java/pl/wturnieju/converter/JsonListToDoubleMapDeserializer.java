package pl.wturnieju.converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonListToDoubleMapDeserializer extends JsonDeserializer<Map<Integer, Double>> {
    @Override
    public Map<Integer, Double> deserialize(JsonParser p,
            DeserializationContext ctxt) throws IOException {

        var oc = p.getCodec();
        JsonNode node = oc.readTree(p);

        Map<Integer, Double> map = new HashMap<>();

        if (node.isArray()) {
            Integer counter = 0;
            for (JsonNode jsonNode : node) {
                map.put(counter, jsonNode.asDouble());
                counter++;
            }
        }

        return map;
    }
}
