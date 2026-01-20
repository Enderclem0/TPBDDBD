package fr.uge;

import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.databind.ObjectMapper;

public class ProducedObjectDeserializer implements Deserializer<ProducedObject> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public ProducedObject deserialize(String topic, byte[] data) {
        return MAPPER.readValue(data, ProducedObject.class);
    }
}
