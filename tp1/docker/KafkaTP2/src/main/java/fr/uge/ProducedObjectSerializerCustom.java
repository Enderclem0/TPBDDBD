package fr.uge;

import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ProducedObjectSerializerCustom implements Serializer<ProducedObject> {
    private final Charset encoding = StandardCharsets.UTF_8;

    @Override
    public byte[] serialize(String topic, ProducedObject data) {
        if (data == null) {
            return null;
        }
        var firstName = data.firstName().getBytes(encoding);
        var lastName = data.lastName().getBytes(encoding);
        var engineeringDegree = data.engineeringDegree().getBytes(encoding);
        var totalSize = (Integer.BYTES*4)+ //3 Sizes and age
                firstName.length + lastName.length +
                engineeringDegree.length;
        var resultBuffer = ByteBuffer.allocate(totalSize);
        resultBuffer.putInt(firstName.length)
                .putInt(lastName.length)
                .putInt(engineeringDegree.length)
                .putInt(data.age())
                .put(firstName)
                .put(lastName)
                .put(engineeringDegree);
        return resultBuffer.array();
    }
}
