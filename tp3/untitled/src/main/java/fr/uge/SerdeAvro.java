package fr.uge;

import fr.uge.avro.Student;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class SerdeAvro implements Serde<Student> {
    private final static Serializer<Student> studentSerializer = new CustomStudentSerializer();
    private final static Deserializer<Student> studentDeserializer = new CustomStudentDeserializer();

    @Override
    public Serializer<Student> serializer() {
        return studentSerializer;
    }

    @Override
    public Deserializer<Student> deserializer() {
        return studentDeserializer;
    }
}
