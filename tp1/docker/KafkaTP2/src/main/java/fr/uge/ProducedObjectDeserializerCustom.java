package fr.uge;

import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ProducedObjectDeserializerCustom implements Deserializer<ProducedObject> {

    private final Charset encoding = StandardCharsets.UTF_8;

    @Override
    public ProducedObject deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        var buffer = ByteBuffer.wrap(data);
        var firstNameSize = buffer.getInt();
        var lastNameSize = buffer.getInt();
        var engineeringDegreeSize = buffer.getInt();
        var age = buffer.getInt();
        var firstName = encoding.decode(buffer.slice().limit(firstNameSize)).toString();
        buffer.position(buffer.position() + firstNameSize);
        var lastName = encoding.decode(buffer.slice().limit(lastNameSize)).toString();
        buffer.position(buffer.position() + lastNameSize);
        var engineeringDegree = encoding.decode(buffer.slice().limit(engineeringDegreeSize)).toString();
        //add position if adding fields
        return new ProducedObject(firstName, lastName, age, engineeringDegree);
    }
}
