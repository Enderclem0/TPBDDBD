package fr.uge;

import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

public class ProducedObjectSerializer implements Serializer<ProducedObject> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, ProducedObject data) {
        Objects.requireNonNull(data);
        Objects.requireNonNull(topic);
        return MAPPER.writeValueAsBytes(data);
    }
}
