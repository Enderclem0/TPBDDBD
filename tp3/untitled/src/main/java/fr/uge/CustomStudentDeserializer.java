package fr.uge;

import fr.uge.avro.Student;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class CustomStudentDeserializer implements Deserializer<Student> {

    @Override
    public Student deserialize(String topic, byte[] data) {
        try {
            return Student.fromByteBuffer(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
