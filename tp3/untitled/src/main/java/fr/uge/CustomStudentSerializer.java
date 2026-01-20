package fr.uge;

import fr.uge.avro.Student;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;

public class CustomStudentSerializer implements Serializer<Student> {
    @Override
    public byte[] serialize(String topic, Student data) {
        try {
            return data.toByteBuffer().array();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
